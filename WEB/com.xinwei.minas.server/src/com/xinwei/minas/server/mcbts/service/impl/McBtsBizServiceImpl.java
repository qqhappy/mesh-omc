/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-12	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.core.conf.service.MoCache;
import com.xinwei.minas.server.mcbts.dao.McBtsBizDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsBizService;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.oamManage.UnsupportedMocCache;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * McBts业务服务接口实现
 * 
 * @author chenjunhua
 * 
 */

public class McBtsBizServiceImpl implements McBtsBizService {

	private Log log = LogFactory.getLog(McBtsBizServiceImpl.class);

	private McBtsBizDAO mcBtsBizDAO;

	private McBtsBizProxy mcBtsBizProxy;

	private McBtsBizServiceImpl() {
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	public void setMcBtsBizDAO(McBtsBizDAO mcBtsBizDAO) {
		this.mcBtsBizDAO = mcBtsBizDAO;
	}

	@Override
	public GenericBizData queryAllBy(Long moId, String bizName)
			throws Exception {
		String condition = "";
		Object[] conditionValue = new Object[0];
		return mcBtsBizDAO.queryAllBy(moId, bizName, condition, conditionValue);
	}

	@Override
	public void sendCommand(Long moId, GenericBizData genericBizData)
			throws Exception {
		// 获取MO的维护状态
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		if (bts != null && bts.isConfigurable()) {
			// 在线管理状态下需要向网元发送消息
			try {
				mcBtsBizProxy.config(moId, genericBizData);
			} catch (UnsupportedOperationException e) {
				log.error(OmpAppContext.getMessage("unsupported_biz_operation")
						+ ":" + genericBizData.getBizName());
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		} 
	}

	@Override
	public GenericBizData queryFromNE(Long moId, GenericBizData genericBizData)
			throws UnsupportedOperationException, Exception {
		return mcBtsBizProxy.query(moId, genericBizData);
	}

	@Override
	public void saveToDB(Long moId, GenericBizData genericBizData)
			throws Exception {
		// 更新数据库
		try {
			mcBtsBizDAO.addOrUpdate(moId, genericBizData);
		} catch (Exception e) {
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}

	}

	@Override
	public List<Map<String, Object>> queryEnumItem(String tableName,
			String sqlStatement) throws Exception {
		return mcBtsBizDAO.queryEnumItem(tableName, sqlStatement);
	}

	@Override
	public void config(Long moId, GenericBizData genericBizData)
			throws Exception {
		// 向网元发送消息
		this.sendCommand(moId, genericBizData);
		// 持久化数据
		this.saveToDB(moId, genericBizData);
	}

	@Override
	public Map<String, Map<Integer, Integer>> getStudyCache() throws Exception {
		return UnsupportedMocCache.getInstance().getBtsUnsupportedMocMap();
	}

}
