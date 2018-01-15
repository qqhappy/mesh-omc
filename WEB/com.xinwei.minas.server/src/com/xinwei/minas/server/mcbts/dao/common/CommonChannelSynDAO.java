/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common;

import com.xinwei.minas.mcbts.core.model.common.CommonChannelSynInfo;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface CommonChannelSynDAO extends GenericDAO<CommonChannelSynInfo, Long>{
	
	
	/**
	 * ����moId����ѯ���鵼��ʧ�ܵ���Ϣ
	 * @param moId
	 * @return
	 */
	public CommonChannelSynInfo queryByMoId(long moId);
}
