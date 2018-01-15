/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-10-22	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.service;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.model.Mo;

/**
 * 
 * ���ܶ����������ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface MoBasicService {
	/**
	 * ��������MO
	 * 
	 * @return
	 */
	public List<Mo> queryAll();

	/**
	 * ��ȡָ�����͵�MO
	 * 
	 * @param moTypeId
	 *            ����
	 * @return
	 */
	public List<Mo> queryByType(int moTypeId);

	/**
	 * ����MO
	 * 
	 * @param mo
	 *            ���ܶ���
	 * @param regionId
	 *            �����������
	 */
	public void add(Mo mo, long regionId);

	/**
	 * �޸�MO
	 * 
	 * @param mo
	 */
	public void modify(Mo mo);

	/**
	 * ɾ��ָ��ID��MO
	 * 
	 * @param moId
	 */
	public void delete(Long moId);
	
	/**
	 * �޸�MO����״̬
	 * 
	 * @param mo
	 */
	public void changeManageState(Mo mo);

	/**
	 * ��ȡָ��moId�� MO
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public Mo queryByMoId(Long moId);
}
