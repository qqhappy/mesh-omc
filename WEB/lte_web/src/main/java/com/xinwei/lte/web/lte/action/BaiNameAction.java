package com.xinwei.lte.web.lte.action;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.lte.model.BaiNameModel;
import com.xinwei.lte.web.lte.model.MmeS1Model;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.WirelessImsiModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.system.action.web.WebConstants;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


/**
 * 白名单
 * @author sunzhangbin
 *
 */

public class BaiNameAction extends ActionSupport {

	@Resource
	private OssAdapter ossAdapter;

	private OnlinePage onlinePage;

	private BaiNameModel baiNameModel;
	private BaiNameModel querybaiNameModel;
	
	private List<BaiNameModel> baiNameModelList;
	// 记录日志
	private static Logger logger = LoggerFactory
			.getLogger(BaiNameAction.class);
	private String showMessage = "暂无相关数据";
	
	
	public String turntoBaiName() {
		logger.debug("turntoBaiName-start");
		baiNameModelList = new ArrayList<BaiNameModel>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if (null == onlinePage) {
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);
			}

			if (null != querybaiNameModel) {
				if (0 != querybaiNameModel.getFirst()) {
					if (querybaiNameModel.getQueryType().equals("1")) {
						map.put("caller", querybaiNameModel.getQueryValue());
					} else {
						map.put("called", querybaiNameModel.getQueryValue());
					}
				}
			} else {
				querybaiNameModel = new BaiNameModel();
			}

			map.put("ltePageSize", LteConstant.PageSize + "");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum() + "");

			// map.put("ipId", systemAddressModel.getIp_id());
			Map<String, Object> resultMap = ossAdapter.invoke(0xc7, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if (!"0".equals(flag)) {
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				return SUCCESS;
			}
			int lteTotalQueryCount = Integer.parseInt((String) resultMap
					.get("lteTotalQueryCount"));
			if (lteTotalQueryCount % LteConstant.PageSize == 0) {
				onlinePage.setTotalPages(lteTotalQueryCount
						/ LteConstant.PageSize);
			} else {
				onlinePage.setTotalPages(lteTotalQueryCount
						/ LteConstant.PageSize + 1);
			}

			List<Map> resultList = (List<Map>) resultMap.get("TAG_WHITELIST_INFO");
			BaiNameModel baiNameModel;
			if (resultList != null) {
				for (Map rMap : resultList) {
					baiNameModel = new BaiNameModel();
					if (rMap.get("caller") != null) {
						baiNameModel.setCaller(((String) rMap.get("caller"))
								.trim());
					}
					if (rMap.get("called") != null) {
						baiNameModel.setCalled(((String) rMap.get("called"))
								.trim());
					}
					if (rMap.get("callType") != null) {
						baiNameModel.setCallType(((String) rMap
								.get("callType")).trim());
					}
					if (rMap.get("comment") != null) {
						baiNameModel.setComment(((String) rMap
								.get("comment")).trim());
					}
					baiNameModelList.add(baiNameModel);
				}
			} else {
				if (onlinePage.getCurrentPageNum() == 1) {
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("turntoBaiName error:" + e.toString());
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			showMessage = e.getMessage();
		}
		logger.debug("turntoBaiName-end");
		return SUCCESS;
	}
	
	
	public String turntoBaiNameAdd(){
		
		return SUCCESS;
	}
	
	public String addBaiName() {
		logger.debug("addBaiName-start");
		JSONObject json = new JSONObject();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("caller", baiNameModel.getCaller());
			map.put("called", baiNameModel.getCalled());
			map.put("callType", baiNameModel.getCallType());
			map.put("comment", baiNameModel.getComment());
			Map<String, Object> resultMap = ossAdapter.invoke(0xc7, 0x01, map);
			String flag = (String) resultMap.get("lteFlag");
			if ("0".equals(flag)) {
				json.put("status", 0);
				ajaxMethod(json.toString());
			} else {
				json.put("status", 1);
				json.put("message", LteFlag.flagReturn(flag));
				ajaxMethod(json.toString());
			}

		} catch (NoAuthorityException e) {
			json.put("status", 1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
			logger.error("addBaiName error:" + e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("addBaiName error:" + e.toString());
		}
		return NONE;
	}
	
	
	
	
	public String toModifyBaiName() {
		logger.debug("toModifyS1-start");
		try {
			


		} catch (Exception e) {
			e.printStackTrace();
			logger.error("toModifyS1 error:" + e.toString());

		}
		logger.debug("toModifyS1-end");
		return SUCCESS;
	}
	public String modifyBaiName() {
		JSONObject json = new JSONObject();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("caller", baiNameModel.getCaller());
			map.put("called", baiNameModel.getCalled());
			map.put("callType", baiNameModel.getCallType());
			map.put("comment", baiNameModel.getComment());
			Map<String, Object> resultMap = ossAdapter.invoke(0xc7, 0x03, map);
			String flag = (String) resultMap.get("lteFlag");
			if ("0".equals(flag)) {
				json.put("status", 0);
				ajaxMethod(json.toString());
			} else {
				json.put("status", 1);
				json.put("message", LteFlag.flagReturn(flag));
				ajaxMethod(json.toString());
			}
		} catch (NoAuthorityException e) {
			json.put("status", 1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	
	public String deleteBaiName(){
		
		JSONObject json = new JSONObject();
		try{
					
			String caller = baiNameModel.getCaller();
			String called = baiNameModel.getCalled();
			String callType = baiNameModel.getCallType();
			
			String[] callerArray = caller.split(",");
			String[] calledArray = called.split(",");
			String[] callTypeArray = callType.split(",");
			
			if(callerArray.length == 1){
				Map<String,Object> map = new HashMap<String,Object>();
				
				map.put("caller", caller);
				map.put("called", called);
				map.put("callType", callType);
				
				Map<String,Object> resultMap = ossAdapter.invoke(0xc7, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					return NONE;
				}
			}else{
				for(int i = 0; i < callerArray.length; i ++){
					Map<String,Object> map = new HashMap<String,Object>();

					map.put("srouteNet", callerArray[i]);
					map.put("srouteNetMask", calledArray[i]);
					map.put("srouteGw", callTypeArray[i]);
					
					Map<String,Object> resultMap = ossAdapter.invoke(0xc7, 0x02, map);
					String flag = (String) resultMap.get("lteFlag");
					if(!"0".equals(flag)){
						json.put("status", 1);
						json.put("message", LteFlag.flagReturn(flag));
						ajaxMethod(json.toString());
						return NONE;
					}
				}
			}
			json.put("status", 0);
			ajaxMethod(json.toString());
		}catch(NoAuthorityException e){
			json.put("status", 1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
		}catch(Exception e){
			e.printStackTrace();
			json.put("status", 1);
			json.put("message", "操作失败");
			ajaxMethod(json.toString());
		}
		return NONE;
	}
	
	
	
	
	
	// 异步请求返回字符串
	private void ajaxMethod(String content) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/jsp; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(content);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}

	}
	public OnlinePage getOnlinePage() {
		return onlinePage;
	}
	public void setOnlinePage(OnlinePage onlinePage) {
		this.onlinePage = onlinePage;
	}
	public List<BaiNameModel> getBaiNameModelList() {
		return baiNameModelList;
	}
	public void setBaiNameModelList(List<BaiNameModel> baiNameModelList) {
		this.baiNameModelList = baiNameModelList;
	}
	public String getShowMessage() {
		return showMessage;
	}
	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}

	public BaiNameModel getQuerybaiNameModel() {
		return querybaiNameModel;
	}

	public void setQuerybaiNameModel(BaiNameModel querybaiNameModel) {
		this.querybaiNameModel = querybaiNameModel;
	}


	public BaiNameModel getBaiNameModel() {
		return baiNameModel;
	}


	public void setBaiNameModel(BaiNameModel baiNameModel) {
		this.baiNameModel = baiNameModel;
	}

	
	
}
