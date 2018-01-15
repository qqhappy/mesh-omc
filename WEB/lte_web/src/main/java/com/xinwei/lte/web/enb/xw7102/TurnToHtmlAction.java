package com.xinwei.lte.web.enb.xw7102;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.enb.cache.EnbVersionBizConfigCache;
import com.xinwei.lte.web.lte.action.AjaxHelper;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.system.action.web.WebConstants;

/**
 * 用于部分简单页面的跳转
 * 
 * @author zhangqiang
 * 
 */
public class TurnToHtmlAction extends ActionSupport {
	
	/**
	 * 核心网类�?
	 */
	@Value("${coreNetType}")
	private String coreNetType;
	//协议版本
	private List<String> protocolVersionList;

	/**
	 * 设备列表刷新间隔
	 */
	@Value("${alarmFreshInterval}")
	private String alarmFreshInterval;

	/**
	 * Mo编号，全局唯一，系统生�?
	 */
	private long moId;

	/**
	 * 基站ID
	 */
	private String enbHexId;

	/**
	 * 基站名称
	 */
	private String enbName;

	/**
	 * 基站类型
	 */
	private int enbType;

	/**
	 * 基站版本
	 */
	private String enbVersion;
	
	/**
	 * eNB类型映射�? 类型ID--类型名称
	 */
	private Map<Integer, String> enbTypeMap;

	/**
	 * 用户是否有权限新增管理员
	 */
	private int canAddAdm;

	/**
	 * 被修改密码的用户的名�?
	 */
	private String modifiedUserName;

	/**
	 * 登陆的用户名
	 */
	private String userName;

	/**
	 * 是否由告警跳转而来 0:�?1:�?
	 */
	private String fromAlarm;

	/**
	 * 角色ID
	 * 
	 */
	private int roleId;

	/**
	 * 跳转至配置管理左部菜�?
	 * 
	 * @return
	 */
	public String turnConfigManageLeft() {
		return SUCCESS;
	}

	/**
	 * 跳转至enb新增页面
	 * 
	 * @return
	 */
	public String turnAddSingleEnb() {

		enbTypeMap = EnbTypeDD.getSupportedTypeMap();
		

//
//		versionList = new ArrayList<String>(versionSet);
//		// 版本倒序排列
//		Collections.sort(versionList, new VersionComparator());

		return SUCCESS;
	}
	
	/**
	 * 根据基站类型获取基站版本列表
	 * @return
	 */
	public String queryEnbVersionListByType() {
		List<String> versionList = new LinkedList();
		// 获取支持的eNB协议版本列表
		Map<Integer, List<String>> typeVersionMap = EnbVersionBizConfigCache.getInstance()
				.getSupportedVersions();
		if (typeVersionMap != null && typeVersionMap.containsKey(enbType)) {
			versionList = typeVersionMap.get(enbType);
		}		
		JSONObject json = new JSONObject();
		json.put("versionList", versionList);
		// AJAX返回Json
		AjaxHelper.ajaxMethod(json.toString());
		return NONE;
	}

	/**
	 * 跳转至enb配置页面(todo)
	 * 
	 * @return
	 */
	public String turnEnbWebLmt() {
		return SUCCESS;
	}

	/**
	 * 跳转至enb配置左部菜单
	 * 
	 * @return
	 */
	public String turnEnbWebLmtLeft_xw7102() {
		try {
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					EnbBasicFacade.class);
			Enb enb = facade.queryByMoId(moId);
			enbType = enb.getEnbType();
			enbName = enb.getName();
		} catch (Exception e) {			
		}		
		String enbTypeName = EnbTypeDD.getTypeNameById(enbType).toLowerCase();
		return enbTypeName + "-biz-menu-" + enbVersion;
	}

	/**
	 * 跳转至告警左部菜�?
	 * 
	 * @return
	 */
	public String turnAlarmLeft() {
		return SUCCESS;
	}

	/**
	 * 跳转至当前告�?
	 * 
	 * @return
	 */
	public String turnCurrentAlarm() {
		return SUCCESS;
	}

	/**
	 * 跳转至单基站当前告警
	 * 
	 * @return
	 */
	public String turnSingleCurrentAlarm() {
		return SUCCESS;
	}

	/**
	 * 跳转至单基站当前告警
	 * 
	 * @return
	 */
	public String turnSingleHistoryAlarm() {
		return SUCCESS;
	}

	/**
	 * 跳转至历史告�?
	 * 
	 * @return
	 */
	public String turnHistoryAlarm() {
		return SUCCESS;
	}

	/**
	 * 跳转至版本管�?
	 * 
	 * @return
	 */
	public String turnVersion() {
		return SUCCESS;
	}

	/**
	 * 跳转至版本文件添�?
	 * 
	 * @return
	 */
	public String turnAddVersion() {
		return SUCCESS;
	}

	/**
	 * 跳转至一键恢�?
	 * 
	 * @return
	 */
	public String turnOneClickRestore() {
		return SUCCESS;
	}

	/**
	 * 跳转至自学习
	 * 
	 * @return
	 */
	public String turnSelfStudy() {
		return SUCCESS;
	}

	/**
	 * 跳转至整表配�?
	 * 
	 * @return
	 */
	public String turnCfg() {
		return SUCCESS;
	}

	/**
	 * 跳转至安全管理左部菜�?
	 * 
	 * @return
	 */
	public String turnSecuLeft() {
		return SUCCESS;
	}

	/**
	 * 跳转到新的登陆页�?
	 * 
	 * @return
	 */
	public String turnNewLogin() {
		return SUCCESS;
	}

	/**
	 * 跳往新增用户页面
	 * 
	 * @return
	 */
	public String turnAddUser() {
		return SUCCESS;
	}

	/**
	 * 跳往修改密码页面
	 * 
	 * @return
	 */
	public String turnModifyPassWord() {
		return SUCCESS;
	}

	/**
	 * 跳转至TCN1000设备页面
	 * 
	 * @return
	 */
	public String turnTcnDevice() {
		return SUCCESS;
	}

	/**
	 * 跳转至性能管理左部菜单
	 * 
	 * @return
	 */
	public String turnPropertyLeft() {
		return SUCCESS;
	}

	/**
	 * 跳转至开站参数导�?
	 * 
	 * @return
	 */
	public String turnExportActiveData() {
		return SUCCESS;
	}

	/**
	 * 跳转至批量开�?
	 * 
	 * @return
	 */
	public String turnActiveUser() {
		return SUCCESS;
	}

	/**
	 * 导入基站配置
	 * 
	 * @return
	 */
	public String turnImportEnbData() {
		return SUCCESS;
	}

	/**
	 * 导出基站配置
	 * 
	 * @return
	 */
	public String turnExportEnbData() {
		protocolVersionList=new ArrayList();
		String sessionId = ((LoginUser) ActionContext.getContext()
				.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
				.getSessionId();
		try {
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);
			List<Enb> enbList=facade.queryAllEnb();
			for(Enb enb:enbList){
				String protocolVersion=enb.getProtocolVersion();
				if(!protocolVersionList.contains(protocolVersion)){
					protocolVersionList.add(protocolVersion);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 跳转到网管与基站数据比较页面
	 * 
	 * @return
	 */
	public String turnCompareData() {
		return SUCCESS;
	}

	/**
	 * TOGO
	 * 
	 * @return
	 */
	public String toGo() {
		return SUCCESS;
	}

	class VersionComparator implements Comparator<String> {

		@Override
		public int compare(String ver1, String ver2) {
			return 0 - compareVersion(ver1, ver2);
		}

		private int compareVersion(String ver1, String ver2) {

			int compare = isVersionBlank(ver1, ver2);
			if (compare != 2) {
				return -1 * compare;
			}

			String[] v1 = ver1.split("\\.");
			String[] v2 = ver2.split("\\.");

			for (int i = 0; i < v1.length; i++) {
				int num1 = Integer.valueOf(v1[i]);
				int num2 = Integer.valueOf(v2[i]);

				if (num1 < num2) {
					return -1;
				}
				if (num1 > num2) {
					return 1;
				}
			}
			return 0;
		}

		private int isVersionBlank(String v1, String v2) {
			v1 = v1 == null || v1.equalsIgnoreCase("null") ? "" : v1;
			v2 = v2 == null || v2.equalsIgnoreCase("null") ? "" : v2;

			if (StringUtils.isBlank(v1) && StringUtils.isBlank(v2))
				return 0;
			if (StringUtils.isBlank(v1))
				return 1;
			if (StringUtils.isBlank(v2))
				return -1;

			return 2;
		}
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public String getEnbHexId() {
		return enbHexId;
	}

	public void setEnbHexId(String enbHexId) {
		this.enbHexId = enbHexId;
	}

	public String getEnbName() {
		return enbName;
	}

	public void setEnbName(String enbName) {
		this.enbName = enbName;
	}

	public int getEnbType() {
		return enbType;
	}

	public void setEnbType(int enbType) {
		this.enbType = enbType;
	}

	public void setEnbVersion(String enbVersion) {
		this.enbVersion = enbVersion;
	}

	public String getEnbVersion() {
		return enbVersion;
	}



	public int getCanAddAdm() {
		return canAddAdm;
	}

	public void setCanAddAdm(int canAddAdm) {
		this.canAddAdm = canAddAdm;
	}

	public String getModifiedUserName() {
		return modifiedUserName;
	}

	public void setModifiedUserName(String modifiedUserName) {
		this.modifiedUserName = modifiedUserName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFromAlarm() {
		return fromAlarm;
	}

	public void setFromAlarm(String fromAlarm) {
		this.fromAlarm = fromAlarm;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getCoreNetType() {
		return coreNetType;
	}

	public void setCoreNetType(String coreNetType) {
		this.coreNetType = coreNetType;
	}

	public String getAlarmFreshInterval() {
		return alarmFreshInterval;
	}

	public void setAlarmFreshInterval(String alarmFreshInterval) {
		this.alarmFreshInterval = alarmFreshInterval;
	}
	public Map<Integer, String> getEnbTypeMap() {
		return enbTypeMap;
	}
	public List<String> getProtocolVersionList() {
		return protocolVersionList;
	}

	public void setProtocolVersionList(List<String> protocolVersionList) {
		this.protocolVersionList = protocolVersionList;
	}
	
	
	
}
