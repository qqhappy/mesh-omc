package com.xinwei.minas.server.enb.enbapi;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * 每个约束条件的结构类定义
 * 
 * @author zhoujie
 * 
 */
public class EnbToOamDispItem{
	private int id;//每个显示项ID号
	private String Name; //显示的字段名
    
	private List<OptionDataInfo> opvalue;
	
	public EnbToOamDispItem(int id, String Name) {
		this.id = id;		
		this.Name = Name;		
		opvalue = new ArrayList<OptionDataInfo>();//选项的list集合
	}	
	
	public void AddOptData(OptionDataInfo opdi)
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
			OptionDataInfo op = opvalue.get(i);
			int value = op.getValue();
			String Name = op.getName();

			System.out.println("--Value:" + value +"\t --"+ "Name:"+ Name);	
		}

	}
}

