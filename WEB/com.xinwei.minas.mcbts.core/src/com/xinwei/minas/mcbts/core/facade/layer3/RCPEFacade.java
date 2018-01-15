package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPE;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItem;
/**
 * RCPE基本业务门面
 * @author yinbinqiang
 *
 */
public interface RCPEFacade extends Remote {
	/**
	 * 查询RCPE配置基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfRCPE queryByMoId(Long moId) throws RemoteException, Exception;
	
	/**
	 * 配置RCPE基本信息
	 * @param remoteBts
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfRCPE rcpe) throws RemoteException, Exception;
	
	/**
	 * 删除指定RCPE信息
	 * @param rcpeItem
	 * @throws Exception
	 */
	@Loggable
	public void deleteRcpe(OperObject operObject, TConfRCPEItem rcpeItem) throws Exception;
}
