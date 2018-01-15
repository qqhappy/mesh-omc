/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-10	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.model.BizErrorModel;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.minas.micro.core.facade.XMicroBizConfigFacade;

/**
 * 
 * 同步eNB数据action
 * 
 * @author fanhaoyu
 * 
 */

public class SyncEnbDataAction extends ActionSupport {

	private long moId;

	/**
	 * 同步方向，0网管到基站，1基站到网管
	 */
	private int syncDirection;

	private BizErrorModel errorModel;

	/**
	 * 同步eNB数据，网管数据同步到基站，基站数据同步到网管
	 * 
	 * @return
	 */
	public String syncEnbData() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			int enbType = getEnbType(moId);		
			if (syncDirection == Enb.SYNC_DIRECTION_EMS_TO_NE) {
				if (enbType == EnbTypeDD.XW7400) {
					// 宏站
					XEnbBizConfigFacade facade = Util
							.getFacadeInstance(XEnbBizConfigFacade.class);
					facade.compareAndSyncEmsDataToNe(moId);
				} else if (enbType == EnbTypeDD.XW7102) {
					// 微站
					XMicroBizConfigFacade xMicroBizConfigFacade = Util
							.getFacadeInstance(XMicroBizConfigFacade.class);
					xMicroBizConfigFacade.compareAndSyncEmsDataToNe(moId);
				}				
			} else {
				if (enbType == EnbTypeDD.XW7400) {
					// 宏站
					XEnbBizConfigFacade facade = Util
							.getFacadeInstance(XEnbBizConfigFacade.class);
					facade.compareAndSyncNeDataToEms(moId);
				} else if (enbType == EnbTypeDD.XW7102) {
					// 微站
					XMicroBizConfigFacade xMicroBizConfigFacade = Util
							.getFacadeInstance(XMicroBizConfigFacade.class);
					xMicroBizConfigFacade.compareAndSyncNeDataToEms(moId);
				}
			}

		} catch (Exception e) {
			String error = e.getLocalizedMessage();
			if (errorModel == null) {
				errorModel = new BizErrorModel();
			}
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
	
	private int getEnbType(long moId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		return facade.queryByMoId(moId).getEnbType();
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public long getMoId() {
		return moId;
	}

	public void setSyncDirection(int syncDirection) {
		this.syncDirection = syncDirection;
	}

	public int getSyncDirection() {
		return syncDirection;
	}

	public BizErrorModel getErrorModel() {
		return errorModel;
	}

	public void setErrorModel(BizErrorModel errorModel) {
		this.errorModel = errorModel;
	}

}
