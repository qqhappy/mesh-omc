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
 * 被管对象基本服务接口
 * 
 * @author chenjunhua
 * 
 */

public interface MoBasicService {
	/**
	 * 查找所有MO
	 * 
	 * @return
	 */
	public List<Mo> queryAll();

	/**
	 * 获取指定类型的MO
	 * 
	 * @param moTypeId
	 *            类型
	 * @return
	 */
	public List<Mo> queryByType(int moTypeId);

	/**
	 * 增加MO
	 * 
	 * @param mo
	 *            被管对象
	 * @param regionId
	 *            归属地区编号
	 */
	public void add(Mo mo, long regionId);

	/**
	 * 修改MO
	 * 
	 * @param mo
	 */
	public void modify(Mo mo);

	/**
	 * 删除指定ID的MO
	 * 
	 * @param moId
	 */
	public void delete(Long moId);
	
	/**
	 * 修改MO管理状态
	 * 
	 * @param mo
	 */
	public void changeManageState(Mo mo);

	/**
	 * 获取指定moId的 MO
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public Mo queryByMoId(Long moId);
}
