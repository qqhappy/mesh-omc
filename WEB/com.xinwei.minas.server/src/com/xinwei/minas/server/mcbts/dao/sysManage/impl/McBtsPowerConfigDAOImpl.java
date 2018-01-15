/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-19	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.xinwei.minas.mcbts.core.model.sysManage.PowerSupply;
import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsPowerConfigDAO;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述:操作基站和电源的关系
 * </p>
 * 
 * @author qiwei
 * 
 */

public class McBtsPowerConfigDAOImpl implements McBtsPowerConfigDAO {

	private static final String TABLE_NAME = "mcbts_powerconfig_relative";
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public McBtsPowerConfigDAOImpl() {
	}

	/**
	 * 通过基站moId查询电源
	 */
	@Override
	public PowerSupply queryByMoId(long moid) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder
				.append("SELECT p.* FROM mcbts_powerconfig_relative AS mpr ,power_supply AS p WHERE mpr.`power_id`=p.`idx` AND mpr.`mo_id`=")
				.append(moid);
		List<PowerSupply> powerSupplyList = getJdbcTemplate().query(
				queryBuilder.toString(), new PowerSupplyRowerMapper());
		if (powerSupplyList != null && powerSupplyList.size() != 0) {
			return powerSupplyList.get(0);
		}
		return null;
	}

	/**
	 * 保存电源和基站的映射关系
	 */
	@Override
	public void saveByMoIdAndPowerId(long idx, PowerSupply powerSupply) {
		StringBuilder delBuilder = new StringBuilder();
		long moId = 0l;
		// 从集合中取出moId
		if (powerSupply.getMoIdSet() != null
				&& powerSupply.getMoIdSet().size() == 1) {
			for (Long num : powerSupply.getMoIdSet()) {
				moId = num;
			}
		}
		// 从对应的表中删除原来的对应关系
		delBuilder.append(
				"DELETE  FROM mcbts_powerconfig_relative WHERE mo_id = ")
				.append(moId);

		StringBuilder insertBuilder = new StringBuilder();
		insertBuilder
				.append("INSERT INTO mcbts_powerconfig_relative(idx,power_id,mo_id) VALUES (")
				.append(idx + ",").append(powerSupply.getIdx() + ",")
				.append(moId).append(")");
		getJdbcTemplate()
				.batchUpdate(
						new String[] { delBuilder.toString(),
								insertBuilder.toString() });
	}

	/**
	 * 通过电源的主键（power_id）查询基站的moId集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HashSet<Long> queryMcbtsByPowerSupply(PowerSupply powerSupply) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder
				.append("SELECT mo_id FROM mcbts_powerconfig_relative WHERE power_id = ")
				.append(powerSupply.getIdx());
		List<Long> moIdList= getJdbcTemplate().query(queryBuilder.toString(),
				new MoIdRowMapper());
		HashSet<Long> moIdSet = new HashSet<Long>();
		moIdSet.addAll(moIdList);
		return moIdSet;
	}

	/**
	 * 电源查询结果映射
	 * 
	 * @author qiwei
	 * 
	 */
	private class PowerSupplyRowerMapper implements RowMapper<PowerSupply> {

		@Override
		public PowerSupply mapRow(ResultSet rs, int index) throws SQLException {
			PowerSupply powerSupply = new PowerSupply();
			powerSupply.setIdx(rs.getLong("idx"));
			powerSupply.setIpAddress(rs.getString("ipAddress"));
			powerSupply.setPort(rs.getInt("port"));
			powerSupply.setCurrentType(rs.getInt("currentType"));
			powerSupply.setFactoryType(rs.getInt("factory"));
			return powerSupply;
		}

	}

	/**
	 * 查询出的moId结果的映射封装
	 */
	private class MoIdRowMapper implements RowMapper<Long> {

		@Override
		public Long mapRow(ResultSet rs, int index) throws SQLException {

			return rs.getLong("mo_id");

		}

	}

	@Override
	public void deleteRelationByMoId(long moid) {
		StringBuilder delBuilder = new StringBuilder();
		delBuilder.append(
				"DELETE FROM mcbts_powerconfig_relative WHERE mo_id = ")
				.append(moid);
		getJdbcTemplate().execute(delBuilder.toString());
	}

}
