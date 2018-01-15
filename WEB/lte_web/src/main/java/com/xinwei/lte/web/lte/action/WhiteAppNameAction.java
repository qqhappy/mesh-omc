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
import com.xinwei.lte.web.lte.model.WhiteAppName;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.oss.adapter.model.OssAdapterInputMessage;
import com.xinwei.oss.adapter.model.OssAdapterOutputMessage;

public class WhiteAppNameAction extends ActionSupport{

	// 记录日志
	private static Log log = LogFactory.getLog(TcnGlobalRoamInfoAction.class);

	@Resource
	private OssAdapter ossAdapter;

	private OnlinePage onlinePage;

	// 显示给用户的错误提示信息
	private String showMessage = "暂无相关数据";

	// 异常信息
	private String exception;
	
	private List<WhiteAppName> whiteAppNameList = new LinkedList();
	
	private WhiteAppName  whiteAppName;
	private List<String> nameList;
	
	/**
	 * 查询列表信息
	 * 
	 * @return
	 */
	public String queryWhiteAppNameList() {
		whiteAppNameList = new ArrayList<WhiteAppName>();
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
			OssAdapterInputMessage req = new OssAdapterInputMessage(0xc4, 0x05,
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
					.getMapListValue("tagWhiteAppInfo");
			if (resultList != null && !resultList.isEmpty()) {
				for (Map map : resultList) {
					WhiteAppName whiteAppName = new WhiteAppName(map);
					whiteAppNameList.add(whiteAppName);
				}
			} else {
				if (onlinePage.getCurrentPageNum() == 1) {
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to queryWhiteAppNameList", e);
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			exception = e.getMessage();
		}
		return SUCCESS;
	}
	
	
	
	/**
	 * 跳转到新增界面
	 * @return
	 */
	public String turnToAddWhiteAppName(){
		return SUCCESS;
	}
	public String addWhiteAppName() {
		try {
			// 构造请求消息
			OssAdapterInputMessage req = new OssAdapterInputMessage(0xc4, 0x01,
					whiteAppName.toMapData());
			// 调用适配层
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);
			// 返回AJAX结果
			AjaxHelper.ajaxMethod(resp);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to addWhiteAppName", e);
			exception = e.getLocalizedMessage();
		}
		return NONE;
	}

	public String deleteWhiteAppName() {
		for (String whiteAppName : nameList) {
			try {
				// 构造请求消息
				Map<String, Object> data = new HashMap();
				data.put("whiteAppName", whiteAppName);
				OssAdapterInputMessage req = new OssAdapterInputMessage(0xc4, 0x02,
						data);
				// 调用适配层
				OssAdapterOutputMessage resp = ossAdapter.invoke(req);
				// 返回AJAX结果
				AjaxHelper.ajaxMethod(resp);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("failed to deleteWhiteAppName", e);
				exception = e.getLocalizedMessage();
			}
		}
		return NONE;
	}
	
	
	public String toWhiteAppNameModify(){
		
		
		return SUCCESS;
	}
	
	public String modifyWhiteAppName() {
		try {
			// 构造请求消息
			OssAdapterInputMessage req = new OssAdapterInputMessage(0xc4, 0x03,
					whiteAppName.toMapData());
			// 调用适配层
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);
			// 返回AJAX结果
			AjaxHelper.ajaxMethod(resp);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to modifyWhiteAppName", e);
			exception = e.getLocalizedMessage();
		}
		return NONE;
	}
	public OssAdapter getOssAdapter() {
		return ossAdapter;
	}

	public void setOssAdapter(OssAdapter ossAdapter) {
		this.ossAdapter = ossAdapter;
	}

	public OnlinePage getOnlinePage() {
		return onlinePage;
	}

	public void setOnlinePage(OnlinePage onlinePage) {
		this.onlinePage = onlinePage;
	}

	public List<String> getNameList() {
		return nameList;
	}



	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
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







	public List<WhiteAppName> getWhiteAppNameList() {
		return whiteAppNameList;
	}







	public void setWhiteAppNameList(List<WhiteAppName> whiteAppNameList) {
		this.whiteAppNameList = whiteAppNameList;
	}







	public WhiteAppName getWhiteAppName() {
		return whiteAppName;
	}







	public void setWhiteAppName(WhiteAppName whiteAppName) {
		this.whiteAppName = whiteAppName;
	}
	
	
}
