package com.xinwei.minas.server.micro.microapi;

/**
 * 
 * 网管输入参数约束检查
 * 
 * @author zhoujie
 * 
 */
public class MicroDataInputInfo {
	int u8RruTypeId;//RRU类型
	String au8eNBName;//eNB标识,取值范围为ASCII码
	int u16TAC;//跟踪区码,0-65535
	int au8MCC[] = new int[3];//移动国家码,0-9,0xFF
	int au8MNC[] = new int[3];//移动网络码,0-9,0xFF	
	String au8CellLable;//取值范围为ASCII码
	int u8FreqBandInd;//频段指示,33-40,61-63,53,58    
	//中心频点,(33:1900-1920, 34:2010-2025, 35:1850-1910, 36:1930-1990, 37:1910-1930, 38:2570-2620, 39:1880-1920, //40:2300-2400MHZ，61:1447-1467，62:1785-1805，63:606-678,53:778-798,58:380-430
	int u32CenterFreq;
	int u8SysBandWidth;//系统带宽,enum (1.4M(6RB)，3M(15RB)，5M(25RB)，10M(50RB)，15M(75RB)，20M(100RB))
	int u16PhyCellId;//物理小区标识,0-503
	int u8TopoNO;//小区拓扑号
	//int u32Status;//小区状态,(0:Normal, 1:Fault)
	int u8ManualOP;//管理状态,0:Unblocked 1:Blocked
	int u16RootSeqIndex;//逻辑根序列索引,0-837

	public int geteRruType() {
		return u8RruTypeId;
	}	
	
	public void setRruType(int u8RruTypeId) {
		this.u8RruTypeId = u8RruTypeId;
	}
	
	public String geteNBName() {
		return au8eNBName;
	}
	
	public void seteNBName(String au8eNBName) {
		this.au8eNBName = au8eNBName;
	}
	
	public int getTAC() {
		return u16TAC;
	}

	public void setTAC(int u16TAC) {
		this.u16TAC = u16TAC;
	}

	public int[] getMCC() {
		return au8MCC;
	}

	public void setMCC(int[] au8mcc) {
		au8MCC = au8mcc;
	}
	
	public int[] getMNC() {
		return au8MNC;
	}

	public void setMNC(int[] au8mnc) {
		au8MNC = au8mnc;
	}

	public String getCellLable() {
		return au8CellLable;
	}

	public void setCellLable(String au8CellLable) {
		this.au8CellLable = au8CellLable;
	}
	
	public int getFreqBandInd() {
		return u8FreqBandInd;
	}

	public void setFreqBandInd(int u8FreqBandInd) {
		this.u8FreqBandInd = u8FreqBandInd;
	}
		
	public int getCenterFreq() {
		return u32CenterFreq;
	}

	public void setCenterFreq(int u32CenterFreq) {
		this.u32CenterFreq = u32CenterFreq;
	}
	
	public int getSysBandWidth() {
		return u8SysBandWidth;
	}

	public void setSysBandWidth(int u8SysBandWidth) {
		this.u8SysBandWidth = u8SysBandWidth;
	}

	public int getPhyCellId() {
		return u16PhyCellId;
	}

	public void setPhyCellId(int u16PhyCellId) {
		this.u16PhyCellId = u16PhyCellId;
	}
	
	public int getTopoNO() {
		return u8TopoNO;
	}

	public void setTopoNO(int u8TopoNO) {
		this.u8TopoNO = u8TopoNO;
	}
	
//	public int getStatus() {
//		return u32Status;
//	}
//
//	public void setStatus(int u32Status) {
//		this.u32Status = u32Status;
//	}
	
	public int getManualOP() {
		return u8ManualOP;
	}

	public void setManualOP(int u8ManualOP) {
		this.u8ManualOP = u8ManualOP;
	}	
	public int getRootSeqIndex() {
		return u16RootSeqIndex;
	}

	public void setRootSeqIndex(int u16RootSeqIndex) {
		this.u16RootSeqIndex = u16RootSeqIndex;
	}	
}
