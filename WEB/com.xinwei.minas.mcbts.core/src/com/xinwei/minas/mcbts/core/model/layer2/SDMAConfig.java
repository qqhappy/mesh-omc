package com.xinwei.minas.mcbts.core.model.layer2;

import java.io.Serializable;

/**
 * SDMA������Ϣ��
 * 
 * @author fangping
 * 
 */
public class SDMAConfig implements Serializable {
	// ��¼����
	private Long idx;
	// MO��ţ�ȫ��Ψһ,ϵͳ�Զ����ɣ�
	private long moId;
	// �Ƿ�����SDMA
	private Integer sdmaEnableFlag;
	// ����ҵ���Ƿ�����SDMA
	private Integer voiceSdmaFlag;
	// ��Ƶҵ���Ƿ�����SDMA
	private Integer videoSdmaFlag;
	// ����SDMA���ŵ�����
	private Integer sdmaSchType;
	
	// ����SDMA��������Դռ������
	private Integer inRadioResIndTh;
	// �˳�SDMA��������Դռ������
	private Integer outRadioResIndTh;
	// ���û��������ƥ���û���
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
