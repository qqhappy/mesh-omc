/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-2-26	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.centersource;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.xinwei.common.dbcp.DataSourceManager;
import com.xinwei.common.dbcp.PropsDBCPConfigurator;
import com.xinwei.minas.server.centersource.dao.McBtsIpDataDao;
import com.xinwei.minas.server.centersource.dao.impl.McBtsIpDataDaoJdbcImpl;
import com.xinwei.minas.server.centersource.model.McBtsIpData;

/**
 * 
 * 基站IP中心数据源模块
 * 
 * @author fanhaoyu
 * 
 */

public class CenterSourceModule {

	public static final CenterSourceModule instance = new CenterSourceModule();

	private McBtsIpDataDao mcBtsIpDataDao;

	private CenterSourceModule() {
	}

	public static CenterSourceModule getInstance() {
		return instance;
	}

	/**
	 * 初始化
	 * 
	 * @param dataSourceConfigPath
	 * @param dataSourceName
	 * @throws Exception
	 */
	public void initialize(String dataSourceConfigPath, String dataSourceName)
			throws Exception {
		DataSource dataSource = DataSourceManager.getInstance().getDataSource(
				dataSourceName);
		if (dataSource != null)
			return;
		PropsDBCPConfigurator.configure(dataSourceConfigPath);
		dataSource = DataSourceManager.getInstance().getDataSource(
				dataSourceName);
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		mcBtsIpDataDao = new McBtsIpDataDaoJdbcImpl(jdbcTemplate);
	}

	public void saveOrUpdate(McBtsIpData data) throws Exception {
		mcBtsIpDataDao.saveOrUpdate(data);
	}

	public McBtsIpData query(long btsId) throws Exception {
		return mcBtsIpDataDao.query(btsId);
	}

	public void delete(long btsId) throws Exception {
		mcBtsIpDataDao.delete(btsId);
	}

	public List<McBtsIpData> queryAll() throws Exception {
		return mcBtsIpDataDao.queryAll();
	}

	public McBtsIpDataDao getMcBtsIpDataDao() {
		return mcBtsIpDataDao;
	}

}
