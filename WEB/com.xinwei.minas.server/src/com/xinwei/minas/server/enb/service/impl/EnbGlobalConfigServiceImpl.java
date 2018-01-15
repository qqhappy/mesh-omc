/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-2	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.SystemProperty;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.corenet.EnbGlobalConfig;
import com.xinwei.minas.enb.core.model.corenet.TaModel;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.core.conf.dao.SystemPropertyDAO;
import com.xinwei.minas.server.enb.dao.TaModelDAO;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.service.EnbBizConfigService;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbGlobalConfigService;
import com.xinwei.omp.core.model.biz.PagingCondition;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB全局配置数据服务接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbGlobalConfigServiceImpl implements EnbGlobalConfigService {

	private TaModelDAO taModelDAO;

	private SystemPropertyDAO systemPropertyDAO;

	private EnbBizConfigService enbBizConfigService;

	private static final String CATEGORY_PLATFORM = "platform";

	private static final String PROPERTY_CORE_NET_TYPE = "core_net_type";

	private static final String CATEGORY_ENB = "enb";

	private static final String PROPERTY_MCC = "mcc";

	private static final String PROPERTY_MNC = "mnc";
	// 加密算法
	private static final String PROPERTY_EEA = "eea";
	// 完整性保护算法
	private static final String PROPERTY_EIA = "eia";
	// 视频配置
	private static final String PROPERTY_VIDEO_CONFIG = "video_config";
	

	@Override
	public EnbGlobalConfig queryEnbGlobalConfig() throws Exception {
		EnbGlobalConfig enbGlobalConfig = new EnbGlobalConfig();

		SystemProperty coreNetTypeProperty = getSystemProperty(
				CATEGORY_PLATFORM, PROPERTY_CORE_NET_TYPE);
		int coreNetType = Integer.valueOf(coreNetTypeProperty.getValue());
		enbGlobalConfig.setCoreNetType(coreNetType);

		SystemProperty mccProperty = getSystemProperty(CATEGORY_ENB,
				PROPERTY_MCC);
		enbGlobalConfig.setMcc(mccProperty.getValue());

		SystemProperty mncProperty = getSystemProperty(CATEGORY_ENB,
				PROPERTY_MNC);
		enbGlobalConfig.setMnc(mncProperty.getValue());

		SystemProperty eeaProperty = getSystemProperty(CATEGORY_ENB,
				PROPERTY_EEA);
		enbGlobalConfig.setEea(eeaProperty.getValue());

		SystemProperty eiaProperty = getSystemProperty(CATEGORY_ENB,
				PROPERTY_EIA);
		enbGlobalConfig.setEia(eiaProperty.getValue());
		
		SystemProperty videoProperty = getSystemProperty(CATEGORY_PLATFORM,
				PROPERTY_VIDEO_CONFIG);
		enbGlobalConfig.setVideoSwitch(videoProperty.getVideoSwitch());
		enbGlobalConfig.setVideoIp(videoProperty.getVideoIp());
		enbGlobalConfig.setVideoPort(videoProperty.getVideoPort());
		return enbGlobalConfig;
	}

	@Override
	public Map<Object, String> configEnbGlobalConfig(EnbGlobalConfig config)
			throws Exception {
		Integer coreNetType = config.getCoreNetType();
		if (coreNetType != null) {
			setSystemProperty(CATEGORY_PLATFORM, PROPERTY_CORE_NET_TYPE,
					coreNetType.toString());
		}
		boolean mccChanged = false;
		String mcc = config.getMcc();
		if (mcc != null && !mcc.equals("")) {
			SystemProperty mccProperty = getSystemProperty(CATEGORY_ENB,
					PROPERTY_MCC);
			if (!mccProperty.getValue().equals(mcc)) {
				mccChanged = true;
				mccProperty.setValue(mcc);
				systemPropertyDAO.saveOrUpdate(mccProperty);
			}
		}
		boolean mncChanged = false;
		String mnc = config.getMnc();
		if (mnc != null && !mnc.equals("")) {
			SystemProperty mncProperty = getSystemProperty(CATEGORY_ENB,
					PROPERTY_MNC);
			if (!mncProperty.getValue().equals(mnc)) {
				mncChanged = true;
				mncProperty.setValue(mnc);
				systemPropertyDAO.saveOrUpdate(mncProperty);
			}
		}
		boolean eeaChanged = false;
		String eea = config.getEea();
		if (eea != null && !eea.equals("")) {
			SystemProperty eeaProperty = getSystemProperty(CATEGORY_ENB,
					PROPERTY_EEA);
			if (!eeaProperty.getValue().equals(eea)) {
				eeaChanged = true;
				eeaProperty.setValue(eea);
				systemPropertyDAO.saveOrUpdate(eeaProperty);
			}
		}
		boolean eiaChanged = false;
		String eia = config.getEia();
		if (eia != null && !eia.equals("")) {
			SystemProperty eiaProperty = getSystemProperty(CATEGORY_ENB,
					PROPERTY_EIA);
			if (!eiaProperty.getValue().equals(eia)) {
				eiaChanged = true;
				eiaProperty.setValue(eia);
				systemPropertyDAO.saveOrUpdate(eiaProperty);
			}
		}
		
		int videoSwitch = config.getVideoSwitch();
		if(-1 != videoSwitch) {
			String videoIp = config.getVideoIp();
			String videoPort = config.getVideoPort();
			if(null == videoIp) {
				videoIp = "";
			}
			if(null == videoPort) {
				videoPort = "";
			}
			SystemProperty videoProperty = getSystemProperty(CATEGORY_PLATFORM,
					PROPERTY_VIDEO_CONFIG);
			if (videoSwitch != videoProperty.getVideoSwitch()
					|| !videoIp.equals(videoProperty.getVideoIp())
					|| !videoPort.equals(videoProperty.getVideoPort())) {
				videoProperty.setVideoValue(videoSwitch, videoIp, videoPort);
				systemPropertyDAO.saveOrUpdate(videoProperty);
			}
		}
		
		boolean cellParaChanged = mccChanged || mncChanged;
		boolean enbParaChanged = eeaChanged || eiaChanged;
		// 将全局配置同步到基站
		return syncGlobalConfigToNE(mcc, mnc, eea, eia);
	}

	@Override
	public void addTaItem(TaModel taModel) throws Exception {
		// ID是否存在
		if (isItemIdExist(taModel)) {
			throw new Exception(OmpAppContext.getMessage("ta_item_id_exist"));
		}
		// code是否存在
		if (isTaCodeExist(taModel)) {
			throw new Exception(OmpAppContext.getMessage("ta_code_exist"));
		}
		taModelDAO.addTaItem(taModel);
	}

	@Override
	public void modifyTaItem(TaModel taModel) throws Exception {
		TaModel model = taModelDAO.queryTaItemById(taModel.getId());
		if (model == null) {
			throw new Exception(OmpAppContext.getMessage("record_not_exist"));
		}
		// code是否存在
		if (isTaCodeExist(taModel)) {
			throw new Exception(OmpAppContext.getMessage("ta_code_exist"));
		}
		// 校验跟踪区码是否被引用
		checkTaCodeReferenced(model.getCode());
		taModelDAO.modifyTaItem(taModel);
	}

	@Override
	public void deleteTaItem(int id) throws Exception {
		TaModel model = taModelDAO.queryTaItemById(id);
		if (model == null) {
			throw new Exception(OmpAppContext.getMessage("record_not_exist"));
		}
		// 校验跟踪区码是否被引用
		checkTaCodeReferenced(model.getCode());
		taModelDAO.deleteTaItem(id);
	}

	@Override
	public TaModel queryTaItemById(int id) throws Exception {
		return taModelDAO.queryTaItemById(id);
	}

	/**
	 * 判断跟踪区码ID是否已存在
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	private boolean isItemIdExist(TaModel item) throws Exception {
		TaModel model = taModelDAO.queryTaItemById(item.getId());
		if (model != null)
			return true;
		return false;
	}

	/**
	 * 跟踪区码是否已存在
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	private boolean isTaCodeExist(TaModel taModel) throws Exception {
		List<TaModel> allItems = taModelDAO.queryAllTaItems();
		for (TaModel model : allItems) {
			if (model.getId() == taModel.getId())
				continue;
			if (model.getCode().equals(taModel.getCode())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<TaModel> queryAllTaItems() throws Exception {
		return taModelDAO.queryAllTaItems();
	}

	@Override
	public PagingData<TaModel> queryTaItems(PagingCondition condition)
			throws Exception {
		return taModelDAO.queryTaItems(condition);
	}

	/**
	 * 校验跟踪区码是否被引用
	 * 
	 * @param taCode
	 * @throws Exception
	 */
	private void checkTaCodeReferenced(String taCode) throws Exception {

		List<Enb> enbList = EnbCache.getInstance().queryAll();
		for (Enb enb : enbList) {
			long moId = enb.getMoId();
			XBizTable bizTable = enbBizConfigService.queryFromEms(moId,
					EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null, false);
			if (EnbBizHelper.hasRecord(bizTable)) {
				for (XBizRecord cellRecord : bizTable.getRecords()) {
					XBizField taField = cellRecord
							.getFieldBy(EnbConstantUtils.FIELD_NAME_TAC);

					if (taField.getValue().equals(taCode)) {
						throw new Exception(
								OmpAppContext
										.getMessage("ta_code_is_referenced"));
					}
				}
			}
		}
	}

	/**
	 * 将算法配置同步到所有eNB配置数据
	 * 
	 * @param eea
	 * @param eia
	 */
	private Map<Object, String> syncGlobalConfigToNE(String mcc, String mnc,
			String eea, String eia) {

		Map<Object, String> messageMap = null;
		List<Enb> enbList = EnbCache.getInstance().queryAll();
		for (Enb enb : enbList) {
			long moId = enb.getMoId();
			String message = null;
			// eNB参数
			if (eea != null || eia != null) {
				try {
					syncEnbPara(moId, eea, eia);
				} catch (Exception e) {
					message = OmpAppContext.getMessage("enb_para_sync_failed")
							+ e.getLocalizedMessage();
				}
			}
			// 小区参数
			if (mcc != null || mnc != null) {
				try {
					syncCellPara(moId, mcc, mnc);
				} catch (Exception e) {
					message = OmpAppContext.getMessage("cell_para_sync_failed")
							+ e.getLocalizedMessage();
				}
			}
			// 邻区
			if (mcc != null || mnc != null) {
				try {
					syncNbrcelPara(moId, mcc, mnc);
				} catch (Exception e) {
					message = OmpAppContext.getMessage("nbrcel_sync_failed")
							+ e.getLocalizedMessage();
				}
			}
			
			if (message != null) {
				// 记录错误消息
				if (messageMap == null) {
					messageMap = new LinkedHashMap<Object, String>();
				}
				messageMap.put(enb.getHexEnbId(), message);
			}
		}
		return messageMap;
	}

	private void syncNbrcelPara(long moId, String mcc, String mnc) throws Exception {
		XBizTable bizTable = enbBizConfigService.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, null);
		if (EnbBizHelper.hasRecord(bizTable)) {
			List<XBizRecord> records = bizTable.getRecords();
			for (XBizRecord bizRecord : records) {
				if (mcc != null) {
					bizRecord.addField(new XBizField(
							EnbConstantUtils.FIELD_NAME_MCC, mcc));
				}
				if (mnc != null) {
					bizRecord.addField(new XBizField(
							EnbConstantUtils.FIELD_NAME_MNC, mnc));
				}
				enbBizConfigService.update(moId,
						EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, bizRecord);
			}
		}
	}

	private void syncEnbPara(long moId, String eea, String eia)
			throws Exception {
		// 依次配置每个eNB的eNB参数表
		XBizTable bizTable = enbBizConfigService.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_ENB_PARA, null, false);
		if (EnbBizHelper.hasRecord(bizTable)) {
			XBizRecord bizRecord = bizTable.getRecords().get(0);
			if (eea != null) {
				bizRecord.addField(new XBizField(
						EnbConstantUtils.FIELD_NAME_EEA, eea));
			}
			if (eia != null) {
				bizRecord.addField(new XBizField(
						EnbConstantUtils.FIELD_NAME_EIA, eia));
			}
			enbBizConfigService.update(moId,
					EnbConstantUtils.TABLE_NAME_T_ENB_PARA, bizRecord);
		}
	}

	private void syncCellPara(long moId, String mcc, String mnc)
			throws Exception {
		// 依次配置每个eNB的小区参数表
		XBizTable bizTable = enbBizConfigService.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null, false);
		if (EnbBizHelper.hasRecord(bizTable)) {
			for (XBizRecord cellRecord : bizTable.getRecords()) {
				if (mcc != null) {
					cellRecord.addField(new XBizField(
							EnbConstantUtils.FIELD_NAME_MCC, mcc));
				}
				if (mnc != null) {
					cellRecord.addField(new XBizField(
							EnbConstantUtils.FIELD_NAME_MNC, mnc));
				}
				enbBizConfigService.update(moId,
						EnbConstantUtils.TABLE_NAME_T_CELL_PARA, cellRecord);
			}
		}

	}

	private SystemProperty getSystemProperty(String category, String property) {
		return systemPropertyDAO.queryByCategoryAndProperty(category, null,
				property);
	}

	private void setSystemProperty(String category, String property,
			String value) {
		SystemProperty systemProperty = getSystemProperty(category, property);
		systemProperty.setValue(value);
		systemPropertyDAO.saveOrUpdate(systemProperty);
	}

	public void setTaModelDAO(TaModelDAO taModelDAO) {
		this.taModelDAO = taModelDAO;
	}

	public void setSystemPropertyDAO(SystemPropertyDAO systemPropertyDAO) {
		this.systemPropertyDAO = systemPropertyDAO;
	}

	public void setEnbBizConfigService(EnbBizConfigService enbBizConfigService) {
		this.enbBizConfigService = enbBizConfigService;
	}

}
