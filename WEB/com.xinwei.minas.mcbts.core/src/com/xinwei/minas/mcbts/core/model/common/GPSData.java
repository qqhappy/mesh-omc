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
 * GPS管理消息模型
 * 
 * 
 * @author tiance
 * 
 */

public class GPSData implements Serializable, Listable {

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 纬度
	private Long latitude;

	// 经度
	private Long longitude;

	// 高度(厘米,但页面上显示为米)
	private Long height;

	// GMT便宜
	private Long gmtOffset;

	// 最小跟踪的卫星个数
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
		// 纬度
		allProperties.add(new FieldProperty(0, "listable.GPSData.latitude",
				String.valueOf(this.getLatitude())));
		// 经度
		allProperties.add(new FieldProperty(0, "listable.GPSData.longitude",
				String.valueOf(this.getLongitude())));
		// 高度
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
