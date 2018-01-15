/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-2-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.server.stat.dao.StatDetailDAO;
import com.xinwei.minas.stat.core.model.StatDetail;

/**
 * 
 * 统计详情DAO接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class StatDetailDaoImpl extends JdbcDaoSupport implements StatDetailDAO {

	private static final String TABLE_NAME = "mcbts_stat_detail";

	@Override
	public void saveOrUpdate(StatDetail statDetail) throws Exception {
		StringBuilder sqlBuilder = new StringBuilder();
		if (exists(statDetail)) {
			sqlBuilder.append("update ").append(TABLE_NAME)
					.append(" set flag = ").append(statDetail.getFlag())
					.append(createWhereCondition(statDetail));
		} else {
			sqlBuilder.append("insert into ").append(TABLE_NAME)
					.append("(timeType ,statTime ,intval ,flag) values(")
					.append(statDetail.getTimeType()).append(",")
					.append(statDetail.getTime()).append(",")
					.append(statDetail.getInterval()).append(",")
					.append(statDetail.getFlag()).append(")");
		}
		getJdbcTemplate().execute(sqlBuilder.toString());
	}

	@Override
	public StatDetail getMaxSuccessStatDetail(int timeType) throws Exception {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from ").append(TABLE_NAME)
				.append(" where timeType = ").append(timeType)
				.append(" and flag = ").append(StatDetail.FLAG_SUCCESS)
				.append(" order by statTime desc limit 0,1");
		List<StatDetail> statDetails = getJdbcTemplate().query(
				sqlBuilder.toString(), new StatDetailRowMapper());
		if (statDetails != null && !statDetails.isEmpty()) {
			return statDetails.get(0);
		}
		return null;
	}

	@Override
	public long getLatestStatTime(int timeType) throws Exception {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select max(statTime) from ").append(TABLE_NAME)
				.append(" where timeType = ").append(timeType);
		return getJdbcTemplate().queryForLong(sqlBuilder.toString());
	}

	@Override
	public List<StatDetail> getFailedStatDetail(int timeType) throws Exception {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from ").append(TABLE_NAME)
				.append(" where timeType = ").append(timeType)
				.append(" and flag = ").append(StatDetail.FLAG_FAIL);
		return getJdbcTemplate().query(sqlBuilder.toString(),
				new StatDetailRowMapper());
	}

	@Override
	public List<StatDetail> getStatDetailBeforeSpecTime(int timeType,
			long latestStatTime, int flag) throws Exception {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from ").append(TABLE_NAME)
				.append(" where timeType = ").append(timeType)
				.append(" and statTime < ").append(latestStatTime)
				.append(" and flag = ").append(flag);
		return getJdbcTemplate().query(sqlBuilder.toString(),
				new StatDetailRowMapper());
	}

	@Override
	public List<StatDetail> getFailedStatDetailBeforeSpecTime(int timeType,
			long latestStatTime) throws Exception {
		return getStatDetailBeforeSpecTime(timeType, latestStatTime,
				StatDetail.FLAG_FAIL);
	}

	@Override
	public void deleteStatDetail(int timeType, long endtime, int flag)
			throws Exception {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("delete from ").append(TABLE_NAME)
				.append(" where timeType = ").append(timeType)
				.append(" and statTime < ").append(endtime)
				.append(" and flag = ").append(flag);
		getJdbcTemplate().execute(sqlBuilder.toString());
	}

	/**
	 * 判断指定对象是否存在数据库表中
	 * 
	 * @param statDetail
	 * @return
	 */
	private boolean exists(StatDetail statDetail) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select count(*) from ").append(TABLE_NAME)
				.append(createWhereCondition(statDetail));
		int resultNum = getJdbcTemplate().queryForInt(sqlBuilder.toString());
		if (resultNum == 0)
			return false;
		return true;
	}

	/**
	 * 创建where条件
	 * 
	 * @param statDetail
	 * @return
	 */
	private String createWhereCondition(StatDetail statDetail) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" where timeType = ")
				.append(statDetail.getTimeType()).append(" and statTime = ")
				.append(statDetail.getTime()).append(" and intval = ")
				.append(statDetail.getInterval());
		return sqlBuilder.toString();
	}

	class StatDetailRowMapper implements RowMapper<StatDetail> {

		@Override
		public StatDetail mapRow(ResultSet rs, int index) throws SQLException {
			StatDetail statDetail = new StatDetail();
			statDetail.setTimeType(rs.getInt("timeType"));
			statDetail.setTime(rs.getLong("statTime"));
			statDetail.setInterval(rs.getLong("intval"));
			statDetail.setFlag(rs.getInt("flag"));
			return statDetail;
		}

	}

}
