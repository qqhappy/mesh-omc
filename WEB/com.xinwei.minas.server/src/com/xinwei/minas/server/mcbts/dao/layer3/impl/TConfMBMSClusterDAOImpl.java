/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-2	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.arrowping.mcwill.ems.dbEntity.TConfMBMSBts;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfMBMSClusterDAO;

/**
 * 
 * 同播集群DAO接口实现
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class TConfMBMSClusterDAOImpl extends JdbcDaoSupport implements TConfMBMSClusterDAO {

	@Override
	public List<Long> queryAllMBMSClusterBtsMoId() throws Exception {
		final List<Long> result = new ArrayList<Long>();
		
		String sql = "SELECT mbms.moId FROM t_conf_mbms_bts AS mbms, " +
				"mcbts_trunkConfig AS trunk " +
				"WHERE mbms.moId>0 AND mbms.moId = trunk.moId AND mbms.flag="+ TConfMBMSBts.FLAG_SUPPORT
				+" AND trunk.Flag=1";
		
		this.getJdbcTemplate().query(sql, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet arg0) throws SQLException {
				result.add(arg0.getLong("moId"));
			}
		});
		return result;
	}

}
