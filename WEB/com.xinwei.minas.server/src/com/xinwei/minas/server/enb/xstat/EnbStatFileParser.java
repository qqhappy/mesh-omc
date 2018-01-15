/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-23	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.EnbStatEntity;
import com.xinwei.minas.server.xstat.service.StatFileParser;

/**
 * 
 * eNB统计数据文件解析接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatFileParser implements StatFileParser {

	private Log log = LogFactory.getLog(EnbStatFileParser.class);

	@SuppressWarnings("rawtypes")
	@Override
	public List parse(File file) throws Exception {
		byte[] content = getFileContent(file);
		return parse(content);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List parse(byte[] fileContent) throws Exception {
		List<EnbStatEntity> entityList = new ArrayList<EnbStatEntity>();
		// 当前基站端设置文件内只有一行
		EnbStatEntity entity = parseEntity(fileContent);
		entityList.add(entity);
		return entityList;
	}

	@Override
	public long getDataTime(String fileName) {
		String time = fileName.split("\\.")[2];
		return Long.valueOf(time + "00");
	}

	private EnbStatEntity parseEntity(byte[] lineBytes) {
		// 系统中不存在的统计项
		List<Integer> idNotExist = new LinkedList<Integer>();
		Map<Integer, CounterItemConfig> counterConfigMap = EnbStatModule
				.getInstance().getCounterConfigMap();

		EnbStatEntity entity = new EnbStatEntity();
		// [eNB ID] 8
		// [STARTT_TIME] 8
		// [END_TIME] 8
		// [VERSION] 4
		// [Rev] 4
		// [LEN] 4
		// [Head CRC] 4
		// [PERFDATA] V

		// header
		ByteBuffer buffer = ByteBuffer.wrap(lineBytes);

		long enbId = buffer.getLong();
		entity.setEnbId(enbId);
		long startTime = getTime(buffer);
		entity.setStartStatTime(startTime);
		long endTime = getTime(buffer);
		entity.setEndStatTime(endTime);
		int version = buffer.getInt();
		entity.setVersion(version);
		entity.setReserve(buffer.getInt());
		entity.setLength(buffer.getInt());
		entity.setHeaderCrc(buffer.getInt());
		// perfData
		while (buffer.remaining() > 0) {
			// 基站级统计项
			int counterNum = buffer.getInt();
			for (int i = 0; i < counterNum; i++) {
				int itemId = buffer.getInt();
				int value = buffer.getInt();
				CounterItemConfig config = counterConfigMap.get(itemId);
				if (config == null) {
					idNotExist.add(itemId);
					continue;
				}
				entity.addItem(itemId, Double.valueOf(value));
			}
			break;
		}
		// 小区级统计项
		while (buffer.remaining() > 0) {
			int cellId = buffer.getInt();
			int counterNum = buffer.getInt();
			for (int i = 0; i < counterNum; i++) {
				int itemId = buffer.getInt();
				int value = buffer.getInt();
				CounterItemConfig config = counterConfigMap.get(itemId);
				if (config == null) {
					idNotExist.add(itemId);
					continue;
				}
				entity.addItem(cellId, itemId, Double.valueOf(value));
			}
		}

		if (!idNotExist.isEmpty()) {
			log.warn("configs are not exist. idList=" + idNotExist);
		}

		return entity;
	}

	private long getTime(ByteBuffer buffer) {
		byte[] bytes = new byte[8];
		buffer.get(bytes);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 7; i++) {
			String num = String.valueOf(bytes[i]);
			if (num.length() == 1) {
				num = "0" + num;
			}
			builder.append(num);
		}
		return Long.valueOf(builder.toString());
	}

	private byte[] getFileContent(File file) throws Exception {
		byte[] content = new byte[0];
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			int fileLength = inputStream.available();
			content = new byte[fileLength];
			inputStream.read(content);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return content;
	}

}