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
 * eNB业务数据校验器接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbBizDataValidator {

	/**
	 * 验证数据是否符合规则
	 * 
	 * @param moId
	 * @param bizRecord
	 * @param actionType
	 *            参见ActionType类中的增、删、改
	 * @throws BizException
	 */
	public abstract void validate(long moId, XBizRecord bizRecord,
			String actionType) throws Exception;

}
