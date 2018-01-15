/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-7	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.core.model;

import java.io.Serializable;

/**
 * 
 * 小区开站配置
 * 
 * @author chenlong
 * 
 */

public class EnbCellStart implements Serializable {
	
	private static final long serialVersionUID = 5776472477108961582L;
	
	public static final int ID_SCENE = 1;
	
	public static final int ID_FREQ_BAND = 2;
	
	public static final int ID_SYS_BAND_WIDTH = 3;
	
	public static final int ID_SF_CFG = 4;
	
	public static final int ID_RRU_TYPE = 5;
	
	public static final int ID_AN_NUM = 6;
	
	public static final String FIELD_SCENE = "scene";
	
	public static final String FIELD_AN_NUM = "anNum";
	
	public static final String FIELD_RRU_TYPE = "rruType";
	
	public static final String FIELD_SYS_BANDWIDTH = "u8SysBandWidth";
	
	public static final String FIELD_ULDL_SLOT_ALLOC = "u8UlDlSlotAlloc";
	
	
	public EnbCellStart() {
		super();
	}
	
	// 小区标识
	private int cid;
	
	// 场景编号
	private int sceneId;
	
	// 频段编号
	private int freqBandId;
	
	// 带宽编号
	private int bandwidthId;
	
	// 子帧配比
	private int sfCfgId;
	
	// 天线数
	private int anNumId;
	
	// RRU类型编号
	private int rruTypeId;
	
	// 跟踪区码
	private int tac;
	
	// 小区名称
	private String cellName;
	
	// 中心频点
	private int centerFreq;
	
	// 物理小区标识
	private int phyCellId;
	
	// 逻辑根序列
	private int rootSeqIndex;
	
	// RRU单板
	private int topoNO;
	
	// 管理状态
	private int manualOP;
	
	// 移动国家码
	private String mcc;
	
	// 移动网络码
	private String mnc;
	
	public String getMcc() {
		return mcc;
	}
	
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	
	public String getMnc() {
		return mnc;
	}
	
	public void setMnc(String mnc) {
		this.mnc = mnc;
	}
	
	public int getCid() {
		return cid;
	}
	
	public void setCid(int cid) {
		this.cid = cid;
	}
	
	public int getSceneId() {
		return sceneId;
	}
	
	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}
	
	public int getFreqBandId() {
		return freqBandId;
	}
	
	public void setFreqBandId(int freqBandId) {
		this.freqBandId = freqBandId;
	}
	
	public int getBandwidthId() {
		return bandwidthId;
	}
	
	public void setBandwidthId(int bandwidthId) {
		this.bandwidthId = bandwidthId;
	}
	
	public int getSfCfgId() {
		return sfCfgId;
	}
	
	public void setSfCfgId(int sfCfgId) {
		this.sfCfgId = sfCfgId;
	}
	
	public int getAnNumId() {
		return anNumId;
	}
	
	public void setAnNumId(int anNumId) {
		this.anNumId = anNumId;
	}
	
	public int getRruTypeId() {
		return rruTypeId;
	}
	
	public void setRruTypeId(int rruTypeId) {
		this.rruTypeId = rruTypeId;
	}
	
	public int getTac() {
		return tac;
	}
	
	public void setTac(int tac) {
		this.tac = tac;
	}
	
	public String getCellName() {
		return cellName;
	}
	
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	
	public int getCenterFreq() {
		return centerFreq;
	}
	
	public void setCenterFreq(int centerFreq) {
		this.centerFreq = centerFreq;
	}
	
	public int getPhyCellId() {
		return phyCellId;
	}
	
	public void setPhyCellId(int phyCellId) {
		this.phyCellId = phyCellId;
	}
	
	public int getRootSeqIndex() {
		return rootSeqIndex;
	}
	
	public void setRootSeqIndex(int rootSeqIndex) {
		this.rootSeqIndex = rootSeqIndex;
	}
	
	public int getTopoNO() {
		return topoNO;
	}
	
	public void setTopoNO(int topoNO) {
		this.topoNO = topoNO;
	}
	
	public int getManualOP() {
		return manualOP;
	}
	
	public void setManualOP(int manualOP) {
		this.manualOP = manualOP;
	}
	
	/**
	 * 将String类型的移动国家码和移动网络码转换成int数组
	 * 
	 * @param str
	 * @return
	 */
	public int[] getIntArray(String str) {
		int[] result = new int[3];
		String str1 = str.substring(0, 2);
		String str2 = str.substring(2, 4);
		String str3 = str.substring(4, 6);
		result[0] = Integer.parseInt(str1, 16);
		result[1] = Integer.parseInt(str2, 16);
		result[2] = Integer.parseInt(str3, 16);
		return result;
	}
	
	@Override
	public String toString() {
		return "EnbCellStart [cid=" + cid + ", sceneId=" + sceneId
				+ ", freqBandId=" + freqBandId + ", bandwidthId=" + bandwidthId
				+ ", sfCfgId=" + sfCfgId + ", anNumId=" + anNumId
				+ ", rruTypeId=" + rruTypeId + ", tac=" + tac + ", cellName="
				+ cellName + ", centerFreq=" + centerFreq + ", phyCellId="
				+ phyCellId + ", rootSeqIndex=" + rootSeqIndex + ", topoNO="
				+ topoNO + ", manualOP=" + manualOP + ", mcc=" + mcc + ", mnc="
				+ mnc + "]";
	}
	
}
