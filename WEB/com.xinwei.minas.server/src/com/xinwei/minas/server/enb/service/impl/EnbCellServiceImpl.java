/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-7	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.EnbCellStart;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbNeighbourRecord;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.minas.enb.core.model.corenet.EnbGlobalConfig;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.dao.EnbBizConfigDAO;
import com.xinwei.minas.server.enb.enbapi.DataInputInfo;
import com.xinwei.minas.server.enb.enbapi.EnbIputItem;
import com.xinwei.minas.server.enb.enbapi.EnbSceneOutput;
import com.xinwei.minas.server.enb.enbapi.EnbSceneRuleEngine;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.helper.EnbBizUniqueIdHelper;
import com.xinwei.minas.server.enb.service.EnbBizConfigService;
import com.xinwei.minas.server.enb.service.EnbBizTemplateService;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbCellService;
import com.xinwei.minas.server.enb.service.EnbGlobalConfigService;
import com.xinwei.minas.server.enb.service.EnbNeighbourService;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.impl.TCelParaDataValidator;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizSceneTable;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 
 * @author chenlong
 * 
 */

public class EnbCellServiceImpl implements EnbCellService {
	
	private Log log = LogFactory.getLog(EnbCellServiceImpl.class);
	
	private EnbBizTemplateService enbBizTemplateService;
	
	private EnbGlobalConfigService enbGlobalConfigService;
	
	private EnbBizConfigDAO enbBizConfigDAO;
	
	private EnbBizDataValidateHelper validateHelper;
	
	private EnbBizConfigService enbBizConfigService;
	
	private TCelParaDataValidator tCelParaDataValidator;
	
	private EnbNeighbourService enbNeighbourService;
	
	@Override
	public void add(long moId, EnbCellStart enbCellStart) throws Exception {
		try {
			Enb enb = EnbCache.getInstance().queryByMoId(moId);
			if (enb == null) {
				throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
			}
			long enbId = enb.getEnbId();
			int cid = enbCellStart.getCid();
			// ��ѯȫ���������㷨����
			EnbGlobalConfig config = enbGlobalConfigService
					.queryEnbGlobalConfig();
			
			enbCellStart.setMcc(config.getMcc());
			enbCellStart.setMnc(config.getMnc());
			// У��
			validate(enb, enbCellStart);
			// ���߼������к�����С����ʶ���뻺��
			/*EnbBizUniqueIdHelper.addPciCache(enbId, cid,
					enbCellStart.getPhyCellId());
			EnbBizUniqueIdHelper.addRsiCache(enbId, cid,
					enbCellStart.getRootSeqIndex());*/
			// ͨ����վ������ȡ���ɵ�����
			EnbSceneOutput sceneOutput = getSceneOutput(enbCellStart);
			Map<String, Map<String, String>> sceneOutputMap = sceneOutput
					.getTableMap();
			// ��ȡС����ر�ģ������
			Map<String, List<XBizRecord>> cellTemplateData = getCellTemplateData(enb);
			
			// ��¼ͬ������
			Map<String, XBizRecord> synchroData = new HashMap<String, XBizRecord>();
			
			for (String tableName : cellTemplateData.keySet()) {
				List<XBizRecord> records = cellTemplateData.get(tableName);
				for (XBizRecord record : records) {
					// �滻С��cid
					replaceCid(enb, enbCellStart.getCid(), tableName, record);
					// �ÿ�վ���û�ȡ��ģ������滻�����ݿ�ģ�����
					Map<String, String> outputMap = sceneOutputMap
							.get(tableName);
					if (null != outputMap) {
						for (String key : outputMap.keySet()) {
							// �ж�api�з��ص��ֶ�����enb��ǰ�汾���Ƿ����
							if (!EnbBizHelper.fieldIsExist(enb.getEnbType(),
									enb.getProtocolVersion(), tableName, key)) {
								System.out.println("table=" + tableName
										+ ",field=" + key + " is not exist.");
								log.error("EnbCellService add error,table="
										+ tableName + ",field=" + key
										+ " is not exist.");
								continue;
							}
							// �����С�����й��ز����� T_CEL_DLPC ���� ��΢վ 
							if (EnbTypeDD.XW7102 == enb.getEnbType()
									&& EnbConstantUtils.TABLE_NAME_T_CEL_DLPC
											.equals(tableName)) {
								if(EnbConstantUtils.FIELD_NAME_CELL_TRANS_PWR
										.equals(key) || EnbConstantUtils.FIELD_NAME_CELL_SPE_REF_SIG_PWR.equals(key)) {
									continue;
								}
							}
							
							XBizField bizField = new XBizField(key,
									outputMap.get(key));
							record.addField(bizField);
						}
					}
					// �û���������Ĳ����滻��ģ�����
					replaceData(record, tableName, enbCellStart);
					// ����¼�е�hexArray�����ֶε�ֵת��Сд
					EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
							enb.getProtocolVersion(), tableName, record);
					enbBizConfigDAO.add(enb.getMoId(), tableName, record);
					synchroData.put(tableName, record);
				}
			}
			// ��װС��������Ϣ
			XBizRecord bizSenceRecord = getSenceRecord(enbCellStart);
			// ����С��������Ϣ
			enbBizConfigDAO.addScene(moId, cid, bizSenceRecord);
			
			// �����Ԫ����,��ͬ������
			if (enb.isConfigurable()) {
				enbBizConfigService.compareAndSyncEmsDataToNe(moId);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	
	/**
	 * ��ģ�������е�С��cid��ֵ�滻��
	 * 
	 * @param enb
	 * 
	 * @param cid
	 * @param tableName
	 * @param record
	 */
	private void replaceCid(Enb enb, int cid, String tableName,
			XBizRecord record) {
		// С��������ֱ���滻
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
			XBizField cidField = new XBizField(
					EnbConstantUtils.FIELD_NAME_CELL_ID, String.valueOf(cid));
			record.addField(cidField);
		}
		// С����ر�,�ҵ�����cid���ֶ�,Ȼ���滻
		else {
			String cidRelatedField = EnbBizHelper.getCidRelatedField(
					enb.getEnbType(), enb.getProtocolVersion(), tableName);
			XBizField cidField = new XBizField(cidRelatedField,
					String.valueOf(cid));
			record.addField(cidField);
		}
	}
	
	/**
	 * ��װ��վ�����������
	 * 
	 * @param enbCellStart
	 * @return
	 */
	public XBizRecord getSenceRecord(EnbCellStart enbCellStart) {
		XBizRecord bizRecord = new XBizRecord();
		bizRecord.addField(new XBizField(EnbCellStart.FIELD_SCENE, String
				.valueOf(enbCellStart.getSceneId())));
		bizRecord.addField(new XBizField(EnbCellStart.FIELD_AN_NUM, String
				.valueOf(enbCellStart.getAnNumId())));
		bizRecord.addField(new XBizField(EnbCellStart.FIELD_RRU_TYPE, String
				.valueOf(enbCellStart.getRruTypeId())));
		return bizRecord;
	}
	
	/**
	 * У��С����վ��������
	 * 
	 * @param enb
	 * @param enbCellStart
	 * @throws Exception
	 */
	public void validate(Enb enb, EnbCellStart enbCellStart) throws Exception {
		// У�������
		XBizTable cellParaTable = enbBizConfigDAO.query(enb.getMoId(),
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
		if (null != cellParaTable && null != cellParaTable.getRecords()) {
			int recordNum = cellParaTable.getRecords().size();
			// ��ȡ�����ֵ䶨��ı�����
			XList bizMeta = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
					enb.getProtocolVersion(),
					EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
			int tableSize = bizMeta.getTableSize();
			if (recordNum >= tableSize) {
				// ���¼��������ֵ
				throw new Exception(OmpAppContext.getMessage(
						"table_size_over_threshold",
						new Integer[] { tableSize }));
			}
		}
		
		// ��װУ������
		XBizRecord bizRecord = new XBizRecord();
		bizRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_CELL_ID,
				String.valueOf(enbCellStart.getCid())));
		bizRecord.addField(new XBizField(
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID, String
						.valueOf(enbCellStart.getPhyCellId())));
		bizRecord.addField(new XBizField(
				EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX, String
						.valueOf(enbCellStart.getRootSeqIndex())));
		bizRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_TOPO_NO,
				String.valueOf(enbCellStart.getTopoNO())));
		// У��rru����
		tCelParaDataValidator.checkTopoNo(enb.getMoId(), enbCellStart.getCid(),
				bizRecord);
		
		// У��ͬenb�µ�С��ID�����ظ�
		checkCidExist(enb.getMoId(), enbCellStart.getCid());
		// У������С��IDȫ��Ψһ
		/*validateHelper.checkUniqueM(enb.getMoId(), bizRecord,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);*/
		// У���߼�������ȫ��Ψһ
		/*validateHelper.checkUniqueM(enb.getMoId(), bizRecord,
				EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
				EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX);*/
		// ��api��У��
		DataInputInfo dataInputInfo = new DataInputInfo();
		dataInputInfo.seteNBName(enb.getName());
		dataInputInfo.setTAC(enbCellStart.getTac());
		dataInputInfo.setCellLable(enbCellStart.getCellName());
		dataInputInfo.setFreqBandInd(enbCellStart.getFreqBandId());
		dataInputInfo.setCenterFreq(enbCellStart.getCenterFreq());
		dataInputInfo.setSysBandWidth(enbCellStart.getBandwidthId());
		dataInputInfo.setPhyCellId(enbCellStart.getPhyCellId());
		dataInputInfo.setTopoNO(enbCellStart.getTopoNO());
		dataInputInfo.setManualOP(enbCellStart.getManualOP());
		dataInputInfo.setRootSeqIndex(enbCellStart.getRootSeqIndex());
		dataInputInfo.setMCC(enbCellStart.getIntArray(enbCellStart.getMcc()));
		dataInputInfo.setMNC(enbCellStart.getIntArray(enbCellStart.getMnc()));
		EnbSceneRuleEngine.checkEnbPara(1, dataInputInfo);
		
	}
	
	/**
	 * У��С����ʶ�Ƿ����
	 * 
	 * @param moId
	 * @param cid
	 * @throws Exception
	 */
	public void checkCidExist(long moId, int cid) throws Exception {
		XBizRecord cellParaKey = new XBizRecord();
		cellParaKey.addField(new XBizField(EnbConstantUtils.FIELD_NAME_CELL_ID,
				String.valueOf(cid)));
		XBizRecord cellParaRecord = enbBizConfigDAO.queryByKey(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, cellParaKey);
		if (null != cellParaRecord) {
			throw new Exception(OmpAppContext.getMessage("cid_exist"));
		}
	}
	
	/**
	 * �滻��ģ����������Ҫ�滻������
	 * 
	 * @param record
	 * @param tableName
	 * @param enbCellStart
	 */
	public void replaceData(XBizRecord record, String tableName,
			EnbCellStart enbCellStart) {
		// �滻��T_CEL_PARA������ز���(С�����ơ�Ƶ��ָʾ��ϵͳ������֡��ȡ�����С����ʶ������Ƶ�㡢�������롢RRU���塢����״̬)
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
			// С������
			XBizField cellNameField = new XBizField(
					EnbConstantUtils.FIELD_NAME_CELL_NAME,
					enbCellStart.getCellName());
			// Ƶ��ָʾ
			XBizField freqBandField = new XBizField(
					EnbConstantUtils.FIELD_NAME_FREQ_BAND,
					String.valueOf(enbCellStart.getFreqBandId()));
			// ������
			XBizField bandwidthField = new XBizField(
					EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH,
					String.valueOf(enbCellStart.getBandwidthId()));
			// ��֡���
			XBizField sfCfgField = new XBizField(
					EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC,
					String.valueOf(enbCellStart.getSfCfgId()));
			// ����С����ʶ
			XBizField phyCellField = new XBizField(
					EnbConstantUtils.FIELD_NAME_PHY_CELL_ID,
					String.valueOf(enbCellStart.getPhyCellId()));
			// ����Ƶ��
			XBizField centerFreqField = new XBizField(
					EnbConstantUtils.FIELD_NAME_CENTER_FREQ,
					String.valueOf(enbCellStart.getCenterFreq()));
			// ��������
			XBizField tacField = new XBizField(EnbConstantUtils.FIELD_NAME_TAC,
					String.valueOf(enbCellStart.getTac()));
			
			// RRU����
			XBizField topoNOField = new XBizField(
					EnbConstantUtils.FIELD_NAME_TOPO_NO,
					String.valueOf(enbCellStart.getTopoNO()));
			// ����״̬
			XBizField manualOPField = new XBizField("u8ManualOP",
					String.valueOf(enbCellStart.getManualOP()));
			// �ƶ�������
			XBizField mmcField = new XBizField(EnbConstantUtils.FIELD_NAME_MCC,
					String.valueOf(enbCellStart.getMcc()));
			// �ƶ�������
			XBizField mncField = new XBizField(EnbConstantUtils.FIELD_NAME_MNC,
					String.valueOf(enbCellStart.getMnc()));
			record.addField(cellNameField);
			record.addField(freqBandField);
			record.addField(bandwidthField);
			record.addField(sfCfgField);
			record.addField(phyCellField);
			record.addField(centerFreqField);
			record.addField(tacField);
			record.addField(topoNOField);
			record.addField(manualOPField);
			record.addField(mmcField);
			record.addField(mncField);
			
		}
		// �滻��T_CEL_PRACH������ز���(�߼�������)
		else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_PRACH)) {
			XBizField rootSeqIndexField = new XBizField(
					EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX,
					String.valueOf(enbCellStart.getRootSeqIndex()));
			record.addField(rootSeqIndexField);
		}
	}
	
	/**
	 * ���ݿ�վ�������ݴ�api�л�ȡ���ɵ�����
	 * 
	 * @param enbCellStart
	 * @return
	 * @throws Exception
	 */
	public EnbSceneOutput getSceneOutput(EnbCellStart enbCellStart)
			throws Exception {
		List<EnbIputItem> inputList = new ArrayList<EnbIputItem>();
		// ��װ�������
		inputList.add(new EnbIputItem(EnbCellStart.ID_SCENE, enbCellStart
				.getSceneId()));
		inputList.add(new EnbIputItem(EnbCellStart.ID_FREQ_BAND, enbCellStart
				.getFreqBandId()));
		inputList.add(new EnbIputItem(EnbCellStart.ID_SYS_BAND_WIDTH,
				enbCellStart.getBandwidthId()));
		inputList.add(new EnbIputItem(EnbCellStart.ID_SF_CFG, enbCellStart
				.getSfCfgId()));
		inputList.add(new EnbIputItem(EnbCellStart.ID_RRU_TYPE, enbCellStart
				.getRruTypeId()));
		inputList.add(new EnbIputItem(EnbCellStart.ID_AN_NUM, enbCellStart
				.getAnNumId()));
		// ����API��ȡ�������
		EnbSceneOutput output = EnbSceneRuleEngine.generate(1, inputList);
		return output;
	}
	
	/**
	 * ������ģ�����ݹ��˳�С����ص�ģ������
	 * 
	 * @param enb
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<XBizRecord>> getCellTemplateData(Enb enb)
			throws Exception {
		// ��ȡ����ģ������
		Map<String, List<XBizRecord>> tableMap = enbBizTemplateService
				.queryTemplateData(enb.getEnbType(), enb.getProtocolVersion());
		// ���˳�С��ģ������,���˳�cid��Ϊ252������
		Map<String, List<XBizRecord>> resultMap = new HashMap<String, List<XBizRecord>>();
		for (String tableName : tableMap.keySet()) {
			if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)
					|| EnbBizHelper.isCellRelatedTable(enb.getEnbType(),
							enb.getProtocolVersion(), tableName)) {
				List<XBizRecord> records = tableMap.get(tableName);
				List<XBizRecord> resultList = new ArrayList<XBizRecord>();
				for (XBizRecord record : records) {
					// ��ȡС��ID�ڱ��е��ֶ�
					String cidRelatedField = "";
					// �����С��������
					if (tableName
							.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
						cidRelatedField = EnbConstantUtils.FIELD_NAME_CELL_ID;
					}
					// �����С����ر�
					else {
						cidRelatedField = EnbBizHelper.getCidRelatedField(
								enb.getEnbType(), enb.getProtocolVersion(),
								tableName);
					}
					String cid = record.getStringValue(cidRelatedField);
					if ("252".equals(cid)) {
						resultList.add(record);
					}
				}
				resultMap.put(tableName, resultList);
			}
		}
		return resultMap;
	}
	
	@Override
	public XBizSceneTable queryByMoId(long moId) throws Exception {
		try {
			// ��ѯС������������
			XBizTable cellBizTable = enbBizConfigService.queryFromEms(moId,
					EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
			List<XBizRecord> records = cellBizTable.getRecords();
			for (XBizRecord record : records) {
				// ����cid��ѯ��С����������
				XBizRecord cellSceneRecord = enbBizConfigDAO
						.querySceneByMoIdAndCid(
								moId,
								record.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID));
				if (null != cellSceneRecord) {
					// �ϲ�����
					for (String bizField : cellSceneRecord.getFieldMap()
							.keySet()) {
						record.addField(cellSceneRecord.getFieldMap().get(
								bizField));
					}
				}
				// ������ǰ̨������
				else {
					record.addField(new XBizField(EnbCellStart.FIELD_SCENE, ""));
					record.addField(new XBizField(EnbCellStart.FIELD_RRU_TYPE,
							""));
					record.addField(new XBizField(EnbCellStart.FIELD_AN_NUM, ""));
				}
				// ��ȡT_CELL_PRACH�������
				XBizRecord celPrachKey = EnbBizHelper.getKeyRecordBy(moId,
						EnbConstantUtils.TABLE_NAME_T_CEL_PRACH, record);
				// ��ѯT_CELL_PRACH�������
				XBizRecord celPrachRecord = enbBizConfigDAO.queryByKey(moId,
						EnbConstantUtils.TABLE_NAME_T_CEL_PRACH, celPrachKey);
				if (null != celPrachRecord) {
					// �ϲ�����
					for (String bizField : celPrachRecord.getFieldMap()
							.keySet()) {
						record.addField(celPrachRecord.getFieldMap().get(
								bizField));
					}
				}
				// ������ǰ̨������
				else {
					record.addField(new XBizField(
							EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX, ""));
				}
			}
			XBizSceneTable bizSceneTable = new XBizSceneTable(moId);
			bizSceneTable.setRecords(cellBizTable.getRecords());
			return bizSceneTable;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public void delete(long moId, int cid) throws Exception {
		try {
			Enb enb = EnbCache.getInstance().queryByMoId(moId);
			if (enb == null) {
				throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
			}
			
			// У��С�����Ƿ��������ɾ��������
			List<EnbNeighbourRecord> neighbourRecords = enbNeighbourService
					.queryNeighbourRecords(moId);
			for (EnbNeighbourRecord enbNeighbourRecord : neighbourRecords) {
				int nbCellId = enbNeighbourRecord.getBizRecord().getIntValue(
						EnbConstantUtils.FIELD_NAME_SVR_CID);
				// �ж�Ҫɾ����С�����Ƿ���������ϵ,����������ϵΪ����ɾ��
				if (cid == nbCellId
						&& 1 == enbNeighbourRecord.getBizRecord().getIntValue(
								"u8NoRemove")) {
					long nbEnbId = enbNeighbourRecord
							.getBizRecord()
							.getLongValue(EnbConstantUtils.FIELD_NAME_NBR_ENBID);
					throw new Exception(
							OmpAppContext.getMessage("nbrcel_cannot_delete1")
									+ EnbBizHelper.getHexEnbId(nbEnbId)
									+ OmpAppContext
											.getMessage("nbrcel_cannot_delete2")
									+ nbCellId
									+ OmpAppContext
											.getMessage("nbrcel_cannot_delete3"));
				}
			}
			
			List<String> cellRelatedTables = EnbBizHelper.getCellRelatedTables(
					enb.getEnbType(), enb.getProtocolVersion());
			// ɾ��С����ر�����
			for (String tableName : cellRelatedTables) {
				String cidRelatedField = EnbBizHelper.getCidRelatedField(
						enb.getEnbType(), enb.getProtocolVersion(), tableName);
				// ���ɲ�ѯ����
				XBizRecord condition = new XBizRecord();
				condition.addField(new XBizField(cidRelatedField, String
						.valueOf(cid)));
				XBizTable bizTable = enbBizConfigDAO.query(moId, tableName,
						condition);
				if (null != bizTable.getRecords()) {
					for (XBizRecord record : bizTable.getRecords()) {
						// ���������������,������������ɾ������
						if (tableName
								.equals(EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL)) {
							EnbNeighbourRecord enbNeighbourRecord = new EnbNeighbourRecord();
							enbNeighbourRecord.setBizRecord(record);
							enbNeighbourService.deleteNeighbour(moId,
									enbNeighbourRecord);
						}
						else {
							// ��ȡ����
							XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(
									moId, tableName, record);
							enbBizConfigDAO.delete(moId, tableName, bizKey);
						}
					}
				}
			}
			/*EnbBizUniqueIdHelper.deletePciCache(enb.getEnbId(), cid);
			EnbBizUniqueIdHelper.deleteRsiCache(enb.getEnbId(), cid);*/
			// ɾ����������
			enbBizConfigDAO.deleteScene(moId, cid);
			// ɾ��С������������
			XBizRecord cellParaKey = new XBizRecord();
			cellParaKey.addField(new XBizField(
					EnbConstantUtils.FIELD_NAME_CELL_ID, String.valueOf(cid)));
			enbBizConfigDAO.delete(moId,
					EnbConstantUtils.TABLE_NAME_T_CELL_PARA, cellParaKey);
			
			// �����Ԫ����,��ͬ������
			if (enb.isConfigurable()) {
				enbBizConfigService.compareAndSyncEmsDataToNe(moId);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("failed to delete cell, moId=" + moId + ", cid=" + cid, e);
			throw e;
		}
	}
	
	public void updateValidate(Enb enb, EnbCellStart enbCellStart)
			throws Exception {
		// ��ѯȫ���������㷨����
		EnbGlobalConfig config = enbGlobalConfigService.queryEnbGlobalConfig();
		
		enbCellStart.setMcc(config.getMcc());
		enbCellStart.setMnc(config.getMnc());
		// ��װУ������
		XBizRecord bizRecord = new XBizRecord();
		bizRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_CELL_ID,
				String.valueOf(enbCellStart.getCid())));
		bizRecord.addField(new XBizField(
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID, String
						.valueOf(enbCellStart.getPhyCellId())));
		bizRecord.addField(new XBizField(
				EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX, String
						.valueOf(enbCellStart.getRootSeqIndex())));
		bizRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_TOPO_NO,
				String.valueOf(enbCellStart.getTopoNO())));
		// У��rru����
		tCelParaDataValidator.checkTopoNo(enb.getMoId(), enbCellStart.getCid(),
				bizRecord);
		// У������С��IDȫ��Ψһ
		/*validateHelper.checkUniqueM(enb.getMoId(), bizRecord,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);*/
		// У���߼�������ȫ��Ψһ
		/*validateHelper.checkUniqueM(enb.getMoId(), bizRecord,
				EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
				EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX);*/
		// ��api��У��
		DataInputInfo dataInputInfo = new DataInputInfo();
		dataInputInfo.seteNBName(enb.getName());
		dataInputInfo.setTAC(enbCellStart.getTac());
		dataInputInfo.setCellLable(enbCellStart.getCellName());
		dataInputInfo.setFreqBandInd(enbCellStart.getFreqBandId());
		dataInputInfo.setCenterFreq(enbCellStart.getCenterFreq());
		dataInputInfo.setSysBandWidth(enbCellStart.getBandwidthId());
		dataInputInfo.setPhyCellId(enbCellStart.getPhyCellId());
		dataInputInfo.setTopoNO(enbCellStart.getTopoNO());
		dataInputInfo.setManualOP(enbCellStart.getManualOP());
		dataInputInfo.setRootSeqIndex(enbCellStart.getRootSeqIndex());
		dataInputInfo.setMCC(enbCellStart.getIntArray(enbCellStart.getMcc()));
		dataInputInfo.setMNC(enbCellStart.getIntArray(enbCellStart.getMnc()));
		EnbSceneRuleEngine.checkEnbPara(1, dataInputInfo);
	}
	
	@Override
	public void update(long moId, EnbCellStart enbCellStart) throws Exception {
		try {
			Enb enb = EnbCache.getInstance().queryByMoId(moId);
			if (enb == null) {
				throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
			}
			// ��ѯ��Ҫ���µ�С����������Ϣ
			XBizRecord cellParaKey = new XBizRecord();
			cellParaKey.addField(new XBizField(
					EnbConstantUtils.FIELD_NAME_CELL_ID, String
							.valueOf(enbCellStart.getCid())));
			XBizRecord cellParaRecord = enbBizConfigDAO.queryByKey(moId,
					EnbConstantUtils.TABLE_NAME_T_CELL_PARA, cellParaKey);
			
			if (null == cellParaRecord) {
				throw new Exception(OmpAppContext.getMessage("cell_not_exist"));
			}
			XBizRecord cellSceneRecord = enbBizConfigDAO
					.querySceneByMoIdAndCid(moId, enbCellStart.getCid());
			// �������������λ��,����ɾ����С������,���´����µ�����
			if (null == cellSceneRecord) {
				updateValidate(enb, enbCellStart);
				delete(moId, enbCellStart.getCid());
				add(moId, enbCellStart);
			}
			else {
				int scene = cellSceneRecord
						.getIntValue(EnbCellStart.FIELD_SCENE);
				int rruType = cellSceneRecord
						.getIntValue(EnbCellStart.FIELD_RRU_TYPE);
				int anNum = cellSceneRecord
						.getIntValue(EnbCellStart.FIELD_AN_NUM);
				int bandwidthId = cellParaRecord
						.getIntValue(EnbCellStart.FIELD_SYS_BANDWIDTH);
				int sfCfgId = cellParaRecord
						.getIntValue(EnbCellStart.FIELD_ULDL_SLOT_ALLOC);
				
				// ����޸ĳ�����Ϣ,����ɾ��,Ȼ��������������.
				if (scene != enbCellStart.getSceneId()
						|| rruType != enbCellStart.getRruTypeId()
						|| anNum != enbCellStart.getAnNumId()
						|| bandwidthId != enbCellStart.getBandwidthId()
						|| sfCfgId != enbCellStart.getSfCfgId()) {
					updateValidate(enb, enbCellStart);
					delete(moId, enbCellStart.getCid());
					add(moId, enbCellStart);
				}
				// ����޸ĵ��Ƿǳ�������,���޸Ķ�Ӧ��Ϣ
				else {
					// ��ѯ���ݿ�ԭ������ XBizRecord
					XBizRecord celPrachRecord = enbBizConfigDAO.queryByKey(
							moId, EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
							cellParaKey);
					// �����ƶ���������ƶ�������
					enbCellStart.setMcc(cellParaRecord
							.getStringValue(EnbConstantUtils.FIELD_NAME_MCC));
					enbCellStart.setMnc(cellParaRecord
							.getStringValue(EnbConstantUtils.FIELD_NAME_MNC));
					// �滻���û��޸ĵ�����
					replaceData(cellParaRecord,
							EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
							enbCellStart);
					replaceData(celPrachRecord,
							EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
							enbCellStart);
					// �����޸ķ���
					enbBizConfigService.update(moId,
							EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
							cellParaRecord);
					enbBizConfigService.update(moId,
							EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
							celPrachRecord);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public XBizRecord queryByCid(long moId, int cid) throws Exception {
		try {
			// ��ѯС������������
			XBizRecord cellParaKey = new XBizRecord();
			cellParaKey.addField(new XBizField(
					EnbConstantUtils.FIELD_NAME_CELL_ID, String.valueOf(cid)));
			XBizTable cellBizTable = enbBizConfigService.queryFromEms(moId,
					EnbConstantUtils.TABLE_NAME_T_CELL_PARA, cellParaKey);
			if (cellBizTable.getRecords().size() <= 0) {
				return null;
			}
			XBizRecord cellParaRecord = cellBizTable.getRecords().get(0);
			if (null == cellParaRecord) {
				return null;
			}
			// ��ѯ��С���������ݺ�T_CEL_PRACH�����ݲ��Һϲ�
			XBizRecord cellScneeRecord = enbBizConfigDAO
					.querySceneByMoIdAndCid(moId, cid);
			if (null != cellScneeRecord) {
				for (String bizField : cellScneeRecord.getFieldMap().keySet()) {
					cellParaRecord.addField(cellScneeRecord.getFieldMap().get(
							bizField));
				}
			}
			// ������ǰ̨������
			else {
				cellParaRecord.addField(new XBizField(EnbCellStart.FIELD_SCENE,
						""));
				cellParaRecord.addField(new XBizField(
						EnbCellStart.FIELD_RRU_TYPE, ""));
				cellParaRecord.addField(new XBizField(
						EnbCellStart.FIELD_AN_NUM, ""));
			}
			XBizRecord celPrachKey = EnbBizHelper.getKeyRecordBy(moId,
					EnbConstantUtils.TABLE_NAME_T_CEL_PRACH, cellParaKey);
			XBizRecord celPrachRecord = enbBizConfigDAO.queryByKey(moId,
					EnbConstantUtils.TABLE_NAME_T_CEL_PRACH, celPrachKey);
			if (null != celPrachRecord) {
				for (String bizField : celPrachRecord.getFieldMap().keySet()) {
					cellParaRecord.addField(celPrachRecord.getFieldMap().get(
							bizField));
				}
			}
			// ������ǰ̨������
			else {
				cellParaRecord.addField(new XBizField(
						EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX, ""));
			}
			return cellParaRecord;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void setEnbBizTemplateService(
			EnbBizTemplateService enbBizTemplateService) {
		this.enbBizTemplateService = enbBizTemplateService;
	}
	
	public void setEnbGlobalConfigService(
			EnbGlobalConfigService enbGlobalConfigService) {
		this.enbGlobalConfigService = enbGlobalConfigService;
	}
	
	public void setEnbBizConfigDAO(EnbBizConfigDAO enbBizConfigDAO) {
		this.enbBizConfigDAO = enbBizConfigDAO;
	}
	
	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}
	
	public void setEnbBizConfigService(EnbBizConfigService enbBizConfigService) {
		this.enbBizConfigService = enbBizConfigService;
	}
	
	public void settCelParaDataValidator(
			TCelParaDataValidator tCelParaDataValidator) {
		this.tCelParaDataValidator = tCelParaDataValidator;
	}
	
	public void setEnbNeighbourService(EnbNeighbourService enbNeighbourService) {
		this.enbNeighbourService = enbNeighbourService;
	}
	
}
