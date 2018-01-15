/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-19	| liuzhongyan 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.service.layer3.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.SystemProperty;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqModule;
import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqToBts;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.utils.FreqConvertUtil;
import com.xinwei.minas.server.core.conf.service.SystemPropertyService;
import com.xinwei.minas.server.mcbts.dao.layer3.TSysFreqDao;
import com.xinwei.minas.server.mcbts.proxy.layer3.SysFreqProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.SysFreqService;
import com.xinwei.minas.server.mcbts.utils.McBtsUtils;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * service实现
 * 
 * @author liuzhongyan
 * 
 */
public class SysFreqServiceImpl implements SysFreqService {
	private Log log = LogFactory.getLog(SysFreqServiceImpl.class);

	private TSysFreqDao sysFreqDao;

	private SysFreqProxy sysFreqProxy;

	private SequenceService sequenceService;

	private SystemPropertyService systemPropertyService;

	private static final String CATEGORY = "platform";
	private static final String PROPERTY = "system_freq_switch";

	public SysFreqServiceImpl() {
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	public void setSysFreqDao(TSysFreqDao sysFreqDao) {
		this.sysFreqDao = sysFreqDao;
	}

	public void setSysFreqProxy(SysFreqProxy sysFreqProxy) {
		this.sysFreqProxy = sysFreqProxy;
	}

	/**
	 * 从基站查询基站的有效频点
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public Object[] queryDataFromBts(long moId) throws Exception {
		// 获取MO的维护状态
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			Object[] objs = sysFreqProxy.queryData(moId);
			if (objs[0] == null || objs[1] == null)
				return null;

			List<TSysFreqModule> list = (List<TSysFreqModule>) objs[1];
			int freqType = McBtsCache.getInstance().queryByMoId(moId)
					.getBtsFreqType();
			FreqConvertUtil util = new FreqConvertUtil();
			util.setFreqType(freqType);
			for (TSysFreqModule module : list) {
				module.setFreqType(freqType);
				module.setDoubleType(util.getMidFreqValue(module.getFreq()));
			}
			return objs;
		}

		throw new Exception("No data.");
	}

	@Override
	public List<TSysFreqModule> queryUsedFreqFromBts() throws Exception {
		List<McBts> btss = McBtsCache.getInstance().queryAll();

		Set<TSysFreqModule> set = new HashSet<TSysFreqModule>();
		List<Future<List<TSysFreqModule>>> futureList = new ArrayList<Future<List<TSysFreqModule>>>();
		ExecutorService exec = Executors.newCachedThreadPool();

		for (McBts mcbts : btss) {
			// 跳过不可配置的基站
			if (!mcbts.isConfigurable()) {
				continue;
			}
			futureList.add(exec.submit(new QueryService(mcbts.getMoId())));
		}
		try {
			for (Future<List<TSysFreqModule>> future : futureList) {
				List<TSysFreqModule> tempFreqList = future.get();
				if (tempFreqList != null)
					set.addAll(tempFreqList);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			exec.shutdown();
		}

		if (set == null || set.size() == 0)
			return null;

		List<TSysFreqModule> list = new ArrayList<TSysFreqModule>(set);
		Collections.sort(list);

		return list;
	}

	private class QueryService implements Callable<List<TSysFreqModule>> {
		private long moId;

		public QueryService(long moId) {
			this.moId = moId;
		}

		@Override
		public List<TSysFreqModule> call() throws Exception {
			Object[] objs = queryDataFromBts(moId);
			if (objs != null && objs[1] != null)
				return (List<TSysFreqModule>) objs[1];

			return null;
		}
	}

	/**
	 * 配置系统有效频点到基站
	 */
	public void configBtsData(Long moId, List<TSysFreqModule> sysFreqList)
			throws Exception {
		Long idx = sequenceService.getNext();
		for (TSysFreqModule sysFreqModule : sysFreqList) {
			if (sysFreqModule.getIdx() == null) {
				sysFreqModule.setIdx(idx);
			}
		}

		// TODO 还没有下文
	}

	/**
	 * 系统频点下发
	 * 
	 */
	public void configData(Long moId, List<TSysFreqModule> sysFreqList,
			int freqSwitch, boolean isConfig) throws Exception {

		if (sysFreqList == null) {
			sysFreqList = new LinkedList<TSysFreqModule>();
		}

		for (TSysFreqModule freq : sysFreqList) {
			if (freq.getIdx() == null) {
				freq.setIdx(sequenceService.getNext());
			}
		}

		// moId为空 代表下发所有基站, 需要更新数据库
		if (moId == null) {
			// 保存有效频点开关
			configFreqSwitch(freqSwitch);

			sysFreqDao.deleteAll();

			// 存库
			for (TSysFreqModule sysFreq : sysFreqList) {
				sysFreqDao.saveOrUpdate(sysFreq);
			}
		}

		// FreqType标识是否引用系统频点
		if (isConfig) {
			// 失败的基站及失败信息
			Map<McBts, String> failedMap = new HashMap<McBts, String>();
			// moId为空 代表下发所有基站，要查询出所有的基站ID
			if (moId == null) {
				List<McBts> btsList = McBtsCache.getInstance().queryAll();
				// 根据每个基站查询出相应的基站类型，将相同类型的基站频点下发
				for (McBts mcbts : btsList) {
					// 判断基站是否是可配置的
					if (!mcbts.isConfigurable()) {
						continue;
					}
					List<TSysFreqModule> freqList = filterMcBtsFreq(mcbts,
							sysFreqList);
					TSysFreqToBts entity = new TSysFreqToBts();
					entity.setSysMoList(freqList);
					GenericBizData data = new GenericBizData(
							"t_conf_system_freq");
					data.addEntity(entity);
					try {
						sysFreqProxy.configData(mcbts.getMoId(), data,
								freqSwitch);
					} catch (UnsupportedOperationException e) {
						log.error(OmpAppContext
								.getMessage("unsupported_biz_operation")
								+ ":t_conf_system_freq");
					} catch (Exception e) {
						failedMap.put(mcbts, e.getLocalizedMessage());
					}
				}

			} else {
				TSysFreqToBts entity = new TSysFreqToBts();

				McBts mcbts = McBtsCache.getInstance().queryByMoId(moId);

				List<TSysFreqModule> freqList = filterMcBtsFreq(mcbts,
						sysFreqList);

				entity.setSysMoList(freqList);
				GenericBizData data = new GenericBizData("t_conf_system_freq");
				data.addEntity(entity);
				try {
					sysFreqProxy.configData(moId, data, freqSwitch);
				} catch (UnsupportedOperationException e) {
					log.error(OmpAppContext
							.getMessage("unsupported_biz_operation")
							+ ":t_conf_system_freq");
				}
			}
			// 触发批量更新基站异常
			McBtsUtils.fireBatchUpdateException(failedMap);
		}

	}

	/**
	 * 从一堆频点中过滤出符合某个基站频点的频点集
	 * 
	 * @param mcbts
	 * @param sysFreqList
	 * @return
	 */
	private List<TSysFreqModule> filterMcBtsFreq(McBts mcbts,
			List<TSysFreqModule> sysFreqList) {
		List<TSysFreqModule> freqList = new ArrayList<TSysFreqModule>();
		for (TSysFreqModule freq : sysFreqList) {
			if (mcbts.getBtsFreqType() == freq.getFreqType())
				freqList.add(freq);
		}

		return freqList;
	}

	/**
	 * 查询系统频点数据
	 * 
	 */
	public List<TSysFreqModule> queryData(int freqType) throws Exception {
		List<TSysFreqModule> sysFreqList = sysFreqDao.queryData(freqType);
		return sysFreqList;
	}

	/**
	 * 查询系统频点数据标识
	 * 
	 */
	public int queryFreqSwitch() throws Exception {
		systemPropertyService = AppContext.getCtx().getBean(
				SystemPropertyService.class);
		SystemProperty sp = systemPropertyService.getProperty(CATEGORY, null,
				PROPERTY);

		if (sp == null)
			return TSysFreqToBts.FREQ_ALL;

		return Integer.parseInt(sp.getValue());
	}

	/**
	 * 配置频点数据标识
	 * 
	 */
	public void configFreqSwitch(int freqSwitch) throws RemoteException,
			Exception {
		systemPropertyService = AppContext.getCtx().getBean(
				SystemPropertyService.class);
		systemPropertyService.setProperty(CATEGORY, null, PROPERTY,
				String.valueOf(freqSwitch));
	}

	public List<TSysFreqModule> queryAllData() throws RemoteException,
			Exception {
		List<TSysFreqModule> sysFreqList = sysFreqDao.queryAllData();
		return sysFreqList;
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		List<TSysFreqModule> sysFreqList = this.queryAllData();
		// 获取freqSwitch
		int freqSwitch = queryFreqSwitch();

		this.configData(moId, sysFreqList, freqSwitch, true);
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		// 什么都不做,不需要把基站的频点同步给ems

	}

	/**
	 * 删除指定的系统有效频点
	 * 
	 * @param freq
	 * @throws Exception
	 */
	@Override
	public void deleteData(TSysFreqModule freq) throws Exception {
		sysFreqDao.delete(freq);
	}

	/**
	 * 添加指定的系统有效频点
	 * 
	 * @param freq
	 * @throws Exception
	 */
	public void saveData(TSysFreqModule freq) throws Exception {
		freq.setIdx(sequenceService.getNext());
		sysFreqDao.saveOrUpdate(freq);
	}

	/**
	 * 更新指定的系统有效频点
	 * 
	 * @param freq
	 * @throws Exception
	 */
	public void updateData(TSysFreqModule freq) throws Exception {
		sysFreqDao.saveOrUpdate(freq);
	}

	@Override
	public void saveAllData(List<TSysFreqModule> freqs) throws Exception {
		for (TSysFreqModule freq : freqs) {
			freq.setIdx(sequenceService.getNext());
			sysFreqDao.saveOrUpdate(freq);
		}
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		// 不需要实现,群配业务

	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// 不需要实现,群配业务

	}

}
