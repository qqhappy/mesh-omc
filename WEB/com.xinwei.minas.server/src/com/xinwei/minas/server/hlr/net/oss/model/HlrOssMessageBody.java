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
 * HLR OSS消息的TV格式消息体
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
	 * 子类需要覆盖此方法
	 * 
	 * @return
	 */
	public byte[] toBytes() {
		return body;
	}
}
