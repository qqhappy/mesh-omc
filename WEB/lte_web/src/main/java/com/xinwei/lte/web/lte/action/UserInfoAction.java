/*      						
  * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-28	|  yinyuelin 	    |  create the file                       
 */

package com.xinwei.lte.web.lte.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.UserInfoModel;
import com.xinwei.lte.web.lte.model.UserTemplateModel;
import com.xinwei.lte.web.lte.model.WirelessImsiModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * 用户信息action
 * 
 * <p>
 * 用户信息action
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class UserInfoAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	//日志
	private static Logger logger = LoggerFactory.getLogger(UserInfoAction.class);
	
	private OnlinePage onlinePage;
	
	private UserInfoModel userInfoModel;
	
	private WirelessImsiModel querywirelessImsiModel;
	
	//保存查询条件
	private UserInfoModel queryuserInfoModel;
	
	private List<UserInfoModel> userInfoModelList;
	
	private List<UserTemplateModel> userTemplateModelList;
	
	private String showMessage = "暂无相关数据";
	
	/**
	 * 跳转到用户信息页面
	 * @return
	 */
	public String turntoUserInfo(){
		logger.debug("turntoUserInfo - start");
		userInfoModelList = new ArrayList<UserInfoModel>();
		userTemplateModelList = new ArrayList<UserTemplateModel>();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			if(null == onlinePage){
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);				
			}
						
			if(null != queryuserInfoModel){
				if( 0 != queryuserInfoModel.getFirst()){
					if(queryuserInfoModel.getQueryType().equals("1")){
						map.put("usrNumber", queryuserInfoModel.getQueryValue());
					}else if(queryuserInfoModel.getQueryType().equals("2")){
						map.put("imsi", queryuserInfoModel.getQueryValue());						
					}else if(queryuserInfoModel.getQueryType().equals("3")){
						map.put("usrNeckName", queryuserInfoModel.getQueryValue());
					}else if(queryuserInfoModel.getQueryType().equals("4")){
						map.put("tmpID", queryuserInfoModel.getQueryValue());						
					}else if(queryuserInfoModel.getQueryType().equals("5")){
						map.put("usrNumberType", queryuserInfoModel.getQueryValue());
					}else if(queryuserInfoModel.getQueryType().equals("6")){
						map.put("usrState", queryuserInfoModel.getQueryValue());
					}else{
						map.put("srvUsrPriority", queryuserInfoModel.getQueryValue());
					}				
				}
			}else{
				queryuserInfoModel = new UserInfoModel();
				//map.put("usrNumberType", "1");
			}
			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			
			Map<String,Object> resultMap = ossAdapter.invoke(0xa1, 0x05, map);
			
			//查询参数模板ID
			Map<String,Object> templateMap = new HashMap<String,Object>();
			templateMap.put("ltePageSize", "50");
			templateMap.put("ltePageIndex", "1");
			Map<String,Object> templateResultMap = ossAdapter.invoke(0xa2, 0x05, templateMap);
			String templateFlag = (String) templateResultMap.get("lteFlag");
			if("0".equals(templateFlag)){
				List<Map> resultList = (List<Map>) templateResultMap.get("tmpInfo");
				UserTemplateModel userTempModel;
				if(resultList != null){
					for(Map rMap : resultList){
						userTempModel = new UserTemplateModel();
						userTempModel.setTmp_id((String)rMap.get("tmpID"));			
						userTemplateModelList.add(userTempModel);
					}
				}			
				
			}
			
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
			
			List<Map> resultList = (List<Map>) resultMap.get("usrInfo");
			UserInfoModel uInfoModel;
			if(null != resultList){
				for(Map rMap : resultList){
					uInfoModel = new UserInfoModel();
					if(rMap.get("usrNumber") != null){
						uInfoModel.setUsr_number(((String)rMap.get("usrNumber")).trim());	
					}
					if(rMap.get("usrNumberType") != null){
						uInfoModel.setUsr_numbertype(((String)rMap.get("usrNumberType")).trim());
					}
					if(rMap.get("usrHaveImsi") != null){
						uInfoModel.setUsr_haveimsi(((String)rMap.get("usrHaveImsi")).trim());
					}
					if(rMap.get("imsi") != null){
						uInfoModel.setImsi(((String)rMap.get("imsi")).trim());
					}
					if(rMap.get("tmpID") != null){
						uInfoModel.setTmp_id(((String)rMap.get("tmpID")).trim());
					}
					if(rMap.get("usrStaticIP") != null){
						uInfoModel.setUsr_staticip(((String)rMap.get("usrStaticIP")).trim());
					}
					if(rMap.get("usrNeckName") != null){
						uInfoModel.setUsr_neckname(((String)rMap.get("usrNeckName")).trim());
					}
					if(rMap.get("usrPassword") != null){
						uInfoModel.setUsr_password(((String)rMap.get("usrPassword")).trim());
					}
					if(rMap.get("usrComment") != null){
						uInfoModel.setUsr_comment(((String)rMap.get("usrComment")).trim());
					}
					if(rMap.get("usrState") != null){
						uInfoModel.setUsr_state(Integer.parseInt(((String)rMap.get("usrState")).trim()));
					}
					if(rMap.get("ipaddrAllocateType") != null){
						uInfoModel.setIpaddr_allocate_type(Integer.valueOf(rMap.get("ipaddrAllocateType")+""));
					}
					if(rMap.get("lteUsrState") != null){
						String lteUserState = ((String)rMap.get("lteUsrState")).trim();
						uInfoModel.setLte_usestate(lteUserState);
						if("1".equals(lteUserState)){
							if(rMap.get("srvSingleCallLteFlag") != null){
								String srvSingleCallLteFlag = Integer.toBinaryString(Integer.parseInt(((String)rMap.get("srvSingleCallLteFlag")).trim()));
								StringBuilder sb = new StringBuilder();
								if(srvSingleCallLteFlag.length() < 6){
									for(int i = 0; i < (6-srvSingleCallLteFlag.length()); i++){
										sb.append("0");
									}
								}
								String srvSingleCallLteFlagStr = sb.toString()+srvSingleCallLteFlag;
								StringBuilder sbResult = new StringBuilder();
			
								if(srvSingleCallLteFlagStr.charAt(5) == '1'){
									sbResult.append("音频单呼 ");
								}
								if(srvSingleCallLteFlagStr.charAt(4) == '1'){
									sbResult.append("视频单呼 ");
								}
								if(srvSingleCallLteFlagStr.charAt(3) == '1'){
									sbResult.append("数据业务 ");
								}
								if(srvSingleCallLteFlagStr.charAt(2) == '1'){
									sbResult.append("漏呼短信 ");
								}
								if(srvSingleCallLteFlagStr.charAt(1) == '1'){
									sbResult.append("夜服业务 ");
								}
								if(srvSingleCallLteFlagStr.charAt(0) == '1'){
									sbResult.append("联动话机 ");
								}
							
								uInfoModel.setSrv_sigcalllte_flag(sbResult.toString());
							}
							
							if(rMap.get("srvGroupCallLteFlag") != null){
								String srvGroupCallLteFlag = Integer.toBinaryString(Integer.parseInt(((String)rMap.get("srvGroupCallLteFlag")).trim()));
								StringBuilder sb = new StringBuilder();
								if(srvGroupCallLteFlag.length() < 3){
									for(int i = 0; i < (3-srvGroupCallLteFlag.length()); i++){
										sb.append("0");
									}
								}
								String srvGroupCallLteFlagStr = sb.toString()+srvGroupCallLteFlag;
								StringBuilder sbResult = new StringBuilder();
								if(srvGroupCallLteFlagStr.charAt(2) == '1'){
									sbResult.append("组呼开关 ");
								}
								if(srvGroupCallLteFlagStr.charAt(1) == '1'){
									sbResult.append("广播呼叫 ");
								}
								if(srvGroupCallLteFlagStr.charAt(0) == '1'){
									sbResult.append("可视组呼 ");
								}
								uInfoModel.setSrv_grpcalllte_flag(sbResult.toString());
							}
							if(rMap.get("audioRecordFlag") != null){
								String audioRecordFlag = Integer.toBinaryString(Integer.parseInt(((String)rMap.get("audioRecordFlag")).trim()));
								StringBuilder builder = new StringBuilder();
								if(audioRecordFlag.charAt(audioRecordFlag.length()-1) == '1'){
									builder.append("录音业务");
								}
								uInfoModel.setAudioRecordFlag(builder.toString());
							}
							
							if(rMap.get("srvUsrPriority") != null){
								uInfoModel.setSrv_usr_pri(((String)rMap.get("srvUsrPriority")).trim());
							}		
						}
					}													
					userInfoModelList.add(uInfoModel);		
				}
			}else{
				if(onlinePage.getCurrentPageNum() == 1 ){
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}				
			}			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("turntoUserInfo error:"+e.toString());
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			showMessage = e.getMessage();
		}		
				
		logger.debug("turntoUserInfo - start");
		return SUCCESS;
	}
	
	/**
	 * 跳转到用户信息配置页面
	 * @return
	 */
	public String turntoUserInfoAdd(){
		logger.debug("turntoUserInfoAdd - start");
		userTemplateModelList = new ArrayList<UserTemplateModel>();
		try{
			//查询参数模板ID
			Map<String,Object> templateMap = new HashMap<String,Object>();
			templateMap.put("ltePageSize",  "50");
			templateMap.put("ltePageIndex", "1");
			
			
			Map<String,Object> templateResultMap = ossAdapter.invoke(0xa2, 0x05, templateMap);
			String templateFlag = (String) templateResultMap.get("lteFlag");
			if("0".equals(templateFlag)){
				List<Map> resultList = (List<Map>) templateResultMap.get("tmpInfo");
				UserTemplateModel userTempModel;
				if(resultList != null){
					for(Map rMap : resultList){
						userTempModel = new UserTemplateModel();
						userTempModel.setTmp_id((String)rMap.get("tmpID"));		
						userTempModel.setTmp_default(((String)rMap.get("tmpDefault")).trim());
						userTemplateModelList.add(userTempModel);
					}
				}				
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("");
		}
		
		logger.debug("turntoUserInfoAdd - end");
		return SUCCESS;
	}
	
	/**
	 * 增加用户信息
	 * @return
	 */
	public String addUserInfo(){
		logger.debug("addUserInfo-start");
		JSONObject json = new JSONObject();
		try{
			//基本信息
			Map<String,Object> baseInfoMap = new HashMap<String,Object>();
			baseInfoMap.put("usrNumber", userInfoModel.getUsr_number());
			baseInfoMap.put("usrNumberType", userInfoModel.getUsr_numbertype());
			baseInfoMap.put("usrNeckName", userInfoModel.getUsr_neckname());			
			baseInfoMap.put("usrPassword", userInfoModel.getUsr_password());	
			baseInfoMap.put("usrComment", userInfoModel.getUsr_comment());
			baseInfoMap.put("usrState", userInfoModel.getUsr_state());			
			baseInfoMap.put("usrHaveImsi", userInfoModel.getUsr_haveimsi());
			if("1".equals(userInfoModel.getUsr_haveimsi())){
				baseInfoMap.put("imsi", userInfoModel.getImsi());
				baseInfoMap.put("tmpID", userInfoModel.getTmp_id());
							
			}		
			
			baseInfoMap.put("ipaddrAllocateType", userInfoModel.getIpaddr_allocate_type());
			if(userInfoModel.getIpaddr_allocate_type() == 0){
				baseInfoMap.put("usrStaticIP","0.0.0.0");
			}else if(userInfoModel.getIpaddr_allocate_type() == 1){
				if(null != userInfoModel.getUsr_staticip()){
					if(!"".equals(userInfoModel.getUsr_staticip())){
						baseInfoMap.put("usrStaticIP", userInfoModel.getUsr_staticip());	
					}else{
						baseInfoMap.put("usrStaticIP","0.0.0.0");		
					}						
				}else{
					baseInfoMap.put("usrStaticIP","0.0.0.0");	
				}
			}else if(userInfoModel.getIpaddr_allocate_type() == 2){
				baseInfoMap.put("usrStartStaticip",userInfoModel.getUsr_start_staticip());
				baseInfoMap.put("usrEndStaticip",userInfoModel.getUsr_end_staticip());
			}
			
			//baseInfoMap.put("usrAuthflag", userInfoModel.getUsr_authflag());
			Map<String,Object> baseInfoResultMap = ossAdapter.invoke(0xa1, 0x01, baseInfoMap);
			String baseInfoflag = (String) baseInfoResultMap.get("lteFlag");
			if(!"0".equals(baseInfoflag)){
				json.put("status", 0);
				json.put("message", LteFlag.flagReturn(baseInfoflag));
				ajaxMethod(json.toString());
				return NONE;
			}
		
			
			//基本业务
			String lte_usestate = userInfoModel.getLte_usestate();
			if("1".equals(lte_usestate)){
				json.put("hasBuz", true);
				Map<String,Object> baseBuzMap = new HashMap<String,Object>();
				baseBuzMap.put("usrNumber", userInfoModel.getUsr_number());		
				
				//map.put("lteUsrState", userInfoModel.getLte_usestate());
				String srv_sigcalllte_flag = userInfoModel.getSrv_sigcalllte_flag();
				String srvGroupCallLteFlag = userInfoModel.getSrv_grpcalllte_flag();
				baseBuzMap.put("srvSingleCallLteFlag",parseLongStr(srv_sigcalllte_flag));
				baseBuzMap.put("srvGroupCallLteFlag", parseShortStr(srvGroupCallLteFlag));
				baseBuzMap.put("srvUsrPriority", userInfoModel.getSrv_usr_pri());
				baseBuzMap.put("audioRecordFlag", userInfoModel.getAudioRecordFlag());
				baseBuzMap.put("srv_location_flag", userInfoModel.getSrv_location_flag());
				baseBuzMap.put("srv_bai_flag", userInfoModel.getSrv_bai_flag());
				Map<String,Object>  baseBuzResultMap = ossAdapter.invoke(0xa3, 0x03, baseBuzMap);
				
				String baseBuzflag = (String) baseBuzResultMap.get("lteFlag");
				if("0".equals(baseBuzflag)){
					json.put("status", 1);
					json.put("buzResult", true);					
					
					//夜服业务是否开通
					String[] nightBuz = srv_sigcalllte_flag.split(",");
					if("1".equals(nightBuz[1])){
						json.put("hasForward", true);
						
						//前传业务 
						Map<String,Object> forwardBuzMap = new HashMap<String,Object>();
						String fwd_condition = userInfoModel.getFwd_condition();
						forwardBuzMap.put("usrNumber", userInfoModel.getUsr_number());
						forwardBuzMap.put("fwdCondition",fwd_condition);
						if("1".equals(fwd_condition)){
							String fwd_servtype =  userInfoModel.getFwd_servtype();
							forwardBuzMap.put("fwdServeType", fwd_servtype);
							if("1".equals(fwd_servtype)){
								forwardBuzMap.put("fwdSHour", userInfoModel.getShour());
								forwardBuzMap.put("fwdEHour", userInfoModel.getEhour());									
							}	
							forwardBuzMap.put("fwdNumber", userInfoModel.getFwd_number());								
						}
						Map<String,Object>  forwardBuzResultMap = ossAdapter.invoke(0xa4, 0x03, forwardBuzMap);
						
						String forwardBuzFlagflag = (String) forwardBuzResultMap.get("lteFlag");
						if("0".equals(forwardBuzFlagflag)){
							json.put("status", 1);
							json.put("forwardResult", true);
						}else{
							json.put("status", 1);
							json.put("forwardResult", true);
							json.put("forwardMessage", LteFlag.flagReturn(baseInfoflag));
						}
					}
				}else{
					json.put("hasForward", false);
					
					json.put("status", 1);
					json.put("buzResult", false);
					json.put("buzMessage", LteFlag.flagReturn(baseInfoflag));
				}	
				
			}else{
				json.put("hasBuz", false);
				json.put("hasForward", false);
			}
			
			ajaxMethod(json.toString());
		}catch(NoAuthorityException e){
			json.put("status", -1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
			logger.error("addSysAddress error:"+e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("addUserInfo error:"+e.toString());
		}
		logger.debug("addUserInfo-end");
		return NONE;
	}
	
	/**
	 * 跳转到修改用户信息
	 * @return
	 */
	public String toModifyUserInfo(){
		logger.debug("modifyUserInfo-start");
		//userTemplateModelList = new ArrayList<UserTemplateModel>();
		userTemplateModelList = new ArrayList<UserTemplateModel>();
		try{
			//查询参数模板ID
			Map<String,Object> templateMap = new HashMap<String,Object>();
			templateMap.put("ltePageSize", "50");
			templateMap.put("ltePageIndex", "1");
			Map<String,Object> templateResultMap = ossAdapter.invoke(0xa2, 0x05, templateMap);
			String templateFlag = (String) templateResultMap.get("lteFlag");
			if("0".equals(templateFlag)){
				List<Map> resultList = (List<Map>) templateResultMap.get("tmpInfo");
				UserTemplateModel userTempModel;
				if(resultList != null){
					for(Map rMap : resultList){
						userTempModel = new UserTemplateModel();
						userTempModel.setTmp_id((String)rMap.get("tmpID"));			
						userTemplateModelList.add(userTempModel);
					}
				}				
			}
			
			//用户信息
			Map<String,Object> map = new HashMap<String,Object>();
						
			map.put("usrNumber", userInfoModel.getUsr_number());
					
			map.put("ltePageSize", "1");
			map.put("ltePageIndex", "1");
			
			Map<String,Object> resultMap = ossAdapter.invoke(0xa1, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				onlinePage.setTotalPages(0);
				onlinePage.setCurrentPageNum(0);
				return SUCCESS;
			}
			int totalPages = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			onlinePage.setTotalPages(totalPages);
			
			List<Map> resultList = (List<Map>) resultMap.get("usrInfo");

			if(null != resultList){
				if(resultList.size() > 0){
					Map rMap = resultList.get(0);
//					if(rMap.get("srv_location_flag") != null){
//						userInfoModel.setSrv_location_flag(((String)rMap.get("srv_location_flag")).trim());
//					}
					if(rMap.get("usrNumber") != null){
						userInfoModel.setUsr_number(((String)rMap.get("usrNumber")).trim());
					}
					if(rMap.get("usrNumberType") != null){
						userInfoModel.setUsr_numbertype(((String)rMap.get("usrNumberType")).trim());
					}
					if(rMap.get("usrHaveImsi") != null){
						userInfoModel.setUsr_haveimsi(((String)rMap.get("usrHaveImsi")).trim());
					}
					if(rMap.get("imsi") != null){
						userInfoModel.setImsi(((String)rMap.get("imsi")).trim());
					}
					if(rMap.get("tmpID") != null){
						userInfoModel.setTmp_id(((String)rMap.get("tmpID")).trim());
					}
					if(rMap.get("usrStaticIP") != null){
						userInfoModel.setUsr_staticip(((String)rMap.get("usrStaticIP")).trim());
					}
					
					if(rMap.get("usrNeckName") != null){
						userInfoModel.setUsr_neckname(((String)rMap.get("usrNeckName")).trim());
					}
					if(rMap.get("usrPassword") != null){
						userInfoModel.setUsr_password(((String)rMap.get("usrPassword")).trim());
					}
					if(rMap.get("usrComment") != null){
						userInfoModel.setUsr_comment(((String)rMap.get("usrComment")).trim());
					}
					if(rMap.get("usrState") != null){
						userInfoModel.setUsr_state(Integer.parseInt(((String)rMap.get("usrState")).trim()));
					}
					
					if(rMap.get("ipaddrAllocateType") != null){
						userInfoModel.setIpaddr_allocate_type(Integer.valueOf(rMap.get("ipaddrAllocateType")+""));
					}
					
					if(rMap.get("usrStartStaticip") != null){
						userInfoModel.setUsr_start_staticip((String)rMap.get("usrStartStaticip"));
					}
					
					if(rMap.get("usrEndStaticip") != null){
						userInfoModel.setUsr_end_staticip((String)rMap.get("usrEndStaticip"));
					}
										
					if(rMap.get("lteUsrState") != null){
						String lteUsrState = ((String)rMap.get("lteUsrState")).trim();
						if("1".equals(lteUsrState)){
							if(rMap.get("srvSingleCallLteFlag") != null){
								String srvSingleCallLteFlag = Integer.toBinaryString(Integer.parseInt(((String)rMap.get("srvSingleCallLteFlag")).trim()));
								StringBuilder sb = new StringBuilder();
								if(srvSingleCallLteFlag.length() < 6){
									for(int i = 0; i < (6-srvSingleCallLteFlag.length()); i++){
										sb.append("0");
									}
								}
								userInfoModel.setSrv_sigcalllte_flag(sb.toString()+srvSingleCallLteFlag);
							}
							if(rMap.get("srvGroupCallLteFlag") != null){
								String srvGroupCallLteFlag = Integer.toBinaryString(Integer.parseInt(((String)rMap.get("srvGroupCallLteFlag")).trim()));
								StringBuilder sb = new StringBuilder();
								System.out.println(srvGroupCallLteFlag.length());
								if(srvGroupCallLteFlag.length() < 3){
									for(int i = 0; i < (3-srvGroupCallLteFlag.length()); i++){
										sb.append("0");
									}
								}
								userInfoModel.setSrv_grpcalllte_flag(sb.toString()+srvGroupCallLteFlag);
							}
							if(rMap.get("srvUsrPriority") != null){
								userInfoModel.setSrv_usr_pri(((String)rMap.get("srvUsrPriority")).trim());
							}
							// 录音开关
							if(rMap.get("audioRecordFlag") != null){
								userInfoModel.setAudioRecordFlag(((String)rMap.get("audioRecordFlag")).trim());
							}
						}
						userInfoModel.setLte_usestate(lteUsrState);
					}
					
					
				}
			}			
			//查询PGIS业务
			Map<String,Object> pgismap = new HashMap<String,Object>();
			
			pgismap.put("usrNumber", userInfoModel.getUsr_number());
			Map<String,Object> pgisresultMap = ossAdapter.invoke(0xa3, 0x04, pgismap);
			String pgisflag = (String) pgisresultMap.get("lteFlag");
			List<Map> pgisresultList = (List<Map>) pgisresultMap.get("srvInfo");
			
			if(null != pgisresultList){
				if(pgisresultList.size() > 0){
					Map rMap = pgisresultList.get(0);
					if(rMap.get("srv_location_flag") != null){
						userInfoModel.setSrv_location_flag(((String)rMap.get("srv_location_flag")).trim());
					}
					if(rMap.get("srv_bai_flag") != null){
						userInfoModel.setSrv_bai_flag(((String)rMap.get("srv_bai_flag")).trim());
					}
				}
			}
			
			
			//前转业务
			Map<String,Object> forwardBuzMap = new HashMap<String,Object>();
			forwardBuzMap.put("usrNumber", userInfoModel.getUsr_number());
			Map<String,Object> forwardBuzResultMap = ossAdapter.invoke(0xa4, 0x04, forwardBuzMap);
			String forwardBuzFlag = (String) forwardBuzResultMap.get("lteFlag");
			userInfoModel.setForwardBuzFlag(forwardBuzFlag);
			if("0".equals(forwardBuzFlag)){
				if(forwardBuzResultMap.get("fwdCondition") != null){
					userInfoModel.setFwd_condition(((String)forwardBuzResultMap.get("fwdCondition")).trim());
				}
				if(forwardBuzResultMap.get("fwdServeType") != null){
					userInfoModel.setFwd_servtype(((String)forwardBuzResultMap.get("fwdServeType")).trim());
				}
				if(forwardBuzResultMap.get("fwdSHour") != null){
					userInfoModel.setShour(((String)forwardBuzResultMap.get("fwdSHour")).trim());
				}
				if(forwardBuzResultMap.get("fwdEHour") != null){
					userInfoModel.setEhour(((String)forwardBuzResultMap.get("fwdEHour")).trim());
				}
				if(forwardBuzResultMap.get("fwdNumber") != null){
					userInfoModel.setFwd_number(((String)forwardBuzResultMap.get("fwdNumber")).trim());
				}				
			}
		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("modifyUserInfo error:"+e.toString());
		}
		logger.debug("modifyUserInfo-end");
		return SUCCESS;
	}
	
	/**
	 * 修改用户信息
	 * @return
	 */
	public String modifyUserInfo(){
		logger.debug("modifyUserInfo-start");
		JSONObject json = new JSONObject();
		try{
			//基本信息
			Map<String,Object> baseInfoMap = new HashMap<String,Object>();
			baseInfoMap.put("usrNumber", userInfoModel.getUsr_number());
			baseInfoMap.put("usrNumberType", userInfoModel.getUsr_numbertype());
			baseInfoMap.put("usrNeckName", userInfoModel.getUsr_neckname());			
			baseInfoMap.put("usrPassword", userInfoModel.getUsr_password());	
			baseInfoMap.put("usrComment", userInfoModel.getUsr_comment());
			baseInfoMap.put("usrState", userInfoModel.getUsr_state());			
			baseInfoMap.put("usrHaveImsi", userInfoModel.getUsr_haveimsi());
			if("1".equals(userInfoModel.getUsr_haveimsi())){
				baseInfoMap.put("imsi", userInfoModel.getImsi());
				baseInfoMap.put("tmpID", userInfoModel.getTmp_id());
			}
			//baseInfoMap.put("usrAuthflag", userInfoModel.getUsr_authflag());
			
			baseInfoMap.put("ipaddrAllocateType", userInfoModel.getIpaddr_allocate_type());
			if(userInfoModel.getIpaddr_allocate_type() == 0){
				baseInfoMap.put("usrStaticIP","0.0.0.0");
			}else if(userInfoModel.getIpaddr_allocate_type() == 1){
				if(null != userInfoModel.getUsr_staticip()){
					if(!"".equals(userInfoModel.getUsr_staticip())){
						baseInfoMap.put("usrStaticIP", userInfoModel.getUsr_staticip());	
					}else{
						baseInfoMap.put("usrStaticIP","0.0.0.0");		
					}						
				}else{
					baseInfoMap.put("usrStaticIP","0.0.0.0");	
				}
			}else if(userInfoModel.getIpaddr_allocate_type() == 2){
				baseInfoMap.put("usrStartStaticip",userInfoModel.getUsr_start_staticip());
				baseInfoMap.put("usrEndStaticip",userInfoModel.getUsr_end_staticip());
			}
			Map<String,Object> baseInfoResultMap = ossAdapter.invoke(0xa1, 0x03, baseInfoMap);
			String baseInfoflag = (String) baseInfoResultMap.get("lteFlag");
			if("0".equals(baseInfoflag)){
				json.put("userResult", true);
				
			}else{
				json.put("userResult", false);
				json.put("userMessage", LteFlag.flagReturn(baseInfoflag));
				json.put("hasBuz", false);
				json.put("hasForward", false);
				json.put("emgResult", true);
				ajaxMethod(json.toString());
				return NONE;
			}	
			
			
			//基本业务
			String lte_usestate = userInfoModel.getLte_usestate();
			//若开通基本业务
			if("1".equals(lte_usestate)){
				json.put("hasBuz", true);
				Map<String,Object> baseBuzMap = new HashMap<String,Object>();
				baseBuzMap.put("usrNumber", userInfoModel.getUsr_number());		
				
				//map.put("lteUsrState", userInfoModel.getLte_usestate());
				String srv_sigcalllte_flag = userInfoModel.getSrv_sigcalllte_flag();
				String srvGroupCallLteFlag = userInfoModel.getSrv_grpcalllte_flag();
				baseBuzMap.put("srvSingleCallLteFlag",parseLongStr(srv_sigcalllte_flag));
				baseBuzMap.put("srvGroupCallLteFlag", parseShortStr(srvGroupCallLteFlag));
				baseBuzMap.put("srvUsrPriority", userInfoModel.getSrv_usr_pri());
				baseBuzMap.put("audioRecordFlag", userInfoModel.getAudioRecordFlag());
				baseBuzMap.put("srv_location_flag", userInfoModel.getSrv_location_flag());
				baseBuzMap.put("srv_bai_flag", userInfoModel.getSrv_bai_flag());
				Map<String,Object>  baseBuzResultMap = ossAdapter.invoke(0xa3, 0x03, baseBuzMap);
				
				String baseBuzflag = (String) baseBuzResultMap.get("lteFlag");
				if("0".equals(baseBuzflag)){
					json.put("buzResult", true);
				}else{
					json.put("buzResult", false);
					json.put("buzMessage", LteFlag.flagReturn(baseBuzflag));
				}	
				
				//夜服业务是否开通
				String[] nightBuz = srv_sigcalllte_flag.split(",");
				if("1".equals(nightBuz[1])){
					json.put("hasForward", true);
					//前传业务 
					Map<String,Object> forwardBuzMap = new HashMap<String,Object>();
					String fwd_condition = userInfoModel.getFwd_condition();
					forwardBuzMap.put("usrNumber", userInfoModel.getUsr_number());
					forwardBuzMap.put("fwdCondition",fwd_condition);
					if("1".equals(fwd_condition)){
						String fwd_servtype =  userInfoModel.getFwd_servtype();
						forwardBuzMap.put("fwdServeType", fwd_servtype);
						
						if("1".equals(fwd_servtype)){
							forwardBuzMap.put("fwdSHour", userInfoModel.getShour());
							forwardBuzMap.put("fwdEHour", userInfoModel.getEhour());									
						}	
						forwardBuzMap.put("fwdNumber", userInfoModel.getFwd_number());
						
					}
					Map<String,Object>  forwardBuzResultMap = ossAdapter.invoke(0xa4, 0x03, forwardBuzMap);
					
					String forwardBuzFlagflag = (String) forwardBuzResultMap.get("lteFlag");
					if("0".equals(forwardBuzFlagflag)){
						json.put("forwardResult", true);
					}else{
						json.put("forwardResult", false);
						json.put("forwardMessage", LteFlag.flagReturn(forwardBuzFlagflag));
					}
				}else{
					if(queryuserInfoModel.getLte_usestate().equals("1")){						
						String srv_sigcalllte_flagUser = queryuserInfoModel.getSrv_sigcalllte_flag();
						if(srv_sigcalllte_flagUser.charAt(1) == '1'){
							json.put("hasForward", true);
							
							//之前开通过前转业务，取消前转业务 
							Map<String,Object> forwardBuzMap = new HashMap<String,Object>();
							forwardBuzMap.put("usrNumber", userInfoModel.getUsr_number());
							Map<String,Object>  forwardBuzResultMap = ossAdapter.invoke(0xa4, 0x02, forwardBuzMap);
							String forwardBuzFlagflag = (String) forwardBuzResultMap.get("lteFlag");
							if("0".equals(forwardBuzFlagflag)){
								json.put("forwardResult", true);
							}else{
								json.put("forwardResult", false);
								json.put("forwardMessage", LteFlag.flagReturn(forwardBuzFlagflag));
							}
						}else{
							json.put("hasForward", false);
						}						
					}else{
						json.put("hasForward", false);
					}					
				}
			}else{
				//不开通基本业务
				//之前是否开通过基本业务
				if(queryuserInfoModel.getLte_usestate().equals("1")){
					//之前开通过基本业务
					json.put("hasBuz", true);
					//取消基本业务
					Map<String,Object> baseBuzMap = new HashMap<String,Object>();
					baseBuzMap.put("usrNumber", userInfoModel.getUsr_number());		
					Map<String,Object>  baseBuzResultMap = ossAdapter.invoke(0xa3, 0x02, baseBuzMap);
					String baseBuzflag = (String) baseBuzResultMap.get("lteFlag");
					if("0".equals(baseBuzflag)){
						json.put("buzResult", true);
					}else{
						json.put("buzResult", false);
						json.put("buzMessage", LteFlag.flagReturn(baseBuzflag));
					}	
					
					String srv_sigcalllte_flagUser = queryuserInfoModel.getSrv_sigcalllte_flag();
					//之前是否有前转业务
					if(srv_sigcalllte_flagUser.charAt(1) == '1'){
						//之前有前传业务
						json.put("hasForward", true);
						//取消前转业务 
						Map<String,Object> forwardBuzMap = new HashMap<String,Object>();
						forwardBuzMap.put("usrNumber", userInfoModel.getUsr_number());
						Map<String,Object>  forwardBuzResultMap = ossAdapter.invoke(0xa4, 0x02, forwardBuzMap);
						String forwardBuzFlagflag = (String) forwardBuzResultMap.get("lteFlag");
						if("0".equals(forwardBuzFlagflag)){
							json.put("forwardResult", true);
						}else{
							json.put("forwardResult", false);
							json.put("forwardMessage", LteFlag.flagReturn(forwardBuzFlagflag));
						}
					}else{
						json.put("hasForward", false);
					}
				}else{
					//之前未开通过基本业务
					json.put("hasBuz", false);
					json.put("hasForward", false);
				}								
			}
			
			
			
			ajaxMethod(json.toString());
		}catch(NoAuthorityException e){
			json.put("status", -1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
			logger.error("addSysAddress error:"+e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("modifyUserInfo error:"+e.toString());
		}
		logger.debug("modifyUserInfo-end");
		return NONE;
	}
	
	/**
	 * 删除用户信息
	 * @return
	 */
	public String deleteUserInfo(){
		logger.debug("deleteUserInfo-start");
		System.out.println(queryuserInfoModel+"--------");
		JSONObject json = new JSONObject();
		try{			
			String usr_number = userInfoModel.getUsr_number();
			String[] usr_numberArray = usr_number.split(",");
			if(usr_numberArray.length == 1){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("usrNumber", usr_number);
				Map<String,Object> resultMap = ossAdapter.invoke(0xa1, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					return NONE;
				}
			}else{
				for(String str : usr_numberArray){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("usrNumber", str);
					Map<String,Object> resultMap = ossAdapter.invoke(0xa1, 0x02, map);
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
			logger.error("addSysAddress error:"+e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteUserInfo error:"+e.toString());
			ajaxMethod("error");
		}
		logger.debug("deleteUserInfo-end");
		return NONE;
	}
	
	/**
	 * 查看用户是否存在
	 * @return
	 */
	public String userExist(){
		
		logger.info("userExist - start");
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			map.put("ltePageSize", "1");
			map.put("ltePageIndex", "1");
			map.put("usrNumber", queryuserInfoModel.getUsr_number());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa1, 0x05, map);
			
			String flag = (String) resultMap.get("lteFlag");
			if((!"0".equals(flag)) || (resultMap.get("usrInfo") == null)){
				ajaxMethod("empty");
			}else{
				ajaxMethod("notempty");
			}

		}catch (Exception e){
			e.printStackTrace();
			logger.info("userExist error: "+e.toString());
		}
		
		logger.info("userExist - end");
		
		return NONE;
	}
	
	/**
	 * 查询IMSI是否存在
	 * @return
	 */
	public String imsiExist(){
		logger.info("imsiExist - start");		
		try{
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ltePageSize","1");
			map.put("ltePageIndex","1");
			map.put("imsi",querywirelessImsiModel.getImsi());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa0, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if((!"0".equals(flag)) || (resultMap.get("imsiInfo") == null)){
				ajaxMethod("empty");
			}else{
				ajaxMethod("notempty");
			}
			
		}catch (Exception e){
			e.printStackTrace();
			logger.error("imsiExist error:"+e);
		}		
		logger.info("imsiExist - end");
		return NONE;
	}

	//异步请求返回字符串
	private void ajaxMethod(String content){
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
	
	//解析字符串
	private String parseLongStr(String content){
		int result = 0;
		String[] rArray = content.split(",");
		for(int i = 0; i < 6; i ++){
			if(i == 0){
				result += Integer.parseInt(rArray[i])*2*2*2*2*2;
			}else if(i == 1){
				result += Integer.parseInt(rArray[i])*2*2*2*2;
			}else if(i == 2){
				result += Integer.parseInt(rArray[i])*2*2*2;
			}else if(i == 3){
				result += Integer.parseInt(rArray[i])*2*2;
			}else if(i == 4){
				result += Integer.parseInt(rArray[i])*2;
			}else if(i == 5){
				result += Integer.parseInt(rArray[i]);
			}
		}
		return result+"";
	}
	
	private String parseShortStr(String content){
		int result = 0;
		String[] rArray = content.split(",");
		for(int i = 0; i < 3; i ++){
			if(i == 0){
				result += Integer.parseInt(rArray[i])*2*2;
			}else if(i == 1){
				result += Integer.parseInt(rArray[i])*2;
			}else if(i == 2){
				result += Integer.parseInt(rArray[i]);
			}
		}
		return result+"";
	}
	public OnlinePage getOnlinePage()
	{
		return onlinePage;
	}

	public void setOnlinePage(OnlinePage onlinePage)
	{
		this.onlinePage = onlinePage;
	}

	public UserInfoModel getUserInfoModel()
	{
		return userInfoModel;
	}

	public void setUserInfoModel(UserInfoModel userInfoModel)
	{
		this.userInfoModel = userInfoModel;
	}

	public List<UserInfoModel> getUserInfoModelList()
	{
		return userInfoModelList;
	}

	public void setUserInfoModelList(List<UserInfoModel> userInfoModelList)
	{
		this.userInfoModelList = userInfoModelList;
	}

	public List<UserTemplateModel> getUserTemplateModelList() {
		return userTemplateModelList;
	}

	public void setUserTemplateModelList(
			List<UserTemplateModel> userTemplateModelList) {
		this.userTemplateModelList = userTemplateModelList;
	}

	public UserInfoModel getQueryuserInfoModel()
	{
		return queryuserInfoModel;
	}

	public void setQueryuserInfoModel(UserInfoModel queryuserInfoModel)
	{
		this.queryuserInfoModel = queryuserInfoModel;
	}

	public WirelessImsiModel getQuerywirelessImsiModel()
	{
		return querywirelessImsiModel;
	}

	public void setQuerywirelessImsiModel(WirelessImsiModel querywirelessImsiModel)
	{
		this.querywirelessImsiModel = querywirelessImsiModel;
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
