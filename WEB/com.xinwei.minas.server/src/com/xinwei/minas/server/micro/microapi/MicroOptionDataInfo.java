package com.xinwei.minas.server.micro.microapi;

/**
 * 
 * Լ��������ѡ��ṹ����
 * 
 * @author zhoujie
 * 
 */
public class MicroOptionDataInfo {
	private int value;//ÿ����ʾ���Ӧ��ö��ֵ
	private String Name; //��ʾ���ֶ���
	
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
