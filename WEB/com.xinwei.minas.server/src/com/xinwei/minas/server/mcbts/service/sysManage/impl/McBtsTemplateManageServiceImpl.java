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
 * 基站模板管理的服务
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
		// 转换template-table.xml的内容为对象
		parserMcBtsTemplate();

	}

	private McBtsTemplateManageDao mcBtsTemplateManageDao;

	public void setMcBtsTemplateManageDao(
			McBtsTemplateManageDao mcBtsTemplateManageDao) {
		this.mcBtsTemplateManageDao = mcBtsTemplateManageDao;
	}

	/**
	 * 获取所有基站同步的模板
	 * 
	 * @return 基站模板列表
	 */
	@Override
	public List<McBtsTemplate> queryAll() {
		return mcBtsTemplateManageDao.queryAll();
	}

	/**
	 * 基于一个模板ID,返回一个新的可用的模板ID
	 * 
	 * @param referId
	 * @param temp
	 * @return
	 */
	@Override
	public synchronized Long applyNewId(Long referId, McBtsTemplate tmep) {
		Long newId = mcBtsTemplateManageDao.applyNewId(referId, tmep);
		log.debug("申请到的ID为:" + newId);
		// 在Mo缓存中注册模板
		addToMoCache(newId);

		// 迁移referId的所有模板数据给standbyId
		for (String table : getTables(referId)) {
			List<Map<String, Object>> list = mcBtsTemplateManageDao
					.queryFromTable(referId, table);
			try {
				// 插入模板数据
				mcBtsTemplateManageDao.insertIntoTable(newId, table, list);
				// 插入模板备份数据
				mcBtsTemplateManageDao.insertIntoTable(newId * 10, table, list);
				log.debug("迁移模板数据成功,表:" + table);
			} catch (Exception e) {
				log.error("迁移模板数据失败,表: " + table, e);
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
				// 插入模板备份数据
				mcBtsTemplateManageDao.insertIntoTable(templateId * 10, table,
						list);
				log.debug("创建模板备份数据成功,表:" + table);
			} catch (Exception e) {
				log.error("创建模板备份数据失败,表: " + table, e);
			}
		}
	}

	@Override
	public void deleteTemplateBackup(long templateId) {

		for (String table : getTables(templateId)) {
			try {
				// 删除模板备份数据
				mcBtsTemplateManageDao.deleteFromTable(templateId * 10, table);
				log.debug("删除模板备份数据成功,表:" + table);
			} catch (Exception e) {
				log.error("删除模板备份数据失败,表: " + table, e);
			}
		}
	}

	/**
	 * 通过moId查询一个模板
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsTemplate queryByMoId(Long moId) {
		return mcBtsTemplateManageDao.queryByMoId(moId);
	}

	/**
	 * 初始化基站数据
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
			// 查询表中的模板数据
			List<Map<String, Object>> list = mcBtsTemplateManageDao
					.queryFromTable(templateId, table);

			if (table.equals("mcbts_antenna_lock")
					|| table.equals("microbts_signal_send")) {
				// 如果不是微蜂窝基站，则跳过这两张表
				if (mcbts.getBtsType() != McBtsTypeDD.MICRO_BEEHIVE_MCBTS)
					continue;
			}
			
			//根据频点类型来设置默认频点偏移量
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
				// 在默认模板中特殊处理的2个表,
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
				log.error("加载初始化模板错误,请检查模板数据在表:" + table);

			try {
				// 插入数据
				mcBtsTemplateManageDao.insertIntoTable(moId, table, list);
			} catch (Exception e) {
				log.error("初始化基站数据时发生错误,表: " + table, e);
			}
		}
	}

	/**
	 * 同步一批基站
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

		// 遍历所有的业务
		for (Operation opr : oprs) {
			List<String> tables = opr.tables;
			// 遍历业务中所有的表
			for (String tableName : tables) {
				// 查询表中的模板数据
				List<McBts> v5Mcbts = new ArrayList<McBts>();
				List<McBts> beehiveMcbts = new ArrayList<McBts>();
				List<McBts> fddiMcbts = new ArrayList<McBts>();
				List<McBts> customMcbts = new ArrayList<McBts>();

				if (templateId > -1000L) {
					// 遍历所有基站,找出特殊的基站
					for (McBts bts : mcbts) {
						// 为基站分类
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
						// 当customMcBts中有值时, 执行特殊表插入操作
						templateId = tableName.equals("mcbts_airlink") ? McBtsTemplate.TEMPLATE_ID_R3R5_AIRLINK
								: McBtsTemplate.TEMPLATE_ID_DEFAULT_AIRLINK_SUBW0;

						queryAndBatchUpdate(templateId, customMcbts, tableName);
					}
				} catch (Exception e) {
					log.error("批量同步基站数据时发生错误,表: " + tableName, e);
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
		
		//zxz: 空中链路和校准数据进行特殊处理，以便把最新的数据同步更新到基站缓存中
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
	 * 更新McBts缓存中指定的属性
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
	 * 通过基站类型获取基站的表
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
			// 微蜂窝
			if (moId == McBtsTemplate.TEMPLATE_ID_MICRO_BEEHIVE
					|| (moId > -40000L && moId <= -30000)) {
				if (mo.getType() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
					return mo.getTable();
				}
			}

			// 光纤
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
			// 微蜂窝
			if (moId == McBtsTemplate.TEMPLATE_ID_MICRO_BEEHIVE
					|| (moId > -40000L && moId <= -30000)) {
				if (mo.getType() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
					return mo.getOperations();
				}
			}

			// 光纤
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
	 * 删除基站的初始化数据
	 * 
	 * @param mcbts
	 */
	@Override
	public void rollBackMcBtsData(long moId) {
		if (moId == 0L)
			return;

		Set<String> tables = new HashSet<String>();

		if (moId < 0L) {
			// 如果moId<0,就从模板表中删除这个模板
			mcBtsTemplateManageDao.delete(moId);
			tables = getTables(moId);
		} else {
			// 通过缓存中的这个基站的类型,查出基站用到的表
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
					log.debug("模板数据删除成功, 表:" + table);
				else
					log.debug("基站数据删除成功, 表:" + table);
			} catch (Exception e) {
				if (moId < 0L)
					log.error("删除模板数据时发生错误,表: " + table, e);
				else
					log.error("删除基站数据时发生错误,表: " + table, e);
			}
		}
	}

	/**
	 * 获得基站业务模型
	 * 
	 * @return
	 */
	@Override
	public List<McBtsOperation> getMcbtsOperation() {
		return operationList;
	}

	/**
	 * 向MoCache中添加Mo
	 * 
	 * @param mo
	 */
	@Override
	public void addToMoCache(Long moId) {
		Mo mo = new Mo(moId, 100, "TEMPLATE", null, ManageState.OFFLINE_STATE);
		MoCache.getInstance().addOrUpdate(mo);
	}

	/**
	 * 从MoCache中删除Mo
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

		// 获取当前类型基站所有业务
		List<Operation> allOper = getOperations(moId);
		List<String> tableList = new ArrayList<String>();
		// 获取被移除业务的数据库表
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

				// 如果是移除业务，则将数据还原为参考模板的数据
				if (isDel) {
					List<Map<String, Object>> list = mcBtsTemplateManageDao
							.queryFromTable(template.getReferredTemplateId(),
									table);
					mcBtsTemplateManageDao.insertIntoTable(moId, table, list);
				} else {
					// 如果是修改的业务，则将数据还原为备份的数据
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
