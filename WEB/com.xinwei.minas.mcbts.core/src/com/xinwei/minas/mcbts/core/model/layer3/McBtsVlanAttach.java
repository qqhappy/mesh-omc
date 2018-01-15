/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-21	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer3;

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

public class McBtsVlanAttach implements Serializable {
	// ��¼���
	private Long idx;
	
	// ���ܶ���Id
	private Long moId;
	
	// �Ƿ�����qinq tag 1:���� 0������
	private Integer enableQinq;
	
	// etype
	private String etype;

	public McBtsVlanAttach(){
		
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

	public Integer getEnableQinq() {
		return enableQinq;
	}

	public void setEnableQinq(Integer enableQinq) {
		this.enableQinq = enableQinq;
	}

	public String getEtype() {
		return etype;
	}

	public void setEtype(String etype) {
		this.etype = etype;
	}
}
