/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-6	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsAttribute;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsOperation;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsOperation.Operation;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsTemplate;
import com.xinwei.minas.mcbts.core.utils.FreqConvertUtil;
import com.xinwei.minas.server.core.conf.service.MoCache;
import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsTemplateManageDao;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.sysManage.McBtsTemplateManageService;

/**
 * 
 * ��վģ�����ķ���
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsTemplateManageServiceImpl implements
		McBtsTemplateManageService {

	private static Log log = LogFactory
			.getLog(McBtsTemplateManageServiceImpl.class);
	private static final String FILE = "./plugins/mcbts/template/template-table.xml";
	private static List<McBtsOperation> operationList = new ArrayList<McBtsOperation>();

	public McBtsTemplateManageServiceImpl() {
		// ת��template-table.xml������Ϊ����
		parserMcBtsTemplate();

	}

	private McBtsTemplateManageDao mcBtsTemplateManageDao;

	public void setMcBtsTemplateManageDao(
			McBtsTemplateManageDao mcBtsTemplateManageDao) {
		this.mcBtsTemplateManageDao = mcBtsTemplateManageDao;
	}

	/**
	 * ��ȡ���л�վͬ����ģ��
	 * 
	 * @return ��վģ���б�
	 */
	@Override
	public List<McBtsTemplate> queryAll() {
		return mcBtsTemplateManageDao.queryAll();
	}

	/**
	 * ����һ��ģ��ID,����һ���µĿ��õ�ģ��ID
	 * 
	 * @param referId
	 * @param temp
	 * @return
	 */
	@Override
	public synchronized Long applyNewId(Long referId, McBtsTemplate tmep) {
		Long newId = mcBtsTemplateManageDao.applyNewId(referId, tmep);
		log.debug("���뵽��IDΪ:" + newId);
		// ��Mo������ע��ģ��
		addToMoCache(newId);

		// Ǩ��referId������ģ�����ݸ�standbyId
		for (String table : getTables(referId)) {
			List<Map<String, Object>> list = mcBtsTemplateManageDao
					.queryFromTable(referId, table);
			try {
				// ����ģ������
				mcBtsTemplateManageDao.insertIntoTable(newId, table, list);
				// ����ģ�屸������
				mcBtsTemplateManageDao.insertIntoTable(newId * 10, table, list);
				log.debug("Ǩ��ģ�����ݳɹ�,��:" + table);
			} catch (Exception e) {
				log.error("Ǩ��ģ������ʧ��,��: " + table, e);
			}
		}

		return newId;
	}

	@Override
	public void generateTemplateBackup(long templateId) {

		for (String table : getTables(templateId)) {
			List<Map<String, Object>> list = mcBtsTemplateManageDao
					.queryFromTable(templateId, table);
			try {
				// ����ģ�屸������
				mcBtsTemplateManageDao.insertIntoTable(templateId * 10, table,
						list);
				log.debug("����ģ�屸�����ݳɹ�,��:" + table);
			} catch (Exception e) {
				log.error("����ģ�屸������ʧ��,��: " + table, e);
			}
		}
	}

	@Override
	public void deleteTemplateBackup(long templateId) {

		for (String table : getTables(templateId)) {
			try {
				// ɾ��ģ�屸������
				mcBtsTemplateManageDao.deleteFromTable(templateId * 10, table);
				log.debug("ɾ��ģ�屸�����ݳɹ�,��:" + table);
			} catch (Exception e) {
				log.error("ɾ��ģ�屸������ʧ��,��: " + table, e);
			}
		}
	}

	/**
	 * ͨ��moId��ѯһ��ģ��
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsTemplate queryByMoId(Long moId) {
		return mcBtsTemplateManageDao.queryByMoId(moId);
	}

	/**
	 * ��ʼ����վ����
	 * 
	 * @param mcbts
	 * @param templateId
	 */
	@Override
	public void initMcBtsData(McBts mcbts) {

		long templateId = mcbts.getTemplateId();
		if (templateId == 0L)
			templateId = McBtsTemplate.TEMPLATE_ID_V5;

		long moId = mcbts.getMoId();
		int btsType = mcbts.getBtsType();
		int antennaType = mcbts.getAntennaType();

		// if (templateId > -1000L) {
		// if (btsType == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
		// templateId = McBtsTemplate.TEMPLATE_ID_MICRO_BEEHIVE;
		// } else if (btsType == McBtsTypeDD.FDDI_MCBTS) {
		// templateId = McBtsTemplate.TEMPLATE_ID_FDDI;
		// }
		// }

		for (String table : getTables(templateId)) {
			// ��ѯ���е�ģ������
			List<Map<String, Object>> list = mcBtsTemplateManageDao
					.queryFromTable(templateId, table);

			if (table.equals("mcbts_antenna_lock")
					|| table.equals("microbts_signal_send")) {
				// �������΢���ѻ�վ�������������ű�
				if (mcbts.getBtsType() != McBtsTypeDD.MICRO_BEEHIVE_MCBTS)
					continue;
			}
			
			//����Ƶ������������Ĭ��Ƶ��ƫ����
			if (table.equals("mcbts_rfconfig") && list != null) {
				for(Map<String, Object> value : list) {
					if(value.keySet().contains("freqOffset")) {
						FreqConvertUtil freqConvertUtil = new FreqConvertUtil();
						freqConvertUtil.setFreqType(mcbts.getBtsFreqType());
						value.put("freqOffset", freqConvertUtil.getMinOffset());
						mcbts.setBtsFreq(freqConvertUtil.getMinOffset());
						break;
					}
				}
			}
			
			if (table.equals("mcbts_airlink") && list != null) {
				for(Map<String, Object> value : list) {
					if(value.keySet().contains("sequenceId")) {
						mcbts.addAttribute(McBtsAttribute.Key.SEQ_ID, value.get("sequenceId"));
						break;
					}
				}
			}

			if (templateId > -1000L) {
				// ��Ĭ��ģ�������⴦���2����,
				if (table.equals("mcbts_airlink")) {
					if (btsType == McBtsTypeDD.R3R5_MCBTS)
						list = mcBtsTemplateManageDao.queryFromTable(
								McBtsTemplate.TEMPLATE_ID_R3R5_AIRLINK, table);
				} else if (table.equals("mcbts_airlink_subw0")) {
					if (btsType != McBtsTypeDD.MICRO_BEEHIVE_MCBTS
							&& antennaType == 1) {
						list = mcBtsTemplateManageDao
								.queryFromTable(
										McBtsTemplate.TEMPLATE_ID_DEFAULT_AIRLINK_SUBW0,
										table);
					}
				}
			}

			if (list == null || list.size() == 0)
				log.error("���س�ʼ��ģ�����,����ģ�������ڱ�:" + table);

			try {
				// ��������
				mcBtsTemplateManageDao.insertIntoTable(moId, table, list);
			} catch (Exception e) {
				log.error("��ʼ����վ����ʱ��������,��: " + table, e);
			}
		}
	}

	/**
	 * ͬ��һ����վ
	 * 
	 * @param templateId
	 * @param oprs
	 * @param mcbts
	 */
	@Override
	public void syncAll(long templateId, Operation[] oprs, McBts[] mcbts)
			throws Exception {
		if (oprs == null || oprs.length == 0)
			return;
		if (mcbts == null || mcbts.length == 0)
			return;

		// �������е�ҵ��
		for (Operation opr : oprs) {
			List<String> tables = opr.tables;
			// ����ҵ�������еı�
			for (String tableName : tables) {
				// ��ѯ���е�ģ������
				List<McBts> v5Mcbts = new ArrayList<McBts>();
				List<McBts> beehiveMcbts = new ArrayList<McBts>();
				List<McBts> fddiMcbts = new ArrayList<McBts>();
				List<McBts> customMcbts = new ArrayList<McBts>();

				if (templateId > -1000L) {
					// �������л�վ,�ҳ�����Ļ�վ
					for (McBts bts : mcbts) {
						// Ϊ��վ����
						int btsType = bts.getBtsType();
						int antennaType = bts.getAntennaType();

						if (btsType == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
							beehiveMcbts.add(bts);
						} else if (btsType == McBtsTypeDD.FDDI_MCBTS) {
							if (tableName.equals("mcbts_airlink_subw0")
									&& antennaType == 1) {
								customMcbts.add(bts);
							} else {
								fddiMcbts.add(bts);
							}
						} else if (btsType == McBtsTypeDD.R3R5_MCBTS) {
							if (tableName.equals("mcbts_airlink")
									|| (tableName.equals("mcbts_airlink_subw0") && antennaType == 1)) {
								customMcbts.add(bts);
							} else {
								v5Mcbts.add(bts);
							}
						} else {
							if (tableName.equals("mcbts_airlink_subw0")
									&& antennaType == 1) {
								customMcbts.add(bts);
							} else {
								v5Mcbts.add(bts);
							}
						}
					}
				}

				try {
					if (templateId < -1000L) {
						queryAndBatchUpdate(templateId, mcbts, tableName);
					}

					if (v5Mcbts.size() > 0) {
						templateId = McBtsTemplate.TEMPLATE_ID_V5;
						queryAndBatchUpdate(templateId, v5Mcbts, tableName);
					}
					if (beehiveMcbts.size() > 0) {
						templateId = McBtsTemplate.TEMPLATE_ID_MICRO_BEEHIVE;
						queryAndBatchUpdate(templateId, beehiveMcbts, tableName);
					}
					if (fddiMcbts.size() > 0) {
						templateId = McBtsTemplate.TEMPLATE_ID_FDDI;
						queryAndBatchUpdate(templateId, fddiMcbts, tableName);
					}
					if (customMcbts.size() > 0) {
						// ��customMcBts����ֵʱ, ִ�������������
						templateId = tableName.equals("mcbts_airlink") ? McBtsTemplate.TEMPLATE_ID_R3R5_AIRLINK
								: McBtsTemplate.TEMPLATE_ID_DEFAULT_AIRLINK_SUBW0;

						queryAndBatchUpdate(templateId, customMcbts, tableName);
					}
				} catch (Exception e) {
					log.error("����ͬ����վ����ʱ��������,��: " + tableName, e);
					throw new Exception(e);
				}
			}
		}
	}

	private void queryAndBatchUpdate(long templateId, List<McBts> mcbts,
			String tableName) throws Exception {
		McBts[] mb = new McBts[mcbts.size()];
		mb = mcbts.toArray(mb);

		queryAndBatchUpdate(templateId, mb, tableName);
	}

	private void queryAndBatchUpdate(long templateId, McBts[] mcbts,
			String tableName) throws Exception {
		List<Map<String, Object>> list = mcBtsTemplateManageDao.queryFromTable(
				templateId, tableName);
		mcBtsTemplateManageDao.batchInsertIntoTable(mcbts, tableName, list);
		
		//zxz: ������·��У׼���ݽ������⴦���Ա�����µ�����ͬ�����µ���վ������
		if ("mcbts_airlink".equals(tableName)) {
			for (Map<String, Object> attributes : list) {
				if (attributes.containsKey("sequenceId")) {
					updateMcbts(true, McBtsAttribute.Key.SEQ_ID, null, attributes.get("sequenceId"), mcbts);
					break;
				}
			}
		} else if ("mcbts_rfconfig".equals(tableName)) {
			for (Map<String, Object> attributes : list) {
				if (attributes.containsKey("freqOffset")) {
					updateMcbts(false, null, "freqOffset", attributes.get("freqOffset"), mcbts);
					break;
				}
			}
		}
	}
	
	/**
	 * ����McBts������ָ��������
	 * @param isAttribute
	 * @param key
	 * @param property
	 * @param value
	 * @param mcbts
	 */
	private void updateMcbts(boolean isAttribute, McBtsAttribute.Key key, String property, Object value, McBts[] mcbtses) {
		for (McBts bts : mcbtses) {
			McBts mcbts = McBtsCache.getInstance().queryByBtsId(bts.getBtsId());
			if (isAttribute) {
				mcbts.addAttribute(key, value);
			} else {
				if ("freqOffset".equals(property)) {
					mcbts.setBtsFreq(Integer.valueOf(value.toString()));
				}
			}
		}
	}

	/**
	 * ͨ����վ���ͻ�ȡ��վ�ı�
	 * 
	 * @param btsType
	 * @return
	 */
	private Set<String> getTables(int btsType) {
		for (McBtsOperation mo : operationList) {
			if (mo.getType() == btsType) {
				return mo.getTable();
			}
		}
		return null;
	}

	private Set<String> getTables(long moId) {
		for (McBtsOperation mo : operationList) {
			// V5
			if (moId == McBtsTemplate.TEMPLATE_ID_V5
					|| (moId > -30000L && moId <= -20000)) {
				if (mo.getType() == McBtsTypeDD.V5_MCBTS) {
					return mo.getTable();
				}
			}
			// ΢����
			if (moId == McBtsTemplate.TEMPLATE_ID_MICRO_BEEHIVE
					|| (moId > -40000L && moId <= -30000)) {
				if (mo.getType() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
					return mo.getTable();
				}
			}

			// ����
			if (moId == McBtsTemplate.TEMPLATE_ID_FDDI
					|| (moId > -50000L && moId <= -40000)) {
				if (mo.getType() == McBtsTypeDD.FDDI_MCBTS) {
					return mo.getTable();
				}
			}
		}
		return null;

	}

	private List<Operation> getOperations(long moId) {
		for (McBtsOperation mo : operationList) {
			// V5
			if (moId == McBtsTemplate.TEMPLATE_ID_V5
					|| (moId > -30000L && moId <= -20000)) {
				if (mo.getType() == McBtsTypeDD.V5_MCBTS) {
					return mo.getOperations();
				}
			}
			// ΢����
			if (moId == McBtsTemplate.TEMPLATE_ID_MICRO_BEEHIVE
					|| (moId > -40000L && moId <= -30000)) {
				if (mo.getType() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
					return mo.getOperations();
				}
			}

			// ����
			if (moId == McBtsTemplate.TEMPLATE_ID_FDDI
					|| (moId > -50000L && moId <= -40000)) {
				if (mo.getType() == McBtsTypeDD.FDDI_MCBTS) {
					return mo.getOperations();
				}
			}
		}
		return null;

	}

	/**
	 * ɾ����վ�ĳ�ʼ������
	 * 
	 * @param mcbts
	 */
	@Override
	public void rollBackMcBtsData(long moId) {
		if (moId == 0L)
			return;

		Set<String> tables = new HashSet<String>();

		if (moId < 0L) {
			// ���moId<0,�ʹ�ģ�����ɾ�����ģ��
			mcBtsTemplateManageDao.delete(moId);
			tables = getTables(moId);
		} else {
			// ͨ�������е������վ������,�����վ�õ��ı�
			McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
			if (mcBts == null)
				return;

			tables = getTables(mcBts.getBtsType());
		}

		for (String table : tables) {
			try {
				mcBtsTemplateManageDao.deleteFromTable(moId, table);
				if (moId < 0L)
					mcBtsTemplateManageDao.deleteFromTable(moId * 10, table);

				if (moId < 0L)
					log.debug("ģ������ɾ���ɹ�, ��:" + table);
				else
					log.debug("��վ����ɾ���ɹ�, ��:" + table);
			} catch (Exception e) {
				if (moId < 0L)
					log.error("ɾ��ģ������ʱ��������,��: " + table, e);
				else
					log.error("ɾ����վ����ʱ��������,��: " + table, e);
			}
		}
	}

	/**
	 * ��û�վҵ��ģ��
	 * 
	 * @return
	 */
	@Override
	public List<McBtsOperation> getMcbtsOperation() {
		return operationList;
	}

	/**
	 * ��MoCache�����Mo
	 * 
	 * @param mo
	 */
	@Override
	public void addToMoCache(Long moId) {
		Mo mo = new Mo(moId, 100, "TEMPLATE", null, ManageState.OFFLINE_STATE);
		MoCache.getInstance().addOrUpdate(mo);
	}

	/**
	 * ��MoCache��ɾ��Mo
	 * 
	 * @param moId
	 */
	@Override
	public void removeFromMoCache(Long moId) {
		MoCache.getInstance().delete(moId);
	}

	@Override
	public void updateTemplate(McBtsTemplate template) {
		mcBtsTemplateManageDao.update(template);
	}

	@Override
	public void recover(Long moId, List<String> operations, boolean isDel) {

		McBtsTemplate template = mcBtsTemplateManageDao.queryByMoId(moId);

		if (operations == null || operations.size() == 0)
			return;

		// ��ȡ��ǰ���ͻ�վ����ҵ��
		List<Operation> allOper = getOperations(moId);
		List<String> tableList = new ArrayList<String>();
		// ��ȡ���Ƴ�ҵ������ݿ��
		for (String operation : operations) {
			for (Operation oper : allOper) {
				if (oper.getName().equals(operation)) {
					tableList.addAll(oper.tables);
				}
			}
		}
		for (String table : tableList) {
			try {
				mcBtsTemplateManageDao.deleteFromTable(moId, table);

				// ������Ƴ�ҵ�������ݻ�ԭΪ�ο�ģ�������
				if (isDel) {
					List<Map<String, Object>> list = mcBtsTemplateManageDao
							.queryFromTable(template.getReferredTemplateId(),
									table);
					mcBtsTemplateManageDao.insertIntoTable(moId, table, list);
				} else {
					// ������޸ĵ�ҵ�������ݻ�ԭΪ���ݵ�����
					List<Map<String, Object>> list = mcBtsTemplateManageDao
							.queryFromTable(moId * 10, table);
					mcBtsTemplateManageDao.insertIntoTable(moId, table, list);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}

	}

	private static void parserMcBtsTemplate() {
		Digester digester = new Digester();
		digester.push(operationList);

		digester.setValidating(false);

		digester.addObjectCreate("template/operations", McBtsOperation.class);
		digester.addSetProperties("template/operations");

		digester.addCallMethod("template/operations", "setOperationsParam", 2);
		digester.addCallParam("template/operations", 0, "type");
		digester.addCallParam("template/operations", 1, "remark");

		digester.addCallMethod("template/operations/operation",
				"setOperationParam", 2);
		digester.addCallParam("template/operations/operation", 0, "name");
		digester.addCallParam("template/operations/operation", 1, "desc");

		digester.addCallMethod("template/operations/operation/table",
				"addTableName", 1);
		digester.addCallParam("template/operations/operation/table", 0, "name");

		digester.addSetNext("template/operations", "add");

		try {
			digester.parse(new FileInputStream(FILE));
		} catch (Exception e) {
			log.error("", e);
		}

	}
}
