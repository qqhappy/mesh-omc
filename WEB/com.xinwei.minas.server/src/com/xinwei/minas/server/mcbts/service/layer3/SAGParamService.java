package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfBackupSag;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * ����SAG�������û���ҵ��ӿ�
 * 
 * @author yinbinqiang
 *
 */
public interface SAGParamService extends ICustomService{
	/**
	 * ��ѯ���е�SAG��Ϣ
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List getAllSagInfo() throws Exception;
	
	/**
	 * ��ѯ���е�������Ϣ
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List getAllLocationArea() throws Exception;
	
	/**
	 * ��ѯSAG����������Ϣ
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfBackupSag queryByMoId(Long moId) throws Exception;
	
	/**
	 * ���ñ���SAG������Ϣ
	 * @param loadBalance
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfBackupSag backupSag) throws Exception;
	
	/**
	 * ����Ԫ��ñ���SAG������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfBackupSag queryFromNE(Long moId) throws Exception;
}
