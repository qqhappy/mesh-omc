/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-29	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.model.sysManage.LocationArea;
import com.xinwei.minas.server.mcbts.dao.sysManage.LocAreaDAO;
import com.xinwei.minas.server.mcbts.service.sysManage.LocAreaService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 位置区服务类
 * 
 * 
 * @author chenshaohua
 * 
 */

public class LocAreaServiceImpl implements LocAreaService {
	private  Log log = LogFactory.getLog(LocAreaServiceImpl.class);
	private LocAreaDAO locAreaDAO;
	private SequenceService sequenceService;

	public LocAreaServiceImpl() {
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	public void setLocAreaDAO(LocAreaDAO locAreaDAO) {
		this.locAreaDAO = locAreaDAO;
	}

	/**
	 * 查询所有位置区信息
	 */
	@Override
	public List<LocationArea> queryAll() {
		return locAreaDAO.queryAll();
	}

	/**
	 * 新增或更新位置区信息
	 * 
	 * @param la
	 */
	@Override
	public void saveOrUpdate(LocationArea la) {
		if (la == null)
			return;

		if (la.getIdx() == null) {
			la.setIdx(sequenceService.getNext());
		}

		locAreaDAO.saveOrUpdate(la);
	}

	/**
	 * 删除一个位置区信息
	 * 
	 * @param la
	 */
	@Override
	public void delete(LocationArea la) {
		locAreaDAO.delete(la);
	}

	@Override
	public void config(List<LocationArea> locationAreaList) throws Exception {
		// 保存数据库
		try {
			locAreaDAO.saveOrUpdate(locationAreaList);

		} catch (Exception e) {
			log.error(e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}
	}

}
