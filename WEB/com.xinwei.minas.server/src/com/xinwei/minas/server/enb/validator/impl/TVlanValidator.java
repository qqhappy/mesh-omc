/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-31	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.validator.impl;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.dao.EnbBizConfigDAO;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.core.model.meta.XMetaRef;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * VLAN表校验类
 * 
 * @author fanhaoyu
 * 
 */

public class TVlanValidator implements EnbBizDataValidator {
	
	// 业务类型是key
	private EnbBizDataValidateHelper validateHelper;
	
	private EnbBizConfigDAO enbBizConfigDAO;
	
	public void setEnbBizConfigDAO(EnbBizConfigDAO enbBizConfigDAO) {
		this.enbBizConfigDAO = enbBizConfigDAO;
	}
	
	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}
	
	@Override
	public void validate(long moId, XBizRecord bizRecord, String actionType)
			throws Exception {
		// V3.0.4新增,如果T_IPV4表中的u8VlanIndex字段引用了T_VLAN表中的u8Id则不允许修改或者删除
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		// 如果是3.0.4版本及以后
		if (enb.getProtocolVersion().compareTo("3.0.4") >= 0) {
			if (actionType.equals(ActionTypeDD.DELETE)
					|| actionType.equals(ActionTypeDD.MODIFY)) {
				// 获取关联字段
				String relationField = getRelationField(moId);
				// 如果存在关联字段
				if (!"".equals(relationField)) {
					XBizRecord condition = new XBizRecord();
					condition.addField(new XBizField(relationField, bizRecord
							.getStringValue(EnbConstantUtils.FIELD_NAME_ID)));
					// 如果有关联字段,查询是否有关联数据
					XBizTable ipv4Table = enbBizConfigDAO.query(moId,
							EnbConstantUtils.TABLE_NAME_T_IPV4, condition);
					if (ipv4Table.getRecords().size() > 0) {
						throw new Exception(OmpAppContext.getMessage(actionType
								+ "_Fail_T_VLAN_IPV4_relation"));
					}
				}
			}
			
		}
		else {
			if (actionType.equals(ActionTypeDD.DELETE)) {
				return;
			}
			int currentVlanId = validateHelper.getIntFieldValue(bizRecord,
					EnbConstantUtils.FIELD_NAME_ID);
			
			List<XBizRecord> vlanRecords = validateHelper.queryRecords(moId,
					EnbConstantUtils.TABLE_NAME_T_VLAN, null);
			List<Integer> serviceTypeList = new ArrayList<Integer>();
			if (vlanRecords != null) {
				for (XBizRecord vlanRecord : vlanRecords) {
					int vlanId = validateHelper.getIntFieldValue(vlanRecord,
							EnbConstantUtils.FIELD_NAME_ID);
					if (currentVlanId == vlanId)
						continue;
					int serviceType = validateHelper.getIntFieldValue(vlanRecord,
							EnbConstantUtils.FIELD_NAME_SERVICE_TYPE);
					serviceTypeList.add(serviceType);
				}
				int currentType = validateHelper.getIntFieldValue(bizRecord,
						EnbConstantUtils.FIELD_NAME_SERVICE_TYPE);
				if (serviceTypeList.contains(currentType)) {
					throw validateHelper.newBizException(
							EnbConstantUtils.FIELD_NAME_SERVICE_TYPE,
							"serviceType_already_used");
				}
			}
		}
		
	}
	
	public String getRelationField(long moId) {
		String relationField = "";
		// 查询T_IPV4是否有关联字段
		XList ipv4XList = EnbBizHelper.getBizMetaBy(moId,
				EnbConstantUtils.TABLE_NAME_T_IPV4);
		List<XList> fieldXLists = ipv4XList.getAllFields();
		for (XList fieldXList : fieldXLists) {
			List<XMetaRef> refList = fieldXList.getFieldRefs();
			for (XMetaRef xMetaRef : refList) {
				if (xMetaRef.getRefTable().equals(
						EnbConstantUtils.TABLE_NAME_T_VLAN)
						|| xMetaRef.getKeyColumn().equals(
								EnbConstantUtils.FIELD_NAME_ID)) {
					relationField = fieldXList.getName();
					break;
				}
			}
		}
		return relationField;
	}
	
}
