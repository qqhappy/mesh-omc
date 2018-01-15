/*      						
 /*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-8-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.opensymphony.xwork2.ActionContext;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbStatBizFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.enb.core.utils.EnbStatConstants;
import com.xinwei.minas.xstat.core.model.PreStatItem;
import com.xinwei.minas.xstat.core.model.StatConstants;
import com.xinwei.minas.xstat.core.model.StatDataQueryCondition;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.utils.DateUtils;
import com.xinwei.system.action.web.WebConstants;

/**
 * 
 * 话务统计性能数据excel助手
 * 
 * @author fanhaoyu
 * 
 */

public class StatDataExcelUtil {

	private static final Log log = LogFactory.getLog(StatDataExcelUtil.class);

	private enum Unit {
		UNIT_PERC, UNIT_ONCE, UNIT_ENTRY, UNIT_KBIT, UNIT_MS, UNIT_KBITPS, UNIT_BYTE, UNIT_PERC100
	}

	/**
	 * 导出SAG群组数据到excel文件
	 * 
	 * @throws IOException
	 */
	public void exportStatData(List<PreStatItem> itemList,
			ServletOutputStream outPutStream, int interval,
			List<String> columnList) throws Exception {
		System.out.println("write file startTime " + (new Date()).toString());
		// 创建workBook
		HSSFWorkbook workBook = new HSSFWorkbook();
		if (itemList != null && !itemList.isEmpty()) {
			Map<Long, List<PreStatItem>> itemMap = groupByMoId(itemList);
			// 创建sheets，每个基站的数据占一个sheet
			for (Long moId : itemMap.keySet()) {
				Enb enb = queryEnb(moId);
				if (enb == null) {
					// System.out.println(moId);
					// log.error("enb is null. moId=" + moId);
					continue;
				}
				String sheetname = enb.getHexEnbId();
				// 创建工作簿
				HSSFSheet sheet = workBook.createSheet(sheetname);
				int rowCount = sheet.getLastRowNum();
				// 创建标题行
				HSSFRow row = sheet.createRow(rowCount++);
				fillRow(row, columnList);
				List<PreStatItem> subItemList = itemMap.get(moId);
				List<List<PreStatItem>> timegroupList = groupByTime(subItemList);
				// Map<Long, List<PreStatItem>> subitemMap =
				// groupByTime(subItemList);
				Map<Integer, String> cellmap = queryCellMap(moId);
				// 先按小区ID排序，再按时间排序
				// for (PreStatItem item : subItemList) {
				// List<String> valueList = getValueList(enb, item, cellmap,
				// interval);
				// HSSFRow valueRow = sheet.createRow(rowCount++);
				// fillRow(valueRow, valueList);
				// }
				for (List<PreStatItem> onetimeList : timegroupList) {
					{
						List<List<PreStatItem>> cellIdgroupList = new ArrayList<List<PreStatItem>>();
						if (onetimeList.get(0).getEntityType()
								.contains(EnbStatConstants.STAT_OBJECT_CELL)) {
							cellIdgroupList = groupByCellId(onetimeList);
						} else {
							cellIdgroupList.add(onetimeList);
						}
						List<List<String>> allList = getValueList(enb,
								cellIdgroupList, cellmap, interval, columnList);
						for (List<String> valueList : allList) {
							HSSFRow valueRow = sheet.createRow(rowCount++);
							fillRow(valueRow, valueList);
						}

					}

				}
				setColumnAutoSize(sheet);
			}
		} else {
			// 当无数据的时候，创建一个新的sheet
			workBook.createSheet();
		}
		// 将workBook写进输出流

		workBook.write(outPutStream);
		if (outPutStream != null) {
			outPutStream.close();
		}

		System.out.println("write file endTime " + (new Date()).toString());
	}

	public List<List<String>> getValueList(Enb enb,
			List<List<PreStatItem>> cellIdgroupList,
			Map<Integer, String> cellmap, int interval, List<String> columnList)
			throws Exception {
		List<List<String>> allList = new ArrayList<List<String>>();
		List<String> valueList = new ArrayList<String>();
		int[] itemIDarray;
		int offeset;
		DecimalFormat df = new DecimalFormat("#.0000");
		// String enbName = enb.getHexEnbId() + "(" + enb.getName() + ")";

		// 小区ID

		for (List<PreStatItem> showList : cellIdgroupList) {
			String enbID = enb.getHexEnbId().toString();
			valueList.add(enbID);
			String enbName = enb.getName().toString();
			// hexEnbId(基站名称)
			valueList.add(enbName);
			if (showList.get(0).getEntityType()
					.contains(EnbStatConstants.STAT_OBJECT_CELL)) {
				offeset = 5;
				itemIDarray = new int[columnList.size() - offeset];

				String cellId = showList.get(0).getEntityOid()
						.split("\\" + StatConstants.POINT)[1];
				valueList.add(cellId);
				valueList.add(cellmap.get(Integer.parseInt(cellId)));
			} else {
				offeset = 3;
				itemIDarray = new int[columnList.size() - offeset];
				// valueList.add("");
				// valueList.add("");
			}
			List<PreStatItem> orderList = orderByitemId(showList);
			long itemtime = DateUtils.getMillisecondTimeFromBriefTime(orderList
					.get(0).getStatTime());
			if (interval == StatDataQueryCondition.INTERVAL_FIFTEEN_MINUTES) {
				itemtime = itemtime + 15 * 60 * 1000;
				valueList.add(DateUtils.getStandardTimeFromBriefTime(DateUtils
						.getBriefTimeFromMillisecondTime(itemtime)));
			} else if (interval == StatDataQueryCondition.INTERVAL_ONE_HOUR) {
				itemtime = itemtime + 45 * 60 * 1000;
				valueList.add(DateUtils.getStandardTimeFromBriefTime(DateUtils
						.getBriefTimeFromMillisecondTime(itemtime)));
			} else {
				itemtime = itemtime + (24 - 8) * 60 * 60 * 1000 - 15 * 60
						* 1000;
				valueList.add(DateUtils.getStandardTimeFromBriefTime(DateUtils
						.getBriefTimeFromMillisecondTime(itemtime)));
			}
			int i = 0;
			for (PreStatItem item : orderList) {
				// valueList.add(String.valueOf(item.getItemId()));
				// // 统计项名称
				// valueList
				// .add("("
				// + String.valueOf(item.getItemId())
				// + ")"
				// + getItemName(item.getItemId(),
				// item.getItemType()));
				// 统计项的值
				itemIDarray = getitemIDarray(columnList, offeset);
				for (; i < itemIDarray.length; i++) {
					String value = "";
					if (item.getItemId() != itemIDarray[i]) {
						valueList.add("");
					} else {
						Unit unit = Unit.valueOf(getConfigUnit(
								item.getItemType(), item.getItemId()));
						switch (unit) {
						case UNIT_PERC:
							value = df.format(item.getStatValue());
							break;
						case UNIT_PERC100:
							value = df.format(item.getStatValue() * 100);
							break;
						case UNIT_ONCE:
						case UNIT_ENTRY:
						case UNIT_KBITPS:
						case UNIT_KBIT:
						case UNIT_MS:
						case UNIT_BYTE:
							if (item.getStatValue() != 0) {
								value = df.format(item.getStatValue()).split(
										"\\.")[0];
							} else {
								value = "0";
							}
							break;
						}
						if (value.equals(".0000")) {
							value = "0";
						}
						valueList.add(value);
						i++;
						break;
					}
				}

			}
			List<String> arrayList = new ArrayList<String>();
			arrayList.addAll(valueList);
			allList.add(arrayList);
			valueList.clear();// 清空临时列表
		}
		return allList;
	}

	public int[] getitemIDarray(List<String> columnList, int offeset) {
		int[] array = new int[columnList.size() - offeset];
		int j = 0;
		for (int i = offeset; i < columnList.size(); i++) {
			int start = columnList.get(i).indexOf("(");
			int end = columnList.get(i).indexOf(")");
			array[j] = Integer.parseInt(columnList.get(i).substring(start + 1,
					end));
			j++;
		}
		return array;
	}

	class SortByItemId implements Comparator<PreStatItem> {
		@Override
		public int compare(PreStatItem item1, PreStatItem item2) {
			return item1.getItemId() - item2.getItemId();
		}
	}

	private Enb queryEnb(long moId) throws Exception {
		// 获取facade
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
		EnbBasicFacade facade = MinasSession.getInstance().getFacade(sessionId,
				EnbBasicFacade.class);
		return facade.queryByMoId(moId);
	}

	private void fillRow(HSSFRow row, List<String> valueList) {
		for (int i = 0; i < valueList.size(); i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(valueList.get(i));
		}
	}

	/**
	 * 获取统计项的单位
	 * 
	 * @param type
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	public String getConfigUnit(String type, int itemId) throws Exception {
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
		EnbStatBizFacade facade = MinasSession.getInstance().getFacade(
				sessionId, EnbStatBizFacade.class);
		List<CounterItemConfig> counterItemConfig = facade
				.queryCounterConfigs();
		List<KpiItemConfig> kpiItemConfig = facade.queryKpiConfigs();
		String unit = "";
		if (type.equalsIgnoreCase("Counter")) {
			for (CounterItemConfig cItemconfig : counterItemConfig)
				if (cItemconfig.getCounterId() == itemId) {
					unit = cItemconfig.getUnit();
					return unit;
				}

		} else {
			for (KpiItemConfig kItemconfig : kpiItemConfig)
				if (kItemconfig.getKpiId() == itemId) {
					unit = kItemconfig.getUnit();
					return unit;
				}
		}

		return unit;
	}

	/**
	 * 将数据按照moId分组
	 * 
	 * @param itemList
	 * @return
	 */
	private Map<Long, List<PreStatItem>> groupByMoId(List<PreStatItem> itemList) {
		// 按照moId分组
		Map<Long, List<PreStatItem>> itemMap = new HashMap<Long, List<PreStatItem>>();
		for (PreStatItem item : itemList) {
			long moId = item.getMoId();
			List<PreStatItem> list = itemMap.get(moId);
			if (list == null) {
				list = new ArrayList<PreStatItem>();
				itemMap.put(moId, list);
			}
			list.add(item);
		}
		return itemMap;
	}

	/**
	 * 按时间分组
	 * 
	 * @param itemList
	 * @return
	 */
	private List<List<PreStatItem>> groupByTime(List<PreStatItem> itemList) {
		List<List<PreStatItem>> timegroupList = new ArrayList<List<PreStatItem>>();
		List<PreStatItem> onetimeList = new ArrayList<PreStatItem>();
		List<PreStatItem> orderItemList = orderBytime(itemList);
		if (orderItemList != null && orderItemList.size() > 0) {
			long time = orderItemList.get(0).getStatTime();
			for (int i = 0; i < orderItemList.size(); i++) {
				if (orderItemList.get(i).getStatTime() == time) {
					onetimeList.add(orderItemList.get(i));
				} else {
					List<PreStatItem> groupList = new ArrayList<PreStatItem>();
					groupList.addAll(onetimeList);
					timegroupList.add(groupList);
					onetimeList.clear();// 清空临时列表
					time = orderItemList.get(i).getStatTime();
					onetimeList.add(orderItemList.get(i));
				}
			}
			if (onetimeList != null && onetimeList.size() > 0) {
				timegroupList.add(onetimeList);
			}
		}
		return timegroupList;
	}

	/**
	 * 按小区分组
	 * 
	 * @param itemList
	 * @return
	 */
	private List<List<PreStatItem>> groupByCellId(List<PreStatItem> itemList) {
		List<List<PreStatItem>> cellIdgroupList = new ArrayList<List<PreStatItem>>();
		List<PreStatItem> statItemList = new ArrayList<PreStatItem>();
		Map<String, List<PreStatItem>> cellIdgroup = new HashMap<String, List<PreStatItem>>();
		if (itemList != null && itemList.size() > 0) {
			for (int i = 0; i < itemList.size(); i++) {
				String cellId = itemList.get(i).getEntityOid()
						.split("\\" + StatConstants.POINT)[1];
				if (!cellIdgroup.containsKey(cellId)) {
					List<PreStatItem> arrayList = new ArrayList<PreStatItem>();
					arrayList.add(itemList.get(i));
					cellIdgroup.put(cellId, arrayList);
				} else
					cellIdgroup.get(cellId).add(itemList.get(i));
			}

		}
		Iterator iterator = cellIdgroup.keySet().iterator();
		while (iterator.hasNext()) {
			String cellId = (String) iterator.next();
			cellIdgroupList.add(cellIdgroup.get(cellId));
		}

		return cellIdgroupList;
	}

	/**
	 * 创建一个新的扩展名为.xls的excel文件
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public static void createXlsFileIfNotExist(String filePath)
			throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
			FileOutputStream fout = null;
			try {
				HSSFWorkbook workbook = new HSSFWorkbook();
				fout = new FileOutputStream(file);
				workbook.createSheet("sheet1");
				workbook.write(fout);
				fout.flush();
			} catch (IOException e) {
				throw e;
			} finally {
				if (fout != null) {
					fout.close();
				}
			}
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * 设置自动调整列宽
	 * 
	 * @param sheet
	 */
	private void setColumnAutoSize(HSSFSheet sheet) {
		int maxColumnIndex = 0;
		if (!isSheetEmpty(sheet)) {
			int columnIndex = 0;
			@SuppressWarnings("rawtypes")
			Iterator rowIterator = sheet.rowIterator();
			HSSFRow row = null;
			while (rowIterator.hasNext()) {
				row = (HSSFRow) rowIterator.next();
				columnIndex = row.getLastCellNum();
				if (columnIndex > maxColumnIndex)
					maxColumnIndex = columnIndex;
			}
		}
		for (int i = 0; i < maxColumnIndex; i++)
			sheet.autoSizeColumn(i);
	}

	/**
	 * 判断工作簿是否为空
	 * 
	 * @param sheet
	 * @return
	 */
	private boolean isSheetEmpty(HSSFSheet sheet) {
		if (sheet.rowIterator().hasNext())
			return false;
		return true;
	}

	// 查询小区ID和小区名称的Map
	private Map<Integer, String> queryCellMap(Long moId) throws Exception {
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
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

	/**
	 * 将列表中的数据按照itemid升序排列
	 * 
	 * @param itemList
	 * @return
	 */
	private List<PreStatItem> orderByitemId(List<PreStatItem> itemList) {
		ComparePreStatItemId compare = new ComparePreStatItemId();
		if (itemList != null && itemList.size() > 0) {
			Collections.sort(itemList, compare);// 按照compare中的规则排序
		}
		return itemList;
	}

	private List<PreStatItem> orderBytime(List<PreStatItem> itemList) {
		ComparePreStatItemtime compare = new ComparePreStatItemtime();
		if (itemList != null && itemList.size() > 0) {
			Collections.sort(itemList, compare);
		}
		return itemList;
	}

	private class ComparePreStatItemId implements Comparator<PreStatItem> {

		public int compare(PreStatItem item1, PreStatItem item2) {
			long flag = item1.getItemId() - item2.getItemId();
			if (flag == 0) {
				return 0;
			} else if (flag > 0) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	private class ComparePreStatItemtime implements Comparator<PreStatItem> {
		public int compare(PreStatItem item1, PreStatItem item2) {
			long flag = item1.getStatTime() - item2.getStatTime();
			if (flag == 0) {
				return 0;
			} else if (flag > 0) {
				return 1;
			} else {
				return -1;
			}
		}
	}
}
