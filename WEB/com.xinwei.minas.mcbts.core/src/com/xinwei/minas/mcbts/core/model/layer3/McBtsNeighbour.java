/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer3;

/**
 * �ڽӱ�ʵ����
 * 
 * @author chenshaohua
 * 
 */
public class McBtsNeighbour implements java.io.Serializable {

	// ��¼����
	private Long idx;

	// ��վMOId
	private long moId;

	// �ڽӻ�վmoId
	private long neighbourMoId;

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public long getNeighbourMoId() {
		return neighbourMoId;
	}

	public void setNeighbourMoId(long neighbourMoId) {
		this.neighbourMoId = neighbourMoId;
	}
}
