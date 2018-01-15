/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.simplenms;

import java.io.Serializable;

/**
 * 
 * ���ڻ�վ�û���Ϣ
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class McBtsGroupUserInfo implements Serializable {

	// �û��������ID
	private Long groupId;
	
	// �û�ID
	private Long uid;
	
	// �������ȼ�
	private Integer priorInGroup;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Integer getPriorInGroup() {
		return priorInGroup;
	}

	public void setPriorInGroup(Integer priorInGroup) {
		this.priorInGroup = priorInGroup;
	}
}