/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-15	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.proxy.impl;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.helper.EnbUtils;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbConnector;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.net.EnbMessageHelper;
import com.xinwei.minas.server.enb.net.TagConst;
import com.xinwei.minas.server.enb.proxy.EnbBizConfigProxy;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.nms.common.util.ByteArrayUtil;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * 通用网元配置Proxy实现
 * 
 * @author chenjunhua
 * 
 */

public class EnbBizConfigProxyImpl implements EnbBizConfigProxy {

	private EnbConnector enbConnector;

	public void setEnbConnector(EnbConnector enbConnector) {
		this.enbConnector = enbConnector;
	}

	@Override
	public String generateInsertSql(int enbTypeId, String version, String tableName,
			XBizRecord bizRecord) {
		// 获取业务元数据定义
		XList bizMeta = EnbBizHelper.getBizMetaBy(enbTypeId, version, tableName);
		// 构造insert语句
		String sql = createInsertSql(bizMeta, tableName, bizRecord);
		return sql;
	}

	@Override
	public XBizTable query(Long moId, String tableName, List<XList> fieldMetas)
			throws Exception {
		// 构造查询语句
		String sql = this.createQuerySql(tableName, fieldMetas);
		// 向网元发送消息
		EnbAppMessage req = buildIncrementalConfigMessage(moId, tableName, sql,
				EnbMessageConstants.ACTION_QUERY);
		if (enbConnector != null) {
			EnbAppMessage resp = enbConnector.syncInvoke(req);
			// 解析应答结果
			return parseResultSet(moId, tableName, fieldMetas, resp);
		}
		return new XBizTable(moId, tableName);
	}

	@Override
	public void add(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		// 构造insert语句
		String sql = this.generateInsertSql(enb.getEnbType(), enb.getProtocolVersion(),
				tableName, bizRecord);
		// 向网元发送消息
		EnbAppMessage req = buildIncrementalConfigMessage(moId, tableName, sql,
				EnbMessageConstants.ACTION_ADD);
		if (enbConnector != null) {
			EnbAppMessage resp = enbConnector.syncInvoke(req);
			// 解析应答结果
			EnbMessageHelper.parseResponse(resp);
		}
	}

	@Override
	public void update(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception {
		// 获取主键
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId, tableName,
				bizRecord);
		// 获取业务元数据定义
		XList bizMeta = EnbBizHelper.getBizMetaBy(moId, tableName);
		// 构造update语句
		String sql = createUpdateSql(bizMeta, tableName, bizRecord, bizKey);
		// 向网元发送消息
		EnbAppMessage req = buildIncrementalConfigMessage(moId, tableName, sql,
				EnbMessageConstants.ACTION_MODIFY);
		if (enbConnector != null) {
			EnbAppMessage resp = enbConnector.syncInvoke(req);
			// 解析应答结果
			EnbMessageHelper.parseResponse(resp);
		}

	}

	@Override
	public void delete(Long moId, String tableName, XBizRecord bizKey)
			throws Exception {
		// 获取业务元数据定义
		XList bizMeta = EnbBizHelper.getBizMetaBy(moId, tableName);
		// 构造delete语句
		String sql = createDeleteSql(bizMeta, tableName, bizKey);
		// 向网元发送消息
		EnbAppMessage req = buildIncrementalConfigMessage(moId, tableName, sql,
				EnbMessageConstants.ACTION_DELETE);
		if (enbConnector != null) {
			EnbAppMessage resp = enbConnector.syncInvoke(req);
			// 解析应答结果
			EnbMessageHelper.parseResponse(resp);
		}
	}

	@Override
	public void fullTableConfig(Long enbId, GenericBizData data, int bizFlag)
			throws Exception {

		// 向网元发送消息
		int moc = 0;
		switch (bizFlag) {
		case 0:
			// 整表配置
			moc = EnbMessageConstants.MOC_FULLTABLECONFIG_CONFIG;
			break;
		case 1:
			// 整表反构
			moc = EnbMessageConstants.MOC_FULLTABLEREVERSE_CONFIG;
			break;
		default:
			// 没有则返回
			return;
		}
		EnbAppMessage req = buildFullTableMessage(enbId, moc, data);
		if (enbConnector != null) {
			EnbAppMessage resp = enbConnector.syncInvoke(req);
			// 解析应答结果
			EnbMessageHelper.parseResponse(resp);
		}
	}

	/**
	 * 根据字段元数据定义转换字段数值
	 * 
	 * @param fieldMeta
	 * @param fieldValue
	 * @return
	 */
	private String transferFieldValue(XList fieldMeta, Object fieldValue) {
		String value = fieldValue.toString();
		if (fieldMeta.isNumber()) {
			// do nothing
		} else if (fieldMeta.isHexArray()) {
			// 16进制数组
			value = "x'" + value + "'";
		} else {
			value = "'" + value + "'";
		}
		return value;
	}

	/**
	 * 创建查询语句
	 * 
	 * @param tableName
	 * @param fieldList
	 * @return
	 */
	private String createQuerySql(String tableName, List<XList> fieldMetas) {
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("select ");
		for (XList fieldMeta : fieldMetas) {
			sqlBuf.append(fieldMeta.getName()).append(",");
		}
		sqlBuf.deleteCharAt(sqlBuf.length() - 1);
		sqlBuf.append(" from ").append(tableName);
		return sqlBuf.toString();
	}

	/**
	 * 创建insert语句
	 * 
	 * @param tableName
	 * @param bizRecord
	 * @return
	 */
	private String createInsertSql(XList bizMeta, String tableName,
			XBizRecord bizRecord) {
		StringBuilder sqlBuf = new StringBuilder();
		StringBuilder paramBuf = new StringBuilder();
		StringBuilder valueBuf = new StringBuilder();

		Map<String, XBizField> fieldMap = bizRecord.getFieldMap();
		for (XBizField field : fieldMap.values()) {
			String fieldName = field.getName();
			Object fieldValue = field.getValue();
			paramBuf.append(fieldName).append(",");
			// 获取字段元数据定义
			XList fieldMeta = bizMeta.getFieldMeta(fieldName);
			// 根据字段类型转换字段数值
			String value = transferFieldValue(fieldMeta, fieldValue);
			valueBuf.append(value).append(",");
		}
		paramBuf.deleteCharAt(paramBuf.length() - 1);
		valueBuf.deleteCharAt(valueBuf.length() - 1);
		sqlBuf.append("INSERT INTO \"").append(tableName).append("\"");
		sqlBuf.append(" (").append(paramBuf).append(") ");
		sqlBuf.append(" VALUES (").append(valueBuf).append(");");
		return sqlBuf.toString();
	}

	/**
	 * 创建update语句
	 * 
	 * @param tableName
	 * @param bizRecord
	 * @param bizKey
	 * @return
	 */
	private String createUpdateSql(XList bizMeta, String tableName,
			XBizRecord bizRecord, XBizRecord bizKey) {
		StringBuilder sqlBuf = new StringBuilder();
		StringBuilder setClause = new StringBuilder();
		// 构造set子句
		Map<String, XBizField> fieldMap = bizRecord.getFieldMap();
		// 获取主键字段列表
		List<String> indexFields = bizMeta.getIndexList();
		for (XBizField field : fieldMap.values()) {
			String fieldName = field.getName();
			// update的set语句中不带主键字段
			if (indexFields.contains(fieldName))
				continue;
			Object fieldValue = field.getValue();
			// 获取字段元数据定义
			XList fieldMeta = bizMeta.getFieldMeta(fieldName);
			if (fieldMeta.isWritable()) {
				// 可写字段才能设置，根据字段类型转换字段数值
				String value = transferFieldValue(fieldMeta, fieldValue);
				setClause.append(fieldName).append("=").append(value)
						.append(",");
			}
		}
		setClause.deleteCharAt(setClause.length() - 1);
		// 根据主键构造where子句
		String whereClause = createWhereClause(bizMeta, bizKey);
		// 拼接sql
		sqlBuf.append("UPDATE ").append(tableName);
		sqlBuf.append(" SET ").append(setClause);
		sqlBuf.append(" WHERE 1=1 ").append(whereClause).append(";");
		return sqlBuf.toString();
	}

	/**
	 * 创建delete语句
	 * 
	 * @param tableName
	 * @param bizKey
	 * @return
	 */
	private String createDeleteSql(XList bizMeta, String tableName,
			XBizRecord bizKey) {
		StringBuilder sqlBuf = new StringBuilder();
		// 根据主键构造where子句
		String whereClause = createWhereClause(bizMeta, bizKey);
		// 拼接sql
		sqlBuf.append("DELETE FROM ").append(tableName);
		sqlBuf.append(" WHERE 1=1 ").append(whereClause).append(";");
		return sqlBuf.toString();
	}

	/**
	 * 根据主键构造where子句
	 * 
	 * @param bizMeta
	 * @param bizKey
	 * @return
	 */
	private String createWhereClause(XList bizMeta, XBizRecord bizKey) {
		// TODO
		StringBuilder whereClause = new StringBuilder();
		// 根据主键构造where子句
		List<String> indexFields = bizMeta.getIndexList();
		Map<String, XBizField> conditionMap = bizKey.getFieldMap();
		for (String indexField : indexFields) {
			XBizField field = conditionMap.get(indexField);
			String fieldName = field.getName();
			Object fieldValue = field.getValue();
			// 获取字段元数据定义
			XList fieldMeta = bizMeta.getFieldMeta(fieldName);
			// 根据字段类型转换字段数值
			String value = transferFieldValue(fieldMeta, fieldValue);
			whereClause.append(" AND ").append(fieldName).append("=")
					.append(value);
		}
		return whereClause.toString();
	}

	/**
	 * 构造增量配置消息
	 * 
	 * @param tableName
	 * @param sql
	 * @return
	 */
	private EnbAppMessage buildIncrementalConfigMessage(Long moId,
			String tableName, String sql, int actionType) throws Exception {
		// 根据moId获取eNB
		EnbAppMessage message = new EnbAppMessage();
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		Long enbId = enb.getEnbId();
		message.setEnbId(enbId);
		message.setMa(EnbMessageConstants.MA_CONF);
		message.setMoc(EnbMessageConstants.MOC_INCREMENTAL_CONFIG);
		message.setActionType(actionType);
		message.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		message.addTagValue(TagConst.DB_NAME, "lte");
		message.addTagValue(TagConst.TABLE_NAME, tableName);
		message.addTagValue(TagConst.SQL_TEXT, sql);

		EnbUtils.log(enbId, "eNB sql", sql);
		return message;
	}

	/**
	 * 构造整表配置消息
	 * 
	 * @param enbId
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private EnbAppMessage buildFullTableMessage(Long enbId, int moc,
			GenericBizData data) throws Exception {
		EnbAppMessage message = new EnbAppMessage();
		// 应用层包头
		message.setEnbId(enbId);
		message.setMa(EnbMessageConstants.MA_CONF);
		message.setMoc(moc);
		message.setActionType(EnbMessageConstants.ACTION_CONFIG);
		message.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		// 消息体
		message.addTagValue(TagConst.FTP_IP,
				data.getProperty(EnbConstantUtils.FTP_IP).getValue());
		message.addTagValue(TagConst.FTP_PORT,
				data.getProperty(EnbConstantUtils.FTP_PORT).getValue());
		message.addTagValue(TagConst.FTP_USER_NAME,
				data.getProperty(EnbConstantUtils.FTP_USER_NAME).getValue());
		message.addTagValue(TagConst.FTP_PASSWORD,
				data.getProperty(EnbConstantUtils.FTP_PASSWORD).getValue());
		message.addTagValue(TagConst.FILE_DIRECTORY,
				data.getProperty(EnbConstantUtils.FILE_DIRECTORY).getValue());
		message.addTagValue(TagConst.FILE_NAME,
				data.getProperty(EnbConstantUtils.FILE_NAME).getValue());
		return message;
	}

	/**
	 * 解析查询结果集
	 * 
	 * @param tableName
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	private XBizTable parseResultSet(Long moId, String tableName,
			List<XList> fieldMetas, EnbAppMessage resp) throws Exception {
		XBizTable bizTable = new XBizTable(moId, tableName);
		// 解析结果
		EnbMessageHelper.parseResponse(resp);
		//
		int rowNum = resp.getIntValue(TagConst.ROW_NUM);
		byte[] buf = resp.getByteValue(TagConst.RESULT_SET);
		int offset = 0;
		for (int i = 0; i < rowNum; i++) {
			XBizRecord record = new XBizRecord();
			for (XList fieldMeta : fieldMetas) {
				String fieldName = fieldMeta.getName();
				String fieldValue = "";
				int length = fieldMeta.getFieldLength();
				if (fieldMeta.isString()) {
					// 字符串
					fieldValue = ByteUtils.toString(buf, offset, length,
							"UTF-8").trim();
				} else if (fieldMeta.isUnsignedNum()) {
					// 无符号数
					long number = ByteUtils.toUnsignedNumber(buf, offset,
							length);
					fieldValue = String.valueOf(number);
				} else if (fieldMeta.isSignedNum()) {
					// 有符号数
					long number = ByteUtils.toSignedNumber(buf, offset, length);
					fieldValue = String.valueOf(number);
				} else if (fieldMeta.isHexArray()) {
					byte[] bytes = new byte[length];
					System.arraycopy(buf, offset, bytes, 0, length);
					// 将hexArray类型的数值统一转成小写
					fieldValue = ByteArrayUtil.toHexString(bytes).toLowerCase();
				}
				offset += length;
				record.addField(new XBizField(fieldName, fieldValue));
			}
			bizTable.addRecord(record);
		}
		return bizTable;
	}

}
