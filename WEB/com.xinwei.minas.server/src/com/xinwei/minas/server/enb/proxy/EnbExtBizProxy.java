/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.proxy;

/**
 * 
 * eNB��չҵ�����ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbExtBizProxy {

	/**
	 * ����ָ��eNB
	 * 
	 * @param enbId
	 * @throws Exception
	 */
	public void reset(long enbId) throws Exception;

	/**
	 * ����ָ��eNB��ָ������
	 * 
	 * @param enbId
	 * @param rackId
	 * @param shelfId
	 * @param boardId
	 * @throws Exception
	 */
	public void reset(long enbId, int rackId, int shelfId, int boardId)
			throws Exception;

	/**
	 * ѧϰeNB�������ã�����֧����Щ��ͱ��е���Щ�ֶ�
	 * 
	 * @param enbId
	 * @return
	 * @throws Exception
	 */
	public String studyEnbDataConfig(long enbId) throws Exception;

}
