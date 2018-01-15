/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-15	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.zk;

import java.util.List;

import com.xinwei.minas.server.zk.dao.ZkClusterDAO;
import com.xinwei.minas.server.zk.service.ZkBasicService;
import com.xinwei.minas.zk.core.basic.ZkCluster;

/**
 * 
 * ZooKeeper管理模块
 * 
 * @author chenjunhua
 * 
 */

public class ZkModule {

	private static final ZkModule instance = new ZkModule();
	
	private ZkBasicService zkBasicService;
	
	private ZkModule() {		
	}
	
	public static ZkModule getInstance() {
		return instance;
	}
	
	/**
	 * zk模块初始化
	 * @param zkBasicService
	 * @throws Exception 
	 */
	public void initialize(ZkBasicService zkBasicService) throws Exception {
		this.zkBasicService = zkBasicService;
		zkBasicService.initialize();
	}
	
	
	
	
}
