/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-2	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.tools;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.mcbts.core.facade.tools.McBtsDiagToolFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.tools.BtsDiagParameter;
import com.xinwei.minas.server.mcbts.service.McBtsBasicService;
import com.xinwei.minas.server.mcbts.service.McBtsBizService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ��վ������
 * 
 * @author tiance
 * 
 */

public class McBtsDiagToolFacadeImpl extends UnicastRemoteObject implements
		McBtsDiagToolFacade {

	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	private McBtsBizService bizService;
	private McBtsBasicService mcBtsService;

	public McBtsDiagToolFacadeImpl() throws RemoteException {
		super();
		bizService = AppContext.getCtx().getBean(McBtsBizService.class);
		mcBtsService = AppContext.getCtx().getBean(McBtsBasicService.class);
	}

	@Override
	public BtsDiagParameter queryBtsDiagParameter(Mo mo)
			throws RemoteException, Exception {
		BtsDiagParameter btsDiagParameter = new BtsDiagParameter();

		GenericBizData genericBizData = null;

		// ��ȡ��վ��Ϣ
		genericBizData = bizService.queryAllBy(mo.getMoId(), "mcbts_telnet");
		McBts mcBts = mcBtsService.queryByMoId(mo.getMoId());

		// ͨ��telnet����,��ȡ�û���������
		GenericBizProperty userProp = genericBizData.getProperty(USERNAME);

		if (userProp == null) {
			throw new Exception(
					OmpAppContext.getMessage("telnet_info_retrieve_failed"));
		}

		btsDiagParameter.setName((String) userProp.getValue());

		GenericBizProperty pwdProp = genericBizData.getProperty(PASSWORD);
		btsDiagParameter.setPassword((String) pwdProp.getValue());

		// ͨ��service��ȡMcBts���ʵ��
		if (mcBts.getPublicIp() != null)
			btsDiagParameter.setIp(mcBts.getPublicIp());
		if (mcBts.getPublicPort() != 0)
			btsDiagParameter.setPort(mcBts.getPublicPort());

		// TODO α����,Ҫɾ��, �������Ҫ����,�������±ߵĴ���
		// btsDiagParameter.setName("ems");
		// btsDiagParameter.setPassword("12345678");
		// btsDiagParameter.setIp("172.16.6.220");
		// btsDiagParameter.setIp("172.16.24.253");
		// btsDiagParameter.setPort(8002);

		return btsDiagParameter;
	}

}
