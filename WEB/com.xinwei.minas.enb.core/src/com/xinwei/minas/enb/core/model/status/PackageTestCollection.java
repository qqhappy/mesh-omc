package com.xinwei.minas.enb.core.model.status;

import java.util.LinkedList;
import java.util.List;

import com.xinwei.minas.enb.core.model.EnbDynamicInfo;

public class PackageTestCollection implements EnbDynamicInfo{
	List<PacketLossRateModel> packageTestCollection = new LinkedList<PacketLossRateModel>();

	
	public List<PacketLossRateModel> getPackageTestCollection() {
		return packageTestCollection;
	}


	public void setPackageTestCollection(
			List<PacketLossRateModel> packageTestCollection) {
		this.packageTestCollection = packageTestCollection;
	}


	public void addPacketLossRateModel(PacketLossRateModel packetLossRateModel) {
		packageTestCollection.add(packetLossRateModel);
	}
}
