/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer3;

import java.util.List;

/**
 * tos实体包装类，使用genericBizData
 * @author chenshaohua
 * 
 */
public class WrappedMcBtsTos {

	// tos配置列表
	private List<McBtsTos> mcBtsTosList;

	public List<McBtsTos> getMcBtsTosList() {
		return mcBtsTosList;
	}

	public void setMcBtsTosList(List<McBtsTos> mcBtsTosList) {
		this.mcBtsTosList = mcBtsTosList;
	}

}
