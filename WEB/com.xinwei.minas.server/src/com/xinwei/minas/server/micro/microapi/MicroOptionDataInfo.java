package com.xinwei.minas.server.micro.microapi;

/**
 * 
 * 约束条件下选项结构定义
 * 
 * @author zhoujie
 * 
 */
public class MicroOptionDataInfo {
	private int value;//每个显示项对应的枚举值
	private String Name; //显示的字段名
	
	public MicroOptionDataInfo(int value, String Name)
	{
		this.value = value;
		this.Name = Name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return Name;
	}
}
