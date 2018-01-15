/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-23	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.parser.impl;

import com.xinwei.oss.adapter.model.biz.ClientIPGroup;
import com.xinwei.oss.adapter.model.biz.ClientIPGroups;
import com.xinwei.oss.adapter.model.meta.OssBizItem;
import com.xinwei.oss.adapter.parser.IOSSParser;
import com.xinwei.shlr.acc.dataEntity.other.IpGroup;
import com.xinwei.shlr.acc.dataEntity.other.IpGroups;

/**
 * 
 * IPGroup对象解析器,前端IPGroup对象与后台IPGroup对象的相互转换
 * 
 * @author chenshaohua
 * 
 */

public class IPGroupParser implements IOSSParser {

	@Override
	public Object parse(Object object, OssBizItem item) {
		ClientIPGroups clientIPGroups = (ClientIPGroups) object;
		IpGroups serverIpGroups = new IpGroups();
		// 设置ip组数量
		serverIpGroups.ipGroupNum = clientIPGroups.getNum();
		// 设置ip组的值
		ClientIPGroup[] clientIPGroupArray = clientIPGroups
				.getClientIPGroupArray();
		serverIpGroups.ipGroups = new IpGroup[clientIPGroupArray.length];
		for (int i = 0; i < clientIPGroupArray.length; i++) {
			serverIpGroups.ipGroups[i] = new IpGroup();
			serverIpGroups.ipGroups[i].fixedIp = clientIPGroupArray[i].getIp();
			serverIpGroups.ipGroups[i].mac = clientIPGroupArray[i].getMac();
			serverIpGroups.ipGroups[i].anchorBtsId = clientIPGroupArray[i]
					.getAnchorBtsId();
		}
		return serverIpGroups;
	}

	@Override
	public Object unParse(Object object, OssBizItem item) {
		IpGroups serverIpGroups = (IpGroups) object;
		ClientIPGroups clientIPGroups = new ClientIPGroups();
		clientIPGroups.setNum(serverIpGroups.ipGroupNum);

		ClientIPGroup[] clientIPGroupArray = new ClientIPGroup[serverIpGroups.ipGroups.length];
		for (int i = 0; i < clientIPGroupArray.length; i++) {
			clientIPGroupArray[i] = new ClientIPGroup();
			clientIPGroupArray[i].setIp(serverIpGroups.ipGroups[i].fixedIp);
			clientIPGroupArray[i].setMac(serverIpGroups.ipGroups[i].mac);
			clientIPGroupArray[i]
					.setAnchorBtsId(serverIpGroups.ipGroups[i].anchorBtsId);
		}
		clientIPGroups.setClientIPGroupArray(clientIPGroupArray);

		return clientIPGroups;
	}

}
