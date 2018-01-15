package com.xinwei.minas.server.mcbts.dao.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPE;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

public interface TConfRCPEDAO extends GenericDAO<TConfRCPE, Long> {
	/**
	 * ≤È—ØRCPE≈‰÷√
	 * @param moid
	 * @return
	 */
	public TConfRCPE queryByMoId(Long moId);
}
