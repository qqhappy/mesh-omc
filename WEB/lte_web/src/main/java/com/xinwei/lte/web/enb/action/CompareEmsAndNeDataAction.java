/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-8	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.cache.EnbVersionBizConfigCache;
import com.xinwei.lte.web.enb.model.CompareDetail;
import com.xinwei.lte.web.enb.model.CompareFieldDetail;
import com.xinwei.lte.web.enb.model.CompareResult;
import com.xinwei.lte.web.enb.util.EnbBizHelper;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbNeighbourFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbNeighbourRecord;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.system.action.web.WebConstants;

/**
 * 
 * 比较网管和基站数据Action
 * 
 * @author fanhaoyu
 * 
 */

public class CompareEmsAndNeDataAction extends ActionSupport {

	private Log log = LogFactory.getLog(EnbVersionBizConfigCache.class);

	private static final String[] BOARD_TABLE_FIELDS = { "u8RackNO",
			"u8BDType", "u8FiberPort", "u8RadioMode", "u8ManualOP" };

	private static final String[] BOARD_TABLE_FIELD_DESCS = { "单板", "单板类型",
			"光口号", "无线模式", "管理状态" };

	private static final String[] ETHEPARA_TABLE_FIELDS = { "u8PortID",
			"u8EthPort", "u8WorkMode", "u8ManualOP" };

	private static final String[] ETHEPARA_TABLE_FIELD_DESCS = { "端口标识", "网口号",
			"工作模式", "管理状态" };

	private static final String[] ENVMON_TABLE_FIELDS = { "u32EnvMNO",
			"u8RackNO", "u32EnvMType", "u32EnvMax", "u32EnvMin", "u32Rsv" };

	private static final String[] ENVMON_TABLE_FIELD_DESCS = { "环境监控编号", "单板",
			"环境监控类型", "环境监控最大值", "环境监控最小值", "保留字段" };

	private static final String FIELD_NAME_IS_NEIGHBOUR = "isNeighbour";

	private static final String FIELD_NAME_IS_NEIGHBOUR_DESC = "是否互为邻区";

	public static final String KEY_SPLIT = "-";

	// 0未开始，1已开始
	private int compareFlag = 0;

	private long moId;

	private List<CompareResult> compareResults;

	private String error;

	/**
	 * 比较网管和基站数据
	 * 
	 * @return
	 */
	public String compareData() {
		try {
			// 获取facade
			String sessionId = getSessionId();
			Enb enb = queryEnb(sessionId);
			if (enb == null) {
				throw new Exception("eNB不存在!");
			}
			if (!enb.isConfigurable()) {
				throw new Exception("该网元处于离线管理模式或者未连接状态!");
			}
			String protocolVersion = enb.getProtocolVersion();

			XEnbBizConfigFacade facade = MinasSession.getInstance().getFacade(
					sessionId, XEnbBizConfigFacade.class);

			// 获取所有需要比较的表名
			List<String> tableList = EnbBizHelper
					.getReverseTopoTableNames(enb.getEnbType(), protocolVersion);
			List<String> dynamicTables = EnbBizHelper
					.getDynamicTables(enb.getEnbType(), protocolVersion);
			// 动态表不进行数据比较
			tableList.removeAll(dynamicTables);
			// 易用性不需要显示的表
			List<String> filterTables = EnbBizHelper.getSimplifyFilterTables();
			tableList.removeAll(filterTables);

			compareResults = new LinkedList<CompareResult>();

			// 依次比较各表数据
			for (String tableName : tableList) {
				CompareResult compareResult = compareTable(facade, enb.getEnbType(),
						protocolVersion, tableName);
				compareResults.add(compareResult);
			}
		} catch (Exception e) {
			setError(e.getLocalizedMessage());
		}
		return SUCCESS;
	}

	/**
	 * 比较一个表的数据
	 * 
	 * @param facade
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	private CompareResult compareTable(XEnbBizConfigFacade facade, int enbTypeId,
			String protocolVersion, String tableName) throws Exception {
		// 获取网管数据
		XBizTable emsTable = null;
		StringBuilder message = null;
		try {
			emsTable = queryBizTable(facade, tableName, true);
		} catch (Exception e) {
			if (message == null) {
				message = new StringBuilder();
			}
			message.append("查询网管数据失败. 原因:" + e.getLocalizedMessage());
			log.warn("query from ems failed. tableName" + tableName, e);
		}
		// 获取基站数据
		XBizTable neTable = null;
		try {
			neTable = queryBizTable(facade, tableName, false);
		} catch (Exception e) {
			if (message == null) {
				message = new StringBuilder();
			}
			message.append("查询基站数据失败. 原因:" + e.getLocalizedMessage());
			log.warn("query from ne failed. tableName" + tableName, e);
		}
		CompareResult compareResult = new CompareResult();
		compareResult.setTableName(tableName);

		// 获取当前表的配置
		XList tableConfig = EnbBizHelper.getBizMetaBy(enbTypeId, protocolVersion,
				tableName);
		// 获取表名描述
		String tableDesc = tableConfig.getDesc();
		compareResult.setTableDesc(tableDesc);
		// 获取当前表的字段描述列表
		List<String> fieldDescs = getFieldDescs(tableConfig);
		compareResult.setFieldList(fieldDescs);
		// 获取主键列表
		List<String> keyFields = getTableIndexList(tableConfig);
		// 获取字段列表
		List<String> fieldNames = getTableFieldNames(tableConfig);
		// 生成比较详情
		List<CompareDetail> dataDetails = generateDetailList(keyFields,
				fieldNames, emsTable, neTable);
		compareResult.setDataDetails(dataDetails);
		// 设置比较结果
		int result = isXBizTableEquals(dataDetails) ? CompareResult.RESULT_EQUAL
				: CompareResult.RESULT_NOT_EQUAL;
		compareResult.setResult(result);
		if (message != null) {
			// 设置比较的附加信息
			compareResult.setMessage(message.toString());
		}
		return compareResult;

	}

	private List<String> getTableIndexList(XList tableConfig) {
		if (tableConfig.getName().equals(EnbConstantUtils.TABLE_NAME_T_BOARD)) {
			List<String> indexList = new LinkedList<String>();
			indexList.add(EnbConstantUtils.FIELD_NAME_RACKNO);
			return indexList;
		}
		return tableConfig.getIndexList();
	}

	/**
	 * 看两张表数据是否一致
	 * 
	 * @param dataDetails
	 * @return
	 */
	private boolean isXBizTableEquals(List<CompareDetail> dataDetails) {
		if (dataDetails == null || dataDetails.isEmpty())
			return true;
		for (CompareDetail compareDetail : dataDetails) {
			List<CompareFieldDetail> fieldDetails = compareDetail
					.getFieldDetails();
			for (CompareFieldDetail fieldDetail : fieldDetails) {
				if (fieldDetail.getFlag() == CompareFieldDetail.FLAG_NOT_EQUAL)
					return false;
			}
		}
		return true;
	}

	/**
	 * 生成详情
	 * 
	 * @param keyFields
	 *            主键列表
	 * @param fieldNames
	 *            表中所有字段列表
	 * @param emsTable
	 * @param neTable
	 * @return
	 */
	private List<CompareDetail> generateDetailList(List<String> keyFields,
			List<String> fieldNames, XBizTable emsTable, XBizTable neTable) {

		List<CompareDetail> details = new LinkedList<CompareDetail>();

		boolean emsHasData = EnbBizHelper.hasRecord(emsTable);
		boolean neHasData = EnbBizHelper.hasRecord(neTable);
		// 如果两表都无数据，则直接返回
		if (!emsHasData && !neHasData)
			return details;
		// 主键值列表
		List<String> keyList = new LinkedList<String>();
		if (emsHasData && neHasData) {
			// 如果两表都有数据，取两者并集
			for (XBizRecord emsRecord : emsTable.getRecords()) {
				String keyValue = getKeyValue(keyFields, emsRecord);
				keyList.add(keyValue);
			}
			for (XBizRecord neRecord : neTable.getRecords()) {
				String keyValue = getKeyValue(keyFields, neRecord);
				if (!keyList.contains(keyValue)) {
					keyList.add(keyValue);
				}
			}
			// 主键排序
			Collections.sort(keyList);
		} else if (emsHasData) {
			// ems有数据，以ems为基准
			for (XBizRecord emsRecord : emsTable.getRecords()) {
				String keyValue = getKeyValue(keyFields, emsRecord);
				keyList.add(keyValue);
			}
		} else if (neHasData) {
			// ems无数据，ne有数据，以ne为基准
			for (XBizRecord neRecord : neTable.getRecords()) {
				String keyValue = getKeyValue(keyFields, neRecord);
				keyList.add(keyValue);
			}
		}
		// 遍历主键值
		for (String keyValue : keyList) {
			XBizRecord emsRecord = getSameKeyRecord(keyFields, keyValue,
					emsTable);
			XBizRecord neRecord = getSameKeyRecord(keyFields, keyValue, neTable);
			// 生成单条对比记录的比较详情
			CompareDetail compareDetail = generateCompareDetail(fieldNames,
					emsRecord, neRecord);
			details.add(compareDetail);
		}

		return details;
	}

	/**
	 * 获取表中字段列表
	 * 
	 * @param tableConfig
	 * @return
	 */
	private List<String> getTableFieldNames(XList tableConfig) {
		String tableName = tableConfig.getName();
		List<String> fieldNames = new LinkedList<String>();
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_BOARD)) {
			Collections.addAll(fieldNames, BOARD_TABLE_FIELDS);
			return fieldNames;
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ETHPARA)) {
			Collections.addAll(fieldNames, ETHEPARA_TABLE_FIELDS);
			return fieldNames;
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENVMON)) {
			Collections.addAll(fieldNames, ENVMON_TABLE_FIELDS);
			return fieldNames;
		}

		List<XList> fieldConfigs = EnbBizHelper.getStaticFields(tableConfig);
		for (XList fieldConfig : fieldConfigs) {
			fieldNames.add(fieldConfig.getName());
		}
		// if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL)) {
		// fieldNames.add(FIELD_NAME_IS_NEIGHBOUR);
		// }
		return fieldNames;
	}

	/**
	 * 生成比较详情
	 * 
	 * @param tableConfig
	 * @param emsRecord
	 * @param neRecord
	 * @return
	 */
	private CompareDetail generateCompareDetail(List<String> fieldNames,
			XBizRecord emsRecord, XBizRecord neRecord) {

		CompareDetail compareDetail = new CompareDetail();
		for (String fieldName : fieldNames) {
			String emsValue = "";
			String neValue = "";
			// 当记录不为null，且字段不为null时，取真实字段值
			if (emsRecord != null) {
				XBizField emsField = emsRecord.getFieldBy(fieldName);
				if (emsField != null) {
					emsValue = emsField.getValue();
				}
			}
			if (neRecord != null) {
				XBizField neField = neRecord.getFieldBy(fieldName);
				if (neField != null) {
					neValue = neField.getValue();
				}
			}
			// 两个字段的值是否相等
			int flag = emsValue.equals(neValue) ? CompareFieldDetail.FLAG_EQUAL
					: CompareFieldDetail.FLAG_NOT_EQUAL;
			// 创建字段比较详情
			CompareFieldDetail fieldDetail = new CompareFieldDetail(fieldName,
					emsValue, neValue, flag);
			compareDetail.addFieldDetail(fieldDetail);
		}

		return compareDetail;
	}

	/**
	 * 获取字段的描述列表，供前台显示，过滤动态字段
	 * 
	 * @param tableConfig
	 * @return
	 */
	private List<String> getFieldDescs(XList tableConfig) {
		// TODO 获取语言环境
		String tableName = tableConfig.getName();
		List<String> fieldList = new LinkedList<String>();
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_BOARD)) {
			Collections.addAll(fieldList, BOARD_TABLE_FIELD_DESCS);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ETHPARA)) {
			Collections.addAll(fieldList, ETHEPARA_TABLE_FIELD_DESCS);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENVMON)) {
			Collections.addAll(fieldList, ENVMON_TABLE_FIELD_DESCS);
		} else {
			for (XList fieldConfig : tableConfig.getAllFields()) {
				// 动态字段不处理
				if (fieldConfig.isReadonly())
					continue;
				fieldList.add(fieldConfig.getDesc());
			}
			// if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL)) {
			// fieldList.add(FIELD_NAME_IS_NEIGHBOUR_DESC);
			// }
		}
		return fieldList;
	}

	/**
	 * 查找相同key的记录
	 * 
	 * @param keyFields
	 * @param keyValue
	 * @param bizTable
	 * @return
	 */
	private XBizRecord getSameKeyRecord(List<String> keyFields,
			String keyValue, XBizTable bizTable) {
		if (!EnbBizHelper.hasRecord(bizTable))
			return null;
		XBizRecord sameKeyRecord = null;
		for (XBizRecord bizRecord : bizTable.getRecords()) {
			// 获取一条记录的key值
			String key = getKeyValue(keyFields, bizRecord);
			// 与目标key值比较
			if (keyValue.equals(key)) {
				sameKeyRecord = bizRecord;
				break;
			}
		}
		return sameKeyRecord;
	}

	/**
	 * 获取一条记录的key值
	 * 
	 * @param keyFields
	 * @param bizRecord
	 * @return
	 */
	private String getKeyValue(List<String> keyFields, XBizRecord bizRecord) {
		String key = "";
		for (String keyField : keyFields) {
			String value = bizRecord.getFieldBy(keyField).getValue();
			key += value + KEY_SPLIT;
		}
		return key;
	}

	/**
	 * 查询eNB模型
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	private Enb queryEnb(String sessionId) throws Exception {
		EnbBasicFacade facade = MinasSession.getInstance().getFacade(sessionId,
				EnbBasicFacade.class);
		return facade.queryByMoId(moId);
	}

	/**
	 * 查询网管中的指定业务表数据
	 * 
	 * @param facade
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	private XBizTable queryBizTable(XEnbBizConfigFacade facade,
			String tableName, boolean fromEms) throws Exception {
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_BOARD)) {
			return queryBoardTable(facade, fromEms);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
			return queryCellParaTable(facade, fromEms);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENVMON)) {
			return queryEnvMonTable(facade, fromEms);
			// } else if
			// (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL)) {
			// return queryNbrCellTable(facade, fromEms);
		} else {
			if (fromEms) {
				return facade.queryFromEms(moId, tableName, null, false);
			} else {
				return facade.queryFromNe(moId, tableName);
			}
		}
	}

	/**
	 * 查询邻区关系表数据
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	private XBizTable queryNbrCellTable(XEnbBizConfigFacade facade,
			boolean fromEms) throws Exception {
		// TODO 邻区关系表是否需要显示互为邻区字段
		String sessionId = getSessionId();
		if (fromEms) {
			EnbNeighbourFacade neighbourFacade = MinasSession.getInstance()
					.getFacade(sessionId, EnbNeighbourFacade.class);
			List<EnbNeighbourRecord> neighbourRecords = neighbourFacade
					.queryNeighbourRecords(moId);
			XBizTable table = new XBizTable(moId,
					EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL);
			if (neighbourRecords != null) {
				for (EnbNeighbourRecord neighbourRecord : neighbourRecords) {
					XBizRecord record = neighbourRecord.getBizRecord();
					// 将是否互为邻区字段加入记录中
					XBizField isNeighbourField = new XBizField(
							FIELD_NAME_IS_NEIGHBOUR,
							String.valueOf(neighbourRecord.getIsNeighbour()));
					record.addField(isNeighbourField);
					table.addRecord(record);
				}
			}
			return table;
		} else {
			return facade.queryFromNe(moId,
					EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL);
		}
	}

	/**
	 * 查询环境监控表数据
	 * 
	 * @param facade
	 * @param fromEms
	 * @return
	 * @throws Exception
	 */
	private XBizTable queryEnvMonTable(XEnbBizConfigFacade facade,
			boolean fromEms) throws Exception {
		XBizTable envMonTable = null;
		XBizTable boardTable = null;
		if (fromEms) {
			envMonTable = facade.queryFromEms(moId,
					EnbConstantUtils.TABLE_NAME_T_ENVMON, null, false);
			boardTable = facade.queryFromEms(moId,
					EnbConstantUtils.TABLE_NAME_T_BOARD, null, false);
		} else {
			envMonTable = facade.queryFromNe(moId,
					EnbConstantUtils.TABLE_NAME_T_ENVMON);
			boardTable = facade.queryFromNe(moId,
					EnbConstantUtils.TABLE_NAME_T_BOARD);
		}
		if (EnbBizHelper.hasRecord(envMonTable)) {
			convertEnvMonTable(envMonTable, boardTable);
		}
		return envMonTable;
	}

	private void convertEnvMonTable(XBizTable envMonTable, XBizTable boardTable) {
		for (XBizRecord envMonRecord : envMonTable.getRecords()) {
			String rackNo = envMonRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_RACKNO).getValue();
			String shelfNo = envMonRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_SHELFNO).getValue();
			String slotNo = envMonRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_SLOTNO).getValue();
			XBizRecord boardRecord = queryBoardRecord(rackNo, shelfNo, slotNo,
					boardTable);
			String boardName = getBoardName(boardRecord);
			envMonRecord.addField(new XBizField(
					EnbConstantUtils.FIELD_NAME_RACKNO, boardName));
		}
	}

	/**
	 * 根据架框槽获取单板记录
	 * 
	 * @param rackNo
	 * @param shelfNo
	 * @param slotNo
	 * @param boardTable
	 * @return
	 */
	private XBizRecord queryBoardRecord(String rackNo, String shelfNo,
			String slotNo, XBizTable boardTable) {
		for (XBizRecord boardRecord : boardTable.getRecords()) {
			String rack = boardRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_RACKNO).getValue();
			String shelf = boardRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_SHELFNO).getValue();
			String slot = boardRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_SLOTNO).getValue();
			if (rack.equals(rackNo) && shelf.equals(shelfNo)
					&& slot.equals(slotNo)) {
				return boardRecord;
			}
		}
		return null;
	}

	/**
	 * 查询小区参数表数据
	 * 
	 * @param facade
	 * @param fromEms
	 * @return
	 * @throws Exception
	 */
	private XBizTable queryCellParaTable(XEnbBizConfigFacade facade,
			boolean fromEms) throws Exception {
		XBizTable cellParaTable = null;
		XBizTable topoTable = null;
		if (fromEms) {
			cellParaTable = facade.queryFromEms(moId,
					EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null, false);
			topoTable = facade.queryFromEms(moId,
					EnbConstantUtils.TABLE_NAME_T_TOPO, null, false);
		} else {
			cellParaTable = facade.queryFromNe(moId,
					EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
			topoTable = facade.queryFromNe(moId,
					EnbConstantUtils.TABLE_NAME_T_TOPO);
		}
		if (EnbBizHelper.hasRecord(cellParaTable)) {
			convertCellParaRecord(cellParaTable, topoTable);
		}
		return cellParaTable;
	}

	/**
	 * 将小区参数表中的拓扑号转换为单板
	 * 
	 * @param cellParaTable
	 * @param topoTable
	 */
	private void convertCellParaRecord(XBizTable cellParaTable,
			XBizTable topoTable) {
		for (XBizRecord cellParaRecord : cellParaTable.getRecords()) {
			String topoId = cellParaRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_TOPO_NO).getValue();
			String boardName = getTopoRelatedRRUBoard(topoId, topoTable);
			cellParaRecord.addField(new XBizField(
					EnbConstantUtils.FIELD_NAME_TOPO_NO, boardName));
		}
	}

	/**
	 * 获取小区相关拓扑记录中的RRU单板名
	 * 
	 * @param topoId
	 * @param topoTable
	 * @return
	 */
	private String getTopoRelatedRRUBoard(String topoId, XBizTable topoTable) {
		for (XBizRecord topoRecord : topoTable.getRecords()) {
			String id = topoRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_TOPO_NO).getValue();
			// 找到拓扑号对应拓扑记录
			if (topoId.equals(id)) {
				// 获取从板机架号
				String rackNo = topoRecord.getFieldBy(
						EnbConstantUtils.FIELD_NAME_SRACKNO).getValue();
				// 机架号减1
				return "RRU" + (Integer.valueOf(rackNo) - 1);
			}
		}
		return "";
	}

	private XBizTable queryBoardTable(XEnbBizConfigFacade facade, boolean fromEms)
			throws Exception {
		XBizTable boardTable = null;
		XBizTable topoTable = null;
		if (fromEms) {
			boardTable = facade.queryFromEms(moId,
					EnbConstantUtils.TABLE_NAME_T_BOARD, null, false);
			topoTable = facade.queryFromEms(moId,
					EnbConstantUtils.TABLE_NAME_T_TOPO, null, false);
		} else {
			boardTable = facade.queryFromNe(moId,
					EnbConstantUtils.TABLE_NAME_T_BOARD);
			topoTable = facade.queryFromNe(moId,
					EnbConstantUtils.TABLE_NAME_T_TOPO);
		}
		if (EnbBizHelper.hasRecord(boardTable)) {
			return mergeToBoardTable(boardTable, topoTable);
		}
		return boardTable;
	}

	/**
	 * 将原始单板表和拓扑表合成易用性的单板表
	 * 
	 * @param boardTable
	 * @param topoTable
	 * @return
	 */
	private XBizTable mergeToBoardTable(XBizTable boardTable,
			XBizTable topoTable) {
		XBizTable table = new XBizTable(boardTable.getMoId(),
				EnbConstantUtils.TABLE_NAME_T_BOARD);
		for (XBizRecord boardRecord : boardTable.getRecords()) {
			XBizRecord record = boardRecord.clone();
			String rackNo = boardRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_RACKNO).getValue();
			String shelfNo = boardRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_SHELFNO).getValue();
			String slotNo = boardRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_SLOTNO).getValue();
			// 单板名称
			String boardName = getBoardName(boardRecord);
			record.addField(new XBizField(EnbConstantUtils.FIELD_NAME_RACKNO,
					boardName));
			// 设置光口号
			String fiberPort = "";
			if (EnbBizHelper.hasRecord(topoTable)) {
				fiberPort = getRelativeFiberPortNum(rackNo, shelfNo, slotNo,
						topoTable.getRecords());
			}
			record.addField(new XBizField(
					EnbConstantUtils.FIELD_NAME_FIBER_PORT, fiberPort));

			table.addRecord(record);
		}
		return table;
	}

	/**
	 * 获取单板相关联的光口号
	 * 
	 * @param rackNo
	 * @param shelfNo
	 * @param slotNo
	 * @param topoRecords
	 * @return
	 */
	private String getRelativeFiberPortNum(String rackNo, String shelfNo,
			String slotNo, List<XBizRecord> topoRecords) {
		for (XBizRecord topoRecord : topoRecords) {
			String rack = topoRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_SRACKNO).getValue();
			String shelf = topoRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_SSHELFNO).getValue();
			String slot = topoRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_SSLOTNO).getValue();
			if (rack.equals(rackNo) && shelf.equals(shelfNo)
					&& slot.equals(slotNo)) {
				return topoRecord.getFieldBy(
						EnbConstantUtils.FIELD_NAME_FIBER_PORT).getValue();
			}
		}
		return "";
	}

	/**
	 * 获取要显示的单板名称
	 * 
	 * @param boardRecord
	 * @return
	 */
	private String getBoardName(XBizRecord boardRecord) {
		String rackNo = boardRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_RACKNO).getValue();

		int boardType = getIntFieldValue(boardRecord,
				EnbConstantUtils.FIELD_NAME_BDTYPE);
		String typeString = (boardType == EnbConstantUtils.BOARD_TYPE_BBU) ? "BBU"
				: "RRU";
		// RRU编号为机架号减1
		String boardName = typeString + (Integer.valueOf(rackNo) - 1);
		if (boardType == EnbConstantUtils.BOARD_TYPE_BBU)
			boardName = "BBU";
		return boardName;
	}

	private int getIntFieldValue(XBizRecord bizRecord, String fieldName) {
		return Integer.valueOf(bizRecord.getFieldBy(fieldName).getValue());
	}

	private String getSessionId() {
		return ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public List<CompareResult> getCompareResults() {
		return compareResults;
	}

	public void setCompareResults(List<CompareResult> compareResults) {
		this.compareResults = compareResults;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setCompareFlag(int compareFlag) {
		this.compareFlag = compareFlag;
	}

	public int getCompareFlag() {
		return compareFlag;
	}

}
