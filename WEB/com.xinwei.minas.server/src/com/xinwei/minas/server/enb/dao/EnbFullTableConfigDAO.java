/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao;

import java.util.List;

import com.xinwei.minas.enb.core.model.FullTableConfigInfo;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * ��������ҵ��DAO��ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface EnbFullTableConfigDAO extends GenericDAO<FullTableConfigInfo, Long> {
	
	/**
	 * ����moId��ѯ����������Ϣ
	 * @param moId
	 * @return
	 */
	public FullTableConfigInfo queryByMoId(Long moId);
	
	/**
	 * ������������״̬��ѯ
	 * @param status
	 * @return
	 */
	public List<FullTableConfigInfo> queryByStatus(int status);
	
}
