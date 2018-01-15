/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

import com.xinwei.minas.mcbts.core.facade.common.MicroBtsSignalSendSettingFacade;
import com.xinwei.minas.mcbts.core.model.common.MicroBtsSignalSendSetting;
import com.xinwei.minas.server.mcbts.service.McBtsBizService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.GenericBizRecord;

/**
 * 
 * 小基站信号发送方式门面实现
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class MicroBtsSignalSendSettingFacadeImpl extends UnicastRemoteObject
		implements MicroBtsSignalSendSettingFacade {

	private McBtsBizService service;

	protected MicroBtsSignalSendSettingFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public MicroBtsSignalSendSetting queryByMoId(Long moId)
			throws RemoteException, Exception {
		service = AppContext.getCtx().getBean(McBtsBizService.class);
		GenericBizData data = service.queryAllBy(moId, MICRO_SIGNAL_BIZ);
		MicroBtsSignalSendSetting setting = null;
		if (!data.getRecords().isEmpty()) {
			GenericBizRecord record = data.getRecords().get(0);
			setting = convertGenericBizRecordToSetting(record);
			setting.setMoId(moId);
		}
		return setting;
	}

	@Override
	public void config(MicroBtsSignalSendSetting setting)
			throws RemoteException, Exception {

		service = AppContext.getCtx().getBean(McBtsBizService.class);
		GenericBizData data = convertSettingToGenericBizRecord(setting);
		service.config(setting.getMoId(), data);
	}

	@Override
	public MicroBtsSignalSendSetting queryFromEMS(Long moId)
			throws RemoteException, Exception {
		return queryByMoId(moId);
	}

	@Override
	public MicroBtsSignalSendSetting queryFromNE(Long moId)
			throws RemoteException, Exception {
		service = AppContext.getCtx().getBean(McBtsBizService.class);
		GenericBizData data = service.queryFromNE(moId, new GenericBizData(
				MICRO_SIGNAL_BIZ));
		MicroBtsSignalSendSetting setting = null;
		if (!data.getRecords().isEmpty()) {
			GenericBizRecord record = data.getRecords().get(0);
			setting = convertGenericBizRecordToSetting(record);
			setting.setMoId(moId);
		}
		return setting;
	}

	public static MicroBtsSignalSendSetting convertGenericBizRecordToSetting(
			GenericBizRecord record) {
		MicroBtsSignalSendSetting setting = new MicroBtsSignalSendSetting();
		GenericBizProperty property = record.getPropertyValue("idx");
		if (property != null) {
			setting.setIdx(Long.parseLong(property.getValue().toString()));
		}
		property = record.getPropertyValue("sendMode");
		setting.setSendMode(Integer.parseInt(property.getValue().toString()));
		property = record.getPropertyValue("antIndex");
		setting.setAntIndex(Integer.parseInt(property.getValue().toString()));
		return setting;
	}

	public static GenericBizData convertSettingToGenericBizRecord(
			MicroBtsSignalSendSetting setting) {

		if (setting == null) {
			return null;
		}

		GenericBizData data = new GenericBizData(MICRO_SIGNAL_BIZ);
		if (setting.getIdx() != null) {
			data.addProperty(new GenericBizProperty("idx", setting.getIdx()));
		}
		data.addProperty(new GenericBizProperty("sendMode", setting
				.getSendMode()));
		data.addProperty(new GenericBizProperty("antIndex", setting
				.getAntIndex()));
		Set<String> primaryKeys = new HashSet<String>();
		primaryKeys.add("idx");
		data.setPrimaryKeys(primaryKeys);
		return data;
	}

}
