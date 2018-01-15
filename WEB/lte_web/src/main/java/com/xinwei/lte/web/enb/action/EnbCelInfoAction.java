package com.xinwei.lte.web.enb.action;

import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.core.model.EnbCellStart;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;

/**
 * 开站向导
 * 
 * @author zhangqiang
 * 
 */
public class EnbCelInfoAction extends ActionSupport {

	// moId
	private long moId;
	// 小区ID
	private int cid;
	// 小区IDs
	private String cids;
	// 协议版本号
	private String enbVersion;
	// 参数模型
	private EnbCellStart enbCellStart;
	// 记录个数
	private int listLength;
	// 多条记录
	private List<XBizRecord> records = new LinkedList<XBizRecord>();
	// 单条记录
	private XBizRecord record = new XBizRecord();

	// 异常
	private String error;

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public String queryCelInfoList() {
		int enbType = 0;
		try {
			enbType = getEnbType(moId);
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			records = facade.querySceneData(moId).getRecords();
			listLength = records.size();
		} catch (Exception e) {
			error = e.getLocalizedMessage();
			e.printStackTrace();
			return ERROR;
		}
		if (enbType == 0) {
			return "xw7400";
		} else {
			return "xw7102";
		}

	}

	/**
	 * 查询单条
	 * 
	 * @return
	 */
	public String queryCelInfo() {
		int enbType = 0;
		try {
			enbType = getEnbType(moId);
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			record = facade.querySceneDataByCid(moId, cid);

		} catch (Exception e) {
			error = e.getLocalizedMessage();
			e.printStackTrace();
			return ERROR;
		}
		if (enbType == 0) {
			return "xw7400";
		} else {
			return "xw7102";
		}
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public String addCelInfo() {
		JSONObject json = new JSONObject();
		try {
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			facade.addSence(moId, enbCellStart);
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
	 * 修改
	 * 
	 * @return
	 */
	public String updateCelInfo() {
		JSONObject json = new JSONObject();
		try {
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			facade.updateSence(moId, enbCellStart);
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
	 * 删除
	 * 
	 * @return
	 */
	public String deleteCelInfo() {
		JSONObject json = new JSONObject();
		try {
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			facade.deleteScene(moId, cid);
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
	 * 删除
	 * 
	 * @return
	 */
	public String batchDeleteCelInfo() {
		JSONObject json = new JSONObject();
		try {
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			String[] cidArray = cids.split(",");
			for (String cid : cidArray) {
				facade.deleteScene(moId, Integer.valueOf(cid));
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
	 * 跳转到新增
	 * 
	 * @return
	 */
	public String turnToCelInfoAdd() {
		int enbType = 0;
		try {
			enbType = getEnbType(moId);
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
	 * 查询RRU单板(拓扑号)
	 * 
	 * @return
	 */
	public String queryTopoNums() {
		JSONObject json = new JSONObject();
		try {
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			XBizTable xBizTable = facade.queryFromEms(moId, "T_TOPO",
					new XBizRecord());
			json.put("status", 0);
			json.put("message", xBizTable.getRecords());
		} catch (Exception e) {
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
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

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getCids() {
		return cids;
	}

	public void setCids(String cids) {
		this.cids = cids;
	}

	public String getEnbVersion() {
		return enbVersion;
	}

	public void setEnbVersion(String enbVersion) {
		this.enbVersion = enbVersion;
	}

	public EnbCellStart getEnbCellStart() {
		return enbCellStart;
	}

	public void setEnbCellStart(EnbCellStart enbCellStart) {
		this.enbCellStart = enbCellStart;
	}

	public int getListLength() {
		return listLength;
	}

	public void setListLength(int listLength) {
		this.listLength = listLength;
	}

	public List<XBizRecord> getRecords() {
		return records;
	}

	public void setRecords(List<XBizRecord> records) {
		this.records = records;
	}

	public XBizRecord getRecord() {
		return record;
	}

	public void setRecord(XBizRecord record) {
		this.record = record;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
