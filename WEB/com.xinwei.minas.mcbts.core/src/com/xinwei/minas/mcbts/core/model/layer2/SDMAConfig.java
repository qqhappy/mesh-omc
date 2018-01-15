package com.xinwei.minas.mcbts.core.model.layer2;

import java.io.Serializable;

/**
 * SDMA配置信息类
 * 
 * @author fangping
 * 
 */
public class SDMAConfig implements Serializable {
	// 记录索引
	private Long idx;
	// MO编号（全局唯一,系统自动生成）
	private long moId;
	// 是否启用SDMA
	private Integer sdmaEnableFlag;
	// 语音业务是否启用SDMA
	private Integer voiceSdmaFlag;
	// 视频业务是否启用SDMA
	private Integer videoSdmaFlag;
	// 启用SDMA子信道类型
	private Integer sdmaSchType;
	
	// 进入SDMA的无线资源占用门限
	private Integer inRadioResIndTh;
	// 退出SDMA的无线资源占用门限
	private Integer outRadioResIndTh;
	// 单用户最大无线匹配用户数
	private Integer wlMatchUserMaxNum;
	private Integer rsv=0;

	public Integer getRsv() {
		return rsv;
	}

	public void setRsv(Integer rsv) {
		this.rsv = rsv;
	}

	public SDMAConfig() {
	}

	public SDMAConfig(Long idx) {
		this.idx = idx;
	}

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

	public Integer getSdmaEnableFlag() {
		return sdmaEnableFlag;
	}

	public void setSdmaEnableFlag(Integer sdmaEnableFlag) {
		this.sdmaEnableFlag = sdmaEnableFlag;
	}

	public Integer getSdmaSchType() {
		return sdmaSchType;
	}

	public void setSdmaSchType(Integer sdmaSchType) {
		this.sdmaSchType = sdmaSchType;
	}

	public Integer getWlMatchUserMaxNum() {
		return wlMatchUserMaxNum;
	}

	public void setWlMatchUserMaxNum(Integer wlMatchUserMaxNum) {
		this.wlMatchUserMaxNum = wlMatchUserMaxNum;
	}

	public Integer getInRadioResIndTh() {
		return inRadioResIndTh;
	}

	public void setInRadioResIndTh(Integer inRadioResIndTh) {
		this.inRadioResIndTh = inRadioResIndTh;
	}

	public Integer getOutRadioResIndTh() {
		return outRadioResIndTh;
	}

	public void setOutRadioResIndTh(Integer outRadioResIndTh) {
		this.outRadioResIndTh = outRadioResIndTh;
	}

	public Integer getVoiceSdmaFlag() {
		return voiceSdmaFlag;
	}

	public void setVoiceSdmaFlag(Integer voiceSdmaFlag) {
		this.voiceSdmaFlag = voiceSdmaFlag;
	}

	public Integer getVideoSdmaFlag() {
		return videoSdmaFlag;
	}

	public void setVideoSdmaFlag(Integer videoSdmaFlag) {
		this.videoSdmaFlag = videoSdmaFlag;
	}

}
