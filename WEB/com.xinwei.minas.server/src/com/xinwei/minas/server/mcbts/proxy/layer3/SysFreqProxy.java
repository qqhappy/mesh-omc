package com.xinwei.minas.server.mcbts.proxy.layer3;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqModule;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * ϵͳƵ���·�
 * 
 * @author liuzhongyan
 */
public interface SysFreqProxy {

	/**
	 * ����ϵͳ��ЧƵ��
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configData(Long moId, GenericBizData bizData, int FreqType)
			throws Exception;

	/**
	 * ��ѯϵͳ��ЧƵ��
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */

	public Object[] queryData(Long moId) throws Exception;

}
