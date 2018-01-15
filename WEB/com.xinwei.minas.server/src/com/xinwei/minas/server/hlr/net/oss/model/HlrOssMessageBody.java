/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-07-31	| chenjunhua 	| 	create the file                    
 */

package com.xinwei.minas.server.hlr.net.oss.model;

/**
 * 
 * HLR OSS��Ϣ��TV��ʽ��Ϣ��
 * 
 * @author chenjunhua
 * 
 */

public class HlrOssMessageBody {

	protected byte[] body = new byte[0];

	public HlrOssMessageBody() {

	}

	public HlrOssMessageBody(byte[] buf, int offset) {
		int bodyLen = buf.length - offset;
		body = new byte[bodyLen];
		System.arraycopy(buf, offset, body, 0, bodyLen);
	}

	/**
	 * ������Ҫ���Ǵ˷���
	 * 
	 * @return
	 */
	public byte[] toBytes() {
		return body;
	}
}
