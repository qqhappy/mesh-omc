/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-10	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.connection;

import java.util.TimerTask;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * 
 * 连接保持任务
 * 
 * @author fanhaoyu
 * 
 */

public class KeepTask extends TimerTask {

	private Log log = LogFactory.getLog(KeepTask.class);

	private String targetTable;

	private QueryAction queryAction;

	public KeepTask(DataSource dataSource, String targetTable) {
		this.targetTable = targetTable;
		queryAction = new QueryAction();
		queryAction.setDataSource(dataSource);
	}

	@Override
	public void run() {
		try {
			log.info("connect with mysql.");
			queryAction.query();
		} catch (Throwable e) {
			log.warn("Connection with mysql is break.");
		}

	}

	class QueryAction extends JdbcDaoSupport {

		public void query() {
			String sql = "select count(*) from " + targetTable;
			getJdbcTemplate().queryForInt(sql);
		}

	}

	/**
	 * 设置要操作的表
	 * 
	 * @param targetTable
	 */
	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}

}
