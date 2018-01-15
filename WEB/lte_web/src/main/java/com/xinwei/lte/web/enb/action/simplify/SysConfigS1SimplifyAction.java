package com.xinwei.lte.web.enb.action.simplify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;

public class SysConfigS1SimplifyAction extends ActionSupport {
	/**
	 * 基站ID
	 */
	private long enbId;

	private static final String tableName = "T_IPV4";

	/**
	 * 根据enbId从对应的IPV4表查询ip列表
	 * 
	 * @return
	 */
	public String getIpListFromIpv4() {
		JSONObject json = new JSONObject();
		try {
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			XBizTable xBizTable = facade.queryFromEms(getEnbMoId(enbId),
					tableName,new XBizRecord());
			List<XBizRecord> records = xBizTable.getRecords();
			List<Map<String,String>> ipList = new ArrayList<Map<String,String>>();
			for(XBizRecord record :records){
				String ipId = record.getFieldMap().get("u8IPID").getValue();
				String ipValue = record.getFieldMap().get("au8IPAddr").getValue();
				String ipString = turnHexIpToString(ipValue);
				Map<String,String> map = new HashMap<String,String>();
				map.put(ipId, ipString);
				ipList.add(map);		
			}
			json.put("flag", 0);
			json.put("message", JSONArray.fromObject(ipList));
		} catch (Exception e) {
			json.put("flag", 1);
			json.put("error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	/**
	 * 根据long型enbId获取moId
	 * 
	 * @param enbHexId
	 * @return
	 * @throws Exception
	 */
	private long getEnbMoId(long enbId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		Enb enb = facade.queryByEnbId(enbId);
		return enb.getMoId();
	}
	
	/**
	 * 将16进制的ip(ffffffff)转换为十进制(255.255.255.255)
	 * @param enbId
	 * @return
	 */
	private String turnHexIpToString(String hexIp){
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<hexIp.length();i=i+2){
			String str = String.valueOf(hexIp.charAt(i))+String.valueOf(hexIp.charAt(i+1));
			sb.append(Integer.valueOf(str, 16));
			sb.append(".");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

	public long getEnbId() {
		return enbId;
	}

	public void setEnbId(long enbId) {
		this.enbId = enbId;
	}

	

	

}
