/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.net;

import java.util.List;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.stat.service.EnbRealtimeItemConfigCache;
import com.xinwei.minas.xstat.core.model.StatConstants;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * 实时性能数据编码接口实现类
 * 
 * @author fanhaoyu
 * 
 */

public class EnbRealtimeDataEncoderImpl implements EnbRealtimeDataEncoder {

	@Override
	public byte[] encode(long moId, List<EnbRealtimeItemData> itemDataList)
			throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		int length = 16 + calLength(itemDataList);
		byte[] buf = new byte[length];

		int offset = 0;
		// moId
		ByteUtils.putLong(buf, offset, moId);
		offset += 8;
		// enbId
		ByteUtils.putLong(buf, offset, enb.getEnbId());
		offset += 8;
		EnbRealtimeItemConfigCache cache = EnbRealtimeItemConfigCache
				.getInstance();
		// itemId|cellId|sfn|time|value
		// 4 |4 |4 |8 |8
		for (EnbRealtimeItemData itemData : itemDataList) {
			// 统计项ID
			ByteUtils.putInt(buf, offset, itemData.getItemId());
			offset += 4;
			EnbRealtimeItemConfig config = cache
					.getConfig(itemData.getItemId());
			// 小区ID
			if (config.isCellStatObject()) {
				String cellId = itemData.getEntityOid().split(
						"\\" + StatConstants.POINT)[1];
				ByteUtils.putInt(buf, offset, Integer.valueOf(cellId));
				offset += 4;
			}
			// 时间
			ByteUtils.putInt(buf, offset, itemData.getSystemFrameNo());
			offset += 4;
			// 时间
			ByteUtils.putLong(buf, offset, itemData.getEndTime());
			offset += 8;
			// 值
			ByteUtils.putDouble(buf, offset, itemData.getStatValue());
			offset += 8;

		}

		return buf;
	}

	private int calLength(List<EnbRealtimeItemData> itemList) {
		EnbRealtimeItemConfigCache cache = EnbRealtimeItemConfigCache
				.getInstance();
		int length = 0;
		for (EnbRealtimeItemData statItem : itemList) {
			length += 24;
			EnbRealtimeItemConfig config = cache
					.getConfig(statItem.getItemId());
			if (config.isCellStatObject()) {
				length += 4;
			}
		}
		return length;
	}

}
