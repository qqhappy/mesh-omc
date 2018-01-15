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
 * 备份SAG参数配置基本业务信息门面
 * 
 * @author yinbinqiang
 *
 */
public interface SAGParamFacade extends MoBizFacade<TConfBackupSag> {
	
	/**
	 * 查询所有的SAG信息
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List getAllSagInfo() throws RemoteException, Exception;
	
	/**
	 * 查询所有的区域信息
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List getAllLocationArea() throws RemoteException, Exception;
	
	/**
	 * 查询SAG参数基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfBackupSag queryByMoId(Long moId) throws RemoteException, Exception;
	
	/**
	 * 配置备份SAG基本信息
	 * @param loadBalance
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfBackupSag backupSag) throws RemoteException, Exception;
	
	/**
	 * 根据moId获取基站信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public McBts getBTSInfo(Long moId) throws RemoteException, Exception;
}
