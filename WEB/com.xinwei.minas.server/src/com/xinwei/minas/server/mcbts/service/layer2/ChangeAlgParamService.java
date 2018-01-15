package com.xinwei.minas.server.mcbts.service.layer2;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer2.TConfChangeAlgParam;
import com.xinwei.minas.server.mcbts.service.ICustomService;

public interface ChangeAlgParamService extends ICustomService {

	/**
	 * 从数据库中根据moId查询
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfChangeAlgParam queryByMoId(Long moId) throws Exception;
	
	/**
	 * 向基站配置
	 * @param changeAlgParam
	 * @throws Exception
	 */
	public void config(TConfChangeAlgParam changeAlgParam) throws Exception;
	
	/**
	 * 从网元获得配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfChangeAlgParam query(Long moId) throws Exception;
	
	/**
	 * 获取限制区标志
	 * @return
	 * @throws Exception
	 */
	public String getRestrictAreaFlag() throws Exception;
}
