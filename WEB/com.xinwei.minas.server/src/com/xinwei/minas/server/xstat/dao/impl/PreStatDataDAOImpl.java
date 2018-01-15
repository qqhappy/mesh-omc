/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.server.xstat.dao.DataBaseChecker;
import com.xinwei.minas.server.xstat.dao.PreStatDataDAO;
import com.xinwei.minas.server.xstat.dao.StatDataTableStrategy;
import com.xinwei.minas.server.xstat.dao.TableCache;
import com.xinwei.minas.server.xstat.model.TableStrategyModel;
import com.xinwei.minas.xstat.core.model.PreStatItem;
import com.xinwei.minas.xstat.core.model.StatConstants;

/**
 * 
 * 预统计数据持久化接口
 * 
 * @author fanhaoyu
 * 
 */

public class PreStatDataDAOImpl extends JdbcDaoSupport implements
		PreStatDataDAO {

	private Log log = LogFactory.getLog(PreStatDataDAOImpl.class);

	private static final String CREATE_TABLE_COMMAND = "create table {0}.{1} (`moid` bigint(20) not null,`entitytype` varchar(128) not null,`entityoid` varchar(128) not null,`itemtype` varchar(64) not null,`itemid` int(11) not null,`stattime` bigint(20) not null,`statvalue` decimal(50,4) default null,primary key (`moid`,`entitytype`,`entityoid`,`itemtype`,`itemid`,`stattime`),KEY `INDEX` (`itemid`)) engine=MYISAM default charset=gb2312";

	private StatDataTableStrategy strategy;

	private DataBaseChecker dataBaseChecker;

	@Override
	public void savePreStatItem(PreStatItem item, int preStatType)
			throws Exception {
		if (item == null)
			return;
		List<PreStatItem> itemList = new ArrayList<PreStatItem>();
		itemList.add(item);
		this.savePreStatItem(itemList, preStatType);

	}

	@Override
	public void savePreStatItem(List<PreStatItem> itemList, int preStatType)
			throws Exception {
		if (itemList == null || itemList.isEmpty())
			return;
		PreStatItem item = itemList.get(0);
		TableStrategyModel model = getTarget(item.getMoId(),
				item.getStatTime(), preStatType);

		// 检测数据库和表是否存在,若不存在需创建
		createTableIfNotExists(model.getTargetDataBase(),
				model.getTargetTable());
		// 创建sql语句 持久化到数据库中
		String sql = createInsertSql(itemList, model.getTargetDataBase(),
				model.getTargetTable());
		getJdbcTemplate().execute(sql);
	}

	@Override
	public List<PreStatItem> queryHourPreStatData(
			Map<Long, List<String>> entityMap, List<String> itemList,
			long startTime, long endTime) throws Exception {

		return queryDayAndHourPreStatData(entityMap, itemList, startTime,
				endTime, StatConstants.PRE_STAT_TYPE_HOUR);
	}

	@Override
	public List<PreStatItem> queryDayPreStatData(
			Map<Long, List<String>> entityMap, List<String> itemList,
			long startTime, long endTime) throws Exception {

		return queryDayAndHourPreStatData(entityMap, itemList, startTime,
				endTime, StatConstants.PRE_STAT_TYPE_DAY);
	}

	/**
	 * 根据条件查询天或者小时的预统计数据
	 * 
	 * @param entityMap
	 *            格式:key-moId,value=List[entityType#entityOid]
	 * @param itemList
	 *            统计项列表,格式:itemType#itemId
	 * @param startTime
	 * @param endTime
	 * @param preType
	 *            预统计类型 1:按照小时统计;2:按照天统计
	 * @return
	 * @throws Exception
	 */
	private List<PreStatItem> queryDayAndHourPreStatData(
			Map<Long, List<String>> entityMap, List<String> itemList,
			long startTime, long endTime, int preType) throws Exception {
		long startQueryTime = System.currentTimeMillis();
		// entityMap 格式:key-moId,value=List<entityType#entityOid>
		// itemList 统计项列表，格式:itemType#itemId
		List<PreStatItem> preStatItems = new ArrayList<PreStatItem>();
		for (Long moId : entityMap.keySet()) {
			List<TableStrategyModel> modelList = getTargetList(moId, startTime,
					endTime, preType);
			for (TableStrategyModel model : modelList) {
				// 判断目标库和表是否存在，如果不存在则跳过
				boolean tableExists = dataBaseChecker.isTableExist(
						model.getTargetDataBase(), model.getTargetTable());
				if (!tableExists)
					continue;
				// 遍历要查询的实体列表
				List<String> entityList = entityMap.get(moId);
				for (String entity : entityList) {
					String[] entityArray = entity
							.split(StatConstants.DEFAULT_SPLIT);
					for (String item : itemList) {
						String[] itemArray = item
								.split(StatConstants.DEFAULT_SPLIT);
						StringBuilder sqlBuilder = new StringBuilder();
						sqlBuilder.append(" select * from ")
								.append(model.getTargetDataBase()).append(".")
								.append(model.getTargetTable())
								.append(" where").append(" itemId=")
								.append(itemArray[1])
								.append(" and entityOid='")
								.append(entityArray[1])
								.append("' and entityType='")
								.append(entityArray[0])
								.append("' and itemType='")
								.append(itemArray[0])
								.append("' and statTime>=").append(startTime)
								.append(" and statTime<").append(endTime)
								.append(";");
						log.info("stat sql: " + sqlBuilder.toString());
						List<PreStatItem> list = getJdbcTemplate().query(
								sqlBuilder.toString(), new PreStatItemMapper());
						if (list == null || list.isEmpty())
							continue;
						preStatItems.addAll(list);
					}
				}
			}
		}
		long finishQueryTime = System.currentTimeMillis();
		long costTime = finishQueryTime - startQueryTime;
		log.info("query PreStatData cost time " + costTime + " ms");

		return preStatItems;
	}

	private List<TableStrategyModel> getTargetList(long moId, long startTime,
			long endTime, int preType) {
		if (preType == StatConstants.PRE_STAT_TYPE_DAY) {
			return strategy.getPreOneDayTarget(moId, startTime, endTime);
		} else {
			return strategy.getPreOneHourTarget(moId, startTime, endTime);
		}
	}

	private TableStrategyModel getTarget(long moId, long time, int preType) {
		if (preType == StatConstants.PRE_STAT_TYPE_DAY) {
			return strategy.getPreOneDayTarget(moId, time);
		} else {
			return strategy.getPreOneHourTarget(moId, time);
		}
	}

	/**
	 * 判断目标库和表是否存在，如果不存在则先创建
	 * 
	 * @param dataBase
	 */
	private void createDataBaseIfNotExist(String dataBase) {
		// 判断数据库是否存在,如果不存在则新建数据库
		if (!dataBaseChecker.isDataBaseExist(dataBase)) {
			getJdbcTemplate().execute("create database " + dataBase);
			// 创建成功后添加到缓存中
			TableCache.getInstance().addDataBase(dataBase);
		}

	}

	/**
	 * 如果数据表不存在，判断表所在的数据库是否存在，如果不存在，先创建数据库再创建表
	 * 
	 * @param dataBase
	 * @param tableName
	 */
	private void createTableIfNotExists(String dataBase, String tableName) {
		// 判断数据库是否存在,如果不存在则新建数据库
		createDataBaseIfNotExist(dataBase);
		// 数据库存在,判断指定的表是否存在,如果不存在则新建表
		if (!dataBaseChecker.isTableExist(dataBase, tableName)) {
			getJdbcTemplate().execute(
					MessageFormat.format(CREATE_TABLE_COMMAND, dataBase,
							tableName));
			// 创建成功后添加到缓存中
			TableCache.getInstance().addTable(dataBase, tableName);
		}

	}

	/**
	 * 对查询结果进行包装
	 * 
	 * @author xieyang
	 * 
	 */
	private class PreStatItemMapper implements RowMapper<PreStatItem> {

		@Override
		public PreStatItem mapRow(ResultSet resultSet, int index)
				throws SQLException {
			PreStatItem statItem = new PreStatItem();
			statItem.setMoId(resultSet.getLong("moId"));
			statItem.setEntityType(resultSet.getString("entityType"));
			statItem.setEntityOid(resultSet.getString("entityOid"));
			statItem.setItemType(resultSet.getString("itemType"));
			statItem.setItemId(resultSet.getInt("itemId"));
			statItem.setStatTime(resultSet.getLong("statTime"));
			statItem.setStatValue(resultSet.getDouble("statValue"));

			return statItem;
		}
	}

	private String createInsertSql(List<PreStatItem> itemList, String dbName,
			String tableName) {
		if (itemList == null || itemList.isEmpty()) {
			return "";
		}
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ").append(dbName).append(".").append(tableName)
				.append(" values");
		for (PreStatItem item : itemList) {
			sql.append(createInsertValueString(item)).append(",");
		}
		if (sql.length() > 0) {
			sql.deleteCharAt(sql.length() - 1);
			sql.append(";");
		}
		return sql.toString();
	}

	private String createInsertValueString(PreStatItem item) {
		StringBuilder sql = new StringBuilder();
		sql.append("(").append(item.getMoId()).append(",'")
				.append(item.getEntityType()).append("','")
				.append(item.getEntityOid()).append("','")
				.append(item.getItemType()).append("',")
				.append(item.getItemId()).append(",")
				.append(item.getStatTime()).append(",")
				.append(item.getStatValue()).append(")");
		return sql.toString();
	}

	public void setStrategy(StatDataTableStrategy strategy) {
		this.strategy = strategy;
	}

	public void setDataBaseChecker(DataBaseChecker dataBaseChecker) {
		this.dataBaseChecker = dataBaseChecker;
	}
}
