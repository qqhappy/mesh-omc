package com.xinwei.minas.mcbts.core.model.layer3;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.omp.core.model.biz.FieldProperty;
import com.xinwei.omp.core.model.biz.Listable;

/**
 * WCPE配置实体类
 * 
 * @author yinbinqiang
 * 
 */
public class TConfWCPE implements Listable, java.io.Serializable {
	// 主键索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// WCPE工作模式
	private Integer workMode;

	// 主WCPE UID
	private String primaryWCPE;

	// 备WCPE UID
	private String standbyWCPE;

	// MAC1
	private String sacMac;

	// MAC2
	private String sacMac2;

	// MAC3
	private String sacMac3;

	// MAC4
	private String sacMac4;

	// MAC5
	private String sacMac5;

	public TConfWCPE() {

	}

	public TConfWCPE(Long idx, Long moId, Integer workMode, String primaryWCPE,
			String standbyWCPE, String sacMac, String sacMac2, String sacMac3,
			String sacMac4, String sacMac5) {
		super();
		this.idx = idx;
		this.moId = moId;
		this.workMode = workMode;
		this.primaryWCPE = primaryWCPE.toUpperCase();
		this.standbyWCPE = standbyWCPE.toUpperCase();
		this.sacMac = sacMac.toUpperCase();
		this.sacMac2 = sacMac2.toUpperCase();
		this.sacMac3 = sacMac3.toUpperCase();
		this.sacMac4 = sacMac4.toUpperCase();
		this.sacMac5 = sacMac5.toUpperCase();
	}

	// ********************
	/** set/get */
	// ********************
	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Integer getWorkMode() {
		return workMode;
	}

	public void setWorkMode(Integer workMode) {
		this.workMode = workMode;
	}

	public String getPrimaryWCPE() {
		return primaryWCPE;
	}

	public void setPrimaryWCPE(String primaryWCPE) {
		this.primaryWCPE = primaryWCPE.toUpperCase();
	}

	public String getStandbyWCPE() {
		return standbyWCPE;
	}

	public void setStandbyWCPE(String standbyWCPE) {
		this.standbyWCPE = standbyWCPE.toUpperCase();
	}

	public String getSacMac() {
		return sacMac;
	}

	public void setSacMac(String sacMac) {
		this.sacMac = sacMac.toUpperCase();
	}

	public String getSacMac2() {
		return sacMac2;
	}

	public void setSacMac2(String sacMac2) {
		this.sacMac2 = sacMac2.toUpperCase();
	}

	public String getSacMac3() {
		return sacMac3;
	}

	public void setSacMac3(String sacMac3) {
		this.sacMac3 = sacMac3.toUpperCase();
	}

	public String getSacMac4() {
		return sacMac4;
	}

	public void setSacMac4(String sacMac4) {
		this.sacMac4 = sacMac4.toUpperCase();
	}

	public String getSacMac5() {
		return sacMac5;
	}

	public void setSacMac5(String sacMac5) {
		this.sacMac5 = sacMac5.toUpperCase();
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
		result = prime * result
				+ ((primaryWCPE == null) ? 0 : primaryWCPE.hashCode());
		result = prime * result + ((sacMac == null) ? 0 : sacMac.hashCode());
		result = prime * result + ((sacMac2 == null) ? 0 : sacMac2.hashCode());
		result = prime * result + ((sacMac3 == null) ? 0 : sacMac3.hashCode());
		result = prime * result + ((sacMac4 == null) ? 0 : sacMac4.hashCode());
		result = prime * result + ((sacMac5 == null) ? 0 : sacMac5.hashCode());
		result = prime * result
				+ ((standbyWCPE == null) ? 0 : standbyWCPE.hashCode());
		result = prime * result
				+ ((workMode == null) ? 0 : workMode.hashCode());
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
		TConfWCPE other = (TConfWCPE) obj;
		if (primaryWCPE == null) {
			if (other.primaryWCPE != null)
				return false;
		} else if (!primaryWCPE.equals(other.primaryWCPE))
			return false;
		if (sacMac == null) {
			if (other.sacMac != null)
				return false;
		} else if (!sacMac.equals(other.sacMac))
			return false;
		if (sacMac2 == null) {
			if (other.sacMac2 != null)
				return false;
		} else if (!sacMac2.equals(other.sacMac2))
			return false;
		if (sacMac3 == null) {
			if (other.sacMac3 != null)
				return false;
		} else if (!sacMac3.equals(other.sacMac3))
			return false;
		if (sacMac4 == null) {
			if (other.sacMac4 != null)
				return false;
		} else if (!sacMac4.equals(other.sacMac4))
			return false;
		if (sacMac5 == null) {
			if (other.sacMac5 != null)
				return false;
		} else if (!sacMac5.equals(other.sacMac5))
			return false;
		if (standbyWCPE == null) {
			if (other.standbyWCPE != null)
				return false;
		} else if (!standbyWCPE.equals(other.standbyWCPE))
			return false;
		if (workMode == null) {
			if (other.workMode != null)
				return false;
		} else if (!workMode.equals(other.workMode))
			return false;
		return true;
	}

	@Override
	public List<FieldProperty> listAll() {

		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		// WCPE工作模式
		allProperties.add(new FieldProperty(0, "listable.TConfWCPE.workMode",
				String.valueOf(this.getWorkMode())));
		// 主WCPE UID
		allProperties.add(new FieldProperty(0,
				"listable.TConfWCPE.primaryWCPE", String.valueOf(this
						.getPrimaryWCPE())));
		// 备WCPE UID
		allProperties.add(new FieldProperty(0,
				"listable.TConfWCPE.standbyWCPE", String.valueOf(this
						.getStandbyWCPE())));
		// MAC1
		allProperties.add(new FieldProperty(0, "listable.TConfWCPE.sacMac",
				String.valueOf(this.getSacMac())));
		// MAC2
		allProperties.add(new FieldProperty(0, "listable.TConfWCPE.sacMac2",
				String.valueOf(this.getSacMac2())));
		// MAC3
		allProperties.add(new FieldProperty(0, "listable.TConfWCPE.sacMac3",
				String.valueOf(this.getSacMac3())));
		// MAC4
		allProperties.add(new FieldProperty(0, "listable.TConfWCPE.sacMac4",
				String.valueOf(this.getSacMac4())));
		// MAC5
		allProperties.add(new FieldProperty(0, "listable.TConfWCPE.sacMac5",
				String.valueOf(this.getSacMac5())));

		return allProperties;
	}

	@Override
	public String getBizName() {

		return "t_conf_wcpe";
	}
}
