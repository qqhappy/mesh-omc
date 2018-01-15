package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfWeakVoiceFault;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfWeakVoiceFaultDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;
/**
 * 语音故障弱化配置DAO接口实现
 * 
 * @author yinbinqiang
 *
 */
public class TConfWeakVoiceFaultDAOImpl extends
		GenericHibernateDAO<TConfWeakVoiceFault, Long> implements
		TConfWeakVoiceFaultDAO {

	@Override
	public TConfWeakVoiceFault queryByMoid(Long moId) {
		List voiceFaults = getHibernateTemplate().find("from TConfWeakVoiceFault t where t.moId="+moId);
		if(voiceFaults.size() != 0 && voiceFaults != null) {
			return (TConfWeakVoiceFault) voiceFaults.get(0);
		}
		return null;
	}
	
}
