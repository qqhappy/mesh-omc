/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbNeighbourRecord;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.dao.EnbNeighbourRelationDAO;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.model.EnbNeighbourRelation;
import com.xinwei.minas.server.enb.service.EnbBizConfigService;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbNeighbourService;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.utils.ReflectUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB�������÷���ʵ��
 * 
 * @author fanhaoyu
 * 
 */

public class EnbNeighbourServiceImpl implements EnbNeighbourService {

	private TransactionTemplate transactionTemplate;

	private EnbBizConfigService enbBizConfigService;

	private EnbNeighbourRelationDAO enbNeighbourRelationDAO;

	private SequenceService sequenceService;

	private int maxNeighbour = 32;

	@Override
	public List<EnbNeighbourRecord> queryNeighbourRecords(long moId)
			throws Exception {
		XBizTable bizTable = enbBizConfigService.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, null, false);
		if (!EnbBizHelper.hasRecord(bizTable)) {
			return Collections.emptyList();
		}
		List<EnbNeighbourRecord> records = new LinkedList<EnbNeighbourRecord>();
		// ��ѯ������ϵ
		List<EnbNeighbourRelation> relations = enbNeighbourRelationDAO
				.queryRelation(moId, null, null, null);
		for (XBizRecord bizRecord : bizTable.getRecords()) {
			EnbNeighbourRecord record = new EnbNeighbourRecord();
			record.setBizRecord(bizRecord);
			// ����С��ID
			XBizField svrCidField = bizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SVR_CID);
			// ����eNB ID
			XBizField enbIdField = bizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_ENBID);
			// ����ID
			XBizField nbrCidField = bizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_CID);
			int srvCellId = Integer.valueOf(svrCidField.getValue());
			long enbId = Long.valueOf(enbIdField.getValue());
			int cellId = Integer.valueOf(nbrCidField.getValue());
			int isNeighbour = getIsNeighbour(srvCellId, enbId, cellId,
					relations);
			record.setIsNeighbour(isNeighbour);
			records.add(record);
		}
		return records;
	}

	@Override
	public EnbNeighbourRecord queryNeighbourRecord(long moId,
			XBizRecord condition) throws Exception {
		XBizTable bizTable = enbBizConfigService.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, condition, false);
		if (!EnbBizHelper.hasRecord(bizTable)) {
			return null;
		}
		XBizRecord bizRecord = bizTable.getRecords().get(0);
		// ����С��ID
		XBizField svrCidField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SVR_CID);
		// ����eNB ID
		XBizField enbIdField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_ENBID);
		// ����ID
		XBizField nbrCidField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_CID);
		int srvCellId = Integer.valueOf(svrCidField.getValue());
		long enbId = Long.valueOf(enbIdField.getValue());
		int cellId = Integer.valueOf(nbrCidField.getValue());
		// ��ѯ������ϵ
		List<EnbNeighbourRelation> relations = enbNeighbourRelationDAO
				.queryRelation(moId, srvCellId, enbId, cellId);
		int isNeighbour = getIsNeighbour(srvCellId, enbId, cellId, relations);

		EnbNeighbourRecord record = new EnbNeighbourRecord();
		record.setBizRecord(bizRecord);
		record.setIsNeighbour(isNeighbour);
		return record;
	}

	private void doTransaction(final String methodName, final Object... args)
			throws Exception {

		Exception exception = transactionTemplate
				.execute(new TransactionCallback<Exception>() {
					@Override
					public Exception doInTransaction(TransactionStatus txStatus) {
						try {
							Method method = ReflectUtils.findMethod(
									EnbNeighbourServiceImpl.class, methodName);
							method.invoke(EnbNeighbourServiceImpl.this, args);
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
	public void addNeighbour(long moId, EnbNeighbourRecord record)
			throws Exception {

		// ÿ��С������������õ�������������Ϊ32��
		doTransaction("doAddNeighbour", moId, record);
	}

	public void doAddNeighbour(long moId, EnbNeighbourRecord record)
			throws Exception {

		XBizRecord bizRecord = record.getBizRecord();
		// ����С��ID
		XBizField svrCidField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SVR_CID);
		// �жϷ���С�������������Ƿ񳬹�����
		boolean over = checkOverMaxNeighbour(moId, svrCidField.getValue(),
				maxNeighbour);
		if (over) {
			throw new Exception(
					OmpAppContext.getMessage("srv_cell_neighbour_reach_max")
							+ maxNeighbour);
		}

		// �ж��Ƿ��Լ��������
		// ����С������enb
		Enb svrEnb = EnbCache.getInstance().queryByMoId(moId);
		// ��������eNB
		XBizField nbrEnbIdField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_ENBID);
		// ��С��ID
		XBizField nbrCidField = bizRecord.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_CID);
		long svrEnbId = svrEnb.getEnbId();
		long nbrEnbId = Long.valueOf(nbrEnbIdField.getValue());
		int svrCid = Integer.valueOf(svrCidField.getValue());
		int nbrCid = Integer.valueOf(nbrCidField.getValue());
		if ( svrEnbId == nbrEnbId
				&& svrCid == nbrCid ) {
			throw new Exception(
					OmpAppContext.getMessage("cannot_add_nbrself"));
		}
		// ����¼��ӵ���ǰeNB������ϵ����
		enbBizConfigService.add(moId, EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL,
				bizRecord);
		boolean isNeighbour = record.getIsNeighbour() == EnbNeighbourRelation.IS_NEIGHBOUR;
		
		Enb nbrEnb = EnbCache.getInstance().queryByEnbId(nbrEnbId);
		// ���eNB�ڱ�ϵͳ�д��ڣ���Ҫ
		if (nbrEnb != null && isNeighbour) {
			addNeighbourRecord(moId, nbrEnb, bizRecord);

			// ���˫��������ϵ��¼
			EnbNeighbourRelation relation = generateRelation(moId, record,
					false);
			EnbNeighbourRelation reverseRelation = generateRelation(moId,
					record, true);
			enbNeighbourRelationDAO.addRelation(relation);
			enbNeighbourRelationDAO.addRelation(reverseRelation);
		}
	}

	@Override
	public void updateNeighbour(long moId, EnbNeighbourRecord record)
			throws Exception {
		doTransaction("doUpdateNeighbour", moId, record);
	}

	public void doUpdateNeighbour(long moId, EnbNeighbourRecord record)
			throws Exception {

		XBizRecord bizRecord = record.getBizRecord();

		// ����¼��ӵ���ǰeNB������ϵ����
		enbBizConfigService.update(moId,
				EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, bizRecord);

		EnbNeighbourRelation oldRelation = queryRelation(moId, bizRecord);
		// ��������eNB
		XBizField enbIdField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_ENBID);
		long enbId = Long.valueOf(enbIdField.getValue());
		Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
		if (enb != null) {
			boolean isNeighbour = record.getIsNeighbour() == EnbNeighbourRelation.IS_NEIGHBOUR;
			if (!isNeighbourBefore(oldRelation) && isNeighbour) {
				// �����ǰ�Ƿ���������Ϊ��Ϊ��������Ҫ�ڶԶ�������ϵ������Ӽ�¼
				addNeighbourRecord(moId, enb, bizRecord);
				// ���������ϵ��¼
				EnbNeighbourRelation relation = generateRelation(moId, record,
						false);
				EnbNeighbourRelation reverseRelation = generateRelation(moId,
						record, true);
				enbNeighbourRelationDAO.addRelation(relation);
				enbNeighbourRelationDAO.addRelation(reverseRelation);

			} else if (isNeighbourBefore(oldRelation) && !isNeighbour) {
				// �����ǰ��Ϊ��������Ϊ����������Ҫ���Զ�������ϵ���¼ɾ��
				deleteNeighbourRecord(moId, enb, bizRecord);
				// ɾ��������ϵ
				deleteRelation(moId, enb, bizRecord);
			}
		}

	}

	/**
	 * �޸�֮ǰ�Ƿ�Ϊ����
	 * 
	 * @param oldRelation
	 * @return
	 */
	private boolean isNeighbourBefore(EnbNeighbourRelation oldRelation) {
		if (oldRelation == null)
			return false;
		return oldRelation.isNeighbour();
	}

	@Override
	public void deleteNeighbour(long moId, EnbNeighbourRecord record)
			throws Exception {
		doTransaction("doDeleteNeighbour", moId, record);
	}

	public void doDeleteNeighbour(long moId, EnbNeighbourRecord record)
			throws Exception {
		XBizRecord bizRecord = record.getBizRecord();
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId,
				EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, bizRecord);
		XBizTable nbrcelTable = enbBizConfigService.queryFromEms(moId, EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, bizKey);
		if(null == nbrcelTable.getRecords() || nbrcelTable.getRecords().size() < 1) {
			return;
		}
		XBizRecord nbrcelRecord = nbrcelTable.getRecords().get(0);
		// �ж������Ƿ���Ա�ɾ��
		int u8NoRemove = nbrcelRecord.getIntValue("u8NoRemove");
		if(1 == u8NoRemove) {
			throw new BizException(OmpAppContext.getMessage("nbrcel_cannot_delete"));
		}
		
		enbBizConfigService.delete(moId,
				EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, bizKey);

		// ��������eNB
		XBizField nbrEnbIdField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_ENBID);
		long nbrEnbId = Long.valueOf(nbrEnbIdField.getValue());
		Enb nbrEnb = EnbCache.getInstance().queryByEnbId(nbrEnbId);
		if (nbrEnb != null) {

			EnbNeighbourRelation relation = queryRelation(moId, bizRecord);
			if (isNeighbourBefore(relation)) {
				// �����Ϊ��������Ҫɾ���Զ˼�¼
				// ɾ���Զ�������ϵ��¼
				deleteNeighbourRecord(moId, nbrEnb, bizRecord);
				// ɾ��˫��������ϵ
				deleteRelation(moId, nbrEnb, bizRecord);
			}

		}

	}

	@Override
	public void syncCellConfigToAll(long moId, XBizRecord cellParaRecord)
			throws Exception {
		Enb currentEnb = EnbCache.getInstance().queryByMoId(moId);
		if (currentEnb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		// ��ѯ�޸�С������������
		String srvCid = cellParaRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_CELL_ID).getValue();
		XBizRecord condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SVR_CID,
				srvCid));
		XBizTable bizTable = enbBizConfigService.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, condition, false);
		if (EnbBizHelper.hasRecord(bizTable)) {
			StringBuilder msgBuilder = new StringBuilder();
			// ����������¼
			for (XBizRecord bizRecord : bizTable.getRecords()) {
				// ����eNB ID
				XBizField nbrEnbIdField = bizRecord
						.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_ENBID);
				long nbrEnbId = Long.valueOf(nbrEnbIdField.getValue());
				Enb nbrEnb = EnbCache.getInstance().queryByEnbId(nbrEnbId);
				// ����ID
				String nbrCid = bizRecord.getFieldBy(
						EnbConstantUtils.FIELD_NAME_NBR_CID).getValue();
				if (nbrEnb != null) {
					// �����Ϊ��������Ҫ���¶Զ˼�¼�е�����С����ʶ���������롢����Ƶ��
					EnbNeighbourRelation relation = queryRelation(
							currentEnb.getMoId(), bizRecord);
					if (isNeighbourBefore(relation)) {
						try {
							syncCellConfigToNeighbour(currentEnb,
									nbrEnb.getMoId(), srvCid, nbrCid,
									cellParaRecord);
						} catch (Exception e) {
							msgBuilder
									.append("sync cell config to neighbour failed. eNB ID=")
									.append(nbrEnb.getHexEnbId());
						}
					}
				}
			}
			if (msgBuilder.length() > 0) {
				throw new BizException(msgBuilder.toString());
			}
		}
		// ���²��ǻ�Ϊ������С������
		// 1����ѯ���л�վ
		List<Enb> enbList = EnbCache.getInstance().queryAll();
		// 2�������ѯ����
		XBizRecord conditionAll = new XBizRecord();
		conditionAll.addField(new XBizField(EnbConstantUtils.FIELD_NAME_NBR_ENBID,
				String.valueOf(currentEnb.getEnbId())));
		conditionAll.addField(new XBizField(
				EnbConstantUtils.FIELD_NAME_NBR_CID, srvCid));
		// 3���������л�վȥ��ѯ
		for (Enb enb : enbList) {
			XBizTable bizTable2 = enbBizConfigService.queryFromEms(
					enb.getMoId(), EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL,
					conditionAll,false);
			if (EnbBizHelper.hasRecord(bizTable2)) {
				// 4����������
				for (XBizRecord bizRecord : bizTable2.getRecords()) {
					XBizField pciField = cellParaRecord
							.getFieldBy(EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
					bizRecord.addField(pciField);
					XBizField taField = cellParaRecord
							.getFieldBy(EnbConstantUtils.FIELD_NAME_TAC);
					bizRecord.addField(taField);
					enbBizConfigService
							.update(enb.getMoId(),
									EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL,
									bizRecord);
				}
			}
		}
	}

	private void syncCellConfigToNeighbour(Enb currentEnb, long nbrEnbMoId,
			String srvCid, String nbrCid, XBizRecord cellParaRecord)
			throws Exception {
		XBizRecord nbrRecordToUpdate = queryNeighbourRecord(nbrEnbMoId, nbrCid,
				currentEnb.getEnbId().toString(), srvCid);
		if (nbrRecordToUpdate != null) {
			// TODO ����Ƶ����θ���?
			XBizField pciField = cellParaRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
			nbrRecordToUpdate.addField(pciField);
			XBizField taField = cellParaRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_TAC);
			nbrRecordToUpdate.addField(taField);
			enbBizConfigService
					.update(nbrEnbMoId,
							EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL,
							nbrRecordToUpdate);
		}

	}

	/**
	 * ��ѯ�Զ������������¼
	 * 
	 * @param moId
	 * @param srvCid
	 * @param enbId
	 * @param nbrCid
	 * @return
	 * @throws Exception
	 */
	private XBizRecord queryNeighbourRecord(long moId, String srvCid,
			String enbId, String nbrCid) throws Exception {
		XBizRecord condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SVR_CID,
				srvCid));
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_NBR_ENBID,
				enbId));
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_NBR_CID,
				nbrCid));

		XBizTable bizTable = enbBizConfigService.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, condition, false);
		if (EnbBizHelper.hasRecord(bizTable)) {
			return bizTable.getRecords().get(0);
		}
		return null;
	}

	/**
	 * ��ӶԶ�eNB�ڽӹ�ϵ���¼
	 * 
	 * @param moId
	 * @param nbrEnb
	 * @param bizRecord
	 * @throws Exception
	 */
	private void addNeighbourRecord(long moId, Enb nbrEnb, XBizRecord bizRecord)
			throws Exception {
		// �Զ�С��ID
		XBizField nbrCidField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_CID);
		// ��ѯ�Զ�С����Ӧ�ļ�¼
		XBizRecord nbrCellRecord = queryCellRecord(nbrEnb.getMoId(),
				nbrCidField.getValue());
		if (nbrCellRecord == null) {
			throw new BizException(EnbConstantUtils.FIELD_NAME_NBR_CID,
					OmpAppContext.getMessage("neighbour_cell_not_exists"));
		}
		// ����С��ID
		XBizField svrCidField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SVR_CID);

		// ��ѯ����С����Ӧ�ļ�¼
		XBizRecord srvCellRecord = queryCellRecord(moId, svrCidField.getValue());
		// �����Զ�eNB��������ϵ���¼
		XBizRecord newRecord = generateNeighbourRecord(moId, bizRecord,
				srvCellRecord);

		// ��ѯ�Զ��Ƿ����д˼�¼�����������£�û�������
		XBizRecord key = EnbBizHelper.getKeyRecordBy(nbrEnb.getEnbType(),
				nbrEnb.getProtocolVersion(),
				EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, newRecord);
		XBizTable nbrNbrTable = enbBizConfigService.queryFromEms(
				nbrEnb.getMoId(), EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL,
				key, false);
		if (EnbBizHelper.hasRecord(nbrNbrTable)) {
			enbBizConfigService.update(nbrEnb.getMoId(),
					EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, newRecord);
		} else {
			// �ж���С�������������Ƿ񳬹�����
			boolean over = checkOverMaxNeighbour(nbrEnb.getMoId(),
					nbrCidField.getValue(), maxNeighbour);
			if (over) {
				throw new Exception(
						OmpAppContext
								.getMessage("nbr_cell_neighbour_reach_max")
								+ maxNeighbour);
			}
			enbBizConfigService.add(nbrEnb.getMoId(),
					EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, newRecord);
		}

	}

	/**
	 * �ж��Ƿ񳬹������������
	 * 
	 * @param moId
	 * @param srvCid
	 * @throws Exception
	 */
	private boolean checkOverMaxNeighbour(long moId, String srvCid,
			int maxNeighbour) throws Exception {
		XBizRecord condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SVR_CID,
				srvCid));
		XBizTable bizTable = enbBizConfigService.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, condition, false);
		if (EnbBizHelper.hasRecord(bizTable)) {
			return bizTable.getRecords().size() >= maxNeighbour;
		}
		return false;
	}

	/**
	 * ɾ���Զ�eNB������ϵ���¼
	 * 
	 * @param moId
	 * @param nbrEnb
	 * @param bizRecord
	 * @throws Exception
	 */
	private void deleteNeighbourRecord(long moId, Enb nbrEnb,
			XBizRecord bizRecord) throws Exception {
		XBizRecord recordToDelete = generateNeighbourRecord(moId, bizRecord,
				null);
		XBizRecord key = EnbBizHelper.getKeyRecordBy(nbrEnb.getMoId(),
				EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, recordToDelete);
		enbBizConfigService.delete(nbrEnb.getMoId(),
				EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, key);
	}

	/**
	 * ��ȡ�Ƿ��������ı�ʶ
	 * 
	 * @param srvCellId
	 * @param enbId
	 * @param cellId
	 * @param relations
	 * @return
	 */
	private int getIsNeighbour(int srvCellId, long enbId, int cellId,
			List<EnbNeighbourRelation> relations) {
		if (relations == null || relations.isEmpty())
			return EnbNeighbourRelation.NOT_NEIGHBOUR;
		for (EnbNeighbourRelation relation : relations) {
			if (relation.getSrvCellId() == srvCellId
					&& relation.getEnbId() == enbId
					&& relation.getCellId() == cellId)
				return relation.getIsNeighbour();
		}
		return EnbNeighbourRelation.NOT_NEIGHBOUR;
	}

	/**
	 * ��ѯ������¼��Ӧ��������ϵ��¼
	 * 
	 * @param moId
	 * @param bizRecord
	 * @return
	 * @throws Exception
	 */
	private EnbNeighbourRelation queryRelation(long moId, XBizRecord bizRecord)
			throws Exception {
		// ����С��ID
		XBizField svrCidField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SVR_CID);
		// ����eNB ID
		XBizField enbIdField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_ENBID);
		// ����ID
		XBizField nbrCidField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_CID);
		int srvCellId = Integer.valueOf(svrCidField.getValue());
		long enbId = Long.valueOf(enbIdField.getValue());
		int cellId = Integer.valueOf(nbrCidField.getValue());

		List<EnbNeighbourRelation> relationsInDB = enbNeighbourRelationDAO
				.queryRelation(moId, srvCellId, enbId, cellId);
		if (relationsInDB == null || relationsInDB.isEmpty()) {
			return null;
		}
		return relationsInDB.get(0);
	}

	/**
	 * ɾ��������ϵ
	 * 
	 * @param moId
	 * @param enb
	 *            �Զ�eNB
	 * @param bizRecord
	 * @throws Exception
	 */
	private void deleteRelation(long moId, Enb enb, XBizRecord bizRecord)
			throws Exception {
		// �ж�������ϵ�Ƿ�����ɾ��
		
		
		// ����С��ID
		XBizField svrCidField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SVR_CID);
		// ����eNB ID
		XBizField enbIdField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_ENBID);
		// ����ID
		XBizField nbrCidField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_CID);
		int srvCellId = Integer.valueOf(svrCidField.getValue());
		long enbId = Long.valueOf(enbIdField.getValue());
		int cellId = Integer.valueOf(nbrCidField.getValue());

		Enb currentEnb = EnbCache.getInstance().queryByMoId(moId);

		enbNeighbourRelationDAO.deleteRelation(moId, srvCellId, enbId, cellId);
		enbNeighbourRelationDAO.deleteRelation(enb.getMoId(), cellId,
				currentEnb.getEnbId(), srvCellId);
	}

	/**
	 * ����������¼����������ϵ��¼
	 * 
	 * @param moId
	 * @param record
	 * @param isReverse
	 *            ���ɷ����ϵ
	 * @return
	 * @throws Exception
	 */
	private EnbNeighbourRelation generateRelation(long moId,
			EnbNeighbourRecord record, boolean isReverse) throws Exception {
		XBizRecord bizRecord = record.getBizRecord();
		// ����С��ID
		XBizField svrCidField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SVR_CID);
		// ����eNB ID
		XBizField nbrEnbIdField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_ENBID);
		// ����ID
		XBizField nbrCidField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_CID);
		int srvCellId = Integer.valueOf(svrCidField.getValue());
		long nbrEnbId = Long.valueOf(nbrEnbIdField.getValue());
		int nbrCid = Integer.valueOf(nbrCidField.getValue());
		if (isReverse) {
			Enb currentEnb = EnbCache.getInstance().queryByMoId(moId);
			Enb nbrEnb = EnbCache.getInstance().queryByEnbId(nbrEnbId);
			return generateRelation(nbrEnb.getMoId(), nbrCid,
					currentEnb.getEnbId(), srvCellId, record.getIsNeighbour());
		}
		return generateRelation(moId, srvCellId, nbrEnbId, nbrCid,
				record.getIsNeighbour());
	}

	private EnbNeighbourRelation generateRelation(long moId, int srvCellId,
			long enbId, int cellId, int isNeighbour) throws Exception {
		List<EnbNeighbourRelation> relationsInDB = enbNeighbourRelationDAO
				.queryRelation(moId, srvCellId, enbId, cellId);
		Long idx = null;
		if (relationsInDB == null || relationsInDB.isEmpty()) {
			idx = sequenceService.getNext();
		} else {
			idx = relationsInDB.get(0).getIdx();
		}
		EnbNeighbourRelation relation = new EnbNeighbourRelation();
		relation.setIdx(idx);
		relation.setMoId(moId);
		relation.setSrvCellId(srvCellId);
		relation.setEnbId(enbId);
		relation.setCellId(cellId);
		relation.setIsNeighbour(isNeighbour);
		return relation;
	}

	/**
	 * �����뵱ǰ������¼���Ӧ��eNB��������¼
	 * 
	 * @param moId
	 * @param nbrRecord
	 * @param srvCellRecord
	 * @return
	 */
	private XBizRecord generateNeighbourRecord(long moId, XBizRecord nbrRecord,
			XBizRecord srvCellRecord) {
		XBizRecord newRecord = nbrRecord.clone();
		// ����С��ID
		XBizField svrCidField = nbrRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SVR_CID);
		// ����ID
		XBizField nbrCidField = nbrRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_CID);
		// ��ǰ��¼�е�����ID��Ϊ����С��ID
		newRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SVR_CID,
				nbrCidField.getValue()));
		// ��ǰeNB��ID��Ϊ����eNBID
		Enb currentEnb = EnbCache.getInstance().queryByMoId(moId);
		newRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_NBR_ENBID,
				String.valueOf(currentEnb.getEnbId())));
		// ��ǰ��¼�еķ���С��ID��Ϊ����ID
		newRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_NBR_CID,
				svrCidField.getValue()));
		// �Զ�������ϵ��¼����Ҫ�������С��������
		if (srvCellRecord != null) {

			newRecord.addField(srvCellRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_MCC));
			newRecord.addField(srvCellRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_MNC));
			newRecord.addField(srvCellRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_PHY_CELL_ID));
			newRecord.addField(srvCellRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_TAC));
		}

		return newRecord;
	}

	/**
	 * ��ȡС���������¼
	 * 
	 * @param moId
	 * @param cellId
	 * @return
	 * @throws Exception
	 */
	private XBizRecord queryCellRecord(long moId, String cellId)
			throws Exception {
		XBizRecord condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_CELL_ID,
				cellId));
		XBizTable bizTable = enbBizConfigService.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, condition, false);
		if (EnbBizHelper.hasRecord(bizTable)) {
			return bizTable.getRecords().get(0);
		}
		return null;
	}

	public void setEnbBizConfigService(EnbBizConfigService enbBizConfigService) {
		this.enbBizConfigService = enbBizConfigService;
	}

	public void setEnbNeighbourRelationDAO(
			EnbNeighbourRelationDAO enbNeighbourRelationDAO) {
		this.enbNeighbourRelationDAO = enbNeighbourRelationDAO;
	}

	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

}
