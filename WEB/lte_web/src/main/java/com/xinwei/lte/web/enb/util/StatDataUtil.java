/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.cache.EnbRealtimeItemConfigCache;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;
import com.xinwei.minas.enb.core.utils.EnbStatConstants;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.system.action.web.WebConstants;

/**
 * 
 * 统计数据显示助手
 * 
 * @author fanhaoyu
 * 
 */

public class StatDataUtil {

	private static Map<String, String> unitMap;

	/**
	 * 获取要显示的单位
	 * 
	 * @param unitConst
	 * @return
	 */
	public static String getUnitToShow(String unitConst) {
		if (unitConst == null || unitConst.equals(""))
			return "无";
		if (unitMap == null) {
			unitMap = new HashMap<String, String>();
			unitMap.put(EnbStatConstants.UNIT_BPS, "bps");
			unitMap.put(EnbStatConstants.UNIT_KBPS, "Kbps");
			unitMap.put(EnbStatConstants.UNIT_MBPS, "Mbps");
			unitMap.put(EnbStatConstants.UNIT_HUNDREDTH, "%");
			unitMap.put(EnbStatConstants.UNIT_PERC, "%");
			unitMap.put(EnbStatConstants.UNIT_KBIT, "KBIT");
			unitMap.put(EnbStatConstants.UNIT_MS,"毫秒");
			unitMap.put(EnbStatConstants.UNIT_KBITPS, "KBIT/S");
			unitMap.put(EnbStatConstants.UNIT_BYTE, "BYTE");
			unitMap.put(EnbStatConstants.UNIT_TEN_THOUSANDTH, "%%");
			unitMap.put(EnbStatConstants.UNIT_ENTRY, "个");
			unitMap.put(EnbStatConstants.UNIT_ONCE, "次");
			unitMap.put(EnbStatConstants.UNIT_DBM, "dbm");
			unitMap.put(EnbStatConstants.UNIT_PERC100, "%*100");
		}
		return unitMap.get(unitConst);
	}

	/**
	 * 根据同一时间的一组数据判断当前数据的单位
	 * 
	 * @param itemId
	 * @param dataList
	 * @return
	 */
	public static String getCurrentUnit(int itemId,
			List<EnbRealtimeItemData> dataList) {
		EnbRealtimeItemConfig itemConfig = EnbRealtimeItemConfigCache
				.getInstance().getConfig(itemId);
		String unit = itemConfig.getUnit();
		// 对单位为bps的项特殊处理
		// 单位 bps
		// 最大值取值区间 Y坐标显示单位 换算规则
		// 0~（1024-1） bps 显示值=原值
		// 1024~（1024*1024-1） kbps 显示值=(原值/1024)
		// (1024*1024)~(1024*1024*1024-1) mbps 显示值=（原值/(1024*1024)）
		if (unit.equals(EnbStatConstants.UNIT_BPS)) {
			double maxValue = getMaxStatValue(dataList);
			if (maxValue >= 0 && maxValue <= 1023) {
				return EnbStatConstants.UNIT_BPS;
			}
			double temp = 1024.0 * 1024.0;
			if (maxValue >= 1024 && maxValue <= temp - 1) {
				return EnbStatConstants.UNIT_KBPS;
			} else {// if (value >= temp) {
				return EnbStatConstants.UNIT_MBPS;
			}
		}
		return unit;
	}

	public static double getMaxStatValue(List<EnbRealtimeItemData> dataList) {
		if (dataList == null || dataList.isEmpty())
			return 0;
		double max = Double.MIN_VALUE;
		for (EnbRealtimeItemData itemData : dataList) {
			double value = itemData.getStatValue();
			if (value > max) {
				max = value;
			}
		}
		return max;
	}

	/**
	 * 数值根据单位进行变换
	 * 
	 * @param unit
	 * @param value
	 * @return
	 */
	public static double convertValueByUnit(int itemId, String unit, double value) {
		if (unit.equals(EnbStatConstants.UNIT_BPS)) {
			// bps
			return value;
		} else if (unit.equals(EnbStatConstants.UNIT_KBPS)) {
			// 转换成Kbps
			if(itemId == 26001 || itemId == 26016)
				return formatTo2Decimal(value /1000);
			else
			return formatTo2Decimal(value / 1024.0);
		} else if (unit.equals(EnbStatConstants.UNIT_MBPS)) {
			if(itemId == 26001 || itemId == 26016)
				return formatTo2Decimal(value/(1000*1000));
			else
			// 转换成Mbps
			return formatTo2Decimal(value / (1024.0 * 1024.0));
		}
		return value;
	}

	/**
	 * 根据统计项显示值与原值转换关系获取显示值
	 * 
	 * @param itemId
	 * @param value
	 * @return
	 */
	public static double getValueToShow(long moId,int itemId, double value) {
		EnbRealtimeItemConfig itemConfig = EnbRealtimeItemConfigCache
				.getInstance().getConfig(itemId);
		String exp = itemConfig.getExp();
		List<Long> moIds = new ArrayList<Long>();
		List<Enb> enbList = new ArrayList<Enb>();
		
		if (exp.equals(EnbStatConstants.TEN_TIMES)) {
			return value / 10;
		}
		if(exp.equals(EnbStatConstants.FOURTIMES) ){
			try {
				EnbBasicFacade facade = MinasSession.getInstance().getFacade(MinasSession.DEFAULT_SESSION_ID,
						EnbBasicFacade.class);
				moIds.add(moId);
			  enbList = facade.queryByMoIdList(moIds);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(enbList.get(0).getEnbType()== 0){
				
				return value*4;
			}
		}
		
		return value;
	}

	/**
	 * 保留两位小数
	 * 
	 * @param number
	 * @return
	 */
	public static double formatTo2Decimal(double number) {
		return Double.valueOf(String.format("%.2f", number));
	}
    
}
