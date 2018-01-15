package com.xinwei.minas.server.mcbts.dao.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfWeakVoiceFault;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;
/**
 * 语音故障弱化配置DAO接口
 * 
 * @author yinbinqiang
 *
 */
public interface TConfWeakVoiceFaultDAO extends GenericDAO<TConfWeakVoiceFault, Long> {
	/**
	 * 根据moId获得语音故障弱化信息
	 * 
	 * @param moId
	 * @return
	 */
	public TConfWeakVoiceFault queryByMoid(Long moId);
}
