package com.xinwei.minas.server.mcbts.dao.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfWeakVoiceFault;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;
/**
 * ����������������DAO�ӿ�
 * 
 * @author yinbinqiang
 *
 */
public interface TConfWeakVoiceFaultDAO extends GenericDAO<TConfWeakVoiceFault, Long> {
	/**
	 * ����moId�����������������Ϣ
	 * 
	 * @param moId
	 * @return
	 */
	public TConfWeakVoiceFault queryByMoid(Long moId);
}
