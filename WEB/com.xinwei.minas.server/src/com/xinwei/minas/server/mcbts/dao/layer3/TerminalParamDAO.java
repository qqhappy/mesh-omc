/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer3;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsTos;
import com.xinwei.minas.mcbts.core.model.layer3.TerminalBizParam;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * @author chenshaohua
 * 
 */
public interface TerminalParamDAO extends GenericDAO<TerminalBizParam, Long> {

	// 查询全部终端业务配置
	public List<TerminalBizParam> queryAllTerminalParam()
			throws RemoteException, Exception;

	public void saveOrUpdate(List<TerminalBizParam> terminalBizParamList);
}
