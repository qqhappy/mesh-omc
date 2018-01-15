/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common.impl;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.common.PerfLogConfigFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.PerfLogConfig;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.McBtsBizDAO;
import com.xinwei.minas.server.mcbts.proxy.common.RealTimePerfProxy;
import com.xinwei.minas.server.mcbts.service.McBtsBizService;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.common.PerfLogConfigService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ������־���÷���ʵ��
 * 
 * @author fanhaoyu
 * 
 */

public class PerfLogConfigServiceImpl implements PerfLogConfigService {

	private static Log log = LogFactory.getLog(PerfLogConfigServiceImpl.class);

	public static final String PERFLOG_BIZ_NAME = "mcbts_perfLog";

	public static final String REALTIMEPERF_BIZ_NAME = "mcbts_realtimeperf";

	private McBtsBizDAO mcBtsBizDAO;

	private McBtsBizService mcBtsBizService;

	private SequenceService sequenceService;

	private RealTimePerfProxy realTimePerfProxy;

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		List<GenericBizData> dataList = mcBtsBizDAO
				.queryExportList(PERFLOG_BIZ_NAME);

		parseBizDataToCell(business, dataList);
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// ��û�վģ��
		long btsId = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);

		// ��������
		PerfLogConfig perfLog = this.queryByMoId(mcBts.getMoId());
		if (perfLog == null) {
			perfLog = new PerfLogConfig();
			perfLog.setMoId(mcBts.getMoId());
		}

		// ������
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value)) {
				return;
			}

			if (key.equals("ftpServerIp")) {
				perfLog.setFtpServerIp(value);
			} else if (key.equals("ftpServerPort")) {
				perfLog.setFtpServerPort(Integer.parseInt(value));
			} else if (key.equals("userName")) {
				perfLog.setUserName(value);
			} else if (key.equals("password")) {
				perfLog.setPassword(value);
			} else if (key.equals("reportInterval")) {
				perfLog.setReportInterval(Integer.parseInt(value));
			} else if (key.equals("collectInterval")) {
				perfLog.setCollectInterval(Integer.parseInt(value));
			}
		}

		// �������
		PerfLogConfigFacade facade = AppContext.getCtx().getBean(
				PerfLogConfigFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, mcBts.getMoId(), perfLog);

	}

	/**
	 * GenericBizDataģ��������䵽Cell��
	 * 
	 * @param business
	 * @param dataList
	 */
	private void parseBizDataToCell(Business business,
			List<GenericBizData> dataList) {
		Set<String> cellNames = business.getCellNames();

		// �������л�վ������
		for (GenericBizData bizData : dataList) {
			GenericBizProperty moIdProperty = bizData.getProperty("moId");
			long moId = Long.parseLong(String.valueOf(moIdProperty.getValue()));

			// ����Ԫ����
			for (String cellName : cellNames) {
				// ��bizData��ȡmoId
				GenericBizProperty property = bizData.getProperty(cellName);

				if (property == null)
					continue;

				// ��ȡԪ����
				Cell cell = business.getCell(cellName);
				// ��ֵ,����"name":"value",JSON��ʽ
				cell.putContent(
						moId,
						"\"" + cellName + "\":\""
								+ String.valueOf(property.getValue()) + "\"");
			}
		}
	}

	@Override
	public PerfLogConfig queryByMoId(Long moId) throws RemoteException,
			Exception {
		GenericBizData data = mcBtsBizDAO.queryAllBy(moId, PERFLOG_BIZ_NAME,
				null, new Object[0]);
		PerfLogConfig config = new PerfLogConfig();
		data.getModel(config);
		return config;
	}

	@Override
	public void config(Long moId, PerfLogConfig config) throws RemoteException,
			Exception {
		// ����������־������Ϣ
		GenericBizData data = createPerfLogData(config);
		Set<String> primaryKeys = new HashSet<String>();
		if (config.getIdx() == null) {
			config.setIdx(sequenceService.getNext());
		}
		primaryKeys.add("idx");
		data.setPrimaryKeys(primaryKeys);
		mcBtsBizService.config(moId, data);

		// ��ȡMO��ά��״̬
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		if (bts != null && bts.isConfigurable()) {
			// ���߹���״̬����Ҫ����Ԫ������Ϣ
			try {
				// ���ͻ�վʵʱ���ܼ��������Ϣ�����账��������
				GenericBizData data2 = new GenericBizData(REALTIMEPERF_BIZ_NAME);
				data2.addProperty(new GenericBizProperty("perfIndex", 0));
				data2.addProperty(new GenericBizProperty("ifPeriod", config
						.getRealTimeInterval()));

				realTimePerfProxy.queryAsync(moId, data2);
			} catch (UnsupportedOperationException e) {
				log.error(OmpAppContext.getMessage("unsupported_biz_operation")
						+ ":" + REALTIMEPERF_BIZ_NAME);
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
	}

	@Override
	public PerfLogConfig query(Long moId) throws RemoteException, Exception {
		GenericBizData data = mcBtsBizService.queryFromNE(moId,
				new GenericBizData(PERFLOG_BIZ_NAME));
		PerfLogConfig config = new PerfLogConfig();
		data.getModel(config);
		return config;
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		GenericBizData data = mcBtsBizDAO.queryAllBy(moId, PERFLOG_BIZ_NAME,
				null, new Object[0]);
		PerfLogConfig config = new PerfLogConfig();
		data.getModel(config);
		// ����
		config(moId, config);
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		GenericBizData result = mcBtsBizService.queryFromNE(moId,
				new GenericBizData(PERFLOG_BIZ_NAME));
		mcBtsBizService.saveToDB(moId, result);
	}

	/**
	 * ����һ��������־�������ݵ�ͨ��ģ��
	 * 
	 * @param config
	 * @return
	 */
	private GenericBizData createPerfLogData(PerfLogConfig config) {
		GenericBizData data = new GenericBizData(PERFLOG_BIZ_NAME);
		data.addProperty(new GenericBizProperty("idx", config.getIdx()));
		// data.addProperty(new GenericBizProperty("moId", config.getMoId()));
		data.addProperty(new GenericBizProperty("ftpServerIp", config
				.getFtpServerIp()));
		data.addProperty(new GenericBizProperty("ftpServerPort", config
				.getFtpServerPort()));
		data.addProperty(new GenericBizProperty("userName", config
				.getUserName()));
		data.addProperty(new GenericBizProperty("password", config
				.getPassword()));
		data.addProperty(new GenericBizProperty("reportInterval", config
				.getReportInterval()));
		data.addProperty(new GenericBizProperty("collectInterval", config
				.getCollectInterval()));
		data.addProperty(new GenericBizProperty("realTimeInterval", config
				.getRealTimeInterval()));
		return data;
	}

	public void setMcBtsBizDAO(McBtsBizDAO mcBtsBizDAO) {
		this.mcBtsBizDAO = mcBtsBizDAO;
	}

	public void setMcBtsBizService(McBtsBizService mcBtsBizService) {
		this.mcBtsBizService = mcBtsBizService;
	}

	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	public void setRealTimePerfProxy(RealTimePerfProxy realTimePerfProxy) {
		this.realTimePerfProxy = realTimePerfProxy;
	}

}
