/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-2-26	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.centersource.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.server.centersource.dao.McBtsIpDataDao;
import com.xinwei.minas.server.centersource.model.McBtsIpData;

/**
 * 
 * 基站IP数据持久化接口JDBC实现
 * 
 * @author fanhaoyu
 * 
 */

public class McBtsIpDataDaoJdbcImpl extends JdbcDaoSupport implements
		McBtsIpDataDao {

	private static final String TABLE_NAME = "mcbts_ip_data";
	public McBtsIpDataDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
		setJdbcTemplate(jdbcTemplate);
	}

	@Override
	public void saveOrUpdate(McBtsIpData data) throws Exception {
		StringBuilder deleteSql = new StringBuilder();
		deleteSql.append("delete from ").append(TABLE_NAME)
				.append(" where btsId=").append(data.getBtsId());
		StringBuilder insertSql = new StringBuilder();
		insertSql
				.append("insert into ")
				.append(TABLE_NAME)
				.append("(btsId,publicIp,publicPort,privateIp,privatePort,reserve)")
				.append(" values(").append(data.getBtsId()).append(",'")
				.append(data.getPublicIp()).append("',")
				.append(data.getPublicPort()).append(",'")
				.append(data.getPrivateIp()).append("',")
				.append(data.getPrivatePort()).append(",'")
				.append(data.getReserve()).append("')");
		getJdbcTemplate().batchUpdate(
				new String[] { deleteSql.toString(), insertSql.toString() });
	}

	@Override
	public McBtsIpData query(long btsId) throws Exception {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from ").append(TABLE_NAME)
				.append(" where btsId=").append(btsId);
		List<McBtsIpData> dataList = getJdbcTemplate().query(
				sqlBuilder.toString(), new McBtsIpDataRowMapper());
		if (dataList == null || dataList.isEmpty())
			return null;
		return dataList.get(0);
	}

	@Override
	public List<McBtsIpData> queryAll() throws Exception {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from ").append(TABLE_NAME);
		return getJdbcTemplate().query(sqlBuilder.toString(),
				new McBtsIpDataRowMapper());
	}

	@Override
	public void delete(long btsId) throws Exception {
		StringBuilder deleteSql = new StringBuilder();
		deleteSql.append("delete from ").append(TABLE_NAME)
				.append(" where btsId=").append(btsId);
		getJdbcTemplate().execute(deleteSql.toString());
	}

	// /**
	// * 数据在库中是否存在
	// *
	// * @param data
	// * @return
	// */
	// private boolean exists(McBtsIpData data) {
	// StringBuilder sqlBuilder = new StringBuilder();
	// sqlBuilder.append("select count(*) from ").append(TABLE_NAME)
	// .append("where btsId=").append(data.getBtsId());
	// int count = getJdbcTemplate().queryForInt(sqlBuilder.toString());
	// if (count == 0)
	// return false;
	// return true;
	// }

	class McBtsIpDataRowMapper implements RowMapper<McBtsIpData> {

		@Override
		public McBtsIpData mapRow(ResultSet rs, int index) throws SQLException {
			McBtsIpData data = new McBtsIpData();
			data.setBtsId(rs.getLong("btsId"));
			data.setPublicIp(rs.getString("publicIp"));
			data.setPublicPort(rs.getInt("publicPort"));
			data.setPrivateIp(rs.getString("privateIp"));
			data.setPrivatePort(rs.getInt("privatePort"));
			data.setReserve(rs.getString("reserve"));
			return data;
		}

	}

}
