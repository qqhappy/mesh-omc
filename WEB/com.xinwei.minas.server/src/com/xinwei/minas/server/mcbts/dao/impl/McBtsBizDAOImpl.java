/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-12	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.server.mcbts.dao.McBtsBizDAO;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.GenericBizRecord;

/**
 * 
 * McBtsҵ��DAO�ӿ�ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class McBtsBizDAOImpl extends JdbcDaoSupport implements McBtsBizDAO {

	private static final Log logger = LogFactory.getLog(McBtsBizDAOImpl.class);

	@Override
	public GenericBizData queryAllBy(Long moId, String bizName,
			String condition, Object[] conditionValue) throws Exception {
		GenericBizData genericBizData = new GenericBizData(bizName);
		// ����ҵ�����ƻ�ȡ����
		String tableName = this.getTableNameBy(bizName);
		// ƴ��SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("select * from ").append(tableName);
		sqlBuf.append(" where moId=? ");
		if (condition != null && !condition.equals("")) {
			sqlBuf.append(" and (").append(condition).append(")");
		}
		// ƴ����������ֵ
		Object[] queryConditionValue = new Object[conditionValue.length + 1];
		queryConditionValue[0] = moId;
		System.arraycopy(conditionValue, 0, queryConditionValue, 1,
				conditionValue.length);
		// ִ�в�ѯ
		getJdbcTemplate().query(sqlBuf.toString(), queryConditionValue,
				new GenericRowMapper(genericBizData));
		return genericBizData;
	}

	@Override
	public List<GenericBizData> queryExportList(String bizName) {
		List<GenericBizData> result = new ArrayList<GenericBizData>();

		String tableName = this.getTableNameBy(bizName);
		String sql = "select * from " + tableName
				+ " where moId > 0 order by moId";
		// ִ�в�ѯ
		result = getJdbcTemplate().query(sql, new DataRowMapper(bizName));
		return result;
	}

	// @Override
	// public void addOrUpdate(Long moId, GenericBizData genericBizData)
	// throws Exception {
	// String bizName = genericBizData.getBizName();
	// // ����ҵ�����ƻ�ȡ����
	// String tableName = this.getTableNameBy(bizName);
	// // ��ȡ�����б�
	// Set<String> primaryKeys = genericBizData.getPrimaryKeys();
	// // ��ȡ��¼��
	// List<GenericBizRecord> records = genericBizData.getRecords();
	// for (GenericBizRecord record : records) {
	// if (isRecordExist(tableName, primaryKeys, record)) {
	// // ��¼���ڣ�ִ�и���
	// this.internalUpdate(tableName, primaryKeys, record);
	// } else {
	// // ִ������
	// this.internalInsert(tableName, moId, primaryKeys, record);
	// }
	// }
	// }

	@Override
	public void addOrUpdate(Long moId, GenericBizData genericBizData)
			throws Exception {
		SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);

		String bizName = genericBizData.getBizName();
		// ����ҵ�����ƻ�ȡ����
		String tableName = this.getTableNameBy(bizName);

		// ��ȡ��¼��
		List<GenericBizRecord> records = genericBizData.getRecords();

		for (GenericBizRecord record : records) {
			// �鿴���ݿ��Ƿ��ж�ӦmoId�ļ�¼
			int count = getJdbcTemplate().queryForInt(
					"select count(1) from " + tableName + " where moId = ?",
					new Object[] { moId });

			StringBuilder sb = new StringBuilder();
			String[] propNames = record.getPropertyNames();

			if (count == 0) {
				// ִ�в������
				sb.append("insert into ").append(tableName).append(" set ");
				sb.append("idx=").append(sequenceService.getNext())
						.append(", ");
				sb.append("moId=").append(moId);

			} else {
				// ִ�и��²���
				sb.append("update ").append(tableName).append(" set ");
				sb.append("moId=").append(moId);
			}

			for (String propName : propNames) {
				if (count == 0) {
					// �����������������idx
					if (propName.equals("idx")) {
						continue;
					}
				}
				sb.append(", ").append(propName).append("=");

				GenericBizProperty prop = record.getPropertyValue(propName);

				if (prop.getValue() instanceof String) {
					sb.append("'")
							.append(prop.getValue() == null ? "''" : prop
									.getValue()).append("'");
				} else {
					sb.append(prop.getValue() == null ? 0 : prop.getValue());
				}
			}

			if (count != 0) {
				sb.append(" where moId=").append(moId);
			}

			getJdbcTemplate().execute(sb.toString());
		}
	}

	@Override
	public void delete(String bizName, Long moId) throws Exception {
		// ����ҵ�����ƻ�ȡ����
		String tableName = this.getTableNameBy(bizName);
		String delSql = "delete from " + tableName + " where moId=" + moId;
		getJdbcTemplate().execute(delSql);
	}

	@Override
	public List<Map<String, Object>> queryEnumItem(String tableName,
			String sqlStatement) throws Exception {

		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("select * from ").append(tableName);
		sqlBuf.append(" where ").append(sqlStatement);
		// ִ�в�ѯ
		return getJdbcTemplate().queryForList(sqlBuf.toString());
	}

	/**
	 * ����ҵ�����ƻ�ȡ����
	 * 
	 * @param bizName
	 * @return
	 */
	private String getTableNameBy(String bizName) {
		return bizName;
	}

	/**
	 * ���ɰ�������ѯ����
	 * 
	 * @param primaryKeys
	 *            �����б�
	 * @param record
	 *            һ����¼
	 * @return
	 */
	private String generatePkCondition(Set<String> primaryKeys,
			GenericBizRecord record) {
		StringBuilder buf = new StringBuilder();
		int i = 0;
		for (String pk : primaryKeys) {
			GenericBizProperty pkValue = record.getPropertyValue(pk);
			if (pkValue != null && pkValue.getValue() != null) {
				if (i != 0) {
					buf.append(" and ");
				}
				buf.append(pk).append("='")
						.append(pkValue.getValue().toString()).append("'");
				i++;
			}
		}
		return buf.toString();
	}

	/**
	 * ���������жϼ�¼�Ƿ����
	 * 
	 * @param tableName
	 * @param primaryKeys
	 * @param record
	 * @return
	 */
	private boolean isRecordExist(String tableName, Set<String> primaryKeys,
			GenericBizRecord record) {
		//
		String pkCondition = generatePkCondition(primaryKeys, record);
		if (pkCondition == null || pkCondition.isEmpty()) {
			return false;
		}
		StringBuilder buf = new StringBuilder();
		buf.append("select * from ").append(tableName);
		buf.append(" where ");
		buf.append(generatePkCondition(primaryKeys, record));
		List<GenericBizRecord> records = getJdbcTemplate().query(
				buf.toString(), new BizRecordMapper());
		if (records == null || records.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * ������¼
	 * 
	 * @param tableName
	 * @param record
	 */
	private void internalInsert(String tableName, Long moId,
			Set<String> primaryKeys, GenericBizRecord record) {
		SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);
		StringBuilder buf = new StringBuilder();
		buf.append("insert into ").append(tableName);
		StringBuilder columnNames = new StringBuilder();
		StringBuilder columnValues = new StringBuilder();
		String[] propertyNames = record.getPropertyNames();
		for (String propertyName : propertyNames) {
			columnNames.append(propertyName).append(",");
			GenericBizProperty columnProperty = record
					.getPropertyValue(propertyName);
			if (columnProperty != null && columnProperty.getValue() != null) {
				columnValues.append("'")
						.append(columnProperty.getValue().toString())
						.append("',");
			} else {
				columnValues.append("'',");
			}
		}
		columnNames.append("moId");
		columnValues.append(moId);
		for (String primaryKey : primaryKeys) {
			// ��ȡ����ID
			long idx = sequenceService.getNext();
			columnNames.append(",").append(primaryKey);
			columnValues.append(",").append(idx);
		}
		buf.append("(");
		buf.append(columnNames);
		buf.append(") values(");
		buf.append(columnValues);
		buf.append(")");
		// �־û�
		logger.info("insert SQL: " + buf.toString());
		getJdbcTemplate().update(buf.toString());
	}

	/**
	 * ���¼�¼
	 * 
	 * @param tableName
	 * @param primaryKeys
	 * @param record
	 */
	private void internalUpdate(String tableName, Set<String> primaryKeys,
			GenericBizRecord record) {
		StringBuilder buf = new StringBuilder();
		buf.append("update ").append(tableName);
		buf.append(" set ");
		String[] propertyNames = record.getPropertyNames();
		for (String propertyName : propertyNames) {
			// ���������ֶ�
			if (primaryKeys.contains(propertyName)) {
				continue;
			}
			buf.append(propertyName).append("=");
			GenericBizProperty columnProperty = record
					.getPropertyValue(propertyName);
			if (columnProperty != null && columnProperty.getValue() != null) {
				buf.append("'").append(columnProperty.getValue().toString())
						.append("'");
			} else {
				buf.append("''");
			}
			buf.append(",");
		}
		buf.deleteCharAt(buf.length() - 1);
		buf.append(" where ");
		buf.append(generatePkCondition(primaryKeys, record));
		// �־û�
		logger.info("update SQL: " + buf.toString());
		getJdbcTemplate().update(buf.toString());
	}

	private class BizRecordMapper implements RowMapper {

		public Object mapRow(ResultSet rs, int index) throws SQLException {
			GenericBizRecord record = new GenericBizRecord();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int column = 1; column <= columnCount; column++) {
				String columnName = metaData.getColumnName(column);
				Object columnValue = rs.getObject(columnName);
				GenericBizProperty property = new GenericBizProperty();
				property.setName(columnName);
				property.setValue(columnValue);
				record.addProperty(property);
			}
			return record;
		}

	}

	private class GenericRowMapper implements RowMapper {

		private GenericBizData genericBizData;

		public GenericRowMapper(GenericBizData genericBizData) {
			this.genericBizData = genericBizData;
		}

		public Object mapRow(ResultSet rs, int index) throws SQLException {
			List<GenericBizProperty> properties = new LinkedList();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int column = 1; column <= columnCount; column++) {
				String columnName = metaData.getColumnName(column);
				Object columnValue = rs.getObject(columnName);
				GenericBizProperty property = new GenericBizProperty();
				property.setName(columnName);
				property.setValue(columnValue);
				properties.add(property);
				genericBizData.addProperty(property);
			}
			return properties;
		}

	}

	private class DataRowMapper implements RowMapper<GenericBizData> {

		private String bizName;

		public DataRowMapper(String bizName) {
			super();
			this.bizName = bizName;
		}

		@Override
		public GenericBizData mapRow(ResultSet rs, int index)
				throws SQLException {
			GenericBizData bizData = new GenericBizData(bizName);

			List<GenericBizProperty> properties = new LinkedList<GenericBizProperty>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int column = 1; column <= columnCount; column++) {
				String columnName = metaData.getColumnName(column);
				Object columnValue = rs.getObject(columnName);
				GenericBizProperty property = new GenericBizProperty();
				property.setName(columnName);
				property.setValue(columnValue);
				properties.add(property);
				bizData.addProperty(property);
			}
			return bizData;
		}
	}

	private class RecordNumRowMapper implements RowMapper {

		public Object mapRow(ResultSet rs, int index) throws SQLException {
			int num = rs.getInt("num");
			return num;
		}

	}

}
