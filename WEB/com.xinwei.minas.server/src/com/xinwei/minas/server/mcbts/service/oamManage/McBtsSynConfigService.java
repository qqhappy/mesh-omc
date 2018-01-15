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
 * ͬ������service
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsSynConfigService {

	public static final int NEED_RESTUDY = 1;

	public static final int NOT_NEED_RESTUDY = 0;

	/**
	 * ͬ������(�����������·�����Ԫ)
	 * 
	 * @param restudy
	 *            �Ƿ���Ҫ��ѧϰ, 0-������, 1-��Ҫ
	 * @param moId
	 *            ��ԪId
	 * @return ʧ�ܵ�ҵ�������б�(��Ӧcommon-ui.xml�е�ui desc)
	 * @throws Exception
	 */
	public List<String> config(Integer restudy, Long moId) throws Exception;

	/**
	 * �ӻ�վͬ�����ݵ�EMS
	 * 
	 * @param moId
	 * @return ʧ�ܵ�ҵ�������б�(��Ӧcommon-ui.xml�е�ui desc)
	 * @throws Exception
	 */
	public List<String> syncFromNEToEMS(Long moId) throws Exception;

}
