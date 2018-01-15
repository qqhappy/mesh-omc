/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-7	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import com.xinwei.minas.core.model.EnbCellStart;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizSceneTable;

/**
 * 
 * enbС�����÷���
 * 
 * @author chenlong
 * 
 */

public interface EnbCellService {
	
	/**
	 * С����վ��,����С��
	 * @param enb
	 * @param enbCellStart
	 * @throws Exception
	 */
	public void add(long moId, EnbCellStart enbCellStart) throws Exception;
	/**
	 * С����վ��,�޸�С��
	 * @param enb
	 * @param enbCellStart
	 * @throws Exception
	 */
	public void update(long moId, EnbCellStart enbCellStart) throws Exception;
	
	/**
	 * ����enb��ѯ��վ��������ӦA�����������
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public XBizSceneTable queryByMoId(long moId) throws Exception;
	
	/**
	 * ɾ��С��������Ϣ
	 * @param moId
	 * @param cid
	 * @throws Exception
	 */
	public void delete(long moId, int cid) throws Exception;
	
	/**
	 * ����С��ID����ԪID��ѯС����Ϣ
	 * @param moId
	 * @param cid
	 * @return
	 * @throws Exception
	 */
	public XBizRecord queryByCid(long moId, int cid) throws Exception;
	
}
