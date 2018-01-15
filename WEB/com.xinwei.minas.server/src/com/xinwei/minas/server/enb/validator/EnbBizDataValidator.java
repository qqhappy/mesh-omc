/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.validator;

import com.xinwei.minas.core.exception.BizException;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * eNBҵ������У�����ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbBizDataValidator {

	/**
	 * ��֤�����Ƿ���Ϲ���
	 * 
	 * @param moId
	 * @param bizRecord
	 * @param actionType
	 *            �μ�ActionType���е�����ɾ����
	 * @throws BizException
	 */
	public abstract void validate(long moId, XBizRecord bizRecord,
			String actionType) throws Exception;

}
