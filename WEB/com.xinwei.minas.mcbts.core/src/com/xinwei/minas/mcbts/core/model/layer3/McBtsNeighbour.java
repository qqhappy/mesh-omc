/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer3;

/**
 * 邻接表实体类
 * 
 * @author chenshaohua
 * 
 */
public class McBtsNeighbour implements java.io.Serializable {

	// 记录索引
	private Long idx;

	// 基站MOId
	private long moId;

	// 邻接基站moId
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
