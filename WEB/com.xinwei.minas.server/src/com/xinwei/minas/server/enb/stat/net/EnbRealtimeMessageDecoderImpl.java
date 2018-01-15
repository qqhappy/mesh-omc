/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-8	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.net;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;
import com.xinwei.minas.enb.core.utils.EnbStatConstants;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.TagConst;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.stat.service.EnbRealtimeItemConfigCache;
import com.xinwei.minas.xstat.core.model.StatConstants;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.core.utils.DateUtils;

/**
 * 
 * ʵʱ������Ϣ������ʵ����
 * 
 * @author fanhaoyu
 * 
 */

public class EnbRealtimeMessageDecoderImpl implements EnbRealtimeMessageDecoder {

	@Override
	public List<EnbRealtimeItemData> decode(EnbAppMessage appMessage)
			throws Exception {

		long currentTime = DateUtils.getBriefTimeFromMillisecondTime(System
				.currentTimeMillis());

		long enbId = appMessage.getEnbId();
		Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
		byte[] dataBytes = appMessage
				.getByteValue(TagConst.REALTIME_REPORT_DATA);

		// typedef struct _T_Ei_MsgHead
		// {
		// U32 u32EiMsgId;
		// U32 u32AirTime;
		// U32 u32Sn;
		// U16 u16TlvNum;
		// U16 u16MsgLen; /* Tlv Head + Tlv Data */
		// }T_Ei_MsgHead;
		int offset = 0;
		long msgId = ByteUtils.toUnsignedNumber(dataBytes, offset, 4);
		if (msgId != 301) {
			// ���˻�վ�ϱ��ķ�301 msgId����Ϣ
			return Collections.emptyList();
		}
		offset += 4;
		long airTime = ByteUtils.toUnsignedNumber(dataBytes, offset, 4);
		// ϵͳ֡��
		int systemFrameNo = (int) ((airTime / 16) % 1024);
		offset += 4;
		// long sn = ByteUtils.toUnsignedNumber(buf, offset, 4);
		offset += 4;
		int tlvNum = ByteUtils.toInt(dataBytes, offset, 2);
		offset += 2;
		// int msgLength = ByteUtils.toInt(buf, offset, 2);
		offset += 2;

		if (tlvNum == 0)
			return Collections.emptyList();

		EnbRealtimeItemConfigCache itemCache = EnbRealtimeItemConfigCache
				.getInstance();

		List<EnbRealtimeItemData> itemDataList = new LinkedList<EnbRealtimeItemData>();
		String enbHexId = enb.getHexEnbId();
		for (int i = 0; i < tlvNum; i++) {
			// typedef struct _T_Ei_TlvHead
			// {
			// U16 u16TlvId;
			// U16 u16Id;
			// U16 u16TlvLen;
			// U8 u8CellId;
			// U8 u8Rcv;
			// }T_Ei_TlvHead;
			int tagId = ByteUtils.toInt(dataBytes, offset, 2);
			offset += 2;
			// int id = ByteUtils.toInt(dataBytes, offset, 2);
			offset += 2;
			int tlvLength = ByteUtils.toInt(dataBytes, offset, 2);
			offset += 2;
			int cellId = ByteUtils.toInt(dataBytes, offset, 1);
			offset += 1;
			// reserved
			offset += 1;
			List<Integer> itemIdList = itemCache.getTagInnerItems(tagId);
			if (itemIdList == null || itemIdList.isEmpty()) {
				offset += tlvLength;
				continue;
			}

			int count = 0;
			for (Integer itemId : itemIdList) {
				EnbRealtimeItemConfig config = itemCache.getConfig(itemId);

				EnbRealtimeItemData itemData = new EnbRealtimeItemData();
				itemData.setMoId(enb.getMoId());
				itemData.setItemId(itemId);
				String dataType = config.getDataType();
				// ��������ΪU32��U16��ȥ��ǰ�����ĸ��Ȼ���8����Ϊ���ݳ���
				int valueLength = Integer.valueOf(dataType.substring(1,
						dataType.length())) / 8;

				if (config.isCellStatObject()) {
					itemData.setEntityType(EnbStatConstants.STAT_OBJECT_ENB
							+ StatConstants.POINT
							+ EnbStatConstants.STAT_OBJECT_CELL);
					itemData.setEntityOid(enbHexId + StatConstants.POINT
							+ cellId);
				} else if (config.isBtsStatObject()) {
					itemData.setEntityType(EnbStatConstants.STAT_OBJECT_ENB);
					itemData.setEntityOid(enbHexId);
				}
				// ��������ͳһϵͳ֡��
				itemData.setSystemFrameNo(systemFrameNo);
				// ��������ʱ��Ϊ���ܷ�����ʱ��
				itemData.setEndTime(currentTime);

				double statValue = 0;
				if (dataType.startsWith("S")) {
					statValue = ByteUtils.toSignedNumber(dataBytes, offset,
							valueLength);
				} else {
					statValue = ByteUtils.toUnsignedNumber(dataBytes, offset,
							valueLength);
				}
				itemData.setStatValue(statValue);
				itemDataList.add(itemData);

				offset += valueLength;
				count += valueLength;
				// �����Դ���
				if (count >= tlvLength)
					break;
			}
		}
		return itemDataList;
	}

}
