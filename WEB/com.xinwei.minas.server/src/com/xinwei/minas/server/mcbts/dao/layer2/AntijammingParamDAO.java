/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer2;

import com.xinwei.minas.mcbts.core.model.layer2.TConfAntijammingParam;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * @author chenshaohua
 *
 */
public interface AntijammingParamDAO extends GenericDAO<TConfAntijammingParam, Long> {

	public TConfAntijammingParam queryByMoId(Long moId);
	
	public void saveOrUpdate(TConfAntijammingParam entity);
}
