/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.mcbts.core.model.common.McBtsSN;
import com.xinwei.minas.server.mcbts.dao.common.McBtsSNDAO;
import com.xinwei.minas.server.mcbts.proxy.common.McBtsSNProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.common.McBtsSNService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ��վ���к�serviceʵ��
 * 
 * @author chenshaohua
 * 
 */

public class McBtsSNServiceImpl implements McBtsSNService {

	private Log log = LogFactory.getLog(McBtsSNServiceImpl.class);

	private McBtsSNDAO mcBtsSNDAO;

	private McBtsSNProxy mcBtsSNProxy;

	private SequenceService sequenceService;

	@Override
	public McBtsSN querySNFromNE(Long moId) throws Exception {

		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		if (mcBts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}

		McBtsSN mcBtsSN1 = null;
		if (mcBts != null && mcBts.isConfigurable()) {
			try {
				mcBtsSN1 = mcBtsSNProxy.querySN(moId);
				if (mcBts.getBtsType() == McBtsTypeDD.FDDI_MCBTS
						|| mcBts.getBtsType() == McBtsTypeDD.MULTI_CARRIER_MCBTS) {
					// ����ǹ�����Զ�����ز�����������һ����Ϣ
					McBtsSN mcBtsSN2 = mcBtsSNProxy.queryRRUSN(moId);
					// �ϲ� mcBtsSN1��mcBtsSN2
					// ʹ��rru����Ƶ��
					mcBtsSN1.setRfPanel1(mcBtsSN2.getRfPanel1());
					mcBtsSN1.setRfPanel2(mcBtsSN2.getRfPanel2());
					mcBtsSN1.setRfPanel3(mcBtsSN2.getRfPanel3());
					mcBtsSN1.setRfPanel4(mcBtsSN2.getRfPanel4());
					mcBtsSN1.setRfPanel5(mcBtsSN2.getRfPanel5());
					mcBtsSN1.setRfPanel6(mcBtsSN2.getRfPanel6());
					mcBtsSN1.setRfPanel7(mcBtsSN2.getRfPanel7());
					mcBtsSN1.setRfPanel8(mcBtsSN2.getRfPanel8());
					// ʹ��rru��Ƶ�۰�
					mcBtsSN1.setSynPanel(mcBtsSN2.getSynPanel());
					// ʹ��rru��dsb��
					mcBtsSN1.setDsbPanel(mcBtsSN2.getDsbPanel());

				}
//				mcBtsSN1.setIdx(sequenceService.getNext());
				mcBtsSN1.setMoId(moId);
				mcBtsSN1.setTimeStamp(new Date());
				saveSN(mcBtsSN1, moId);
				return mcBtsSN1;
			} catch (Exception e) {
				log.error(e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_query_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		return null;
	}

	// ��������
	public void saveSN(McBtsSN mcBtsSN, Long moId) {
		McBtsSN newestRecord = mcBtsSNDAO.queryNewestRecord(moId);
		if (newestRecord == null || !mcBtsSN.equals(newestRecord)) {
			mcBtsSN.setIdx(sequenceService.getNext());
			// ������ݿ���û����ʷ���ݣ����������¼
			mcBtsSNDAO.saveOrUpdate(mcBtsSN);
		} else {
			newestRecord.setTimeStamp(new Date());
			mcBtsSNDAO.saveOrUpdate(newestRecord);
		}
	}

	@Override
	public List<McBtsSN> querySNFromDB(long moId) throws Exception {
		return mcBtsSNDAO.querySNFromDB(moId);
	}

	public McBtsSNDAO getMcBtsSNDAO() {
		return mcBtsSNDAO;
	}

	public void setMcBtsSNDAO(McBtsSNDAO mcBtsSNDAO) {
		this.mcBtsSNDAO = mcBtsSNDAO;
	}

	public McBtsSNProxy getMcBtsSNProxy() {
		return mcBtsSNProxy;
	}

	public void setMcBtsSNProxy(McBtsSNProxy mcBtsSNProxy) {
		this.mcBtsSNProxy = mcBtsSNProxy;
	}

	public SequenceService getSequenceService() {
		return sequenceService;
	}

	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}
}
