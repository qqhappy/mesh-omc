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
 * HLR OSS联络消息私有报头
 * 
 * @author chenjunhua
 * 
 */

public class HlrOssHandshakeMessagePrivateHeader implements HlrOssPrivateHeader {

	@Override
	public byte[] toBytes() {
		return new byte[0];
	}

}
