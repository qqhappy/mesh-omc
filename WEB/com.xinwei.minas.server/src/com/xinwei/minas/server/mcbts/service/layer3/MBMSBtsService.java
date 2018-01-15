package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.server.mcbts.service.ICustomService;

public interface MBMSBtsService extends ICustomService{
	/**
	 * 查询同播基站配置基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfMBMSBts queryByMoId(Long moId) throws Exception;
	
	/**
	 * 配置同播基站基本信息
	 * @param remoteBts
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfMBMSBts memsBts) throws Exception;
}
