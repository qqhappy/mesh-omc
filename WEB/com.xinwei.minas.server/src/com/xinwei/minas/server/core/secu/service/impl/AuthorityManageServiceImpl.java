/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-5	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinwei.minas.core.model.OperAction;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.OperObjectTypeDD;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.core.model.secu.OperSignature;
import com.xinwei.minas.core.model.secu.RoleType;
import com.xinwei.minas.core.model.secu.syslog.LogQueryCondition;
import com.xinwei.minas.core.model.secu.syslog.SystemLog;
import com.xinwei.minas.core.model.secu.syslog.SystemLogQueryResult;
import com.xinwei.minas.server.core.secu.dao.AuthorityManageDao;
import com.xinwei.minas.server.core.secu.service.AuthorityManageService;
import com.xinwei.minas.server.core.secu.service.LoginUserCache;
import com.xinwei.minas.server.core.secu.service.UserPrivilegeCache;
import com.xinwei.minas.sxc.core.model.SxcBasic;
import com.xinwei.system.xworder.domain.PageObject;
import com.xinwei.system.xworder.domain.SystemOrder;
import com.xinwei.system.xworder.service.SystemOrderService;

/**
 * 
 * Ȩ�޹������ӿ�ʵ��
 * 
 * @author fanhaoyu
 * 
 */

public class AuthorityManageServiceImpl implements AuthorityManageService {

	private static Log log = LogFactory
			.getLog(AuthorityManageServiceImpl.class);

	private AuthorityManageDao authorityManageDao;
	@Autowired
	private SystemOrderService systemOrderService;

	public List<OperAction> queryAuthority(String username) throws Exception {
		LoginUser currentUser = getCurrentUser(username, true);
		if (currentUser == null)
			return null;
		return authorityManageDao.queryAuthority(currentUser.getRoleId());
	}

	@Override
	public OperAction queryOperAction(OperSignature signature) throws Exception {
		return authorityManageDao.queryOperAction(signature);
	}

	@Override
	public boolean checkPrivilege(String sessionId, OperAction operAction)
			throws Exception {
		if (operAction == null)
			return false;
		LoginUser currentUser = getCurrentUser(sessionId, false);
		if (currentUser == null)
			return false;
		// ����Ա������Ȩ��
		if (currentUser.getRoleId() == RoleType.ADMIN)
			return true;
		// ����û���Ȩ���б��д���facade��method��Ӧ��operAction
		List<OperAction> operActionList = UserPrivilegeCache.getInstance()
				.getPrivilege(currentUser.getRoleId());
		for (OperAction action : operActionList) {
			if (action.getOperName().equals(operAction.getOperName())) {
				if (action.getActions()
						.contains(operAction.getActions().get(0)))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean checkPrivilege(String sessionId, OperSignature signature)
			throws Exception {
		LoginUser currentUser = getCurrentUser(sessionId, false);
		if (currentUser == null)
			return false;
		// ����Ա������Ȩ��
		if (currentUser.getRoleId() == RoleType.ADMIN)
			return true;
		// operName��һ��action
		OperAction operAction = queryOperAction(signature);
		if (operAction == null)
			return false;
		return checkPrivilege(sessionId, operAction);
	}

	@Override
	public void addLog(String sessionId, OperSignature signature,
			OperObject operObject, Object[] params) throws Exception {
		// ����ǲ�������������SAG, ��SAG ID���⴦��
		if (operObject.getObjectType().equals(OperObjectTypeDD.SAG)) {
			if (params != null && params[0] instanceof SxcBasic) {
				SxcBasic sag = (SxcBasic) params[0];
				operObject.setObjectId(sag.getSagId().toString());
			}
		}
		// username��ӦoperatorName
		// loginIp��Ӧplace
		// operTime��ӦcreateTime
		// operName��ӦorderCmd
		// action��Ӧchannel
		// operType��ӦorderType
		// objectType��ӦscheduledParam
		// objectId��ӦcmdObject
		// data��Ӧremark

		SystemOrder order = new SystemOrder();
		// orderType = operType
		order.setOrderType(operObject.getOperType());
		// scheduleParam = objectType
		order.setScheduleParam(operObject.getObjectType());
		// cmdObject = objectId
		if (operObject.getObjectId() != null)
			order.setCmdObject(operObject.getObjectId());
		// operatorName = userName
		LoginUser currentUser = getCurrentUser(sessionId, false);
		order.setOperatorName(currentUser.getUsername());
		// place = loginIp
		order.setPlace(currentUser.getLoginIp());
		// ��ȡoperAction
		OperAction operAction = authorityManageDao.queryOperAction(signature);
		if (operAction != null) {
			// orderCmd = operName
			order.setOrderCmd(operAction.getOperName());
			// channel = action
			order.setChannel(operAction.getActions().get(0));
		} else {
			log.error("operAction is null. signature=" + signature);
			return;
		}

		// �������Ĳ���ת��Ϊjson��
		try {
			String data = "";
			if (params != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				data = objectMapper.writeValueAsString(params);
			}
			// ������ݳ�����󳤶ȣ�����н�ȡ
			if (data.length() > 8000) {
				data = data.substring(0, 8000);
			}
			// remark = data
			order.setRemark(data);

		} catch (Exception e) {
			log.error(e);
		}
		// ��¼��־
		try {
			systemOrderService.insert(order);
		} catch (Exception e) {
			log.error("failed to log order. order=" + order, e);
			throw e;
		}
	}

	@Override
	public SystemLogQueryResult queryLog(LogQueryCondition condition)
			throws Exception {
		// username��ӦoperatorName
		// loginIp��Ӧplace
		// operTime��ӦcreateTime
		// operName��ӦorderCmd
		// action��Ӧchannel
		// operType��ӦorderType
		// objectType��ӦscheduledParam
		// objectId��ӦcmdObject
		// data��Ӧremark
		// ����
		// orderType��operatorName��channel��scheduleParam��place��cmdObject��remark
		Map<String, Object> map = new HashMap<String, Object>();
		if (condition.getUsername() != null)
			map.put("operatorName", condition.getUsername());
		if (condition.getOperType() != null)
			map.put("orderType", condition.getOperType());
		if (condition.getOperObjType() != null)
			map.put("scheduleParam", condition.getOperObjType());
		if (condition.getObjectId() != null)
			map.put("cmdObject", condition.getObjectId());
		if (condition.getActionType() != null)
			map.put("channel", condition.getActionType());
		if (condition.getStartTime() != null)
			map.put("startTime", condition.getStartTime());
		if (condition.getEndTime() != null)
			map.put("endTime", condition.getEndTime());
		// ��ȡ�ܽ������
		int totalNum = systemOrderService.countRecordByMutliCondition(map);
		SystemLogQueryResult result = new SystemLogQueryResult();
		result.setTotalNum(totalNum);
		// ֻ����Ŀ����0����Ҫ��ѯ��ϸ
		if (totalNum > 0) {
			PageObject pageObject = new PageObject(condition.getPageSize(),
					condition.getCurrentPage());
			map.put("page", pageObject);
			List<SystemLog> logList = null;
			// ����ҳ��������
			try {
				// orderType��operatorName��customerId��scheduleParam��
				// place��remark��orderCmd�� startTime, endTime, page
				List<SystemOrder> orderList = systemOrderService
						.findRecordByMutliCondition(map);
				logList = getSystemLogList(orderList);
			} catch (Exception e) {
				log.error("query systemLog failed. " + condition.toString(), e);
			}
			result.setLogList(logList);
			if (logList != null)
				result.setResultNum(logList.size());
		}
		int totalPage = totalNum / condition.getPageSize();
		if (totalNum % condition.getPageSize() != 0)
			totalPage++;
		result.setTotalPage(totalPage);
		result.setCurrentPage(condition.getCurrentPage());
		return result;
	}

	/**
	 * �Ѷ����б�ת��Ϊϵͳ��־�б� <br>
	 * username��ӦoperatorName <br>
	 * loginIp��Ӧplace <br>
	 * operTime��ӦcreateTime <br>
	 * operName��ӦorderCmd <br>
	 * action��Ӧchannel <br>
	 * operType��ӦorderType <br>
	 * objectType��ӦScheduledParam <br>
	 * objectId��ӦcmdObject <br>
	 * remark��Ӧdata
	 * 
	 * @param orderList
	 * @return
	 * @throws Exception
	 */
	private List<SystemLog> getSystemLogList(List<SystemOrder> orderList)
			throws Exception {
		List<SystemLog> logList = new ArrayList<SystemLog>();
		if (orderList == null || orderList.isEmpty())
			return logList;
		for (SystemOrder order : orderList) {
			SystemLog sysLog = new SystemLog();

			sysLog.setUsername(order.getOperatorName());
			// orderType = operType
			String operType = order.getOrderType();
			// ScheduleParam = objectType
			String objectType = order.getScheduleParam();
			String objectId = order.getCmdObject();
			OperObject operObject = new OperObject();
			operObject.setOperType(operType);
			operObject.setObjectId(objectId);
			operObject.setObjectType(objectType);
			sysLog.setOperObject(operObject);
			// ��ԭʼ�Ĳ���ϸ��ת��Ϊ�ϼ����ĸ�ʽ	
			String orginalDetail = order.getRemark();
			sysLog.setDataDesc(translateOperDetail(orginalDetail));
			//
			sysLog.setLoginIp(order.getPlace());
			sysLog.setOperTime(order.getCreateTime());
			// orderCmd = operName
			String operDesc = authorityManageDao.queryOperation(order
					.getOrderCmd());
			if (operDesc == null)
				log.error("operDesc is null! operName=" + order.getOrderCmd());
			sysLog.setOperDesc(operDesc);
			// channel = action
			sysLog.setActionDesc(order.getChannel());

			logList.add(sysLog);
		}
		return logList;
	}
	
	/**
	 * ��ԭʼ�Ĳ���ϸ��ת��Ϊ�ϼ����ĸ�ʽ
	 * @param orginalDetail ԭʼ�Ĳ���ϸ��
	 * @return ������ʽ�Ĳ���ϸ��
	 */
	private String translateOperDetail(String orginalDetail) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			StringBuffer finalDetail = new StringBuffer();
			Object[] detailParams = objectMapper.readValue(orginalDetail, Object[].class);
			for(Object detailParam : detailParams) {
				finalDetail.append(detailParam).append(";\r\n");
			}
			return finalDetail.toString();
		} catch (Exception e) {
			return orginalDetail;
		}
	}

	/**
	 * ����������ȡ��ǰ�û���Ϣ
	 * 
	 * @param condition
	 * @param byName
	 * @return
	 */
	private LoginUser getCurrentUser(String condition, boolean byName) {
		List<LoginUser> userList = LoginUserCache.getInstance()
				.queryOnlineUser();
		LoginUser currentUser = null;
		for (LoginUser loginUser : userList) {
			if (byName) {
				if (loginUser.getUsername().equals(condition))
					currentUser = loginUser;
			} else {
				if (loginUser.getSessionId().equals(condition))
					currentUser = loginUser;
			}
		}
		return currentUser;
	}

	public void setAuthorityManageDao(AuthorityManageDao authorityManageDao) {
		this.authorityManageDao = authorityManageDao;
	}

	public void setSystemOrderService(SystemOrderService systemOrderService) {
		this.systemOrderService = systemOrderService;
	}

}
