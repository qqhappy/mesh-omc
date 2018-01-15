/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-17	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.SystemProperty;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.mcbts.core.model.layer2.PECCHSetting;
import com.xinwei.minas.mcbts.core.model.layer2.SCGChannelConfigItem;
import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.mcbts.core.model.sysManage.LocationArea;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsLink;
import com.xinwei.minas.mcbts.core.model.sysManage.Simulcast;
import com.xinwei.minas.server.core.conf.service.SystemPropertyService;
import com.xinwei.minas.server.mcbts.dao.sysManage.SimulcastManageDAO;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer2.AirlinkService;
import com.xinwei.minas.server.mcbts.service.layer3.MBMSBtsService;
import com.xinwei.minas.server.mcbts.service.sysManage.LocAreaService;
import com.xinwei.minas.server.mcbts.service.sysManage.SimulcastManageService;
import com.xinwei.minas.server.mcbts.utils.MBMSUtils;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * ͬ����Դ���������
 * 
 * 
 * @author tiance
 * 
 */

public class SimulcastManageServiceImpl implements SimulcastManageService {

	private static Logger logger = null;

	private SequenceService sequenceService;

	private SimulcastManageDAO simulcastManageDAO;

	private SystemPropertyService systemPropertyService;

	private static final String CATEGORY = "platform";
	// ������·��Ϣ,Ŀǰ����ʵ��
	public static final String PROPERTY1 = "mcbts_link_sync";
	// ͬ����Դ��Ϣ
	private static final String PROPERTY2 = "simulcast_resource_sync";

	private static final String UN_SYNC = "false";

	private static final String SYNC = "true";

	private Lock syncLock = new ReentrantLock();

	private String iM3000IP;

	private String iM3000PORT;

	public SimulcastManageServiceImpl() {
		super();
		createLoggerIfNotExist();
	}

	@Override
	public List<Simulcast> queryAll() {
		return simulcastManageDAO.queryAll();
	}

	@Override
	public List<Simulcast> queryByDistrictId(long districtId) {
		return simulcastManageDAO.queryByDistrictId(districtId);
	}

	/**
	 * ���������ļ���־
	 */
	private void createLoggerIfNotExist() {
		if (logger != null) {
			return;
		}
		String fileName = "log/SimulcastSynchronize.log";

		try {
			logger = Logger.getLogger(SimulcastManageServiceImpl.class);
			logger.setAdditivity(false);
			logger.setLevel(Level.DEBUG);

			logger.removeAllAppenders();

			PatternLayout patterLayout = new PatternLayout();
			patterLayout.setConversionPattern("%d %m%n");
			RollingFileAppender appender = null;
			appender = new RollingFileAppender(patterLayout, fileName);
			appender.setMaxBackupIndex(5);
			appender.setMaxFileSize("4096kb");
			logger.addAppender(appender);

		} catch (Exception e) {
			logger.error("createLoggerIfNotExist()", e); //$NON-NLS-1$
			logger = Logger.getLogger(SimulcastManageServiceImpl.class);
		}
	}

	public static Logger getLogger() {
		return logger;
	}

	@Override
	public void saveOrUpdate(Simulcast simulcast) {
		if (simulcast.getIdx() == 0L)
			simulcast.setIdx(sequenceService.getNext());

		simulcastManageDAO.saveOrUpdate(simulcast);

		// ͬ����Դ��Ϣ�޸�Ϊ
		systemPropertyService = AppContext.getCtx().getBean(
				SystemPropertyService.class);
		systemPropertyService.setProperty(CATEGORY, null, PROPERTY2, UN_SYNC);
	}

	@Override
	public void setSimulMcBtsLinkUnSync() {
		systemPropertyService = AppContext.getCtx().getBean(
				SystemPropertyService.class);
		systemPropertyService.setProperty(CATEGORY, null, PROPERTY1, UN_SYNC);
	}

	@Override
	public void delete(Simulcast simulcast) {
		simulcastManageDAO.delete(simulcast);

		// ͬ����Դ��Ϣ�޸�Ϊ
		systemPropertyService = AppContext.getCtx().getBean(
				SystemPropertyService.class);
		systemPropertyService.setProperty(CATEGORY, null, PROPERTY2, UN_SYNC);
	}

	@Override
	public boolean[] querySyncStatus() {
		boolean[] b = new boolean[2];

		systemPropertyService = AppContext.getCtx().getBean(
				SystemPropertyService.class);

		SystemProperty s1 = systemPropertyService.getProperty(CATEGORY, null,
				PROPERTY1);
		String value1 = s1.getValue();
		b[0] = stringToBoolean(value1);

		SystemProperty s2 = systemPropertyService.getProperty(CATEGORY, null,
				PROPERTY2);
		String value2 = s2.getValue();
		b[1] = stringToBoolean(value2);

		return b;
	}

	private boolean stringToBoolean(String value) {
		if (value.equalsIgnoreCase(SYNC))
			return true;
		else
			return false;
	}

	@Override
	public void sync(boolean[] toSync) throws Exception {
		if (syncLock.tryLock()) {
			try {
				MBMSUtils.initIM3000Info(iM3000IP, iM3000PORT);

				// ��ȡ���л�վ�õ���SAG ID
				Map<Long, List<McBts>> sagBtsMap = getSagMcBtsMap();

				Set<Long> sagSet = sagBtsMap.keySet();

				// TODO ɾ�����´���:
				// sagSet = new HashSet<Long>();
				// sagSet.add(1112L);
				// TODO ɾ������
				systemPropertyService = AppContext.getCtx().getBean(
						SystemPropertyService.class);

				if (sagSet.isEmpty()) {
					// ���û��sag,ֱ������Ϊ��ͬ��
					if (toSync[0])
						systemPropertyService.setProperty(CATEGORY, null,
								PROPERTY1, SYNC);
					if (toSync[1])
						systemPropertyService.setProperty(CATEGORY, null,
								PROPERTY2, SYNC);
					return;
				}

				StringBuilder noSagId = new StringBuilder();
				StringBuilder configError = new StringBuilder();

				List<Simulcast> simuList = getFixedSimuList();

				for (long sagId : sagSet) {
					try {
						// ͬ����վ��·��Ϣ
						if (toSync[0])
							syncMcBtsLink(sagId, sagBtsMap.get(sagId));

						// ͬ��ͬ����Դ��Ϣ
						if (toSync[1])
							syncSimulcast(sagId, simuList);
					} catch (NullPointerException e) {
						// �豸ID������
						noSagId.append(String.valueOf(sagId)).append("\n");
					} catch (Exception e) {
						logger.error(e);
						String errorMsg = e.getLocalizedMessage();
						if (errorMsg.equals("iM3000_CONFIG_ERROR")) {
							// iM3000�����ô���,����platform-config.properties
							MBMSUtils.disconnect();
							throw new Exception("SyncSagFail");
						}
						if (errorMsg.contains("CONFIG_ERROR")) {
							configError.append(String.valueOf(sagId)).append(
									"\n");
						}
					}
				}
				// �ͷ���Դ
				MBMSUtils.disconnect();

				if (toSync[0])
					systemPropertyService.setProperty(CATEGORY, null,
							PROPERTY1, SYNC);
				if (toSync[1])
					systemPropertyService.setProperty(CATEGORY, null,
							PROPERTY2, SYNC);

				if (noSagId.length() > 0 || configError.length() > 0) {
					throw new Exception(noSagId.toString() + "##"
							+ configError.toString());
				}
			} finally {
				syncLock.unlock();
			}
		} else {
			throw new Exception("ACTION_BLOCKED");
		}
	}

	/**
	 * ͬ����վ��·��Ϣ
	 * 
	 * @param sagId
	 * @param mcbtsList
	 * @return
	 * @throws Exception
	 */
	private boolean syncMcBtsLink(long sagId, List<McBts> mcbtsList)
			throws Exception {
		// ��iM3000��ѯ�Ļ�վ��·�б�
		List<McBtsLink> sagLinkList = MBMSUtils.queryBtsLinkList(sagId);
		// ��EMS��ƴ���Ļ�վ��·Map,KEYΪ��վHex ID
		Map<String, McBtsLink> btsLinkMap = getBtsLinkMap(mcbtsList);

		// �޸�
		for (McBtsLink sagLink : sagLinkList) {
			String btsId = sagLink.getBtsId();

			McBtsLink link = btsLinkMap.get(btsId);

			// ��֤����EMS�Ƿ���������վ��·,�����ھ���һ��
			if (link == null) {
				MBMSUtils.deleteMcBtsLink(sagLink.getLinkIndex(), sagId);
				continue;
			}

			// ��sag�е�index�Ƶ�ems��link��
			link.setLinkIndex(sagLink.getLinkIndex());

			// ���sag�ϵĻ�վ��·��Ϣ�ͱ���ems�ϵĻ�վ��·��Ϣ��ͬ,�Ͳ��ø���,��һ��
			if (sagLink.equals(link)) {
				btsLinkMap.remove(btsId);
				continue;
			}

			MBMSUtils.modifyMcBtsLink(sagId, sagLink, link);

			// ɾ���Ѿ���sag�д��ڵĻ�վ��·
			btsLinkMap.remove(btsId);
		}

		boolean[] index = queryIndex(sagId);

		// ����
		for (Entry<String, McBtsLink> entry : btsLinkMap.entrySet()) {
			McBtsLink link = entry.getValue();

			// �ҵ����ظ���index,Ȼ������¼
			for (int i = 1; i < index.length; i++) {
				if (index[i])
					continue;

				MBMSUtils.addMcBtsLink(i, sagId, link);
				index[i] = true;
				break;
			}
		}

		return true;
	}

	/**
	 * ͬ��ͬ����Դ��Ϣ
	 */
	private boolean syncSimulcast(long sagId, List<Simulcast> simuList)
			throws Exception {
		// ��ѯSAG�ϵ�ͬ����Դ
		List<Simulcast> sagSimuList = MBMSUtils.querySimulcastList(sagId);

		// ɾ��SAG�����
		for (Simulcast sagSimulcast : sagSimuList) {
			boolean exist = false;
			for (Simulcast simulcast : simuList) {
				if (simulcast.sameKeyWith(sagSimulcast)) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				MBMSUtils.deleteSimulcast(sagId, sagSimulcast);
			}
		}

		// �޸Ĳ�ͬ��
		for (Simulcast sagSimulcast : sagSimuList) {
			for (Iterator<Simulcast> iter = simuList.iterator(); iter.hasNext();) {
				Simulcast simulcast = iter.next();
				if (simulcast.sameKeyWith(sagSimulcast)) {
					if (!simulcast.sameValueWith(sagSimulcast)) {
						MBMSUtils.modifySimulcast(sagId, sagSimulcast,
								simulcast);
					}
					// ɾ�����Ƕ��е�ͬ����Դ
					iter.remove();
					break;
				}
			}
		}

		// ����SAGû�е�
		for (Simulcast simulcast : simuList) {
			MBMSUtils.addSimulcast(sagId, simulcast);
		}

		return true;
	}

	/**
	 * ��ȡ�����Ƿ�ʹ��,��ʹ����Ϊtrue
	 * 
	 * @return
	 */
	private boolean[] queryIndex(long sagId) throws Exception {
		boolean[] indexes = new boolean[129];

		// ��iM3000��ѯ�Ļ�վ��·�б�
		List<McBtsLink> sagLinkList = MBMSUtils.queryBtsLinkList(sagId);

		for (McBtsLink mcbtsLink : sagLinkList) {
			int i = Integer.parseInt(mcbtsLink.getLinkIndex());

			indexes[i] = true;
		}
		return indexes;
	}

	/**
	 * ����btsId��ȡ��վ��·��Ϣ�ŵ�HashMap�У�keyΪbtsId��valueΪMcBtsLink
	 * 
	 * @param btsList
	 * @return
	 * @throws Exception
	 */
	private static Map<String, McBtsLink> getBtsLinkMap(List<McBts> btsList) {
		Map<String, McBtsLink> linkMap = new HashMap<String, McBtsLink>();

		// ��ȡ����λ����
		List<LocationArea> areaList = getArea();

		for (McBts mcBts : btsList) {
			McBtsLink link = new McBtsLink();

			linkMap.put(mcBts.getHexBtsId(), link);

			long moId = mcBts.getMoId();
			// ���������Ϣ
			link.setBtsId(mcBts.getHexBtsId());
			link.setBtsName(mcBts.getName());
			link.setSafeParamIndex(String.valueOf(mcBts.getNatAPKey()));
			link.setBtsDVoiceFlag(String.valueOf(mcBts.getVoiceDirectConnFlag()));

			// λ����
			long areaId = mcBts.getLocationAreaId();
			for (LocationArea location : areaList) {
				if (areaId == location.getLongLocationId()) {
					link.setAreaID(location.getAreaId());
					break;
				}
			}

			// ͬ����վ
			TConfMBMSBts mbmsBts = getMBMSBtsConfig(moId);
			if (mbmsBts != null && mbmsBts.getFlag() != null)
				link.setMBMSFlag(String.valueOf(mbmsBts.getFlag()));

			// ����
			link.setDistrictId(String.valueOf(mcBts.getDistrictId()));

			// Ƶ��
			int freqType = mcBts.getBtsFreqType();
			link.setFreqType(String.valueOf(freqType + 2));

			// Ƶ��ƫ��
			link.setFreqOffset(String.valueOf(mcBts.getBtsFreq()));

			// ��վ�����
			link.setBtsSignalPointID(String.valueOf(mcBts
					.getBtsSignalPointCode()));

			// ��վ�����ý����Ϣ
			link.setBtsSignalIP(mcBts.getBtsConfigIp());
			link.setBtsSignalPort(String.valueOf(mcBts.getBtsSignalPort()));
			link.setBtsMediaIP(mcBts.getBtsConfigIp());
			link.setBtsMediaPort(String.valueOf(mcBts.getBtsMediaPort()));

			// SAG��Ϣ
			link.setSagMediaIP(mcBts.getSagVoiceIp());
			link.setSagMediaPort(String.valueOf(mcBts.getSagMediaPort()));
			link.setSagSignalIP(mcBts.getSagSignalIp());
			link.setSagSignalPort(String.valueOf(mcBts.getSagSignalPort()));

			// ������·
			AirlinkConfig airlink = getAirlinkConfig(moId);
			if (airlink != null) {
				link.setSeqID(String.valueOf(airlink.getSequenceId()));
				link.setTsNum(String.valueOf(airlink.getTotalTS()));
				link.setDownlinkTsNum(String.valueOf(airlink.getDownlinkTS()));
				link.setSCGMask(String.valueOf(airlink.getScgMask()));

				String[] SCGTSMasks = getForbiddenStr(airlink.getTchForbidden());
				link.setSCG0TSMask(SCGTSMasks[0]);
				link.setSCG1TSMask(SCGTSMasks[1]);
				link.setSCG2TSMask(SCGTSMasks[2]);
				link.setSCG3TSMask(SCGTSMasks[3]);
				link.setSCG4TSMask(SCGTSMasks[4]);

				List<SCGChannelConfigItem> list = airlink
						.getScgChannelConfigList();
				List<SCGChannelConfigItem> bch = new ArrayList<SCGChannelConfigItem>();
				List<SCGChannelConfigItem> rrch = new ArrayList<SCGChannelConfigItem>();
				List<SCGChannelConfigItem> rarch = new ArrayList<SCGChannelConfigItem>();
				for (SCGChannelConfigItem item : list) {
					if (item.getChannelType() == SCGChannelConfigItem.BCH) {
						bch.add(item);
						continue;
					}
					if (item.getChannelType() == SCGChannelConfigItem.RRCH) {
						rrch.add(item);
						continue;
					}
					if (item.getChannelType() == SCGChannelConfigItem.RARCH) {
						rarch.add(item);
						continue;
					}
				}
				link.setBCH(getSCGChannelStr(bch, AirlinkConfig.BCH_COUNT));
				link.setRRCH(getSCGChannelStr(rrch, AirlinkConfig.RRCH_COUNT));
				link.setRARCH(getSCGChannelStr(rarch, AirlinkConfig.RARCH_COUNT));
			}

			PECCHSetting pecch = airlink.getPecchSetting();
			if (pecch != null) {
				int scgIndex = 255;
				if (pecch.getScgIndex() != null) {
					if (pecch.getScgIndex() >= 1 && pecch.getScgIndex() <= 3) {
						scgIndex = pecch.getScgIndex();
					}
				}
				link.setPescgIdx(String.valueOf(scgIndex));

				link.setPepchNum(String.valueOf(pecch.getPchCount() == null
						|| pecch.getPchCount() > 255 ? 255 : pecch
						.getPchCount()));

				link.setPepepchId(String.valueOf(pecch.getPchIndex() == null
						|| pecch.getPchIndex() > 255 ? 255 : pecch
						.getPchIndex()));

				link.setPerarchNum(String.valueOf(pecch.getRarchCount() == null
						|| pecch.getRarchCount() > 255 ? 255 : pecch
						.getRarchCount()));

				link.setPerrchNum(String.valueOf(pecch.getRrchCount() == null
						|| pecch.getRrchCount() > 255 ? 255 : pecch
						.getRrchCount()));
			}

		}

		return linkMap;
	}

	public static String[] getForbiddenStr(String tsForbiddenStr) {
		String forbidden[] = new String[AirlinkConfig.SCG_COUNT];

		char[] tsForbiddenChars = tsForbiddenStr.toCharArray();
		if (tsForbiddenChars.length >= 20) {
			for (int i = 0; i < AirlinkConfig.SCG_COUNT; i++) {
				String str = new String(tsForbiddenChars, i * 4, 4);
				int tsMask = Integer.parseInt(str, 16);
				forbidden[i] = String.valueOf(tsMask);
			}
		}
		return forbidden;
	}

	public static String getSCGChannelStr(List<SCGChannelConfigItem> list,
			int size) {
		String bch[] = new String[size];
		// ��ʼ��
		for (int i = 0; i < size; i++) {
			bch[i] = "9999";
		}

		for (int i = 0; i < list.size(); i++) {
			SCGChannelConfigItem item = list.get(i);
			bch[i] = getValue(item.getScgIndex().intValue())
					+ getValue(item.getTsIndex().intValue());
		}
		return getObjValue(bch);
	}

	/**
	 * ������ƴ��Ϊ�ַ���
	 * 
	 * @param obj
	 * @return
	 */
	public static String getObjValue(Object[] obj) {
		String str = "";
		for (int i = 0; i < obj.length; i++) {
			str = str + obj[i];
		}
		return str;
	}

	/**
	 * 10���µ�����ǰ�油0
	 * 
	 * @param value
	 * @return
	 */
	public static String getValue(int value) {
		if (value < 10) {
			return String.valueOf("0" + value);
		} else {
			return String.valueOf(value);
		}
	}

	/**
	 * ��ȡͬ����վ������
	 * 
	 * @param moId
	 * @return
	 */
	private static TConfMBMSBts getMBMSBtsConfig(long moId) {
		try {
			MBMSBtsService service = AppContext.getCtx().getBean(
					MBMSBtsService.class);

			return service.queryByMoId(moId);
		} catch (Exception e) {
			logger.error("Error getting mbms bts config.", e);
		}

		return null;
	}

	/**
	 * ��ȡλ����
	 * 
	 * @return
	 */
	private static List<LocationArea> getArea() {
		LocAreaService service = AppContext.getCtx().getBean(
				LocAreaService.class);
		return service.queryAll();
	}

	/**
	 * ��ȡ������·������
	 * 
	 * @param moId
	 * @return
	 */
	private static AirlinkConfig getAirlinkConfig(long moId) {
		try {
			AirlinkService service = AppContext.getCtx().getBean(
					AirlinkService.class);
			return service.queryByMoId(moId);
		} catch (Exception e) {
			logger.error("Error getting airlink config.", e);
		}
		return null;
	}

	/**
	 * ��ȡ��sag IDΪkey,��վ�б�Ϊvalue��map
	 * 
	 * @return
	 */
	private Map<Long, List<McBts>> getSagMcBtsMap() {
		Map<Long, List<McBts>> map = new HashMap<Long, List<McBts>>();

		List<McBts> mcbtsList = McBtsCache.getInstance().queryAll();

		if (mcbtsList == null || mcbtsList.isEmpty())
			return new HashMap<Long, List<McBts>>();

		for (McBts mcBts : mcbtsList) {
			long sagId = mcBts.getSagDeviceId();

			List<McBts> list = map.get(sagId);

			if (list == null) {
				list = new ArrayList<McBts>();
				map.put(sagId, list);
			}

			list.add(mcBts);
		}

		return map;
	}

	/**
	 * ��ȡ�Ѿ��õ����ε�ͬ����Դ�б�
	 * 
	 * @return
	 */
	private List<Simulcast> getFixedSimuList() {
		// ��ȡ����ͬ����Դ��Ϣ
		List<Simulcast> simuList = queryAll();
		if (simuList == null)
			simuList = new ArrayList<Simulcast>();

		for (Simulcast simu : simuList) {
			// Ϊ�˺�iM3000��Ƶ�����Ͷ�Ӧ,Minas�е�����Ƶ�㶼+2
			simu.setFreqType(simu.getFreqType() + 2);
		}

		return simuList;
	}

	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	public void setSimulcastManageDAO(SimulcastManageDAO simulcastManageDAO) {
		this.simulcastManageDAO = simulcastManageDAO;
	}

	public void setiM3000IP(String iM3000IP) {
		this.iM3000IP = iM3000IP;
	}

	public void setiM3000PORT(String iM3000PORT) {
		this.iM3000PORT = iM3000PORT;
	}

}
