/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer2;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.omp.core.model.biz.FieldProperty;
import com.xinwei.omp.core.model.biz.Listable;

/**
 * @author chenshaohua
 * 
 */
public class TConfResmanagement implements java.io.Serializable, Listable {

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 带宽请求间隔
	private int bandwidth_requst_interval;

	private int session_release_threshold;

	private int sustained_min_ul_signal_strength;

	private int max_dl_power_per_user;

	private int sustained_ul_bandwidth_per_user;

	private int sustained_dl_bandwidth_per_user;

	private int reserved_power_for_pc;

	private int pc_range;

	private int reassignment_step_size;

	private int max_simultaneous_user;

	private int reserved_tch_for_access;

	private int ul_modulation_mask;

	private int dl_modulation_mask;

	private int bandwidthClass0;

	private int bandwidthClass1;

	private int bandwidthClass2;

	private int bandwidthClass3;

	private int bandwidthClass4;

	private int bandwidthClass5;

	private int bandwidthClass6;

	private int bandwidthClass7;

	public int getBandwidth_requst_interval() {
		return bandwidth_requst_interval;
	}

	public void setBandwidth_requst_interval(int bandwidth_requst_interval) {
		this.bandwidth_requst_interval = bandwidth_requst_interval;
	}

	public int getDl_modulation_mask() {
		return dl_modulation_mask;
	}

	public void setDl_modulation_mask(int dl_modulation_mask) {
		this.dl_modulation_mask = dl_modulation_mask;
	}

	public int getMax_dl_power_per_user() {
		return max_dl_power_per_user;
	}

	public void setMax_dl_power_per_user(int max_dl_power_per_user) {
		this.max_dl_power_per_user = max_dl_power_per_user;
	}

	public int getMax_simultaneous_user() {
		return max_simultaneous_user;
	}

	public void setMax_simultaneous_user(int max_simultaneous_user) {
		this.max_simultaneous_user = max_simultaneous_user;
	}

	public int getPc_range() {
		return pc_range;
	}

	public void setPc_range(int pc_range) {
		this.pc_range = pc_range;
	}

	public int getReassignment_step_size() {
		return reassignment_step_size;
	}

	public void setReassignment_step_size(int reassignment_step_size) {
		this.reassignment_step_size = reassignment_step_size;
	}

	public int getReserved_power_for_pc() {
		return reserved_power_for_pc;
	}

	public void setReserved_power_for_pc(int reserved_power_for_pc) {
		this.reserved_power_for_pc = reserved_power_for_pc;
	}

	public int getReserved_tch_for_access() {
		return reserved_tch_for_access;
	}

	public void setReserved_tch_for_access(int reserved_tch_for_access) {
		this.reserved_tch_for_access = reserved_tch_for_access;
	}

	public int getSession_release_threshold() {
		return session_release_threshold;
	}

	public void setSession_release_threshold(int session_release_threshold) {
		this.session_release_threshold = session_release_threshold;
	}

	public int getSustained_dl_bandwidth_per_user() {
		return sustained_dl_bandwidth_per_user;
	}

	public void setSustained_dl_bandwidth_per_user(
			int sustained_dl_bandwidth_per_user) {
		this.sustained_dl_bandwidth_per_user = sustained_dl_bandwidth_per_user;
	}

	public int getSustained_min_ul_signal_strength() {
		return sustained_min_ul_signal_strength;
	}

	public void setSustained_min_ul_signal_strength(
			int sustained_min_ul_signal_strength) {
		this.sustained_min_ul_signal_strength = sustained_min_ul_signal_strength;
	}

	public int getSustained_ul_bandwidth_per_user() {
		return sustained_ul_bandwidth_per_user;
	}

	public void setSustained_ul_bandwidth_per_user(
			int sustained_ul_bandwidth_per_user) {
		this.sustained_ul_bandwidth_per_user = sustained_ul_bandwidth_per_user;
	}

	public int getUl_modulation_mask() {
		return ul_modulation_mask;
	}

	public void setUl_modulation_mask(int ul_modulation_mask) {
		this.ul_modulation_mask = ul_modulation_mask;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public int getBandwidthClass0() {
		return bandwidthClass0;
	}

	public void setBandwidthClass0(Integer bandwidthClass0) {
		this.bandwidthClass0 = bandwidthClass0;
	}

	public int getBandwidthClass1() {
		return bandwidthClass1;
	}

	public void setBandwidthClass1(Integer bandwidthClass1) {
		this.bandwidthClass1 = bandwidthClass1;
	}

	public int getBandwidthClass2() {
		return bandwidthClass2;
	}

	public void setBandwidthClass2(Integer bandwidthClass2) {
		this.bandwidthClass2 = bandwidthClass2;
	}

	public int getBandwidthClass3() {
		return bandwidthClass3;
	}

	public void setBandwidthClass3(Integer bandwidthClass3) {
		this.bandwidthClass3 = bandwidthClass3;
	}

	public int getBandwidthClass4() {
		return bandwidthClass4;
	}

	public void setBandwidthClass4(Integer bandwidthClass4) {
		this.bandwidthClass4 = bandwidthClass4;
	}

	public int getBandwidthClass5() {
		return bandwidthClass5;
	}

	public void setBandwidthClass5(Integer bandwidthClass5) {
		this.bandwidthClass5 = bandwidthClass5;
	}

	public int getBandwidthClass6() {
		return bandwidthClass6;
	}

	public void setBandwidthClass6(Integer bandwidthClass6) {
		this.bandwidthClass6 = bandwidthClass6;
	}

	public int getBandwidthClass7() {
		return bandwidthClass7;
	}

	public void setBandwidthClass7(Integer bandwidthClass7) {
		this.bandwidthClass7 = bandwidthClass7;
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
		result = prime * result + bandwidthClass0;
		result = prime * result + bandwidthClass1;
		result = prime * result + bandwidthClass2;
		result = prime * result + bandwidthClass3;
		result = prime * result + bandwidthClass4;
		result = prime * result + bandwidthClass5;
		result = prime * result + bandwidthClass6;
		result = prime * result + bandwidthClass7;
		result = prime * result + bandwidth_requst_interval;
		result = prime * result + dl_modulation_mask;
		result = prime * result + max_dl_power_per_user;
		result = prime * result + max_simultaneous_user;
		result = prime * result + pc_range;
		result = prime * result + reassignment_step_size;
		result = prime * result + reserved_power_for_pc;
		result = prime * result + reserved_tch_for_access;
		result = prime * result + session_release_threshold;
		result = prime * result + sustained_dl_bandwidth_per_user;
		result = prime * result + sustained_min_ul_signal_strength;
		result = prime * result + sustained_ul_bandwidth_per_user;
		result = prime * result + ul_modulation_mask;
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
		TConfResmanagement other = (TConfResmanagement) obj;
		if (bandwidthClass0 != other.bandwidthClass0)
			return false;
		if (bandwidthClass1 != other.bandwidthClass1)
			return false;
		if (bandwidthClass2 != other.bandwidthClass2)
			return false;
		if (bandwidthClass3 != other.bandwidthClass3)
			return false;
		if (bandwidthClass4 != other.bandwidthClass4)
			return false;
		if (bandwidthClass5 != other.bandwidthClass5)
			return false;
		if (bandwidthClass6 != other.bandwidthClass6)
			return false;
		if (bandwidthClass7 != other.bandwidthClass7)
			return false;
		if (bandwidth_requst_interval != other.bandwidth_requst_interval)
			return false;
		if (dl_modulation_mask != other.dl_modulation_mask)
			return false;
		if (max_dl_power_per_user != other.max_dl_power_per_user)
			return false;
		if (max_simultaneous_user != other.max_simultaneous_user)
			return false;
		if (pc_range != other.pc_range)
			return false;
		if (reassignment_step_size != other.reassignment_step_size)
			return false;
		if (reserved_power_for_pc != other.reserved_power_for_pc)
			return false;
		if (reserved_tch_for_access != other.reserved_tch_for_access)
			return false;
		if (session_release_threshold != other.session_release_threshold)
			return false;
		if (sustained_dl_bandwidth_per_user != other.sustained_dl_bandwidth_per_user)
			return false;
		if (sustained_min_ul_signal_strength != other.sustained_min_ul_signal_strength)
			return false;
		if (sustained_ul_bandwidth_per_user != other.sustained_ul_bandwidth_per_user)
			return false;
		if (ul_modulation_mask != other.ul_modulation_mask)
			return false;
		return true;
	}

	@Override
	public List<FieldProperty> listAll() {
		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.bandwidth_requst_interval", String
						.valueOf(this.getBandwidth_requst_interval())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.session_release_threshold", String
						.valueOf(this.getSession_release_threshold())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.sustained_min_ul_signal_strength",
				String.valueOf(this.getSustained_min_ul_signal_strength())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.max_dl_power_per_user", String
						.valueOf(this.getMax_dl_power_per_user())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.sustained_ul_bandwidth_per_user",
				String.valueOf(this.getSustained_ul_bandwidth_per_user())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.sustained_dl_bandwidth_per_user",
				String.valueOf(this.getSustained_dl_bandwidth_per_user())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.reserved_power_for_pc", String
						.valueOf(this.getReserved_power_for_pc())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.pc_range", String.valueOf(this
						.getPc_range())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.reassignment_step_size", String
						.valueOf(this.getReassignment_step_size())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.max_simultaneous_user", String
						.valueOf(this.getMax_simultaneous_user())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.reserved_tch_for_access", String
						.valueOf(this.getReserved_tch_for_access())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.ul_modulation_mask", String
						.valueOf(this.getUl_modulation_mask())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.dl_modulation_mask", String
						.valueOf(this.getDl_modulation_mask())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.bandwidthClass0", String
						.valueOf(this.getBandwidthClass0())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.bandwidthClass1", String
						.valueOf(this.getBandwidthClass1())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.bandwidthClass2", String
						.valueOf(this.getBandwidthClass2())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.bandwidthClass3", String
						.valueOf(this.getBandwidthClass3())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.bandwidthClass4", String
						.valueOf(this.getBandwidthClass4())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.bandwidthClass5", String
						.valueOf(this.getBandwidthClass5())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.bandwidthClass6", String
						.valueOf(this.getBandwidthClass6())));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfResmanagement.bandwidthClass7", String
						.valueOf(this.getBandwidthClass7())));

		return allProperties;
	}

	@Override
	public String getBizName() {
		return "t_conf_resmanagement";
	}
}
