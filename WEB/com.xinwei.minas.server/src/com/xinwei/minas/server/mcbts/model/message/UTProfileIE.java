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
 * ��Ϣ��UT Profile IE���ն���Ϣ��
 * 
 * @author fanhaoyu
 * 
 */

public class UTProfileIE {

	// ����״̬ 1
	private Integer adminStatus;

	// ������־״̬ 1
	private Integer perfLogStatus;

	// ���������ռ���� 2
	private Integer perfDataCollectionInterval;

	// �ƶ��� 1
	private Integer mobility;

	// DHCP�������� 1
	private Integer allowDHCPRenewInServingBTS;

	// ��� 2
	private Integer groupID;

	// �����ֶ� 1
	private Integer reserve1;

	// ���IP��ַ��Ŀ 1
	private Integer maxIPAddressNumber;

	// �����˿����� 4
	private String voicePortMask;

	// �ն�ҵ������������Ϣ
	private UTServiceDescriptorConfigurationIE uTServiceDescConfigIE
		= new UTServiceDescriptorConfigurationIE();

	// �����ֶ� 1
	private Integer reserve2;

	// �̶���IP��ַ��Ŀ 1
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

		// MAC��ַ 6
		private String mac;

		// �̶�IP 4
		private String fixIp;

		// ê��վIP��ַ 4
		private String anchorBTSIp;

		// ·��ID 4
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