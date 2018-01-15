package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.TConfWCPE;
/**
 * WCPE基本业务门面
 * 
 * @author yinbinqiang
 *
 */
public interface WCPEFacade extends MoBizFacade<TConfWCPE> {
	/**
	 * 查询WCPE基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfWCPE queryByMoId(Long moId) throws RemoteException, Exception;
	
	/**
	 * 配置WCPE基本信息
	 * @param wcpe
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfWCPE wcpe) throws RemoteException, Exception;
	
	/**
	 * 从基站查询WCPE基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfWCPE query(Long moId) throws RemoteException, Exception;
}
