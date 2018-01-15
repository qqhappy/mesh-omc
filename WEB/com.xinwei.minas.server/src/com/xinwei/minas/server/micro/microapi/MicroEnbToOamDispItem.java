package com.xinwei.minas.server.micro.microapi;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * 每个约束条件的结构类定义
 * 
 * @author zhoujie
 * 
 */
public class MicroEnbToOamDispItem{
	private int id;//每个显示项ID号
	private String Name; //显示的字段名
    
	private List<MicroOptionDataInfo> opvalue;
	
	public MicroEnbToOamDispItem(int id, String Name) {
		this.id = id;		
		this.Name = Name;		
		opvalue = new ArrayList<MicroOptionDataInfo>();//选项的list集合
	}	
	
	public void AddOptData(MicroOptionDataInfo opdi)
	{		
		opvalue.add(opdi);
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return Name;
	}
	
	public void getOptionDataInfo() {
		
		for(int i=0; i<opvalue.size(); i++)
		{
			MicroOptionDataInfo op = opvalue.get(i);
			int value = op.getValue();
			String Name = op.getName();

			System.out.println("--Value:" + value +"\t --"+ "Name:"+ Name);	
		}

	}
}

