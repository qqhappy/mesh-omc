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
import com.xinwei.lte.web.lte.model.NumAnalyseModel;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.PdtConfigInfo;
import com.xinwei.lte.web.lte.model.SameRegionModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.oss.adapter.OssAdapter;

public class SameRegionAction extends ActionSupport{
	//记录日志
	private static Logger logger = LoggerFactory.getLogger(PdtConfigInfoAction.class);
	
	@Resource
	private OssAdapter ossAdapter;
	
	private List<SameRegionModel> sameRegionModelList;
	
	private JSONResult jsonResult;
	
	private OnlinePage onlinePage = new OnlinePage();
	
	private SameRegionModel sameRegionModel;

	/**
	 * 查询列表
	 * @return
	 */
	public String showSameRegionConfigInfo(){
		
		jsonResult = new JSONResult();
		sameRegionModelList = new ArrayList<SameRegionModel>();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ltePageSize", onlinePage.getPageSize()+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			if(null != sameRegionModel){
				if(null != sameRegionModel.getMmetaTal()){
					if(!"".equals(sameRegionModel.getMmetaTal())){
						map.put("mmetaTal", sameRegionModel.getMmetaTal());
					}
					
				}
			}else{
				sameRegionModel = new SameRegionModel();
			}
			Map<String,Object> resultMap = ossAdapter.invoke(0xc1, 0x05, map);
			
			int lteTotalQueryCount = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				return "SHOW_CONFIG_INFO";
			}
			if(lteTotalQueryCount%LteConstant.PageSize == 0){
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize);
			}else{
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize+1);
			}	
			List<Map> resultList = (List<Map>) resultMap.get("mbmsAreaInfo");
			SameRegionModel sameRegionModel = null;
			if(null != resultList){
				for(Map rMap : resultList){
					
					sameRegionModel = new SameRegionModel();
					
					if(rMap.get("mmetaId") != null){
						sameRegionModel.setMmetaId(rMap.get("mmetaId")+"");
					}
					
					if(rMap.get("mmetaTal") != null){
						sameRegionModel.setMmetaTal(rMap.get("mmetaTal")+"");
					}
					
					if(rMap.get("mmetaComment") != null){
						sameRegionModel.setMmetaComment(rMap.get("mmetaComment")+"");
					}
					
					sameRegionModelList.add(sameRegionModel);
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
		
		return "TO_ADD_PAGE";
	}
	/**
	 * 新增
	 * @return
	 */
	public String addSameRegion(){
		jsonResult = new JSONResult();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			
			map.put("mmetaId", sameRegionModel.getMmetaId());
			map.put("mmetaTal", sameRegionModel.getMmetaTal());
			map.put("mmetaComment", sameRegionModel.getMmetaComment());
			Map<String,Object> resultMap = ossAdapter.invoke(0xc1, 0x01, map);
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
	/**
	 * 删除
	 * @return
	 */
	public String deleteSameRegions(){
		jsonResult = new JSONResult();
		try {
			String mmetaIdStr = sameRegionModel.getMmetaId();
			String[] mmetaIds = mmetaIdStr.split(",");
			for(String mmetaId : mmetaIds){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("mmetaId", mmetaId);
				Map<String,Object> resultMap = ossAdapter.invoke(0xc1, 0x02, map);
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
	
	
	/**
	 * 跳转至修改页面
	 * @return
	 */
	public String toSaveRegionModifyPage(){
		
		String mmetaTal = sameRegionModel.getMmetaTal();
		
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ltePageSize", "1");
			map.put("ltePageIndex", "1");
			map.put("mmetaTal", mmetaTal);
			Map<String,Object> resultMap = ossAdapter.invoke(0xc1, 0x05, map);

			List<Map> resultList = (List<Map>) resultMap.get("mbmsAreaInfo");
			if(resultList == null || resultList.isEmpty()){
				throw new Exception("数据为空");
			}
			Map rMap = resultList.get(0);
			sameRegionModel.setMmetaId(rMap.get("mmetaId")+"");
			sameRegionModel.setMmetaTal(rMap.get("mmetaTal")+"");
			sameRegionModel.setMmetaComment(rMap.get("mmetaComment")+"");
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
	public String modifySameRegion(){
		jsonResult = new JSONResult();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("mmetaTal", sameRegionModel.getMmetaTal());
			map.put("mmetaComment", sameRegionModel.getMmetaComment());
			Map<String,Object> resultMap = ossAdapter.invoke(0xc1, 0x03, map);
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
	
	
	public List<SameRegionModel> getSameRegionModelList() {
		return sameRegionModelList;
	}

	public void setSameRegionModelList(List<SameRegionModel> sameRegionModelList) {
		this.sameRegionModelList = sameRegionModelList;
	}

	public OnlinePage getOnlinePage() {
		return onlinePage;
	}

	public void setOnlinePage(OnlinePage onlinePage) {
		this.onlinePage = onlinePage;
	}

	public SameRegionModel getSameRegionModel() {
		return sameRegionModel;
	}

	public void setSameRegionModel(SameRegionModel sameRegionModel) {
		this.sameRegionModel = sameRegionModel;
	}

	public JSONResult getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(JSONResult jsonResult) {
		this.jsonResult = jsonResult;
	}
	
	
	

}
