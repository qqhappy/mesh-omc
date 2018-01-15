/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.core.model.conf.KeyDesc;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.dao.EnbBizConfigDAO;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizSceneTable;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.core.model.meta.XMetaRef;
import com.xinwei.omp.server.utils.XJsonUtils;

/**
 * 
 * eNBͨ������DAOʵ��
 * 
 * @author chenjunhua
 * 
 */

public class EnbBizConfigDAOImpl extends JdbcDaoSupport implements
		EnbBizConfigDAO {
	
	private Log log = LogFactory.getLog(EnbBizConfigDAOImpl.class);
	
	// ��Ԫҵ���׼��
	private String BIZ_TABLE = "enb_biz_table";
	
	// С�������
	private String BIZ_CELL = "enb_cell_basic";
	
	
	@Override
	public XBizTable query(Long moId, String tableName, XBizRecord condition)
			throws Exception {
		XBizTable bizTable = new XBizTable(moId, tableName);
		// ƴ��SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append(" select * from ").append(BIZ_TABLE);
		sqlBuf.append(" where mo_id=? and biz_name=?");
		sqlBuf.append(" order by biz_key");
		// ƴ����������ֵ
		Object[] queryConditionValue = new Object[] { moId, tableName };
		/*log.debug(" select * from " + BIZ_TABLE + " where mo_id=" + moId
				+ " and biz_name='" + tableName + "' order by biz_key");*/
		// ִ��SQL
		getJdbcTemplate().query(sqlBuf.toString(), queryConditionValue,
				new GenericRowMapper(bizTable));
		// �����������˱�����
		if (condition != null) {
			XList bizMeta = EnbBizHelper.getBizMetaBy(moId, tableName);
			bizTable.filterRecord(condition, bizMeta);
		}
		// �����ʽ�뵱ǰ�汾��һ�£����յ�ǰ�汾��ʽת��
		XBizTable newTable = convertFormat(moId, bizTable);
		return newTable;
	}
	
	/**
	 * ������ݸ�ʽ�뵱ǰ�汾��ʽ�������򰴰汾��ʽ����ת��
	 * 
	 * @param moId
	 * @param xBizTable
	 * @return
	 */
	private XBizTable convertFormat(long moId, XBizTable xBizTable) {
		String tableName = xBizTable.getTableName();
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		String protocolVersion = enb.getProtocolVersion();
		// ������ݸ�ʽ�뵱ǰ�汾ƥ�䣬������ת��
		if (EnbBizHelper.isDataMatchVersion(enb.getEnbType(), protocolVersion,
				xBizTable))
			return xBizTable;
		XList bizMeta = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				protocolVersion, xBizTable.getTableName());
		if (bizMeta == null) {
			return xBizTable;
		}
		List<XList> xList = bizMeta.getAllFields();
		XBizTable newTable = new XBizTable(moId, tableName);
		List<XBizRecord> records = new ArrayList<XBizRecord>();
		for (XBizRecord record : xBizTable.getRecords()) {
			XBizRecord newRecord = new XBizRecord();
			for (XList fieldMeta : xList) {
				String fieldName = fieldMeta.getName();
				// ���û���¸�ʽ�е��ֶΣ������Ĭ��ֵ
				if (record.getFieldBy(fieldMeta.getName()) == null) {
					newRecord.addField(new XBizField(fieldName, fieldMeta
							.getPropertyValue(XList.P_DEFAULT)));
				}
				else {
					newRecord.addField(record.getFieldBy(fieldName));
				}
			}
			// �����õ����ֶ�˳��
			newRecord = EnbBizHelper.formatBizRecord(enb.getEnbType(),
					protocolVersion, tableName, newRecord);
			records.add(newRecord);
		}
		newTable.setRecords(records);
		return newTable;
	}
	
	@Override
	public XBizRecord queryByKey(Long moId, String tableName, XBizRecord bizKey)
			throws Exception {
		XBizTable bizTable = new XBizTable(moId, tableName);
		
		// ƴ��SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append(" select * from ").append(BIZ_TABLE);
		sqlBuf.append(" where mo_id=? and biz_name=? and biz_key=?");
		// ƴ����������ֵ
		String keyJsonStr = XJsonUtils.object2JsonObjStr(bizKey);
		Object[] queryConditionValue = new Object[] { moId, tableName,
				keyJsonStr };
		log.debug(" select * from " + BIZ_TABLE + " where mo_id=" + moId
				+ " and biz_name='" + tableName + "' and biz_key='"
				+ keyJsonStr + "'");
		// ִ��SQL
		getJdbcTemplate().query(sqlBuf.toString(), queryConditionValue,
				new GenericRowMapper(bizTable));
		// ��ȡ���
		List<XBizRecord> records = bizTable.getRecords();
		if (records.isEmpty()) {
			return null;
		}
		// �����ʽ�뵱ǰ�汾��һ�£����յ�ǰ�汾��ʽת��
		XBizTable newTable = convertFormat(moId, bizTable);
		return newTable.getRecords().get(0);
	}
	
	@Override
	public void add(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception {
		// ��ȡ����
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId, tableName,
				bizRecord);
		
		add(moId, tableName, bizKey, bizRecord);
	}
	
	@Override
	public void add(Long moId, String tableName, XBizRecord bizKey,
			XBizRecord bizRecord) throws Exception {
		// ƴ��SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append(" insert into ").append(BIZ_TABLE);
		sqlBuf.append(" (mo_id, biz_name, biz_key, biz_parameter) ");
		sqlBuf.append(" values (?,?,?,?)");
		// ִ��SQL
		String recordJsonStr = XJsonUtils.object2JsonObjStr(bizRecord);
		String keyJsonStr = XJsonUtils.object2JsonObjStr(bizKey);
		log.debug(" insert into " + BIZ_TABLE
				+ " (mo_id, biz_name, biz_key, biz_parameter) " + " values ("
				+ moId + ",'" + tableName + "','" + keyJsonStr + "','"
				+ recordJsonStr + "')");
		getJdbcTemplate().update(sqlBuf.toString(), moId, tableName,
				keyJsonStr, recordJsonStr);
	}
	
	@Override
	public void batchAdd(Long moId, List<XBizTable> tableList) throws Exception {
		if (tableList == null || tableList.isEmpty())
			return;
		// ƴ��SQL
		StringBuilder sqlBuf = new StringBuilder();
		// ������
		for (XBizTable bizTable : tableList) {
			if (EnbBizHelper.hasRecord(bizTable)) {
				String tableName = bizTable.getTableName();
				// ������¼
				for (XBizRecord bizRecord : bizTable.getRecords()) {
					XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId,
							tableName, bizRecord);
					String recordJsonStr = XJsonUtils
							.object2JsonObjStr(bizRecord);
					String keyJsonStr = XJsonUtils.object2JsonObjStr(bizKey);
					sqlBuf.append("(").append(moId).append(",'")
							.append(tableName).append("','").append(keyJsonStr)
							.append("','").append(recordJsonStr).append("'),");
				}
			}
		}
		if (sqlBuf.length() == 0)
			return;
		String valueString = sqlBuf.toString()
				.substring(0, sqlBuf.length() - 1);
		String sql = "insert into " + BIZ_TABLE
				+ " (mo_id, biz_name, biz_key, biz_parameter) values"
				+ valueString + ";";
		// ִ��SQL
		getJdbcTemplate().execute(sql);
	}
	
	@Override
	public void update(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception {
		// ��ȡ����
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId, tableName,
				bizRecord);
		// ƴ��SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append(" update ").append(BIZ_TABLE);
		sqlBuf.append(" set biz_parameter=? ");
		sqlBuf.append(" where mo_id=? and biz_name=? and biz_key=?");
		// ִ��SQL
		String recordJsonStr = XJsonUtils.object2JsonObjStr(bizRecord);
		String keyJsonStr = XJsonUtils.object2JsonObjStr(bizKey);
		log.debug(" update " + BIZ_TABLE + " set biz_parameter='"
				+ recordJsonStr + "' where mo_id=" + moId + " and biz_name='"
				+ tableName + "' and biz_key='" + keyJsonStr + "'");
		getJdbcTemplate().update(sqlBuf.toString(), recordJsonStr, moId,
				tableName, keyJsonStr);
	}
	
	@Override
	public void delete(Long moId, String tableName, XBizRecord bizKey)
			throws Exception {
		// ƴ��SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append(" delete from ").append(BIZ_TABLE);
		sqlBuf.append(" where mo_id=? and biz_name=?");
		// ���bizKeyΪnull��ɾ���������м�¼
		if (bizKey == null) {
			log.debug(" delete from " + BIZ_TABLE + " where mo_id=" + moId
					+ " and biz_name='" + tableName + "'");
			getJdbcTemplate().update(sqlBuf.toString(), moId, tableName);
		}
		else {
			sqlBuf.append(" and biz_key=?");
			// ִ��SQL
			String keyJsonStr = XJsonUtils.object2JsonObjStr(bizKey);
			log.debug(" delete from " + BIZ_TABLE + " where mo_id=" + moId
					+ " and biz_name='" + tableName + "' and biz_key='"
					+ keyJsonStr + "'");
			getJdbcTemplate().update(sqlBuf.toString(), moId, tableName,
					keyJsonStr);
		}
		
	}
	
	@Override
	public void deleteAll(Long moId) throws Exception {
		// ƴ��SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append(" delete from ").append(BIZ_TABLE);
		sqlBuf.append(" where mo_id=?");
		log.debug(" delete from " + BIZ_TABLE + " where mo_id=" + moId);
		// ִ��SQL
		getJdbcTemplate().update(sqlBuf.toString(), moId);
	}
	
	@Override
	public void deleteAll(Long moId, String tableName) throws Exception {
		// ƴ��SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append(" delete from ").append(BIZ_TABLE);
		sqlBuf.append(" where mo_id=? and biz_name=?");
		log.debug(" delete from " + BIZ_TABLE + " where mo_id=" + moId
				+ " and biz_name='" + tableName + "'");
		// ִ��SQL
		getJdbcTemplate().update(sqlBuf.toString(), moId, tableName);
		
	}
	
	@Override
	public List<KeyDesc> queryByMetaRef(Long moId, List<XMetaRef> metaRefList)
			throws Exception {
		List<KeyDesc> keyDescList = new LinkedList<KeyDesc>();
		for (XMetaRef metaRef : metaRefList) {
			List<KeyDesc> list = queryByMetaRef(moId, metaRef);
			keyDescList.addAll(list);
		}
		return keyDescList;
	}
	
	private List<KeyDesc> queryByMetaRef(Long moId, XMetaRef metaRef)
			throws Exception {
		List<KeyDesc> keyDescList = new LinkedList<KeyDesc>();
		String tableName = metaRef.getRefTable();
		XBizRecord condition = generateConditionBy(metaRef);
		XBizTable bizTable = this.query(moId, tableName, condition);
		List<XBizRecord> records = bizTable.getRecords();
		for (XBizRecord record : records) {
			XBizField keyField = record.getFieldBy(metaRef.getKeyColumn());
			XBizField descField = record.getFieldBy(metaRef.getDescColumn());
			KeyDesc keyDesc = new KeyDesc(keyField.getValue(),
					descField.getValue());
			keyDescList.add(keyDesc);
		}
		return keyDescList;
	}
	
	private XBizRecord generateConditionBy(XMetaRef metaRef) {
		XBizRecord condition = new XBizRecord();
		String whereClause = metaRef.getWhereClause();
		if (whereClause == null) {
			return null;
		}
		// TODO: generateConditionBy
		condition.addField(new XBizField());
		return condition;
	}
	
	private class GenericRowMapper implements RowMapper<XBizRecord> {
		
		private XBizTable bizTable;
		
		public GenericRowMapper(XBizTable bizTable) {
			this.bizTable = bizTable;
		}
		
		public XBizRecord mapRow(ResultSet rs, int index) throws SQLException {
			String bizParameter = rs.getString("biz_parameter");
			XBizRecord record = XJsonUtils.jsonObjStr2Object(bizParameter,
					XBizRecord.class);
			// ����record��field��˳��
			// XBizRecord newRecord = EnbBizHelper.formatBizRecord(version,
			// bizTable.getTableName(), record);
			bizTable.addRecord(record);
			return record;
			
		}
		
	}
	
	@Override
	public void addScene(long moId, int cid, XBizRecord bizRecord)
			throws Exception {
		// ���ж�С�����������Ƿ��Ѿ�������С������,���������Ϊ������,ɾ����
		XBizRecord sceneRecord = querySceneByMoIdAndCid(moId, cid);
		if(null != sceneRecord) {
			deleteScene(moId, cid);
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ").append(BIZ_CELL);
		sql.append(" (mo_id, cid, biz_parameter) ");
		sql.append(" values (?,?,?)");
		// ִ��SQL
		String recordJsonStr = XJsonUtils.object2JsonObjStr(bizRecord);
		log.debug(" insert into " + BIZ_CELL + " (mo_id, cid, biz_parameter) "
				+ " values (" + moId + ",'" + cid + "','" + recordJsonStr
				+ "')");
		getJdbcTemplate().update(sql.toString(), moId, cid, recordJsonStr);
	}
	
	@Override
	public XBizSceneTable querySceneByMoId(long moId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from ").append(BIZ_CELL);
		sql.append(" where mo_id=?");
		sql.append(" order by cid");
		// ƴ����������ֵ
		Object[] queryConditionValue = new Object[] { moId };
		log.debug(" select * from " + BIZ_CELL + " where mo_id=" + moId
				+ "' order by cid");
		XBizSceneTable bizSenceTable = new XBizSceneTable(moId);
		// ִ��SQL
		getJdbcTemplate().query(sql.toString(), queryConditionValue,
				new SenceRowMapper(bizSenceTable));
		return bizSenceTable;
	}
	
	private class SenceRowMapper implements RowMapper<XBizRecord> {
		
		private XBizSceneTable bizSenceTable;
		
		public SenceRowMapper(XBizSceneTable bizSenceTable) {
			this.bizSenceTable = bizSenceTable;
		}
		
		@Override
		public XBizRecord mapRow(ResultSet rs, int index) throws SQLException {
			String bizParameter = rs.getString("biz_parameter");
			String cid = rs.getString("cid");
			XBizRecord record = XJsonUtils.jsonObjStr2Object(bizParameter,
					XBizRecord.class);
			record.addField(new XBizField(EnbConstantUtils.FIELD_NAME_CELL_ID,
					cid));
			bizSenceTable.addRecord(record);
			return record;
		}
	}
	
	@Override
	public XBizRecord querySceneByMoIdAndCid(long moId, int cid)
			throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from ").append(BIZ_CELL);
		sql.append(" where mo_id=? and cid=?");
		// ƴ����������ֵ
		Object[] queryConditionValue = new Object[] { moId, cid };
		log.debug(" select * from " + BIZ_CELL + " where mo_id=" + moId
				+ "' and cid=" + cid);
		XBizSceneTable bizSenceTable = new XBizSceneTable(moId);
		// ִ��SQL
		getJdbcTemplate().query(sql.toString(), queryConditionValue,
				new SenceRowMapper(bizSenceTable));
		if (null != bizSenceTable.getRecords()
				&& bizSenceTable.getRecords().size() > 0) {
			return bizSenceTable.getRecords().get(0);
		}
		return null;
	}
	
	@Override
	public void deleteScene(long moId, int cid) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from ").append(BIZ_CELL);
		sql.append(" where mo_id=? and cid=?");
		log.debug("delete from " + BIZ_CELL + " where mo_id=" + moId
				+ " and cid=" + cid);
		getJdbcTemplate().update(sql.toString(), moId, cid);
	}
	
	@Override
	public void updateScene(long moId, int cid, XBizRecord bizRecord)
			throws Exception {
		// ƴ��SQL
		StringBuilder sql = new StringBuilder();
		sql.append(" update ").append(BIZ_CELL);
		sql.append(" set biz_parameter=? ");
		sql.append(" where mo_id=? and cid=?");
		// ִ��SQL
		String recordJsonStr = XJsonUtils.object2JsonObjStr(bizRecord);
		log.debug(" update " + BIZ_CELL + " set biz_parameter='"
				+ recordJsonStr + "' where mo_id=" + moId + " and cid='" + cid);
		getJdbcTemplate().update(sql.toString(), recordJsonStr, moId, cid);
		
	}
}
