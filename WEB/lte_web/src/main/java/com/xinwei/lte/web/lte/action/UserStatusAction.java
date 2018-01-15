/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-28	|  yinyuelin 	    |  create the file                       
 */

package com.xinwei.lte.web.lte.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.UserStatusModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * 用户状态action
 * 
 * <p>
 * 用户状态action
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class UserStatusAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	//日志
	private static Logger logger = LoggerFactory.getLogger(UserStatusAction.class);
	
	private UserStatusModel userStatusModel;
	
	private UserStatusModel queryUserStatusModel;

	private OnlinePage onlinePage;
	
	private List<UserStatusModel> userStatusModelList;
	
	private String showMessage = "暂无相关数据";
	/**
	 * 跳转到用户状态页面
	 * @return
	 */
	public String turntoUserStatus(){
		
		logger.debug("turntoUserStatus - start");
		
		userStatusModelList = new ArrayList<UserStatusModel>();
		
		try{
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(null == onlinePage){				
				onlinePage = new OnlinePage();				
				onlinePage.setCurrentPageNum(1);
				onlinePage.setTotalPages(1);
				onlinePage.setPageSize(LteConstant.PageSize);
			}
			
			if(onlinePage.getCurrentPageNum() == 0){
				onlinePage.setCurrentPageNum(1);
			}
			
			if(null != queryUserStatusModel){
				if(0 != queryUserStatusModel.getFirst()){
					if(queryUserStatusModel.getQueryType().equals("1")){
						map.put("usrNumber", queryUserStatusModel.getQueryValue());
					}else if(queryUserStatusModel.getQueryType().equals("2")){
						map.put("usIp", queryUserStatusModel.getQueryValue());	
					}else if(queryUserStatusModel.getQueryType().equals("3")){
						map.put("imsi", queryUserStatusModel.getQueryValue());
					}else{
						map.put("usEmmState", queryUserStatusModel.getQueryValue());
					}
				}
			}else{
				queryUserStatusModel = new UserStatusModel();
			}
			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");

			Map<String,Object> resultMap = ossAdapter.invoke(0xa5, 0x04, map);
			String flag = (String) resultMap.get("lteFlag");
			
			if(!"0".equals(flag)){
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				return SUCCESS;
			}
			
			int lteTotalQueryCount = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			if(lteTotalQueryCount%LteConstant.PageSize == 0){
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize);
			}else{
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize+1);
			}	
			
			List<Map> resultList = (List<Map>) resultMap.get("usInfo");
			
			UserStatusModel userStatModel;
			if(null != resultList){
				for(Map rMap : resultList){
					userStatModel = new UserStatusModel();
					if(rMap.get("usrNumber") != null){
						userStatModel.setUsr_number(((String)rMap.get("usrNumber")).trim());
					}
					if(rMap.get("imsi") != null){
						userStatModel.setImsi(((String)rMap.get("imsi")).trim());
					}
					if(rMap.get("usGuti") != null){
						userStatModel.setUs_guti(((String)rMap.get("usGuti")).trim());
					}
					if(rMap.get("usTai") != null){
						userStatModel.setUs_tai(((String)rMap.get("usTai")).trim());
					}
					if(rMap.get("usEmmState") != null){
						userStatModel.setUs_emmstate(((String)rMap.get("usEmmState")).trim());
					}
					if(rMap.get("usRoamState") != null){
						userStatModel.setUs_roamstate(((String)rMap.get("usRoamState")).trim());
					}
					if(rMap.get("usIp") != null){
						userStatModel.setUs_ip(((String)rMap.get("usIp")).trim());
					}
					if(rMap.get("usAPNIP") != null){
						userStatModel.setUs_apn_ip(((String)rMap.get("usAPNIP")).trim());
					}					
								
					userStatusModelList.add(userStatModel);
				}
			}else{
				if(onlinePage.getCurrentPageNum() == 1 ){
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}
			}
			
		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("turntoUserStatus error:"+e.toString());
			showMessage = e.getMessage();
		}
		logger.debug("turntoUserStatus - end");
		return SUCCESS;
	}
	
	public UserStatusModel getUserStatusModel()
	{
		return userStatusModel;
	}
	public void setUserStatusModel(UserStatusModel userStatusModel)
	{
		this.userStatusModel = userStatusModel;
	}
	public UserStatusModel getQueryUserStatusModel()
	{
		return queryUserStatusModel;
	}
	public void setQueryUserStatusModel(UserStatusModel queryUserStatusModel)
	{
		this.queryUserStatusModel = queryUserStatusModel;
	}
	public OnlinePage getOnlinePage()
	{
		return onlinePage;
	}
	public void setOnlinePage(OnlinePage onlinePage)
	{
		this.onlinePage = onlinePage;
	}
	public List<UserStatusModel> getUserStatusModelList()
	{
		return userStatusModelList;
	}
	public void setUserStatusModelList(List<UserStatusModel> userStatusModelList)
	{
		this.userStatusModelList = userStatusModelList;
	}

	public String getShowMessage()
	{
		return showMessage;
	}

	public void setShowMessage(String showMessage)
	{
		this.showMessage = showMessage;
	}

}

