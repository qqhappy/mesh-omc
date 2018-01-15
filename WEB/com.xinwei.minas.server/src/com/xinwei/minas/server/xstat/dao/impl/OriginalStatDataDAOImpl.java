/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-26	| fanhaoyu 	| 	create the file                       
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
import com.xinwei.minas.server.xstat.dao.OriginalStatDataDAO;
import com.xinwei.minas.server.xstat.dao.StatDataTableStrategy;
import com.xinwei.minas.server.xstat.dao.TableCache;
import com.xinwei.minas.server.xstat.model.TableStrategyModel;
import com.xinwei.minas.xstat.core.model.StatConstants;
import com.xinwei.minas.xstat.core.model.StatItem;

/**
 * 
 * ͳ������ʵ��־û��ӿ�ʵ��
 * 
 * @author fanhaoyu
 * 
 */

public class OriginalStatDataDAOImpl extends JdbcDaoSupport implements
		OriginalStatDataDAO {

	private Log log = LogFactory.getLog(OriginalStatDataDAOImpl.class);

	private static final String CREATE_TABLE_COMMAND = "create table {0}.{1} (`moid` bigint(20) not null,`entitytype` varchar(128) not null,`entityoid` varchar(128) not null,`itemtype` varchar(64) not null,`itemid` int(11) not null,`starttime` bigint(20) not null,`endtime` bigint(20) not null,`statvalue` decimal(50,4) not null,`version` int(11) default null,primary key (moid,entitytype,entityoid,itemtype,itemid,starttime),KEY `INDEX` (`itemid`)) engine=MYISAM default charset=gb2312;";

	private StatDataTableStrategy strategy;

	private DataBaseChecker dataBaseChecker;

	@Override
	public void save(List<StatItem> itemList) throws Exception {
		if (itemList == null || itemList.isEmpty())
			return;
		StatItem item = itemList.get(0);
		// ��ȡĿ�����ݿ����ͱ���
		TableStrategyModel model = strategy.getOriginalTarget(item.getMoId(),
				item.getStartTime());
		// �ж�Ŀ���ͱ��Ƿ���ڣ�������������ȴ�����ԭʼ���ݵı����Ƕ��߳��첽ִ�еģ�������Ҫ����ͬ��
		synchronized (this) {
			createTableIfNotExists(model.getTargetDataBase(),
					model.getTargetTable());
		}

		// ����sql���
		String sql = createInsertSql(itemList, model.getTargetDataBase(),
				model.getTargetTable());
		if (sql.length() > 0) {
			// ִ��sql���
			getJdbcTemplate().execute(sql.toString());
		}
	}

	/**
	 * �Բ�ѯ������а�װ
	 * 
	 * @author xieyang
	 * 
	 */
	private class StatItemMapper implements RowMapper<StatItem> {

		@Override
		public StatItem mapRow(ResultSet resultSet, int index)
				throws SQLException {
			StatItem statItem = new StatItem();
			statItem.setMoId(resultSet.getLong("moId"));
			statItem.setEntityType(resultSet.getString("entityType"));
			statItem.setEntityOid(resultSet.getString("entityOid"));
			statItem.setItemType(resultSet.getString("itemType"));
			statItem.setItemId(resultSet.getInt("itemId"));
			statItem.setStartTime(resultSet.getLong("startTime"));
			statItem.setEndTime(resultSet.getLong("endTime"));
			statItem.setStatValue(resultSet.getDouble("statValue"));
			statItem.setVersion(resultSet.getInt("version"));

			return statItem;
		}
	}

	private String createInsertSql(List<StatItem> itemList, String dbName,
			String tableName) {
		if (itemList == null || itemList.isEmpty()) {
			return "";
		}
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ").append(dbName).append(".").append(tableName)
				.append(" values");
		for (StatItem item : itemList) {
			sql.append(createInsertValueString(item)).append(",");
		}
		if (sql.length() > 0) {
			sql.deleteCharAt(sql.length() - 1);
			sql.append(";");
		}
		return sql.toString();
	}

	private String createInsertValueString(StatItem item) {
		StringBuilder sql = new StringBuilder();
		sql.append("(").append(item.getMoId()).append(",'")
				.append(item.getEntityType()).append("','")
				.append(item.getEntityOid()).append("','")
				.append(item.getItemType()).append("',")
				.append(item.getItemId()).append(",")
				.append(item.getStartTime()).append(",")
				.append(item.getEndTime()).append(",")
				.append(item.getStatValue()).append(",")
				.append(item.getVersion()).append(")");
		return sql.toString();
	}

	@Override
	public List<StatItem> queryBy(long moId, long startTime, long endTime)
			throws Exception {
		// ��ȡĿ�����ݿ����ͱ���
		TableStrategyModel model = strategy.getOriginalTarget(moId, startTime);
		if (!dataBaseChecker.isTableExist(model.getTargetDataBase(),
				model.getTargetTable())) {
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(model.getTargetDataBase())
				.append(".").append(model.getTargetTable())
				.append(" where moId=").append(moId).append(" and startTime>=")
//				.append(startTime).append(" and endTime<").append(endTime)
				.append(startTime).append(" and startTime<").append(endTime)
				.append(" and itemtype = 'COUNTER'")  //����itemtype�Ĺ�������
				.append(" order by startTime")
				.append(";");
		return getJdbcTemplate().query(sql.toString(), new StatItemMapper());
	}

	@Override
	public List<StatItem> queryBy(Map<Long, List<String>> entityMap,
			List<String> itemList, long startTime, long endTime)
			throws Exception {
		long startQueryTime = System.currentTimeMillis();
		// entityMap ��ʽ:key-moId,value=List<entityType#entityOid>
		// itemList ͳ�����б���ʽ:itemType#itemId
		List<StatItem> statItems = new ArrayList<StatItem>();
		for (Long moId : entityMap.keySet()) {
			// ��ȡĿ�����ݿ����ͱ���
			List<TableStrategyModel> modelList = strategy.getOriginalTarget(
					moId, startTime, endTime);
			for (TableStrategyModel model : modelList) {
				// �ж�Ŀ���ͱ��Ƿ���ڣ����������������
				boolean tableExists = dataBaseChecker.isTableExist(
						model.getTargetDataBase(), model.getTargetTable());
				if (!tableExists)
					continue;
				// ����Ҫ��ѯ��ʵ���б�
				List<String> entityList = entityMap.get(moId);
				for (String entity : entityList) {
					String[] entityArray = entity
							.split(StatConstants.DEFAULT_SPLIT);
					// ����Ҫ��ѯ��ͳ�����б�
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
								.append("' and startTime>=").append(startTime)
								.append(" and startTime<").append(endTime)
								.append(";");
						log.info("stat sql: " + sqlBuilder.toString());
						List<StatItem> items = getJdbcTemplate().query(
								sqlBuilder.toString(), new StatItemMapper());
						if (items == null || items.isEmpty())
							continue;
						statItems.addAll(items);
					}
				}

			}
		}
		long finishQueryTime = System.currentTimeMillis();
		long costTime = finishQueryTime - startQueryTime;
		log.info("query OriginalStatData cost time " + costTime + " ms");

		return statItems;

	}

	@Override
	public void delete(long startTime, long endTime) throws Exception {
		List<String> dataBases = strategy.getTargetDataBaseList(startTime,
				endTime);
		// ɾ�����ݿ�
		if (dataBases == null || dataBases.isEmpty()) {
			return;
		}
		for (String dataBase : dataBases) {
			// ������ݿ���ڣ���ɾ�����ݿ�
			if (dataBaseChecker.isDataBaseExist(dataBase)) {
				log.info("drop database " + dataBase);
				getJdbcTemplate().execute("drop database " + dataBase);
				TableCache.getInstance().remove(dataBase);
			}
		}

	}

	/**
	 * �ж�Ŀ���ͱ��Ƿ���ڣ�������������ȴ���
	 * 
	 * @param dataBase
	 */
	private void createDataBaseIfNotExist(String dataBase) {
		// �ж����ݿ��Ƿ����,������������½����ݿ�
		if (!dataBaseChecker.isDataBaseExist(dataBase)) {
			getJdbcTemplate().execute("create database " + dataBase);
			TableCache.getInstance().addDataBase(dataBase);
		}

	}

	/**
	 * ������ݱ����ڣ��жϱ����ڵ����ݿ��Ƿ���ڣ���������ڣ��ȴ������ݿ��ٴ�����
	 * 
	 * @param dataBase
	 * @param tableName
	 */
	private void createTableIfNotExists(String dataBase, String tableName) {
		// �ж����ݿ��Ƿ����,������������½����ݿ�
		createDataBaseIfNotExist(dataBase);
		// ���ݿ����,�ж�ָ���ı��Ƿ����,������������½���
		if (!dataBaseChecker.isTableExist(dataBase, tableName)) {
			getJdbcTemplate().execute(
					MessageFormat.format(CREATE_TABLE_COMMAND, dataBase,
							tableName));
			TableCache.getInstance().addTable(dataBase, tableName);
		}

	}

	public void setStrategy(StatDataTableStrategy strategy) {
		this.strategy = strategy;
	}

	public void setDataBaseChecker(DataBaseChecker dataBaseChecker) {
		this.dataBaseChecker = dataBaseChecker;
	}

}
