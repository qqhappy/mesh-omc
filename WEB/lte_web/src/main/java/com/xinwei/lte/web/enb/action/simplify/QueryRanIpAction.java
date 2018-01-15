package com.xinwei.lte.web.enb.action.simplify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.lte.web.lte.model.SystemAddressModel;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 查询ran地址
 * @author zhangqiang
 *
 */
public class QueryRanIpAction extends ActionSupport {
	
	@Resource
	private OssAdapter ossAdapter;
	
	/**
	 * 查询ran地址
	 * @return
	 */
	public String queryRanIp(){
		String ranIp = null;
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			Map<String,Object> resultMap = ossAdapter.invoke(0xa9, 0x04, map);
			String ranId = null;
			if(resultMap.get("sysconfRANIPID") != null){
				ranId = ((String)resultMap.get("sysconfRANIPID")).trim();
			}
			
			Map<String,Object> addrMap = new HashMap<String,Object>();
			addrMap.put("ltePageSize","50");
			addrMap.put("ltePageIndex","1");
			Map<String,Object> addrResultMap = ossAdapter.invoke(0xa6, 0x05, addrMap);
			String flag = (String) resultMap.get("lteFlag");
			if("0".equals(flag)){
				List<Map> resultAddrList = (List<Map>) addrResultMap.get("ipInfo");
				if(null != resultAddrList){
					for(Map rMap : resultAddrList){
						String ipID = (String)rMap.get("ipID");
						if(ipID != null && ipID.equals(ranId)){
							ranIp = (String)rMap.get("ipAddr");
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			ranIp = "";
		}
		Util.ajaxSimpleUtil(ranIp);
		return NONE;
	}
}
