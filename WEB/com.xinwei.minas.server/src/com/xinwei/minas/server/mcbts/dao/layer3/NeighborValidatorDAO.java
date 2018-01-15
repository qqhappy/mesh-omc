/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-18	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.ArrayList;
import java.util.Map;

/**
 * 
 * �ڽӱ�У��Dao�ӿ�
 * 
 * @author tiance
 * 
 */

public interface NeighborValidatorDAO {

	/**
	 * ��ѯ���л�վ�ڽӱ������Ϣ
	 */
	public Map<Integer, String> queryNeighborCounts();

	/**
	 * ��ѯ���л�վ�����ڽӱ������Ϣ
	 */
	public Map<Integer, String> queryAppendNeighborCounts();

	/**
	 * ��ѯ���л�վƵ�㼰ǰ�����к���Ϣ
	 */
	public Map<String, Map<Integer, String>> queryBtsFreqSeqInfo();
	
	/**
	 * ��ѯͬƵͬ���Ļ�վ��Ϣ
	 * @return
	 */
	public Map<String, ArrayList<Long>> querySameFreqSeqInfo();
}
