/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-15	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.vo;

import java.util.Arrays;

import com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeReserve;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeVO;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * BTS�ڵ�����
 * 
 * @author chenjunhua
 * 
 */

public class ZkBtsVO extends ZkNodeVO {
	
	public static final int VOICDFLG_NODIR = 0;
	public static final int VOICDFLG_DIR = 1;

	// ��վID, 1-65534
	private Long btsId;

	// �û�վ������btsGroup��Id
	private Long homeBtsGroupId;

	// ��վ����IP
	private String btsSignalIP;

	// ��վ����Port
	private int btsSignalPort;

	// ��վý��IP
	private String btsMediaIP;

	// ��վý��Port
	private int btsMediaPort;

	// ��վ����ֱָͨʾ 0����ֱͨ 1��ֱͨ
	private int btsDVoiceFlag;

	// NATע����,Ĭ��ֵΪ5
	private int T_EST;

	// NATע�����
	private int N_EST;

	// NAT���ּ�� ,Ĭ��ֵΪ5
	private int T_HS;

	// NAT�ȴ�������Ӧʱ��,Ĭ��ֵΪ3
	private int T_HS_ACK;

	// NAT��·����ʧ������,Ĭ��ֵΪ3
	private int N_HS;

	// ͸������ָ֤ʾ(NAT AP�����)�� 1����֤/0������֤
	// Ŀǰ����ý����·��������Ϊ0������������·��������Ϊ1,���ܽ�����ʱ����ʾ���ֶ�
	private int OPCODE;

	// �ͻ��˳�ʼ������֤���(NAT AP�����)��Ĭ��ֵΪ1
	private int CSEQ_CLI;

	// ����˳�ʼ������֤���(NAT AP�����)��Ĭ��ֵΪ1
	private int CSEQ_SER;

	// �Ƿ�֧�ֹ㲥�ಥҵ�� 0���� 1���� Ĭ�ϣ���
	private int btsLinkMBMSFlag;

	// ��վ�����
	private Long btsDpId;

	// λ������ʶ����Ч����Ϊ10λ
	private byte[] LAI = new byte[16];

	// ��ȫ��������
	private int SPI;

	public Long getBtsId() {
		return btsId;
	}

	public void setBtsId(Long btsId) {
		this.btsId = btsId;
	}

	public Long getHomeBtsGroupId() {
		return homeBtsGroupId;
	}

	public void setHomeBtsGroupId(Long homeBtsGroupId) {
		this.homeBtsGroupId = homeBtsGroupId;
	}

	public String getBtsSignalIP() {
		return btsSignalIP;
	}

	public void setBtsSignalIP(String btsSignalIP) {
		this.btsSignalIP = btsSignalIP;
	}

	public int getBtsSignalPort() {
		return btsSignalPort;
	}

	public void setBtsSignalPort(int btsSignalPort) {
		this.btsSignalPort = btsSignalPort;
	}

	public String getBtsMediaIP() {
		return btsMediaIP;
	}

	public void setBtsMediaIP(String btsMediaIP) {
		this.btsMediaIP = btsMediaIP;
	}

	public int getBtsMediaPort() {
		return btsMediaPort;
	}

	public void setBtsMediaPort(int btsMediaPort) {
		this.btsMediaPort = btsMediaPort;
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

	public Long getBtsDpId() {
		return btsDpId;
	}

	public void setBtsDpId(Long btsDpId) {
		this.btsDpId = btsDpId;
	}

	public byte[] getLAI() {
		return LAI;
	}

	public void setLAI(byte[] lAI) {
		LAI = lAI;
	}

	public int getSPI() {
		return SPI;
	}

	public void setSPI(int sPI) {
		SPI = sPI;
	}

	@Override
	public void decode(byte[] buf, int offset) {
		this.btsId = Long.valueOf(ByteUtils.toLong(buf, offset, 4));
		offset += 4;
		this.btsSignalIP = String.valueOf(ByteUtils.toIp(buf, offset, 4));
		offset += 4;
		this.btsSignalPort = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.btsMediaIP = String.valueOf(ByteUtils.toIp(buf, offset, 4));
		offset += 4;
		this.btsMediaPort = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
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
		this.btsDpId = Long.valueOf(ByteUtils.toLong(buf, offset, 4));
		offset += 4;
		System.arraycopy(buf, offset, this.LAI, 0, 16);
		offset += 16;
		this.SPI = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.zkNodeReserve.decode(buf, offset);
		offset += ZkNodeReserve.LEN;
		
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[4096];
		int offset = 0;
		ByteUtils.putNumber(buf, offset, this.btsId + "", 4);
		offset += 4;
		ByteUtils.putIp(buf, offset, this.btsSignalIP);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.btsSignalPort + "", 4);
		offset += 4;
		ByteUtils.putIp(buf, offset, this.btsMediaIP);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.btsMediaPort + "", 4);
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
		ByteUtils.putNumber(buf, offset, this.btsDpId + "", 4);
		offset += 4;
		System.arraycopy(this.LAI, 0, buf, offset, 16);
		offset += 16;
		ByteUtils.putNumber(buf, offset, this.SPI + "", 4);
		offset += 4;
		if(this.zkNodeReserve == null)
			this.zkNodeReserve = new ZkNodeReserve();
		System.arraycopy(this.zkNodeReserve.encode(), 0, buf, offset, ZkNodeReserve.LEN);
		offset += ZkNodeReserve.LEN;
		// copy��ʵ���ֽڳ��ȵĻ���
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
		result = prime * result + Arrays.hashCode(LAI);
		result = prime * result + N_EST;
		result = prime * result + N_HS;
		result = prime * result + OPCODE;
		result = prime * result + SPI;
		result = prime * result + T_EST;
		result = prime * result + T_HS;
		result = prime * result + T_HS_ACK;
		result = prime * result + btsDVoiceFlag;
		result = prime * result + ((btsDpId == null) ? 0 : btsDpId.hashCode());
		result = prime * result + (int) (btsId ^ (btsId >>> 32));
		result = prime * result + btsLinkMBMSFlag;
		result = prime * result
				+ ((btsMediaIP == null) ? 0 : btsMediaIP.hashCode());
		result = prime * result + btsMediaPort;
		result = prime * result
				+ ((btsSignalIP == null) ? 0 : btsSignalIP.hashCode());
		result = prime * result + btsSignalPort;
		result = prime * result
				+ (int) (homeBtsGroupId ^ (homeBtsGroupId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkBtsVO))
			return false;
		ZkBtsVO other = (ZkBtsVO) obj;
		if (CSEQ_CLI != other.CSEQ_CLI)
			return false;
		if (CSEQ_SER != other.CSEQ_SER)
			return false;
		if (!Arrays.equals(LAI, other.LAI))
			return false;
		if (N_EST != other.N_EST)
			return false;
		if (N_HS != other.N_HS)
			return false;
		if (OPCODE != other.OPCODE)
			return false;
		if (SPI != other.SPI)
			return false;
		if (T_EST != other.T_EST)
			return false;
		if (T_HS != other.T_HS)
			return false;
		if (T_HS_ACK != other.T_HS_ACK)
			return false;
		if (btsDVoiceFlag != other.btsDVoiceFlag)
			return false;
		if (btsDpId == null) {
			if (other.btsDpId != null)
				return false;
		} else if (!btsDpId.equals(other.btsDpId))
			return false;
		if (btsId != other.btsId)
			return false;
		if (btsLinkMBMSFlag != other.btsLinkMBMSFlag)
			return false;
		if (btsMediaIP == null) {
			if (other.btsMediaIP != null)
				return false;
		} else if (!btsMediaIP.equals(other.btsMediaIP))
			return false;
		if (btsMediaPort != other.btsMediaPort)
			return false;
		if (btsSignalIP == null) {
			if (other.btsSignalIP != null)
				return false;
		} else if (!btsSignalIP.equals(other.btsSignalIP))
			return false;
		if (btsSignalPort != other.btsSignalPort)
			return false;
		if (homeBtsGroupId != other.homeBtsGroupId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkBtsVO [btsId=" + btsId + ", homeBtsGroupId=" + homeBtsGroupId
				+ ", btsSignalIP=" + btsSignalIP + ", btsSignalPort="
				+ btsSignalPort + ", btsMediaIP=" + btsMediaIP
				+ ", btsMediaPort=" + btsMediaPort + ", btsDVoiceFlag="
				+ btsDVoiceFlag + ", T_EST=" + T_EST + ", N_EST=" + N_EST
				+ ", T_HS=" + T_HS + ", T_HS_ACK=" + T_HS_ACK + ", N_HS="
				+ N_HS + ", OPCODE=" + OPCODE + ", CSEQ_CLI=" + CSEQ_CLI
				+ ", CSEQ_SER=" + CSEQ_SER + ", btsLinkMBMSFlag="
				+ btsLinkMBMSFlag + ", btsDpId=" + btsDpId + ", LAI="
				+ Arrays.toString(LAI) + ", SPI=" + SPI + "]";
	}
	
	
	
}
