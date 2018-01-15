/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.service;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.ut.core.model.UTBacthUpgradeResult;
import com.xinwei.minas.ut.core.model.UTBatchUpgradeQueryModel;

/**
 * 
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����:�ն�������������
 * </p> 
 * 
 * @author qiwei
 * 
 */

public interface UTBatchUpgradeResultService {
	/**
	 * ��ѯ�ն������������ܽ��
	 * @return
	 */
  public List<UTBacthUpgradeResult> queryUTBathUpgradeAll();
  /**
   * �����ݿ��ѯ�ն����������ļ�¼��
   * @param flag
   * @return
   */
  public Integer queryUTBatchUpgradeTotalCounts(Integer flag);
  /**
   * ����������ѯ�ն����������Ľ����
   * @param queryModel
   * @return
   */
  public List<UTBacthUpgradeResult> queryUTBatchUpgradeByCondition(
			UTBatchUpgradeQueryModel queryModel);
  /**
   * �����ݿⱣ���ն��������
   */
  public void saveUTBatchUpgradeResult(UTBacthUpgradeResult utResult);
  /**
   * ��ѯidx
   */
  public Long getIdx();
  /**
   * ���������ն��������
   * @param utResults
   */
  public void batchDeleteResults(List<UTBacthUpgradeResult> utResults);
  /**
   * ��ѯ�Ƿ����������ı�־��
   * @param utResult
   * @return
   */
  public Integer queryIsUpgradingFlag(UTBacthUpgradeResult utResult);
}
