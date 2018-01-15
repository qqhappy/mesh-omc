/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer2;

import com.xinwei.minas.mcbts.core.model.layer2.TConfChangeAlgParam;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * @author chenshaohua
 *
 */
public interface ChangeAlgParamDAO extends GenericDAO<TConfChangeAlgParam, Long> {
	
	public TConfChangeAlgParam queryByMoId(Long moId);
	

}
