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
 * SXC基本服务
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
		// 校验输入
		checkInput(newSxc);
		// 获取并设置MoId
		Long moId = sequenceService.getNext();
		newSxc.setMoId(moId);
		// 调用DAO存储数据
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
		// 校验输入
		checkInput(newSxc);
		
		StringBuilder buf = new StringBuilder();

		// 先修改SAG基本信息
		try {
			sxcBasicDAO.saveOrUpdate(newSxc);
		} catch (Exception e) {
			log.error("failed to modify sxc: " + newSxc, e);
			buf.append(OmpAppContext.getMessage(
					("sag.failed_to_save_sag_data"),
					new Object[] { newSxc.getSagId() })
					+ "\n");
		}

		// 更新基站信息
		List<McBts> failedBtsList = new LinkedList<McBts>();
		// 如果不是仅仅修改SAG名称，则修改基站的基本信息
		if (!isOnlyModifySagName(oldSxc, newSxc)) {
			// 查询旧SAGID下的基站列表
			McBtsCondition condition = new McBtsCondition();
			condition.setSagId(oldSxc.getSagId());
			List<McBts> btsList = mcBtsBasicFacade.queryMcBtsBy(condition);
			for (McBts bts : btsList) {
				try {
					// 更新基站的SAG链路信息
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
		// 设置查询条件
		McBtsCondition condition = new McBtsCondition();
		condition.setSagId(sxcBasic.getSagId());
		// 查询符合条件的基站列表
		List<McBts> mcBtsList = mcBtsBasicFacade.queryMcBtsBy(condition);
		if (mcBtsList != null && !mcBtsList.isEmpty()) {
			// throw new Exception("[该SAG下存在基站, 不允许删除!]");
			String msg = OmpAppContext
					.getMessage("sag.not_allow_delete_for_contain_bts");
			throw new Exception(msg);
		}
		// 调用DAO存储数据
		try {
			sxcBasicDAO.delete(sxcBasic);
		} catch (Exception e) {
			log.error("failed to delete sxc: " + sxcBasic, e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason"));
		}
	}

	/**
	 * 校验SAG信息是否重复<br>
	 * 名称，设备号，信令点，信令IP，语音IP都不可重复
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
	 * 判断是否是仅仅修改SAG名称
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
