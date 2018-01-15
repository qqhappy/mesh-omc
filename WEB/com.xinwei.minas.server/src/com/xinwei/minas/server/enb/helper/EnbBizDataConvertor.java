/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-10	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.helper;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.dao.EnbBizConfigDAO;
import com.xinwei.minas.server.enb.dao.EnbBizTemplateDAO;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.core.model.meta.XMetaRef;

/**
 * 
 * eNB��վ��������ת����
 * 
 * @author fanhaoyu
 * 
 */

public class EnbBizDataConvertor {

	private Log log = LogFactory.getLog(EnbBizDataConvertor.class);

	private EnbBizConfigDAO enbBizConfigDAO;

	private EnbBizTemplateDAO enbBizTemplateDAO;

	/**
	 * �����еĻ�վ���ݰ���Ŀ�굱ǰ�汾��enb-biz.xml��������ת��
	 * 
	 * @throws Exception
	 */
	public void convert(long moId, String targetVersion) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		// ��ȡĿ��汾�е�ҵ�������
		Set<String> targetTableList = EnbBizHelper.getAllTableNames(
				enb.getEnbType(), targetVersion);
		// ��ȡ��ǰ�汾��ҵ�������

		Set<String> currentTableList = EnbBizHelper.getAllTableNames(
				enb.getEnbType(), enb.getProtocolVersion());
		// ���Ŀ��汾�޴˱���ԭ�汾�д˱���Ҫ���˱�����ɾ��
		for (String tableName : currentTableList) {
			boolean hasTable = EnbBizHelper.hasBizMeta(enb.getEnbType(),
					targetVersion, tableName);
			if (!hasTable) {
				enbBizConfigDAO.deleteAll(moId, tableName);
			}
		}

		for (String tableName : targetTableList) {
			// ��ѯ����ĳ��ҵ�������
			XBizTable oldBizTable = enbBizConfigDAO
					.query(moId, tableName, null);

			// ����ޱ����ݣ���ԭ�汾�޴˱���Ҫ���Ĭ������
			if (!EnbBizHelper.hasRecord(oldBizTable)) {
				boolean hasTable = EnbBizHelper.hasBizMeta(moId, tableName);
				if (!hasTable) {
					// �����С����صı���Ҫ����С����������
					if (EnbBizHelper.isCellRelatedTable(enb.getEnbType(),
							targetVersion, tableName)) {
						generateDefaultCellRelatedRecords(moId, targetVersion,
								tableName);
					} else {
						// ��Ҫ�����Ƿ���ģ�����ݣ�������Ҫ���
						generateTemplateData(moId, targetVersion, tableName);
					}
					// TODO: ����ǻ�վ��صı���Ҫ���ջ�վģ��������������
				}
			}

			// �����ǰ����������Ԫ���ݲ�ƥ��,��Ҫ������ת��
			if (!EnbBizHelper.isDataMatchVersion(enb.getEnbType(),
					targetVersion, oldBizTable)) {

				// ���ݾ����ݺ��°汾�Ŵ��������ݣ������¿�������
				for (XBizRecord xBizRecord : oldBizTable.getRecords()) {
					XBizRecord newRecord = EnbBizHelper.convertDataByVersion(
							enb.getEnbType(), targetVersion, tableName,
							xBizRecord);
					// FIXME: ��������仯���޷�����
					try {
						// ����¼�е�hexArray�����ֶε�ֵת��Сд
						EnbBizHelper.changeHexArrayToLowerCase(
								enb.getEnbType(), targetVersion, tableName,
								newRecord);
						enbBizConfigDAO.update(moId, tableName, newRecord);
					} catch (Exception e) {
						log.error("convert biz data with error. tableName="
								+ tableName, e);
					}

				}

				// �����ǰ�汾�޴˱���ֱ������
				if (!EnbBizHelper.hasBizMeta(moId, tableName))
					continue;
				// ���Ŀ��汾�޴˱���ֱ������
				if (!EnbBizHelper.hasBizMeta(enb.getEnbType(), targetVersion,
						tableName))
					continue;

				// REMARK: ���´�����������⣬�������С��������������ICIC��A3��������������
				// ���ֶ����ò������ñ�����Ϊ6�ļ�¼���ü�¼���°汾����
				// ��ȡĿ��汾�ȵ�ǰ�汾��ӵ��ֶ�
				List<XList> newFieldMetas = EnbBizHelper.getNewFields(
						enb.getEnbType(), enb.getProtocolVersion(),
						targetVersion, tableName);
				// ���Ŀ��汾���������ֶ�
				if (newFieldMetas != null && !newFieldMetas.isEmpty()) {
					for (XList fieldMeta : newFieldMetas) {
						// ��������ֶ��������������Ĭ�����ü�¼
						if (fieldMeta.containsRef()) {
							handleNewRefField(moId, targetVersion, fieldMeta);
						}
					}
				}
				dataNoMatchSpecialProcess(enb, targetVersion, tableName,
						oldBizTable);

			}
			dataMatchSpecialProcess(enb, targetVersion, tableName, oldBizTable);
			// �ֶ�����ʱ�޸����ֶ����͡�����ȡֵ��Χʱ�Ĵ���
			sameFieldUpdate(enb,targetVersion,tableName,oldBizTable);
		}
	}

	/**
	 * ����ԭ�汾��Ŀ��汾��,��ͬ�ֶ��޸����ֶ����ͻ���ȡֵ��Χ�����
	 * @param enb
	 * @param targetVersion
	 * @param tableName
	 * @param oldBizTable
	 * @throws Exception 
	 */
	public void sameFieldUpdate(Enb enb, String targetVersion,
			String tableName, XBizTable oldBizTable) throws Exception {
		// ��ȡ����Ŀ��汾����ֶ���Ϣ
		XList tableList = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				targetVersion, tableName);
		XList[] fieldlists = tableList.getFieldMetaList();
		// �õ���Ҫ�Ķ�������
		List<XBizRecord> records = oldBizTable.getRecords();
		for (XBizRecord record : records) {
			for (XList fieldlist : fieldlists) {
				if(fieldlist.isRef()) {
					continue;
				}
				// �ж��°汾���ֶξɰ汾�Ƿ����
				String fieldName = fieldlist.getName();
	 			XBizField oldField = record.getFieldBy(fieldName);
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
						long fieldValue = record.getLongValue(fieldName);
						if(fieldValue >= min && fieldValue <= max) {
							continue;
						} 
						String defaultValue = fieldlist.getDefault();
						// ������ڷ�Χ��,�д���Ĭ��ֵ,���滻ΪĬ��ֵ
						if(null != defaultValue && !defaultValue.equals("")) {
							// Ϊ�˷�ֹĬ��ֵ������������,��ת����������ת�����ַ���,������������쳣
							record
									.addField(new XBizField(fieldName, String
											.valueOf(Integer.valueOf(defaultValue
													.trim()))));
						} 
						// ���Ĭ��ֵΪ��,������Сֵ�滻
						else {
							record.addField(new XBizField(fieldName, String
									.valueOf(min)));
						}
					}
				}
			}
			enbBizConfigDAO.update(enb.getMoId(), tableName, record);
		}
	}

	/**
	 * �汾���ͬ�����ݵ����⴦��:�����¾����ݸ�ʽƥ������
	 * 
	 * @param enb
	 * @param targetVersion
	 * @param tableName
	 * @param oldBizTable
	 * @throws Exception
	 */
	public void dataMatchSpecialProcess(Enb enb, String targetVersion,
			String tableName, XBizTable oldBizTable) throws Exception {
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
				List<XBizRecord> records = oldBizTable.getRecords();
				for (XBizRecord record : records) {
					if (0 != record.getIntValue("u32DetIntervalTime")) {
						continue;
					}
					XBizTable templateData = enbBizTemplateDAO
							.queryTemplateData(enb.getEnbType(), targetVersion,
									tableName);
					List<XBizRecord> templateRecords = templateData
							.getRecords();
					boolean flag = false;
					for (XBizRecord templateRecord : templateRecords) {
						if (record.getIntValue("u8Indx") == templateRecord
								.getIntValue("u8Indx")) {
							record.addField(new XBizField(
									"u32DetIntervalTime",
									templateRecord
											.getStringValue("u32DetIntervalTime")));
							flag = true;
						}
					}
					if (!flag) {
						record.addField(new XBizField("u32DetIntervalTime", "1"));
					}
					enbBizConfigDAO.update(enb.getMoId(), tableName, record);
				}

			}
		}
	}

	/**
	 * �汾���ͬ�����ݵ����⴦��:�����¾����ݸ�ʽ��ƥ������
	 * 
	 * @param tableName
	 * @param targetVersion
	 * @param enb
	 * @param oldBizTable
	 * @throws Exception
	 */
	public void dataNoMatchSpecialProcess(Enb enb, String targetVersion,
			String tableName, XBizTable oldBizTable) throws Exception {
		// ���⴦��T_VLAN��
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_VLAN)) {
			// ���⴦��3.0.4�������ϰ汾��3.0.3�������°汾��
			if (enb.getProtocolVersion().compareTo("3.0.4") >= 0
					&& targetVersion.compareTo("3.0.3") <= 0) {
				// �жϵ�ǰ�汾�Ƿ����T_VLAN��,�������򲻴���
				if (!EnbBizHelper.hasBizMeta(enb.getEnbType(), targetVersion,
						tableName)) {
					return;
				}
				// �����豸�ĸñ��������
				enbBizConfigDAO.deleteAll(enb.getMoId(), tableName);
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
				// ��ѯ����ǰ����
				XBizTable newBizTable = enbBizConfigDAO.query(enb.getMoId(),
						tableName, null);
				List<XBizRecord> newRecords = newBizTable.getRecords();
				List<XBizRecord> oldRecords = oldBizTable.getRecords();
				for (XBizRecord oldRecord : oldRecords) {
					for (XBizRecord newRecord : newRecords) {
						if (oldRecord
								.getIntValue(EnbConstantUtils.FIELD_NAME_PORT_ID) == newRecord
								.getIntValue(EnbConstantUtils.FIELD_NAME_PORT_ID)) {
							// �����ݼ���ת��
							EnbBizHelper.ethParaDataConvertor(newRecord,oldRecord);
							// ��������
							enbBizConfigDAO.update(enb.getMoId(), tableName, newRecord);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * �°汾����������С���޹����ı���Ҫ�鿴�Ƿ���ģ�����ݣ�������Ҫ���
	 * 
	 * @param moId
	 * @param targetVersion
	 * @param tableName
	 * @throws Exception
	 */
	private void generateTemplateData(long moId, String targetVersion,
			String tableName) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		XBizTable bizTable = enbBizTemplateDAO.queryTemplateData(
				enb.getEnbType(), targetVersion, tableName);
		if (EnbBizHelper.hasRecord(bizTable)) {
			for (XBizRecord bizRecord : bizTable.getRecords()) {
				try {
					// ����¼�е�hexArray�����ֶε�ֵת��Сд
					EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
							targetVersion, tableName, bizRecord);
					// �����°汾��������ʽ�������
					XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(
							enb.getEnbType(), targetVersion, tableName,
							bizRecord);
					enbBizConfigDAO.add(moId, tableName, bizKey, bizRecord);
				} catch (Exception e) {
					log.error("generate template data with error. tableName="
							+ tableName, e);
				}

			}
		}
	}

	/**
	 * ����С����ر��Ĭ������
	 * 
	 * @param moId
	 * @param targetVersion
	 * @param tableName
	 * @throws Exception
	 */
	private void generateDefaultCellRelatedRecords(long moId,
			String targetVersion, String tableName) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		XBizTable cellTable = enbBizConfigDAO.query(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
		List<XBizRecord> newRecords = EnbBizHelper.createCellRelatedRecords(
				enb.getEnbType(), targetVersion, tableName, cellTable);
		if (newRecords != null) {
			// ��������ݵ����ݿ�
			for (XBizRecord newRecord : newRecords) {
				try {
					// ����¼�е�hexArray�����ֶε�ֵת��Сд
					EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
							targetVersion, tableName, newRecord);
					// �����°汾��������ʽ�������
					XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(
							enb.getEnbType(), targetVersion, tableName,
							newRecord);
					enbBizConfigDAO.add(moId, tableName, bizKey, newRecord);
				} catch (Exception e) {
					log.error("createCellRelatedRecords with error. tableName="
							+ tableName, e);
				}
			}
		}

	}

	/**
	 * ����Ŀ��汾���µ�����ֶ�
	 * 
	 * @param moId
	 * @param targetVersion
	 * @param fieldMeta
	 * @throws Exception
	 */
	private void handleNewRefField(long moId, String targetVersion,
			XList fieldMeta) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
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
						enb.getEnbType(), targetVersion, refTable, keyField);
				if (defaultRefRecord != null) {
					// ����¼�е�hexArray�����ֶε�ֵת��Сд
					EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
							targetVersion, refTable, defaultRefRecord);
					// �����°汾��������ʽ�������
					XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(
							enb.getEnbType(), targetVersion, refTable,
							defaultRefRecord);
					enbBizConfigDAO.add(moId, refTable, bizKey,
							defaultRefRecord);
				}
			} catch (Exception e) {
				log.error(
						"create default reference record failed. refFieldName="
								+ fieldMeta.getName(), e);
			}
		}
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
		// protocolVersion, tableName, null);
		// }
		return retRecord;
	}

	public void setEnbBizConfigDAO(EnbBizConfigDAO enbBizConfigDAO) {
		this.enbBizConfigDAO = enbBizConfigDAO;
	}

	public void setEnbBizTemplateDAO(EnbBizTemplateDAO enbBizTemplateDAO) {
		this.enbBizTemplateDAO = enbBizTemplateDAO;
	}

}
