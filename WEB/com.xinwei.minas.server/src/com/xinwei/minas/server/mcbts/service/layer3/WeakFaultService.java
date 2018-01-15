package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer3.TConfFaultSwitch;
import com.xinwei.minas.mcbts.core.model.layer3.TConfWeakVoiceFault;
import com.xinwei.minas.server.mcbts.service.ICustomService;
/**
 * 故障弱化业务层接口
 * 
 * @author yinbinqiang
 *
 */
public interface WeakFaultService extends ICustomService{
	/**
	 * 查询故障弱化开关配置基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfFaultSwitch querySwitchByMoId(Long moId) throws Exception;
	
	/**
	 * 查询语音故障弱化配置基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfWeakVoiceFault queryVoiceByMoId(Long moId) throws Exception;
	
	/**
	 * 配置故障弱化基本信息
	 * @param faultSwitch
	 * @param weakVoiceFault
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfFaultSwitch faultSwitch, TConfWeakVoiceFault weakVoiceFault) throws Exception;
	
	/**
	 * 从网元查询故障弱化开关配置基本信息
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfFaultSwitch querySwitchFromNE(Long moId) throws Exception;
	
	/**
	 * 从网元查询语音故障弱化配置基本信息
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfWeakVoiceFault queryVoiceFromNE(Long moId) throws Exception;
	
}
