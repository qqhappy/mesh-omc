package com.xinwei.minas.server.enb.enbapi;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * ÿ��Լ�������Ľṹ�ඨ��
 * 
 * @author zhoujie
 * 
 */
public class EnbToOamDispItem{
	private int id;//ÿ����ʾ��ID��
	private String Name; //��ʾ���ֶ���
    
	private List<OptionDataInfo> opvalue;
	
	public EnbToOamDispItem(int id, String Name) {
		this.id = id;		
		this.Name = Name;		
		opvalue = new ArrayList<OptionDataInfo>();//ѡ���list����
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

