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
 * Hlr��ѯ��վӦ����Ϣ
 * 
 * @author tiance
 * 
 */

public class HlrQueryBtsResponse {
	// ��վ������
	private Integer nBts;
	// ��վ�б�
	private List<McBts> btsList;
	// OSS˽�б�ͷ
	HlrOssBizMessagePrivateHeader privateHeader;
	// ͸����Ϣ��
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

	// TODO ������Ҫ͸������Ϣ
	public HlrOssBizMessage toHlrOssBizMessage() {
		HlrOssBizMessage responseMessage = new HlrOssBizMessage(privateHeader,
				messageBody);

		return responseMessage;
	}
}
