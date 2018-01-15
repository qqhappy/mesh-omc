/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.layer2;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer1.RFConfig;
import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.mcbts.core.model.layer2.W0ConfigItem;
import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * 空中链路配置业务门面
 * 
 * @author jiayi
 * 
 */

public interface AirlinkFacade extends MoBizFacade<AirlinkConfig> {

	/**
	 * 查询空中链路配置信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public AirlinkConfig queryConfigByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * 配置空中链路基本信息
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Long moId, AirlinkConfig config)
			throws RemoteException, Exception;

	/**
	 * 配置空中链路基本信息(网规信息导入时使用)
	 * 
	 * @param moId
	 * @param config
	 * @param isSyncConfig
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(Long moId, AirlinkConfig config, boolean isSyncConfig)
			throws RemoteException, Exception;

	/**
	 * 获得基站默认W0配置值
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public List<W0ConfigItem> getDefaultW0(Long moId) throws RemoteException,
			Exception;

	/**
	 * 根据天线类型获得默认W0配置值
	 * 
	 * @param antennaType
	 * @throws Exception
	 */
	public List<W0ConfigItem> getDefaultW0ByAntennaType(int antennaType)
			throws RemoteException, Exception;

	/**
	 * 校验空中链路相关业务属性是否合法(参见《邻接表配置规则说明（2013最终版）》)
	 * 
	 * @param conf
	 * @param rfConfig
	 * @param clusterConf
	 * @param mbmsBts
	 * @param bts
	 * @throws RemoteException
	 * @throws Exception
	 */
	public String validateAirlink(AirlinkConfig airlinkConfig,
			RFConfig rfConfig, GenericBizData clusterConf,
			TConfRemoteBts remoteBts, TConfMBMSBts mbmsBts, McBts bts)
			throws RemoteException, Exception;
}
