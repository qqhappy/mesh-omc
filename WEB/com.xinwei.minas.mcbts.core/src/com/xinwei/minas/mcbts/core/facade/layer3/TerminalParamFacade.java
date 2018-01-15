/**
 * 
 */
package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TerminalBizParam;

/**
 * @author chenshaohua
 * 
 */
public interface TerminalParamFacade extends Remote {

	// ��ѯȫ���ն�ҵ������
	public List<TerminalBizParam> queryAllTerminalParam()
			throws RemoteException, Exception;

	//����ҵ��
	public void config(List<TerminalBizParam> terminalBizParamList)
			throws RemoteException, Exception;
}
