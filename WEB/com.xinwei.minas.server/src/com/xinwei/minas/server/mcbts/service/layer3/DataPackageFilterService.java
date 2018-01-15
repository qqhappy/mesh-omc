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
 * DataPackageFilterService�·�service
 * 
 */
public interface DataPackageFilterService extends ICustomService {

	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public int queryFilterType();

	/**
	 * �����ݿ��ȡ��Ϣ
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<DataPackageFilter> queryAllFromEMS() throws Exception;

	/**
	 * ����Ԫ��ѯ����
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public Object[] queryFromNE(Long moId) throws Exception;

	/**
	 * �������ݰ����˹�������ݿ�,���ж��Ƿ��·������л�վ
	 * 
	 * @param filterList
	 * @param isSync
	 * @throws Exception
	 */
	public void config(int filterType, List<DataPackageFilter> filterList)
			throws Exception;

	/**
	 * �������ݰ�����List����NE
	 * 
	 * @param moId
	 * @param filterList
	 * @throws Exception
	 */
	public void config(Long moId, int filterType,
			List<DataPackageFilter> filterList) throws Exception;

}
