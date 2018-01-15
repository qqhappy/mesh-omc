package com.xinwei.lte.web.enb.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.util.StatDataExcelUtil;
import com.xinwei.lte.web.enb.util.StatDataUtil;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbStatBizFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;
import com.xinwei.minas.xstat.core.model.PreStatItem;
import com.xinwei.minas.xstat.core.model.StatDataQueryCondition;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.system.action.web.WebConstants;

/**
 * 导出统计项数据
 * 
 * @author zhangqiang
 * 
 */
public class ExportStatDataAction extends ActionSupport {

	/**
	 * 统计项类别
	 */
	private String itemType;

	/**
	 * 开始时间
	 */
	private String beginTime;

	/**
	 * 结束时间
	 */
	private String endTime;

	/**
	 * 统计间隔
	 */
	private int interval;

	/**
	 * 统计项的标识，格式：统计项类型#统计项ID，这里有多个，之间用","分隔
	 */
	private String itemParameters;

	/**
	 * 统计对象标识，格式：moId#enbId(16进制),多个，用","分隔
	 */
	private String objectParameters;

	/**
	 * 异常
	 */
	private String error;

	/**
	 * 统计对象类别，设备还是小区
	 */
	private String entityType;

	public String exportStatData() {
		try {
			// 构建查询条件
			StatDataQueryCondition condition = new StatDataQueryCondition();
			condition.setStartTime(turnToLong(beginTime));
			condition.setEndTime(turnToLong(endTime));
			condition.setInterval(interval);
			// 统计项集合
			String[] items = itemParameters.split(",");
			List<String> itemList = new ArrayList<String>();
			for (int i = 0; i < items.length; i++) {
				String s = itemType + "#" + items[i];
				itemList.add(s);
			}
			condition.setItemList(itemList);
			// 设备集合
			String[] objects = objectParameters.split(",");
			List<Long> moIds = new ArrayList<Long>();
			for (int i = 0; i < objects.length; i++) {
				moIds.add(Long.valueOf(objects[i]));
			}
			Map<Long, String> map = queryEnbHexIdByMoId(moIds);
			if (map != null && map.keySet().size() > 0) {
				for (Long moId : map.keySet()) {
					if (entityType.equals("ENB")) {
						condition.addEntity(moId, entityType, map.get(moId));
					} else {
						List<Integer> list = queryCellByMoId(Long.valueOf(moId));
						for (Integer u8CId : list) {
							String entityOid = map.get(moId) + "." + u8CId;
							condition.addEntity(Long.valueOf(moId), entityType,
									entityOid);
						}
					}
				}
			}
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbStatBizFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbStatBizFacade.class);
			List<PreStatItem> resultList = facade.queryStatData(condition);
			String statFileName = "statData-" + entityType + "-"
					+ condition.getStartTime() + "-" + condition.getEndTime()
					+ ".xls";

			HttpServletResponse response = ServletActionContext.getResponse();
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("Application/msexcel;charset=utf-8");
			response.setHeader("Content-disposition", "attachment;filename="
					+ statFileName);
			StatDataExcelUtil util = new StatDataExcelUtil();
			util.exportStatData(resultList, out, interval, getColumnNames());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 查询某个基站的小区ID列表
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	private List<Integer> queryCellByMoId(long moId) throws Exception {
		// 获取facade
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
		XEnbBizConfigFacade facade = MinasSession.getInstance().getFacade(
				sessionId, XEnbBizConfigFacade.class);
		List<Integer> list = new ArrayList<Integer>();
		XBizTable xBizTable = facade.queryFromEms(moId, "T_CEL_PARA",
				new XBizRecord(), false);
		List<XBizRecord> records = new LinkedList<XBizRecord>();
		records = xBizTable.getRecords();
		for (XBizRecord xBizRecord : records) {
			int u8CId = Integer.valueOf(String.valueOf(xBizRecord.getFieldMap()
					.get("u8CId").getValue()));
			list.add(u8CId);
		}
		return list;
	}

	private Map<Long, String> queryEnbHexIdByMoId(List<Long> moIds)
			throws Exception {
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
		EnbBasicFacade facade = MinasSession.getInstance().getFacade(sessionId,
				EnbBasicFacade.class);
		List<Enb> list = facade.queryByMoIdList(moIds);
		Map<Long, String> map = new HashMap<Long, String>();
		for (Enb enb : list) {
			map.put(enb.getMoId(), enb.getHexEnbId());
		}
		return map;
	}

	/**
	 * 将时间字符串转为long型
	 * 
	 * @param str
	 * @return
	 */
	public long turnToLong(String str) {

		String[] strArray = str.split("");
		String strLong = "";
		if (strArray.length > 0) {
			for (int i = 0; i < strArray.length; i++) {
				if (strArray[i].matches("\\d")) {
					strLong += strArray[i];
				}
			}
		}
		return Long.parseLong(strLong);
	}
    //得到导出excel的表头信息
	public List<String> getColumnNames() throws Exception {
		List<String> columnList = new ArrayList<String>();
		String[] items = itemParameters.split(",");
		columnList.add("eNB ID");
		columnList.add("eNB名称");
		if (entityType.equalsIgnoreCase("ENB.CELL")) {
			columnList.add("小区ID");
			columnList.add("小区名称");
		}
		columnList.add("统计时间");

		for (String itemId : items) {
			columnList.add("(" + itemId + ")" + getConfigName(itemType, itemId)
					+ "(" + StatDataUtil.getUnitToShow(getConfigUnit(itemType, itemId)) + ")");
		}

		return columnList;

	}
	/**
	 * 得到统计项的名称，生成excel表头使用
	 * 
	 * @param itemList
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public String getConfigName(String type, String itemId) throws Exception {
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
		EnbStatBizFacade facade = MinasSession.getInstance().getFacade(
				sessionId, EnbStatBizFacade.class);
		List<CounterItemConfig> counterItemConfig = facade
				.queryCounterConfigs();
		List<KpiItemConfig> kpiItemConfig = facade.queryKpiConfigs();
		String name = "";
		if (type.equalsIgnoreCase("Counter")) {
			for (CounterItemConfig config : counterItemConfig) {
				if (config.getCounterId() == Integer.parseInt(itemId)) {
					name = config.getCounterName_zh();
					return name;
				}
			}
		} else {
			for (KpiItemConfig config : kpiItemConfig) {
				if (config.getKpiId() == Integer.parseInt(itemId)) {
					name = config.getKpiName_zh();
					return name;
				}
			}
		}

		return name;
	}
	
	/**
	 * 获取统计项的单位
	 * 
	 * @param type
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	public String getConfigUnit(String type, String itemId) throws Exception {
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
				if (cItemconfig.getCounterId() == Integer.parseInt(itemId)) {
					unit = cItemconfig.getUnit();
					return unit;
				}

		} else {
			for (KpiItemConfig kItemconfig : kpiItemConfig)
				if (kItemconfig.getKpiId() == Integer.parseInt(itemId)) {
					unit = kItemconfig.getUnit();
					return unit;
				}
		}

		return unit;
	}
	
	public String getItemParameters() {
		return itemParameters;
	}

	public void setItemParameters(String itemParameters) {
		this.itemParameters = itemParameters;
	}

	public String getObjectParameters() {
		return objectParameters;
	}

	public void setObjectParameters(String objectParameters) {
		this.objectParameters = objectParameters;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
}
