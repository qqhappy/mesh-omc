package com.xinwei.minas.mcbts.core.model.layer3;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.omp.core.model.biz.FieldProperty;
import com.xinwei.omp.core.model.biz.Listable;

/**
 * 故障弱化开关配置类
 * 
 * @author yinbinqiang
 * 
 */
public class TConfFaultSwitch implements Listable, java.io.Serializable {

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 语音弱化开关
	private int flag;

	public TConfFaultSwitch() {
	}

	// public TConfFaultSwitch(long moId, int typeId, String name, String desc,
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

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	@Override
	public List<FieldProperty> listAll() {
		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		allProperties.add(new FieldProperty(0,
				"listable.TConfFaultSwitch.flag", String.valueOf(this
						.getFlag())));
		return allProperties;
	}

	@Override
	public String getBizName() {
		return "t_conf_fault_switch";
	}
}
