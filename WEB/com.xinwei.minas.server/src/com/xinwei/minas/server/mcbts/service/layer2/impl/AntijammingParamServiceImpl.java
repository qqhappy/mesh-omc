/**
 * 
 */
package com.xinwei.minas.server.mcbts.service.layer2.impl;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer2.TConfAntijammingParam;
import com.xinwei.minas.mcbts.core.model.layer2.TConfFreqSet;
import com.xinwei.minas.server.core.conf.service.MoCache;
import com.xinwei.minas.server.mcbts.dao.layer2.AntijammingParamDAO;
import com.xinwei.minas.server.mcbts.dao.layer2.FreqSetDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.proxy.layer2.FreqSetProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer2.AntijammingParamService;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * @author chenshaohua
 * 
 */
public class AntijammingParamServiceImpl implements AntijammingParamService {
	private Log log = LogFactory.getLog(AntijammingParamServiceImpl.class);
	private McBtsBizProxy mcBtsBizProxy;

	private AntijammingParamDAO antijammingParamDAO;

	private FreqSetProxy freqSetProxy;

	private FreqSetDAO freqSetDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xinwei.minas.mcbts.core.facade.layer2.AntijammingParamFacade#config
	 * (com.xinwei.minas.mcbts.core.model.layer2.TConfAntijamming)
	 */
	public void config(TConfAntijammingParam antijamming)
			throws RemoteException, Exception {
		// TODO Auto-generated method stub

		Long moId = antijamming.getMoId();
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 在线管理状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			GenericBizData data = new GenericBizData("t_conf_antijamming_param");
			data.addEntity(antijamming);
			try {
				mcBtsBizProxy.config(moId, data);
			} catch (Exception e) {
				log.error(e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		try {
			antijammingParamDAO.saveOrUpdate(antijamming);
		} catch (Exception e) {
			log.error(e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xinwei.minas.mcbts.core.facade.layer2.AntijammingParamFacade#queryByMoId
	 * (java.lang.Long)
	 */
	public TConfAntijammingParam queryByMoId(Long moId) throws RemoteException,
			Exception {
		// TODO Auto-generated method stub
		return antijammingParamDAO.queryByMoId(moId);
	}

	public void config(TConfFreqSet freqSetEntity) throws RemoteException,
			Exception {
		Long moId = freqSetEntity.getMoId();
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 在线管理状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			GenericBizData data = new GenericBizData("t_conf_freqSet");
			data.addEntity(freqSetEntity);
			try {
				freqSetProxy.config(moId, data);
			} catch (Exception e) {
				log.error(e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		try {
			freqSetDAO.saveOrUpdate(freqSetEntity);
		} catch (Exception e) {
			log.error(e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}
	}

	public AntijammingParamDAO getAntijammingParamDAO() {
		return antijammingParamDAO;
	}

	public void setAntijammingParamDAO(AntijammingParamDAO antijammingParamDAO) {
		this.antijammingParamDAO = antijammingParamDAO;
	}

	public McBtsBizProxy getMcBtsBizProxy() {
		return mcBtsBizProxy;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	public FreqSetProxy getFreqSetProxy() {
		return freqSetProxy;
	}

	public void setFreqSetProxy(FreqSetProxy freqSetProxy) {
		this.freqSetProxy = freqSetProxy;
	}

	public FreqSetDAO getFreqSetDAO() {
		return freqSetDAO;
	}

	public void setFreqSetDAO(FreqSetDAO freqSetDAO) {
		this.freqSetDAO = freqSetDAO;
	}

}
