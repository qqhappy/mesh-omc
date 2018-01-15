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
 * ��վ���������ֵ� McBts Type Data Dictionary
 * 
 * @author chenjunhua
 * 
 */

@SuppressWarnings("serial")
public class McBtsTypeDD implements java.io.Serializable {

	/**
	 * 0��V5��վ
	 */
	public static final int V5_MCBTS = 0;
	/**
	 * 10��R3R5��վ
	 */
	public static final int R3R5_MCBTS = 10;
	/**
	 * 20��΢���ѻ�վ
	 */
	public static final int MICRO_BEEHIVE_MCBTS = 20;
	/**
	 * 30��������Զ��վ
	 */
	public static final int FDDI_MCBTS = 30;
	/**
	 * 40�����ز���վ
	 */
	public static final int MULTI_CARRIER_MCBTS = 40;
	/**
	 * 50��XW65��618��Ŀ����վ
	 */
	public static final int XW65_MCBTS = 50;
	/**
	 * 60������RRU4ͨ����վ
	 */
	public static final int HIGH_POWER_RRU4_MCBTS = 60;
	/**
	 * 70������RRU8ͨ����վ
	 */
	public static final int HIGH_POWER_RRU8_MCBTS = 70;
	/**
	 * 80��10M��վ
	 */
	public static final int _10M_MCBTS = 80;

	// ��ǰ֧�ֵĻ�վ�����б�
	private static final int[] SUPPORTED_TYPELIST = new int[] { 0, 20, 30 };

	// ��վ����
	private int btsType;

	// ��վ��������
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
