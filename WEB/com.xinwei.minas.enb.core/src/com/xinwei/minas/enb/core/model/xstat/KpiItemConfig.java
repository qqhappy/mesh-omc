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
 * KPIͳ��������ģ��
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class KpiItemConfig implements Serializable {

	// ��������
	private String statObject;
	// ����������
	private String perfType;
	// KPIId
	private int kpiId;
	// KPI��������
	private String kpiName_zh;
	// KPIӢ������
	private String kpiName_en;
	// KPI��������
	private String kpiDesc_zh;
	// KPIӢ������
	private String kpiDesc_en;
	// ��λ
	private String unit;
	// ��������
	private String dataType;
	// ����ʱ����ܷ�ʽ
	private String timeType;
	// ���ܿռ���ܷ�ʽ
	private String spaceType;
	// ���㹫ʽ
	private String exp;
	// Ĭ��ֵ
	private double defaultValue;
	// ��ע,�Ƿ�ʵ��,0δʵ��,1��ʵ��
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
