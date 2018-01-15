/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-6	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsTemplate;
import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsTemplateManageDao;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 基站模板管理的Dao
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsTemplateManageDaoImpl extends JdbcDaoSupport implements
		McBtsTemplateManageDao {

	/**
	 * 获取所有基站同步的模板
	 * 
	 * @return 基站模板列表
	 */
	@Override
	public List<McBtsTemplate> queryAll() {
		// 备份ID和真实ID的临界点
		String queryStr = "select * from mcbts_template where moId > -100000 order by addedTime";
		return getJdbcTemplate().query(queryStr, new TemplateRowMapper());
	}

	/**
	 * 基于一个模板ID,返回一个可用的备用模板ID
	 * 
	 * @param referId
	 * @return
	 */
	@Override
	public Long applyNewId(Long referId, McBtsTemplate temp) {
		if (referId > -1000)
			referId = referId * 100;

		String str = String.valueOf(referId).substring(0, 2);
		Long referId2 = (Long.valueOf(str) - 1) * 10000;

		String queryStr = "select min(moId) from mcbts_template where moId between ? and ?";
		long id = getJdbcTemplate().queryForLong(queryStr,
				new Object[] { referId2 + 1, referId });
		// 如果找到当前用到的id是-20003, 那么进行-1操作,得到-20004,再*10,得到-200040作为备份ID.
		if (id == 0L)
			id = referId;
		else if (id == referId2) {
			id = referId;
		} else
			id = id - 1;

		temp.setMoId(id);
		temp.setBackupId(id * 10);

		insert(temp);

		return id;
	}

	/**
	 * 向mcbts_template插入一个模板
	 * 
	 * @param temp
	 */
	@Override
	public void insert(McBtsTemplate temp) {
		String insertStr = "insert into mcbts_template values (?,?,?,?,?,?,?,?)";

		getJdbcTemplate().update(
				insertStr,
				new Object[] { temp.getMoId(), temp.getName(),
						temp.getBackupId(), temp.getReferredTemplateId(),
						temp.getReferredTemplateName(), "", new Date(),
						new Date() });
	}

	/**
	 * 修改mcbts_template中的记录
	 * 
	 * @param temp
	 */
	@Override
	public void update(McBtsTemplate temp) {
		String updateStr = "update mcbts_template set name = ?,operationName = ?, lastModifiedTime = ? where moId = ?";

		String operations;
		if (temp.getOperations() == null || temp.getOperations().size() == 0) {
			operations = "";
		} else {
			List<String> opts = temp.getOperations();
			Collections.sort(opts);
			StringBuilder sb = new StringBuilder();
			for (String s : opts) {
				sb.append(",").append(s);
			}
			operations = sb.toString().substring(1);
		}

		getJdbcTemplate().update(
				updateStr,
				new Object[] { temp.getName(), operations, new Date(),
						temp.getMoId() });
	}

	/**
	 * 从mcbts_template删除一个模板
	 * 
	 * @param moId
	 */
	@Override
	public void delete(long moId) {
		String deleteStr = "delete from mcbts_template where moId = " + moId;

		getJdbcTemplate().update(deleteStr);
	}

	@Override
	public McBtsTemplate queryByMoId(Long moId) {
		String queryStr = "select * from mcbts_template where moId = ?";
		List<McBtsTemplate> list = getJdbcTemplate().query(queryStr,
				new TemplateRowMapper(), new Object[] { moId });
		if (list == null || list.size() == 0)
			return null;

		return list.get(0);
	}

	/**
	 * McBtsTemplate的RowMapper类
	 * 
	 * @author tiance
	 * 
	 */
	private class TemplateRowMapper implements RowMapper<McBtsTemplate> {
		@Override
		public McBtsTemplate mapRow(ResultSet rs, int arg1) throws SQLException {
			McBtsTemplate mt = new McBtsTemplate();
			if (rs != null) {
				mt.setMoId(rs.getLong("moId"));
				mt.setName(rs.getString("name"));
				mt.setBackupId(rs.getLong("backupId"));
				mt.setReferredTemplateId(rs.getLong("referredTemplateId"));
				mt.setReferredTemplateName(rs.getString("referredTemplateName"));
				mt.setAddedTime(rs.getTimestamp("addedTime"));
				mt.setLastModifiedTime(rs.getTimestamp("lastModifiedTime"));

				String operationName = rs.getString("operationName");
				if (operationName != null && operationName.length() > 0) {
					String[] names = operationName.split(",");
					mt.setOperations(Arrays.asList(names));
				}
			}
			return mt;
		}
	}

	/**
	 * 根据表名和moId(templateId),获取表对象
	 * 
	 * @param templateId
	 * @param table
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryFromTable(long templateId,
			String table) {
		String sql = "select * from " + table + " where moId = " + templateId;
		// RCPE Item表特殊处理
		if (table.equals("t_conf_rcpe_item")) {
			sql = "select * from " + table + " where parent_id = " + templateId;
		}

		SqlRowSet srs = getJdbcTemplate().queryForRowSet(sql);
		SqlRowSetMetaData metadata = srs.getMetaData();
		final String[] columns = metadata.getColumnNames();

		return getJdbcTemplate().query(sql,
				new RowMapper<Map<String, Object>>() {
					@Override
					public Map<String, Object> mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, Object> value = new HashMap<String, Object>();
						for (String column : columns) {
							value.put(column, rs.getObject(column));
						}
						return value;
					}
				});
	}

	/**
	 * 根据表名,moId插入指定的数据集
	 * 
	 * @param moId
	 * @param table
	 * @param toInsert
	 */
	@Override
	public void insertIntoTable(long moId, String table,
			List<Map<String, Object>> toInsert) throws Exception {
		if (moId == 0L || table == null || table.length() == 0
				|| toInsert == null || toInsert.size() == 0)
			return;

		SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);

		List<String> columns = new ArrayList<String>(toInsert.get(0).keySet());
		String sql = combinSql(table, columns);
		List<Object> obj;

		try {
			// 插入值操作
			for (Map<String, Object> value : toInsert) {
				obj = new ArrayList<Object>();
				for (String column : columns) {
					if (column.equalsIgnoreCase("idx")
							|| column.equalsIgnoreCase("id")) {
						obj.add(sequenceService.getNext());
					} else if (column.equalsIgnoreCase("moId")
							|| column.equalsIgnoreCase("parent_id")) {
						obj.add(moId);
					} else {
						obj.add(value.get(column));
					}

				}
				getJdbcTemplate().update(sql,
						obj.toArray(new Object[columns.size()]));
			}
		} catch (Exception e) {
			throw new Exception(e.getLocalizedMessage());
		}

	}

	/**
	 * 批量插入模板数据给一批基站
	 * 
	 * @param mcbts
	 * @param table
	 * @param list
	 * @throws Exception
	 */
	public void batchInsertIntoTable(McBts[] mcbts, String table,
			List<Map<String, Object>> toInsert) throws Exception {
		if (table == null || table.length() == 0)
			return;
		if (toInsert == null || toInsert.size() == 0)
			return;

		List<String> columns = new ArrayList<String>(toInsert.get(0).keySet());

		// 删除表中待同步基站的所有数据
		try {
			for (McBts bts : mcbts) {
				long moId = bts.getMoId();
				deleteFromTable(moId, table);
			}
		} catch (Exception e) {
			throw new SQLException("批量删除表数据时出错", e);
		}

		batchUpdate(table, toInsert, mcbts, columns);
	}

	/**
	 * 批量执行插入表操作
	 * 
	 * @param table
	 * @param toInsert
	 * @param mcbts
	 * @param columns
	 * @throws Exception
	 */
	private void batchUpdate(String table, List<Map<String, Object>> toInsert,
			final McBts[] mcbts, final List<String> columns) throws Exception {

		String sql = combinSql(table, columns);

		final SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);

		try {
			// 遍历每条数据
			for (final Map<String, Object> value : toInsert) {
				// 将每条数据都插入给这批基站
				getJdbcTemplate().batchUpdate(sql,
						new BatchPreparedStatementSetter() {
							@Override
							public int getBatchSize() {
								// 获取基站的数量
								return mcbts.length;
							}

							@Override
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								McBts bts = mcbts[i];
								for (int j = 0; j < columns.size(); j++) {
									String column = columns.get(j);
									if (column.equalsIgnoreCase("idx")
											|| column.equalsIgnoreCase("id")) {
										ps.setLong(j + 1,
												sequenceService.getNext());
									} else if (column.equalsIgnoreCase("moId")
											|| column
													.equalsIgnoreCase("parent_id")) {
										ps.setLong(j + 1, bts.getMoId());
									} else {
										ps.setObject(j + 1, value.get(column));
									}
								}
							}
						});
			}
		} catch (Exception e) {
			throw new SQLException("对基站执行批量插入操作时出错", e);
		}
	}

	private String combinSql(String table, List<String> columns) {
		StringBuilder sb = new StringBuilder();
		StringBuilder vsb = new StringBuilder();

		// 拼接sql语句
		String sql = "insert into " + table + "(";

		for (String column : columns) {
			sb.append(",").append(column);
			vsb.append(",").append("?");
		}

		sql = sql + sb.toString().substring(1) + ") values (";
		sql = sql + vsb.toString().substring(1) + ")";

		return sql;
	}

	/**
	 * 根据表名,删除指定moId的数据
	 * 
	 * @param moId
	 */
	@Override
	public void deleteFromTable(long moId, String table) throws Exception {
		String sql = "delete from " + table + " where moId = " + moId;
		// RCPE Item表特殊处理
		if (table.equals("t_conf_rcpe_item")) {
			sql = "delete from " + table + " where parent_id = " + moId;
		}

		getJdbcTemplate().update(sql);

	}

}
