/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.McBtsSN;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * ��վ���к�DAO
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsSNDAO extends GenericDAO<McBtsSN, Long> {

	/**
	 * �����ݿ��ѯ��¼
	 * 
	 * @param moId
	 * @return
	 */
	public List<McBtsSN> querySNFromDB(long moId);

	/**
	 * �����¼�����ݿ�
	 * 
	 * @param mcBtsSN1
	 */
	public void saveFixedCountRecord(McBtsSN mcBtsSN1);

	/**
	 * ��ѯ���¼�¼
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsSN queryNewestRecord(long moId);

}
