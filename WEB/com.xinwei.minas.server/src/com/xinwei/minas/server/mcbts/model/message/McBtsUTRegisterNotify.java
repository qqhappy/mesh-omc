/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-27	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.message;

import com.xinwei.minas.server.mcbts.model.message.UTProfileIE.FixedIPInfo;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * 终端注册消息体
 * 
 * @author tiance
 * 
 */

public class McBtsUTRegisterNotify {

	// 终端ID 4
	private Integer eid;

	// 基站信息
	private UTBaseInfo uTBaseInfo = new UTBaseInfo();

	// UT Profile IE（终端信息）
	private UTProfileIE uTProfileIE = new UTProfileIE();

	// 上行最小维持带宽 2
	private Integer ulMinMainteinBW;

	// 下行最小维持带宽 2
	private Integer dlMinMaintein;

	public McBtsUTRegisterNotify() {

	}

	public McBtsUTRegisterNotify(byte[] buf) {
		super();
		if (buf == null || buf.length == 0)
			return;
		int offset = 0;
		// 终端ID 4
		eid = ByteUtils.toInt(buf, offset, 4);
		offset += 4;
		// 基站信息
		// 归属网络ID 2
		uTBaseInfo.setHomeNetworkId(ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// 硬件类型 1
		uTBaseInfo.setHardwareType(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// 软件类型 1
		uTBaseInfo.setSoftwareType(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// 当前软件版本号4 Active SW version
		uTBaseInfo.setActiveSWVersion(ByteUtils.toVersion(buf, offset, 4));
		offset += 4;
		// 备用软件版本号4 Standby SW version
		uTBaseInfo.setStandbySWVersion(ByteUtils.toVersion(buf, offset, 4));
		offset += 4;
		// 硬件版本号 16
		uTBaseInfo.setHardwareVersion(ByteUtils.toVersion(buf, offset, 16));
		offset += 16;
		// UT Profile IE（终端信息）
		// 管理状态 1
		uTProfileIE.setAdminStatus(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// 性能日志状态 1
		uTProfileIE.setPerfLogStatus(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// 性能数据收集间隔 2
		uTProfileIE.setPerfDataCollectionInterval(ByteUtils.toInt(buf, offset,
				2));
		offset += 2;
		// 移动性 1
		uTProfileIE.setMobility(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// DHCP更新设置 1
		uTProfileIE.setAllowDHCPRenewInServingBTS(ByteUtils.toInt(buf, offset,
				1));
		offset += 1;
		// 组号 2
		uTProfileIE.setGroupID(ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// 保留字段 1
		uTProfileIE.setReserve1(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// 最大IP地址数目 1
		uTProfileIE.setMaxIPAddressNumber(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// 语音端口掩码 4
		uTProfileIE.setVoicePortMask(ByteUtils.toIp(buf, offset, 4));
		offset += 4;
		// 终端业务属性配置信息-开始
		// 类型 1
		uTProfileIE.getuTServiceDescConfigIE().setKlass(
				ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// Bit0用于维持带宽标识 1
		uTProfileIE.getuTServiceDescConfigIE().setBandFlag(
				ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// 上行最大带宽 2
		uTProfileIE.getuTServiceDescConfigIE().setUploadMaxBandwidth(
				ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// 上行最小带宽 2
		uTProfileIE.getuTServiceDescConfigIE().setUploadMinBandwidth(
				ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// 下行最大带宽 2
		uTProfileIE.getuTServiceDescConfigIE().setDownloadMaxBandwidth(
				ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// 下行最小带宽 2
		uTProfileIE.getuTServiceDescConfigIE().setDownloadMinBandwidth(
				ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// 终端业务属性配置信息-结束

		// 保留字段 1
		uTProfileIE.setReserve2(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// 固定的IP地址数目 1
		uTProfileIE.setFixedIPNumber(ByteUtils.toInt(buf, offset, 1));
		offset += 1;

		for (int i = 0; i < uTProfileIE.getFixedIPNumber(); i++) {
			FixedIPInfo fixedIPInfo = uTProfileIE.new FixedIPInfo();
			// MAC地址 6
			// TODO: mac地址直接转成IPv4可能不正确
			fixedIPInfo.setMac(ByteUtils.toIp(buf, offset, 6));
			offset += 6;
			// 固定IP 4
			fixedIPInfo.setFixIp(ByteUtils.toIp(buf, offset, 4));
			offset += 4;
			// 锚基站IP地址 4
			fixedIPInfo.setAnchorBTSIp(ByteUtils.toIp(buf, offset, 4));
			offset += 4;
			// 路由ID 4
			fixedIPInfo.setRouterAreaID(ByteUtils.toIp(buf, offset, 4));
			offset += 4;
			uTProfileIE.getFixedIPList().add(fixedIPInfo);
		}

		if (uTProfileIE.getuTServiceDescConfigIE().getBandFlag() == 1) {
			// 上行最小维持带宽 2
			ulMinMainteinBW = ByteUtils.toInt(buf, offset, 2);
			offset += 2;
			// 下行最小维持带宽 2
			dlMinMaintein = ByteUtils.toInt(buf, offset, 2);
			offset += 2;
		}
	}

	public Integer getEid() {
		return eid;
	}

	public void setEid(Integer eid) {
		this.eid = eid;
	}

	public UTBaseInfo getuTBaseInfo() {
		return uTBaseInfo;
	}

	public void setuTBaseInfo(UTBaseInfo uTBaseInfo) {
		this.uTBaseInfo = uTBaseInfo;
	}

	public UTProfileIE getuTProfileIE() {
		return uTProfileIE;
	}

	public void setuTProfileIE(UTProfileIE uTProfileIE) {
		this.uTProfileIE = uTProfileIE;
	}

	public Integer getUlMinMainteinBW() {
		return ulMinMainteinBW;
	}

	public void setUlMinMainteinBW(Integer ulMinMainteinBW) {
		this.ulMinMainteinBW = ulMinMainteinBW;
	}

	public Integer getDlMinMaintein() {
		return dlMinMaintein;
	}

	public void setDlMinMaintein(Integer dlMinMaintein) {
		this.dlMinMaintein = dlMinMaintein;
	}

}
