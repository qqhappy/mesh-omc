/**
 * 
 */
package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsNeighborFailed;

/**
 * @author chenshaohua
 * 
 */
public interface NeighbourFacade extends Remote {

	/**
	 * 查询邻接基站
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryNeighbour(Long moId) throws RemoteException,
			Exception;

	/**
	 * 查询邻接基站
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryAppendNeighbour(Long moId) throws RemoteException,
			Exception;
	
	/**
	 * 配置邻接表和附加邻接表
	 * @param moId
	 * @param neighborList
	 * @param appendNeighborList
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Long moId, List<McBts> neighborList, List<McBts> appendNeighborList)
			throws RemoteException, Exception;

	/**
	 * 查询所有基站邻接表个数信息
	 */
	public Map<Integer, String> queryNeighborCounts() throws RemoteException,
			Exception;

	/**
	 * 查询所有基站附加邻接表个数信息
	 */
	public Map<Integer, String> queryAppendNeighborCounts()
			throws RemoteException, Exception;

	/**
	 * 查询所有基站频点及前导序列号信息
	 */
	public Map<String, Map<Integer, String>> queryBtsFreqSeqInfo()
			throws RemoteException, Exception;
	
	/**
	 * 查询同频同导的基站信息
	 * @return
	 */
	public Map<String, ArrayList<Long>> querySameFreqSeqInfo()throws RemoteException, Exception;
	
	/**
	 * 查询邻接表配置失败的基站
	 * @param moId
	 * @return
	 */
	public McBtsNeighborFailed queryConfigFailedBts(Long moId) throws Exception;
	
}
