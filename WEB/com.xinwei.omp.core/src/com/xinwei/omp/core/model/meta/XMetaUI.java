/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-13	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

import java.util.ResourceBundle;

/**
 * 
 * ��Ԫҵ��Ԫ����
 * 
 * @author chenjunhua
 * 
 */

public class XMetaUI {

	// ҵ������(Ĭ�϶��ڱ���)
	private String name;

	// ҵ������
	private String desc;

	// �û��Զ���ҵ�����
	private XMetaPanel panel;

	// �Զ��������ɵ�������
	private XMetaShell shell;

	// ��ҵ�񼯺�
	private XMetaBiz[] biz = new XMetaBiz[0];

	// moc����
	private XMetaMocs mocs;

	// UI��Ӧ��ҵ������
	private String operName;

	/**
	 * ���ʻ���Դ
	 * 
	 * @param resourceBundle
	 */
	public void i18n(ResourceBundle resourceBundle) {
		desc = XMetaUtils.replaceResource(desc, resourceBundle);
		if (biz != null) {
			for (XMetaBiz metaBiz : biz) {
				metaBiz.i18n(resourceBundle);
			}
		}
	}

	/**
	 * ��ȡָ��ҵ�����Ƶ�ҵ��Ԫ����
	 * 
	 * @param bizName
	 * @return
	 */
	public XMetaBiz getMetaBizBy(String bizName) {
		for (XMetaBiz metaBiz : biz) {
			if (metaBiz.getName().equals(bizName)) {
				return metaBiz;
			}
		}
		return null;
	}

	public XMetaBiz[] getBiz() {
		return biz;
	}

	public void setBiz(XMetaBiz[] biz) {
		this.biz = biz;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public XMetaPanel getPanel() {
		return panel;
	}

	public void setPanel(XMetaPanel panel) {
		this.panel = panel;
	}

	public XMetaShell getShell() {
		return shell;
	}

	public void setShell(XMetaShell shell) {
		this.shell = shell;
	}

	public XMetaMocs getMocs() {
		return mocs;
	}

	public void setMocs(XMetaMocs mocs) {
		this.mocs = mocs;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getOperName() {
		return operName;
	}
}
