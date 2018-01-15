package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItem;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItemPK;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * rcpe uid信息DAO类
 * 
 * @author yinbinqiang
 */
public interface TConfRCPEItemDAO extends
		GenericDAO<TConfRCPEItem, TConfRCPEItemPK> {
	/**
	 * 通过moId查询rcpe uid信息
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<TConfRCPEItem> queryByMoId(Long moId) throws Exception;

	/**
	 * 删除所有rcpe配置
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void removeAll(Long moId) throws Exception;

	/**
	 * 存储rcpe uid信息
	 * 
	 * @param rcpeItems
	 * @throws Exception
	 */
	public void saveRCPEItems(List<TConfRCPEItem> rcpeItems) throws Exception;
}
