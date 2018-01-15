package com.xinwei.lte.web.enb.action.simplify;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.cache.EnbVersionBizConfigCache;
import com.xinwei.lte.web.enb.model.BizErrorModel;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbSimplifyConfigFacade;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 单板的增删改
 * 
 * @author zhangqiang
 * 
 */
public class BoardSimplifyAction extends ActionSupport {

	private long moId;

	/**
	 * 操作类型
	 */
	private String operType;

	/**
	 * 拓扑表光口号
	 */
	private String fiberPort;

	/**
	 * 版本号
	 */
	private String enbVersion;

	/**
	 * 前端传来的参数数组的字符串格式，包含单板表的信息
	 */
	private String parameters;

	/**
	 * 异常
	 */
	private String error;

	private static final String tableName = "T_BOARD";

	/**
	 * 进行增、改的业务校验，如若校验通过，则操作直接生效
	 */
	public String checkBoardBizRules() {
		JSONObject json = new JSONObject();
		BizErrorModel errorModel = new BizErrorModel();
		try {
			EnbSimplifyConfigFacade facade = Util
					.getFacadeInstance(EnbSimplifyConfigFacade.class);
			// 获取当前版本支持的字段
			List<String> supportedFields = EnbVersionBizConfigCache
					.getInstance().getFields(getEnbTypeByMoId(moId),enbVersion, tableName);
			XBizRecord condition = getCondition(parameters);
			String enbHexId = getEnbHexId(moId);
			Integer myFiberPort = null;
			if (!"".equals(fiberPort) && fiberPort != null) {
				myFiberPort = Integer.valueOf(fiberPort);
			}
			if ("add".equals(operType)) {
				// 过滤掉当前版本不支持的字段值
				filterUnsupportedFields(supportedFields, condition);
				facade.addBoard(OperObject.createEnbOperObject(enbHexId), moId,
						condition, myFiberPort);
			}
			if ("config".equals(operType)) {
				// 过滤掉当前版本不支持的字段值
				filterUnsupportedFields(supportedFields, condition);
				facade.updateBoard(OperObject.createEnbOperObject(enbHexId),
						moId, condition, myFiberPort);
			}
			if ("delete".equals(operType)) {
				facade.deleteBoard(OperObject.createEnbOperObject(enbHexId), moId,
						condition);
			}
			if("multiDelete".equals(operType)){
				String[] strFir = parameters.split(";");
				for (int i = 0; i < strFir.length; i++) {
					Map<String, String> map = new LinkedHashMap<String, String>();
					String[] strSec = strFir[i].split(",");
					for (int j = 0; j < strSec.length; j++) {
						String[] str = strSec[j].split("=");
						map.put(str[0], str[1]);
					}
					XBizRecord deleteCondition = new XBizRecord();
					Iterator<String> iter = map.keySet().iterator();
					while (iter.hasNext()) {
						String key = iter.next();
						deleteCondition.addField(makeXBizField(key,
								map.get(key)));
					}
					facade.deleteBoard(OperObject.createEnbOperObject(enbHexId), moId,
							deleteCondition);
				}
			}
		} catch (BizException e) {
			String error = e.getLocalizedMessage();
			Object target = e.getTarget();
			if (target == null) {
				target = "";
			}
			errorModel.setError(error);
			errorModel.setErrorEntity(target.toString());
			errorModel.setErrorType(0);
		} catch (Exception e) {
			String error = e.getLocalizedMessage();
			errorModel.setError(error);
		}
		json.put("errorModel", JSONObject.fromObject(errorModel));
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 获取增删改所需的condition
	 * 
	 * @return
	 */
	private XBizRecord getCondition(String parameters) {
		XBizRecord condition = new XBizRecord();
		Map<String, String> map = getMap(parameters);
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			condition.addField(makeXBizField(key, map.get(key)));
		}
		return condition;
	}

	/**
	 * 由获取的parameters参数获得键值对
	 * 
	 * @return
	 */
	public Map<String, String> getMap(String parameters) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		// 根据";"进行拆分
		String[] strFir = parameters.split(";");
		for (int i = 0; i < strFir.length; i++) {
			// 根据"="进行拆分
			String[] str = strFir[i].split("=");
			map.put(str[0], str[1]);
		}
		return map;

	}

	/**
	 * 生成xBizField
	 * 
	 * @param name
	 * @return
	 */
	public XBizField makeXBizField(String name, String value) {
		XBizField xBizField = new XBizField();
		xBizField.setName(name);
		xBizField.setValue(value);
		return xBizField;
	}

	/**
	 * 根据moId获取enbHexId
	 * 
	 * @param moId
	 * @return
	 */
	private String getEnbHexId(long moId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		return facade.queryByMoId(moId).getHexEnbId();
	}
	
	/**
	 * 根据moId获取enbType
	 * 
	 * @param moId
	 * @return
	 */
	private int getEnbTypeByMoId(long moId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		return facade.queryByMoId(moId).getEnbType();
	}

	/**
	 * 向im5000服务器发送数据时需要按照基站当前版本支持的字段进行发送
	 * 
	 * @param supportedFields
	 * @param bizRecord
	 */
	private void filterUnsupportedFields(List<String> supportedFields,
			XBizRecord bizRecord) {
		Map<String, XBizField> fieldMap = bizRecord.getFieldMap();
		List<String> toRemove = new ArrayList<String>();
		for (String fieldName : fieldMap.keySet()) {
			if (!supportedFields.contains(fieldName)) {
				toRemove.add(fieldName);
			}
		}
		for (String fieldName : toRemove) {
			fieldMap.remove(fieldName);
		}
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public String getFiberPort() {
		return fiberPort;
	}

	public void setFiberPort(String fiberPort) {
		this.fiberPort = fiberPort;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getEnbVersion() {
		return enbVersion;
	}

	public void setEnbVersion(String enbVersion) {
		this.enbVersion = enbVersion;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
