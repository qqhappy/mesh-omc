/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.net;

/**
 * 
 * McBTS自学习服务接口
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsStudyService {

	/**
	 * 根据基站类型、基站版本判断是否支持moc代表的操作
	 * 
	 * @param btsType
	 *            基站类型
	 * @param version
	 *            基站版本
	 * @param moc
	 *            业务moc值
	 * @return
	 */
	public boolean isSupportedOperation(int btsType, String version, int moc);

	/**
	 * 更新指定网元和Moc的操作是否支持的标识
	 * 
	 * @param moId
	 * @param moc
	 * @param supported
	 */
	public void addUnsupportedResult(int btsType, String version, int moc,
			Integer value);

	/**
	 * 根据btsType清除数据库
	 * 
	 * @param moId
	 */
	public void clearSupportedOperation(int btsType, String version);

}
