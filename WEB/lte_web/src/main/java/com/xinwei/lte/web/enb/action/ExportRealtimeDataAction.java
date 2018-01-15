/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.cache.EnbRealTimeDataCache;
import com.xinwei.lte.web.enb.cache.EnbRealtimeItemConfigCache;
import com.xinwei.lte.web.enb.util.EnbBizHelper;
import com.xinwei.lte.web.enb.util.EnbRealtimeDataExporter;
import com.xinwei.lte.web.enb.util.StatDataUtil;
import com.xinwei.lte.web.enb.util.poi.PoiUtil;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.xstat.core.model.StatConstants;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.utils.DateUtils;
import com.xinwei.system.action.web.WebConstants;

/**
 * 
 * 导出实时性能数据Action
 * 
 * @author fanhaoyu
 * 
 */

public class ExportRealtimeDataAction extends ActionSupport {

	public static final int TIME_FLAG_ONE_MINUTE = 1;

	public static final int TIME_FLAG_FIVE_MINUTE = 5;

	public static final int TIME_FLAG_FIFTEEN_MINUTE = 15;

	private long moId;

	// 要导出最近多久的数据,可选项为1,5,15分钟
	private int timeFlag;

	/**
	 * 统计项ID列表，以","分割
	 */
	private String itemIds;

	/**
	 * 小区ID列表，以","分割
	 */
	private String cellIds;

	private String errorMessage;

	public String exportRealtimeData() {
		try {
			// 监控的小区列表
			List<Integer> cellIdList = turnToList(cellIds);
			// 监控的统计项列表
			List<Integer> itemIdList = turnToList(itemIds);
			// 查询数据
			int secondOffset = getTimeCondition(timeFlag);
			List<EnbRealtimeItemData> dataList = queryExportingData(moId,
					secondOffset, itemIdList);
//			for(EnbRealtimeItemData data : dataList){
//				double valueToShow = StatDataUtil.getValueToShow(
//						data.getItemId(),
//						data.getStatValue());
//				if(valueToShow == data.getStatValue())
//					continue;
//				else
//					 data.setStatValue(valueToShow);
//			}
			// 将数据按小区ID分组
			Map<Integer, List<EnbRealtimeItemData>> dataMap = organizeData(
					cellIdList, dataList);
			Enb enb = queryEnb(moId);
			// 查询小区信息
			Map<Integer, String> cellMap = queryCellMap();

			List<String> columnList = getOrderedColumnName(itemIdList);

			// 导出数据
			HSSFWorkbook workBook = exportData(cellIdList, columnList, dataMap,
					cellMap);

			Long time = DateUtils.getBriefTimeFromMillisecondTime(System
					.currentTimeMillis());

			String fileName = "eNBRealtimeData-" + enb.getHexEnbId() + "-"
					+ time.toString() + PoiUtil.POST_FIX;
			ServletOutputStream outputStream = null;
			try {
				HttpServletResponse response = ServletActionContext
						.getResponse();
				outputStream = response.getOutputStream();
				response.setContentType("Application/msexcel;charset=utf-8");
				response.setHeader("Content-disposition",
						"attachment;filename=" + fileName);
				workBook.write(outputStream);
			} catch (Exception e) {
				errorMessage = e.getLocalizedMessage();
			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
			}

		} catch (Exception e) {
			errorMessage = e.getLocalizedMessage();
		}
		return NONE;
	}

	/**
	 * 将数据按小区ID分组
	 * 
	 * @param cellIdList
	 * @param dataList
	 * @return
	 */
	private Map<Integer, List<EnbRealtimeItemData>> organizeData(
			List<Integer> cellIdList, List<EnbRealtimeItemData> dataList) {
		// 按要查询的小区ID分组
		Map<Integer, List<EnbRealtimeItemData>> dataMap = new LinkedHashMap<Integer, List<EnbRealtimeItemData>>();
		for (int cellId : cellIdList) {
			for (EnbRealtimeItemData itemData : dataList) {
				int dataCellId = Integer.valueOf(itemData.getEntityOid().split(
						"\\" + StatConstants.POINT)[1]);
				if (dataCellId == cellId) {
					List<EnbRealtimeItemData> list = dataMap.get(cellId);
					if (list == null) {
						list = new LinkedList<EnbRealtimeItemData>();
						dataMap.put(cellId, list);
					}
					list.add(itemData);
				}
			}
		}
		return dataMap;
	}

	/**
	 * 查询数据
	 * 
	 * @param itemIdList
	 * @return
	 */
	private List<EnbRealtimeItemData> queryExportingData(long moId,
			int secondOffset, List<Integer> itemIdList) {
		List<EnbRealtimeItemData> dataList = new LinkedList<EnbRealtimeItemData>();
		
		// 查询数据
		for (Integer itemId : itemIdList) {
			List<EnbRealtimeItemData> list = EnbRealTimeDataCache.getInstance()
					.queryLatestData(moId, itemId, secondOffset);
			
			if (list == null || list.isEmpty())
				continue;
			dataList.addAll(list);
		}
		return dataList;
	}

	/**
	 * 导出数据
	 * 
	 * @param cellIdList
	 * @param dataMap
	 * @param cellMap
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private HSSFWorkbook exportData(List<Integer> cellIdList,
			List<String> columnList,
			Map<Integer, List<EnbRealtimeItemData>> dataMap,
			Map<Integer, String> cellMap) throws IOException, Exception {
		// 每个小区一个sheet
		HSSFWorkbook workBook = PoiUtil.createXlsWorkbook();

		for (int i = 0; i < cellIdList.size(); i++) {
			boolean append = true;
			if (i == 0)
				append = false;
			int cellId = cellIdList.get(i);
			String targetSheetName = cellMap.get(cellId) + "(" + cellId + ")";
			// sheet名是小区名（小区ID），列名：时间、帧号、统计项名i（单位）....
			EnbRealtimeDataExporter exporter = new EnbRealtimeDataExporter(
					workBook, targetSheetName, append);
			exporter.setColumnList(columnList);
			List<EnbRealtimeItemData> modelList = dataMap.get(cellId);
			workBook = exporter.exportModelList(modelList);
		}
		return workBook;
	}

	/**
	 * 获取要导出的列名
	 * 
	 * @param itemIdList
	 * @return
	 */
	private List<String> getOrderedColumnName(List<Integer> itemIdList) {

		Collections.sort(itemIdList);

		EnbRealtimeItemConfigCache configCache = EnbRealtimeItemConfigCache
				.getInstance();

		List<String> columns = new ArrayList<String>();

		Collections.addAll(columns, EnbRealtimeDataExporter.COLUMN_NAMES);

		for (Integer itemId : itemIdList) {
			EnbRealtimeItemConfig itemConfig = configCache.getConfig(itemId);
			String itemName = itemConfig.getName_zh();
			String unitString = StatDataUtil
					.getUnitToShow(itemConfig.getUnit());
			if (unitString != "") {
				columns.add(itemName + "(" + unitString + ")");
			} else {
				columns.add(itemName);
			}
		}
		return columns;
	}

	private Enb queryEnb(long moId) throws Exception {
		String sessionId = getSessionId();
		EnbBasicFacade facade = MinasSession.getInstance().getFacade(sessionId,
				EnbBasicFacade.class);
		return facade.queryByMoId(moId);
	}

	/**
	 * 查询小区ID和小区名称的Map
	 * 
	 * @return
	 * @throws Exception
	 */
	private Map<Integer, String> queryCellMap() throws Exception {
		String sessionId = getSessionId();
		XEnbBizConfigFacade facade = MinasSession.getInstance().getFacade(
				sessionId, XEnbBizConfigFacade.class);
		XBizTable bizTable = facade.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null, false);
		if (EnbBizHelper.hasRecord(bizTable)) {
			Map<Integer, String> cellMap = new HashMap<Integer, String>();
			for (XBizRecord bizRecord : bizTable.getRecords()) {
				XBizField cellId = bizRecord
						.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
				XBizField cellName = bizRecord
						.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_NAME);
				cellMap.put(Integer.valueOf(cellId.getValue()),
						cellName.getValue());
			}
			return cellMap;
		} else {
			return Collections.emptyMap();
		}
	}

	private String getSessionId() {
		return ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
	}

	/**
	 * 根据查询时间标识获取查询时间条件
	 * 
	 * @param timeFlag
	 * @return
	 */
	private int getTimeCondition(int timeFlag) {
		switch (timeFlag) {
		case TIME_FLAG_ONE_MINUTE:
			return 60;
		case TIME_FLAG_FIVE_MINUTE:
			return 300;
		case TIME_FLAG_FIFTEEN_MINUTE:
			return 900;
		default:
			return 900;
		}
	}

	/**
	 * 获取字符串中的数字转换为list
	 * 
	 * @param str
	 * @return
	 */
	private List<Integer> turnToList(String str) {
		List<Integer> list = new ArrayList<Integer>();
		String[] strArray = str.split(",");
		if (strArray.length > 0) {
			for (int i = 0; i < strArray.length; i++) {
				if (strArray[i].matches("\\d+")) {
					list.add(Integer.parseInt(strArray[i]));
				}
			}
		}
		return list;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public int getTimeFlag() {
		return timeFlag;
	}

	public void setTimeFlag(int timeFlag) {
		this.timeFlag = timeFlag;
	}

	public String getItemIds() {
		return itemIds;
	}

	public void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}

	public String getCellIds() {
		return cellIds;
	}

	public void setCellIds(String cellIds) {
		this.cellIds = cellIds;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
