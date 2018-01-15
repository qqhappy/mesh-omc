package com.xinwei.lte.web.enb.model;

/**
 * eNB统计项菜单模型
 * 
 * @author zhangqiang
 * 
 */
public class EnbRealtimeItemConfigModel {

	/**
	 * 菜单节点Id
	 */
	private String id = "";

	/**
	 * 菜单节点父Id
	 */
	private String pId = "";

	/**
	 * 菜单节点名
	 */
	private String name = "";

	/**
	 * 菜单节点链接目标
	 */
	private String target = "";

	/**
	 * 菜单节点是否有复选框
	 */
	private boolean nocheck = false;

	/**
	 * 菜单节点是否展开
	 */
	private boolean open = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNocheck() {
		return nocheck;
	}

	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
