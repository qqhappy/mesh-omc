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
 * ��վģ���ģ��
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsTemplate implements Serializable {
	// Ĭ��,�ʺ�v5��վ��ģ��
	public static final Long TEMPLATE_ID_V5 = -200L;
	// �ʺ�΢���ѻ�վ(��С��վ)��ģ��
	public static final Long TEMPLATE_ID_MICRO_BEEHIVE = -300L;
	// �ʺϹ��˻�վ��ģ��
	public static final Long TEMPLATE_ID_FDDI = -400L;
	// ����վ����Ϊv3v5ʱ,��airlink��ʹ�õ�ģ��
	public static final Long TEMPLATE_ID_R3R5_AIRLINK = -500L;

	public static final Long TEMPLATE_ID_DEFAULT_AIRLINK_SUBW0 = -201L;

	// ����-200xxΪV5��վ�İ汾,ֻ�ʺ�V5��վ
	// ����-300xxΪ΢�����������İ汾,ֻ�ʺ�΢���ѻ�վ
	// ����-400xxΪ������Զ��վ�������İ汾,ֻ�ʺϹ�����Զ��վ
	// ����-500xxΪv3v5��վ��airlink�����õ������汾
	// ����-201xxΪ�������ͻ�վ��airlink_subw0�����õ������汾
	// ����-200xx0Ϊ����ͨ�û�վ
	// ����-300xx0Ϊ����΢���ѻ�վ
	// ����-400xx0Ϊ���ݹ�����Զ��վ

	// moId,��templateId
	private Long moId;
	// ��ʾ��ҳ���ϵ�ģ�������
	private String name;
	// �������ݵ�ID
	private Long backupId;
	// �ο���ģ��ID
	private Long referredTemplateId;
	// �ο���ģ������
	private String referredTemplateName;
	// ��ӵ�ʱ��
	private Date addedTime;
	// ����޸ĵ�ʱ��
	private Date lastModifiedTime;
	// ����ҵ����б�
	private List<String> operations;

	public Long getMoId() {
		return moId;
	}

	public void setMoId(Long moId) {
		this.moId = moId;
	}

	// ����btsType�ж�moId
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
