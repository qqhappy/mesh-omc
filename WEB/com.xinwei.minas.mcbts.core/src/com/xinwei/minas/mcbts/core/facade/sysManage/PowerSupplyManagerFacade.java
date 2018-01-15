/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-18	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.util.HashSet;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.PowerSupply;

/**
 * 
 * 电源管理门面接口
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface PowerSupplyManagerFacade extends Remote {
	/**
	 * 保存电源信息
	 * @param data
	 */
	public void savePowerSupplyInfo(PowerSupply data) throws Exception;
	
	/**
	 * 查询所有电源信息 
	 * @return
	 */
	public List<PowerSupply> queryAll() throws Exception;
	/**
	 * 删除电源信息
	 */
	public void delPowerSupplyInfo(PowerSupply powerSupply) throws Exception;
	/**
	 * 根据基站MoId查询电源基本类（PowerSupply）
	 */
	public PowerSupply querybyMcBtsMoId(long moid)throws Exception;
	/**
	 * 保存基站和电源的对应信息关系；
	 */
	public void savePowerSupplyAndMcbtsRelation(PowerSupply powerSupply) throws Exception;
	/**
	 * 根据电源信息查询对应基站的moId集合
	 */
	public HashSet<Long> queryMcbtsByPowerSupply(PowerSupply powerSupply)throws Exception;
	/**
	 * 重新加载电源监控服务
	 * @param pollInterval
	 */
	public void reloadPowerSupplyServices(int pollInterval)throws Exception;;
}
