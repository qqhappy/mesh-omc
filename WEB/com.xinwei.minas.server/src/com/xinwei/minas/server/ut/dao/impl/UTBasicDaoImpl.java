/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-31	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.server.ut.dao.UTBasicDao;

/**
 * 
 * ÖÕ¶Ë»ù´¡³Ö¾Ã²ã
 * 
 * 
 * @author tiance
 * 
 */

public class UTBasicDaoImpl extends JdbcDaoSupport implements UTBasicDao {

	private Log log = LogFactory.getLog(UTBasicDaoImpl.class);

	private String platformLang;

	public void setPlatformLang(String platformLang) {
		this.platformLang = "type_name_" + platformLang;
	}

	@Override
	public List<TerminalVersion> queryUTTypes() {
		String querySql = "select id, " + platformLang
				+ " from terminal_version_type order by id";
		return getJdbcTemplate().query(querySql, new TVMRowMapper());
	}

	private class TVMRowMapper implements RowMapper<TerminalVersion> {
		@Override
		public TerminalVersion mapRow(ResultSet rs, int arg1)
				throws SQLException {
			TerminalVersion tv = new TerminalVersion();
			if (rs != null) {
				tv.setTypeId(rs.getInt("id"));
				tv.setType(rs.getString(platformLang));
			}
			return tv;
		}
	}
}
