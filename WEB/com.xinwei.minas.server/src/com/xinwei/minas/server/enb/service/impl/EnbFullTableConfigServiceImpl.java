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
 * eNB�������÷�����
 * 
 * <p>
 * ����ϸ����
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

		if (enb.setFullTableOperation(true)) {// �ж��Ƿ�����ã���������Ԫ��ס�����ڽ�����������
			enb.setBizName(EnbConstantUtils.FULL_TABLE_CONFIG);
			FullTableConfigInfo info = enbFullTableConfigDAO.queryByMoId(moId);
			if (info == null) {
				info = new FullTableConfigInfo();
				info.setIdx(sequenceService.getNext());
				info.setMoId(moId);
			}
			info.setStartConfigTime(new Date());
			try {

				// ��ȡ�ļ�����
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

				// ����������Ϣ
				enbBizConfigProxy.fullTableConfig(enb.getEnbId(), gbData, 0);

				// ���뵽���񻺴�����
				FutureResult configRuesult = new FutureResult();
				EnbFullTableTaskManager.getInstance().addFullTableConfigTask(
						enb.getEnbId(), configRuesult);
				// ͬ���ȴ���Ӧ���
				FullTableConfigInfo result = (FullTableConfigInfo) configRuesult
						.timedGet(EnbFullTableTaskManager.getInstance()
								.getOverTime());
				// ���յ��ϱ�֪ͨ��ʾ����
				if (result.getConfigStatus() == FullTableConfigInfo.CONFIG_FAIL) {
					throw new Exception(result.getErrorMessage());
				} else {
					// ���յ��ϱ�֪ͨ��ʾ�ɹ�
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
				// ��������״̬Ϊ����ʧ��
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
				// ����Ϊ������״̬
				enb.setBizName("");
				enb.setFullTableOperation(false);
				// �Ƴ�����
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
		// �����ҵ�����������õ�״̬��������ɾ��
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

		// �����������õ�sql���
		Map<String, List<String>> fullTableSql = generateFullTableSql(moId,
				targetProtocolVersion);
		StringBuilder data = new StringBuilder();
		for (List<String> sqls : fullTableSql.values()) {
			for (String sql : sqls) {
				data.append(sql + "\n");
			}
		}
		// ȥ�����Ļ��з�
		if (data.length() > 0) {
			data.deleteCharAt(data.length() - 1);
		}
		String fileName = EnbFullTableTaskManager.getInstance().createFileName(
				enb.getHexEnbId());
		// �ϴ���ftp����
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
		// ��ȡ���˷�������ı��б�
		List<String> tableNames = EnbBizHelper.getReverseTopoTableNames(
				enb.getEnbType(), targetProtocolVersion);

		// ��̬��������������
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
				// �����ǰ�汾�޴˱���ֱ������
				if (!EnbBizHelper.hasBizMeta(moId, tableName))
					continue;
				// ���Ŀ��汾�޴˱���ֱ������
				if (!EnbBizHelper.hasBizMeta(enb.getEnbType(),
						targetProtocolVersion, tableName))
					continue;
				// ���Ŀ��汾�뵱ǰ��վ�汾��һ��
				// ��ȡĿ��汾�ȵ�ǰ�汾��ӵ��ֶ�
				List<XList> newFieldMetas = EnbBizHelper.getNewFields(
						enb.getEnbType(), enb.getProtocolVersion(),
						targetProtocolVersion, tableName);
				// ���Ŀ��汾���������ֶ�
				if (newFieldMetas != null && !newFieldMetas.isEmpty()) {
					for (XList fieldMeta : newFieldMetas) {
						// ��������ֶ������
						if (fieldMeta.containsRef()) {
							String sql = generateDefaultRefRecordSql(moId,
									targetProtocolVersion, fieldMeta);
							if (sql == "")
								continue;
							XMetaRef metaRef = fieldMeta.getFieldRefs().get(0);
							String refTable = metaRef.getRefTable();
							// ��sql���뵽�ܽ����
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
	 * ��������ֶ�����Ĭ�����ü�¼��sql
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
		// ����ֶ�
		XBizField keyField = new XBizField(metaRef.getKeyColumn(),
				fieldMeta.getPropertyValue(XList.P_DEFAULT));
		// ����������ñ��в����������Ӧ�ļ�¼������Ҫ�������ü�¼
		if (!isRecordExists(moId, refTable, keyField)) {
			try {
				// ����Ĭ�����ü�¼
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
	 * ָ����¼�Ƿ����
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
		// �������ñ����Ƿ�������ֵ��Ӧ�ļ�¼
		XBizRecord bizRecord = enbBizConfigDAO.queryByKey(moId, tableName,
				bizKey);
		return bizRecord != null;

	}

	/**
	 * �������Ĭ�ϵ����ü�¼
	 * 
	 * @param refBizField
	 * @throws Exception
	 */
	private XBizRecord createDefaultRefRecord(int enbTypeId,
			String protocolVersion, String tableName, XBizField keyField)
			throws Exception {

		// ��ѯĿ��汾���ñ��ģ���������Ƿ�������ֵ��Ӧ��ģ���¼
		XBizTable bizTable = enbBizTemplateDAO.queryTemplateData(enbTypeId,
				protocolVersion, tableName);
		String keyName = keyField.getName();
		String keyValue = keyField.getValue();

		XBizRecord retRecord = null;
		// ������ڣ��򷵻ظ���ģ���¼
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
		// // �����ģ���¼�������Ĭ��ֵ������¼
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
		// ���ԭ�汾�޴˱������˱�����Ҫ����Ĭ������
		boolean hasTable = EnbBizHelper.hasBizMeta(moId, tableName);
		if (!hasTable) {
			// �����С����صı���Ҫ����С����������
			if (EnbBizHelper.isCellRelatedTable(enb.getEnbType(),
					targetVersion, tableName)) {
				XBizTable cellTable = enbBizConfigDAO.query(moId,
						EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
				records = EnbBizHelper.createCellRelatedRecords(
						enb.getEnbType(), targetVersion, tableName, cellTable);
			} else {
				// ����С����������Ҫ���ģ������
				XBizTable bizTable = enbBizTemplateDAO.queryTemplateData(
						enb.getEnbType(), targetVersion, tableName);
				records = bizTable.getRecords();
			}
		} else {
			// ���ԭ�汾�д˱���Ҫ�ӿ��в������
			XBizTable bizTable = enbBizConfigDAO.query(moId, tableName, null);
			records = bizTable.getRecords();
		}

		// ���ԭ�汾��Ŀ��汾���д˱�����������ݸ�ʽת��
		boolean needConvert = hasTable;
		if (records != null) {
			for (XBizRecord bizRecord : records) {
				// �������̫��������,��¼������
				XBizRecord oldRecord = new XBizRecord();
				if(tableName.equals(EnbConstantUtils.TABLE_NAME_T_ETHPARA)) {
					Set<String> keySet = bizRecord.getFieldMap().keySet();
					for (String key : keySet) {
						oldRecord.addField(bizRecord.getFieldBy(key));
					}
				}
				
				XBizRecord newRecord = bizRecord;
				if (needConvert) {
					// ����Ŀ��汾���ݸ�ʽ����ת��
					newRecord = EnbBizHelper.convertDataByVersion(
							enb.getEnbType(), targetVersion, tableName,
							bizRecord);
				}
				specialProcess(enb, targetVersion, tableName, newRecord,oldRecord);
				// �ֶ�����ʱ�޸����ֶ����͡�����ȡֵ��Χʱ�Ĵ���
				sameFieldUpdate(enb,targetVersion,tableName,newRecord,bizRecord);
				
				String sql = enbBizConfigProxy.generateInsertSql(
						enb.getEnbType(), targetVersion, tableName, newRecord);

				sqls.add(sql);
			}
		}
		return sqls;
	}
	
	/**
	 * ����ԭ�汾��Ŀ��汾��,��ͬ�ֶ��޸����ֶ����ͻ���ȡֵ��Χ�����(ֻ֧��Unsigned32��Integer֮��������޸�)
	 * @param enb
	 * @param targetVersion
	 * @param tableName
	 * @param newRecord
	 */
	public void sameFieldUpdate(Enb enb, String targetVersion,
			String tableName, XBizRecord newRecord, XBizRecord oldRecord)
			throws Exception {
		
		// ��ȡ����Ŀ��汾����ֶ���Ϣ
		XList tableList = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				targetVersion, tableName);
		XList[] fieldlists = tableList.getFieldMetaList();
		for (XList fieldlist : fieldlists) {
			if(fieldlist.isRef()) {
				continue;
			}
			// �ж��°汾���ֶξɰ汾�Ƿ����
			String fieldName = fieldlist.getName();
 			XBizField oldField = oldRecord.getFieldBy(fieldName);
			if (oldField == null) {
				// ���������,������һ���ֶ�
				continue;
			} else {
				// �������������
				if(fieldlist.isUnsignedNum()) {
					int min = 0;
					int max = 0;
					// �����enum
					if (fieldlist.isEnum()) {
						// ��ȡ�ֶη�Χ
						int[] enumRange = fieldlist.getEnumRange();
						if(null == enumRange || enumRange.length <= 0) {
							continue;
						}
						min = enumRange[0];
						max = enumRange[enumRange.length - 1];
					} 
					// �����unsigned32
					else if(fieldlist.isUnsigned32()) {
						int[] rangeText = fieldlist.getRange();
						min = rangeText[0];
						max = rangeText[1];
					}
					// �ж��Ѿ�ת���õ������Ƿ��ڷ�Χ��
					long fieldValue = newRecord.getLongValue(fieldName);
					if(fieldValue >= min && fieldValue <= max) {
						continue;
					} 
					String defaultValue = fieldlist.getDefault();
					// ������ڷ�Χ��,�д���Ĭ��ֵ,���滻ΪĬ��ֵ
					if(null != defaultValue && !defaultValue.equals("")) {
						// Ϊ�˷�ֹĬ��ֵ������������,��ת����������ת�����ַ���,������������쳣
						newRecord
								.addField(new XBizField(fieldName, String
										.valueOf(Integer.valueOf(defaultValue
												.trim()))));
					} 
					// ���Ĭ��ֵΪ��,������Сֵ�滻
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
		// �澯������Ҫ���⴦��
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ALARM_PARA)) {
			// ���⴦��3.0.5���°汾������3.0.5�����ϰ汾
			if (enb.getProtocolVersion().compareTo("3.0.5") < 0
					&& targetVersion.compareTo("3.0.5") >= 0) {
				// �жϵ�ǰ�汾�Ƿ���ڸ澯������,�������򲻴���
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
		// ���⴦��T_ETHPARA(��̫��������)
		else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ETHPARA)) {
			// ���⴦��3.0.1�������°汾������3.0.2�������ϰ汾
			if (enb.getProtocolVersion().compareTo("3.0.1") <= 0
					&& targetVersion.compareTo("3.0.2") >= 0) {
				// �жϵ�ǰ�汾�Ƿ����T_ETHPARA��,�������򲻴���
				if (!EnbBizHelper.hasBizMeta(enb.getEnbType(), targetVersion,
						tableName)) {
					return;
				}
				if (oldRecord
						.getIntValue(EnbConstantUtils.FIELD_NAME_PORT_ID) == newRecord
						.getIntValue(EnbConstantUtils.FIELD_NAME_PORT_ID)) {
					// �����ݼ���ת��
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
