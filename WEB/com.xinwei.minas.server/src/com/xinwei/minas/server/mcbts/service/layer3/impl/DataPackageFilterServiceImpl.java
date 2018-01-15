/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-19	| fangping 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.service.layer3.impl;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.SystemProperty;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.DataPackageFilter;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.server.core.conf.service.SystemPropertyService;
import com.xinwei.minas.server.mcbts.dao.layer3.DataPackageFilterDAO;
import com.xinwei.minas.server.mcbts.proxy.layer3.DataPackageFilterProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.DataPackageFilterService;
import com.xinwei.minas.server.mcbts.utils.McBtsUtils;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * service实现
 * 
 * @author fangping
 * 
 */
public class DataPackageFilterServiceImpl implements DataPackageFilterService {
	private Log log = LogFactory.getLog(DataPackageFilterServiceImpl.class);
	private SequenceService sequenceService;

	private SystemPropertyService systemPropertyService;

	private DataPackageFilterDAO dataPackageFilterDAO;

	private DataPackageFilterProxy dataPackageFilterProxy;

	private static final String CATEGORY = "platform";

	private static final String PROPERTY = "data_package_filter_mode";

	public DataPackageFilterServiceImpl() {
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public int queryFilterType() {
		systemPropertyService = AppContext.getCtx().getBean(
				SystemPropertyService.class);
		SystemProperty prop = systemPropertyService.getProperty(CATEGORY, null,
				PROPERTY);
		return Integer.parseInt(prop.getValue());
	}

	@Override
	public List<DataPackageFilter> queryAllFromEMS() throws RemoteException,
			Exception {
		return dataPackageFilterDAO.queryAll();
	}

	@Override
	public Object[] queryFromNE(Long moId) throws RemoteException, Exception {
		return dataPackageFilterProxy.queryFromNE(moId);
	}

	// 配置数据包过滤规则给数据库,并判断是否下发给所有基站
	@Override
	public void config(int filterType, List<DataPackageFilter> filterList)
			throws Exception {
		if (filterList != null) {
			// set idx
			for (DataPackageFilter dataPackageFilter : filterList) {
				dataPackageFilter.setIdx(sequenceService.getNext());
			}
		}

		// 存库
		systemPropertyService = AppContext.getCtx().getBean(
				SystemPropertyService.class);
		systemPropertyService.setProperty(CATEGORY, null, PROPERTY,
				String.valueOf(filterType));
		dataPackageFilterDAO.saveOrUpdate(filterList);

		// 失败的基站及失败信息
		Map<McBts, String> failedMap = new HashMap<McBts, String>();
		// 获取所有基站
		List<McBts> btsList = McBtsCache.getInstance().queryAll();
		for (McBts mcbts : btsList) {
			if (!mcbts.isConfigurable()) {
				continue;
			}

			try {
				config(mcbts.getMoId(), filterType, filterList);
			} catch (Exception e) {
				failedMap.put(mcbts, e.getLocalizedMessage());
			}
		}
		// 处理失败信息
		McBtsUtils.fireBatchUpdateException(failedMap);
	}

	// 配置基站的数据包过滤规则
	@Override
	public void config(Long moId, int filterType,
			List<DataPackageFilter> filterList) throws RemoteException,
			Exception {

		// 获取MO的维护状态
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		if (mcBts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 下发
		try {
			if (mcBts != null && mcBts.isConfigurable()) {
				dataPackageFilterProxy.config(moId, filterType, filterList);
			}

		} catch (UnsupportedOperationException e) {
			log.error(OmpAppContext.getMessage("unsupported_biz_operation")
					+ ":" + "Data package filter push down");
		} catch (Exception e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_config_failed_reason")
							+ e.getLocalizedMessage());
		}
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		int filterType = queryFilterType();
		List<DataPackageFilter> filterList = queryAllFromEMS();
		this.config(moId, filterType, filterList);
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		// do nothing
	}

	public void setDataPackageFilterDAO(
			DataPackageFilterDAO dataPackageFilterDAO) {
		this.dataPackageFilterDAO = dataPackageFilterDAO;
	}

	public void setDataPackageFilterProxy(
			DataPackageFilterProxy dataPackageFilterProxy) {
		this.dataPackageFilterProxy = dataPackageFilterProxy;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
