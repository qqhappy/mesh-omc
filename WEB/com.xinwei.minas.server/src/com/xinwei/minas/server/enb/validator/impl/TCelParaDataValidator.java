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
 * 小区参数表数据校验类
 * 
 * @author fanhaoyu
 * 
 */

public class TCelParaDataValidator implements EnbBizDataValidator {
	// (1)T_CEL_PRACH表中的u8PrachFreqOffset小于等于u8SysBandWidth代表的最大RB个数减去6；
	// (2)T_CEL_PUCH表中的u8DeltaPucchShift、u8N_RB2、u16N_SrChn与T_CEL_PARA表中u8SysBandWidth字段之间约束关系：
	// u8N_RB2+(u16N_SrChn\(36\(u8DeltaPucchShift)))不能超过u8SysBandWidth代表的最大RB个数；
	// (3)T_CEL_ALG.u8Cfi取值为4时u8SysBandWidth只能取值为0
	// (4)当u8UlDlSlotAlloc为2时，T_CEL_PUCH表中的u8SRITransPrd与u8RptCqiPrd不能同时为5ms；
	// u8UlDlSlotAlloc配置为0时，T_CEL_PTT.u8PttBPagingSubFN可取0、5；
	// u8UlDlSlotAlloc配置为1时，T_CEL_PTT.u8PttBPagingSubFN可取0、4、5、9；
	// u8UlDlSlotAlloc配置为2时，T_CEL_PTT.u8PttBPagingSubFN可取0 、3、4、5、8、9；
	// u8UlDlSlotAlloc配置为3时，T_CEL_PTT.u8PttBPagingSubFN可取0、5、6、7、8、9；
	// u8UlDlSlotAlloc配置为4时，T_CEL_PTT.u8PttBPagingSubFN可取0、4、5、6、7、8、9；
	// u8UlDlSlotAlloc配置为5时，T_CEL_PTT.u8PttBPagingSubFN可取0、3、4、5、6、7、8、9；
	// u8UlDlSlotAlloc配置为6时，T_CEL_PTT.u8PttBPagingSubFN可取0、5、9；
	// (5) u8UlDlSlotAlloc为0时，T_CEL_PRACH.u8PrachCfgIndex取值不能是(11,19)，
	// u8UlDlSlotAlloc为1时，T_CEL_PRACH.u8PrachCfgIndex取值不能是(8,13,14,40-47)，
	// u8UlDlSlotAlloc为2时，T_CEL_PRACH.u8PrachCfgIndex取值不能是(5,7,8,11,13,14,17,19-47)，
	// u8UlDlSlotAlloc为3时，T_CEL_PRACH.u8PrachCfgIndex取值不能是(10,11,19,22,24,32,34,42,44,50,52)，
	// u8UlDlSlotAlloc为4时，T_CEL_PRACH.u8PrachCfgIndex取值不能是(5,7,8,11,13,14,17,19,22,24,32,34,40-47,50,52)，
	// u8UlDlSlotAlloc为5时，T_CEL_PRACH.u8PrachCfgIndex取值不能是(2,4,5,7,8,10,11,13,14,16,17,19-47,50,52)，
	// u8UlDlSlotAlloc为6时，T_CEL_PRACH.u8PrachCfgIndex取值不能是(16,17,42,44)；
	// (6) 同频小区的PCI不能相同
	// (7)T_CEL_NBRCEL表中u8SvrCID与T_CEL_PARA表中的u8CId相等
	// 且u8CenterFreqCfgIdx为255记录中的u16PhyCellId不能与T_CEL_PARA对应记录中的u16PhyCellId字段相等
	// (8) T_CEL_DLPC表的u16CellSpeRefSigPwr可配置的最大值由如下公式决定：
	// CRS_EPRE_max = T_CEL_DLPC.u16CellTransPwr - 10*log10(u8DlAntPortNum) +
	// ERS_nor，
	// 公式中涉及的字段都是按照界面值（P）来计算，其中ERS_nor查表可得，表格见T_CEL_DLPC表的附件
	// (9)u8IntraFreqHOMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为3
	// (10)u8A2ForInterFreqMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为2
	// (11)u8A1ForInterFreqMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为1
	// (12)u8A2ForRedirectMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为2
	// (13)u8IcicA3MeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为3
	// (14)不同u8CId（多条不同记录）u8TopoNO不能重复
	// (15)T_CEL_ALG表中的（u8UlRbNum*b8UlPreSchEnable）的最大值不超过u8SysBandWidth对应的RB数；
	// (16)T_CEL_ALG表中的（u8DlRbNum*b8DlPreSchEnable）的最大值不超过u8SysBandWidth对应的RB数；
	// (17)T_CEL_ALG表中的（u8UlMaxRbNum*b8UlRbEnable）的最大值不超过u8SysBandWidth对应的RB数；
	// (18)T_CEL_ALG表中的（u8UlMinRbNum*b8UlRbEnable）的最大值不超过u8SysBandWidth对应的RB数；
	// (19)T_CEL_ALG表中的（u8DlMaxRbNum*b8DlRbEnable）的最大值不超过u8SysBandWidth对应的RB数；
	// (20)u8SysBandWidth取值为20M、15M时，T_CEL_ALG.（u8DlMaxRbNum*b8DlRbEnable）取4的整数倍；
	// u8SysBandWidth取值为10M时，T_CEL_ALG.（u8DlMaxRbNum*b8DlRbEnable）取3的整数倍；
	// u8SysBandWidth取值为5M时，T_CEL_ALG.（u8DlMaxRbNum*b8DlRbEnable）取2的整数倍；
	// (21)T_CEL_ALG表中的（u8DlMinRbNum*b8DlRbEnable）的最大值不超过u8SysBandWidth对应的RB数；
	// (22)T_CEL_PTT表中的（u8PttDlMaxRbNum*b8PttDlRbEnable）的最大值不超过u8SysBandWidth对应的RB数
	// (23)u8SysBandWidth取值为20M、15M时，T_CEL_PTT.(u8PttDlMaxRbNum*b8PttDlRbEnable)取4的整数倍；u8SysBandWidth取值为10M时，T_CEL_PTT.(u8PttDlMaxRbNum*b8PttDlRbEnable)取3的整数倍；u8SysBandWidth取值为5M时，T_CEL_PTT.(u8PttDlMaxRbNum*b8PttDlRbEnable)取2的整数倍
	
	// (24)u8UlDlSlotAlloc配置为0时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3、4、7、8、9可配置为1；
	// u8UlDlSlotAlloc配置为1时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3、7、8可配置为1；
	// u8UlDlSlotAlloc配置为2时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、7可配置为1；
	// u8UlDlSlotAlloc配置为3时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3、4可配置为1；
	// u8UlDlSlotAlloc配置为4时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3可配置为1；
	// u8UlDlSlotAlloc配置为5时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2可配置为1；
	// u8UlDlSlotAlloc配置为6时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3、4、7、8可配置为1
	// (25)u8UlDlSlotAlloc配置为0时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、5、6可配置为1；
	// u8UlDlSlotAlloc配置为1时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、4、5、6、9可配置为1；
	// u8UlDlSlotAlloc配置为2时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、3、4、5、6、8、9可配置为1；
	// u8UlDlSlotAlloc配置为3时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、5、6、7、8、9可配置为1；
	// u8UlDlSlotAlloc配置为4时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、4、5、6、7、8、9可配置为1；
	// u8UlDlSlotAlloc配置为5时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、3、4、5、6、7、8、9可配置为1；
	// u8UlDlSlotAlloc配置为6时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、5、6、9可配置为1
	
	// u8UeTransMode配置为0时，T_CEL_ALG.u8TS可取0、3；
	// u8UeTransMode为1时，T_CEL_ALG.u8TS可取0、1、3；
	// u8UeTransMode配置为2时，T_CEL_ALG.u8TS可取0、1、2、3
	// （14）当u8SysBandWidth配置为5M时，T_CEL_PUCH.u8SrsBwCfgIndex不能配置为0、1
	// （15）当u8SysBandWidth配置为3M时，T_CEL_PUCH.u8SrsBwCfgIndex只能配置5-7
	// （16）当u8SysBandWidth配置为1.4M时，T_CEL_PUCH.u8SrsBwCfgIndex只能配置7
	
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
		
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		// 获取小区ID
		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID);
		// 获取带宽内存值
		int sysBandWidth = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
		
		checkCellParaAndPrach(enb, cellId, bizRecord);
		
		checkCellParaAndPuch(enb, cellId, bizRecord);
		// ABC类参数校验判断
		if(ValidatorSwitchHelper.AB_C_CHECK_OPEN == ValidatorSwitchHelper.AB_C_CHECK_SWITCH) {
			checkAlgAndSysBandWidth(enb, cellId, sysBandWidth);
		}
		
		// checkSysBandWidthAndPttDlMaxRbNum(enb, cellId, sysBandWidth);
		
		checkUlDlSlotAllocAndCellPtt(moId, cellId, bizRecord);
		
		// ABC类参数校验判断
		if(ValidatorSwitchHelper.AB_C_CHECK_OPEN == ValidatorSwitchHelper.AB_C_CHECK_SWITCH) {
			checkCellParaAndCellAlgPara(moId, cellId, bizRecord);
		}
		
		// 全网PCI不能重复
		/*validateHelper.checkUnique(moId, bizRecord,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
		*/
		// T_CEL_NBRCEL表中u8SvrCID与T_CEL_PARA表中的u8CId相等且u8CenterFreqCfgIdx为255记录中的u16PhyCellId不能与T_CEL_PARA对应记录中的u16PhyCellId字段相等
		validateHelper.checkNeighborCellAndCellPara(moId, bizRecord, true);
		// T_CEL_DLPC表的小区参考信号功率是否超过范围
		checkCellSpeRefSigPwr(enb, cellId, bizRecord);
		// 校验MEASCFG相关
		checkMeasCfg(moId, bizRecord);
		// 不同u8CId（多条不同记录）u8TopoNO不能重复
		checkTopoNo(moId, cellId, bizRecord);
	}
	
	private void checkAlgAndSysBandWidth(Enb enb, int cellId, int sysBandWidth)
			throws Exception {
		// 查询ALG表对应小区的记录
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
		
		// 查询PRACH表对应小区的记录
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
		// 查询PUCH表对应小区的记录
		List<XBizRecord> puchRecords = validateHelper.queryRecordsByCellId(
				enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PUCH);
		if (puchRecords != null) {
			XBizRecord puchRecord = puchRecords.get(0);
			validateHelper.checkCellParaAndPuch(enb.getEnbType(),
					enb.getProtocolVersion(), bizRecord, puchRecord, false);
			// 获取带宽内存值
			int sysBandWidth = validateHelper.getIntFieldValue(bizRecord,
					EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
			// SRS带宽配置
			int srsBwCfgIndex = validateHelper.getIntFieldValue(puchRecord,
					EnbConstantUtils.FIELD_NAME_SRS_BW_CFG_INDEX);
			
			validateHelper
					.checkSysBandWidthAndSrsBwCfgIndex(enb.getEnbType(),
							enb.getProtocolVersion(), sysBandWidth,
							srsBwCfgIndex, true);
			// 版本兼容性处理
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
		// 查询对应小区集群配置参数表记录
		List<XBizRecord> pttRecords = validateHelper.queryRecordsByCellId(moId,
				cellId, EnbConstantUtils.TABLE_NAME_T_CEL_PTT);
		if (pttRecords != null) {
			XBizRecord pttRecord = pttRecords.get(0);
			validateHelper.checkUlDlSlotAllocAndCellPtt(ulDlSlotAlloc,
					pttRecord, false);
		}
	}
	
	
	/**
	 * 验证测量配置相关外键
	 * 
	 * @param moId
	 * @param bizRecord
	 * @throws Exception
	 */
	private void checkMeasCfg(long moId, XBizRecord bizRecord) throws Exception {
		
		List<XBizRecord> measCfgRecords = validateHelper.queryRecords(moId,
				EnbConstantUtils.TABLE_NAME_T_ENB_MEASCFG, null);
		
		for (XBizRecord measCfgRecord : measCfgRecords) {
			
			// (16)u8IntraFreqHOMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为3
			String fieldName = EnbConstantUtils.FIELD_NAME_INTRA_FREQ_HO_MEAS_CFG;
			validateHelper.checkMeasCfgOk(fieldName, bizRecord, measCfgRecord,
					false);
			// (17)u8A2ForInterFreqMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为2
			fieldName = EnbConstantUtils.FIELD_NAME_A2_FOR_INTER_FREQ_MEAS_CFG;
			validateHelper.checkMeasCfgOk(fieldName, bizRecord, measCfgRecord,
					false);
			// (18)u8A1ForInterFreqMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为1
			fieldName = EnbConstantUtils.FIELD_NAME_A1_FOR_INTER_FREQ_MEAS_CFG;
			validateHelper.checkMeasCfgOk(fieldName, bizRecord, measCfgRecord,
					false);
			// (19)u8A2ForRedirectMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为2
			fieldName = EnbConstantUtils.FIELD_NAME_A2_FOR_REDIRECT_MEAS_CFG;
			validateHelper.checkMeasCfgOk(fieldName, bizRecord, measCfgRecord,
					false);
			// (20)u8IcicA3MeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为3
			fieldName = EnbConstantUtils.FIELD_NAME_ICIC_A3_MEAS_CFG;
			validateHelper.checkMeasCfgOk(fieldName, bizRecord, measCfgRecord,
					false);
		}
		
	}
	
	/**
	 * 验证拓扑号是否被占用
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
			// 拓扑号被占用
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
	 * 校验小区集群参数配置表中的集群下行最大RB数
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
		// 查询对应小区算法参数表记录
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
