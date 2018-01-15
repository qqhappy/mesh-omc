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
 * 基站类型数据字典服务实现
 * 
 * @author chenjunhua
 * 
 */

public class McBtsTypeDDServiceImpl implements McBtsTypeDDService {

	// 目前系统支持的基站类型
	// 0：V5基站
	// 10：R3R5基站
	// 20：微蜂窝基站
	// 30：光纤拉远基站
	// 40：多载波基站
	// 50：XW65（618项目）基站
	// 60：大功率RRU4通道基站
	// 70：大功率RRU8通道基站
	// 80：10M基站
	// 2000: eNB
	private Integer[] mcBtsTypes = new Integer[] { 0, 10, 20, 30, 40, 50, 60,
			70, 80, 2000 };

	// 基站类型数据字典映射表
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
