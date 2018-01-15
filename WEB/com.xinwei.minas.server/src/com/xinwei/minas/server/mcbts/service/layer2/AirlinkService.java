/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer2;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer1.RFConfig;
import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.mcbts.core.model.layer2.W0ConfigItem;
import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;
import com.xinwei.minas.mcbts.core.model.sysManage.ChannelComparableMode;
import com.xinwei.minas.server.mcbts.service.ICustomService;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * 空中链路配置业务服务接口
 * 
 * @author jiayi
 * 
 */

public interface AirlinkService extends ICustomService{

	/**
	 * 查询空中链路配置信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public AirlinkConfig queryByMoId(Long moId) throws Exception;
	
	
	/**
	 * 查询空中链路配置信息,不根据模式设置默认值
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public AirlinkConfig queryFromEMS(Long moId) throws Exception;

	/**
	 * 配置空中链路配置信息
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	public void config(Long moId, AirlinkConfig airlinkConfig, boolean isSynConfig) throws Exception;

	/**
	 * 从网元获得空中链路配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public AirlinkConfig queryFromNE(Long moId) throws Exception;

	/**
	 * 获得基站默认W0配置值
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public List<W0ConfigItem> getDefaultW0(Long moId) throws Exception;

	/**
	 * 根据天线类型获得默认W0配置值
	 * 
	 * @param antennaType
	 * @throws Exception
	 */
	public List<W0ConfigItem> getDefaultW0ByAntennaType(int antennaType)
			throws Exception;

	/**
	 * 根据兼容模式获得默认空中链路配置值
	 * 
	 * @param mcBts
	 * @param mode
	 * @throws Exception
	 */
	public AirlinkConfig getDefaultConfigByComparableMode(McBts mcBts,
			ChannelComparableMode mode) throws Exception;
	
	
	/**
	 * 校验空中链路信息
	 * @param airlinkConfig
	 * @param rfConfig
	 * @param clusterConf
	 * @param remoteBts
	 * @param mbmsBts
	 * @param bts
	 * @return
	 * @throws Exception
	 */
	public String validateAirlink(AirlinkConfig airlinkConfig,
			RFConfig rfConfig, GenericBizData clusterConf,
			TConfRemoteBts remoteBts, TConfMBMSBts mbmsBts, McBts bts)
			throws Exception;
}
