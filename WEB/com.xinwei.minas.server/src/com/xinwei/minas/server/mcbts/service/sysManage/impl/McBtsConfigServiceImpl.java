/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-2	| tiance 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.McBtsBasicFacade;
import com.xinwei.minas.mcbts.core.facade.McBtsBizFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsConfig;
import com.xinwei.minas.server.mcbts.dao.McBtsBizDAO;
import com.xinwei.minas.server.mcbts.service.McBtsBasicService;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.common.GPSDataService;
import com.xinwei.minas.server.mcbts.service.common.PerfLogConfigService;
import com.xinwei.minas.server.mcbts.service.layer1.CalibrationDataService;
import com.xinwei.minas.server.mcbts.service.layer1.CalibrationTypeService;
import com.xinwei.minas.server.mcbts.service.layer1.L1GeneralSettingService;
import com.xinwei.minas.server.mcbts.service.layer2.AirlinkService;
import com.xinwei.minas.server.mcbts.service.layer2.ChangeAlgParamService;
import com.xinwei.minas.server.mcbts.service.layer2.ResManagementService;
import com.xinwei.minas.server.mcbts.service.layer2.SDMAConfigService;
import com.xinwei.minas.server.mcbts.service.layer3.ACLService;
import com.xinwei.minas.server.mcbts.service.layer3.LoadBalanceService;
import com.xinwei.minas.server.mcbts.service.layer3.MBMSBtsService;
import com.xinwei.minas.server.mcbts.service.layer3.NeighborService;
import com.xinwei.minas.server.mcbts.service.layer3.RCPEService;
import com.xinwei.minas.server.mcbts.service.layer3.RemoteBtsService;
import com.xinwei.minas.server.mcbts.service.layer3.RepeaterService;
import com.xinwei.minas.server.mcbts.service.layer3.SAGParamService;
import com.xinwei.minas.server.mcbts.service.layer3.VlanService;
import com.xinwei.minas.server.mcbts.service.layer3.WCPEService;
import com.xinwei.minas.server.mcbts.service.layer3.WeakFaultService;
import com.xinwei.minas.server.mcbts.service.sysManage.McBtsConfigService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;

/**
 * 
 * ��վ���õ��뵼���ķ�����
 * 
 * 
 * @author tiance
 * 
 */
public class McBtsConfigServiceImpl implements McBtsConfigService {

	private static Log log = LogFactory.getLog(McBtsConfigServiceImpl.class);

	private static McBtsConfig mcBtsConfig;

	private static final String EXPORT_CONFIG = "./plugins/mcbts/importexport/mcbts-config.xml";

	// moid���б�
	private static List<Long> moIdList;

	private McBtsBizDAO mcBtsBizDAO;

	public McBtsConfigServiceImpl() {
		super();
	}

	public static void init() {
		Digester digester = new Digester();

		List<Business> businesses = new LinkedList<Business>();
		digester.push(businesses);

		digester.setValidating(false);

		digester.addObjectCreate("bizs/biz", Business.class);
		digester.addSetProperties("bizs/biz");

		digester.addCallMethod("bizs/biz", "initBusiness", 5);
		digester.addCallParam("bizs/biz", 0, "name");
		digester.addCallParam("bizs/biz", 1, "desc_zh");
		digester.addCallParam("bizs/biz", 2, "desc_en");
		digester.addCallParam("bizs/biz", 3, "generic");
		digester.addCallParam("bizs/biz", 4, "configurable");

		digester.addCallMethod("bizs/biz/bizName/cell", "addCell", 4);
		digester.addCallParam("bizs/biz/bizName/cell", 0, "name");
		digester.addCallParam("bizs/biz/bizName/cell", 1, "desc_zh");
		digester.addCallParam("bizs/biz/bizName/cell", 2, "desc_en");
		digester.addCallParam("bizs/biz/bizName/cell", 3, "wireless");

		digester.addCallMethod("bizs/biz/service/cell", "addCell", 4);
		digester.addCallParam("bizs/biz/service/cell", 0, "name");
		digester.addCallParam("bizs/biz/service/cell", 1, "desc_zh");
		digester.addCallParam("bizs/biz/service/cell", 2, "desc_en");
		digester.addCallParam("bizs/biz/service/cell", 3, "wireless");

		digester.addCallMethod("bizs/biz/bizName", "addSource", 1);
		digester.addCallParam("bizs/biz/bizName", 0, "value");

		digester.addCallMethod("bizs/biz/service", "addSource", 1);
		digester.addCallParam("bizs/biz/service", 0, "class");

		digester.addSetNext("bizs/biz", "add");

		try {
			digester.parse(new FileInputStream(EXPORT_CONFIG));

			mcBtsConfig = new McBtsConfig();
			mcBtsConfig.setBusinesses(businesses);

		} catch (Exception e) {
			log.error("Error initializing mcbts-config.xml");
		}
	}

	@Override
	public String[] export() throws Exception {
		mcBtsConfig = getMcBtsConfig(true);

		// �����������ɽ��
		String[] btsStr = generateConfig();

		mcBtsConfig = null;

		return btsStr;
	}

	@Override
	public McBtsConfig getMcBtsConfig(boolean withData) throws RemoteException,
			Exception {
		// ��ʼ��ģ��
		init();

		if (withData) {
			// �������
			fillCell();
		}

		return mcBtsConfig;
	}

	@Override
	public void importScript(String str) throws Exception {
		// TODO Auto-generated method stub
		init();

		fillCell(str);
	}

	private void fillCell() throws Exception {
		List<Business> bizList = mcBtsConfig.getBusinesses();
		for (Business business : bizList) {
			List<String> sources = business.getSources();

			for (String source : sources) {
				if (business.isGeneric()) {
					// ͨ��ҵ��
					try {
						List<GenericBizData> list = mcBtsBizDAO
								.queryExportList(source);
						// ��BizData���б�ת��ΪCell����business
						parseBizDataToCell(business, list);
					} catch (Exception e) {
						log.error("[McBtsConfig]: Error querying data for operation:"
								+ source);
						throw e;
					}
				} else {
					// ��ͨ��ҵ��
					Class<?> customClazz = Class.forName(source);
					Object obj = AppContext.getCtx().getBean(customClazz);
					if (obj instanceof McBtsBasicService) {
						McBtsBasicService service = (McBtsBasicService) obj;
						service.fillExportList(business);

						// ��ȡ���е�moId
						moIdList = new ArrayList<Long>(business
								.getCell("btsId").getContent().keySet());

						if (moIdList.isEmpty()) {
							return;
						}
					} else {
						Class<? extends Object> clazz = obj.getClass();

						Method method = clazz.getMethod("fillExportList",
								Business.class, List.class);
						method.invoke(obj, business, moIdList);
					}
				}
			}
		}
	}

	private void fillCell(String str) throws Exception {

	}

	/**
	 * �������õ�����
	 * 
	 * @return
	 */
	public static String[] generateConfig() {
		StringBuilder[] sbs = new StringBuilder[moIdList.size()];

		if (moIdList.isEmpty()) {
			return null;
		}

		for (int i = 0; i < moIdList.size(); i++) {
			sbs[i] = new StringBuilder();
			sbs[i].append("[");
		}

		// ����ҵ���б�
		for (Business business : mcBtsConfig.getBusinesses()) {
			// ���ҵ���Ԫ�����б�
			List<Cell> cellList = business.getCellList();

			// �������е�moId
			for (int i = 0; i < moIdList.size(); i++) {
				// �ж����ĳ��moId��ȡֵΪnull,˵��û�������վ������
				boolean stop_this = true;
				for (Cell cell : cellList) {
					if (cell.getContentByMoId(moIdList.get(i)) != null) {
						stop_this = false;
					}
				}
				if (stop_this)
					continue;

				// ����ƴд��ҵ�������
				if (sbs[i].length() > 1)
					sbs[i].append(",{\"bizName\":\"")
							.append(business.getName()).append("\"");
				else {
					sbs[i].append("{\"bizName\":\"").append(business.getName())
							.append("\"");
				}

				// ����ÿ��Ԫ��
				for (Cell cell : cellList) {
					String str = cell.getContentByMoId(moIdList.get(i));
					if (StringUtils.isNotBlank(str))
						sbs[i].append(",").append(str);
				}

				sbs[i].append("}");
			}
		}

		String[] result = new String[sbs.length];
		for (int i = 0; i < sbs.length; i++) {
			result[i] = sbs[i].append("]").toString();
		}

		return result;
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
	public void importMcBtsConfig(List<String> idList, McBtsConfig mcBtsConfig)
			throws Exception {
		// ���ڴ�McBtsConfig�е�content��ֱ��ע��xls����е�ֵ,���Ժ͵���ʱ���content��ȫһ��,����������Ϊ׼
		List<Business> bizList = mcBtsConfig.getBusinesses();

		for (String hexBtsId : idList) {
			for (Business business : bizList) {
				List<String> sources = business.getSources();

				if (business.isGeneric()) {
					// �޸�ͨ��ҵ��
					try {
						log.debug("[McBtsConfig IMPORT] BID: " + hexBtsId
								+ " ,Generic Business: " + business.getName()
								+ ", zh: " + business.getDesc_zh());
						genericModify(hexBtsId, business);
					} catch (Exception e) {
						log.error("[McBtsConfig IMPORT ERROR] BID: " + hexBtsId
								+ " ,Generic Business: " + business.getName()
								+ ", zh: " + business.getDesc_zh(), e);
					}
				} else {
					// �޸��Զ���ҵ��
					log.debug("[McBtsConfig IMPORT] BID: " + hexBtsId
							+ " ,Custom Business: " + business.getName()
							+ ", zh: " + business.getDesc_zh());
					for (String source : sources) {
						log.debug("[McBtsConfig IMPORT SERVICE] Name: "
								+ source);
						Class<? extends Object> clazz = Class.forName(source);
						Object obj = AppContext.getCtx().getBean(clazz);

						try {
							Method method = clazz.getMethod(
									"addOrUpdateBusiness", String.class,
									Business.class);
							method.invoke(obj, hexBtsId, business);
						} catch (Exception e) {
							log.error(
									"[McBtsConfig IMPORT ERROR] BID: "
											+ hexBtsId + " ,Custom Business: "
											+ business.getName() + ", zh: "
											+ business.getDesc_zh(), e);
						}
					}
				}
			}
		}
	}

	/**
	 * ����XLS,��ͨ��ҵ��Ĵ洢
	 * 
	 * @param hexBtsId
	 * @param business
	 * @throws Exception
	 */
	private void genericModify(String hexBtsId, Business business)
			throws Exception {
		Long bid = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(bid);
		if (mcBts == null)
			return;
		for (String operName : business.getSources()) {
			log.debug("[McBtsConfig IMPORT OPERATION] Name: " + operName);
			GenericBizData bizData = new GenericBizData(operName);
			List<Cell> cellList = business.getCellListByOperName(operName);
			for (Cell cell : cellList) {
				String content = cell.getContent().get(bid);
				if (StringUtils.isBlank(content)) {
					return;
				}

				GenericBizProperty property = new GenericBizProperty(
						cell.getName(), content);

				bizData.addProperty(property);
			}

			OperObject operObject = OperObject.createBtsOperObject(hexBtsId);

			McBtsBizFacade bizFacade = getMcBtsBizFacade();
			bizFacade.config(operObject, mcBts.getMoId(), bizData);
			log.debug("[McBtsConfig IMPORT OPERATION SUCCESSFULLY] Name: "
					+ operName);
		}
	}

	private static McBtsBizFacade getMcBtsBizFacade() {
		McBtsBizFacade facade = AppContext.getCtx().getBean(
				McBtsBizFacade.class);

		return facade;
	}

	public void setMcBtsBizDAO(McBtsBizDAO mcBtsBizDAO) {
		this.mcBtsBizDAO = mcBtsBizDAO;
	}

}
