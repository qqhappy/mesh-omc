package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer3.TConfWCPE;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * WCPE业务层接口
 * 
 * @author yinbinqiang
 *
 */
public interface WCPEService extends ICustomService{
	/**
	 * 查询WCPE基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfWCPE queryByMoId(Long moId) throws Exception;
	
	/**
	 * 配置WCPE基本信息
	 * @param wcpe
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfWCPE wcpe) throws Exception;
	
	/**
	 * 从基站查询WCPE基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfWCPE query(Long moId) throws Exception;
}
