package com.xinwei.minas.server.mcbts.service.layer3.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.WeakFaultFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfDnInfo;
import com.xinwei.minas.mcbts.core.model.layer3.TConfDnInfoPK;
import com.xinwei.minas.mcbts.core.model.layer3.TConfFaultSwitch;
import com.xinwei.minas.mcbts.core.model.layer3.TConfWeakVoiceFault;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfDnInfoDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfFaultSwitchDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfWeakVoiceFaultDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.proxy.layer3.WeakFaultProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.WeakFaultService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 故障弱化业务层接口实现
 * 
 * @author yinbinqiang
 * 
 */
public class WeakFaultServiceImpl implements WeakFaultService {

	private TConfFaultSwitchDAO faultSwitchDAO;
	private TConfWeakVoiceFaultDAO weakVoiceFaultDAO;
	private TConfDnInfoDAO dnInfoDAO;

	private WeakFaultProxy weakFaultProxy;
	private McBtsBizProxy mcBtsBizProxy;

	public void setFaultSwitchDAO(TConfFaultSwitchDAO faultSwitchDAO) {
		this.faultSwitchDAO = faultSwitchDAO;
	}

	public void setWeakVoiceFaultDAO(TConfWeakVoiceFaultDAO weakVoiceFaultDAO) {
		this.weakVoiceFaultDAO = weakVoiceFaultDAO;
	}

	public void setDnInfoDAO(TConfDnInfoDAO dnInfoDAO) {
		this.dnInfoDAO = dnInfoDAO;
	}

	public void setWeakFaultProxy(WeakFaultProxy weakFaultProxy) {
		this.weakFaultProxy = weakFaultProxy;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	@Override
	public TConfFaultSwitch querySwitchByMoId(Long moId)
			throws RemoteException, Exception {
		return faultSwitchDAO.queryByMoid(moId);
	}

	@Override
	public TConfWeakVoiceFault queryVoiceByMoId(Long moId)
			throws RemoteException, Exception {
		List<TConfDnInfo> dnInfos = dnInfoDAO.queryByMoid(moId);

		if (dnInfos != null) {
			for (TConfDnInfo info : dnInfos) {
				if (info.getDn_prefix().equals("0000000000"))
					info.setDn_prefix("");
			}
		}

		TConfWeakVoiceFault weakVoiceFault = weakVoiceFaultDAO
				.queryByMoid(moId);
		if (weakVoiceFault == null)
			return null;
		weakVoiceFault.TConfDnInfos = dnInfos;

		return weakVoiceFault;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		for (long moId : moIdList) {
			// 查开关
			TConfFaultSwitch fSwitch = querySwitchByMoId(moId);

			business.getCell("switch").putContent(moId,
					toJSON("switch", String.valueOf(fSwitch.getFlag())));
			// 查主体
			TConfWeakVoiceFault wvFault = queryVoiceByMoId(moId);

			business.getCell("voiceSwitch").putContent(moId,
					toJSON("voiceSwitch", String.valueOf(wvFault.getFlag())));
			business.getCell("voice_user_list_file").putContent(
					moId,
					toJSON("voice_user_list_file",
							String.valueOf(wvFault.getVoice_user_list_file())));
			business.getCell("voice_user_list_file2")
					.putContent(
							moId,
							toJSON("voice_user_list_file2",
									String.valueOf(wvFault
											.getVoice_user_list_file2())));
			business.getCell("trunk_list_file").putContent(
					moId,
					toJSON("trunk_list_file",
							String.valueOf(wvFault.getTrunk_list_file())));
			business.getCell("multi_call_idle_time").putContent(
					moId,
					toJSON("multi_call_idle_time",
							String.valueOf(wvFault.getMulti_call_idle_time())));
			business.getCell("voice_max_time").putContent(
					moId,
					toJSON("voice_max_time",
							String.valueOf(wvFault.getVoice_max_time())));
			business.getCell("multi_call_max_time").putContent(
					moId,
					toJSON("multi_call_max_time",
							String.valueOf(wvFault.getMulti_call_max_time())));
			business.getCell("delay_interval").putContent(
					moId,
					toJSON("delay_interval",
							String.valueOf(wvFault.getDelay_interval())));
			business.getCell("division_code").putContent(moId,
					toJSON("division_code", wvFault.getDivision_code()));

			List<TConfDnInfo> dnList = wvFault.TConfDnInfos;
			if (dnList == null || dnList.isEmpty()) {
				continue;
			}

			StringBuilder dnSb = new StringBuilder();
			for (TConfDnInfo dn : dnList) {
				dnSb.append(",{");
				dnSb.append(toJSON("dn_prefix",
						String.valueOf(dn.getDn_prefix())));
				dnSb.append(",");
				dnSb.append(toJSON("dn_len", String.valueOf(dn.getDn_len())));
				dnSb.append("}");
			}
			String content = "\"TConfDnInfos\":[" + dnSb.substring(1) + "]";
			business.getCell("TConfDnInfos").putContent(moId, content);
		}
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// 获得基站模型
		long btsId = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		// 创建对象
		TConfFaultSwitch weakSwitch = this.querySwitchByMoId(mcBts.getMoId());
		if (weakSwitch == null) {
			weakSwitch = new TConfFaultSwitch();
			weakSwitch.setMoId(mcBts.getMoId());
		}
		TConfWeakVoiceFault weak = this.queryVoiceByMoId(mcBts.getMoId());
		if (weak == null) {
			weak = new TConfWeakVoiceFault();
			weak.setMoId(mcBts.getMoId());
		}

		// 填充对象
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value)) {
				return;
			}

			if (key.equals("switch")) {
				weakSwitch.setFlag(Integer.parseInt(value));
			} else if (key.equals("voiceSwitch")) {
				weak.setFlag(Integer.valueOf(value));
			} else if (key.equals("voice_user_list_file")) {
				weak.setVoice_user_list_file(Integer.valueOf(value));
			} else if (key.equals("voice_user_list_file2")) {
				weak.setVoice_user_list_file2(Integer.valueOf(value));
			} else if (key.equals("trunk_list_file")) {
				weak.setTrunk_list_file(Integer.valueOf(value));
			} else if (key.equals("multi_call_idle_time")) {
				weak.setMulti_call_idle_time(Integer.valueOf(value));
			} else if (key.equals("voice_max_time")) {
				weak.setVoice_max_time(Integer.valueOf(value));
			} else if (key.equals("multi_call_max_time")) {
				weak.setMulti_call_max_time(Integer.valueOf(value));
			} else if (key.equals("delay_interval")) {
				weak.setDelay_interval(Integer.valueOf(value));
			} else if (key.equals("division_code")) {
				weak.setDivision_code(value);
			} else if (key.equals("TConfDnInfos")) {
				// 形如:[111,8];[123,8];[222,8];[,0];[,0];[,0];[,0];[,0];[,0];[,0];[,0];[,0];[,0];[,0];[,0];[,0];[,0];[,0];[,0];[,0]
				if (StringUtils.isBlank(value))
					continue;

				List<TConfDnInfo> dnInfoList = new ArrayList<TConfDnInfo>();
				String dnStr = value.replaceAll("(\\[|\\])", "");
				String[] dnInfos = dnStr.split(";");
				for (int i = 0; i < dnInfos.length; i++) {
					TConfDnInfo info = new TConfDnInfo();

					TConfDnInfoPK pk = new TConfDnInfoPK();
					pk.setParentid(mcBts.getMoId());
					pk.setRowindex(i);
					info.setId(pk);

					String infoStr = dnInfos[i];
					String[] dn_values = infoStr.split(",");
					info.setDn_prefix(dn_values[0]);
					info.setDn_len(Integer.valueOf(dn_values[1]));

					dnInfoList.add(info);
				}
				weak.TConfDnInfos = dnInfoList;
			}
		}

		// 保存对象
		WeakFaultFacade facade = AppContext.getCtx().getBean(
				WeakFaultFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, weakSwitch, weak);
	}

	private static String toJSON(String key, String value) {
		return "\"" + key + "\":\"" + value + "\"";
	}

	@Override
	public void config(TConfFaultSwitch faultSwitch,
			TConfWeakVoiceFault weakVoiceFault) throws RemoteException,
			Exception {
		Long moId = weakVoiceFault.getMoId();
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 检查BTS的工作状态是否为抗干扰跳频状态
			if (bts != null && !bts.isConnected()) {
				throw new Exception(OmpAppContext.getMessage("bts_unconnected"));
			}

			GenericBizData data = new GenericBizData("t_conf_fault_switch");
			data.addEntity(faultSwitch);

			List<TConfDnInfo> dnInfos = weakVoiceFault.TConfDnInfos;
			if (dnInfos == null
					|| dnInfos.size() < TConfWeakVoiceFault.DNINFO_MAX_SIZE) {
				if (dnInfos == null) {
					dnInfos = new ArrayList<TConfDnInfo>();
				}
				for (int i = dnInfos.size(); i < TConfWeakVoiceFault.DNINFO_MAX_SIZE; i++) {
					TConfDnInfo info = new TConfDnInfo();
					TConfDnInfoPK pk = new TConfDnInfoPK(moId, i);
					info.setId(pk);
					info.setDn_prefix("");
					info.setDn_len(0);
					dnInfos.add(info);
				}
			}

			try {
				mcBtsBizProxy.config(faultSwitch.getMoId(), data);
				weakFaultProxy.config(weakVoiceFault.getMoId(), weakVoiceFault);
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}

		// 存储故障弱化开关配置信息
		faultSwitchDAO.saveOrUpdate(faultSwitch);
		// 存储语音故障弱化开关配置信息
		weakVoiceFaultDAO.saveOrUpdate(weakVoiceFault);

		List<TConfDnInfo> dnInfos = weakVoiceFault.TConfDnInfos;
		// 存储故障弱化中配置号码信息
		dnInfoDAO.saveDnInfoList(dnInfos);
	}

	/**
	 * 从网元查询故障弱化开关配置基本信息
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	@Override
	public TConfFaultSwitch querySwitchFromNE(Long moId) throws Exception {
		GenericBizData data = new GenericBizData("t_conf_fault_switch");
		GenericBizData genericBizData = mcBtsBizProxy.query(moId, data);

		TConfFaultSwitch tConfFaultSwitch = new TConfFaultSwitch();
		tConfFaultSwitch.setFlag(Integer.parseInt(String.valueOf(genericBizData
				.getProperty("flag").getValue())));

		return tConfFaultSwitch;
	}

	/**
	 * 从网元查询语音故障弱化配置基本信息
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	@Override
	public TConfWeakVoiceFault queryVoiceFromNE(Long moId) throws Exception {
		TConfWeakVoiceFault configWeakVoiceFault = new TConfWeakVoiceFault();
		TConfWeakVoiceFault tconfWeakVoiceFault = weakFaultProxy.query(moId,
				configWeakVoiceFault);

		List<TConfDnInfo> list = tconfWeakVoiceFault.TConfDnInfos;

		for (TConfDnInfo info : list) {
			if (info.getDn_prefix().equals("0000000000"))
				info.setDn_prefix("");
		}

		return tconfWeakVoiceFault;
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		TConfFaultSwitch faultSwitch = this.querySwitchByMoId(moId);
		TConfWeakVoiceFault weakVoiceFault = this.queryVoiceByMoId(moId);
		if (faultSwitch != null && weakVoiceFault != null) {
			this.config(faultSwitch, weakVoiceFault);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		// 开关
		TConfFaultSwitch resultSwitch = querySwitchFromNE(moId);
		TConfFaultSwitch switchDataFromDB = querySwitchByMoId(moId);

		if (switchDataFromDB == null) {
			SequenceService sequenceService = AppContext.getCtx().getBean(
					SequenceService.class);
			resultSwitch.setIdx(sequenceService.getNext());
		} else {
			resultSwitch.setIdx(switchDataFromDB.getIdx());
		}
		resultSwitch.setMoId(moId);

		faultSwitchDAO.saveOrUpdate(resultSwitch);

		// 数据
		TConfWeakVoiceFault resultVoiceFault = queryVoiceFromNE(moId);
		TConfWeakVoiceFault voiceFaultFromDB = queryVoiceByMoId(moId);

		if (voiceFaultFromDB == null) {
			SequenceService sequenceService = AppContext.getCtx().getBean(
					SequenceService.class);
			resultVoiceFault.setIdx(sequenceService.getNext());
		} else {
			resultVoiceFault.setIdx(voiceFaultFromDB.getIdx());
		}

		resultVoiceFault.setMoId(moId);
		weakVoiceFaultDAO.saveOrUpdate(resultVoiceFault);

		List<TConfDnInfo> dnInfos = resultVoiceFault.TConfDnInfos;
		for (int i = 0; i < dnInfos.size(); i++) {
			TConfDnInfoPK id = new TConfDnInfoPK();
			id.setParentid(moId);
			id.setRowindex(i);

			dnInfos.get(i).setId(id);
		}

		dnInfoDAO.saveDnInfoList(dnInfos);

	}
}
