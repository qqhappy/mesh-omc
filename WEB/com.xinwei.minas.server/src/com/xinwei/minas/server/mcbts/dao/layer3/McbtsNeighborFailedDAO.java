/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-10	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.dao.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsNeighborFailed;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * �ڽӱ������±����ݿ���ʽӿ�
 * 
 * @author zhuxiaozhan
 *
 */
public interface McbtsNeighborFailedDAO extends GenericDAO<McBtsNeighborFailed, Long>{
	
	/**
	 * ���ݲ�ѯʧ�ܵĻ�վ
	 * @param moid
	 * @return
	 * @throws Exception
	 */
	public McBtsNeighborFailed queryByMoId(Long moId) throws Exception;
	
	/**
	 * ɾ��ָ���Ļ�վ
	 * @param moId
	 * @throws Exception
	 */
	public void delByMoId(Long moId) throws Exception;
}
