package com.xinwei.minas.mcbts.core.facade.oamManage;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.mcbts.core.model.common.McBtsSateQuery;
import com.xinwei.minas.mcbts.core.model.common.McBtsUserCountsQuery;
import com.xinwei.minas.mcbts.core.model.layer2.TConfResmanagement;

public interface McBtsUserCountsQueryFacade extends MoBizFacade<TConfResmanagement> {
	/**
	 * 查询无线资源配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfResmanagement queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * 配置无线资源信息
	 * 
	 * @param resManagement
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfResmanagement resManagement)
			throws RemoteException, Exception;

	/**
	 * 从网元获得配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfResmanagement queryFromNE(Long moId) throws RemoteException,
			Exception;

	public List<McBtsUserCountsQuery> queryInfoFromDB(long moId) throws RemoteException, Exception;

	public McBtsUserCountsQuery queryInfoFromNE(long moId) throws RemoteException, Exception;
}