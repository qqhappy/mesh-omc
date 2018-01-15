package com.xinwei.minas.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MinasClientFacade extends Remote {
	/**
	 * ��ȡָ���ӿڵ�����
	 * 
	 * @param facadeInterface
	 *            ����ӿ�
	 * @return ָ���ӿڵ�����
	 */
	public <T> T getFacade(Class<T> facadeInterface) throws RemoteException,
			Exception;
	
}
