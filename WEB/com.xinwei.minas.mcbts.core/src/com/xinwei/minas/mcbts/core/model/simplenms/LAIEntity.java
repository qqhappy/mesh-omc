package com.xinwei.minas.mcbts.core.model.simplenms;

import java.io.Serializable;

public class LAIEntity implements Serializable{
	private long lai;
	private String laiName;
	public long getLai() {
		return lai;
	}
	public void setLai(long lai) {
		this.lai = lai;
	}
	public String getLaiName() {
		return laiName;
	}
	public void setLaiName(String laiName) {
		this.laiName = laiName;
	}

}
