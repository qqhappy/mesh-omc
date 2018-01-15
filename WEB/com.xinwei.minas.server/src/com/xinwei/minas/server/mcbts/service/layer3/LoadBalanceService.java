package com.xinwei.minas.server.mcbts.service.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfLoadBalance;
import com.xinwei.minas.server.mcbts.service.ICustomService;
/**
 * 负载均衡配置业务层接口
 * 
 * @author yinbinqiang
 *
 */
public interface LoadBalanceService extends ICustomService{
	
	/**
	 * 查询负载均衡基本信息
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfLoadBalance queryByMoId(Long moId) throws Exception;
	
	/**
	 * 配置负载均衡基本信息
	 * @param loadBalance
	 * @throws Exception
	 */
	public void config(TConfLoadBalance loadBalance) throws Exception;

}
