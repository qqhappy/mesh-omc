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
	 * 查询全部终端业务配置
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<TerminalBizParam> queryAllTerminalParam()
			throws RemoteException, Exception;

	/**
	 * 配置终端业务
	 * @param terminalBizParamList
	 * @throws Exception
	 */
	public void config(List<TerminalBizParam> terminalBizParamList)
			throws Exception;
}
