/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-12	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer1.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.mcbts.core.model.layer1.AntennaCalibrationConfig;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfCalibAntDataConfigDAO;

/**
 * 
 * 校准数据天线校准参数配置信息DAO实现
 * 
 * @author chenjunhua
 * 
 */

public class TConfCalibAntDataConfigDAOImpl extends JdbcDaoSupport implements
		TConfCalibAntDataConfigDAO {

	public List<AntennaCalibrationConfig> queryByMoId(Long moId) {
		List<AntennaCalibrationConfig> antConfigList = new ArrayList<AntennaCalibrationConfig>();
		// 获取表名
		String tableName = getTableName();
		// 拼接SQL
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("select * from ").append(tableName);
		sqlBuf.append(" where moId=? ");
		// 拼接条件参数值
		Object[] queryConditionValue = new Object[1];
		queryConditionValue[0] = moId;
		// 执行查询
		getJdbcTemplate().query(sqlBuf.toString(), queryConditionValue,
				new AntennaCalibrationConfigRowMapper(antConfigList));
		return antConfigList;
	}

	public void saveOrUpdate(List<AntennaCalibrationConfig> configList) {
		if (configList == null || configList.isEmpty()) {
			return;
		}

		deleteAll(configList.get(0).getMoId());

		for (AntennaCalibrationConfig item : configList) {
			item.convertListToBytes();
			saveOrUpdate(item);
		}
	}

	private void deleteAll(Long moId) {
		getJdbcTemplate().update(
				"delete from " + getTableName() + " where moId = ?",
				new Object[] { moId });
	}

	public void saveOrUpdate(AntennaCalibrationConfig config) {
		// 根据业务名称获取表名
		String tableName = getTableName();
		// FIXME:JIAYI 貌似所有idx都是重新生成的哇,这里永远执行的insert吧?
		if (isRecordExist(tableName, config.getIdx())) {
			// 记录存在，执行更新
			internalUpdate(tableName, config);
		} else {
			// 执行新增
			internalInsert(tableName, config);
		}
	}

	/**
	 * 根据条件判断记录是否存在
	 * 
	 * @param tableName
	 * @param primaryKeys
	 * @param record
	 * @return
	 */
	private boolean isRecordExist(String tableName, Long idx) {
		// 获取表名
		StringBuilder buf = new StringBuilder();
		buf.append("select * from ").append(tableName);
		buf.append(" where idx=? ");
		// 拼接条件参数值
		Object[] queryConditionValue = new Object[1];
		queryConditionValue[0] = idx;
		// 执行查询
		List<AntennaCalibrationConfig> configs = getJdbcTemplate().query(
				buf.toString(), queryConditionValue,
				new AntennaCalibrationConfigRowMapper());
		if (configs == null || configs.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 新增记录
	 * 
	 * @param tableName
	 * @param record
	 */
	private void internalInsert(String tableName,
			AntennaCalibrationConfig config) {
		StringBuilder buf = new StringBuilder();
		buf.append("insert into ").append(tableName);
		StringBuilder columnNames = new StringBuilder();
		// index
		columnNames.append("idx").append(", ");
		// moID
		columnNames.append("moId").append(", ");
		// antennaIndex
		columnNames.append("antennaIndex").append(", ");
		// dataType
		columnNames.append("dataType").append(", ");
		// dataType
		columnNames.append("detailInfo");
		buf.append("(");
		buf.append(columnNames);
		buf.append(") values(");
		buf.append("?, ?, ?, ?, ?");
		buf.append(")");
		// 持久化
		logger.info("insert SQL: " + buf.toString());
		getJdbcTemplate().update(buf.toString(), config.getIdx(),
				config.getMoId(), config.getAntennaIndex(),
				config.getDataType(), config.getDetailInfo());
	}

	/**
	 * 更新记录
	 * 
	 * @param tableName
	 * @param primaryKeys
	 * @param record
	 */
	private void internalUpdate(String tableName,
			AntennaCalibrationConfig config) {
		StringBuilder buf = new StringBuilder();
		buf.append("update ").append(tableName);
		buf.append(" set ");
		StringBuilder columnNames = new StringBuilder();
		// moID
		columnNames.append("moId").append("=?, ");
		// antennaIndex
		columnNames.append("antennaIndex").append("=?, ");
		// dataType
		columnNames.append("dataType").append("=?, ");
		// dataType
		columnNames.append("detailInfo").append("=? ");
		buf.append(columnNames);
		buf.append(" where idx=? ");
		// 持久化
		logger.info("update SQL: " + buf.toString());
		getJdbcTemplate().update(buf.toString(), config.getMoId(),
				config.getAntennaIndex(), config.getDataType(),
				config.getDetailInfo(), config.getIdx());
	}

	private String getTableName() {
		return "mcbts_calibantdataconfig";
	}

	private class AntennaCalibrationConfigRowMapper implements RowMapper {

		private List<AntennaCalibrationConfig> antConfigList;

		public AntennaCalibrationConfigRowMapper() {
			this.antConfigList = new ArrayList<AntennaCalibrationConfig>();
		}

		public AntennaCalibrationConfigRowMapper(
				List<AntennaCalibrationConfig> antConfigList) {
			this.antConfigList = antConfigList;
		}

		public Object mapRow(ResultSet rs, int index) throws SQLException {
			AntennaCalibrationConfig config = new AntennaCalibrationConfig();
			config.setIdx(rs.getLong("idx"));
			config.setMoId(rs.getLong("moId"));
			config.setAntennaIndex(rs.getInt("antennaIndex"));
			config.setDataType(rs.getInt("dataType"));
			config.setDetailInfo(rs.getBytes("detailInfo"));
			config.convertBytesToList();
			antConfigList.add(config);
			return config;
		}
	}
}
