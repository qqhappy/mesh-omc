/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsACL;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * @author chenshaohua
 * 
 */
public interface ACLDAO extends GenericDAO<McBtsACL, Long> {

	/**
	 * ��ѯ����ʵ��
	 */
	public List<McBtsACL> queryAll();

	/**
	 * ����moid��ѯȫ��ʵ��
	 * 
	 * @return
	 */
	public List<McBtsACL> queryByMoId(Long moId);

	/**
	 * ���ӻ����ʵ��
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(List<McBtsACL> mcBtsACLList, Long moId);

	/**
	 * ɾ������ACL��¼
	 * 
	 * @param moId
	 */
	public void deleteAll(Long moId);
}
