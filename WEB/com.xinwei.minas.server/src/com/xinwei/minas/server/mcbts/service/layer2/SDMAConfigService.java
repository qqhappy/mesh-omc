package com.xinwei.minas.server.mcbts.service.layer2;

import com.xinwei.minas.mcbts.core.model.layer2.SDMAConfig;
import com.xinwei.minas.server.mcbts.service.ICustomService;
/**
 * SDMA配置业务层接口
 * 
 * @author fangping
 *
 */
public interface SDMAConfigService extends ICustomService{
	
	/**
	 * 查询SDMA基本信息
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public SDMAConfig queryByMoId(Long moId) throws Exception;
	
	/**
	 * 配置SDMA基本信息
	 * @param loadBalance
	 * @throws Exception
	 */
	public void config(SDMAConfig SDMAConfig) throws Exception;

}
