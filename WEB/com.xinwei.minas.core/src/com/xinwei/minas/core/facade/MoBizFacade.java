/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-8	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * 所有Facade接口的父接口
 * 所有的实现Facade的类都要实现此接口定义的两个方法
 * 
 * @author tiance
 * 
 */

public interface MoBizFacade<T> extends Remote{

	/**
	 * 从数据库获取配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public T queryFromEMS(Long moId) throws RemoteException, Exception;
	
	/**
	 * 从网元获得配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public T queryFromNE(Long moId) throws RemoteException, Exception;
	
}
