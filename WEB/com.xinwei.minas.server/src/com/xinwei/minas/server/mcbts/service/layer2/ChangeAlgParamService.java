package com.xinwei.minas.server.mcbts.service.layer2;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer2.TConfChangeAlgParam;
import com.xinwei.minas.server.mcbts.service.ICustomService;

public interface ChangeAlgParamService extends ICustomService {

	/**
	 * �����ݿ��и���moId��ѯ
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfChangeAlgParam queryByMoId(Long moId) throws Exception;
	
	/**
	 * ���վ����
	 * @param changeAlgParam
	 * @throws Exception
	 */
	public void config(TConfChangeAlgParam changeAlgParam) throws Exception;
	
	/**
	 * ����Ԫ���������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfChangeAlgParam query(Long moId) throws Exception;
	
	/**
	 * ��ȡ��������־
	 * @return
	 * @throws Exception
	 */
	public String getRestrictAreaFlag() throws Exception;
}
