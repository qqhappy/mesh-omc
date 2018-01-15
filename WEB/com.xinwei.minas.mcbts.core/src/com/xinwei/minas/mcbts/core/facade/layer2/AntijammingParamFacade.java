/**
 * 
 */
package com.xinwei.minas.mcbts.core.facade.layer2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer2.TConfAntijammingParam;
import com.xinwei.minas.mcbts.core.model.layer2.TConfFreqSet;

/**
 * @author chenshaohua
 *
 */
public interface AntijammingParamFacade extends Remote {

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
	 * @param freqSetParam
	 */
	public void config(TConfFreqSet freqSetParam) throws RemoteException, Exception;

	
}
