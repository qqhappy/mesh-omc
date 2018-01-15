/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer3;

import java.util.List;

/**
 * @author chenshaohua
 * 
 */
public class WrappedVlan {

	// vlan≈‰÷√¡–±Ì
	private List<McBtsVlan> mcBtsVlanList;

	private McBtsVlanAttach vlanAttach;

	public List<McBtsVlan> getMcBtsVlanList() {
		return mcBtsVlanList;
	}

	public void setMcBtsVlanList(List<McBtsVlan> mcBtsVlanList) {
		this.mcBtsVlanList = mcBtsVlanList;
	}

	public McBtsVlanAttach getVlanAttach() {
		return vlanAttach;
	}

	public void setVlanAttach(McBtsVlanAttach vlanAttach) {
		this.vlanAttach = vlanAttach;
	}

}
