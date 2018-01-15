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

	// 查询全部终端业务配置
	public List<TerminalBizParam> queryAllTerminalParam()
			throws RemoteException, Exception;

	//配置业务
	public void config(List<TerminalBizParam> terminalBizParamList)
			throws RemoteException, Exception;
}
