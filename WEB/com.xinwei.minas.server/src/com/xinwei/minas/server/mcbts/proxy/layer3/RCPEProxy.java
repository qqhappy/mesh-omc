package com.xinwei.minas.server.mcbts.proxy.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPE;

/**
 * rcpeҵ��Э��������
 * 
 * @author yinbinqiang
 */
public interface RCPEProxy {
	/**
	 * ��ѯ��Ԫҵ������
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfRCPE query(Long moId) throws Exception;

	/**
	 * ������Ԫҵ������
	 * 
	 * @param moId
	 * @param rcpe
	 * @throws Exception
	 */
	public void config(Long moId, TConfRCPE rcpe) throws Exception;
}
