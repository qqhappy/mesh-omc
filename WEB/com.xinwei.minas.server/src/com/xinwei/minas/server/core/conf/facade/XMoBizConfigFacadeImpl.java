/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-20	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.facade;

import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.facade.conf.XMoBizConfigFacade;
import com.xinwei.minas.core.model.conf.KeyDesc;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.core.conf.service.XMoBizConfigService;
import com.xinwei.minas.server.core.conf.service.XMoBizConfigServiceFactory;
import com.xinwei.minas.server.enb.helper.EnbBizUniqueIdHelper;
import com.xinwei.minas.server.enb.service.EnbCellService;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XCollection;
import com.xinwei.omp.core.model.meta.XMetaRef;
import com.xinwei.omp.server.OmpAppContext;
import com.xinwei.omp.server.cache.XUIMetaCache;

/**
 * 
 * 通用网元配置门面
 * 
 * @author chenjunhua
 * 
 */

public class XMoBizConfigFacadeImpl extends UnicastRemoteObject implements
		XMoBizConfigFacade {
	
	private EnbCellService enbCellService;
	
	protected XMoBizConfigFacadeImpl() throws RemoteException {
		super();
	}
	
	@Override
	public XBizTable queryFromEms(Long moId, String bizName,
			XBizRecord condition) throws Exception {
		return getService(moId).queryFromEms(moId, bizName, condition);
	}
	
	@Override
	public XBizTable queryFromEms(Long moId, String tableName,
			XBizRecord condition, boolean queryStatus) throws Exception {
		return getService(moId).queryFromEms(moId, tableName, condition,
				queryStatus);
	}
	
	@Override
	public XBizTable queryFromNe(Long moId, String tableName) throws Exception {
		return getService(moId).queryFromNe(moId, tableName);
	}
	
	@Override
	public void add(Long moId, String bizName, XBizRecord bizRecord)
			throws Exception {
		getService(moId).add(moId, bizName, bizRecord);
	}
	
	@Override
	public void update(Long moId, String bizName, XBizRecord bizRecord)
			throws Exception {
		getService(moId).update(moId, bizName, bizRecord);
	}
	
	@Override
	public void delete(Long moId, String bizName, XBizRecord bizKey)
			throws Exception {
		getService(moId).delete(moId, bizName, bizKey);
	}
	
	@Override
	public List<KeyDesc> queryByMetaRef(Long moId, List<XMetaRef> metaRefList)
			throws Exception {
		return getService(moId).queryByMetaRef(moId, metaRefList);
	}
	
	@Override
	public Map<String, XCollection> queryUIMap(int moTypeId, int subTypeId)
			throws Exception {
		boolean configOk = XUIMetaCache.getInstance().isInitialized();
		if (!configOk) {
			throw new Exception(
					OmpAppContext.getMessage("enb_biz_config_not_initialized"));
		}
		int key = XUIMetaCache.getInstance().createKey(moTypeId, subTypeId);
		return XUIMetaCache.getInstance().getUiMap(key);
	}
	
	/**
	 * 根据moId获取指定的网元业务配置服务接口
	 * 
	 * @param moId
	 * @return
	 */
	private XMoBizConfigService getService(Long moId) {
		XMoBizConfigService service = XMoBizConfigServiceFactory.getInstance()
				.createXMoBizConfigService(moId);
		if (service == null) {
			throw new UnsupportedOperationException();
		}
		return service;
	}
	
	/**
	 * 根据网元类型moType获取指定的网元业务配置服务接口
	 * 
	 * @param moId
	 * @return
	 */
	private XMoBizConfigService getService(int moType) {
		XMoBizConfigService service = XMoBizConfigServiceFactory.getInstance()
				.createXMoBizConfigService(moType);
		if (service == null) {
			throw new UnsupportedOperationException();
		}
		return service;
	}
	
	@Override
	public InetSocketAddress queryEmsNetAddress(Long moId) throws Exception {
		return getService(moId).queryEmsNetAddress(moId);
	}
	
	@Override
	public Map<String, Map<String, Map<String, List<String>>>> getTableFieldLevelConfig(
			int moType) throws Exception {
		return getService(moType).getTableFieldLevelConfig(moType);
	}
	
	@Override
	public Map<String, Map<String, List<String>>> queryBizConfig(int moTypeId)
			throws Exception {
		return getService(moTypeId).queryBizConfig(moTypeId);
	}
	
	@Override
	public Map<Integer, List<String>> querySupportedProtocolVersion(int moTypeId)
			throws Exception {
		return getService(moTypeId).querySupportedProtocolVersion();
	}
	
	@Override
	public Map<Long, List<XBizTable>> queryDataByMoIdList(int moTypeId,
			List<Long> moIdList) throws Exception {
		return getService(moTypeId).queryDataByMoIdList(moIdList);
	}
	
	@Override
	public void compareAndSyncEmsDataToNe(Long moId) throws Exception {
		getService(moId).compareAndSyncEmsDataToNe(moId);
	}
	
	@Override
	public void compareAndSyncNeDataToEms(Long moId) throws Exception {
		getService(moId).compareAndSyncNeDataToEms(moId);
	}
	
	@Override
	public int getFreePci(int enbType, String protocolVersion) throws Exception {
		
		return EnbBizUniqueIdHelper.getFreeValue(enbType, protocolVersion,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
	}
	
	@Override
	public int getFreeRsi(int enbType, String protocolVersion) throws Exception {
		
		return EnbBizUniqueIdHelper.getFreeValue(enbType, protocolVersion,
				EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
				EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX);
	}
	
	public EnbCellService getEnbCellService() {
		return enbCellService;
	}
	
	public void setEnbCellService(EnbCellService enbCellService) {
		this.enbCellService = enbCellService;
	}
	
}
