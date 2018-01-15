package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.server.mcbts.service.ICustomService;

public interface MBMSBtsService extends ICustomService{
	/**
	 * ��ѯͬ����վ���û�����Ϣ
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfMBMSBts queryByMoId(Long moId) throws Exception;
	
	/**
	 * ����ͬ����վ������Ϣ
	 * @param remoteBts
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfMBMSBts memsBts) throws Exception;
}
