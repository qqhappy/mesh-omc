/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsTos;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * @author chenshaohua
 * 
 */
public interface TosConfDAO extends GenericDAO<McBtsTos, Long> {

	public List<McBtsTos> queryAllTos();

	public void saveOrUpdate(List<McBtsTos> mcBtsTosList);

}
