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
 * MO基本门面实现
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
	 * 查找所有MO
	 * 
	 * @return
	 */
	public List<Mo> queryAll() throws Exception {
		MoBasicService service = getService();
		return service.queryAll();
	}

	/**
	 * 获取制定类型的MO
	 * 
	 * @param moTypeId
	 *            类型
	 * @return
	 */
	public List<Mo> queryByType(int moTypeId) throws Exception {
		MoBasicService service = getService();
		return service.queryByType(moTypeId);

	}

	/**
	 * 增加MO
	 * 
	 * @param mo
	 *            被管对象
	 * @param regionId
	 *            归属地区编号
	 */
	public void add(Mo mo, long regionId) throws Exception {
		MoBasicService service = getService();
		service.add(mo, regionId);
	}

	/**
	 * 修改MO
	 * 
	 * @param mo
	 */
	public void modify(Mo mo) throws Exception {
		MoBasicService service = getService();
		service.modify(mo);
	}

	/**
	 * 删除指定ID的MO
	 * 
	 * @param moId
	 */
	public void delete(Long moId) throws Exception {
		MoBasicService service = getService();
		service.delete(moId);
	}

	/**
	 * 修改MO管理状态
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
	 * 获取指定moId的 MO
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
