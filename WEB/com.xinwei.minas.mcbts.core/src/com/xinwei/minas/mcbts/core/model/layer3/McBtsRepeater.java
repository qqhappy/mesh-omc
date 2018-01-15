/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

/**
 * @author chenshaohua
 * 
 */
public class McBtsRepeater implements Serializable {

	// ��¼����
	private Long idx;

	// ���ܶ���id
	private Long moId;

	// Ƶ��
	private Double middleFreq;

	private int offset;

	public Object getPropertyValueByName(String propertyName) {
		if (propertyName.equals("middleFreq")) {
			return this.middleFreq;
		}
		return null;
	}

	public Double getMiddleFreq() {
		return middleFreq;
	}

	public void setMiddleFreq(Double middleFreq) {
		this.middleFreq = middleFreq;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Long getMoId() {
		return moId;
	}

	public void setMoId(Long moId) {
		this.moId = moId;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

}
