package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfDnInfo;
import com.xinwei.minas.mcbts.core.model.layer3.TConfDnInfoPK;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 故障弱化中配置号码信息DAO接口
 * 
 * @author yinbinqiang
 *
 */
public interface TConfDnInfoDAO extends GenericDAO<TConfDnInfo, TConfDnInfoPK> {
	/**
	 * 根据moid得到所需实体
	 * 
	 * @param moid
	 * @return
	 */
	public List<TConfDnInfo> queryByMoid(Long moid);
	
	/**
	 * 存储配置号码数据集
	 * 
	 * @param dnInfos
	 */
	public void saveDnInfoList(List<TConfDnInfo> dnInfos); 
}
