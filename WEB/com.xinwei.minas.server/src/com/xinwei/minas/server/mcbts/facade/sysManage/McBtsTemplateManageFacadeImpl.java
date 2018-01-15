/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-6	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.McBtsTemplateManageFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsOperation;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsOperation.Operation;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsTemplate;
import com.xinwei.minas.server.mcbts.service.sysManage.McBtsTemplateManageService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 基站模板管理的门面
 * 
 * 
 * @author tiance
 * 
 */

@SuppressWarnings("serial")
public class McBtsTemplateManageFacadeImpl extends UnicastRemoteObject
		implements McBtsTemplateManageFacade {

	private final McBtsTemplateManageService service;

	protected McBtsTemplateManageFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(McBtsTemplateManageService.class);
	}

	/**
	 * 获取所有基站同步的模板
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public List<McBtsTemplate> queryAll() throws RemoteException, Exception {
		return service.queryAll();
	}

	/**
	 * 基于一个模板ID,返回一个新的可用的模板ID,并迁移模板ID的数据给新的模板ID
	 * 
	 * @param referId
	 * @return
	 */
	@Override
	public Long applyNewId(OperObject operObject, Long referId,
			McBtsTemplate temp) throws RemoteException, Exception {
		return service.applyNewId(referId, temp);
	}

	@Override
	public void generateTemplateBackup(long templateId) throws RemoteException,
			Exception {
		service.generateTemplateBackup(templateId);
	}

	@Override
	public void deleteTemplateBackup(long templateId) throws RemoteException,
			Exception {
		service.deleteTemplateBackup(templateId);
	}

	/**
	 * 通过moId查询一个模板
	 * 
	 * @param moId
	 * @return
	 */
	@Override
	public McBtsTemplate queryByMoId(Long moId) throws RemoteException,
			Exception {
		return service.queryByMoId(moId);
	}

	/**
	 * 初始化基站数据
	 * 
	 * @param mcbts
	 * @param templateId
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public void initMcBtsData(McBts mcbts) throws RemoteException, Exception {
		service.initMcBtsData(mcbts);
	}

	/**
	 * 同步一批基站
	 * 
	 * @param templateId
	 * @param oprs
	 * @param mcbts
	 */
	@Override
	public void syncAll(OperObject operObject, long templateId,
			Operation[] oprs, McBts[] mcbts) throws RemoteException, Exception {
		service.syncAll(templateId, oprs, mcbts);
	}

	/**
	 * 删除基站的初始化数据
	 * 
	 * @param mcbts
	 */
	@Override
	public void rollBackMcBtsData(long moId) throws RemoteException, Exception {
		service.rollBackMcBtsData(moId);

	}

	/**
	 * 获得基站业务模型
	 * 
	 * @return
	 */
	public List<McBtsOperation> getMcbtsOperation() {
		return service.getMcbtsOperation();
	}

	/**
	 * 向MoCache中添加Mo
	 * 
	 * @param mo
	 */
	public void addToMoCache(Long moId) {
		service.addToMoCache(moId);
	}

	/**
	 * 从MoCache中删除Mo
	 * 
	 * @param moId
	 */
	public void removeFromMoCache(OperObject operObject, Long moId) {
		service.removeFromMoCache(moId);
	}

	@Override
	public void updateTemplate(OperObject operObject, McBtsTemplate template)
			throws RemoteException {
		service.updateTemplate(template);
	}

	@Override
	public void recover(Long moId, List<String> operations, boolean isDel)
			throws RemoteException {
		service.recover(moId, operations, isDel);

	}

}
