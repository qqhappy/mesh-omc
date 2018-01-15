package com.xinwei.minas.server.mcbts.proxy.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfBackupSag;
import com.xinwei.omp.core.model.biz.GenericBizData;
/**
 * 备份SAG业务协议适配器
 * 
 * @author yinbinqiang
 *
 */
public interface SAGParamProxy {
	/**
	 * 查询网元业务数据
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfBackupSag query(Long moId, TConfBackupSag backupSag) throws Exception;
	
	/**
	 * 配置网元业务数据
	 * 
	 * @param moId
	 * @param weakVoiceFault
	 * @throws Exception
	 */
	public void config(Long moId, GenericBizData bizData) throws Exception;
}
