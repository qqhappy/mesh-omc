/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer1.CalibrationTypeFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationType;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfCalibrationTypeDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer1.CalibrationTypeService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * У׼��������ҵ�����ӿ�ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class CalibrationTypeServiceImpl implements CalibrationTypeService {
	private Log log = LogFactory.getLog(CalibrationTypeServiceImpl.class);
	private McBtsBizProxy mcBtsBizProxy;

	private TConfCalibrationTypeDAO calibrationTypeDAO;

	/**
	 * @param settingDAO
	 *            the l1generalSettingDAO to set
	 */
	public void setCalibrationTypeDAO(TConfCalibrationTypeDAO settingDAO) {
		calibrationTypeDAO = settingDAO;
	}

	/**
	 * @param mcBtsBizProxy
	 *            the mcBtsBizProxy to set
	 */
	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		List<CalibrationType> typeList = calibrationTypeDAO.queryAll();

		for (CalibrationType type : typeList) {
			long moId = type.getMoId();
			// ���moId< 0�򼯺ϲ�������ǰmoId,�ͽ�����һ��
			if (moId < 0 || !moIdList.contains(moId))
				continue;

			business.getCell("calibPeriod")
					.putContent(
							moId,
							toJSON("calibPeriod",
									String.valueOf(type.getCalibPeriod())));

			business.getCell("calibType").putContent(moId,
					toJSON("calibType", String.valueOf(type.getCalibType())));
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
		CalibrationType type = this.queryByMoId(mcBts.getMoId());
		if (type == null) {
			type = new CalibrationType();
			type.setMoId(mcBts.getMoId());
		}

		// ������
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value)) {
				return;
			}

			if (key.equals("calibPeriod")) {
				type.setCalibPeriod(Integer.valueOf(value));
			} else if (key.equals("calibType")) {
				type.setCalibType(Integer.valueOf(value));
			}
		}

		// �������
		CalibrationTypeFacade facade = AppContext.getCtx().getBean(
				CalibrationTypeFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, type);

	}

	/**
	 * ��ѯУ׼����������Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public CalibrationType queryByMoId(Long moId) throws Exception {
		return calibrationTypeDAO.queryByMoId(moId);
	}

	/**
	 * ����У׼����������Ϣ
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	public void config(CalibrationType setting) throws Exception {
		Long moId = setting.getMoId();
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// ���߹���״̬�£���Ҫͨ��Proxy��MO����������Ϣ
		if (bts != null && bts.isConfigurable()) {
			// ���BTS�Ĺ���״̬�Ƿ�Ϊ��������Ƶ״̬
			if (bts != null && bts.isAntijamming()) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_jumping_hold"));
			}

			// ת��ģ��
			GenericBizData data = new GenericBizData("mcbts_calibrationtype");
			data.addEntity(setting);
			try {
				mcBtsBizProxy.config(moId, data);
			} catch (Exception e) {
				log.error(e);
				throw e;
			}
		}
		calibrationTypeDAO.saveOrUpdate(setting);
	}

	/**
	 * ����Ԫ���������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public CalibrationType queryFromNE(Long moId) throws Exception {
		GenericBizData data = new GenericBizData("mcbts_calibrationtype");
		GenericBizData fromNE = mcBtsBizProxy.query(moId, data);

		CalibrationType calibrationType = new CalibrationType();
		calibrationType.setCalibPeriod(Integer.valueOf(String.valueOf(fromNE
				.getProperty("calibPeriod").getValue())));
		calibrationType.setCalibType(Integer.valueOf(String.valueOf(fromNE
				.getProperty("calibType").getValue())));

		return calibrationType;
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		CalibrationType calibrationType = this.queryByMoId(moId);
		if (calibrationType != null) {
			this.config(calibrationType);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		CalibrationType result = queryFromNE(moId);
		CalibrationType dataFromDB = queryByMoId(moId);

		if (dataFromDB == null) {
			SequenceService sequenceService = AppContext.getCtx().getBean(
					SequenceService.class);
			result.setIdx(sequenceService.getNext());
		} else {
			result.setIdx(dataFromDB.getIdx());
		}

		result.setMoId(moId);

		calibrationTypeDAO.saveOrUpdate(result);
	}

}
