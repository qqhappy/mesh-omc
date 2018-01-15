package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.SysFreqFacade;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlan;
import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqModule;
import com.xinwei.minas.server.mcbts.service.layer3.LoadBalanceService;
import com.xinwei.minas.server.mcbts.service.layer3.SysFreqService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 实现方法
 * 
 * @author liuzhongyan
 * 
 */
public class SysFreqFacadeImpl extends UnicastRemoteObject implements
		SysFreqFacade {
	private SysFreqService service;

	protected SysFreqFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(SysFreqService.class);

	}

	/**
	 * 从基站查询基站的有效频点
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public Object[] queryDataFromBts(long moId) throws RemoteException,
			Exception {
		return service.queryDataFromBts(moId);
	}

	/**
	 * 获取所有基站的频点的汇总
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TSysFreqModule> queryUsedFreqFromBts() throws Exception,
			RemoteException {
		return service.queryUsedFreqFromBts();
	}

	@Override
	public void configBtsData(Long moId, List<TSysFreqModule> sysFreqList)
			throws RemoteException, Exception {
		service.configBtsData(moId, sysFreqList);

	}

	/**
	 * 配置系统频点数据
	 * 
	 */
	@Override
	public void configData(OperObject operObject, Long moId, List<TSysFreqModule> sysFreqList,
			int freqSwitch, boolean isConfig) throws RemoteException, Exception {

		service.configData(moId, sysFreqList, freqSwitch, isConfig);

	}

	/**
	 * 查询系统频点数据
	 * 
	 */
	@Override
	public List<TSysFreqModule> queryData(int freqType) throws RemoteException,
			Exception {
		return service.queryData(freqType);
	}

	/**
	 * 配置系统频点数据标识
	 * 
	 */
	@Override
	public int queryFreqSwitch() throws RemoteException, Exception {
		return service.queryFreqSwitch();
	}

	/**
	 * 配置系统频点数据标识
	 * 
	 */
	@Override
	public void configFreqSwitch(int freqSwitch) throws RemoteException,
			Exception {
		service.configFreqSwitch(freqSwitch);

	}

	@Override
	public List<TSysFreqModule> queryAllData() throws RemoteException,
			Exception {
		return service.queryAllData();
	}

	/**
	 * 删除指定的系统有效频点
	 * @param freq
	 * @throws Exception
	 */
	@Override
	public void deleteData(OperObject operObject, TSysFreqModule freq) throws Exception {
		service.deleteData(freq);
	}
	
	/**
	 * 添加指定的系统有效频点
	 * @param freq
	 * @throws Exception
	 */
	@Override
	public void saveData(OperObject operObject, TSysFreqModule freq) throws Exception {
		service.saveData(freq);
	}
	
	/**
	 * 更新指定的系统有效频点
	 * @param freq
	 * @throws Exception
	 */
	public void updateData(OperObject operObject, TSysFreqModule freq) throws Exception {
		service.updateData(freq);
	}

	@Override
	public void saveAllData(OperObject operObject, List<TSysFreqModule> freqs)
			throws Exception {
		service.saveAllData(freqs);
	}
}
