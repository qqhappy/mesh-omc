/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-30	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.model.message;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.hlr.net.oss.model.HlrOssBizMessage;
import com.xinwei.minas.server.hlr.net.oss.model.HlrOssBizMessagePrivateHeader;
import com.xinwei.minas.server.hlr.net.udp.HlrUdpMessage;
import com.xinwei.minas.server.ut.model.QueryBtsHlrOssMessageBody;

/**
 * 
 * Hlr查询基站应答消息
 * 
 * @author tiance
 * 
 */

public class HlrQueryBtsResponse {
	// 基站的总数
	private Integer nBts;
	// 基站列表
	private List<McBts> btsList;
	// OSS私有报头
	HlrOssBizMessagePrivateHeader privateHeader;
	// 透传消息体
	private QueryBtsHlrOssMessageBody messageBody;

	private static final int OPERATION_OBJECT = 0x87;

	private static final int OPERATION_TYPE = 0x03;

	public HlrQueryBtsResponse() {

	}

	public HlrQueryBtsResponse(boolean isLastPack, int btsNum, int pageIndex,
			List<McBts> list) {
		this.btsList = list;

		privateHeader = new HlrOssBizMessagePrivateHeader();
		privateHeader.setOperObject(OPERATION_OBJECT);
		privateHeader.setOperType(OPERATION_TYPE);
		privateHeader.setPackageNo(pageIndex);
		privateHeader.setCompleteFlag(isLastPack ? 1 : 0);

		messageBody = new QueryBtsHlrOssMessageBody(btsNum, pageIndex, btsList);
	}

	// TODO 配置需要透传的消息
	public HlrOssBizMessage toHlrOssBizMessage() {
		HlrOssBizMessage responseMessage = new HlrOssBizMessage(privateHeader,
				messageBody);

		return responseMessage;
	}
}
