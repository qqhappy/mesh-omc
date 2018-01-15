/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-30	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.server.hlr.net.udp.HlrUdpMessage;
import com.xinwei.minas.server.mcbts.model.message.McBtsUTRegisterNotify;
import com.xinwei.minas.server.mcbts.service.McBtsBasicService;
import com.xinwei.minas.server.mcbts.service.sysManage.TerminalVersionForceUpdateService;
import com.xinwei.minas.server.mcbts.service.sysManage.impl.TerminalVersionForceUpdateServiceImpl;
import com.xinwei.minas.server.mcbts.utils.McBtsUtils;
import com.xinwei.minas.server.mcbts.utils.TypeUtil;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * 终端强制升级任务管理器
 * 
 * 
 * @author tiance
 * 
 */

public class UTForceUpgradeTaskManager {
	private Log log = LogFactory.getLog(UTForceUpgradeTaskManager.class);

	private TerminalVersionForceUpdateService service;

	private McBtsBasicService mcBtsService;

	// 强制升级规则
	private Set<TerminalVersion> rules = null;

	// 强制升级开关
	private boolean switcher = false;

	// 升级的目标版本
	private TerminalVersion upgradeVersion;

	private static final UTForceUpgradeTaskManager instance = new UTForceUpgradeTaskManager();

//	private UTForceUpgradeTaskManager() {
//		service = AppContext.getCtx().getBean(
//				TerminalVersionForceUpdateServiceImpl.class);
//		mcBtsService = AppContext.getCtx().getBean(McBtsBasicService.class);
//	}
	

	public static UTForceUpgradeTaskManager getInstance() {
		return instance;
	}

	public void setService(TerminalVersionForceUpdateService service) {
		this.service = service;
	}

	public void setMcBtsService(McBtsBasicService mcBtsService) {
		this.mcBtsService = mcBtsService;
	}

	/**
	 * 处理终端强制升级通知
	 * 
	 * @param message
	 */
	public void handleUTRegisterNotify(HlrUdpMessage message) {
		byte[] content = message.getContent();

		UserTerminal ut = new UserTerminal();
		decodeUtMessage(ut, content);

		// 仅对开机注册和周期注册进行升级处理,其它类型不处理
		if (!(ut.getRegType() == UserTerminal.REG_TYPE_ON_OPEN || ut
				.getRegType() == UserTerminal.REG_TYPE_PERIOD))
			return;

		// 如果关闭强制升级,不处理
		if (!isStartForceUpgrade())
			return;

		try {
			boolean toUpgrade = verifyRegulation(ut);

			if (!toUpgrade)
				return;

			log.debug("Force upgrade on bts: " + ut.getBtsId() + ", on ut:"
					+ ut.getPid());

			McBts mcBts = mcBtsService.queryByBtsId(ut.getBtsId());

			service.upgradeConfig(mcBts.getMoId(), ut.getPid(), upgradeVersion);

		} catch (Exception e) {
			log.error("Error getting mcbts, upgrade failed", e);
			return;
		}

	}

	private void decodeUtMessage(UserTerminal ut, byte[] content) {
		int offset = 0;
		ut.setUid(ByteUtils.toHexString(content, offset, 4));
		offset += 4;

		ut.setPid(ByteUtils.toHexString(content, offset, 4));
		offset += 4;

		ut.setBtsId(ByteUtils.toLong(content, offset, 4));
		offset += 4;

		ut.setSagId(ByteUtils.toLong(content, offset, 4));
		offset += 4;

		ut.setRegType(ByteUtils.toInt(content, offset, 1));
		offset++;

		String regTimeStr = ByteUtils.toString(content, offset, 14, "US-ASCII");
		offset += 14;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			ut.setRegTime(dateFormatter.parse(regTimeStr));
		} catch (ParseException e) {
			log.error(e);
		}

		// XXX 转换类型不明确
		ut.setHwType(ByteUtils.toHexString(content, offset, 2).trim());
		offset += 2;

		ut.setSwType(ByteUtils.toInt(content, offset, 1));
		offset++;

		// 保留字段一个字节
		offset++;

		ut.setActiveSwVersion(ByteUtils.toVersion(content, offset, 4));
		offset += 4;

		ut.setStandbySwVersion(ByteUtils.toVersion(content, offset, 4));
		offset += 4;

		ut.setHwVersion(ByteUtils.toString(content, offset, 16, "US-ASCII"));
	}

	/**
	 * 判断是否开启强制升级
	 * 
	 * @return
	 */
	private boolean isStartForceUpgrade() {
		
		if (service == null) {
			return false;
		}
		
		if (rules == null) {
			switcher = service.getSwitchStatus();
			List<TerminalVersion> list = service.queryList();
			if (list == null)
				rules = new HashSet<TerminalVersion>();
			else
				rules = new HashSet<TerminalVersion>(list);
		}

		if (!switcher || rules.isEmpty()) {
			return false;
		}

		return true;
	}

	/**
	 * 通过通知判断是否发送升级配置, true为配置, false为不配置
	 * 
	 * @param ut
	 * @return
	 */
	private boolean verifyRegulation(UserTerminal ut) {
		String hwType = ut.getDecimalHwType();
		int swType = ut.getSwType();
		//FIXME zxz 没有验证终端bootloader升级
		for (TerminalVersion tv : rules) {
			int typeId = tv.getTypeId();
			if (!hwType.equals(String.valueOf(TypeUtil.getModulType(typeId)))
					|| swType != TypeUtil.getSoftwareType(typeId)) {
				continue;
			}

			int updateDependency = tv.getUpdateDependency();
			String[] compareVer = null;
			if (updateDependency == TerminalVersion.COMPARE_ACTIVE) {
				compareVer = new String[1];
				compareVer[0] = ut.getActiveSwVersion();
			} else if (updateDependency == TerminalVersion.COMPARE_STANDBY) {
				compareVer = new String[1];
				compareVer[0] = ut.getStandbySwVersion();
			} else if (updateDependency == TerminalVersion.COMPARE_BOTH) {
				compareVer = new String[2];
				compareVer[0] = ut.getActiveSwVersion();
				compareVer[1] = ut.getStandbySwVersion();
			}

			int compareResult = versionCompare(tv.getVersion(), compareVer);

			int updateCondition = tv.getUpdateCondition();
			if ((updateCondition == TerminalVersion.COMPARE_METHOD_LESS && compareResult == 1)
					|| (updateCondition == TerminalVersion.COMPARE_METHOD_NOT_EQUALS && compareResult != 0)) {
				upgradeVersion = tv;
				return true;
			}
		}
		return false;
	}

	/**
	 * 将终端的升级版本 与终端当前版本进行比较
	 * 
	 * @param targetVer
	 * @param compareVer
	 * @return
	 */
	private int versionCompare(String targetVer, String[] compareVer) {
		String[] tarVer = targetVer.split("\\.");
		for (String currentVer : compareVer) {
			String[] curVer = currentVer.split("\\.");
			for (int i = 0; i < tarVer.length; i++) {
				int tarItem = Integer.valueOf(tarVer[i]);
				int curItem = Integer.valueOf(curVer[i]);
				if (tarItem < curItem) {
					return -1;
				} else if (tarItem == curItem) {
					continue;
				} else {
					return 1;
				}
			}
		}
		return 0;
	}

	public void setSwitch(boolean switcher) {
		this.switcher = switcher;
	}

	public void setRules(List<TerminalVersion> rules) {
		if (this.rules == null)
			this.rules = new HashSet<TerminalVersion>();
		this.rules.clear();
		for (TerminalVersion tv : rules) {
			this.rules.add(tv);
		}
	}
}
