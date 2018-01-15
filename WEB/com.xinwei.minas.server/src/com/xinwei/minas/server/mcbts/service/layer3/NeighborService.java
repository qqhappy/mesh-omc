package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsNeighborFailed;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * �ڽӱ�ҵ���ӿ�
 * 
 * @author chenshaohua
 * 
 */
public interface NeighborService extends ICustomService {

	/**
	 * ��ѯ�ڽӱ�
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryNeighbor(Long moId) throws Exception;
	
	/**
	 * ����Ԫ��ѯ�ڽӱ�
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<McBts> queryNeighborFromNE(Long moId) throws Exception;
	
	/**
	 * ��ѯ�л������ڽӱ�
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<McBts> queryAppendNeighbor(Long moId) throws Exception;
	
	/**
	 * ����Ԫ��ѯ�����ڽӱ�
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<McBts> queryAppendNeighborFromNE(Long moId) throws Exception;
	
	/**
	 * �����ڽӱ�͸����ڽӱ�
	 * @param moId
	 * @param neighborList
	 * @param appendNeighborList
	 * @throws Exception
	 */
	public void config(Long moId, List<McBts> neighborList, List<McBts> appendNeighborList) throws Exception;


	/**
	 * ��ѯ���л�վ�ڽӱ������Ϣ
	 */
	public Map<Integer, String> queryNeighborCounts();

	/**
	 * ��ѯ���л�վ�����ڽӱ������Ϣ
	 */
	public Map<Integer, String> queryAppendNeighborCounts();

	/**
	 * ��ѯ���л�վƵ�㼰ǰ�����к���Ϣ
	 */
	public Map<String, Map<Integer, String>> queryBtsFreqSeqInfo();
	
	/**
	 * ��ѯͬƵͬ���Ļ�վ��Ϣ
	 * @return
	 */
	public Map<String, ArrayList<Long>> querySameFreqSeqInfo();
	
	/**
	 * �����ڽӱ�����ʧ�ܵĻ�վ
	 */
	public void processNbConfigFailedBts();
	
	/**
	 * ��ѯ�ڽӱ�����ʧ�ܵĻ�վ
	 * @param moId
	 * @return
	 */
	public McBtsNeighborFailed queryConfigFailedBts(Long moId) throws Exception;
	
	/**
	 * ���ڽ�վ�����ڽӱ�����
	 * @param moId
	 * @param isSyncConfig
	 */
	public void sendNeighborConfig(Long moId, boolean isSyncConfig); 
}
