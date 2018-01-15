package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer3.TConfFaultSwitch;
import com.xinwei.minas.mcbts.core.model.layer3.TConfWeakVoiceFault;
import com.xinwei.minas.server.mcbts.service.ICustomService;
/**
 * ��������ҵ���ӿ�
 * 
 * @author yinbinqiang
 *
 */
public interface WeakFaultService extends ICustomService{
	/**
	 * ��ѯ���������������û�����Ϣ
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfFaultSwitch querySwitchByMoId(Long moId) throws Exception;
	
	/**
	 * ��ѯ���������������û�����Ϣ
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfWeakVoiceFault queryVoiceByMoId(Long moId) throws Exception;
	
	/**
	 * ���ù�������������Ϣ
	 * @param faultSwitch
	 * @param weakVoiceFault
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfFaultSwitch faultSwitch, TConfWeakVoiceFault weakVoiceFault) throws Exception;
	
	/**
	 * ����Ԫ��ѯ���������������û�����Ϣ
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfFaultSwitch querySwitchFromNE(Long moId) throws Exception;
	
	/**
	 * ����Ԫ��ѯ���������������û�����Ϣ
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfWeakVoiceFault queryVoiceFromNE(Long moId) throws Exception;
	
}
