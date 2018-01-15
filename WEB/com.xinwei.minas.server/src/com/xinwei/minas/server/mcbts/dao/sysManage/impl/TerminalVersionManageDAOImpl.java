/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-18	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.mcbts.core.model.sysManage.TDLHistoryKey;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersionType;
import com.xinwei.minas.mcbts.core.model.sysManage.UTCodeDownloadTask;
import com.xinwei.minas.server.mcbts.dao.sysManage.TerminalVersionManageDAO;

/**
 * 
 * �ն˰汾����Dao��
 * 
 * @author tiance
 * 
 */

public class TerminalVersionManageDAOImpl extends JdbcDaoSupport implements
		TerminalVersionManageDAO {

	private String platformLang;

	private String platformDesc;

	public void setPlatformDesc(String platformDesc) {
		this.platformDesc = "desc_" + platformDesc;
	}

	public void setPlatformLang(String platformLang) {
		this.platformLang = "type_name_" + platformLang;

	}

	/**
	 * ��ȡ�������ϵ������ն˰汾���б�
	 * 
	 */
	@Override
	public List<TerminalVersion> queryAll() {
		String querySql = "select tv.typeId, tv.version, tv.fileName, tv.fileType, "
				+ "tvt."
				+ platformLang
				+ " type_name from terminal_version tv, terminal_version_type "
				+ "tvt where tv.typeId = tvt.id";
		return getJdbcTemplate().query(querySql, new TVMRowMapper());
	}

	/**
	 * ����typeId��ȡĳ���ն˵����а汾
	 * 
	 */
	@Override
	public List<TerminalVersion> queryByTypeId(Integer typeId) {
		String querySql = "select tv.typeId, tv.version, tv.fileName, tv.fileType, "
				+ "tvt."
				+ platformLang
				+ " type_name from terminal_version tv, terminal_version_type "
				+ "tvt where tv.typeId = tvt.id and tv.typeId = ?";

		return getJdbcTemplate().query(querySql, new Object[] { typeId },
				new TVMRowMapper());
	}

	/**
	 * ����typeId��ѯ�ն˰汾
	 */
	@Override
	public TerminalVersion queryByTypeIdAndVersion(Integer typeId,
			String version, int fileType) {
		String querySql = "select tv.typeId, tv.version, tv.fileName, tv.fileType, "
				+ "tvt."
				+ platformLang
				+ " type_name from terminal_version tv, terminal_version_type "
				+ "tvt where tv.typeId = tvt.id and tv.typeId = ? and tv.version = ? and tv.fileType = ?";
		final List<TerminalVersion> tvs = getJdbcTemplate().query(querySql,
				new Object[] { typeId, version, fileType }, new TVMRowMapper());

		if (tvs == null || tvs.size() == 0)
			return null;
		return tvs.get(0);
	}

	/**
	 * ������������������ն˰汾
	 * 
	 */
	@Override
	public int add(TerminalVersion terminalVersion) {
		String insertSql = "insert into terminal_version(typeId, version, fileName, fileType) values (?, ?, ?, ?)";

		return getJdbcTemplate().update(
				insertSql,
				new Object[] { terminalVersion.getTypeId(),
						terminalVersion.getVersion(),
						terminalVersion.getFileName(),
						terminalVersion.getFileType() });
	}

	/**
	 * �ӷ�����ɾ���ն˰汾
	 * 
	 */
	@Override
	public int delete(TerminalVersion terminalVersion) {
		String dateleSql = "delete from terminal_version where typeId = ? and version = ? and fileType = ?";
		return getJdbcTemplate().update(
				dateleSql,
				new Object[] { terminalVersion.getTypeId(),
						terminalVersion.getVersion(),
						terminalVersion.getFileType() });
	}

	/**
	 * �޸����ݿ��е�ĳ���ն˰汾
	 * 
	 */
	@Override
	public int update(TerminalVersion terminalVersion) {
		String updateSql = "update terminal_version set fileName = ?, fileType = ? where typeId = ? and version = ? and fileType = ?";
		return getJdbcTemplate().update(
				updateSql,
				new Object[] { terminalVersion.getFileName(),
						terminalVersion.getTypeId(),
						terminalVersion.getVersion(),
						terminalVersion.getFileType() });
	}

	/**
	 * ��terminal_version_type���в�ѯ�Ƿ��Ѿ�����typeId��Ӧ������ ���û��,��Ҫ�����û��ȼ�����Ӧ������
	 * 
	 */
	@Override
	public int queryForType(Integer typeId) {
		String querySql = "select count(1) from terminal_version_type where id = ?";
		return getJdbcTemplate().queryForInt(querySql, new Object[] { typeId });
	}

	private class TVMRowMapper implements RowMapper<TerminalVersion> {
		@Override
		public TerminalVersion mapRow(ResultSet rs, int arg1)
				throws SQLException {
			TerminalVersion tv = new TerminalVersion();
			if (rs != null) {
				tv.setTypeId(rs.getInt("typeId"));
				tv.setVersion(rs.getString("version"));
				tv.setFileName(rs.getString("fileName"));
				tv.setFileType(rs.getString("fileType"));
				tv.setType(rs.getString("type_name"));
			}
			return tv;
		}
	}

	/**
	 * ��ȡȫ��������ʷ
	 * 
	 * @return ����btsId, version�洢��MAP
	 */
	@Override
	public Map<TDLHistoryKey, UTCodeDownloadTask> queryHistroy(Long btsId) {

		String querySql = "select tvh.id, tvh.btsId, tvh.typeId, tvh.version, tvh.actionResult, "
				+ "tv.fileName, tv.fileType, tvh.startTime, tvh.endTime, tvt."
				+ platformLang
				+ " as typeName "
				+ "from terminal_version_dl_history tvh, terminal_version tv, terminal_version_type tvt "
				+ "where tvh.version = tv.version and tvh.typeId = tv.typeId and tv.typeId = tvt.id and tvh.fileType = tv.fileType "
				+ "and tvh.btsId = ? order by tvh.startTime desc";

		List<TVDlHistoryBean> result = getJdbcTemplate().query(querySql,
				new Object[] { btsId }, new RowMapper<TVDlHistoryBean>() {
					@Override
					public TVDlHistoryBean mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TVDlHistoryBean bean = new TVDlHistoryBean();
						bean.setId(rs.getLong("id"));
						bean.setBtsId(rs.getLong("btsId"));
						bean.setTypeId(rs.getInt("typeId"));
						bean.setTypeName(rs.getString("typeName"));
						bean.setVersion(rs.getString("version"));
						bean.setActionResult(rs.getInt("actionResult"));
						bean.setFileName(rs.getString("fileName"));
						bean.setFileType(rs.getString("fileType"));
						bean.setStartTime(rs.getTimestamp("startTime"));
						bean.setEndTime(rs.getTimestamp("endTime"));
						return bean;
					}
				});

		if (result == null || result.size() == 0)
			return null;

		Map<TDLHistoryKey, UTCodeDownloadTask> historyMap = new LinkedHashMap<TDLHistoryKey, UTCodeDownloadTask>();
		for (TVDlHistoryBean bean : result) {
			// ����key
			TDLHistoryKey key = new TDLHistoryKey(bean.getBtsId(),
					bean.getTypeId(), bean.getVersion(), Integer.parseInt(bean
							.getFileType()));
			TerminalVersion tv = new TerminalVersion();
			tv.setTypeId(bean.getTypeId());
			tv.setType(bean.getTypeName());
			tv.setVersion(bean.getVersion());
			tv.setFileName(bean.getFileName());
			tv.setFileType(bean.getFileType());
			UTCodeDownloadTask value = new UTCodeDownloadTask(tv);
			value.setId(bean.getId());
			value.setActionResult(bean.getActionResult());
			value.setStartTime(bean.getStartTime());
			value.setEndTime(bean.getEndTime());

			historyMap.put(key, value);
		}

		return historyMap;
	}

	/**
	 * ��ӵ���ʷ��¼��
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 */
	@Override
	public int insertToHistory(Long btsId, UTCodeDownloadTask task) {
		String insertSql = "insert into terminal_version_dl_history(id, btsId, "
				+ "typeId, version, fileType, actionResult, startTime, endTime) values (?,?,?,?,?,?,?,?)";

		TerminalVersion tv = task.getTerminalVersion();

		Timestamp startDateTime = null;
		Timestamp endDateTime = null;

		if (task.getStartTime() != null)
			startDateTime = new Timestamp(task.getStartTime().getTime());
		if (task.getEndTime() != null)
			endDateTime = new Timestamp(task.getEndTime().getTime());

		return getJdbcTemplate().update(
				insertSql,
				new Object[] { task.getId(), btsId, tv.getTypeId(),
						tv.getVersion(), Integer.parseInt(tv.getFileType()),
						task.getActionResult(), startDateTime, endDateTime });
	}

	/**
	 * ����ʷ��¼��ɾ��
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 */
	@Override
	public int deleteFromHistory(Long btsId, UTCodeDownloadTask task) {
		String deleteSql = "delete from terminal_version_dl_history where btsId = ? and typeId = ? and version = ? and fileType = ?";

		TerminalVersion tv = task.getTerminalVersion();

		return getJdbcTemplate().update(
				deleteSql,
				new Object[] { btsId, tv.getTypeId(), tv.getVersion(),
						Integer.parseInt(tv.getFileType()) });
	}

	/**
	 * ɾ��ĳ����Ԫ��������ʷ��¼
	 * 
	 * @param btsId
	 * @return
	 */
	@Override
	public int deleteAllFromHistory(Long btsId) {
		String deleteSql = "delete from terminal_version_dl_history where btsId = ?";
		return getJdbcTemplate().update(deleteSql, new Object[] { btsId });
	}

	/**
	 * �޸�ĳ����ʷ��¼
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 */
	@Override
	public int updateHistory(Long btsId, UTCodeDownloadTask task) {
		String updateSql = "update terminal_version_dl_history set actionResult = ?, startTime = ?, "
				+ "endTime = ? where btsId = ? and typeId = ? and version = ? and fileType = ?";

		TerminalVersion tv = task.getTerminalVersion();

		Timestamp startDateTime = null;
		Timestamp endDateTime = null;

		if (task.getStartTime() != null)
			startDateTime = new Timestamp(task.getStartTime().getTime());
		if (task.getEndTime() != null)
			endDateTime = new Timestamp(task.getEndTime().getTime());

		return getJdbcTemplate().update(
				updateSql,
				new Object[] { task.getActionResult(), startDateTime,
						endDateTime, btsId, tv.getTypeId(), tv.getVersion(),
						Integer.parseInt(tv.getFileType()) });
	}

	/**
	 * ͨ��TDLHistoryKey����ĳһ��history��¼
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public UTCodeDownloadTask queryHistoryByKey(TDLHistoryKey key) {
		String querySql = "select tvh.id, tvh.btsId, tvh.typeId, tvh.version, "
				+ "tvh.actionResult, tv.fileName, tv.fileType, tvh.startTime, "
				+ "tvh.endTime from terminal_version_dl_history tvh, terminal_version tv "
				+ "where tvh.version = tv.version and tvh.typeId = tv.typeId "
				+ "and tvh.btsId = ? and tvh.typeId = ? and tvh.version = ? and "
				+ "tvh.fileType = ? and tvh.fileType = tv.fileType";

		List<TVDlHistoryBean> result = getJdbcTemplate().query(
				querySql,
				new Object[] { key.getBtsId(), key.getTypeId(),
						key.getVersion(), key.getFileType() },
				new RowMapper<TVDlHistoryBean>() {
					@Override
					public TVDlHistoryBean mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TVDlHistoryBean bean = new TVDlHistoryBean();
						bean.setId(rs.getLong("id"));
						bean.setBtsId(rs.getLong("btsId"));
						bean.setTypeId(rs.getInt("typeId"));
						bean.setVersion(rs.getString("version"));
						bean.setActionResult(rs.getInt("actionResult"));
						bean.setFileName(rs.getString("fileName"));
						bean.setFileType(rs.getString("fileType"));
						bean.setStartTime(rs.getTimestamp("startTime"));
						bean.setEndTime(rs.getTimestamp("endTime"));
						return bean;
					}
				});
		if (result == null || result.size() == 0)
			return null;

		TVDlHistoryBean bean = result.get(0);

		TerminalVersion tv = new TerminalVersion();

		tv.setTypeId(bean.getTypeId());
		tv.setVersion(bean.getVersion());
		tv.setFileName(bean.getFileName());
		tv.setFileType(bean.getFileType());

		UTCodeDownloadTask task = new UTCodeDownloadTask(tv);
		task.setId(bean.getId());
		task.setActionResult(bean.getActionResult());
		task.setStartTime(bean.getStartTime());
		task.setEndTime(bean.getEndTime());

		return task;
	}

	/**
	 * Ϊ�ӱ�terminal_version_dl_history��ȡֵ�ṩbean
	 * 
	 * @author tiance
	 * 
	 */
	private class TVDlHistoryBean {
		private Long id;
		private Long btsId;
		private Integer typeId;
		private String typeName;
		private String version;
		private Integer actionResult;
		private String fileName;
		private String fileType;
		private Date startTime;
		private Date endTime;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getBtsId() {
			return btsId;
		}

		public void setBtsId(Long btsId) {
			this.btsId = btsId;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public Integer getTypeId() {
			return typeId;
		}

		public void setTypeId(Integer typeId) {
			this.typeId = typeId;
		}

		public Integer getActionResult() {
			return actionResult;
		}

		public void setActionResult(Integer actionResult) {
			this.actionResult = actionResult;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFileType() {
			return fileType;
		}

		public void setFileType(String fileType) {
			this.fileType = fileType;
		}

		public Date getStartTime() {
			return startTime;
		}

		public void setStartTime(Date startTime) {
			this.startTime = startTime;
		}

		public Date getEndTime() {
			return endTime;
		}

		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}

	}

	// �±�Ϊ�ն���������
	/**
	 * ��ѯ�����ն˰汾����
	 */
	@Override
	public List<TerminalVersionType> queryAllType() {
		// select id,type_name_zh,type_name_en,desc_zh,desc_en from
		// terminal_version_type;
		String querySql = "select id," + platformLang + "," + platformDesc
				+ " from terminal_version_type where id != 100000 order by id";
		return getJdbcTemplate().query(querySql, new TVTMRowMapper());

	}

	/**
	 * �����ն˰汾����
	 */
	@Override
	public int addType(TerminalVersionType model) {
		// insert into terminal_version_type(id, type_name_zh,desc_zh) values
		// (?, ?, ?)
		String insertSql = "insert into terminal_version_type(id,"
				+ platformLang + "," + platformDesc + ") values (?, ?, ?)";

		return getJdbcTemplate()
				.update(insertSql,
						new Object[] { model.getIdx(), model.getType(),
								model.getDesc() });
	}

	/**
	 * �޸��ն˰汾����
	 * 
	 */
	@Override
	public int modifyType(TerminalVersionType model) {
		String updateSql = "update terminal_version_type set " + platformLang
				+ " = ?, " + platformDesc + "= ? where id = ?";
		return getJdbcTemplate().update(
				updateSql,
				new Object[] { model.getType(), model.getDesc(),
						((model.getIdx() + 1000) * 100) });
	}

	private class TVTMRowMapper implements RowMapper<TerminalVersionType> {
		@Override
		public TerminalVersionType mapRow(ResultSet rs, int arg1)
				throws SQLException {
			TerminalVersionType model = new TerminalVersionType();
			if (rs != null) {
				model.setIdx(rs.getInt("id") / 100 - 1000);
				model.setType(rs.getString(platformLang));
				model.setDesc(rs.getString(platformDesc));
			}
			return model;
		}
	}

	@Override
	public int deleteType(int id) {
		// �����Modem������ɾ��
		String dateleSql = "delete from terminal_version_type where id = ?";
		return getJdbcTemplate().update(dateleSql,
				new Object[] { ((id + 1000) * 100) });
	}
}
