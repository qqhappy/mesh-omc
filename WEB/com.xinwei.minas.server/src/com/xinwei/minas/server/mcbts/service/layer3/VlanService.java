/**
 * 
 */
package com.xinwei.minas.server.mcbts.service.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlan;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlanAttach;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * @author chenshaohua
 * 
 */
public interface VlanService extends ICustomService {

	/**
	 * 查询全部记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<McBtsVlan> queryAllByMoId(Long moId) throws Exception;

	/**
	 * 配置基站并保存到数据库
	 * 
	 * @param mcBtsVlanList
	 * @throws Exception
	 */
	public void config(List<McBtsVlan> mcBtsVlanList, Long moId)
			throws Exception;

	public McBtsVlanAttach queryAttachByMoId(long moId) throws Exception;

}
