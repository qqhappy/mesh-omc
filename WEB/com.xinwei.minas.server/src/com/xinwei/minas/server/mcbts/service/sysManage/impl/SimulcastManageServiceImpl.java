/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-17	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.SystemProperty;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.mcbts.core.model.layer2.PECCHSetting;
import com.xinwei.minas.mcbts.core.model.layer2.SCGChannelConfigItem;
import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.mcbts.core.model.sysManage.LocationArea;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsLink;
import com.xinwei.minas.mcbts.core.model.sysManage.Simulcast;
import com.xinwei.minas.server.core.conf.service.SystemPropertyService;
import com.xinwei.minas.server.mcbts.dao.sysManage.SimulcastManageDAO;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer2.AirlinkService;
import com.xinwei.minas.server.mcbts.service.layer3.MBMSBtsService;
import com.xinwei.minas.server.mcbts.service.sysManage.LocAreaService;
import com.xinwei.minas.server.mcbts.service.sysManage.SimulcastManageService;
import com.xinwei.minas.server.mcbts.utils.MBMSUtils;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 同播资源管理服务类
 * 
 * 
 * @author tiance
 * 
 */

public class SimulcastManageServiceImpl implements SimulcastManageService {

	private static Logger logger = null;

	private SequenceService sequenceService;

	private SimulcastManageDAO simulcastManageDAO;

	private SystemPropertyService systemPropertyService;

	private static final String CATEGORY = "platform";
	// 数据链路信息,目前还不实现
	public static final String PROPERTY1 = "mcbts_link_sync";
	// 同播资源信息
	private static final String PROPERTY2 = "simulcast_resource_sync";

	private static final String UN_SYNC = "false";

	private static final String SYNC = "true";

	private Lock syncLock = new ReentrantLock();

	private String iM3000IP;

	private String iM3000PORT;

	public SimulcastManageServiceImpl() {
		super();
		createLoggerIfNotExist();
	}

	@Override
	public List<Simulcast> queryAll() {
		return simulcastManageDAO.queryAll();
	}

	@Override
	public List<Simulcast> queryByDistrictId(long districtId) {
		return simulcastManageDAO.queryByDistrictId(districtId);
	}

	/**
	 * 创建滚动文件日志
	 */
	private void createLoggerIfNotExist() {
		if (logger != null) {
			return;
		}
		String fileName = "log/SimulcastSynchronize.log";

		try {
			logger = Logger.getLogger(SimulcastManageServiceImpl.class);
			logger.setAdditivity(false);
			logger.setLevel(Level.DEBUG);

			logger.removeAllAppenders();

			PatternLayout patterLayout = new PatternLayout();
			patterLayout.setConversionPattern("%d %m%n");
			RollingFileAppender appender = null;
			appender = new RollingFileAppender(patterLayout, fileName);
			appender.setMaxBackupIndex(5);
			appender.setMaxFileSize("4096kb");
			logger.addAppender(appender);

		} catch (Exception e) {
			logger.error("createLoggerIfNotExist()", e); //$NON-NLS-1$
			logger = Logger.getLogger(SimulcastManageServiceImpl.class);
		}
	}

	public static Logger getLogger() {
		return logger;
	}

	@Override
	public void saveOrUpdate(Simulcast simulcast) {
		if (simulcast.getIdx() == 0L)
			simulcast.setIdx(sequenceService.getNext());

		simulcastManageDAO.saveOrUpdate(simulcast);

		// 同播资源信息修改为
		systemPropertyService = AppContext.getCtx().getBean(
				SystemPropertyService.class);
		systemPropertyService.setProperty(CATEGORY, null, PROPERTY2, UN_SYNC);
	}

	@Override
	public void setSimulMcBtsLinkUnSync() {
		systemPropertyService = AppContext.getCtx().getBean(
				SystemPropertyService.class);
		systemPropertyService.setProperty(CATEGORY, null, PROPERTY1, UN_SYNC);
	}

	@Override
	public void delete(Simulcast simulcast) {
		simulcastManageDAO.delete(simulcast);

		// 同播资源信息修改为
		systemPropertyService = AppContext.getCtx().getBean(
				SystemPropertyService.class);
		systemPropertyService.setProperty(CATEGORY, null, PROPERTY2, UN_SYNC);
	}

	@Override
	public boolean[] querySyncStatus() {
		boolean[] b = new boolean[2];

		systemPropertyService = AppContext.getCtx().getBean(
				SystemPropertyService.class);

		SystemProperty s1 = systemPropertyService.getProperty(CATEGORY, null,
				PROPERTY1);
		String value1 = s1.getValue();
		b[0] = stringToBoolean(value1);

		SystemProperty s2 = systemPropertyService.getProperty(CATEGORY, null,
				PROPERTY2);
		String value2 = s2.getValue();
		b[1] = stringToBoolean(value2);

		return b;
	}

	private boolean stringToBoolean(String value) {
		if (value.equalsIgnoreCase(SYNC))
			return true;
		else
			return false;
	}

	@Override
	public void sync(boolean[] toSync) throws Exception {
		if (syncLock.tryLock()) {
			try {
				MBMSUtils.initIM3000Info(iM3000IP, iM3000PORT);

				// 获取所有基站用到的SAG ID
				Map<Long, List<McBts>> sagBtsMap = getSagMcBtsMap();

				Set<Long> sagSet = sagBtsMap.keySet();

				// TODO 删除如下代码:
				// sagSet = new HashSet<Long>();
				// sagSet.add(1112L);
				// TODO 删除结束
				systemPropertyService = AppContext.getCtx().getBean(
						SystemPropertyService.class);

				if (sagSet.isEmpty()) {
					// 如果没有sag,直接设置为已同步
					if (toSync[0])
						systemPropertyService.setProperty(CATEGORY, null,
								PROPERTY1, SYNC);
					if (toSync[1])
						systemPropertyService.setProperty(CATEGORY, null,
								PROPERTY2, SYNC);
					return;
				}

				StringBuilder noSagId = new StringBuilder();
				StringBuilder configError = new StringBuilder();

				List<Simulcast> simuList = getFixedSimuList();

				for (long sagId : sagSet) {
					try {
						// 同步基站链路信息
						if (toSync[0])
							syncMcBtsLink(sagId, sagBtsMap.get(sagId));

						// 同步同播资源信息
						if (toSync[1])
							syncSimulcast(sagId, simuList);
					} catch (NullPointerException e) {
						// 设备ID不存在
						noSagId.append(String.valueOf(sagId)).append("\n");
					} catch (Exception e) {
						logger.error(e);
						String errorMsg = e.getLocalizedMessage();
						if (errorMsg.equals("iM3000_CONFIG_ERROR")) {
							// iM3000的配置错误,检验platform-config.properties
							MBMSUtils.disconnect();
							throw new Exception("SyncSagFail");
						}
						if (errorMsg.contains("CONFIG_ERROR")) {
							configError.append(String.valueOf(sagId)).append(
									"\n");
						}
					}
				}
				// 释放资源
				MBMSUtils.disconnect();

				if (toSync[0])
					systemPropertyService.setProperty(CATEGORY, null,
							PROPERTY1, SYNC);
				if (toSync[1])
					systemPropertyService.setProperty(CATEGORY, null,
							PROPERTY2, SYNC);

				if (noSagId.length() > 0 || configError.length() > 0) {
					throw new Exception(noSagId.toString() + "##"
							+ configError.toString());
				}
			} finally {
				syncLock.unlock();
			}
		} else {
			throw new Exception("ACTION_BLOCKED");
		}
	}

	/**
	 * 同步基站链路信息
	 * 
	 * @param sagId
	 * @param mcbtsList
	 * @return
	 * @throws Exception
	 */
	private boolean syncMcBtsLink(long sagId, List<McBts> mcbtsList)
			throws Exception {
		// 从iM3000查询的基站链路列表
		List<McBtsLink> sagLinkList = MBMSUtils.queryBtsLinkList(sagId);
		// 从EMS中拼出的基站链路Map,KEY为基站Hex ID
		Map<String, McBtsLink> btsLinkMap = getBtsLinkMap(mcbtsList);

		// 修改
		for (McBtsLink sagLink : sagLinkList) {
			String btsId = sagLink.getBtsId();

			McBtsLink link = btsLinkMap.get(btsId);

			// 验证本地EMS是否存在这个基站链路,不存在就下一个
			if (link == null) {
				MBMSUtils.deleteMcBtsLink(sagLink.getLinkIndex(), sagId);
				continue;
			}

			// 将sag中的index移到ems的link中
			link.setLinkIndex(sagLink.getLinkIndex());

			// 如果sag上的基站链路信息和本地ems上的基站链路信息相同,就不用更新,下一个
			if (sagLink.equals(link)) {
				btsLinkMap.remove(btsId);
				continue;
			}

			MBMSUtils.modifyMcBtsLink(sagId, sagLink, link);

			// 删除已经在sag中存在的基站链路
			btsLinkMap.remove(btsId);
		}

		boolean[] index = queryIndex(sagId);

		// 新增
		for (Entry<String, McBtsLink> entry : btsLinkMap.entrySet()) {
			McBtsLink link = entry.getValue();

			// 找到不重复的index,然后插入记录
			for (int i = 1; i < index.length; i++) {
				if (index[i])
					continue;

				MBMSUtils.addMcBtsLink(i, sagId, link);
				index[i] = true;
				break;
			}
		}

		return true;
	}

	/**
	 * 同步同播资源信息
	 */
	private boolean syncSimulcast(long sagId, List<Simulcast> simuList)
			throws Exception {
		// 查询SAG上的同播资源
		List<Simulcast> sagSimuList = MBMSUtils.querySimulcastList(sagId);

		// 删除SAG多余的
		for (Simulcast sagSimulcast : sagSimuList) {
			boolean exist = false;
			for (Simulcast simulcast : simuList) {
				if (simulcast.sameKeyWith(sagSimulcast)) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				MBMSUtils.deleteSimulcast(sagId, sagSimulcast);
			}
		}

		// 修改不同的
		for (Simulcast sagSimulcast : sagSimuList) {
			for (Iterator<Simulcast> iter = simuList.iterator(); iter.hasNext();) {
				Simulcast simulcast = iter.next();
				if (simulcast.sameKeyWith(sagSimulcast)) {
					if (!simulcast.sameValueWith(sagSimulcast)) {
						MBMSUtils.modifySimulcast(sagId, sagSimulcast,
								simulcast);
					}
					// 删除它们都有的同播资源
					iter.remove();
					break;
				}
			}
		}

		// 增加SAG没有的
		for (Simulcast simulcast : simuList) {
			MBMSUtils.addSimulcast(sagId, simulcast);
		}

		return true;
	}

	/**
	 * 获取索引是否被使用,被使用了为true
	 * 
	 * @return
	 */
	private boolean[] queryIndex(long sagId) throws Exception {
		boolean[] indexes = new boolean[129];

		// 从iM3000查询的基站链路列表
		List<McBtsLink> sagLinkList = MBMSUtils.queryBtsLinkList(sagId);

		for (McBtsLink mcbtsLink : sagLinkList) {
			int i = Integer.parseInt(mcbtsLink.getLinkIndex());

			indexes[i] = true;
		}
		return indexes;
	}

	/**
	 * 根据btsId获取基站链路信息放到HashMap中，key为btsId，value为McBtsLink
	 * 
	 * @param btsList
	 * @return
	 * @throws Exception
	 */
	private static Map<String, McBtsLink> getBtsLinkMap(List<McBts> btsList) {
		Map<String, McBtsLink> linkMap = new HashMap<String, McBtsLink>();

		// 获取所有位置区
		List<LocationArea> areaList = getArea();

		for (McBts mcBts : btsList) {
			McBtsLink link = new McBtsLink();

			linkMap.put(mcBts.getHexBtsId(), link);

			long moId = mcBts.getMoId();
			// 插入基本信息
			link.setBtsId(mcBts.getHexBtsId());
			link.setBtsName(mcBts.getName());
			link.setSafeParamIndex(String.valueOf(mcBts.getNatAPKey()));
			link.setBtsDVoiceFlag(String.valueOf(mcBts.getVoiceDirectConnFlag()));

			// 位置区
			long areaId = mcBts.getLocationAreaId();
			for (LocationArea location : areaList) {
				if (areaId == location.getLongLocationId()) {
					link.setAreaID(location.getAreaId());
					break;
				}
			}

			// 同播基站
			TConfMBMSBts mbmsBts = getMBMSBtsConfig(moId);
			if (mbmsBts != null && mbmsBts.getFlag() != null)
				link.setMBMSFlag(String.valueOf(mbmsBts.getFlag()));

			// 地域
			link.setDistrictId(String.valueOf(mcBts.getDistrictId()));

			// 频率
			int freqType = mcBts.getBtsFreqType();
			link.setFreqType(String.valueOf(freqType + 2));

			// 频率偏移
			link.setFreqOffset(String.valueOf(mcBts.getBtsFreq()));

			// 基站信令点
			link.setBtsSignalPointID(String.valueOf(mcBts
					.getBtsSignalPointCode()));

			// 基站信令和媒体信息
			link.setBtsSignalIP(mcBts.getBtsConfigIp());
			link.setBtsSignalPort(String.valueOf(mcBts.getBtsSignalPort()));
			link.setBtsMediaIP(mcBts.getBtsConfigIp());
			link.setBtsMediaPort(String.valueOf(mcBts.getBtsMediaPort()));

			// SAG信息
			link.setSagMediaIP(mcBts.getSagVoiceIp());
			link.setSagMediaPort(String.valueOf(mcBts.getSagMediaPort()));
			link.setSagSignalIP(mcBts.getSagSignalIp());
			link.setSagSignalPort(String.valueOf(mcBts.getSagSignalPort()));

			// 空中链路
			AirlinkConfig airlink = getAirlinkConfig(moId);
			if (airlink != null) {
				link.setSeqID(String.valueOf(airlink.getSequenceId()));
				link.setTsNum(String.valueOf(airlink.getTotalTS()));
				link.setDownlinkTsNum(String.valueOf(airlink.getDownlinkTS()));
				link.setSCGMask(String.valueOf(airlink.getScgMask()));

				String[] SCGTSMasks = getForbiddenStr(airlink.getTchForbidden());
				link.setSCG0TSMask(SCGTSMasks[0]);
				link.setSCG1TSMask(SCGTSMasks[1]);
				link.setSCG2TSMask(SCGTSMasks[2]);
				link.setSCG3TSMask(SCGTSMasks[3]);
				link.setSCG4TSMask(SCGTSMasks[4]);

				List<SCGChannelConfigItem> list = airlink
						.getScgChannelConfigList();
				List<SCGChannelConfigItem> bch = new ArrayList<SCGChannelConfigItem>();
				List<SCGChannelConfigItem> rrch = new ArrayList<SCGChannelConfigItem>();
				List<SCGChannelConfigItem> rarch = new ArrayList<SCGChannelConfigItem>();
				for (SCGChannelConfigItem item : list) {
					if (item.getChannelType() == SCGChannelConfigItem.BCH) {
						bch.add(item);
						continue;
					}
					if (item.getChannelType() == SCGChannelConfigItem.RRCH) {
						rrch.add(item);
						continue;
					}
					if (item.getChannelType() == SCGChannelConfigItem.RARCH) {
						rarch.add(item);
						continue;
					}
				}
				link.setBCH(getSCGChannelStr(bch, AirlinkConfig.BCH_COUNT));
				link.setRRCH(getSCGChannelStr(rrch, AirlinkConfig.RRCH_COUNT));
				link.setRARCH(getSCGChannelStr(rarch, AirlinkConfig.RARCH_COUNT));
			}

			PECCHSetting pecch = airlink.getPecchSetting();
			if (pecch != null) {
				int scgIndex = 255;
				if (pecch.getScgIndex() != null) {
					if (pecch.getScgIndex() >= 1 && pecch.getScgIndex() <= 3) {
						scgIndex = pecch.getScgIndex();
					}
				}
				link.setPescgIdx(String.valueOf(scgIndex));

				link.setPepchNum(String.valueOf(pecch.getPchCount() == null
						|| pecch.getPchCount() > 255 ? 255 : pecch
						.getPchCount()));

				link.setPepepchId(String.valueOf(pecch.getPchIndex() == null
						|| pecch.getPchIndex() > 255 ? 255 : pecch
						.getPchIndex()));

				link.setPerarchNum(String.valueOf(pecch.getRarchCount() == null
						|| pecch.getRarchCount() > 255 ? 255 : pecch
						.getRarchCount()));

				link.setPerrchNum(String.valueOf(pecch.getRrchCount() == null
						|| pecch.getRrchCount() > 255 ? 255 : pecch
						.getRrchCount()));
			}

		}

		return linkMap;
	}

	public static String[] getForbiddenStr(String tsForbiddenStr) {
		String forbidden[] = new String[AirlinkConfig.SCG_COUNT];

		char[] tsForbiddenChars = tsForbiddenStr.toCharArray();
		if (tsForbiddenChars.length >= 20) {
			for (int i = 0; i < AirlinkConfig.SCG_COUNT; i++) {
				String str = new String(tsForbiddenChars, i * 4, 4);
				int tsMask = Integer.parseInt(str, 16);
				forbidden[i] = String.valueOf(tsMask);
			}
		}
		return forbidden;
	}

	public static String getSCGChannelStr(List<SCGChannelConfigItem> list,
			int size) {
		String bch[] = new String[size];
		// 初始化
		for (int i = 0; i < size; i++) {
			bch[i] = "9999";
		}

		for (int i = 0; i < list.size(); i++) {
			SCGChannelConfigItem item = list.get(i);
			bch[i] = getValue(item.getScgIndex().intValue())
					+ getValue(item.getTsIndex().intValue());
		}
		return getObjValue(bch);
	}

	/**
	 * 把数组拼接为字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String getObjValue(Object[] obj) {
		String str = "";
		for (int i = 0; i < obj.length; i++) {
			str = str + obj[i];
		}
		return str;
	}

	/**
	 * 10以下的数字前面补0
	 * 
	 * @param value
	 * @return
	 */
	public static String getValue(int value) {
		if (value < 10) {
			return String.valueOf("0" + value);
		} else {
			return String.valueOf(value);
		}
	}

	/**
	 * 获取同播基站的配置
	 * 
	 * @param moId
	 * @return
	 */
	private static TConfMBMSBts getMBMSBtsConfig(long moId) {
		try {
			MBMSBtsService service = AppContext.getCtx().getBean(
					MBMSBtsService.class);

			return service.queryByMoId(moId);
		} catch (Exception e) {
			logger.error("Error getting mbms bts config.", e);
		}

		return null;
	}

	/**
	 * 获取位置区
	 * 
	 * @return
	 */
	private static List<LocationArea> getArea() {
		LocAreaService service = AppContext.getCtx().getBean(
				LocAreaService.class);
		return service.queryAll();
	}

	/**
	 * 获取空中链路的配置
	 * 
	 * @param moId
	 * @return
	 */
	private static AirlinkConfig getAirlinkConfig(long moId) {
		try {
			AirlinkService service = AppContext.getCtx().getBean(
					AirlinkService.class);
			return service.queryByMoId(moId);
		} catch (Exception e) {
			logger.error("Error getting airlink config.", e);
		}
		return null;
	}

	/**
	 * 获取以sag ID为key,基站列表为value的map
	 * 
	 * @return
	 */
	private Map<Long, List<McBts>> getSagMcBtsMap() {
		Map<Long, List<McBts>> map = new HashMap<Long, List<McBts>>();

		List<McBts> mcbtsList = McBtsCache.getInstance().queryAll();

		if (mcbtsList == null || mcbtsList.isEmpty())
			return new HashMap<Long, List<McBts>>();

		for (McBts mcBts : mcbtsList) {
			long sagId = mcBts.getSagDeviceId();

			List<McBts> list = map.get(sagId);

			if (list == null) {
				list = new ArrayList<McBts>();
				map.put(sagId, list);
			}

			list.add(mcBts);
		}

		return map;
	}

	/**
	 * 获取已经得到修饰的同播资源列表
	 * 
	 * @return
	 */
	private List<Simulcast> getFixedSimuList() {
		// 获取所有同播资源信息
		List<Simulcast> simuList = queryAll();
		if (simuList == null)
			simuList = new ArrayList<Simulcast>();

		for (Simulcast simu : simuList) {
			// 为了和iM3000的频点类型对应,Minas中的所有频点都+2
			simu.setFreqType(simu.getFreqType() + 2);
		}

		return simuList;
	}

	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	public void setSimulcastManageDAO(SimulcastManageDAO simulcastManageDAO) {
		this.simulcastManageDAO = simulcastManageDAO;
	}

	public void setiM3000IP(String iM3000IP) {
		this.iM3000IP = iM3000IP;
	}

	public void setiM3000PORT(String iM3000PORT) {
		this.iM3000PORT = iM3000PORT;
	}

}
