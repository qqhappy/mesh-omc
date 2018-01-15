/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-18	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.util.HashSet;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.PowerSupply;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * 电源管理服务接口
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author zhuxiaozhan
 * 
 */

public interface PowerSupplyManagerService extends ICustomService {
	/**
	 * 保存电源信息
	 * 
	 * @param data
	 */
	public void savePowerSupplyInfo(PowerSupply data);

	/**
	 * 查询所有电源信息
	 * 
	 * @return
	 */
	public List<PowerSupply> queryAll();

	/**
	 * 删除电源信息
	 * 
	 * @param powerSupply
	 */
	public void delPowerSupplyInfo(PowerSupply powerSupply);

	/**
	 * 根据基站MoId查询电源基本类（PowerSupply）
	 */
	public PowerSupply querybyMcBtsMoId(long moid);

	/**
	 * 保存基站和电源的对应信息关系；
	 */
	public void savePowerSupplyAndMcbtsRelation(PowerSupply powerSupply);

	/**
	 * 根据电源信息查询对应基站的moId集合
	 */
	public HashSet<Long> queryMcbtsByPowerSupply(PowerSupply powerSupply);

	/**
	 * 重新加载电源监控服务
	 * 
	 * @param pollInterval
	 */
	public void reloadPowerSupplyServices(int pollInterval);
}
