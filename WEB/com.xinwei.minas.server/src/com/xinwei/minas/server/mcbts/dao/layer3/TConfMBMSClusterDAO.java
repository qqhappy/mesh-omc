/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-2	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.List;

/**
 * 
 * ͬ����ȺDAO�ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface TConfMBMSClusterDAO {
	
	/**
	 * ��ѯ����ͬ����Ⱥ�µĻ�վmoId
	 * @return
	 * @throws Exception
	 */
	public List<Long> queryAllMBMSClusterBtsMoId() throws Exception;
}
