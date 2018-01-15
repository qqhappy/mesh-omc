/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.dao;

import java.util.List;

import com.xinwei.minas.ut.core.model.UTBacthUpgradeResult;
import com.xinwei.minas.ut.core.model.UTBatchUpgradeQueryModel;



/**
 * 
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ�������������������ѯ�������ɹ�����������ʧ�ܵ�
 * </p> 
 * 
 * @author qiwei
 * 
 */

public interface UTBathUpgradeResultDAO {
public List<UTBacthUpgradeResult> queryUTBathUpgradeAll();
/**
 *  ��ѯ�ն����������ļ�¼����;
 */
public Integer queryUTBatchUpgradeTotalCounts(Integer flag);
/**
 * ��������ѯ�����������
 */
public List<UTBacthUpgradeResult> queryUTBatchUpgradeByCondition(
		UTBatchUpgradeQueryModel queryModel);
/**
 * �����ݿ�����ն����������Ľ��
 */
public void saveUTBatchUpgradeResult(UTBacthUpgradeResult utResult);
/**
 * �����ݿ��ѯidx
 * 
 * 
 */
public Long getIdx();
/**
 * ����ɾ���ն��������
 * @param utResults
 */
public void batchDeleteResults(List<UTBacthUpgradeResult> utResults);
/**
 * ��ѯ�Ƿ����������ı�־��
 */
public Integer queryIsUpgradingFlag(UTBacthUpgradeResult utResult);
}
