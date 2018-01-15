package com.xinwei.minas.mcbts.core.facade.oamManage;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.mcbts.core.model.common.McBtsSateQuery;
import com.xinwei.minas.mcbts.core.model.layer2.TConfResmanagement;

public interface McBtsStateQueryFacade extends MoBizFacade<TConfResmanagement> {
	/**
	 * ��ѯ������Դ������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfResmanagement queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * ����������Դ��Ϣ
	 * 
	 * @param resManagement
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfResmanagement resManagement)
			throws RemoteException, Exception;

	/**
	 * ����Ԫ���������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfResmanagement queryFromNE(Long moId) throws RemoteException,
			Exception;

	public List<McBtsSateQuery> queryInfoFromDB(long moId) throws RemoteException, Exception;

	public McBtsSateQuery queryInfoFromNE(long moId) throws RemoteException, Exception;
}