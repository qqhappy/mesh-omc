/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-11-4	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model.check;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * enb状态信息检查结果
 * 
 * @author chenlong
 * 
 */

public class EnbStateInfoResult extends DetailCheckResult {
	
	// 基站时间
	private String enbTime;
	
	// 运行时间
	private String runningTime;
	
	// 时钟类型0-gps;1-本板时钟
	private int clockType;
	
	// 时钟状态 <3:自由振荡(0:startup 1:warmup 2:fast) 3:锁定/同步(lock) 4:保持(holdover)
	// 5:保持超时(holdover timeout) 6:异常(abnormal)
	private int clockStatus;
	
	// 可视卫星数量
	private int visibleSatelliteNum;
	
	// 追踪卫星数量
	private int trackSatelliteNum;
	
	// BBU温度(℃)
	private int bbuTemperature;
	
	// 基站即时功率(瓦)
	private int power;
	
	// 风扇转速 (RPM)
	private int[] fanSpeeds;
	
	@Override
	public String getCheckName() throws Exception {
		return "eNB状态信息检查结果";
	}
	
	@Override
	public int check() throws Exception {
		// 时钟状态
		if (3 != clockStatus && 4 != clockStatus) {
			return EnbCheckConstants.NOT_NORMAL;
		}
		
		// 可视卫星数量
		if (visibleSatelliteNum < 3) {
			return EnbCheckConstants.NOT_NORMAL;
		}
		
		// 跟踪卫星数量
		if (trackSatelliteNum < 3) {
			return EnbCheckConstants.NOT_NORMAL;
		}
		// 风扇转速,当BBU温度大于30度时才做检查
		if(bbuTemperature > 30) {
			if (null != fanSpeeds && fanSpeeds.length > 0) {
				for (int i = 0; i < fanSpeeds.length; i++) {
					if (fanSpeeds[i] <= 0) {
						return EnbCheckConstants.NOT_NORMAL;
					}
				}
			}
		} 
		return EnbCheckConstants.NORMAL;
	}
	
	@Override
	public String checkDesc() throws Exception {
		StringBuilder sb = new StringBuilder();
		// 时钟状态
		if (3 != clockStatus && 4 != clockStatus) {
			sb.append("时钟状态故障.\n");
		}
		
		// 可视卫星数量
		if (visibleSatelliteNum < 3) {
			sb.append("可视卫星数量小于3.\n");
		}
		
		// 跟踪卫星数量
		if (trackSatelliteNum < 3) {
			sb.append("跟踪卫星数量小于3.\n");
		}
		
		// 风扇转速,当BBU温度大于30度时才做检查
		if(bbuTemperature > 30) {
			if (null != fanSpeeds && fanSpeeds.length > 0) {
				for (int i = 0; i < fanSpeeds.length; i++) {
					if (fanSpeeds[i] <= 0) {
						sb.append("风扇"+(i+1)+"故障.\n");
					}
				}
			}
		} 
		return sb.toString();
	}
	
	@Override
	public List<EnbCheckForm> getCheckForm() throws Exception {
		List<EnbCheckForm> checkForms = new ArrayList<EnbCheckForm>();
		// 基站时间
		addCheckForm(checkForms, "基站时间", enbTime, "",
				EnbCheckConstants.NOT_CHECK);
		// 基站运行时间
		addCheckForm(checkForms, "运行时间", getEnbRunningTime(runningTime), "",
				EnbCheckConstants.NOT_CHECK);
		// 时钟类型
		if (0 == clockType) {
			addCheckForm(checkForms, "时钟类型", "GPS", "",
					EnbCheckConstants.NOT_CHECK);
		}
		else if (1 == clockType) {
			addCheckForm(checkForms, "时钟类型", "本板时钟", "",
					EnbCheckConstants.NOT_CHECK);
		}
		else {
			addCheckForm(checkForms, "时钟类型", "未知", "",
					EnbCheckConstants.NOT_CHECK);
		}
		
		// 时钟状态
		if (clockStatus < 3) {
			addCheckForm(checkForms, "时钟状态", "自由振荡", "同步/保持",
					EnbCheckConstants.NOT_NORMAL);
		}
		else if (clockStatus == 3) {
			addCheckForm(checkForms, "时钟状态", "同步", "同步/保持",
					EnbCheckConstants.NORMAL);
		}
		else if (clockStatus == 4) {
			addCheckForm(checkForms, "时钟状态", "保持", "同步/保持",
					EnbCheckConstants.NORMAL);
		}
		else if (clockStatus == 5) {
			addCheckForm(checkForms, "时钟状态", "保持超时", "同步/保持",
					EnbCheckConstants.NOT_NORMAL);
		}
		else if (clockStatus == 6) {
			addCheckForm(checkForms, "时钟状态", "异常", "同步/保持",
					EnbCheckConstants.NOT_NORMAL);
		}
		
		// 可视卫星数量
		if (visibleSatelliteNum < 3) {
			addCheckForm(checkForms, "可视卫星数量",
					String.valueOf(visibleSatelliteNum), "大于等于3",
					EnbCheckConstants.NOT_NORMAL);
		}
		else {
			addCheckForm(checkForms, "可视卫星数量",
					String.valueOf(visibleSatelliteNum), "大于等于3",
					EnbCheckConstants.NORMAL);
		}
		
		// 跟踪卫星数量
		if (trackSatelliteNum < 3) {
			addCheckForm(checkForms, "跟踪卫星数量",
					String.valueOf(trackSatelliteNum), "大于等于3",
					EnbCheckConstants.NOT_NORMAL);
		}
		else {
			addCheckForm(checkForms, "跟踪卫星数量",
					String.valueOf(trackSatelliteNum), "大于等于3",
					EnbCheckConstants.NORMAL);
		}
		// BBU温度
		addCheckForm(checkForms, "BBU温度(℃)", String.valueOf(bbuTemperature),
				"", EnbCheckConstants.NOT_CHECK);
		
		// 基站即使功率
		addCheckForm(checkForms, "基站即时功率(瓦)", String.valueOf(power), "",
				EnbCheckConstants.NOT_CHECK);
		// 风扇转速
		if (null != fanSpeeds && fanSpeeds.length > 0) {
			for (int i = 0; i < fanSpeeds.length; i++) {
				if(bbuTemperature < 30) {
					addCheckForm(checkForms, "风扇" + (i + 1) + "转速(RPM)",
							String.valueOf(fanSpeeds[i]), "当BBU单板温度大于等于30℃时,风扇转速必须大于0",
							EnbCheckConstants.NORMAL);
				} else {
					if(fanSpeeds[i] > 0) {
						addCheckForm(checkForms, "风扇" + (i + 1) + "转速(RPM)",
								String.valueOf(fanSpeeds[i]), "当BBU单板温度大于等于30℃时,风扇转速必须大于0",
								EnbCheckConstants.NORMAL);
					} else {
						addCheckForm(checkForms, "风扇" + (i + 1) + "转速(RPM)",
								String.valueOf(fanSpeeds[i]), "当BBU单板温度大于等于30℃时,风扇转速必须大于0",
								EnbCheckConstants.NOT_NORMAL);
					}
					
				}
			}
		}
		return checkForms;
	}
	
	public String getEnbRunningTime(String runningTime) {
		try {
			long time = Long.valueOf(runningTime);
			long oneSecondTime  = 1;
			long oneMinuteTime  = 60*oneSecondTime;
			long oneHourTime  = 60*oneMinuteTime;
			long onedayTime  = 24*oneHourTime;
			long days = time/onedayTime;
			time = time - days*onedayTime;
			long hours = time/oneHourTime;
			time = time - hours*oneHourTime;
			long minutes = time/oneMinuteTime;
			time = time - minutes*oneMinuteTime;
			long seconds = time/oneSecondTime;
			return days+"天"+hours+"小时"+minutes+"分钟"+seconds+"秒";
		}
		catch (Exception e) {
			e.printStackTrace();
			return runningTime;
		}
	}
	
	public String getEnbTime() {
		return enbTime;
	}
	
	public void setEnbTime(String enbTime) {
		this.enbTime = enbTime;
	}
	
	public String getRunningTime() {
		return runningTime;
	}
	
	public void setRunningTime(String runningTime) {
		this.runningTime = runningTime;
	}
	
	public int getClockType() {
		return clockType;
	}
	
	public void setClockType(int clockType) {
		this.clockType = clockType;
	}
	
	public int getClockStatus() {
		return clockStatus;
	}
	
	public void setClockStatus(int clockStatus) {
		this.clockStatus = clockStatus;
	}
	
	public int getVisibleSatelliteNum() {
		return visibleSatelliteNum;
	}
	
	public void setVisibleSatelliteNum(int visibleSatelliteNum) {
		this.visibleSatelliteNum = visibleSatelliteNum;
	}
	
	public int getTrackSatelliteNum() {
		return trackSatelliteNum;
	}
	
	public void setTrackSatelliteNum(int trackSatelliteNum) {
		this.trackSatelliteNum = trackSatelliteNum;
	}
	
	public int getBbuTemperature() {
		return bbuTemperature;
	}
	
	public void setBbuTemperature(int bbuTemperature) {
		this.bbuTemperature = bbuTemperature;
	}
	
	public int getPower() {
		return power;
	}
	
	public void setPower(int power) {
		this.power = power;
	}
	
	public int[] getFanSpeeds() {
		return fanSpeeds;
	}
	
	public void setFanSpeeds(int[] fanSpeeds) {
		this.fanSpeeds = fanSpeeds;
	}
}
