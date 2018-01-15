/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-22	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;

/**
 * 
 * 基站链路信息
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsLink implements Serializable {

	// 索引
	private String linkIndex;
	// 基站ID
	private String btsId;
	// 基站信令点
	private String btsSignalPointID;
	// 位置区ID
	private String areaID;
	// 基站类型
	private String btsType = "0";
	// 安全参数索引
	private String safeParamIndex;
	// 基站语音直通指示
	private String btsDVoiceFlag;
	// 基站信令IP
	private String btsSignalIP;
	// 基站信令端口
	private String btsSignalPort;
	// 基站媒体IP
	private String btsMediaIP;
	// 基站媒体端口
	private String btsMediaPort;
	// SAG信令IP
	private String sagSignalIP;
	// SAG信令端口
	private String sagSignalPort;
	// SAG媒体IP
	private String sagMediaIP;
	// SAG媒体端口
	private String sagMediaPort;
	// NAT注册间隔(s)
	private String natRegisterInterval = "5";
	// 注册次数
	private String registerCount = "65525";
	// 握手间隔(s)
	private String shakeHandsInteval = "5";
	// 等待握手响应时间(s)
	private String timeWaitingShakeHands = "3";
	// NAT链路握手失连次数
	private String natShakeHandsDisconnectCount = "3";
	// 透传包认证指示
	private String ossCert = "1";
	// 客户端初始加密认证序号
	private String cliInitEncryptIndex = "1";
	// 服务端初始加密认证序号
	private String serInitEncryptIndex = "1";
	// 基站名称
	private String btsName;
	// 信令链路状态
	private String btsLinkStatus;
	// 媒体链路状态
	private String mediaLinkStatus;
	// 是否支持广播多播业务
	private String MBMSFlag = "0";
	// 地域ID
	private String districtId;
	// 频率类型
	private String freqType;
	// 频率偏移量
	private String freqOffset;
	// 前导序列号
	private String seqID;
	// 时隙总数
	private String tsNum;
	// 下行时隙总数
	private String downlinkTsNum;
	// BCH子载波组号和时隙号信息
	private String BCH;
	// RRCH的子载波组号和时隙号信息
	private String RRCH;
	// RARCH的子载波组号和时隙号信息
	private String RARCH;
	// 子载波组掩码
	private String SCGMask;
	// 子载波组时隙0掩码
	private String SCG0TSMask;
	// 子载波组时隙1掩码
	private String SCG1TSMask;
	// 子载波组时隙2掩码
	private String SCG2TSMask;
	// 子载波组时隙3掩码
	private String SCG3TSMask;
	// 子载波组时隙4掩码
	private String SCG4TSMask;

	// 增强公共信道SCG_IDX
	private String pescgIdx;
	// 增强公共信道PCH个数
	private String pepchNum;
	// 增强公共信道PCHID
	private String pepepchId;
	// 增强公共信道RARCH个数
	private String perarchNum;
	// 增强公共信道RRCH个数
	private String perrchNum;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((BCH == null) ? 0 : BCH.hashCode());
		result = prime * result
				+ ((MBMSFlag == null) ? 0 : MBMSFlag.hashCode());
		result = prime * result + ((RARCH == null) ? 0 : RARCH.hashCode());
		result = prime * result + ((RRCH == null) ? 0 : RRCH.hashCode());
		result = prime * result
				+ ((SCG0TSMask == null) ? 0 : SCG0TSMask.hashCode());
		result = prime * result
				+ ((SCG1TSMask == null) ? 0 : SCG1TSMask.hashCode());
		result = prime * result
				+ ((SCG2TSMask == null) ? 0 : SCG2TSMask.hashCode());
		result = prime * result
				+ ((SCG3TSMask == null) ? 0 : SCG3TSMask.hashCode());
		result = prime * result
				+ ((SCG4TSMask == null) ? 0 : SCG4TSMask.hashCode());
		result = prime * result + ((SCGMask == null) ? 0 : SCGMask.hashCode());
		result = prime * result + ((areaID == null) ? 0 : areaID.hashCode());
		result = prime * result
				+ ((btsDVoiceFlag == null) ? 0 : btsDVoiceFlag.hashCode());
		result = prime * result + ((btsId == null) ? 0 : btsId.hashCode());
		result = prime * result
				+ ((btsMediaIP == null) ? 0 : btsMediaIP.hashCode());
		result = prime * result
				+ ((btsMediaPort == null) ? 0 : btsMediaPort.hashCode());
		result = prime * result + ((btsName == null) ? 0 : btsName.hashCode());
		result = prime * result
				+ ((btsSignalIP == null) ? 0 : btsSignalIP.hashCode());
		result = prime * result
				+ ((btsSignalPort == null) ? 0 : btsSignalPort.hashCode());
		result = prime
				* result
				+ ((btsSignalPointID == null) ? 0 : btsSignalPointID.hashCode());
		result = prime * result
				+ ((districtId == null) ? 0 : districtId.hashCode());
		result = prime * result
				+ ((downlinkTsNum == null) ? 0 : downlinkTsNum.hashCode());
		result = prime * result
				+ ((freqOffset == null) ? 0 : freqOffset.hashCode());
		result = prime * result
				+ ((freqType == null) ? 0 : freqType.hashCode());
		result = prime * result
				+ ((safeParamIndex == null) ? 0 : safeParamIndex.hashCode());
		result = prime * result
				+ ((sagMediaIP == null) ? 0 : sagMediaIP.hashCode());
		result = prime * result
				+ ((sagMediaPort == null) ? 0 : sagMediaPort.hashCode());
		result = prime * result
				+ ((sagSignalIP == null) ? 0 : sagSignalIP.hashCode());
		result = prime * result
				+ ((sagSignalPort == null) ? 0 : sagSignalPort.hashCode());
		result = prime * result + ((seqID == null) ? 0 : seqID.hashCode());
		result = prime * result + ((tsNum == null) ? 0 : tsNum.hashCode());
		result = prime * result
				+ ((pescgIdx == null) ? 0 : pescgIdx.hashCode());
		result = prime * result
				+ ((pepchNum == null) ? 0 : pepchNum.hashCode());
		result = prime * result
				+ ((pepepchId == null) ? 0 : pepepchId.hashCode());
		result = prime * result
				+ ((perarchNum == null) ? 0 : perarchNum.hashCode());
		result = prime * result
				+ ((perrchNum == null) ? 0 : perrchNum.hashCode());

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
		McBtsLink other = (McBtsLink) obj;
		if (BCH == null) {
			if (other.BCH != null)
				return false;
		} else if (!BCH.equals(other.BCH))
			return false;
		if (MBMSFlag == null) {
			if (other.MBMSFlag != null)
				return false;
		} else if (!MBMSFlag.equals(other.MBMSFlag))
			return false;
		if (RARCH == null) {
			if (other.RARCH != null)
				return false;
		} else if (!RARCH.equals(other.RARCH))
			return false;
		if (RRCH == null) {
			if (other.RRCH != null)
				return false;
		} else if (!RRCH.equals(other.RRCH))
			return false;
		if (SCG0TSMask == null) {
			if (other.SCG0TSMask != null)
				return false;
		} else if (!SCG0TSMask.equals(other.SCG0TSMask))
			return false;
		if (SCG1TSMask == null) {
			if (other.SCG1TSMask != null)
				return false;
		} else if (!SCG1TSMask.equals(other.SCG1TSMask))
			return false;
		if (SCG2TSMask == null) {
			if (other.SCG2TSMask != null)
				return false;
		} else if (!SCG2TSMask.equals(other.SCG2TSMask))
			return false;
		if (SCG3TSMask == null) {
			if (other.SCG3TSMask != null)
				return false;
		} else if (!SCG3TSMask.equals(other.SCG3TSMask))
			return false;
		if (SCG4TSMask == null) {
			if (other.SCG4TSMask != null)
				return false;
		} else if (!SCG4TSMask.equals(other.SCG4TSMask))
			return false;
		if (SCGMask == null) {
			if (other.SCGMask != null)
				return false;
		} else if (!SCGMask.equals(other.SCGMask))
			return false;
		if (areaID == null) {
			if (other.areaID != null)
				return false;
		} else if (!areaID.equals(other.areaID))
			return false;
		if (btsDVoiceFlag == null) {
			if (other.btsId != null)
				return false;
		} else if (!btsDVoiceFlag.equals(other.btsDVoiceFlag))
			return false;
		if (btsId == null) {
			if (other.btsId != null)
				return false;
		} else if (!btsId.equals(other.btsId))
			return false;
		if (btsMediaIP == null) {
			if (other.btsMediaIP != null)
				return false;
		} else if (!btsMediaIP.equals(other.btsMediaIP))
			return false;
		if (btsMediaPort == null) {
			if (other.btsMediaPort != null)
				return false;
		} else if (!btsMediaPort.equals(other.btsMediaPort))
			return false;
		if (btsName == null) {
			if (other.btsName != null)
				return false;
		} else if (!btsName.equals(other.btsName))
			return false;
		if (btsSignalIP == null) {
			if (other.btsSignalIP != null)
				return false;
		} else if (!btsSignalIP.equals(other.btsSignalIP))
			return false;
		if (btsSignalPort == null) {
			if (other.btsSignalPort != null)
				return false;
		} else if (!btsSignalPort.equals(other.btsSignalPort))
			return false;
		if (btsSignalPointID == null) {
			if (other.btsSignalPointID != null)
				return false;
		} else if (!btsSignalPointID.equals(other.btsSignalPointID))
			return false;
		if (districtId == null) {
			if (other.districtId != null)
				return false;
		} else if (!districtId.equals(other.districtId))
			return false;
		if (downlinkTsNum == null) {
			if (other.downlinkTsNum != null)
				return false;
		} else if (!downlinkTsNum.equals(other.downlinkTsNum))
			return false;
		if (freqOffset == null) {
			if (other.freqOffset != null)
				return false;
		} else if (!freqOffset.equals(other.freqOffset))
			return false;
		if (freqType == null) {
			if (other.freqType != null)
				return false;
		} else if (!freqType.equals(other.freqType))
			return false;
		if (safeParamIndex == null) {
			if (other.safeParamIndex != null)
				return false;
		} else if (!safeParamIndex.equals(other.safeParamIndex))
			return false;
		if (sagMediaIP == null) {
			if (other.sagMediaIP != null)
				return false;
		} else if (!sagMediaIP.equals(other.sagMediaIP))
			return false;
		if (sagMediaPort == null) {
			if (other.sagMediaPort != null)
				return false;
		} else if (!sagMediaPort.equals(other.sagMediaPort))
			return false;
		if (sagSignalIP == null) {
			if (other.sagSignalIP != null)
				return false;
		} else if (!sagSignalIP.equals(other.sagSignalIP))
			return false;
		if (sagSignalPort == null) {
			if (other.sagSignalPort != null)
				return false;
		} else if (!sagSignalPort.equals(other.sagSignalPort))
			return false;
		if (seqID == null) {
			if (other.seqID != null)
				return false;
		} else if (!seqID.equals(other.seqID))
			return false;
		if (tsNum == null) {
			if (other.tsNum != null)
				return false;
		} else if (!tsNum.equals(other.tsNum))
			return false;
		if (pescgIdx == null) {
			if (other.pescgIdx != null)
				return false;
		} else if (!pescgIdx.equals(other.pescgIdx))
			return false;
		if (pepchNum == null) {
			if (other.pepchNum != null)
				return false;
		} else if (!pepchNum.equals(other.pepchNum))
			return false;
		if (pepepchId == null) {
			if (other.pepepchId != null)
				return false;
		} else if (!pepepchId.equals(other.pepepchId))
			return false;
		if (perarchNum == null) {
			if (other.perarchNum != null)
				return false;
		} else if (!perarchNum.equals(other.perarchNum))
			return false;
		if (perrchNum == null) {
			if (other.perrchNum != null)
				return false;
		} else if (!perrchNum.equals(other.perrchNum))
			return false;

		return true;
	}

	public String getLinkIndex() {
		return linkIndex;
	}

	public void setLinkIndex(String linkIndex) {
		this.linkIndex = linkIndex;
	}

	public String getBtsId() {
		return btsId;
	}

	public void setBtsId(String btsId) {
		this.btsId = btsId;
	}

	public String getBtsSignalPointID() {
		return btsSignalPointID;
	}

	public void setBtsSignalPointID(String btsSignalPointID) {
		this.btsSignalPointID = btsSignalPointID;
	}

	public String getAreaID() {
		return areaID;
	}

	public void setAreaID(String areaID) {
		this.areaID = areaID;
	}

	public String getBtsType() {
		return btsType;
	}

	public void setBtsType(String btsType) {
		this.btsType = btsType;
	}

	public String getSafeParamIndex() {
		return safeParamIndex;
	}

	public void setSafeParamIndex(String safeParamIndex) {
		this.safeParamIndex = safeParamIndex;
	}

	public String getBtsDVoiceFlag() {
		return btsDVoiceFlag;
	}

	public void setBtsDVoiceFlag(String btsDVoiceFlag) {
		this.btsDVoiceFlag = btsDVoiceFlag;
	}

	public String getBtsSignalIP() {
		return btsSignalIP;
	}

	public void setBtsSignalIP(String btsSignalIP) {
		this.btsSignalIP = btsSignalIP;
	}

	public String getBtsSignalPort() {
		return btsSignalPort;
	}

	public void setBtsSignalPort(String btsSignalPort) {
		this.btsSignalPort = btsSignalPort;
	}

	public String getBtsMediaIP() {
		return btsMediaIP;
	}

	public void setBtsMediaIP(String btsMediaIP) {
		this.btsMediaIP = btsMediaIP;
	}

	public String getBtsMediaPort() {
		return btsMediaPort;
	}

	public void setBtsMediaPort(String btsMediaPort) {
		this.btsMediaPort = btsMediaPort;
	}

	public String getSagSignalIP() {
		return sagSignalIP;
	}

	public void setSagSignalIP(String sagSignalIP) {
		this.sagSignalIP = sagSignalIP;
	}

	public String getSagSignalPort() {
		return sagSignalPort;
	}

	public void setSagSignalPort(String sagSignalPort) {
		this.sagSignalPort = sagSignalPort;
	}

	public String getSagMediaIP() {
		return sagMediaIP;
	}

	public void setSagMediaIP(String sagMediaIP) {
		this.sagMediaIP = sagMediaIP;
	}

	public String getSagMediaPort() {
		return sagMediaPort;
	}

	public void setSagMediaPort(String sagMediaPort) {
		this.sagMediaPort = sagMediaPort;
	}

	public String getNatRegisterInterval() {
		return natRegisterInterval;
	}

	public void setNatRegisterInterval(String natRegisterInterval) {
		this.natRegisterInterval = natRegisterInterval;
	}

	public String getRegisterCount() {
		return registerCount;
	}

	public void setRegisterCount(String registerCount) {
		this.registerCount = registerCount;
	}

	public String getShakeHandsInteval() {
		return shakeHandsInteval;
	}

	public void setShakeHandsInteval(String shakeHandsInteval) {
		this.shakeHandsInteval = shakeHandsInteval;
	}

	public String getTimeWaitingShakeHands() {
		return timeWaitingShakeHands;
	}

	public void setTimeWaitingShakeHands(String timeWaitingShakeHands) {
		this.timeWaitingShakeHands = timeWaitingShakeHands;
	}

	public String getNatShakeHandsDisconnectCount() {
		return natShakeHandsDisconnectCount;
	}

	public void setNatShakeHandsDisconnectCount(
			String natShakeHandsDisconnectCount) {
		this.natShakeHandsDisconnectCount = natShakeHandsDisconnectCount;
	}

	public String getOssCert() {
		return ossCert;
	}

	public void setOssCert(String ossCert) {
		this.ossCert = ossCert;
	}

	public String getCliInitEncryptIndex() {
		return cliInitEncryptIndex;
	}

	public void setCliInitEncryptIndex(String cliInitEncryptIndex) {
		this.cliInitEncryptIndex = cliInitEncryptIndex;
	}

	public String getSerInitEncryptIndex() {
		return serInitEncryptIndex;
	}

	public void setSerInitEncryptIndex(String serInitEncryptIndex) {
		this.serInitEncryptIndex = serInitEncryptIndex;
	}

	public String getBtsName() {
		return btsName;
	}

	public void setBtsName(String btsName) {
		this.btsName = btsName;
	}

	public String getBtsLinkStatus() {
		return btsLinkStatus;
	}

	public void setBtsLinkStatus(String btsLinkStatus) {
		this.btsLinkStatus = btsLinkStatus;
	}

	public String getMediaLinkStatus() {
		return mediaLinkStatus;
	}

	public void setMediaLinkStatus(String mediaLinkStatus) {
		this.mediaLinkStatus = mediaLinkStatus;
	}

	public String getMBMSFlag() {
		return MBMSFlag;
	}

	public void setMBMSFlag(String mBMSFlag) {
		MBMSFlag = mBMSFlag;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getFreqType() {
		return freqType;
	}

	public void setFreqType(String freqType) {
		this.freqType = freqType;
	}

	public String getFreqOffset() {
		return freqOffset;
	}

	public void setFreqOffset(String freqOffset) {
		this.freqOffset = freqOffset;
	}

	public String getSeqID() {
		return seqID;
	}

	public void setSeqID(String seqID) {
		this.seqID = seqID;
	}

	public String getTsNum() {
		return tsNum;
	}

	public void setTsNum(String tsNum) {
		this.tsNum = tsNum;
	}

	public String getDownlinkTsNum() {
		return downlinkTsNum;
	}

	public void setDownlinkTsNum(String downlinkTsNum) {
		this.downlinkTsNum = downlinkTsNum;
	}

	public String getBCH() {
		return BCH;
	}

	public void setBCH(String bCH) {
		BCH = bCH;
	}

	public String getRRCH() {
		return RRCH;
	}

	public void setRRCH(String rRCH) {
		RRCH = rRCH;
	}

	public String getRARCH() {
		return RARCH;
	}

	public void setRARCH(String rARCH) {
		RARCH = rARCH;
	}

	public String getSCGMask() {
		return SCGMask;
	}

	public void setSCGMask(String sCGMask) {
		SCGMask = sCGMask;
	}

	public String getSCG0TSMask() {
		return SCG0TSMask;
	}

	public void setSCG0TSMask(String sCG0TSMask) {
		SCG0TSMask = sCG0TSMask;
	}

	public String getSCG1TSMask() {
		return SCG1TSMask;
	}

	public void setSCG1TSMask(String sCG1TSMask) {
		SCG1TSMask = sCG1TSMask;
	}

	public String getSCG2TSMask() {
		return SCG2TSMask;
	}

	public void setSCG2TSMask(String sCG2TSMask) {
		SCG2TSMask = sCG2TSMask;
	}

	public String getSCG3TSMask() {
		return SCG3TSMask;
	}

	public void setSCG3TSMask(String sCG3TSMask) {
		SCG3TSMask = sCG3TSMask;
	}

	public String getSCG4TSMask() {
		return SCG4TSMask;
	}

	public void setSCG4TSMask(String sCG4TSMask) {
		SCG4TSMask = sCG4TSMask;
	}

	public String getPescgIdx() {
		return pescgIdx;
	}

	public void setPescgIdx(String pescgIdx) {
		this.pescgIdx = pescgIdx;
	}

	public String getPepchNum() {
		return pepchNum;
	}

	public void setPepchNum(String pepchNum) {
		this.pepchNum = pepchNum;
	}

	public String getPepepchId() {
		return pepepchId;
	}

	public void setPepepchId(String pepepchId) {
		this.pepepchId = pepepchId;
	}

	public String getPerarchNum() {
		return perarchNum;
	}

	public void setPerarchNum(String perarchNum) {
		this.perarchNum = perarchNum;
	}

	public String getPerrchNum() {
		return perrchNum;
	}

	public void setPerrchNum(String perrchNum) {
		this.perrchNum = perrchNum;
	}

}
