/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-5	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.backup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.zk.core.basic.ZkBackup;

/**
 * 
 * NK数据备份管理器
 * 
 * @author fanhaoyu
 * 
 */

public class ZkBackupManager {

	private ConcurrentHashMap<String, ZkBackup> backupMap = new ConcurrentHashMap<String, ZkBackup>();

	private List<ZkBackup> backupList = new ArrayList<ZkBackup>();

	public void initialize() throws Exception {
		List<ZkBackup> list = ZkBackupUtil.getBackupsOnDisk();

		for (ZkBackup backup : list) {
			addBackup(backup);
		}

		Collections.sort(backupList, new Comparator<ZkBackup>() {
			@Override
			public int compare(ZkBackup backup1, ZkBackup backup2) {
				return (int) (backup1.getCreateTime().getTime() - backup2
						.getCreateTime().getTime());
			}
		});
	}

	public void addBackup(ZkBackup backup) {
		if (backupMap.containsKey(backup.getName()))
			return;
		backupMap.putIfAbsent(backup.getName(), backup);
		backupList.add(backup);
	}

	public void removeBackup(String backupName) {
		backupList.remove(backupMap.get(backupName));
		backupMap.remove(backupName);
	}

	public ZkBackup getBackup(String backupName) {
		return backupMap.get(backupName);
	}

	public List<ZkBackup> getBackupList() {
		return backupList;
	}
	
	/**
	 * 清空备份列表
	 */
	public void clearBackupList() {
		backupList.clear();
		backupMap.clear();
	}
}