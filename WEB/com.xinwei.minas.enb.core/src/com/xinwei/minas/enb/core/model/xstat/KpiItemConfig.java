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

import com.xinwei.minas.enb.core.utils.EnbStatConstants;

/**
 * 
 * KPI统计项配置模型
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class KpiItemConfig implements Serializable {

	// 测量对象
	private String statObject;
	// 性能子类型
	private String perfType;
	// KPIId
	private int kpiId;
	// KPI中文名称
	private String kpiName_zh;
	// KPI英文名称
	private String kpiName_en;
	// KPI中文描述
	private String kpiDesc_zh;
	// KPI英文描述
	private String kpiDesc_en;
	// 单位
	private String unit;
	// 数据类型
	private String dataType;
	// 网管时间汇总方式
	private String timeType;
	// 网管空间汇总方式
	private String spaceType;
	// 计算公式
	private String exp;
	// 默认值
	private double defaultValue;
	// 备注,是否实现,0未实现,1已实现
	private int remark;

	public boolean isBtsStatObject() {
		return this.statObject.equals(EnbStatConstants.STAT_OBJECT_ENB);
	}

	public boolean isCellStatObject() {
		return this.statObject.equals(EnbStatConstants.STAT_OBJECT_CELL);
	}

	public String getStatObject() {
		return statObject;
	}

	public void setStatObject(String statObject) {
		this.statObject = statObject;
	}

	public String getPerfType() {
		return perfType;
	}

	public void setPerfType(String perfType) {
		this.perfType = perfType;
	}

	public int getKpiId() {
		return kpiId;
	}

	public void setKpiId(int kpiId) {
		this.kpiId = kpiId;
	}

	public String getKpiName_zh() {
		return kpiName_zh;
	}

	public void setKpiName_zh(String kpiName_zh) {
		this.kpiName_zh = kpiName_zh;
	}

	public String getKpiName_en() {
		return kpiName_en;
	}

	public void setKpiName_en(String kpiName_en) {
		this.kpiName_en = kpiName_en;
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

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public double getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(double defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setKpiDesc_zh(String kpiDesc_zh) {
		this.kpiDesc_zh = kpiDesc_zh;
	}

	public String getKpiDesc_zh() {
		return kpiDesc_zh;
	}

	public void setKpiDesc_en(String kpiDesc_en) {
		this.kpiDesc_en = kpiDesc_en;
	}

	public String getKpiDesc_en() {
		return kpiDesc_en;
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
		result = prime * result + kpiId;
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
		KpiItemConfig other = (KpiItemConfig) obj;
		if (kpiId != other.kpiId)
			return false;
		return true;
	}

}
