package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class McBtsConfig implements Serializable {

	private List<Business> businesses = new LinkedList<Business>();

	public List<Business> getBusinesses() {
		return businesses;
	}

	public void setBusinesses(List<Business> businesses) {
		this.businesses = businesses;
	}

	public Business getBusiness(String bizName) {
		if (bizName == null || bizName.isEmpty())
			return null;
		for (Business business : businesses) {
			if (business.getName().equalsIgnoreCase(bizName))
				return business;
		}
		return null;
	}

	public Business getBusinessByIndex(int index) {
		if (index >= businesses.size())
			return null;

		return businesses.get(index);
	}
}
