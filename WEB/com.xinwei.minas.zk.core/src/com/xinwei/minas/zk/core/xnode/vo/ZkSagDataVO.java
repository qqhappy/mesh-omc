/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-15	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.vo;

import com.xinwei.minas.zk.core.xnode.common.ZkNodeReserve;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeVO;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * SAG Data节点数据内容
 * 
 * @author chenjunhua
 * 
 */

public class ZkSagDataVO extends ZkNodeVO {

	// SAG ID
	private Long sagId;

	// SAG信令IP
	private String sagSignalIp;

	// SAG信令Port，要求使用相同端口
	private int sagSignalPort;

	// SAG媒体IP
	private String sagMediaIP;

	// SAG媒体Port
	private int sagMediaPort;

	// 基站语音直通指示, 0：非直通/1：直通
	private int btsDVoiceFlag;

	// NAT注册间隔,默认值为5
	private int T_EST;

	// NAT注册次数 ，注册次数(NAT AP层参数)，0xFFFF为反复尝试 默认值为0xFFFF（客户端使用）
	private int N_EST;

	// NAT握手间隔 ,默认值为5
	private int T_HS;

	// NAT等待握手响应时间,默认值为3
	private int T_HS_ACK;

	// NAT链路握手失连次数,默认值为3
	private int N_HS;

	// 透传包认证指示(NAT AP层参数)， 1：认证/0：不认证
	// 目前对于媒体链路，必须设为0，对于信令链路，必须设为1,网管界面暂时不显示此字段
	private int OPCODE;

	// 客户端初始加密认证序号(NAT AP层参数)，默认值为1
	private int CSEQ_CLI;

	// 服务端初始加密认证序号(NAT AP层参数)，默认值为1
	private int CSEQ_SER;

	// 是否支持广播多播业务, 0:否 , 1:是, 默认:否
	private int btsLinkMBMSFlag;

	// SAG的DPID
	private Long sagDPID;

	public Long getSagId() {
		return sagId;
	}

	public void setSagId(Long sagId) {
		this.sagId = sagId;
	}

	public String getSagSignalIp() {
		return sagSignalIp;
	}

	public void setSagSignalIp(String sagSignalIp) {
		this.sagSignalIp = sagSignalIp;
	}

	public int getSagSignalPort() {
		return sagSignalPort;
	}

	public void setSagSignalPort(int sagSignalPort) {
		this.sagSignalPort = sagSignalPort;
	}

	public String getSagMediaIP() {
		return sagMediaIP;
	}

	public void setSagMediaIP(String sagMediaIP) {
		this.sagMediaIP = sagMediaIP;
	}

	public int getSagMediaPort() {
		return sagMediaPort;
	}

	public void setSagMediaPort(int sagMediaPort) {
		this.sagMediaPort = sagMediaPort;
	}

	public int getBtsDVoiceFlag() {
		return btsDVoiceFlag;
	}

	public void setBtsDVoiceFlag(int btsDVoiceFlag) {
		this.btsDVoiceFlag = btsDVoiceFlag;
	}

	public int getT_EST() {
		return T_EST;
	}

	public void setT_EST(int t_EST) {
		T_EST = t_EST;
	}

	public int getN_EST() {
		return N_EST;
	}

	public void setN_EST(int n_EST) {
		N_EST = n_EST;
	}

	public int getT_HS() {
		return T_HS;
	}

	public void setT_HS(int t_HS) {
		T_HS = t_HS;
	}

	public int getT_HS_ACK() {
		return T_HS_ACK;
	}

	public void setT_HS_ACK(int t_HS_ACK) {
		T_HS_ACK = t_HS_ACK;
	}

	public int getN_HS() {
		return N_HS;
	}

	public void setN_HS(int n_HS) {
		N_HS = n_HS;
	}

	public int getOPCODE() {
		return OPCODE;
	}

	public void setOPCODE(int oPCODE) {
		OPCODE = oPCODE;
	}

	public int getCSEQ_CLI() {
		return CSEQ_CLI;
	}

	public void setCSEQ_CLI(int cSEQ_CLI) {
		CSEQ_CLI = cSEQ_CLI;
	}

	public int getCSEQ_SER() {
		return CSEQ_SER;
	}

	public void setCSEQ_SER(int cSEQ_SER) {
		CSEQ_SER = cSEQ_SER;
	}

	public int getBtsLinkMBMSFlag() {
		return btsLinkMBMSFlag;
	}

	public void setBtsLinkMBMSFlag(int btsLinkMBMSFlag) {
		this.btsLinkMBMSFlag = btsLinkMBMSFlag;
	}

	public Long getSagDPID() {
		return sagDPID;
	}

	public void setSagDPID(Long sagDPID) {
		this.sagDPID = sagDPID;
	}

	@Override
	public void decode(byte[] buf, int offset) {
		this.sagId = Long.valueOf(ByteUtils.toLong(buf, offset, 4));
		offset += 4;
		this.sagSignalIp = String.valueOf(ByteUtils.toIp(buf, offset, 4));
		offset += 4;
		this.sagSignalPort = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.sagMediaIP = String.valueOf(ByteUtils.toIp(buf, offset, 4));
		offset += 4;
		this.sagMediaPort = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.btsDVoiceFlag = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.T_EST = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.N_EST = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.T_HS = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.T_HS_ACK = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.N_HS = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.OPCODE = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.CSEQ_CLI = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.CSEQ_SER = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.btsLinkMBMSFlag = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.sagDPID = Long.valueOf(ByteUtils.toLong(buf, offset, 4));
		offset += 4;
		this.zkNodeReserve.decode(buf, offset);
		offset += ZkNodeReserve.LEN;
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[4096];
		int offset = 0;
		ByteUtils.putNumber(buf, offset, this.sagId + "", 4);
		offset += 4;
		ByteUtils.putIp(buf, offset, this.sagSignalIp);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.sagSignalPort + "", 4);
		offset += 4;
		ByteUtils.putIp(buf, offset, this.sagMediaIP);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.sagMediaPort + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.btsDVoiceFlag + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.T_EST + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.N_EST + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.T_HS + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.T_HS_ACK + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.N_HS + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.OPCODE + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.CSEQ_CLI + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.CSEQ_SER + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.btsLinkMBMSFlag + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.sagDPID + "", 4);
		offset += 4;
		if(this.zkNodeReserve == null)
			this.zkNodeReserve = new ZkNodeReserve();
		System.arraycopy(this.zkNodeReserve.encode(), 0, buf, offset, ZkNodeReserve.LEN);
		offset += ZkNodeReserve.LEN;
		// copy到实际字节长度的缓存
		byte[] result = new byte[offset];
		System.arraycopy(buf, 0, result, 0, result.length);
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + CSEQ_CLI;
		result = prime * result + CSEQ_SER;
		result = prime * result + N_EST;
		result = prime * result + N_HS;
		result = prime * result + OPCODE;
		result = prime * result + T_EST;
		result = prime * result + T_HS;
		result = prime * result + T_HS_ACK;
		result = prime * result + btsDVoiceFlag;
		result = prime * result + btsLinkMBMSFlag;
		result = prime * result + (int) (sagDPID ^ (sagDPID >>> 32));
		result = prime * result + (int) (sagId ^ (sagId >>> 32));
		result = prime * result
				+ ((sagMediaIP == null) ? 0 : sagMediaIP.hashCode());
		result = prime * result + sagMediaPort;
		result = prime * result
				+ ((sagSignalIp == null) ? 0 : sagSignalIp.hashCode());
		result = prime * result + sagSignalPort;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkSagDataVO))
			return false;
		ZkSagDataVO other = (ZkSagDataVO) obj;
		if (CSEQ_CLI != other.CSEQ_CLI)
			return false;
		if (CSEQ_SER != other.CSEQ_SER)
			return false;
		if (N_EST != other.N_EST)
			return false;
		if (N_HS != other.N_HS)
			return false;
		if (OPCODE != other.OPCODE)
			return false;
		if (T_EST != other.T_EST)
			return false;
		if (T_HS != other.T_HS)
			return false;
		if (T_HS_ACK != other.T_HS_ACK)
			return false;
		if (btsDVoiceFlag != other.btsDVoiceFlag)
			return false;
		if (btsLinkMBMSFlag != other.btsLinkMBMSFlag)
			return false;
		if (sagDPID != other.sagDPID)
			return false;
		if (sagId != other.sagId)
			return false;
		if (sagMediaIP == null) {
			if (other.sagMediaIP != null)
				return false;
		} else if (!sagMediaIP.equals(other.sagMediaIP))
			return false;
		if (sagMediaPort != other.sagMediaPort)
			return false;
		if (sagSignalIp == null) {
			if (other.sagSignalIp != null)
				return false;
		} else if (!sagSignalIp.equals(other.sagSignalIp))
			return false;
		if (sagSignalPort != other.sagSignalPort)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkSagDataVO [sagId=" + sagId + ", sagSignalIp=" + sagSignalIp
				+ ", sagSignalPort=" + sagSignalPort + ", sagMediaIP="
				+ sagMediaIP + ", sagMediaPort=" + sagMediaPort
				+ ", btsDVoiceFlag=" + btsDVoiceFlag + ", T_EST=" + T_EST
				+ ", N_EST=" + N_EST + ", T_HS=" + T_HS + ", T_HS_ACK="
				+ T_HS_ACK + ", N_HS=" + N_HS + ", OPCODE=" + OPCODE
				+ ", CSEQ_CLI=" + CSEQ_CLI + ", CSEQ_SER=" + CSEQ_SER
				+ ", btsLinkMBMSFlag=" + btsLinkMBMSFlag + ", sagDPID="
				+ sagDPID + "]";
	}
	
	

}
