/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-12	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.model.FullTableConfigInfo;

/**
 * 
 * ��������ҵ������ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p>
 * 
 * @author zhuxiaozhan
 * 
 */
public interface EnbFullTableConfigFacade extends Remote {
	/**
	 * ������������
	 * 
	 * @param moId
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Long moId) throws Exception;

	/**
	 * ɾ��ָ��������������Ϣ
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void delete(FullTableConfigInfo data) throws Exception;

	/**
	 * ��ѯָ��eNB������������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public FullTableConfigInfo queryByMoId(Long moId) throws Exception;

	/**
	 * ��ѯ���е�����������Ϣ
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<FullTableConfigInfo> queryAll() throws Exception;

	/**
	 * ��������״̬��ѯ����������Ϣ
	 * 
	 * @param status
	 *            ���ɹ���ʧ�ܡ�������
	 * @return
	 * @throws Exception
	 */
	public List<FullTableConfigInfo> queryByStatus(int status) throws Exception;
}
