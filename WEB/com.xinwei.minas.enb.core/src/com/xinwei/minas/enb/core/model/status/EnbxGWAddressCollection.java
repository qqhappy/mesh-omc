package com.xinwei.minas.enb.core.model.status;

import java.util.LinkedList;
import java.util.List;

import com.xinwei.minas.enb.core.model.EnbDynamicInfo;

public class EnbxGWAddressCollection implements EnbDynamicInfo {
	List<EnbxGWAddress> enbxGWAddresslist = new LinkedList<EnbxGWAddress>();

	public List<EnbxGWAddress> getEnbxGWAddresslist() {
		return enbxGWAddresslist;
	}

	public void setEnbxGWAddresslist(List<EnbxGWAddress> enbxGWAddresslist) {
		this.enbxGWAddresslist = enbxGWAddresslist;
	}

	public void addEnbxGWAddress(EnbxGWAddress enbxGWAddress) {
		enbxGWAddresslist.add(enbxGWAddress);
	}

}
