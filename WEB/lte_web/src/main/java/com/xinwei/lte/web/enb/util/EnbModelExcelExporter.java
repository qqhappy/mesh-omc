/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-1	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.util;

import java.util.LinkedList;
import java.util.List;

import com.xinwei.lte.web.enb.util.poi.ModelExcelExporter;
import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbTypeDD;

/**
 * eNB模型导出助手实现类
 * 
 * @author fanhaoyu
 * 
 */

public class EnbModelExcelExporter extends ModelExcelExporter<Enb> {

	public EnbModelExcelExporter(String targetSheetName) {
		super(targetSheetName);
	}

	public static final String SHEET_NAME_ENB = "eNB";

	public static final String[] COLUMN_NAMES = { "eNB ID", "eNB名称", "eNB类型","协议版本号",
			"管理状态", "基站注册时数据同步方向" };
//	public static final String[] COLUMN_NAMES = { "eNB ID", "eNB名称", "协议版本号",
//		"管理状态", "基站注册时数据同步方向" };
	@Override
	public List<List<String>> getRowDataList(List<Enb> enbList) {
		List<List<String>> rowDataList = new LinkedList<List<String>>();
		for (Enb enb : enbList) {
			List<String> rowData = new LinkedList<String>();

			rowData.add(enb.getHexEnbId());
			rowData.add(enb.getName());
			if(EnbTypeDD.XW7400==enb.getEnbType()){
				rowData.add("XW7400");
			}else{
				rowData.add("XW7102");
			}
			rowData.add(enb.getProtocolVersion());
			rowData.add(getManageState(enb.getManageStateCode()));
			rowData.add(getSyncDirection(enb.getSyncDirection()));

			rowDataList.add(rowData);
		}
		return rowDataList;
	}

	private String getSyncDirection(int syncDirection) {
		if (syncDirection == Enb.SYNC_DIRECTION_EMS_TO_NE) {
			return "网管到基站";
		} else if (syncDirection == Enb.SYNC_DIRECTION_NE_TO_EMS) {
			return "基站到网管";
		} else {
			return "";
		}
	}

	private String getManageState(int manageState) {
		if (manageState == ManageState.ONLINE_STATE) {
			return "在线管理";
		} else if (manageState == ManageState.OFFLINE_STATE) {
			return "离线管理";
		} else {
			return "";
		}
	}

}
