/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-19	| fangping 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.DataPackageFilter;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * DataPackageFilterService下发service
 * 
 */
public interface DataPackageFilterService extends ICustomService {

	/**
	 * 获取过滤类型
	 * 
	 * @return
	 */
	public int queryFilterType();

	/**
	 * 从数据库获取信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<DataPackageFilter> queryAllFromEMS() throws Exception;

	/**
	 * 从网元查询数据
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public Object[] queryFromNE(Long moId) throws Exception;

	/**
	 * 配置数据包过滤规则给数据库,并判断是否下发给所有基站
	 * 
	 * @param filterList
	 * @param isSync
	 * @throws Exception
	 */
	public void config(int filterType, List<DataPackageFilter> filterList)
			throws Exception;

	/**
	 * 根据数据包过滤List配置NE
	 * 
	 * @param moId
	 * @param filterList
	 * @throws Exception
	 */
	public void config(Long moId, int filterType,
			List<DataPackageFilter> filterList) throws Exception;

}
