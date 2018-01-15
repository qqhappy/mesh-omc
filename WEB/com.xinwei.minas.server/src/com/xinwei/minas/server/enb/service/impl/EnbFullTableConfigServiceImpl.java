/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;
import EDU.oswego.cs.dl.util.concurrent.TimeoutException;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.FullTableConfigInfo;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.dao.EnbBizConfigDAO;
import com.xinwei.minas.server.enb.dao.EnbBizTemplateDAO;
import com.xinwei.minas.server.enb.dao.EnbFullTableConfigDAO;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.proxy.EnbBizConfigProxy;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbFullTableConfigService;
import com.xinwei.minas.server.enb.task.EnbFullTableTaskManager;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.core.model.meta.XMetaRef;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB整表配置服务类
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author zhuxiaozhan
 * 
 */

public class EnbFullTableConfigServiceImpl implements EnbFullTableConfigService {

	private Log log = LogFactory.getLog(EnbFullTableConfigServiceImpl.class);

	private EnbBizConfigDAO enbBizConfigDAO;

	private EnbBizConfigProxy enbBizConfigProxy;

	private EnbFullTableConfigDAO enbFullTableConfigDAO;

	private EnbBizTemplateDAO enbBizTemplateDAO;

	private SequenceService sequenceService;

	public void setEnbBizConfigDAO(EnbBizConfigDAO enbBizConfigDAO) {
		this.enbBizConfigDAO = enbBizConfigDAO;
	}

	public void setEnbBizConfigProxy(EnbBizConfigProxy enbBizConfigProxy) {
		this.enbBizConfigProxy = enbBizConfigProxy;
	}

	public void setEnbFullTableConfigDAO(
			EnbFullTableConfigDAO enbFullTableConfigDAO) {
		this.enbFullTableConfigDAO = enbFullTableConfigDAO;
	}

	public void setEnbBizTemplateDAO(EnbBizTemplateDAO enbBizTemplateDAO) {
		this.enbBizTemplateDAO = enbBizTemplateDAO;
	}

	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	@Override
	public void config(Long moId) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		if (!enb.isConfigurable()) {
			throw new Exception(OmpAppContext.getMessage("enb_cannot_config"));
		}

		if (enb.setFullTableOperation(true)) {// 判断是否可配置，并将该网元锁住，不在进行其它操作
			enb.setBizName(EnbConstantUtils.FULL_TABLE_CONFIG);
			FullTableConfigInfo info = enbFullTableConfigDAO.queryByMoId(moId);
			if (info == null) {
				info = new FullTableConfigInfo();
				info.setIdx(sequenceService.getNext());
				info.setMoId(moId);
			}
			info.setStartConfigTime(new Date());
			try {

				// 获取文件名称
				String fileName = generateFullTableSqlFile(enb.getMoId(),
						enb.getProtocolVersion());

				GenericBizData gbData = new GenericBizData("");
				gbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.FTP_IP, EnbFullTableTaskManager
								.getInstance().getFtpServerIp()));
				gbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.FTP_PORT, EnbFullTableTaskManager
								.getInstance().getFtpPort()));
				gbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.FTP_USER_NAME, EnbFullTableTaskManager
								.getInstance().getFtpUsername()));
				gbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.FTP_PASSWORD, EnbFullTableTaskManager
								.getInstance().getFtpPassword()));
				gbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.FILE_DIRECTORY,
						EnbFullTableTaskManager.getInstance()
								.getConfigFtpdDrectory()));
				gbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.FILE_NAME, fileName));

				// 发送配置消息
				enbBizConfigProxy.fullTableConfig(enb.getEnbId(), gbData, 0);

				// 加入到任务缓存里面
				FutureResult configRuesult = new FutureResult();
				EnbFullTableTaskManager.getInstance().addFullTableConfigTask(
						enb.getEnbId(), configRuesult);
				// 同步等待响应结果
				FullTableConfigInfo result = (FullTableConfigInfo) configRuesult
						.timedGet(EnbFullTableTaskManager.getInstance()
								.getOverTime());
				// 接收的上报通知提示错误
				if (result.getConfigStatus() == FullTableConfigInfo.CONFIG_FAIL) {
					throw new Exception(result.getErrorMessage());
				} else {
					// 接收的上报通知提示成功
					info.setConfigStatus(result.getConfigStatus());
					info.setErrorMessage(result.getErrorMessage());
					enbFullTableConfigDAO.saveOrUpdate(info);
				}

			} catch (TimeoutException e) {
				log.error(e.getMessage());
				info.setConfigStatus(FullTableConfigInfo.CONFIG_FAIL);
				info.setErrorMessage("enb_response_timeout");
				enbFullTableConfigDAO.saveOrUpdate(info);
				throw new Exception(
						OmpAppContext.getMessage("enb_response_timeout"));
			} catch (Exception e) {
				log.error(e.getLocalizedMessage());
				// 更新任务状态为配置失败
				info.setConfigStatus(FullTableConfigInfo.CONFIG_FAIL);
				info.setErrorMessage(e instanceof java.util.concurrent.TimeoutException ? OmpAppContext
						.getMessage("enb_response_timeout") : e
						.getLocalizedMessage());
				try {
					enbFullTableConfigDAO.saveOrUpdate(info);
				} catch (Exception e2) {
					log.error("record full table config error message failed. "
							+ e2.getLocalizedMessage());
				}
				if (e instanceof java.util.concurrent.TimeoutException) {
					throw new Exception(
							OmpAppContext.getMessage("enb_response_timeout"));
				}
				throw new Exception(e.getLocalizedMessage());
			} finally {
				// 更改为可配置状态
				enb.setBizName("");
				enb.setFullTableOperation(false);
				// 移除任务
				EnbFullTableTaskManager.getInstance()
						.removeFullTableConfigTask(enb.getEnbId());
			}

		} else {
			throw new Exception(
					OmpAppContext.getMessage("enb_unconfig_alert",
							new Object[] { OmpAppContext.getMessage(enb
									.getBizName()) }));
		}
	}

	@Override
	public void delete(FullTableConfigInfo data) throws Exception {
		// 如果该业务处在正在配置的状态，则不允许删除
		if (data.getConfigStatus() == FullTableConfigInfo.CONFIGING) {
			throw new Exception(
					OmpAppContext.getMessage("full_table_configuring"));
		}
		enbFullTableConfigDAO.delete(data);
	}

	@Override
	public FullTableConfigInfo queryByMoId(Long moId) throws Exception {
		return enbFullTableConfigDAO.queryByMoId(moId);
	}

	@Override
	public List<FullTableConfigInfo> queryAll() throws Exception {
		return enbFullTableConfigDAO.queryAll();
	}

	@Override
	public List<FullTableConfigInfo> queryByStatus(int status) throws Exception {
		return enbFullTableConfigDAO.queryByStatus(status);
	}

	@Override
	public void saveUpdate(FullTableConfigInfo data) throws Exception {
		enbFullTableConfigDAO.saveOrUpdate(data);
	}

	@Override
	public String generateFullTableSqlFile(Long moId,
			String targetProtocolVersion) throws Exception {

		Enb enb = EnbCache.getInstance().queryByMoId(moId);

		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}

		// 生成整表配置的sql语句
		Map<String, List<String>> fullTableSql = generateFullTableSql(moId,
				targetProtocolVersion);
		StringBuilder data = new StringBuilder();
		for (List<String> sqls : fullTableSql.values()) {
			for (String sql : sqls) {
				data.append(sql + "\n");
			}
		}
		// 去掉最后的换行符
		if (data.length() > 0) {
			data.deleteCharAt(data.length() - 1);
		}
		String fileName = EnbFullTableTaskManager.getInstance().createFileName(
				enb.getHexEnbId());
		// 上传到ftp里面
		EnbFullTableTaskManager.getInstance()
				.upFiletoFtp(
						EnbFullTableTaskManager.getInstance()
								.getConfigFtpdDrectory(),
						EnbFullTableTaskManager.getInstance()
								.getConfigLocalDirectory(), fileName,
						data.toString(), enb.getHexEnbId());
		return fileName;
	}

	@Override
	public Map<String, List<String>> generateFullTableSql(Long moId,
			String targetProtocolVersion) throws Exception {

		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}

		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		// 获取拓扑反向排序的表列表
		List<String> tableNames = EnbBizHelper.getReverseTopoTableNames(
				enb.getEnbType(), targetProtocolVersion);

		// 动态表不进行整表配置
		List<String> dynamicTables = EnbBizHelper.getDynamicTables(
				enb.getEnbType(), targetProtocolVersion);
		tableNames.removeAll(dynamicTables);

		if (tableNames != null) {
			for (String tableName : tableNames) {
				List<String> sqls = this.generateFullTableSql(moId,
						targetProtocolVersion, tableName);
				List<String> sqlList = map.get(tableName);
				if (sqlList == null) {
					sqlList = new LinkedList<String>();
					map.put(tableName, sqlList);
				}
				sqlList.addAll(sqls);

				if (targetProtocolVersion.equals(enb.getProtocolVersion()))
					continue;
				// 如果当前版本无此表，则直接跳过
				if (!EnbBizHelper.hasBizMeta(moId, tableName))
					continue;
				// 如果目标版本无此表，则直接跳过
				if (!EnbBizHelper.hasBizMeta(enb.getEnbType(),
						targetProtocolVersion, tableName))
					continue;
				// 如果目标版本与当前基站版本不一致
				// 获取目标版本比当前版本添加的字段
				List<XList> newFieldMetas = EnbBizHelper.getNewFields(
						enb.getEnbType(), enb.getProtocolVersion(),
						targetProtocolVersion, tableName);
				// 如果目标版本中新增了字段
				if (newFieldMetas != null && !newFieldMetas.isEmpty()) {
					for (XList fieldMeta : newFieldMetas) {
						// 如果新增字段是外键
						if (fieldMeta.containsRef()) {
							String sql = generateDefaultRefRecordSql(moId,
									targetProtocolVersion, fieldMeta);
							if (sql == "")
								continue;
							XMetaRef metaRef = fieldMeta.getFieldRefs().get(0);
							String refTable = metaRef.getRefTable();
							// 将sql加入到总结果中
							List<String> refTableSqls = map.get(refTable);
							if (refTableSqls == null) {
								refTableSqls = new LinkedList<String>();
								map.put(refTable, refTableSqls);
							}
							refTableSqls.add(sql);
						}
					}
				}
			}
		}
		return map;
	}

	/**
	 * 根据外键字段生成默认引用记录的sql
	 * 
	 * @param moId
	 * @param targetProtocolVersion
	 * @param refTable
	 * @param keyField
	 * @return
	 * @throws Exception
	 */
	private String generateDefaultRefRecordSql(long moId,
			String targetProtocolVersion, XList fieldMeta) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		String sql = "";
		XMetaRef metaRef = fieldMeta.getFieldRefs().get(0);
		String refTable = metaRef.getRefTable();
		// 外键字段
		XBizField keyField = new XBizField(metaRef.getKeyColumn(),
				fieldMeta.getPropertyValue(XList.P_DEFAULT));
		// 如果库中引用表中不存在外键对应的记录，则需要创建引用记录
		if (!isRecordExists(moId, refTable, keyField)) {
			try {
				// 创建默认引用记录
				XBizRecord defaultRefRecord = createDefaultRefRecord(
						enb.getEnbType(), targetProtocolVersion, refTable,
						keyField);
				sql = enbBizConfigProxy.generateInsertSql(enb.getEnbType(),
						targetProtocolVersion, refTable, defaultRefRecord);
			} catch (Exception e) {
				log.error(
						"generate default reference record sql failed. refFieldName="
								+ fieldMeta.getName(), e);
			}
		}

		return sql;
	}

	/**
	 * 指定记录是否存在
	 * 
	 * @param moId
	 * @param tableName
	 * @param keyName
	 * @param keyValue
	 * @return
	 * @throws Exception
	 */
	private boolean isRecordExists(long moId, String tableName,
			XBizField keyField) throws Exception {
		XBizRecord bizKey = new XBizRecord();
		bizKey.addField(keyField);
		// 查找引用表中是否存在外键值对应的记录
		XBizRecord bizRecord = enbBizConfigDAO.queryByKey(moId, tableName,
				bizKey);
		return bizRecord != null;

	}

	/**
	 * 创建添加默认的引用记录
	 * 
	 * @param refBizField
	 * @throws Exception
	 */
	private XBizRecord createDefaultRefRecord(int enbTypeId,
			String protocolVersion, String tableName, XBizField keyField)
			throws Exception {

		// 查询目标版本引用表的模板数据中是否存在外键值对应的模板记录
		XBizTable bizTable = enbBizTemplateDAO.queryTemplateData(enbTypeId,
				protocolVersion, tableName);
		String keyName = keyField.getName();
		String keyValue = keyField.getValue();

		XBizRecord retRecord = null;
		// 如果存在，则返回该条模板记录
		if (EnbBizHelper.hasRecord(bizTable)) {
			for (XBizRecord bizRecord : bizTable.getRecords()) {
				String value = bizRecord.getFieldBy(keyName).getValue();
				if (keyValue.equals(value)) {
					retRecord = bizRecord;
					break;
				}
			}
		}
		// else {
		// // 如果无模板记录，则根据默认值创建记录
		// retRecord = EnbBizHelper.convertDataByVersion(enbTypeId,
		// protocolVersion,
		// tableName, null);
		// }
		return retRecord;
	}

	@Override
	public List<String> generateFullTableSql(Long moId, String targetVersion,
			String tableName) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		List<String> sqls = new LinkedList<String>();

		List<XBizRecord> records = null;
		// 如果原版本无此表，无需查此表，但需要生成默认数据
		boolean hasTable = EnbBizHelper.hasBizMeta(moId, tableName);
		if (!hasTable) {
			// 如果是小区相关的表，需要按照小区生成数据
			if (EnbBizHelper.isCellRelatedTable(enb.getEnbType(),
					targetVersion, tableName)) {
				XBizTable cellTable = enbBizConfigDAO.query(moId,
						EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
				records = EnbBizHelper.createCellRelatedRecords(
						enb.getEnbType(), targetVersion, tableName, cellTable);
			} else {
				// 不是小区关联表，需要查出模板数据
				XBizTable bizTable = enbBizTemplateDAO.queryTemplateData(
						enb.getEnbType(), targetVersion, tableName);
				records = bizTable.getRecords();
			}
		} else {
			// 如果原版本有此表，需要从库中查出数据
			XBizTable bizTable = enbBizConfigDAO.query(moId, tableName, null);
			records = bizTable.getRecords();
		}

		// 如果原版本和目标版本都有此表，则需进行数据格式转换
		boolean needConvert = hasTable;
		if (records != null) {
			for (XBizRecord bizRecord : records) {
				// 如果是以太网参数表,记录旧数据
				XBizRecord oldRecord = new XBizRecord();
				if(tableName.equals(EnbConstantUtils.TABLE_NAME_T_ETHPARA)) {
					Set<String> keySet = bizRecord.getFieldMap().keySet();
					for (String key : keySet) {
						oldRecord.addField(bizRecord.getFieldBy(key));
					}
				}
				
				XBizRecord newRecord = bizRecord;
				if (needConvert) {
					// 根据目标版本数据格式进行转换
					newRecord = EnbBizHelper.convertDataByVersion(
							enb.getEnbType(), targetVersion, tableName,
							bizRecord);
				}
				specialProcess(enb, targetVersion, tableName, newRecord,oldRecord);
				// 字段升级时修改了字段类型、或者取值范围时的处理
				sameFieldUpdate(enb,targetVersion,tableName,newRecord,bizRecord);
				
				String sql = enbBizConfigProxy.generateInsertSql(
						enb.getEnbType(), targetVersion, tableName, newRecord);

				sqls.add(sql);
			}
		}
		return sqls;
	}
	
	/**
	 * 处理原版本和目标版本中,相同字段修改了字段类型或者取值范围的情况(只支持Unsigned32和Integer之间的类型修改)
	 * @param enb
	 * @param targetVersion
	 * @param tableName
	 * @param newRecord
	 */
	public void sameFieldUpdate(Enb enb, String targetVersion,
			String tableName, XBizRecord newRecord, XBizRecord oldRecord)
			throws Exception {
		
		// 获取表在目标版本表的字段信息
		XList tableList = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				targetVersion, tableName);
		XList[] fieldlists = tableList.getFieldMetaList();
		for (XList fieldlist : fieldlists) {
			if(fieldlist.isRef()) {
				continue;
			}
			// 判断新版本的字段旧版本是否存在
			String fieldName = fieldlist.getName();
 			XBizField oldField = oldRecord.getFieldBy(fieldName);
			if (oldField == null) {
				// 如果不存在,则处理下一个字段
				continue;
			} else {
				// 如果是数字类型
				if(fieldlist.isUnsignedNum()) {
					int min = 0;
					int max = 0;
					// 如果是enum
					if (fieldlist.isEnum()) {
						// 获取字段范围
						int[] enumRange = fieldlist.getEnumRange();
						if(null == enumRange || enumRange.length <= 0) {
							continue;
						}
						min = enumRange[0];
						max = enumRange[enumRange.length - 1];
					} 
					// 如果是unsigned32
					else if(fieldlist.isUnsigned32()) {
						int[] rangeText = fieldlist.getRange();
						min = rangeText[0];
						max = rangeText[1];
					}
					// 判断已经转换好的数据是否在范围内
					long fieldValue = newRecord.getLongValue(fieldName);
					if(fieldValue >= min && fieldValue <= max) {
						continue;
					} 
					String defaultValue = fieldlist.getDefault();
					// 如果不在范围内,切存在默认值,则替换为默认值
					if(null != defaultValue && !defaultValue.equals("")) {
						// 为了防止默认值不是数字类型,先转换成数字再转换回字符串,有问题则会抛异常
						newRecord
								.addField(new XBizField(fieldName, String
										.valueOf(Integer.valueOf(defaultValue
												.trim()))));
					} 
					// 如果默认值为空,则用最小值替换
					else {
						newRecord.addField(new XBizField(fieldName, String
								.valueOf(min)));
					}
				}
			}
		}
	}

	public void specialProcess(Enb enb, String targetVersion,
			String tableName, XBizRecord newRecord ,XBizRecord oldRecord) throws Exception {
		// 告警参数表要特殊处理
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ALARM_PARA)) {
			// 特殊处理3.0.5以下版本升级到3.0.5及以上版本
			if (enb.getProtocolVersion().compareTo("3.0.5") < 0
					&& targetVersion.compareTo("3.0.5") >= 0) {
				// 判断当前版本是否存在告警参数表,不存在则不处理
				if (!EnbBizHelper.hasBizMeta(enb.getEnbType(), targetVersion,
						tableName)) {
					return;
				}
				if (0 != newRecord.getIntValue("u32DetIntervalTime")) {
					return;
				}
				XBizTable templateData = enbBizTemplateDAO.queryTemplateData(
						enb.getEnbType(), targetVersion, tableName);
				List<XBizRecord> templateRecords = templateData.getRecords();
				boolean flag = false;
				for (XBizRecord templateRecord : templateRecords) {
					if (newRecord.getIntValue("u8Indx") == templateRecord
							.getIntValue("u8Indx")) {
						newRecord.addField(new XBizField("u32DetIntervalTime",
								templateRecord
										.getStringValue("u32DetIntervalTime")));
						flag = true;
					}
				}
				if (!flag) {
					newRecord
							.addField(new XBizField("u32DetIntervalTime", "1"));
				}
			}
		}
		// 特殊处理T_ETHPARA(以太网参数表)
		else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ETHPARA)) {
			// 特殊处理3.0.1及其以下版本升级到3.0.2及其以上版本
			if (enb.getProtocolVersion().compareTo("3.0.1") <= 0
					&& targetVersion.compareTo("3.0.2") >= 0) {
				// 判断当前版本是否存在T_ETHPARA表,不存在则不处理
				if (!EnbBizHelper.hasBizMeta(enb.getEnbType(), targetVersion,
						tableName)) {
					return;
				}
				if (oldRecord
						.getIntValue(EnbConstantUtils.FIELD_NAME_PORT_ID) == newRecord
						.getIntValue(EnbConstantUtils.FIELD_NAME_PORT_ID)) {
					// 做数据兼容转换
					EnbBizHelper.ethParaDataConvertor(newRecord,oldRecord);
				}
			}
		}
	}

	public static void main(String[] args) {
		String enumText = "(0)rf2|(1)rf4|(2)rf8|(3)rf16|(4)rf32|(5)rf64|(6)rf128|(7)rf256|(8)rf512|(9)rf1024|(10)rf4|(11)rf8|(12)rf16|(124)rf32|(3245)rf64|(4234236)rf128|(7112)rf256|(832)rf512|(91)rf1024";
		String[] enumArray = enumText.split("\\|");
		int[] enumRange = new int[enumArray.length];
		for (int i = 0; i < enumRange.length; i++) {
			char[] charArray = enumArray[i].trim().toCharArray();
			StringBuilder sb = new StringBuilder();
			boolean start = false;
			for (int j = 0; j < charArray.length; j++) {
				if('(' == charArray[j]) {
					start = true;
					continue;
				}
				if(')' == charArray[j]) {
					break;
				}
				if(start) {
					sb.append(charArray[j]);
				}
			}
			enumRange[i] = Integer.valueOf(sb.toString());
			System.out.println(enumRange[i]);
		}
		
	}
	
}
