package com.xinwei.minas.enb.core.model;

import java.util.Date;
import java.util.List;

import com.xinwei.omp.core.model.biz.PagingCondition;

public class EnbBlackBoxFileCondition extends PagingCondition{

	//��ʼʱ��
	private String begindate ;
	//����ʱ��
	private String enddate ;
	//enbid
	private String enbids;
	//��λԭ��
	private String resetReason;
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getEnbids() {
		return enbids;
	}
	public void setEnbids(String enbids) {
		this.enbids = enbids;
	}
	public String getResetReason() {
		return resetReason;
	}
	public void setResetReason(String resetReason) {
		this.resetReason = resetReason;
	}
}
