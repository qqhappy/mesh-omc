package com.xinwei.minas.server.mcbts.proxy.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfWeakVoiceFault;
/**
 * ��������ҵ��Э��������
 * 
 * @author yinbinqiang
 *
 */
public interface WeakFaultProxy {
	/**
	 * ��ѯ��Ԫҵ������
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfWeakVoiceFault query(Long moId, TConfWeakVoiceFault weakVoiceFault) throws Exception;
	
	/**
	 * ������Ԫҵ������
	 * 
	 * @param moId
	 * @param weakVoiceFault
	 * @throws Exception
	 */
	public void config(Long moId, TConfWeakVoiceFault weakVoiceFault) throws Exception;
}
