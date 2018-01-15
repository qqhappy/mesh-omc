/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-18	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.sxc.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.McBtsBasicFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsCondition;
import com.xinwei.minas.server.sxc.dao.SxcBasicDAO;
import com.xinwei.minas.server.sxc.service.SxcBasicService;
import com.xinwei.minas.sxc.core.model.SxcBasic;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * SXC��������
 * 
 * @author chenjunhua
 * 
 */

public class SxcBasicServiceImpl implements SxcBasicService {

	private static final Log log = LogFactory.getLog(SxcBasicServiceImpl.class);

	private SxcBasicDAO sxcBasicDAO;

	private SequenceService sequenceService;

	private McBtsBasicFacade mcBtsBasicFacade;

	public SxcBasicServiceImpl() {
		sequenceService = OmpAppContext.getCtx().getBean(SequenceService.class);
		mcBtsBasicFacade = OmpAppContext.getCtx().getBean(
				McBtsBasicFacade.class);
	}

	@Override
	public List<SxcBasic> queryAll() throws Exception {
		List<SxcBasic> list = sxcBasicDAO.queryAll();
		Collections.sort(list, new Comparator<SxcBasic>() {
			@Override
			public int compare(SxcBasic obj1, SxcBasic obj2) {
				return (int) (obj1.getSagId().longValue() - obj2.getSagId()
						.longValue());
			}

		});
		return list;
	}

	@Override
	public Long addSxc(SxcBasic newSxc) throws Exception {
		// У������
		checkInput(newSxc);
		// ��ȡ������MoId
		Long moId = sequenceService.getNext();
		newSxc.setMoId(moId);
		// ����DAO�洢����
		try {
			sxcBasicDAO.saveOrUpdate(newSxc);
		} catch (Exception e) {
			log.error("failed to add sxc: " + newSxc, e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason"));
		}
		return moId;
	}

	@Override
	public void modifySxc(SxcBasic oldSxc, SxcBasic newSxc) throws Exception {
		// У������
		checkInput(newSxc);
		
		StringBuilder buf = new StringBuilder();

		// ���޸�SAG������Ϣ
		try {
			sxcBasicDAO.saveOrUpdate(newSxc);
		} catch (Exception e) {
			log.error("failed to modify sxc: " + newSxc, e);
			buf.append(OmpAppContext.getMessage(
					("sag.failed_to_save_sag_data"),
					new Object[] { newSxc.getSagId() })
					+ "\n");
		}

		// ���»�վ��Ϣ
		List<McBts> failedBtsList = new LinkedList<McBts>();
		// ������ǽ����޸�SAG���ƣ����޸Ļ�վ�Ļ�����Ϣ
		if (!isOnlyModifySagName(oldSxc, newSxc)) {
			// ��ѯ��SAGID�µĻ�վ�б�
			McBtsCondition condition = new McBtsCondition();
			condition.setSagId(oldSxc.getSagId());
			List<McBts> btsList = mcBtsBasicFacade.queryMcBtsBy(condition);
			for (McBts bts : btsList) {
				try {
					// ���»�վ��SAG��·��Ϣ
					bts.setSagDeviceId(newSxc.getSagId());
					bts.setSagSignalPointCode(newSxc.getSagSignalPointCode()
							.intValue());
					bts.setSagSignalIp(newSxc.getSagSignalIp());
					bts.setSagVoiceIp(newSxc.getSagVoiceIp());
					OperObject operObject = OperObject.createBtsOperObject(bts
							.getHexBtsId());
					mcBtsBasicFacade.modify(operObject, bts);
				} catch (Exception e) {
					failedBtsList.add(bts);
				}
			}
		}


		if (!failedBtsList.isEmpty()) {
			buf.append(OmpAppContext.getMessage("sag.failed_to_update_bts")
					+ "\n");
			for (McBts bts : failedBtsList) {
				buf.append(bts.getHexBtsId()).append("(").append(bts.getName())
						.append("),");
			}
			buf.deleteCharAt(buf.length() - 1);
		}
		if (buf.length() > 0) {
			throw new Exception(buf.toString());
		}
	}

	@Override
	public void deleteSxc(SxcBasic sxcBasic) throws Exception {
		// ���ò�ѯ����
		McBtsCondition condition = new McBtsCondition();
		condition.setSagId(sxcBasic.getSagId());
		// ��ѯ���������Ļ�վ�б�
		List<McBts> mcBtsList = mcBtsBasicFacade.queryMcBtsBy(condition);
		if (mcBtsList != null && !mcBtsList.isEmpty()) {
			// throw new Exception("[��SAG�´��ڻ�վ, ������ɾ��!]");
			String msg = OmpAppContext
					.getMessage("sag.not_allow_delete_for_contain_bts");
			throw new Exception(msg);
		}
		// ����DAO�洢����
		try {
			sxcBasicDAO.delete(sxcBasic);
		} catch (Exception e) {
			log.error("failed to delete sxc: " + sxcBasic, e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason"));
		}
	}

	/**
	 * У��SAG��Ϣ�Ƿ��ظ�<br>
	 * ���ƣ��豸�ţ�����㣬����IP������IP�������ظ�
	 * 
	 * @param newSxc
	 * @throws Exception
	 */
	private void checkInput(SxcBasic newSxc) throws Exception {
		List<SxcBasic> allSxc = sxcBasicDAO.queryAll();
		for (SxcBasic sxc : allSxc) {
			if (sxc.getMoId() != newSxc.getMoId()) {
				if (sxc.getSagId().longValue() == newSxc.getSagId().longValue()) {
					String msg = OmpAppContext
							.getMessage("sag.sag_id_is_duplicated");
					throw new Exception(msg);
				} else if (sxc.getSagName().equals(newSxc.getSagName())) {
					throw new Exception(
							OmpAppContext
									.getMessage("sag.sag_name_is_duplicated"));
				} else if (sxc.getSagSignalPointCode().equals(
						newSxc.getSagSignalPointCode())) {
					throw new Exception(
							OmpAppContext
									.getMessage("sag.sag_pointcode_is_duplicated"));
				} else if (sxc.getSagSignalIp().equals(newSxc.getSagSignalIp())) {
					throw new Exception(
							OmpAppContext
									.getMessage("sag.sag_signalip_is_duplicated"));
				} else if (sxc.getSagVoiceIp().equals(newSxc.getSagVoiceIp())) {
					throw new Exception(
							OmpAppContext
									.getMessage("sag.sag_voiceip_is_duplicated"));
				}
			}
		}
	}

	/**
	 * �ж��Ƿ��ǽ����޸�SAG����
	 * 
	 * @param oldSxc
	 * @param newSxc
	 * @return
	 */
	private boolean isOnlyModifySagName(SxcBasic oldSxc, SxcBasic newSxc) {
		if (oldSxc.getSagDefaultGateway().equals(newSxc.getSagDefaultGateway())
				&& oldSxc.getSagId().equals(newSxc.getSagId())
				&& oldSxc.getSagSignalIp().equals(newSxc.getSagSignalIp())
				&& oldSxc.getSagSignalPointCode().equals(
						newSxc.getSagSignalPointCode())
				&& oldSxc.getSagSubnetMask().equals(newSxc.getSagSubnetMask())
				&& oldSxc.getSagVoiceIp().equals(newSxc.getSagVoiceIp())) {
			return true;
		}
		return false;
	}

	public SxcBasicDAO getSxcBasicDAO() {
		return sxcBasicDAO;
	}

	public void setSxcBasicDAO(SxcBasicDAO sxcBasicDAO) {
		this.sxcBasicDAO = sxcBasicDAO;
	}

}
