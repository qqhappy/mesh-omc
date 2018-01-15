/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.proxy;

import com.xinwei.minas.mcbts.core.model.McBts;

/**
 * 
 * ͨ��ҵ��Proxy
 * 
 * @author chenshaohua
 * 
 */

public abstract class GenericBizProxy<T> {
	
	
	
	public void sycConfig(Long moId, T model) throws Exception {
		// �ж�ҵ���Ƿ�֧��
		McBts mcBts = null;
		mcBts.getSoftwareVersion();
		mcBts.getTypeId();
		int configMoc = this.getConfigMoc();
		try {
			this.config(moId, model);
			
		}
		catch (UnsupportedOperationException e) {
			
		}		
	}
	
	public boolean isSupportConfig() {
		// 
		return true;
	}
	
	public abstract int getConfigMoc();
	
	public abstract int getQueryMoc();
	
	public abstract void  config(Long moId, T model) throws UnsupportedOperationException, Exception;
	
}
