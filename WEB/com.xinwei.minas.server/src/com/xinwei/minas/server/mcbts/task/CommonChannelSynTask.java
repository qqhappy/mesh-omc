/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-6	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.xinwei.minas.core.exception.NeighborException;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.CommonChannelSynInfo;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationDataInfo;
import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;
import com.xinwei.minas.server.mcbts.dao.common.CommonChannelSynDAO;
import com.xinwei.minas.server.mcbts.helper.McBtsAlarmHelper;
import com.xinwei.minas.server.mcbts.service.McBtsBizService;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer1.CalibrationDataService;
import com.xinwei.minas.server.mcbts.service.layer2.AirlinkService;
import com.xinwei.minas.server.mcbts.service.layer3.MBMSBtsService;
import com.xinwei.minas.server.mcbts.service.layer3.NeighborService;
import com.xinwei.minas.server.mcbts.service.layer3.RemoteBtsService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 网归信息同步线程
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class CommonChannelSynTask {
	
	private CommonChannelSynDAO mChannelSynDAO;
	
	private McBtsAlarmHelper mMcBtsAlarmHelper;
	
	private final int max_syn_times = 6; 
	
	private static final CommonChannelSynTask instance = new CommonChannelSynTask();
	
	private CommonChannelSynTask() {
		initTask();
	}
	
	public static CommonChannelSynTask getInstance() {
		return instance;
	}
	
	public void setmMcBtsAlarmHelper(McBtsAlarmHelper mMcBtsAlarmHelper) {
		this.mMcBtsAlarmHelper = mMcBtsAlarmHelper;
	}	
	
	public void setmChannelSynDAO(CommonChannelSynDAO mChannelSynDAO) {
		this.mChannelSynDAO = mChannelSynDAO;
	}
	
	public void initTask() {
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new SynTask(), 1, 1, TimeUnit.HOURS);
	}
	
	private class SynTask implements Runnable{
		
		public void run() {
			doWork();
		}
		
		private void doWork() {
			List<CommonChannelSynInfo> infos  = mChannelSynDAO.queryAll();
			if (infos != null) {
				for (CommonChannelSynInfo sInfo : infos) {
					int synTimes = sInfo.getSynTimes() + 1;
					sInfo.setSynTimes(synTimes);
					synConfig(sInfo);
				}
			}
		}
		
		/**
		 * 对基站进行网归信息配置
		 * @param synInfo
		 */
		private void synConfig(CommonChannelSynInfo synInfo){
			long moId = synInfo.getMoId();
			McBts bts = McBtsCache.getInstance().queryByMoId(moId);
			if (bts == null) {
				mChannelSynDAO.delete(synInfo);
				return;
			}
			boolean impAirlinkSuccess = false;
			boolean impCalibrationDataSuccess = false;
			boolean impNeighborSuccess = false;
			boolean impClusterSwitchSuccess = false;
			boolean impMbmsSwitchSuccess = false;
			boolean imRemoteSwitchSuccess = false;
			StringBuilder synMessage = new StringBuilder();
			try {
				configAirlink(moId);
				impAirlinkSuccess = true;
			} catch (Exception e) {
				impAirlinkSuccess = false;
				synMessage.append(OmpAppContext
						.getMessage("commonchannel_airlink_config_failed")
						+ ":"
						+ e.getMessage());
			}
			
			try {
				configCalibrationDataInfo(moId);
				impCalibrationDataSuccess = true;
			} catch (Exception e) {
				impCalibrationDataSuccess = false;
				synMessage.append(OmpAppContext
						.getMessage("commonchannel_calibrationData_config_failed")
						+ ":"
						+ e.getMessage());
			}
			
			try {
				configNeighborList(moId);
				impNeighborSuccess = true;
			}catch (NeighborException e) {
				impNeighborSuccess = false;
				synMessage.append(OmpAppContext
						.getMessage("commonchannel_neighborlist_config_failed")
						+ ":" + e.getErrorMessage());
			}catch (Exception e) {
				impNeighborSuccess = false;
				synMessage.append(OmpAppContext
						.getMessage("commonchannel_neighborlist_config_failed")
						+ ":" + e.getMessage());
			}
			
			try {
				configClusterSwitch(moId);
				impClusterSwitchSuccess = true;
			} catch (Exception e) {
				impClusterSwitchSuccess = false;
				synMessage.append(OmpAppContext
						.getMessage("commonchannel_trunk_config_failed")
						+ ":" + e.getMessage());
			}
			
			try {
				configMbmsSwitch(moId);
				impMbmsSwitchSuccess = true;
			} catch (Exception e) {
				impMbmsSwitchSuccess = false;
				synMessage.append(OmpAppContext
						.getMessage("commonchannel_mbms_config_failed")
						+ ":" + e.getMessage());
			}
			
			try {
				configRemoteSwitch(moId);
				imRemoteSwitchSuccess = true;
			} catch (Exception e) {
				imRemoteSwitchSuccess = false;
				synMessage.append(OmpAppContext
						.getMessage("commonchannel_remote_config_failed")
						+ ":" + e.getMessage());
			}
			if (impAirlinkSuccess && impCalibrationDataSuccess
					&& impNeighborSuccess && impClusterSwitchSuccess
					&& impMbmsSwitchSuccess && imRemoteSwitchSuccess) {
				// 同步成功后从失败列表中删除
				mChannelSynDAO.delete(synInfo);
			} else {
				//同步次数达到最大值，则删除该内容并产生告警
				if (synInfo.getSynTimes() == max_syn_times) {
					mChannelSynDAO.delete(synInfo);
					//产生告警
					String alram = OmpAppContext.getMessage("commonchannel_config_failed", new Object[]{synMessage.toString()});
					mMcBtsAlarmHelper.fireImportCommonChannelFailedAlarm(bts, alram);
				} else {
					//更新数据库
					mChannelSynDAO.saveOrUpdate(synInfo);
				}
			}
		}
		
		
		/**
		 * 配置空中链路
		 * @param moId
		 * @throws Exception
		 */
		private void configAirlink(long moId) throws Exception {
			AirlinkService mAirlinkService = AppContext.getCtx().getBean(AirlinkService.class);
			AirlinkConfig config = mAirlinkService.queryByMoId(moId);
			mAirlinkService.config(moId, config, true);
		}

	
		/**
		 * 配置校准数据
		 * @param moId
		 * @throws Exception
		 */
		private void configCalibrationDataInfo(long moId)
				throws Exception {
			CalibrationDataService service = AppContext.getCtx().getBean(CalibrationDataService.class);
			CalibrationDataInfo config = service.queryCalibrationDataConfigByMoId(moId);
			service.config(moId, config, true);
		}
		
	
		/**
		 * 配置邻接表
		 * @param moId
		 * @throws Exception
		 */
		private void configNeighborList(long moId) throws Exception {
			NeighborService service = AppContext.getCtx().getBean(
					NeighborService.class);
			List<McBts> neighborList = service.queryNeighbor(moId);
			List<McBts> appendNeighborList = service.queryAppendNeighbor(moId);
			try {
				service.config(moId, neighborList, appendNeighborList);
			} catch (NeighborException e) {
				if (!"".equals(e.getErrorMessage())) {
					throw e;
				}
			}
			
		}
		
		/**
		 * 集群配置
		 * @param moId
		 * @throws Exception
		 */
		private void configClusterSwitch(long moId) throws Exception {
			McBtsBizService service =  AppContext.getCtx().getBean(
					McBtsBizService.class);
			GenericBizData data = new GenericBizData("mcbts_trunkConfig");
			data = service.queryAllBy(moId, data.getBizName());
			service.config( moId, data);
		}
		
		/**
		 * 同播配置
		 * @param moId
		 * @throws Exception
		 */
		private void configMbmsSwitch(long moId) throws Exception {
			MBMSBtsService service = AppContext.getCtx().getBean(
					MBMSBtsService.class);
			TConfMBMSBts data = service.queryByMoId(moId);
			service.config(data);
		}
		
		/**
		 * 远距离基站配置
		 * @param moId
		 * @throws Exception
		 */
		private void configRemoteSwitch(long moId) throws Exception {
			RemoteBtsService service = AppContext.getCtx().getBean(
					RemoteBtsService.class);
			TConfRemoteBts data = service.queryByMoId(moId);
			service.config(data, false);
		}
		
	}
	
}
