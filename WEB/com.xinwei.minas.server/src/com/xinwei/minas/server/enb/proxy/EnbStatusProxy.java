/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.proxy;

import java.util.List;

import com.xinwei.minas.enb.core.model.EnbDynamicInfo;
import com.xinwei.minas.enb.core.model.EnbDynamicInfoCondition;
import com.xinwei.minas.enb.core.model.status.EnbStatusConfigCondition;
import com.xinwei.minas.enb.core.model.status.EnbStatusQueryCondition;

/**
 * 
 * eNB״̬����ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbStatusProxy {

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

}
