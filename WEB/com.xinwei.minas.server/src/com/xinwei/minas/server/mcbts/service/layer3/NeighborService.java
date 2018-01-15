package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsNeighborFailed;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 邻接表业务层接口
 * 
 * @author chenshaohua
 * 
 */
public interface NeighborService extends ICustomService {

	/**
	 * 查询邻接表
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryNeighbor(Long moId) throws Exception;
	
	/**
	 * 从网元查询邻接表
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<McBts> queryNeighborFromNE(Long moId) throws Exception;
	
	/**
	 * 查询切换附加邻接表
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<McBts> queryAppendNeighbor(Long moId) throws Exception;
	
	/**
	 * 从网元查询附加邻接表
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<McBts> queryAppendNeighborFromNE(Long moId) throws Exception;
	
	/**
	 * 配置邻接表和附加邻接表
	 * @param moId
	 * @param neighborList
	 * @param appendNeighborList
	 * @throws Exception
	 */
	public void config(Long moId, List<McBts> neighborList, List<McBts> appendNeighborList) throws Exception;


	/**
	 * 查询所有基站邻接表个数信息
	 */
	public Map<Integer, String> queryNeighborCounts();

	/**
	 * 查询所有基站附加邻接表个数信息
	 */
	public Map<Integer, String> queryAppendNeighborCounts();

	/**
	 * 查询所有基站频点及前导序列号信息
	 */
	public Map<String, Map<Integer, String>> queryBtsFreqSeqInfo();
	
	/**
	 * 查询同频同导的基站信息
	 * @return
	 */
	public Map<String, ArrayList<Long>> querySameFreqSeqInfo();
	
	/**
	 * 处理邻接表配置失败的基站
	 */
	public void processNbConfigFailedBts();
	
	/**
	 * 查询邻接表配置失败的基站
	 * @param moId
	 * @return
	 */
	public McBtsNeighborFailed queryConfigFailedBts(Long moId) throws Exception;
	
	/**
	 * 对邻接站进行邻接表配置
	 * @param moId
	 * @param isSyncConfig
	 */
	public void sendNeighborConfig(Long moId, boolean isSyncConfig); 
}
