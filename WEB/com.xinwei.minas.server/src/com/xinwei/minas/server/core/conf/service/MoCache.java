/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-30	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.core.model.Mo;

/**
 * 
 * MO缓存
 * 
 * @author chenjunhua
 * 
 */

public class MoCache {
	
	
	private static final MoCache instance = new MoCache();

	private Map<Long, Mo> mapByMoId = new ConcurrentHashMap();
	
	private MoCache() {
		
	}
	
	public static MoCache getInstance() {
		return instance;
	}
	
	/**
	 * 查找所有MO
	 * 
	 * @return
	 */
	public List<Mo> queryAll() {		
		Collection<Mo> moCollection = mapByMoId.values();		
		List<Mo> moList = new LinkedList();
		for (Mo mo : moCollection) {
			if (mo.isRealMo()) {
				moList.add(mo);
			}
		}
		sortMoList(moList);
		
		return moList;
	}
	
	/**
	 * 获取指定MO
	 * @param moId
	 * @return
	 */
	public Mo queryByMoId(Long moId) {
		return mapByMoId.get(moId);
	}
	
	
	/**
	 * 获取制定类型的MO
	 * 
	 * @param moTypeId
	 *            类型
	 * @return
	 */
	public List<Mo> queryByType(int moTypeId) {
		Collection<Mo> moCollection = mapByMoId.values();		
		List<Mo> moList = new LinkedList();
		for (Mo mo : moCollection) {
			if (mo.isRealMo() && mo.getTypeId() == moTypeId) {
				moList.add(mo);
			}
		}
		sortMoList(moList);
		
		return moList;
	}
	
	/**
	 * 对Mo列表进行排序
	 * @param moList
	 */
	private void sortMoList(List<Mo> moList) {
		Collections.sort(moList, new Comparator<Mo>() {
			@Override
			public int compare(Mo o1, Mo o2) {
				return o1.getDisplayName().compareTo(o2.getDisplayName());
			}
			
		});
	}
	
	/**
	 * 增加或更新Mo
	 * @param mo
	 */
	public void addOrUpdate(Mo mo) {
		mapByMoId.put(mo.getMoId(), mo);
	}
	
	
	/**
	 * 删除MO
	 * @param moId
	 */
	public void delete(Long moId) {
		mapByMoId.remove(moId);
	}
	
	// FIXME: 以下代码在适当的时候将删除
	
	/**
	 * 增加MO
	 * @param mo
	 */
	public void add(Mo mo) {
		mapByMoId.put(mo.getMoId(), mo);
	}
	
	/**
	 * 更新MO(只更新名称和描述)
	 * @param mo
	 */
	public void update(Mo mo) {
		Mo cacheMo = mapByMoId.get(mo.getMoId());
		cacheMo.setName(mo.getName());
		cacheMo.setDescription(mo.getDescription());	
	}
	
	
	
	/**
	 * 修改管理状态
	 * @param mo
	 */
	public void changeManageState(Mo mo) {
		Mo cacheMo = mapByMoId.get(mo.getMoId());
		cacheMo.setManageStateCode(mo.getManageStateCode());
	}
	 
	

	
}
