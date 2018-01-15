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
	 * ��ѯȫ����¼
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<McBtsRepeater> queryByMoId(Long moId) throws Exception;

	/**
	 * ���û�վ���������ݿ�
	 * 
	 * @param mcBtsRepeaterList
	 */
	public void config(long moId, List<McBtsRepeater> mcBtsRepeaterList)
			throws Exception;

	/**
	 * ɾ��һ����¼
	 * 
	 * @param temp
	 * @throws Exception
	 */
	public void delete(McBtsRepeater temp) throws Exception;

}
