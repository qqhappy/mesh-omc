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
 * ͨ����Ԫ����Proxyʵ��
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
		// ��ȡҵ��Ԫ���ݶ���
		XList bizMeta = EnbBizHelper.getBizMetaBy(enbTypeId, version, tableName);
		// ����insert���
		String sql = createInsertSql(bizMeta, tableName, bizRecord);
		return sql;
	}

	@Override
	public XBizTable query(Long moId, String tableName, List<XList> fieldMetas)
			throws Exception {
		// �����ѯ���
		String sql = this.createQuerySql(tableName, fieldMetas);
		// ����Ԫ������Ϣ
		EnbAppMessage req = buildIncrementalConfigMessage(moId, tableName, sql,
				EnbMessageConstants.ACTION_QUERY);
		if (enbConnector != null) {
			EnbAppMessage resp = enbConnector.syncInvoke(req);
			// ����Ӧ����
			return parseResultSet(moId, tableName, fieldMetas, resp);
		}
		return new XBizTable(moId, tableName);
	}

	@Override
	public void add(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		// ����insert���
		String sql = this.generateInsertSql(enb.getEnbType(), enb.getProtocolVersion(),
				tableName, bizRecord);
		// ����Ԫ������Ϣ
		EnbAppMessage req = buildIncrementalConfigMessage(moId, tableName, sql,
				EnbMessageConstants.ACTION_ADD);
		if (enbConnector != null) {
			EnbAppMessage resp = enbConnector.syncInvoke(req);
			// ����Ӧ����
			EnbMessageHelper.parseResponse(resp);
		}
	}

	@Override
	public void update(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception {
		// ��ȡ����
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId, tableName,
				bizRecord);
		// ��ȡҵ��Ԫ���ݶ���
		XList bizMeta = EnbBizHelper.getBizMetaBy(moId, tableName);
		// ����update���
		String sql = createUpdateSql(bizMeta, tableName, bizRecord, bizKey);
		// ����Ԫ������Ϣ
		EnbAppMessage req = buildIncrementalConfigMessage(moId, tableName, sql,
				EnbMessageConstants.ACTION_MODIFY);
		if (enbConnector != null) {
			EnbAppMessage resp = enbConnector.syncInvoke(req);
			// ����Ӧ����
			EnbMessageHelper.parseResponse(resp);
		}

	}

	@Override
	public void delete(Long moId, String tableName, XBizRecord bizKey)
			throws Exception {
		// ��ȡҵ��Ԫ���ݶ���
		XList bizMeta = EnbBizHelper.getBizMetaBy(moId, tableName);
		// ����delete���
		String sql = createDeleteSql(bizMeta, tableName, bizKey);
		// ����Ԫ������Ϣ
		EnbAppMessage req = buildIncrementalConfigMessage(moId, tableName, sql,
				EnbMessageConstants.ACTION_DELETE);
		if (enbConnector != null) {
			EnbAppMessage resp = enbConnector.syncInvoke(req);
			// ����Ӧ����
			EnbMessageHelper.parseResponse(resp);
		}
	}

	@Override
	public void fullTableConfig(Long enbId, GenericBizData data, int bizFlag)
			throws Exception {

		// ����Ԫ������Ϣ
		int moc = 0;
		switch (bizFlag) {
		case 0:
			// ��������
			moc = EnbMessageConstants.MOC_FULLTABLECONFIG_CONFIG;
			break;
		case 1:
			// ������
			moc = EnbMessageConstants.MOC_FULLTABLEREVERSE_CONFIG;
			break;
		default:
			// û���򷵻�
			return;
		}
		EnbAppMessage req = buildFullTableMessage(enbId, moc, data);
		if (enbConnector != null) {
			EnbAppMessage resp = enbConnector.syncInvoke(req);
			// ����Ӧ����
			EnbMessageHelper.parseResponse(resp);
		}
	}

	/**
	 * �����ֶ�Ԫ���ݶ���ת���ֶ���ֵ
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
			// 16��������
			value = "x'" + value + "'";
		} else {
			value = "'" + value + "'";
		}
		return value;
	}

	/**
	 * ������ѯ���
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
	 * ����insert���
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
			// ��ȡ�ֶ�Ԫ���ݶ���
			XList fieldMeta = bizMeta.getFieldMeta(fieldName);
			// �����ֶ�����ת���ֶ���ֵ
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
	 * ����update���
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
		// ����set�Ӿ�
		Map<String, XBizField> fieldMap = bizRecord.getFieldMap();
		// ��ȡ�����ֶ��б�
		List<String> indexFields = bizMeta.getIndexList();
		for (XBizField field : fieldMap.values()) {
			String fieldName = field.getName();
			// update��set����в��������ֶ�
			if (indexFields.contains(fieldName))
				continue;
			Object fieldValue = field.getValue();
			// ��ȡ�ֶ�Ԫ���ݶ���
			XList fieldMeta = bizMeta.getFieldMeta(fieldName);
			if (fieldMeta.isWritable()) {
				// ��д�ֶβ������ã������ֶ�����ת���ֶ���ֵ
				String value = transferFieldValue(fieldMeta, fieldValue);
				setClause.append(fieldName).append("=").append(value)
						.append(",");
			}
		}
		setClause.deleteCharAt(setClause.length() - 1);
		// ������������where�Ӿ�
		String whereClause = createWhereClause(bizMeta, bizKey);
		// ƴ��sql
		sqlBuf.append("UPDATE ").append(tableName);
		sqlBuf.append(" SET ").append(setClause);
		sqlBuf.append(" WHERE 1=1 ").append(whereClause).append(";");
		return sqlBuf.toString();
	}

	/**
	 * ����delete���
	 * 
	 * @param tableName
	 * @param bizKey
	 * @return
	 */
	private String createDeleteSql(XList bizMeta, String tableName,
			XBizRecord bizKey) {
		StringBuilder sqlBuf = new StringBuilder();
		// ������������where�Ӿ�
		String whereClause = createWhereClause(bizMeta, bizKey);
		// ƴ��sql
		sqlBuf.append("DELETE FROM ").append(tableName);
		sqlBuf.append(" WHERE 1=1 ").append(whereClause).append(";");
		return sqlBuf.toString();
	}

	/**
	 * ������������where�Ӿ�
	 * 
	 * @param bizMeta
	 * @param bizKey
	 * @return
	 */
	private String createWhereClause(XList bizMeta, XBizRecord bizKey) {
		// TODO
		StringBuilder whereClause = new StringBuilder();
		// ������������where�Ӿ�
		List<String> indexFields = bizMeta.getIndexList();
		Map<String, XBizField> conditionMap = bizKey.getFieldMap();
		for (String indexField : indexFields) {
			XBizField field = conditionMap.get(indexField);
			String fieldName = field.getName();
			Object fieldValue = field.getValue();
			// ��ȡ�ֶ�Ԫ���ݶ���
			XList fieldMeta = bizMeta.getFieldMeta(fieldName);
			// �����ֶ�����ת���ֶ���ֵ
			String value = transferFieldValue(fieldMeta, fieldValue);
			whereClause.append(" AND ").append(fieldName).append("=")
					.append(value);
		}
		return whereClause.toString();
	}

	/**
	 * ��������������Ϣ
	 * 
	 * @param tableName
	 * @param sql
	 * @return
	 */
	private EnbAppMessage buildIncrementalConfigMessage(Long moId,
			String tableName, String sql, int actionType) throws Exception {
		// ����moId��ȡeNB
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
	 * ��������������Ϣ
	 * 
	 * @param enbId
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private EnbAppMessage buildFullTableMessage(Long enbId, int moc,
			GenericBizData data) throws Exception {
		EnbAppMessage message = new EnbAppMessage();
		// Ӧ�ò��ͷ
		message.setEnbId(enbId);
		message.setMa(EnbMessageConstants.MA_CONF);
		message.setMoc(moc);
		message.setActionType(EnbMessageConstants.ACTION_CONFIG);
		message.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		// ��Ϣ��
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
	 * ������ѯ�����
	 * 
	 * @param tableName
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	private XBizTable parseResultSet(Long moId, String tableName,
			List<XList> fieldMetas, EnbAppMessage resp) throws Exception {
		XBizTable bizTable = new XBizTable(moId, tableName);
		// �������
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
					// �ַ���
					fieldValue = ByteUtils.toString(buf, offset, length,
							"UTF-8").trim();
				} else if (fieldMeta.isUnsignedNum()) {
					// �޷�����
					long number = ByteUtils.toUnsignedNumber(buf, offset,
							length);
					fieldValue = String.valueOf(number);
				} else if (fieldMeta.isSignedNum()) {
					// �з�����
					long number = ByteUtils.toSignedNumber(buf, offset, length);
					fieldValue = String.valueOf(number);
				} else if (fieldMeta.isHexArray()) {
					byte[] bytes = new byte[length];
					System.arraycopy(buf, offset, bytes, 0, length);
					// ��hexArray���͵���ֵͳһת��Сд
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
