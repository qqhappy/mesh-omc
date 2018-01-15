package com.xinwei.minas.server.mcbts.proxy.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPE;

/**
 * rcpe业务协议适配器
 * 
 * @author yinbinqiang
 */
public interface RCPEProxy {
	/**
	 * 查询网元业务数据
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfRCPE query(Long moId) throws Exception;

	/**
	 * 配置网元业务数据
	 * 
	 * @param moId
	 * @param rcpe
	 * @throws Exception
	 */
	public void config(Long moId, TConfRCPE rcpe) throws Exception;
}
