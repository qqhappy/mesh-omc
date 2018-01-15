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
 * ��վģ����������ӿ�
 * 
 * 
 * @author tiance
 * 
 */

public interface McBtsTemplateManageFacade extends Remote {
	/**
	 * ��ȡ���л�վͬ����ģ��
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsTemplate> queryAll() throws RemoteException, Exception;

	/**
	 * ����һ��ģ��ID,����һ���µĿ��õ�ģ��ID
	 * 
	 * @param referId
	 * @return
	 */
	@Loggable
	public Long applyNewId(OperObject operObject, Long referId,
			McBtsTemplate temp) throws RemoteException, Exception;

	/**
	 * �����ݿ��ÿ����������ģ�屸������
	 * 
	 * @param templateId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void generateTemplateBackup(long templateId) throws RemoteException,
			Exception;

	/**
	 * �����ݿ��ÿ������ɾ��ģ�屸������
	 * 
	 * @param templateId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void deleteTemplateBackup(long templateId) throws RemoteException,
			Exception;

	/**
	 * ͨ��moId��ѯһ��ģ��
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsTemplate queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * ��ʼ����վ����
	 * 
	 * @param mcbts
	 * @param templateId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void initMcBtsData(McBts mcbts) throws RemoteException, Exception;

	/**
	 * ͬ��һ����վ
	 * 
	 * @param templateId
	 * @param oprs
	 * @param mcbts
	 */
	@Loggable
	public void syncAll(OperObject operObject, long templateId,
			Operation[] oprs, McBts[] mcbts) throws RemoteException, Exception;

	/**
	 * ɾ����վ�ĳ�ʼ������
	 * 
	 * @param mcbts
	 */
	public void rollBackMcBtsData(long moId) throws RemoteException, Exception;

	/**
	 * ��û�վҵ��ģ��
	 * 
	 * @return
	 */
	public List<McBtsOperation> getMcbtsOperation() throws RemoteException;

	/**
	 * ��MoCache�����Mo
	 * 
	 * @param mo
	 */
	public void addToMoCache(Long moId) throws RemoteException;

	/**
	 * ��MoCache��ɾ��Mo
	 * 
	 * @param moId
	 */
	@Loggable
	public void removeFromMoCache(OperObject operObject, Long moId)
			throws RemoteException;

	/**
	 * ����ģ������
	 * 
	 * @param template
	 * @throws RemoteException
	 */
	@Loggable
	public void updateTemplate(OperObject operObject, McBtsTemplate template)
			throws RemoteException;

	/**
	 * ��ȡ��ģ���������ɾ��ҵ���ʱ��ִ�лָ�����
	 */
	public void recover(Long moId, List<String> operations, boolean isDel)
			throws RemoteException;
}
