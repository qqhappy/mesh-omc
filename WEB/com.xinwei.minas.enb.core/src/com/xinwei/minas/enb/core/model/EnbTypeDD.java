/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2015-1-26	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.minas.enb.core.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * 
 * McLTE��վ���������ֵ�
 * 
 * @author chenjunhua
 * 
 */
public class EnbTypeDD implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1896925281553926297L;

	/**
	 * 0: McLTE���վ
	 */
	public static final int XW7400 = 0;
	
	/**
	 * 200��McLTEһ�廯��վ
	 */
	public static final int XW7102 = 200;
	
	// ��ǰ֧�ֵ�eNB��վ����ӳ���
	private static final Map<Integer, String> typeMap = new HashMap();
	
	static {
		typeMap.put(XW7400, "XW7400");
		typeMap.put(XW7102, "XW7102");
	}
	

	// eNB��վ����
	private int enbTypeId;

	// eNB��վ��������
	private String enbTypeName;


	public EnbTypeDD() {
		
	}
	
	public EnbTypeDD(int enbTypeId, String enbTypeName) {
		this.setEnbTypeId(enbTypeId);
		this.setEnbTypeName(enbTypeName);
	}
	
	
	public static boolean isXW7400(int enbTypeId) {
		return enbTypeId == XW7400;
	}
	
	
	public static boolean isXW7102(int enbTypeId) {
		return enbTypeId == XW7102;
	}
	
	/**
	 * �ж�ָ����eNB����ID�Ƿ�֧��
	 * @param enbTypeId
	 * @return
	 */
	public static boolean isSupported(int enbTypeId) {		
		return typeMap.containsKey(enbTypeId);
	}
	
	/**
	 * �ж�ָ����eNB���������Ƿ�֧��
	 * @param enbTypeId
	 * @return
	 */
	public static boolean isSupported(String enbTypeName) {
		Collection<String> names = typeMap.values();
		for (String name : names) {
			if (name.equalsIgnoreCase(enbTypeName)) {
				return true;
			}
		}
		return false;
	}

	public static String getTypeNameById(int enbTypeId) {
		String name = typeMap.get(enbTypeId);
		if (name == null) {
			name = "";
		}
		return name;
	}
	
	/**
	 * �����������ƻ�ȡ����ID
	 * @param enbTypeName
	 * @return
	 */
	public static int getTypeIdByName(String enbTypeName) {
		Iterator<Integer> itr = typeMap.keySet().iterator();
		while (itr.hasNext()) {
			try {
				Integer enbTypeId = itr.next();
				String name = typeMap.get(enbTypeId);
				if (name.equalsIgnoreCase(enbTypeName)) {
					return enbTypeId;
				}
			} catch (Exception e) {
			}
		}
		return -1;
	}
	
	/**
	 * ��ȡ֧�ֵ�����ӳ���
	 * @return
	 */
	public static Map<Integer, String> getSupportedTypeMap() {
		return typeMap;
	}
	
	/**
	 * ��ȡ֧�ֵ�����ID�б� 
	 * @return
	 */
	public static Set<Integer> getSupportedTypeIds() {
		return typeMap.keySet();		
	}
	

	public int getEnbTypeId() {
		return enbTypeId;
	}

	public void setEnbTypeId(int enbTypeId) {
		this.enbTypeId = enbTypeId;
	}

	public String getEnbTypeName() {
		return enbTypeName;
	}

	public void setEnbTypeName(String enbTypeName) {
		this.enbTypeName = enbTypeName;
	}
	
	
	
}
