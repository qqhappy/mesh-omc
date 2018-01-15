/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-22	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.stat.core.model;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * 统计数据包
 * 
 * @author fanhaoyu
 * 
 */

public class StatDataPackage {

	private List<SingleStatItemData> statDatas;

	public StatDataPackage(List<SingleStatItemData> statDatas) {
		this.statDatas = statDatas;
	}

	/**
	 * 将传输层的数据包转换为字节数组
	 * 
	 * @return
	 */
	public byte[] toBytes() {
		int totalLength = 2;
		int count = statDatas.size();
		for (int i = 0; i < count; i++) {
			SingleStatItemData data = statDatas.get(i);
			byte[] dataBuf = data.toBytes();
			int dataBufLen = dataBuf.length;
			totalLength += 2;
			totalLength += dataBufLen;
		}

		byte[] totalBuf = new byte[totalLength];
		int offset = 0;
		// 消息总长度
		ByteUtils.putShort(totalBuf, offset, (short) totalLength);
		offset += 2;

		for (int i = 0; i < count; i++) {
			SingleStatItemData data = statDatas.get(i);
			byte[] dataBuf = data.toBytes();
			int bufLen = dataBuf.length;
			ByteUtils.putShort(totalBuf, offset, (short) bufLen);
			offset += 2;
			System.arraycopy(dataBuf, 0, totalBuf, offset, dataBuf.length);
			offset += dataBuf.length;
		}

		return totalBuf;
	}

	public List<SingleStatItemData> getStatDatas(byte[] buf) {
		int offset = 0;
		int bufLen = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
		statDatas = new ArrayList<SingleStatItemData>();
		while (offset < bufLen) {
			int dataLen = ByteUtils.toInt(buf, offset, 2);
			offset += 2;
			byte[] dataBuf = new byte[dataLen];
			System.arraycopy(buf, offset, dataBuf, 0, dataLen);
			offset += dataLen;
			SingleStatItemData data = new SingleStatItemData();
			data.parse(dataBuf);
			statDatas.add(data);
		}
		return statDatas;
	}
}
