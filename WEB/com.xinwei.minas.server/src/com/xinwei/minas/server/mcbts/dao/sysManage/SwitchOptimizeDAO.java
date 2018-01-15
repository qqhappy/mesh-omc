/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.SwitchOptimizeConfig;

/**
 * 
 * �л��Ż���������DAO�ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface SwitchOptimizeDAO {

	/**
	 * ��ѯȫ������
	 * 
	 * @return
	 */
	public List<SwitchOptimizeConfig> queryAll();

	/**
	 * ����ָ������
	 * 
	 * @param config
	 */
	public void saveOrUpdate(SwitchOptimizeConfig config);

}
