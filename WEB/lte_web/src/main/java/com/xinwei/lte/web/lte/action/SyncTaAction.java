/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-12	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.lte.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.lte.web.lte.model.MmeTaModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.core.model.secu.OperSignature;
import com.xinwei.minas.core.model.secu.syslog.LogParam;
import com.xinwei.minas.enb.core.facade.EnbGlobalConfigFacade;
import com.xinwei.minas.enb.core.model.corenet.TaModel;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.system.action.web.WebConstants;

/**
 * 
 * 同步跟踪区码Action
 * 
 * @author fanhaoyu
 * 
 */

public class SyncTaAction extends ActionSupport {

	private static Logger logger = LoggerFactory.getLogger(SyncTaAction.class);

	@Resource
	private OssAdapter ossAdapter;

	public String syncTa() {
		logger.debug("sync ta list to tcn1000 start.");
		String message = null;
		try {
			String sessionId = getSessionId();
			// 鉴权记日志
			checkPrivilege(sessionId);

			EnbGlobalConfigFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, EnbGlobalConfigFacade.class);
			// 查询网管中的跟踪区码列表
			List<TaModel> emsTaModels = facade.queryAllTaItems();
			// 查询TCN1000设备的跟踪区码列表
			List<MmeTaModel> tcn1000TaModels = queryTaFromTcn1000();
			// 比较两个列表，找出需要增、删、改的记录
			CompareResult compareResult = compare(emsTaModels, tcn1000TaModels);
			// 同步到TCN1000
			message = syncToTcn1000(compareResult);
		} catch (Exception e) {
			logger.warn("sync ta to tcn1000 with error.", e);
			message = e.getLocalizedMessage();
		}

		JSONObject json = new JSONObject();
		json.put("message", message);
		Util.ajaxSimpleUtil(json.toString());

		logger.debug("sync ta list to tcn1000 finish.");

		return NONE;
	}

	/**
	 * 鉴权记日志
	 * 
	 * @param sessionId
	 * @throws Exception
	 */
	private void checkPrivilege(String sessionId) throws Exception {
		OperSignature signature = new OperSignature();
		signature.setFacade(this.getClass().getCanonicalName());
		signature.setMethod("syncTa");
		// 鉴权
		MinasSession.getInstance().checkPrivilege(sessionId, signature);
		// 记日志
		LogParam logParam = new LogParam(sessionId, signature,
				OperObject.createSystemOperObject(), null);
		MinasSession.getInstance().addLog(logParam);
	}

	private String syncToTcn1000(CompareResult compareResult) {
		StringBuilder message = new StringBuilder();

		String msg = doSync(compareResult.getDeleteModels(),
				ActionTypeDD.DELETE);
		message.append(msg);

		msg = doSync(compareResult.getAddModels(), ActionTypeDD.ADD);
		message.append(msg);

		msg = doSync(compareResult.getUpdateModels(), ActionTypeDD.MODIFY);
		message.append(msg);

		return message.toString();
	}

	private String doSync(List<MmeTaModel> modelList, String actionType) {
		StringBuilder message = new StringBuilder();
		if (modelList != null) {
			for (MmeTaModel mmeTaModel : modelList) {
				try {
					String flag = sendRequest(mmeTaModel, actionType);
					if (!"0".equals(flag)) {
						throw new Exception(LteFlag.flagReturn(flag));
					}
				} catch (Exception e) {
					message.append(getActionMessage(actionType))
							.append("失败. 跟踪区码:")
							.append(mmeTaModel.getMmeta_id()).append(" 原因:")
							.append(e.getLocalizedMessage()).append("\n");
				}
			}
		}
		return message.toString();
	}

	/**
	 * 向TCN1000发送消息
	 * 
	 * @param mmeTaModel
	 * @param actionType
	 * @return
	 * @throws Exception
	 */
	private String sendRequest(MmeTaModel mmeTaModel, String actionType)
			throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mmeTAID", mmeTaModel.getMmeta_id());
		Map<String, Object> resultMap = null;

		if (actionType.equals(ActionTypeDD.DELETE)) {
			resultMap = ossAdapter.invoke(0xac, 0x02, map);
		} else {

			map.put("mmeTATAI", mmeTaModel.getMmeta_tai());
			map.put("mmeTAComment", mmeTaModel.getMmeta_comment());
			if (actionType.equals(ActionTypeDD.ADD)) {
				resultMap = ossAdapter.invoke(0xac, 0x01, map);
			} else if (actionType.equals(ActionTypeDD.MODIFY)) {
				resultMap = ossAdapter.invoke(0xac, 0x03, map);
			}
		}
		return (String) resultMap.get("lteFlag");
	}

	/**
	 * 比较
	 * 
	 * @param emsTaModels
	 * @param tcn1000TaModels
	 * @return
	 */
	private CompareResult compare(List<TaModel> emsTaModels,
			List<MmeTaModel> tcn1000TaModels) {
		CompareResult compareResult = new CompareResult();
		if (emsTaModels == null || emsTaModels.isEmpty()) {
			compareResult.setDeleteModels(tcn1000TaModels);
			return compareResult;
		}
		if (tcn1000TaModels == null || tcn1000TaModels.isEmpty()) {
			List<MmeTaModel> taModelsToAdd = convert(emsTaModels);
			compareResult.setAddModels(taModelsToAdd);
			return compareResult;
		}
		List<MmeTaModel> modelsToUpdate = new ArrayList<MmeTaModel>();
		List<MmeTaModel> modelsToAdd = new ArrayList<MmeTaModel>();
		for (TaModel emsTa : emsTaModels) {
			String id = String.valueOf(emsTa.getId());
			MmeTaModel mmeTaModel = findById(id, tcn1000TaModels);
			if (mmeTaModel == null) {
				// tcn1000设备中没有，需要添加
				modelsToAdd.add(convert(emsTa));
			} else {
				// id相同，code不同，需要修改
				boolean codeEqual = emsTa.getCode().equals(
						mmeTaModel.getMmeta_tai());
				boolean commentEqual = emsTa.getRemark().equals(
						mmeTaModel.getMmeta_comment());
				if (!codeEqual || !commentEqual) {
					modelsToUpdate.add(convert(emsTa));
				}
			}
		}

		List<MmeTaModel> modelsToDelete = new ArrayList<MmeTaModel>();
		for (MmeTaModel mmeTaModel : tcn1000TaModels) {
			TaModel taModel = findTaModelById(mmeTaModel.getMmeta_id(),
					emsTaModels);
			if (taModel == null) {
				// 网管没有，需要删除
				modelsToDelete.add(mmeTaModel);
			}
		}
		compareResult.setAddModels(modelsToAdd);
		compareResult.setDeleteModels(modelsToDelete);
		compareResult.setUpdateModels(modelsToUpdate);
		return compareResult;
	}

	/**
	 * 按ID查找
	 * 
	 * @param id
	 * @param models
	 * @return
	 */
	private MmeTaModel findById(String id, List<MmeTaModel> models) {
		if (models == null || models.isEmpty())
			return null;
		for (MmeTaModel mmeTaModel : models) {
			if (mmeTaModel.getMmeta_id().equals(id)) {
				return mmeTaModel;
			}
		}
		return null;
	}

	/**
	 * 按ID查找
	 * 
	 * @param id
	 * @param models
	 * @return
	 */
	private TaModel findTaModelById(String id, List<TaModel> models) {
		if (models == null || models.isEmpty())
			return null;
		int idInt = Integer.valueOf(id);
		for (TaModel taModel : models) {
			if (taModel.getId() == idInt) {
				return taModel;
			}
		}
		return null;
	}

	/**
	 * 模型转换
	 * 
	 * @param taModels
	 * @return
	 */
	private List<MmeTaModel> convert(List<TaModel> taModels) {
		if (taModels == null || taModels.isEmpty()) {
			return Collections.emptyList();
		}
		List<MmeTaModel> models = new LinkedList<MmeTaModel>();
		for (TaModel taModel : taModels) {
			models.add(convert(taModel));
		}
		return models;
	}

	private MmeTaModel convert(TaModel taModel) {
		MmeTaModel mmeTaModel = new MmeTaModel();
		mmeTaModel.setMmeta_id(String.valueOf(taModel.getId()));
		mmeTaModel.setMmeta_tai(taModel.getCode());
		mmeTaModel.setMmeta_comment(taModel.getRemark());
		return mmeTaModel;
	}

	/**
	 * 从Tcn1000查询
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<MmeTaModel> queryTaFromTcn1000() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// TODO
		map.put("ltePageSize", LteConstant.PageSize + "");
		map.put("ltePageIndex", "1");

		Map<String, Object> resultMap = ossAdapter.invoke(0xac, 0x05, map);

		String flag = (String) resultMap.get("lteFlag");
		// Tcn1000返回失败
		if (!"0".equals(flag)) {
			throw new Exception(LteFlag.flagReturn(flag));
		}

		int totalPage = 0;
		// 获取总记录数
		int lteTotalQueryCount = Integer.parseInt((String) resultMap
				.get("lteTotalQueryCount"));
		// 计算总页数
		if (lteTotalQueryCount % LteConstant.PageSize == 0) {
			totalPage = lteTotalQueryCount / LteConstant.PageSize;
		} else {
			totalPage = lteTotalQueryCount / LteConstant.PageSize + 1;
		}
		if (lteTotalQueryCount == 0) {
			return Collections.emptyList();
		}
		List<MmeTaModel> resultList = new LinkedList<MmeTaModel>();
		// 获取第一页中的记录
		List<MmeTaModel> modelList = parseTaList(resultMap);
		resultList.addAll(modelList);
		// 查询剩余页中的记录
		for (int i = 2; i <= totalPage; i++) {
			map.clear();
			map.put("ltePageSize", LteConstant.PageSize + "");
			map.put("ltePageIndex", i + "");

			resultMap = ossAdapter.invoke(0xac, 0x05, map);
			flag = (String) resultMap.get("lteFlag");
			// Tcn1000返回失败
			if (!"0".equals(flag)) {
				throw new Exception(LteFlag.flagReturn(flag));
			}
			modelList = parseTaList(resultMap);
			resultList.addAll(modelList);
		}

		return resultList;
	}

	private List<MmeTaModel> parseTaList(Map<String, Object> resultMap) {
		List<MmeTaModel> mmeTaModelList = null;
		List<Map> resultList = (List<Map>) resultMap.get("mmeTAInfo");
		if (resultList != null) {
			mmeTaModelList = new ArrayList<MmeTaModel>();
			for (Map rMap : resultList) {
				MmeTaModel mmeTaModel = new MmeTaModel();
				if (rMap.get("mmeTAID") != null) {
					mmeTaModel.setMmeta_id(((String) rMap.get("mmeTAID"))
							.trim());
				}
				if (rMap.get("mmeTATAI") != null) {
					mmeTaModel.setMmeta_tai(((String) rMap.get("mmeTATAI"))
							.trim());
				}
				if (rMap.get("mmeTAComment") != null) {
					mmeTaModel.setMmeta_comment(((String) rMap
							.get("mmeTAComment")).trim());
				}
				mmeTaModelList.add(mmeTaModel);
			}
		}
		return mmeTaModelList;
	}

	class CompareResult {

		private List<MmeTaModel> addModels;
		private List<MmeTaModel> updateModels;
		private List<MmeTaModel> deleteModels;

		public List<MmeTaModel> getAddModels() {
			return addModels;
		}

		public void setAddModels(List<MmeTaModel> addModels) {
			this.addModels = addModels;
		}

		public List<MmeTaModel> getUpdateModels() {
			return updateModels;
		}

		public void setUpdateModels(List<MmeTaModel> updateModels) {
			this.updateModels = updateModels;
		}

		public List<MmeTaModel> getDeleteModels() {
			return deleteModels;
		}

		public void setDeleteModels(List<MmeTaModel> deleteModels) {
			this.deleteModels = deleteModels;
		}

	}

	private String getActionMessage(String actionType) {
		if (actionType.equals(ActionTypeDD.DELETE)) {
			return "删除";
		} else if (actionType.equals(ActionTypeDD.ADD)) {
			return "添加";
		} else if (actionType.equals(ActionTypeDD.MODIFY)) {
			return "修改";
		}
		return "操作";
	}

	private String getSessionId() {
		return ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
	}
}
