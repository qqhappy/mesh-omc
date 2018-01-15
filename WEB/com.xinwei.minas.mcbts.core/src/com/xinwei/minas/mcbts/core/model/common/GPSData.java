/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-21	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xinwei.omp.core.model.biz.FieldProperty;
import com.xinwei.omp.core.model.biz.Listable;

/**
 * 
 * GPS������Ϣģ��
 * 
 * 
 * @author tiance
 * 
 */

public class GPSData implements Serializable, Listable {

	// ��¼����
	private Long idx;

	// MO��ţ�ȫ��Ψһ,ϵͳ�Զ����ɣ�
	private long moId;

	// γ��
	private Long latitude;

	// ����
	private Long longitude;

	// �߶�(����,��ҳ������ʾΪ��)
	private Long height;

	// GMT����
	private Long gmtOffset;

	// ��С���ٵ����Ǹ���
	private Integer minimumTrackingsatellite;

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public Long getLatitude() {
		return latitude;
	}

	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}

	public Long getLongitude() {
		return longitude;
	}

	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}

	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public Long getGmtOffset() {
		return gmtOffset;
	}

	public void setGmtOffset(Long gmtOffset) {
		this.gmtOffset = gmtOffset;
	}

	public Integer getMinimumTrackingsatellite() {
		return minimumTrackingsatellite;
	}

	public void setMinimumTrackingsatellite(Integer minimumTrackingsatellite) {
		this.minimumTrackingsatellite = minimumTrackingsatellite;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((gmtOffset == null) ? 0 : gmtOffset.hashCode());
		result = prime * result + ((height == null) ? 0 : height.hashCode());
		result = prime * result
				+ ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result
				+ ((longitude == null) ? 0 : longitude.hashCode());
		result = prime
				* result
				+ ((minimumTrackingsatellite == null) ? 0
						: minimumTrackingsatellite.hashCode());
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
		GPSData other = (GPSData) obj;
		if (gmtOffset == null) {
			if (other.gmtOffset != null)
				return false;
		} else if (!gmtOffset.equals(other.gmtOffset))
			return false;
		if (height == null) {
			if (other.height != null)
				return false;
		} else if (!height.equals(other.height))
			return false;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		if (minimumTrackingsatellite == null) {
			if (other.minimumTrackingsatellite != null)
				return false;
		} else if (!minimumTrackingsatellite
				.equals(other.minimumTrackingsatellite))
			return false;
		return true;
	}

	@Override
	public List<FieldProperty> listAll() {
		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		// γ��
		allProperties.add(new FieldProperty(0, "listable.GPSData.latitude",
				String.valueOf(this.getLatitude())));
		// ����
		allProperties.add(new FieldProperty(0, "listable.GPSData.longitude",
				String.valueOf(this.getLongitude())));
		// �߶�
		allProperties.add(new FieldProperty(0, "listable.GPSData.height",
				String.valueOf(this.getHeight())));

		allProperties.add(new FieldProperty(0, "listable.GPSData.gmtOffset",
				String.valueOf(this.getGmtOffset())));

		allProperties.add(new FieldProperty(0,
				"listable.GPSData.minimumTrackingsatellite", String
						.valueOf(this.getMinimumTrackingsatellite())));

		return allProperties;
	}

	@Override
	public String getBizName() {
		return "mcbts_gpsData";
	}

}
