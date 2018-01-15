/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-5	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;

/**
 * 
 * 基站模板的模型
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsTemplate implements Serializable {
	// 默认,适合v5基站的模板
	public static final Long TEMPLATE_ID_V5 = -200L;
	// 适合微蜂窝基站(即小基站)的模板
	public static final Long TEMPLATE_ID_MICRO_BEEHIVE = -300L;
	// 适合光纤基站的模板
	public static final Long TEMPLATE_ID_FDDI = -400L;
	// 当基站类型为v3v5时,在airlink中使用的模板
	public static final Long TEMPLATE_ID_R3R5_AIRLINK = -500L;

	public static final Long TEMPLATE_ID_DEFAULT_AIRLINK_SUBW0 = -201L;

	// 定义-200xx为V5基站的版本,只适合V5基站
	// 定义-300xx为微蜂窝衍生出的版本,只适合微蜂窝基站
	// 定义-400xx为光纤拉远基站衍生出的版本,只适合光纤拉远基站
	// 定义-500xx为v3v5基站的airlink的配置的衍生版本
	// 定义-201xx为其它类型基站的airlink_subw0的配置的衍生版本
	// 定义-200xx0为备份通用基站
	// 定义-300xx0为备份微蜂窝基站
	// 定义-400xx0为备份光纤拉远基站

	// moId,即templateId
	private Long moId;
	// 显示在页面上的模板的名称
	private String name;
	// 备份数据的ID
	private Long backupId;
	// 参考的模板ID
	private Long referredTemplateId;
	// 参考的模板名称
	private String referredTemplateName;
	// 添加的时间
	private Date addedTime;
	// 最后修改的时间
	private Date lastModifiedTime;
	// 保存业务的列表
	private List<String> operations;

	public Long getMoId() {
		return moId;
	}

	public void setMoId(Long moId) {
		this.moId = moId;
	}

	// 基于btsType判断moId
	public void setMoIdByType(Integer btsType) {
		if (moId == null) {
			if (btsType.intValue() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
				moId = TEMPLATE_ID_MICRO_BEEHIVE;
			} else if (btsType.intValue() == 30) {
				moId = TEMPLATE_ID_FDDI;
			} else {
				moId = TEMPLATE_ID_V5;
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getBackupId() {
		return backupId;
	}

	public void setBackupId(Long backupId) {
		this.backupId = backupId;
	}

	public Long getReferredTemplateId() {
		return referredTemplateId;
	}

	public void setReferredTemplateId(Long referredTemplateId) {
		this.referredTemplateId = referredTemplateId;
	}

	public String getReferredTemplateName() {
		return referredTemplateName;
	}

	public void setReferredTemplateName(String referredTemplateName) {
		this.referredTemplateName = referredTemplateName;
	}

	public Date getAddedTime() {
		return addedTime;
	}

	public void setAddedTime(Date addedTime) {
		this.addedTime = addedTime;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public List<String> getOperations() {
		return operations;
	}

	public void setOperations(List<String> operations) {
		this.operations = operations;
	}

}
