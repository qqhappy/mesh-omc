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
	 * ��ѯ�ڽӻ�վ
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryNeighbour(Long moId) throws RemoteException,
			Exception;

	/**
	 * ��ѯ�ڽӻ�վ
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryAppendNeighbour(Long moId) throws RemoteException,
			Exception;
	
	/**
	 * �����ڽӱ�͸����ڽӱ�
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
	 * ��ѯ���л�վ�ڽӱ������Ϣ
	 */
	public Map<Integer, String> queryNeighborCounts() throws RemoteException,
			Exception;

	/**
	 * ��ѯ���л�վ�����ڽӱ������Ϣ
	 */
	public Map<Integer, String> queryAppendNeighborCounts()
			throws RemoteException, Exception;

	/**
	 * ��ѯ���л�վƵ�㼰ǰ�����к���Ϣ
	 */
	public Map<String, Map<Integer, String>> queryBtsFreqSeqInfo()
			throws RemoteException, Exception;
	
	/**
	 * ��ѯͬƵͬ���Ļ�վ��Ϣ
	 * @return
	 */
	public Map<String, ArrayList<Long>> querySameFreqSeqInfo()throws RemoteException, Exception;
	
	/**
	 * ��ѯ�ڽӱ�����ʧ�ܵĻ�վ
	 * @param moId
	 * @return
	 */
	public McBtsNeighborFailed queryConfigFailedBts(Long moId) throws Exception;
	
}
