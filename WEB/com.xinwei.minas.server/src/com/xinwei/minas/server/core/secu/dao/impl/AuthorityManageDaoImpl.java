/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-5	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.core.model.OperAction;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.core.model.secu.OperSignature;
import com.xinwei.minas.server.core.secu.dao.AuthorityManageDao;
import com.xinwei.minas.server.core.secu.service.LoginUserCache;

/**
 * 
 * 权限管理Dao接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class AuthorityManageDaoImpl extends JdbcDaoSupport implements
		AuthorityManageDao {

	@Override
	public List<OperAction> queryAuthority(String username) throws Exception {
		List<LoginUser> userList = LoginUserCache.getInstance()
				.queryOnlineUser();
		LoginUser currenUser = null;
		for (LoginUser user : userList) {
			if (user.getUsername().equals(username))
				currenUser = user;
		}
		if (currenUser == null)
			return null;
		return queryAuthority(currenUser.getRoleId());
	}

	@Override
	public List<OperAction> queryAuthority(int roleType) throws Exception {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("select operName, actions from role_privilege ")
				.append("where roleId=").append(roleType);
		List<OperAction> list = getJdbcTemplate().query(strBuilder.toString(),
				new OperActionMap(true));
		return list;
	}

	@Override
	public OperAction queryOperAction(OperSignature signature) throws Exception {
		// 找到facade和method所对应的operAction列表
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("select operName,action from minas_oper_action ")
				.append("where facade='").append(signature.getFacade())
				.append("' and method='").append(signature.getMethod())
				.append("'");
		List<OperAction> operActionList = getJdbcTemplate().query(
				strBuilder.toString(), new OperActionMap(false));
		if (operActionList == null || operActionList.isEmpty())
			return null;
		// 如果是通用业务，则需要根据映射找到bizName所对应的operName
		if (signature.isGenericBiz()) {
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder
					.append("select operName from minas_biz_operation where bizName='")
					.append(signature.getBizName()).append("'");
			// 结果只有一个或没有
			List<String> operNameList = getJdbcTemplate().query(
					sqlBuilder.toString(), new OperNameMap());
			if (operNameList == null || operNameList.isEmpty())
				return null;
			String operName = operNameList.get(0);
			for (OperAction action : operActionList) {
				if (action.getOperName().equals(operName))
					return action;
			}
		}
		// 如果是特殊业务，则需要跟据bizName获取operAction
		if (signature.isSpecialBiz()) {
			for (OperAction action : operActionList) {
				if (action.getOperName().equals(signature.getBizName()))
					return action;
			}
		}
		return operActionList.get(0);
	}

	@Override
	public String queryOperation(String operName) throws Exception {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("select operDesc from minas_operation ")
				.append("where operName='").append(operName).append("'");
		List<String> list = getJdbcTemplate().query(strBuilder.toString(),
				new OperDescMap());
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}

	private class OperActionMap implements RowMapper<OperAction> {

		boolean isPrivilege = false;

		OperActionMap(boolean isPrivilege) {
			this.isPrivilege = isPrivilege;
		}

		@Override
		public OperAction mapRow(ResultSet rs, int index) throws SQLException {
			OperAction operAction = new OperAction();
			operAction.setOperName(rs.getString("operName"));
			List<String> actionList = new ArrayList<String>();
			String columnName = "action";
			if (isPrivilege) {
				columnName = "actions";
			}
			String[] actions = rs.getString(columnName).split(",");
			for (String action : actions) {
				actionList.add(action);
			}
			operAction.setActions(actionList);
			return operAction;
		}

	}

	private class OperNameMap implements RowMapper<String> {

		@Override
		public String mapRow(ResultSet rs, int index) throws SQLException {
			return rs.getString("operName");
		}

	}

	private class OperDescMap implements RowMapper<String> {

		@Override
		public String mapRow(ResultSet rs, int columnIndex) throws SQLException {
			return rs.getString("operDesc");
		}

	}

}
