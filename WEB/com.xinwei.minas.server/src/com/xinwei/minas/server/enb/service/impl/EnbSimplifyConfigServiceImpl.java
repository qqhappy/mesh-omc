/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbNeighbourRecord;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.dao.EnbBizConfigDAO;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.service.EnbBizConfigService;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbNeighbourService;
import com.xinwei.minas.server.enb.service.EnbSimplifyConfigService;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidatorRegistry;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.core.utils.ReflectUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB简易配置服务接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbSimplifyConfigServiceImpl implements EnbSimplifyConfigService {

	private Log log = LogFactory.getLog(EnbSimplifyConfigServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private EnbBizConfigDAO enbBizConfigDAO;

	private EnbBizConfigService enbBizConfigService;

	private EnbNeighbourService enbNeighbourService;

	private void doTransaction(final String methodName, final Object... args)
			throws Exception {

		Exception exception = transactionTemplate
				.execute(new TransactionCallback<Exception>() {
					@Override
					public Exception doInTransaction(TransactionStatus txStatus) {
						try {
							Method method = ReflectUtils.findMethod(
									EnbSimplifyConfigServiceImpl.class,
									methodName);
							method.invoke(EnbSimplifyConfigServiceImpl.this,
									args);
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
	public void addBoard(long moId, XBizRecord boardRecord, Integer fiberPort)
			throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		synchronized (enb) {
			doTransaction("doAddBoard", moId, boardRecord, fiberPort);
		}
	}

	public void doAddBoard(long moId, XBizRecord boardRecord, Integer fiberPort)
			throws Exception {
		// 如果是RRU单板，需要校验光口号是否被占用
		int boardType = getBoardType(boardRecord);
		if (boardType == EnbConstantUtils.BOARD_TYPE_RRU) {
			XBizRecord topoRecord = generateTopoRecord(boardRecord, fiberPort);

			EnbBizDataValidator validator = EnbBizDataValidatorRegistry
					.getInstance().getValidator(
							EnbConstantUtils.TABLE_NAME_T_TOPO);
			validator.validate(moId, topoRecord, ActionTypeDD.MODIFY);
			// 添加单板表记录
			enbBizConfigService.add(moId, EnbConstantUtils.TABLE_NAME_T_BOARD,
					boardRecord);
			// 添加拓扑表记录
			enbBizConfigService.add(moId, EnbConstantUtils.TABLE_NAME_T_TOPO,
					topoRecord);
		} else {
			// 添加单板表记录
			enbBizConfigService.add(moId, EnbConstantUtils.TABLE_NAME_T_BOARD,
					boardRecord);
		}

	}

	@Override
	public void updateBoard(long moId, XBizRecord boardRecord, Integer fiberPort)
			throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		synchronized (enb) {
			doTransaction("doUpdateBoaard", moId, boardRecord, fiberPort);
		}
	}

	public void doUpdateBoaard(long moId, XBizRecord boardRecord,
			Integer fiberPort) throws Exception {
		// 更新单板表记录
		enbBizConfigService.update(moId, EnbConstantUtils.TABLE_NAME_T_BOARD,
				boardRecord);
		// 获取单板记录相关联的拓扑表记录
		XBizRecord topoRecord = getRelatedTopoRecord(moId, boardRecord);
		if (topoRecord != null) {
			// 更新光口号
			topoRecord.addField(new XBizField(
					EnbConstantUtils.FIELD_NAME_FIBER_PORT, fiberPort
							.toString()));
			// 更新拓扑表记录
			enbBizConfigService.update(moId,
					EnbConstantUtils.TABLE_NAME_T_TOPO, topoRecord);
		}
	}

	@Override
	public void deleteBoard(long moId, XBizRecord boardBizKey) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		synchronized (enb) {
			doTransaction("doDeleteBoard", moId, boardBizKey);
		}
	}

	public void doDeleteBoard(long moId, XBizRecord boardBizKey)
			throws Exception {
		XBizRecord boardRecord = enbBizConfigDAO.queryByKey(moId,
				EnbConstantUtils.TABLE_NAME_T_BOARD, boardBizKey);
		// 判断要删除的记录是否存在
		if (boardRecord == null) {
			throw new Exception(OmpAppContext.getMessage("record_not_exist"));
		}
		// 获取单板记录相关联的拓扑表记录
		XBizRecord topoRecord = getRelatedTopoRecord(moId, boardRecord);
		if (topoRecord != null) {
			// 删除拓扑表记录
			XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId,
					EnbConstantUtils.TABLE_NAME_T_TOPO, topoRecord);
			enbBizConfigService.delete(moId,
					EnbConstantUtils.TABLE_NAME_T_TOPO, bizKey);
		}
		// 删除单板表记录
		enbBizConfigService.delete(moId, EnbConstantUtils.TABLE_NAME_T_BOARD,
				boardBizKey);
	}

	@Override
	public void updateCellPara(final long moId, final XBizRecord cellParaRecord)
			throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, cellParaRecord);
		XBizRecord oldCellPara = enbBizConfigDAO.queryByKey(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, bizKey);

		// 更新配置
		enbBizConfigService.update(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, cellParaRecord);

		if (shouldSyncCellPara(oldCellPara, cellParaRecord)) {
			// 如果物理小区标识、跟踪区码或中心载频改变，需要向该小区的所有邻小区发送邻区关系配置指令并更新网管记录
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						enbNeighbourService.syncCellConfigToAll(moId,
								cellParaRecord);
					} catch (Exception e) {
						log.warn("sync cell config to all neighbours failed.",
								e);
					}
				}
			});
		}
	}

	/**
	 * 是否需要做同步
	 * 
	 * @param oldCellPara
	 * @param newCellPara
	 * @return
	 */
	private boolean shouldSyncCellPara(XBizRecord oldCellPara,
			XBizRecord newCellPara) {
		XBizField pciField1 = oldCellPara
				.getFieldBy(EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
		XBizField tacField1 = oldCellPara
				.getFieldBy(EnbConstantUtils.FIELD_NAME_TAC);
		XBizField freqField1 = oldCellPara
				.getFieldBy(EnbConstantUtils.FIELD_NAME_CENTER_FREQ);

		XBizField pciField2 = newCellPara
				.getFieldBy(EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
		XBizField tacField2 = newCellPara
				.getFieldBy(EnbConstantUtils.FIELD_NAME_TAC);
		XBizField freqField2 = newCellPara
				.getFieldBy(EnbConstantUtils.FIELD_NAME_CENTER_FREQ);
		if (!pciField1.equals(pciField2))
			return true;
		if (!tacField1.equals(tacField2))
			return true;
		if (!freqField1.equals(freqField2))
			return true;
		return false;
	}

	/**
	 * 查询单板对应的从板拓扑表记录
	 * 
	 * @param moId
	 * @param boardRecord
	 * @return
	 * @throws Exception
	 */
	private XBizRecord getRelatedTopoRecord(long moId, XBizRecord boardRecord)
			throws Exception {
		XBizRecord condition = new XBizRecord();
		XBizField rackNo = boardRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_RACKNO);
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SRACKNO,
				rackNo.getValue()));

		XBizField shelfNo = boardRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SHELFNO);
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SSHELFNO,
				shelfNo.getValue()));

		XBizField slotNo = boardRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SLOTNO);
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SSLOTNO,
				slotNo.getValue()));
		XBizTable bizTable = enbBizConfigDAO.query(moId,
				EnbConstantUtils.TABLE_NAME_T_TOPO, condition);
		if (EnbBizHelper.hasRecord(bizTable)) {
			return bizTable.getRecords().get(0);
		}
		return null;
	}

	/**
	 * 生成拓扑表记录
	 * 
	 * @param boardRecord
	 * @param fiberPort
	 * @return
	 */
	private XBizRecord generateTopoRecord(XBizRecord boardRecord,
			Integer fiberPort) {
		XBizRecord topoRecord = new XBizRecord();
		topoRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_MRACKNO,
				"1"));
		topoRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_MSHELFNO,
				"1"));
		topoRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_MSLOTNO,
				"1"));
		topoRecord.addField(new XBizField(
				EnbConstantUtils.FIELD_NAME_FIBER_PORT, fiberPort.toString()));

		XBizField rackNo = boardRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_RACKNO);
		topoRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SRACKNO,
				rackNo.getValue()));

		XBizField shelfNo = boardRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SHELFNO);
		topoRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SSHELFNO,
				shelfNo.getValue()));

		XBizField slotNo = boardRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SLOTNO);
		topoRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SSLOTNO,
				slotNo.getValue()));
		// TODO 默认设置拓扑号为从机架号减2
		Integer topoNo = Integer.valueOf(rackNo.getValue()) - 2;
		topoRecord.addField(new XBizField(EnbConstantUtils.FIELD_NAME_TOPO_NO,
				topoNo.toString()));
		return topoRecord;
	}

	/**
	 * 获取单板类型
	 * 
	 * @param boardRecord
	 * @return
	 */
	private int getBoardType(XBizRecord boardRecord) {
		XBizField field = boardRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_BDTYPE);
		return Integer.valueOf(field.getValue());
	}

	@Override
	public void deleteCellPara(long moId, final XBizRecord cellParaRecord)
			throws Exception {
		final Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		doTransaction("doDeleteCell", enb, cellParaRecord);
	}

	public void doDeleteCell(Enb enb, XBizRecord cellParaRecord)
			throws Exception {

		long moId = enb.getMoId();

		XBizField cellIdField = cellParaRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
		List<String> tableNames = EnbBizHelper.getCellRelatedTables(
				enb.getEnbType(), enb.getProtocolVersion());
		// 先删除小区相关表的记录
		for (String tableName : tableNames) {
			try {
				XBizRecord bizKey = new XBizRecord();
				if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_DRX)
						|| tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_SPS)
						|| tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_ADMIT)
						|| tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_DRX)) {
					bizKey.addField(new XBizField(
							EnbConstantUtils.FIELD_NAME_DRX_CELL_ID,
							cellIdField.getValue()));
				} else {
					bizKey.addField(cellIdField);
				}
				// SI调度参数表
				if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_SISCH)) {
					// 查找当前小区的记录
					XBizTable siTable = enbBizConfigDAO.query(moId, tableName,
							bizKey);
					if (EnbBizHelper.hasRecord(siTable)) {
						for (XBizRecord siRecord : siTable.getRecords()) {
							XBizRecord siKey = EnbBizHelper.getKeyRecordBy(
									enb.getEnbType(), enb.getProtocolVersion(),
									tableName, siRecord);
							enbBizConfigService.delete(moId, tableName, siKey);
						}
					}
				} else if (tableName
						.equals(EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL)) {
					// 邻区关系参数表需要特殊处理
					List<EnbNeighbourRecord> records = enbNeighbourService
							.queryNeighbourRecords(moId);
					if (records != null) {
						for (EnbNeighbourRecord enbNeighbourRecord : records) {
							XBizField srvCidField = enbNeighbourRecord
									.getBizRecord()
									.getFieldBy(
											EnbConstantUtils.FIELD_NAME_SVR_CID);
							if (srvCidField.getValue().equals(
									cellIdField.getValue())) {
								enbNeighbourService.deleteNeighbour(moId,
										enbNeighbourRecord);
							}
						}
					}
					continue;
				} else {
					// 查询库里的数据，按照网管数据删
					XBizTable bizTable = enbBizConfigDAO.query(moId, tableName,
							bizKey);
					if (EnbBizHelper.hasRecord(bizTable)) {
						enbBizConfigService.delete(moId, tableName, bizKey);
					}
				}
			} catch (Exception e) {
				XList tableConfig = EnbBizHelper.getBizMetaBy(moId, tableName);
				String tableDesc = tableConfig.getDesc();
				throw new Exception(tableDesc + ":" + e.getLocalizedMessage());
			}
		}
		// 再删除小区记录
		enbBizConfigService.delete(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, cellParaRecord);

	}

	public void setEnbBizConfigDAO(EnbBizConfigDAO enbBizConfigDAO) {
		this.enbBizConfigDAO = enbBizConfigDAO;
	}

	public void setEnbBizConfigService(EnbBizConfigService enbBizConfigService) {
		this.enbBizConfigService = enbBizConfigService;
	}

	public void setEnbNeighbourService(EnbNeighbourService enbNeighbourService) {
		this.enbNeighbourService = enbNeighbourService;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

}
