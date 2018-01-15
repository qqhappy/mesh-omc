/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.server.mcbts.dao.sysManage.TerminalVersionForceUpdateDao;

/**
 * 
 * 终端版本强制升级Dao
 * 
 * @author tiance
 * 
 */

public class TerminalVersionForceUpdateDaoImpl extends JdbcDaoSupport implements
		TerminalVersionForceUpdateDao {

	/**
	 * 获取所有终端版本强制升级规则
	 */
	@Override
	public List<TerminalVersion> queryList() {
		String querySql = "select tv.typeId, tv.version, tv.fileName, tv.fileType, tv.update_dependency, "
				+ "tv.update_condition, tvt.type_name_zh type_name from terminal_version tv, "
				+ "terminal_version_type tvt where tv.typeId = tvt.id and "
				+ "tv.update_dependency is not null and tv.update_condition is not null";
		return getJdbcTemplate().query(querySql, new TVForceUpdateMapper());
	}

	/**
	 * 从数据库查询一条强制升级的规则
	 * 
	 * @param btsId
	 * @param typeId
	 * @return TVForceUpdate
	 */
	@Override
	public TerminalVersion query(Long btsId, Integer typeId) {
		String querySql = "select tv.typeId, tv.version, tv.update_dependency, "
				+ "tv.update_condition, tvt.type_name_zh type_name from terminal_version tv, "
				+ "terminal_version_type tvt where tv.typeId = tvt.id and "
				+ "tv.update_dependency is not null and tv.update_condition is not null "
				+ "and tv.typeId = ?";
		List<TerminalVersion> list = getJdbcTemplate().query(querySql,
				new Object[] { typeId }, new TVForceUpdateMapper());

		if (list == null)
			return null;

		return list.get(0);
	}

	@Override
	public void update(final List<TerminalVersion> ruleList) {
		// 清除所有标注的升级规则
		String cleanSql = "update terminal_version set update_dependency = null, update_condition = null";
		getJdbcTemplate().update(cleanSql);

		if (ruleList == null || ruleList.size() == 0)
			return;
		// 标注新的升级规则
		String updateSql = "update terminal_version set update_dependency = ?, update_condition = ? "
				+ "where typeId = ? and version = ? and fileType = ?";
		getJdbcTemplate().batchUpdate(updateSql,
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						TerminalVersion tv = ruleList.get(i);
						ps.setInt(1, tv.getUpdateDependency());
						ps.setInt(2, tv.getUpdateCondition());
						ps.setInt(3, tv.getTypeId());
						ps.setString(4, tv.getVersion());
						ps.setString(5, tv.getFileType());
					}

					@Override
					public int getBatchSize() {
						return ruleList.size();
					}
				});
	}

	private class TVForceUpdateMapper implements RowMapper<TerminalVersion> {
		@Override
		public TerminalVersion mapRow(ResultSet rs, int arg1)
				throws SQLException {
			TerminalVersion t = new TerminalVersion();
			t.setTypeId(rs.getInt("typeId"));
			t.setVersion(rs.getString("version"));
			t.setFileName(rs.getString("fileName"));
			t.setFileType(rs.getString("fileType"));
			t.setUpdateDependency(rs.getInt("update_dependency"));
			t.setUpdateCondition(rs.getInt("update_condition"));
			t.setType(rs.getString("type_name"));
			return t;
		}
	}

}
