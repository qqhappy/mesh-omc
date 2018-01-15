package com.xinwei.minas.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MinasClientFacade extends Remote {
	/**
	 * 获取指定接口的门面
	 * 
	 * @param facadeInterface
	 *            门面接口
	 * @return 指定接口的门面
	 */
	public <T> T getFacade(Class<T> facadeInterface) throws RemoteException,
			Exception;
	
}
