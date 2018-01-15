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
 * MO����
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
	 * ��������MO
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
	 * ��ȡָ��MO
	 * @param moId
	 * @return
	 */
	public Mo queryByMoId(Long moId) {
		return mapByMoId.get(moId);
	}
	
	
	/**
	 * ��ȡ�ƶ����͵�MO
	 * 
	 * @param moTypeId
	 *            ����
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
	 * ��Mo�б��������
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
	 * ���ӻ����Mo
	 * @param mo
	 */
	public void addOrUpdate(Mo mo) {
		mapByMoId.put(mo.getMoId(), mo);
	}
	
	
	/**
	 * ɾ��MO
	 * @param moId
	 */
	public void delete(Long moId) {
		mapByMoId.remove(moId);
	}
	
	// FIXME: ���´������ʵ���ʱ��ɾ��
	
	/**
	 * ����MO
	 * @param mo
	 */
	public void add(Mo mo) {
		mapByMoId.put(mo.getMoId(), mo);
	}
	
	/**
	 * ����MO(ֻ�������ƺ�����)
	 * @param mo
	 */
	public void update(Mo mo) {
		Mo cacheMo = mapByMoId.get(mo.getMoId());
		cacheMo.setName(mo.getName());
		cacheMo.setDescription(mo.getDescription());	
	}
	
	
	
	/**
	 * �޸Ĺ���״̬
	 * @param mo
	 */
	public void changeManageState(Mo mo) {
		Mo cacheMo = mapByMoId.get(mo.getMoId());
		cacheMo.setManageStateCode(mo.getManageStateCode());
	}
	 
	

	
}
