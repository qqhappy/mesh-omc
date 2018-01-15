/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.util.List;

import com.xinwei.minas.enb.core.model.EnbDynamicInfo;
import com.xinwei.minas.enb.core.model.EnbDynamicInfoCondition;
import com.xinwei.minas.enb.core.model.status.EnbStatusConfigCondition;
import com.xinwei.minas.enb.core.model.status.EnbStatusQueryCondition;

/**
 * 
 * eNB״̬��Ϣ����ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbStatusService {

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
	 * ��������ѯ��̬��Ϣ
	 * 
	 * @param condition
	 * @return
	 */
	public EnbDynamicInfo queryEnbDynamicInfo(EnbDynamicInfoCondition condition)
			throws Exception;
	

	/**
	 * ����eNB״̬
	 * 
	 * @param moId
	 * @param condition
	 * @throws Exception
	 */
	public void configStatus(long moId, EnbStatusConfigCondition condition)
			throws Exception;

	/**
	 * ����eNBʱ��
	 * 
	 * @param moId
	 * @param enbTime
	 * @throws Exception
	 */
	public void configEnbTime(long moId, Long enbTime) throws Exception;

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
	public void configRfSwitch(long moId, int rackNo, int shelfNo, int slotNo,
			Integer rfSwitch) throws Exception;
	

	/**
	 * ���ÿտ��������Կ�ʼ��ע
	 * @param moId
	 * @param ipAddress
	 * @return 
	 * @throws Exception 
	 */
	public List<Object> configAirFlowBegin(long moId, String ipAddress) throws Exception;

	/**
	 * ���ÿտ��������Խ�����ע
	 * @param moId
	 * @return
	 * @throws Exception 
	 */
	public List<Object> configAirFlowEnd(long moId) throws Exception;
	
	/**
	 * ����S1��������
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<Object> configS1Package(long moId) throws Exception;

	public List<Object> configS1Time(long moId)throws Exception;

}
