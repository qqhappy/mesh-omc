/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model;

/**
 * 
 * 基站类型数据字典 McBts Type Data Dictionary
 * 
 * @author chenjunhua
 * 
 */

@SuppressWarnings("serial")
public class McBtsTypeDD implements java.io.Serializable {

	/**
	 * 0：V5基站
	 */
	public static final int V5_MCBTS = 0;
	/**
	 * 10：R3R5基站
	 */
	public static final int R3R5_MCBTS = 10;
	/**
	 * 20：微蜂窝基站
	 */
	public static final int MICRO_BEEHIVE_MCBTS = 20;
	/**
	 * 30：光纤拉远基站
	 */
	public static final int FDDI_MCBTS = 30;
	/**
	 * 40：多载波基站
	 */
	public static final int MULTI_CARRIER_MCBTS = 40;
	/**
	 * 50：XW65（618项目）基站
	 */
	public static final int XW65_MCBTS = 50;
	/**
	 * 60：大功率RRU4通道基站
	 */
	public static final int HIGH_POWER_RRU4_MCBTS = 60;
	/**
	 * 70：大功率RRU8通道基站
	 */
	public static final int HIGH_POWER_RRU8_MCBTS = 70;
	/**
	 * 80：10M基站
	 */
	public static final int _10M_MCBTS = 80;

	// 当前支持的基站类型列表
	private static final int[] SUPPORTED_TYPELIST = new int[] { 0, 20, 30 };

	// 基站类型
	private int btsType;

	// 基站类型名称
	private String btsTypeName;

	public int getBtsType() {
		return btsType;
	}

	public void setBtsType(int btsType) {
		this.btsType = btsType;
	}

	public String getBtsTypeName() {
		return btsTypeName;
	}

	public void setBtsTypeName(String btsTypeName) {
		this.btsTypeName = btsTypeName;
	}

	public static boolean isSupported(int btsType) {
		for (int type : SUPPORTED_TYPELIST) {
			if (btsType == type) {
				return true;
			}
		}
		return false;
	}

	public static int[] getSupportedTypeList() {
		return SUPPORTED_TYPELIST;
	}

}
