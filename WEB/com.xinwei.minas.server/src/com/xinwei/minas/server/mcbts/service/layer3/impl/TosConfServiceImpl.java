/**
 * 
 */
package com.xinwei.minas.server.mcbts.service.layer3.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsTos;
import com.xinwei.minas.mcbts.core.model.layer3.WrappedMcBtsTos;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.server.mcbts.dao.layer3.TosConfDAO;
import com.xinwei.minas.server.mcbts.proxy.layer3.TosConfProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.TosConfService;
import com.xinwei.minas.server.mcbts.utils.McBtsUtils;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * @author chenshaohua
 * 
 */

public class TosConfServiceImpl implements TosConfService {

	private Log log = LogFactory.getLog(TosConfServiceImpl.class);

	private TosConfDAO tosConfDAO;

	private TosConfProxy tosConfProxy;

	public List<McBtsTos> queryAllTos() throws Exception {
		return tosConfDAO.queryAllTos();
	}

	// 向所有网元发送
	public void config(List<McBtsTos> mcBtsTosList) throws Exception {
		WrappedMcBtsTos entity = new WrappedMcBtsTos();
		entity.setMcBtsTosList(mcBtsTosList);
		GenericBizData data = new GenericBizData("mcbts_tos");
		data.addEntity(entity);

		// 保存数据库
		try {
			tosConfDAO.saveOrUpdate(mcBtsTosList);
		} catch (Exception e) {
			log.error("Error saving Tos(QoS):" + e.getLocalizedMessage());
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}

		// 失败的基站及失败信息
		Map<McBts, String> failedMap = new HashMap<McBts, String>();
		// 向所有在线已连接基站下发
		List<McBts> btsList = McBtsCache.getInstance().queryAll();
		for (McBts mcBts : btsList) {
			if (mcBts.isConfigurable()) {
				try {
					tosConfProxy.config(mcBts.getMoId(), data);
				} catch (Exception e) {
					if (e instanceof UnsupportedOperationException) {
						log.error("Error config Tos(QoS):"
								+ e.getLocalizedMessage());
					} else {
						failedMap.put(mcBts, e.getLocalizedMessage());
					}
				}

			}
		}
		// 处理失败信息
		McBtsUtils.fireBatchUpdateException(failedMap);

	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		List<McBtsTos> mcBtsTosList = this.queryAllTos();

		if (mcBtsTosList == null || mcBtsTosList.isEmpty())
			return;

		WrappedMcBtsTos entity = new WrappedMcBtsTos();
		entity.setMcBtsTosList(mcBtsTosList);

		GenericBizData bizData = new GenericBizData("mcbts_tos");
		bizData.addEntity(entity);
		tosConfProxy.config(moId, bizData);
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		// DO NOTHING
	}

	public void setTosConfDAO(TosConfDAO tosConfDAO) {
		this.tosConfDAO = tosConfDAO;
	}

	public void setTosConfProxy(TosConfProxy tosConfProxy) {
		this.tosConfProxy = tosConfProxy;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		// 不需要实现,群配业务

	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// 不需要实现,群配业务

	}

}
