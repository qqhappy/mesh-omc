/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-12	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.model.status.EnbStatusQueryCondition;

/**
 * 
 * eNB״̬��Ϣ����ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbStatusFacade extends Remote {

	/**
	 * ���ղ�ѯ������ѯeNB״̬
	 * 
	 * @param moId
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryStatus(long moId, EnbStatusQueryCondition condition)
			throws Exception;

	/**
	 * ����eNBʱ��
	 * 
	 * @param moId
	 * @param enbTime
	 * @throws Exception
	 */
	@Loggable
	public void configEnbTime(OperObject operObject, long moId, Long enbTime)
			throws Exception;

	/**
	 * ���ù��ſ���
	 * 
	 * @param moId
	 * @param rackNo
	 * @param shelfNo
	 * @param slotNo
	 * @param rfSwitch
	 * @throws Exception
	 */
	@Loggable
	public void configRfSwitch(OperObject operObject, long moId, int rackNo,
			int shelfNo, int slotNo, Integer rfSwitch) throws Exception;

	/**
	 * ��ʼ���
	 * @param createEnbOperObject
	 * @param moId
	 * @param ipAddress
	 * @return
	 * @throws Exception
	 */
	List<Object> configAirFlowBegin(OperObject createEnbOperObject, long moId,
			String ipAddress) throws Exception;

	/**
	 * ֹͣ���
	 * @param createEnbOperObject
	 * @param moId
	 * @return
	 * @throws Exception 
	 */
	public List<Object> configAirFlowEnd(OperObject createEnbOperObject,
			long moId) throws Exception;
	/**
	 * S1��ʼ���������ʼ��
	 * @param createEnbOperObject
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<Object> configS1Package(OperObject createEnbOperObject,
			long moId)throws Exception;

	/**
	 * S1ʱ�Ӽ��
	 * @param createEnbOperObject
	 * @param moId
	 * @return
	 */
	public List<Object> configS1Time(OperObject createEnbOperObject, long moId)throws Exception;
	
}
