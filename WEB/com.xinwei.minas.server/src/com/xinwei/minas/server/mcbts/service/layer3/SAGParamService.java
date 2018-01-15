package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfBackupSag;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 备份SAG参数配置基本业务接口
 * 
 * @author yinbinqiang
 *
 */
public interface SAGParamService extends ICustomService{
	/**
	 * 查询所有的SAG信息
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List getAllSagInfo() throws Exception;
	
	/**
	 * 查询所有的区域信息
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List getAllLocationArea() throws Exception;
	
	/**
	 * 查询SAG参数基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfBackupSag queryByMoId(Long moId) throws Exception;
	
	/**
	 * 配置备份SAG基本信息
	 * @param loadBalance
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfBackupSag backupSag) throws Exception;
	
	/**
	 * 从网元获得备份SAG基本信息
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfBackupSag queryFromNE(Long moId) throws Exception;
}
