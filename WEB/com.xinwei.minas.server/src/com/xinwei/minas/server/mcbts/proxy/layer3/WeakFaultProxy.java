package com.xinwei.minas.server.mcbts.proxy.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfWeakVoiceFault;
/**
 * 故障弱化业务协议适配器
 * 
 * @author yinbinqiang
 *
 */
public interface WeakFaultProxy {
	/**
	 * 查询网元业务数据
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfWeakVoiceFault query(Long moId, TConfWeakVoiceFault weakVoiceFault) throws Exception;
	
	/**
	 * 配置网元业务数据
	 * 
	 * @param moId
	 * @param weakVoiceFault
	 * @throws Exception
	 */
	public void config(Long moId, TConfWeakVoiceFault weakVoiceFault) throws Exception;
}
