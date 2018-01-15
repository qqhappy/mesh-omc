package com.xinwei.minas.server.mcbts.service.layer3.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.xinwei.common.util.ConvertUtil;
import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.SAGParamFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfBackupSag;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfBackupSagDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.proxy.layer3.SAGParamProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.SAGParamService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 备份SAG参数配置基本业务接口实现
 * 
 * @author yinbinqiang
 * 
 */
public class SAGParamServiceImpl implements SAGParamService {

	private TConfBackupSagDAO backupSagDAO;
	private SAGParamProxy sagParamProxy;
	private McBtsBizProxy mcBtsBizProxy;

	public void setBackupSagDAO(TConfBackupSagDAO backupSagDAO) {
		this.backupSagDAO = backupSagDAO;
	}

	public void setSagParamProxy(SAGParamProxy sagParamProxy) {
		this.sagParamProxy = sagParamProxy;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	@Override
	public List getAllSagInfo() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAllLocationArea() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TConfBackupSag queryByMoId(Long moId) throws Exception {
		return backupSagDAO.queryByMoId(moId);
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		List<TConfBackupSag> sagList = backupSagDAO.queryAll();
		if (sagList == null || sagList.isEmpty()) {
			return;
		}

		for (TConfBackupSag sag : sagList) {
			long moId = sag.getMoId();
			// 如果moId< 0或集合不包括当前moId,就进行下一个
			if (moId < 0 || !moIdList.contains(moId))
				continue;

			business.getCell("sAGID").putContent(moId,
					toJSON("sAGID", String.valueOf(sag.getSAGID())));
			business.getCell("sAGIPforVoice").putContent(
					moId,
					toJSON("sAGIPforVoice",
							ConvertUtil.longToIp(sag.getsAGIPforVoice())));

			business.getCell("sAGIPforsignal").putContent(
					moId,
					toJSON("sAGIPforsignal",
							ConvertUtil.longToIp(sag.getsAGIPforsignal())));
			business.getCell("bSForceUseJitterbuffer").putContent(
					moId,
					toJSON("bSForceUseJitterbuffer",
							String.valueOf(sag.getbSForceUseJitterbuffer())));
			business.getCell("zModelUseJitterbuffer").putContent(
					moId,
					toJSON("zModelUseJitterbuffer",
							String.valueOf(sag.getzModelUseJitterbuffer())));
			business.getCell("jitterbufferSize").putContent(
					moId,
					toJSON("jitterbufferSize",
							String.valueOf(sag.getJitterbufferSize())));
			business.getCell("jitterbufferPackageThreshold").putContent(
					moId,
					toJSON("jitterbufferPackageThreshold", String.valueOf(sag
							.getJitterbufferPackageThreshold())));
			business.getCell("sAGVoiceTOS")
					.putContent(
							moId,
							toJSON("sAGVoiceTOS",
									String.valueOf(sag.getsAGVoiceTOS())));
			business.getCell("sAGRxPortForVoice").putContent(
					moId,
					toJSON("sAGRxPortForVoice",
							String.valueOf(sag.getSAGRxPortForVoice())));
			business.getCell("sAGTxPortForVoice").putContent(
					moId,
					toJSON("sAGTxPortForVoice",
							String.valueOf(sag.getSAGTxPortForVoice())));
			business.getCell("sAGRxPortForSignal").putContent(
					moId,
					toJSON("sAGRxPortForSignal",
							String.valueOf(sag.getSAGRxPortForSignal())));
			business.getCell("sAGTxPortForSignal").putContent(
					moId,
					toJSON("sAGTxPortForSignal",
							String.valueOf(sag.getSAGTxPortForSignal())));
			business.getCell("locationAreaID").putContent(
					moId,
					toJSON("locationAreaID",
							String.valueOf(sag.getLocationAreaID())));
			business.getCell("sAGSignalPointCode").putContent(
					moId,
					toJSON("sAGSignalPointCode",
							String.valueOf(sag.getSAGSignalPointCode())));
			business.getCell("bTSSignalPointCode").putContent(
					moId,
					toJSON("bTSSignalPointCode",
							String.valueOf(sag.getBTSSignalPointCode())));
			business.getCell("natAPKey").putContent(moId,
					toJSON("natAPKey", String.valueOf(sag.getNatAPKey())));
		}
	}

	private static String toJSON(String key, String value) {
		return "\"" + key + "\":\"" + value + "\"";
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// 获得基站模型
		long btsId = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		// 创建对象
		TConfBackupSag sag = this.queryByMoId(mcBts.getMoId());
		if (sag == null) {
			sag = new TConfBackupSag();
			sag.setMoId(mcBts.getMoId());
		}
		// 填充对象
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value)) {
				return;
			}

			if (key.equals("sAGID")) {
				sag.setSAGID(Long.valueOf(value));
			} else if (key.equals("sAGIPforVoice")) {
				sag.setsAGIPforVoice(ConvertUtil.ipToLong(value));
			} else if (key.equals("sAGIPforsignal")) {
				sag.setsAGIPforsignal(ConvertUtil.ipToLong(value));
			} else if (key.equals("bSForceUseJitterbuffer")) {
				sag.setbSForceUseJitterbuffer(Integer.valueOf(value));
			} else if (key.equals("zModelUseJitterbuffer")) {
				sag.setzModelUseJitterbuffer(Integer.valueOf(value));
			} else if (key.equals("jitterbufferSize")) {
				sag.setJitterbufferSize(Integer.valueOf(value));
			} else if (key.equals("jitterbufferPackageThreshold")) {
				sag.setJitterbufferPackageThreshold(Integer.valueOf(value));
			} else if (key.equals("sAGVoiceTOS")) {
				sag.setsAGVoiceTOS(Integer.valueOf(value));
			} else if (key.equals("sAGRxPortForVoice")) {
				sag.setSAGRxPortForVoice(Integer.valueOf(value));
			} else if (key.equals("sAGTxPortForVoice")) {
				sag.setSAGTxPortForVoice(Integer.valueOf(value));
			} else if (key.equals("sAGRxPortForSignal")) {
				sag.setSAGRxPortForSignal(Integer.valueOf(value));
			} else if (key.equals("sAGTxPortForSignal")) {
				sag.setSAGTxPortForSignal(Integer.valueOf(value));
			} else if (key.equals("locationAreaID")) {
				sag.setLocationAreaID(Long.valueOf(value));
			} else if (key.equals("sAGSignalPointCode")) {
				sag.setSAGSignalPointCode(Integer.valueOf(value));
			} else if (key.equals("bTSSignalPointCode")) {
				sag.setBTSSignalPointCode(Integer.valueOf(value));
			} else if (key.equals("natAPKey")) {
				sag.setNatAPKey(Integer.valueOf(value));
			}
		}

		// 保存对象
		SAGParamFacade facade = AppContext.getCtx().getBean(
				SAGParamFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, sag);

	}

	@Override
	public void config(TConfBackupSag backupSag) throws Exception {
		Long moId = backupSag.getMoId();
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			// GenericBizData data = new GenericBizData("t_conf_backup_sag");
			// data.addEntity(backupSag);
			// 创建sag参数bizData类
			GenericBizData backupSagData = new GenericBizData("mcbts_backupSag");
			backupSagData.addEntity(backupSag);
			// 创建缓存参数bizData类
			GenericBizData jitterbufferData = new GenericBizData(
					"mcbts_jitterbuffer");
			jitterbufferData.addEntity(backupSag);
			// 创建TOS参数bizData类
			GenericBizData tosData = new GenericBizData("mcbts_sag_tos");
			tosData.addEntity(backupSag);

			try {
				// sagParamProxy.config(moId, data);
				// sag参数配置
				mcBtsBizProxy.config(moId, backupSagData);
				// 缓存参数配置
				mcBtsBizProxy.config(moId, jitterbufferData);
				// tos参数配置
				mcBtsBizProxy.config(moId, tosData);
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		backupSagDAO.saveOrUpdate(backupSag);
	}

	/**
	 * 从网元获得备份SAG基本信息
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	@Override
	public TConfBackupSag queryFromNE(Long moId) throws Exception {
		TConfBackupSag tConfBackupSag = new TConfBackupSag();
		return sagParamProxy.query(moId, tConfBackupSag);
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		TConfBackupSag backupSag = this.queryByMoId(moId);
		if (backupSag != null) {
			this.config(backupSag);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		TConfBackupSag result = queryFromNE(moId);
		TConfBackupSag dataFromDB = queryByMoId(moId);

		if (dataFromDB == null) {
			SequenceService sequenceService = AppContext.getCtx().getBean(
					SequenceService.class);
			result.setIdx(sequenceService.getNext());
		} else {
			result.setIdx(dataFromDB.getIdx());
		}
		result.setMoId(moId);

		backupSagDAO.saveOrUpdate(result);
	}
}
