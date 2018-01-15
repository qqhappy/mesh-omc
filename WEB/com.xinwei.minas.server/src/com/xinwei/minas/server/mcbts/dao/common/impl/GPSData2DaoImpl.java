/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-22	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.mcbts.core.model.common.GPSData;
import com.xinwei.minas.server.mcbts.dao.common.GPSData2Dao;

/**
 * 
 * GPS������ϢDao�ĵڶ�����,��Ҫ���ڴ���MA=7��GPS��Ϣ
 * 
 * @author tiance
 * 
 */

public class GPSData2DaoImpl extends JdbcDaoSupport implements GPSData2Dao {

	private Log log = LogFactory.getLog(GPSData2DaoImpl.class);

	/**
	 * �����������¸�����վ��GPS���ݵ����ݿ�
	 */
	@Override
	public void batchSaveOrUpdate(List<GPSData> tasks) {
		String querySql = "select moId from mcbts_gpsdata";

		// ��ѯ�����е�moId
		List<String> moIds = getJdbcTemplate().query(querySql,
				new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int arg1)
							throws SQLException {
						return rs.getString("moId");
					}
				});

		log.debug("Finished querying existing gps data.");

		StringBuilder sb = new StringBuilder();

		for (String moId : moIds) {
			sb.append(moId).append("/");
		}
		// ���ջ��һ��moId���ַ���,����: ***/***/***
		String moIdStr = sb.toString();

		final List<GPSData> toInsert = new ArrayList<GPSData>();
		final List<GPSData> toUpdate = new ArrayList<GPSData>();

		for (GPSData data : tasks) {
			// ���������ǰmoId,�ͷ���toUpdateִ�и��²���,����ִ�в������
			if (moIdStr.contains(data.getMoId() + "/"))
				toUpdate.add(data);
			else
				toInsert.add(data);
		}

		if (toInsert.size() > 0) {
			log.debug("to insert gps data...");
			final String insertSql = "insert into mcbts_gpsdata(idx, moId, Latitude, Longitude, "
					+ "Height, GMTOffset, MinimumTrackingsatellite) values (?,?,?,?,?,?,?)";
			// ִ����������
			getJdbcTemplate().batchUpdate(insertSql,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							GPSData data = toInsert.get(i);
							int j = 1;
							ps.setLong(j++, data.getIdx());
							ps.setLong(j++, data.getMoId());
							ps.setLong(j++, data.getLatitude());
							ps.setLong(j++, data.getLongitude());
							ps.setLong(j++, data.getHeight());
							ps.setLong(j++, data.getGmtOffset());
							ps.setInt(j++, data.getMinimumTrackingsatellite());
						}

						@Override
						public int getBatchSize() {
							return toInsert.size();
						}
					});
			log.debug("Finish batch inserting GPS information. Amount: "
					+ toInsert.size());
		}

		if (toUpdate.size() > 0) {
			log.debug("to update gps data...");
			final String updateSql = "update mcbts_gpsdata set Latitude = ?, Longitude = ?, "
					+ "Height = ? where moId = ?";
			// ִ����������
			getJdbcTemplate().batchUpdate(updateSql,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							GPSData data = toUpdate.get(i);
							int j = 1;
							ps.setLong(j++, data.getLatitude());
							ps.setLong(j++, data.getLongitude());
							ps.setLong(j++, data.getHeight());
							ps.setLong(j++, data.getMoId());
						}

						@Override
						public int getBatchSize() {
							return toUpdate.size();
						}
					});
			log.debug("Finish batch updating GPS information. Amount: "
					+ toUpdate.size());
		}
	}
}
