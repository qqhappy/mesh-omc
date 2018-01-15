/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-28	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.message;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 消息：UT Profile IE（终端信息）
 * 
 * @author fanhaoyu
 * 
 */

public class UTProfileIE {

	// 管理状态 1
	private Integer adminStatus;

	// 性能日志状态 1
	private Integer perfLogStatus;

	// 性能数据收集间隔 2
	private Integer perfDataCollectionInterval;

	// 移动性 1
	private Integer mobility;

	// DHCP更新设置 1
	private Integer allowDHCPRenewInServingBTS;

	// 组号 2
	private Integer groupID;

	// 保留字段 1
	private Integer reserve1;

	// 最大IP地址数目 1
	private Integer maxIPAddressNumber;

	// 语音端口掩码 4
	private String voicePortMask;

	// 终端业务属性配置信息
	private UTServiceDescriptorConfigurationIE uTServiceDescConfigIE
		= new UTServiceDescriptorConfigurationIE();

	// 保留字段 1
	private Integer reserve2;

	// 固定的IP地址数目 1
	private Integer fixedIPNumber;

	private List<FixedIPInfo> fixedIPList = new ArrayList<FixedIPInfo>();

	public Integer getAdminStatus() {
		return adminStatus;
	}

	public void setAdminStatus(Integer adminStatus) {
		this.adminStatus = adminStatus;
	}

	public Integer getPerfLogStatus() {
		return perfLogStatus;
	}

	public void setPerfLogStatus(Integer perfLogStatus) {
		this.perfLogStatus = perfLogStatus;
	}

	public Integer getPerfDataCollectionInterval() {
		return perfDataCollectionInterval;
	}

	public void setPerfDataCollectionInterval(Integer perfDataCollectionInterval) {
		this.perfDataCollectionInterval = perfDataCollectionInterval;
	}

	public Integer getMobility() {
		return mobility;
	}

	public void setMobility(Integer mobility) {
		this.mobility = mobility;
	}

	public Integer getAllowDHCPRenewInServingBTS() {
		return allowDHCPRenewInServingBTS;
	}

	public void setAllowDHCPRenewInServingBTS(Integer allowDHCPRenewInServingBTS) {
		this.allowDHCPRenewInServingBTS = allowDHCPRenewInServingBTS;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public Integer getReserve1() {
		return reserve1;
	}

	public void setReserve1(Integer reserve1) {
		this.reserve1 = reserve1;
	}

	public Integer getMaxIPAddressNumber() {
		return maxIPAddressNumber;
	}

	public void setMaxIPAddressNumber(Integer maxIPAddressNumber) {
		this.maxIPAddressNumber = maxIPAddressNumber;
	}

	public String getVoicePortMask() {
		return voicePortMask;
	}

	public void setVoicePortMask(String voicePortMask) {
		this.voicePortMask = voicePortMask;
	}

	public UTServiceDescriptorConfigurationIE getuTServiceDescConfigIE() {
		return uTServiceDescConfigIE;
	}

	public void setuTServiceDescConfigIE(
			UTServiceDescriptorConfigurationIE uTServiceDescConfigIE) {
		this.uTServiceDescConfigIE = uTServiceDescConfigIE;
	}

	public Integer getReserve2() {
		return reserve2;
	}

	public void setReserve2(Integer reserve2) {
		this.reserve2 = reserve2;
	}

	public Integer getFixedIPNumber() {
		return fixedIPNumber;
	}

	public void setFixedIPNumber(Integer fixedIPNumber) {
		this.fixedIPNumber = fixedIPNumber;
	}

	public void setFixedIPList(java.util.List<FixedIPInfo> fixedIPList) {
		this.fixedIPList = fixedIPList;
	}

	public java.util.List<FixedIPInfo> getFixedIPList() {
		return fixedIPList;
	}

	class FixedIPInfo {

		// MAC地址 6
		private String mac;

		// 固定IP 4
		private String fixIp;

		// 锚基站IP地址 4
		private String anchorBTSIp;

		// 路由ID 4
		private String routerAreaID;

		public String getMac() {
			return mac;
		}

		public void setMac(String mac) {
			this.mac = mac;
		}

		public String getFixIp() {
			return fixIp;
		}

		public void setFixIp(String fixIp) {
			this.fixIp = fixIp;
		}

		public String getAnchorBTSIp() {
			return anchorBTSIp;
		}

		public void setAnchorBTSIp(String anchorBTSIp) {
			this.anchorBTSIp = anchorBTSIp;
		}

		public String getRouterAreaID() {
			return routerAreaID;
		}

		public void setRouterAreaID(String routerAreaID) {
			this.routerAreaID = routerAreaID;
		}
	}
}