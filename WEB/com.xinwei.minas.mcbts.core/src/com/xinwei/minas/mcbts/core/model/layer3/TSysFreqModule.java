package com.xinwei.minas.mcbts.core.model.layer3;

import java.util.Comparator;

public class TSysFreqModule implements java.io.Serializable,
		Comparable<TSysFreqModule> {

	// 记录索引
	private Long idx;

	// private int num; // 有限频点时 频点个数 最大20个

	// 频段类型
	private int freqType;
	// 有限频点
	private int freq;
	// 中间频点值
	private double doubleType;

	public TSysFreqModule() {
		super();
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public int getFreqType() {
		return freqType;
	}

	public void setFreqType(int freqType) {
		this.freqType = freqType;
	}

	public TSysFreqModule(int freqType, int freq, double doubleType) {
		super();
		this.freqType = freqType;
		this.freq = freq;
		this.doubleType = doubleType;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public TSysFreqModule(Long idx, int freqType, int freq, int doubleType) {
		super();
		this.idx = idx;
		this.freqType = freqType;
		this.freq = freq;
		this.doubleType = doubleType;
	}

	public double getDoubleType() {
		return doubleType;
	}

	public void setDoubleType(double doubleType) {
		this.doubleType = doubleType;
	}

	@Override
	public int compareTo(TSysFreqModule other) {
		if (this.doubleType < other.doubleType)
			return -1;
		else if (this.doubleType > other.doubleType)
			return 1;

		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(doubleType);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + freq;
		result = prime * result + freqType;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TSysFreqModule other = (TSysFreqModule) obj;
		if (Double.doubleToLongBits(doubleType) != Double
				.doubleToLongBits(other.doubleType))
			return false;
		if (freq != other.freq)
			return false;
		if (freqType != other.freqType)
			return false;
		return true;
	}

}
