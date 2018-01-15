/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-28	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.facade.conf.MoBasicFacade;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.server.core.conf.service.MoBasicService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * MO��������ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class MoBasicFacadeImpl extends UnicastRemoteObject implements
		MoBasicFacade {

	public MoBasicFacadeImpl() throws RemoteException {
		super();
	}

	/**
	 * ��������MO
	 * 
	 * @return
	 */
	public List<Mo> queryAll() throws Exception {
		MoBasicService service = getService();
		return service.queryAll();
	}

	/**
	 * ��ȡ�ƶ����͵�MO
	 * 
	 * @param moTypeId
	 *            ����
	 * @return
	 */
	public List<Mo> queryByType(int moTypeId) throws Exception {
		MoBasicService service = getService();
		return service.queryByType(moTypeId);

	}

	/**
	 * ����MO
	 * 
	 * @param mo
	 *            ���ܶ���
	 * @param regionId
	 *            �����������
	 */
	public void add(Mo mo, long regionId) throws Exception {
		MoBasicService service = getService();
		service.add(mo, regionId);
	}

	/**
	 * �޸�MO
	 * 
	 * @param mo
	 */
	public void modify(Mo mo) throws Exception {
		MoBasicService service = getService();
		service.modify(mo);
	}

	/**
	 * ɾ��ָ��ID��MO
	 * 
	 * @param moId
	 */
	public void delete(Long moId) throws Exception {
		MoBasicService service = getService();
		service.delete(moId);
	}

	/**
	 * �޸�MO����״̬
	 * 
	 * @param mo
	 */
	public void changeManageState(Mo mo) throws Exception {
		MoBasicService service = getService();
		service.changeManageState(mo);
	}

	private MoBasicService getService() {
		return AppContext.getCtx().getBean(MoBasicService.class);
	}

	/**
	 * ��ȡָ��moId�� MO
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public Mo queryByMoId(Long moId) throws Exception {
		// TODO Auto-generated method stub
		MoBasicService service = getService();
		return service.queryByMoId(moId);
	}

}
