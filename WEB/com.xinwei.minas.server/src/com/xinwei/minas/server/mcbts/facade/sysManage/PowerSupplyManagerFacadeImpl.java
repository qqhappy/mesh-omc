/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-18	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.List;

import com.xinwei.minas.mcbts.core.facade.sysManage.PowerSupplyManagerFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.PowerSupply;
import com.xinwei.minas.server.mcbts.service.sysManage.PowerSupplyManagerService;

/**
 * 
 * 电源管理门面
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class PowerSupplyManagerFacadeImpl extends UnicastRemoteObject implements
		PowerSupplyManagerFacade {

	private PowerSupplyManagerService mPowerSupplyManagerService;
	
	protected PowerSupplyManagerFacadeImpl() throws RemoteException {
		super();
	}
	
	public void setmPowerSupplyManagerService(
			PowerSupplyManagerService mPowerSupplyManagerService) {
		this.mPowerSupplyManagerService = mPowerSupplyManagerService;
	}

	@Override
	public void savePowerSupplyInfo(PowerSupply data) throws Exception {
		mPowerSupplyManagerService.savePowerSupplyInfo(data);
	}

	@Override
	public List<PowerSupply> queryAll() throws Exception {
		return mPowerSupplyManagerService.queryAll();
	}

	@Override
	public void delPowerSupplyInfo(PowerSupply powerSupply) throws Exception {
		mPowerSupplyManagerService.delPowerSupplyInfo(powerSupply);
	}

	@Override
	public PowerSupply querybyMcBtsMoId(long moid) throws Exception {
		return mPowerSupplyManagerService.querybyMcBtsMoId(moid);
	}

	@Override
	public void savePowerSupplyAndMcbtsRelation(PowerSupply powerSupply)
			throws Exception {
		mPowerSupplyManagerService.savePowerSupplyAndMcbtsRelation(powerSupply);
	}

	@Override
	public HashSet<Long> queryMcbtsByPowerSupply(PowerSupply powerSupply)
			throws Exception {
		return mPowerSupplyManagerService.queryMcbtsByPowerSupply(powerSupply);
	}

	@Override
	public void reloadPowerSupplyServices(int pollInterval)throws Exception  {
		
		mPowerSupplyManagerService.reloadPowerSupplyServices(pollInterval);
		
	}

}
