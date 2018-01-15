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
import com.xinwei.lte.web.domain.JSONResult;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.PdtConfigInfo;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.oss.adapter.OssAdapter;

public class PdtConfigInfoAction extends ActionSupport{

	//记录日志
	private static Logger logger = LoggerFactory.getLogger(PdtConfigInfoAction.class);
	
	@Resource
	private OssAdapter ossAdapter;
	
	private JSONResult jsonResult;
	
	private List<PdtConfigInfo> pdtConfigInfoList;
	
	private OnlinePage onlinePage = new OnlinePage();
	
	private PdtConfigInfo pdtConfigInfo;
	
	/**
	 * 查询列表
	 * @return
	 */
	public String showConfigInfo(){
		
		jsonResult = new JSONResult();
		pdtConfigInfoList = new ArrayList<PdtConfigInfo>();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ltePageSize", onlinePage.getPageSize()+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			
			Map<String,Object> resultMap = ossAdapter.invoke(0xb9, 0x05, map);
			
			int lteTotalQueryCount = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				return SUCCESS;
			}
			if(lteTotalQueryCount%LteConstant.PageSize == 0){
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize);
			}else{
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize+1);
			}	
			
			List<Map> resultList = (List<Map>) resultMap.get("tagPeerPdtInfo");
			
			PdtConfigInfo configInfo = null;
			if(null != resultList){
				for(Map rMap : resultList){
					
					configInfo = new PdtConfigInfo();
					
					if(rMap.get("pdtId") != null){
						configInfo.setPdtId(Integer.valueOf(rMap.get("pdtId")+""));
					}
					
					if(rMap.get("pdtRemoteIp") != null){
						configInfo.setPdtRemoteIp(rMap.get("pdtRemoteIp")+"");
					}
					
					if(rMap.get("pdtRemotePort") != null){
						configInfo.setPdtRemotePort(Integer.valueOf(rMap.get("pdtRemotePort")+""));
					}
					
					if(rMap.get("pdtLinkState") != null){
						configInfo.setPdtLinkState(Integer.valueOf(rMap.get("pdtLinkState")+""));
					}
					
					if(rMap.get("comment") != null){
						configInfo.setComment(rMap.get("comment")+"");
					}
					pdtConfigInfoList.add(configInfo);
				}
			}else{
				if(onlinePage.getCurrentPageNum() == 1 ){
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}	
			}	
			
		} catch (Exception e) {
			jsonResult.setErrorCode("-1");
			jsonResult.setErrorMsg(e.getMessage());
			logger.debug("showConfigInfo error.",e);
			e.printStackTrace();
		}
		
		return "SHOW_CONFIG_INFO";
	}
	
	/**
	 * 跳转至新增页面
	 * @return
	 */
	public String toAddPage(){
		try {
			/*Map<String,Object> map = new HashMap<String,Object>();
			map.put("ltePageSize", "0");
			map.put("ltePageIndex", "0");
			map.put("pdtId", pdtConfigInfo.getPdtId());
			Map<String,Object> resultMap = ossAdapter.invoke(0xb9, 0x05, map);
			List<Map> resultList = (List<Map>) resultMap.get("tagPeerPdtInfo");
			Map rMap = resultList.get(0);
			if(rMap.get("pdtId") != null){
				pdtConfigInfo.setPdtId(Integer.valueOf(rMap.get("pdtId")+""));
			}
			
			if(rMap.get("pdtRemoteIp") != null){
				pdtConfigInfo.setPdtRemoteIp(rMap.get("pdtRemoteIp")+"");
			}
			
			if(rMap.get("pdtRemotePort") != null){
				pdtConfigInfo.setPdtRemotePort(Integer.valueOf(rMap.get("pdtRemotePort")+""));
			}
			
			if(rMap.get("pdtLinkState") != null){
				pdtConfigInfo.setPdtLinkState(Integer.valueOf(rMap.get("pdtLinkState")+""));
			}
			
			if(rMap.get("comment") != null){
				pdtConfigInfo.setComment(rMap.get("comment")+"");
			}*/
		} catch (Exception e) {
			logger.debug("toAddPage error.",e);
			e.printStackTrace();
		}
		return "TO_ADD_PAGE";
	}
	
	/**
	 * 新增
	 * @return
	 */
	public String add(){
		jsonResult = new JSONResult();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("pdtId", pdtConfigInfo.getPdtId());
			map.put("pdtRemoteIp", pdtConfigInfo.getPdtRemoteIp());
			map.put("pdtRemotePort", pdtConfigInfo.getPdtRemotePort());
			map.put("comment", pdtConfigInfo.getComment());
			Map<String,Object> resultMap = ossAdapter.invoke(0xb9, 0x01, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				jsonResult.setErrorCode("-1");
				jsonResult.setErrorMsg(LteFlag.flagReturn(flag));
			}
		} catch (Exception e) {
			logger.debug("add error.",e);
			e.printStackTrace();
			jsonResult.setErrorCode("-1");
			jsonResult.setErrorMsg(e.getMessage());
		}
		
		returnJsonObject(jsonResult);
		return NONE;
	}
	
	/**
	 * 跳转至修改页面
	 * @return
	 */
	public String toModifyPage(){
		
		int pdtId = pdtConfigInfo.getPdtId();
		
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ltePageSize", "1");
			map.put("ltePageIndex", "1");
			map.put("pdtId", pdtId);
			Map<String,Object> resultMap = ossAdapter.invoke(0xb9, 0x05, map);
			List<Map> resultList = (List<Map>) resultMap.get("tagPeerPdtInfo");
			if(resultList == null || resultList.isEmpty()){
				throw new Exception("数据为空");
			}
			Map rMap = resultList.get(0);
			pdtConfigInfo.setPdtRemoteIp(rMap.get("pdtRemoteIp")+"");
			pdtConfigInfo.setPdtRemotePort(Integer.valueOf(rMap.get("pdtRemotePort")+""));
			pdtConfigInfo.setComment(rMap.get("comment")+"");
		} catch (Exception e) {
			logger.debug("toModifyPage error.",e);
			e.printStackTrace();
		}
		
		return "TO_MODIFY_PAGE";
	}
	
	/**
	 * 修改
	 * @return
	 */
	public String modify(){
		jsonResult = new JSONResult();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("pdtId", pdtConfigInfo.getPdtId());
			map.put("pdtRemoteIp", pdtConfigInfo.getPdtRemoteIp());
			map.put("pdtRemotePort", pdtConfigInfo.getPdtRemotePort());
			map.put("comment", pdtConfigInfo.getComment());
			Map<String,Object> resultMap = ossAdapter.invoke(0xb9, 0x03, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				jsonResult.setErrorCode("-1");
				jsonResult.setErrorMsg(LteFlag.flagReturn(flag));
			}
		} catch (Exception e) {
			logger.debug("add error.",e);
			e.printStackTrace();
			jsonResult.setErrorCode("-1");
			jsonResult.setErrorMsg(e.getMessage());
		}
		
		returnJsonObject(jsonResult);
		return NONE;
	}
	
	/**
	 * 删除
	 * @return
	 */
	public String delete(){
		jsonResult = new JSONResult();
		try {
			String pdtIdStr = pdtConfigInfo.getPdtIdStr();
			String[] pdtIds = pdtIdStr.split(",");
			for(String pdtId : pdtIds){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("pdtId", pdtId);
				Map<String,Object> resultMap = ossAdapter.invoke(0xb9, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					jsonResult.setErrorCode("-1");
					jsonResult.setErrorMsg(LteFlag.flagReturn(flag));
					break;
				}
			}
		} catch (Exception e) {
			logger.debug("delete error.",e);
			e.printStackTrace();
			jsonResult.setErrorCode("-1");
			jsonResult.setErrorMsg(e.getMessage());
		}
		
		returnJsonObject(jsonResult);
		return NONE;
	}
	
	//异步请求返回单个对象
	private void returnJsonObject(Object content){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/jsp; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			
			JSONObject object = new JSONObject();
			object = JSONObject.fromObject(content);
			out.println(object.toString());
			out.flush();
		} catch (Exception e){
			e.printStackTrace();
		} finally{
			if (out != null)
				out.close();
		}
	}

	public JSONResult getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(JSONResult jsonResult) {
		this.jsonResult = jsonResult;
	}

	public List<PdtConfigInfo> getPdtConfigInfoList() {
		return pdtConfigInfoList;
	}

	public void setPdtConfigInfoList(List<PdtConfigInfo> pdtConfigInfoList) {
		this.pdtConfigInfoList = pdtConfigInfoList;
	}

	public PdtConfigInfo getPdtConfigInfo() {
		return pdtConfigInfo;
	}

	public void setPdtConfigInfo(PdtConfigInfo pdtConfigInfo) {
		this.pdtConfigInfo = pdtConfigInfo;
	}

	public OnlinePage getOnlinePage() {
		return onlinePage;
	}

	public void setOnlinePage(OnlinePage onlinePage) {
		this.onlinePage = onlinePage;
	}
	
}
