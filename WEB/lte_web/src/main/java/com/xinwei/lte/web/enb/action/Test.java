package com.xinwei.lte.web.enb.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;
import com.xinwei.omp.core.utils.DateUtils;

public class Test {
	
	public static void main(String[] args) {
		
		Map<String,Integer> map = new HashMap<String,Integer>();
		System.out.println(map.put("1", 1));
		System.out.println(map.put("2", 3));
		System.out.println(map.remove("2"));
		System.out.println(map.keySet().size());
		
		
//		EnbRealTimeDataCache cache = EnbRealTimeDataCache.getInstance();
//		//监控的小区列表
//		List<Integer> cellIdList = new ArrayList<Integer>();
//		{
//			cellIdList.add(1);
//			cellIdList.add(2);
//		}		
//		//监控的统计项列表
//		List<Integer> itemIdList = new ArrayList<Integer>();
//		{
//			itemIdList.add(12);
//			itemIdList.add(22);
//		}	
//		StringBuilder totalBuilder = new StringBuilder();
//		totalBuilder.append("[");
//		for(int cellId :cellIdList){
//			//单小区所有统计项列表
//			List<List<EnbRealtimeItemData>> allCounterItemList = new ArrayList<List<EnbRealtimeItemData>>();
//			//该组统计项列表中的最小时间
//			Long minTime = 0L;
//			for(int itemId : itemIdList){
//				//属于该小区的统计项list
//				List<EnbRealtimeItemData> myList = new ArrayList<EnbRealtimeItemData>();
//				//缓存中的最新数据
//				List<EnbRealtimeItemData> list  = makeList(itemId);
//				for(EnbRealtimeItemData data:list){
//					//TODO:此处需要预留接口判断entityType
//					String entityOid = data.getEntityOid();
//					String[] str = entityOid.split("\\.");
//					if(cellId == Integer.valueOf(str[1])){
//						myList.add(data);
//					}
//				}
//				//按照时间升序排列
//				sort(myList);
//				if(myList.size() > 0){
//					minTime = myList.get(0).getEndTime();
//				}					
//				allCounterItemList.add(myList);
//			}
//			StringBuilder allPointBuilder = new StringBuilder();
//			allPointBuilder.append("{\"cellId\":"+cellId+",\"data\":[");
//			for(int i = 0;i<3;i++){
//				long plusMinTime = plusTime(minTime, i);
//				StringBuilder pointBuilder = new StringBuilder();
//				int frame = 0;
//				if(allCounterItemList.size()>0){
//					List<EnbRealtimeItemData> list = allCounterItemList.get(0);
//					for(EnbRealtimeItemData data:list){
//						if(data.getEndTime() == plusMinTime){
//							frame = data.getSystemFrameNo();
//						}
//					}							
//				}
//				pointBuilder.append("{\"date\":\""+frame+"\\n"+getHourMinSec(plusMinTime)+"\",");
//				for(List<EnbRealtimeItemData> list :allCounterItemList){											
//					for(EnbRealtimeItemData data:list){
//						if(data.getEndTime() == plusMinTime){
//							pointBuilder.append("\""+data.getItemId()+"\":"+data.getStatValue()+",");
//						}
//					}						
//				}
//				pointBuilder.deleteCharAt(pointBuilder.length() - 1);
//				pointBuilder.append("},");
//				allPointBuilder.append(pointBuilder);
//			}
//			allPointBuilder.deleteCharAt(allPointBuilder.length() - 1);
//			allPointBuilder.append("]},");
//			totalBuilder.append(allPointBuilder);
//		}
//		totalBuilder.deleteCharAt(totalBuilder.length() - 1);
//		totalBuilder.append("]");
//		String jsonData = totalBuilder.toString();
//		if(jsonData.equals("]")){
//			jsonData = "[]";
//		}
//		System.out.println(jsonData);
		
		
	}
	
	private static void sort(List<EnbRealtimeItemData> list){
		Collections.sort(list,new Comparator<EnbRealtimeItemData>(){
			public int compare(EnbRealtimeItemData arg0 ,EnbRealtimeItemData arg1){
				return Long.valueOf(arg0.getEndTime()).compareTo(Long.valueOf(arg1.getEndTime()));
			}
		});
	}
	
	private static long plusTime(long time, int secondCount) {
		long millSecond = DateUtils.getMillisecondTimeFromBriefTime(time);
		// 最新时间减去secondeOffset
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(millSecond));
		calendar.add(Calendar.SECOND, secondCount);
		return DateUtils.getBriefTimeFromMillisecondTime(calendar
				.getTimeInMillis());
	}
	
	private static List<EnbRealtimeItemData> makeList(int itemId){
		List<EnbRealtimeItemData> list = new ArrayList<EnbRealtimeItemData>();
		{
			EnbRealtimeItemData data = new EnbRealtimeItemData();
			data.setSystemFrameNo(111);
			data.setEntityOid("enb.1");
			data.setItemId(12);
			data.setEndTime(20141212000000L);
			data.setStatValue(1.0);
			list.add(data);
		}
		{
			EnbRealtimeItemData data = new EnbRealtimeItemData();
			data.setSystemFrameNo(112);
			data.setEntityOid("enb.1");
			data.setItemId(12);
			data.setEndTime(20141212000001L);
			data.setStatValue(2.0);
			list.add(data);
		}
		{
			EnbRealtimeItemData data = new EnbRealtimeItemData();
			data.setSystemFrameNo(113);
			data.setEntityOid("enb.1");
			data.setItemId(12);
			data.setEndTime(20141212000002L);
			data.setStatValue(3.0);
			list.add(data);
		}
		{
			EnbRealtimeItemData data = new EnbRealtimeItemData();
			data.setSystemFrameNo(114);
			data.setEntityOid("enb.1");
			data.setItemId(22);
			data.setEndTime(20141212000000L);
			data.setStatValue(4.0);
			list.add(data);
		}
		{
			EnbRealtimeItemData data = new EnbRealtimeItemData();
			data.setSystemFrameNo(115);
			data.setEntityOid("enb.1");
			data.setItemId(22);
			data.setEndTime(20141212000001L);
			data.setStatValue(5.0);
			list.add(data);
		}
		{
			EnbRealtimeItemData data = new EnbRealtimeItemData();
			data.setSystemFrameNo(116);
			data.setEntityOid("enb.1");
			data.setItemId(22);
			data.setEndTime(20141212000002L);
			data.setStatValue(6.0);
			list.add(data);
		}
		{
			EnbRealtimeItemData data = new EnbRealtimeItemData();
			data.setSystemFrameNo(117);
			data.setEntityOid("enb.2");
			data.setItemId(12);
			data.setEndTime(20141212000000L);
			data.setStatValue(7.0);
			list.add(data);
		}
		{
			EnbRealtimeItemData data = new EnbRealtimeItemData();
			data.setSystemFrameNo(118);
			data.setEntityOid("enb.2");
			data.setItemId(12);
			data.setEndTime(20141212000001L);
			data.setStatValue(8.0);
			list.add(data);
		}
		{
			EnbRealtimeItemData data = new EnbRealtimeItemData();
			data.setSystemFrameNo(119);
			data.setEntityOid("enb.2");
			data.setItemId(12);
			data.setEndTime(20141212000002L);
			data.setStatValue(9.0);
			list.add(data);
		}
		{
			EnbRealtimeItemData data = new EnbRealtimeItemData();
			data.setSystemFrameNo(1110);
			data.setEntityOid("enb.2");
			data.setItemId(22);
			data.setEndTime(20141212000000L);
			data.setStatValue(10.0);
			list.add(data);
		}
		{
			EnbRealtimeItemData data = new EnbRealtimeItemData();
			data.setSystemFrameNo(1111);
			data.setEntityOid("enb.2");
			data.setItemId(22);
			data.setEndTime(20141212000001L);
			data.setStatValue(11.0);
			list.add(data);
		}
		{
			EnbRealtimeItemData data = new EnbRealtimeItemData();
			data.setSystemFrameNo(1112);
			data.setEntityOid("enb.2");
			data.setItemId(22);
			data.setEndTime(20141212000002L);
			data.setStatValue(12.0);
			list.add(data);
		}
		List<EnbRealtimeItemData> list2 = new ArrayList<EnbRealtimeItemData>();
		for(EnbRealtimeItemData data :list){
			if(data.getItemId() == itemId){
				list2.add(data);
			}
		}
		return list2;
		
		
		
	}
	private static String getHourMinSec(long time){
		String timeStr = String.valueOf(time);
		String hour = timeStr.substring(8, 10);
		String minute = timeStr.substring(10, 12);
		String second = timeStr.substring(12, 14);
		return hour+":"+minute+":"+second;
	}
	
	
	
}