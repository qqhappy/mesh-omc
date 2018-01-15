package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfBackupSag;

/**
 * ����SAG�������û���ҵ����Ϣ����
 * 
 * @author yinbinqiang
 *
 */
public interface SAGParamFacade extends MoBizFacade<TConfBackupSag> {
	
	/**
	 * ��ѯ���е�SAG��Ϣ
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List getAllSagInfo() throws RemoteException, Exception;
	
	/**
	 * ��ѯ���е�������Ϣ
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List getAllLocationArea() throws RemoteException, Exception;
	
	/**
	 * ��ѯSAG����������Ϣ
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfBackupSag queryByMoId(Long moId) throws RemoteException, Exception;
	
	/**
	 * ���ñ���SAG������Ϣ
	 * @param loadBalance
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfBackupSag backupSag) throws RemoteException, Exception;
	
	/**
	 * ����moId��ȡ��վ��Ϣ
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public McBts getBTSInfo(Long moId) throws RemoteException, Exception;
}
