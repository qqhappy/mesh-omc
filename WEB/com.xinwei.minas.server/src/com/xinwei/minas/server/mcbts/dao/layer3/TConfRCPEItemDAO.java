package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItem;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItemPK;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * rcpe uid��ϢDAO��
 * 
 * @author yinbinqiang
 */
public interface TConfRCPEItemDAO extends
		GenericDAO<TConfRCPEItem, TConfRCPEItemPK> {
	/**
	 * ͨ��moId��ѯrcpe uid��Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<TConfRCPEItem> queryByMoId(Long moId) throws Exception;

	/**
	 * ɾ������rcpe����
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void removeAll(Long moId) throws Exception;

	/**
	 * �洢rcpe uid��Ϣ
	 * 
	 * @param rcpeItems
	 * @throws Exception
	 */
	public void saveRCPEItems(List<TConfRCPEItem> rcpeItems) throws Exception;
}
