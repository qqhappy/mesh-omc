/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.impl;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.McBtsBasicFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsAttribute;
import com.xinwei.minas.mcbts.core.model.McBtsAttribute.Key;
import com.xinwei.minas.mcbts.core.model.McBtsCondition;
import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqModule;
import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqToBts;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.mcbts.core.model.sysManage.District;
import com.xinwei.minas.mcbts.core.utils.FreqConvertUtil;
import com.xinwei.minas.server.core.alarm.dao.AlarmDAO;
import com.xinwei.minas.server.mcbts.dao.McBtsBasicDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.AppendNeighborDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.McbtsNeighborFailedDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.NeighbourDAO;
import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsPowerConfigDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.AutomaticFindMcBtsCache;
import com.xinwei.minas.server.mcbts.service.McBtsBasicService;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.MBMSBtsService;
import com.xinwei.minas.server.mcbts.service.layer3.SysFreqService;
import com.xinwei.minas.server.mcbts.service.sysManage.DistrictManageService;
import com.xinwei.minas.server.mcbts.service.sysManage.McBtsTemplateManageService;
import com.xinwei.minas.server.mcbts.service.sysManage.SimulcastManageService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.sxc.service.SxcBasicService;
import com.xinwei.minas.sxc.core.model.SxcBasic;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * McBts����ҵ�����ӿ�ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class McBtsBasicServiceImpl implements McBtsBasicService {

	private static final Logger logger = Logger
			.getLogger(McBtsBasicServiceImpl.class);

	// EMS������IP��ַ
	private String emsServerIp = "127.0.0.1";

	private McBtsBizProxy mcBtsBizProxy;

	private McBtsBasicDAO mcBtsBasicDAO;

	private SequenceService sequenceService;

	private McBtsTemplateManageService mcBtsTemplateManageService;

	private AlarmDAO alarmDAO;

	private NeighbourDAO neighbourDAO;

	private AppendNeighborDAO appendNeighborDAO;

	private McbtsNeighborFailedDAO neighborFailedDAO;

	private SysFreqService sysFreqService;

	private SxcBasicService sxcBasicService;

	private McBtsPowerConfigDAO mcBtsPowerConfigDAO;

	public McBtsBasicServiceImpl() {
		sequenceService = OmpAppContext.getCtx().getBean(SequenceService.class);
		alarmDAO = OmpAppContext.getCtx().getBean(AlarmDAO.class);
	}

	public void setMcBtsBasicDAO(McBtsBasicDAO mcBtsBasicDAO) {
		this.mcBtsBasicDAO = mcBtsBasicDAO;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	public void setEmsServerIp(String emsServerIp) {
		this.emsServerIp = emsServerIp;
	}

	public void setNeighbourDAO(NeighbourDAO neighbourDAO) {
		this.neighbourDAO = neighbourDAO;
	}

	public void setAppendNeighborDAO(AppendNeighborDAO appendNeighborDAO) {
		this.appendNeighborDAO = appendNeighborDAO;
	}

	public void setNeighborFailedDAO(McbtsNeighborFailedDAO neighborFailedDAO) {
		this.neighborFailedDAO = neighborFailedDAO;
	}

	public void setSysFreqService(SysFreqService sysFreqService) {
		this.sysFreqService = sysFreqService;
	}

	public void setSxcBasicService(SxcBasicService sxcBasicService) {
		this.sxcBasicService = sxcBasicService;
	}

	public void setMcBtsPowerConfigDAO(McBtsPowerConfigDAO mcBtsPowerConfigDAO) {
		this.mcBtsPowerConfigDAO = mcBtsPowerConfigDAO;
	}

	/**
	 * ����һ����վ
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public void add(McBts mcBts) throws Exception {
		McBts mcBtsInCache = McBtsCache.getInstance().queryByBtsId(
				mcBts.getBtsId());
		if (mcBtsInCache != null) {
			throw new Exception(OmpAppContext.getMessage("mcbts_id_duplicated"));
		}

		// ��֤SAG��Ϣ
		checkSag(mcBts);
		// У���վ��Ϣ�Ƿ񸴺Ϲ���
		checkBts(mcBts);

		// ��֤��ʼ����Ƶ���Ƿ�����ЧƵ�㼯���棬ֻ����<����ɨ����ЧƵ�㼯>��<��ɨ����ЧƵ�㼯>���������֤
		systemFreqValidate(mcBts.getBtsFreqType());

		// ��ȡ������MoId
		Long moId = sequenceService.getNext("BTS");
		mcBts.setMoId(moId);

		// ���û�վ����״̬Ϊ���߹���״̬
		mcBts.setManageStateCode(ManageState.ONLINE_STATE);

		// ����DAO�洢����
		try {
			mcBtsBasicDAO.saveOrUpdate(mcBts);
		} catch (Exception e) {
			logger.error("failed to add mcbts: " + mcBts, e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason"));
		}
		// ���»���
		McBtsCache.getInstance().addOrUpdate(mcBts);

		// ��ʼ����վ����
		mcBtsTemplateManageService = OmpAppContext.getCtx().getBean(
				McBtsTemplateManageService.class);
		mcBtsTemplateManageService.initMcBtsData(mcBts);
		// ���û����еĻ�վƵ��ƫ����
		// RFConfig rfConfig = rfConfigDAO.queryByMoId(mcBts.getMoId());
		// if (rfConfig != null) {
		// mcBts.setBtsFreq(rfConfig.getFreqOffset());
		// }
		// ɾ���Է��ֻ���
		AutomaticFindMcBtsCache.getInstance().delete(mcBts.getBtsId());
	}

	/**
	 * �޸�һ����վ
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public void modify(McBts mcBts) throws Exception {
		Long moId = mcBts.getMoId();
		McBts mcBtsInCache = McBtsCache.getInstance().queryByMoId(moId);
		if (mcBtsInCache == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}

		// ��֤SAG��Ϣ
		checkSag(mcBts);
		// У���վ��Ϣ�Ƿ񸴺Ϲ���
		checkBts(mcBts);

		// ������ı�ʱ�����¶Ի�վ����ͬ����Դ����
		if (mcBtsInCache.getDistrictId() != mcBts.getDistrictId()) {
			MBMSBtsService mMBMSbtsService = AppContext.getCtx().getBean(
					MBMSBtsService.class);
			TConfMBMSBts mbmsBts = mMBMSbtsService.queryByMoId(moId);
			mbmsBts.setDistrictId(mcBts.getDistrictId());
			mMBMSbtsService.config(mbmsBts);
		}

		// ����DAO�洢����
		try {
			mcBtsBasicDAO.saveOrUpdate(mcBts);
		} catch (Exception e) {
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason"));
		}

		// ����ͬ����Դ�еĻ�վ��·��ϢΪδͬ��
		SimulcastManageService simulcastManageService = AppContext.getCtx()
				.getBean(SimulcastManageService.class);
		simulcastManageService.setSimulMcBtsLinkUnSync();

		// ���»���
		McBtsCache.getInstance().addOrUpdate(mcBts);

		// ���߹���״̬���һ�վΪ�����ӣ���Ҫͨ��Proxy���վ����������Ϣ
		if (mcBtsInCache != null && mcBtsInCache.isConfigurable()) {
			String publicIP = mcBtsInCache.getPublicIp();
			int publicPort = mcBtsInCache.getPublicPort();
			String handwareVersion = mcBtsInCache.getHardwareVersion();
			String softwareVersion = mcBtsInCache.getSoftwareVersion();
			int btsFreq = mcBtsInCache.getBtsFreq();
			HashMap<Key, Object> attrs = mcBtsInCache.getAttributes();
			mcBts.setEmsIp(emsServerIp);
			mcBts.setPublicIp(publicIP);
			mcBts.setPublicPort(publicPort);
			mcBts.setHardwareVersion(handwareVersion);
			mcBts.setSoftwareVersion(softwareVersion);
			mcBts.setBtsFreq(btsFreq);
			mcBts.setAttributes(attrs);
			mcBts.setBtsIp(mcBtsInCache.getBtsIp());
			mcBts.setManageStateCode(mcBtsInCache.getManageStateCode());
			// ת��ģ��
			GenericBizData data = new GenericBizData("mcbts_basic_info");

			data.addProperty(new GenericBizProperty("btsIp", mcBts.getBtsIp()));
			data.addProperty(new GenericBizProperty("sagDefaultGateway", mcBts
					.getSagDefaultGateway()));
			data.addProperty(new GenericBizProperty("sagSubNetMask", mcBts
					.getSagSubNetMask()));
			data.addProperty(new GenericBizProperty("sagDeviceId", mcBts
					.getSagDeviceId()));
			data.addProperty(new GenericBizProperty("sagVoiceIp", mcBts
					.getSagVoiceIp()));
			data.addProperty(new GenericBizProperty("sagSignalIp", mcBts
					.getSagSignalIp()));
			data.addProperty(new GenericBizProperty("btsMediaPort", mcBts
					.getBtsMediaPort()));
			data.addProperty(new GenericBizProperty("sagMediaPort", mcBts
					.getSagMediaPort()));
			data.addProperty(new GenericBizProperty("sagSignalPort", mcBts
					.getSagSignalPort()));
			data.addProperty(new GenericBizProperty("btsSignalPort", mcBts
					.getBtsSignalPort()));
			data.addProperty(new GenericBizProperty("locationAreaId", mcBts
					.getLocationAreaId()));
			data.addProperty(new GenericBizProperty("sagSignalPointCode", mcBts
					.getSagSignalPointCode()));
			data.addProperty(new GenericBizProperty("btsSignalPointCode", mcBts
					.getBtsSignalPointCode()));
			data.addProperty(new GenericBizProperty("emsIp", mcBts.getEmsIp()));
			data.addProperty(new GenericBizProperty("networkId", mcBts
					.getNetworkId()));
			data.addProperty(new GenericBizProperty("bootSource", mcBts
					.getBootSource()));
			data.addProperty(new GenericBizProperty("natAPKey", mcBts
					.getNatAPKey()));
			data.addProperty(new GenericBizProperty("sagVlanUsedFlag", mcBts
					.getSagVlanUsedFlag()));
			data.addProperty(new GenericBizProperty("sagVlanId", mcBts
					.getSagVlanId()));

			try {
				mcBtsBizProxy.config(moId, data);
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext
								.getMessage("mcbts.save_ok_but_send_failed")
								+ e.getLocalizedMessage());
			}
		}
	}

	/**
	 * ɾ��һ����վ
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void delete(Long moId) throws Exception {
		mcBtsBasicDAO.delete(moId);

		mcBtsTemplateManageService = OmpAppContext.getCtx().getBean(
				McBtsTemplateManageService.class);
		// ɾ��ͨ��ģ�����ӵ�ҵ��
		mcBtsTemplateManageService.rollBackMcBtsData(moId);

		// ɾ���ڽӱ�͸����ڽӱ�ҵ��
		neighbourDAO.deleteOld(moId);
		appendNeighborDAO.deleteOld(moId);
		neighborFailedDAO.delByMoId(moId);
		alarmDAO.deleteAlarmByMoId(moId);
		// ɾ�����Դ�Ĺ�����ϵ
		mcBtsPowerConfigDAO.deleteRelationByMoId(moId);

		// �ӻ�����ɾ����վ,���������,��Ϊ�ϱߵ�rollBackMcBtsData�����õ���������еļ�¼
		McBtsCache.getInstance().delete(moId);
	}

	/**
	 * �޸Ĺ���״̬
	 * 
	 * @param moId
	 * @param manageState
	 * @throws Exception
	 */
	public void changeManageState(Long moId, ManageState manageState)
			throws Exception {
		McBts mcBtsInCache = McBtsCache.getInstance().queryByMoId(moId);
		if (mcBtsInCache == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		int manageStateCode = manageState.getStateCode();
		mcBtsInCache.setManageStateCode(manageStateCode);
		mcBtsBasicDAO.saveOrUpdate(mcBtsInCache);
	}

	/**
	 * ����MoId��ѯMcBts������Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public McBts queryByMoId(Long moId) throws Exception {
		return McBtsCache.getInstance().queryByMoId(moId);
	}

	@Override
	public void fillExportList(Business business) throws Exception {
		List<McBts> btsList = McBtsCache.getInstance().queryAll();

		for (McBts mcBts : btsList) {
			long moId = mcBts.getMoId();

			business.getCell("btsId").putContent(moId,
					toJSON("btsId", "0x" + mcBts.getHexBtsId()));

			business.getCell("name").putContent(moId,
					toJSON("name", mcBts.getName()));

			business.getCell("btsType").putContent(
					moId,
					toJSON("btsType", getBtsTypeString(mcBts.getBtsType())
							+ " " + getFreqTypeString(mcBts.getBtsFreqType())
							+ "(" + mcBts.getBtsFreqType() + ")"));

			business.getCell("btsIp").putContent(moId,
					toJSON("btsIp", mcBts.getBtsIp()));

			business.getCell("locationAreaId").putContent(
					moId,
					toJSON("locationAreaId",
							getHexLocationId(mcBts.getLocationAreaId()) + "("
									+ mcBts.getLocationAreaId() + ")"));

			business.getCell("antennaType").putContent(
					moId,
					toJSON("antennaType",
							getAntennaTypeString(mcBts.getAntennaType()) + "("
									+ mcBts.getAntennaType() + ")"));

			business.getCell("antennaAngle").putContent(
					moId,
					toJSON("antennaAngle",
							String.valueOf(mcBts.getAntennaAngle())));

			business.getCell("voiceDirectConnFlag").putContent(
					moId,
					toJSON("voiceDirectConnFlag",
							String.valueOf(mcBts.getVoiceDirectConnFlag())));

			business.getCell("networkId").putContent(moId,
					toJSON("networkId", String.valueOf(mcBts.getNetworkId())));

			business.getCell("bootSource").putContent(
					moId,
					toJSON("bootSource",
							getBootSourceString(mcBts.getBootSource()) + "("
									+ mcBts.getBootSource() + ")"));

			business.getCell("natAPKey").putContent(moId,
					toJSON("natAPKey", String.valueOf(mcBts.getNatAPKey())));

			business.getCell("sagVlanUsedFlag")
					.putContent(
							moId,
							toJSON("sagVlanUsedFlag",
									getSagVlanUsedFlagString(mcBts
											.getSagVlanUsedFlag())
											+ "("
											+ mcBts.getSagVlanUsedFlag() + ")"));

			business.getCell("sagVlanId").putContent(moId,
					toJSON("sagVlanId", String.valueOf(mcBts.getSagVlanId())));

			business.getCell("sagBtsIp").putContent(moId,
					toJSON("sagBtsIp", mcBts.getSagBtsIp()));

			business.getCell("sagDefaultGateway").putContent(moId,
					toJSON("sagDefaultGateway", mcBts.getSagDefaultGateway()));

			business.getCell("sagSubNetMask").putContent(moId,
					toJSON("sagSubNetMask", mcBts.getSagSubNetMask()));

			business.getCell("sagDeviceId").putContent(
					moId,
					toJSON("sagDeviceId",
							String.valueOf(mcBts.getSagDeviceId())));

			business.getCell("sagVoiceIp").putContent(moId,
					toJSON("sagVoiceIp", mcBts.getSagVoiceIp()));

			business.getCell("sagSignalIp").putContent(moId,
					toJSON("sagSignalIp", mcBts.getSagSignalIp()));

			business.getCell("sagSignalPointCode").putContent(
					moId,
					toJSON("sagSignalPointCode",
							String.valueOf(mcBts.getSagSignalPointCode())));

			business.getCell("sagMediaPort").putContent(
					moId,
					toJSON("sagMediaPort",
							String.valueOf(mcBts.getSagMediaPort())));

			business.getCell("sagSignalPort").putContent(
					moId,
					toJSON("sagSignalPort",
							String.valueOf(mcBts.getSagSignalPort())));

			business.getCell("btsMediaPort").putContent(
					moId,
					toJSON("btsMediaPort",
							String.valueOf(mcBts.getBtsMediaPort())));

			business.getCell("btsSignalPort").putContent(
					moId,
					toJSON("btsSignalPort",
							String.valueOf(mcBts.getBtsSignalPort())));

			business.getCell("btsSignalPointCode").putContent(
					moId,
					toJSON("btsSignalPointCode",
							String.valueOf(mcBts.getBtsSignalPointCode())));

			business.getCell("districtName").putContent(
					moId,
					toJSON("districtName",
							getDistrictNameById(mcBts.getDistrictId())));
			business.getCell("templateId")
					.putContent(
							moId,
							toJSON("templateId",
									String.valueOf(mcBts.getTemplateId())));
		}
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		// ����Ҫʵ��
	}

	/**
	 * ����ҵ��,��ӻ�վ
	 * 
	 * @param bidStr
	 * @param business
	 * @throws Exception
	 */
	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		Map<String, Cell> cellMap = business.getCellMap();
		if (hexBtsId.toLowerCase().startsWith("0x")) {
			hexBtsId = hexBtsId.substring(2);
		}
		long btsId = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		boolean _add = false;
		if (mcBts == null) {
			_add = true;
			mcBts = new McBts();
		}

		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			Cell cell = entry.getValue();
			String value = cell.getContent().get(btsId);

			if (StringUtils.isBlank(value)) {
				return;
			}

			if (entry.getKey().equals("btsId"))
				mcBts.setBtsId(btsId);
			else if (entry.getKey().equals("name"))
				mcBts.setName(value);
			else if (entry.getKey().equals("btsType")) {
				int[] type = getTypeValue(value);
				mcBts.setBtsType(type[0]);
				mcBts.setBtsFreqType(type[1]);
			} else if (entry.getKey().equals("btsIp"))
				mcBts.setBtsConfigIp(value);
			else if (entry.getKey().equals("locationAreaId"))
				mcBts.setLocationAreaId(Long
						.parseLong(getPropertyRealValue(value)));
			else if (entry.getKey().equals("antennaType"))
				mcBts.setAntennaType(Integer
						.parseInt(getPropertyRealValue(value)));
			else if (entry.getKey().equals("antennaAngle"))
				mcBts.setAntennaAngle(Integer.parseInt(value));
			else if (entry.getKey().equals("voiceDirectConnFlag"))
				mcBts.setVoiceDirectConnFlag(Integer.parseInt(value));
			else if (entry.getKey().equals("networkId"))
				mcBts.setNetworkId(Integer.parseInt(value));
			else if (entry.getKey().equals("bootSource"))
				mcBts.setBootSource(Integer
						.parseInt(getPropertyRealValue(value)));
			else if (entry.getKey().equals("natAPKey"))
				mcBts.setNatAPKey(Integer.parseInt(value));
			else if (entry.getKey().equals("sagVlanUsedFlag"))
				mcBts.setSagVlanUsedFlag(Integer
						.parseInt(getPropertyRealValue(value)));
			else if (entry.getKey().equals("sagVlanId"))
				mcBts.setSagVlanId(Integer.parseInt(value));
			else if (entry.getKey().equals("sagBtsIp"))
				mcBts.setSagBtsIp(value);
			else if (entry.getKey().equals("sagDefaultGateway"))
				mcBts.setSagDefaultGateway(value);
			else if (entry.getKey().equals("sagSubNetMask"))
				mcBts.setSagSubNetMask(value);
			else if (entry.getKey().equals("sagDeviceId"))
				mcBts.setSagDeviceId(Long.parseLong(value));
			else if (entry.getKey().equals("sagVoiceIp"))
				mcBts.setSagVoiceIp(value);
			else if (entry.getKey().equals("sagSignalIp"))
				mcBts.setSagSignalIp(value);
			else if (entry.getKey().equals("sagSignalPointCode"))
				mcBts.setSagSignalPointCode(Integer.parseInt(value));
			else if (entry.getKey().equals("sagMediaPort"))
				mcBts.setSagMediaPort(Integer.parseInt(value));
			else if (entry.getKey().equals("sagSignalPort"))
				mcBts.setSagSignalPort(Integer.parseInt(value));
			else if (entry.getKey().equals("btsMediaPort"))
				mcBts.setBtsMediaPort(Integer.parseInt(value));
			else if (entry.getKey().equals("btsSignalPort"))
				mcBts.setBtsSignalPort(Integer.parseInt(value));
			else if (entry.getKey().equals("btsSignalPointCode"))
				mcBts.setBtsSignalPointCode(Integer.parseInt(value));
			else if (entry.getKey().equals("districtName")) {
				Long id = getDistrictIdByName(value);
				mcBts.setDistrictId(id);
			} else if (entry.getKey().equals("templateId"))
				mcBts.setTemplateId(Integer.parseInt(value));

			mcBts.setTypeId(100);
		}
		// TODO ���λ���������ڣ����Զ����λ����

		McBtsBasicFacade facade = AppContext.getCtx().getBean(
				McBtsBasicFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(mcBts
				.getHexBtsId());
		if (_add) {
			facade.add(operObject, mcBts);
		} else {
			facade.modify(operObject, mcBts);
		}
	}

	/**
	 * ���ݵ������ƻ�ȡID
	 * 
	 * @param districtName
	 * @return
	 */
	private Long getDistrictIdByName(String districtName) {
		DistrictManageService service = AppContext.getCtx().getBean(
				DistrictManageService.class);
		List<District> districtList = service.queryAll();
		for (District district : districtList) {
			if (district.getName().equals(districtName)) {
				return district.getId();
			}
		}
		return null;
	}

	/**
	 * ���ݵ���ID��ȡ����
	 * 
	 * @param id
	 * @return
	 */
	private String getDistrictNameById(long id) {
		DistrictManageService service = AppContext.getCtx().getBean(
				DistrictManageService.class);
		List<District> districtList = service.queryAll();
		for (District district : districtList) {
			if (district.getId() == id) {
				return district.getName();
			}
		}
		return "";
	}

	private static String toJSON(String key, String value) {
		return "\"" + key + "\":\"" + value + "\"";
	}

	/**
	 * ����BtsId��ѯMcBts��Ϣ
	 * 
	 * @param btsId
	 * @return
	 * @throws Exception
	 */
	public McBts queryByBtsId(Long btsId) throws Exception {
		return McBtsCache.getInstance().queryByBtsId(btsId);
	}

	/**
	 * ������ͬƵ���ѯMcBts
	 */
	public List<McBts> queryBySameFreq(int freq) throws Exception {
		return McBtsCache.getInstance().queryBySameFreq(freq);
	}

	public List<McBts> queryAll() throws Exception {
		return McBtsCache.getInstance().queryAll();
	}

	@Override
	public List<McBts> queryByBtsType(int btsType) {
		return McBtsCache.getInstance().queryByBtsType(btsType);
	}

	@Override
	public Map<Long, McBts> getMapByMoId() {
		return McBtsCache.getInstance().getMapByMoId();
	}

	@Override
	public Map<Long, McBts> getMapByBtsId() {
		return McBtsCache.getInstance().getMapByBtsId();
	}

	/**
	 * ����������ѯ��վ
	 * 
	 * @param mcBtsCondition
	 * @return
	 * @throws Exception
	 */
	public PagingData queryAllByCondition(McBtsCondition mcBtsCondition)
			throws Exception {
		PagingData<McBts> pagingData = McBtsCache.getInstance()
				.queryAllByCondition(mcBtsCondition);
		List<McBts> btsList = pagingData.getResults();
		// ���澯����
		fillAlarmLevel(btsList);
		return pagingData;
	}

	@Override
	public List<McBts> queryMcBtsBy(McBtsCondition condition) throws Exception {
		return McBtsCache.getInstance().queryClonedMcBtsBy(condition);
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		McBts mcbts = this.queryByMoId(moId);
		if (mcbts != null && mcbts.isConfigurable()) {
			this.modify(mcbts);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		McBts mcbts = McBtsCache.getInstance().queryByMoId(moId);

		if (mcbts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// ���߹���״̬�£���Ҫͨ��Proxy��MO����������Ϣ
		if (mcbts != null && mcbts.isConfigurable()) {
			McBts mcBtsInCache = McBtsCache.getInstance().queryByMoId(moId);
			if (!mcBtsInCache.isConnected()) {
				throw new Exception(OmpAppContext.getMessage("bts_unconnected"));
			}

			GenericBizData data = new GenericBizData("mcbts_basic_info");
			try {
				GenericBizData result = mcBtsBizProxy.query(moId, data);
				mcbts = (McBts) result.getModel(mcbts);
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_query_failed_reason")
								+ e.getLocalizedMessage());
			}
			// ����DAO�洢����
			try {
				mcBtsBasicDAO.saveOrUpdate(mcbts);
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("save_data_failed_reason"));
			}
			// ���»���
			McBtsCache.getInstance().addOrUpdate(mcbts);
		}

	}

	/**
	 * ���ݻ�վMoId�б��ѯһ�л�վ������״̬
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public List<McBts> queryByMoIdList(List<Long> moIds) throws Exception {
		List<McBts> btsList = new ArrayList<McBts>();
		for (Long moId : moIds) {
			McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
			McBts newBts = null;
			if (mcBts == null) {
				newBts = new McBts();
				newBts.setMoId(moId);
				newBts.setDeleted();
			} else {
				newBts = mcBts.clone();
			}
			btsList.add(newBts);
		}
		// ���澯����
		fillAlarmLevel(btsList);
		return btsList;
	}

	/**
	 * ���澯����
	 * 
	 * @param btsList
	 */
	private void fillAlarmLevel(List<McBts> btsList) {
		List<Long> moIds = new LinkedList<Long>();
		for (McBts bts : btsList) {
			moIds.add(bts.getMoId());
		}
		// ��ȡ��߼���澯
		Map<Long, Integer> alarmLevelMapping = alarmDAO
				.queryMaxLevelMapping(moIds);
		for (McBts bts : btsList) {
			Integer alarmLevel = 0;
			if (alarmLevelMapping.containsKey(bts.getMoId())) {
				alarmLevel = alarmLevelMapping.get(bts.getMoId());
			}
			bts.addAttribute(McBtsAttribute.Key.ALARM_LEVEL, alarmLevel);
		}
	}

	/**
	 * ��֤Ƶ���Ƿ��������ЧƵ�㼯����
	 * 
	 * @param freqType
	 * @param freq
	 * @throws Exception
	 */
	private void systemFreqValidate(int freqType) throws Exception {
		FreqConvertUtil freqConvertUtil = new FreqConvertUtil();
		freqConvertUtil.setFreqType(freqType);
		int freq = freqConvertUtil.getMinOffset();
		double freqValue = freqConvertUtil.getMidFreqValue(freq);
		String typeName = freqConvertUtil.getTypeName();
		int freqSwitch = sysFreqService.queryFreqSwitch();
		if (freqSwitch != TSysFreqToBts.FREQ_ALL) {
			List<TSysFreqModule> configList = sysFreqService
					.queryData(freqType);
			if (configList == null || configList.size() == 0) {
				throw new Exception(OmpAppContext.getMessage(
						"mcbts.add.system_freq_validate_error", new Object[] {
								String.valueOf(freqValue), typeName }));
			}
			for (TSysFreqModule module : configList) {
				if (module.getFreq() == freq) {
					return;
				}
			}
			throw new Exception(OmpAppContext.getMessage(
					"mcbts.add.system_freq_validate_error", new Object[] {
							String.valueOf(freqValue), typeName }));
		}
	}

	/**
	 * У���վ���ò���
	 * 
	 * @param bts
	 * @throws Exception
	 */
	private void checkBts(McBts bts) throws Exception {
		// ͬһ��SAG�£����»�վ����������ͬ��
		// ��վID��������3λ��
		// ��վ�����
		// SAG����IP + Port
		// SAGý��IP + Port

		// ���Ȼ�ȡͬһ��SAG�µ����л�վ�б�
		McBtsCondition condition = new McBtsCondition();
		condition.setSagId(bts.getSagDeviceId());
		List<McBts> btsList = McBtsCache.getInstance().queryClonedMcBtsBy(
				condition);
		for (McBts btsInCache : btsList) {
			if (btsInCache.getBtsId().longValue() == bts.getBtsId().longValue()) {
				// ��ͬ��վ���Ƚ�
				continue;
			}
			// ��վID��3λ������ͬ
			if (getBriefBtsId(btsInCache) == getBriefBtsId(bts)) {
				String errorMsg = OmpAppContext.getMessage(
						"mcbts.duplicate_last_3_char_of_btsid",
						new Object[] { btsInCache.getHexBtsId(),
								btsInCache.getName() });
				throw new Exception(errorMsg);
			}
			// ��վ����㲻����ͬ
			if (btsInCache.getBtsSignalPointCode() == bts
					.getBtsSignalPointCode()) {
				String errorMsg = OmpAppContext.getMessage(
						"mcbts.duplicate_bts_signal_pointcode",
						new Object[] { btsInCache.getHexBtsId(),
								btsInCache.getName() });
				throw new Exception(errorMsg);
			}
			// SAG����IP + Port ������ͬ
			if (btsInCache.getSagSignalIp().equals(bts.getSagSignalIp())
					&& btsInCache.getSagSignalPort() == bts.getSagSignalPort()) {
				String errorMsg = OmpAppContext.getMessage(
						"mcbts.duplicate_sag_signal_ip_port",
						new Object[] { btsInCache.getHexBtsId(),
								btsInCache.getName() });
				throw new Exception(errorMsg);
			}
			// SAGý��IP + Port ������ͬ
			if (btsInCache.getSagVoiceIp().equals(bts.getSagVoiceIp())
					&& btsInCache.getSagMediaPort() == bts.getSagMediaPort()) {
				String errorMsg = OmpAppContext.getMessage(
						"mcbts.duplicate_sag_media_ip_port",
						new Object[] { btsInCache.getHexBtsId(),
								btsInCache.getName() });
				throw new Exception(errorMsg);
			}
		}

	}

	/**
	 * ��֤SAG�Ƿ���ڣ����������Ϣ�Ƿ��������Ϣ���
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	private void checkSag(McBts mcBts) throws Exception {
		long sagDeviceId = mcBts.getSagDeviceId();
		List<SxcBasic> sagList = sxcBasicService.queryAll();
		// ����SAG
		SxcBasic sagInDB = null;
		for (SxcBasic sag : sagList) {
			if (sag.getSagId().equals(sagDeviceId)) {
				sagInDB = sag;
				break;
			}
		}
		// δ�ҵ�
		if (sagInDB == null) {
			throw new Exception(MessageFormat.format(
					OmpAppContext.getMessage("sag_not_exist"), sagDeviceId));
		} else {
			// ����ҵ��ˣ����������Ϣ����
			SxcBasic toBeCheck = new SxcBasic();
			toBeCheck.setSagId(mcBts.getSagDeviceId());
			toBeCheck.setSagDefaultGateway(mcBts.getSagDefaultGateway());
			toBeCheck.setSagSignalIp(mcBts.getSagSignalIp());
			toBeCheck.setSagVoiceIp(mcBts.getSagVoiceIp());
			// Ϊ�˱Ƚϣ�����һ�����Ե�ֵ����е���ͬ
			toBeCheck.setSagName(sagInDB.getSagName());
			toBeCheck.setSagSubnetMask(sagInDB.getSagSubnetMask());
			toBeCheck.setSagSignalPointCode(sagInDB.getSagSignalPointCode());
			if (!toBeCheck.equals(sagInDB)) {
				throw new Exception(OmpAppContext.getMessage("saginfo_illegal"));
			}
		}
	}

	/**
	 * ��ȡ��վID��3λ
	 * 
	 * @param bts
	 * @return
	 */
	private int getBriefBtsId(McBts bts) {
		return (int) (bts.getBtsId() & 0xfff);
	}

	private String getHexLocationId(long locationId) {
		try {
			String str = Long.toHexString(locationId);
			int fillLen = 7 - str.length();
			for (int i = 0; i < fillLen; i++) {
				str = "0" + str;
			}
			return str;
		} catch (Exception e) {
			return "";
		}
	}

	private String getAntennaTypeString(int antType) {
		switch (antType) {
		case McBts.ANTENNA_TYPE_OMNI:
			return OmpAppContext.getMessage("mcbts.antType.omni");
		case McBts.ANTENNA_TYPE_PANEL:
			return OmpAppContext.getMessage("mcbts.antType.panel");
		default:
			return "";
		}
	}

	private String getBootSourceString(int bootSource) {
		switch (bootSource) {
		case McBts.BOOT_SOURCE_BTS:
			return "BTS";
		case McBts.BOOT_SOURCE_EMS:
			return "EMS";
		default:
			return "";
		}
	}

	private static final int FLAG_NOT_USE = 0;
	private static final int FLAG_USED = 1;

	private String getSagVlanUsedFlagString(int flag) {
		switch (flag) {
		case FLAG_NOT_USE:
			return OmpAppContext.getMessage("mcbts.sagVlanUsedFlag.notuse");
		case FLAG_USED:
			return OmpAppContext.getMessage("mcbts.sagVlanUsedFlag.used");
		default:
			return "";
		}
	}

	private String getPropertyRealValue(String propertyValue) {
		int index = propertyValue.lastIndexOf("(");
		return propertyValue.substring(index + 1, propertyValue.length() - 1);
	}

	private int[] getTypeValue(String typeStr) {
		String[] array = typeStr.split(" ");
		int btsType = getBtsTypeValue(array[0]);
		String freqType = getPropertyRealValue(array[1]);
		int[] ret = new int[2];
		ret[0] = btsType;
		ret[1] = Integer.valueOf(freqType);
		return ret;
	}

	private int getBtsTypeValue(String btsTypeStr) {
		if (btsTypeStr.equals("")) {
			return 0;
		}
		return 0;
	}

	private String getBtsTypeString(int btsType) {
		return OmpAppContext.getMessage("mcbts.export.type." + btsType);
	}

	private static final String[] FREQ_TYPES = new String[] { "1.8G", "2.4G",
			"400M", "3.3G", "700M", "2.1G", "340M", "340M(R20)", "1.4G" };

	private String getFreqTypeString(int freqType) {
		return FREQ_TYPES[freqType];
	}

	@Override
	public void updateMcBtsCache(McBts mcBts) throws Exception {
		McBtsCache.getInstance().addOrUpdate(mcBts);
	}

}
