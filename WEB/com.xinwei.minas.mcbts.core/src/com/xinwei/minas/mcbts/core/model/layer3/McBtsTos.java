/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

/**
 * @author chenshaohua
 * 
 */
public class McBtsTos implements Serializable {

	// tos���
	private long idx;

	// ����ȼ�
	private int serviceLevel;

	public int getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(int serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public long getIdx() {
		return idx;
	}

	public void setIdx(long idx) {
		this.idx = idx;
	}

}
