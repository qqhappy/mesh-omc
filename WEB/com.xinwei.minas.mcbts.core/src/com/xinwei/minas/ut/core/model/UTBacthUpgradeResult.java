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
 * 类简要描述
 * 
 * <p>
 * 类详细描述:终端批量升级结果的实体
 * </p> 
 * 
 * @author qiwei
 * 
 */

public class UTBacthUpgradeResult implements Serializable {
private Long idx;
//终端pid
private String pid;
//终端所在btsId
private Long btsId;
//升级时间
private Date upgradeTime;
//终端类型
private String utType;
//终端升级目标版本
private String utTargetVersion;
//终端升级结果   1--成功  -1--失败  0--正在升级中
private Integer utUpgradeFlag;
//升级进度
private String utUpgradeProgress;
//终端升级结果描述
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
