/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-3	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.oamManage;

import java.util.List;

/**
 * 
 * 同步配置service
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsSynConfigService {

	public static final int NEED_RESTUDY = 1;

	public static final int NOT_NEED_RESTUDY = 0;

	/**
	 * 同步配置(将网管数据下发给网元)
	 * 
	 * @param restudy
	 *            是否需要自学习, 0-不需求, 1-需要
	 * @param moId
	 *            网元Id
	 * @return 失败的业务名称列表(对应common-ui.xml中的ui desc)
	 * @throws Exception
	 */
	public List<String> config(Integer restudy, Long moId) throws Exception;

	/**
	 * 从基站同步数据到EMS
	 * 
	 * @param moId
	 * @return 失败的业务名称列表(对应common-ui.xml中的ui desc)
	 * @throws Exception
	 */
	public List<String> syncFromNEToEMS(Long moId) throws Exception;

}
