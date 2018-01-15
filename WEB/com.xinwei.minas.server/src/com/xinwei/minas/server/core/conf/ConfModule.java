/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-13	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf;

import java.util.List;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.server.core.conf.dao.MoBasicDAO;
import com.xinwei.minas.server.core.conf.service.MoCache;

/**
 * 
 * ����ģ��
 * 
 * @author chenjunhua
 * 
 */

public class ConfModule {
	
	private static final ConfModule instance = new ConfModule();
	
	private MoBasicDAO moBasicDao;
	
	private ConfModule() {		
	}
	
	public static ConfModule getInstance() {
		return instance;
	}
	
	public void initialize(MoBasicDAO moBasicDao) {
		this.moBasicDao = moBasicDao;
		// Mo���治��Ҫ�����ر�ĳ�ʼ�����޸�Ϊ������Ԫ�����ʼ��ʱ���г�ʼ��
		//this.initializeMoCache();		
	}
	
	
//	private void initializeMoCache() {
//		List<Mo> allMo = moBasicDao.queryAll();
//		for (Mo mo : allMo) {			
//			MoCache.getInstance().add(mo);
//		}		
//	}

}
