package com.xinwei.lte.web.enb.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.core.facade.secu.UserSecuFacade;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbAssetFacade;
import com.xinwei.minas.enb.core.model.EnbAsset;
import com.xinwei.minas.enb.core.model.EnbAssetCondition;
import com.xinwei.minas.enb.core.model.EnbAssetHistory;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.system.action.web.WebConstants;

public class EnbAssetManageAction extends ActionSupport {

	// 查询条件
	private EnbAssetCondition assetCondition;

	private long lastServeTime;

	private String remark;

	/**
	 * 查询当前资产
	 * @return
	 */
	public String queryCurrentAsset() {
		JSONObject json = new JSONObject();
		try {
			EnbAssetFacade facade = Util
					.getFacadeInstance(EnbAssetFacade.class);
			PagingData<EnbAsset> result = facade
					.queryByCondition(assetCondition);
			json.put("status", 0);
			json.put("message", JSONArray.fromObject(result.getResults()));
			json.put("currentPage", result.getCurrentPage());
			json.put("totalPage", result.calcTotalPageNum());
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 查询历史资产
	 * @return
	 */
	public String queryHistoryAsset() {
		JSONObject json = new JSONObject();
		try {
			EnbAssetFacade facade = Util
					.getFacadeInstance(EnbAssetFacade.class);
			PagingData<EnbAssetHistory> result = facade
					.queryHistoryByCondition(assetCondition);
			json.put("status", 0);
			json.put("message", JSONArray.fromObject(result.getResults()));
			json.put("currentPage", result.getCurrentPage());
			json.put("totalPage", result.calcTotalPageNum());
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 确认资产
	 * @return
	 */
	public String confirmAsset() {
		JSONObject json = new JSONObject();
		try {
			EnbAssetFacade facade = Util
					.getFacadeInstance(EnbAssetFacade.class);
			PagingData<EnbAsset> result = facade
					.queryByCondition(assetCondition);
			EnbAsset enbAsset = result.getResults().get(0);
			//new EnbAssetHistory
			EnbAssetHistory historyAsset = new EnbAssetHistory();
			historyAsset.setEnbId(enbAsset.getEnbId());
			historyAsset.setHardwareVersion(enbAsset.getHardwareVersion());
			historyAsset.setLastServeTime(enbAsset.getLastServeTime());
			historyAsset.setLocationInfo(enbAsset.getLocationInfo());
			historyAsset.setManufactureDate(enbAsset.getManufactureDate());
			historyAsset.setNodeType(enbAsset.getNodeType());
			historyAsset.setProductionSN(enbAsset.getProductionSN());
			historyAsset.setProviderName(enbAsset.getProviderName());
			historyAsset.setRemark(enbAsset.getRemark());
			historyAsset.setStartTime(enbAsset.getStartTime());
			historyAsset.setStatus(enbAsset.getStatus());
			historyAsset.setStopTime(enbAsset.getStopTime());
			historyAsset.setId(enbAsset.getId());
			
			//放入确认用户、时间
			LoginUser user = (LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT);
			historyAsset.setConfirmUser(user.getUsername());			
			historyAsset.setConfirmStopTime(getSysCurrentTime());
			facade.confirmStop(historyAsset);
			json.put("status", 0);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}
	
	/**
	 * 修后最后服务日期
	 * @return
	 */
	public String updateLastServerTime(){
		JSONObject json = new JSONObject();
		try {
			EnbAssetFacade facade = Util
					.getFacadeInstance(EnbAssetFacade.class);
			PagingData<EnbAsset> result = facade
					.queryByCondition(assetCondition);
			EnbAsset enbAsset = result.getResults().get(0);
			enbAsset.setLastServeTime(lastServeTime);
			facade.update(enbAsset);
			json.put("status", 0);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}
	
	/**
	 * 修后其他信息
	 * @return
	 */
	public String updateRemark(){
		JSONObject json = new JSONObject();
		try {
			EnbAssetFacade facade = Util
					.getFacadeInstance(EnbAssetFacade.class);
			PagingData<EnbAsset> result = facade
					.queryByCondition(assetCondition);
			EnbAsset enbAsset = result.getResults().get(0);
			enbAsset.setRemark(remark);
			facade.update(enbAsset);
			json.put("status", 0);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}
	
	/**
	 * 查询所有用户
	 * @return
	 */
	public String queryAllUserForAsset() {
		JSONObject json = new JSONObject();
		try {
			UserSecuFacade facade = Util
					.getFacadeInstance(UserSecuFacade.class);
			List<LoginUser> list = facade.queryAllUser();
			json.put("status", 0);
			json.put("message", JSONArray.fromObject(list));
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}
	
	/**
	 * 获取系统当前时间
	 * @return
	 */
	private long getSysCurrentTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String t = format.format(new Date());
		return Long.valueOf(t);
	}

	public EnbAssetCondition getAssetCondition() {
		return assetCondition;
	}

	public void setAssetCondition(EnbAssetCondition assetCondition) {
		this.assetCondition = assetCondition;
	}

	public long getLastServeTime() {
		return lastServeTime;
	}

	public void setLastServeTime(long lastServeTime) {
		this.lastServeTime = lastServeTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
