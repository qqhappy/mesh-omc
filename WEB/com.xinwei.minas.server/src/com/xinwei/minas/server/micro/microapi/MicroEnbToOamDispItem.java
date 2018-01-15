package com.xinwei.minas.server.micro.microapi;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * ÿ��Լ�������Ľṹ�ඨ��
 * 
 * @author zhoujie
 * 
 */
public class MicroEnbToOamDispItem{
	private int id;//ÿ����ʾ��ID��
	private String Name; //��ʾ���ֶ���
    
	private List<MicroOptionDataInfo> opvalue;
	
	public MicroEnbToOamDispItem(int id, String Name) {
		this.id = id;		
		this.Name = Name;		
		opvalue = new ArrayList<MicroOptionDataInfo>();//ѡ���list����
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

