package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * Զ�����վ���û���ҵ��ӿ�
 * 
 * @author yinbinqiang
 * 
 */
public interface RemoteBtsService extends ICustomService {
	/**
	 * ��ѯԶ�����վ���û�����Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfRemoteBts queryByMoId(Long moId) throws Exception;

	/**
	 * ����Զ�����վ������Ϣ
	 * 
	 * @param remoteBts
	 * @param isSyncConfig
	 *            �Ƿ�ͬ�������ڽӱ�
	 * @throws Exception
	 */
	public void config(TConfRemoteBts remoteBts, boolean isSyncConfig)
			throws Exception;
}
