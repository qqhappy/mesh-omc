package com.xinwei.lte.web.enb.model;

import com.xinwei.minas.core.model.secu.LoginUser;

/**
 * 登陆用户模型，用于解决freemarker处理boolean的问题
 * 
 * @author zhangqiang
 * 
 */
public class UserModel {
	/**
	 * 是否在线，1：是，2：否
	 */
	private int isOnline;

	/**
	 * 有效时间
	 */
	private String validtimeString;

	/**
	 * 登陆用户
	 */
	private LoginUser user = new LoginUser();

	public UserModel(LoginUser user) {
		this.user = user;
		if (user.getIspermanentuser() == 1) {
			validtimeString = toTimeString(user.getValidtime());
		}
		if (user.isOnline()) {
			isOnline = 1;
		} else {
			isOnline = 2;
		}
	}

	/**
	 * 将Long型时间串转为String类型表示
	 * 
	 * @return
	 */
	public String toTimeString(long time) {
		String str = String.valueOf(time);
		String str1 = str.substring(0, 4);
		String str2 = str.substring(4, 6);
		String str3 = str.substring(6, 8);
		return str1 + "-" + str2 + "-" + str3;
	}

	public int getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}

	public LoginUser getUser() {
		return user;
	}

	public void setUser(LoginUser user) {
		this.user = user;
	}

	public String getValidtimeString() {
		return validtimeString;
	}

	public void setValidtimeString(String validtimeString) {
		this.validtimeString = validtimeString;
	}

}
