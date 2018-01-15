/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-14	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.net;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.lte.web.enb.cache.EnbRealtimeItemConfigCache;
import com.xinwei.lte.web.enb.util.StatDataUtil;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;
import com.xinwei.minas.enb.core.utils.EnbStatConstants;
import com.xinwei.minas.xstat.core.model.StatConstants;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.core.utils.StringUtils;
/**
 * 
 * eNB实时性能数据解码器接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbRealtimeDataDecoderImpl implements EnbRealtimeDataDecoder {

	private Log log = LogFactory.getLog(EnbRealtimeDataDecoderImpl.class);

	@Override
	public List<EnbRealtimeItemData> decode(byte[] dataBytes) throws Exception {

		int offset = 0;
		// moId
		long moId = ByteUtils.toLong(dataBytes, offset, 8);
		offset += 8;
		// enbId
		long enbId = ByteUtils.toLong(dataBytes, offset, 8);
		String enbHexId = StringUtils.to8HexString(enbId);
		offset += 8;

		EnbRealtimeItemConfigCache cache = EnbRealtimeItemConfigCache
				.getInstance();

		List<EnbRealtimeItemData> itemList = new LinkedList<EnbRealtimeItemData>();

		while (offset < dataBytes.length) {

			EnbRealtimeItemData itemData = new EnbRealtimeItemData();
			itemData.setMoId(moId);

			// 统计项ID
			int itemId = ByteUtils.toInt(dataBytes, offset, 4);
			itemData.setItemId(itemId);
			offset += 4;
			EnbRealtimeItemConfig config = cache.getConfig(itemId);
			if (config == null) {
				log.warn("config is null. itemId=" + itemId);
				continue;
			}
			// 小区
			if (config.isCellStatObject()) {
				int cellId = ByteUtils.toInt(dataBytes, offset, 4);
				itemData.setEntityType(EnbStatConstants.STAT_OBJECT_ENB
						+ StatConstants.POINT
						+ EnbStatConstants.STAT_OBJECT_CELL);
				itemData.setEntityOid(enbHexId + StatConstants.POINT + cellId);
				offset += 4;
			} else if (config.isBtsStatObject()) {
				itemData.setEntityType(EnbStatConstants.STAT_OBJECT_ENB);
				itemData.setEntityOid(enbHexId);
			}
			// 帧号
			int sfn = ByteUtils.toInt(dataBytes, offset, 4);
			itemData.setSystemFrameNo(sfn);
			offset += 4;
			// 时间
			long time = ByteUtils.toLong(dataBytes, offset, 8);
			itemData.setEndTime(time);
			offset += 8;
			// 值
			double value = ByteUtils.toDouble(dataBytes, offset, 8);
			//根据统计项显示值与原值转换关系获取显示值*10 *4
			double showvalue = StatDataUtil.getValueToShow(moId,itemData.getItemId(), value);
			itemData.setStatValue(showvalue);
			offset += 8;
			itemList.add(itemData);
		}

		return itemList;
	}

}
