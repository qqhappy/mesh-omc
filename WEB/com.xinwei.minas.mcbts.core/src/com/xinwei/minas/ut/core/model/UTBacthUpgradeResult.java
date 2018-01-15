/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.model;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����:�ն��������������ʵ��
 * </p> 
 * 
 * @author qiwei
 * 
 */

public class UTBacthUpgradeResult implements Serializable {
private Long idx;
//�ն�pid
private String pid;
//�ն�����btsId
private Long btsId;
//����ʱ��
private Date upgradeTime;
//�ն�����
private String utType;
//�ն�����Ŀ��汾
private String utTargetVersion;
//�ն��������   1--�ɹ�  -1--ʧ��  0--����������
private Integer utUpgradeFlag;
//��������
private String utUpgradeProgress;
//�ն������������
private String utUpgradeDesc;
public UTBacthUpgradeResult() {
	super();
	// TODO Auto-generated constructor stub
}

public Long getBtsId() {
	return btsId;
}

public void setBtsId(Long btsId) {
	this.btsId = btsId;
}

public Long getIdx() {
	return idx;
}

public void setIdx(Long idx) {
	this.idx = idx;
}

public String getPid() {
	return pid;
}
public void setPid(String pid) {
	this.pid = pid;
}
public Date getUpgradeTime() {
	return upgradeTime;
}
public void setUpgradeTime(Date upgradeTime) {
	this.upgradeTime = upgradeTime;
}
public String getUtType() {
	return utType;
}
public void setUtType(String utType) {
	this.utType = utType;
}
public String getUtTargetVersion() {
	return utTargetVersion;
}
public void setUtTargetVersion(String utTargetVersion) {
	this.utTargetVersion = utTargetVersion;
}
public Integer getUtUpgradeFlag() {
	return utUpgradeFlag;
}
public void setUtUpgradeFlag(Integer utUpgradeFlag) {
	this.utUpgradeFlag = utUpgradeFlag;
}
public String getUtUpgradeDesc() {
	return utUpgradeDesc;
}
public void setUtUpgradeDesc(String utUpgradeDesc) {
	this.utUpgradeDesc = utUpgradeDesc;
}

public String getUtUpgradeProgress() {
	return utUpgradeProgress;
}

public void setUtUpgradeProgress(String utUpgradeProgress) {
	this.utUpgradeProgress = utUpgradeProgress;
}

}
