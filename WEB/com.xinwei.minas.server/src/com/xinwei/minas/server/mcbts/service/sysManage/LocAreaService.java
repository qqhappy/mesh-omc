/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-29	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.LocationArea;

/**
 * 
 * λ��������ӿ�
 * 
 * 
 * @author chenshaohua
 * 
 */

public interface LocAreaService {

	/**
	 * ��ѯȫ����¼
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<LocationArea> queryAll();

	/**
	 * ���������λ������Ϣ
	 * 
	 * @param la
	 */
	public void saveOrUpdate(LocationArea la);

	/**
	 * ɾ��һ��λ������Ϣ
	 * 
	 * @param la
	 */
	public void delete(LocationArea la);

	/**
	 * ��������
	 * 
	 * @param locationAreaList
	 * @throws Exception
	 */
	public void config(List<LocationArea> locationAreaList) throws Exception;

}
