package com.xinwei.minas.mcbts.core.model.layer3;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.omp.core.model.biz.FieldProperty;
import com.xinwei.omp.core.model.biz.Listable;

/**
 * ����SAG��������ʵ����
 * 
 * @author yinbinqiang
 * 
 */
public class TConfBackupSag implements Listable, java.io.Serializable {

	// ��¼����
	private Long idx;

	// MO��ţ�ȫ��Ψһ,ϵͳ�Զ����ɣ�
	private long moId;

	// SAG����IP��ַ
	private Long sAGIPforVoice;

	// SAG����IP��ַ
	private Long sAGIPforsignal;

	// BS�Ƿ�ǿ��ʹ�û���
	private Integer bSForceUseJitterbuffer;

	// Zģ���Ƿ�ʹ�û���
	private Integer zModelUseJitterbuffer;

	// �����С
	private Integer jitterbufferSize;

	// ���淢�����ݰ�����
	private Integer jitterbufferPackageThreshold;

	// SAG����TOS
	private Integer sAGVoiceTOS;

	// SAG ID
	private Long SAGID;

	// �������ն˿ں�
	private Integer SAGRxPortForVoice;

	// �������Ͷ˿ں�
	private Integer SAGTxPortForVoice;

	// ������ն˿ں�
	private Integer SAGRxPortForSignal;

	// ����Ͷ˿ں�
	private Integer SAGTxPortForSignal;

	// ����ID
	private Long LocationAreaID;

	// SAG��������
	private Integer SAGSignalPointCode;

	// ��վ��������
	private Integer BTSSignalPointCode;

	//reserved
	private Integer rsv;
	
	// NatAPKey
	private Integer NatAPKey;

	public TConfBackupSag() {

	}

	public TConfBackupSag(Long idx, Long moId, Long sAGIPforVoice,
			Long sAGIPforsignal, Integer bSForceUseJitterbuffer,
			Integer zModelUseJitterbuffer, Integer jitterbufferSize,
			Integer jitterbufferPackageThreshold, Integer sAGVoiceTOS,
			Long sAGID, Integer sAGRxPortForVoice, Integer sAGTxPortForVoice,
			Integer sAGRxPortForSignal, Integer sAGTxPortForSignal,
			Long locationAreaID, Integer sAGSignalPointCode,
			Integer bTSSignalPointCode, Integer natAPKey) {
		// super(moId, typeId, name, desc, manageStateCode);
		this.idx = idx;
		this.moId = moId;
		this.sAGIPforVoice = sAGIPforVoice;
		this.sAGIPforsignal = sAGIPforsignal;
		this.bSForceUseJitterbuffer = bSForceUseJitterbuffer;
		this.zModelUseJitterbuffer = zModelUseJitterbuffer;
		this.jitterbufferSize = jitterbufferSize;
		this.jitterbufferPackageThreshold = jitterbufferPackageThreshold;
		this.sAGVoiceTOS = sAGVoiceTOS;
		SAGID = sAGID;
		SAGRxPortForVoice = sAGRxPortForVoice;
		SAGTxPortForVoice = sAGTxPortForVoice;
		SAGRxPortForSignal = sAGRxPortForSignal;
		SAGTxPortForSignal = sAGTxPortForSignal;
		LocationAreaID = locationAreaID;
		SAGSignalPointCode = sAGSignalPointCode;
		BTSSignalPointCode = bTSSignalPointCode;
		NatAPKey = natAPKey;
	}

	// ****************************************
	//
	// ����Ϊgetter/setter
	//
	// ****************************************

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Long getsAGIPforVoice() {
		return sAGIPforVoice;
	}

	public void setsAGIPforVoice(Long sAGIPforVoice) {
		this.sAGIPforVoice = sAGIPforVoice;
	}

	public Long getsAGIPforsignal() {
		return sAGIPforsignal;
	}

	public void setsAGIPforsignal(Long sAGIPforsignal) {
		this.sAGIPforsignal = sAGIPforsignal;
	}

	public Integer getbSForceUseJitterbuffer() {
		return bSForceUseJitterbuffer;
	}

	public void setbSForceUseJitterbuffer(Integer bSForceUseJitterbuffer) {
		this.bSForceUseJitterbuffer = bSForceUseJitterbuffer;
	}

	public Integer getzModelUseJitterbuffer() {
		return zModelUseJitterbuffer;
	}

	public void setzModelUseJitterbuffer(Integer zModelUseJitterbuffer) {
		this.zModelUseJitterbuffer = zModelUseJitterbuffer;
	}

	public Integer getJitterbufferSize() {
		return jitterbufferSize;
	}

	public void setJitterbufferSize(Integer jitterbufferSize) {
		this.jitterbufferSize = jitterbufferSize;
	}

	public Integer getJitterbufferPackageThreshold() {
		return jitterbufferPackageThreshold;
	}

	public void setJitterbufferPackageThreshold(
			Integer jitterbufferPackageThreshold) {
		this.jitterbufferPackageThreshold = jitterbufferPackageThreshold;
	}

	public Integer getsAGVoiceTOS() {
		return sAGVoiceTOS;
	}

	public void setsAGVoiceTOS(Integer sAGVoiceTOS) {
		this.sAGVoiceTOS = sAGVoiceTOS;
	}

	public Long getSAGID() {
		return SAGID;
	}

	public void setSAGID(Long sAGID) {
		SAGID = sAGID;
	}

	public Integer getSAGRxPortForVoice() {
		return SAGRxPortForVoice;
	}

	public void setSAGRxPortForVoice(Integer sAGRxPortForVoice) {
		SAGRxPortForVoice = sAGRxPortForVoice;
	}

	public Integer getSAGTxPortForVoice() {
		return SAGTxPortForVoice;
	}

	public void setSAGTxPortForVoice(Integer sAGTxPortForVoice) {
		SAGTxPortForVoice = sAGTxPortForVoice;
	}

	public Integer getSAGRxPortForSignal() {
		return SAGRxPortForSignal;
	}

	public void setSAGRxPortForSignal(Integer sAGRxPortForSignal) {
		SAGRxPortForSignal = sAGRxPortForSignal;
	}

	public Integer getSAGTxPortForSignal() {
		return SAGTxPortForSignal;
	}

	public void setSAGTxPortForSignal(Integer sAGTxPortForSignal) {
		SAGTxPortForSignal = sAGTxPortForSignal;
	}

	public Long getLocationAreaID() {
		return LocationAreaID;
	}

	public void setLocationAreaID(Long locationAreaID) {
		LocationAreaID = locationAreaID;
	}

	public Integer getSAGSignalPointCode() {
		return SAGSignalPointCode;
	}

	public void setSAGSignalPointCode(Integer sAGSignalPointCode) {
		SAGSignalPointCode = sAGSignalPointCode;
	}

	public Integer getBTSSignalPointCode() {
		return BTSSignalPointCode;
	}

	public void setBTSSignalPointCode(Integer bTSSignalPointCode) {
		BTSSignalPointCode = bTSSignalPointCode;
	}

	public Integer getNatAPKey() {
		return NatAPKey;
	}

	public void setNatAPKey(Integer natAPKey) {
		NatAPKey = natAPKey;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((BTSSignalPointCode == null) ? 0 : BTSSignalPointCode
						.hashCode());
		result = prime * result
				+ ((LocationAreaID == null) ? 0 : LocationAreaID.hashCode());
		result = prime * result
				+ ((NatAPKey == null) ? 0 : NatAPKey.hashCode());
		result = prime * result + ((SAGID == null) ? 0 : SAGID.hashCode());
		result = prime
				* result
				+ ((SAGRxPortForSignal == null) ? 0 : SAGRxPortForSignal
						.hashCode());
		result = prime
				* result
				+ ((SAGRxPortForVoice == null) ? 0 : SAGRxPortForVoice
						.hashCode());
		result = prime
				* result
				+ ((SAGSignalPointCode == null) ? 0 : SAGSignalPointCode
						.hashCode());
		result = prime
				* result
				+ ((SAGTxPortForSignal == null) ? 0 : SAGTxPortForSignal
						.hashCode());
		result = prime
				* result
				+ ((SAGTxPortForVoice == null) ? 0 : SAGTxPortForVoice
						.hashCode());
		result = prime
				* result
				+ ((bSForceUseJitterbuffer == null) ? 0
						: bSForceUseJitterbuffer.hashCode());
		result = prime
				* result
				+ ((jitterbufferPackageThreshold == null) ? 0
						: jitterbufferPackageThreshold.hashCode());
		result = prime
				* result
				+ ((jitterbufferSize == null) ? 0 : jitterbufferSize.hashCode());
		result = prime * result
				+ ((sAGIPforVoice == null) ? 0 : sAGIPforVoice.hashCode());
		result = prime * result
				+ ((sAGIPforsignal == null) ? 0 : sAGIPforsignal.hashCode());
		result = prime * result
				+ ((sAGVoiceTOS == null) ? 0 : sAGVoiceTOS.hashCode());
		result = prime
				* result
				+ ((zModelUseJitterbuffer == null) ? 0 : zModelUseJitterbuffer
						.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TConfBackupSag other = (TConfBackupSag) obj;
		if (BTSSignalPointCode == null) {
			if (other.BTSSignalPointCode != null)
				return false;
		} else if (!BTSSignalPointCode.equals(other.BTSSignalPointCode))
			return false;
		if (LocationAreaID == null) {
			if (other.LocationAreaID != null)
				return false;
		} else if (!LocationAreaID.equals(other.LocationAreaID))
			return false;
		if (NatAPKey == null) {
			if (other.NatAPKey != null)
				return false;
		} else if (!NatAPKey.equals(other.NatAPKey))
			return false;
		if (SAGID == null) {
			if (other.SAGID != null)
				return false;
		} else if (!SAGID.equals(other.SAGID))
			return false;
		if (SAGRxPortForSignal == null) {
			if (other.SAGRxPortForSignal != null)
				return false;
		} else if (!SAGRxPortForSignal.equals(other.SAGRxPortForSignal))
			return false;
		if (SAGRxPortForVoice == null) {
			if (other.SAGRxPortForVoice != null)
				return false;
		} else if (!SAGRxPortForVoice.equals(other.SAGRxPortForVoice))
			return false;
		if (SAGSignalPointCode == null) {
			if (other.SAGSignalPointCode != null)
				return false;
		} else if (!SAGSignalPointCode.equals(other.SAGSignalPointCode))
			return false;
		if (SAGTxPortForSignal == null) {
			if (other.SAGTxPortForSignal != null)
				return false;
		} else if (!SAGTxPortForSignal.equals(other.SAGTxPortForSignal))
			return false;
		if (SAGTxPortForVoice == null) {
			if (other.SAGTxPortForVoice != null)
				return false;
		} else if (!SAGTxPortForVoice.equals(other.SAGTxPortForVoice))
			return false;
		if (bSForceUseJitterbuffer == null) {
			if (other.bSForceUseJitterbuffer != null)
				return false;
		} else if (!bSForceUseJitterbuffer.equals(other.bSForceUseJitterbuffer))
			return false;
		if (jitterbufferPackageThreshold == null) {
			if (other.jitterbufferPackageThreshold != null)
				return false;
		} else if (!jitterbufferPackageThreshold
				.equals(other.jitterbufferPackageThreshold))
			return false;
		if (jitterbufferSize == null) {
			if (other.jitterbufferSize != null)
				return false;
		} else if (!jitterbufferSize.equals(other.jitterbufferSize))
			return false;
		if (sAGIPforVoice == null) {
			if (other.sAGIPforVoice != null)
				return false;
		} else if (!sAGIPforVoice.equals(other.sAGIPforVoice))
			return false;
		if (sAGIPforsignal == null) {
			if (other.sAGIPforsignal != null)
				return false;
		} else if (!sAGIPforsignal.equals(other.sAGIPforsignal))
			return false;
		if (sAGVoiceTOS == null) {
			if (other.sAGVoiceTOS != null)
				return false;
		} else if (!sAGVoiceTOS.equals(other.sAGVoiceTOS))
			return false;
		if (zModelUseJitterbuffer == null) {
			if (other.zModelUseJitterbuffer != null)
				return false;
		} else if (!zModelUseJitterbuffer.equals(other.zModelUseJitterbuffer))
			return false;
		return true;
	}

	@Override
	public List<FieldProperty> listAll() {
		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		// SAG����IP��ַ
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.sAGIPforVoice", String.valueOf(this
						.getsAGIPforVoice())));
		// SAG����IP��ַ
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.sAGIPforsignal", String.valueOf(this
						.getsAGIPforsignal())));
		// BS�Ƿ�ǿ��ʹ�û���
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.bSForceUseJitterbuffer", String
						.valueOf(this.getbSForceUseJitterbuffer())));
		// Zģ���Ƿ�ʹ�û���
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.zModelUseJitterbuffer", String
						.valueOf(this.getzModelUseJitterbuffer())));
		// �����С
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.jitterbufferSize", String.valueOf(this
						.getJitterbufferSize())));
		// ���淢�����ݰ�����
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.jitterbufferPackageThreshold", String
						.valueOf(this.getJitterbufferPackageThreshold())));
		// SAG����TOS
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.sAGVoiceTOS", String.valueOf(this
						.getsAGVoiceTOS())));
		// SAG ID
		allProperties.add(new FieldProperty(0, "listable.TConfBackupSag.SAGID",
				String.valueOf(this.getSAGID())));
		// �������ն˿ں�
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.SAGRxPortForVoice", String
						.valueOf(this.getSAGRxPortForVoice())));
		// �������Ͷ˿ں�
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.SAGTxPortForVoice", String
						.valueOf(this.getSAGTxPortForVoice())));
		// ������ն˿ں�
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.SAGRxPortForSignal", String
						.valueOf(this.getSAGRxPortForSignal())));
		// ����Ͷ˿ں�
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.SAGTxPortForSignal", String
						.valueOf(this.getSAGTxPortForSignal())));
		// ����ID
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.LocationAreaID", String.valueOf(this
						.getLocationAreaID())));
		// SAG��������
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.SAGSignalPointCode", String
						.valueOf(this.getSAGSignalPointCode())));
		// ��վ��������
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.BTSSignalPointCode", String
						.valueOf(this.getBTSSignalPointCode())));
		// NatAPKey
		allProperties.add(new FieldProperty(0,
				"listable.TConfBackupSag.NatAPKey", String.valueOf(this
						.getNatAPKey())));

		return allProperties;
	}

	@Override
	public String getBizName() {
		return "mcbts_backup_sag";
	}

	public Integer getRsv() {
		return rsv;
	}

	public void setRsv(Integer rsv) {
		this.rsv = rsv;
	}

}
