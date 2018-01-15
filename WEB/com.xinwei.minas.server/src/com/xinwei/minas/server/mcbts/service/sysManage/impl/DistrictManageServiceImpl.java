/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-14	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.model.sysManage.District;
import com.xinwei.minas.server.mcbts.dao.sysManage.DistrictManageDAO;
import com.xinwei.minas.server.mcbts.service.sysManage.DistrictManageService;

/**
 * 
 * 地域管理服务类
 * 
 * @author tiance
 * 
 */

public class DistrictManageServiceImpl implements DistrictManageService {

	private DistrictManageDAO districtManageDAO;

	private SequenceService sequenceService;

	@Override
	public List<District> queryAll() {
		return districtManageDAO.queryAll();
	}

	@Override
	public void saveOrUpdate(District district) {
		if (district.getId() == 0L) {
			district.setId(sequenceService.getNext());
		}
		districtManageDAO.saveOrUpdate(district);
	}

	@Override
	public void delete(District district) {
		districtManageDAO.delete(district);
	}

	// spring赋值bean
	public void setDistrictManageDAO(DistrictManageDAO districtManageDAO) {
		this.districtManageDAO = districtManageDAO;
	}

	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

}
