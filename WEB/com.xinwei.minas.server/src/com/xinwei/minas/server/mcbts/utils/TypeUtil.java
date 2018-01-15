/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-27	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.utils;

/**
 * 
 * �ն˰汾��Type���͹��߼�,ҵ��Ҳ�ʺϻ�վ�汾,δ����
 * <p>
 * �ն�������Ӳ�����ͺ�������͹���. ��:112800���ն�����,128ΪӲ������,00Ϊ�������
 * </p>
 * 
 * 
 * @author tiance
 * 
 */

public class TypeUtil {
	/**
	 * Ӳ������
	 * 
	 * @param typeId
	 * @return
	 */
	public static int getModulType(int typeId) {
		return typeId / 100 - 1000;
	}

	/**
	 * �������
	 * 
	 * @param typeId
	 * @return
	 */
	public static int getSoftwareType(int typeId) {
		return typeId % 100;
	}

	/**
	 * �ն�����
	 * 
	 * @param moduleType
	 * @param softwareType
	 * @return
	 */
	public static int getType(int moduleType, int softwareType) {
		return (moduleType + 1000) * 100 + softwareType;
	}

	public static int interfaceNeTypeToEms(int type) {
		switch (type) {
		case 0:
			return 2;
		case 1:
			return 3;
		}
		return type;
	}
}
