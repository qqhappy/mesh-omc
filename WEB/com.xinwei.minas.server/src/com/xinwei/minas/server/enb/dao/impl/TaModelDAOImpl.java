/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-2	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.enb.core.model.corenet.TaModel;
import com.xinwei.minas.server.enb.dao.TaModelDAO;
import com.xinwei.omp.core.model.biz.PagingCondition;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * ¸ú×ÙÇøÂëDAO½Ó¿Ú
 * 
 * @author fanhaoyu
 * 
 */

public class TaModelDAOImpl extends JdbcDaoSupport implements TaModelDAO {

	private static final String TA_TABLE_NAME = "ta_table";

	@Override
	public void addTaItem(TaModel taModel) throws Exception {
		String sql = "insert into " + TA_TABLE_NAME + " values(?,?,?)";
		getJdbcTemplate().update(sql, taModel.getId(), taModel.getCode(),
				taModel.getRemark());
	}

	@Override
	public void modifyTaItem(TaModel taModel) throws Exception {
		String sql = "update " + TA_TABLE_NAME + " set code=?"
				+ ", remark=? where id=?";
		getJdbcTemplate().update(sql, taModel.getCode(), taModel.getRemark(),
				taModel.getId());
	}

	@Override
	public void deleteTaItem(int id) throws Exception {
		String sql = "delete from " + TA_TABLE_NAME + " where id=?";
		getJdbcTemplate().update(sql, id);
	}

	@Override
	public TaModel queryTaItemById(int id) throws Exception {
		String sql = "select * from " + TA_TABLE_NAME + " where id=" + id;
		List<TaModel> modelList = getJdbcTemplate().query(sql,
				new TaModelRowMapper());
		if (modelList == null || modelList.isEmpty())
			return null;
		return modelList.get(0);
	}

	@Override
	public List<TaModel> queryAllTaItems() throws Exception {
		String sql = "select * from " + TA_TABLE_NAME;
		return getJdbcTemplate().query(sql, new TaModelRowMapper());
	}

	@Override
	public PagingData<TaModel> queryTaItems(PagingCondition condition)
			throws Exception {
		int currentPage = condition.getCurrentPage();
		if (currentPage <= 0) {
			currentPage = 1;
		}
		int numPerPage = condition.getNumPerPage();
		int startIndex = (currentPage - 1) * numPerPage;

		int totalNum = getJdbcTemplate().queryForInt(
				"select count(*) from " + TA_TABLE_NAME);

		String sql = "select * from " + TA_TABLE_NAME + " limit ?,?";
		List<TaModel> taModels = getJdbcTemplate()
				.query(sql, new Object[] { startIndex, numPerPage },
						new TaModelRowMapper());

		PagingData<TaModel> result = new PagingData<TaModel>();
		result.setResults(taModels);
		result.setCurrentPage(currentPage);
		result.setNumPerPage(numPerPage);

		int totalPages = totalNum / numPerPage;
		if (totalNum % numPerPage > 0) {
			totalPages++;
		}
		result.setTotalPages(totalPages);
		result.setTotalNum(totalNum);

		return result;
	}

	class TaModelRowMapper implements RowMapper<TaModel> {

		@Override
		public TaModel mapRow(ResultSet rs, int arg1) throws SQLException {
			TaModel taModel = new TaModel();
			taModel.setId(rs.getInt("id"));
			taModel.setCode(rs.getString("code"));
			taModel.setRemark(rs.getString("remark"));
			return taModel;
		}

	}

}
