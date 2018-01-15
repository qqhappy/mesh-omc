/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer2;

import java.io.Serializable;
import java.util.List;

import com.xinwei.minas.core.model.Mo;

/**
 * @author chenshaohua
 * 
 */
public class TConfFreqSet implements Serializable {

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 频点个数
	private int freqCount;

	// 频点集
	private List<String> freqList;

	// 频点尾数
	private double mantissa;

	// 频点集的初始频点索引，用以把freqList存储到数据库中
	private int freqIndex;

	// 频点集持续范围，用以把freqList存到数据库中
	private int freqSpan;

	public int getFreqCount() {
		return freqCount;
	}

	public void setFreqCount(int freqCount) {
		this.freqCount = freqCount;
	}

	public int getFreqSpan() {
		return freqSpan;
	}

	public void setFreqSpan(int freqSpan) {
		this.freqSpan = freqSpan;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public List<String> getFreqList() {
		return freqList;
	}

	public void setFreqList(List<String> freqList) {
		this.freqList = freqList;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public int getFreqIndex() {
		return freqIndex;
	}

	public void setFreqIndex(int freqIndex) {
		this.freqIndex = freqIndex;
	}

	public double getMantissa() {
		return mantissa;
	}

	public void setMantissa(double mantissa) {
		this.mantissa = mantissa;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + freqCount;
		result = prime * result + freqIndex;
		result = prime * result
				+ ((freqList == null) ? 0 : freqList.hashCode());
		result = prime * result + freqSpan;
		long temp;
		temp = Double.doubleToLongBits(mantissa);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/* (non-Javadoc)
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
		TConfFreqSet other = (TConfFreqSet) obj;
		if (freqCount != other.freqCount)
			return false;
		if (freqIndex != other.freqIndex)
			return false;
		if (freqList == null) {
			if (other.freqList != null)
				return false;
		} else if (!freqList.equals(other.freqList))
			return false;
		if (freqSpan != other.freqSpan)
			return false;
		if (Double.doubleToLongBits(mantissa) != Double
				.doubleToLongBits(other.mantissa))
			return false;
		return true;
	}

}
