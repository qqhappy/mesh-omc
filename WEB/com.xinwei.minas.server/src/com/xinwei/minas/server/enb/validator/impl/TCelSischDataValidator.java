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
 * SI�������ò���������У����
 * 
 * @author fanhaoyu
 * 
 */

public class TCelSischDataValidator implements EnbBizDataValidator {

	// 1��������ͬu8CId�ļ�¼�в������ظ���sib��
	// 2��һ����¼����������sib��
	// 3��������ͬ��u8CId��¼�б���������sib2����ͬʵ�֣������һ��С���ĵ�һ��SI��¼������Sib2����
	// 4��T_ENB_PARA���u8PttEnable�ֶ�Ϊ1ʱ����������һ��ֻ��u8SibPttΪTRUE������Sib�ֶ�ΪFALSE�ļ�¼��

	private EnbBizDataValidateHelper validateHelper;

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}

	@Override
	public void validate(long moId, XBizRecord bizRecord, String actionType)
			throws Exception {
		// �ñ�����У��ɾ������
		if (actionType.equals(ActionTypeDD.DELETE))
			return;

		int[] currentFlags = validateHelper.getSibFlag(bizRecord);
		checkSibContained(currentFlags);
		// ��ǰ��¼���Ƿ����sib2
		boolean sib2Contained = currentFlags[0] == 1;
		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID);
		List<XBizRecord> sisRecords = validateHelper.queryRecordsByCellId(moId,
				cellId, EnbConstantUtils.TABLE_NAME_T_CEL_SISCH);
		int currentSiId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_SIID);
		// ������¼���Ƿ����sib2
		sib2Contained = sib2Contained
				|| isSib2Contained(currentSiId, sisRecords);
		if (!sib2Contained) {
			throw validateHelper.newBizException(
					EnbConstantUtils.FIELD_NAME_SIB2,
					"sib2_must_be_contained_in_each_cell");
		}
		// У���Ƿ����ظ���sib
		checkRepeatSib(moId, currentSiId, currentFlags, sisRecords);
		// pttSib�Ƿ񵥶�������һ����¼��
		checkSibPtt(moId, currentFlags);
	}

	/**
	 * һ����¼����������sib
	 * 
	 * @param bizRecord
	 * @throws Exception
	 */
	private void checkSibContained(int[] sibFlags) throws Exception {
		// У���Ƿ����Sib
		if (!validateHelper.isSibContained(sibFlags)) {
			throw validateHelper.newBizException(
					EnbConstantUtils.FIELD_NAME_CELL_ID,
					"sib_must_be_contained");
		}
	}

	/**
	 * ����ǰ��¼���������¼�Ƿ����sib2
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
	 * У���Ƿ����ظ���sib
	 * 
	 * @param currentSiId
	 * @param currentFlags
	 * @param sisRecords
	 * @throws Exception
	 */
	private void checkRepeatSib(long moId, int currentSiId, int[] currentFlags,
			List<XBizRecord> sisRecords) throws Exception {
		// ������¼���Ƿ���sib��flag
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
		// sib�Ƿ��ظ���flag
		int[] repeatFlags = currentFlags;
		repeatFlags = andSibFlag(repeatFlags, hasSibFlags);
		String repeatSibName = getRepeatSibName(repeatFlags);
		if (!StringUtils.isBlank(repeatSibName)) {
			throw validateHelper.newBizException(repeatSibName,
					"sib_is_open_in_other_record");
		}
		hasSibFlags = orSibFlag(hasSibFlags, currentFlags);
		boolean hasPttSib = hasSibFlags[4] == 1;
		// ��Ⱥ�����Ƿ��
		boolean pttEnabled = validateHelper.isPttEnabled(moId);
		// ��Ⱥ���ش�ʱ��pttSib�����
		if (pttEnabled && !hasPttSib) {
			throw validateHelper.newBizException(
					EnbConstantUtils.FIELD_NAME_SIBPTT,
					"sisch_record_of_all_cell_must_contain_pptsib");
		}
	}

	/**
	 * pttsib���뵥��������һ����¼��
	 * 
	 * @param moId
	 * @param currentFlags
	 * @throws Exception
	 */
	private void checkSibPtt(long moId, int[] currentFlags) throws Exception {
		// ��ǰ��¼�Ƿ���ڳ�pttSib���������sib
		boolean hasOtherPtt = currentFlags[0] == 1 || currentFlags[1] == 1
				|| currentFlags[2] == 1 || currentFlags[3] == 1;
		boolean hasPttSib = currentFlags[4] == 1;
		if (hasPttSib && hasOtherPtt) {
			// ��ȺSib���뵥��������һ����¼��
			throw validateHelper.newBizException(
					EnbConstantUtils.FIELD_NAME_SIBPTT,
					"pttsib_must_enable_individually");
		}

	}

	/**
	 * Sib��ʶ������
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
	 * Sib��ʶ������
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
	 * ��ȡ�ظ���sib����
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
