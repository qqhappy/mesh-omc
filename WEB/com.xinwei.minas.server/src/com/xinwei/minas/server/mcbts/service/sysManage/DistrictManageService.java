/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-14	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.District;

/**
 * 
 * ����������ӿ�
 * 
 * 
 * @author tiance
 * 
 */

public interface DistrictManageService {
	/**
	 * ��ѯ���еĵ�����Ϣ
	 * 
	 * @return
	 */
	public List<District> queryAll();

	/**
	 * ������߸���һ��������Ϣ
	 * 
	 * @param district
	 */
	public void saveOrUpdate(District district);

	/**
	 * ɾ��һ��������Ϣ
	 * 
	 * @param district
	 */
	public void delete(District district);
}
