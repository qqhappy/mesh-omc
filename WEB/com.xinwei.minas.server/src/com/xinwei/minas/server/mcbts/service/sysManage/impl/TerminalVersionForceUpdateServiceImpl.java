/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.SystemProperty;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.server.core.conf.service.SystemPropertyService;
import com.xinwei.minas.server.mcbts.dao.sysManage.TerminalVersionForceUpdateDao;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.sysManage.TerminalVersionForceUpdateService;
import com.xinwei.minas.server.mcbts.task.McBtsUTUpgradeTaskManager;
import com.xinwei.minas.server.mcbts.utils.TypeUtil;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.ut.task.UTForceUpgradeTaskManager;
import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;

/**
 * 
 * 终端版本强制升级服务
 * 
 * @author tiance
 * 
 */

public class TerminalVersionForceUpdateServiceImpl implements
		TerminalVersionForceUpdateService {
	private Log log = LogFactory
			.getLog(TerminalVersionForceUpdateServiceImpl.class);

	// MZ模块
	public static final int MzType = 0x7c;
	// XXX: sys_properties中的列
	private static final String CATEGORY = "platform";
	private static final String PROPERTY = "terminal_version_force_update";

	private TerminalVersionForceUpdateDao tvForceUpdateDao;
	private McBtsBizProxy mcBtsBizProxy;
	private SystemPropertyService systemPropertyService;
	// private McBtsUTRegisterTaskManager mcBtsUTRegisterTaskManager;
	private UTForceUpgradeTaskManager utForceUpgradeTaskManager;
	// 升级进度
	private McBtsUTUpgradeTaskManager mcbtsUTUpgradeTaskManager;

	public void setTvForceUpdateDao(
			TerminalVersionForceUpdateDao tvForceUpdateDao) {
		this.tvForceUpdateDao = tvForceUpdateDao;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	public void setUtForceUpgradeTaskManager(
			UTForceUpgradeTaskManager utForceUpgradeTaskManager) {
		this.utForceUpgradeTaskManager = utForceUpgradeTaskManager;
	}

	public void setMcbtsUTUpgradeTaskManager(
			McBtsUTUpgradeTaskManager mcbtsUTUpgradeTaskManager) {
		this.mcbtsUTUpgradeTaskManager = mcbtsUTUpgradeTaskManager;
	}

	/**
	 * 从数据库查询所有强制升级的规则
	 * 
	 * @param btsId
	 * @return List<TerminalVersion>
	 */
	@Override
	public List<TerminalVersion> queryList() {
		return tvForceUpdateDao.queryList();
	}

	/**
	 * 从数据库查询所有强制升级的规则
	 * 
	 * @param btsId
	 * @return boolean
	 */
	@Override
	public boolean getSwitchStatus() {
		systemPropertyService = AppContext.getCtx().getBean(
				SystemPropertyService.class);
		SystemProperty sp = systemPropertyService.getProperty(CATEGORY, null,
				PROPERTY);
		if (sp.getValue().equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}

	/**
	 * 配置终端版本强制升级客户端配置
	 * 
	 * @param status
	 * @param ruleList
	 */
	@Override
	public void config(boolean status, List<TerminalVersion> ruleList) {
		String value = status ? "true" : "false";
		systemPropertyService = AppContext.getCtx().getBean(
				SystemPropertyService.class);
		systemPropertyService.setProperty(CATEGORY, null, PROPERTY, value);

		// 更新终端注册任务管理器中的开关和强制升级列表
		utForceUpgradeTaskManager.setSwitch(status);

		// 更新数据库
		tvForceUpdateDao.update(ruleList);
		utForceUpgradeTaskManager.setRules(ruleList);

	}

	/**
	 * 升级终端请求配置
	 * 
	 * @param eid
	 * @param tv
	 */
	@Override
	public void upgradeConfig(Long moId, String eid, TerminalVersion tv)
			throws Exception {
		GenericBizData genericBizData;
		if (TypeUtil.getModulType(tv.getTypeId().intValue()) == MzType) {
			// TODO: MZ升级, 目前底层还不支持
			genericBizData = new GenericBizData("utMzForceUpdate");

		} else {
			genericBizData = new GenericBizData("utForceUpdate");
			genericBizData.addProperty(new GenericBizProperty("eid", eid));
		}
		// 加入文件长度和文件名属性
		genericBizData.addProperty(new GenericBizProperty("fileName", tv
				.getFileName()));

		genericBizData.addProperty(new GenericBizProperty("modelType", TypeUtil
				.getModulType(tv.getTypeId().intValue())));

		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		McBtsUTUpgradeTaskManager.getInstance().setUTProgress(bts.getBtsId(), eid, "0");
		try {
			mcBtsBizProxy.config(moId, genericBizData);
		} catch (UnsupportedOperationException e) {
			log.error("Fail to upgrade bootloader.", e);
			McBtsUTUpgradeTaskManager.getInstance().setUTProgress(bts.getBtsId(), eid, null);
		} catch (Exception e) {
			log.error("Fail to upgrade terminal version.", e);
			McBtsUTUpgradeTaskManager.getInstance().setUTProgress(bts.getBtsId(), eid, null);
			throw e;
		}
	}

	/**
	 * 获取终端升级进度
	 * 
	 * @param utList
	 * @return Map<pid, 进度>
	 */
	@Override
	public Map<String, String> getUTProgress(List<UserTerminal> utList) {
		return mcbtsUTUpgradeTaskManager.getUTProgress(utList);
	}

	/**
	 * BootLoader升级配置
	 * 
	 * @param moId
	 *            基站moid
	 * @param eid
	 *            终端的pid(eid)
	 * @param tv
	 *            目标软件版本
	 */
	@Override
	public void bootloaderUpgrade(Long moId, String eid, TerminalVersion tv)
			throws Exception {

		GenericBizData genericBizData = new GenericBizData("bootloaderUpgrade");

		genericBizData.addProperty(new GenericBizProperty("eid", eid));
		// 加入文件长度和文件名属性
		genericBizData.addProperty(new GenericBizProperty("fileName", tv
				.getFileName()));

		try {
			mcBtsBizProxy.config(moId, genericBizData);
		} catch (UnsupportedOperationException e) {
			log.error("Fail to upgrade bootloader.", e);
		} catch (Exception e) {
			log.error("Fail to upgrade bootloader.", e);
			throw e;
		}
	}

}
