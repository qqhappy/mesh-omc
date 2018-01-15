/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-18	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import java.util.HashSet;
import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.PowerSupply;
import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsPowerConfigDAO;
import com.xinwei.minas.server.mcbts.dao.sysManage.PowerSupplyManagerDAO;
import com.xinwei.minas.server.mcbts.service.powerSupply.PowerSupplyCache;
import com.xinwei.minas.server.mcbts.service.sysManage.PowerSupplyManagerService;

/**
 * 
 * ��Դ�������ʵ����
 * 
 * <p>
 * ����ϸ����
 * </p>
 * 
 * @author zhuxiaozhan
 * 
 */

public class PowerSupplyManagerServiceImpl implements PowerSupplyManagerService {

	private PowerSupplyManagerDAO mPowerSupplyManagerDAO;
	private McBtsPowerConfigDAO mcBtsPowerConfigDAO;

	private SequenceService mSequenceService;

	public void setmSequenceService(SequenceService mSequenceService) {
		this.mSequenceService = mSequenceService;
	}

	public void setmPowerSupplyManagerDAO(
			PowerSupplyManagerDAO mPowerSupplyManagerDAO) {
		this.mPowerSupplyManagerDAO = mPowerSupplyManagerDAO;
	}

	public void setMcBtsPowerConfigDAO(McBtsPowerConfigDAO mcBtsPowerConfigDAO) {
		this.mcBtsPowerConfigDAO = mcBtsPowerConfigDAO;
	}

	@Override
	public void savePowerSupplyInfo(PowerSupply data) {
		if (data.getIdx() == null) {
			data.setIdx(mSequenceService.getNext());
		}
		mPowerSupplyManagerDAO.saveOrUpdate(data);
	}

	@Override
	public List<PowerSupply> queryAll() {
		return mPowerSupplyManagerDAO.queryAll();
	}

	@Override
	public void delPowerSupplyInfo(PowerSupply powerSupply) {
		mPowerSupplyManagerDAO.delete(powerSupply);
	}

	@Override
	public PowerSupply querybyMcBtsMoId(long moid) {
		return mcBtsPowerConfigDAO.queryByMoId(moid);
	}

	@Override
	public void savePowerSupplyAndMcbtsRelation(PowerSupply powerSupply) {
		long idx = mSequenceService.getNext();
		mcBtsPowerConfigDAO.saveByMoIdAndPowerId(idx, powerSupply);
	}

	@Override
	public void reloadPowerSupplyServices(int pollInterval) {
		// ���³�ʼ����Դ��ػ���
		PowerSupplyCache.getInstance().initCache();

		List<PowerSupply> powers = queryAll();

		// Ϊÿ����Դ����ʼ����Դ�����
		for (PowerSupply power : powers) {
			HashSet<Long> moIds = this.queryMcbtsByPowerSupply(power);
			power.addMoIdCollections(moIds);
			PowerSupplyCache.getInstance().addPowerSupply(power);
		}
		// ���¼��ص�Դ�����
		PowerSupplyCache.getInstance().readloadMoniterService(pollInterval);
	}

	@Override
	public HashSet<Long> queryMcbtsByPowerSupply(PowerSupply powerSupply) {
		return mcBtsPowerConfigDAO.queryMcbtsByPowerSupply(powerSupply);
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		for (Long moId : moIdList) {
			if (moId < 0 || !moIdList.contains(moId))
				continue;
			PowerSupply power = mcBtsPowerConfigDAO.queryByMoId(moId);
			if (power == null)
				continue;
			business.getCell("ipAddress").putContent(moId,
					toJSON("ipAddress", String.valueOf(power.getIpAddress())));

			business.getCell("port").putContent(moId,
					toJSON("port", String.valueOf(power.getPort())));

		}

	}

	private static String toJSON(String key, String value) {
		return "\"" + key + "\":\"" + value + "\"";
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		// Do Nothing
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		// Do Nothing
	}
}
