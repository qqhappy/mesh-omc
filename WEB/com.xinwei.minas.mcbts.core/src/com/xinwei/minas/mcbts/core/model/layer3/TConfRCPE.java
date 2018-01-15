package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.core.model.Mo;

/**
 * rcpe配置实体类
 * 
 * @author yinbinqiang
 * 
 */
public class TConfRCPE implements Serializable {
	// RCPE配置
	// public final static int RCPE_TYPE_OLD = 0;

	// RCPE+配置
	// public final static int RCPE_TYPE_EX = 1;

	// 配置标志
	public final static int CFG_FLAG_RCPE = 0; // 只配置RCPE信息到基础

	public final static int CFG_FLAG_RCPEEX = 1; // 只配置RCPE+信息到基础

	public final static int CFG_FLAG_BOTH = 2; // 都配置

	public static final int WORK_MODE_OFF = 0;

	public static final int WORK_MODE_ON = 1;

	private int cfgFlag = CFG_FLAG_BOTH;

	// 主键
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// rcpe 工作模式
	private Integer workMode;

	public TConfRCPE() {

	}

	public TConfRCPE(Long idx, Integer workMode) {
		super();
		this.idx = idx;
		this.workMode = workMode;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public java.lang.Integer getWorkMode() {
		return workMode;
	}

	public void setWorkMode(java.lang.Integer workMode) {
		this.workMode = workMode;
	}

	// rcpe uid数据集
	public List<TConfRCPEItem> items = new ArrayList<TConfRCPEItem>();

	public List<TConfRCPEItem> getItems() {
		return items;
	}

	public void setItems(List<TConfRCPEItem> items) {
		this.items = items;
	}

	/**
	 * 获得普通RCPE的配置信息
	 * 
	 * @return
	 */
	public List<TConfRCPEItem> getRcpeItems() {
		List<TConfRCPEItem> ret = new ArrayList<TConfRCPEItem>();
		for (TConfRCPEItem item : items) {
			if (item.getUIDType() == null
					|| item.getUIDType().intValue() == CFG_FLAG_RCPE) {
				ret.add(item);
			}
		}
		return ret;
	}

	/**
	 * 获得RCPE+的配置信息
	 * 
	 * @return
	 */
	public List<TConfRCPEItem> getRcpeExItems() {
		List<TConfRCPEItem> ret = new ArrayList<TConfRCPEItem>();
		for (TConfRCPEItem item : items) {
			if (item.getUIDType() != null
					&& item.getUIDType().intValue() == CFG_FLAG_RCPEEX) {
				ret.add(item);
			}
		}
		return ret;
	}

	public int getCfgFlag() {
		return cfgFlag;
	}

	public void setCfgFlag(int cfgFlag) {
		this.cfgFlag = cfgFlag;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

}
