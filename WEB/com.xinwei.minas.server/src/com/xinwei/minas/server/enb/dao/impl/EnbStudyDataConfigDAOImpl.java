/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-9-5	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.server.enb.dao.EnbStudyDataConfigDAO;

/**
 * 
 * eNB自学习数据配置DAO实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStudyDataConfigDAOImpl extends JdbcDaoSupport implements
		EnbStudyDataConfigDAO {

	public static final String TABLE_NAME = "enb_study_dataconfig";

	@Override
	public Map<String, String> queryAll() throws Exception {
		String sql = "select * from " + TABLE_NAME;
		List<DataConfig> configList = getJdbcTemplate().query(sql,
				new ConfigRowMapper());
		if (configList == null || configList.isEmpty())
			return null;
		Map<String, String> configMap = new HashMap<String, String>();
		for (DataConfig dataConfig : configList) {
			configMap.put(dataConfig.getVersion(), dataConfig.getConfig());
		}
		return configMap;
	}

	@Override
	public String queryByVersion(String version) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(TABLE_NAME)
				.append(" where version='").append(version).append("'");
		return getJdbcTemplate().queryForObject(sql.toString(), String.class);
	}

	@Override
	public void saveDataConfig(String version, String dataConfig)
			throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ").append(TABLE_NAME)
				.append("(version, dataconfig) ").append("values('")
				.append(version).append("','").append(dataConfig).append("')");
		getJdbcTemplate().execute(sql.toString());
	}

	@Override
	public void delete(String version) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ").append(TABLE_NAME)
				.append(" where version='").append(version).append("'");
		getJdbcTemplate().execute(sql.toString());
	}

	class DataConfig {
		private String version;
		private String config;

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getConfig() {
			return config;
		}

		public void setConfig(String config) {
			this.config = config;
		}
	}

	class ConfigRowMapper implements RowMapper<DataConfig> {

		@Override
		public DataConfig mapRow(ResultSet rs, int index) throws SQLException {
			DataConfig config = new DataConfig();
			config.setVersion(rs.getString("version"));
			config.setConfig(rs.getString("dataconfig"));
			return config;
		}

	}

}
