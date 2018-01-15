package com.xinwei.lte.web.enb.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.cache.EnbVersionBizConfigCache;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.core.model.EnbCellStart;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbSimplifyConfigFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.minas.micro.core.facade.XMicroBizConfigFacade;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;

public class CellConfigGuideAction extends ActionSupport {

	private long moId;
	// 8位、16进制的基站标识
	private String enbHexId;
	// 协议版本号
	private String enbVersion;
	// 操作员级别ID
	private int roleId;
	// 可供新增或修改的RRU单板的拓扑号集合
	private List<Integer> topoList = new ArrayList<Integer>();
	// RRU光口号（拓扑号）
	private int fiberPort;
	// 新增删除单板时所需的单板信息
	private String parameters;
	// 新增修改删除小区向导时所需要的向导信息
	private EnbCellStart enbCellStart;
	// 单条记录
	private XBizRecord record = new XBizRecord();
	// 小区标识
	private int cid;
	// 基站类型
	private int enbType;

	private static final String tableName_topo = "T_TOPO";

	private static final String tableName_board = "T_BOARD";

	/**
	 * 跳转到新增
	 * 
	 * @return
	 */
	public String turnCellAddGuide() {
		enbType = 0;
		try {
			enbType = getEnbType(moId);
			enbHexId = getEnbHexIdByMoId(moId);
			enbVersion = getEnbVersion(moId);
			//
			topoList.add(0);
			topoList.add(1);
			topoList.add(2);
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			List<XBizRecord> records = facade.querySceneData(moId).getRecords();
			for (XBizRecord record : records) {
				topoList.remove(Integer.valueOf(record.getFieldMap()
						.get("u8TopoNO").getValue()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (enbType == 0) {
			return "xw7400";
		} else {
			return "xw7102";
		}
	}

	/**
	 * 进行新增
	 * 
	 * @return
	 */
	public String addCellGuide() {
		JSONObject json = new JSONObject();
		json.put("status", 0);
		try {
			int enbType = getEnbType(moId);
			XEnbBizConfigFacade xmoFacade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			EnbSimplifyConfigFacade simFacade = Util
					.getFacadeInstance(EnbSimplifyConfigFacade.class);
			if (enbType == 0) {
				// 查询当前是否已配有目标RRU
				XBizTable xBizTable = xmoFacade.queryFromEms(moId,
						tableName_topo, new XBizRecord());
				List<Integer> topoIdList = new ArrayList<Integer>();
				for (XBizRecord record : xBizTable.getRecords()) {
					topoIdList.add(Integer.valueOf(record.getFieldMap()
							.get("u8TopoNO").getValue()));
				}
				if (!topoIdList.contains(Integer.valueOf(fiberPort))) {
					// 当前未配有目标RRU

					// 获取当前版本支持的字段
					List<String> supportedFields = EnbVersionBizConfigCache
							.getInstance().getFields(getEnbType(moId),
									enbVersion, tableName_board);
					XBizRecord condition = getCondition(parameters);
					String enbHexId = getEnbHexIdByMoId(moId);
					// 过滤掉当前版本不支持的字段值
					filterUnsupportedFields(supportedFields, condition);
					// 新增单板
					simFacade.addBoard(
							OperObject.createEnbOperObject(enbHexId), moId,
							condition, fiberPort);
				}
			}
			if (enbType == EnbTypeDD.XW7400) {
				// 宏站
				xmoFacade.addSence(moId, enbCellStart);
			} else if (enbType == EnbTypeDD.XW7102) {
				// 微站
				XMicroBizConfigFacade xMicroBizConfigFacade = Util
						.getFacadeInstance(XMicroBizConfigFacade.class);
				xMicroBizConfigFacade.addSence(moId, enbCellStart);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 进行删除
	 * 
	 * @return
	 */
	public String deleteCellGuide() {
		JSONObject json = new JSONObject();
		json.put("status", 0);
		try {
			int enbType = getEnbType(moId);
			XEnbBizConfigFacade xmoFacade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			XBizRecord cellInfo = xmoFacade.querySceneDataByCid(moId, cid);
			if (enbType == EnbTypeDD.XW7400) {
				// 宏站
				xmoFacade.deleteScene(moId, cid);
			} else if (enbType == EnbTypeDD.XW7102) {
				// 微站
				XMicroBizConfigFacade xMicroBizConfigFacade = Util
						.getFacadeInstance(XMicroBizConfigFacade.class);
				xMicroBizConfigFacade.deleteScene(moId, cid);
			}
			if (enbType == 0) {
				XBizTable xBizTable = xmoFacade.queryFromEms(moId,
						tableName_topo, new XBizRecord());
				for (XBizRecord record : xBizTable.getRecords()) {
					if (Integer.valueOf(record.getFieldMap().get("u8TopoNO")
							.getValue()) == Integer.valueOf(cellInfo
							.getFieldMap().get("u8TopoNO").getValue())) {
						parameters = "u8RackNO="
								+ record.getFieldMap().get("u8SRackNO")
										.getValue()
								+ ";u8ShelfNO="
								+ record.getFieldMap().get("u8SShelfNO")
										.getValue()
								+ ";u8SlotNO="
								+ record.getFieldMap().get("u8SSlotNO")
										.getValue();
						XBizRecord condition = getCondition(parameters);
						EnbSimplifyConfigFacade facade = Util
								.getFacadeInstance(EnbSimplifyConfigFacade.class);
						facade.deleteBoard(
								OperObject.createEnbOperObject(enbHexId), moId,
								condition);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 跳转至修改
	 * 
	 * @return
	 */
	public String turnCellModifyGuide() {
		try {
			enbType = getEnbType(moId);
			enbHexId = getEnbHexIdByMoId(moId);
			enbVersion = getEnbVersion(moId);
			//
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			XBizTable xBizTable = facade.queryFromEms(moId, tableName_topo,
					new XBizRecord());
			for (XBizRecord record : xBizTable.getRecords()) {
				topoList.add(Integer.valueOf(record.getFieldMap()
						.get("u8TopoNO").getValue()));
			}
			List<XBizRecord> records = facade.querySceneData(moId).getRecords();
			for (XBizRecord record : records) {
				topoList.remove(Integer.valueOf(record.getFieldMap()
						.get("u8TopoNO").getValue()));
			}
			record = facade.querySceneDataByCid(moId, cid);
			topoList.add(Integer.valueOf(record.getFieldMap().get("u8TopoNO")
					.getValue()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (enbType == 0) {
			return "xw7400";
		} else {
			return "xw7102";
		}
	}

	/**
	 * 进行修改
	 * 
	 * @return
	 */
	public String modifyCellGuide() {
		JSONObject json = new JSONObject();
		try {
			int enbType = getEnbType(moId);
			if (enbType == EnbTypeDD.XW7400) {
				// 宏站
				XEnbBizConfigFacade xmoFacade = Util
						.getFacadeInstance(XEnbBizConfigFacade.class);

				xmoFacade.updateSence(moId, enbCellStart);
			
			} else if (enbType == EnbTypeDD.XW7102) {
				// 微站
				XMicroBizConfigFacade xMicroBizConfigFacade = Util
						.getFacadeInstance(XMicroBizConfigFacade.class);
				xMicroBizConfigFacade.updateSence(moId, enbCellStart);
			}

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
	 * 检查是否有进入新增向导资格
	 * 
	 * @return
	 */
	public String checkCurrentCellNum() {
		JSONObject json = new JSONObject();
		json.put("status", 0);
		json.put("message", true);
		try {
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			List<XBizRecord> records = facade.querySceneData(moId).getRecords();
			if (records != null) {
				int enbType = getEnbType(moId);
				if (enbType == 0) {
					if (records.size() == 3) {
						json.put("message", false);
					}
				} else {
					if (records.size() == 1) {
						json.put("message", false);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	private int getEnbType(long moId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		return facade.queryByMoId(moId).getEnbType();
	}

	private String getEnbHexIdByMoId(long moId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		return facade.queryByMoId(moId).getHexEnbId();
	}

	private String getEnbVersion(long moId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		return facade.queryByMoId(moId).getProtocolVersion();
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

	public String getEnbVersion() {
		return enbVersion;
	}

	public void setEnbVersion(String enbVersion) {
		this.enbVersion = enbVersion;
	}

	public List<Integer> getTopoList() {
		return topoList;
	}

	public void setTopoList(List<Integer> topoList) {
		this.topoList = topoList;
	}

	public int getFiberPort() {
		return fiberPort;
	}

	public void setFiberPort(int fiberPort) {
		this.fiberPort = fiberPort;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public EnbCellStart getEnbCellStart() {
		return enbCellStart;
	}

	public void setEnbCellStart(EnbCellStart enbCellStart) {
		this.enbCellStart = enbCellStart;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public XBizRecord getRecord() {
		return record;
	}

	public void setRecord(XBizRecord record) {
		this.record = record;
	}

	public int getEnbType() {
		return enbType;
	}

	public void setEnbType(int enbType) {
		this.enbType = enbType;
	}

}
