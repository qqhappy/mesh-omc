/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.layer2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.McBtsBasicFacade;
import com.xinwei.minas.mcbts.core.facade.layer2.AirlinkFacade;
import com.xinwei.minas.mcbts.core.facade.layer3.NeighbourFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer1.RFConfig;
import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.mcbts.core.model.layer2.SCGChannelConfigItem;
import com.xinwei.minas.mcbts.core.model.layer2.W0ConfigItem;
import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;
import com.xinwei.minas.server.mcbts.service.layer2.AirlinkService;
import com.xinwei.minas.server.mcbts.service.layer3.NeighborService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 空中链路配置基本业务处理
 * 
 * @author jiayi
 * 
 */

@SuppressWarnings("serial")
public class AirlinkFacadeImpl extends UnicastRemoteObject implements
		AirlinkFacade {

	private AirlinkService service;

	public AirlinkFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(AirlinkService.class);
	}

	/**
	 * 查询校准类型配置基本信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public AirlinkConfig queryConfigByMoId(Long moId) throws Exception {
		return service.queryByMoId(moId);
	}

	@Override
	public void config(OperObject operObject, Long moId, AirlinkConfig config)
			throws Exception {
		// 配置空中链路
		service.config(moId, config, false);
	}

	/**
	 * 配置校准类型配置基本信息
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	public void config(Long moId, AirlinkConfig airlinkConfig,
			boolean isSynConfig) throws Exception {
		// 配置空中链路
		service.config(moId, airlinkConfig, isSynConfig);
	}

	/**
	 * 从数据库获取配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public AirlinkConfig queryFromEMS(Long moId) throws Exception {
		return service.queryByMoId(moId);

	}

	/**
	 * 从网元获得配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public AirlinkConfig queryFromNE(Long moId) throws Exception {
		return service.queryFromNE(moId);
	}

	/**
	 * 获得基站默认W0配置值
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public List<W0ConfigItem> getDefaultW0(Long moId) throws Exception {
		return service.getDefaultW0(moId);
	}

	/**
	 * 根据天线类型获得默认W0配置值
	 * 
	 * @param antennaType
	 * @throws Exception
	 */
	public List<W0ConfigItem> getDefaultW0ByAntennaType(int antennaType)
			throws Exception {
		return service.getDefaultW0ByAntennaType(antennaType);
	}

	@Override
	public String validateAirlink(AirlinkConfig airlinkConfig,
			RFConfig rfConfig, GenericBizData clusterConf,
			TConfRemoteBts remoteBts, TConfMBMSBts mbmsBts, McBts bts)
			throws Exception {
		
		return service.validateAirlink(airlinkConfig, rfConfig, clusterConf,
				remoteBts, mbmsBts, bts);
	}



}
