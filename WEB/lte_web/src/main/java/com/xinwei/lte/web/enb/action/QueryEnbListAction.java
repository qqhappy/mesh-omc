package com.xinwei.lte.web.enb.action;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.cache.EnbVersionBizConfigCache;
import com.xinwei.lte.web.enb.model.AsyncEnbModel;
import com.xinwei.lte.web.enb.model.EnbModel;
import com.xinwei.lte.web.enb.util.EnbBizHelper;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.core.facade.secu.UserSecuFacade;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.OperObjectTypeDD;
import com.xinwei.minas.core.model.OperTypeDD;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbCondition;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.micro.core.facade.MicEnbBasicFacade;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.system.action.web.WebConstants;

/**
 * 
 * eNB增量操作action
 * 
 * @author zhangqiang
 * 
 */
public class QueryEnbListAction extends ActionSupport {

	/**
	 * 设备列表刷新间隔
	 */
	@Value("${enbFreshInterval}")
	private String enbFreshInterval;

	private long moId;

	/**
	 * 查询到的当前页eNB集合
	 */
	private List<EnbModel> enbModelList = new ArrayList<EnbModel>();

	/**
	 * 异步查询到的eNB集合
	 */
	private List<AsyncEnbModel> asyncEnbModelList = new ArrayList<AsyncEnbModel>();

	/**
	 * 查询到当前页eNB数量
	 */
	private int enbCount;

	/**
	 * 复选框状态
	 */
	private String checkStateArray;

	/**
	 * 当前页号
	 */
	private int currentPage = 1;

	/**
	 * 总页�?
	 */
	private int totalPages;

	/**
	 * 排序条件
	 */
	private int sortBy;

	/**
	 * 基站ID
	 */
	private String enbHexId;

	/**
	 * 基站名称
	 */
	private String enbName;

	/**
	 * 基站类型ID
	 */
	private String enbTypeId;

	/**
	 * 基站软件版本
	 */
	private String enbVersion;

	/**
	 * IP地址
	 */
	private String ipAddress;

	/**
	 * 子网掩码
	 */
	private String netMask;

	/**
	 * 网关
	 */
	private String gateway;

	/**
	 * 管理状态，2离线管理�?在线管理
	 */
	private int manageState;

	/**
	 * 数据同步方向�?网管到基站，1基站到网�?
	 */
	private int syncDirection;

	/**
	 * 按何条件查询
	 */
	private int searchBy;

	public static final int SEARCH_BY_ENB_ID = 1;

	public static final int SEARCH_BY_ENB_NAME = 2;

	public static final int SEARCH_BY_ENB_IP = 3;

	public static final int SEARCH_BY_ENB_VERSION = 4;

	/**
	 * 模糊查询的�?
	 */
	private String searchValue = "";

	/**
	 * 支持的协议版本列�?
	 */
	private List<String> versionList;

	/**
	 * 私网IP
	 */
	private String privateIp;

	/**
	 * 需要删除的索引集合
	 */
	private String parameters;

	/**
	 * 操作类型
	 */
	private String operType;

	/**
	 * 是否新增 1：新�?
	 */
	private int isAdd;

	/**
	 * 是否修改 1：修�?
	 */
	private int isModify;

	/**
	 * 是否删除 1：删�?
	 */
	private int isDelete;

	/**
	 * 操作员角色ID
	 */
	private int roleId;

	/**
	 * 错误
	 */
	private String error;

	/**
	 * 查询eNB
	 * 
	 * @return
	 */
	public String queryEnbList() {
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

		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return SUCCESS;
	}

	public String queryEnbListAsync() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {

			out = response.getWriter();
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
			if (enbModelList.size() > 0) {
				for (EnbModel enb : enbModelList) {
					AsyncEnbModel model = new AsyncEnbModel();
					model.setAlarmLevel(enb.getAlarmLevel());
					model.setEnbId(enb.getEnbId());
					model.setName(enb.getEnb().getName());
					model.setMoId(enb.getEnb().getMoId());
					model.setPublicIp(enb.getEnb().getPublicIp());
					model.setPublicPort(enb.getEnb().getPublicPort());
					model.setEnbType(enb.getEnb().getEnbType());
					model.setPrivateIp(enb.getEnb().getPrivateIp());
					model.setIsActive(enb.getIsActive());
					model.setSoftwareVersion(enb.getEnb().getSoftwareVersion());
					model.setProtocolVersion(enb.getEnb().getProtocolVersion());
					model.setRegisterState(enb.getEnb().getRegisterState());
					model.setManageStateCode(enb.getEnb().getManageStateCode());
					model.setCellStatus(enb.getCellStatus());
					asyncEnbModelList.add(model);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				JSONObject json = new JSONObject();
				JSONArray array = new JSONArray();
				array = JSONArray.fromObject(asyncEnbModelList);
				json.put("asyncEnbModelList", array);
				json.put("checkStateArray", checkStateArray);
				out.println(json.toString());
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	/**
	 * 查询增加后的eNB
	 * 
	 * @return
	 */
	public String queryAddedEnbList() {
		try {
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);

			// 构造新增所需的OperObject
			OperObject object = OperObject.createEnbOperObject(enbHexId);
			// 构建新增所需的Enb
			Enb enb = new Enb();
			enb.setEnbId(Long.parseLong(enbHexId, 16));
			enbName = new String(enbName.getBytes("iso-8859-1"), "utf-8");
			enb.setName(enbName);
			enb.setEnbType(Integer.parseInt(enbTypeId));
			// 添加基站界面上传过来的为协议版本
			enb.setProtocolVersion(enbVersion);
			enb.setManageStateCode(manageState);
			enb.setSyncDirection(syncDirection);

			enb.setIpAddress(ipAddress);
			enb.setNetMask(netMask);
			enb.setGateway(gateway);
			// 新增eNB
			if (Integer.parseInt(enbTypeId) == EnbTypeDD.XW7400) {
				// 宏站
				facade.add(object, enb);
			} else if (Integer.parseInt(enbTypeId) == EnbTypeDD.XW7102) {
				// 微站
				MicEnbBasicFacade micEnbBasicFacade = Util
						.getFacadeInstance(MicEnbBasicFacade.class);
				micEnbBasicFacade.add(object, enb);
			}

			// 新增成功后查询最新基站列�?
			EnbCondition condition = makeCondition();
			PagingData<Enb> data = facade.queryAllByCondition(condition);
			// 构造页面eNB模型
			makeEnbModel(data);
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 查询修改后的eNB
	 * 
	 * @return
	 */
	public String queryConfigedEnbList() {
		try {
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);

			// 构造新增所需的OperObject
			OperObject object = OperObject.createEnbOperObject(enbHexId);
			// 构建新增所需的Enb
			Enb enb = facade.queryByEnbId(Long.parseLong(enbHexId, 16));
			enbName = new String(enbName.getBytes("iso-8859-1"), "utf-8");
			enb.setName(enbName);
			enb.setProtocolVersion(enbVersion);
			enb.setManageStateCode(manageState);
			enb.setSyncDirection(syncDirection);

			enb.setIpAddress(ipAddress);
			enb.setNetMask(netMask);
			enb.setGateway(gateway);
			// 进行修改
			if (enb.getEnbType() == EnbTypeDD.XW7400) {
				// 宏站
				facade.modify(object, enb);
			} else if (enb.getEnbType() == EnbTypeDD.XW7102) {
				// 微站
				MicEnbBasicFacade micEnbBasicFacade = Util
						.getFacadeInstance(MicEnbBasicFacade.class);
				micEnbBasicFacade.modify(object, enb);
			}
			// 构造查询eNB的condition
			EnbCondition condition = makeCondition();
			// 查询出需要的数据
			PagingData<Enb> data = facade.queryAllByCondition(condition);
			makeEnbModel(data);
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 查询单个eNB
	 * 
	 * @return
	 */
	public String querySingleEnb() {
		try {
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);

			// 查询出需要的数据
			Enb enb = facade.queryByEnbId(Long.parseLong(enbHexId, 16));
			enbName = enb.getName();
			enbVersion = enb.getProtocolVersion();
			manageState = enb.getManageStateCode();
			syncDirection = enb.getSyncDirection();

			XBizRecord ipRecord = queryEnbIpRecord(enb.getMoId(), sessionId);
			if (ipRecord != null) {
				// 需要将8�?6进制形式的IP改为*.*.*.*格式
				ipAddress = ipRecord.getFieldBy(
						EnbConstantUtils.FIELD_NAME_IP_ADDR).getValue();
				ipAddress = convert8HexStringToIp(ipAddress);

				netMask = ipRecord.getFieldBy(
						EnbConstantUtils.FIELD_NAME_NET_MASK).getValue();
				netMask = convert8HexStringToIp(netMask);

				gateway = ipRecord.getFieldBy(
						EnbConstantUtils.FIELD_NAME_GATEWAY).getValue();
				gateway = convert8HexStringToIp(gateway);
			}

			// 获取支持的协议版本列�?
			// 获取支持的协议版本列�?
			Map<Integer, List<String>> typeVersionMap = EnbVersionBizConfigCache
					.getInstance().getSupportedVersions();
			versionList = typeVersionMap.get(enb.getEnbType());

		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return SUCCESS;
	}

	private String convert8HexStringToIp(String hexString) {
		String point = ".";
		String a1 = Integer.valueOf(hexString.substring(0, 2), 16).toString();
		String a2 = Integer.valueOf(hexString.substring(2, 4), 16).toString();
		String a3 = Integer.valueOf(hexString.substring(4, 6), 16).toString();
		String a4 = Integer.valueOf(hexString.substring(6, 8), 16).toString();
		return a1 + point + a2 + point + a3 + point + a4;
	}

	/**
	 * 查询eNB与网管建立连接利用的IPv4表中记录
	 * 
	 * @param moId
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	private XBizRecord queryEnbIpRecord(long moId, String sessionId)
			throws Exception {
		XEnbBizConfigFacade configFacade = MinasSession.getInstance().getFacade(
				sessionId, XEnbBizConfigFacade.class);
		XBizTable omcTable = configFacade.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_OMC, null, false);
		if (!EnbBizHelper.hasRecord(omcTable))
			return null;
		XBizRecord omcRecord = omcTable.getRecords().get(0);
		XBizField enbIpIdField = omcRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_ENB_IP_ID);
		// 查询网管表引用的IPv4表中记录
		XBizRecord condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_IP_ID,
				enbIpIdField.getValue()));
		XBizTable ipv4Table = configFacade.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_IPV4, condition, false);
		return ipv4Table.getRecords().get(0);
	}

	/**
	 * 查询删除后的eNB
	 * 
	 * @return
	 */
	public String queryDeletedEnbList() {
		try {
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);
			MicEnbBasicFacade micEnbBasicFacade = Util
					.getFacadeInstance(MicEnbBasicFacade.class);
			// 分解索引集合，进行多次删�?
			String[] strFir = parameters.split(";");
			for (int i = 0; i < strFir.length; i++) {
				String[] strSec = strFir[i].split(",");
				// 构建删除需要的object
				OperObject object = new OperObject();
				object.setObjectId(String.valueOf(strSec[0]));
				object.setOperType(OperTypeDD.CONFIG);
				object.setObjectType(OperObjectTypeDD.ENB);
				// 进行删除
				long moId = Long.parseLong(strSec[1]);
				int enbType = facade.queryByMoId(moId).getEnbType();
				if (enbType == EnbTypeDD.XW7400) {
					// 宏站
					facade.delete(object, moId);
				} else if (enbType == EnbTypeDD.XW7102) {
					// 微站
					micEnbBasicFacade.delete(object, moId);
				}
			}
			// 构造查询eNB的condition
			EnbCondition condition = makeCondition();
			// 查询出需要的数据
			PagingData<Enb> data = facade.queryAllByCondition(condition);
			makeEnbModel(data);
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
	 * @throws Exception
	 */
	public EnbCondition makeCondition() throws Exception {
		EnbCondition condition = new EnbCondition();
		condition.setCurrentPage(currentPage);
		condition.setNumPerPage(15);
		condition.setSortBy(sortBy);
		if (searchBy == SEARCH_BY_ENB_ID) {
			condition.setEnbId(searchValue);
		} else if (searchBy == SEARCH_BY_ENB_NAME) {
			try {
				searchValue = new String(searchValue.getBytes("iso-8859-1"),
						"utf-8");
			} catch (UnsupportedEncodingException e) {
				throw new Exception("基站名称的编码不支持");
			}
			condition.setEnbName(searchValue);
		} else if (searchBy == SEARCH_BY_ENB_IP) {
			condition.setPublicIp(searchValue);
		} else if (searchBy == SEARCH_BY_ENB_VERSION) {
			condition.setSoftwareVersion(searchValue);
		}
		return condition;
	}

	/**
	 * 封装用于传参的enb模型
	 */
	public void makeEnbModel(PagingData<Enb> data) throws Exception {
		if (data != null) {
			List<Enb> enbList = data.getResults();
			enbCount = enbList.size();
			totalPages = data.getTotalPages();
			if (totalPages == 0) {
				totalPages = 1;
			}
			// 构建EnbModel的集�?
			for (Enb enb : enbList) {
				String enbHexId = enb.getHexEnbId();
				EnbModel enbModel = new EnbModel();
				enbModel.setEnb(enb);
				enbModel.setAlarmLevel(enb.getAlarmLevel());
				enbModel.setEnbId(enbHexId);
				Map<Integer, Integer> map = enb.getCellStatusMap();
				enbModel.setCellStatus(mapToMap(map));
				if (enb.isActive()) {
					enbModel.setIsActive(1);
				} else {
					enbModel.setIsActive(0);
				}
				enbModelList.add(enbModel);
			}
		}
		// 获取角色roleId
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
		UserSecuFacade userFacade = MinasSession.getInstance().getFacade(
				sessionId, UserSecuFacade.class);
		LoginUser user = (LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT);
		roleId = userFacade.queryUser(user.getUsername()).getRoleId();
	}

	private Map<String, Integer> mapToMap(Map<Integer, Integer> cellStatusMap) {
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		if (cellStatusMap != null) {
			for (Integer key : cellStatusMap.keySet()) {
				map.put(String.valueOf(key), cellStatusMap.get(key));
			}
		}
		return map;
	}

	/**
	 * 查询某基站是否已开�?
	 * 
	 * @return
	 */
	public String queryIsActive() {
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
			Enb enb = facade.queryByMoId(moId);
			if (enb.isActive()) {
				json.put("isActive", "true");
			} else {
				json.put("isActive", "false");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	 * 查询某基站版�?
	 * 
	 * @return
	 */
	public String queryEnbVersion() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		String version = null;
		try {
			out = response.getWriter();
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);
			version = facade.queryByMoId(moId).getProtocolVersion();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.println(version);
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	public List<EnbModel> getEnbModelList() {
		return enbModelList;
	}

	public void setEnbModelList(List<EnbModel> enbModelList) {
		this.enbModelList = enbModelList;
	}

	public int getEnbCount() {
		return enbCount;
	}

	public void setEnbCount(int enbCount) {
		this.enbCount = enbCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getSortBy() {
		return sortBy;
	}

	public void setSortBy(int sortBy) {
		this.sortBy = sortBy;
	}

	public String getEnbHexId() {
		return enbHexId;
	}

	public void setEnbHexId(String enbHexId) {
		this.enbHexId = enbHexId;
	}

	public String getEnbName() {
		return enbName;
	}

	public String getEnbVersion() {
		return enbVersion;
	}

	public void setEnbVersion(String enbVersion) {
		this.enbVersion = enbVersion;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setNetMask(String netMask) {
		this.netMask = netMask;
	}

	public String getNetMask() {
		return netMask;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getGateway() {
		return gateway;
	}

	public void setVersionList(List<String> versionList) {
		this.versionList = versionList;
	}

	public List<String> getVersionList() {
		return versionList;
	}

	public void setEnbName(String enbName) {
		this.enbName = enbName;
	}

	public String getPrivateIp() {
		return privateIp;
	}

	public void setPrivateIp(String privateIp) {
		this.privateIp = privateIp;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public int getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(int isAdd) {
		this.isAdd = isAdd;
	}

	public int getIsModify() {
		return isModify;
	}

	public void setIsModify(int isModify) {
		this.isModify = isModify;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public List<AsyncEnbModel> getAsyncEnbModelList() {
		return asyncEnbModelList;
	}

	public void setAsyncEnbModelList(List<AsyncEnbModel> asyncEnbModelList) {
		this.asyncEnbModelList = asyncEnbModelList;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public void setManageState(int manageState) {
		this.manageState = manageState;
	}

	public int getManageState() {
		return manageState;
	}

	public void setSyncDirection(int syncDirection) {
		this.syncDirection = syncDirection;
	}

	public int getSyncDirection() {
		return syncDirection;
	}

	public void setSearchBy(int searchBy) {
		this.searchBy = searchBy;
	}

	public int getSearchBy() {
		return searchBy;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public String getEnbFreshInterval() {
		return enbFreshInterval;
	}

	public void setEnbFreshInterval(String enbFreshInterval) {
		this.enbFreshInterval = enbFreshInterval;
	}

	public String getEnbTypeId() {
		return enbTypeId;
	}

	public void setEnbTypeId(String enbTypeId) {
		this.enbTypeId = enbTypeId;
	}

	public String getCheckStateArray() {
		return checkStateArray;
	}

	public void setCheckStateArray(String checkStateArray) {
		this.checkStateArray = checkStateArray;
	}

}
