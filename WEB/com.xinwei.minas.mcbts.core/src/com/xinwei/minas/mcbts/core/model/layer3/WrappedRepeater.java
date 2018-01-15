/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer3;

import java.util.List;

/**
 * @author chenshaohua
 * 
 */
public class WrappedRepeater {
	// 列表size
	private int totalNum;

	// 直放站频点配置列表
	private List<McBtsRepeater> mcBtsRepeaterList;

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public List<McBtsRepeater> getMcBtsRepeaterList() {
		return mcBtsRepeaterList;
	}

	public void setMcBtsRepeaterList(List<McBtsRepeater> mcBtsRepeaterList) {
		this.mcBtsRepeaterList = mcBtsRepeaterList;
	}

}
