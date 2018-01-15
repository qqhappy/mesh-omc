/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.server.enb.dao.EnbBizTemplateDAO;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.server.utils.XJsonUtils;

/**
 * 
 * eNB业务数据模板DAO接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbBizTemplateDAOImpl extends JdbcDaoSupport implements
		EnbBizTemplateDAO {

	private static String TABLE_NAME = "enb_biz_template";

	@Override
	public Map<String, List<XBizRecord>> queryTemplateData(int enbTypeId,
			String version) throws Exception {
		Map<String, List<XBizRecord>> dataMap = new HashMap<String, List<XBizRecord>>();

		String sql = "select * from " + TABLE_NAME + " where enb_type='"
				+ enbTypeId + "' " + " and version='" + version + "'";
		getJdbcTemplate().query(sql, new BizRowMapper(dataMap));

		return dataMap;
	}

	@Override
	public XBizTable queryTemplateData(int enbTypeId, String version,
			String tableName) throws Exception {
		Map<String, List<XBizRecord>> dataMap = new HashMap<String, List<XBizRecord>>();

		String sql = "select * from " + TABLE_NAME + " where enb_type='"
				+ enbTypeId + "' " + " and version='" + version
				+ "' and biz_name='" + tableName + "'";
		getJdbcTemplate().query(sql, new BizRowMapper(dataMap));

		XBizTable bizTable = new XBizTable((long) MoTypeDD.ENODEB, tableName);
		bizTable.setRecords(dataMap.get(tableName));
		return bizTable;
	}

	class BizRowMapper implements RowMapper<XBizRecord> {

		private Map<String, List<XBizRecord>> dataMap;

		public BizRowMapper(Map<String, List<XBizRecord>> dataMap) {
			this.dataMap = dataMap;
		}

		public XBizRecord mapRow(ResultSet rs, int index) throws SQLException {
			int enbTypeId = rs.getInt("enb_type");
			String version = rs.getString("version");
			String tableName = rs.getString("biz_name");
			String bizParameter = rs.getString("biz_parameter");
			XBizRecord record = XJsonUtils.jsonObjStr2Object(bizParameter,
					XBizRecord.class);
			// 调整record中field的顺序
			XBizRecord newRecord = EnbBizHelper.formatBizRecord(enbTypeId,
					version, tableName, record);
			List<XBizRecord> records = dataMap.get(tableName);
			if (records == null) {
				records = new ArrayList<XBizRecord>();
				dataMap.put(tableName, records);
			}
			records.add(newRecord);
			return newRecord;
		}

	}

}
