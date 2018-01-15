/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.validator.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * SI调度配置参数表数据校验类
 * 
 * @author fanhaoyu
 * 
 */

public class TCelSischDataValidator implements EnbBizDataValidator {

	// 1，对于相同u8CId的记录中不能有重复的sib；
	// 2，一条记录不容许不配置sib；
	// 3，对于相同的u8CId记录中必须至少有sib2（等同实现：新添加一个小区的第一条SI记录必须是Sib2）；
	// 4，T_ENB_PARA表的u8PttEnable字段为1时，必须配置一条只有u8SibPtt为TRUE，其它Sib字段为FALSE的记录。

	private EnbBizDataValidateHelper validateHelper;

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}

	@Override
	public void validate(long moId, XBizRecord bizRecord, String actionType)
			throws Exception {
		// 该表无需校验删除操作
		if (actionType.equals(ActionTypeDD.DELETE))
			return;

		int[] currentFlags = validateHelper.getSibFlag(bizRecord);
		checkSibContained(currentFlags);
		// 当前记录中是否包含sib2
		boolean sib2Contained = currentFlags[0] == 1;
		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID);
		List<XBizRecord> sisRecords = validateHelper.queryRecordsByCellId(moId,
				cellId, EnbConstantUtils.TABLE_NAME_T_CEL_SISCH);
		int currentSiId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_SIID);
		// 其他记录中是否包含sib2
		sib2Contained = sib2Contained
				|| isSib2Contained(currentSiId, sisRecords);
		if (!sib2Contained) {
			throw validateHelper.newBizException(
					EnbConstantUtils.FIELD_NAME_SIB2,
					"sib2_must_be_contained_in_each_cell");
		}
		// 校验是否有重复的sib
		checkRepeatSib(moId, currentSiId, currentFlags, sisRecords);
		// pttSib是否单独配置在一条记录中
		checkSibPtt(moId, currentFlags);
	}

	/**
	 * 一条记录不容许不配置sib
	 * 
	 * @param bizRecord
	 * @throws Exception
	 */
	private void checkSibContained(int[] sibFlags) throws Exception {
		// 校验是否包含Sib
		if (!validateHelper.isSibContained(sibFlags)) {
			throw validateHelper.newBizException(
					EnbConstantUtils.FIELD_NAME_CELL_ID,
					"sib_must_be_contained");
		}
	}

	/**
	 * 除当前记录外的其他记录是否包含sib2
	 * 
	 * @param moId
	 * @param bizRecord
	 * @throws Exception
	 */
	private boolean isSib2Contained(int currentSiId, List<XBizRecord> sisRecords)
			throws Exception {
		if (sisRecords == null)
			return false;
		for (XBizRecord record : sisRecords) {
			int siId = validateHelper.getIntFieldValue(record,
					EnbConstantUtils.FIELD_NAME_SIID);
			if (siId == currentSiId)
				continue;
			int[] sibFlags = validateHelper.getSibFlag(record);
			if (sibFlags[0] == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 校验是否有重复的sib
	 * 
	 * @param currentSiId
	 * @param currentFlags
	 * @param sisRecords
	 * @throws Exception
	 */
	private void checkRepeatSib(long moId, int currentSiId, int[] currentFlags,
			List<XBizRecord> sisRecords) throws Exception {
		// 其他记录中是否有sib的flag
		int[] hasSibFlags = { 0, 0, 0, 0, 0 };
		if (sisRecords != null) {
			for (XBizRecord record : sisRecords) {
				int siId = validateHelper.getIntFieldValue(record,
						EnbConstantUtils.FIELD_NAME_SIID);
				if (siId == currentSiId)
					continue;
				int[] sibFlags = validateHelper.getSibFlag(record);
				hasSibFlags = orSibFlag(hasSibFlags, sibFlags);
			}
		}
		// sib是否重复的flag
		int[] repeatFlags = currentFlags;
		repeatFlags = andSibFlag(repeatFlags, hasSibFlags);
		String repeatSibName = getRepeatSibName(repeatFlags);
		if (!StringUtils.isBlank(repeatSibName)) {
			throw validateHelper.newBizException(repeatSibName,
					"sib_is_open_in_other_record");
		}
		hasSibFlags = orSibFlag(hasSibFlags, currentFlags);
		boolean hasPttSib = hasSibFlags[4] == 1;
		// 集群开关是否打开
		boolean pttEnabled = validateHelper.isPttEnabled(moId);
		// 集群开关打开时，pttSib必须打开
		if (pttEnabled && !hasPttSib) {
			throw validateHelper.newBizException(
					EnbConstantUtils.FIELD_NAME_SIBPTT,
					"sisch_record_of_all_cell_must_contain_pptsib");
		}
	}

	/**
	 * pttsib必须单独配置在一条记录中
	 * 
	 * @param moId
	 * @param currentFlags
	 * @throws Exception
	 */
	private void checkSibPtt(long moId, int[] currentFlags) throws Exception {
		// 当前记录是否存在除pttSib以外的其他sib
		boolean hasOtherPtt = currentFlags[0] == 1 || currentFlags[1] == 1
				|| currentFlags[2] == 1 || currentFlags[3] == 1;
		boolean hasPttSib = currentFlags[4] == 1;
		if (hasPttSib && hasOtherPtt) {
			// 集群Sib必须单独配置在一条记录中
			throw validateHelper.newBizException(
					EnbConstantUtils.FIELD_NAME_SIBPTT,
					"pttsib_must_enable_individually");
		}

	}

	/**
	 * Sib标识与运算
	 * 
	 * @param flags1
	 * @param flags2
	 * @return
	 */
	private int[] andSibFlag(int[] flags1, int[] flags2) {
		int[] flags = new int[flags1.length];
		for (int i = 0; i < flags.length; i++) {
			flags[i] = flags1[i] & flags2[i];
		}
		return flags;
	}

	/**
	 * Sib标识或运算
	 * 
	 * @param flags1
	 * @param flags2
	 * @return
	 */
	private int[] orSibFlag(int[] flags1, int[] flags2) {
		int[] flags = new int[flags1.length];
		for (int i = 0; i < flags.length; i++) {
			flags[i] = flags1[i] | flags2[i];
		}
		return flags;
	}

	/**
	 * 获取重复的sib名称
	 * 
	 * @param flags
	 * @return
	 */
	private String getRepeatSibName(int[] flags) {
		int index = -1;
		for (int i = 0; i < flags.length; i++) {
			if (flags[i] == 1) {
				index = i;
				break;
			}
		}
		switch (index) {
		case 0:
			return EnbConstantUtils.FIELD_NAME_SIB2;
		case 1:
			return EnbConstantUtils.FIELD_NAME_SIB3;
		case 2:
			return EnbConstantUtils.FIELD_NAME_SIB4;
		case 3:
			return EnbConstantUtils.FIELD_NAME_SIB5;
		case 4:
			return EnbConstantUtils.FIELD_NAME_SIBPTT;
		default:
			return "";
		}
	}
}
