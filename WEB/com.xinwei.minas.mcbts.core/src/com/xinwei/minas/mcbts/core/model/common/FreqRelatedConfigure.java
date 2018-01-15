package com.xinwei.minas.mcbts.core.model.common;

import java.io.Serializable;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer1.RFConfig;
import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;

public class FreqRelatedConfigure implements Serializable{

	// 同频组网配置验证相关的参数
	private Long moId;
	private McBts bts = null;
	private List<McBts> neighborList = null;
	private List<McBts> appendNeighborList = null;
	private AirlinkConfig airlink = null;
	private RFConfig rfconf = null;
	
	//是否允许进行强制配置
	private boolean permitForceConfig = false;
	
	private boolean warningFlag = false;

	public boolean isPermitForceConfig() {
		return permitForceConfig;
	}


	public void setPermitForceConfig(boolean permitForceConfig) {
		this.permitForceConfig = permitForceConfig;
	}


	public FreqRelatedConfigure(Long moId) {
		this.moId = moId;
	}


	/**
	 * @return the moId
	 */
	public Long getMoId() {
		return moId;
	}

	public void setBts(McBts bts) {
		this.bts = bts;
	}


	/**
	 * @return the bts
	 */
	public McBts getBts() {
		return bts;
	}

	/**
	 * @return the neighborList
	 */
	public List<McBts> getNeighborList() {
		return neighborList;
	}

	/**
	 * @return the airlink
	 */
	public AirlinkConfig getAirlink() {
		return airlink;
	}

	/**
	 * @return the rfconf
	 */
	public RFConfig getRfconf() {
		return rfconf;
	}

	/**
	 * @param neighborList
	 *            the neighborList to set
	 */
	public void setNeighborList(List<McBts> neighborList) {
		this.neighborList = neighborList;
	}

	/**
	 * @param airlink
	 *            the airlink to set
	 */
	public void setAirlink(AirlinkConfig airlink) {
		this.airlink = airlink;
	}

	/**
	 * @param rfconf
	 *            the rfconf to set
	 */
	public void setRfconf(RFConfig rfconf) {
		this.rfconf = rfconf;
	}

	public void setAppendNeighborList(List<McBts> appendNeighborList) {
		this.appendNeighborList = appendNeighborList;
	}

	public List<McBts> getAppendNeighborList() {
		return appendNeighborList;
	}


	public void setWarningFlag(boolean warningFlag) {
		this.warningFlag = warningFlag;
	}


	public boolean isWarningFlag() {
		return warningFlag;
	}
}
