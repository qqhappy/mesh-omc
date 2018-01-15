package com.xinwei.minas.server.enb.enbapi;

import java.util.ArrayList;

/**
 * 
 * 网管调用api获取配置信息的输入接口
 * 
 * @author zhoujie
 * 
 */
public class EnbIputItem
{
	public int id;//配置项目
	public int value;//配置值
	
	public EnbIputItem(int id, int value) {
		this.id = id;		
		this.value = value;		
	}		
}
