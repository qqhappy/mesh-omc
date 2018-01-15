package com.xinwei.minas.server.mcbts.proxy.layer3;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqModule;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 系统频点下发
 * 
 * @author liuzhongyan
 */
public interface SysFreqProxy {

	/**
	 * 配置系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configData(Long moId, GenericBizData bizData, int FreqType)
			throws Exception;

	/**
	 * 查询系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */

	public Object[] queryData(Long moId) throws Exception;

}
