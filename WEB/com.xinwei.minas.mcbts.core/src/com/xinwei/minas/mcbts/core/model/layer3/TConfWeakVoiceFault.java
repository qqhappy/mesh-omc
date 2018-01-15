package com.xinwei.minas.mcbts.core.model.layer3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.omp.core.model.biz.Listable;
import com.xinwei.omp.core.model.biz.FieldProperty;
import com.xinwei.omp.core.utils.ReflectUtils;

public class TConfWeakVoiceFault implements java.io.Serializable {

	//号码分析表的最大长度为20
	public static final int DNINFO_MAX_SIZE = 20;
	
	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 语音故障弱化开关
	private Integer flag;

	// 是否使用用户ACL配置文件
	private Integer voice_user_list_file;

	// 是否使用用户语音列表文件
	private Integer voice_user_list_file2;

	// 是否使用集群数据文件
	private Integer trunk_list_file;

	// 组呼空闲时长
	private Integer multi_call_idle_time;

	// 讲话方最大时长
	private Integer voice_max_time;

	// 组呼最大时长
	private Integer multi_call_max_time;

	// 迟后进入周期
	private Integer delay_interval;

	// 本地区号
	private String division_code;

	// 配置号码分析表信息数据集
	public List<TConfDnInfo> TConfDnInfos = new ArrayList<TConfDnInfo>();

	public TConfWeakVoiceFault() {

	}

	// public TConfWeakVoiceFault(long moId, int typeId, String name, String
	// desc,
	// int manageStateCode) {
	// super(moId, typeId, name, desc, manageStateCode);
	// }

	// ****************************************
	//
	// 以下为getter/setter
	//
	// ****************************************

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

	public Integer getVoice_user_list_file() {
		return voice_user_list_file;
	}

	public void setVoice_user_list_file(Integer voice_user_list_file) {
		this.voice_user_list_file = voice_user_list_file;
	}

	public Integer getVoice_user_list_file2() {
		return voice_user_list_file2;
	}

	public void setVoice_user_list_file2(Integer voice_user_list_file2) {
		this.voice_user_list_file2 = voice_user_list_file2;
	}

	public Integer getTrunk_list_file() {
		return trunk_list_file;
	}

	public void setTrunk_list_file(Integer trunk_list_file) {
		this.trunk_list_file = trunk_list_file;
	}

	public Integer getMulti_call_idle_time() {
		return multi_call_idle_time;
	}

	public void setMulti_call_idle_time(Integer multi_call_idle_time) {
		this.multi_call_idle_time = multi_call_idle_time;
	}

	public Integer getVoice_max_time() {
		return voice_max_time;
	}

	public void setVoice_max_time(Integer voice_max_time) {
		this.voice_max_time = voice_max_time;
	}

	public Integer getMulti_call_max_time() {
		return multi_call_max_time;
	}

	public void setMulti_call_max_time(Integer multi_call_max_time) {
		this.multi_call_max_time = multi_call_max_time;
	}

	public Integer getDelay_interval() {
		return delay_interval;
	}

	public void setDelay_interval(Integer delay_interval) {
		this.delay_interval = delay_interval;
	}

	public String getDivision_code() {
		return division_code;
	}

	public void setDivision_code(String division_code) {
		this.division_code = division_code;
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
				+ ((TConfDnInfos == null) ? 0 : TConfDnInfos.hashCode());
		result = prime * result
				+ ((delay_interval == null) ? 0 : delay_interval.hashCode());
		result = prime * result
				+ ((division_code == null) ? 0 : division_code.hashCode());
		result = prime * result + ((flag == null) ? 0 : flag.hashCode());
		result = prime
				* result
				+ ((multi_call_idle_time == null) ? 0 : multi_call_idle_time
						.hashCode());
		result = prime
				* result
				+ ((multi_call_max_time == null) ? 0 : multi_call_max_time
						.hashCode());
		result = prime * result
				+ ((trunk_list_file == null) ? 0 : trunk_list_file.hashCode());
		result = prime * result
				+ ((voice_max_time == null) ? 0 : voice_max_time.hashCode());
		result = prime
				* result
				+ ((voice_user_list_file == null) ? 0 : voice_user_list_file
						.hashCode());
		result = prime
				* result
				+ ((voice_user_list_file2 == null) ? 0 : voice_user_list_file2
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
		TConfWeakVoiceFault other = (TConfWeakVoiceFault) obj;
		if (TConfDnInfos == null) {
			if (other.TConfDnInfos != null)
				return false;
		} else if (!TConfDnInfos.equals(other.TConfDnInfos))
			return false;
		if (delay_interval == null) {
			if (other.delay_interval != null)
				return false;
		} else if (!delay_interval.equals(other.delay_interval))
			return false;
		if (division_code == null) {
			if (other.division_code != null)
				return false;
		} else if (!division_code.equals(other.division_code))
			return false;
		if (flag == null) {
			if (other.flag != null)
				return false;
		} else if (!flag.equals(other.flag))
			return false;
		if (multi_call_idle_time == null) {
			if (other.multi_call_idle_time != null)
				return false;
		} else if (!multi_call_idle_time.equals(other.multi_call_idle_time))
			return false;
		if (multi_call_max_time == null) {
			if (other.multi_call_max_time != null)
				return false;
		} else if (!multi_call_max_time.equals(other.multi_call_max_time))
			return false;
		if (trunk_list_file == null) {
			if (other.trunk_list_file != null)
				return false;
		} else if (!trunk_list_file.equals(other.trunk_list_file))
			return false;
		if (voice_max_time == null) {
			if (other.voice_max_time != null)
				return false;
		} else if (!voice_max_time.equals(other.voice_max_time))
			return false;
		if (voice_user_list_file == null) {
			if (other.voice_user_list_file != null)
				return false;
		} else if (!voice_user_list_file.equals(other.voice_user_list_file))
			return false;
		if (voice_user_list_file2 == null) {
			if (other.voice_user_list_file2 != null)
				return false;
		} else if (!voice_user_list_file2.equals(other.voice_user_list_file2))
			return false;
		return true;
	}

}
