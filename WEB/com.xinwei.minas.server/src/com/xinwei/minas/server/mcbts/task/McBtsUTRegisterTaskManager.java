/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-27	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.server.mcbts.model.message.McBtsUTRegisterNotify;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.service.McBtsBasicService;
import com.xinwei.minas.server.mcbts.service.sysManage.TerminalVersionForceUpdateService;
import com.xinwei.minas.server.mcbts.utils.McBtsUtils;
import com.xinwei.minas.server.mcbts.utils.TypeUtil;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * �ն�ע�����������
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsUTRegisterTaskManager {
	private Log log = LogFactory.getLog(McBtsUTRegisterTaskManager.class);

	private boolean forceUpdateSwitch = false;
	// private List<TerminalVersion> forceUpdateRuleList = null;
	private TerminalVersion terminal_version;
	private TerminalVersionForceUpdateService tvForceUpdateService;
	private McBtsBasicService service;

	private Set<TerminalVersion> rules = null;

	private McBtsUTRegisterTaskManager() {
		// ��ȡ��վ��ϢService
		service = AppContext.getCtx().getBean(McBtsBasicService.class);

	}

	private static McBtsUTRegisterTaskManager instance = new McBtsUTRegisterTaskManager();

	public static McBtsUTRegisterTaskManager getInstance() {
		return instance;
	}

	public void handleUTRegisterNotify(McBtsMessage response) {
		// ��ȡǿ������Service
		tvForceUpdateService = AppContext.getCtx().getBean(
				TerminalVersionForceUpdateService.class);
		if (rules == null) {
			forceUpdateSwitch = tvForceUpdateService.getSwitchStatus();
			List<TerminalVersion> list = tvForceUpdateService.queryList();
			if (list == null)
				rules = new HashSet<TerminalVersion>();
			else
				rules = new HashSet<TerminalVersion>(list);
		}

		if (!forceUpdateSwitch || rules.isEmpty()) {
			return;
		}

		Long btsId = response.getBtsId();
		McBts mcBts = null;
		try {
			mcBts = service.queryByBtsId(btsId);
		} catch (Exception e) {
			log.error("��ȡ��վʧ��");
		}

		debug(btsId, "�յ�ע��֪ͨ.");
		McBtsUTRegisterNotify utRegisterNotify = new McBtsUTRegisterNotify(
				response.getContent());

		// ��֤�Ƿ���Ҫ����
		if (verifyRegulation(utRegisterNotify)) {
			// ����ǿ����������
			try {
				tvForceUpdateService.upgradeConfig(mcBts.getMoId(),
						String.valueOf(utRegisterNotify.getEid()),
						terminal_version);
			} catch (Exception e) {
				log.error(e.getMessage());
			}

		}
	}

	/**
	 * ͨ��֪ͨ�ж��Ƿ�����������, trueΪ����, falseΪ������
	 * 
	 * @param notify
	 * @return
	 */
	private boolean verifyRegulation(McBtsUTRegisterNotify notify) {

		int hardwareType = notify.getuTBaseInfo().getHardwareType().intValue();
		int softwareType = notify.getuTBaseInfo().getSoftwareType().intValue();

		for (TerminalVersion tv : rules) {
			int typeId = tv.getTypeId().intValue();
			// ardwaretype��software�ıȽ�,����ն˲�ƥ��,�ͼ�����һ��
			if (hardwareType != TypeUtil.getModulType(typeId)
					|| softwareType != TypeUtil.getSoftwareType(typeId))
				continue;

			// �ж��ǵ�ǰ�汾���Ǳ��ݰ汾,���߶�����
			String[] compareVer = null;
			int update_dependency = tv.getUpdateDependency();
			if (update_dependency == TerminalVersion.COMPARE_ACTIVE) {
				compareVer = new String[1];
				compareVer[0] = notify.getuTBaseInfo().getActiveSWVersion();
			} else if (update_dependency == TerminalVersion.COMPARE_STANDBY) {
				compareVer = new String[1];
				compareVer[0] = notify.getuTBaseInfo().getStandbySWVersion();
			} else if (update_dependency == TerminalVersion.COMPARE_BOTH) {
				compareVer = new String[2];
				compareVer[0] = notify.getuTBaseInfo().getActiveSWVersion();
				compareVer[1] = notify.getuTBaseInfo().getStandbySWVersion();
			}

			// V51 ������������ V52
			// for (String compareItem : compareVer) {
			// if (!checkversion(compareItem) && checkversion(tv.getVersion()))
			// return false;
			// }

			// ǿ�������汾���ڵ�ǰ1, ǿ�������汾���ڵ�ǰ0, ǿ�������汾С�ڵ�ǰ-1
			int compareResult = compare(tv.getVersion(), compareVer);

			int update_method = tv.getUpdateCondition();
			if ((update_method == TerminalVersion.COMPARE_METHOD_LESS && compareResult == 1)
					|| (update_method == TerminalVersion.COMPARE_METHOD_NOT_EQUALS && compareResult != 0)) {
				terminal_version = tv;
				return true;
			}
		}
		return false;
	}

	private void debug(Long btsId, String message) {
		McBtsUtils.log(btsId, "UTRegister", message);
	}

	/**
	 * �Ƚ����ݿ�汾�͵�ǰ�汾{������ݿ�汾<��ǰ�汾(���ݰ汾);����-1 ������ݿ�汾=��ǰ�汾(���ݰ汾);���� 0
	 * ������ݿ�汾>��ǰ�汾(���ݰ汾), ���� 1}
	 * 
	 * @param version
	 * @param compareVer
	 * @return
	 */
	private static int compare(String version, String[] compareVer) {
		String[] ver = version.split("\\.");
		for (String compareItem : compareVer) {

			String[] cver = compareItem.split("\\.");
			for (int i = 0; i < ver.length; i++) {
				int verItem = Integer.parseInt(ver[i]);
				int cverItem = Integer.parseInt(cver[i]);
				if (verItem < cverItem)
					return -1;
				else if (verItem == cverItem)
					continue;
				else
					return 1;
			}
		}
		return 0;
	}

	/**
	 * Ϊ��Web�ͻ��˲��ֵĴ������,���V5BTS�ṩ�Ľӿ� ��Ӧ�ɴ���FrontModule 155��
	 */
	public static boolean checkversion(String btssoftwarever) {
		if (btssoftwarever.equals("0.0.0.1"))
			return true;

		String ver = "1.5.0.0-";
		if (versioncheck_space(ver, btssoftwarever))
			return true;

		return false;
	}

	/**
	 * ����version��version��Χ���壬���ĳ���汾�Ƿ��ڹ涨�ķ�Χ
	 * 
	 * @param version_check_str
	 *            ��version��Χ����
	 * @param version
	 *            �� Ҫ���İ汾
	 * @return
	 */
	public static boolean versioncheck_space(String version_check_str,
			String version) {
		String tmp = version_check_str.replaceAll(";", ",");
		String ver_check_str_item[] = tmp.split(",");
		String ver;
		for (int i = 0; i < ver_check_str_item.length; i++) {
			ver = ver_check_str_item[i];
			if (ver.indexOf("-") == -1) {
				// equals or matches
				if (version.equals(ver) || version.matches(ver)) {
					return true;
				}
			} else if (ver.startsWith("-")) { // small than
				if (smallerthan(version, ver, true))
					return true;
			} else if (ver.endsWith("-")) { // large than
				if (largerthan(version, ver, true))
					return true;
			} else { // between
				String veritem[] = ver.split("-");
				if (largerthan(version, veritem[0], true)
						&& (smallerthan(version, veritem[1], false)))
					return true;
			}
		}
		return false;
	}

	/**
	 * �Ƚ�btssoftwarever�Ƿ�С�ڹ涨�汾
	 * 
	 * @param btssoftwarever
	 * @param ver
	 * @param isEquals
	 * @return
	 */
	private static boolean smallerthan(String btssoftwarever, String ver,
			boolean isEquals) {
		// large than
		ver = ver.replaceAll("-", "");
		ver = ver.replace('*', '0');
		String veritemOld[] = ver.split("\\.");
		String veritem[] = btssoftwarever.split("\\.");
		int valueToBeCheck;
		int valueOld;
		for (int k = 0; k < veritem.length; k++) {
			valueOld = Integer.parseInt(veritemOld[k]);
			valueToBeCheck = Integer.parseInt(veritem[k]);
			if (valueToBeCheck < valueOld) {
				// large than
				return true;
			} else if (valueToBeCheck > valueOld) {
				isEquals = false;
				break;
			}
		}
		if (isEquals)
			return true;
		else
			return false;
	}

	/**
	 * �Ƚ�btsversion�Ƿ���ڹ涨�汾
	 * 
	 * @param btssoftwarever
	 * @param ver
	 * @param isEquals
	 * @return
	 */
	private static boolean largerthan(String btssoftwarever, String ver,
			boolean isEquals) {
		// large than
		ver = ver.replaceAll("-", "");
		// >=1.*.*.* >=1.0.0.0
		// >=1.2.*.* >=1.2.0.0
		ver = ver.replace('*', '0');
		String veritemOld[] = ver.split("\\.");
		String veritem[] = btssoftwarever.split("\\.");
		int valueToBeCheck;
		int valueOld;
		for (int k = 0; k < veritem.length; k++) {
			valueOld = Integer.parseInt(veritemOld[k]);
			valueToBeCheck = Integer.parseInt(veritem[k]);
			if (valueToBeCheck > valueOld) {
				// large than
				return true;
			} else if (valueToBeCheck < valueOld) {
				isEquals = false;
				break;
			}
		}
		if (isEquals)
			return true;
		else
			return false;

	}

	public void setForceUpdateSwitch(boolean forceUpdateSwitch) {
		this.forceUpdateSwitch = forceUpdateSwitch;
	}

	public void setRules(List<TerminalVersion> rules) {
		this.rules = new HashSet<TerminalVersion>(rules);
	}

}
