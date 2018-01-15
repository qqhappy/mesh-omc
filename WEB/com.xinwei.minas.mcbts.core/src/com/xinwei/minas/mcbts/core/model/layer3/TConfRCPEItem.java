package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

/**
 * rcpe uid ��Ŀ��Ϣ
 *
 * @author yinbinqiang
 *
 */
public class TConfRCPEItem implements Serializable {
	// uid ��Ŀ��Ϣ����
	private TConfRCPEItemPK id;
	// rcpe uidֵ
	private Long rCPEUid;
	// rcpe uid ����
	private Integer UIDType;
	
	public TConfRCPEItem() {
		
	}
	
	public TConfRCPEItem(TConfRCPEItemPK id, Long rCPEUid, Integer uIDType) {
		super();
		this.id = id;
		this.rCPEUid = rCPEUid;
		UIDType = uIDType;
	}

	public TConfRCPEItemPK getId() {
		return id;
	}

	public void setId(TConfRCPEItemPK id) {
		this.id = id;
	}

	public Long getrCPEUid() {
		return rCPEUid;
	}

	public void setrCPEUid(Long rCPEUid) {
		this.rCPEUid = rCPEUid;
	}

	public Integer getUIDType() {
		return UIDType;
	}

	public void setUIDType(Integer uIDType) {
		UIDType = uIDType;
	}
}
