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
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.server.stat.dao.StatDataDAO;
import com.xinwei.minas.server.stat.dao.TableStrategy;
import com.xinwei.minas.stat.core.StatUtil;
import com.xinwei.minas.stat.core.model.SingleStatItemData;
import com.xinwei.minas.stat.core.model.StatData;

/**
 * 
 * 统计数据DAO接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class StatDataDaoImpl extends JdbcDaoSupport implements StatDataDAO {

	private TableStrategy tableStrategy;

	@Override
	public void saveData(List<StatData> dataList) throws Exception {
		if (dataList != null && dataList.size() > 0) {
			String[] sqlList = new String[dataList.size()];
			for (int i = 0; i < dataList.size(); i++) {
				StatData statData = dataList.get(i);
				String tableName = tableStrategy.getTableName(
						statData.getTimeType(), statData.getBtsId());
				sqlList[i] = createInsertSql(statData, tableName);
			}
			// 批处理
			getJdbcTemplate().batchUpdate(sqlList);
		}
	}

	@Override
	public void saveData(StatData statData) throws Exception {
		String tableName = tableStrategy.getTableName(statData.getTimeType(),
				statData.getBtsId());
		String sql = createInsertSql(statData, tableName);
		getJdbcTemplate().execute(sql);
	}

	@Override
	public List<StatData> getData(long begintime, long endtime, int timeType)
			throws Exception {
		List<String> tableList = tableStrategy.getTables(timeType);
		List<StatData> dataList = new ArrayList<StatData>();
		for (String tableName : tableList) {
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("select * from ").append(tableName)
					.append(" where collectTime >=").append(begintime)
					.append(" and collectTime < ").append(endtime);
			List<StatData> datas = getJdbcTemplate().query(
					sqlBuilder.toString(), new StatDataRowMapper());
			if (datas != null && !datas.isEmpty()) {
				dataList.addAll(datas);
			}
		}
		return dataList;
	}

	@Override
	public List<StatData> getData(long btsId, long begintime, long endtime,
			int timeType) throws Exception {
		String tableName = tableStrategy.getTableName(timeType, btsId);
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from ").append(tableName)
				.append(" where btsId = ").append(btsId)
				.append(" and collectTime >=").append(begintime)
				.append(" and collectTime < ").append(endtime);
		List<StatData> dataList = getJdbcTemplate().query(
				sqlBuilder.toString(), new StatDataRowMapper());
		return dataList;
	}

	@Override
	public int deleteData(long beginTime, long endTime, int timeType)
			throws Exception {
		List<String> tableList = tableStrategy.getTables(timeType);
		int sum = 0;
		for (String tableName : tableList) {
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("delete from ").append(tableName)
					.append(" where collectTime >=").append(beginTime)
					.append(" and collectTime < ").append(endTime);
			sum += getJdbcTemplate().update(sqlBuilder.toString());
		}
		return sum;
	}

	@Override
	public int deleteData(long endTime, int timeType) throws Exception {
		List<String> tableList = tableStrategy.getTables(timeType);
		int sum = 0;
		for (String tableName : tableList) {
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("delete from ").append(tableName)
					.append(" where collectTime <").append(endTime);
			sum += getJdbcTemplate().update(sqlBuilder.toString());
		}
		return sum;
	}

	/**
	 * 创建添加数据的SQL语句
	 * 
	 * @param statData
	 * @param tableName
	 * @return
	 */
	private String createInsertSql(StatData statData, String tableName) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("insert into ").append(tableName).append("(");

		StringBuilder itemBuilder = new StringBuilder();

		itemBuilder.append("btsId").append(",").append("collectTime")
				.append(",").append("intval").append(",").append("timeType");
		for (Integer itemId : StatUtil.COLLECT_ITEMS) {
			itemBuilder.append(",").append("item" + itemId);
		}
		sqlBuilder.append(itemBuilder);

		sqlBuilder.append(") values(");

		StringBuilder valueBuilder = new StringBuilder();
		valueBuilder.append(statData.getBtsId()).append(",")
				.append(statData.getCollectTime()).append(",")
				.append(statData.getInterval()).append(",")
				.append(statData.getTimeType());
		for (Integer itemId : StatUtil.COLLECT_ITEMS) {
			SingleStatItemData itemData = statData.getItemData(itemId);
			if (itemData != null) {
				valueBuilder.append(",").append(itemData.getValue());
			} else {
				valueBuilder.append(",").append("null");
			}
		}
		sqlBuilder.append(valueBuilder);

		sqlBuilder.append(")");

		return sqlBuilder.toString();
	}

	public void setTableStrategy(TableStrategy tableStrategy) {
		this.tableStrategy = tableStrategy;
	}

	class StatDataRowMapper implements RowMapper<StatData> {

		@Override
		public StatData mapRow(ResultSet rs, int index) throws SQLException {
			StatData statData = new StatData();
			statData.setBtsId(rs.getLong("btsId"));
			statData.setCollectTime(rs.getLong("collectTime"));
			statData.setInterval(rs.getLong("intval"));
			statData.setTimeType(rs.getInt("timeType"));
			for (Integer itemId : StatUtil.COLLECT_ITEMS) {
				Object object = rs.getObject("item" + itemId);
				if (object != null) {
					statData.addItemData(itemId, rs.getDouble("item" + itemId));
				}
			}
			return statData;
		}

	}
}
