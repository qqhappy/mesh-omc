/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsAttribute;
import com.xinwei.minas.mcbts.core.model.McBtsCondition;
import com.xinwei.minas.mcbts.core.model.McBtsFreqType;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsTemplate;
import com.xinwei.minas.mcbts.core.utils.FreqConvertUtil;
import com.xinwei.minas.server.core.conf.service.MoCache;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * McBts缓存
 * 
 * @author chenjunhua
 * 
 */

public class McBtsCache {

	private static final McBtsCache instance = new McBtsCache();

	private Map<Long, McBts> mapByBtsId = new ConcurrentHashMap<Long, McBts>();

	private Map<Long, McBts> mapByMoId = new ConcurrentHashMap<Long, McBts>();

	private McBtsCache() {

	}

	public static McBtsCache getInstance() {
		return instance;
	}

	/**
	 * 获取使用McBTS
	 * 
	 * @return
	 */
	public Set<Long> queryAllBtsId() {
		return mapByBtsId.keySet();
	}

	/**
	 * 根据BtsId查询McBts
	 * 
	 * @param btsId
	 * @return
	 */
	public McBts queryByBtsId(Long btsId) {
		return mapByBtsId.get(btsId);
	}

	/**
	 * 根据Mo Id查询McBts
	 * 
	 * @param btsId
	 * @return
	 */
	public McBts queryByMoId(Long moId) {
		return mapByMoId.get(moId);
	}

	/**
	 * 根据相同的频点查询McBts
	 * 
	 * @param freq
	 * @return mcBtsList 返回的mcbts
	 */
	public List<McBts> queryBySameFreq(int freq) {
		List<McBts> mcBtsList = new ArrayList<McBts>();
		Set<Long> keySet = mapByMoId.keySet();
		Iterator<Long> it = keySet.iterator();
		while (it.hasNext()) {
			McBts mcBts = mapByMoId.get(it.next());
			if (mcBts.getBtsFreqType() == freq) {
				mcBtsList.add(mcBts);
			}
		}
		return mcBtsList;
	}

	/**
	 * 增加或更新McBts
	 * 
	 * @param mcBts
	 */
	public void addOrUpdate(McBts mcBts) {
		McBts cacheBts = mapByMoId.get(mcBts.getMoId());
		if (cacheBts != null) {
			// 如果存在缓存，则需要保留缓存中基站的Ip、管理状态、注册状态
			mcBts.setBtsIp(cacheBts.getBtsIp());
			mcBts.setManageStateCode(cacheBts.getManageStateCode());
			mcBts.setRegisterState(cacheBts.getRegisterState());
			mcBts.setPublicIp(cacheBts.getPublicIp());
			mcBts.setPublicPort(cacheBts.getPublicPort());
			mcBts.setHardwareVersion(cacheBts.getHardwareVersion());
			mcBts.setSoftwareVersion(cacheBts.getSoftwareVersion());
			mcBts.setWorkingStatus(cacheBts.getWorkingStatus());
			mcBts.setBtsFreq(cacheBts.getBtsFreq());
			mcBts.setAttributes(cacheBts.getAttributes());
		}
		mapByBtsId.put(mcBts.getBtsId(), mcBts);
		mapByMoId.put(mcBts.getMoId(), mcBts);
		// 加入Mo缓存
		MoCache.getInstance().addOrUpdate(mcBts);
	}

	public void addTemplateToMoCache(McBtsTemplate temp) {
		Mo mo = new Mo(temp.getMoId(), MoTypeDD.MCBTS, "TEMPLATE", null,
				ManageState.OFFLINE_STATE);
		MoCache.getInstance().addOrUpdate(mo);
	}

	/**
	 * 删除McBTS
	 * 
	 * @param moId
	 */
	public void delete(Long moId) {
		McBts mcBts = mapByMoId.remove(moId);
		if (mcBts != null) {
			mapByBtsId.remove(mcBts.getBtsId());
		}
		MoCache.getInstance().delete(moId);
	}

	/**
	 * 查询所有基站
	 * 
	 * @return
	 */
	public List<McBts> queryAll() {
		List<McBts> mcBtsList = new ArrayList<McBts>();
		Set<Long> keySet = mapByMoId.keySet();
		Iterator<Long> it = keySet.iterator();
		while (it.hasNext()) {
			McBts mcBts = mapByMoId.get(it.next());
			mcBtsList.add(mcBts);
		}

		Collections.sort(mcBtsList, new Comparator<McBts>() {
			@Override
			public int compare(McBts o1, McBts o2) {
				return (int) (o1.getBtsId().longValue() - o2.getBtsId()
						.longValue());
			}
		});

		return mcBtsList;
	}

	// /**
	// * 查询所有可配置的基站
	// *
	// * @return
	// */
	// public List<McBts> queryAllConfigurable() {
	// List<McBts> mcBtsList = new ArrayList<McBts>();
	// Set<Long> keySet = mapByMoId.keySet();
	// Iterator<Long> it = keySet.iterator();
	// while (it.hasNext()) {
	// McBts mcBts = mapByMoId.get(it.next());
	// if (mcBts.isOnlineManage() && mcBts.isConnected()) {
	// // 只有在线管理且已连接的基站才是可配置的基站
	// mcBtsList.add(mcBts);
	// }
	// }
	//
	// Collections.sort(mcBtsList, new Comparator<McBts>() {
	// @Override
	// public int compare(McBts o1, McBts o2) {
	// return (int) (o1.getBtsId().longValue() - o2.getBtsId()
	// .longValue());
	// }
	// });
	//
	// return mcBtsList;
	// }

	/**
	 * 按基站类型查询基站
	 * 
	 * @param btsType
	 * @return
	 */
	public List<McBts> queryByBtsType(int btsType) {
		List<McBts> mcBtsList = new ArrayList<McBts>();

		for (Map.Entry<Long, McBts> entry : mapByMoId.entrySet()) {
			McBts mcbts = entry.getValue();
			if (mcbts.getBtsType() == btsType)
				mcBtsList.add(mcbts);
		}

		return mcBtsList;
	}

	/**
	 * 获取以moId为key的基站map
	 * 
	 * @return
	 */
	public Map<Long, McBts> getMapByMoId() {
		return mapByMoId;
	}

	/**
	 * 获取以btsId为key的基站map
	 * 
	 * @return
	 */
	public Map<Long, McBts> getMapByBtsId() {
		return mapByBtsId;
	}

	/**
	 * 根据条件查询基站
	 * 
	 */
	public PagingData<McBts> queryAllByCondition(McBtsCondition condition) {
		List<McBts> mcBtsList = queryClonedMcBtsBy(condition);
		int lastPNum = mcBtsList.size() % condition.getNumPerPage();
		int page = 0;
		page = mcBtsList.size() / condition.getNumPerPage();
		if (lastPNum > 0) {
			page = page + 1;
		}

		PagingData<McBts> pagingData = new PagingData<McBts>();
		pagingData.setTotalPages(page);
		pagingData.setCurrentPage(condition.getCurrentPage());
		pagingData.setNumPerPage(condition.getNumPerPage());
		List<McBts> btsList = currentList(condition.getCurrentPage(),
				condition.getNumPerPage(), mcBtsList);
		pagingData.setResults(btsList);
		return pagingData;
	}

	/**
	 * 查询所有符合条件的基站Clone模型
	 * 
	 * @param condition
	 *            查询条件
	 * @return 符合条件的基站Clone模型
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryClonedMcBtsBy(McBtsCondition condition) {
		List<McBts> mcBtsList = new ArrayList<McBts>();
		long sagId = condition.getSagId();
		String btsId = condition.getBtsId().toUpperCase();
		String btsName = condition.getBtsName().toUpperCase();
		if (btsName == null) {
			btsName = "";
		}

		List<McBts> toQueryList = queryAll();
		if (toQueryList == null || toQueryList.isEmpty())
			return mcBtsList;

		for (McBts mcBts : toQueryList) {
			String hexBtsId = mcBts.getHexBtsId().toUpperCase();
			if (!btsId.equals("-1") && !hexBtsId.contains(btsId)) {
				continue;
			}

			String name = mcBts.getName().toUpperCase();
			if (!name.equals("") && !name.contains(btsName)) {
				continue;
			}

			if (sagId >= 0 && mcBts.getSagDeviceId() != sagId) {
				continue;
			}
			// 使用克隆方法，确保返回给客户端的数据不会被频繁更新，避免出现OptionDataException
			mcBtsList.add(mcBts.clone());
		}

		if (mcBtsList.isEmpty())
			return mcBtsList;

		// 按基站ID排序
		Collections.sort(mcBtsList, new McBtsComparator(condition.getSortBy()));

		return mcBtsList;
	}

	/**
	 * 对基站进行排序
	 * 
	 * @author tiance
	 * 
	 */
	private static class McBtsComparator implements Comparator<McBts> {
		// 排序的常量,如果改变title的顺序,这里也相应要做修改,index从1开始
		public static final int SORT_BY_ID = 1;
		public static final int SORT_BY_NAME = 2;
		public static final int SORT_BY_MODE = 3;
		public static final int SORT_BY_STATUS = 4;
		public static final int SORT_BY_BTS_IP = 5;
		public static final int SORT_BY_FREQ = 6;
		public static final int SORT_BY_FREQ_TYPE = 7;
		public static final int SORT_BY_SEQ_ID = 8;
		public static final int SORT_BY_BTS_TYPE = 9;
		public static final int SORT_BY_BTS_DISTRICT= 10;
		public static final int SORT_BY_SW_VER = 11;
		public static final int SORT_BY_PUB_IP = 12;
		public static final int SORT_BY_PORT = 13;
		public static final int SORT_BY_ANTE_TYPE = 14;
		public static final int SORT_BY_ANTE_ANGLE = 15;
		public static final int SORT_BY_HW_VER = 16;
		public static final int SORT_BY_MCU_VER = 17;
		public static final int SORT_BY_FPGA_VER = 18;

		private int sortBy;

		public McBtsComparator(int sortBy) {
			this.sortBy = sortBy;
		}

		@Override
		public int compare(McBts m1, McBts m2) {
			int order = sortBy > 0 ? 1 : -1;

			switch (Math.abs(sortBy)) {
			case SORT_BY_ID: {
				// 按ID
				return order
						* m1.getHexBtsId().toLowerCase()
								.compareTo(m2.getHexBtsId().toLowerCase());
			}
			case SORT_BY_NAME: {
				// 按名称
				return order
						* m1.getName().toLowerCase()
								.compareTo(m2.getName().toLowerCase());
			}
			case SORT_BY_SW_VER: {
				// 按软件版本
				String v1 = m1.getSoftwareVersion();
				String v2 = m2.getSoftwareVersion();

//				int goon = isVersionBlank(v1, v2);
//				if (goon != 2)
//					return goon;

				return order * compareVersion(v1, v2);
			}
			case SORT_BY_MCU_VER: {
				// 按mcu版本
				String v1 = String.valueOf(m1
						.getAttribute(McBtsAttribute.Key.MCU_VERSION));
				String v2 = String.valueOf(m2
						.getAttribute(McBtsAttribute.Key.MCU_VERSION));

				int goon = isVersionBlank(v1, v2);
				if (goon != 2)
					return goon;

				return order * compareVersion(v1, v2);
			}
			case SORT_BY_FPGA_VER: {
				// 按fpga版本
				String v1 = String.valueOf(m1
						.getAttribute(McBtsAttribute.Key.FPGA_VERSION));
				String v2 = String.valueOf(m2
						.getAttribute(McBtsAttribute.Key.FPGA_VERSION));

				int goon = isVersionBlank(v1, v2);
				if (goon != 2)
					return goon;

				return order * compareVersion(v1, v2);
			}
			case SORT_BY_HW_VER: {
				// 按硬件版本
				String v1 = m1.getHardwareVersion();
				String v2 = m2.getHardwareVersion();

				int goon = isVersionBlank(v1, v2);
				if (goon != 2)
					return goon;

				return order * compareVersion(v1, v2);
			}
			case SORT_BY_FREQ_TYPE: {
				// 按频点类型
				return order * compareFreqType(m1, m2);
			}
			case SORT_BY_BTS_TYPE: {
				// 按基站类型
				return order * compareNum(m1.getBtsType(), m2.getBtsType());
			}
			case SORT_BY_BTS_DISTRICT :{
				//按基站区域
				return order * compareNumLong(m1.getDistrictId(), m2.getDistrictId());
			}
			case SORT_BY_PORT: {
				// 按端口号
				return order
						* compareNum(m1.getPublicPort(), m2.getPublicPort());
			}
			case SORT_BY_STATUS: {
				// 按注册状态
				int s1 = m1.getRegisterState();
				int s2 = m2.getRegisterState();

				if (s1 == 30 && s2 == 30)
					return 0;
				if (s1 == 30)
					return order * -1;
				if (s2 == 30)
					return order * 1;

				return order * -compareNum(s1, s2);
			}
			case SORT_BY_MODE: {
				// 按管理模式
				int ms1 = m1.getManageStateCode();
				int ms2 = m2.getManageStateCode();

				if (ms1 == 0 && ms2 == 0)
					return 0;
				if (ms1 == 0)
					return order * -1;
				if (ms2 == 0)
					return order * 1;

				return order * compareNum(ms1, ms2);
			}
			case SORT_BY_FREQ: {
				// 按频点
				return order * compareFreq(m1, m2);
			}
			case SORT_BY_BTS_IP: {
				// 按bts ip
				String ip1 = m1.getBtsIp();
				String ip2 = m2.getBtsIp();

				if (StringUtils.isBlank(ip1) && StringUtils.isBlank(ip2))
					return 0;
				if (StringUtils.isBlank(ip1))
					return 1;
				if (StringUtils.isBlank(ip2))
					return -1;

				return order * compareIP(ip1, ip2);
			}
			case SORT_BY_PUB_IP: {
				// 按public ip
//				String ip1 = m1.getPublicIp();
//				String ip2 = m2.getPublicIp();
//				if (StringUtils.isBlank(ip1) && StringUtils.isBlank(ip2))
//					return 0;
//				if (StringUtils.isBlank(ip1))
//					return 1;
//				if (StringUtils.isBlank(ip2))
//					return -1;

				return order * compareIP(m1.getPublicIp(), m2.getPublicIp());
			}
			case SORT_BY_SEQ_ID: {
				String s1 = String.valueOf(
						m1.getAttribute(McBtsAttribute.Key.SEQ_ID)).replace(
						"null", "");
				String s2 = String.valueOf(
						m2.getAttribute(McBtsAttribute.Key.SEQ_ID)).replace(
						"null", "");

				if (StringUtils.isBlank(s1) && StringUtils.isBlank(s2))
					return 0;
				if (StringUtils.isBlank(s1))
					return 1;
				if (StringUtils.isBlank(s2))
					return -1;

				Integer i1 = Integer.valueOf(s1);
				Integer i2 = Integer.valueOf(s2);

				return order * i1.compareTo(i2);

			}
			case SORT_BY_ANTE_TYPE: {
				int t1 = m1.getAntennaType();
				int t2 = m2.getAntennaType();
				return order * compareNum(t1, t2);
			}
			case SORT_BY_ANTE_ANGLE: {
				int t1 = m1.getAntennaAngle();
				int t2 = m2.getAntennaAngle();
				return order * compareNum(t1, t2);
			}
			default:
				return 0;
			}
		}

		/**
		 * 按数字排序
		 * 
		 * @param n1
		 * @param n2
		 * @return
		 */
		private int compareNum(int n1, int n2) {
			if (n1 > n2)
				return 1;
			else if (n1 < n2)
				return -1;
			else
				return 0;
		}
		
		/**
		 * 按数字排序
		 * 
		 * @param n1
		 * @param n2
		 * @return
		 */
		private int compareNumLong(long n1, long n2) {
			if (n1 > n2)
				return 1;
			else if (n1 < n2)
				return -1;
			else
				return 0;
		}
		
		/**
		 * 按频点类型排序
		 * 
		 * @param m1
		 * @param m2
		 * @return
		 */
		private int compareFreqType(McBts m1, McBts m2) {
			String ft1 = "";
			String ft2 = "";

			List<McBtsFreqType> mapList = FreqConvertUtil
					.getSystemSupportedFreqMap();
			for (McBtsFreqType freqType : mapList) {
				if (m1.getBtsFreqType() == freqType.getFreqType()) {
					ft1 = freqType.getFreqName();
				}
				if (m2.getBtsFreqType() == freqType.getFreqType()) {
					ft2 = freqType.getFreqName();
				}
			}

			int intFreqType1 = 0;
			int intFreqType2 = 0;

			if (ft1.contains("G")) {
				ft1 = ft1.replace("G", "");
				intFreqType1 = (int) ((Double.parseDouble(ft1)) * 1000);
			} else {
				ft1 = ft1.replace("M", "");
				intFreqType1 = Integer.parseInt(ft1);
			}

			if (ft2.contains("G")) {
				ft2 = ft2.replace("G", "");
				intFreqType2 = (int) ((Double.parseDouble(ft2)) * 1000);
			} else {
				ft2 = ft2.replace("M", "");
				intFreqType2 = Integer.parseInt(ft2);
			}

			if (intFreqType1 > intFreqType2)
				return 1;
			else if (intFreqType1 < intFreqType2)
				return -1;
			else
				return 0;
		}

		/**
		 * 按频点排序
		 * 
		 * @param m1
		 * @param m2
		 * @return
		 */
		private int compareFreq(McBts m1, McBts m2) {
			FreqConvertUtil freqUtil = new FreqConvertUtil();
			freqUtil.setFreqType(m1.getBtsFreqType());
			Integer f1 = (int) (freqUtil.getMidFreqValue(m1.getBtsFreq()) * 100);

			freqUtil.setFreqType(m2.getBtsFreqType());
			Integer f2 = (int) (freqUtil.getMidFreqValue(m2.getBtsFreq()) * 100);

			return f1.compareTo(f2);
		}

		private int isVersionBlank(String v1, String v2) {
			v1 = v1 == null || v1.equalsIgnoreCase("null") ? "" : v1;
			v2 = v2 == null || v2.equalsIgnoreCase("null") ? "" : v2;

			if (StringUtils.isBlank(v1) && StringUtils.isBlank(v2))
				return 0;
			if (StringUtils.isBlank(v1))
				return 1;
			if (StringUtils.isBlank(v2))
				return -1;

			return 2;
		}

		private int compareIP(String ip1, String ip2) {
			return this.compareVersion(ip1, ip2);
		}

		/**
		 * 按版本排序
		 * 
		 * @param ver1
		 * @param ver2
		 * @return
		 */
		private int compareVersion(String ver1, String ver2) {
			
			int compare = isVersionBlank(ver1, ver2);
			if (compare != 2) {
				return -1 * compare;
			}
			
			String[] v1 = ver1.split("\\.");
			String[] v2 = ver2.split("\\.");
			
			for (int i = 0; i < v1.length; i++) {
				int num1 = Integer.valueOf(v1[i]);
				int num2 = Integer.valueOf(v2[i]);

				if (num1 < num2) {
					return -1;
				}
				if (num1 > num2) {
					return 1;
				}
			}
			return 0;
		}
	}

	// 筛选显示列表
	private List<McBts> currentList(int currentPage, int everyPageNumber,
			List<McBts> mcBtsList) {
		List<McBts> btsList = new ArrayList();
		for (int number = 0; number < everyPageNumber; number++) {
			int index = ((currentPage - 1) * everyPageNumber) + number;
			if (index < mcBtsList.size()) {
				btsList.add(mcBtsList.get(index));
			} else {
				break;
			}
		}
		return btsList;
	}

}
