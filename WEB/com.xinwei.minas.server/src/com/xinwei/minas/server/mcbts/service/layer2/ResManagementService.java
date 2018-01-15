package com.xinwei.minas.server.mcbts.service.layer2;

import com.xinwei.minas.mcbts.core.model.layer2.TConfResmanagement;
import com.xinwei.minas.server.mcbts.service.ICustomService;


/**
 * 无线资源管理业务层接口
 * 
 * @author chenshaohua
 *
 */
public interface ResManagementService extends ICustomService {
	
	/**
	 * 查询无线资源信息
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfResmanagement queryByMoId(Long moId) throws Exception;
	
	/**
	 * 配置无线资源信息
	 * @param resManagement
	 * @throws Exception
	 */
	public void config(TConfResmanagement resManagement) throws Exception;
	
	/**
	 * 从网元获得配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfResmanagement queryFromNE(Long moId) throws Exception;
}
