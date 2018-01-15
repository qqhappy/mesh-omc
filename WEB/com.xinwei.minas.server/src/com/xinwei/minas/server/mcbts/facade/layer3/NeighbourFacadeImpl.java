/**
 * 
 */
package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.NeighbourFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsNeighborFailed;
import com.xinwei.minas.server.mcbts.service.layer3.NeighborService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * @author chenshaohua
 * 
 */
public class NeighbourFacadeImpl extends UnicastRemoteObject implements
		NeighbourFacade {

	private NeighborService service;

	private SequenceService sequenceService;

	protected NeighbourFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(NeighborService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	public List<McBts> queryNeighbour(Long moId) throws RemoteException,
			Exception {
		return service.queryNeighbor(moId);
	}

	@Override
	public List<McBts> queryAppendNeighbour(Long moId) throws RemoteException,
			Exception {
		return service.queryAppendNeighbor(moId);
	}
	
	public NeighborService getService() {
		return service;
	}

	public void setService(NeighborService service) {
		this.service = service;
	}

	@Override
	public void config(OperObject operObject, Long moId, List<McBts> neighborList, List<McBts> appendNeighborList)
			throws RemoteException, Exception {
		service.config(moId, neighborList, appendNeighborList);
	}

	/**
	 * 查询所有基站邻接表个数信息
	 */
	@Override
	public Map<Integer, String> queryNeighborCounts() throws RemoteException,
			Exception {
		return service.queryNeighborCounts();
	}

	/**
	 * 查询所有基站附加邻接表个数信息
	 */
	@Override
	public Map<Integer, String> queryAppendNeighborCounts()
			throws RemoteException, Exception {
		return service.queryAppendNeighborCounts();
	}

	/**
	 * 查询所有基站频点及前导序列号信息
	 */
	@Override
	public Map<String, Map<Integer, String>> queryBtsFreqSeqInfo()
			throws RemoteException, Exception {
		return service.queryBtsFreqSeqInfo();
	}

	@Override
	public McBtsNeighborFailed queryConfigFailedBts(Long moId) throws Exception {
		return service.queryConfigFailedBts(moId);
	}

	@Override
	public Map<String, ArrayList<Long>> querySameFreqSeqInfo()throws RemoteException, Exception {
		return service.querySameFreqSeqInfo();
	}

}
