package com.xinwei.lte.web.enb.action;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.cache.EnbVersionBizConfigCache;
import com.xinwei.lte.web.enb.cache.FieldLevelCache;
import com.xinwei.lte.web.enb.model.BizErrorModel;
import com.xinwei.lte.web.enb.model.OperType;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbSimplifyConfigFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.micro.core.facade.XMicroBizConfigFacade;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.system.action.web.WebConstants;

/**
 * 用于enb业务的增删改�?
 * 
 * @author zhangqiang
 * 
 */
public class QueryGenericEnbAction extends ActionSupport {

	/**
	 * MO编号（全局唯一,系统自动生成)
	 */
	private long moId;

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 操作类型
	 */
	private String operType;

	/**
	 * 关联�?
	 */
	private String referTable;

	/**
	 * 查询到的数据集合
	 */
	private List<XBizRecord> records = new LinkedList<XBizRecord>();

	/**
	 * 查询到的数据个数
	 */
	private int listLength;

	/**
	 * 查询到的单体数据
	 */
	private XBizRecord record = new XBizRecord();

	/**
	 * 关联表数据集�?
	 */
	private List<XBizRecord> referRecords = new LinkedList<XBizRecord>();

	/**
	 * 小区表关联topo表中的主键（临时编码，需要合并）
	 */
	private List<XBizRecord> referTopoRecords = new LinkedList<XBizRecord>();

	/**
	 * 小区关联的topo�?临时编码，需要合�?
	 */
	private String referTableTopo;

	/**
	 * IPV4表关联的vlan表的主键（临时编码，需要合并）
	 */
	private List<XBizRecord> referVlanRecords = new LinkedList<XBizRecord>();

	/**
	 * IPV4表关联的vlan表
	 */
	private String referTableVlan;

	/**
	 * 前端传来的参数数组的字符串格�?
	 */
	private String parameters;

	/**
	 * 基站ID
	 */
	private String enbHexId;

	/**
	 * 基站名称
	 */
	private String enbName;

	/**
	 * eNB软件版本
	 */
	private String enbVersion;

	/**
	 * 是否是开站参数中的表 �?0：否 1：是
	 */
	private int isActiveEnbTable;

	/**
	 * 当前版本需要显示的字段，按级别分类的Map
	 */
	private String fieldLevelString = "";

	/**
	 * 异常
	 */
	private String error;

	/**
	 * 特殊校验
	 */
	private BizErrorModel errorModel = new BizErrorModel();

	/**
	 * 网管IP地址和端�?
	 */
	private InetSocketAddress address;

	private int vlanFlag = 0;

	private String vlan_u8VlanTag;

	private String vlan_u16VlanId;

	private String vlan_u8VlanPri;

	/**
	 * 异步查询enbBiz
	 * 
	 * @return
	 */
	public String queryAsyncEnbBiz() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			// 版本兼容性处�?
			String compatibleTableName = tableName;
			if (compatibleTableName.contains("-")) {
				String[] array = compatibleTableName.split("-");
				compatibleTableName = array[0];
			}
			out = response.getWriter();
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			XEnbBizConfigFacade facade = MinasSession.getInstance().getFacade(
					sessionId, XEnbBizConfigFacade.class);
			XBizRecord condition = new XBizRecord();

			// 判断操作类型：查、增、改、删
			if (!operType.equals(OperType.SELECT)
					&& !operType.equals(OperType.MULTIDELETE)) {
				// 构建增、改、删的condition
				Map<String, String> map = getMap();
				Iterator<String> iter = map.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					condition.addField(makeXBizField(key, map.get(key)));
				}
			}
			Enb enb = queryEnbByMoId(moId);
			// 获取当前版本支持的字�?
			List<String> supportedFields = EnbVersionBizConfigCache
					.getInstance().getFields(enb.getEnbType(), enbVersion,
							compatibleTableName);
			// 增加
			if (operType.equals(OperType.ADD)) {
				// 过滤掉当前版本不支持的字段�?
				filterUnsupportedFields(supportedFields, condition);
				if (enb.getEnbType() == EnbTypeDD.XW7400) {
					// 宏站
					facade.add(moId, compatibleTableName, condition);
				} else if (enb.getEnbType() == EnbTypeDD.XW7102) {
					// 微站
					XMicroBizConfigFacade xMicroBizConfigFacade = Util
							.getFacadeInstance(XMicroBizConfigFacade.class);
					xMicroBizConfigFacade.add(moId, compatibleTableName,
							condition);
				}
			}
			// 配置
			if (operType.equals(OperType.CONFIG)) {
				// 过滤掉当前版本不支持的字段�?
				filterUnsupportedFields(supportedFields, condition);
				// 小区参数表需要特殊处�?
				if (compatibleTableName
						.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
					handleCellPara(sessionId, condition, ActionTypeDD.MODIFY);
				} else {
					if (enb.getEnbType() == EnbTypeDD.XW7400) {
						// 宏站
						facade.update(moId, compatibleTableName, condition);
					} else if (enb.getEnbType() == EnbTypeDD.XW7102) {
						// 微站
						XMicroBizConfigFacade xMicroBizConfigFacade = Util
								.getFacadeInstance(XMicroBizConfigFacade.class);
						xMicroBizConfigFacade.update(moId, compatibleTableName,
								condition);
					}
				}
			}
			// 删除
			if (operType.equals(OperType.DELETE)) {
				// 小区参数表需要特殊处�?
				if (compatibleTableName
						.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
					handleCellPara(sessionId, condition, ActionTypeDD.DELETE);
				} else {
					if (enb.getEnbType() == EnbTypeDD.XW7400) {
						// 宏站
						facade.delete(moId, compatibleTableName, condition);
					} else if (enb.getEnbType() == EnbTypeDD.XW7102) {
						// 微站
						XMicroBizConfigFacade xMicroBizConfigFacade = Util
								.getFacadeInstance(XMicroBizConfigFacade.class);
						xMicroBizConfigFacade.delete(moId, compatibleTableName,
								condition);
					}
				}
			}
			// 批量删除
			if (operType.equals(OperType.MULTIDELETE)) {
				XMicroBizConfigFacade xMicroBizConfigFacade = Util
						.getFacadeInstance(XMicroBizConfigFacade.class);
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
					// 小区参数表需要特殊处�?
					if (compatibleTableName
							.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
						handleCellPara(sessionId, deleteCondition,
								ActionTypeDD.DELETE);
					} else {
						if (enb.getEnbType() == EnbTypeDD.XW7400) {
							// 宏站
							facade.delete(moId, compatibleTableName,
									deleteCondition);
						} else if (enb.getEnbType() == EnbTypeDD.XW7102) {
							// 微站
							xMicroBizConfigFacade.delete(moId,
									compatibleTableName, deleteCondition);
						}
					}
				}
			}

			// 如果是动态表，向基站查询结果
			if (isDynamicTable(compatibleTableName)) {
				XBizTable xBizTable = facade.queryFromNe(moId,
						compatibleTableName);
				records = xBizTable.getRecords();
				listLength = records.size();
			} else {
				XBizTable xBizTable = facade.queryFromEms(moId,
						compatibleTableName, new XBizRecord());
				records = xBizTable.getRecords();
				listLength = records.size();
			}
		} catch (BizException e) {
			String para = String.valueOf(e.getTarget());
			if (para != null && !"".equals(para) && !"null".equals(para)) {
				errorModel.setError(e.getLocalizedMessage());
				errorModel.setErrorEntity(para);
			} else {
				String error = e.getLocalizedMessage();
				String[] errorString = error.split("#");
				errorModel.setError(errorString[1]);
				errorModel.setErrorEntity(errorString[0]);
			}
			errorModel.setErrorType(0);
		} catch (Exception e) {
			String error = e.getLocalizedMessage();
			errorModel.setError(error);
		} finally {
			if (out != null) {
				JSONObject json = new JSONObject();
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(errorModel);
				json.put("errorModel", object);
				out.println(json.toString());
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	/**
	 * 小区参数表特殊处�?
	 * 
	 * @param sessionId
	 * @param bizRecord
	 * @param actionType
	 * @throws Exception
	 */
	private void handleCellPara(String sessionId, XBizRecord bizRecord,
			String actionType) throws Exception {
		EnbSimplifyConfigFacade configFacade = MinasSession.getInstance()
				.getFacade(sessionId, EnbSimplifyConfigFacade.class);
		if (actionType.equals(ActionTypeDD.DELETE)) {
			configFacade.deleteCellPara(
					OperObject.createEnbOperObject(getEnbHexIdByMoId(moId)),
					moId, bizRecord);
		} else if (actionType.equals(ActionTypeDD.MODIFY)) {
			configFacade.updateCellPara(
					OperObject.createEnbOperObject(getEnbHexIdByMoId(moId)),
					moId, bizRecord);
		}
	}

	/**
	 * 判断是否是动态表
	 * 
	 * @param tableName
	 * @return
	 */
	private boolean isDynamicTable(String tableName) {
		// TODO: 当新增动态表时，需要修改如下逻辑
		return tableName.equals(EnbConstantUtils.TABLE_NAME_T_SWPKG)
				|| tableName.equals(EnbConstantUtils.TABLE_NAME_T_SWINFO)
				|| tableName.equals(EnbConstantUtils.TABLE_NAME_T_DEVICE);
	}

	/**
	 * 查询enbBiz
	 * 
	 * @return
	 */
	public String queryEnbBiz() {
		// 版本兼容性处�?
		String compatibleTableName = tableName;
		Enb enb;
		try {
			if (compatibleTableName.contains("-")) {
				String[] array = compatibleTableName.split("-");
				compatibleTableName = array[0];
			}
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			XEnbBizConfigFacade facade = MinasSession.getInstance().getFacade(
					sessionId, XEnbBizConfigFacade.class);
			XBizRecord condition = new XBizRecord();

			// 判断操作类型：查、增、改、删
			if (!operType.equals(OperType.SELECT)
					&& !operType.equals(OperType.MULTIDELETE)) {
				// 构建增、改、删的condition
				Map<String, String> map = getMap();
				Iterator<String> iter = map.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					condition.addField(makeXBizField(key, map.get(key)));
				}
			}
			enb = queryEnbByMoId(moId);
			// 获取当前版本支持的字�?
			List<String> supportedFields = EnbVersionBizConfigCache
					.getInstance().getFields(enb.getEnbType(), enbVersion,
							compatibleTableName);
			// 增加
			if (operType.equals(OperType.ADD)) {
				// 过滤掉当前版本不支持的字段�?
				filterUnsupportedFields(supportedFields, condition);
				if (enb.getEnbType() == EnbTypeDD.XW7400) {
					// 宏站
					facade.add(moId, compatibleTableName, condition);
				} else if (enb.getEnbType() == EnbTypeDD.XW7102) {
					// 微站
					XMicroBizConfigFacade xMicroBizConfigFacade = Util
							.getFacadeInstance(XMicroBizConfigFacade.class);
					xMicroBizConfigFacade.add(moId, compatibleTableName,
							condition);
				}
			}
			// 配置
			if (operType.equals(OperType.CONFIG)) {
				// 过滤掉当前版本不支持的字段�?
				filterUnsupportedFields(supportedFields, condition);
				// 小区参数表需要特殊处�?
				if (compatibleTableName
						.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
					handleCellPara(sessionId, condition, ActionTypeDD.MODIFY);
				} else {
					if (enb.getEnbType() == EnbTypeDD.XW7400) {
						// 宏站
						facade.update(moId, compatibleTableName, condition);
					} else if (enb.getEnbType() == EnbTypeDD.XW7102) {
						// 微站
						XMicroBizConfigFacade xMicroBizConfigFacade = Util
								.getFacadeInstance(XMicroBizConfigFacade.class);
						xMicroBizConfigFacade.update(moId, compatibleTableName,
								condition);
					}
				}
			}
			// 删除
			if (operType.equals(OperType.DELETE)) {
				// 小区参数表需要特殊处�?
				if (compatibleTableName
						.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
					handleCellPara(sessionId, condition, ActionTypeDD.DELETE);
				} else {
					if (enb.getEnbType() == EnbTypeDD.XW7400) {
						// 宏站
						facade.delete(moId, compatibleTableName, condition);
					} else if (enb.getEnbType() == EnbTypeDD.XW7102) {
						// 微站
						XMicroBizConfigFacade xMicroBizConfigFacade = Util
								.getFacadeInstance(XMicroBizConfigFacade.class);
						xMicroBizConfigFacade.delete(moId, compatibleTableName,
								condition);
					}
				}
			}
			// 批量删除
			if (operType.equals(OperType.MULTIDELETE)) {
				XMicroBizConfigFacade xMicroBizConfigFacade = Util
				.getFacadeInstance(XMicroBizConfigFacade.class);
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
					// 小区参数表需要特殊处�?
					if (compatibleTableName
							.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
						handleCellPara(sessionId, deleteCondition,
								ActionTypeDD.DELETE);
					} else {
						if (enb.getEnbType() == EnbTypeDD.XW7400) {
							// 宏站
							facade.delete(moId, compatibleTableName, deleteCondition);
						} else if (enb.getEnbType() == EnbTypeDD.XW7102) {
							// 微站
							xMicroBizConfigFacade.delete(moId, compatibleTableName,
									deleteCondition);
						}
					}
				}
			}
			// 生成支持的字段列�?
			// 获取角色roleId
			int roleId = getCurrentUser().getRoleId();
			// 获取需要显示的字段
			Map<String, List<String>> fieldMap = FieldLevelCache.getInstance()
					.getFieldMapBy(roleId, getEnbType(moId), enbVersion,
							compatibleTableName);
			fieldLevelString = mapToJson(fieldMap);

			// 如果是动态表，向基站查询结果
			if (isDynamicTable(compatibleTableName)) {
				XBizTable xBizTable = facade.queryFromNe(moId,
						compatibleTableName);
				records = xBizTable.getRecords();
				listLength = records.size();
			} else {
				XBizTable xBizTable = facade.queryFromEms(moId,
						compatibleTableName, new XBizRecord());
				records = xBizTable.getRecords();
				listLength = records.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		// int enbTypeId = enb.getEnbType();
		// if (enb == null || EnbTypeDD.isXW7400(enbTypeId)) {
		// return tableName;
		// }
		// else {
		// String enbTypeName =
		// EnbTypeDD.getTypeNameById(enbTypeId).toLowerCase();
		// return enbTypeName + "-" + tableName;
		// }
		return tableName;
	}

	private Enb queryEnbByMoId(Long moId) {
		try {
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);
			return facade.queryByMoId(moId);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 字段等级Map转换json�?
	 * 
	 * @param fieldMap
	 * @return
	 */
	private String mapToJson(Map<String, List<String>> fieldMap) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.putAll(fieldMap);
		return jsonObject.toString();
	}

	/**
	 * 异步查询基站在线状�?
	 * 
	 * @return
	 */
	public String queryAsynEnbState() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		int enbState = 0;
		try {
			out = response.getWriter();
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);
			enbState = facade.queryByMoId(moId).getRegisterState();
		} catch (Exception e) {

		} finally {
			if (out != null) {
				JSONObject json = new JSONObject();
				json.put("enbState", enbState);
				out.println(json.toString());
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	/**
	 * 跳转至enbBiz的新增页�?
	 * 
	 * @return
	 */
	public String turnAddEnbBiz() {
		// 版本兼容性处�?
		String compatibleTableName = tableName;
		try {
			if (compatibleTableName.contains("-")) {
				String[] array = compatibleTableName.split("-");
				compatibleTableName = array[0];
			}

			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			XEnbBizConfigFacade facade = MinasSession.getInstance().getFacade(
					sessionId, XEnbBizConfigFacade.class);
			// 获取角色roleId
			int roleId = getCurrentUser().getRoleId();

			Enb enb = queryEnbByMoId(moId);
			// 获取需要显示的字段
			// 获取需要显示的字段
			Map<String, List<String>> fieldMap = FieldLevelCache.getInstance()
					.getFieldMapBy(roleId, getEnbType(moId), enbVersion,
							compatibleTableName);
			fieldLevelString = mapToJson(fieldMap);

			// 判断是否有关联表
			if (referTable != null && !referTable.equals("")) {
				XBizTable xBizTable = facade.queryFromEms(moId, referTable,
						new XBizRecord());
				// 关联表数�?
				referRecords = xBizTable.getRecords();
			}
			// 小区表关联topo表（临时编码，需要合并）
			if (referTableTopo != null && !referTableTopo.equals("")) {
				XBizTable xBizTable = facade.queryFromEms(moId, referTableTopo,
						new XBizRecord());
				// 关联的TOPO表数�?
				referTopoRecords = xBizTable.getRecords();
			}
			// ipv4表关联VLAN表
			if (referTableVlan != null && !referTableVlan.equals("")) {
				XBizTable xBizTable = facade.queryFromEms(moId, referTableVlan,
						new XBizRecord());
				// 关联的vlan表数�?
				referVlanRecords = xBizTable.getRecords();
			}
			if (compatibleTableName.equals("T_OMC")) {
				address = facade.queryEmsNetAddress(moId);
			}
			if (compatibleTableName.equals("T_VLAN")) {
				XBizTable xBizTable = facade.queryFromEms(moId,
						compatibleTableName, new XBizRecord());
				List<XBizRecord> record = xBizTable.getRecords();
				;
				if (record != null && record.size() > 0) {
					vlanFlag = 1;
					vlan_u16VlanId = record.get(0).getFieldMap()
							.get("u16VlanId").getValue();
					vlan_u8VlanTag = record.get(0).getFieldMap()
							.get("u8VlanTag").getValue();
					vlan_u8VlanPri = record.get(0).getFieldMap()
							.get("u8VlanPri").getValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return tableName;
	}

	/**
	 * 跳转至enbBiz的配置页�?
	 * 
	 * @return
	 */
	public String turnConfigEnbBiz() {
		// 版本兼容性处�?
		String compatibleTableName = tableName;
		try {
			if (compatibleTableName.contains("-")) {
				String[] array = compatibleTableName.split("-");
				compatibleTableName = array[0];
			}

			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			XEnbBizConfigFacade facade = MinasSession.getInstance().getFacade(
					sessionId, XEnbBizConfigFacade.class);
			// 获取角色roleId
			int roleId = getCurrentUser().getRoleId();

			Enb enb = queryEnbByMoId(moId);
			// 获取需要显示的字段
			Map<String, List<String>> fieldMap = FieldLevelCache.getInstance()
					.getFieldMapBy(roleId, getEnbType(moId), enbVersion,
							compatibleTableName);
			fieldLevelString = mapToJson(fieldMap);

			// 判断是否有关联表
			if (referTable != null && !referTable.equals("")) {
				XBizTable xBizTable = facade.queryFromEms(moId, referTable,
						new XBizRecord());
				// 关联表数�?
				referRecords = xBizTable.getRecords();
			}
			// 小区表关联topo表（临时编码，需要合并）
			if (referTableTopo != null && !referTableTopo.equals("")) {
				XBizTable xBizTable = facade.queryFromEms(moId, referTableTopo,
						new XBizRecord());
				// 关联的TOPO表数�?
				referTopoRecords = xBizTable.getRecords();
			}
			// ipv4表关联VLAN表
			if (referTableVlan != null && !referTableVlan.equals("")) {
				XBizTable xBizTable = facade.queryFromEms(moId, referTableVlan,
						new XBizRecord());
				// 关联的vlan表数�?
				referVlanRecords = xBizTable.getRecords();
			}
			// 构建查询单体数据条件
			XBizRecord condition = new XBizRecord();
			Map<String, String> map = getMap();
			Iterator<String> iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				condition.addField(makeXBizField(key, map.get(key)));
			}
			// 查询出结�?
			XBizTable xBizTable = facade.queryFromEms(moId,
					compatibleTableName, condition);
			record = xBizTable.getRecords().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return tableName;
	}

	/**
	 * 获取一个空闲的小区物理ID
	 * 
	 * @return
	 */
	public String getFreePci() {
		JSONObject json = new JSONObject();
		try {
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			int pci = facade.getFreePci(getEnbType(moId), enbVersion);
			json.put("status", 0);
			json.put("message", pci);
		} catch (Exception e) {
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 获取一个空闲的逻辑根序列
	 * 
	 * @return
	 */
	public String getFreeRsi() {
		JSONObject json = new JSONObject();
		try {
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			int rsi = facade.getFreeRsi(getEnbType(moId), enbVersion);
			json.put("status", 0);
			json.put("message", rsi);
		} catch (Exception e) {
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
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
	 * 由获取的parameters参数获得键值对
	 * 
	 * @return
	 */
	public Map<String, String> getMap() {
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
	 * 向im5000服务器发送数据时需要按照基站当前版本支持的字段进行发�?
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

	// /**
	// * 按照所有版本中字段的全集补充数据中缺失的字段�?
	// *
	// * @param bizTable
	// */
	// private void makeFullData(XBizTable bizTable) {
	// // TODO delete
	// List<XBizRecord> records = bizTable.getRecords();
	// if (records == null || records.isEmpty())
	// return;
	// Set<String> fullFields = getFullFields(bizTable.getTableName());
	// for (XBizRecord bizRecord : records) {
	// for (String fieldName : fullFields) {
	// XBizField bizField = bizRecord.getFieldBy(fieldName);
	// if (bizField == null) {
	// bizRecord.addField(new XBizField(fieldName, ""));
	// }
	// }
	// }
	//
	// }

	// /**
	// * 获取所有版本中字段的全�?
	// *
	// * @param tableName
	// * @return
	// */
	// private Set<String> getFullFields(String tableName) {
	// Set<String> versions = EnbVersionBizConfigCache.getInstance()
	// .getSupportedVersions();
	// Set<String> fullFields = new HashSet<String>();
	// for (String version : versions) {
	// List<String> fieldNames = EnbVersionBizConfigCache.getInstance()
	// .getFields(version, tableName);
	// fullFields.addAll(fieldNames);
	// }
	// return fullFields;
	// }

	/**
	 * 获取当前用户
	 * 
	 * @return
	 */
	private LoginUser getCurrentUser() {
		return (LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT);
	}

	private int getEnbType(long moId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		return facade.queryByMoId(moId).getEnbType();
	}

	private String getEnbHexIdByMoId(long moId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		return facade.queryByMoId(moId).getHexEnbId();
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getReferTable() {
		return referTable;
	}

	public void setReferTable(String referTable) {
		this.referTable = referTable;
	}

	public List<XBizRecord> getRecords() {
		return records;
	}

	public void setRecords(List<XBizRecord> records) {
		this.records = records;
	}

	public List<XBizRecord> getReferRecords() {
		return referRecords;
	}

	public void setReferRecords(List<XBizRecord> referRecords) {
		this.referRecords = referRecords;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public XBizRecord getRecord() {
		return record;
	}

	public void setRecord(XBizRecord record) {
		this.record = record;
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

	public String getEnbName() {
		return enbName;
	}

	public void setEnbName(String enbName) {
		this.enbName = enbName;
	}

	/**
	 * @param enbVersion
	 *            the enbVersion to set
	 */
	public void setEnbVersion(String enbVersion) {
		this.enbVersion = enbVersion;
	}

	/**
	 * @return the enbVersion
	 */
	public String getEnbVersion() {
		return enbVersion;
	}

	public int getIsActiveEnbTable() {
		return isActiveEnbTable;
	}

	public void setIsActiveEnbTable(int isActiveEnbTable) {
		this.isActiveEnbTable = isActiveEnbTable;
	}

	public String getFieldLevelString() {
		return fieldLevelString;
	}

	public void setFieldLevelString(String fieldLevelString) {
		this.fieldLevelString = fieldLevelString;
	}

	public BizErrorModel getErrorModel() {
		return errorModel;
	}

	public void setErrorModel(BizErrorModel errorModel) {
		this.errorModel = errorModel;
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}

	public List<XBizRecord> getReferTopoRecords() {
		return referTopoRecords;
	}

	public void setReferTopoRecords(List<XBizRecord> referTopoRecords) {
		this.referTopoRecords = referTopoRecords;
	}

	public String getReferTableTopo() {
		return referTableTopo;
	}

	public void setReferTableTopo(String referTableTopo) {
		this.referTableTopo = referTableTopo;
	}

	public String getVlan_u8VlanTag() {
		return vlan_u8VlanTag;
	}

	public void setVlan_u8VlanTag(String vlan_u8VlanTag) {
		this.vlan_u8VlanTag = vlan_u8VlanTag;
	}

	public String getVlan_u16VlanId() {
		return vlan_u16VlanId;
	}

	public void setVlan_u16VlanId(String vlan_u16VlanId) {
		this.vlan_u16VlanId = vlan_u16VlanId;
	}

	public String getVlan_u8VlanPri() {
		return vlan_u8VlanPri;
	}

	public void setVlan_u8VlanPri(String vlan_u8VlanPri) {
		this.vlan_u8VlanPri = vlan_u8VlanPri;
	}

	public int getVlanFlag() {
		return vlanFlag;
	}

	public void setVlanFlag(int vlanFlag) {
		this.vlanFlag = vlanFlag;
	}

	public String getReferTableVlan() {
		return referTableVlan;
	}

	public void setReferTableVlan(String referTableVlan) {
		this.referTableVlan = referTableVlan;
	}

	public List<XBizRecord> getReferVlanRecords() {
		return referVlanRecords;
	}

	public void setReferVlanRecords(List<XBizRecord> referVlanRecords) {
		this.referVlanRecords = referVlanRecords;
	}

}
