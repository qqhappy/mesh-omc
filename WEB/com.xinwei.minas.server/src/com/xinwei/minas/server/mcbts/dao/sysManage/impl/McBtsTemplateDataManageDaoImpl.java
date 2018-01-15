/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-27	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage.impl;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsTemplateDataManageDao;

/**
 * 
 * ��վģ�����ݹ������
 * 
 * 
 * @author tiance
 * 
 */

/**
 * @author tiance
 * 
 */
public class McBtsTemplateDataManageDaoImpl extends JdbcDaoSupport implements
		McBtsTemplateDataManageDao {

	private String ftpIp;

	private String ftpUserName;

	private String ftpPassword;

	public void setFtpIp(String ftpIp) {
		this.ftpIp = ftpIp;
	}

	public void setFtpUserName(String ftpUserName) {
		this.ftpUserName = ftpUserName;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	/**
	 * ����������־��FTP IPΪ���õ�IP
	 */
	@Override
	public void updateFtpIp() {
		getJdbcTemplate()
				.update("update mcbts_perflog set ftpServerIp = ?, userName = ?, password = ? where moId < 0",
						ftpIp, ftpUserName, ftpPassword);
	}

}
