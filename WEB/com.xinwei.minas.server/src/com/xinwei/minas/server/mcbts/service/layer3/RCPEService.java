package com.xinwei.minas.server.mcbts.service.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPE;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItem;
import com.xinwei.minas.server.mcbts.service.ICustomService;
/**
 * rcpe配置基本业务层接口
 * @author yinbinqiang
 *
 */
public interface RCPEService extends ICustomService{
	/**
	 * RCPE均衡基本信息
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfRCPE queryByMoId(Long moId) throws Exception;
	
	/**
	 * 配置RCPE基本信息
	 * @param loadBalance
	 * @throws Exception
	 */
	public void config(TConfRCPE rcpe) throws Exception;
	
	/**
	 * 删除指定RCPE信息
	 * @param rcpeItem
	 * @throws Exception
	 */
	public void deleteRcpe(TConfRCPEItem rcpeItem) throws Exception;
}
