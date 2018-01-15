package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.TConfFaultSwitch;
import com.xinwei.minas.mcbts.core.model.layer3.TConfWeakVoiceFault;
import com.xinwei.minas.mcbts.core.model.layer3.TConfWeakVoiceFaultWithSwitch;
/**
 * 故障弱化基本业务门面
 * 
 * @author yinbinqiang
 *
 */
public interface WeakFaultFacade extends MoBizFacade<TConfWeakVoiceFaultWithSwitch> {
	/**
	 * 查询故障弱化开关配置基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfFaultSwitch querySwitchByMoId(Long moId) throws RemoteException, Exception;
	
	/**
	 * 查询语音故障弱化配置基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfWeakVoiceFault queryVoiceByMoId(Long moId) throws RemoteException, Exception;
	
	/**
	 * 配置故障弱化基本信息
	 * @param faultSwitch
	 * @param weakVoiceFault
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfFaultSwitch faultSwitch, TConfWeakVoiceFault weakVoiceFault) throws RemoteException, Exception;
}
