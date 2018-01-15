/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-28	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.service.impl;

import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.server.core.conf.dao.MoBasicDAO;
import com.xinwei.minas.server.core.conf.service.MoBasicService;
import com.xinwei.minas.server.core.conf.service.MoCache;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 被管对象基本服务实现
 * 
 * @author chenjunhua
 * 
 */

public class MoBasicServiceImpl implements MoBasicService {
	
	private MoBasicDAO moBasicDAO;
	
	
	public void setMoBasicDAO(MoBasicDAO moBasicDAO) {
		this.moBasicDAO = moBasicDAO;
	}
	
	
	/**
	 * 查找所有MO
	 * 
	 * @return
	 */
	public List<Mo> queryAll() {
		return MoCache.getInstance().queryAll();
	}

	/**
	 * 获取制定类型的MO
	 * 
	 * @param moTypeId
	 *            类型
	 * @return
	 */
	public List<Mo> queryByType(int moTypeId) {
		return MoCache.getInstance().queryByType(moTypeId);
	}

	/**
	 * 增加被管对象
	 * 
	 * @param mo
	 *            被管对象
	 * @param regionId
	 *            归属地区编号
	 */
	public void add(Mo mo, long regionId) {
		SequenceService sequenceService = AppContext.getCtx().getBean(SequenceService.class);
		long moId = sequenceService.getNext();
		mo.setMoId(moId);
		// 更新数据库
		moBasicDAO.add(mo, regionId);
		// 更新缓存
		MoCache.getInstance().add(mo);
	}
	

	/**
	 * 修改MO
	 * 
	 * @param mo
	 */
	public void modify(Mo mo) {
		// 更新数据库
		moBasicDAO.saveOrUpdate(mo);
		// 更新缓存
		MoCache.getInstance().update(mo);
	}

	/**
	 * 删除指定ID的MO
	 * 
	 * @param moId
	 */
	public void delete(Long moId) {
		// 更新数据库
		moBasicDAO.delete(moId);
		// 更新缓存
		MoCache.getInstance().delete(moId);
	}
	
	
	/**
	 * 修改MO管理状态
	 * 
	 * @param mo
	 */
	public void changeManageState(Mo mo) {		
		// 更新数据库
		moBasicDAO.saveOrUpdate(mo);
		// 更新缓存
		MoCache.getInstance().changeManageState(mo);
	}


	/**
	 * 获取指定moId的 MO
	 * 
	 * @param moId
	 * @return
	 */
	public Mo queryByMoId(Long moId) {
		// TODO Auto-generated method stub
		return MoCache.getInstance().queryByMoId(moId);
	}
}
