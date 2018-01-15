/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model.xstat;

import java.io.Serializable;

/**
 * 
 * Counter统计项配置模型
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class CounterItemConfig implements Serializable {

	// 测量对象
	private String statObject;
	// 上报子系统
	private String reportSystem;
	// 测量子类型
	private String measureType;
	// 测量事件
	private String measureEvent;
	// CounterId
	private int counterId;
	// Counter中文名称
	private String counterName_zh;
	// Counter英文名称
	private String counterName_en;
	// Counter中文描述
	private String counterDesc_zh;
	// Counter英文描述
	private String counterDesc_en;
	// 单位
	private String unit;
	// 数据类型
	private String dataType;
	// 网管时间汇总方式
	private String timeType;
	// 网管空间汇总方式
	private String spaceType;
	// 特殊Counter的计算公式
	private String exp;
	// 备注,是否实现,0未实现,1已实现
	private int remark;

	public String getStatObject() {
		return statObject;
	}

	public void setStatObject(String statObject) {
		this.statObject = statObject;
	}

	public String getReportSystem() {
		return reportSystem;
	}

	public void setReportSystem(String reportSystem) {
		this.reportSystem = reportSystem;
	}

	public String getMeasureType() {
		return measureType;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	/**
	 * @param measureEvent
	 *            the measureEvent to set
	 */
	public void setMeasureEvent(String measureEvent) {
		this.measureEvent = measureEvent;
	}

	/**
	 * @return the measureEvent
	 */
	public String getMeasureEvent() {
		return measureEvent;
	}

	public int getCounterId() {
		return counterId;
	}

	public void setCounterId(int counterId) {
		this.counterId = counterId;
	}

	public String getCounterName_zh() {
		return counterName_zh;
	}

	public void setCounterName_zh(String counterName_zh) {
		this.counterName_zh = counterName_zh;
	}

	public String getCounterName_en() {
		return counterName_en;
	}

	public void setCounterName_en(String counterName_en) {
		this.counterName_en = counterName_en;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public String getSpaceType() {
		return spaceType;
	}

	public void setSpaceType(String spaceType) {
		this.spaceType = spaceType;
	}

	public void setCounterDesc_zh(String counterDesc_zh) {
		this.counterDesc_zh = counterDesc_zh;
	}

	public String getCounterDesc_zh() {
		return counterDesc_zh;
	}

	public void setCounterDesc_en(String counterDesc_en) {
		this.counterDesc_en = counterDesc_en;
	}

	public String getCounterDesc_en() {
		return counterDesc_en;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getExp() {
		return exp;
	}

	public void setRemark(int remark) {
		this.remark = remark;
	}

	public int getRemark() {
		return remark;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + counterId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CounterItemConfig other = (CounterItemConfig) obj;
		if (counterId != other.counterId)
			return false;
		return true;
	}

}
