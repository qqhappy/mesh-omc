/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-21	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.common.GPSDataFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.GPSData;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.common.GPSDataDao;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.common.GPSDataService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * GPS������Ϣ������
 * 
 * 
 * @author tiance
 * 
 */

public class GPSDataServiceImpl implements GPSDataService {
	private Log log = LogFactory.getLog(GPSDataServiceImpl.class);
	private McBtsBizProxy mcBtsBizProxy;
	private GPSDataDao gpsDataDao;

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	public void setGpsDataDao(GPSDataDao gpsDataDao) {
		this.gpsDataDao = gpsDataDao;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		List<GPSData> dataList = gpsDataDao.queryAll();

		for (GPSData gpsData : dataList) {
			long moId = gpsData.getMoId();
			// ���moId< 0�򼯺ϲ�������ǰmoId,�ͽ�����һ��
			if (moId < 0 || !moIdList.contains(moId))
				continue;

			business.getCell("longitude")
					.putContent(
							moId,
							toJSON("longitude",
									String.valueOf(gpsData.getLongitude())));

			business.getCell("latitude").putContent(moId,
					toJSON("latitude", String.valueOf(gpsData.getLatitude())));

			business.getCell("height").putContent(gpsData.getMoId(),
					toJSON("height", String.valueOf(gpsData.getHeight())));

			business.getCell("gmtOffset").putContent(gpsData.getMoId(),
					toJSON("gmtOffset", String.valueOf(gpsData.getHeight())));

			business.getCell("minimumTrackingsatellite").putContent(
					gpsData.getMoId(),
					toJSON("minimumTrackingsatellite",
							String.valueOf(gpsData.getHeight())));
		}
	}

	private static String toJSON(String key, String value) {
		return "\"" + key + "\":\"" + value + "\"";
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// ��û�վģ��
		long btsId = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		// ��������
		GPSData gps = this.queryFromEMS(mcBts.getMoId());
		if (gps == null) {
			gps = new GPSData();
			gps.setMoId(mcBts.getMoId());
		}

		// ������
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value)) {
				return;
			}

			if (key.equals("longitude")) {
				gps.setLongitude(Long.valueOf(value));
			} else if (key.equals("latitude")) {
				gps.setLatitude(Long.valueOf(value));
			} else if (key.equals("height")) {
				gps.setHeight(Long.valueOf(value));
			} else if (key.equals("gmtOffset")) {
				gps.setGmtOffset(Long.valueOf(value));
			} else if (key.equals("minimumTrackingsatellite")) {
				gps.setMinimumTrackingsatellite(Integer.valueOf(value));
			}
		}

		// �������
		GPSDataFacade facade = AppContext.getCtx().getBean(GPSDataFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, gps);

	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		config(queryFromEMS(moId));
	}

	@Override
	public GPSData queryFromEMS(Long moId) throws Exception {
		return gpsDataDao.queryByMoId(moId);
	}

	@Override
	public GPSData queryFromNE(Long moId) throws Exception {
		GenericBizData genericBizData = new GenericBizData("mcbts_gpsData");
		GenericBizData result = mcBtsBizProxy.query(moId, genericBizData);

		GPSData data = new GPSData();
		data.setMoId(moId);
		data.setLatitude(Long.valueOf(result.getProperty("latitude").getValue()
				.toString()));
		data.setLongitude(Long.valueOf(result.getProperty("longitude")
				.getValue().toString()));
		data.setHeight(Long.parseLong(result.getProperty("height").getValue()
				.toString()));
		data.setGmtOffset(Long.parseLong(result.getProperty("gmtOffset")
				.getValue().toString()));
		data.setMinimumTrackingsatellite(Integer.parseInt(result
				.getProperty("minimumTrackingsatellite").getValue().toString()));

		return data;
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		GPSData result = queryFromNE(moId);
		GPSData dataFromDB = queryFromEMS(moId);

		if (dataFromDB == null) {
			SequenceService sequenceService = AppContext.getCtx().getBean(
					SequenceService.class);
			result.setIdx(sequenceService.getNext());
		} else {
			result.setIdx(dataFromDB.getIdx());
		}

		result.setMoId(moId);

		gpsDataDao.saveOrUpdate(result);
	}

	@Override
	public void config(GPSData data) throws Exception {
		Long moId = data.getMoId();
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// ������״̬�£���Ҫͨ��Proxy��MO����������Ϣ
		if (bts != null && bts.isConfigurable()) {
			// ���BTS�Ĺ���״̬�Ƿ�Ϊ��������Ƶ״̬
			if (bts != null && bts.isAntijamming()) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_jumping_hold"));
			}

			// ת��ģ��
			GenericBizData genericBizData = new GenericBizData("mcbts_gpsData");
			genericBizData.addEntity(data);
			try {
				mcBtsBizProxy.config(moId, genericBizData);
			} catch (Exception e) {
				log.error(e);
				throw e;
			}
		}

		gpsDataDao.saveOrUpdate(data);
	}

}
