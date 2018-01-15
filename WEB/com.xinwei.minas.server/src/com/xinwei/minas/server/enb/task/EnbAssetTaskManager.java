/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-19	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbAsset;
import com.xinwei.minas.enb.core.model.EnbAssetCondition;
import com.xinwei.minas.enb.core.model.EnbAssetHistory;
import com.xinwei.minas.server.core.conf.net.model.CompositeValue;
import com.xinwei.minas.server.enb.dao.EnbAssetDAO;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbConnector;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.net.TagConst;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * �ʲ���Ϣ�ϱ���������
 * 
 * @author chenlong
 * 
 */

public class EnbAssetTaskManager {
	
	private Log log = LogFactory.getLog(EnbAssetTaskManager.class);
	
	private static final EnbAssetTaskManager instance = new EnbAssetTaskManager();
	
	public static EnbAssetTaskManager getInstance() {
		return instance;
	}
	
	// �ʲ���Ϣ�ϱ���Ϣ����
	private BlockingQueue<EnbAppMessage> tasks = new LinkedBlockingDeque<EnbAppMessage>();
	
	private int RESULT_SUCCESS = 0;
	
	private int RESULT_FAIL = 1;
	
	public EnbAssetTaskManager() {
		// ������Ϣ�����߳�
		startTask();
	}
	
	/**
	 * ���������ʲ���Ϣ���е��߳�
	 */
	private void startTask() {
		Thread thread = new Thread(new EnbAssetTask());
		thread.start();
	}
	
	/**
	 * ������в���һ���ϱ���Ϣ
	 * 
	 * @param message
	 * @throws InterruptedException 
	 */
	public void addMessage(EnbAppMessage message) throws InterruptedException {
		tasks.put(message);
	}
	
	/**
	 * �Ӷ������ó�һ����Ϣ
	 * 
	 * @return
	 * @throws InterruptedException 
	 */
	public EnbAppMessage getMessage() throws InterruptedException {
		return tasks.take();
	}
	
	/**
	 * ����Ϣ������в��Ҹ���վ��Ӧ
	 */
	public void handler(EnbAppMessage message) {
		try {
			long enbId = message.getEnbId();
			Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
			if (enb == null) {
				// ���վ����ʧ��
				sendResponse(message, RESULT_FAIL);
			}
			// ����Ϣ���뵽���������
			addMessage(message);
			// ���վ���سɹ�
			sendResponse(message, RESULT_SUCCESS);
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("EnbAssetTaskManager handler error.", e);
			sendResponse(message, RESULT_FAIL);
		}
	}
	
	/**
	 * ���վ������Ӧ
	 * 
	 * @param message
	 */
	public void sendResponse(EnbAppMessage req, int result) {
		EnbAppMessage resp = new EnbAppMessage();
		resp.setEnbId(req.getEnbId());
		resp.setTransactionId(req.getTransactionId());
		resp.setMa(req.getMa());
		resp.setMoc(req.getMoc());
		resp.setActionType(req.getActionType());
		resp.setMessageType(EnbMessageConstants.MESSAGE_RESPONSE);
		resp.addTagValue(TagConst.RESULT, result);
		EnbConnector enbConnector = getEnbConnector();
		if (enbConnector != null) {
			try {
				enbConnector.asyncInvoke(resp);
			}
			catch (Exception e) {
			}
		}
	}
	
	/**
	 * ������Ϣ���Ҵ�����Ӧ��ҵ���߼�
	 */
	public synchronized void process(EnbAppMessage message) {
		if(null == message) {
			return ;
		}
		long enbId = message.getEnbId();
		try {
			log.debug("AssetInfo Notice start:transactionId="+message.getTransactionId() + ",enbId="+enbId);
			List<EnbAsset> oldAssets = EnbCache.getInstance()
					.getAssetListByEnbId(enbId);
			log.debug("Old assets size = " + oldAssets.size());
			List<EnbAsset> newAssets = parseMessage(message, enbId);
			log.debug("New assets size = " + newAssets.size());
			for (EnbAsset newAsset : newAssets) {
				log.debug("Process a new asset, hardVersion="
						+ newAsset.getHardwareVersion() + ",productionSN="
						+ newAsset.getProductionSN());
				boolean flag = false;
				for (EnbAsset oldAsset : oldAssets) {
					// �����վ�ϱ����ʲ��Ѿ��������д���,���бȽ��޸Ļ��߲�����
					if (newAsset.getProductionSN().equals(
							oldAsset.getProductionSN())) {
						// �ԱȲ����Ƿ��޸�
						if (isModify(oldAsset, newAsset)) {
							// ����޸���������ݿ�ͻ���
							updateAsset(oldAsset);
							log.debug("Find the same,and was modified.");
							// ȷ���ʲ������������
							confirmAssetUpdateData(enbId, oldAsset.getProductionSN());
						}
						// �Ѿ���������ʲ��Ӵ����б���ɾ��
						oldAssets.remove(oldAsset);
						flag = true;
						break;
					}
				}
				// �����վ�ϱ����ʲ������в�����,������������
				if (!flag) {
					newAsset.setStartTime(EnbAsset.getLongFromDate(new Date()));
					newAsset.setStatus(EnbAsset.ASSET_STATUS_NORMAL);
					addAsset(newAsset);
					log.debug("Dont find the same,and add.");
					confirmAssetUpdateData(enbId, newAsset.getProductionSN());
				}
			}
			// �����վԭ���е��Ǳ���δ�ϱ����ʲ�
			for (EnbAsset enbAsset : oldAssets) {
				log.debug("Process a old asset,and hardVersion="
						+ enbAsset.getHardwareVersion() + ",productionSN="
						+ enbAsset.getProductionSN());
				if(EnbAsset.ASSET_STATUS_NORMAL == enbAsset.getStatus()) {
					// ��ͣ�ʲ�
					stopAsset(enbAsset.getEnbId(), enbAsset.getProductionSN());
					log.debug("Asset is normal,and stop.");
					// ȷ���ʲ������������
					confirmAssetUpdateData(enbId, enbAsset.getProductionSN());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("EnbAssetTaskManager process error.", e);
		}
		log.debug("AssetInfo Notice end:transactionId="+message.getTransactionId() + ",enbId="+enbId);
	}
	
	
	public void confirmAssetUpdateData(long enbId,String productionSN) throws Exception {
		EnbAssetDAO enbAssetDAO = AppContext.getCtx().getBean(
				EnbAssetDAO.class);
		EnbAssetCondition enbAssetCondition = new EnbAssetCondition();
		enbAssetCondition.setEnbId(enbId);
		enbAssetCondition.setProductionSN(productionSN);
		PagingData<EnbAsset> assetPageDate = enbAssetDAO.queryByCondition(enbAssetCondition);
		EnbAsset enbAsset = assetPageDate.getResults().get(0);
		EnbAsset cacheAsset = EnbCache.getInstance().queryAssetByEnbAndProdSN(enbId, productionSN);
		try {
			log.debug("cacheAsset="+cacheAsset.toString());
			log.debug("enbAsset="+enbAsset.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * �����ʲ���Ϣ
	 * 
	 * @param enbAsset
	 */
	public void updateAsset(EnbAsset asset) throws Exception {
		// �������ݿ�
		EnbAssetDAO enbAssetDAO = AppContext.getCtx()
				.getBean(EnbAssetDAO.class);
		enbAssetDAO.update(asset);
		// ���»���
		EnbCache.getInstance().addOrUpdateAsset(asset.getEnbId(), asset);
	}
	
	/**
	 * �Աȱ����ϱ����ʲ���Ϣ�Ƿ��޸�,����޸�,���������޸ĺ󷵻�
	 * 
	 * @param oldAsset
	 * @param newAsset
	 * @return
	 */
	public boolean isModify(EnbAsset oldAsset, EnbAsset newAsset)
			throws Exception {
		boolean result = false;
		// �ж��ʲ��Ƿ�����ͣ�������
		if (EnbAsset.ASSET_STATUS_SUSPEND == oldAsset.getStatus()) {
			oldAsset.setStatus(EnbAsset.ASSET_STATUS_NORMAL);
			oldAsset.setStopTime(0);
			result = true;
		}
		// �ж�λ����Ϣ�Ƿ������
		if (!oldAsset.getLocationInfo().equals(newAsset.getLocationInfo())) {
			oldAsset.setLocationInfo(newAsset.getLocationInfo());
			result = true;
		}
		return result;
	}
	
	/**
	 * ��ͣһ���ʲ�
	 * 
	 * @param productionSN
	 */
	public void stopAsset(long enbId, String productionSN) throws Exception {
		// ��ѯ�����ʲ���Ϣ
		EnbAsset asset = EnbCache.getInstance().queryAssetByEnbAndProdSN(enbId,
				productionSN);
		if (null != asset) {
			asset.setStatus(EnbAsset.ASSET_STATUS_SUSPEND);
			asset.setStopTime(EnbAsset.getLongFromDate(new Date()));
			// �������ݿ�
			EnbAssetDAO enbAssetDAO = AppContext.getCtx().getBean(
					EnbAssetDAO.class);
			enbAssetDAO.update(asset);
			// ���»���
			EnbCache.getInstance().addOrUpdateAsset(enbId, asset);
		}
	}
	
	/**
	 * �¼�һ���ʲ�
	 * 
	 * @param asset
	 * @throws Exception
	 */
	public void addAsset(EnbAsset asset) throws Exception {
		// �������ݿ�
		EnbAssetDAO enbAssetDAO = AppContext.getCtx()
				.getBean(EnbAssetDAO.class);
		enbAssetDAO.add(asset);
		EnbAssetCondition enbAssetCondition = new EnbAssetCondition();
		enbAssetCondition.setEnbId(asset.getEnbId());
		enbAssetCondition.setProductionSN(asset.getProductionSN());
		PagingData<EnbAsset> assetPageDate = enbAssetDAO.queryByCondition(enbAssetCondition);
		EnbAsset enbAsset = assetPageDate.getResults().get(0);
		asset.setId(enbAsset.getId());
		// ���»���
		EnbCache.getInstance().addOrUpdateAsset(asset.getEnbId(), asset);
	}
	
	/**
	 * ɾ����վ����ʲ����д���
	 * @param enb
	 * @throws Exception
	 */
	public void deleteEnb(Enb enb) throws Exception {
		EnbAssetDAO enbAssetDAO = OmpAppContext.getCtx().getBean(EnbAssetDAO.class);
		List<EnbAsset> enbAssets = EnbCache.getInstance().getAssetListByEnbId(enb.getEnbId());
		for (EnbAsset enbAsset : enbAssets) {
			EnbAssetHistory assetHistory = getHistoryAsset(enbAsset);
			if(null == assetHistory) {
				continue;
			}
			EnbAsset asset = enbAssetDAO.queryById(assetHistory.getId());
			if (null != asset) {
				// ����ʷ��������һ���¼�¼
				enbAssetDAO.addHistory(assetHistory);
				// ɾ���ʲ����м�¼
				enbAssetDAO.delete(asset);
				// ɾ�������¼
				EnbCache.getInstance().deleteAsset(asset.getEnbId(),
						asset.getProductionSN());
			}
		}
		// ����ظ����쳣����
		enbAssetDAO.deleteEnbAll(enb.getEnbId());
		
	}
	
	public EnbAssetHistory getHistoryAsset(EnbAsset enbAsset){
		if(null == enbAsset) {
			return null;
		}
		EnbAssetHistory assetHistory = new EnbAssetHistory();
		assetHistory.setId(enbAsset.getId());
		assetHistory.setEnbId(enbAsset.getEnbId());
		assetHistory.setHardwareVersion(enbAsset.getHardwareVersion());
		assetHistory.setLastServeTime(enbAsset.getLastServeTime());
		assetHistory.setLocationInfo(enbAsset.getLocationInfo());
		assetHistory.setManufactureDate(enbAsset.getManufactureDate());
		assetHistory.setNodeType(enbAsset.getNodeType());
		assetHistory.setProductionSN(enbAsset.getProductionSN());
		assetHistory.setProviderName(enbAsset.getProviderName());
		assetHistory.setRemark(enbAsset.getRemark());
		assetHistory.setStartTime(enbAsset.getStartTime());
		assetHistory.setStatus(enbAsset.getStatus());
		assetHistory.setStopTime(enbAsset.getStopTime());
		assetHistory.setConfirmUser("system");
		assetHistory.setConfirmStopTime(EnbAsset.getLongFromDate(new Date()));
		return assetHistory;
	}
	
	/**
	 * ��message�����ɶ���
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<EnbAsset> parseMessage(EnbAppMessage message, long enbId)
			throws Exception {
		List<EnbAsset> enbAssets = new ArrayList<EnbAsset>();
		List<Object> list = message.getListValue(TagConst.ASSETS_INFO_NOTIFY);
		for (Object object : list) {
			EnbAsset enbAsset = new EnbAsset();
			CompositeValue compositeValue = (CompositeValue) object;
			enbAsset.setProductionSN(compositeValue
					.getStringValue(TagConst.BBU_PRODUCTION_SN));
			if(null == enbAsset.getProductionSN() || "".equals(enbAsset.getProductionSN())) {
				enbAsset.setProductionSN("empty");
			}
			enbAsset.setHardwareVersion(compositeValue
					.getStringValue(TagConst.BBU_HARDWARE_VERSION));
			enbAsset.setNodeType(compositeValue
					.getIntValue(TagConst.ASSET_NODE_TYPE));
			byte[] byteLocation = compositeValue
					.getByteValue(TagConst.ASSET_LOACTION_INFO);
			enbAsset.setLocationInfo(EnbAsset.getStringLocation(byteLocation));
			enbAsset.setProviderName(compositeValue
					.getStringValue(TagConst.ASSET_PROVIDER_NAME));
			enbAsset.setManufactureDate(compositeValue
					.getDateValue(TagConst.ASSET_MANUFACTURE_DATE));
			enbAsset.setEnbId(enbId);
			enbAssets.add(enbAsset);
		}
		return enbAssets;
	}
	
	private EnbConnector getEnbConnector() {
		return OmpAppContext.getCtx().getBean(EnbConnector.class);
	}
}
