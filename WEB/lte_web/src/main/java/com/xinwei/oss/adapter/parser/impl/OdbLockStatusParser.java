/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-25	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.parser.impl;

import com.xinwei.oss.adapter.model.biz.ClientLockStatus;
import com.xinwei.oss.adapter.model.biz.ClientOdbLockStatus;
import com.xinwei.oss.adapter.model.meta.OssBizItem;
import com.xinwei.oss.adapter.parser.IOSSParser;
import com.xinwei.shlr.acc.dataEntity.other.LockStatus;
import com.xinwei.shlr.acc.dataEntity.other.OdbLockStatus;

/**
 * 
 * 前端OdbLockStatus与后台OdbLockStatus的转换
 * 
 * @author chenshaohua
 * 
 */

public class OdbLockStatusParser implements IOSSParser {

	@Override
	public Object parse(Object object, OssBizItem item) {
		ClientOdbLockStatus clientOdbLockStatus = (ClientOdbLockStatus) object;
		OdbLockStatus odbLockStatus = new OdbLockStatus();
		ClientLockStatus[] clientLockStatusItems = clientOdbLockStatus
				.getLockStatusItems();

		for (int i = 0; i < clientLockStatusItems.length; i++) {
			odbLockStatus.lockStatusItems[i] = new LockStatus();
			odbLockStatus.lockStatusItems[i].lockId = clientLockStatusItems[i]
					.getLockId();
			odbLockStatus.lockStatusItems[i].lockStatus = clientLockStatusItems[i]
					.getLockStatus();
		}

		return odbLockStatus;
	}

	@Override
	public Object unParse(Object object, OssBizItem item) {
		OdbLockStatus odbLockStatus = (OdbLockStatus) object;
		ClientOdbLockStatus clientOdbLockStatus = new ClientOdbLockStatus();
		ClientLockStatus[] clientLockStatusItems = clientOdbLockStatus
				.getLockStatusItems();
		for (int i = 0; i < odbLockStatus.lockStatusItems.length; i++) {
			clientLockStatusItems[i] = new ClientLockStatus();
			clientLockStatusItems[i]
					.setLockId(odbLockStatus.lockStatusItems[i].lockId);
			clientLockStatusItems[i]
					.setLockStatus(odbLockStatus.lockStatusItems[i].lockStatus);
		}

		return clientOdbLockStatus;
	}
}
