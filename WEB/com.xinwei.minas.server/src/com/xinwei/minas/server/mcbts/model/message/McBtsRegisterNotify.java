/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.message;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * �����վע��֪ͨ��Ϣ��
 * 
 * @author chenjunhua
 * 
 */

public class McBtsRegisterNotify {
	
	// ��վID
	private Long btsId;
	
	// Ӳ���汾
	private String hardwareVersion;
	
	// ����汾
	private String sortwareVersion;
	
	// ��վ���õ�IP
	private String btsConfigIp;
	
	public McBtsRegisterNotify(Long btsId, byte[] buf) {
		// ��վID
		this.btsId = btsId;
		int offset = 0;
		// Ӳ���汾
		hardwareVersion = ByteUtils.toVersion(buf, offset, 4);
		offset += 4;
		// ����汾
		sortwareVersion = ByteUtils.toVersion(buf, offset, 4);
		offset += 4;
		// ����32�ֽڵļ��ܻ�վid
		offset += 32;
		// ��վ���õ�IP
		btsConfigIp = ByteUtils.toIp(buf, offset, 4);
		offset += 4;
		//
	}

	


	public Long getBtsId() {
		return btsId;
	}


	public String getBtsConfigIp() {
		return btsConfigIp;
	}

	public String getHardwareVersion() {
		return hardwareVersion;
	}

	public String getSortwareVersion() {
		return sortwareVersion;
	}


	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("btsConfigIp=").append(btsConfigIp);
		return buf.toString();
	}
	
	
}
