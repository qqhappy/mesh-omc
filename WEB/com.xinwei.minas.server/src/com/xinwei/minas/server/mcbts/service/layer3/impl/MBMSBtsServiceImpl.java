package com.xinwei.minas.server.mcbts.service.layer3.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.MBMSBtsFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfMBMSBtsDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfRemoteBtsDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.MBMSBtsService;
import com.xinwei.minas.server.mcbts.service.sysManage.SimulcastManageService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

public class MBMSBtsServiceImpl implements MBMSBtsService {

	private TConfRemoteBtsDAO remoteBtsDAO;
	private TConfMBMSBtsDAO mbmsBtsDAO;
	private McBtsBizProxy mcBtsBizProxy;

	public void setRemoteBtsDAO(TConfRemoteBtsDAO remoteBtsDAO) {
		this.remoteBtsDAO = remoteBtsDAO;
	}

	public void setMbmsBtsDAO(TConfMBMSBtsDAO mbmsBtsDAO) {
		this.mbmsBtsDAO = mbmsBtsDAO;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	@Override
	public TConfMBMSBts queryByMoId(Long moId) throws Exception {
		return mbmsBtsDAO.queryByMoId(moId);
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		List<TConfMBMSBts> mbmsList = mbmsBtsDAO.queryAll();
		if (mbmsList == null || mbmsList.isEmpty())
			return;

		for (TConfMBMSBts mbms : mbmsList) {
			long moId = mbms.getMoId();
			// ���moId< 0�򼯺ϲ�������ǰmoId,�ͽ�����һ��
			if (moId < 0 || !moIdList.contains(moId))
				continue;

			business.getCell("flag").putContent(moId,
					toJSON("flag", String.valueOf(mbms.getFlag())));
		}
	}

	private static String toJSON(String key, String value) {
		return "\"" + key + "\":\"" + value + "\"";
	}

	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// ��û�վģ��
		long btsId = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		// ��������
		TConfMBMSBts mbmsBts = queryByMoId(mcBts.getMoId());
		if (mbmsBts == null) {
			mbmsBts = new TConfMBMSBts();
			mbmsBts.setMoId(mcBts.getMoId());
		}

		// ������
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String value = entry.getValue().getContentByBID(btsId);
			if (StringUtils.isBlank(value))
				return;

			// MBMSֻ��һ��cell:flag,��˲����ж�
			mbmsBts.setFlag(Integer.parseInt(value));
		}

		// �������
		MBMSBtsFacade facade = AppContext.getCtx().getBean(MBMSBtsFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, mbmsBts);
	}

	@Override
	public void config(TConfMBMSBts mbmsBts) throws Exception {
		Long moId = mbmsBts.getMoId();
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}

		TConfRemoteBts remoteBts = remoteBtsDAO.queryByMoId(mbmsBts.getMoId());
		// �Ƿ���֧��Զ�����վ��ͻ
		if (remoteBts != null) {
			if (mbmsBts.getFlag() == TConfMBMSBts.FLAG_SUPPORT
					&& remoteBts.getFlag() == TConfRemoteBts.FLAG_SUPPORT) {
				throw new Exception(
						OmpAppContext
								.getMessage("mcbts_cor_broadcast_unsupported"));
			}
		}

		// ������״̬�£���Ҫͨ��Proxy��MO����������Ϣ
		if (bts != null && bts.isConfigurable()) {
			// ת��ģ��
			GenericBizData data = new GenericBizData("t_conf_mbms_bts");
			//����վ�ĵ���ΪĬ��ֵ0ʱ����Ҫ���õ���ID
			if (mbmsBts.getDistrictId() == 0) {
				mbmsBts.setDistrictId(bts.getDistrictId());
			}
			data.addEntity(mbmsBts);
			try {
				mcBtsBizProxy.config(moId, data);
			} catch (UnsupportedOperationException e) {
				throw e;
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		mbmsBtsDAO.saveOrUpdate(mbmsBts);

		// ����ͬ����Դ�еĻ�վ��·��ϢΪδͬ��
		SimulcastManageService simulcastManageService = AppContext.getCtx()
				.getBean(SimulcastManageService.class);
		simulcastManageService.setSimulMcBtsLinkUnSync();
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		TConfMBMSBts mBMSBts = this.queryByMoId(moId);
		if (mBMSBts != null) {
			this.config(mBMSBts);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		GenericBizData data = new GenericBizData("t_conf_mbms_bts");
		GenericBizData genericBizData = mcBtsBizProxy.query(moId, data);

		int flag = Integer.parseInt(String.valueOf(genericBizData.getProperty(
				"flag").getValue()));

		TConfMBMSBts mbmsBts = new TConfMBMSBts();
		mbmsBts.setFlag(flag);
		mbmsBts.setMoId(moId);

		TConfMBMSBts queryFromDB = mbmsBtsDAO.queryByMoId(moId);

		if (queryFromDB != null)
			mbmsBts.setIdx(queryFromDB.getIdx());
		else {
			SequenceService sequenceService = AppContext.getCtx().getBean(
					SequenceService.class);

			mbmsBts.setIdx(sequenceService.getNext());
		}

		mbmsBtsDAO.saveOrUpdate(mbmsBts);
	}
}
