/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-2	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.oamManage;

import java.io.Serializable;

/**
 * 
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����
 * </p>
 * 
 * @author chenshaohua
 * 
 */

public class McbtsSupportedBiz implements Serializable {

	private Long idx;

	// private Long moId;

	// ��վ����
	private Integer btsType;

	// ����汾
	private String softwareVersion;

	// ҵ��moc
	private int moc;

	// �Ƿ�֧�� 0����֧�� 1��֧��
	private Integer support;

	public McbtsSupportedBiz() {

	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Integer getBtsType() {
		return btsType;
	}

	public void setBtsType(Integer btsType) {
		this.btsType = btsType;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public Integer getSupport() {
		return support;
	}

	public void setSupport(Integer support) {
		this.support = support;
	}

	public int getMoc() {
		return moc;
	}

	public void setMoc(int moc) {
		this.moc = moc;
	}

	// public Long getMoId() {
	// return moId;
	// }
	//
	// public void setMoId(Long moId) {
	// this.moId = moId;
	// }

}
