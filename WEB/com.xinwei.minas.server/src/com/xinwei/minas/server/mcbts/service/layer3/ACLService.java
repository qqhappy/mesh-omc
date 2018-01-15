/**
 * 
 */
package com.xinwei.minas.server.mcbts.service.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsACL;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * @author chenshaohua
 * 
 */
public interface ACLService extends ICustomService {

	/**
	 * 查询全部实体
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<McBtsACL> queryByMoId(Long moId) throws Exception;

	/**
	 * 向基站配置，并保存
	 * 
	 * @param mcBtsACLList
	 * @throws Exception
	 */
	public void config(Long moId, List<McBtsACL> mcBtsACLList) throws Exception;

	/**
	 * 删除一条记录
	 * 
	 * @param temp
	 * @throws Exception
	 */
	public void delete(McBtsACL temp) throws Exception;
}
