/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.omp.core.model.biz.FieldProperty;
import com.xinwei.omp.core.model.biz.Listable;

/**
 * @author chenshaohua
 * 
 */
public class TConfChangeAlgParam implements Serializable, Listable {

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	//
	private Integer m_ho_pwr_thd;

	private Integer m_ho_pwr_offset1;

	private Integer m_ho_pwr_offset2;

	private Integer m_cpe_cm_ho_period;

	private Integer m_ho_pwr_filtercoef_stat;

	private Integer m_ho_pwr_filtercoef_mobile;

	private Integer time_to_trigger;

	// 下边是四种门限
	private Integer strictArea_pwr_thd;

	private Integer strictArea_time_to_trigger;

	private Integer strictArea_ho_pwr_offset;

	private Integer strictArea_2_normal_pwr_thres;

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
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
		result = prime * result + m_cpe_cm_ho_period;
		result = prime * result + m_ho_pwr_filtercoef_mobile;
		result = prime * result + m_ho_pwr_filtercoef_stat;
		result = prime * result + m_ho_pwr_offset1;
		result = prime * result + m_ho_pwr_offset2;
		result = prime * result + m_ho_pwr_thd;
		result = prime * result + time_to_trigger;
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
		TConfChangeAlgParam other = (TConfChangeAlgParam) obj;
		if (m_cpe_cm_ho_period.intValue() != other.m_cpe_cm_ho_period
				.intValue())
			return false;
		if (m_ho_pwr_filtercoef_mobile.intValue() != other.m_ho_pwr_filtercoef_mobile
				.intValue())
			return false;
		if (m_ho_pwr_filtercoef_stat.intValue() != other.m_ho_pwr_filtercoef_stat
				.intValue())
			return false;
		if (m_ho_pwr_offset1.intValue() != other.m_ho_pwr_offset1.intValue())
			return false;
		if (m_ho_pwr_offset2.intValue() != other.m_ho_pwr_offset2.intValue())
			return false;
		if (m_ho_pwr_thd.intValue() != other.m_ho_pwr_thd.intValue())
			return false;
		if (time_to_trigger.intValue() != other.time_to_trigger.intValue())
			return false;
		return true;
	}

	@Override
	public List<FieldProperty> listAll() {
		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfChangeAlgParam.m_ho_pwr_thd", String
						.valueOf(this.m_ho_pwr_thd)));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfChangeAlgParam.m_ho_pwr_offset1", String
						.valueOf(this.m_ho_pwr_offset1)));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfChangeAlgParam.m_ho_pwr_offset2", String
						.valueOf(this.m_ho_pwr_offset2)));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfChangeAlgParam.m_cpe_cm_ho_period", String
						.valueOf(this.m_cpe_cm_ho_period)));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfChangeAlgParam.m_ho_pwr_filtercoef_stat", String
						.valueOf(this.m_ho_pwr_filtercoef_stat)));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfChangeAlgParam.m_ho_pwr_filtercoef_mobile",
				String.valueOf(this.m_ho_pwr_filtercoef_mobile)));
		//
		allProperties.add(new FieldProperty(0,
				"listable.TConfChangeAlgParam.time_to_trigger", String
						.valueOf(this.time_to_trigger)));

		return allProperties;
	}

	@Override
	public String getBizName() {
		return "t_conf_alg_param";
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public Integer getM_ho_pwr_thd() {
		return m_ho_pwr_thd;
	}

	public void setM_ho_pwr_thd(Integer m_ho_pwr_thd) {
		this.m_ho_pwr_thd = m_ho_pwr_thd;
	}

	public Integer getM_ho_pwr_offset1() {
		return m_ho_pwr_offset1;
	}

	public void setM_ho_pwr_offset1(Integer m_ho_pwr_offset1) {
		this.m_ho_pwr_offset1 = m_ho_pwr_offset1;
	}

	public Integer getM_ho_pwr_offset2() {
		return m_ho_pwr_offset2;
	}

	public void setM_ho_pwr_offset2(Integer m_ho_pwr_offset2) {
		this.m_ho_pwr_offset2 = m_ho_pwr_offset2;
	}

	public Integer getM_cpe_cm_ho_period() {
		return m_cpe_cm_ho_period;
	}

	public void setM_cpe_cm_ho_period(Integer m_cpe_cm_ho_period) {
		this.m_cpe_cm_ho_period = m_cpe_cm_ho_period;
	}

	public Integer getM_ho_pwr_filtercoef_stat() {
		return m_ho_pwr_filtercoef_stat;
	}

	public void setM_ho_pwr_filtercoef_stat(Integer m_ho_pwr_filtercoef_stat) {
		this.m_ho_pwr_filtercoef_stat = m_ho_pwr_filtercoef_stat;
	}

	public Integer getM_ho_pwr_filtercoef_mobile() {
		return m_ho_pwr_filtercoef_mobile;
	}

	public void setM_ho_pwr_filtercoef_mobile(Integer m_ho_pwr_filtercoef_mobile) {
		this.m_ho_pwr_filtercoef_mobile = m_ho_pwr_filtercoef_mobile;
	}

	public Integer getTime_to_trigger() {
		return time_to_trigger;
	}

	public void setTime_to_trigger(Integer time_to_trigger) {
		this.time_to_trigger = time_to_trigger;
	}

	public Integer getStrictArea_pwr_thd() {
		return strictArea_pwr_thd;
	}

	public void setStrictArea_pwr_thd(Integer strictArea_pwr_thd) {
		this.strictArea_pwr_thd = strictArea_pwr_thd;
	}

	public Integer getStrictArea_time_to_trigger() {
		return strictArea_time_to_trigger;
	}

	public void setStrictArea_time_to_trigger(Integer strictArea_time_to_trigger) {
		this.strictArea_time_to_trigger = strictArea_time_to_trigger;
	}

	public Integer getStrictArea_ho_pwr_offset() {
		return strictArea_ho_pwr_offset;
	}

	public void setStrictArea_ho_pwr_offset(Integer strictArea_ho_pwr_offset) {
		this.strictArea_ho_pwr_offset = strictArea_ho_pwr_offset;
	}

	public Integer getStrictArea_2_normal_pwr_thres() {
		return strictArea_2_normal_pwr_thres;
	}

	public void setStrictArea_2_normal_pwr_thres(
			Integer strictArea_2_normal_pwr_thres) {
		this.strictArea_2_normal_pwr_thres = strictArea_2_normal_pwr_thres;
	}

}
