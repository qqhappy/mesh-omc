/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-28	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

/**
 * 
 * 位置区信息模型
 * 
 * 
 * @author chenshaohua
 * 
 */

public class LocationArea implements java.io.Serializable,
		Comparable<LocationArea> {

	// 记录索引
	private Long idx;

	// 运营商
	private String operator;

	// 子网号
	private String subnet;

	// LAC
	private String lac;

	// 位置区名称
	private String locAreaName;

	public LocationArea() {
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public String getLocAreaName() {
		return locAreaName;
	}

	public void setLocAreaName(String locAreaName) {
		this.locAreaName = locAreaName;
	}

	public Long getLongLocationId() {
		String stringLocationId = "";
		if (operator.length() == 2) {
			stringLocationId += operator;
		}
		if (operator.length() == 1) {
			stringLocationId += "0" + operator;
		}
		if (subnet.length() == 3) {
			stringLocationId += subnet;
		}
		if (subnet.length() == 2) {
			stringLocationId += "0" + subnet;
		}
		if (subnet.toString().length() == 1) {
			stringLocationId += "00" + subnet;
		}
		if (lac.length() == 2) {
			stringLocationId += lac;
		}
		if (lac.length() == 1) {
			stringLocationId += "0" + lac;
		}

		return Long.parseLong(stringLocationId, 16);
	}

	public String getStringLocationId() {
		String stringLocationId = operator + "-" + subnet.toString() + "-"
				+ lac;

		return stringLocationId;
	}

	public String getAreaId() {
		return operator + subnet + lac;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getSubnet() {
		return subnet;
	}

	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}

	public String getLac() {
		return lac;
	}

	public void setLac(String lac) {
		this.lac = lac;
	}

	@Override
	public int compareTo(LocationArea other) {
		int operator_int = Integer.parseInt(this.getOperator(), 16);
		int operator_int_other = Integer.parseInt(other.getOperator(), 16);
		if (operator_int > operator_int_other)
			return 1;
		else if (operator_int < operator_int_other)
			return -1;

		int subnet_int = Integer.parseInt(this.subnet, 16);
		int subnet_int_other = Integer.parseInt(other.subnet, 16);

		if (subnet_int > subnet_int_other)
			return 1;
		else if (subnet_int < subnet_int_other)
			return -1;

		int loc_int = Integer.parseInt(this.getLac(), 16);
		int loc_int_other = Integer.parseInt(other.getLac(), 16);

		if (loc_int > loc_int_other)
			return 1;
		else if (loc_int < loc_int_other)
			return -1;

		return 0;
	}

	@Override
	public String toString() {
		return "LocationArea [idx=" + idx + ", operator=" + operator
				+ ", subnet=" + subnet + ", lac=" + lac + ", locAreaName="
				+ locAreaName + "]";
	}

	/**
	 * 获取显示字符串
	 * 
	 * @return
	 */
	public String toDiaplayString() {
		return getStringLocationId() + "(" + getLocAreaName() + ")";
	}

	/**
	 * 获取LAI字符串(前5位,不带-)
	 * 
	 * @return
	 */
	public String getLaiString() {
		return operator + subnet.toString();
	}
}
