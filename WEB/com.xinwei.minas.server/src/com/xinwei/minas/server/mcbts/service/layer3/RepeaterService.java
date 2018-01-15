/**
 * 
 */
package com.xinwei.minas.server.mcbts.service.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsRepeater;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * @author chenshaohua
 * 
 */
public interface RepeaterService extends ICustomService {

	/**
	 * 查询全部记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<McBtsRepeater> queryByMoId(Long moId) throws Exception;

	/**
	 * 配置基站，保存数据库
	 * 
	 * @param mcBtsRepeaterList
	 */
	public void config(long moId, List<McBtsRepeater> mcBtsRepeaterList)
			throws Exception;

	/**
	 * 删除一条记录
	 * 
	 * @param temp
	 * @throws Exception
	 */
	public void delete(McBtsRepeater temp) throws Exception;

}
