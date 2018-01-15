package com.xinwei.minas.server.mcbts.dao.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfBackupSag;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * ����SAG��������DAO�ӿ�
 * 
 * @author yinbinqiang
 *
 */
public interface TConfBackupSagDAO extends GenericDAO<TConfBackupSag, Long> {
	/**
	 * �õ�����SAG����������Ϣ
	 * @param moid
	 * @return
	 */
	public TConfBackupSag queryByMoId(Long moid);
}
