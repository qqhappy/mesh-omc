/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-22	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.dao.impl;

import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.core.model.secu.User;
import com.xinwei.minas.server.core.secu.dao.UserSecuDao;
import com.xinwei.omp.core.utils.DateUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * �û���ȫ����
 * 
 * @author liuzhongyan
 * 
 */

public class UserSecuDaoImpl extends JdbcDaoSupport implements UserSecuDao {

	/**
	 * �����û�
	 * 
	 * @param user
	 */
	public void addUser(User user) throws Exception {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("insert into secu_user(username,userpwd,");
		if (user.getDesc() != null && !user.getDesc().equals("")) {
			strBuffer.append("userdesc,");
		}
		strBuffer.append("roleId,canuse,ispermanentuser");
		if (user.getIspermanentuser() == 1) {
			strBuffer.append(",validtime");
		}
		strBuffer.append(") values('");
		strBuffer.append(user.getUsername() + "','");
		strBuffer.append(encodePassword(user.getPassword()) + "' ,");
		if (user.getDesc() != null && !user.getDesc().equals("")) {
			strBuffer.append("'" + user.getDesc() + "',");
		}
		strBuffer.append(user.getRoleId() + ",");
		strBuffer.append(user.getCanuse() + ",");
		strBuffer.append(user.getIspermanentuser());
		if (user.getIspermanentuser() == 1) {
			strBuffer.append("," + user.getValidtime() + ")");
		} else {
			strBuffer.append(")");
		}
		getJdbcTemplate().update(strBuffer.toString());
	}

	/**
	 * �޸��û�
	 */
	public void modUser(User user) throws Exception {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(" update secu_user set ");
		strBuffer.append(" userdesc = '" + user.getDesc() + "',");
		strBuffer.append(" roleId = " + user.getRoleId() + ",");
		strBuffer.append(" ispermanentuser = " + user.getIspermanentuser()
				+ ",");
		strBuffer.append(" validtime = " + user.getValidtime() + ",");
		strBuffer.append(" canuse = " + user.getCanuse());
		strBuffer.append(" where username  = '" + user.getUsername() + "'");
		getJdbcTemplate().update(strBuffer.toString());
	}

	/**
	 * �޸��û�����
	 */
	public void modUserPassword(String userName, String userPassword)
			throws Exception {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(" update secu_user set ");
		strBuffer.append(" userpwd = '" + encodePassword(userPassword) + "'");
		strBuffer.append(" where username  = '" + userName + "'");
		getJdbcTemplate().update(strBuffer.toString());
	}

	/**
	 * ɾ���û�
	 * 
	 * @param userId
	 */
	public void delUser(List<String> userNameList) throws RemoteException,
			Exception {
		for (String userName : userNameList) {
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("delete from secu_user where username  = '"
					+ userName + "'");
			getJdbcTemplate().update(strBuffer.toString());
		}

	}

	@Override
	public LoginUser queryUser(String userName) throws Exception {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("select * from secu_user where username  = '"
				+ userName + "'");
		List<LoginUser> userList = getJdbcTemplate().query(
				strBuffer.toString(), new allUserMapper());
		if (userList == null || userList.isEmpty())
			return null;
		return userList.get(0);
	}

	/**
	 * Ϊ�û����������
	 * 
	 * @param s
	 * @return
	 */
	private static String encodePassword(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes());
			byte[] b = md.digest();

			int i;
			StringBuilder buf = new StringBuilder();

			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");

				buf.append(Integer.toHexString(i));
			}

			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return "";
	}

	/**
	 * ��ѯ�����û�
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LoginUser> queryAllUser() throws Exception {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("select *  from secu_user ");
		List<LoginUser> userList = getJdbcTemplate().query(
				strBuffer.toString(), new allUserMapper());
		return userList;
	}

	/**
	 * ��ѯ���������û�
	 * 
	 * @param isOnline
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LoginUser> queryAllUserByOnline() throws Exception {
		// StringBuffer strBuffer = new StringBuffer();
		// strBuffer.append("select *  from secu_user ");
		// List<LoginUser> userList =
		// getJdbcTemplate().query(strBuffer.toString(),
		// new allUserMapper());
		// return userList;
		return null;
	}

	/**
	 * У���û��Ƿ���ڣ������Ƿ���ȷ
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void checkUser(String userName, String password) throws Exception {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("select * from secu_user where username = '"
				+ userName + "'");
		List<LoginUser> userList = getJdbcTemplate().query(
				strBuffer.toString(), new allUserMapper());
		if (userList.isEmpty()) {
			throw new Exception(OmpAppContext.getMessage("user_not_exist"));
		} else {
			// ��֤�����Ƿ���ȷ
			strBuffer = new StringBuffer();
			strBuffer.append("select * from secu_user where username = '"
					+ userName + "' and userpwd ='" + encodePassword(password)
					+ "'");
			userList = getJdbcTemplate().query(strBuffer.toString(),
					new allUserMapper());
			if (userList.isEmpty())
				throw new Exception(OmpAppContext.getMessage("wrong_password"));
		}
		User currentUser = userList.get(0);

		if (currentUser.getCanuse() == 1) {
			throw new Exception(OmpAppContext.getMessage("user_can_not_use"));
		}
		if (currentUser.getIspermanentuser() == 1) {
			long currentTime = Long.valueOf(String.valueOf(
					DateUtils.getBriefTimeFromMillisecondTime(new Date()
							.getTime())).substring(0, 8));
			if (currentUser.getValidtime() < currentTime) {
				throw new Exception(OmpAppContext.getMessage("user_overdue"));
			}
		}
	}

	/**
	 * �û���¼
	 * 
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void userLogin(LoginUser loginUser) throws Exception {
		this.checkUser(loginUser.getUsername(), loginUser.getPassword());
		// StringBuffer strBuffer = new StringBuffer();
		// strBuffer.append(" update secu_user set isOnline = 1,loginIp = '" +
		// loginUser.getLoginIp() +"', logintime = "+loginUser.getLogintime() );
		// strBuffer.append(" where username  = '" + loginUser.getUsername() +
		// "'");
		// getJdbcTemplate().update(strBuffer.toString());
	}

	/**
	 * �û��ǳ�
	 * 
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void userLogout(String userName) throws Exception {
		// StringBuffer strBuffer = new StringBuffer();
		// strBuffer.append(" update secu_user set isOnline = 0");
		// strBuffer.append(" where username  = '" + userName + "'");
		// getJdbcTemplate().update(strBuffer.toString());
	}

	private class allUserMapper implements RowMapper<LoginUser> {

		@Override
		public LoginUser mapRow(ResultSet rs, int index) throws SQLException {
			LoginUser user = new LoginUser();
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("userpwd"));
			user.setDesc(rs.getString("userdesc"));
			user.setRoleId(rs.getInt("roleId"));
			user.setIspermanentuser(rs.getInt("ispermanentuser"));
			user.setValidtime(rs.getLong("validtime"));
			user.setCanuse(rs.getInt("canuse"));
			return user;
		}

	}

}
