/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer3;

import java.util.List;

/**
 * @author chenshaohua
 * 
 */
public class WrappedACL {

	private int index;

	private int totalNum;

	// acl≈‰÷√¡–±Ì
	private List<McBtsACL> mcBtsACLList;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public List<McBtsACL> getMcBtsACLList() {
		return mcBtsACLList;
	}

	public void setMcBtsACLList(List<McBtsACL> mcBtsACLList) {
		this.mcBtsACLList = mcBtsACLList;
	}

}
