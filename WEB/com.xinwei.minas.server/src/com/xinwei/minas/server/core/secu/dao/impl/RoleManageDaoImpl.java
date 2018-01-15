/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.core.model.secu.Role;
import com.xinwei.minas.server.core.secu.dao.RoleManageDao;

/**
 * 
 * 角色管理Dao接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class RoleManageDaoImpl extends JdbcDaoSupport implements RoleManageDao {

	@Override
	public void addRole(Role role) throws Exception {
		StringBuilder strBuffer = new StringBuilder();
		strBuffer.append("insert into minas_role values(")
				.append(role.getRoleId()).append(",'")
				.append(role.getRoleName()).append("','")
				.append(role.getRoleDesc()).append("')");
		getJdbcTemplate().update(strBuffer.toString());
	}

	@Override
	public void deleteRole(Role role) throws Exception {
		StringBuilder strBuffer = new StringBuilder();
		strBuffer.append("delete from minas_role where roleId=").append(
				role.getRoleId());
		getJdbcTemplate().update(strBuffer.toString());
	}

	@Override
	public List<Role> queryAllRoles() throws Exception {
		String str = "select * from minas_role";
		return getJdbcTemplate().query(str, new RoleMap());
	}

	@Override
	public void modifyRole(Role role) throws Exception {

	}

	class RoleMap implements RowMapper<Role> {

		@Override
		public Role mapRow(ResultSet rs, int index) throws SQLException {
			Role role = new Role();
			role.setRoleId(Integer.valueOf(rs.getString("roleId")));
			role.setRoleName(rs.getString("roleName"));
			role.setRoleDesc(rs.getString("roleDesc"));
			return role;
		}

	}

}
