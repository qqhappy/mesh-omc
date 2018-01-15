/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.alarm.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.common.utils.FuncDateUtil;
import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.alarm.Alarm;
import com.xinwei.minas.core.model.alarm.AlarmConstants;
import com.xinwei.minas.core.model.alarm.AlarmDef;
import com.xinwei.minas.core.model.alarm.AlarmQueryCondition;
import com.xinwei.minas.server.core.alarm.dao.AlarmDAO;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.omp.core.utils.DateUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 告警DAO接口实现
 * 
 * @author chenjunhua
 * @author liuzhongyan
 * 
 */

public class AlarmDAOImpl extends JdbcDaoSupport implements AlarmDAO {

	private static final Log log = LogFactory.getLog(AlarmDAOImpl.class);

	// 语言选项
	private String language = "en";

	// 历史表的分表数量
	private int shardNumOfHistoryTable = 10;

	// 最大告警查询数量
	private int maxQueryAlarmNum = 10000;

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public Alarm queryCurrentBy(long moId, String entityType, String entityOid,
			long alarmDefId) throws Exception {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("select * from alarm_current where ");
		strBuffer.append(" moId = " + moId);
		strBuffer.append(" and  alarmDefId = '" + alarmDefId + "'");
		strBuffer.append(" and  entityType = '" + entityType + "'");
		strBuffer.append(" and  entityOid = '" + entityOid + "'");
		strBuffer.append(" limit 1");

		List<Alarm> alarmList = getJdbcTemplate().query(strBuffer.toString(),
				new CurrentMapper());
		if (alarmList.size() > 0) {
			return alarmList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public PagingData<Alarm> queryCurrentAlarm(
			AlarmQueryCondition queryCondition) throws Exception {
		PagingData<Alarm> pagingAlarm = new PagingData<Alarm>();

		// 查询当前告警数量
		StringBuffer countBuffer = new StringBuffer();
		countBuffer.append("select count(*) as num from alarm_current ");
		String whereClause = generateWhereClause(queryCondition);
		countBuffer.append(whereClause);
		int alarmCount = getJdbcTemplate().queryForInt(countBuffer.toString());

		// 查询当前告警明细
		List<Alarm> alarmList = new LinkedList();
		if (alarmCount > 0) {
			// 获取要排序的列
			String sortBy = viewColumnToTableColumn(queryCondition
					.getSortColumn());
			String sortDirec = queryCondition.getSortDirection();
			if (sortBy.equals("entityOid, entityType")) {
				sortBy = sortBy.replace(",", " " + sortDirec + ",");
			} else if (sortBy.equals("alarmLevel")) {
				// 告警级别的数字和级别是相反的.
				if (sortDirec.equals(AlarmQueryCondition.ASC)) {
					sortDirec = AlarmQueryCondition.DESC;
				} else {
					sortDirec = AlarmQueryCondition.ASC;
				}
			}

			// 只有告警数目大于0才需要查询明细
			StringBuffer strBuffer = new StringBuffer();
			int currentPage = queryCondition.getCurrentPage();
			int numPerPage = queryCondition.getNumPerPage();
			int beginNum = (currentPage - 1) * numPerPage;
			strBuffer.append("select * from alarm_current ");
			strBuffer.append(whereClause);
			strBuffer.append(" order by " + sortBy + sortDirec);
			strBuffer.append(sortBy.equals("firstAlarmTime") ? ""
					: ", firstAlarmTime desc");
			strBuffer.append(" limit ").append(beginNum).append(",")
					.append(numPerPage);
			alarmList = getJdbcTemplate().query(strBuffer.toString(),
					new CurrentMapper());
		}
		pagingAlarm.setResults(alarmList);

		int page = alarmCount / queryCondition.getNumPerPage();
		if (alarmCount % queryCondition.getNumPerPage() != 0) {
			page = page + 1;
		}
		pagingAlarm.setNumPerPage(queryCondition.getNumPerPage());
		pagingAlarm.setCurrentPage(queryCondition.getCurrentPage());
		pagingAlarm.setTotalNum(alarmCount);
		pagingAlarm.setTotalPages(page);
		return pagingAlarm;
	}
	
	@Override
	//按条件查询当前所有告警，不分页，用于导出告警信息
	public List<Alarm> queryAllCurrentAlarm(AlarmQueryCondition queryCondition)
			throws Exception {

		// 查询当前告警数量
		StringBuffer countBuffer = new StringBuffer();
		countBuffer.append("select count(*) as num from alarm_current ");
		String whereClause = generateWhereClause(queryCondition);
		countBuffer.append(whereClause);
		int alarmCount = getJdbcTemplate().queryForInt(countBuffer.toString());

		// 查询当前告警明细
		List<Alarm> alarmList = new LinkedList();
		if (alarmCount > 0) {
			// 获取要排序的列
			String sortBy = viewColumnToTableColumn(queryCondition
					.getSortColumn());
			String sortDirec = queryCondition.getSortDirection();
			if (sortBy.equals("entityOid, entityType")) {
				sortBy = sortBy.replace(",", " " + sortDirec + ",");
			} else if (sortBy.equals("alarmLevel")) {
				// 告警级别的数字和级别是相反的.
				if (sortDirec.equals(AlarmQueryCondition.ASC)) {
					sortDirec = AlarmQueryCondition.DESC;
				} else {
					sortDirec = AlarmQueryCondition.ASC;
				}
			}

			// 只有告警数目大于0才需要查询明细
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("select * from alarm_current ");
			strBuffer.append(whereClause);
			strBuffer.append(" order by " + sortBy + sortDirec);
			strBuffer.append(sortBy.equals("firstAlarmTime") ? ""
					: ", firstAlarmTime desc");
			alarmList = getJdbcTemplate().query(strBuffer.toString(),
					new CurrentMapper());
		}
		return alarmList;
	}
	

	/**
	 * 转换基站界面的告警列 为 数据库的列
	 * 
	 * @param str
	 * @return
	 */
	private String viewColumnToTableColumn(String str) {
		if (StringUtils.isBlank(str))
			return "firstAlarmTime";

		if (str.equalsIgnoreCase("serialNumber")) {
			str = "id";
		} else if (str.equalsIgnoreCase("alarmLevel")) {
			str = "alarmLevel";
		} else if (str.equalsIgnoreCase("alarmContent")) {
			str = "alarmContent";
		} else if (str.equalsIgnoreCase("NeName")) {
			str = "moName";
		} else if (str.equalsIgnoreCase("alignedInfo")) {
			str = "entityOid, entityType";
		} else if (str.equalsIgnoreCase("alarmState")) {
			str = "alarmState";
		} else if (str.equalsIgnoreCase("lastAlarmTime")) {
			str = "lastAlarmTime";
		} else if (str.equalsIgnoreCase("restoredTime")) {
			str = "restoredTime";
		} else if (str.equalsIgnoreCase("confirmTime")) {
			str = "confirmTime";
		}
		return str;
	}

	@Override
	public PagingData<Alarm> queryHistoryAlarm(
			AlarmQueryCondition queryCondition) throws Exception {
		List<Alarm> alarmList = new ArrayList<Alarm>();
		PagingData<Alarm> pagingAlarm = new PagingData<Alarm>();
		int alarmCount = 0;

		// 构造where条件语句
		String whereClause = generateWhereClause(queryCondition);
		// 获取待查的历史表列表
		List<String> historyTables = generateHistoryTables(queryCondition);
		// 获取历史表告警数目的列表
		List<Integer> alarmNumList = this.queryHistoryAlarmNum(historyTables,
				whereClause);
		// 累计告警总数
		for (Integer alarmNum : alarmNumList) {
			alarmCount += alarmNum;
		}
		if (alarmCount > maxQueryAlarmNum) {
			// String errorMsg = "符合条件的告警记录数为{0}条, 系统最大允许{1}条.\n请缩小查询范围后重新查询.";
			String errorMsg = OmpAppContext.getMessage(
					"alarm_alarm_num_over_threshold", new Object[] {
							alarmCount, maxQueryAlarmNum });
			throw new Exception(errorMsg);
		}
		// 生成查询明细历史表范围的Map
		Map<String, Integer[]> tableMap = this.generateFinalHistoryTables(
				queryCondition, historyTables, alarmNumList);

		// 获取要排序的列
		String sortBy = viewColumnToTableColumn(queryCondition.getSortColumn());
		String sortDirec = queryCondition.getSortDirection();
		if (sortBy.equals("entityOid, entityType")) {
			sortBy = sortBy.replace(",", " " + sortDirec + ",");
		} else if (sortBy.equals("alarmLevel")) {
			// 告警级别的数字和级别是相反的.
			if (sortDirec.equals(AlarmQueryCondition.ASC)) {
				sortDirec = AlarmQueryCondition.DESC;
			} else {
				sortDirec = AlarmQueryCondition.ASC;
			}
		}

		Iterator<String> itr = tableMap.keySet().iterator();
		while (itr.hasNext()) {
			String historyTable = itr.next();
			Integer[] range = tableMap.get(historyTable);
			StringBuffer sqlBuf = new StringBuffer();
			// 查询历史告警sql
			sqlBuf.append("select * from " + historyTable);
			sqlBuf.append(whereClause);
			sqlBuf.append(" order by " + sortBy + sortDirec);
			sqlBuf.append(sortBy.equals("firstAlarmTime") ? ""
					: ", firstAlarmTime desc");
			sqlBuf.append(" limit ").append(range[0]).append(",")
					.append(range[1]);
			log.debug("Query history alarm: " + sqlBuf.toString());
			List<Alarm> alarms = getJdbcTemplate().query(sqlBuf.toString(),
					new CurrentMapper());
			alarmList.addAll(alarms);
		}
		int sort = 1;
		if (sortDirec.toLowerCase().trim().equals("desc")) {
			sort = getIndex(sortBy);
		} else {
			sort = -getIndex(sortBy);
		}
		Collections.sort(alarmList, new AlarmComparator(sort));
		// 计算页数
		int page = alarmCount / queryCondition.getNumPerPage();
		if (alarmCount % queryCondition.getNumPerPage() != 0) {
			page = page + 1;
		}
		pagingAlarm.setResults(alarmList);
		pagingAlarm.setCurrentPage(queryCondition.getCurrentPage());
		pagingAlarm.setNumPerPage(queryCondition.getNumPerPage());
		pagingAlarm.setTotalNum(alarmCount);
		pagingAlarm.setTotalPages(page);
		return pagingAlarm;
	}
	
	@Override
	//不分页的按条件查询历史告警，用于告警导出
	public List<Alarm> queryAllHistoryAlarm(
			AlarmQueryCondition queryCondition) throws Exception {
		List<Alarm> alarmList = new ArrayList<Alarm>();
		int alarmCount = 0;

		// 构造where条件语句
		String whereClause = generateWhereClause(queryCondition);
		// 获取待查的历史表列表
		List<String> historyTables = generateHistoryTables(queryCondition);
		// 获取历史表告警数目的列表
		List<Integer> alarmNumList = this.queryHistoryAlarmNum(historyTables,
				whereClause);
		
		// 累计告警总数
		for (Integer alarmNum : alarmNumList) {
			alarmCount += alarmNum;
		}
		
		queryCondition.setCurrentPage(1);
		queryCondition.setNumPerPage(alarmCount);
		// 生成查询明细历史表范围的Map
		Map<String, Integer[]> tableMap = this.generateFinalHistoryTables(
				queryCondition, historyTables, alarmNumList);

		// 获取要排序的列
		String sortBy = viewColumnToTableColumn(queryCondition.getSortColumn());
		String sortDirec = " ASC";
		if (sortBy.equals("entityOid, entityType")) {
			sortBy = sortBy.replace(",", " " + sortDirec + ",");
		} else if (sortBy.equals("alarmLevel")) {
			// 告警级别的数字和级别是相反的.
			if (sortDirec.equals(AlarmQueryCondition.ASC)) {
				sortDirec = AlarmQueryCondition.DESC;
			} else {
				sortDirec = AlarmQueryCondition.ASC;
			}
		}

		Iterator<String> itr = tableMap.keySet().iterator();
		while (itr.hasNext()) {
			String historyTable = itr.next();
			StringBuffer sqlBuf = new StringBuffer();
			// 查询历史告警sql
			sqlBuf.append("select * from " + historyTable);
			sqlBuf.append(whereClause);
			sqlBuf.append(" order by " + sortBy + sortDirec);
			sqlBuf.append(sortBy.equals("firstAlarmTime") ? ""
					: ", firstAlarmTime desc");
			log.debug("Query history alarm: " + sqlBuf.toString());
			List<Alarm> alarms = getJdbcTemplate().query(sqlBuf.toString(),
					new CurrentMapper());
			alarmList.addAll(alarms);
		}
		int sort = 1;
		if (sortDirec.toLowerCase().trim().equals("desc")) {
			sort = getIndex(sortBy);
		} else {
			sort = -getIndex(sortBy);
		}
		Collections.sort(alarmList, new AlarmComparator(sort));
		return alarmList;
	}

	// 根据列名称获取index
	private int getIndex(String columnName) {
		if ("serialNumber".equals(columnName)) {
			return 1;
		} else if ("alarmLevel".equals(columnName)) {
			return 2;
		} else if ("alarmContent".equals(columnName)) {
			return 3;
		} else if ("moName".equals(columnName)) {
			return 4;
		} else if ("entityOid  asc, entityType".equals(columnName)) {
			return 5;
		} else if ("alarmState".equals(columnName)) {
			return 6;
		} else if ("firstAlarmTime".equals(columnName)) {
			return 7;
		} else if ("restoredTime".equals(columnName)) {
			return 8;
		} else if ("restoreUser".equals(columnName)) {
			return 9;
		} else if ("confirmTime".equals(columnName)) {
			return 10;
		} else if ("confirmUser".equals(columnName)) {
			return 11;
		} else
			return 1;
	}

	// 排序处理
	private class AlarmComparator implements Comparator<Alarm> {

		// 记录ID
		public static final int id = 1;

		// 告警级别
		public static final int alarmLevel = 2;

		// 告警内容
		public static final int alarmContent = 3;

		// MO名称
		public static final int moName = 4;

		// 定位信息
		public static final int location = 5;

		// 状态:特殊处理
		public static final int state = 6;

		// 发生时间
		public static final int firstAlarmTime = 7;

		// 恢复时间
		public static final int restoredTime = 8;

		// 恢复用户
		public static final int restoreUser = 9;

		// 确认时间(14位长度yyyyMMddhhmmss)
		public static final int confirmTime = 10;

		// 确认用户
		public static final int confirmUser = 11;

		private int sortBy;

		public AlarmComparator(int sortBy) {
			this.sortBy = sortBy;
		}

		@Override
		public int compare(Alarm alarm1, Alarm alarm2) {

			// 升序与降序的替换标志位:sortBy是列的编号1开始
			int order = sortBy > 0 ? 1 : -1;
			switch (Math.abs(sortBy)) {
			case id:

				return order * compareNumLong(alarm1.getId(), alarm2.getId());
			case alarmLevel:

				return order
						* compareNum(alarm1.getAlarmLevel(),
								alarm2.getAlarmLevel());
			case alarmContent:

				return order
						* alarm1.getAlarmContent()
								.toLowerCase()
								.compareTo(
										alarm2.getAlarmContent().toLowerCase());
			case moName:

				return order
						* alarm1.getMoName().toLowerCase()
								.compareTo(alarm2.getMoName().toLowerCase());
			case location:

				return order
						* alarm1.getLocation().toLowerCase()
								.compareTo(alarm2.getLocation().toLowerCase());
			case state:
				// 特殊处理
				String tempSate1 = alarm1.getConfirmState() + ""
						+ alarm1.getAlarmState();
				String tempSate2 = alarm2.getConfirmState() + ""
						+ alarm2.getAlarmState();
				return order * tempSate1.compareTo(tempSate2);
			case firstAlarmTime:
				return order
						* compareNumLong(alarm1.getFirstAlarmTime(),
								alarm2.getFirstAlarmTime());
			case restoredTime:
				return order
						* compareNumLong(alarm1.getRestoredTime(),
								alarm2.getRestoredTime());
			case restoreUser:
				return order
						* alarm1.getRestoreUser()
								.toLowerCase()
								.compareTo(
										alarm2.getRestoreUser().toLowerCase());
			case confirmTime:
				return order
						* compareNumLong(alarm1.getConfirmTime(),
								alarm2.getConfirmTime());
			case confirmUser:
				return order
						* alarm1.getConfirmUser()
								.toLowerCase()
								.compareTo(
										alarm2.getConfirmUser().toLowerCase());
			default:
				return 0;
			}
		}

	}

	/**
	 * 按数字排序
	 * 
	 * @param n1
	 * @param n2
	 * @return
	 */
	private int compareNum(int n1, int n2) {
		if (n1 > n2)
			return 1;
		else if (n1 < n2)
			return -1;
		else
			return 0;
	}

	/**
	 * 按数字排序
	 * 
	 * @param n1
	 * @param n2
	 * @return
	 */
	private int compareNumLong(long n1, long n2) {
		if (n1 > n2)
			return 1;
		else if (n1 < n2)
			return -1;
		else
			return 0;
	}

	@Override
	public void add(Alarm alarm) throws Exception {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("insert into alarm_current");
		strBuffer
				.append("(id,moId,moName,entityType,entityOid,alarmDefId,alarmContent,");
		strBuffer
				.append("alarmLevel,alarmState,firstAlarmTime,lastAlarmTime,alarmTimes,");
		strBuffer.append("restoredTime,alarmType,confirmState)");
		strBuffer.append("values(");
		SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);
		long id = sequenceService.getNext("ALARM");
		strBuffer.append(id).append(",");
		strBuffer.append(alarm.getMoId()).append(",'");
		strBuffer.append(alarm.getMoName()).append("','");
		strBuffer.append(alarm.getEntityType()).append("','");
		strBuffer.append(alarm.getEntityOid()).append("',");
		strBuffer.append(alarm.getAlarmDefId()).append(",'");
		strBuffer.append(alarm.getAlarmContent()).append("',");
		strBuffer.append(alarm.getAlarmLevel()).append(",");
		strBuffer.append(alarm.getAlarmState()).append(",");
		strBuffer.append(alarm.getFirstAlarmTime()).append(",");
		strBuffer.append(alarm.getLastAlarmTime()).append(",");
		strBuffer.append(alarm.getAlarmTimes()).append(",");
		strBuffer.append(alarm.getRestoredTime()).append(",");
		strBuffer.append(alarm.getAlarmType()).append(",");
		strBuffer.append(alarm.getConfirmState());
		// strBuffer.append(alarm.getRestoreUser()).append("',");
		// strBuffer.append(alarm.getRestoreFlag()).append(",");
		// strBuffer.append(alarm.getConfirmUser()).append("',");
		// strBuffer.append(alarm.getConfirmTime()).append(")");

		strBuffer.append(")");
		getJdbcTemplate().update(strBuffer.toString());
	}

	@Override
	public List<Alarm> queryActiveAlarm(long moId) {
		String queryStr = "SELECT  * FROM alarm_current where moId = ?  ORDER BY  firstAlarmTime DESC";
		return getJdbcTemplate().query(queryStr, new Object[] { moId },
				new CurrentMapper());
	}

	@Override
	public void update(Alarm alarm) throws Exception {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("update alarm_current set moId =" + alarm.getMoId())
				.append(",moName ='");
		strBuffer.append(alarm.getMoName()).append("',entityType ='");
		strBuffer.append(alarm.getEntityType()).append("', entityOid = '");
		strBuffer.append(alarm.getEntityOid()).append("',alarmDefId =");
		strBuffer.append(alarm.getAlarmDefId()).append(",alarmContent ='");
		strBuffer.append(alarm.getAlarmContent()).append("',alarmLevel =");
		strBuffer.append(alarm.getAlarmLevel()).append(",alarmState =");
		strBuffer.append(alarm.getAlarmState()).append(",firstAlarmTime =");
		strBuffer.append(alarm.getFirstAlarmTime()).append(",lastAlarmTime =");
		strBuffer.append(alarm.getLastAlarmTime()).append(",alarmTimes =");
		strBuffer.append(alarm.getAlarmTimes()).append(",restoredTime =");
		strBuffer.append(alarm.getRestoredTime()).append(",restoreUser ='");
		strBuffer.append(alarm.getRestoreUser()).append("',restoreFlag = ");
		strBuffer.append(alarm.getRestoreFlag()).append(",alarmType =");
		strBuffer.append(alarm.getAlarmType()).append(",confirmState = ");
		strBuffer.append(alarm.getConfirmState()).append(",confirmUser ='");
		strBuffer.append(alarm.getConfirmUser()).append("', confirmTime =");
		strBuffer.append(alarm.getConfirmTime()).append(
				" where id =" + alarm.getId());
		getJdbcTemplate().update(strBuffer.toString());
	}

	@Override
	public void move2History(Alarm alarm) throws Exception {
		Long alarmId = alarm.getId();
		// 如果历史表有这个id的值,删除之
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("delete from " + findHistoryTableByAlarm(alarm)
				+ " where id = " + alarmId);
		getJdbcTemplate().update(strBuffer.toString());

		// 插入历史表
		strBuffer.setLength(0);
		strBuffer.append("insert into " + findHistoryTableByAlarm(alarm)
				+ " select * from alarm_current where id = " + alarmId);
		getJdbcTemplate().update(strBuffer.toString());

		// 删除当前alarm
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("delete from alarm_current where id = " + alarmId);
		getJdbcTemplate().update(strBuf.toString());
	}

	@Override
	public void restoreManually(List<Long> alarmIds, String username)
			throws Exception {
		if (alarmIds == null || alarmIds.isEmpty()) {
			return;
		}
		StringBuffer buf = new StringBuffer();
		buf.append("update alarm_current set alarmState = 0 ");
		buf.append(" , restoredTime = ").append(getNowTime());
		buf.append(" , restoreFlag = ").append(AlarmConstants.MANURESTORE);
		buf.append(" , restoreUser = '").append(username).append("'");
		buf.append(" where alarmState = 1 and (1 = 2 ");
		for (Long alarmId : alarmIds) {
			buf.append(" or id = ").append(alarmId);
		}
		buf.append(")");
		getJdbcTemplate().update(buf.toString());
		// 告警后处理
		postProcessAlarm(alarmIds);
	}

	@Override
	public void confirmManually(List<Long> alarmIds, String username)
			throws Exception {
		if (alarmIds == null || alarmIds.isEmpty()) {
			return;
		}
		StringBuffer buf = new StringBuffer();
		buf.append("update alarm_current set confirmState = 1 ");
		buf.append(" , confirmTime = ").append(getNowTime());
		buf.append(" , confirmUser = '").append(username).append("'");
		buf.append(" where confirmState = 0 and (1 = 2 ");
		for (Long alarmId : alarmIds) {
			buf.append(" or id = ").append(alarmId);
		}
		buf.append(")");
		getJdbcTemplate().update(buf.toString());
		// 告警后处理
		postProcessAlarm(alarmIds);
	}

	@Override
	public Map<Long, Integer> queryMaxLevelMapping(List<Long> moIds) {
		Map<Long, Integer> result = new HashMap();
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("select moId, MIN(alarmLevel) as alarmLevel");
		strBuffer.append(" FROM alarm_current ");
		// 拼Where条件
		StringBuffer whereClause = new StringBuffer();
		whereClause.append(" WHERE alarmState=").append(AlarmConstants.ALARM);
		whereClause.append(" and (1=2 ");
		for (Long moId : moIds) {
			whereClause.append(" or moId=").append(moId);
		}
		whereClause.append(")");
		strBuffer.append(whereClause);
		// 拼Group by
		StringBuffer groupByClause = new StringBuffer();
		groupByClause.append(" GROUP BY moId ");
		strBuffer.append(groupByClause);
		// 查询
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(
				strBuffer.toString());
		for (Map map : list) {
			Long moId = (Long) map.get("moId");
			Long alarmLevel = (Long) map.get("alarmLevel");
			if (moId != null && alarmLevel != null) {
				result.put(moId, alarmLevel.intValue());
			}
		}
		return result;
	}

	@Override
	public List<Alarm> queryCurrentAlarm(long moId) throws Exception {
		String sql = "select * from alarm_current where moId=" + moId;

		return getJdbcTemplate().query(sql, new CurrentMapper());
	}

	@Override
	public void deleteHistoryAlarm(int alarmReservedDay) {
		Set<String> historyTables = generateAllHistoryTables();
		// Date date = org.apache.commons.lang.time.DateUtils.addDays(new
		// Date(),
		// alarmReservedDay * -1);
		// long deadtime = DateUtils.getBriefTimeFromMillisecondTime(date
		// .getTime());
		String currentDateTime = FuncDateUtil.getCurrentDate() + " 00:00:00";
		String lastDateTime = FuncDateUtil.addDateTime(currentDateTime,
				String.valueOf(alarmReservedDay * -1), "d");
		long deadtime = DateUtils.getBriefTimeFromStandardTime(lastDateTime);

		for (String historyTable : historyTables) {
			StringBuffer buf = new StringBuffer();
			buf.append("delete from ").append(historyTable);
			buf.append(" where lastAlarmTime < ").append(deadtime);
			log.debug("deleteHistoryAlarm: " + buf);
			getJdbcTemplate().update(buf.toString());
		}
	}

	@Override
	public void deleteAlarmByMoId(long moId) {
		Set<String> historyTables = generateAllHistoryTables();

		StringBuffer buf = new StringBuffer();
		buf.append("delete from alarm_current");
		buf.append(" where moId = ").append(moId);
		log.debug("deleteCurrentAlarm: " + buf);
		getJdbcTemplate().update(buf.toString());

		for (String historyTable : historyTables) {
			buf = new StringBuffer();
			buf.append("delete from ").append(historyTable);
			buf.append(" where moId = ").append(moId);
			log.debug("deleteHistoryAlarm: " + buf);
			getJdbcTemplate().update(buf.toString());
		}

	}

	/**
	 * 根据查询条件构造待查询的历史告警表列表
	 * 
	 * @param queryCondition
	 *            查询条件
	 * @return
	 */
	private List<String> generateHistoryTables(
			AlarmQueryCondition queryCondition) {
		Set<String> historyTables = new LinkedHashSet();
		List<Long> moIdList = queryCondition.getMoIds();
		if (moIdList != null) {
			// 根据moId获取相应的历史表
			for (Long moId : moIdList) {
				historyTables.add(findHistoryTableByMoId(moId));
			}
		}
		if (historyTables.isEmpty()) {
			// 如果历史表为空，则填入所有历史表
			historyTables = generateAllHistoryTables();
		}
		List<String> tables = new LinkedList();
		tables.addAll(historyTables);
		Collections.sort(tables, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}

		});
		return tables;
	}

	/**
	 * 生成最终的告警历史表列表
	 * 
	 * @param queryCondition
	 * @param historyTables
	 * @param alarmNumList
	 * 
	 * @return
	 */
	private Map<String, Integer[]> generateFinalHistoryTables(
			AlarmQueryCondition queryCondition, List<String> historyTables,
			List<Integer> alarmNumList) {
		// KEY-表名; Value-范围数组
		Map<String, Integer[]> tableMap = new LinkedHashMap();
		// 获取告警查询的起始数目和结束数目
		int currentPage = queryCondition.getCurrentPage();
		int numPerPage = queryCondition.getNumPerPage();
		// 闭开区间
		int beginNum = (currentPage - 1) * numPerPage;
		int endNum = currentPage * numPerPage;
		// 当前告警起始数目和结束数目
		int currentBeginNum = 0;
		int currentEndNum = 0;
		for (int index = 0; index < alarmNumList.size(); index++) {
			Integer alarmNum = alarmNumList.get(index);
			String tableName = historyTables.get(index);
			if (alarmNum == 0) {
				continue;
			}
			Integer[] range = new Integer[2];
			// 累计当前结束
			currentEndNum += alarmNum;
			// 计算查询落在的范围
			if (endNum < currentBeginNum || beginNum > currentEndNum) {
				// do nothing
			} else {
				if (beginNum <= currentBeginNum) {
					range[0] = currentBeginNum;
				} else {
					range[0] = beginNum;
				}
				if (endNum >= currentEndNum) {
					range[1] = currentEndNum;
				} else {
					range[1] = endNum;
				}
				// 将范围减去一个偏移量
				range[1] -= range[0];
				range[0] -= currentBeginNum;

				// 加入哈希表
				if (range[1] > 0) {
					tableMap.put(tableName, range);
				}
			}
			// 累计当前起始
			currentBeginNum += alarmNum;
		}
		return tableMap;
	}

	/**
	 * 生成所有历史表集合
	 * 
	 * @return
	 */
	private Set<String> generateAllHistoryTables() {
		Set<String> historyTables = new LinkedHashSet();
		for (int i = 0; i < shardNumOfHistoryTable; i++) {
			historyTables.add(findHistoryTableByPostfix(i));
		}
		return historyTables;
	}

	/**
	 * 按条件查询历史告警表的数量
	 * 
	 * @param historyTables
	 * @param whereClause
	 * @return
	 */
	private List<Integer> queryHistoryAlarmNum(List<String> historyTables,
			String whereClause) {
		List<Integer> alarmNumList = new LinkedList();
		for (String historyTable : historyTables) {
			// 查询历史告警sql数量
			StringBuffer countBuffer = new StringBuffer();
			countBuffer.append("select count(*) from " + historyTable);
			countBuffer.append(whereClause);
			int count = getJdbcTemplate().queryForInt(countBuffer.toString());
			alarmNumList.add(count);
		}
		return alarmNumList;
	}

	/**
	 * 根据告警模型获取历史告警表名称
	 * 
	 * @param alarm
	 * @return
	 */
	private String findHistoryTableByAlarm(Alarm alarm) {
		// 根据moId进行分表
		Long moId = alarm.getMoId();
		return findHistoryTableByMoId(moId);
	}

	/**
	 * 根据moId获取历史告警表名称
	 * 
	 * @param alarmId
	 * @return
	 */
	private String findHistoryTableByMoId(Long moId) {
		int postfix = (int) (moId % shardNumOfHistoryTable);
		return findHistoryTableByPostfix(postfix);
	}

	/**
	 * 根据后缀获取历史告警表名称
	 * 
	 * @param alarmId
	 * @return
	 */
	private String findHistoryTableByPostfix(int postfix) {
		return "alarm_history_" + postfix;
	}

	/**
	 * 生成where条件语句
	 * 
	 * @param queryCondition
	 *            查询条件
	 * @param mapFlag
	 * @return
	 */
	private String generateWhereClause(AlarmQueryCondition queryCondition) {
		// 当前表和历史表需要构造2个索引
		// INDEX1: lastAlarmTime, alarmLevel, alarmState, confirmState,
		// alarmContent
		// INDEX2: moId, lastAlarmTime, alarmLevel, alarmState, confirmState,
		// alarmContent
		StringBuffer buf = new StringBuffer();
		buf.append(" where 1=1 ");

		// 拼网元ID
		List<Long> moIdList = queryCondition.getMoIds();
		if (moIdList != null && !moIdList.isEmpty()) {
			buf.append(" and (1=2 ");
			for (Long moId : moIdList) {
				buf.append(" or moId=").append(moId);
			}
			buf.append(")");
		}

		// 拼接告警时间
		if (queryCondition.getBeginTime() > 0
				&& queryCondition.getEndTime() > 0) {
			buf.append("  and ( firstAlarmTime >= "
					+ queryCondition.getBeginTime() + " and firstAlarmTime <= "
					+ queryCondition.getEndTime() + " ) ");
		}

		// 拼接告警级别
		List<Integer> alarmLevels = queryCondition.getAlarmLevels();
		if (alarmLevels == null || alarmLevels.isEmpty()) {
			alarmLevels = queryCondition.getAllAlarmLevels();
		}
		buf.append(" and (1=2 ");
		for (Integer alarmLevel : alarmLevels) {
			buf.append(" or alarmLevel=").append(alarmLevel);
		}
		buf.append(")");

		// 拼接确认恢复状态
		List<Integer> confirmRestoreFlags = queryCondition
				.getConfirmRestoreFlags();
		if (confirmRestoreFlags == null || confirmRestoreFlags.isEmpty()) {
			buf.append(" and (alarmState = 0 or alarmState = 1) ");
			buf.append(" and (confirmState = 0 or confirmState = 1) ");
		} else {
			buf.append(" and (1=2 ");
			for (int flag : confirmRestoreFlags) {
				switch (flag) {
				case 1:
					// 未确认未恢复
					buf.append(" or ( alarmState = 1 and confirmState = 0 ) ");
					break;
				case 2:
					// 已确认未恢复
					buf.append(" or ( alarmState = 1 and confirmState = 1 ) ");
					break;
				case 3:
					// 未确认已恢复
					buf.append(" or ( alarmState = 0 and confirmState = 0 ) ");
					break;
				case 4:
					// 已确认已恢复
					buf.append(" or ( alarmState = 0 and confirmState = 1 ) ");
					break;
				}
			}
			buf.append(" ) ");
		}

		// 拼接告警内容
		if (queryCondition.getContent() != null
				&& !queryCondition.getContent().equals("")) {
			buf.append(" and alarmContent like '%"
					+ queryCondition.getContent() + "%'  ");
		}

		return buf.toString();
	}

	/**
	 * 告警后处理
	 * 
	 * @param alarmId
	 *            告警流水
	 * @throws Exception
	 */
	private void postProcessAlarm(List<Long> alarmIds) throws Exception {
		if (alarmIds == null || alarmIds.isEmpty()) {
			return;
		}
		StringBuilder buf = new StringBuilder();
		buf.append("select * from alarm_current where 1=2 ");
		for (Long alarmId : alarmIds) {
			buf.append(" or id = ").append(alarmId);
		}
		List<Alarm> alarmList = getJdbcTemplate().query(buf.toString(),
				new CurrentMapper());
		if (alarmList != null && alarmList.size() > 0) {
			for (Alarm alarm : alarmList) {
				if (alarm.isConfirmed() && alarm.isRestored()) {
					move2History(alarm);
				}
			}
		}
	}

	private class CurrentMapper implements RowMapper<Alarm> {
		@Override
		public Alarm mapRow(ResultSet rs, int index) throws SQLException {

			Alarm alarm = new Alarm();

			alarm.setId(rs.getLong("id"));
			alarm.setMoId(rs.getLong("moId"));
			alarm.setMoName(rs.getString("moName"));
			alarm.setEntityType(rs.getString("entityType"));
			alarm.setEntityOid(rs.getString("entityOid"));
			alarm.setAlarmDefId(rs.getLong("alarmDefId"));
			alarm.setAlarmContent(rs.getString("alarmContent"));
			alarm.setAlarmLevel(rs.getInt("alarmLevel"));
			alarm.setAlarmState(rs.getInt("alarmState"));
			alarm.setFirstAlarmTime(rs.getLong("firstAlarmTime"));
			alarm.setLastAlarmTime(rs.getLong("lastAlarmTime"));
			alarm.setAlarmTimes(rs.getLong("alarmTimes"));
			alarm.setRestoredTime(rs.getLong("restoredTime"));
			// 恢复用户
			String restoreUser = rs.getString("restoreUser");
			restoreUser = (restoreUser == null) ? "" : restoreUser;
			alarm.setRestoreUser(restoreUser);
			//
			alarm.setRestoreFlag(rs.getInt("restoreFlag"));
			alarm.setAlarmType(rs.getInt("alarmType"));
			alarm.setConfirmState(rs.getInt("confirmState"));
			// 确认用户
			String confirmUser = rs.getString("confirmUser");
			confirmUser = (confirmUser == null) ? "" : confirmUser;
			alarm.setConfirmUser(confirmUser);
			//
			alarm.setConfirmTime(rs.getLong("confirmTime"));

			return alarm;
		}

	}

	private static long getNowTime() {
		SimpleDateFormat smDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = smDate.format(new Date());
		long dateTime = DateUtils.getBriefTimeFromStandardTime(nowTime);
		return dateTime;

	}

	@Override
	public List<AlarmDef> queryAllAlarmDef() {
		String sql = "SELECT  * FROM alarm_def ";
		List<AlarmDef> alarmDefs = getJdbcTemplate().query(sql,
				new AlarmDefMapper());
		if (alarmDefs == null) {
			return new LinkedList();
		}
		return alarmDefs;
	}

	private class AlarmDefMapper implements RowMapper<AlarmDef> {
		@Override
		public AlarmDef mapRow(ResultSet rs, int index) throws SQLException {
			AlarmDef alarmDef = new AlarmDef();
			alarmDef.setAlarmDefId(rs.getLong("alarm_def_id"));
			alarmDef.setMoType(rs.getInt("mo_type"));
			alarmDef.setAlarmLevel(rs.getInt("alarm_level"));
			alarmDef.setAlarmName(rs.getString("alarm_name_" + language));
			return alarmDef;
		}

	}
}
