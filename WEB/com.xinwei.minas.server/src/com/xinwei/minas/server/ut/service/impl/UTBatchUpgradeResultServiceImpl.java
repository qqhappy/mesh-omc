/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.service.impl;

import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.inms.commons.utils.service.impl.SequenceServiceImpl;
import com.xinwei.minas.server.ut.dao.UTBathUpgradeResultDAO;
import com.xinwei.minas.server.ut.service.UTBatchUpgradeResultService;
import com.xinwei.minas.ut.core.model.UTBacthUpgradeResult;
import com.xinwei.minas.ut.core.model.UTBatchUpgradeQueryModel;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����:�ն�������������ҵ��
 * </p> 
 * 
 * @author qiwei
 * 
 */

public class UTBatchUpgradeResultServiceImpl implements UTBatchUpgradeResultService {
    private UTBathUpgradeResultDAO  utBathUpgradeResultDAO;
    private SequenceService sequenceService;
	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}
	public UTBatchUpgradeResultServiceImpl() {
	this.utBathUpgradeResultDAO = OmpAppContext.getCtx().getBean(UTBathUpgradeResultDAO.class);
	this.sequenceService= OmpAppContext.getCtx().getBean(SequenceService.class);
     }
    /**
     * ��ѯ�ն������������
     */
	@Override
	public List<UTBacthUpgradeResult> queryUTBathUpgradeAll() {
		return utBathUpgradeResultDAO.queryUTBathUpgradeAll();
	}
	 /**
     * ��ѯ�ն����������ļ�¼����;
     * 1--�����ɹ�  -1---����ʧ��  0---��������
     */
	@Override
	public Integer queryUTBatchUpgradeTotalCounts(Integer flag) {
		// TODO Auto-generated method stub
		return utBathUpgradeResultDAO.queryUTBatchUpgradeTotalCounts(flag);
	}
	
	/**
	 * ��������ѯ�����������
	 */
	@Override
	public List<UTBacthUpgradeResult> queryUTBatchUpgradeByCondition(
			UTBatchUpgradeQueryModel queryModel) {
		return utBathUpgradeResultDAO.queryUTBatchUpgradeByCondition(queryModel);
	}
	/**
	 * �����ݿⱣ���ն��������
	 */
	@Override
	public void saveUTBatchUpgradeResult(UTBacthUpgradeResult utResult) {
		//����ǵ�һ�δ洢��������idx����
		if(utResult.getIdx()==null||utResult.getIdx()==0L){
			utResult.setIdx(sequenceService.getNext());
		}
		utBathUpgradeResultDAO.saveUTBatchUpgradeResult(utResult);
		
	}
	/**
	 * �����ݿ��ѯidx
	 */
	
	@Override
	public Long getIdx() {
		return utBathUpgradeResultDAO.getIdx();
	}
	/**
	 * ����ɾ���ն��������
	 */
	@Override
	public void batchDeleteResults(List<UTBacthUpgradeResult> utResults) {
		utBathUpgradeResultDAO.batchDeleteResults(utResults);		
	}
	/**
	 * ��ѯ�Ƿ����������ı�־
	 */
	@Override
	public Integer queryIsUpgradingFlag(UTBacthUpgradeResult utResult) {
		return utBathUpgradeResultDAO.queryIsUpgradingFlag(utResult);
	}
}
