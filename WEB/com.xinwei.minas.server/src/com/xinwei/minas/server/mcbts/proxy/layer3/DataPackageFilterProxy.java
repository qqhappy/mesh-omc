package com.xinwei.minas.server.mcbts.proxy.layer3;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.DataPackageFilter;

/**
 * DataPackageFilterProxy
 * 
 * @author fangping
 */
public interface DataPackageFilterProxy {
	/**
	 * ��ѯNE
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Object[] queryFromNE(Long moId) throws RemoteException, Exception;

	/**
	 * ���ݹ������ͺ����ݰ�����List����NE
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(Long moId, int filterType,
			List<DataPackageFilter> filterList) throws RemoteException,
			Exception;
}
