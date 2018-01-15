/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.layer1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer1.CalibrationDataFacade;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationDataInfo;
import com.xinwei.minas.server.mcbts.service.layer1.CalibrationDataService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * У׼�������û���ҵ����
 * 
 * @author chenjunhua
 * 
 */

@SuppressWarnings("serial")
public class CalibrationDataFacadeImpl extends UnicastRemoteObject implements
		CalibrationDataFacade {

	private CalibrationDataService service;

	public CalibrationDataFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(CalibrationDataService.class);
	}

	/**
	 * ��ѯУ׼�������û�����Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	@Override
	public CalibrationDataInfo queryByMoId(Long moId) throws Exception {
		return service.queryCalibrationDataConfigByMoId(moId);
	}


	@Override
	public void config(OperObject operObject, Long moId, CalibrationDataInfo data) throws Exception {
		service.config(moId, data, false);
	}

	@Override
	public void config(Long moId, CalibrationDataInfo data, boolean isSyncConfig)
			throws Exception {
		service.config(moId, data, isSyncConfig);
	}

	/**
	 * �����ݿ��ȡ������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public CalibrationDataInfo queryFromEMS(Long moId) throws RemoteException,
			Exception {
		return service.queryCalibrationDataConfigByMoId(moId);
	}

	/**
	 * ����Ԫ���������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public CalibrationDataInfo queryFromNE(Long moId) throws RemoteException,
			Exception {
		return service.queryDeviceCalibrationDataConfigByMoId(moId);
	}
}
