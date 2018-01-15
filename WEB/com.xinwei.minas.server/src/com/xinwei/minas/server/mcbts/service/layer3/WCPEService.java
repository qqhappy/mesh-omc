package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer3.TConfWCPE;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * WCPEҵ���ӿ�
 * 
 * @author yinbinqiang
 *
 */
public interface WCPEService extends ICustomService{
	/**
	 * ��ѯWCPE������Ϣ
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfWCPE queryByMoId(Long moId) throws Exception;
	
	/**
	 * ����WCPE������Ϣ
	 * @param wcpe
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfWCPE wcpe) throws Exception;
	
	/**
	 * �ӻ�վ��ѯWCPE������Ϣ
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfWCPE query(Long moId) throws Exception;
}
