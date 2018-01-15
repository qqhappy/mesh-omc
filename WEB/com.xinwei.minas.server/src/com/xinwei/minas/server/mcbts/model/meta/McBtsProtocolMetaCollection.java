/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.meta;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * McBtsЭ��Ԫ���ݼ��� 
 * 
 * @author chenjunhua
 * 
 */

public class McBtsProtocolMetaCollection {
	
	private McBtsProtocolMeta[] protocol;
	
	private Map<String, McBtsProtocolMeta> protocolMap = new HashMap();

	public McBtsProtocolMeta[] getProtocol() {
		return protocol;
	}

	public void setProtocol(McBtsProtocolMeta[] protocol) {
		this.protocol = protocol;
		for (McBtsProtocolMeta p : protocol) {
			String protocolKey = this.getProtocolKey(p.getBizName(), p.getOperation());
			protocolMap.put(protocolKey, p);
		}
	}
	
	/**
	 * ����ҵ�����ƺͲ�����ȡ��Ӧ��Э��Ԫ����
	 * @param bizName ҵ������
	 * @param operation ����
	 * @return
	 */
	public McBtsProtocolMeta getProtocolMetaBy(String bizName, String operation) {
		String key = getProtocolKey(bizName, operation);
		return protocolMap.get(key);
	}
	
	private String getProtocolKey(String bizName, String operation) {
		return bizName + "/" + operation;
	} 
}
