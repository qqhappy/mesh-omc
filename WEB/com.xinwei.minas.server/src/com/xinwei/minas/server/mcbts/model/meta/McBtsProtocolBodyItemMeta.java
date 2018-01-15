/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.meta;

/**
 * 
 * McBtsЭ�� ��Ϣ������  Ԫ����  
 * 
 * @author chenjunhua
 * 
 */

public class McBtsProtocolBodyItemMeta {
	
	// ��������
	private String name;
	
	// ��������
	private String type;
	
	// ���Գ���
	private String length;
	
	// change by yinbinqiang ��ӹ��캯��
	public McBtsProtocolBodyItemMeta() {
		
	}
	// change by yinbinqiang ��ӹ��캯��
	public McBtsProtocolBodyItemMeta(String name, String type, String length) {
		this.name = name;
		this.type = type;
		this.length = length;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}
	
	
}
