/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.mcbts.core.model.McBts;

/**
 * 
 * 自发现的McBts缓存
 * 
 * @author chenjunhua
 * 
 */

public class AutomaticFindMcBtsCache {

	private static final AutomaticFindMcBtsCache instance = new AutomaticFindMcBtsCache();

	private Map<Long, McBts> mapByBtsId = new ConcurrentHashMap<Long, McBts>();

	private AutomaticFindMcBtsCache() {

	}

	public static AutomaticFindMcBtsCache getInstance() {
		return instance;
	}

	/**
	 * 增加或更新McBts
	 * 
	 * @param mcBts
	 */
	public void addOrUpdate(McBts mcBts) {
//		McBts cacheBts = mapByBtsId.get(mcBts.getBtsId());
//		if (cacheBts != null) {
//			// 如果存在缓存，则需要保留缓存中基站的Ip等信息
//			mcBts.setBtsIp(cacheBts.getBtsIp());
//			mcBts.setPublicIp(cacheBts.getPublicIp());
//			mcBts.setPublicPort(cacheBts.getPublicPort());
//			mcBts.setHardwareVersion(cacheBts.getHardwareVersion());
//			mcBts.setSoftwareVersion(cacheBts.getSoftwareVersion());
//		}
		mapByBtsId.put(mcBts.getBtsId(), mcBts);
	}

	/**
	 * 删除McBTS
	 * 
	 * @param moId
	 */
	public void delete(Long btsId) {
		mapByBtsId.remove(btsId);
	}

	/**
	 * 查询所有基站
	 * 
	 * @return
	 */
	public List<McBts> queryAll() {
		List<McBts> mcBtsList = new ArrayList<McBts>();
		Set<Long> keySet = mapByBtsId.keySet();
		Iterator<Long> it = keySet.iterator();
		while (it.hasNext()) {
			McBts mcBts = mapByBtsId.get(it.next());
			mcBtsList.add(mcBts);
		}

		// 按基站ID排序
		Collections.sort(mcBtsList, new Comparator<McBts>() {
			@Override
			public int compare(McBts o1, McBts o2) {
				long delta = (o1.getBtsId().longValue() - o2.getBtsId()
						.longValue());
				if (delta > 0) {
					return 1;
				}
				else if (delta == 0) {
					return 0;
				}
				else {
					return -1;
				}
			}
		});

		return mcBtsList;
	}
	

	/**
	 * 根据BtsId查询McBts
	 * 
	 * @param btsId
	 * @return
	 */
	public McBts queryByBtsId(Long btsId) {
		return mapByBtsId.get(btsId);
	}

}
