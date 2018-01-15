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
 * McBTS��ѧϰ����ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsStudyService {

	/**
	 * ���ݻ�վ���͡���վ�汾�ж��Ƿ�֧��moc����Ĳ���
	 * 
	 * @param btsType
	 *            ��վ����
	 * @param version
	 *            ��վ�汾
	 * @param moc
	 *            ҵ��mocֵ
	 * @return
	 */
	public boolean isSupportedOperation(int btsType, String version, int moc);

	/**
	 * ����ָ����Ԫ��Moc�Ĳ����Ƿ�֧�ֵı�ʶ
	 * 
	 * @param moId
	 * @param moc
	 * @param supported
	 */
	public void addUnsupportedResult(int btsType, String version, int moc,
			Integer value);

	/**
	 * ����btsType������ݿ�
	 * 
	 * @param moId
	 */
	public void clearSupportedOperation(int btsType, String version);

}
