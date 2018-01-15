package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;

public interface EnbFullTableReverseFacade extends Remote{
	/**
	 * 进行整表反构配置
	 * @param moId
	 * @throws Exception
	 */
	public void config(Long moId) throws Exception;
}
