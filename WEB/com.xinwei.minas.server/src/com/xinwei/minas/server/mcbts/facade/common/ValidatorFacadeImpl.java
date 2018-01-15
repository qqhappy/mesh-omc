/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-4	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.mcbts.core.facade.common.ValidatorFacade;
import com.xinwei.minas.mcbts.core.model.common.FreqRelatedConfigure;
import com.xinwei.minas.server.mcbts.service.common.ValidatorService;

/**
 * 
 *��֤���������ʵ����
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class ValidatorFacadeImpl extends UnicastRemoteObject implements ValidatorFacade{

	private ValidatorService validatorService;
	
	protected ValidatorFacadeImpl() throws RemoteException {
		super();
	}

	public void setValidatorService(ValidatorService validatorService) {
		this.validatorService = validatorService;
	}

	@Override
	public String validateFreqConfiguration(FreqRelatedConfigure freqconf)
			throws Exception {
		return validatorService.validateFreqConfiguration(freqconf);
	}

}
