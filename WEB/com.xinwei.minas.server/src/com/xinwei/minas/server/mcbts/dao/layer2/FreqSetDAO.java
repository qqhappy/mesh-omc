/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer2;

import com.xinwei.minas.mcbts.core.model.layer2.TConfFreqSet;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * @author chenshaohua
 * 
 */
public interface FreqSetDAO extends GenericDAO<TConfFreqSet, Long> {

	public TConfFreqSet queryByMoId(Long moId);

	public void saveOrUpdate(TConfFreqSet entity);

}
