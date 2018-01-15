/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.micro.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbAttribute;
import com.xinwei.minas.enb.core.model.EnbCondition;
import com.xinwei.minas.enb.core.model.EnbNeighbourRecord;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.minas.enb.core.model.corenet.EnbGlobalConfig;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.core.alarm.dao.AlarmDAO;
import com.xinwei.minas.server.enb.dao.EnbBasicDAO;
import com.xinwei.minas.server.enb.dao.EnbBizConfigDAO;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.service.EnbBizConfigService;
import com.xinwei.minas.server.enb.service.EnbBizTemplateService;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbGlobalConfigService;
import com.xinwei.minas.server.enb.service.EnbNeighbourService;
import com.xinwei.minas.server.enb.task.EnbAssetTaskManager;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidatorRegistry;
import com.xinwei.minas.server.micro.service.MicEnbBasicService;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.utils.ReflectUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB������Ϣ����ӿ�ʵ��
 * 
 * @author fanhaoyu
 * 
 */

public class MicEnbBasicServiceImpl implements MicEnbBasicService {

	private Log log = LogFactory.getLog(MicEnbBasicServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private EnbBasicDAO enbBasicDAO;

	private EnbBizConfigDAO enbBizConfigDAO;

	private EnbBizConfigService enbBizConfigService;

	private EnbBizTemplateService enbBizTemplateService;

	private EnbGlobalConfigService enbGlobalConfigService;

	private EnbNeighbourService enbNeighbourService;

	private AlarmDAO alarmDAO;

	private SequenceService sequenceService;

	private void doTransaction(final String methodName, final Object... args)
			throws Exception {

		Exception exception = transactionTemplate
				.execute(new TransactionCallback<Exception>() {

					@Override
					public Exception doInTransaction(TransactionStatus txStatus) {
						try {
							Method method = ReflectUtils.findMethod(
									MicEnbBasicServiceImpl.class, methodName);
							method.invoke(MicEnbBasicServiceImpl.this, args);
						} catch (Exception e) {
							txStatus.setRollbackOnly();
							return e;
						}
						return null;
					}
				});
		if (exception != null) {
			if (exception instanceof InvocationTargetException) {
				InvocationTargetException e = (InvocationTargetException) exception;
				Throwable throwable = e.getTargetException();
				if (throwable instanceof Exception) {
					throw (Exception) throwable;
				}
			}
			throw exception;
		}
	}

	@Override
	public void add(Enb enb) throws Exception {
		// ����
		if (EnbCache.getInstance().enbExists(enb.getEnbId())) {
			throw new Exception(OmpAppContext.getMessage("enb_duplicated"));
		}
		// У��eNB�����Ƿ��ظ�
		checkEnbNameDuplicated(enb);

		Map<String, XBizField> fieldMap = createBizFieldMap(enb);
		// У��IP
		// ����Ĭ��IPv4���¼
		XBizRecord ipRecord = generateDefaultIPv4Record(fieldMap);
		// ��װ�°汾���ӵ�����
		createIpv4RecordNew(enb, ipRecord);

		checkIp(ipRecord);

		synchronized (this) {
			long moId = sequenceService.getNext("BTS");
			enb.setMoId(moId);
			enb.setTypeId(MoTypeDD.ENODEB);
			try {
				doTransaction("doAddEnb", moId, enb, ipRecord);
			} catch (Exception e) {
				// ���ʧ�ܣ���eNB�ӻ������Ƴ�
				EnbCache.getInstance().delete(moId);
				log.error("add eNB failed.", e);
				throw new Exception(OmpAppContext.getMessage("add_eNB_failed"));
			}
		}

	}

	public void createIpv4RecordNew(Enb enb, XBizRecord ipRecord) {
		// ��ȡipv4��ǰ�汾xml�ֶ�
		List<String> allFieldNames = EnbBizHelper.getAllFieldNames(
				enb.getEnbType(), enb.getProtocolVersion(),
				EnbConstantUtils.TABLE_NAME_T_IPV4);
		if (allFieldNames.contains(EnbConstantUtils.FIELD_NAME_IPV4_VLAN_INDEX)) {
			ipRecord.addField(new XBizField(
					EnbConstantUtils.FIELD_NAME_IPV4_VLAN_INDEX, "0"));
		}
	}

	public void updateIpv4RecordNew(Enb enb, XBizRecord ipRecord)
			throws Exception {
		// ��ȡipv4��ǰ�汾xml�ֶ�
		List<String> allFieldNames = EnbBizHelper.getAllFieldNames(
				enb.getEnbType(), enb.getProtocolVersion(),
				EnbConstantUtils.TABLE_NAME_T_IPV4);
		if (allFieldNames.contains(EnbConstantUtils.FIELD_NAME_IPV4_VLAN_INDEX)) {
			XBizRecord keyRecord = EnbBizHelper.getKeyRecordBy(
					enb.getEnbType(), enb.getProtocolVersion(),
					EnbConstantUtils.TABLE_NAME_T_IPV4, ipRecord);
			XBizRecord record = enbBizConfigDAO.queryByKey(enb.getMoId(),
					EnbConstantUtils.TABLE_NAME_T_IPV4, keyRecord);
			ipRecord.addField(record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_IPV4_VLAN_INDEX));
		}
	}

	public void doAddEnb(long moId, Enb enb, XBizRecord ipRecord)
			throws Exception {
		// ������Ϣ���
		enbBasicDAO.saveOrUpdate(enb);
		// ��ӵ�����
		EnbCache.getInstance().addOrUpdate(enb);
		// ����ģ���������ɻ�վ����ҵ������
		generateDataByTemplate(enb);
		// IPv4��Ĭ�ϼ�¼���
		enbBizConfigDAO.add(moId, EnbConstantUtils.TABLE_NAME_T_IPV4, ipRecord);
		// ����service����Ĭ�����ܱ��¼���Ա�����ӳɹ��󽫻�վ״̬��Ϊ�ѿ�վ
		XBizRecord omcRecord = generateDefaultOmcRecord();
		enbBizConfigService.add(moId, EnbConstantUtils.TABLE_NAME_T_OMC,
				omcRecord);
	}

	/**
	 * ����Ĭ�ϵ����ܱ��¼
	 * 
	 * @return
	 * @throws Exception
	 */
	private XBizRecord generateDefaultOmcRecord() throws Exception {
		XBizRecord bizRecord = new XBizRecord();
		// ��ȡ������IP
		InetSocketAddress address = enbBizConfigService.queryEmsNetAddress(0l);
		bizRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_OMC_ID,
				"1"));
		bizRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_ENB_IP_ID,
				"1"));

		String omcIp = convertIpTo8HexString(address.getAddress()
				.getHostAddress());
		XBizField ipField = new XBizField(
				EnbConstantUtils.FIELD_NAME_OMC_SERVER_IP, omcIp.toLowerCase());
		bizRecord.addField(ipField);

		bizRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SRC_PORT,
				String.valueOf(address.getPort())));
		bizRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_DST_PORT,
				"4999"));
		bizRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_QOS, "0"));
		// Ĭ��״̬������
		bizRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_STATUS,
				"1"));

		return bizRecord;
	}

	/**
	 * ����IPv4��Ĭ�ϼ�¼(IP��ʶΪ1���˿ڱ�ʶΪ1)
	 * 
	 * @param ipAddress
	 * @param netMask
	 * @param gateway
	 * @return
	 */
	private XBizRecord generateDefaultIPv4Record(Map<String, XBizField> fieldMap) {
		XBizRecord bizRecord = new XBizRecord();
		bizRecord.addField(new XBizField(
				EnbConstantUtils.FIELD_NAME_IPV4_PORT_ID, "1"));
		bizRecord
				.addField(new XBizField(EnbConstantUtils.FIELD_NAME_IP_ID, "1"));
		bizRecord.addField(fieldMap.get(EnbConstantUtils.FIELD_NAME_IP_ADDR));
		bizRecord.addField(fieldMap.get(EnbConstantUtils.FIELD_NAME_NET_MASK));
		bizRecord.addField(fieldMap.get(EnbConstantUtils.FIELD_NAME_GATEWAY));

		return bizRecord;
	}

	private Map<String, XBizField> createBizFieldMap(Enb enb) throws Exception {
		if (enb.getIpAddress() == null || enb.getNetMask() == null
				|| enb.getGateway() == null)
			return null;
		Map<String, XBizField> fieldMap = new HashMap<String, XBizField>();

		String ip = convertIpTo8HexString(enb.getIpAddress());
		XBizField ipField = new XBizField(EnbConstantUtils.FIELD_NAME_IP_ADDR,
				ip.toLowerCase());
		fieldMap.put(EnbConstantUtils.FIELD_NAME_IP_ADDR, ipField);

		String netMask = convertIpTo8HexString(enb.getNetMask());
		XBizField netMaskField = new XBizField(
				EnbConstantUtils.FIELD_NAME_NET_MASK, netMask.toLowerCase());
		fieldMap.put(EnbConstantUtils.FIELD_NAME_NET_MASK, netMaskField);

		String gateway = convertIpTo8HexString(enb.getGateway());
		XBizField gatewayField = new XBizField(
				EnbConstantUtils.FIELD_NAME_GATEWAY, gateway.toLowerCase());
		fieldMap.put(EnbConstantUtils.FIELD_NAME_GATEWAY, gatewayField);

		return fieldMap;
	}

	/**
	 * ��*.*.*.*���͵�IP�ַ���ת��Ϊ8λ16�����ַ���
	 * 
	 * @param ip
	 * @return
	 */
	private String convertIpTo8HexString(String ip) {
		String[] array = ip.split("\\.");
		String result = "";
		for (int i = 0; i < array.length; i++) {
			int a = Integer.valueOf(array[i]);
			String aa = Integer.toHexString(a);
			if (aa.length() == 1) {
				aa = "0" + aa;
			}
			result = result + aa;
		}
		return result;
	}

	/**
	 * У��������վ��IP��ַ�Ƿ�Ϸ�(IP��ַ�Ƿ��ظ������ص�ַ�Ƿ���IP��ַ��ͬһ����)
	 * 
	 * @param ipRecord
	 * @throws Exception
	 */
	private void checkIp(XBizRecord ipRecord) throws Exception {
		// ��ȡУ����
		EnbBizDataValidator validator = EnbBizDataValidatorRegistry
				.getInstance().getValidator(EnbConstantUtils.TABLE_NAME_T_IPV4);
		validator.validate(0l, ipRecord, ActionTypeDD.ADD);
	}

	/**
	 * У��eNB�����Ƿ��ظ�
	 * 
	 * @param enb
	 * @throws Exception
	 */
	private void checkEnbNameDuplicated(Enb enb) throws Exception {
		List<Enb> enbList = EnbCache.getInstance().queryAll();
		for (Enb enb2 : enbList) {
			if (enb2.getMoId() == enb.getMoId())
				continue;
			if (enb2.getName().equals(enb.getName())) {
				throw new Exception(
						OmpAppContext.getMessage("enb_name_duplicated"));
			}
		}
	}

	/**
	 * ����ģ���������ɻ�վ����ҵ������
	 * 
	 * @param enb
	 * @throws Exception
	 */
	private void generateDataByTemplate(Enb enb) throws Exception {
		long moId = enb.getMoId();
		try {
			// ��ѯȫ���������㷨����
			EnbGlobalConfig config = enbGlobalConfigService
					.queryEnbGlobalConfig();
			String eea = config.getEea();
			String eia = config.getEia();

			// ��ѯģ������
			Map<String, List<XBizRecord>> tableMap = enbBizTemplateService
					.queryTemplateData(enb.getEnbType(),
							enb.getProtocolVersion());
			for (String tableName : tableMap.keySet()) {
				// �����С���������������ص�С�����ñ�,������ģ������
				if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)
						|| EnbBizHelper.isCellRelatedTable(enb.getEnbType(),
								enb.getProtocolVersion(), tableName)) {
					continue;
				}
				
				// ����Ǻ��վ���˱�������ģ������
				if(tableName.equals(EnbConstantUtils.TABLE_NAME_T_TOPO) && EnbTypeDD.XW7400 == enb.getEnbType()) {
					continue;
				}
				
				List<XBizRecord> records = tableMap.get(tableName);
				
				if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENB_PARA)) {
					// ��ENB�������е�enbId�ĳɱ���վ��enbId�����Ƹĳɱ���վ��enbName
					XBizRecord bizRecord = records.get(0);
					XBizField idField = new XBizField(
							EnbConstantUtils.FIELD_NAME_ENB_ID, enb.getEnbId()
									.toString());
					bizRecord.addField(idField);
					// �����㷨����
					bizRecord.addField(new XBizField(
							EnbConstantUtils.FIELD_NAME_EEA, eea));
					bizRecord.addField(new XBizField(
							EnbConstantUtils.FIELD_NAME_EIA, eia));
					XBizField nameField = new XBizField(
							EnbConstantUtils.FIELD_NAME_ENB_NAME, enb.getName());
					bizRecord.addField(nameField);
				}

				for (XBizRecord record : records) {
					// ����ǵ����,RRU������ģ������
					if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_BOARD)) {
						if (EnbConstantUtils.BOARD_TYPE_RRU == record
								.getIntValue(EnbConstantUtils.FIELD_NAME_BDTYPE)) {
							continue;
						}
					}
					// ����¼�е�hexArray�����ֶε�ֵת��Сд
					EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
							enb.getProtocolVersion(), tableName, record);
					enbBizConfigDAO.add(moId, tableName, record);
				}
			}
		} catch (Exception e) {
			log.error(
					"generate enb biz data failed. enbId=" + enb.getHexEnbId(),
					e);
		}
	}

	@Override
	public void modify(Enb enb) throws Exception {
		Enb cachedEnb = EnbCache.getInstance().queryByMoId(enb.getMoId());
		if (cachedEnb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		try {
			doTransaction("doModifyEnb", enb, cachedEnb);
		} catch (Exception e) {
			log.error("modify eNB failed.", e);
			throw new Exception(OmpAppContext.getMessage("modify_eNB_failed"));
		}
		// �޸Ļ���
		EnbCache.getInstance().addOrUpdate(enb);
	}

	public void doModifyEnb(Enb enb, Enb cachedEnb) throws Exception {
		// У��eNB�����Ƿ��ظ�
		checkEnbNameDuplicated(enb);
		// �޸����ݿ�
		enbBasicDAO.saveOrUpdate(enb);
		// �޸������ܱ��������IPv4���м�¼
		Map<String, XBizField> fieldMap = createBizFieldMap(enb);
		updateEnbIpRecord(cachedEnb.getMoId(), fieldMap);

		// ������Ƹı䣬���޸�eNB�������е�����
		updateEnbNameOfEnbPara(enb);
	}

	/**
	 * ����eNB��IPv4���м�¼
	 * 
	 * @param moId
	 * @param fieldMap
	 * @throws Exception
	 */
	private void updateEnbIpRecord(long moId, Map<String, XBizField> fieldMap)
			throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		if (fieldMap == null || fieldMap.isEmpty())
			return;
		XBizRecord ipRecord = queryEnbIpRecord(moId);
		if (ipRecord == null) {
			ipRecord = generateDefaultIPv4Record(fieldMap);
			updateIpv4RecordNew(enb, ipRecord);
		} else {
			String oldIp = ipRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_IP_ADDR).getValue();
			String oldNetMask = ipRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_NET_MASK).getValue();
			String oldGateway = ipRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_GATEWAY).getValue();

			XBizField newIpField = fieldMap
					.get(EnbConstantUtils.FIELD_NAME_IP_ADDR);
			String newIp = newIpField.getValue();
			XBizField newNetMaskField = fieldMap
					.get(EnbConstantUtils.FIELD_NAME_NET_MASK);
			String newNetMask = newNetMaskField.getValue();
			XBizField newGatewayField = fieldMap
					.get(EnbConstantUtils.FIELD_NAME_GATEWAY);
			String newGateway = newGatewayField.getValue();
			// ���δ�ı䣬�������޸�
			if (oldIp.equals(newIp) && oldNetMask.equals(newNetMask)
					&& oldGateway.equals(newGateway))
				return;
			ipRecord.addField(newIpField);
			ipRecord.addField(newNetMaskField);
			ipRecord.addField(newGatewayField);
		}
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId,
				EnbConstantUtils.TABLE_NAME_T_IPV4, ipRecord);
		XBizRecord oldIpRecord = enbBizConfigDAO.queryByKey(moId,
				EnbConstantUtils.TABLE_NAME_T_IPV4, bizKey);
		if (oldIpRecord == null) {
			// IPv4��Ĭ�ϼ�¼���
			enbBizConfigService.add(moId, EnbConstantUtils.TABLE_NAME_T_IPV4,
					ipRecord);
		} else {
			// ���¼�¼
			enbBizConfigService.update(moId,
					EnbConstantUtils.TABLE_NAME_T_IPV4, ipRecord);
		}
		// ����service����Ĭ�����ܱ��¼���Ա�����ӳɹ��󽫻�վ״̬��Ϊ�ѿ�վ
		XBizTable omcTable = enbBizConfigDAO.query(moId,
				EnbConstantUtils.TABLE_NAME_T_OMC, null);
		XBizRecord omcRecord = null;
		if (EnbBizHelper.hasRecord(omcTable)) {
			omcRecord = omcTable.getRecords().get(0);
			// �˴�ΪʲôҪ��ipv4��Ĺ���ǿ������Ϊ��һ��ipv4���¼?
			/*omcRecord.addField(new XBizField(
					EnbConstantUtils.FIELD_NAME_ENB_IP_ID, "1"));*/
			enbBizConfigService.update(moId, EnbConstantUtils.TABLE_NAME_T_OMC,
					omcRecord);
		} else {
			omcRecord = generateDefaultOmcRecord();
			enbBizConfigService.add(moId, EnbConstantUtils.TABLE_NAME_T_OMC,
					omcRecord);
		}
	}

	/**
	 * ��ѯeNB�����ܽ����������õ�IPv4���м�¼
	 * 
	 * @param moId
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	private XBizRecord queryEnbIpRecord(long moId) throws Exception {
		XBizTable omcTable = enbBizConfigDAO.query(moId,
				EnbConstantUtils.TABLE_NAME_T_OMC, null);
		if (!EnbBizHelper.hasRecord(omcTable))
			return null;
		XBizRecord omcRecord = omcTable.getRecords().get(0);
		XBizField enbIpIdField = omcRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_ENB_IP_ID);
		// ��ѯ���ܱ����õ�IPv4���м�¼
		XBizRecord condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_IP_ID,
				enbIpIdField.getValue()));
		XBizTable ipv4Table = enbBizConfigDAO.query(moId,
				EnbConstantUtils.TABLE_NAME_T_IPV4, condition);
		return ipv4Table.getRecords().get(0);
	}

	/**
	 * ������Ƹı䣬���޸�eNB�������е�����
	 * 
	 * @param enb
	 * @throws Exception
	 */
	private void updateEnbNameOfEnbPara(Enb enb) throws Exception {
		// �޸�eNB�������е�����
		XBizRecord bizKey = new XBizRecord();
		bizKey.addField(new XBizField(EnbConstantUtils.FIELD_NAME_ENB_ID,
				String.valueOf(enb.getEnbId())));
		XBizRecord record = enbBizConfigDAO.queryByKey(enb.getMoId(),
				EnbConstantUtils.TABLE_NAME_T_ENB_PARA, bizKey);
		String oldEnbName = record.getFieldBy(
				EnbConstantUtils.FIELD_NAME_ENB_NAME).getValue();
		// �ı�ʱ�Ÿ���
		if (!oldEnbName.equals(enb.getName())) {
			record.addField(new XBizField(EnbConstantUtils.FIELD_NAME_ENB_NAME,
					enb.getName()));
			// ����¼�е�hexArray�����ֶε�ֵת��Сд
			EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
					enb.getProtocolVersion(),
					EnbConstantUtils.TABLE_NAME_T_ENB_PARA, record);
			enbBizConfigService.update(enb.getMoId(),
					EnbConstantUtils.TABLE_NAME_T_ENB_PARA, record);
		}
	}

	@Override
	public void delete(final Long moId) throws Exception {
		final Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		// ��¼��Ԫ����״̬
		int manageStatus = enb.getManageStateCode();
		// ����Ԫ����Ϊ���߹����������ҵ������Ԫ����Ϣ
		enb.setManageStateCode(ManageState.OFFLINE_STATE);
		try {
			doTransaction("doDeleteEnb", moId);
		} catch (Exception e) {
			log.error("delete eNB failed.", e);
			enb.setManageStateCode(manageStatus);
			throw e;
		}
		// ɾ������
		EnbAssetTaskManager.getInstance().deleteEnb(enb);
		EnbCache.getInstance().delete(moId);
		EnbCache.getInstance().deletePciByEnb(enb.getEnbId());
		EnbCache.getInstance().deleteRsiByEnb(enb.getEnbId());
	}

	public void doDeleteEnb(long moId) throws Exception {
		// У���վ�Ƿ�����ɾ��
		List<EnbNeighbourRecord> neighbourRecords = enbNeighbourService
				.queryNeighbourRecords(moId);
		checkDelete(moId,neighbourRecords);
		// ɾ��������Ϣ
		enbBasicDAO.delete(moId);
		// ɾ���ڽӱ���Ϣ
		for (EnbNeighbourRecord enbNeighbourRecord : neighbourRecords) {
			enbNeighbourService.deleteNeighbour(moId, enbNeighbourRecord);
		}
		// ɾ�����ݿ��л�վ������������
		enbBizConfigDAO.deleteAll(moId);
		// ɾ����վ���и澯
		alarmDAO.deleteAlarmByMoId(moId);
	}

	private void checkDelete(long moId, List<EnbNeighbourRecord> neighbourRecords) throws Exception {
		// У��������ϵ�Ƿ�����ɾ��
		for (EnbNeighbourRecord enbNeighbourRecord : neighbourRecords) {
			XBizRecord bizRecord = enbNeighbourRecord.getBizRecord();
			// �ж������Ƿ���Ա�ɾ��
			int u8NoRemove = bizRecord.getIntValue("u8NoRemove");
			int svrCellId = bizRecord.getIntValue(EnbConstantUtils.FIELD_NAME_SVR_CID);
			long nbEnbId = bizRecord.getLongValue(EnbConstantUtils.FIELD_NAME_NBR_ENBID);
			int nbCellId = bizRecord.getIntValue(EnbConstantUtils.FIELD_NAME_NBR_CID);
			if(1 == u8NoRemove) {
				throw new BizException(
						OmpAppContext.getMessage("nbrcel_cannot_delete4")
								+ svrCellId
								+ OmpAppContext
										.getMessage("nbrcel_cannot_delete5")
								+ EnbBizHelper.getHexEnbId(nbEnbId)
								+ OmpAppContext
										.getMessage("nbrcel_cannot_delete2")
								+ nbCellId
								+ OmpAppContext
										.getMessage("nbrcel_cannot_delete3"));
			}
		}
		
	}

	@Override
	public void changeManageState(Long moId, ManageState manageState)
			throws Exception {
		// ���»���
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		enb.setManageStateCode(manageState.getStateCode());
		// �������ݿ�
		enbBasicDAO.saveOrUpdate(enb);
	}

	@Override
	public List<Enb> queryAllEnb() throws Exception {
		return EnbCache.getInstance().queryAll();
	}

	@Override
	public Enb queryByMoId(Long moId) throws Exception {
		return EnbCache.getInstance().queryByMoId(moId);
	}

	@Override
	public Enb queryByEnbId(Long enbId) throws Exception {
		return EnbCache.getInstance().queryByEnbId(enbId);
	}

	@Override
	public PagingData<Enb> queryAllByCondition(EnbCondition condition)
			throws Exception {
		List<Enb> enbList = queryClonedEnbBy(condition);
		int lastPNum = enbList.size() % condition.getNumPerPage();
		int page = 0;
		page = enbList.size() / condition.getNumPerPage();
		if (lastPNum > 0) {
			page = page + 1;
		}

		PagingData<Enb> pagingData = new PagingData<Enb>();
		pagingData.setTotalPages(page);
		pagingData.setCurrentPage(condition.getCurrentPage());
		pagingData.setNumPerPage(condition.getNumPerPage());
		List<Enb> btsList = currentList(condition.getCurrentPage(),
				condition.getNumPerPage(), enbList);
		pagingData.setResults(btsList);
		return pagingData;
	}

	private List<Enb> queryClonedEnbBy(EnbCondition condition) throws Exception {
		List<Enb> enbList = new ArrayList<Enb>();
		String conditionId = condition.getEnbId();
		String conditionName = condition.getEnbName();
		String conditionIp = condition.getPublicIp();
		String conditionVersion = condition.getSoftwareVersion();

		if (conditionId == null) {
			conditionId = "";
		} else {
			conditionId = conditionId.toUpperCase();
		}
		if (conditionName == null) {
			conditionName = "";
		} else {
			conditionName = conditionName.toUpperCase();
		}
		if (conditionIp == null) {
			conditionIp = "";
		} else {
			conditionIp = conditionIp.toUpperCase();
		}
		if (conditionVersion == null) {
			conditionVersion = "";
		} else {
			conditionVersion = conditionVersion.toUpperCase();
		}

		List<Enb> toQueryList = EnbCache.getInstance().queryAll();
		if (toQueryList == null || toQueryList.isEmpty())
			return enbList;

		for (Enb enb : toQueryList) {
			// ��eNBId����
			String hexEnbId = enb.getHexEnbId().toUpperCase();
			if (!conditionId.equals("") && !hexEnbId.contains(conditionId)) {
				continue;
			}
			// ��eNB���ƹ���
			String name = enb.getName().toUpperCase();
			if (!name.equals("") && !name.contains(conditionName)) {
				continue;
			}
			// ������IP����
			String enbPublicIp = enb.getPublicIp();
			if (enbPublicIp == null) {
				enbPublicIp = "";
			} else {
				enbPublicIp = enbPublicIp.toUpperCase();
			}
			if (!conditionIp.equals("") && !enbPublicIp.contains(conditionIp)) {
				continue;
			}
			// ������汾����
			String version = enb.getSoftwareVersion();
			if (version == null) {
				version = "";
			} else {
				version = version.toUpperCase();
			}
			if (!conditionVersion.equals("")
					&& !version.contains(conditionVersion)) {
				continue;
			}
			// �����״̬����
			int monitorState = enb.getMonitorState();
			Integer conditionState = condition.getMonitorState();
			if (conditionState != null) {
				if (monitorState != conditionState)
					continue;
			}

			// ʹ�ÿ�¡������ȷ�����ظ��ͻ��˵����ݲ��ᱻƵ�����£��������OptionDataException
			enbList.add(enb.clone());
		}
		// ��ȡ�澯�ȼ�
		fillAlarmLevel(enbList);

		// ���С��״̬
		fillCellStatus(enbList);

		if (enbList.isEmpty())
			return enbList;

		// ����վID����
		Collections.sort(enbList, new EnbComparator(condition.getSortBy()));

		return enbList;
	}

	@Override
	public List<Enb> queryByMoIdList(List<Long> moIds) throws Exception {
		List<Enb> enbList = new ArrayList<Enb>();
		for (Long moId : moIds) {
			Enb enb = EnbCache.getInstance().queryByMoId(moId);
			Enb newEnb = null;
			if (enb == null) {
				newEnb = new Enb();
				newEnb.setMoId(moId);
				newEnb.setDeleted();
			} else {
				newEnb = enb.clone();
			}
			enbList.add(newEnb);
		}
		// ���澯����
		fillAlarmLevel(enbList);

		// ���С��״̬
		fillCellStatus(enbList);

		return enbList;
	}

	/**
	 * ���澯����
	 * 
	 * @param enbList
	 */
	private void fillAlarmLevel(List<Enb> enbList) {
		List<Long> moIds = new LinkedList<Long>();
		for (Enb enb : enbList) {
			moIds.add(enb.getMoId());
		}
		// ��ȡ��߼���澯
		Map<Long, Integer> alarmLevelMapping = alarmDAO
				.queryMaxLevelMapping(moIds);
		for (Enb enb : enbList) {
			Integer alarmLevel = 0;
			if (alarmLevelMapping.containsKey(enb.getMoId())) {
				alarmLevel = alarmLevelMapping.get(enb.getMoId());
			}
			enb.addAttribute(EnbAttribute.Key.ALARM_LEVEL, alarmLevel);
		}
	}

	/**
	 * ���� ����վС��״̬
	 * 
	 * @param enbList
	 * @throws Exception
	 */
	private void fillCellStatus(List<Enb> enbList) throws Exception {
		for (Enb enb : enbList) {
			// ����enbId�ͱ�����ȡ��¼
			XBizTable emsTable = enbBizConfigService.queryFromEms(
					enb.getMoId(), EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
					null, false);
			// �����Ϊ�գ�����ÿ����¼
			Map<Integer, Integer> cellStatusMap = new LinkedHashMap<Integer, Integer>();
			if (EnbBizHelper.hasRecord(emsTable)) {
				for (XBizRecord bizRecord : emsTable.getRecords()) {
					String cellId = bizRecord.getFieldBy(
							EnbConstantUtils.FIELD_NAME_CELL_ID).getValue();
					// Ĭ�ϲ�����
					cellStatusMap.put(Integer.valueOf(cellId),
							EnbConstantUtils.STATUS_ABNORMAL);
				}
				enb.setCellStatusMap(cellStatusMap);
				try {
					if (enb.isConfigurable()) {
						XBizTable neTable = enbBizConfigService.queryFromNe(
								enb.getMoId(),
								EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
						// ����С��״̬
						updateCellStatus(cellStatusMap, neTable);
					}
				} catch (Exception e) {
					log.warn(
							"query cell status from enb failed. enbId="
									+ enb.getHexEnbId(), e);
				}
			}
		}
	}

	/**
	 * ������eNB��ѯ�������С��״̬
	 * 
	 * @param cellStatusMap
	 * @param neTable
	 */
	private void updateCellStatus(Map<Integer, Integer> cellStatusMap,
			XBizTable neTable) {
		if (EnbBizHelper.hasRecord(neTable)) {
			for (Integer cellId : cellStatusMap.keySet()) {
				for (XBizRecord bizRecord : neTable.getRecords()) {
					// �ҵ���ӦcellId�ļ�¼������С��״̬
					String id = bizRecord.getFieldBy(
							EnbConstantUtils.FIELD_NAME_CELL_ID).getValue();
					String status = bizRecord.getFieldBy(
							EnbConstantUtils.FIELD_NAME_STATUS).getValue();
					if (cellId.toString().equals(id)) {
						cellStatusMap.put(cellId, Integer.valueOf(status));
					}
				}
			}
		}
	}

	/**
	 * �Ի�վ��������
	 * 
	 * @author tiance
	 * 
	 */
	private static class EnbComparator implements Comparator<Enb> {

		private int sortBy;

		public EnbComparator(int sortBy) {
			this.sortBy = sortBy;
		}

		@Override
		public int compare(Enb enb1, Enb enb2) {
			int order = sortBy > 0 ? 1 : -1;

			switch (Math.abs(sortBy)) {
			case EnbCondition.SORT_BY_ENB_ID: {
				// ��ID
				return order * compareNumLong(enb1.getEnbId(), enb2.getEnbId());
			}
			case EnbCondition.SORT_BY_ENB_NAME: {
				// ������
				return order
						* enb1.getName().toLowerCase()
								.compareTo(enb2.getName().toLowerCase());
			}
			case EnbCondition.SORT_BY_STATE: {
				// ��ע��״̬
				int s1 = enb1.getRegisterState();
				int s2 = enb2.getRegisterState();

				if (!enb1.isActive()) {
					s1 = -1;
				}
				if (!enb2.isActive()) {
					s2 = -1;
				}

				return order * compareNum(s1, s2);
			}
			case EnbCondition.SORT_BY_MANAGE_STATE: {
				// ������ģʽ
				int ms1 = enb1.getManageStateCode();
				int ms2 = enb2.getManageStateCode();

				if (ms1 == 0 && ms2 == 0)
					return 0;
				if (ms1 == 0)
					return order * -1;
				if (ms2 == 0)
					return order * 1;

				return order * compareNum(ms1, ms2);
			}
			case EnbCondition.SORT_BY_PUBLIC_IP: {
				// ��public ip
				return order
						* compareIP(enb1.getPublicIp(), enb2.getPublicIp());
			}
			case EnbCondition.SORT_BY_PUBLIC_PORT: {
				// ���˿ں�
				return order
						* compareNum(enb1.getPublicPort(), enb2.getPublicPort());
			}
			case EnbCondition.SORT_BY_SOFTWARE_VERSION: {
				return order
						* compareVersion(enb1.getSoftwareVersion(),
								enb2.getSoftwareVersion());
			}
			case EnbCondition.SORT_BY_ALARM_LEVEL: {
				return order
						* compareNum(enb1.getAlarmLevel(), enb2.getAlarmLevel());
			}
			case EnbCondition.SORT_BY_PRIVATE_IP: {
				// ��private ip
				return order
						* compareIP(enb1.getPrivateIp(), enb2.getPrivateIp());
			}
			case EnbCondition.SORT_BY_PROTOCOL_VERSION: {
				// ��Э��汾��
				return order
						* compareIP(enb1.getProtocolVersion(),
								enb2.getProtocolVersion());
			}
			case EnbCondition.SORT_BY_MONITOR_STATE: {
				// �����״̬
				return order
						* (enb1.getMonitorState() - enb2.getMonitorState());
			}
			default:
				return 0;
			}
		}

		/**
		 * ����������
		 * 
		 * @param n1
		 * @param n2
		 * @return
		 */
		private int compareNum(int n1, int n2) {
			if (n1 > n2)
				return 1;
			else if (n1 < n2)
				return -1;
			else
				return 0;
		}

		/**
		 * ����������
		 * 
		 * @param n1
		 * @param n2
		 * @return
		 */
		private int compareNumLong(long n1, long n2) {
			if (n1 > n2)
				return 1;
			else if (n1 < n2)
				return -1;
			else
				return 0;
		}

		private int isVersionBlank(String v1, String v2) {
			v1 = v1 == null || v1.equalsIgnoreCase("null") ? "" : v1;
			v2 = v2 == null || v2.equalsIgnoreCase("null") ? "" : v2;

			if (StringUtils.isBlank(v1) && StringUtils.isBlank(v2))
				return 0;
			if (StringUtils.isBlank(v1))
				return 1;
			if (StringUtils.isBlank(v2))
				return -1;

			return 2;
		}

		private int compareIP(String ip1, String ip2) {
			return this.compareVersion(ip1, ip2);
		}

		/**
		 * ���汾����
		 * 
		 * @param ver1
		 * @param ver2
		 * @return
		 */
		private int compareVersion(String ver1, String ver2) {

			int compare = isVersionBlank(ver1, ver2);
			if (compare != 2) {
				return -1 * compare;
			}

			ver1 = ver1.trim();
			ver2 = ver2.trim();

			String[] v1 = ver1.split("\\.");
			String[] v2 = ver2.split("\\.");

			for (int i = 0; i < v1.length; i++) {
				String num1 = v1[i];
				String num2 = v2[i];
				int result = num1.compareTo(num2);
				if (result < 0) {
					return -1;
				}
				if (result > 0) {
					return 1;
				}
			}
			return 0;
		}

	}

	/**
	 * ɸѡ��ʾ�б�
	 * 
	 * @param currentPage
	 * @param everyPageNumber
	 * @param allEnbs
	 * @return
	 */
	private List<Enb> currentList(int currentPage, int everyPageNumber,
			List<Enb> allEnbs) {
		List<Enb> enbList = new ArrayList<Enb>();
		for (int number = 0; number < everyPageNumber; number++) {
			int index = ((currentPage - 1) * everyPageNumber) + number;
			if (index < allEnbs.size()) {
				enbList.add(allEnbs.get(index));
			} else {
				break;
			}
		}
		return enbList;
	}

	public void setEnbBasicDAO(EnbBasicDAO enbBasicDAO) {
		this.enbBasicDAO = enbBasicDAO;
	}

	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	public void setEnbBizConfigDAO(EnbBizConfigDAO enbBizConfigDAO) {
		this.enbBizConfigDAO = enbBizConfigDAO;
	}

	public void setAlarmDAO(AlarmDAO alarmDAO) {
		this.alarmDAO = alarmDAO;
	}

	public void setEnbBizConfigService(EnbBizConfigService enbBizConfigService) {
		this.enbBizConfigService = enbBizConfigService;
	}

	public void setEnbBizTemplateService(
			EnbBizTemplateService enbBizTemplateService) {
		this.enbBizTemplateService = enbBizTemplateService;
	}

	public void setEnbGlobalConfigService(
			EnbGlobalConfigService enbGlobalConfigService) {
		this.enbGlobalConfigService = enbGlobalConfigService;
	}

	public void setEnbNeighbourService(EnbNeighbourService enbNeighbourService) {
		this.enbNeighbourService = enbNeighbourService;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

}
