package com.xinwei.lte.web.enb.xw7400;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.cache.EnbVersionBizConfigCache;
import com.xinwei.lte.web.enb.cache.FieldLevelCache;
import com.xinwei.lte.web.enb.model.BizErrorModel;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbNeighbourFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbNeighbourRecord;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.system.action.web.WebConstants;

/**
 * 邻区关系参数表的增删改查
 * 
 * @author zhangqiang
 * 
 */
public class NbrcelSimplifyAction extends ActionSupport {

	private long moId;

	/**
	 * 16进制的基站ID
	 */
	private String enbHexId;

	/**
	 * 操作类型
	 */
	private String operType;

	/**
	 * 版本号
	 */
	private String enbVersion;

	/**
	 * 前端传来的参数数组的字符串格式，包含邻区表相关信息
	 */
	private String parameters;

	/**
	 * 是否互为邻区
	 */
	private int isNeighbour;

	/**
	 * 网元所有邻区记录
	 */
	private List<EnbNeighbourRecord> records;

	/**
	 * 网元单条邻区记录
	 */
	private EnbNeighbourRecord record;

	/**
	 * 网元邻区记录个数
	 */
	private int listLength;

	/**
	 * 当前版本需要显示的字段，形式"A,B,C"
	 */
	private String fieldLevelString = "";

	private String tableName = "T_CEL_NBRCEL" ;

	/**
	 * 异常
	 */
	private String error;

	/**
	 * 进行邻区参数表的增删改校验，如果校验通过，则操作直接生效
	 * 
	 * @return
	 */
	public String checkNbrcelBizRules() {
		BizErrorModel errorModel = new BizErrorModel();
		try {
			String compatibleTableName = tableName;
			if (compatibleTableName.contains("-")) {
				String[] array = compatibleTableName.split("-");
				compatibleTableName = array[0];
			}
			EnbNeighbourFacade facade = Util
					.getFacadeInstance(EnbNeighbourFacade.class);
			// 增删改需要的condition
			EnbNeighbourRecord condition = getCondition(parameters, isNeighbour);
			// 获取当前版本支持的字段
			List<String> supportedFields = EnbVersionBizConfigCache
					.getInstance().getFields(getEnbType(moId), enbVersion,
							compatibleTableName);
			// 根据moId获取16进制的enbHexId
			String enbHexId = getEnbHexId(moId);
			if ("add".equals(operType)) {
				// 过滤掉当前版本不支持的字段值
				filterUnsupportedFields(supportedFields,
						condition.getBizRecord());
				facade.addNeighbour(OperObject.createEnbOperObject(enbHexId),
						moId, condition);
			}
			if ("config".equals(operType)) {
				// 过滤掉当前版本不支持的字段值
				filterUnsupportedFields(supportedFields,
						condition.getBizRecord());
				facade.updateNeighbour(
						OperObject.createEnbOperObject(enbHexId), moId,
						condition);
			}
			if ("delete".equals(operType)) {
				facade.deleteNeighbour(
						OperObject.createEnbOperObject(enbHexId), moId,
						condition);
			}
			if ("multiDelete".equals(operType)) {
				String[] strFir = parameters.split(";");
				for (int i = 0; i < strFir.length; i++) {
					Map<String, String> map = new LinkedHashMap<String, String>();
					String[] strSec = strFir[i].split(",");
					for (int j = 0; j < strSec.length; j++) {
						String[] str = strSec[j].split("=");
						map.put(str[0], str[1]);
					}
					EnbNeighbourRecord deleteCondition = new EnbNeighbourRecord();
					XBizRecord record = new XBizRecord();
					Iterator<String> iter = map.keySet().iterator();
					while (iter.hasNext()) {
						String key = iter.next();
						record.addField(makeXBizField(key, map.get(key)));
					}
					deleteCondition.setBizRecord(record);
					deleteCondition.setIsNeighbour(isNeighbour);
					facade.deleteNeighbour(
							OperObject.createEnbOperObject(enbHexId), moId,
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
		JSONObject json = new JSONObject();
		json.put("errorModel", JSONObject.fromObject(errorModel));
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 查询该网元所有邻区记录
	 * 
	 * @return
	 */
	public String queryNbrcelRecords_xw7400() {
		try {
			String compatibleTableName = tableName;
			if (compatibleTableName.contains("-")) {
				String[] array = compatibleTableName.split("-");
				compatibleTableName = array[0];
			}
			EnbNeighbourFacade facade = Util
					.getFacadeInstance(EnbNeighbourFacade.class);
			// 生成支持的字段列表
			// 获取角色roleId
			int roleId = getCurrentUser().getRoleId();
			// 获取需要显示的字段
			Map<String, List<String>> fieldMap = FieldLevelCache.getInstance()
					.getFieldMapBy(roleId, getEnbType(moId), enbVersion,
							compatibleTableName);
			fieldLevelString = mapToJson(fieldMap);

			records = facade.queryNeighbourRecords(moId);
			listLength = records.size();
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
		}
		return tableName;
	}

	/**
	 * 查询单条邻区记录
	 * 
	 * @return
	 */
	public String querySingleNbrcelRecord_xw7400() {
		try {
			String compatibleTableName = tableName;
			if (compatibleTableName.contains("-")) {
				String[] array = compatibleTableName.split("-");
				compatibleTableName = array[0];
			}
			EnbNeighbourFacade facade = Util
					.getFacadeInstance(EnbNeighbourFacade.class);
			// 生成支持的字段列表
			// 获取角色roleId
			int roleId = getCurrentUser().getRoleId();
			// 获取需要显示的字段
			Map<String, List<String>> fieldMap = FieldLevelCache.getInstance()
					.getFieldMapBy(roleId, getEnbType(moId), enbVersion,
							compatibleTableName);
			fieldLevelString = mapToJson(fieldMap);
			XBizRecord bizRecord = new XBizRecord();
			Map<String, String> map = getMap(parameters);
			Iterator<String> iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				bizRecord.addField(makeXBizField(key, map.get(key)));
			}
			record = facade.queryNeighbourRecord(moId, bizRecord);
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
		}
		return tableName;
	}

	/**
	 * 判断某个基站是否在系统内已存在 0:否 1：是
	 * 
	 * @return
	 */
	public String isExistingInSystem() {
		int flag = 0;
		try {
			EnbBasicFacade facade = Util
					.getFacadeInstance(EnbBasicFacade.class);
			List<Enb> enbList = facade.queryAllEnb();
			// 是否存在
			if (enbHexId != null) {
				for (Enb enb : enbList) {
					if (enbHexId.equals(enb.getHexEnbId())) {
						flag = 1;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.put("flag", flag);
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 根据enbHexId获取该网元小区列表
	 * 
	 * @return
	 */
	public String getCellListByEnbHexId() {
		List<XBizRecord> cellList = null;
		try {
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			long moId = getEnbMoId(enbHexId);
			XBizTable xBizTable = facade.queryFromEms(moId, "T_CEL_PARA", null);
			cellList = xBizTable.getRecords();
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.put("cellList", JSONArray.fromObject(cellList));
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 根据cellId获取小区数据
	 * 
	 * @return
	 */
	public String getCellDataByCellId() {
		XBizRecord record = null;
		try {
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			long moId = getEnbMoId(enbHexId);
			XBizRecord condition = new XBizRecord();
			Map<String, String> map = getMap(parameters);
			Iterator<String> iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				condition.addField(makeXBizField(key, map.get(key)));
			}
			XBizTable xBizTable = facade.queryFromEms(moId, "T_CEL_PARA",
					condition);
			record = xBizTable.getRecords().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.put("record", JSONObject.fromObject(record));
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 获取增删改所需的enbNeighbourRecord
	 * 
	 * @return
	 */
	private EnbNeighbourRecord getCondition(String parameters, int isNeighbour) {
		EnbNeighbourRecord enbNeighbourRecord = new EnbNeighbourRecord();
		XBizRecord bizRecord = new XBizRecord();
		Map<String, String> map = getMap(parameters);
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			bizRecord.addField(makeXBizField(key, map.get(key)));
		}
		enbNeighbourRecord.setBizRecord(bizRecord);
		enbNeighbourRecord.setIsNeighbour(isNeighbour);
		return enbNeighbourRecord;
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
	 * 根据moId获取enbHexId
	 * 
	 * @param moId
	 * @return
	 */
	public String getEnbHexIdAsync() {
		String enbHexId = "";
		try {
			EnbBasicFacade facade = Util
					.getFacadeInstance(EnbBasicFacade.class);
			enbHexId = facade.queryByMoId(moId).getHexEnbId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.put("enbHexId", enbHexId);
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 根据16进制的enbHexId获取moId
	 * 
	 * @param enbHexId
	 * @return
	 * @throws Exception
	 */
	private long getEnbMoId(String enbHexId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		long enbId = Long.valueOf(enbHexId, 16);
		return facade.queryByEnbId(enbId).getMoId();
	}

	/**
	 * 获取当前用户
	 * 
	 * @return
	 */
	private LoginUser getCurrentUser() {
		return (LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT);
	}

	/**
	 * 字段等级Map转换json串
	 * 
	 * @param fieldMap
	 * @return
	 */
	private String mapToJson(Map<String, List<String>> fieldMap) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.putAll(fieldMap);
		return jsonObject.toString();
	}

	private int getEnbType(long moId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		return facade.queryByMoId(moId).getEnbType();
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public List<EnbNeighbourRecord> getRecords() {
		return records;
	}

	public void setRecords(List<EnbNeighbourRecord> records) {
		this.records = records;
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

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public int getIsNeighbour() {
		return isNeighbour;
	}

	public void setIsNeighbour(int isNeighbour) {
		this.isNeighbour = isNeighbour;
	}

	public String getFieldLevelString() {
		return fieldLevelString;
	}

	public void setFieldLevelString(String fieldLevelString) {
		this.fieldLevelString = fieldLevelString;
	}

	public int getListLength() {
		return listLength;
	}

	public void setListLength(int listLength) {
		this.listLength = listLength;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getEnbHexId() {
		return enbHexId;
	}

	public void setEnbHexId(String enbHexId) {
		this.enbHexId = enbHexId;
	}

	public EnbNeighbourRecord getRecord() {
		return record;
	}

	public void setRecord(EnbNeighbourRecord record) {
		this.record = record;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	

}
