package com.xinwei.minas.server.mcbts.dao.layer3;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.DataPackageFilter;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;
import com.xinwei.omp.core.model.biz.GenericBizData;

public interface DataPackageFilterDAO extends
		GenericDAO<DataPackageFilter, Long> {

	/**
	 * Ìí¼ÓOR¸üÐÂ
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(List<DataPackageFilter> entity);

}
