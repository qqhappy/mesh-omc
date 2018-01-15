package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 远距离基站配置基本业务接口
 * 
 * @author yinbinqiang
 * 
 */
public interface RemoteBtsService extends ICustomService {
	/**
	 * 查询远距离基站配置基本信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfRemoteBts queryByMoId(Long moId) throws Exception;

	/**
	 * 配置远距离基站基本信息
	 * 
	 * @param remoteBts
	 * @param isSyncConfig
	 *            是否同步配置邻接表
	 * @throws Exception
	 */
	public void config(TConfRemoteBts remoteBts, boolean isSyncConfig)
			throws Exception;
}
