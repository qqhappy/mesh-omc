package com.xinwei.lte.web.enb.action;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.cache.StatItemConfigCache;
import com.xinwei.lte.web.enb.model.EnbModel;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbCondition;
import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;
import com.xinwei.minas.enb.core.utils.EnbStatConstants;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.system.action.web.WebConstants;

/**
 * 查询统计项
 * 
 * @author zhangqiang
 * 
 */
public class QueryStatAction extends ActionSupport {

	/**
	 * Counter统计项的key
	 */
	private String counterKey;

	/**
	 * KPI统计项的key
	 */
	private String kpiKey;

	/**
	 * 查询出的Counter统计项
	 */
	private List<CounterItemConfig> counterList = new ArrayList<CounterItemConfig>();

	/**
	 * 查询出的Counter统计项
	 */
	private List<KpiItemConfig> kpiList = new ArrayList<KpiItemConfig>();

	/**
	 * 查询到的当前页eNB集合
	 */
	private List<EnbModel> enbModelList = new ArrayList<EnbModel>();

	/**
	 * ENB.CELL 或 ENB
	 */
	private String entityType;

	/**
	 * 异常
	 */
	private String error;

	/**
	 * 跳转至空的查询页面
	 * 
	 * @return
	 */
	public String queryPropertyNONE() {
		try {
			// 获取facade
			// String sessionId = ((LoginUser) ActionContext.getContext()
			// .getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
			// .getSessionId();
			// StatBizFacade facade = MinasSession.getInstance().getFacade(
			// sessionId, StatBizFacade.class);
			// StatItemConfigCache.getInstance().initialize(facade);
		} catch (Exception e) {
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 查询Counter统计项
	 * 
	 * @return
	 */
	public String queryPropertyCounter() {

		try {
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);
			// 构造查询eNB的condition
			EnbCondition condition = makeCondition();
			// 查询出需要的数据
			PagingData<Enb> data = facade.queryAllByCondition(condition);
			makeEnbModel(data);
			String[] keySplit = counterKey.split("\\.");
			if (keySplit[1].equals("CELL")) {
				entityType = "ENB.CELL";
			} else {
				entityType = "ENB";
			}
			if (StatItemConfigCache.getInstance().getCounterMap()
					.get(counterKey) != null) {
				// TODO 过滤未实现的统计项,需要更新到CC
				List<CounterItemConfig> configList = StatItemConfigCache
						.getInstance().getCounterMap().get(counterKey);
				for (CounterItemConfig config : configList) {
					if (config.getRemark() == EnbStatConstants.COUNTER_REALITY) {
						counterList.add(config);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 查询KPI统计项
	 * 
	 * @return
	 */
	public String queryPropertyKpi() {
		try {
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);
			// 构造查询eNB的condition
			EnbCondition condition = makeCondition();
			// 查询出需要的数据
			PagingData<Enb> data = facade.queryAllByCondition(condition);
			makeEnbModel(data);
			String[] keySplit = kpiKey.split("\\.");
			if (keySplit[1].equals("CELL")) {
				entityType = "ENB.CELL";
			} else {
				entityType = "ENB";
			}
			if (StatItemConfigCache.getInstance().getKpiMap().get(kpiKey) != null) {
				List<KpiItemConfig> configList = StatItemConfigCache
						.getInstance().getKpiMap().get(kpiKey);
				// TODO 过滤未实现的统计项,需要更新到CC
				for (KpiItemConfig config : configList) {
					if (config.getRemark() == EnbStatConstants.COUNTER_REALITY) {
						kpiList.add(config);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 封装查询条件
	 * 
	 * @return EnbCondition
	 */
	public EnbCondition makeCondition() {
		EnbCondition condition = new EnbCondition();
		condition.setCurrentPage(1);
		condition.setNumPerPage(250);
		return condition;
	}

	/**
	 * 封装用于传参的enb模型
	 */
	public void makeEnbModel(PagingData<Enb> data) {
		if (data != null) {
			List<Enb> enbList = data.getResults();
			// 构建EnbModel的集合
			for (Enb enb : enbList) {
				String enbHexId = enb.getHexEnbId();
				EnbModel enbModel = new EnbModel();
				enbModel.setEnb(enb);
				enbModel.setEnbId(enbHexId);
				enbModelList.add(enbModel);
			}
		}
	}

	public String getCounterKey() {
		return counterKey;
	}

	public void setCounterKey(String counterKey) {
		this.counterKey = counterKey;
	}

	public List<CounterItemConfig> getCounterList() {
		return counterList;
	}

	public void setCounterList(List<CounterItemConfig> counterList) {
		this.counterList = counterList;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<EnbModel> getEnbModelList() {
		return enbModelList;
	}

	public void setEnbModelList(List<EnbModel> enbModelList) {
		this.enbModelList = enbModelList;
	}

	public List<KpiItemConfig> getKpiList() {
		return kpiList;
	}

	public void setKpiList(List<KpiItemConfig> kpiList) {
		this.kpiList = kpiList;
	}

	public String getKpiKey() {
		return kpiKey;
	}

	public void setKpiKey(String kpiKey) {
		this.kpiKey = kpiKey;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
}
