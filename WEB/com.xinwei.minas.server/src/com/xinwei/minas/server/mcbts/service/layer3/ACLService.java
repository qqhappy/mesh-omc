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
	 * ��ѯȫ��ʵ��
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<McBtsACL> queryByMoId(Long moId) throws Exception;

	/**
	 * ���վ���ã�������
	 * 
	 * @param mcBtsACLList
	 * @throws Exception
	 */
	public void config(Long moId, List<McBtsACL> mcBtsACLList) throws Exception;

	/**
	 * ɾ��һ����¼
	 * 
	 * @param temp
	 * @throws Exception
	 */
	public void delete(McBtsACL temp) throws Exception;
}
