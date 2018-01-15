/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.validator.impl;

import java.util.List;

import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.helper.EnbBizUniqueIdHelper;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * С������������У����
 * 
 * @author fanhaoyu
 * 
 */

public class TCelParaDataValidator implements EnbBizDataValidator {
	// (1)T_CEL_PRACH���е�u8PrachFreqOffsetС�ڵ���u8SysBandWidth��������RB������ȥ6��
	// (2)T_CEL_PUCH���е�u8DeltaPucchShift��u8N_RB2��u16N_SrChn��T_CEL_PARA����u8SysBandWidth�ֶ�֮��Լ����ϵ��
	// u8N_RB2+(u16N_SrChn\(36\(u8DeltaPucchShift)))���ܳ���u8SysBandWidth��������RB������
	// (3)T_CEL_ALG.u8CfiȡֵΪ4ʱu8SysBandWidthֻ��ȡֵΪ0
	// (4)��u8UlDlSlotAllocΪ2ʱ��T_CEL_PUCH���е�u8SRITransPrd��u8RptCqiPrd����ͬʱΪ5ms��
	// u8UlDlSlotAlloc����Ϊ0ʱ��T_CEL_PTT.u8PttBPagingSubFN��ȡ0��5��
	// u8UlDlSlotAlloc����Ϊ1ʱ��T_CEL_PTT.u8PttBPagingSubFN��ȡ0��4��5��9��
	// u8UlDlSlotAlloc����Ϊ2ʱ��T_CEL_PTT.u8PttBPagingSubFN��ȡ0 ��3��4��5��8��9��
	// u8UlDlSlotAlloc����Ϊ3ʱ��T_CEL_PTT.u8PttBPagingSubFN��ȡ0��5��6��7��8��9��
	// u8UlDlSlotAlloc����Ϊ4ʱ��T_CEL_PTT.u8PttBPagingSubFN��ȡ0��4��5��6��7��8��9��
	// u8UlDlSlotAlloc����Ϊ5ʱ��T_CEL_PTT.u8PttBPagingSubFN��ȡ0��3��4��5��6��7��8��9��
	// u8UlDlSlotAlloc����Ϊ6ʱ��T_CEL_PTT.u8PttBPagingSubFN��ȡ0��5��9��
	// (5) u8UlDlSlotAllocΪ0ʱ��T_CEL_PRACH.u8PrachCfgIndexȡֵ������(11,19)��
	// u8UlDlSlotAllocΪ1ʱ��T_CEL_PRACH.u8PrachCfgIndexȡֵ������(8,13,14,40-47)��
	// u8UlDlSlotAllocΪ2ʱ��T_CEL_PRACH.u8PrachCfgIndexȡֵ������(5,7,8,11,13,14,17,19-47)��
	// u8UlDlSlotAllocΪ3ʱ��T_CEL_PRACH.u8PrachCfgIndexȡֵ������(10,11,19,22,24,32,34,42,44,50,52)��
	// u8UlDlSlotAllocΪ4ʱ��T_CEL_PRACH.u8PrachCfgIndexȡֵ������(5,7,8,11,13,14,17,19,22,24,32,34,40-47,50,52)��
	// u8UlDlSlotAllocΪ5ʱ��T_CEL_PRACH.u8PrachCfgIndexȡֵ������(2,4,5,7,8,10,11,13,14,16,17,19-47,50,52)��
	// u8UlDlSlotAllocΪ6ʱ��T_CEL_PRACH.u8PrachCfgIndexȡֵ������(16,17,42,44)��
	// (6) ͬƵС����PCI������ͬ
	// (7)T_CEL_NBRCEL����u8SvrCID��T_CEL_PARA���е�u8CId���
	// ��u8CenterFreqCfgIdxΪ255��¼�е�u16PhyCellId������T_CEL_PARA��Ӧ��¼�е�u16PhyCellId�ֶ����
	// (8) T_CEL_DLPC���u16CellSpeRefSigPwr�����õ����ֵ�����¹�ʽ������
	// CRS_EPRE_max = T_CEL_DLPC.u16CellTransPwr - 10*log10(u8DlAntPortNum) +
	// ERS_nor��
	// ��ʽ���漰���ֶζ��ǰ��ս���ֵ��P�������㣬����ERS_nor���ɵã�����T_CEL_DLPC��ĸ���
	// (9)u8IntraFreqHOMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ3
	// (10)u8A2ForInterFreqMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ2
	// (11)u8A1ForInterFreqMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ1
	// (12)u8A2ForRedirectMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ2
	// (13)u8IcicA3MeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ3
	// (14)��ͬu8CId��������ͬ��¼��u8TopoNO�����ظ�
	// (15)T_CEL_ALG���еģ�u8UlRbNum*b8UlPreSchEnable�������ֵ������u8SysBandWidth��Ӧ��RB����
	// (16)T_CEL_ALG���еģ�u8DlRbNum*b8DlPreSchEnable�������ֵ������u8SysBandWidth��Ӧ��RB����
	// (17)T_CEL_ALG���еģ�u8UlMaxRbNum*b8UlRbEnable�������ֵ������u8SysBandWidth��Ӧ��RB����
	// (18)T_CEL_ALG���еģ�u8UlMinRbNum*b8UlRbEnable�������ֵ������u8SysBandWidth��Ӧ��RB����
	// (19)T_CEL_ALG���еģ�u8DlMaxRbNum*b8DlRbEnable�������ֵ������u8SysBandWidth��Ӧ��RB����
	// (20)u8SysBandWidthȡֵΪ20M��15Mʱ��T_CEL_ALG.��u8DlMaxRbNum*b8DlRbEnable��ȡ4����������
	// u8SysBandWidthȡֵΪ10Mʱ��T_CEL_ALG.��u8DlMaxRbNum*b8DlRbEnable��ȡ3����������
	// u8SysBandWidthȡֵΪ5Mʱ��T_CEL_ALG.��u8DlMaxRbNum*b8DlRbEnable��ȡ2����������
	// (21)T_CEL_ALG���еģ�u8DlMinRbNum*b8DlRbEnable�������ֵ������u8SysBandWidth��Ӧ��RB����
	// (22)T_CEL_PTT���еģ�u8PttDlMaxRbNum*b8PttDlRbEnable�������ֵ������u8SysBandWidth��Ӧ��RB��
	// (23)u8SysBandWidthȡֵΪ20M��15Mʱ��T_CEL_PTT.(u8PttDlMaxRbNum*b8PttDlRbEnable)ȡ4����������u8SysBandWidthȡֵΪ10Mʱ��T_CEL_PTT.(u8PttDlMaxRbNum*b8PttDlRbEnable)ȡ3����������u8SysBandWidthȡֵΪ5Mʱ��T_CEL_PTT.(u8PttDlMaxRbNum*b8PttDlRbEnable)ȡ2��������
	
	// (24)u8UlDlSlotAlloc����Ϊ0ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3��4��7��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ1ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3��7��8������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ2ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��7������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ3ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3��4������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ4ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ5ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ6ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3��4��7��8������Ϊ1
	// (25)u8UlDlSlotAlloc����Ϊ0ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��5��6������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ1ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��4��5��6��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ2ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��3��4��5��6��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ3ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��5��6��7��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ4ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��4��5��6��7��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ5ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��3��4��5��6��7��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ6ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��5��6��9������Ϊ1
	
	// u8UeTransMode����Ϊ0ʱ��T_CEL_ALG.u8TS��ȡ0��3��
	// u8UeTransModeΪ1ʱ��T_CEL_ALG.u8TS��ȡ0��1��3��
	// u8UeTransMode����Ϊ2ʱ��T_CEL_ALG.u8TS��ȡ0��1��2��3
	// ��14����u8SysBandWidth����Ϊ5Mʱ��T_CEL_PUCH.u8SrsBwCfgIndex��������Ϊ0��1
	// ��15����u8SysBandWidth����Ϊ3Mʱ��T_CEL_PUCH.u8SrsBwCfgIndexֻ������5-7
	// ��16����u8SysBandWidth����Ϊ1.4Mʱ��T_CEL_PUCH.u8SrsBwCfgIndexֻ������7
	
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
		
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		// ��ȡС��ID
		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID);
		// ��ȡ�����ڴ�ֵ
		int sysBandWidth = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
		
		checkCellParaAndPrach(enb, cellId, bizRecord);
		
		checkCellParaAndPuch(enb, cellId, bizRecord);
		// ABC�����У���ж�
		if(ValidatorSwitchHelper.AB_C_CHECK_OPEN == ValidatorSwitchHelper.AB_C_CHECK_SWITCH) {
			checkAlgAndSysBandWidth(enb, cellId, sysBandWidth);
		}
		
		// checkSysBandWidthAndPttDlMaxRbNum(enb, cellId, sysBandWidth);
		
		checkUlDlSlotAllocAndCellPtt(moId, cellId, bizRecord);
		
		// ABC�����У���ж�
		if(ValidatorSwitchHelper.AB_C_CHECK_OPEN == ValidatorSwitchHelper.AB_C_CHECK_SWITCH) {
			checkCellParaAndCellAlgPara(moId, cellId, bizRecord);
		}
		
		// ȫ��PCI�����ظ�
		/*validateHelper.checkUnique(moId, bizRecord,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
		*/
		// T_CEL_NBRCEL����u8SvrCID��T_CEL_PARA���е�u8CId�����u8CenterFreqCfgIdxΪ255��¼�е�u16PhyCellId������T_CEL_PARA��Ӧ��¼�е�u16PhyCellId�ֶ����
		validateHelper.checkNeighborCellAndCellPara(moId, bizRecord, true);
		// T_CEL_DLPC���С���ο��źŹ����Ƿ񳬹���Χ
		checkCellSpeRefSigPwr(enb, cellId, bizRecord);
		// У��MEASCFG���
		checkMeasCfg(moId, bizRecord);
		// ��ͬu8CId��������ͬ��¼��u8TopoNO�����ظ�
		checkTopoNo(moId, cellId, bizRecord);
	}
	
	private void checkAlgAndSysBandWidth(Enb enb, int cellId, int sysBandWidth)
			throws Exception {
		// ��ѯALG���ӦС���ļ�¼
		List<XBizRecord> algRecords = validateHelper.queryRecordsByCellId(
				enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CEL_ALG);
		if (algRecords != null) {
			XBizRecord algRecord = algRecords.get(0);
			validateHelper.checkAlgAndSysBandWidth(enb.getEnbType(),
					enb.getProtocolVersion(), algRecord, sysBandWidth, false);
		}
	}

	private void checkCellParaAndPrach(Enb enb, int cellId, XBizRecord bizRecord)
			throws Exception {
		
		// ��ѯPRACH���ӦС���ļ�¼
		List<XBizRecord> prachRecords = validateHelper.queryRecordsByCellId(
				enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CEL_PRACH);
		if (prachRecords != null) {
			XBizRecord prachRecord = prachRecords.get(0);
			validateHelper.checkCellParaAndPrach(enb.getEnbType(),
					enb.getProtocolVersion(), bizRecord, prachRecord, false);
		}
	}
	
	private void checkCellParaAndPuch(Enb enb, int cellId, XBizRecord bizRecord)
			throws Exception {
		// ��ѯPUCH���ӦС���ļ�¼
		List<XBizRecord> puchRecords = validateHelper.queryRecordsByCellId(
				enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PUCH);
		if (puchRecords != null) {
			XBizRecord puchRecord = puchRecords.get(0);
			validateHelper.checkCellParaAndPuch(enb.getEnbType(),
					enb.getProtocolVersion(), bizRecord, puchRecord, false);
			// ��ȡ�����ڴ�ֵ
			int sysBandWidth = validateHelper.getIntFieldValue(bizRecord,
					EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
			// SRS��������
			int srsBwCfgIndex = validateHelper.getIntFieldValue(puchRecord,
					EnbConstantUtils.FIELD_NAME_SRS_BW_CFG_INDEX);
			
			validateHelper
					.checkSysBandWidthAndSrsBwCfgIndex(enb.getEnbType(),
							enb.getProtocolVersion(), sysBandWidth,
							srsBwCfgIndex, true);
			// �汾�����Դ���
			boolean hasSps = EnbBizHelper.hasBizMeta(enb.getEnbType(),enb.getProtocolVersion(),
					EnbConstantUtils.TABLE_NAME_T_CEL_SPS);
			if (hasSps) {
				List<XBizRecord> spsRecords = validateHelper
						.queryRecordsByCellId(enb.getMoId(), cellId,
								EnbConstantUtils.TABLE_NAME_T_CEL_SPS);
				if (spsRecords != null) {
					XBizRecord spsRecord = spsRecords.get(0);
					validateHelper.checkSpsANChnAndSysBandWidthAndPuch(enb.getEnbType(),
							enb.getProtocolVersion(), sysBandWidth, spsRecord,
							puchRecord,
							EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
				}
				
			}
			else {
				validateHelper.checkSysBandWidthAndPuch(enb.getEnbType(),
						enb.getProtocolVersion(), bizRecord, puchRecord, false);
			}
		}
	}
	
	private void checkUlDlSlotAllocAndCellPtt(long moId, int cellId,
			XBizRecord bizRecord) throws Exception {
		int ulDlSlotAlloc = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC);
		// ��ѯ��ӦС����Ⱥ���ò������¼
		List<XBizRecord> pttRecords = validateHelper.queryRecordsByCellId(moId,
				cellId, EnbConstantUtils.TABLE_NAME_T_CEL_PTT);
		if (pttRecords != null) {
			XBizRecord pttRecord = pttRecords.get(0);
			validateHelper.checkUlDlSlotAllocAndCellPtt(ulDlSlotAlloc,
					pttRecord, false);
		}
	}
	
	
	/**
	 * ��֤��������������
	 * 
	 * @param moId
	 * @param bizRecord
	 * @throws Exception
	 */
	private void checkMeasCfg(long moId, XBizRecord bizRecord) throws Exception {
		
		List<XBizRecord> measCfgRecords = validateHelper.queryRecords(moId,
				EnbConstantUtils.TABLE_NAME_T_ENB_MEASCFG, null);
		
		for (XBizRecord measCfgRecord : measCfgRecords) {
			
			// (16)u8IntraFreqHOMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ3
			String fieldName = EnbConstantUtils.FIELD_NAME_INTRA_FREQ_HO_MEAS_CFG;
			validateHelper.checkMeasCfgOk(fieldName, bizRecord, measCfgRecord,
					false);
			// (17)u8A2ForInterFreqMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ2
			fieldName = EnbConstantUtils.FIELD_NAME_A2_FOR_INTER_FREQ_MEAS_CFG;
			validateHelper.checkMeasCfgOk(fieldName, bizRecord, measCfgRecord,
					false);
			// (18)u8A1ForInterFreqMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ1
			fieldName = EnbConstantUtils.FIELD_NAME_A1_FOR_INTER_FREQ_MEAS_CFG;
			validateHelper.checkMeasCfgOk(fieldName, bizRecord, measCfgRecord,
					false);
			// (19)u8A2ForRedirectMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ2
			fieldName = EnbConstantUtils.FIELD_NAME_A2_FOR_REDIRECT_MEAS_CFG;
			validateHelper.checkMeasCfgOk(fieldName, bizRecord, measCfgRecord,
					false);
			// (20)u8IcicA3MeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ3
			fieldName = EnbConstantUtils.FIELD_NAME_ICIC_A3_MEAS_CFG;
			validateHelper.checkMeasCfgOk(fieldName, bizRecord, measCfgRecord,
					false);
		}
		
	}
	
	/**
	 * ��֤���˺��Ƿ�ռ��
	 * 
	 * @param moId
	 * @param cellId
	 * @param bizRecord
	 * @throws Exception
	 */
	public void checkTopoNo(long moId, int cellId, XBizRecord bizRecord)
			throws Exception {
		String currentTopo = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_TOPO_NO).getValue();
		List<XBizRecord> records = validateHelper.queryRecords(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
		if (records == null)
			return;
		for (XBizRecord record : records) {
			int cell = validateHelper.getIntFieldValue(record,
					EnbConstantUtils.FIELD_NAME_CELL_ID);
			if (cell == cellId)
				continue;
			String topo = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_TOPO_NO).getValue();
			// ���˺ű�ռ��
			if (currentTopo.equals(topo)) {
				throw validateHelper.newBizException(
						EnbConstantUtils.FIELD_NAME_TOPO_NO,
						"topo_no_has_been_referenced");
			}
		}
	}
	
	private void checkCellSpeRefSigPwr(Enb enb, int cellId, XBizRecord bizRecord)
			throws Exception {
		List<XBizRecord> records = validateHelper.queryRecordsByCellId(
				enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CEL_DLPC);
		if (records == null)
			return;
		XBizRecord dlpcRecord = records.get(0);
		validateHelper.checkCellParaAndDlpc(enb.getEnbType(),
				enb.getProtocolVersion(), dlpcRecord, bizRecord, false);
	}
	
	/**
	 * У��С����Ⱥ�������ñ��еļ�Ⱥ�������RB��
	 * 
	 * @param enb
	 * @param cellId
	 * @param sysBandWidth
	 * @throws Exception
	 */
	private void checkSysBandWidthAndPttDlMaxRbNum(Enb enb, int cellId,
			int sysBandWidth) throws Exception {
		List<XBizRecord> pttRecords = validateHelper.queryRecordsByCellId(
				enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CEL_PTT);
		if (pttRecords == null)
			return;
		XBizRecord pttRecord = pttRecords.get(0);
		XBizField dlMaxRbNumField = pttRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_PTT_DL_MAX_RB_NUM);
		XBizField dlRbEnableField = pttRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_PTT_DL_RB_ENABLE);
		
		validateHelper.checkSysBandWidthAndDlMaxRbNum(enb.getEnbType(),
				enb.getProtocolVersion(), sysBandWidth, dlMaxRbNumField,
				dlRbEnableField, true);
	}
	
	private void checkCellParaAndCellAlgPara(long moId, int cellId,
			XBizRecord bizRecord) throws Exception {
		int ulDlSlotAlloc = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC);
		int ueTransMode = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_UE_TRANS_MODE);
		// ��ѯ��ӦС���㷨�������¼
		List<XBizRecord> algRecords = validateHelper.queryRecordsByCellId(moId,
				cellId, EnbConstantUtils.TABLE_NAME_T_CEL_ALG);
		if (algRecords != null) {
			XBizRecord algRecord = algRecords.get(0);
			validateHelper.checkUlDlSlotAllocAndCellAlgPara(ulDlSlotAlloc,
					algRecord, false);
			validateHelper.checkUeTransModeAndCellAlgPara(ueTransMode,
					algRecord, false);
		}
	}



}
