/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model.stat;

import java.io.Serializable;

import com.xinwei.minas.enb.core.utils.EnbStatConstants;

/**
 * 
 * 实时性能监控统计项配置
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbRealtimeItemConfig implements Serializable {

	// 测量对象
	private String statObject;
	// 上报子系统
	private String reportSystem;
	// 测量子类型
	private String measureType;
	// 测量事件
	private String measureEvent;

	private int tagId;
	// 统计项ID
	private int itemId;

	private String name_zh;

	private String name_en;

	private String desc_zh;

	private String desc_en;
	// 显示值与原值换算关系
	private String exp;
	// 单位
	private String unit;
	// 数据类型
	private String dataType;
	// 网管时间汇总方式
	private String timeType;
	// 网管空间汇总方式
	private String spaceType;

	public String getStatObject() {
		return statObject;
	}

	public void setStatObject(String statObject) {
		this.statObject = statObject;
	}

	public boolean isBtsStatObject() {
		return this.statObject.equals(EnbStatConstants.STAT_OBJECT_ENB);
	}

	public boolean isCellStatObject() {
		return this.statObject.equals(EnbStatConstants.STAT_OBJECT_CELL);
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

	public String getMeasureEvent() {
		return measureEvent;
	}

	public void setMeasureEvent(String measureEvent) {
		this.measureEvent = measureEvent;
	}

	/**
	 * @param tagId
	 *            the tagId to set
	 */
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	/**
	 * @return the tagId
	 */
	public int getTagId() {
		return tagId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getName_zh() {
		return name_zh;
	}

	public void setName_zh(String name_zh) {
		this.name_zh = name_zh;
	}

	public String getName_en() {
		return name_en;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	public String getDesc_zh() {
		return desc_zh;
	}

	public void setDesc_zh(String desc_zh) {
		this.desc_zh = desc_zh;
	}

	public String getDesc_en() {
		return desc_en;
	}

	public void setDesc_en(String desc_en) {
		this.desc_en = desc_en;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getExp() {
		return exp;
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

}
