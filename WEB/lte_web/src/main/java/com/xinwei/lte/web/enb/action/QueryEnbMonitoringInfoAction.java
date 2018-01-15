package com.xinwei.lte.web.enb.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.cache.EnbRealTimeDataCache;
import com.xinwei.lte.web.enb.cache.EnbRealtimeItemConfigCache;
import com.xinwei.lte.web.enb.model.EnbRealtimeItemConfigModel;
import com.xinwei.lte.web.enb.util.StatDataUtil;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbRealtimeMonitorFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbCondition;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.enb.core.utils.EnbStatConstants;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.utils.DateUtils;
import com.xinwei.system.action.web.WebConstants;

public class QueryEnbMonitoringInfoAction extends ActionSupport {

	/**
	 * 基站标识
	 */
	private long moId;

	/**
	 * 基站Id，16进制
	 */
	private String enbHexId;

	/**
	 * 操作异常
	 */
	private String manageError = "";

	/**
	 * 当前页
	 */
	private int currentPage;

	/**
	 * 排序条件
	 */
	private int sortBy;

	/**
	 * 菜单树
	 */
	private String menu;

	/**
	 * 小区列表信息(json串)
	 */
	private String cellList;

	/**
	 * 统计项ID列表，以","分割
	 */
	private String itemIds;

	/**
	 * 小区ID列表，以","分割
	 */
	private String cellIds;

	/**
	 * 界面传来的小区ID和小区名的对应信息
	 */
	private String cellIDToNames;

	/**
	 * 界面上的定时器传来的小区选择信息
	 */
	private String cellMap;

	/**
	 * 所有统计项的数据
	 */
	private String jsonData;

	/**
	 * 统计项
	 */
	private String itemData;

	/**
	 * 跳转至基站监控列表查询界面
	 * 
	 * @return
	 */
	public String turnToMonitoringInfoHtml() {
		return SUCCESS;
	}

	/**
	 * 查询基站监控列表
	 * 
	 * @return
	 */
	public String queryMonitoringEnbList() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		JSONObject json = new JSONObject();
		try {
			out = response.getWriter();
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);
			// 查询条件
			EnbCondition condition = new EnbCondition();
			if (currentPage == 0) {
				currentPage = 1;
			}
			condition.setCurrentPage(currentPage);
			condition.setNumPerPage(15);
			condition.setSortBy(sortBy);
			// 查询
			PagingData<Enb> pagingData = facade.queryAllByCondition(condition);
			// 成功
			json.put("status", 0);
			json.put("sortBy", sortBy);
			json.put("message", getPageDataJson(pagingData));
		} catch (Exception e) {
			// 失败
			json.put("status", 1);
			json.put("error", e.getLocalizedMessage());
		} finally {
			if (out != null) {
				out.println(json.toString());
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	/**
	 * 停止监控
	 * 
	 * @return
	 */
	public String stopMonitor() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		JSONObject json = new JSONObject();
		try {
			out = response.getWriter();
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbRealtimeMonitorFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, EnbRealtimeMonitorFacade.class);
			// 停止监控
			facade.stopMonitor(sessionId, moId);
		} catch (Exception e) {
			manageError = e.getLocalizedMessage();
		} finally {
			if (out != null) {
				json.put("error", manageError);
				out.println(json.toString());
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	/**
	 * 开始监控
	 * 
	 * @return
	 */
	public String beginMonitor() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		JSONObject json = new JSONObject();
		try {
			out = response.getWriter();
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();

			EnbRealtimeMonitorFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, EnbRealtimeMonitorFacade.class);
			// 开始监控
			facade.startMonitor(sessionId, moId);
		} catch (Exception e) {
			manageError = e.getLocalizedMessage();
		} finally {
			if (out != null) {
				json.put("error", manageError);
				out.println(json.toString());
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	/**
	 * 跳转至查看详细界面
	 * 
	 * @return
	 */
	public String toMonitorDetailhtml() {
		// 菜单树
		EnbRealtimeItemConfigCache cache = EnbRealtimeItemConfigCache
				.getInstance();
		Map<Integer, EnbRealtimeItemConfig> map = cache.getAllItemConfigs();
		List<EnbRealtimeItemConfigModel> list = getRealTimeItemMenu(map);
		menu = JSONArray.fromObject(list).toString();
		// 小区列表
		try {
			cellList = generateCellList(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 异步查询小区集
	 * 
	 * @return
	 */
	public String queryCellList() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			cellList = generateCellList(getCellCheckInfoMap(cellMap));
		} catch (Exception e) {

		} finally {
			if (out != null) {
				out.println(cellList);
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	/**
	 * 获取小区列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String generateCellList(Map<String, String> map) throws Exception {
		// 获取facade
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
		XEnbBizConfigFacade facade = MinasSession.getInstance().getFacade(
				sessionId, XEnbBizConfigFacade.class);
		// 查询数据
		XBizTable xBizTable = facade.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
		List<XBizRecord> records = xBizTable.getRecords();
		JSONArray array = new JSONArray();
		for (XBizRecord record : records) {
			JSONObject jb = new JSONObject();
			String cellId = record.getFieldBy(
					EnbConstantUtils.FIELD_NAME_CELL_ID).getValue();
			jb.put("cellId", cellId);
			jb.put("cellName",
					record.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_NAME)
							.getValue());
			if (map != null) {
				if (map.keySet().contains(cellId)) {
					jb.put("checked", map.get(cellId));
				} else {
					jb.put("checked", "");
				}
			} else {
				jb.put("checked", "true");
			}
			array.add(jb);
		}
		String cellList = JSONArray.fromObject(array).toString();
		return cellList;
	}

	/**
	 * 异步查询统计项的值
	 * 
	 * @return
	 */
	public String queryStatData() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		JSONObject json = new JSONObject();
		try {
			out = response.getWriter();
			// 生成图表内容
			generateChartContent();

			json.put("jsonData", jsonData);
			json.put("itemData", itemData);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.println(json.toString());
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	/**
	 * 把List<EnbRealtimeItemData>按照元素中的endTime按升序排序
	 * 
	 * @param list
	 */
	private void sort(List<EnbRealtimeItemData> list) {
		Collections.sort(list, new Comparator<EnbRealtimeItemData>() {
			public int compare(EnbRealtimeItemData arg0,
					EnbRealtimeItemData arg1) {
				return Long.valueOf(arg0.getEndTime()).compareTo(
						Long.valueOf(arg1.getEndTime()));
			}
		});
	}

	/**
	 * 构造菜单树
	 * 
	 * @param map
	 * @return
	 */
	private List<EnbRealtimeItemConfigModel> getRealTimeItemMenu(
			Map<Integer, EnbRealtimeItemConfig> map) {
		List<EnbRealtimeItemConfigModel> list = new LinkedList<EnbRealtimeItemConfigModel>();
		// 测量事件set
		Set<String> measureEvents = new HashSet<String>();
		// 测量子类型set
		Set<String> measureTypes = new HashSet<String>();
		// 上报子系统set
		Set<String> reportSystems = new HashSet<String>();
		// 测量对象set
		Set<String> statObjects = new HashSet<String>();
		// 放入所有菜单叶子节点
		for (int i : map.keySet()) {
			// 叶子节点model
			EnbRealtimeItemConfigModel model = new EnbRealtimeItemConfigModel();
			model.setId(String.valueOf(map.get(i).getItemId()));
			model.setpId(map.get(i).getMeasureEvent());
			model.setName(map.get(i).getName_zh());
			model.setTarget(String.valueOf(map.get(i).getItemId()));
			list.add(model);

			if (!measureEvents.contains(map.get(i).getMeasureEvent())) {
				EnbRealtimeItemConfigModel eventModel = new EnbRealtimeItemConfigModel();
				eventModel.setId(map.get(i).getMeasureEvent());
				eventModel.setpId(map.get(i).getMeasureType());
				eventModel.setName(getStatCnName().get(
						map.get(i).getMeasureEvent()));
				eventModel.setOpen(true);
				eventModel.setNocheck(true);
				list.add(eventModel);
				measureEvents.add(map.get(i).getMeasureEvent());
			}
			if (!measureTypes.contains(map.get(i).getMeasureType())) {
				EnbRealtimeItemConfigModel typeModel = new EnbRealtimeItemConfigModel();
				typeModel.setId(map.get(i).getMeasureType());
				typeModel.setpId(map.get(i).getReportSystem());
				typeModel.setName(getStatCnName().get(
						map.get(i).getMeasureType()));
				typeModel.setOpen(true);
				typeModel.setNocheck(true);
				list.add(typeModel);
				measureTypes.add(map.get(i).getMeasureType());
			}
			if (!reportSystems.contains(map.get(i).getReportSystem())) {
				EnbRealtimeItemConfigModel systemModel = new EnbRealtimeItemConfigModel();
				systemModel.setId(map.get(i).getReportSystem());
				systemModel.setpId(map.get(i).getStatObject());
				systemModel.setName(getStatCnName().get(
						map.get(i).getReportSystem()));
				systemModel.setOpen(true);
				systemModel.setNocheck(true);
				list.add(systemModel);
				reportSystems.add(map.get(i).getReportSystem());
			}
			if (!statObjects.contains(map.get(i).getStatObject())) {
				EnbRealtimeItemConfigModel objectModel = new EnbRealtimeItemConfigModel();
				objectModel.setId(map.get(i).getStatObject());
				objectModel.setpId("ROOT");
				objectModel.setName(getStatCnName().get(
						map.get(i).getStatObject()));
				objectModel.setOpen(true);
				objectModel.setNocheck(true);
				list.add(objectModel);
				statObjects.add(map.get(i).getStatObject());
			}
		}
		return list;
	}

	/**
	 * 转换统计项非叶子节点名称至中文(暂时使用，后期改为国际化)
	 * 
	 * @return
	 */
	private static Map<String, String> map;

	private Map<String, String> getStatCnName() {
		if (map == null) {
			map = new HashMap<String, String>();
			map.put(EnbStatConstants.STAT_OBJECT_CELL, "小区");
			map.put(EnbStatConstants.REPORT_SYSTEM_RTS, "RTS子系统");
			map.put(EnbStatConstants.MEASURE_TYPE_PERFORMANCE, "性能相关");
			map.put("MEASURE_EVENT_DL_CELL", "MAC层下行小区级统计事件");
			map.put("MEASURE_EVENT_UL_CELL", "MAC层上行小区级统计事件");
		}
		return map;
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

	/**
	 * 由获取的cellMap参数获得键值对
	 * 
	 * @return
	 */
	private Map<String, String> getCellCheckInfoMap(String mapString)
			throws Exception {
		Map<String, String> map = new LinkedHashMap<String, String>();
		// 根据";"进行拆分
		String[] strFir = mapString.split(";");
		for (int i = 0; i < strFir.length; i++) {
			// 根据"="进行拆分
			String[] str = strFir[i].split(",");
			map.put(str[0], new String(str[1].getBytes("iso-8859-1"), "utf-8"));
		}
		return map;

	}

	/**
	 * 在time时间基础上加上secondCount秒
	 * 
	 * @param time
	 * @param secondCount
	 * @return
	 */
	private long plusTime(long time, int secondCount) {
		long millSecond = DateUtils.getMillisecondTimeFromBriefTime(time);
		// 最新时间减去secondeOffset
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(millSecond));
		calendar.add(Calendar.SECOND, secondCount);
		return DateUtils.getBriefTimeFromMillisecondTime(calendar
				.getTimeInMillis());
	}

	/**
	 * 获取14位时间中的时分秒
	 * 
	 * @param time
	 * @return
	 */
	private String getHourMinSec(long time) {
		String timeStr = String.valueOf(time);
		String hour = timeStr.substring(8, 10);
		String minute = timeStr.substring(10, 12);
		String second = timeStr.substring(12, 14);
		return hour + ":" + minute + ":" + second;
	}

	/**
	 * 根据页面传来的数据,由小区获取
	 * 
	 * @param cellId
	 * @return
	 */
	private String getCellNameByIdFromHtml(String cellId) throws Exception {
		Map<String, String> map = getCellCheckInfoMap(cellIDToNames);
		return map.get(cellId);
	}

	/**
	 * 根据统计项ID获取统计项中文名
	 * 
	 * @param itemId
	 * @return
	 */
	private EnbRealtimeItemConfig getItemConfig(int itemId) {
		EnbRealtimeItemConfigCache cache = EnbRealtimeItemConfigCache
				.getInstance();
		return cache.getConfig(itemId);
	}

	private String getPageDataJson(PagingData<Enb> pageData) {
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[] { "attributes","cellStatusMap" });
		return JSONObject.fromObject(pageData, config).toString();
	}

	/**
	 * iframe查询监控详情
	 * 
	 * @return
	 */
	public String turnToMoniDetailInfo() {
		try {
			generateChartContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	private void generateChartContent() throws Exception {
		EnbRealTimeDataCache cache = EnbRealTimeDataCache.getInstance();
		// 监控的小区列表
		List<Integer> cellIdList = turnToList(cellIds);
		// 监控的统计项列表
		List<Integer> itemIdList = turnToList(itemIds);
		// 要显示的数据点个数
		int period = 60;

		StringBuilder totalBuilder = new StringBuilder();
		StringBuilder graphBuilder = new StringBuilder();
		totalBuilder.append("[");
		graphBuilder.append("[");
		for (int cellId : cellIdList) {
			// 单小区所有统计项列表
			List<List<EnbRealtimeItemData>> allCounterItemList = new ArrayList<List<EnbRealtimeItemData>>();
			// 该组统计项列表中的最小时间
			Long minTime = 0L;
			StringBuilder itembuilder = new StringBuilder();
			itembuilder.append("{\"cellId\":").append(cellId)
					.append(",\"cellName\":\"")
					.append(getCellNameByIdFromHtml(String.valueOf(cellId)))
					.append("\",\"items\":[");

			// x轴需要显示的统计项的描述
			Map<Integer, String> itemXaxisMap = new HashMap<Integer, String>();
			Map<Integer, String> itemUnitMap = new HashMap<Integer, String>();

			for (int itemId : itemIdList) {
				// 属于该小区的统计项list
				List<EnbRealtimeItemData> myList = new ArrayList<EnbRealtimeItemData>();
				// 缓存中的最新数据
				List<EnbRealtimeItemData> list = cache.queryLatestData(moId,
						itemId, period);
				if (list == null) {
					list = new ArrayList<EnbRealtimeItemData>();
				}

				for (EnbRealtimeItemData data : list) {
					// TODO:此处需要预留接口判断entityType

					String entityOid = data.getEntityOid();
					String[] str = entityOid.split("\\.");
					if (cellId == Integer.valueOf(str[1])) {
						myList.add(data);
					}
				}

				// 根据统计项的值判断数值的单位
				String unit = StatDataUtil.getCurrentUnit(itemId, myList);
				itemUnitMap.put(itemId, unit);

				// 按照时间升序排列
				sort(myList);
				if (myList.size() > 0) {
					minTime = myList.get(0).getEndTime();
					allCounterItemList.add(myList);
				}
				EnbRealtimeItemConfig itemConfig = getItemConfig(itemId);
				// 根据转换后的单位获取需要显示的单位
				String unitToShow = StatDataUtil.getUnitToShow(unit);
				String namezh = itemConfig.getName_zh();
				// 统计项显示：统计项名称(单位：**)
				String itemXaxis = namezh + "(单位:" + unitToShow + ")";
				itemXaxisMap.put(itemId, itemXaxis);

				itembuilder.append("\"").append(itemXaxis).append("\",");
			}
			if (String.valueOf(itembuilder.charAt(itembuilder.length() - 1))
					.equals(",")) {
				itembuilder.deleteCharAt(itembuilder.length() - 1);
			}
			itembuilder.append("]},");
			graphBuilder.append(itembuilder);

			StringBuilder allPointBuilder = new StringBuilder();
			allPointBuilder.append("{\"cellId\":").append(cellId)
					.append(",\"data\":[");

			if (allCounterItemList.size() > 0) {
				for (int i = 0; i < period; i++) {
					long plusMinTime = plusTime(minTime, i);
					StringBuilder pointBuilder = new StringBuilder();
					int frame = 0;
					if (allCounterItemList.size() > 0) {
						List<EnbRealtimeItemData> list = allCounterItemList
								.get(0);
						for (EnbRealtimeItemData data : list) {
							if (data.getEndTime() == plusMinTime) {
								frame = data.getSystemFrameNo();
							}
						}
					}
					// 拼接X轴需要显示的坐标值
					pointBuilder.append("{\"date\":\"").append(frame)
							.append("\\n").append(getHourMinSec(plusMinTime))
							.append("\",");
					// 拼接每个统计项在该点的数据
					for (List<EnbRealtimeItemData> list : allCounterItemList) {
						for (EnbRealtimeItemData data : list) {
							if (data.getEndTime() == plusMinTime) {
								String unit = itemUnitMap.get(data.getItemId());
								double valueToShow = 0;
								// bps相关单位特殊处理
								
								if (unit.equals(EnbStatConstants.UNIT_BPS)
										|| unit.equals(EnbStatConstants.UNIT_KBPS)
										|| unit.equals(EnbStatConstants.UNIT_MBPS)) {
									valueToShow = StatDataUtil.convertValueByUnit(data.getItemId(),
											unit, data.getStatValue());
								} else {
//									valueToShow = StatDataUtil.getValueToShow(
//											data.getItemId(),
//											data.getStatValue());
									valueToShow = data.getStatValue();
								}

								String itemXaxis = itemXaxisMap.get(data
										.getItemId());
								pointBuilder.append("\"").append(itemXaxis)
										.append("\":").append(valueToShow)
										.append(",");
							}
						}
					}
					pointBuilder.deleteCharAt(pointBuilder.length() - 1);
					pointBuilder.append("},");
					allPointBuilder.append(pointBuilder);
				}
			}
			if (String.valueOf(
					allPointBuilder.charAt(allPointBuilder.length() - 1))
					.equals(",")) {
				allPointBuilder.deleteCharAt(allPointBuilder.length() - 1);
			}
			allPointBuilder.append("]},");
			totalBuilder.append(allPointBuilder);
		}
		totalBuilder.deleteCharAt(totalBuilder.length() - 1);
		totalBuilder.append("]");
		graphBuilder.deleteCharAt(graphBuilder.length() - 1);
		graphBuilder.append("]");
		jsonData = totalBuilder.toString();
		itemData = graphBuilder.toString();
		if (jsonData.equals("]")) {
			jsonData = "[]";
		}
		if (itemData.equals("]")) {
			itemData = "[]";
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getSortBy() {
		return sortBy;
	}

	public void setSortBy(int sortBy) {
		this.sortBy = sortBy;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public String getManageError() {
		return manageError;
	}

	public void setManageError(String manageError) {
		this.manageError = manageError;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getCellList() {
		return cellList;
	}

	public void setCellList(String cellList) {
		this.cellList = cellList;
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

	public String getCellMap() {
		return cellMap;
	}

	public void setCellMap(String cellMap) {
		this.cellMap = cellMap;
	}

	public String getCellIDToNames() {
		return cellIDToNames;
	}

	public void setCellIDToNames(String cellIDToNames) {
		this.cellIDToNames = cellIDToNames;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getItemData() {
		return itemData;
	}

	public void setItemData(String itemData) {
		this.itemData = itemData;
	}

	public String getEnbHexId() {
		return enbHexId;
	}

	public void setEnbHexId(String enbHexId) {
		this.enbHexId = enbHexId;
	}

}
