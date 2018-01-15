/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-29	| jiayi 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import com.xinwei.minas.mcbts.core.model.sysManage.ChannelComparableMode;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * ��վ����ģʽ���÷���ӿ�
 * 
 * @author jiayi
 * 
 */

public interface ChannelComparableModeService extends ICustomService{

	/**
	 * ����ָ����վ�Ļ�վ����ģʽ
	 * 
	 * @param moId ��վ��MO Id
	 * 
	 * @param config ��վ����ģʽ��Ϣ
	 * 
	 * @throws Exception
	 */
	public void config(long moId, ChannelComparableMode config)
			throws Exception;

	/**
	 * ����ϵͳ�Ļ�վ����ģʽ
	 * 
	 * @param config ��վ����ģʽ��Ϣ
	 * 
	 * @throws Exception
	 */
	public void config(ChannelComparableMode config) throws Exception;

	/**
	 * ���������ݿ��ѯ����ģʽ������Ϣ
	 * 
	 * @return ChannelComparableMode ��վ����ģʽ��Ϣ
	 * 
	 * @throws Exception
	 */
	public ChannelComparableMode queryFromEMS() throws Exception;

	/**
	 * �ӻ�վ��ѯ����ģʽ������Ϣ
	 * 
	 * @param moId ��վ��MO Id
	 * 
	 * @return ChannelComparableMode ��վ����ģʽ��Ϣ
	 * 
	 * @throws Exception
	 */
	public ChannelComparableMode queryFromNE(long moId) throws Exception;

}
