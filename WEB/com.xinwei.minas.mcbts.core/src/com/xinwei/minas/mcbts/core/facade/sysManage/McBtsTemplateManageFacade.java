/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-6	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsOperation;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsOperation.Operation;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsTemplate;

/**
 * 
 * 基站模板管理的门面接口
 * 
 * 
 * @author tiance
 * 
 */

public interface McBtsTemplateManageFacade extends Remote {
	/**
	 * 获取所有基站同步的模板
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsTemplate> queryAll() throws RemoteException, Exception;

	/**
	 * 基于一个模板ID,返回一个新的可用的模板ID
	 * 
	 * @param referId
	 * @return
	 */
	@Loggable
	public Long applyNewId(OperObject operObject, Long referId,
			McBtsTemplate temp) throws RemoteException, Exception;

	/**
	 * 在数据库的每个表中生成模板备份数据
	 * 
	 * @param templateId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void generateTemplateBackup(long templateId) throws RemoteException,
			Exception;

	/**
	 * 在数据库的每个表中删除模板备份数据
	 * 
	 * @param templateId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void deleteTemplateBackup(long templateId) throws RemoteException,
			Exception;

	/**
	 * 通过moId查询一个模板
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsTemplate queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * 初始化基站数据
	 * 
	 * @param mcbts
	 * @param templateId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void initMcBtsData(McBts mcbts) throws RemoteException, Exception;

	/**
	 * 同步一批基站
	 * 
	 * @param templateId
	 * @param oprs
	 * @param mcbts
	 */
	@Loggable
	public void syncAll(OperObject operObject, long templateId,
			Operation[] oprs, McBts[] mcbts) throws RemoteException, Exception;

	/**
	 * 删除基站的初始化数据
	 * 
	 * @param mcbts
	 */
	public void rollBackMcBtsData(long moId) throws RemoteException, Exception;

	/**
	 * 获得基站业务模型
	 * 
	 * @return
	 */
	public List<McBtsOperation> getMcbtsOperation() throws RemoteException;

	/**
	 * 向MoCache中添加Mo
	 * 
	 * @param mo
	 */
	public void addToMoCache(Long moId) throws RemoteException;

	/**
	 * 从MoCache中删除Mo
	 * 
	 * @param moId
	 */
	@Loggable
	public void removeFromMoCache(OperObject operObject, Long moId)
			throws RemoteException;

	/**
	 * 更新模板数据
	 * 
	 * @param template
	 * @throws RemoteException
	 */
	@Loggable
	public void updateTemplate(OperObject operObject, McBtsTemplate template)
			throws RemoteException;

	/**
	 * 在取消模板操作或者删除业务的时候执行恢复操作
	 */
	public void recover(Long moId, List<String> operations, boolean isDel)
			throws RemoteException;
}
