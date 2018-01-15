/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.backup;

import java.util.Date;

import org.quartz.SimpleTrigger;

/**
 * 
 * NK集群数据定时备份任务触发器
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class ZkBackupTaskTrigger extends SimpleTrigger {
	public ZkBackupTaskTrigger() {
		this.setStartTime(new Date(System.currentTimeMillis()));
		this.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
		this.setName("ZkBackupTaskTrigger");
	}
}