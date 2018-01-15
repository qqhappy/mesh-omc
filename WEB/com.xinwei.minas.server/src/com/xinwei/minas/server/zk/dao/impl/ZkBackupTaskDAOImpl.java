/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.server.zk.dao.ZkBackupTaskDAO;
import com.xinwei.minas.zk.core.basic.ZkBackupTask;

/**
 * 
 * NK集群备份任务DAO接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class ZkBackupTaskDAOImpl extends JdbcDaoSupport implements
		ZkBackupTaskDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<ZkBackupTask> queryBackupTasks() throws Exception {
		String sql = "select * from zk_backup_task";
		List<ZkBackupTask> taskList = getJdbcTemplate().query(sql,
				new ZkBackupTaskMapper());
		return taskList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ZkBackupTask queryBackupTask(int id) throws Exception {
		String sql = "select * from zk_backup_task where id = " + id;
		List<ZkBackupTask> taskList = getJdbcTemplate().query(sql,
				new ZkBackupTaskMapper());
		if (taskList == null || taskList.isEmpty()) {
			return null;
		}
		return taskList.get(0);
	}

	@Override
	public void createBackupTask(ZkBackupTask task) throws Exception {
		StringBuffer str = new StringBuffer();
		str.append("insert into zk_backup_task values (" + task.getId() + ",'"
				+ task.getInterval() + "',");
		str.append("'" + task.getLastTime() + "',");
		str.append(task.getState() + ")");
		getJdbcTemplate().update(str.toString());
	}

	@Override
	public void modifyBackupTask(ZkBackupTask task) throws Exception {
		StringBuffer str = new StringBuffer();
		str.append("update zk_backup_task set intval = '" + task.getInterval());
		str.append("', lastTime = '" + task.getLastTime() + "'");
		str.append(", state = " + task.getState() + " where id = "
				+ task.getId());
		getJdbcTemplate().update(str.toString());
	}

	@Override
	public void deleteBackupTask(int id) throws Exception {
		String sql = "delete from zk_backup_task where id = " + id;
		getJdbcTemplate().update(sql);
	}

	@SuppressWarnings("rawtypes")
	class ZkBackupTaskMapper implements RowMapper {

		@Override
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			ZkBackupTask task = new ZkBackupTask();
			task.setId(rs.getInt("id"));
			task.setInterval(rs.getString("intval"));
			task.setLastTime(rs.getString("lastTime"));
			task.setState(rs.getInt("state"));
			return task;
		}
	}
}