package com.xinwei.minas.server.enb.enbapi;

/**
 * 
 * Լ��������ѡ��ṹ����
 * 
 * @author zhoujie
 * 
 */
public class OptionDataInfo {
	private int value;//ÿ����ʾ���Ӧ��ö��ֵ
	private String Name; //��ʾ���ֶ���
	
	public OptionDataInfo(int value, String Name)
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
