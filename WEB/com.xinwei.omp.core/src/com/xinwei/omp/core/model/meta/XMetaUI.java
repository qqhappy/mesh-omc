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
 * 网元业务元数据
 * 
 * @author chenjunhua
 * 
 */

public class XMetaUI {

	// 业务名称(默认对于表名)
	private String name;

	// 业务描述
	private String desc;

	// 用户自定义业务面板
	private XMetaPanel panel;

	// 自动界面生成的外壳面板
	private XMetaShell shell;

	// 子业务集合
	private XMetaBiz[] biz = new XMetaBiz[0];

	// moc集合
	private XMetaMocs mocs;

	// UI对应的业务名称
	private String operName;

	/**
	 * 国际化资源
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
	 * 获取指定业务名称的业务元数据
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
