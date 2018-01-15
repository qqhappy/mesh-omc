/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-30	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.lte.web.lte.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.TcnGlobalRoamInfo;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.oss.adapter.model.OssAdapterInputMessage;
import com.xinwei.oss.adapter.model.OssAdapterOutputMessage;

/**
 * 
 * TCN1000全局漫游信息Action
 * 
 * @author chenjunhua
 * 
 */
public class TcnGlobalRoamInfoAction extends ActionSupport {

	// 记录日志
	private static Log log = LogFactory.getLog(TcnGlobalRoamInfoAction.class);

	@Resource
	private OssAdapter ossAdapter;

	private OnlinePage onlinePage;

	// 显示给用户的错误提示信息
	private String showMessage = "暂无相关数据";

	// 异常信息
	private String exception;

	// 向设备查询返回的列表
	private List<TcnGlobalRoamInfo> tcnGlobalRoamInfoList = new LinkedList();

	// 查询、增加或修改时的模型
	private TcnGlobalRoamInfo tcnGlobalRoamInfo;

	// 删除数据时的模型
	private List<String> sdcIdList;
	
	// 查询时的sdcId
	private String querySdcId;

	/**
	 * 查询列表信息
	 * 
	 * @return
	 */
	public String queryTcnGlobalRoamInfoList() {
		tcnGlobalRoamInfoList = new ArrayList<TcnGlobalRoamInfo>();
		try {
			// 构造请求数据
			Map<String, Object> data = new HashMap<String, Object>();
			if (null == onlinePage) {
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);
			}
			data.put("ltePageSize", LteConstant.PageSize + "");
			data.put("ltePageIndex", onlinePage.getCurrentPageNum() + "");

			// 调用适配层发送消息
			OssAdapterInputMessage req = new OssAdapterInputMessage(0xb7, 0x05,
					data);
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);

			// 解析返回消息
			if (resp.isFailed()) {
				showMessage = resp.getReason();
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				return SUCCESS;
			}
			int lteTotalQueryCount = resp.getIntValue("lteTotalQueryCount");
			if (lteTotalQueryCount % LteConstant.PageSize == 0) {
				onlinePage.setTotalPages(lteTotalQueryCount
						/ LteConstant.PageSize);
			} else {
				onlinePage.setTotalPages(lteTotalQueryCount
						/ LteConstant.PageSize + 1);
			}
			// 构造返回结果模型
			List<Map> resultList = resp
					.getMapListValue("tcnGlobalRoamInfoList");
			if (resultList != null && !resultList.isEmpty()) {
				for (Map map : resultList) {
					TcnGlobalRoamInfo tcnGlobalRoamInfo = new TcnGlobalRoamInfo(map);
					tcnGlobalRoamInfoList.add(tcnGlobalRoamInfo);
				}
			} else {
				if (onlinePage.getCurrentPageNum() == 1) {
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to queryTcnGlobalRoamInfoList", e);
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			exception = e.getMessage();
		}
		return SUCCESS;
	}
	

	/**
	 * 增加全局漫游信息
	 * 
	 * @return
	 */
	public String addTcnGlobalRoamInfo() {
		try {
			// 构造请求消息
			OssAdapterInputMessage req = new OssAdapterInputMessage(0xb7, 0x01,
					tcnGlobalRoamInfo.toMapData());
			// 调用适配层
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);
			// 返回AJAX结果
			AjaxHelper.ajaxMethod(resp);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to addTcnGlobalRoamInfo", e);
			exception = e.getLocalizedMessage();
		}
		return NONE;
	}

	/**
	 * 修改全局漫游信息
	 * 
	 * @return
	 */
	public String modifyTcnGlobalRoamInfo() {
		try {
			// 构造请求消息
			OssAdapterInputMessage req = new OssAdapterInputMessage(0xb7, 0x03,
					tcnGlobalRoamInfo.toMapData());
			// 调用适配层
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);
			// 返回AJAX结果
			AjaxHelper.ajaxMethod(resp);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to modifyTcnGlobalRoamInfo", e);
			exception = e.getLocalizedMessage();
		}
		return NONE;
	}

	/**
	 * 删除全局漫游信息
	 * 
	 * @return
	 */
	public String deleteTcnGlobalRoamInfo() {
		for (String sdcId : sdcIdList) {
			try {
				// 构造请求消息
				Map<String, Object> data = new HashMap();
				data.put("sdcId", sdcId);
				OssAdapterInputMessage req = new OssAdapterInputMessage(0xb7, 0x02,
						data);
				// 调用适配层
				OssAdapterOutputMessage resp = ossAdapter.invoke(req);
				// 返回AJAX结果
				AjaxHelper.ajaxMethod(resp);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("failed to deleteTcnGlobalRoamInfo", e);
				exception = e.getLocalizedMessage();
			}
		}
		return NONE;
	}
	
	/**
	 * 跳转到新增界面
	 * @return
	 */
	public String turnToAddRoamInfoHtml(){
		return SUCCESS;
	}
	
	/**
	 * 跳转到修改界面
	 * @return
	 */
	public String turnToModifyRoamInfoHtml(){
		try {
			tcnGlobalRoamInfo = new TcnGlobalRoamInfo();
			// 构造请求数据
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("ltePageSize", LteConstant.PageSize + "");
			data.put("ltePageIndex", "1");			
			if (querySdcId == null || "".equals(querySdcId)) {
				throw new Exception("请输入SDC ID.");
			}
			data.put("sdcId", querySdcId);

			// 调用适配层发送消息
			OssAdapterInputMessage req = new OssAdapterInputMessage(0xb7, 0x05,
					data);
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);

			// 解析返回消息
			if (resp.isFailed()) {
				showMessage = resp.getReason();
				return SUCCESS;
			}

			// 构造返回结果模型
			List<Map> resultList = resp
					.getMapListValue("tcnGlobalRoamInfoList");
			if (resultList != null && !resultList.isEmpty()) {
				for (Map map : resultList) {
					tcnGlobalRoamInfo = new TcnGlobalRoamInfo(map);
					break;
				}
			} 

		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to queryTcnGlobalRoamInfo", e);
			exception = e.getMessage();
		}
		return SUCCESS;
	}

	public List<TcnGlobalRoamInfo> getTcnGlobalRoamInfoList() {
		return tcnGlobalRoamInfoList;
	}

	public void setTcnGlobalRoamInfoList(
			List<TcnGlobalRoamInfo> tcnGlobalRoamInfoList) {
		this.tcnGlobalRoamInfoList = tcnGlobalRoamInfoList;
	}

	public TcnGlobalRoamInfo getTcnGlobalRoamInfo() {
		return tcnGlobalRoamInfo;
	}

	public void setTcnGlobalRoamInfo(TcnGlobalRoamInfo tcnGlobalRoamInfo) {
		this.tcnGlobalRoamInfo = tcnGlobalRoamInfo;
	}



	public List<String> getSdcIdList() {
		return sdcIdList;
	}

	public void setSdcIdList(List<String> sdcIdList) {
		this.sdcIdList = sdcIdList;
	}

	public OnlinePage getOnlinePage() {
		return onlinePage;
	}

	public void setOnlinePage(OnlinePage onlinePage) {
		this.onlinePage = onlinePage;
	}

	public String getShowMessage() {
		return showMessage;
	}

	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public OssAdapter getOssAdapter() {
		return ossAdapter;
	}

	public void setOssAdapter(OssAdapter ossAdapter) {
		this.ossAdapter = ossAdapter;
	}

	public String getQuerySdcId() {
		return querySdcId;
	}

	public void setQuerySdcId(String querySdcId) {
		this.querySdcId = querySdcId;
	}

	
	
}
