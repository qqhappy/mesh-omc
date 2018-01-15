/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-20	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.micro.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.core.model.EnbCellStart;
import com.xinwei.minas.core.model.EnbSceneDataShow;
import com.xinwei.minas.micro.core.facade.XMicroBizConfigFacade;
import com.xinwei.minas.server.micro.microapi.MicroEnbSceneDataManager;
import com.xinwei.minas.server.micro.service.MicroEnbCellService;
import com.xinwei.minas.server.micro.service.XMicroBizConfigService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizSceneTable;

/**
 * 
 * 通用网元配置门面
 * 
 * @author chenjunhua
 * 
 */

public class XMicroBizConfigFacadeImpl extends UnicastRemoteObject implements
		XMicroBizConfigFacade {
	
	private MicroEnbCellService microEnbCellService;
	
	protected XMicroBizConfigFacadeImpl() throws RemoteException {
		super();
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
	
	/**
	 * 根据moId获取指定的网元业务配置服务接口
	 * 
	 * @param moId
	 * @return
	 */
	private XMicroBizConfigService getService(Long moId) {
		return AppContext.getCtx().getBean(XMicroBizConfigService.class);
	}
	
	/**
	 * 根据网元类型moType获取指定的网元业务配置服务接口
	 * 
	 * @param moId
	 * @return
	 */
	private XMicroBizConfigService getService(int moType) {
		return AppContext.getCtx().getBean(XMicroBizConfigService.class);
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
	public void addSence(long moId, EnbCellStart enbCellStart) throws Exception {
		microEnbCellService.add(moId, enbCellStart);
	}
	
	@Override
	public XBizSceneTable querySceneData(Long moId) throws Exception {
		return microEnbCellService.queryByMoId(moId);
	}
	
	@Override
	public void deleteScene(Long moId, int cid) throws Exception {
		microEnbCellService.delete(moId, cid);
	}
	
	@Override
	public void updateSence(long moId, EnbCellStart enbCellStart)
			throws Exception {
		microEnbCellService.update(moId, enbCellStart);
	}
	
	@Override
	public XBizRecord querySceneDataByCid(Long moId, int cid) throws Exception {
		return microEnbCellService.queryByCid(moId, cid);
	}
	
	public void setMicroEnbCellService(MicroEnbCellService microEnbCellService) {
		this.microEnbCellService = microEnbCellService;
	}
	
	@Override
	public EnbSceneDataShow querySceneDataShow() throws Exception {
		return MicroEnbSceneDataManager.getInstance().getEnbSceneDataShow();
	}
}
