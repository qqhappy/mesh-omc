/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.impl;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.server.mcbts.service.McBtsTypeDDService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ��վ���������ֵ����ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class McBtsTypeDDServiceImpl implements McBtsTypeDDService {

	// Ŀǰϵͳ֧�ֵĻ�վ����
	// 0��V5��վ
	// 10��R3R5��վ
	// 20��΢���ѻ�վ
	// 30��������Զ��վ
	// 40�����ز���վ
	// 50��XW65��618��Ŀ����վ
	// 60������RRU4ͨ����վ
	// 70������RRU8ͨ����վ
	// 80��10M��վ
	// 2000: eNB
	private Integer[] mcBtsTypes = new Integer[] { 0, 10, 20, 30, 40, 50, 60,
			70, 80, 2000 };

	// ��վ���������ֵ�ӳ���
	private Map<Integer, McBtsTypeDD> mcBtsTypeDDMap = new LinkedHashMap();

	public McBtsTypeDDServiceImpl() {
		for (Integer mcBtsType : mcBtsTypes) {
			String typeName = OmpAppContext.getMessage("mcbts.type."
					+ mcBtsType);
			McBtsTypeDD dd = new McBtsTypeDD();
			dd.setBtsType(mcBtsType);
			dd.setBtsTypeName(typeName);
			mcBtsTypeDDMap.put(mcBtsType, dd);
		}
	}

	@Override
	public List<McBtsTypeDD> queryAll() {
		List<McBtsTypeDD> list = new LinkedList();
		list.addAll(mcBtsTypeDDMap.values());
		return list;
	}

	@Override
	public McBtsTypeDD queryByTypeId(int mcBtsType) {
		return mcBtsTypeDDMap.get(mcBtsType);
	}

}
