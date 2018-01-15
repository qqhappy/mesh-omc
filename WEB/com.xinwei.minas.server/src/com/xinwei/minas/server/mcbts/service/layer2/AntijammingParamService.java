/**
 * 
 */
package com.xinwei.minas.server.mcbts.service.layer2;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer2.TConfAntijammingParam;
import com.xinwei.minas.mcbts.core.model.layer2.TConfFreqSet;

/**
 * @author chenshaohua
 *
 */
public interface AntijammingParamService {

	/**
	 * �����ݿ��в�ѯ��������Ϣ
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfAntijammingParam queryByMoId(Long moId) throws RemoteException, Exception;
	
	/**
	 * ���վ���Ϳ�����������Ϣ�������浽���ݿ�
	 * @param antijamming
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfAntijammingParam antijamming) throws RemoteException, Exception;

	/**
	 * ���վ����Ƶ�㼯������Ϣ�������浽���ݿ�
	 * @param freqSetEntity
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfFreqSet freqSetEntity) throws RemoteException, Exception;;
}
