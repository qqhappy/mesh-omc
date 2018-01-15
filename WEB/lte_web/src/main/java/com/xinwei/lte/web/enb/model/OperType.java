package com.xinwei.lte.web.enb.model;

/**
 * 执行的操作类型
 * 
 * @author zhangqiang
 *
 */
public class OperType {
	/**
	 * 查询
	 */
	public static final String SELECT = "select";
	
	/**
	 * 新增
	 */
	public static final String ADD = "add";
	
	/**
	 * 配置
	 */
	public static final String CONFIG = "config";
	
	/**
	 * 删除
	 */
	public static final String DELETE = "delete";
	
	/**
	 * 批量删除
	 */
	public static final String MULTIDELETE = "multiDelete";
}
