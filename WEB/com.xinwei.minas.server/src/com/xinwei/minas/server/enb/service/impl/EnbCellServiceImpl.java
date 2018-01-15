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
			// 查询全局配置中算法设置
			EnbGlobalConfig config = enbGlobalConfigService
					.queryEnbGlobalConfig();
			
			enbCellStart.setMcc(config.getMcc());
			enbCellStart.setMnc(config.getMnc());
			// 校验
			validate(enb, enbCellStart);
			// 将逻辑根序列和物理小区标识加入缓存
			/*EnbBizUniqueIdHelper.addPciCache(enbId, cid,
					enbCellStart.getPhyCellId());
			EnbBizUniqueIdHelper.addRsiCache(enbId, cid,
					enbCellStart.getRootSeqIndex());*/
			// 通过开站参数获取生成的数据
			EnbSceneOutput sceneOutput = getSceneOutput(enbCellStart);
			Map<String, Map<String, String>> sceneOutputMap = sceneOutput
					.getTableMap();
			// 获取小区相关表模板数据
			Map<String, List<XBizRecord>> cellTemplateData = getCellTemplateData(enb);
			
			// 记录同步数据
			Map<String, XBizRecord> synchroData = new HashMap<String, XBizRecord>();
			
			for (String tableName : cellTemplateData.keySet()) {
				List<XBizRecord> records = cellTemplateData.get(tableName);
				for (XBizRecord record : records) {
					// 替换小区cid
					replaceCid(enb, enbCellStart.getCid(), tableName, record);
					// 用开站配置获取的模板参数替换掉数据库模板参数
					Map<String, String> outputMap = sceneOutputMap
							.get(tableName);
					if (null != outputMap) {
						for (String key : outputMap.keySet()) {
							// 判断api中返回的字段网管enb当前版本中是否存在
							if (!EnbBizHelper.fieldIsExist(enb.getEnbType(),
									enb.getProtocolVersion(), tableName, key)) {
								System.out.println("table=" + tableName
										+ ",field=" + key + " is not exist.");
								log.error("EnbCellService add error,table="
										+ tableName + ",field=" + key
										+ " is not exist.");
								continue;
							}
							// 如果是小区下行功控参数表 T_CEL_DLPC 并且 是微站 
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
					// 用户界面输入的参数替换到模板参数
					replaceData(record, tableName, enbCellStart);
					// 将记录中的hexArray类型字段的值转成小写
					EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
							enb.getProtocolVersion(), tableName, record);
					enbBizConfigDAO.add(enb.getMoId(), tableName, record);
					synchroData.put(tableName, record);
				}
			}
			// 封装小区场景信息
			XBizRecord bizSenceRecord = getSenceRecord(enbCellStart);
			// 增加小区场景信息
			enbBizConfigDAO.addScene(moId, cid, bizSenceRecord);
			
			// 如果网元在线,则同步数据
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
	 * 将模板数据中的小区cid的值替换掉
	 * 
	 * @param enb
	 * 
	 * @param cid
	 * @param tableName
	 * @param record
	 */
	private void replaceCid(Enb enb, int cid, String tableName,
			XBizRecord record) {
		// 小区参数表直接替换
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
			XBizField cidField = new XBizField(
					EnbConstantUtils.FIELD_NAME_CELL_ID, String.valueOf(cid));
			record.addField(cidField);
		}
		// 小区相关表,找到关联cid的字段,然后替换
		else {
			String cidRelatedField = EnbBizHelper.getCidRelatedField(
					enb.getEnbType(), enb.getProtocolVersion(), tableName);
			XBizField cidField = new XBizField(cidRelatedField,
					String.valueOf(cid));
			record.addField(cidField);
		}
	}
	
	/**
	 * 封装开站场景表的数据
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
	 * 校验小区开站场景数据
	 * 
	 * @param enb
	 * @param enbCellStart
	 * @throws Exception
	 */
	public void validate(Enb enb, EnbCellStart enbCellStart) throws Exception {
		// 校验表容量
		XBizTable cellParaTable = enbBizConfigDAO.query(enb.getMoId(),
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
		if (null != cellParaTable && null != cellParaTable.getRecords()) {
			int recordNum = cellParaTable.getRecords().size();
			// 获取数据字典定义的表容量
			XList bizMeta = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
					enb.getProtocolVersion(),
					EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
			int tableSize = bizMeta.getTableSize();
			if (recordNum >= tableSize) {
				// 表记录数超过阈值
				throw new Exception(OmpAppContext.getMessage(
						"table_size_over_threshold",
						new Integer[] { tableSize }));
			}
		}
		
		// 封装校验数据
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
		// 校验rru单板
		tCelParaDataValidator.checkTopoNo(enb.getMoId(), enbCellStart.getCid(),
				bizRecord);
		
		// 校验同enb下的小区ID不能重复
		checkCidExist(enb.getMoId(), enbCellStart.getCid());
		// 校验物理小区ID全网唯一
		/*validateHelper.checkUniqueM(enb.getMoId(), bizRecord,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);*/
		// 校验逻辑根序列全网唯一
		/*validateHelper.checkUniqueM(enb.getMoId(), bizRecord,
				EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
				EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX);*/
		// 在api中校验
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
	 * 校验小区标识是否存在
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
	 * 替换掉模板数据中需要替换的数据
	 * 
	 * @param record
	 * @param tableName
	 * @param enbCellStart
	 */
	public void replaceData(XBizRecord record, String tableName,
			EnbCellStart enbCellStart) {
		// 替换掉T_CEL_PARA表中相关参数(小区名称、频段指示、系统带宽、子帧配比、物理小区标识、中心频点、跟踪区码、RRU单板、管理状态)
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
			// 小区名称
			XBizField cellNameField = new XBizField(
					EnbConstantUtils.FIELD_NAME_CELL_NAME,
					enbCellStart.getCellName());
			// 频段指示
			XBizField freqBandField = new XBizField(
					EnbConstantUtils.FIELD_NAME_FREQ_BAND,
					String.valueOf(enbCellStart.getFreqBandId()));
			// 带宽编号
			XBizField bandwidthField = new XBizField(
					EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH,
					String.valueOf(enbCellStart.getBandwidthId()));
			// 子帧配比
			XBizField sfCfgField = new XBizField(
					EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC,
					String.valueOf(enbCellStart.getSfCfgId()));
			// 物理小区标识
			XBizField phyCellField = new XBizField(
					EnbConstantUtils.FIELD_NAME_PHY_CELL_ID,
					String.valueOf(enbCellStart.getPhyCellId()));
			// 中心频点
			XBizField centerFreqField = new XBizField(
					EnbConstantUtils.FIELD_NAME_CENTER_FREQ,
					String.valueOf(enbCellStart.getCenterFreq()));
			// 跟踪区码
			XBizField tacField = new XBizField(EnbConstantUtils.FIELD_NAME_TAC,
					String.valueOf(enbCellStart.getTac()));
			
			// RRU单板
			XBizField topoNOField = new XBizField(
					EnbConstantUtils.FIELD_NAME_TOPO_NO,
					String.valueOf(enbCellStart.getTopoNO()));
			// 管理状态
			XBizField manualOPField = new XBizField("u8ManualOP",
					String.valueOf(enbCellStart.getManualOP()));
			// 移动国家码
			XBizField mmcField = new XBizField(EnbConstantUtils.FIELD_NAME_MCC,
					String.valueOf(enbCellStart.getMcc()));
			// 移动网络码
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
		// 替换掉T_CEL_PRACH表中相关参数(逻辑根序列)
		else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_PRACH)) {
			XBizField rootSeqIndexField = new XBizField(
					EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX,
					String.valueOf(enbCellStart.getRootSeqIndex()));
			record.addField(rootSeqIndexField);
		}
	}
	
	/**
	 * 根据开站场景数据从api中获取生成的数据
	 * 
	 * @param enbCellStart
	 * @return
	 * @throws Exception
	 */
	public EnbSceneOutput getSceneOutput(EnbCellStart enbCellStart)
			throws Exception {
		List<EnbIputItem> inputList = new ArrayList<EnbIputItem>();
		// 封装输入参数
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
		// 调用API获取输出数据
		EnbSceneOutput output = EnbSceneRuleEngine.generate(1, inputList);
		return output;
	}
	
	/**
	 * 将所有模板数据过滤出小区相关的模板数据
	 * 
	 * @param enb
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<XBizRecord>> getCellTemplateData(Enb enb)
			throws Exception {
		// 获取所有模板数据
		Map<String, List<XBizRecord>> tableMap = enbBizTemplateService
				.queryTemplateData(enb.getEnbType(), enb.getProtocolVersion());
		// 过滤出小区模板数据,过滤出cid都为252的数据
		Map<String, List<XBizRecord>> resultMap = new HashMap<String, List<XBizRecord>>();
		for (String tableName : tableMap.keySet()) {
			if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)
					|| EnbBizHelper.isCellRelatedTable(enb.getEnbType(),
							enb.getProtocolVersion(), tableName)) {
				List<XBizRecord> records = tableMap.get(tableName);
				List<XBizRecord> resultList = new ArrayList<XBizRecord>();
				for (XBizRecord record : records) {
					// 获取小区ID在表中的字段
					String cidRelatedField = "";
					// 如果是小区参数表
					if (tableName
							.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
						cidRelatedField = EnbConstantUtils.FIELD_NAME_CELL_ID;
					}
					// 如果是小区相关表
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
			// 查询小区参数表数据
			XBizTable cellBizTable = enbBizConfigService.queryFromEms(moId,
					EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
			List<XBizRecord> records = cellBizTable.getRecords();
			for (XBizRecord record : records) {
				// 根据cid查询出小区场景数据
				XBizRecord cellSceneRecord = enbBizConfigDAO
						.querySceneByMoIdAndCid(
								moId,
								record.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID));
				if (null != cellSceneRecord) {
					// 合并数据
					for (String bizField : cellSceneRecord.getFieldMap()
							.keySet()) {
						record.addField(cellSceneRecord.getFieldMap().get(
								bizField));
					}
				}
				// 做兼容前台的数据
				else {
					record.addField(new XBizField(EnbCellStart.FIELD_SCENE, ""));
					record.addField(new XBizField(EnbCellStart.FIELD_RRU_TYPE,
							""));
					record.addField(new XBizField(EnbCellStart.FIELD_AN_NUM, ""));
				}
				// 获取T_CELL_PRACH表的主键
				XBizRecord celPrachKey = EnbBizHelper.getKeyRecordBy(moId,
						EnbConstantUtils.TABLE_NAME_T_CEL_PRACH, record);
				// 查询T_CELL_PRACH表的数据
				XBizRecord celPrachRecord = enbBizConfigDAO.queryByKey(moId,
						EnbConstantUtils.TABLE_NAME_T_CEL_PRACH, celPrachKey);
				if (null != celPrachRecord) {
					// 合并数据
					for (String bizField : celPrachRecord.getFieldMap()
							.keySet()) {
						record.addField(celPrachRecord.getFieldMap().get(
								bizField));
					}
				}
				// 做兼容前台的数据
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
			
			// 校验小区中是否包含不能删除的邻区
			List<EnbNeighbourRecord> neighbourRecords = enbNeighbourService
					.queryNeighbourRecords(moId);
			for (EnbNeighbourRecord enbNeighbourRecord : neighbourRecords) {
				int nbCellId = enbNeighbourRecord.getBizRecord().getIntValue(
						EnbConstantUtils.FIELD_NAME_SVR_CID);
				// 判断要删除的小区中是否有邻区关系,并且邻区关系为不能删除
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
			// 删除小区相关表数据
			for (String tableName : cellRelatedTables) {
				String cidRelatedField = EnbBizHelper.getCidRelatedField(
						enb.getEnbType(), enb.getProtocolVersion(), tableName);
				// 生成查询条件
				XBizRecord condition = new XBizRecord();
				condition.addField(new XBizField(cidRelatedField, String
						.valueOf(cid)));
				XBizTable bizTable = enbBizConfigDAO.query(moId, tableName,
						condition);
				if (null != bizTable.getRecords()) {
					for (XBizRecord record : bizTable.getRecords()) {
						// 如果是邻区参数表,调用邻区参数删除方法
						if (tableName
								.equals(EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL)) {
							EnbNeighbourRecord enbNeighbourRecord = new EnbNeighbourRecord();
							enbNeighbourRecord.setBizRecord(record);
							enbNeighbourService.deleteNeighbour(moId,
									enbNeighbourRecord);
						}
						else {
							// 获取主键
							XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(
									moId, tableName, record);
							enbBizConfigDAO.delete(moId, tableName, bizKey);
						}
					}
				}
			}
			/*EnbBizUniqueIdHelper.deletePciCache(enb.getEnbId(), cid);
			EnbBizUniqueIdHelper.deleteRsiCache(enb.getEnbId(), cid);*/
			// 删除场景数据
			enbBizConfigDAO.deleteScene(moId, cid);
			// 删除小区参数表数据
			XBizRecord cellParaKey = new XBizRecord();
			cellParaKey.addField(new XBizField(
					EnbConstantUtils.FIELD_NAME_CELL_ID, String.valueOf(cid)));
			enbBizConfigDAO.delete(moId,
					EnbConstantUtils.TABLE_NAME_T_CELL_PARA, cellParaKey);
			
			// 如果网元在线,则同步数据
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
		// 查询全局配置中算法设置
		EnbGlobalConfig config = enbGlobalConfigService.queryEnbGlobalConfig();
		
		enbCellStart.setMcc(config.getMcc());
		enbCellStart.setMnc(config.getMnc());
		// 封装校验数据
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
		// 校验rru单板
		tCelParaDataValidator.checkTopoNo(enb.getMoId(), enbCellStart.getCid(),
				bizRecord);
		// 校验物理小区ID全网唯一
		/*validateHelper.checkUniqueM(enb.getMoId(), bizRecord,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);*/
		// 校验逻辑根序列全网唯一
		/*validateHelper.checkUniqueM(enb.getMoId(), bizRecord,
				EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
				EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX);*/
		// 在api中校验
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
			// 查询出要更新的小区参数表信息
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
			// 如果场景表数据位空,则先删除该小区数据,从新创建新的数据
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
				
				// 如果修改场景信息,则先删除,然后重新生成数据.
				if (scene != enbCellStart.getSceneId()
						|| rruType != enbCellStart.getRruTypeId()
						|| anNum != enbCellStart.getAnNumId()
						|| bandwidthId != enbCellStart.getBandwidthId()
						|| sfCfgId != enbCellStart.getSfCfgId()) {
					updateValidate(enb, enbCellStart);
					delete(moId, enbCellStart.getCid());
					add(moId, enbCellStart);
				}
				// 如果修改的是非场景数据,则修改对应信息
				else {
					// 查询数据库原有数据 XBizRecord
					XBizRecord celPrachRecord = enbBizConfigDAO.queryByKey(
							moId, EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
							cellParaKey);
					// 处理移动国家码和移动网络码
					enbCellStart.setMcc(cellParaRecord
							.getStringValue(EnbConstantUtils.FIELD_NAME_MCC));
					enbCellStart.setMnc(cellParaRecord
							.getStringValue(EnbConstantUtils.FIELD_NAME_MNC));
					// 替换成用户修改的数据
					replaceData(cellParaRecord,
							EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
							enbCellStart);
					replaceData(celPrachRecord,
							EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
							enbCellStart);
					// 调用修改方法
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
			// 查询小区参数表数据
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
			// 查询出小区场景数据和T_CEL_PRACH表数据并且合并
			XBizRecord cellScneeRecord = enbBizConfigDAO
					.querySceneByMoIdAndCid(moId, cid);
			if (null != cellScneeRecord) {
				for (String bizField : cellScneeRecord.getFieldMap().keySet()) {
					cellParaRecord.addField(cellScneeRecord.getFieldMap().get(
							bizField));
				}
			}
			// 做兼容前台的数据
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
			// 做兼容前台的数据
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
