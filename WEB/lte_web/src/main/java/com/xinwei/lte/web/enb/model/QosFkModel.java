package com.xinwei.lte.web.enb.model;

import java.util.ArrayList;
import java.util.List;

/**
 * QOS参数表外键模型
 */
import com.xinwei.omp.core.model.biz.XBizRecord;

public class QosFkModel {
	
	private List<XBizRecord> macFk = new ArrayList<XBizRecord>();
	
	private List<XBizRecord> pcFk = new ArrayList<XBizRecord>();
	
	private List<XBizRecord> pdcpFk = new ArrayList<XBizRecord>();
	
	private List<XBizRecord> rlcFk = new ArrayList<XBizRecord>();

	public List<XBizRecord> getMacFk() {
		return macFk;
	}

	public void setMacFk(List<XBizRecord> macFk) {
		this.macFk = macFk;
	}

	public List<XBizRecord> getPcFk() {
		return pcFk;
	}

	public void setPcFk(List<XBizRecord> pcFk) {
		this.pcFk = pcFk;
	}

	public List<XBizRecord> getPdcpFk() {
		return pdcpFk;
	}

	public void setPdcpFk(List<XBizRecord> pdcpFk) {
		this.pdcpFk = pdcpFk;
	}

	public List<XBizRecord> getRlcFk() {
		return rlcFk;
	}

	public void setRlcFk(List<XBizRecord> rlcFk) {
		this.rlcFk = rlcFk;
	}
	
	
	
}
