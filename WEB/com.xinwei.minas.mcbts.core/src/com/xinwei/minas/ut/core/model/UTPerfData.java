/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-18	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.model;

import java.io.Serializable;

/**
 * 
 * 终端性能数据模型
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class UTPerfData implements Serializable{
	
	public static final int TIME_SLOT = 8;
	
	//pid, 4
	private long pid;
	
	//2
	private int press;
	
	//data status
	private int dataStatus;
	
	//voice status
	private int voiceStatus;
	
	//UplinkBW, 4
	private long uplinkBW;
	
	//DownlinkBW, 4 
	private long DownlinkBW;
	
	private UTChannelPpc[] uTChannelPpcs;
	
	private SNRCI[] sNRCIs;

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public int getDataStatus() {
		return dataStatus;
	}
	
	public void setPress(int press) {
		this.press = press;
	}
	
	public int getPress() {
		return press;
	}

	public void setDataStatus(int dataStatus) {
		this.dataStatus = dataStatus;
	}

	public int getVoiceStatus() {
		return voiceStatus;
	}

	public void setVoiceStatus(int voiceStatus) {
		this.voiceStatus = voiceStatus;
	}

	public long getUplinkBW() {
		return uplinkBW;
	}

	public void setUplinkBW(long uplinkBW) {
		this.uplinkBW = uplinkBW;
	}

	public long getDownlinkBW() {
		return DownlinkBW;
	}

	public void setDownlinkBW(long downlinkBW) {
		DownlinkBW = downlinkBW;
	}

	public UTChannelPpc[] getuTChannelPpcs() {
		return uTChannelPpcs;
	}

	public void setuTChannelPpcs(UTChannelPpc[] uTChannelPpcs) {
		this.uTChannelPpcs = uTChannelPpcs;
	}

	public SNRCI[] getsNRCIs() {
		return sNRCIs;
	}

	public void setsNRCIs(SNRCI[] sNRCIs) {
		this.sNRCIs = sNRCIs;
	}
}
