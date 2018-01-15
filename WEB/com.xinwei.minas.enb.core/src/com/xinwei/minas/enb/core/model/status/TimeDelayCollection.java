package com.xinwei.minas.enb.core.model.status;

import java.util.LinkedList;
import java.util.List;

import com.xinwei.minas.enb.core.model.EnbDynamicInfo;

public class TimeDelayCollection implements EnbDynamicInfo{
	List<TimeDelayModel> timeDelayModellist = new LinkedList<TimeDelayModel>();

	public List<TimeDelayModel> getTimeDelayModellist() {
		return timeDelayModellist;
	}

	public void setTimeDelayModellist(List<TimeDelayModel> timeDelayModellist) {
		this.timeDelayModellist = timeDelayModellist;
	}
	public void addTimeDelayModel(TimeDelayModel timeDelayModel) {
		timeDelayModellist.add(timeDelayModel);
	}
}
