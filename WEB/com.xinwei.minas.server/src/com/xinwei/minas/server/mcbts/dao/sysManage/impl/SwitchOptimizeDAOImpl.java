/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.mcbts.core.model.sysManage.SwitchOptimizeConfig;
import com.xinwei.minas.server.mcbts.dao.sysManage.SwitchOptimizeDAO;

/**
 * 
 * 切换优化开关配置DAO实现
 * 
 * @author fanhaoyu
 * 
 */

public class SwitchOptimizeDAOImpl extends JdbcDaoSupport implements
		SwitchOptimizeDAO {

	@Override
	public List<SwitchOptimizeConfig> queryAll() {
		String sql = "select * from mcbts_switchoptimize";
		List<SwitchOptimizeConfig> configList = getJdbcTemplate().query(sql,
				new SwitchOptimizeConfigMapper());
		return configList;
	}

	@Override
	public void saveOrUpdate(SwitchOptimizeConfig config) {
		String sql = "select * from mcbts_switchoptimize";
		StringBuilder builder = new StringBuilder();
		builder.append(sql).append(" where idx='").append(config.getIdx())
				.append("'");
		List<SwitchOptimizeConfig> configList = getJdbcTemplate().query(
				builder.toString(), new SwitchOptimizeConfigMapper());
		// 保存
		if (configList == null || configList.isEmpty()) {
			StringBuffer str = new StringBuffer();
			str.append("insert into mcbts_switchoptimize values ('")
					.append(config.getIdx()).append("','")
					.append(config.getSwitchFlag()).append("')");
			getJdbcTemplate().update(str.toString());
		}
		// 更新
		else {
			StringBuffer str = new StringBuffer();
			str.append("update mcbts_switchoptimize set switchFlag = '")
					.append(config.getSwitchFlag())
					.append("' where idx = '" + config.getIdx()).append("'");
			getJdbcTemplate().update(str.toString());
		}
	}

	class SwitchOptimizeConfigMapper implements RowMapper<SwitchOptimizeConfig> {

		@Override
		public SwitchOptimizeConfig mapRow(ResultSet rs, int index)
				throws SQLException {
			SwitchOptimizeConfig config = new SwitchOptimizeConfig();
			config.setIdx(rs.getLong("idx"));
			config.setSwitchFlag(rs.getInt("switchFlag"));
			return config;
		}
	}
}
