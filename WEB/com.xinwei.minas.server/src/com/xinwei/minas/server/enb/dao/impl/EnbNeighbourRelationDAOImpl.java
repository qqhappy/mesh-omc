/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.server.enb.dao.EnbNeighbourRelationDAO;
import com.xinwei.minas.server.enb.model.EnbNeighbourRelation;

/**
 * 
 * ¿‡ºÚ“™√Ë ˆ
 * 
 * @author fanhaoyu
 * 
 */

public class EnbNeighbourRelationDAOImpl extends JdbcDaoSupport implements
		EnbNeighbourRelationDAO {

	private static final String TABLE_NAME = "enb_neighbour_relation";

	@Override
	public List<EnbNeighbourRelation> queryRelation(long moId,
			Integer srvCellId, Long enbId, Integer cellId) throws Exception {
		String whereCondition = generateWhereConditon(moId, srvCellId, enbId,
				cellId);
		String sql = "select * from " + TABLE_NAME + whereCondition;

		return getJdbcTemplate().query(sql, new RelationRowMapper());
	}

	@Override
	public void addRelation(EnbNeighbourRelation relation) throws Exception {
		String sql = "insert into " + TABLE_NAME + " values(?,?,?,?,?,?)";
		getJdbcTemplate().update(sql, relation.getIdx(), relation.getMoId(),
				relation.getSrvCellId(), relation.getEnbId(),
				relation.getCellId(), relation.getIsNeighbour());
	}

	@Override
	public void updateRelation(EnbNeighbourRelation relation) throws Exception {
		String sql = "update " + TABLE_NAME + " set isNeighbour=? where idx=?";
		getJdbcTemplate().update(sql, relation.getIsNeighbour(),
				relation.getIdx());
	}

	@Override
	public void deleteRelation(long moId, Integer srvCellId, Long enbId,
			Integer cellId) throws Exception {
		String whereCondition = generateWhereConditon(moId, srvCellId, enbId,
				cellId);
		String sql = "delete from " + TABLE_NAME + whereCondition;
		getJdbcTemplate().update(sql);
	}

	private String generateWhereConditon(long moId, Integer srvCellId,
			Long enbId, Integer cellId) {
		StringBuilder builder = new StringBuilder();
		builder.append(" where moId=" + moId);
		if (srvCellId != null) {
			builder.append(" and srvCellId=").append(srvCellId);
		}
		if (enbId != null) {
			builder.append(" and enbId=").append(enbId);
		}
		if (cellId != null) {
			builder.append(" and cellId=").append(cellId);
		}
		return builder.toString();
	}

	class RelationRowMapper implements RowMapper<EnbNeighbourRelation> {

		@Override
		public EnbNeighbourRelation mapRow(ResultSet rs, int arg1)
				throws SQLException {
			EnbNeighbourRelation relation = new EnbNeighbourRelation();
			relation.setIdx(rs.getLong("idx"));
			relation.setMoId(rs.getLong("moId"));
			relation.setSrvCellId(rs.getInt("srvCellId"));
			relation.setEnbId(rs.getLong("enbId"));
			relation.setCellId(rs.getInt("cellId"));
			relation.setIsNeighbour(rs.getInt("isNeighbour"));
			return relation;
		}

	}

}
