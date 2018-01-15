/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.common;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * ª˘’æ–Ú¡–∫≈ µÃÂ¿‡
 * 
 * @author chenshaohua
 * 
 */

public class McBtsSN implements Serializable {

	private Long idx;

	private Long moId;

	// dsb∞Â
	private String dsbPanel;

	// ª˘¥¯∞Â
	private String bbPanel;

	// ∆µ◊€∞Â
	private String synPanel;

	// …‰∆µ∞Â1
	private String rfPanel1;

	// …‰∆µ∞Â2
	private String rfPanel2;

	// …‰∆µ∞Â3
	private String rfPanel3;

	// …‰∆µ∞Â4
	private String rfPanel4;

	// …‰∆µ∞Â5
	private String rfPanel5;

	// …‰∆µ∞Â6
	private String rfPanel6;

	// …‰∆µ∞Â7
	private String rfPanel7;

	// …‰∆µ∞Â8
	private String rfPanel8;

	//  ±º‰¥¡
	private Date timeStamp;

	public McBtsSN() {

	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Long getMoId() {
		return moId;
	}

	public void setMoId(Long moId) {
		this.moId = moId;
	}

	public String getRfPanel1() {
		return rfPanel1;
	}

	public void setRfPanel1(String rfPanel1) {
		this.rfPanel1 = rfPanel1;
	}

	public String getRfPanel2() {
		return rfPanel2;
	}

	public void setRfPanel2(String rfPanel2) {
		this.rfPanel2 = rfPanel2;
	}

	public String getRfPanel3() {
		return rfPanel3;
	}

	public void setRfPanel3(String rfPanel3) {
		this.rfPanel3 = rfPanel3;
	}

	public String getRfPanel4() {
		return rfPanel4;
	}

	public void setRfPanel4(String rfPanel4) {
		this.rfPanel4 = rfPanel4;
	}

	public String getRfPanel5() {
		return rfPanel5;
	}

	public void setRfPanel5(String rfPanel5) {
		this.rfPanel5 = rfPanel5;
	}

	public String getRfPanel6() {
		return rfPanel6;
	}

	public void setRfPanel6(String rfPanel6) {
		this.rfPanel6 = rfPanel6;
	}

	public String getRfPanel7() {
		return rfPanel7;
	}

	public void setRfPanel7(String rfPanel7) {
		this.rfPanel7 = rfPanel7;
	}

	public String getRfPanel8() {
		return rfPanel8;
	}

	public void setRfPanel8(String rfPanel8) {
		this.rfPanel8 = rfPanel8;
	}

	public String getBbPanel() {
		return bbPanel;
	}

	public void setBbPanel(String bbPanel) {
		this.bbPanel = bbPanel;
	}

	public String getSynPanel() {
		return synPanel;
	}

	public void setSynPanel(String synPanel) {
		this.synPanel = synPanel;
	}

	public String getDsbPanel() {
		return dsbPanel;
	}

	public void setDsbPanel(String dsbPanel) {
		this.dsbPanel = dsbPanel;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bbPanel == null) ? 0 : bbPanel.hashCode());
		result = prime * result
				+ ((dsbPanel == null) ? 0 : dsbPanel.hashCode());
		result = prime * result
				+ ((rfPanel1 == null) ? 0 : rfPanel1.hashCode());
		result = prime * result
				+ ((rfPanel2 == null) ? 0 : rfPanel2.hashCode());
		result = prime * result
				+ ((rfPanel3 == null) ? 0 : rfPanel3.hashCode());
		result = prime * result
				+ ((rfPanel4 == null) ? 0 : rfPanel4.hashCode());
		result = prime * result
				+ ((rfPanel5 == null) ? 0 : rfPanel5.hashCode());
		result = prime * result
				+ ((rfPanel6 == null) ? 0 : rfPanel6.hashCode());
		result = prime * result
				+ ((rfPanel7 == null) ? 0 : rfPanel7.hashCode());
		result = prime * result
				+ ((rfPanel8 == null) ? 0 : rfPanel8.hashCode());
		result = prime * result
				+ ((synPanel == null) ? 0 : synPanel.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		McBtsSN other = (McBtsSN) obj;
		if (bbPanel == null) {
			if (other.bbPanel != null)
				return false;
		} else if (!bbPanel.equals(other.bbPanel))
			return false;
		if (dsbPanel == null) {
			if (other.dsbPanel != null)
				return false;
		} else if (!dsbPanel.equals(other.dsbPanel))
			return false;
		if (rfPanel1 == null) {
			if (other.rfPanel1 != null)
				return false;
		} else if (!rfPanel1.equals(other.rfPanel1))
			return false;
		if (rfPanel2 == null) {
			if (other.rfPanel2 != null)
				return false;
		} else if (!rfPanel2.equals(other.rfPanel2))
			return false;
		if (rfPanel3 == null) {
			if (other.rfPanel3 != null)
				return false;
		} else if (!rfPanel3.equals(other.rfPanel3))
			return false;
		if (rfPanel4 == null) {
			if (other.rfPanel4 != null)
				return false;
		} else if (!rfPanel4.equals(other.rfPanel4))
			return false;
		if (rfPanel5 == null) {
			if (other.rfPanel5 != null)
				return false;
		} else if (!rfPanel5.equals(other.rfPanel5))
			return false;
		if (rfPanel6 == null) {
			if (other.rfPanel6 != null)
				return false;
		} else if (!rfPanel6.equals(other.rfPanel6))
			return false;
		if (rfPanel7 == null) {
			if (other.rfPanel7 != null)
				return false;
		} else if (!rfPanel7.equals(other.rfPanel7))
			return false;
		if (rfPanel8 == null) {
			if (other.rfPanel8 != null)
				return false;
		} else if (!rfPanel8.equals(other.rfPanel8))
			return false;
		if (synPanel == null) {
			if (other.synPanel != null)
				return false;
		} else if (!synPanel.equals(other.synPanel))
			return false;
		return true;
	}

	
}
