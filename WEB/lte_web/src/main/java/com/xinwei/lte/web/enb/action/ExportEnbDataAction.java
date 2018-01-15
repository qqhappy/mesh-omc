package com.xinwei.lte.web.enb.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.util.EnbBizDataExcelExporter;
import com.xinwei.lte.web.enb.util.EnbModelExcelExporter;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbDataImportExportFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.utils.DateUtils;
import com.xinwei.system.action.web.WebConstants;

public class ExportEnbDataAction extends ActionSupport {

	private String error;

	private String protocolVersion;

	public String exportEnbData() {
		ServletOutputStream outputStream = null;
		try {
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			// 获取facade
			EnbDataImportExportFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, EnbDataImportExportFacade.class);

			Map<Enb, List<XBizTable>> dataMap = facade.exportEnbData();
			// 按enbId升序
			List<Enb> enbAllList = new LinkedList<Enb>(dataMap.keySet());
			List<Enb> enbList = new LinkedList<Enb>();
			for (Enb enb : enbAllList) {
				if (protocolVersion.equals(enb.getProtocolVersion())) {
					enbList.add(enb);
				}
			}
			// 获取指定协议版本的基站
			Collections.sort(enbList, new EnbCompartor());
			List<XBizTable> xBizTableList = new ArrayList<XBizTable>();
			// 遍历enb所有的XBizTable
			for (int i = 0; i < enbList.size(); i++) {
				xBizTableList.addAll(dataMap.get(enbList.get(i)));
			}
			// 去重，获取XBizTable的所有表名集合
			List<XBizTable> realList = listToOnly(xBizTableList);
			// 导出eNB列表
			EnbModelExcelExporter enbExporter = new EnbModelExcelExporter(
					EnbModelExcelExporter.SHEET_NAME_ENB);
			List<String> enbColumnList = new ArrayList<String>();
			Collections.addAll(enbColumnList,
					EnbModelExcelExporter.COLUMN_NAMES);
			enbExporter.setColumnList(enbColumnList);
			HSSFWorkbook workBook = enbExporter.exportModelList(enbList);

			// 遍历表名
			for (XBizTable XBizTable : realList) {
				// 导出表的数据，每一个表名占一个sheet，sheet名为表名
				String targetSheetName = XBizTable.getTableName();
				// 根据表名获取表列表
				Map<Enb, XBizTable> realMap = listToMapList(dataMap, enbList,
						targetSheetName);
				List sheetList = new LinkedList<Map>();
				sheetList.add(realMap);

				// 获取sheet的列名
				List<String> bizColumnList = getColumnNames(facade, realMap);

				EnbBizDataExcelExporter bizDataExporter = new EnbBizDataExcelExporter(
						workBook, targetSheetName, true);
				bizDataExporter.setColumnList(bizColumnList);
				bizDataExporter.exportModelList(sheetList);
			}

			// for (Enb enb : enbList) {
			// // 导出基站数据，每个基站的数据占一个sheet，sheet名为十六进制的eNB ID
			// String targetSheetName = enb.getHexEnbId();
			// List<XBizTable> bizTables = dataMap.get(enb);
			//
			// EnbBizDataExcelExporter bizDataExporter = new
			// EnbBizDataExcelExporter(
			// workBook, targetSheetName, true);
			// List<String> bizColumnList = new ArrayList<String>();
			// Collections.addAll(bizColumnList,
			// EnbBizDataExcelExporter.COLUMN_NAMES);
			// bizDataExporter.setColumnList(bizColumnList);
			// bizDataExporter.exportModelList(bizTables);
			// }
			long currentTimeMill = DateUtils
					.getBriefTimeFromMillisecondTime(System.currentTimeMillis());
			String currentTime = String.valueOf(currentTimeMill);
			// 文件名：eNBData-当前时间.xls
			String fileName = "eNBData-" + currentTime + ".xls";
			HttpServletResponse response = ServletActionContext.getResponse();
			outputStream = response.getOutputStream();
			response.setContentType("Application/msexcel;charset=utf-8");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileName);
			workBook.write(outputStream);

		} catch (Exception e) {
			error = e.getLocalizedMessage();
			return ERROR;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return NONE;
	}

	class EnbCompartor implements Comparator<Enb> {
		@Override
		public int compare(Enb enb1, Enb enb2) {
			long result = enb1.getEnbId() - enb2.getEnbId();
			if (result > 0)
				return 1;
			else if (result < 0)
				return -1;
			return 0;
		}

	}

	private List<String> getColumnNames(EnbDataImportExportFacade facade,
			Map<Enb, XBizTable> realMap) throws Exception {
		/*
		 * Enb enb=enbList.get(0); int enbTypeId=enb.getEnbType(); String
		 * protocolVersion=enb.getProtocolVersion(); String
		 * tableName=dataMap.get(enb).get(0).getTableName(); List<String>
		 * list=facade.getAllFieldNames(enbTypeId,protocolVersion,tableName);
		 */
		List<String> bizColumnList = new LinkedList<String>();
		List<String> list = new LinkedList<String>();
		Iterator<Map.Entry<Enb, XBizTable>> iterator = realMap.entrySet()
				.iterator();
		bizColumnList.add("eNB ID");
		if (iterator.hasNext()) {
			Entry<Enb, XBizTable> entry = iterator.next();
			Enb enb = entry.getKey();
			XBizTable xBizTable = entry.getValue();
			int enbTypeId = enb.getEnbType();
			String protocolVersion = enb.getProtocolVersion();
			String tableName = xBizTable.getTableName();
			list = facade.getAllFieldNames(enbTypeId, protocolVersion,
					tableName);
		}
		bizColumnList.addAll(list);
		return bizColumnList;
	}

	// 根据表名获取列表名
	public Map<Enb, XBizTable> listToMapList(Map<Enb, List<XBizTable>> dataMap,
			List<Enb> enbList, String tableName) {
		Map<Enb, XBizTable> map = new LinkedHashMap<Enb, XBizTable>();
		for (Enb enbr : enbList) {
			List<XBizTable> list = dataMap.get(enbr);
			List<XBizTable> tableList = new LinkedList<XBizTable>();
			for (XBizTable xBizTable : list) {
				if (tableName.equals(xBizTable.getTableName())) {
					map.put(enbr, xBizTable);
				}
			}

		}

		return map;

	}

	// 去重
	public List<XBizTable> listToOnly(List<XBizTable> list) {
		List<XBizTable> xBizTableList = new ArrayList<XBizTable>();
		boolean flag = true;
		for (XBizTable xBizTable : list) {
			String tableName = xBizTable.getTableName();
			for (XBizTable BizTable : xBizTableList) {
				if (BizTable.getTableName().equals(tableName)) {
					flag = false;
					break;
				} else {
					flag = true;
				}
			}
			if (flag) {
				xBizTableList.add(xBizTable);
			}

		}
		return xBizTableList;

	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

}
