/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsACL;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsRepeater;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * @author chenshaohua
 * 
 */
public interface RepeaterDAO extends GenericDAO<McBtsRepeater, Long> {

	/**
	 * ��ѯ���м�¼
	 */
	public List<McBtsRepeater> queryByMoId(Long moId);

	/**
	 * ����ʵ��
	 */
	public void saveOrUpdate(List<McBtsRepeater> mcBtsRepeaterList, Long moId);
}
