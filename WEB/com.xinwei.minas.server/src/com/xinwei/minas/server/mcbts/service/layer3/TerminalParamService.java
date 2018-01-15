/**
 * 
 */
package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TerminalBizParam;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * @author chenshaohua
 * 
 */
public interface TerminalParamService extends ICustomService{

	
	/**
	 * ��ѯȫ���ն�ҵ������
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<TerminalBizParam> queryAllTerminalParam()
			throws RemoteException, Exception;

	/**
	 * �����ն�ҵ��
	 * @param terminalBizParamList
	 * @throws Exception
	 */
	public void config(List<TerminalBizParam> terminalBizParamList)
			throws Exception;
}
