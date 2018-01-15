/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-20	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.enb.core.model.EnbAsset;
import com.xinwei.minas.enb.core.model.EnbAssetCondition;
import com.xinwei.minas.enb.core.model.EnbAssetHistory;
import com.xinwei.minas.server.enb.dao.EnbAssetDAO;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * @author chenlong
 * 
 */

public class EnbAssetDAOImpl extends JdbcDaoSupport implements EnbAssetDAO {

	private Log log = LogFactory.getLog(EnbAssetDAOImpl.class);

	private String ENB_ASSET_TABLE = "enb_asset_table";

	private String ENB_ASSET_HISTORY_TABLE = "enb_asset_history_table";

	@Override
	public void add(EnbAsset enbAsset) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ").append(ENB_ASSET_TABLE);
		sql.append(" (production_sn,enbId,hardware_version,status,node_type,location_info,provider_name,manufacture_date,start_time,stop_time,last_serve_time,remark)");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?)");
		log.debug(sql + ",enbId=" + enbAsset.getEnbId() + ",production_sn="
				+ enbAsset.getProductionSN());
		getJdbcTemplate().update(sql.toString(), enbAsset.getProductionSN(),
				enbAsset.getEnbId(), enbAsset.getHardwareVersion(),
				enbAsset.getStatus(), enbAsset.getNodeType(),
				enbAsset.getLocationInfo(), enbAsset.getProviderName(),
				enbAsset.getManufactureDate(), enbAsset.getStartTime(),
				enbAsset.getStopTime(), enbAsset.getLastServeTime(),
				enbAsset.getRemark());

	}

	@Override
	public void update(EnbAsset enbAsset) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(ENB_ASSET_TABLE);
		sql.append(" set production_sn=?,enbId=?,hardware_version=?,status=?,node_type=?,location_info=?,provider_name=?,manufacture_date=?,start_time=?,stop_time=?,last_serve_time=?,remark=?");
		sql.append(" where id=?");
		getJdbcTemplate().update(sql.toString(), enbAsset.getProductionSN(),
				enbAsset.getEnbId(), enbAsset.getHardwareVersion(),
				enbAsset.getStatus(), enbAsset.getNodeType(),
				enbAsset.getLocationInfo(), enbAsset.getProviderName(),
				enbAsset.getManufactureDate(), enbAsset.getStartTime(),
				enbAsset.getStopTime(), enbAsset.getLastServeTime(),
				enbAsset.getRemark(), enbAsset.getId());
	}

	@Override
	public void delete(EnbAsset enbAsset) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ").append(ENB_ASSET_TABLE);
		sql.append(" where id=?");
		getJdbcTemplate().update(sql.toString(), enbAsset.getId());
	}

	@Override
	public EnbAsset queryById(long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(ENB_ASSET_TABLE);
		sql.append(" where id=?");
		Object[] params = { id };
		List<EnbAsset> list = getJdbcTemplate().query(sql.toString(), params,
				new AssetRowMapper());
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<EnbAsset> queryAll() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(ENB_ASSET_TABLE);
		List<EnbAsset> list = getJdbcTemplate().query(sql.toString(),
				new AssetRowMapper());
		if (null == list) {
			return new ArrayList<EnbAsset>();
		}
		return list;
	}

	@Override
	public void addHistory(EnbAssetHistory assetHistory) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ").append(ENB_ASSET_HISTORY_TABLE);
		sql.append(" (production_sn,enbId,hardware_version,node_type,location_info,provider_name,manufacture_date,start_time,stop_time,confirm_stop_time,confirm_user,last_serve_time,remark)");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
		getJdbcTemplate().update(sql.toString(),
				assetHistory.getProductionSN(), assetHistory.getEnbId(),
				assetHistory.getHardwareVersion(), assetHistory.getNodeType(),
				assetHistory.getLocationInfo(), assetHistory.getProviderName(),
				assetHistory.getManufactureDate(), assetHistory.getStartTime(),
				assetHistory.getStopTime(), assetHistory.getConfirmStopTime(),
				assetHistory.getConfirmUser(), assetHistory.getLastServeTime(),
				assetHistory.getRemark());

	}

	@Override
	public PagingData<EnbAsset> queryByCondition(EnbAssetCondition condition)
			throws Exception {
		PagingData<EnbAsset> result = new PagingData<EnbAsset>();

		result.setCurrentPage(condition.getCurrentPage());
		result.setNumPerPage(condition.getNumPerPage());

		int firstResult = (condition.getCurrentPage() - 1)
				* condition.getNumPerPage();
		int maxResults = condition.getNumPerPage();

		StringBuilder sqlNum = new StringBuilder();
		StringBuilder sql = new StringBuilder();

		sqlNum.append("select count(*) from ").append(ENB_ASSET_TABLE);
		sql.append("select * from ").append(ENB_ASSET_TABLE);
		sqlNum.append(" where 1=1");
		sql.append(" where 1=1");
		List<Object> params = new ArrayList<Object>();
		if (-1 != condition.getId()) {
			sqlNum.append(" and id = ?");
			sql.append(" and id = ?");
			params.add(condition.getId());
		} else {
			if (-1 != condition.getEnbId()) {
				sqlNum.append(" and enbId = ?");
				sql.append(" and enbId = ?");
				params.add(condition.getEnbId());
			}
			if (-1 != condition.getStatus()) {
				sqlNum.append(" and status = ?");
				sql.append(" and status = ?");
				params.add(condition.getStatus());
			}
			if (-1 != condition.getNodeType()) {
				sqlNum.append(" and node_type = ?");
				sql.append(" and node_type = ?");
				params.add(condition.getNodeType());
			}
			if (null != condition.getProductionSN()
					&& !"".equals(condition.getProductionSN())) {
				sqlNum.append(" and production_sn = ?");
				sql.append(" and production_sn = ?");
				params.add(condition.getProductionSN());
			}
			if (-1 != condition.getStartTime_start()) {
				sqlNum.append(" and start_time >= ?");
				sql.append(" and start_time >= ?");
				params.add(condition.getStartTime_start());
			}
			if (-1 != condition.getStartTime_end()) {
				sqlNum.append(" and start_time <= ?");
				sql.append(" and start_time <= ?");
				params.add(condition.getStartTime_end());
			}
			if (-1 != condition.getStopTime_start()) {
				sqlNum.append(" and stop_time >= ?");
				sql.append(" and stop_time >= ?");
				params.add(condition.getStopTime_start());
			}
			if (-1 != condition.getStopTime_end()) {
				sqlNum.append(" and stop_time <= ?");
				sql.append(" and stop_time <= ?");
				params.add(condition.getStopTime_end());
			}
		}
		sql.append(" order by id desc limit " + firstResult + "," + maxResults);
		sql.append(" ");
		Object[] paramObjects = new Object[params.size()];
		for (int i = 0; i < params.size(); i++) {
			paramObjects[i] = params.get(i);
		}
		result.setTotalNum(getJdbcTemplate().queryForLong(sqlNum.toString(),paramObjects));
		List<EnbAsset> list = getJdbcTemplate().query(sql.toString(),
				paramObjects, new AssetRowMapper());
		result.setResults(list);
		return result;
	}

	@Override
	public PagingData<EnbAssetHistory> queryHistoryByCondition(
			EnbAssetCondition condition) throws Exception {
		PagingData<EnbAssetHistory> result = new PagingData<EnbAssetHistory>();
		result.setCurrentPage(condition.getCurrentPage());
		result.setNumPerPage(condition.getNumPerPage());

		int firstResult = (condition.getCurrentPage() - 1)
				* condition.getNumPerPage();
		int maxResults = condition.getNumPerPage();

		StringBuilder sql = new StringBuilder();
		StringBuilder sqlNum = new StringBuilder();
		sqlNum.append("select count(*) from ").append(ENB_ASSET_HISTORY_TABLE);

		sql.append("select * from ").append(ENB_ASSET_HISTORY_TABLE);
		sqlNum.append(" where 1=1");
		sql.append(" where 1=1");
		List<Object> params = new ArrayList<Object>();
		if (-1 != condition.getId()) {
			sqlNum.append(" and id = ?");
			sql.append(" and id = ?");
			params.add(condition.getId());
		} else {
			if (-1 != condition.getEnbId()) {
				sqlNum.append(" and enbId = ?");
				sql.append(" and enbId = ?");
				params.add(condition.getEnbId());
			}
			if (-1 != condition.getNodeType()) {
				sqlNum.append(" and node_type = ?");
				sql.append(" and node_type = ?");
				params.add(condition.getNodeType());
			}
			if (null != condition.getProductionSN()
					&& !"".equals(condition.getProductionSN())) {
				sqlNum.append(" and production_sn = ?");
				sql.append(" and production_sn = ?");
				params.add(condition.getProductionSN());
			}
			if (-1 != condition.getStartTime_start()) {
				sqlNum.append(" and start_time >= ?");
				sql.append(" and start_time >= ?");
				params.add(condition.getStartTime_start());
			}
			if (-1 != condition.getStartTime_end()) {
				sqlNum.append(" and start_time <= ?");
				sql.append(" and start_time <= ?");
				params.add(condition.getStartTime_end());
			}
			if (-1 != condition.getStopTime_start()) {
				sqlNum.append(" and stop_time >= ?");
				sql.append(" and stop_time >= ?");
				params.add(condition.getStopTime_start());
			}
			if (-1 != condition.getStopTime_end()) {
				sqlNum.append(" and stop_time <= ?");
				sql.append(" and stop_time <= ?");
				params.add(condition.getStopTime_end());
			}
			if (-1 != condition.getConfirmStopTime_start()) {
				sqlNum.append(" and confirm_stop_time >= ?");
				sql.append(" and confirm_stop_time >= ?");
				params.add(condition.getConfirmStopTime_start());
			}
			if (-1 != condition.getConfirmStopTime_end()) {
				sqlNum.append(" and confirm_stop_time <= ?");
				sql.append(" and confirm_stop_time <= ?");
				params.add(condition.getConfirmStopTime_end());
			}
			if (null != condition.getConfirmUser()
					&& !"".equals(condition.getConfirmUser())) {
				sqlNum.append(" and confirm_user = ?");
				sql.append(" and confirm_user = ?");
				params.add(condition.getConfirmUser());
			}
		}
		sql.append(" order by id desc limit " + firstResult + "," + maxResults);

		Object[] paramObjects = new Object[params.size()];
		for (int i = 0; i < params.size(); i++) {
			paramObjects[i] = params.get(i);
		}
		result.setTotalNum(getJdbcTemplate().queryForLong(sqlNum.toString(),paramObjects));
		List<EnbAssetHistory> list = getJdbcTemplate().query(sql.toString(),
				paramObjects, new AssetHistoryRowMapper());
		result.setResults(list);
		return result;
	}

	private class AssetRowMapper implements RowMapper<EnbAsset> {

		@Override
		public EnbAsset mapRow(ResultSet rs, int index) throws SQLException {
			EnbAsset asset = new EnbAsset();
			asset.setId(rs.getLong("id"));
			asset.setProductionSN(rs.getString("production_sn"));
			asset.setEnbId(rs.getLong("enbId"));
			asset.setHardwareVersion(rs.getString("hardware_version"));
			asset.setStatus(rs.getInt("status"));
			asset.setNodeType(rs.getInt("node_type"));
			asset.setLocationInfo(rs.getString("location_info"));
			asset.setProviderName(rs.getString("provider_name"));
			asset.setManufactureDate(rs.getLong("manufacture_date"));
			asset.setStartTime(rs.getLong("start_time"));
			asset.setStopTime(rs.getLong("stop_time"));
			asset.setLastServeTime(rs.getLong("last_serve_time"));
			asset.setRemark(rs.getString("remark"));
			return asset;
		}

	}

	private class AssetHistoryRowMapper implements RowMapper<EnbAssetHistory> {

		@Override
		public EnbAssetHistory mapRow(ResultSet rs, int index)
				throws SQLException {
			EnbAssetHistory assetHistory = new EnbAssetHistory();
			assetHistory.setId(rs.getLong("id"));
			assetHistory.setProductionSN(rs.getString("production_sn"));
			assetHistory.setEnbId(rs.getLong("enbId"));
			assetHistory.setHardwareVersion(rs.getString("hardware_version"));
			assetHistory.setNodeType(rs.getInt("node_type"));
			assetHistory.setLocationInfo(rs.getString("location_info"));
			assetHistory.setProviderName(rs.getString("provider_name"));
			assetHistory.setManufactureDate(rs.getLong("manufacture_date"));
			assetHistory.setStartTime(rs.getLong("start_time"));
			assetHistory.setStopTime(rs.getLong("stop_time"));
			assetHistory.setConfirmStopTime(rs.getLong("confirm_stop_time"));
			assetHistory.setConfirmUser(rs.getString("confirm_user"));
			assetHistory.setLastServeTime(rs.getLong("last_serve_time"));
			assetHistory.setRemark(rs.getString("remark"));
			return assetHistory;
		}
	}

	@Override
	public void deleteEnbAll(Long enbId) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ").append(ENB_ASSET_TABLE);
		sql.append(" where enbId=?");
		getJdbcTemplate().update(sql.toString(), enbId);
	}

}
