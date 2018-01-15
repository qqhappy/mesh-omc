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
 * 终端版本的Type类型工具集,业务也适合基站版本,未测试
 * <p>
 * 终端类型由硬件类型和软件类型构成. 如:112800是终端类型,128为硬件类型,00为软件类型
 * </p>
 * 
 * 
 * @author tiance
 * 
 */

public class TypeUtil {
	/**
	 * 硬件类型
	 * 
	 * @param typeId
	 * @return
	 */
	public static int getModulType(int typeId) {
		return typeId / 100 - 1000;
	}

	/**
	 * 软件类型
	 * 
	 * @param typeId
	 * @return
	 */
	public static int getSoftwareType(int typeId) {
		return typeId % 100;
	}

	/**
	 * 终端类型
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
