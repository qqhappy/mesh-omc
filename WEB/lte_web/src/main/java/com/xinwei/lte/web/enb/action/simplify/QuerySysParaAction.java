package com.xinwei.lte.web.enb.action.simplify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.lte.web.lte.model.MmeTaModel;
import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.facade.EnbGlobalConfigFacade;
import com.xinwei.minas.enb.core.model.corenet.EnbGlobalConfig;
import com.xinwei.minas.enb.core.model.corenet.TaModel;
import com.xinwei.omp.core.model.biz.PagingCondition;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 系统参数操作action
 * 
 * @author zhangqiang
 * 
 */
public class QuerySysParaAction extends ActionSupport {

	private static Logger logger = LoggerFactory
			.getLogger(QuerySysParaAction.class);

	@Resource
	private OssAdapter ossAdapter;

	/**
	 * 分页条件
	 */
	private PagingCondition condition;

	/**
	 * 跟踪区码新增修改所需的TA模型
	 */
	private TaModel ta;

	/**
	 * 跟踪区码ID,多个时以","分隔
	 */
	private String idArray;

	/**
	 * 系统参数修改时所需的config模型
	 */
	private EnbGlobalConfig config;

	/**
	 * 跟踪区码列表中的当前页号
	 */
	private int currentPage;

	/**
	 * 查询系统参数
	 * 
	 * @return
	 */
	public String querySysPara() {
		JSONObject json = new JSONObject();
		try {
			EnbGlobalConfigFacade facade = Util
					.getFacadeInstance(EnbGlobalConfigFacade.class);
			json.put("status", 0);
			json.put("message", facade.queryEnbGlobalConfig());
		} catch (Exception e) {
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 修改tcn系统参数
	 * 
	 * @return
	 */
	public String modifyTcnSysPara() {
		JSONObject json = new JSONObject();
		try {
			//
			String plmn = config.getMcc() + config.getMnc();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < plmn.length(); i++) {
				if (i % 2 != 0) {
					sb.append(plmn.charAt(i));
				}
			}
			if (sb.charAt(sb.length() - 1) == 'f'
					|| sb.charAt(sb.length() - 1) == 'F') {
				sb.deleteCharAt(sb.length() - 1);
			}
			Map<String, Object> sysConfigMap = ossAdapter.invoke(0xa9, 0x04,
					new HashMap<String, Object>());
			sysConfigMap.put("sysconfPLMN", sb.toString());
			sysConfigMap.remove("sysconfSipPort");
			sysConfigMap.remove("sysconfMMEIPID");
			sysConfigMap.remove("sysconfMMES1PORT");
			sysConfigMap.remove("lteFlag");
			if (((String) sysConfigMap.get("sysconfDnsIp")).split("\\.").length != 4) {
				sysConfigMap.remove("sysconfDnsIp");
			}
			Map<String, Object> resultMap = ossAdapter.invoke(0xa9, 0x03,
					sysConfigMap);
			String flag = (String) resultMap.get("lteFlag");
			if ("0".equals(flag)) {
				EnbGlobalConfigFacade facade = Util
						.getFacadeInstance(EnbGlobalConfigFacade.class);
				Map<Object, String> messageMap = facade.configEnbGlobalConfig(
						OperObject.createSystemOperObject(), config);
				StringBuilder message = new StringBuilder();
				if (messageMap != null && !messageMap.isEmpty()) {
					for (Object object : messageMap.keySet()) {
						message.append("eNB ID:").append(object.toString())
								.append("  消息:").append(messageMap.get(object))
								.append("\n");
					}
				}
				json.put("status", 0);
				json.put("message", message.toString());
			} else {
				json.put("status", 1);
				json.put("message", LteFlag.flagReturn(flag));
			}
		} catch (Exception e) {
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 修改enb系统参数
	 * 
	 * @return
	 */
	public String modifyEnbSysPara() {
		JSONObject json = new JSONObject();
		try {
			EnbGlobalConfigFacade facade = Util
					.getFacadeInstance(EnbGlobalConfigFacade.class);
			Map<Object, String> messageMap = facade.configEnbGlobalConfig(
					OperObject.createSystemOperObject(), config);
			StringBuilder message = new StringBuilder();
			if (messageMap != null && !messageMap.isEmpty()) {
				for (Object object : messageMap.keySet()) {
					message.append("eNB ID:").append(object.toString())
							.append("  消息:").append(messageMap.get(object))
							.append("\n");
				}
			}
			json.put("status", 0);
			json.put("message", message.toString());
		} catch (Exception e) {
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}
	
	/**
	 * 修改视频会议系统参数
	 * 
	 * @return
	 */
	public String modifyVideoSysPara() {
		JSONObject json = new JSONObject();
		try {
			EnbGlobalConfigFacade facade = Util
					.getFacadeInstance(EnbGlobalConfigFacade.class);
			facade.configEnbGlobalConfig(
					OperObject.createSystemOperObject(), config);
			json.put("status", 0);
		} catch (Exception e) {
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 查询所有跟踪区�?
	 * 
	 * @return
	 */
	public String queryAllTa() {
		JSONObject json = new JSONObject();
		try {
			EnbGlobalConfigFacade facade = Util
					.getFacadeInstance(EnbGlobalConfigFacade.class);
			json.put("status", 0);
			json.put("message", facade.queryAllTaItems());
		} catch (Exception e) {
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 查询跟踪区码
	 * 
	 * @return
	 */
	public String queryTaList() {
		JSONObject json = new JSONObject();
		try {
			EnbGlobalConfigFacade facade = Util
					.getFacadeInstance(EnbGlobalConfigFacade.class);
			if (condition == null) {
				condition = new PagingCondition();
				condition.setCurrentPage(1);
				condition.setNumPerPage(10);
			}
			json.put("status", 0);
			PagingData<TaModel> model = facade.queryTaItems(condition);
			if (model.getTotalPages() < condition.getCurrentPage()) {
				condition.setCurrentPage(model.getTotalPages());
			}
			json.put("message", model);
		} catch (Exception e) {
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 新增跟踪区码
	 * 
	 * @return
	 */
	public String addSysParaTa() {
		JSONObject json = new JSONObject();
		try {
			EnbGlobalConfigFacade facade = Util
					.getFacadeInstance(EnbGlobalConfigFacade.class);

			facade.addTaItem(OperObject.createSystemOperObject(), ta);
			// 同步到Tcn1000
			syncTaToTcn1000(ta, ActionTypeDD.ADD);
			json.put("status", 0);
		} catch (Exception e) {
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 跳转至修改跟踪区码页�?
	 * 
	 * @return
	 */
	public String turnToModifyTaHtml() {
		try {
			EnbGlobalConfigFacade facade = Util
					.getFacadeInstance(EnbGlobalConfigFacade.class);
			ta = facade.queryTaItemById(Integer.valueOf(idArray));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 修改跟踪区码
	 * 
	 * @return
	 */
	public String modifySysParaTa() {
		JSONObject json = new JSONObject();
		try {
			EnbGlobalConfigFacade facade = Util
					.getFacadeInstance(EnbGlobalConfigFacade.class);

			facade.modifyTaItem(OperObject.createSystemOperObject(), ta);
			// 同步到Tcn1000
			syncTaToTcn1000(ta, ActionTypeDD.MODIFY);
			json.put("status", 0);
		} catch (Exception e) {
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 删除跟踪区码
	 * 
	 * @return
	 */
	public String deleteSysParaTa() {
		JSONObject json = new JSONObject();
		json.put("status", 0);
		Map<String, String> deleteFailedInfo = new HashMap<String, String>();
		try {
			EnbGlobalConfigFacade facade = Util
					.getFacadeInstance(EnbGlobalConfigFacade.class);
			String[] ids = idArray.split(",");
			for (int i = 0; i < ids.length; i++) {
				if (ids[i] != null && !"".equals(ids[i])) {
					TaModel taModel = new TaModel();
					taModel.setId(Integer.valueOf(ids[i]));

					try {
						facade.deleteTaItem(
								OperObject.createSystemOperObject(),
								Integer.valueOf(ids[i]));
						// 同步到Tcn1000
						syncTaToTcn1000(taModel, ActionTypeDD.DELETE);
					} catch (Exception e) {
						deleteFailedInfo.put(ids[i], e.getLocalizedMessage());
						e.printStackTrace();
					}

				}
			}
			if (deleteFailedInfo.keySet().size() != 0) {
				json.put("status", -1);
				StringBuilder sb = new StringBuilder();
				for (String str : deleteFailedInfo.keySet()) {
					sb.append("ID为" + str + "的记录删除失败,");
					sb.append(deleteFailedInfo.get(str));
				}
				json.put("error", sb.toString());
			}
		} catch (Exception e) {
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 查询是否具备修改MNC和MCC的条�?
	 * 
	 * @return
	 */
	public String checkModifyCondition() {
		JSONObject json = new JSONObject();
		json.put("checkResult", false);
		json.put("message", "");
		try {
			EnbGlobalConfigFacade facade = Util
					.getFacadeInstance(EnbGlobalConfigFacade.class);
			EnbGlobalConfig globalConfig = facade.queryEnbGlobalConfig();
			if (globalConfig.getMcc().equals(config.getMcc())
					&& globalConfig.getMnc().equals(config.getMnc())) {
				json.put("checkResult", true);
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ltePageSize", 10);
				map.put("ltePageIndex", 1);
				Map<String, Object> resultMap = ossAdapter.invoke(0xa1, 0x05,
						map);
				String flag = (String) resultMap.get("lteFlag");
				if ("0".equals(flag)) {
					List<Map> resultList = (List<Map>) resultMap.get("usrInfo");
					if (resultList == null) {
						json.put("checkResult", true);
					} else {
						if (resultList.size() == 0) {
							json.put("checkResult", true);
						}
					}
				}
			}
		} catch (Exception e) {
			json.put("message", e.getLocalizedMessage());
			e.printStackTrace();
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 将新增或修改或删除的TA同步到Tcn1000
	 * 
	 * @param taModel
	 * @param actionType
	 */
	private void syncTaToTcn1000(TaModel taModel, String actionType) {
		try {
			MmeTaModel mmeTaModel = convert(taModel);
			sendRequest(mmeTaModel, actionType);
		} catch (Exception e) {
			logger.debug("sync ta to tcn1000 failed. actionType=" + actionType
					+ " ,TA ID=" + taModel.getId(), e);
		}
	}

	/**
	 * 向TCN1000发送消�?
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

	private MmeTaModel convert(TaModel taModel) {
		MmeTaModel mmeTaModel = new MmeTaModel();
		mmeTaModel.setMmeta_id(String.valueOf(taModel.getId()));
		mmeTaModel.setMmeta_tai(taModel.getCode());
		mmeTaModel.setMmeta_comment(taModel.getRemark());
		return mmeTaModel;
	}

	public PagingCondition getCondition() {
		return condition;
	}

	public void setCondition(PagingCondition condition) {
		this.condition = condition;
	}

	public TaModel getTa() {
		return ta;
	}

	public void setTa(TaModel ta) {
		this.ta = ta;
	}

	public String getIdArray() {
		return idArray;
	}

	public void setIdArray(String idArray) {
		this.idArray = idArray;
	}

	public EnbGlobalConfig getConfig() {
		return config;
	}

	public void setConfig(EnbGlobalConfig config) {
		this.config = config;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

}
