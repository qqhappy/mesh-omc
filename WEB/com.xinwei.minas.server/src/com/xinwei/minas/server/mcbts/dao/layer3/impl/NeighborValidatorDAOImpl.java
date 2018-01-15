/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-18	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.server.mcbts.dao.layer3.NeighborValidatorDAO;

/**
 * 
 * 邻接表校验Dao类
 * 
 * @author tiance
 * 
 */

public class NeighborValidatorDAOImpl extends JdbcDaoSupport implements
		NeighborValidatorDAO {

	/**
	 * 查询所有基站邻接表个数信息
	 */
	@Override
	public Map<Integer, String> queryNeighborCounts() {

		String querySql = "select neighbor.moId, bts.btsId, count(neighbor.moId) "
				+ "from mcbts_neighbour neighbor, mcbts_basic bts "
				+ "where neighbor.moId = bts.moId "
				+ "group by neighbor.moId order by count(neighbor.moId) desc ";

		List<NeighborCountBean> result = getJdbcTemplate().query(querySql,
				new Object[] {}, new RowMapper<NeighborCountBean>() {
					@Override
					public NeighborCountBean mapRow(ResultSet rs, int arg1)
							throws SQLException {
						NeighborCountBean bean = new NeighborCountBean();
						bean.setMoId(rs.getLong("moId"));
						bean.setBtsId(rs.getLong("btsId"));
						bean.setCount(rs.getInt("count(neighbor.moId)"));
						return bean;
					}
				});

		if (result == null || result.size() == 0) {
			// 返回空Map
			return new LinkedHashMap<Integer, String>();
		}

		Map<Integer, String> countMap = new LinkedHashMap<Integer, String>();
		for (NeighborCountBean bean : result) {

			Integer count = bean.getCount();
			if (count != null && !countMap.containsKey(count)) {
				countMap.put(count, "");
			}
			String btsInfo = countMap.get(count);
			// moId信息字符串
			if (btsInfo == null) {
				btsInfo = "(" + bean.getMoId() + ")";
			} else {
				btsInfo += "(" + bean.getMoId() + ")";
			}
			countMap.put(count, btsInfo);
		}

		return countMap;
	}

	/**
	 * 查询所有基站附加邻接表个数信息
	 */
	@Override
	public Map<Integer, String> queryAppendNeighborCounts() {
		String querySql = "select neighbor.moId, bts.btsId, count(neighbor.moId) "
				+ "from mcbts_append_neighbor neighbor, mcbts_basic bts "
				+ "where neighbor.moId = bts.moId "
				+ "group by neighbor.moId order by count(neighbor.moId) desc ";

		List<NeighborCountBean> result = getJdbcTemplate().query(querySql,
				new Object[] {}, new RowMapper<NeighborCountBean>() {
					@Override
					public NeighborCountBean mapRow(ResultSet rs, int arg1)
							throws SQLException {
						NeighborCountBean bean = new NeighborCountBean();
						bean.setMoId(rs.getLong("moId"));
						bean.setBtsId(rs.getLong("btsId"));
						bean.setCount(rs.getInt("count(neighbor.moId)"));
						return bean;
					}
				});

		if (result == null || result.size() == 0) {
			// 返回空Map
			return new LinkedHashMap<Integer, String>();
		}

		Map<Integer, String> countMap = new LinkedHashMap<Integer, String>();
		for (NeighborCountBean bean : result) {

			Integer count = bean.getCount();
			if (count != null && !countMap.containsKey(count)) {
				countMap.put(count, "");
			}
			String btsInfo = countMap.get(count);
			// moId信息字符串
			if (btsInfo == null) {
				btsInfo = "(" + bean.getMoId() + ")";
			} else {
				btsInfo += "(" + bean.getMoId() + ")";
			}
			countMap.put(count, btsInfo);
		}

		return countMap;
	}

	/**
	 * 查询所有基站频点及前导序列号信息
	 */
	@Override
	public Map<String, Map<Integer, String>> queryBtsFreqSeqInfo() {

		String querySql = "select bts.moId, bts.btsId, bts.btsFreqType, rf.freqOffset, air.sequenceId "
				+ "from mcbts_rfconfig rf, mcbts_airlink air, mcbts_basic bts "
				+ "where rf.moId = bts.moId and air.moId = bts.moId "
				+ "order by bts.btsFreqType, rf.freqOffset, air.sequenceId ";

		List<BtsFreqSeqBean> result = getJdbcTemplate().query(querySql,
				new Object[] {}, new RowMapper<BtsFreqSeqBean>() {
					@Override
					public BtsFreqSeqBean mapRow(ResultSet rs, int arg1)
							throws SQLException {
						BtsFreqSeqBean bean = new BtsFreqSeqBean();
						bean.setMoId(rs.getLong("moId"));
						bean.setBtsId(rs.getLong("btsId"));
						bean.setFreqType(rs.getInt("btsFreqType"));
						bean.setFreqOffset(rs.getInt("freqOffset"));
						bean.setSequenceId(rs.getInt("sequenceId"));
						return bean;
					}
				});

		if (result == null || result.size() == 0) {
			// 返回空Map
			return new LinkedHashMap<String, Map<Integer, String>>();
		}

		Map<String, Map<Integer, String>> freqSeqMap = new LinkedHashMap<String, Map<Integer, String>>();
		for (BtsFreqSeqBean bean : result) {

			Integer freqType = bean.getFreqType();
			Integer freqOffset = bean.getFreqOffset();
			String key = freqType + "#" + freqOffset;
			if (!freqSeqMap.containsKey(key)) {
				freqSeqMap.put(key, new LinkedHashMap<Integer, String>());
			}
			Map<Integer, String> btsSeqInfo = freqSeqMap.get(key);
			Integer seqId = bean.getSequenceId();
			if (seqId == null) {
				continue;
			}
			if (!btsSeqInfo.containsKey(seqId)) {
				btsSeqInfo.put(seqId, "");
			}
			String btsInfo = btsSeqInfo.get(seqId);
			// moId信息字符串
			if (btsInfo == null) {
				btsInfo = "(" + bean.getMoId() + ")";
			} else {
				btsInfo += "(" + bean.getMoId() + ")";
			}
			btsSeqInfo.put(seqId, btsInfo);
		}

		return freqSeqMap;
	}
	
	@Override
	public Map<String, ArrayList<Long>> querySameFreqSeqInfo() {

		String querySql = "select bts.moId, bts.btsId, bts.btsFreqType, rf.freqOffset, air.sequenceId "
				+ "from mcbts_rfconfig rf, mcbts_airlink air, mcbts_basic bts "
				+ "where rf.moId = bts.moId and air.moId = bts.moId "
				+ "order by bts.btsFreqType, rf.freqOffset, air.sequenceId ";

		List<BtsFreqSeqBean> result = getJdbcTemplate().query(querySql,
				new Object[] {}, new RowMapper<BtsFreqSeqBean>() {
					@Override
					public BtsFreqSeqBean mapRow(ResultSet rs, int arg1)
							throws SQLException {
						BtsFreqSeqBean bean = new BtsFreqSeqBean();
						bean.setMoId(rs.getLong("moId"));
						bean.setBtsId(rs.getLong("btsId"));
						bean.setFreqType(rs.getInt("btsFreqType"));
						bean.setFreqOffset(rs.getInt("freqOffset"));
						bean.setSequenceId(rs.getInt("sequenceId"));
						return bean;
					}
				});
		
		Map<String, ArrayList<Long>> freqSeqMap = new HashMap<String, ArrayList<Long>>();
		
		if (result == null || result.size() == 0) {
			return freqSeqMap;
		}
		
		for (BtsFreqSeqBean bean : result) {
			Integer freqType = bean.getFreqType();
			Integer freqOffset = bean.getFreqOffset();
			Integer seqId = bean.getSequenceId();
			//频点类型+频点偏移量+前导序列号作为key
			String key = freqType + "#" + freqOffset + "#" + seqId;
			ArrayList<Long> sameFreqSeqList = freqSeqMap.get(key);
			if (sameFreqSeqList == null) {
				sameFreqSeqList = new ArrayList<Long>();
			}
			sameFreqSeqList.add(bean.getMoId());
			freqSeqMap.put(key, sameFreqSeqList);
		}
		
		return freqSeqMap;
		
	}
	

	/**
	 * 为获取邻接表个数值提供bean
	 */
	private class NeighborCountBean {
		private Long moId;
		private Long btsId;
		private Integer count;

		/**
		 * @return the btsId
		 */
		public Long getBtsId() {
			return btsId;
		}

		/**
		 * @param btsId
		 *            the btsId to set
		 */
		public void setBtsId(Long btsId) {
			this.btsId = btsId;
		}

		/**
		 * @return the moId
		 */
		public Long getMoId() {
			return moId;
		}

		/**
		 * @param moId
		 *            the moId to set
		 */
		public void setMoId(Long moId) {
			this.moId = moId;
		}

		/**
		 * @return the count
		 */
		public Integer getCount() {
			return count;
		}

		/**
		 * @param count
		 *            the count to set
		 */
		public void setCount(Integer count) {
			this.count = count;
		}
	}

	/**
	 * 为获取邻接表个数值提供bean
	 */
	private class BtsFreqSeqBean {
		private Long moId;
		private Long btsId;
		private Integer freqType;
		private Integer freqOffset;
		private Integer sequenceId;

		/**
		 * @return the btsId
		 */
		public Long getBtsId() {
			return btsId;
		}

		/**
		 * @param btsId
		 *            the btsId to set
		 */
		public void setBtsId(Long btsId) {
			this.btsId = btsId;
		}

		/**
		 * @return the moId
		 */
		public Long getMoId() {
			return moId;
		}

		/**
		 * @param moId
		 *            the moId to set
		 */
		public void setMoId(Long moId) {
			this.moId = moId;
		}

		/**
		 * @return the freqType
		 */
		public Integer getFreqType() {
			return freqType;
		}

		/**
		 * @param freqType
		 *            the freqType to set
		 */
		public void setFreqType(Integer freqType) {
			this.freqType = freqType;
		}

		/**
		 * @return the freqOffset
		 */
		public Integer getFreqOffset() {
			return freqOffset;
		}

		/**
		 * @param freqOffset
		 *            the freqOffset to set
		 */
		public void setFreqOffset(Integer freqOffset) {
			this.freqOffset = freqOffset;
		}

		/**
		 * @return the sequenceId
		 */
		public Integer getSequenceId() {
			return sequenceId;
		}

		/**
		 * @param sequenceId
		 *            the sequenceId to set
		 */
		public void setSequenceId(Integer sequenceId) {
			this.sequenceId = sequenceId;
		}
	}

}
