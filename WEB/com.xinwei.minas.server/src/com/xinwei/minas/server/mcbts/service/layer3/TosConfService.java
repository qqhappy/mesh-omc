/**
 * 
 */
package com.xinwei.minas.server.mcbts.service.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsTos;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * @author chenshaohua
 * 
 */

public interface TosConfService extends ICustomService {

	/**
	 * ҵ������ȫ��tos����
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<McBtsTos> queryAllTos() throws Exception;

	/**
	 * ҵ�������tos
	 * 
	 * @param mcBtsTosList
	 * @throws Exception
	 */
	public void config(List<McBtsTos> mcBtsTosList) throws Exception;

}
