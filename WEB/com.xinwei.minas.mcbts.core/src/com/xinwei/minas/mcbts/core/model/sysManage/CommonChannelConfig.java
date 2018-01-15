/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-8	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer2.SCGChannelConfigItem;

/**
 * 
 * 公共信道配置信息
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class CommonChannelConfig implements Serializable {

	public static final int CLUSTER_SWITCH_CLOSE = 0;

	public static final int CLUSTER_SWITCH_OPEN = 1;

	public static final int MBMS_SWITCH_CLOSE = 0;

	public static final int MBMS_SWITCH_OPEN = 0;

	public static final int REMOTE_SWITCH_CLOSE = 0;

	public static final int REMOTE_SWITCH_OPEN = 0;

	private Long btsId;

	private Double longitude;

	private Double latitude;

	private Double freq;

	private Integer seqId;

	private List<SCGChannelConfigItem> bchList;

	private List<SCGChannelConfigItem> rarchList;

	private List<SCGChannelConfigItem> rrchList;

	private List<SCGChannelConfigItem> bchn1List;

	private List<SCGChannelConfigItem> rarchn1List;

	private List<SCGChannelConfigItem> rrchn1List;

	private List<Long> neighbourList;

	private List<Long> appendNeighbourList;

	private Integer downlinkTSNum;

	private Integer totalTSNum;

	private Integer scgMask;

	private Integer clusterSwitch;

	private Integer mbmsSwitch;

	private Integer remoteSwitch;

	private Integer compatibilityMode;

	private Integer ecchScgIndex;

	private Integer pchSetNum;

	private Integer pchIndex;

	private Integer erarchNum;

	private Integer errchNum;

	public Long getBtsId() {
		return btsId;
	}

	public void setBtsId(Long btsId) {
		this.btsId = btsId;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getFreq() {
		return freq;
	}

	public void setFreq(Double freq) {
		this.freq = freq;
	}

	public Integer getSeqId() {
		return seqId;
	}

	public void setSeqId(Integer seqId) {
		this.seqId = seqId;
	}

	public List<SCGChannelConfigItem> getBchList() {
		return bchList;
	}

	public void setBchList(List<SCGChannelConfigItem> bchList) {
		this.bchList = bchList;
	}

	public List<SCGChannelConfigItem> getRarchList() {
		return rarchList;
	}

	public void setRarchList(List<SCGChannelConfigItem> rarchList) {
		this.rarchList = rarchList;
	}

	public List<SCGChannelConfigItem> getRrchList() {
		return rrchList;
	}

	public void setRrchList(List<SCGChannelConfigItem> rrchList) {
		this.rrchList = rrchList;
	}

	public void setBchn1List(List<SCGChannelConfigItem> bchn1List) {
		this.bchn1List = bchn1List;
	}

	public List<SCGChannelConfigItem> getBchn1List() {
		return bchn1List;
	}

	public void setRarchn1List(List<SCGChannelConfigItem> rarchn1List) {
		this.rarchn1List = rarchn1List;
	}

	public List<SCGChannelConfigItem> getRarchn1List() {
		return rarchn1List;
	}

	public void setRrchn1List(List<SCGChannelConfigItem> rrchn1List) {
		this.rrchn1List = rrchn1List;
	}

	public List<SCGChannelConfigItem> getRrchn1List() {
		return rrchn1List;
	}

	public List<Long> getNeighbourList() {
		return neighbourList;
	}

	public void setNeighbourList(List<Long> neighbourList) {
		this.neighbourList = neighbourList;
	}

	public Integer getScgMask() {
		return scgMask;
	}

	public void setScgMask(Integer scgMask) {
		this.scgMask = scgMask;
	}

	public Integer getClusterSwitch() {
		return clusterSwitch;
	}

	public void setClusterSwitch(Integer clusterSwitch) {
		this.clusterSwitch = clusterSwitch;
	}

	public Integer getMbmsSwitch() {
		return mbmsSwitch;
	}

	public void setMbmsSwitch(Integer mbmsSwitch) {
		this.mbmsSwitch = mbmsSwitch;
	}

	public Integer getRemoteSwitch() {
		return remoteSwitch;
	}

	public void setRemoteSwitch(Integer remoteSwitch) {
		this.remoteSwitch = remoteSwitch;
	}

	public Integer getCompatibilityMode() {
		return compatibilityMode;
	}

	public void setCompatibilityMode(Integer compatibilityMode) {
		this.compatibilityMode = compatibilityMode;
	}

	public Integer getEcchScgIndex() {
		return ecchScgIndex;
	}

	public void setEcchScgIndex(Integer ecchScgIndex) {
		this.ecchScgIndex = ecchScgIndex;
	}

	public Integer getPchSetNum() {
		return pchSetNum;
	}

	public void setPchSetNum(Integer pchSetNum) {
		this.pchSetNum = pchSetNum;
	}

	public Integer getPchIndex() {
		return pchIndex;
	}

	public void setPchIndex(Integer pchIndex) {
		this.pchIndex = pchIndex;
	}

	public Integer getErarchNum() {
		return erarchNum;
	}

	public void setErarchNum(Integer erarchNum) {
		this.erarchNum = erarchNum;
	}

	public Integer getErrchNum() {
		return errchNum;
	}

	public void setErrchNum(Integer errchNum) {
		this.errchNum = errchNum;
	}

	public void setAppendNeighbourList(List<Long> appendNeighbourList) {
		this.appendNeighbourList = appendNeighbourList;
	}

	public List<Long> getAppendNeighbourList() {
		return appendNeighbourList;
	}

	public Integer getDownlinkTSNum() {
		return downlinkTSNum;
	}

	public void setDownlinkTSNum(Integer downlinkTSNum) {
		this.downlinkTSNum = downlinkTSNum;
	}

	public Integer getTotalTSNum() {
		return totalTSNum;
	}

	public void setTotalTSNum(Integer totalTSNum) {
		this.totalTSNum = totalTSNum;
	}

}
