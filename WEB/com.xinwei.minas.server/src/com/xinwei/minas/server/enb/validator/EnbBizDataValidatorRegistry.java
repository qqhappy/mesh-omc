/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.validator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * eNB业务规则校验器工厂类
 * 
 * @author fanhaoyu
 * 
 */

public class EnbBizDataValidatorRegistry {

	private static EnbBizDataValidatorRegistry instance = new EnbBizDataValidatorRegistry();

	private static Map<String, EnbBizDataValidator> validatorMap = new ConcurrentHashMap<String, EnbBizDataValidator>();

	public static EnbBizDataValidatorRegistry getInstance() {
		return instance;
	}

	public void register(String tableName, EnbBizDataValidator validator) {
		validatorMap.put(tableName, validator);
	}

	public EnbBizDataValidator getValidator(String tableName) {
		return validatorMap.get(tableName);
	}

}
