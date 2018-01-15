/**
 * 
 */
package com.xinwei.minas.server.mcbts.service.layer3.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.core.exception.NeighborException;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer1.L1GeneralSetting;
import com.xinwei.minas.mcbts.core.model.layer1.RFConfig;
import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.mcbts.core.model.layer2.PECCHSetting;
import com.xinwei.minas.mcbts.core.model.layer2.SCGChannelConfigItem;
import com.xinwei.minas.mcbts.core.model.layer2.SCGScaleConfigItem;
import com.xinwei.minas.mcbts.core.model.layer3.Airlink_BCH;
import com.xinwei.minas.mcbts.core.model.layer3.Airlink_RARCH;
import com.xinwei.minas.mcbts.core.model.layer3.Airlink_RRCH;
import com.xinwei.minas.mcbts.core.model.layer3.AppendNeighborMessage;
import com.xinwei.minas.mcbts.core.model.layer3.AppendNeighborPECCHMessage;
import com.xinwei.minas.mcbts.core.model.layer3.BtsNeighborPECCHITtem;
import com.xinwei.minas.mcbts.core.model.layer3.BtsNeighbourItem;
import com.xinwei.minas.mcbts.core.model.layer3.ChannelItem;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsAppendNeighbor;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsNeighborFailed;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsNeighbour;
import com.xinwei.minas.mcbts.core.model.layer3.NbSameFreqMessage;
import com.xinwei.minas.mcbts.core.model.layer3.NeighborChannelItem;
import com.xinwei.minas.mcbts.core.model.layer3.NeighborMessage;
import com.xinwei.minas.mcbts.core.model.layer3.NeighborPECCHMessage;
import com.xinwei.minas.mcbts.core.model.layer3.NeighborSmallPack;
import com.xinwei.minas.mcbts.core.model.layer3.PECCHConfig;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.ChannelComparableMode;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfL1GeneralSettingDAO;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfRfConfigDAO;
import com.xinwei.minas.server.mcbts.dao.layer2.TConfAirlinkConfigDAO;
import com.xinwei.minas.server.mcbts.dao.layer2.TConfSubChannelConfigDAO;
import com.xinwei.minas.server.mcbts.dao.layer2.TConfSubScaleConfigDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.AppendNeighborDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.McbtsNeighborFailedDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.NeighborValidatorDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.NeighbourDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfRemoteBtsDAO;
import com.xinwei.minas.server.mcbts.proxy.layer3.NeighbourProxy;
import com.xinwei.minas.server.mcbts.service.McBtsBasicService;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer2.AirlinkService;
import com.xinwei.minas.server.mcbts.service.layer3.NeighborService;
import com.xinwei.minas.server.mcbts.service.sysManage.ChannelComparableModeService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.server.OmpAppContext;

/**
 * @author chenshaohua
 * 
 */
public class NeighborServiceImpl implements NeighborService {

	Log log = LogFactory.getLog(NeighborServiceImpl.class);

	private NeighbourDAO neighbourDAO;

	private AppendNeighborDAO appendNeighborDAO;

	private McbtsNeighborFailedDAO neighborFailedDAO;

	private TConfRemoteBtsDAO remoteBtsDAO;

	private TConfRfConfigDAO rfConfigDAO;

	private TConfL1GeneralSettingDAO l1generalSettingDAO;

	private TConfAirlinkConfigDAO airlinkConfigDAO;

	private TConfSubChannelConfigDAO subChannelConfigDAO;

	private TConfSubScaleConfigDAO subScaleConfigDAO;

	private NeighbourProxy neighbourProxy;

	private NeighborValidatorDAO neighborValidatorDAO;
	
//	private boolean isEnablePECCHConfig = false;

	private static int MAX_NEIGHBOR_LIST_SIZE = 20;

	private static int MAX_APPEND_NEIGHBOR_LIST_SIZE = 20;

	// private volatile boolean isConfigNeighbourList = false;

	// private volatile long curConfigMoId;

	// 邻接表配置锁
	private Lock configLock = new ReentrantLock();

	// 同步对象,用于对数据库同步进行操作
	private Object syncObject = new Object();

	@Override
	public List<McBts> queryNeighbor(Long moId) throws Exception {

		McBtsBasicService mcBtsBasicService = AppContext.getCtx().getBean(
				McBtsBasicService.class);

		List<McBts> mcBtsList = new ArrayList<McBts>();
		try {
			List<McBtsNeighbour> neighbourMoIdList;

			synchronized (syncObject) {
				neighbourMoIdList = neighbourDAO.queryNeighbour(moId);
			}

			for (McBtsNeighbour obj : neighbourMoIdList) {
				McBts mcBts = mcBtsBasicService.queryByMoId(obj
						.getNeighbourMoId());
				if (mcBts == null || mcBtsList.contains(mcBts)) {
					continue;
				}
				mcBtsList.add(mcBts);
			}
			// 按btsId升序
			Collections.sort(mcBtsList, new Comparator<McBts>() {
				@Override
				public int compare(McBts o1, McBts o2) {
					long temp = o1.getBtsId() - o2.getBtsId();
					if (temp > 0)
						return 1;
					else if (temp < 0)
						return -1;
					return 0;
				}
			});
		} catch (Exception e) {
			log.error("query Neighbor failed. ", e);
			throw new Exception(
					OmpAppContext.getMessage("neighbor_config_alert2"));
		}
		return mcBtsList;
	}

	@Override
	public List<McBts> queryAppendNeighbor(Long moId) throws Exception {

		McBtsBasicService mcBtsBasicService = AppContext.getCtx().getBean(
				McBtsBasicService.class);

		List<McBts> mcBtsList = new ArrayList<McBts>();
		try {
			List<McBtsAppendNeighbor> appendNeighborList;

			synchronized (syncObject) {
				appendNeighborList = appendNeighborDAO.queryNeighbour(moId);
			}

			for (McBtsAppendNeighbor obj : appendNeighborList) {
				McBts mcBts = mcBtsBasicService.queryByMoId(obj
						.getAppendNeighborMoId());
				if (mcBts == null || mcBtsList.contains(mcBts)) {
					continue;
				}
				mcBtsList.add(mcBts);
			}
			// 按btsId升序
			Collections.sort(mcBtsList, new Comparator<McBts>() {
				@Override
				public int compare(McBts o1, McBts o2) {
					long temp = o1.getBtsId() - o2.getBtsId();
					if (temp > 0)
						return 1;
					else if (temp < 0)
						return -1;
					return 0;
				}
			});
		} catch (Exception e) {
			log.error("query AppendNeighbor failed. ", e);
			throw new Exception(
					OmpAppContext.getMessage("neighbor_config_alert3"));
		}
		return mcBtsList;
	}

	@Override
	public List<McBts> queryNeighborFromNE(Long moId) throws Exception {
		return null;
	}

	@Override
	public List<McBts> queryAppendNeighborFromNE(Long moId) throws Exception {

		return null;
	}

	@Override
	public void config(Long moId, List<McBts> neighborList,
			List<McBts> appendNeighborList) throws Exception {
		if (configLock.tryLock()) {
			try {
				neighborListConfig(moId, neighborList, appendNeighborList);
			} finally {
				configLock.unlock();
			}
		} else {
			// 将基站保存到失败列表中
			saveConfigFailedBts(moId);
			McBts bts = McBtsCache.getInstance().queryByMoId(moId);
			if (bts != null) {
				throw new Exception(OmpAppContext.getMessage(
						"neighbor_config_alert7",
						new Object[] { bts.getHexBtsId() }));
			}

		}

		// if (!isConfig()) {
		// setConfigFlag(true);
		// try {
		// curConfigMoId = moId;
		// neighborListConfig(moId, neighborList, appendNeighborList);
		// } catch (Exception e) {
		// throw e;
		// } finally {
		// setConfigFlag(false);
		// }
		// } else {
		// // 将基站保存到失败列表中
		// saveConfigFailedBts(moId);
		// McBts bts = McBtsCache.getInstance().queryByMoId(curConfigMoId);
		// String btsId = "";
		// if (bts != null) {
		// btsId = bts.getHexBtsId();
		// }
		// throw new Exception(
		// OmpAppContext.getMessage("neighbor_config_alert7", new
		// Object[]{btsId}));
		// }
	}

	@Override
	public void sendNeighborConfig(Long moId, boolean isSyncConfig) {
		if (isSyncConfig) {
			syncSendNeighborConfig(moId);
		} else {
			asyncSendNeighborConfig(moId);
		}
	}

	/**
	 * 异步方式配置指定mo的邻接基站的邻接表
	 * 
	 * @param moId
	 */
	private void asyncSendNeighborConfig(Long moId) {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);

		if (bts == null || !bts.isConfigurable()) {
			// 基站不可配置，直接返回
			return;
		}
		final List<McBts> neighborList = new ArrayList<McBts>();
		try {
			neighborList.addAll(queryNeighbor(moId));
			neighborList.addAll(queryAppendNeighbor(moId));
		} catch (Exception ex) {
			log.error("Error querying all neighbour.", ex);
		}

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				for (McBts neighbor : neighborList) {
					try {
						List<McBts> neighbors = queryNeighbor(neighbor
								.getMoId());
						List<McBts> appendNeighbors = queryAppendNeighbor(neighbor
								.getMoId());
						// 异步方式配置邻接表就不进行同步等待了
						// config(neighbor.getMoId(), neighbors,
						// appendNeighbors);
						neighborListConfig(neighbor.getMoId(), neighbors,
								appendNeighbors);
					} catch (Exception ex) {
						log.error("Error sending neighbour config.", ex);
					}
				}
			}

		});
		executorService.shutdown();

	}

	/**
	 * 同步方式配置指定mo的邻接基站的邻接表
	 * 
	 * @param moId
	 */
	private void syncSendNeighborConfig(Long moId) {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);

		if (bts == null || !bts.isConfigurable()) {
			// 基站不可配置，直接返回
			return;
		}
		final List<McBts> neighborList = new ArrayList<McBts>();
		try {
			neighborList.addAll(queryNeighbor(moId));
			neighborList.addAll(queryAppendNeighbor(moId));
		} catch (Exception ex) {
			log.error("Error querying all neighbour.", ex);
		}

		for (McBts neighbor : neighborList) {
			try {
				List<McBts> neighbors = queryNeighbor(neighbor.getMoId());
				List<McBts> appendNeighbors = queryAppendNeighbor(neighbor
						.getMoId());
				config(neighbor.getMoId(), neighbors, appendNeighbors);
			} catch (Exception ex) {
				log.error("Error sending neighbour config.", ex);
			}
		}
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		List<McBts> neighbors = this.queryNeighbor(moId);
		List<McBts> appendNeighbors = this.queryAppendNeighbor(moId);
		// 同步网管数据到基站时不使用同步等待的方式
		// this.config(moId, neighbors, appendNeighbors);
		try {
			neighborListConfig(moId, neighbors, appendNeighbors);
		} catch (NeighborException e) {
			if (!StringUtils.isBlank(e.getErrorMessage())) {
				throw e;
			}
		}
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		for (long moId : moIdList) {
			// 查询当前基站的邻接表
			List<McBts> neighborList = queryNeighbor(moId);

			String str = getBidStr(neighborList);
			if (str != null) {
				business.getCell("neighbour").putContent(moId,
						toJSON("neighbour", str));
			}
			// 查询当前基站的附加邻接表
			neighborList = queryAppendNeighbor(moId);
			str = getBidStr(neighborList);
			if (str != null) {
				business.getCell("neighbourAppend").putContent(moId,
						toJSON("neighbourAppend", str));
			}
		}
	}

	private String getBidStr(List<McBts> btsList) {
		StringBuilder neighborSb = new StringBuilder();
		if (btsList == null || btsList.isEmpty()) {
			return null;
		}

		// {"btsId":"09103109"}]}
		for (McBts mcBts : btsList) {
			neighborSb.append(",{\"btsId\":\"");
			neighborSb.append(mcBts.getHexBtsId()).append("\"}");
		}
		return neighborSb.substring(1);
	}

	private static String toJSON(String key, String value) {
		return "\"" + key + "\":[" + value + "]";
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// Do nothing
		// 不支持邻接表配置导入,邻接表配置导入在网规信息导入中进行
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		// do nothing
	}

	@Override
	public void processNbConfigFailedBts() {
		// 获取邻接表配置失败的队列
		List<McBtsNeighborFailed> configFailedList = neighborFailedDAO
				.queryAll();
		try {
			for (McBtsNeighborFailed failedBts : configFailedList) {
				Long moId = failedBts.getMoId();
				List<McBts> neighbors = this.queryNeighbor(moId);
				List<McBts> appendNeighbors = this.queryAppendNeighbor(moId);
				try {
					this.config(moId, neighbors, appendNeighbors);
				} catch (Exception e) {
					log.error(e.getMessage());
				}

			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public McBtsNeighborFailed queryConfigFailedBts(Long moId) throws Exception {
		return neighborFailedDAO.queryByMoId(moId);
	}

	/**
	 * 邻接表配置过程
	 * 
	 * @param moId
	 * @param newNbList
	 * @param newAppendNbList
	 * @throws Exception
	 */
	private void neighborListConfig(Long moId, List<McBts> newNbList,
			List<McBts> newAppendNbList) throws Exception {

		if (newNbList == null) {
			newNbList = new ArrayList<McBts>();
		}

		if (newAppendNbList == null) {
			newAppendNbList = new ArrayList<McBts>();
		}

		// 对邻接表和附加邻接表进行验证
		validNeighborList(newNbList, newAppendNbList);

		McBts bts = McBtsCache.getInstance().queryByMoId(moId);

		// 判断基站是否存在
		if (bts == null) {
			throw new Exception(OmpAppContext.getMessage(
					"neighbor_mcbts_not_exist",
					new Object[] { moId + "(moId)" }));
		}

		// 查找需要重新进行邻接表配置的邻接站，包括删除的、新增的以及之前配置错误的基站
		List<McBts> modifiedNbList = findNeedConfigNbBts(moId, newNbList,
				newAppendNbList);

		// 将新的邻接关系和附加邻接关系保存到数据库中
		updateNbRelationship(moId, newNbList, newAppendNbList);

		//根据当前兼容模式判断是否进行PECCH的相关配置
		boolean isEnablePECCHConfig = startPECCHByComparebleMode();
		
		StringBuilder infoBuffer = new StringBuilder("");

		StringBuilder errorBuffer = new StringBuilder("");

		// 向本基站发送邻接表配置请求
		try {
			configBts(moId, newNbList, newAppendNbList, isEnablePECCHConfig);
		} catch (BizException e) {
			infoBuffer.append(e.getMessage() + "\n");
			saveConfigFailedBts(moId);
		} catch (Exception e) {
			errorBuffer.append(e.getMessage() + "\n");
			saveConfigFailedBts(moId);
		}

		// 对邻接站进行配置
		for (McBts nbMcBts : modifiedNbList) {
			List<McBts> nbList = queryNeighbor(nbMcBts.getMoId());
			List<McBts> appendList = queryAppendNeighbor(nbMcBts.getMoId());
			try {
				configBts(nbMcBts.getMoId(), nbList, appendList, isEnablePECCHConfig);
			} catch (BizException e) {
				// String error = OmpAppContext
				// .getMessage("neighbor_config_alert6", new Object[] {
				// bts.getHexBtsId(), nbMcBts.getHexBtsId() });
				// errorBuffer.append(error + e.getMessage() + "\n");
				infoBuffer.append(e.getMessage() + "\n");
				saveConfigFailedBts(nbMcBts.getMoId());
			} catch (Exception e) {
				errorBuffer.append(e.getMessage() + "\n");
				saveConfigFailedBts(nbMcBts.getMoId());
			}
		}

		if (!StringUtils.isBlank(errorBuffer.toString())
				|| !StringUtils.isBlank(infoBuffer.toString())) {
			throw new NeighborException(infoBuffer.toString(),
					errorBuffer.toString());
		}
	}

	/**
	 * 判断是否存在邻接关系
	 * 
	 * @param basicMoId
	 * @param neighbourMoId
	 * @return
	 */
	private boolean isNeighborRelation(Long basicMoId, Long neighborMoId) {
		return neighbourDAO.isNeighborRelation(basicMoId, neighborMoId);
	}

	private boolean isAppendNeighborRelation(Long basicMoId,
			Long appendNeighborMoId) {
		return appendNeighborDAO.isAppendNeighborRelation(basicMoId,
				appendNeighborMoId);
	}

	/**
	 * 将邻接表保存到数据库中
	 * 
	 * @param moId
	 * @param nbList
	 */
	private void saveNeigborList(Long moId, List<McBts> nbList) {

		SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);

		for (McBts mcBts : nbList) {
			// 邻接站ID
			long tempId = mcBts.getMoId();

			// 删除这对基站在附加邻接表中的对应关系
			appendNeighborDAO.delAppendNbRelationship(moId, tempId);

			// 增加本基站的邻接关系
			McBtsNeighbour neigbour1 = new McBtsNeighbour();
			neigbour1.setIdx(sequenceService.getNext());
			neigbour1.setMoId(moId);
			neigbour1.setNeighbourMoId(tempId);
			neighbourDAO.saveOrUpdate(neigbour1);

			// 为邻接站增加邻接关系
			McBtsNeighbour neigbour2 = new McBtsNeighbour();
			neigbour2.setIdx(sequenceService.getNext());
			neigbour2.setMoId(tempId);
			neigbour2.setNeighbourMoId(moId);
			neighbourDAO.saveOrUpdate(neigbour2);

		}
	}

	/**
	 * 将附加邻接表关系保存到数据库
	 * 
	 * @param moId
	 * @param appendNbList
	 */
	private void saveAppendNeighborList(Long moId, List<McBts> appendNbList) {

		SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);

		for (McBts mcBts : appendNbList) {
			long nbId = mcBts.getMoId();

			// 删除这对基站在邻接表中的对应关系
			neighbourDAO.delNbRelationship(moId, nbId);

			// 增加本基站的的邻接关系
			McBtsAppendNeighbor aNb = new McBtsAppendNeighbor();
			aNb.setIdx(sequenceService.getNext());
			aNb.setMoId(moId);
			aNb.setAppendNeighborMoId(nbId);
			appendNeighborDAO.saveOrUpdate(aNb);

			// 为邻接站增加邻接关系
			McBtsAppendNeighbor aNb1 = new McBtsAppendNeighbor();
			aNb1.setIdx(sequenceService.getNext());
			aNb1.setMoId(nbId);
			aNb1.setAppendNeighborMoId(moId);
			appendNeighborDAO.saveOrUpdate(aNb1);
		}
	}

	/**
	 * 保存邻接表配置失败的基站
	 * 
	 * @param moId
	 */
	private void saveConfigFailedBts(Long moId) {

		SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);

		try {
			McBtsNeighborFailed nf = neighborFailedDAO.queryByMoId(moId);
			if (nf == null) {
				nf = new McBtsNeighborFailed();
				nf.setIdx(sequenceService.getNext());
				nf.setMoId(moId);
			}
			nf.setUpdateTime(new Date());
			neighborFailedDAO.saveOrUpdate(nf);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	// private synchronized boolean isConfig() {
	// return isConfigNeighbourList;
	// }
	//
	// private synchronized void setConfigFlag(boolean flag) {
	// isConfigNeighbourList = flag;
	// }

	/**
	 * 对邻接表和附加邻接表进行验证
	 * 
	 * @param neighborList
	 * @param appendNeighborList
	 */
	private void validNeighborList(List<McBts> neighborList,
			List<McBts> appendNeighborList) throws Exception {

		// 验证邻接表的数量是否超过范围
		if (neighborList.size() > MAX_NEIGHBOR_LIST_SIZE) {
			throw new Exception(
					OmpAppContext.getMessage("neighbor_list_size_out_range"));
		}
		// 验证附加邻接表的数量是否超过范围
		if (appendNeighborList.size() > MAX_APPEND_NEIGHBOR_LIST_SIZE) {
			throw new Exception(
					OmpAppContext
							.getMessage("append_neighbor_list_size_out_range"));
		}

		// 验证邻接表和附加邻接表的邻接站是否重复
		String errMessage = "";
		for (McBts mcBts : neighborList) {
			if (appendNeighborList.contains(mcBts)) {
				if (StringUtils.isBlank(errMessage)) {
					errMessage = mcBts.getHexBtsId() + "(" + mcBts.getName()
							+ ")";
				} else {
					errMessage = errMessage + "," + mcBts.getHexBtsId() + "("
							+ mcBts.getName() + ")";
				}
			}
		}

		if (!StringUtils.isBlank(errMessage)) {
			throw new Exception(
					OmpAppContext.getMessage("neighbor_config_alert1")
							+ errMessage);
		}
	}

	/**
	 * 查找需要重新进行邻接表配置的邻接站，包括删除的、新增的以及之前配置错误的基站
	 * 
	 * @param moId
	 * @param newNbList
	 * @param newAppendNbList
	 * @return
	 * @throws Exception
	 */
	private List<McBts> findNeedConfigNbBts(Long moId, List<McBts> newNbList,
			List<McBts> newAppendNbList) throws Exception {

		// modifiedNbList用来记录需要重新配置邻接表的邻接站
		List<McBts> modifiedNbList = new ArrayList<McBts>();

		// 分别获得本基站旧的邻接表和附加邻接表
		List<McBts> oldNbList = this.queryNeighbor(moId);
		List<McBts> oldAppendNbList = this.queryAppendNeighbor(moId);

		// 之前邻接表配置失败的基站
		List<McBtsNeighborFailed> configFailedList = neighborFailedDAO
				.queryAll();

		if (configFailedList == null) {// 以防出现空指针
			configFailedList = new ArrayList<McBtsNeighborFailed>();
		}

		/*
		 * 对邻接表进行操作
		 */

		// 旧的邻接站，查找邻接表需要删除的邻接站
		for (McBts oldMcBts : oldNbList) {
			if (isConfigFailed(oldMcBts.getMoId(), configFailedList)
					|| !newNbList.contains(oldMcBts)) {// 要删除的邻接站，以及未删除，但是之前配置失败的邻接站
				modifiedNbList.add(oldMcBts);
			}
		}

		// 新的邻接站，判断新邻接站的邻接表中是否包含该基站，没有则需要对新邻接站重新配置邻接表
		for (McBts newMcBts : newNbList) {
			if (oldNbList.contains(newMcBts)) {
				continue;
			}
			if (isConfigFailed(newMcBts.getMoId(), configFailedList)
					|| !isNeighborRelation(newMcBts.getMoId(), moId)) {// 新增以及之前配置失败的基站
				modifiedNbList.add(newMcBts);
			}
		}

		/*
		 * 对附加邻接表进行操作
		 */

		// 旧的附加邻接站，查找需要删除的邻接站
		for (McBts mcBts : oldAppendNbList) {
			if (modifiedNbList.contains(mcBts)) {
				continue;
			}
			if (isConfigFailed(mcBts.getMoId(), configFailedList)
					|| !newAppendNbList.contains(mcBts)) {// 要删除的邻接站,
															// 以及未删除，但是之前配置失败的邻接站
				modifiedNbList.add(mcBts);
			}
		}

		// 新的附加邻接站，判断新邻接站的附加邻接表中是否包含该基站，没有则需要对新邻接站重新配置邻接表
		for (McBts mcBts : newAppendNbList) {
			if (oldAppendNbList.contains(mcBts)
					|| modifiedNbList.contains(mcBts)) {
				continue;
			}
			if (isConfigFailed(mcBts.getMoId(), configFailedList)
					|| !isAppendNeighborRelation(mcBts.getMoId(), moId)) {// 新增基站，以及之前配置失败的基站
				modifiedNbList.add(mcBts);
			}
		}

		return modifiedNbList;

	}

	private boolean isConfigFailed(long moId,
			List<McBtsNeighborFailed> failedList) {
		for (McBtsNeighborFailed failed : failedList) {
			if (moId == failed.getMoId()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将新的邻接表和附加邻接表保存到数据库中
	 * 
	 * @param moId
	 * @param newNbList
	 * @param newAppendNbList
	 */
	private void updateNbRelationship(Long moId, List<McBts> newNbList,
			List<McBts> newAppendNbList) {
		synchronized (syncObject) {
			/*
			 * 邻接表
			 */
			// 删除旧的邻接表
			neighbourDAO.deleteOld(moId);

			// 保存新的邻接表
			saveNeigborList(moId, newNbList);

			/*
			 * 附加邻接表
			 */
			// 删除旧的附加邻接表
			appendNeighborDAO.deleteOld(moId);

			// 保存新的附加邻接表
			saveAppendNeighborList(moId, newAppendNbList);
		}
	}

	/**
	 * 对基站进行邻接表配置
	 * 
	 * @param moId
	 * @param newNbList
	 * @param newAppendNbList
	 */
	private void configBts(Long moId, List<McBts> newNbList,
			List<McBts> newAppendNbList, boolean isEnablePECCHConfig) throws Exception {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);

		if (bts == null) {
			throw new Exception(OmpAppContext.getMessage(
					"neighbor_mcbts_not_exist",
					new Object[] { moId + "(moId)" }));
		}

		// 基站不可配置
		if (!bts.isConfigurable()) {
			// throw new BizException(OmpAppContext.getMessage(
			// "neighbor_config_alert4",
			// new Object[] { bts.getHexBtsId() }));
			throw new BizException(bts.getHexBtsId() + "(" + bts.getName()
					+ ")");
		}

		// 封装消息
		NeighborMessage nbMessage2CurMcBts = fillNbMessage(newNbList); // 邻站表配置请求
		AppendNeighborMessage appendNbMsg2CurMcBts = fillAppendNbMessage(newAppendNbList);// 附加邻站表配置请求
		NbSameFreqMessage nbSameFreqMsg2CurMcBts = fillNbSameFreqMessage(newNbList);// 邻站同频配置请求
		NeighborPECCHMessage pecchNeighborMessage = null;
		AppendNeighborPECCHMessage pecchAppendNbMessage = null;
		
		
		
		if(isEnablePECCHConfig) {
			//PECCH邻站表配置请求
			pecchNeighborMessage = fillPECCHNbMessage(newNbList);
			//PECCH切换附加邻站表配置请求
			pecchAppendNbMessage = fillPECCHAppendNbMessage(newAppendNbList);
		}

		// 发送邻站表配置请求
		try {
			neighbourProxy.configNeighbour(moId, nbMessage2CurMcBts);
		} catch (Exception e) {
			throw new Exception(OmpAppContext.getMessage("neighbor_config_alert5", new Object[]{bts.getHexBtsId() + "(" + bts.getName() + ")" }) + e.getMessage());
		}
		
		//发送PECCH邻站表配置请求
		if (isEnablePECCHConfig) {
			try {
				neighbourProxy.configPECCHNeighbor(moId, pecchNeighborMessage);
			} catch (BizException e) {
				throw new Exception(OmpAppContext.getMessage("neighbor_config_alert5", new Object[]{bts.getHexBtsId() + "(" + bts.getName() + ")" }) + e.getMessage());
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		
		//发送附加邻接站配置请求
		try {
			neighbourProxy.configAppendNeighbor(moId, appendNbMsg2CurMcBts);
		} catch (BizException e) {
			throw new Exception(OmpAppContext.getMessage("neighbor_config_alert5", new Object[]{bts.getHexBtsId() + "(" + bts.getName() + ")" }) + e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		//发送PECCH切换附加邻站表配置请求
		if (isEnablePECCHConfig) {
			try {
				neighbourProxy.configPECCHAppendNeighbor(moId, pecchAppendNbMessage);
			} catch (BizException e) {
				throw new Exception(OmpAppContext.getMessage("neighbor_config_alert5", new Object[]{bts.getHexBtsId() + "(" + bts.getName() + ")" }) + e.getMessage());
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		
		//发送邻站同频配置请求
		try {
			neighbourProxy.configNbSameFreq(moId, nbSameFreqMsg2CurMcBts);
		} catch (BizException e) {
			throw new Exception(OmpAppContext.getMessage(
					"neighbor_config_alert5", new Object[] { bts.getHexBtsId()
							+ "(" + bts.getName() + ")" })
					+ e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		// 基站配置成功，删除之前失败的记录
		neighborFailedDAO.delByMoId(moId);
	}
	
	/**
	 * 根据当前的兼容模式判断是否进行PECCH的相关配置
	 */
	private boolean  startPECCHByComparebleMode() throws Exception{
		
		ChannelComparableModeService comparableModeService = AppContext
				.getCtx().getBean(ChannelComparableModeService.class);

		try {
			ChannelComparableMode mode = comparableModeService.queryFromEMS();
			if (mode.getChannelMode() == ChannelComparableMode.PCCH_ONLY) {
				return  false;
			} else {
				return true;
			}
		} catch (Exception e) {
			throw new Exception(OmpAppContext.getMessage("neighbor_config_alert8"));
		}
	}
	
	/**
	 * 构造邻站表配置请求消息
	 * 
	 * @param neighbouList
	 * @return
	 */
	private NeighborMessage fillNbMessage(List<McBts> neighborList) {
		NeighborMessage neighbourMessage = new NeighborMessage();

		// LongDistModFlag,固定设置为支持远距离应用模式，开关1表示支持
		neighbourMessage.setLongDistModFlag(0x5a5a);

		// mNeighborBTSnumber
		neighbourMessage.setmNeighborBTSnumber(neighborList.size());

		BtsNeighbourItem item[] = new BtsNeighbourItem[neighborList.size()];

		for (int i = 0; i < item.length; i++) {
			item[i] = new BtsNeighbourItem();
			TConfRemoteBts remoteBts = remoteBtsDAO.queryByMoId(neighborList
					.get(i).getMoId());
			if (remoteBts != null
					&& remoteBts.getFlag() == TConfRemoteBts.FLAG_SUPPORT) {
				// 设置btsMask
				neighbourMessage.setAllNeighbourBtsMask(i);
			}

			// 设置btsId
			item[i].setmBTSID(neighborList.get(i).getBtsId());

			// 设置btsIp
			item[i].setBtsIp(neighborList.get(i).getBtsIp());

			RFConfig rfConfig = rfConfigDAO.queryByMoId(neighborList.get(i)
					.getMoId());
			// 设置Frequencyindex
			item[i].setmFrequencyindex(rfConfig.getFreqOffset());
			// 设置TRANSMIT_PWR
			item[i].setmTRANSMIT_PWR(rfConfig.getAntennaPower());
			// 设置 RECEIVE_SENSITIVITY
			item[i].setmRECEIVE_SENSITIVITY(rfConfig.getRxSensitivity());

			// 设置m N_ANT
			L1GeneralSetting l1GeneralSetting = l1generalSettingDAO
					.queryByMoId(neighborList.get(i).getMoId());
			int inum = 0;
			for (int j = 31; j >= 0; j--) {
				if (((1 << j) & l1GeneralSetting.getAntennaMask().intValue()) != 0) {
					inum++;
				}
			}
			item[i].setmN_ANT(inum);

			AirlinkConfig airlinkConfig = airlinkConfigDAO
					.queryByMoId(neighborList.get(i).getMoId());

			// 设置SequenceID
			item[i].setmSequenceID(airlinkConfig.getSequenceId());
			// 设置SubcarriergroupMask
			item[i].setmSubcarriergroupMask(airlinkConfig.getScgMask());
			// 设置N_TS
			item[i].setmN_TS(airlinkConfig.getTotalTS());
			// 设置N_DN_TS
			item[i].setmN_DN_TS(airlinkConfig.getDownlinkTS());
			// 设置MAX_SCALE
			item[i].setmMAX_SCALE(airlinkConfig.getMaxScale() * 32767 / 640000);
			// 设置PREAMBLE_SCALE
			item[i].setmPREAMBLE_SCALE(airlinkConfig.getPreambleScale() * 32767 / 640000);

			List<SCGChannelConfigItem> SCGChannelConfigItemList = subChannelConfigDAO
					.queryByMoId(neighborList.get(i).getMoId());

			// 设置BCH
			List<SCGChannelConfigItem> bchList = new ArrayList<SCGChannelConfigItem>();
			for (SCGChannelConfigItem obj : SCGChannelConfigItemList) {
				if (obj.getChannelType() == SCGChannelConfigItem.BCH) {
					bchList.add(obj);
				}
			}
			Airlink_BCH bch[] = new Airlink_BCH[AirlinkConfig.BCH_COUNT];
			for (int j = 0; j < bch.length; j++) {
				bch[j] = new Airlink_BCH();
				bch[j].setmBCHSCGindex(0xff);
				bch[j].setmBCHTSindex(0xff);
			}
			for (int j = 0; j < bchList.size() && j < bch.length; j++) {
				SCGChannelConfigItem bchConfigItem = bchList.get(j);
				bch[j].setmBCHSCGindex(bchConfigItem.getScgIndex());
				bch[j].setmBCHTSindex(bchConfigItem.getTsIndex());
			}
			item[i].setmAirlink_BCH(bch);

			// 设置RRCH
			List<SCGChannelConfigItem> rrchList = new ArrayList<SCGChannelConfigItem>();
			for (SCGChannelConfigItem obj : SCGChannelConfigItemList) {
				if (obj.getChannelType() == SCGChannelConfigItem.RRCH) {
					rrchList.add(obj);
				}
			}
			Airlink_RRCH rrch[] = new Airlink_RRCH[AirlinkConfig.RRCH_COUNT];
			for (int j = 0; j < rrch.length; j++) {
				rrch[j] = new Airlink_RRCH();
				rrch[j].setmRRCHSCGindex(0xff);
				rrch[j].setmRRCHTSindex(0xff);
			}
			for (int j = 0; j < rrchList.size() && j < rrch.length; j++) {
				SCGChannelConfigItem rrchConfigItem = rrchList.get(j);
				rrch[j].setmRRCHSCGindex(rrchConfigItem.getScgIndex());
				rrch[j].setmRRCHTSindex(rrchConfigItem.getTsIndex());
			}
			item[i].setmAirlink_RRCH(rrch);

			// 设置RARCH
			List<SCGChannelConfigItem> rarchList = new ArrayList<SCGChannelConfigItem>();
			for (SCGChannelConfigItem obj : SCGChannelConfigItemList) {
				if (obj.getChannelType() == SCGChannelConfigItem.RARCH) {
					rarchList.add(obj);
				}
			}
			Airlink_RARCH rarch[] = new Airlink_RARCH[AirlinkConfig.RARCH_COUNT];
			for (int j = 0; j < rarch.length; j++) {
				rarch[j] = new Airlink_RARCH();
				rarch[j].setmRARCHSCGindex(0xff);
				rarch[j].setmRARCHTSindex(0xff);
			}
			for (int j = 0; j < rarchList.size() && j < rarch.length; j++) {
				SCGChannelConfigItem rarchConfigItem = rarchList.get(j);
				rarch[j].setmRARCHSCGindex(rarchConfigItem.getScgIndex());
				rarch[j].setmRARCHTSindex(rarchConfigItem.getTsIndex());
			}
			item[i].setmAirlink_RARCH(rarch);

			// 设置TCH
			List<SCGScaleConfigItem> SCGScaleConfigItemList = subScaleConfigDAO
					.queryByMoId(neighborList.get(i).getMoId());
			for (SCGScaleConfigItem obj : SCGScaleConfigItemList) {
				if (obj.getTsIndex() == 0) {
					item[i].setmTCH_SCALE0(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 1) {
					item[i].setmTCH_SCALE1(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 2) {
					item[i].setmTCH_SCALE2(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 3) {
					item[i].setmTCH_SCALE3(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 4) {
					item[i].setmTCH_SCALE4(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 5) {
					item[i].setmTCH_SCALE5(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 6) {
					item[i].setmTCH_SCALE6(obj.getTchScale() * 32767 / 640000);
				}
			}
			// 直放站数目先设为0
			item[i].setmRepeaternumber(0);
		}
		neighbourMessage.setItem(item);

		return neighbourMessage;
	}

	/**
	 * 构造PECCH邻站表配置请求
	 * @param neighborList
	 * @return
	 */
	private NeighborPECCHMessage fillPECCHNbMessage(List<McBts> neighborList)throws Exception {

		AirlinkService airlinkService = AppContext.getCtx().getBean(
				AirlinkService.class);
		
		NeighborPECCHMessage nbPECCHMessage = new NeighborPECCHMessage();
		
		// LongDistModFlag,固定设置为支持远距离应用模式，开关1表示支持
		nbPECCHMessage.setLongDistModFlag(0x5a5a);
		
		// mNeighborBTSnumber
		nbPECCHMessage.setmNeighborBTSnumber(neighborList.size());
		
		//BTS_INFO_IE2
		BtsNeighborPECCHITtem[] items = new BtsNeighborPECCHITtem[neighborList.size()];
		
		for (int i = 0; i < neighborList.size(); i++) {
			items[i] = new BtsNeighborPECCHITtem();
			
			TConfRemoteBts remoteBts = remoteBtsDAO.queryByMoId(neighborList
					.get(i).getMoId());
			if (remoteBts != null
					&& remoteBts.getFlag() == TConfRemoteBts.FLAG_SUPPORT) {
				// 设置btsMask
				nbPECCHMessage.setAllNeighbourBtsMask(i);
			}

			// 设置btsId
			items[i].setmBTSID(neighborList.get(i).getBtsId());

			// 设置btsIp
			items[i].setBtsIp(neighborList.get(i).getBtsIp());

			RFConfig rfConfig = rfConfigDAO.queryByMoId(neighborList.get(i)
					.getMoId());
			// 设置Frequencyindex
			items[i].setmFrequencyindex(rfConfig.getFreqOffset());
			// 设置TRANSMIT_PWR
			items[i].setmTRANSMIT_PWR(rfConfig.getAntennaPower());
			// 设置 RECEIVE_SENSITIVITY
			items[i].setmRECEIVE_SENSITIVITY(rfConfig.getRxSensitivity());

			// 设置m N_ANT
			L1GeneralSetting l1GeneralSetting = l1generalSettingDAO
					.queryByMoId(neighborList.get(i).getMoId());
			int inum = 0;
			for (int j = 31; j >= 0; j--) {
				if (((1 << j) & l1GeneralSetting.getAntennaMask().intValue()) != 0) {
					inum++;
				}
			}
			items[i].setmN_ANT(inum);

			AirlinkConfig airlinkConfig = airlinkService
					.queryByMoId(neighborList.get(i).getMoId());

			// 设置SequenceID
			items[i].setmSequenceID(airlinkConfig.getSequenceId());
			// 设置SubcarriergroupMask
			items[i].setmSubcarriergroupMask(airlinkConfig.getScgMask());
			// 设置N_TS
			items[i].setmN_TS(airlinkConfig.getTotalTS());
			// 设置N_DN_TS
			items[i].setmN_DN_TS(airlinkConfig.getDownlinkTS());
			// 设置MAX_SCALE
			items[i].setmMAX_SCALE(airlinkConfig.getMaxScale() * 32767 / 640000);
			// 设置PREAMBLE_SCALE
			items[i].setmPREAMBLE_SCALE(airlinkConfig.getPreambleScale() * 32767 / 640000);
			
			//PECCH_Config
			PECCHSetting pecch = airlinkConfig.getPecchSetting();
			PECCHConfig mPECCHConfig = new PECCHConfig();
			if (pecch != null) {
				//SCG_IDX
				mPECCHConfig.setmSCG_IDX(pecch.getScgIndex());
				//N_PCH_SET
				mPECCHConfig.setmN_PCH_SET(pecch.getPchCount());
				//PCH_ID
				mPECCHConfig.setmPCH_ID(pecch.getPchIndex());
				//N_RARCH
				mPECCHConfig.setmN_RARCH(pecch.getRarchCount());
				//N_RRCH
				mPECCHConfig.setmN_RRCH(pecch.getRrchCount());
			}
			items[i].setmPECCHConfig(mPECCHConfig);
			
			// 设置TCH
			List<SCGScaleConfigItem> SCGScaleConfigItemList = subScaleConfigDAO
					.queryByMoId(neighborList.get(i).getMoId());
			for (SCGScaleConfigItem obj : SCGScaleConfigItemList) {
				if (obj.getTsIndex() == 0) {
					items[i].setmTCH_SCALE0(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 1) {
					items[i].setmTCH_SCALE1(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 2) {
					items[i].setmTCH_SCALE2(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 3) {
					items[i].setmTCH_SCALE3(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 4) {
					items[i].setmTCH_SCALE4(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 5) {
					items[i].setmTCH_SCALE5(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 6) {
					items[i].setmTCH_SCALE6(obj.getTchScale() * 32767 / 640000);
				}
			}
			// 直放站数目先设为0
			items[i].setmRepeaternumber(0);
			
		}
		
		nbPECCHMessage.setItems(items);
		return nbPECCHMessage;
	}
	
	/**
	 * 构造切换附加邻接表配置
	 * @param appendNbList
	 * @return
	 */
	private AppendNeighborMessage fillAppendNbMessage(List<McBts> appendNbList) {
		AppendNeighborMessage appendNbMessage = new AppendNeighborMessage();

		// ConfigFlag, 目前默认填0
		appendNbMessage.setConfigFlag(0);

		// mAppendNeighborBTSnumber
		appendNbMessage.setmAppendNeighborBTSnumber(appendNbList.size());

		BtsNeighbourItem item[] = new BtsNeighbourItem[appendNbList.size()];

		for (int i = 0; i < item.length; i++) {
			item[i] = new BtsNeighbourItem();
			TConfRemoteBts remoteBts = remoteBtsDAO.queryByMoId(appendNbList
					.get(i).getMoId());
			if (remoteBts != null
					&& remoteBts.getFlag() == TConfRemoteBts.FLAG_SUPPORT) {
				// 设置btsMask
				appendNbMessage.setAllNeighbourBtsMask(i);
			}

			// 设置btsId
			item[i].setmBTSID(appendNbList.get(i).getBtsId());

			// 设置btsIp
			item[i].setBtsIp(appendNbList.get(i).getBtsIp());

			RFConfig rfConfig = rfConfigDAO.queryByMoId(appendNbList.get(i)
					.getMoId());
			// 设置Frequencyindex
			item[i].setmFrequencyindex(rfConfig.getFreqOffset());
			// 设置TRANSMIT_PWR
			item[i].setmTRANSMIT_PWR(rfConfig.getAntennaPower());
			// 设置 RECEIVE_SENSITIVITY
			item[i].setmRECEIVE_SENSITIVITY(rfConfig.getRxSensitivity());

			// 设置m N_ANT
			L1GeneralSetting l1GeneralSetting = l1generalSettingDAO
					.queryByMoId(appendNbList.get(i).getMoId());
			int inum = 0;
			for (int j = 31; j >= 0; j--) {
				if (((1 << j) & l1GeneralSetting.getAntennaMask().intValue()) != 0) {
					inum++;
				}
			}
			item[i].setmN_ANT(inum);

			AirlinkConfig airlinkConfig = airlinkConfigDAO
					.queryByMoId(appendNbList.get(i).getMoId());

			// 设置SequenceID
			item[i].setmSequenceID(airlinkConfig.getSequenceId());
			// 设置SubcarriergroupMask
			item[i].setmSubcarriergroupMask(airlinkConfig.getScgMask());
			// 设置N_TS
			item[i].setmN_TS(airlinkConfig.getTotalTS());
			// 设置N_DN_TS
			item[i].setmN_DN_TS(airlinkConfig.getDownlinkTS());
			// 设置MAX_SCALE
			item[i].setmMAX_SCALE(airlinkConfig.getMaxScale() * 32767 / 640000);
			// 设置PREAMBLE_SCALE
			item[i].setmPREAMBLE_SCALE(airlinkConfig.getPreambleScale() * 32767 / 640000);

			List<SCGChannelConfigItem> SCGChannelConfigItemList = subChannelConfigDAO
					.queryByMoId(appendNbList.get(i).getMoId());

			// 设置BCH
			List<SCGChannelConfigItem> bchList = new ArrayList<SCGChannelConfigItem>();
			for (SCGChannelConfigItem obj : SCGChannelConfigItemList) {
				if (obj.getChannelType() == SCGChannelConfigItem.BCH) {
					bchList.add(obj);
				}
			}
			Airlink_BCH bch[] = new Airlink_BCH[AirlinkConfig.BCH_COUNT];
			for (int j = 0; j < bch.length; j++) {
				bch[j] = new Airlink_BCH();
				bch[j].setmBCHSCGindex(0xff);
				bch[j].setmBCHTSindex(0xff);
			}
			for (int j = 0; j < bchList.size() && j < bch.length; j++) {
				SCGChannelConfigItem bchConfigItem = bchList.get(j);
				bch[j].setmBCHSCGindex(bchConfigItem.getScgIndex());
				bch[j].setmBCHTSindex(bchConfigItem.getTsIndex());
			}
			item[i].setmAirlink_BCH(bch);

			// 设置RRCH
			List<SCGChannelConfigItem> rrchList = new ArrayList<SCGChannelConfigItem>();
			for (SCGChannelConfigItem obj : SCGChannelConfigItemList) {
				if (obj.getChannelType() == SCGChannelConfigItem.RRCH) {
					rrchList.add(obj);
				}
			}
			Airlink_RRCH rrch[] = new Airlink_RRCH[AirlinkConfig.RRCH_COUNT];
			for (int j = 0; j < rrch.length; j++) {
				rrch[j] = new Airlink_RRCH();
				rrch[j].setmRRCHSCGindex(0xff);
				rrch[j].setmRRCHTSindex(0xff);
			}
			for (int j = 0; j < rrchList.size() && j < rrch.length; j++) {
				SCGChannelConfigItem rrchConfigItem = rrchList.get(j);
				rrch[j].setmRRCHSCGindex(rrchConfigItem.getScgIndex());
				rrch[j].setmRRCHTSindex(rrchConfigItem.getTsIndex());
			}
			item[i].setmAirlink_RRCH(rrch);

			// 设置RARCH
			List<SCGChannelConfigItem> rarchList = new ArrayList<SCGChannelConfigItem>();
			for (SCGChannelConfigItem obj : SCGChannelConfigItemList) {
				if (obj.getChannelType() == SCGChannelConfigItem.RARCH) {
					rarchList.add(obj);
				}
			}
			Airlink_RARCH rarch[] = new Airlink_RARCH[AirlinkConfig.RARCH_COUNT];
			for (int j = 0; j < rarch.length; j++) {
				rarch[j] = new Airlink_RARCH();
				rarch[j].setmRARCHSCGindex(0xff);
				rarch[j].setmRARCHTSindex(0xff);
			}
			for (int j = 0; j < rarchList.size() && j < rarch.length; j++) {
				SCGChannelConfigItem rarchConfigItem = rarchList.get(j);
				rarch[j].setmRARCHSCGindex(rarchConfigItem.getScgIndex());
				rarch[j].setmRARCHTSindex(rarchConfigItem.getTsIndex());
			}
			item[i].setmAirlink_RARCH(rarch);

			// 设置TCH
			List<SCGScaleConfigItem> SCGScaleConfigItemList = subScaleConfigDAO
					.queryByMoId(appendNbList.get(i).getMoId());
			for (SCGScaleConfigItem obj : SCGScaleConfigItemList) {
				if (obj.getTsIndex() == 0) {
					item[i].setmTCH_SCALE0(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 1) {
					item[i].setmTCH_SCALE1(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 2) {
					item[i].setmTCH_SCALE2(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 3) {
					item[i].setmTCH_SCALE3(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 4) {
					item[i].setmTCH_SCALE4(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 5) {
					item[i].setmTCH_SCALE5(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 6) {
					item[i].setmTCH_SCALE6(obj.getTchScale() * 32767 / 640000);
				}
			}
			// 直放站数目先设为0
			item[i].setmRepeaternumber(0);
		}
		appendNbMessage.setItem(item);

		return appendNbMessage;
	}
	
	
	/**
	 * 构造PECCH切换附加邻站表配置请求
	 * @param appendNbList
	 * @return
	 */
	private AppendNeighborPECCHMessage fillPECCHAppendNbMessage(List<McBts> appendNbList) throws Exception{

		AirlinkService airlinkService = AppContext.getCtx().getBean(
				AirlinkService.class);
		
		AppendNeighborPECCHMessage appendNbPECCHMessage = new AppendNeighborPECCHMessage();
		
		//ConfigFlag, 目前默认填0
		appendNbPECCHMessage.setConfigFlag(0);

		// mAppendNeighborBTSnumber
		appendNbPECCHMessage.setmAppendNeighborBTSnumber(appendNbList.size());
		
		//BTS_INFO_IE2
		BtsNeighborPECCHITtem[] items = new BtsNeighborPECCHITtem[appendNbList.size()];
		
		for (int i = 0; i < appendNbList.size(); i++) {
			items[i] = new BtsNeighborPECCHITtem();
			
			TConfRemoteBts remoteBts = remoteBtsDAO.queryByMoId(appendNbList
					.get(i).getMoId());
			if (remoteBts != null
					&& remoteBts.getFlag() == TConfRemoteBts.FLAG_SUPPORT) {
				// 设置btsMask
				appendNbPECCHMessage.setAllNeighbourBtsMask(i);
			}

			// 设置btsId
			items[i].setmBTSID(appendNbList.get(i).getBtsId());

			// 设置btsIp
			items[i].setBtsIp(appendNbList.get(i).getBtsIp());

			RFConfig rfConfig = rfConfigDAO.queryByMoId(appendNbList.get(i)
					.getMoId());
			// 设置Frequencyindex
			items[i].setmFrequencyindex(rfConfig.getFreqOffset());
			// 设置TRANSMIT_PWR
			items[i].setmTRANSMIT_PWR(rfConfig.getAntennaPower());
			// 设置 RECEIVE_SENSITIVITY
			items[i].setmRECEIVE_SENSITIVITY(rfConfig.getRxSensitivity());

			// 设置m N_ANT
			L1GeneralSetting l1GeneralSetting = l1generalSettingDAO
					.queryByMoId(appendNbList.get(i).getMoId());
			int inum = 0;
			for (int j = 31; j >= 0; j--) {
				if (((1 << j) & l1GeneralSetting.getAntennaMask().intValue()) != 0) {
					inum++;
				}
			}
			items[i].setmN_ANT(inum);

			AirlinkConfig airlinkConfig = airlinkService.queryByMoId(appendNbList.get(i).getMoId());

			// 设置SequenceID
			items[i].setmSequenceID(airlinkConfig.getSequenceId());
			// 设置SubcarriergroupMask
			items[i].setmSubcarriergroupMask(airlinkConfig.getScgMask());
			// 设置N_TS
			items[i].setmN_TS(airlinkConfig.getTotalTS());
			// 设置N_DN_TS
			items[i].setmN_DN_TS(airlinkConfig.getDownlinkTS());
			// 设置MAX_SCALE
			items[i].setmMAX_SCALE(airlinkConfig.getMaxScale() * 32767 / 640000);
			// 设置PREAMBLE_SCALE
			items[i].setmPREAMBLE_SCALE(airlinkConfig.getPreambleScale() * 32767 / 640000);
			
			//PECCH_Config
			PECCHSetting pecch = airlinkConfig.getPecchSetting();
			PECCHConfig mPECCHConfig = new PECCHConfig();
			if (pecch != null) {
				//SCG_IDX
				mPECCHConfig.setmSCG_IDX(pecch.getScgIndex());
				//N_PCH_SET
				mPECCHConfig.setmN_PCH_SET(pecch.getPchCount());
				//PCH_ID
				mPECCHConfig.setmPCH_ID(pecch.getPchIndex());
				//N_RARCH
				mPECCHConfig.setmN_RARCH(pecch.getRarchCount());
				//N_RRCH
				mPECCHConfig.setmN_RRCH(pecch.getRrchCount());
			}
			items[i].setmPECCHConfig(mPECCHConfig);
			
			// 设置TCH
			List<SCGScaleConfigItem> SCGScaleConfigItemList = subScaleConfigDAO
					.queryByMoId(appendNbList.get(i).getMoId());
			for (SCGScaleConfigItem obj : SCGScaleConfigItemList) {
				if (obj.getTsIndex() == 0) {
					items[i].setmTCH_SCALE0(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 1) {
					items[i].setmTCH_SCALE1(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 2) {
					items[i].setmTCH_SCALE2(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 3) {
					items[i].setmTCH_SCALE3(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 4) {
					items[i].setmTCH_SCALE4(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 5) {
					items[i].setmTCH_SCALE5(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 6) {
					items[i].setmTCH_SCALE6(obj.getTchScale() * 32767 / 640000);
				}
			}
			// 直放站数目先设为0
			items[i].setmRepeaternumber(0);

		}
		appendNbPECCHMessage.setItems(items);
		
		return appendNbPECCHMessage;
	}
	
	/**
	 * 构造邻站同频配置请求信息
	 * 
	 * @param neighbouList
	 * @return
	 */
	private NbSameFreqMessage fillNbSameFreqMessage(List<McBts> neighbouList) {
		NbSameFreqMessage nbSameFreqMessage = new NbSameFreqMessage();

		// 设置mNeighborBTSnumber
		nbSameFreqMessage.setmNeighborBTSnumber(neighbouList.size());

		BtsNeighbourItem item[] = new BtsNeighbourItem[neighbouList.size()];

		for (int i = 0; i < item.length; i++) {
			item[i] = new BtsNeighbourItem();

			// 设置btsId
			item[i].setmBTSID(neighbouList.get(i).getBtsId());

			// 设置btsIp
			item[i].setBtsIp(neighbouList.get(i).getBtsIp());

			RFConfig rfConfig = rfConfigDAO.queryByMoId(neighbouList.get(i)
					.getMoId());

			// 设置Frequencyindex
			item[i].setmFrequencyindex(rfConfig.getFreqOffset());
			// 设置TRANSMIT_PWR
			item[i].setmTRANSMIT_PWR(rfConfig.getAntennaPower());
			// 设置 RECEIVE_SENSITIVITY
			item[i].setmRECEIVE_SENSITIVITY(rfConfig.getRxSensitivity());

			// 设置mN_ANT
			L1GeneralSetting l1GeneralSetting = l1generalSettingDAO
					.queryByMoId(neighbouList.get(i).getMoId());
			int inum = 0;
			for (int j = 31; j >= 0; j--) {
				if (((1 << j) & l1GeneralSetting.getAntennaMask().intValue()) != 0) {
					inum++;
				}
			}
			item[i].setmN_ANT(inum);

			AirlinkConfig airlinkConfig = airlinkConfigDAO
					.queryByMoId(neighbouList.get(i).getMoId());

			// 设置SequenceID
			item[i].setmSequenceID(airlinkConfig.getSequenceId());
			// 设置SubcarriergroupMask
			item[i].setmSubcarriergroupMask(airlinkConfig.getScgMask());
			// 设置N_TS
			item[i].setmN_TS(airlinkConfig.getTotalTS());
			// 设置N_DN_TS
			item[i].setmN_DN_TS(airlinkConfig.getDownlinkTS());
			// 设置MAX_SCALE
			item[i].setmMAX_SCALE(airlinkConfig.getMaxScale() * 32767 / 640000);
			// 设置PREAMBLE_SCALE
			item[i].setmPREAMBLE_SCALE(airlinkConfig.getPreambleScale() * 32767 / 640000);

			List<SCGChannelConfigItem> SCGChannelConfigItemList = subChannelConfigDAO
					.queryByMoId(neighbouList.get(i).getMoId());

			// 设置BCH
			List<SCGChannelConfigItem> bchList = new ArrayList<SCGChannelConfigItem>();
			for (SCGChannelConfigItem obj : SCGChannelConfigItemList) {
				if (obj.getChannelType() == SCGChannelConfigItem.BCHN1) {
					bchList.add(obj);
				}
			}
			Airlink_BCH bch[] = new Airlink_BCH[AirlinkConfig.BCH_COUNT];
			for (int j = 0; j < bch.length; j++) {
				bch[j] = new Airlink_BCH();
				bch[j].setmBCHSCGindex(0xff);
				bch[j].setmBCHTSindex(0xff);
			}
			for (int j = 0; j < bchList.size() && j < bch.length; j++) {
				SCGChannelConfigItem bchConfigItem = bchList.get(j);
				bch[j].setmBCHSCGindex(bchConfigItem.getScgIndex());
				bch[j].setmBCHTSindex(bchConfigItem.getTsIndex());
			}
			item[i].setmAirlink_BCH(bch);

			// 设置RRCH
			List<SCGChannelConfigItem> rrchList = new ArrayList<SCGChannelConfigItem>();
			for (SCGChannelConfigItem obj : SCGChannelConfigItemList) {
				if (obj.getChannelType() == SCGChannelConfigItem.RRCHN1) {
					rrchList.add(obj);
				}
			}
			Airlink_RRCH rrch[] = new Airlink_RRCH[AirlinkConfig.RRCH_COUNT];
			for (int j = 0; j < rrch.length; j++) {
				rrch[j] = new Airlink_RRCH();
				rrch[j].setmRRCHSCGindex(0xff);
				rrch[j].setmRRCHTSindex(0xff);
			}
			for (int j = 0; j < rrchList.size() && j < rrch.length; j++) {
				SCGChannelConfigItem rrchConfigItem = rrchList.get(j);
				rrch[j].setmRRCHSCGindex(rrchConfigItem.getScgIndex());
				rrch[j].setmRRCHTSindex(rrchConfigItem.getTsIndex());
			}
			item[i].setmAirlink_RRCH(rrch);

			// 设置RARCH
			List<SCGChannelConfigItem> rarchList = new ArrayList<SCGChannelConfigItem>();
			for (SCGChannelConfigItem obj : SCGChannelConfigItemList) {
				if (obj.getChannelType() == SCGChannelConfigItem.RARCHN1) {
					rarchList.add(obj);
				}
			}
			Airlink_RARCH rarch[] = new Airlink_RARCH[AirlinkConfig.RARCH_COUNT];
			for (int j = 0; j < rarch.length; j++) {
				rarch[j] = new Airlink_RARCH();
				rarch[j].setmRARCHSCGindex(0xff);
				rarch[j].setmRARCHTSindex(0xff);
			}
			for (int j = 0; j < rarchList.size() && j < rarch.length; j++) {
				SCGChannelConfigItem rarchConfigItem = rarchList.get(j);
				rarch[j].setmRARCHSCGindex(rarchConfigItem.getScgIndex());
				rarch[j].setmRARCHTSindex(rarchConfigItem.getTsIndex());
			}
			item[i].setmAirlink_RARCH(rarch);

			// 设置TCH
			List<SCGScaleConfigItem> SCGScaleConfigItemList = subScaleConfigDAO
					.queryByMoId(neighbouList.get(i).getMoId());
			for (SCGScaleConfigItem obj : SCGScaleConfigItemList) {
				if (obj.getTsIndex() == 0) {
					item[i].setmTCH_SCALE0(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 1) {
					item[i].setmTCH_SCALE1(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 2) {
					item[i].setmTCH_SCALE2(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 3) {
					item[i].setmTCH_SCALE3(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 4) {
					item[i].setmTCH_SCALE4(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 5) {
					item[i].setmTCH_SCALE5(obj.getTchScale() * 32767 / 640000);
				}
				if (obj.getTsIndex() == 6) {
					item[i].setmTCH_SCALE6(obj.getTchScale() * 32767 / 640000);
				}
			}
			// 直放站数目先设为0
			item[i].setmRepeaternumber(0);
		}
		nbSameFreqMessage.setItem(item);

		return nbSameFreqMessage;
	}

	/**
	 * 构造邻站小包信道配置请求（基站还没有配置，所以不用发）
	 * 
	 * @param neighbourSamllPack
	 * @param neighbouList
	 */
	private NeighborSmallPack fillNbSmallPack(List<McBts> neighbouList) {

		NeighborSmallPack neighbourSamllPack = new NeighborSmallPack();

		// 设置邻接基站数目
		neighbourSamllPack.setmNeighborBTSnumber(neighbouList.size());

		NeighborChannelItem[] item = new NeighborChannelItem[neighbouList
				.size()];
		for (int i = 0; i < item.length; i++) {
			item[i] = new NeighborChannelItem();

			// BTS ID
			item[i].setmBtsId(neighbouList.get(i).getBtsId().longValue());

			List<SCGChannelConfigItem> SCGChannelConfigItemList = subChannelConfigDAO
					.queryByMoId(neighbouList.get(i).getMoId());

			// FACH
			ChannelItem fachs[] = new ChannelItem[NeighborChannelItem.CH_NUM];
			List<SCGChannelConfigItem> fachList = new ArrayList<SCGChannelConfigItem>();

			for (SCGChannelConfigItem obj : SCGChannelConfigItemList) {
				if (obj.getChannelType() == SCGChannelConfigItem.FACHN1) {
					fachList.add(obj);
				}
			}
			int fachIndex = 0;
			for (SCGChannelConfigItem fach : fachList) {
				fachs[fachIndex] = new ChannelItem();
				fachs[fachIndex].setSCGindex(fach.getScgIndex());
				fachs[fachIndex].setTSindex(fach.getTsIndex());
				fachIndex++;
			}
			for (; fachIndex < NeighborChannelItem.CH_NUM; fachIndex++) {
				fachs[fachIndex] = new ChannelItem();
				fachs[fachIndex].setSCGindex(0xff);
				fachs[fachIndex].setTSindex(0xff);
			}

			item[i].setFachs(fachs);

			// RPCH
			ChannelItem[] rpchs = new ChannelItem[NeighborChannelItem.CH_NUM];
			List<SCGChannelConfigItem> rpchList = new ArrayList<SCGChannelConfigItem>();

			for (SCGChannelConfigItem obj : SCGChannelConfigItemList) {
				if (obj.getChannelType() == SCGChannelConfigItem.RPCHN1) {
					rpchList.add(obj);
				}
			}
			int rpchIndex = 0;
			for (SCGChannelConfigItem rpch : rpchList) {
				rpchs[rpchIndex] = new ChannelItem();
				rpchs[rpchIndex].setSCGindex(rpch.getScgIndex());
				rpchs[rpchIndex].setTSindex(rpch.getTsIndex());
				rpchIndex++;
			}
			for (; rpchIndex < NeighborChannelItem.CH_NUM; rpchIndex++) {
				rpchs[rpchIndex] = new ChannelItem();
				rpchs[rpchIndex].setSCGindex(0xff);
				rpchs[rpchIndex].setTSindex(0xff);
			}

			item[i].setRpchs(rpchs);
		}

		neighbourSamllPack.setItems(item);
		return neighbourSamllPack;
	}

	/**
	 * 查询所有基站邻接表个数信息
	 */
	@Override
	public Map<Integer, String> queryNeighborCounts() {
		return neighborValidatorDAO.queryNeighborCounts();
	}

	/**
	 * 查询所有基站附加邻接表个数信息
	 */
	@Override
	public Map<Integer, String> queryAppendNeighborCounts() {
		return neighborValidatorDAO.queryAppendNeighborCounts();
	}

	/**
	 * 查询所有基站频点及前导序列号信息
	 */
	@Override
	public Map<String, Map<Integer, String>> queryBtsFreqSeqInfo() {
		return neighborValidatorDAO.queryBtsFreqSeqInfo();
	}

	@Override
	public Map<String, ArrayList<Long>> querySameFreqSeqInfo() {
		return neighborValidatorDAO.querySameFreqSeqInfo();
	}

	public void setNeighbourTableDAO(NeighbourDAO neighbourTableDAO) {
		this.neighbourDAO = neighbourTableDAO;
	}

	public void setRemoteBtsDAO(TConfRemoteBtsDAO remoteBtsDAO) {
		this.remoteBtsDAO = remoteBtsDAO;
	}

	public void setRfConfigDAO(TConfRfConfigDAO rfConfigDAO) {
		this.rfConfigDAO = rfConfigDAO;
	}

	public void setL1generalSettingDAO(
			TConfL1GeneralSettingDAO l1generalSettingDAO) {
		this.l1generalSettingDAO = l1generalSettingDAO;
	}

	public void setAirlinkConfigDAO(TConfAirlinkConfigDAO airlinkConfigDAO) {
		this.airlinkConfigDAO = airlinkConfigDAO;
	}

	public void setSubChannelConfigDAO(
			TConfSubChannelConfigDAO subChannelConfigDAO) {
		this.subChannelConfigDAO = subChannelConfigDAO;
	}

	public void setSubScaleConfigDAO(TConfSubScaleConfigDAO subScaleConfigDAO) {
		this.subScaleConfigDAO = subScaleConfigDAO;
	}

	public void setNeighbourDAO(NeighbourDAO neighbourDAO) {
		this.neighbourDAO = neighbourDAO;
	}

	public void setNeighbourProxy(NeighbourProxy neighbourProxy) {
		this.neighbourProxy = neighbourProxy;
	}

	public void setNeighborValidatorDAO(
			NeighborValidatorDAO neighborValidatorDAO) {
		this.neighborValidatorDAO = neighborValidatorDAO;
	}

	public void setAppendNeighborDAO(AppendNeighborDAO appendNeighborDAO) {
		this.appendNeighborDAO = appendNeighborDAO;
	}

	public void setNeighborFailedDAO(McbtsNeighborFailedDAO neighborFailedDAO) {
		this.neighborFailedDAO = neighborFailedDAO;
	}

}
