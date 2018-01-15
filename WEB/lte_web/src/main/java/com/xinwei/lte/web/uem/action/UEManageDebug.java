package com.xinwei.lte.web.uem.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.lte.web.uem.model.ParaConverter;
import com.xinwei.lte.web.uem.model.UEDisplayModel;
import com.xinwei.lte.web.uem.utils.UemConstantUtils;

public class UEManageDebug {
	private int curPage;
	private int countPerPage;
	
	private String QueryUeInfoDebug(int type) {
		List<Map<String,String>> UEList = new ArrayList();
		Map<String,String> UEMap = null;
		int UEMaxCount = 20;
		int i =0;
		String IMSIStart = "460030912121";
		String UESeqStart = "1111110000";
		String[] WorkMode = {"集抄","配网自动化","负控","视频","其它"};
		String[] AlarmStatus = {"告警屏蔽","告警开启"};
		String[] ReportSwitch = {"开","关"};
		String[] AntType = {"内置","外置"};
		String[] UEType = {"CPE","海康","普天"};
		String[] UEStatus = {"在线","掉线","故障"};
		String VerStart = "1.0.";
		String IPStart = "10.10.10.";		
		if(type ==2){
			UEMap =  new HashMap();
			UEMap.put("UEID", i+"");
			UEMap.put("IMSI", (IMSIStart+i)+String.format("%1$0"+(15-IMSIStart.length())+"d", 0));			
			UEMap.put("BussinessType", 1+"");	
	
			
			UEMap.put("Location", "西安");			
			UEMap.put("Latitude", i+"");
			UEMap.put("Longitude", i+"");
			
			UEMap.put("BitRate", 1+"");
			UEMap.put("DataBit", 1+"");
			UEMap.put("StopBit", 1+"");
			UEMap.put("CheckBit", 1+"");
			UEMap.put("AlarmMaskBit", 1+"");		
			UEMap.put("DataReportPeriod", (int)(1+Math.random()*10)+"");
			UEMap.put("ControlReportPeriod", (int)(2+Math.random()*10)+"");
			UEMap.put("NTPSwitch", 1+"");
			UEMap.put("reportSwitch", "2");
			
			
			return JSONArray.fromObject(UEMap).toString();
		}
		
		for(i=0;i < 60;i++)
		{
			UEMap =  new HashMap();
			if(type ==0){
				UEMap.put("UEID", i+"");
				UEMap.put("IMSI", (IMSIStart+i)+String.format("%1$0"+(15-IMSIStart.length())+"d", 0));			
				UEMap.put("BussinessType", 0+"");
				UEMap.put("Version", VerStart+i);
				UEMap.put("Serial", (UESeqStart+i)+String.format("%1$0"+(15-UESeqStart.length())+"d", 0));
				UEMap.put("IP", IPStart+(i%255+1));
				UEMap.put("Type", WorkMode[i%WorkMode.length]);
				UEMap.put("Loc", "西安");			
				UEMap.put("Latitude", i+"");
				UEMap.put("Longitude", i+"");
				
				UEMap.put("BitRate", i+"kbps");
				UEMap.put("DataBit", i%8+"");
				UEMap.put("StopBit", i%8+"");
				UEMap.put("CheckBit", i%8+"");
				UEMap.put("AlarmMaskBit", AlarmStatus[i%2]);			
				UEMap.put("reportSwitch", ReportSwitch[i%2]);			
				
				UEMap.put("ntp", ReportSwitch[i%2]);
				
				
				UEMap.put("AntType",AntType[i%2]);
				
				UEMap.put("HardWareVer", VerStart+i);
				
				UEMap.put("OSVer", VerStart+i);
				UEMap.put("ModemVer", VerStart+i);
				UEMap.put("AppVer", VerStart+i);
				UEMap.put("ConstructorLen",i+"");
				UEMap.put("Constructor", UEType[i%3]);
				UEMap.put("UDPPort", i+"");
			}
			else if(type ==1){
				UEMap.put("SignalStrength", (int)(1+Math.random()*10)+"");
				UEMap.put("RecentStateChange","2012091315302"+i%60);
				UEMap.put("PCI", i+"");
				UEMap.put("UlRate", (int)(1+Math.random()*10)+"");
				UEMap.put("DlRate", (int)(2+Math.random()*10)+"");
				UEMap.put("RSRP", (int)(10+Math.random()*10)+"");
				UEMap.put("SNR", (int)(10+Math.random()*10)+"");
				UEMap.put("RSSI", (int)(10+Math.random()*10)+"");
				UEMap.put("UpTime", i+"");
				UEMap.put("UEID", i+"");
				UEMap.put("Status", UEStatus[i%3]);
				UEMap.put("ClientNotifyPeriod", "");
				UEMap.put("ServerQueryPeriod", "");
			}
	
			UEList.add(UEMap);
		}
		if(type ==0){
			JSONObject json = new JSONObject();
			if(getCurPage() !=0){
				UEList = UEList.subList((getCurPage()-1)*getCountPerPage(), (getCurPage())*getCountPerPage());
			}
			json.put("UE", UEList);
			json.put("Statistics", new HashMap<String, String>(){{put("TotalPage","4");}});
			return json.toString();
		}
		else{
			return JSONArray.fromObject(UEList).toString();
		}
		
		
	}

	public String QueryStaticUeInfoDebug(String curPage,String count){		
		setCountPerPage(Integer.parseInt(count));
		setCurPage(Integer.parseInt(curPage));
		return QueryUeInfoDebug(0);		
	}
	public void QueryDynamicUeInfoDebug(){		
		
		Util.ajaxSimpleUtil(QueryUeInfoDebug(1));
	}
	public String QueryConfigUeInfoDebug(){		
		
		return QueryUeInfoDebug(2);
	}
	private List<Map<String,String>> HandleResultInfo(int msgInfo, String body){
		
		HashMap<String, ParaConverter> HandleMap = null;	
		List<Map<String,String>> UEList = new ArrayList();
		Map<String,String> UEMap = null;
		int UEIndex = 0;
		int ParaIndex = 0;		
		List UEListJson = null;
		
		if(body ==null || body.length()==0){
			
			return null;
		}		
		
		switch(msgInfo){
			case UemConstantUtils.MSG_INFO_STATIC:
				HandleMap = UemConstantUtils.DISPLAY_STATIC;
				JSONObject Obj = JSONObject.fromObject(body);
				if(Obj.isEmpty()){
					
					return null;
				}
				
				try{
					UEListJson = (List<JSONObject>)Obj.get("UE");	
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
				for(UEIndex=0;UEIndex<UEListJson.size();UEIndex++){
					UEMap =  new HashMap();
					JSONObject UEInstJson = (JSONObject)UEListJson.get(UEIndex);
					for(Map.Entry<String, ParaConverter> entry:HandleMap.entrySet()){
						String ParaKey = entry.getKey();
						String ParaVal = entry.getValue().convert((String) UEInstJson.get(ParaKey));
						UEMap.put(ParaKey,ParaVal);
					}
					UEList.add(UEMap);
				}					
				return UEList;
				
			case UemConstantUtils.MSG_INFO_CONFIG:				
				JSONObject UEInstJson = (JSONObject)UEListJson.get(0);
				String[] DisplayParas = UemConstantUtils.DISPLAY_CONFIG;
				Map<String,String> UEConfigInst = new HashMap<String, String>(){};
				for(ParaIndex=0;ParaIndex < DisplayParas.length;ParaIndex++){
					String ParaName = DisplayParas[ParaIndex];
					String ParaVal = (String) UEInstJson.get(ParaName);
					if(ParaVal==null||ParaVal.length()==0){						
						ParaVal = "";
					}
					UEConfigInst.put(ParaName,ParaVal);
				}
				
				break;
		}
		
		return null;
	}

	
	public int getCountPerPage() {
		return countPerPage;
	}

	public void setCountPerPage(int countPerPage) {
		this.countPerPage = countPerPage;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
}
