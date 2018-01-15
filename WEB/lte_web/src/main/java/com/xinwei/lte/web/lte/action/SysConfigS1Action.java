/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-28	|  yinyuelin 	    |  create the file                       
 */

package com.xinwei.lte.web.lte.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.lte.web.lte.model.MmeS1Model;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.system.action.web.WebConstants;

/**
 * 
 * lte系统配置MME s1链路action
 * 
 * <p>
 * lte系统配置MME s1链路action
 * </p>
 * 
 * @author yinyuelin
 * 
 */

public class SysConfigS1Action extends ActionSupport {

	@Resource
	private OssAdapter ossAdapter;

	private OnlinePage onlinePage;

	private MmeS1Model mmeS1Model;

	// 保存查询条件
	private MmeS1Model querymmeS1Model;
	private List<MmeS1Model> mmeS1ModelList;

	private List<Enb> enbList = new ArrayList<Enb>();

	// 记录日志
	private static Logger logger = LoggerFactory
			.getLogger(SysConfigS1Action.class);

	private String showMessage = "暂无相关数据";

	private static final String tableName = "T_SCTP";

	/**
	 * 跳转到MME SI链路页面
	 * 
	 * @return
	 */
	public String turntoS1() {
		logger.debug("turntoS1-start");
		mmeS1ModelList = new ArrayList<MmeS1Model>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if (null == onlinePage) {
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);
			}

			if (null != querymmeS1Model) {
				if (0 != querymmeS1Model.getFirst()) {
					if (querymmeS1Model.getQueryType().equals("1")) {
						map.put("mmeS1ID", querymmeS1Model.getQueryValue());
					} else if (querymmeS1Model.getQueryType().equals("2")) {
						map.put("mmeS1peerIP", querymmeS1Model.getQueryValue());
					} else if (querymmeS1Model.getQueryType().equals("3")) {
						map.put("mmeS1peerPort",
								querymmeS1Model.getQueryValue());
					} else {
						map.put("mmeS1State", querymmeS1Model.getQueryValue());
					}

				}
			} else {
				querymmeS1Model = new MmeS1Model();
			}

			map.put("ltePageSize", LteConstant.PageSize + "");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum() + "");

			// map.put("ipId", systemAddressModel.getIp_id());
			Map<String, Object> resultMap = ossAdapter.invoke(0xab, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if (!"0".equals(flag)) {
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				return SUCCESS;
			}
			int lteTotalQueryCount = Integer.parseInt((String) resultMap
					.get("lteTotalQueryCount"));
			if (lteTotalQueryCount % LteConstant.PageSize == 0) {
				onlinePage.setTotalPages(lteTotalQueryCount
						/ LteConstant.PageSize);
			} else {
				onlinePage.setTotalPages(lteTotalQueryCount
						/ LteConstant.PageSize + 1);
			}

			List<Map> resultList = (List<Map>) resultMap.get("mmeS1Info");
			MmeS1Model s1Model;
			if (resultList != null) {
				for (Map rMap : resultList) {
					s1Model = new MmeS1Model();
					if (rMap.get("mmeS1ID") != null) {
						s1Model.setMmes1_id(((String) rMap.get("mmeS1ID"))
								.trim());
					}
					if (rMap.get("mmeS1enbID") != null) {
						s1Model.setMmes1_enbid(((String) rMap.get("mmeS1enbID"))
								.trim());
					}
					if (rMap.get("mmeS1peerIP") != null) {
						s1Model.setMmes1_peerip(((String) rMap
								.get("mmeS1peerIP")).trim());
					}
					if (rMap.get("mmeS1peerPort") != null) {
						s1Model.setMmes1_peerport(((String) rMap
								.get("mmeS1peerPort")).trim());
					}
					if (rMap.get("mmeS1State") != null) {
						s1Model.setMmes1_state(((String) rMap.get("mmeS1State"))
								.trim());
					}
					if (rMap.get("mmeS1Comment") != null) {
						s1Model.setMmes1_comment(((String) rMap
								.get("mmeS1Comment")).trim());
					}

					mmeS1ModelList.add(s1Model);
				}
			} else {
				if (onlinePage.getCurrentPageNum() == 1) {
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("turntoS1 error:" + e.toString());
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			showMessage = e.getMessage();
		}
		logger.debug("turntoS1-end");
		return SUCCESS;
	}

	/**
	 * 跳转到MME SI链路配置页面
	 * 
	 * @return
	 */
	public String turntoS1Add() {
		logger.debug("turntoS1Add");// 获取facade
		try {
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);

			// 查询出需要的数据
			enbList = facade.queryAllEnb();

		} catch (Exception e) {
			logger.error("turntoS1Add error", e);
		}

		return SUCCESS;
	}

	/**
	 * 增加MME SI链路
	 * 
	 * @return
	 */
	public String addS1() {
		logger.debug("addS1-start");
		JSONObject json = new JSONObject();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("mmeS1ID", mmeS1Model.getMmes1_id());
			map.put("mmeS1enbID", mmeS1Model.getMmes1_enbid());
			map.put("mmeS1peerIP", mmeS1Model.getMmes1_peerip());
			map.put("mmeS1peerPort", mmeS1Model.getMmes1_peerport());
			map.put("mmeS1Comment", mmeS1Model.getMmes1_comment());
			Map<String, Object> resultMap = ossAdapter.invoke(0xab, 0x01, map);
			String flag = (String) resultMap.get("lteFlag");
			if ("0".equals(flag)) {
				json.put("status", 0);
				ajaxMethod(json.toString());
			} else {
				json.put("status", 1);
				json.put("message", LteFlag.flagReturn(flag));
				ajaxMethod(json.toString());
			}

		} catch (NoAuthorityException e) {
			json.put("status", 1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
			logger.error("addSysAddress error:" + e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("addS1 error:" + e.toString());
		}
		logger.debug("addS1-end");
		return NONE;
	}

	/**
	 * 跳转到修改MME SI链路
	 * 
	 * @return
	 */
	public String toModifyS1() {
		logger.debug("toModifyS1-start");
		try {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ltePageSize", "1");
			map.put("ltePageIndex", "1");
			map.put("mmeS1ID", mmeS1Model.getMmes1_id());
			/*
			 * map.put("mmeS1peerIP", mmeS1Model.getMmes1_peerip());
			 * map.put("mmeS1peerPort", mmeS1Model.getMmes1_peerport());
			 * map.put("mmeS1State", mmeS1Model.getMmes1_state());
			 */

			// map.put("ipId", systemAddressModel.getIp_id());
			Map<String, Object> resultMap = ossAdapter.invoke(0xab, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if (!"0".equals(flag)) {

				return SUCCESS;
			}

			List<Map> resultList = (List<Map>) resultMap.get("mmeS1Info");
			if (resultList.size() != 0) {
				Map rMap = resultList.get(0);
				if (rMap.get("mmeS1ID") != null) {
					mmeS1Model.setMmes1_id(((String) rMap.get("mmeS1ID"))
							.trim());
				}
				if (rMap.get("mmeS1enbID") != null) {
					mmeS1Model.setMmes1_enbid(((String) rMap.get("mmeS1enbID"))
							.trim());
				}
				if (rMap.get("mmeS1peerIP") != null) {
					mmeS1Model.setMmes1_peerip(((String) rMap
							.get("mmeS1peerIP")).trim());
				}
				if (rMap.get("mmeS1peerPort") != null) {
					mmeS1Model.setMmes1_peerport(((String) rMap
							.get("mmeS1peerPort")).trim());
				}
				if (rMap.get("mmeS1State") != null) {
					mmeS1Model.setMmes1_state(((String) rMap.get("mmeS1State"))
							.trim());
				}
				if (rMap.get("mmeS1Comment") != null) {
					mmeS1Model.setMmes1_comment(((String) rMap
							.get("mmeS1Comment")).trim());
				}
			}

			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);

			// 查询出需要的数据
			enbList = facade.queryAllEnb();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("toModifyS1 error:" + e.toString());

		}
		logger.debug("toModifyS1-end");
		return SUCCESS;
	}

	/**
	 * 修改MME SI链路
	 * 
	 * @return
	 */
	public String modifyS1() {
		logger.debug("modifyS1-start");
		JSONObject json = new JSONObject();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("mmeS1ID", mmeS1Model.getMmes1_id());
			map.put("mmeS1enbID", mmeS1Model.getMmes1_enbid());
			map.put("mmeS1peerIP", mmeS1Model.getMmes1_peerip());
			map.put("mmeS1peerPort", mmeS1Model.getMmes1_peerport());
			// map.put("mmeS1State", mmeS1Model.getMmes1_state());
			map.put("mmeS1Comment", mmeS1Model.getMmes1_comment());
			Map<String, Object> resultMap = ossAdapter.invoke(0xab, 0x03, map);
			String flag = (String) resultMap.get("lteFlag");
			if ("0".equals(flag)) {
				json.put("status", 0);
				ajaxMethod(json.toString());
			} else {
				json.put("status", 1);
				json.put("message", LteFlag.flagReturn(flag));
				ajaxMethod(json.toString());
			}
		} catch (NoAuthorityException e) {
			json.put("status", 1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
			logger.error("addSysAddress error:" + e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("modifyS1 error:" + e.toString());
		}
		logger.debug("modifyS1-end");
		return NONE;
	}

	/**
	 * 删除MME SI链路
	 * 
	 * @return
	 */
	public String deleteS1() {
		logger.debug("deleteS1-start");
		JSONObject json = new JSONObject();
		try {
			String mmes1_id = mmeS1Model.getMmes1_id();
			String[] mmes1_idArray = mmes1_id.split(",");
			String[] enbIdArray = mmeS1Model.getMmes1_enbid().split(",");
			if (mmes1_idArray.length == 1) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("mmeS1ID", mmes1_id);
				Map<String, Object> resultMap = ossAdapter.invoke(0xab, 0x02,
						map);
				String flag = (String) resultMap.get("lteFlag");
				if (!"0".equals(flag)) {
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());

					return NONE;
				}
			} else {
				for (int i = 0; i < mmes1_idArray.length; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("mmeS1ID", mmes1_idArray[i]);
					Map<String, Object> resultMap = ossAdapter.invoke(0xab,
							0x02, map);
					String flag = (String) resultMap.get("lteFlag");
					if (!"0".equals(flag)) {
						json.put("status", 1);
						json.put("message", LteFlag.flagReturn(flag));
						ajaxMethod(json.toString());
						return NONE;
					}
				}

			}

			json.put("status", 0);
			ajaxMethod(json.toString());

		} catch (NoAuthorityException e) {
			json.put("status", 1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
			logger.error("addSysAddress error:" + e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("deleteS1 error:" + e.toString());
		}
		logger.debug("deleteS1-end");
		return NONE;
	}

	// 异步请求返回字符串
	private void ajaxMethod(String content) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/jsp; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(content);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}

	}

	// 异步请求返回单个对象
	private void ajaxJsonMethod(Object content) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/jsp; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();

			JSONObject object = new JSONObject();
			object = JSONObject.fromObject(content);
			out.println(object.toString());
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	// 异步请求返回数组
	private void ajaxArrayMethod(Object content) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/jsp; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();

			JSONArray object = new JSONArray();
			object = JSONArray.fromObject(content);
			out.println(object.toString());
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	/**
	 * 根据long型enbId获取moId
	 * 
	 * @param enbHexId
	 * @return
	 * @throws Exception
	 */
	private long getEnbMoId(long enbId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		Enb enb = facade.queryByEnbId(enbId);
		return enb.getMoId();
	}

	/**
	 * 根据long型enbId获取EnbType
	 * 
	 * @param enbHexId
	 * @return
	 * @throws Exception
	 */
	private int getEnbType(long enbId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		Enb enb = facade.queryByEnbId(enbId);
		return enb.getEnbType();
	}

	/**
	 * 是否存在u16AssID为1的流控制传输协议表记录
	 * 
	 * @param enbId
	 * @return
	 * @throws Exception
	 */
	private boolean existSctpRecordU16AssIDIsOne(long enbId, String tableName)
			throws Exception {

		XEnbBizConfigFacade facade = Util
				.getFacadeInstance(XEnbBizConfigFacade.class);
		XBizTable xBizTable = facade.queryFromEms(getEnbMoId(enbId), tableName,
				new XBizRecord());
		List<XBizRecord> records = xBizTable.getRecords();
		if (records != null && records.size() > 0) {
			for (XBizRecord record : records) {
				String u16AssID = record.getFieldMap().get("u16AssID")
						.getValue();
				if ("1".equals(u16AssID)) {
					return true;
				}
			}
		}
		return false;
	}

	private XBizRecord getXBizRecordCondition(String u8SrcIPID1,
			String au8DstIP1) {
		XBizRecord condition = new XBizRecord();
		condition.addField(makeXBizField("u16AssID", "1"));
		condition.addField(makeXBizField("u8SrcIPID1", u8SrcIPID1));
		condition.addField(makeXBizField("u8SrcIPID2", "0"));
		condition.addField(makeXBizField("u8SrcIPID3", "0"));
		condition.addField(makeXBizField("u8SrcIPID4", "0"));
		condition.addField(makeXBizField("au8DstIP1",
				turnIpStringToHex(au8DstIP1)));
		condition.addField(makeXBizField("au8DstIP2", "00000000"));
		condition.addField(makeXBizField("au8DstIP3", "00000000"));
		condition.addField(makeXBizField("au8DstIP4", "00000000"));
		condition.addField(makeXBizField("u16LocPort", "36412"));
		condition.addField(makeXBizField("u16RemotPort", "36412"));
		condition.addField(makeXBizField("u8InOutStream", "2"));
		condition.addField(makeXBizField("u8ManualOP", "0"));
		condition.addField(makeXBizField("u32Status", "1"));
		return condition;
	}

	/**
	 * 将十进制(255.255.255.255)转换为16进制的ip(ffffffff)
	 * 
	 * @return
	 */
	private String turnIpStringToHex(String ipString) {
		String result = "";
		try {
			String[] str = ipString.split("\\.");
			for (int i = 0; i < str.length; i++) {
				int partIp = Integer.valueOf(str[i]);
				String s = String.valueOf(Integer.toHexString(partIp));
				if (s.length() < 2) {
					s = "0" + s;
				}
				result = result + s;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "00000000";
		}
		return result;
	}

	/**
	 * 获取ranIp
	 * 
	 * @return
	 */
	private String getRanIp() {
		String ranIp = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			Map<String, Object> resultMap = ossAdapter.invoke(0xa9, 0x04, map);
			String ranId = null;
			if (resultMap.get("sysconfRANIPID") != null) {
				ranId = ((String) resultMap.get("sysconfRANIPID")).trim();
			}

			Map<String, Object> addrMap = new HashMap<String, Object>();
			addrMap.put("ltePageSize", "50");
			addrMap.put("ltePageIndex", "1");
			Map<String, Object> addrResultMap = ossAdapter.invoke(0xa6, 0x05,
					addrMap);
			String flag = (String) resultMap.get("lteFlag");
			if ("0".equals(flag)) {
				List<Map> resultAddrList = (List<Map>) addrResultMap
						.get("ipInfo");
				if (null != resultAddrList) {
					for (Map rMap : resultAddrList) {
						String ipID = (String) rMap.get("ipID");
						if (ipID != null && ipID.equals(ranId)) {
							ranIp = (String) rMap.get("ipAddr");
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			ranIp = "0.0.0.0";
		}
		return ranIp;
	}

	/**
	 * 生成xBizField
	 * 
	 * @param name
	 * @return
	 */
	private XBizField makeXBizField(String name, String value) {
		XBizField xBizField = new XBizField();
		xBizField.setName(name);
		xBizField.setValue(value);
		return xBizField;
	}

	public OnlinePage getOnlinePage() {
		return onlinePage;
	}

	public void setOnlinePage(OnlinePage onlinePage) {
		this.onlinePage = onlinePage;
	}

	public MmeS1Model getMmeS1Model() {
		return mmeS1Model;
	}

	public void setMmeS1Model(MmeS1Model mmeS1Model) {
		this.mmeS1Model = mmeS1Model;
	}

	public List<MmeS1Model> getMmeS1ModelList() {
		return mmeS1ModelList;
	}

	public void setMmeS1ModelList(List<MmeS1Model> mmeS1ModelList) {
		this.mmeS1ModelList = mmeS1ModelList;
	}

	public MmeS1Model getQuerymmeS1Model() {
		return querymmeS1Model;
	}

	public void setQuerymmeS1Model(MmeS1Model querymmeS1Model) {
		this.querymmeS1Model = querymmeS1Model;
	}

	public String getShowMessage() {
		return showMessage;
	}

	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}

	public List<Enb> getEnbList() {
		return enbList;
	}

	public void setEnbList(List<Enb> enbList) {
		this.enbList = enbList;
	}

}
