/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-3	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.xinwei.lte.web.enb.util.poi.ModelExcelImporter;
import com.xinwei.lte.web.enb.util.poi.RowData;
import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbTypeDD;

/**
 * 
 * eNB模型导入器类
 * 
 * @author fanhaoyu
 * 
 */

public class EnbModelExcelImporter extends ModelExcelImporter<Enb> {

	public static final String SHEET_NAME_ENB = "eNB";

	public static final String[] COLUMN_NAMES = { "eNB ID", "eNB名称","eNB类型", "协议版本号",
			"管理状态", "基站注册时数据同步方向" };

	public EnbModelExcelImporter(HSSFWorkbook workbook, String targetSheetName)
			throws Exception {
		super(workbook, targetSheetName);
	}

	@Override
	public List<Enb> getModelList(List<RowData> rowDataList) {
		List<Enb> enbList = new ArrayList<Enb>();
		for (RowData rowData : rowDataList) {
			Enb enb = new Enb();
			enb.setEnbId(Long.valueOf(rowData.getColumnValue(COLUMN_NAMES[0]),
					16));
			enb.setName(rowData.getColumnValue(COLUMN_NAMES[1]));
			String typeName=rowData.getColumnValue(COLUMN_NAMES[2]);
			if("XW7400".equals(typeName)){
				enb.setEnbType(EnbTypeDD.XW7400);
			}else{
				enb.setEnbType(EnbTypeDD.XW7102);
			}
			enb.setProtocolVersion(rowData.getColumnValue(COLUMN_NAMES[3]));
			String manageState = rowData.getColumnValue(COLUMN_NAMES[4]);
			enb.setManageStateCode(getManageState(manageState));
			String syncDirection = rowData.getColumnValue(COLUMN_NAMES[5]);
			enb.setSyncDirection(getSyncDirection(syncDirection));
			enbList.add(enb);
		}
		return enbList;
	}

	private int getSyncDirection(String syncDirection) {
		if (syncDirection.equals("网管到基站")) {
			return Enb.SYNC_DIRECTION_EMS_TO_NE;
		} else if (syncDirection.equals("基站到网管")) {
			return Enb.SYNC_DIRECTION_NE_TO_EMS;
		} else {
			return -1;
		}
	}

	private int getManageState(String manageState) {
		if (manageState.equals("在线管理")) {
			return ManageState.ONLINE_STATE;
		} else if (manageState.equals("离线管理")) {
			return ManageState.OFFLINE_STATE;
		} else {
			return ManageState.UNKNOWN_STATE;
		}
	}

	@Override
	protected void checkRowData(RowData rowData) throws Exception {
		String hexEnbId = rowData.getColumnValue(COLUMN_NAMES[0]);
		if (hexEnbId.length() != 8 || !hexEnbId.matches("[0-9a-fA-F]+")) {
			throw new Exception("eNB ID必须为8位16进制数. 行号:" + rowData.getRowIndex());
		}
		String enbName = rowData.getColumnValue(COLUMN_NAMES[1]);
		if (enbName.length() > 80
				|| !enbName.matches("[0-9a-zA-Z\\u4e00-\\u9fa5_]+")) {
			throw new Exception("eNB名称必须为汉字、数字、字母或下划线的组合,且长度不超过80个字符. 行号:"
					+ rowData.getRowIndex());
		}
		String typeName=rowData.getColumnValue(COLUMN_NAMES[2]);
		if(!"XW7400".equals(typeName) && !"XW7102".equals(typeName)){
			throw new Exception("eNB类型必须为XW7400或者XW7102. 行号:"
					+ rowData.getRowIndex());
		}
		String protocolVersion = rowData.getColumnValue(COLUMN_NAMES[3]);
		if (!protocolVersion.matches("[0-9]{1,2}.[0-9]{1,2}.[0-9]{1,2}")) {
			throw new Exception("协议版本号的格式必须为:[*.*.*]. 行号:"
					+ rowData.getRowIndex());
		}
		int manageState = getManageState(rowData
				.getColumnValue(COLUMN_NAMES[4]));
		if (manageState == ManageState.UNKNOWN_STATE) {
			throw new Exception("管理状态必须为在线管理或离线管理. 行号:" + rowData.getRowIndex());
		}
		int syncDirection = getSyncDirection(rowData
				.getColumnValue(COLUMN_NAMES[5]));
		if (syncDirection != Enb.SYNC_DIRECTION_EMS_TO_NE
				&& syncDirection != Enb.SYNC_DIRECTION_NE_TO_EMS) {
			throw new Exception("基站注册时数据同步方向必须为基站到网管或网管到基站. 行号:"
					+ rowData.getRowIndex());
		}
	}

}
