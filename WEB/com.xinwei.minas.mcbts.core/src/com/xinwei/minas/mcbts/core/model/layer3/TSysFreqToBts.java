/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-21	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer3;

import java.util.List;

/**
 * 系统频点下发实体包装类，使用genericBizData
 * 
 * @author liuzhongyan
 * 
 */

public class TSysFreqToBts {
	public static final int FREQ_ALL = 0;
	public static final int VALID_FREQ_PRIOR = 1;
	public static final int VALID_FREQ_ONLY = 2;

	// 频点开关
	private int freqSwitch;
	// 有效频点列表
	private List<TSysFreqModule> sysMoList;

	public TSysFreqToBts() {
		super();
	}

	public TSysFreqToBts(List<TSysFreqModule> sysMoList) {
		super();
		this.sysMoList = sysMoList;
	}

	public int getFreqSwitch() {
		return freqSwitch;
	}

	public void setFreqSwitch(int freqSwitch) {
		this.freqSwitch = freqSwitch;
	}

	public List<TSysFreqModule> getSysMoList() {
		return sysMoList;
	}

	public void setSysMoList(List<TSysFreqModule> sysMoList) {
		this.sysMoList = sysMoList;
	}

}
