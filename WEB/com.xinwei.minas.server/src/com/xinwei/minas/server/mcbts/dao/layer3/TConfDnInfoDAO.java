package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfDnInfo;
import com.xinwei.minas.mcbts.core.model.layer3.TConfDnInfoPK;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * �������������ú�����ϢDAO�ӿ�
 * 
 * @author yinbinqiang
 *
 */
public interface TConfDnInfoDAO extends GenericDAO<TConfDnInfo, TConfDnInfoPK> {
	/**
	 * ����moid�õ�����ʵ��
	 * 
	 * @param moid
	 * @return
	 */
	public List<TConfDnInfo> queryByMoid(Long moid);
	
	/**
	 * �洢���ú������ݼ�
	 * 
	 * @param dnInfos
	 */
	public void saveDnInfoList(List<TConfDnInfo> dnInfos); 
}
