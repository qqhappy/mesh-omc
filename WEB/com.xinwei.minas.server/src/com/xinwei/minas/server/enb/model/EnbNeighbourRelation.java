/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.model;

/**
 * 
 * eNB邻区关系模型
 * 
 * @author fanhaoyu
 * 
 */

public class EnbNeighbourRelation {

	public static final int NOT_NEIGHBOUR = 0;

	public static final int IS_NEIGHBOUR = 1;

	private Long idx;

	private long moId;

	private int srvCellId;

	private long enbId;

	private int cellId;

	private int isNeighbour;

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

	public int getSrvCellId() {
		return srvCellId;
	}

	public void setSrvCellId(int srvCellId) {
		this.srvCellId = srvCellId;
	}

	public long getEnbId() {
		return enbId;
	}

	public void setEnbId(long enbId) {
		this.enbId = enbId;
	}

	public int getCellId() {
		return cellId;
	}

	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	public int getIsNeighbour() {
		return isNeighbour;
	}

	public void setIsNeighbour(int isNeighbour) {
		this.isNeighbour = isNeighbour;
	}

	public boolean isNeighbour() {
		return this.isNeighbour == IS_NEIGHBOUR;
	}

}
