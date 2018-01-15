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
 * eNB通用配置DAO实现
 * 
 * @author chenjunhua
 * 
 */

public class EnbBizConfigDAOImpl extends JdbcDaoSupport implements
		EnbBizConfigDAO {
	
	private Log log = LogFactory.getLog(EnbBizConfigDAOImpl.class);
	
	// 网元业务标准表
	private String BIZ_TABLE = "enb_biz_table";
	
	// 小区管理表
	private String BIZ_CELL = "enb_cell_basic";
	
	
	@Override
	public XBizTable query(Long moId, String tableName, XBizRecord condition)
			throws Exception {
		XBizTable bizTable = new XBizTable(moId, tableName);
		// 拼接SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append(" select * from ").append(BIZ_TABLE);
		sqlBuf.append(" where mo_id=? and biz_name=?");
		sqlBuf.append(" order by biz_key");
		// 拼接条件参数值
		Object[] queryConditionValue = new Object[] { moId, tableName };
		/*log.debug(" select * from " + BIZ_TABLE + " where mo_id=" + moId
				+ " and biz_name='" + tableName + "' order by biz_key");*/
		// 执行SQL
		getJdbcTemplate().query(sqlBuf.toString(), queryConditionValue,
				new GenericRowMapper(bizTable));
		// 根据条件过滤表数据
		if (condition != null) {
			XList bizMeta = EnbBizHelper.getBizMetaBy(moId, tableName);
			bizTable.filterRecord(condition, bizMeta);
		}
		// 如果格式与当前版本不一致，则按照当前版本格式转换
		XBizTable newTable = convertFormat(moId, bizTable);
		return newTable;
	}
	
	/**
	 * 如果数据格式与当前版本格式不符，则按版本格式进行转换
	 * 
	 * @param moId
	 * @param xBizTable
	 * @return
	 */
	private XBizTable convertFormat(long moId, XBizTable xBizTable) {
		String tableName = xBizTable.getTableName();
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		String protocolVersion = enb.getProtocolVersion();
		// 如果数据格式与当前版本匹配，则无需转换
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
				// 如果没有新格式中的字段，则添加默认值
				if (record.getFieldBy(fieldMeta.getName()) == null) {
					newRecord.addField(new XBizField(fieldName, fieldMeta
							.getPropertyValue(XList.P_DEFAULT)));
				}
				else {
					newRecord.addField(record.getFieldBy(fieldName));
				}
			}
			// 按配置调整字段顺序
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
		
		// 拼接SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append(" select * from ").append(BIZ_TABLE);
		sqlBuf.append(" where mo_id=? and biz_name=? and biz_key=?");
		// 拼接条件参数值
		String keyJsonStr = XJsonUtils.object2JsonObjStr(bizKey);
		Object[] queryConditionValue = new Object[] { moId, tableName,
				keyJsonStr };
		log.debug(" select * from " + BIZ_TABLE + " where mo_id=" + moId
				+ " and biz_name='" + tableName + "' and biz_key='"
				+ keyJsonStr + "'");
		// 执行SQL
		getJdbcTemplate().query(sqlBuf.toString(), queryConditionValue,
				new GenericRowMapper(bizTable));
		// 获取结果
		List<XBizRecord> records = bizTable.getRecords();
		if (records.isEmpty()) {
			return null;
		}
		// 如果格式与当前版本不一致，则按照当前版本格式转换
		XBizTable newTable = convertFormat(moId, bizTable);
		return newTable.getRecords().get(0);
	}
	
	@Override
	public void add(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception {
		// 获取主键
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId, tableName,
				bizRecord);
		
		add(moId, tableName, bizKey, bizRecord);
	}
	
	@Override
	public void add(Long moId, String tableName, XBizRecord bizKey,
			XBizRecord bizRecord) throws Exception {
		// 拼接SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append(" insert into ").append(BIZ_TABLE);
		sqlBuf.append(" (mo_id, biz_name, biz_key, biz_parameter) ");
		sqlBuf.append(" values (?,?,?,?)");
		// 执行SQL
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
		// 拼接SQL
		StringBuilder sqlBuf = new StringBuilder();
		// 遍历表
		for (XBizTable bizTable : tableList) {
			if (EnbBizHelper.hasRecord(bizTable)) {
				String tableName = bizTable.getTableName();
				// 遍历记录
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
		// 执行SQL
		getJdbcTemplate().execute(sql);
	}
	
	@Override
	public void update(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception {
		// 获取主键
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId, tableName,
				bizRecord);
		// 拼接SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append(" update ").append(BIZ_TABLE);
		sqlBuf.append(" set biz_parameter=? ");
		sqlBuf.append(" where mo_id=? and biz_name=? and biz_key=?");
		// 执行SQL
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
		// 拼接SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append(" delete from ").append(BIZ_TABLE);
		sqlBuf.append(" where mo_id=? and biz_name=?");
		// 如果bizKey为null则删除表中所有记录
		if (bizKey == null) {
			log.debug(" delete from " + BIZ_TABLE + " where mo_id=" + moId
					+ " and biz_name='" + tableName + "'");
			getJdbcTemplate().update(sqlBuf.toString(), moId, tableName);
		}
		else {
			sqlBuf.append(" and biz_key=?");
			// 执行SQL
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
		// 拼接SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append(" delete from ").append(BIZ_TABLE);
		sqlBuf.append(" where mo_id=?");
		log.debug(" delete from " + BIZ_TABLE + " where mo_id=" + moId);
		// 执行SQL
		getJdbcTemplate().update(sqlBuf.toString(), moId);
	}
	
	@Override
	public void deleteAll(Long moId, String tableName) throws Exception {
		// 拼接SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append(" delete from ").append(BIZ_TABLE);
		sqlBuf.append(" where mo_id=? and biz_name=?");
		log.debug(" delete from " + BIZ_TABLE + " where mo_id=" + moId
				+ " and biz_name='" + tableName + "'");
		// 执行SQL
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
			// 调整record中field的顺序
			// XBizRecord newRecord = EnbBizHelper.formatBizRecord(version,
			// bizTable.getTableName(), record);
			bizTable.addRecord(record);
			return record;
			
		}
		
	}
	
	@Override
	public void addScene(long moId, int cid, XBizRecord bizRecord)
			throws Exception {
		// 先判断小区场景表中是否已经包含该小区数据,如果包含认为脏数据,删除掉
		XBizRecord sceneRecord = querySceneByMoIdAndCid(moId, cid);
		if(null != sceneRecord) {
			deleteScene(moId, cid);
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ").append(BIZ_CELL);
		sql.append(" (mo_id, cid, biz_parameter) ");
		sql.append(" values (?,?,?)");
		// 执行SQL
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
		// 拼接条件参数值
		Object[] queryConditionValue = new Object[] { moId };
		log.debug(" select * from " + BIZ_CELL + " where mo_id=" + moId
				+ "' order by cid");
		XBizSceneTable bizSenceTable = new XBizSceneTable(moId);
		// 执行SQL
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
		// 拼接条件参数值
		Object[] queryConditionValue = new Object[] { moId, cid };
		log.debug(" select * from " + BIZ_CELL + " where mo_id=" + moId
				+ "' and cid=" + cid);
		XBizSceneTable bizSenceTable = new XBizSceneTable(moId);
		// 执行SQL
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
		// 拼接SQL
		StringBuilder sql = new StringBuilder();
		sql.append(" update ").append(BIZ_CELL);
		sql.append(" set biz_parameter=? ");
		sql.append(" where mo_id=? and cid=?");
		// 执行SQL
		String recordJsonStr = XJsonUtils.object2JsonObjStr(bizRecord);
		log.debug(" update " + BIZ_CELL + " set biz_parameter='"
				+ recordJsonStr + "' where mo_id=" + moId + " and cid='" + cid);
		getJdbcTemplate().update(sql.toString(), recordJsonStr, moId, cid);
		
	}
}
