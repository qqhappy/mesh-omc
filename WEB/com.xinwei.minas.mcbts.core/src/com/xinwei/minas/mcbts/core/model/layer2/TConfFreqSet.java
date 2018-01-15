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

	// ��¼����
	private Long idx;

	// MO��ţ�ȫ��Ψһ,ϵͳ�Զ����ɣ�
	private long moId;

	// Ƶ�����
	private int freqCount;

	// Ƶ�㼯
	private List<String> freqList;

	// Ƶ��β��
	private double mantissa;

	// Ƶ�㼯�ĳ�ʼƵ�����������԰�freqList�洢�����ݿ���
	private int freqIndex;

	// Ƶ�㼯������Χ�����԰�freqList�浽���ݿ���
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
