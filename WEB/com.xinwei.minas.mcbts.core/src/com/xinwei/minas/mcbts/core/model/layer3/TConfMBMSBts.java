package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

import com.xinwei.minas.core.model.Mo;

/**
 * 远距离基站配置实体类
 * 
 * @author yinbinqiang
 * 
 */
public class TConfMBMSBts implements Serializable {

	public final static int FLAG_NOT_SUPPORT = 0;

	public final static int FLAG_SUPPORT = 1;

	// primary key
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// fields
	private Integer flag;

	private Integer rev = 0;

	private long districtId = 0;

	public TConfMBMSBts() {

	}

	public TConfMBMSBts(Long idx, Integer flag) {
		super();
		this.idx = idx;
		this.flag = flag;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public Integer getRev() {
		return rev;
	}

	public void setRev(Integer rev) {
		this.rev = rev;
	}

	public long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}

	public boolean isMBMSBTS() {
		if (flag.intValue() == FLAG_SUPPORT) {
			return true;
		} else {
			return false;
		}
	}

}
