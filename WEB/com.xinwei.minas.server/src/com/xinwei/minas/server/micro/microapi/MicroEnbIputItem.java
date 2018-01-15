package com.xinwei.minas.server.micro.microapi;


/**
 * 
 * 网管调用api获取配置信息的输入接口
 * 
 * @author zhoujie
 * 
 */
public class MicroEnbIputItem
{
	public int id;//配置项目
	public int value;//配置值
	
	public MicroEnbIputItem(int id, int value) {
		this.id = id;		
		this.value = value;		
	}		
}
