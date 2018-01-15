/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-22	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.message;

import java.util.Calendar;

import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessageConstants;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * emsÊ±¼äÓ¦´ð
 * 
 * @author chenjunhua
 * 
 */

public class McBtsEmsTimeResponse {

	private Long btsId;
	
	
	public McBtsMessage toMcBtsMessage() {
		McBtsMessage resp = new McBtsMessage();
		resp.setBtsId(btsId);
		resp.setMa(McBtsMessage.MA_CONF);
		resp.setMoc(McBtsMessageConstants.MOC_EMS_TIME_RESP);
		resp.setActionType(McBtsMessage.ACTION_TYPE_RESP);
		resp.setContent(getContentByte());
		return resp;
	}
	

	private byte[] getContentByte() {
		Calendar c = Calendar.getInstance();
		byte[] buf = new byte[7];
		int offset = 0;
		// year
		ByteUtils.putShort(buf, offset, (short)c.get(Calendar.YEAR));
		offset += 2;
		// month
		buf[offset++] = (byte)(c.get(Calendar.MONTH) + 1);
		// day
		buf[offset++] = (byte)(c.get(Calendar.DAY_OF_MONTH));
		// hour
		buf[offset++] = (byte)(c.get(Calendar.HOUR_OF_DAY));
		// minute
		buf[offset++] = (byte)(c.get(Calendar.MINUTE));
		// second
		buf[offset++] = (byte)(c.get(Calendar.SECOND));		
		
		return buf;
	}

	public Long getBtsId() {
		return btsId;
	}

	public void setBtsId(Long btsId) {
		this.btsId = btsId;
	}

	
}
