package com.xinwei.minas.mcbts.core.facade.oamManage;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.mcbts.core.model.common.McBtsRfPanelStatus;
import com.xinwei.minas.mcbts.core.model.common.McBtsSateQuery;
import com.xinwei.minas.mcbts.core.model.layer2.TConfResmanagement;

public interface McBtsRfPanelStatusFacade extends
		MoBizFacade<TConfResmanagement> {
	/**
	 * 从网元获得配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception   McBtsRfPanelStatus
	 */
	public McBtsRfPanelStatus queryInfoFromNE(long moId) throws RemoteException,
			Exception;

	/*public List<McBtsSateQuery> queryInfoFromDB(long moId)
			throws RemoteException, Exception;*/

}