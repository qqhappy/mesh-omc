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
 * �ն�ע����Ϣ��
 * 
 * @author tiance
 * 
 */

public class McBtsUTRegisterNotify {

	// �ն�ID 4
	private Integer eid;

	// ��վ��Ϣ
	private UTBaseInfo uTBaseInfo = new UTBaseInfo();

	// UT Profile IE���ն���Ϣ��
	private UTProfileIE uTProfileIE = new UTProfileIE();

	// ������Сά�ִ��� 2
	private Integer ulMinMainteinBW;

	// ������Сά�ִ��� 2
	private Integer dlMinMaintein;

	public McBtsUTRegisterNotify() {

	}

	public McBtsUTRegisterNotify(byte[] buf) {
		super();
		if (buf == null || buf.length == 0)
			return;
		int offset = 0;
		// �ն�ID 4
		eid = ByteUtils.toInt(buf, offset, 4);
		offset += 4;
		// ��վ��Ϣ
		// ��������ID 2
		uTBaseInfo.setHomeNetworkId(ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// Ӳ������ 1
		uTBaseInfo.setHardwareType(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// ������� 1
		uTBaseInfo.setSoftwareType(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// ��ǰ����汾��4 Active SW version
		uTBaseInfo.setActiveSWVersion(ByteUtils.toVersion(buf, offset, 4));
		offset += 4;
		// ��������汾��4 Standby SW version
		uTBaseInfo.setStandbySWVersion(ByteUtils.toVersion(buf, offset, 4));
		offset += 4;
		// Ӳ���汾�� 16
		uTBaseInfo.setHardwareVersion(ByteUtils.toVersion(buf, offset, 16));
		offset += 16;
		// UT Profile IE���ն���Ϣ��
		// ����״̬ 1
		uTProfileIE.setAdminStatus(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// ������־״̬ 1
		uTProfileIE.setPerfLogStatus(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// ���������ռ���� 2
		uTProfileIE.setPerfDataCollectionInterval(ByteUtils.toInt(buf, offset,
				2));
		offset += 2;
		// �ƶ��� 1
		uTProfileIE.setMobility(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// DHCP�������� 1
		uTProfileIE.setAllowDHCPRenewInServingBTS(ByteUtils.toInt(buf, offset,
				1));
		offset += 1;
		// ��� 2
		uTProfileIE.setGroupID(ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// �����ֶ� 1
		uTProfileIE.setReserve1(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// ���IP��ַ��Ŀ 1
		uTProfileIE.setMaxIPAddressNumber(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// �����˿����� 4
		uTProfileIE.setVoicePortMask(ByteUtils.toIp(buf, offset, 4));
		offset += 4;
		// �ն�ҵ������������Ϣ-��ʼ
		// ���� 1
		uTProfileIE.getuTServiceDescConfigIE().setKlass(
				ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// Bit0����ά�ִ����ʶ 1
		uTProfileIE.getuTServiceDescConfigIE().setBandFlag(
				ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// ���������� 2
		uTProfileIE.getuTServiceDescConfigIE().setUploadMaxBandwidth(
				ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// ������С���� 2
		uTProfileIE.getuTServiceDescConfigIE().setUploadMinBandwidth(
				ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// ���������� 2
		uTProfileIE.getuTServiceDescConfigIE().setDownloadMaxBandwidth(
				ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// ������С���� 2
		uTProfileIE.getuTServiceDescConfigIE().setDownloadMinBandwidth(
				ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// �ն�ҵ������������Ϣ-����

		// �����ֶ� 1
		uTProfileIE.setReserve2(ByteUtils.toInt(buf, offset, 1));
		offset += 1;
		// �̶���IP��ַ��Ŀ 1
		uTProfileIE.setFixedIPNumber(ByteUtils.toInt(buf, offset, 1));
		offset += 1;

		for (int i = 0; i < uTProfileIE.getFixedIPNumber(); i++) {
			FixedIPInfo fixedIPInfo = uTProfileIE.new FixedIPInfo();
			// MAC��ַ 6
			// TODO: mac��ֱַ��ת��IPv4���ܲ���ȷ
			fixedIPInfo.setMac(ByteUtils.toIp(buf, offset, 6));
			offset += 6;
			// �̶�IP 4
			fixedIPInfo.setFixIp(ByteUtils.toIp(buf, offset, 4));
			offset += 4;
			// ê��վIP��ַ 4
			fixedIPInfo.setAnchorBTSIp(ByteUtils.toIp(buf, offset, 4));
			offset += 4;
			// ·��ID 4
			fixedIPInfo.setRouterAreaID(ByteUtils.toIp(buf, offset, 4));
			offset += 4;
			uTProfileIE.getFixedIPList().add(fixedIPInfo);
		}

		if (uTProfileIE.getuTServiceDescConfigIE().getBandFlag() == 1) {
			// ������Сά�ִ��� 2
			ulMinMainteinBW = ByteUtils.toInt(buf, offset, 2);
			offset += 2;
			// ������Сά�ִ��� 2
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
