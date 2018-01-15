/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-8-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.dao.impl;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.server.xstat.dao.DataBaseChecker;
import com.xinwei.minas.server.xstat.dao.TableCache;

/**
 * 
 * ���ݿ�����JDBCʵ��
 * 
 * @author fanhaoyu
 * 
 */

public class DataBaseCheckerJdbcImpl extends JdbcDaoSupport implements
		DataBaseChecker {

	@Override
	public boolean isDataBaseExist(String dataBase) {
		if (!TableCache.getInstance().isDatabaseExist(dataBase)) {
			// ������治���ڣ����ж����ݿ����Ƿ����
			int countDataBaseNum = getJdbcTemplate().queryForInt(
					"select count(*) from information_schema.schemata where schema_name='"
							+ dataBase + "'");
			// �ж����ݿ��Ƿ����,������������½����ݿ�
			if (countDataBaseNum == 0) {
				return false;
			} else {
				// ������ݿ��д��ڣ�����ӵ�������
				TableCache.getInstance().addDataBase(dataBase);
			}
		}
		return true;
	}

	@Override
	public boolean isTableExist(String dataBase, String tableName) {
		// ���ָ��������
		if (TableCache.getInstance().isTableNotExist(dataBase, tableName)) {
			return false;
		}
		if (!TableCache.getInstance().isTableExist(dataBase, tableName)) {
			// ���ݿ����,�ж�ָ���ı��Ƿ����,������������½���
			String sql = "select count(*) from information_schema.tables where table_schema='"
					+ dataBase + "' and table_name='" + tableName + "'";
			int countTableNum = getJdbcTemplate().queryForInt(sql);
			if (countTableNum == 0) {
				// ��һ�β�ѯʱ�����ڣ�����벻���ڵı����У�֮���ѯֻ���ж�Ŀ����Ƿ��ڲ����ڵı����оͿ����жϱ��Ƿ����
				TableCache.getInstance().addNotExistTable(dataBase, tableName);
				return false;
			} else {
				// ������ݿ��д��ڣ�����ӵ�������
				TableCache.getInstance().addTable(dataBase, tableName);
			}
		}
		return true;
	}

}
