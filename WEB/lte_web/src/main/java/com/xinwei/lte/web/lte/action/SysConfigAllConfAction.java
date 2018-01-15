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

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.SysAllConfigModel;
import com.xinwei.lte.web.lte.model.SystemAddressModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.minas.enb.core.facade.EnbGlobalConfigFacade;
import com.xinwei.minas.enb.core.model.corenet.EnbGlobalConfig;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * lte系统配置系统全局配置action
 * 
 * <p>
 * lte系统配置系统全局配置action
 * </p>
 * 
 * @author yinyuelin
 * 
 */

public class SysConfigAllConfAction extends ActionSupport {

	// 记录日志
	private static Logger logger = LoggerFactory
			.getLogger(SysConfigAllConfAction.class);

	@Resource
	private OssAdapter ossAdapter;

	private OnlinePage onlinePage;

	private SysAllConfigModel sysAllConfigModel;

	private List<SysAllConfigModel> sysAllConfigModelList;

	private List<SystemAddressModel> systemAddressModelList;

	/**
	 * 跳转到系统全局配置页面
	 * 
	 * @return
	 */
	public String turntoWholeConfig() {
		logger.debug("turntoWholeConfig-start");
		sysAllConfigModelList = new ArrayList<SysAllConfigModel>();
		try {

			Map<String, Object> map = new HashMap<String, Object>();

			Map<String, Object> resultMap = ossAdapter.invoke(0xa9, 0x04, map);

			sysAllConfigModel = new SysAllConfigModel();
			if (resultMap.get("sdcId") != null) {
				sysAllConfigModel.setSdcId(((String) resultMap.get("sdcId"))
						.trim());
			}
			if (resultMap.get("sysconfSipIPID") != null) {
				sysAllConfigModel.setSysconf_sipip_id(((String) resultMap
						.get("sysconfSipIPID")).trim());
			}
			if (resultMap.get("sysconfSipPort") != null) {
				sysAllConfigModel.setSysconf_sipport(((String) resultMap
						.get("sysconfSipPort")).trim());
			}
			if (resultMap.get("sysconfPLMN") != null) {
				sysAllConfigModel.setSysconf_plmn(((String) resultMap
						.get("sysconfPLMN")).trim());
			}
			if (resultMap.get("sysconfMMECODE") != null) {
				sysAllConfigModel.setSysconf_mmecode(((String) resultMap
						.get("sysconfMMECODE")).trim());
			}
			if (resultMap.get("sysconfGROUPID") != null) {
				sysAllConfigModel.setSysconf_groupid(((String) resultMap
						.get("sysconfGROUPID")).trim());
			}
			if (resultMap.get("sysconfMMEIPID") != null) {
				sysAllConfigModel.setSysconf_mmeip_id(((String) resultMap
						.get("sysconfMMEIPID")).trim());
			}
			if (resultMap.get("sysconfRANIPID") != null) {
				sysAllConfigModel.setSysconf_ranip_id(((String) resultMap
						.get("sysconfRANIPID")).trim());
			}
			if (resultMap.get("sysconfPDNID") != null) {
				sysAllConfigModel.setSysconf_pdnip_id(((String) resultMap
						.get("sysconfPDNID")).trim());
			}
			if (resultMap.get("sysconfAPN") != null) {
				sysAllConfigModel.setSysconf_apn(((String) resultMap
						.get("sysconfAPN")).trim());
			}
			if (resultMap.get("sysconfMMES1PORT") != null) {
				sysAllConfigModel.setSysconf_mmes1_port(((String) resultMap
						.get("sysconfMMES1PORT")).trim());
			}
			if (resultMap.get("sysconfMasterKey") != null) {
				sysAllConfigModel.setSysconf_master_key(((String) resultMap
						.get("sysconfMasterKey")).trim());
			}
			if (resultMap.get("sysconfMaxDuration") != null) {
				sysAllConfigModel.setSysconf_maxduration(((String) resultMap
						.get("sysconfMaxDuration")).trim());
			}
			if (resultMap.get("sysconfRecordAudio") != null) {
				sysAllConfigModel
						.setSysconf_decordaudio(Integer
								.parseInt(((String) resultMap
										.get("sysconfRecordAudio")).trim()));
			}
			if (resultMap.get("sysconfRecordVedio") != null) {
				sysAllConfigModel
						.setSysconf_decordvedio(Integer
								.parseInt(((String) resultMap
										.get("sysconfRecordVedio")).trim()));
			}
			if (resultMap.get("sysconfRecordIM") != null) {
				sysAllConfigModel.setSysconf_decordim(Integer
						.parseInt(((String) resultMap.get("sysconfRecordIM"))
								.trim()));
			}

			if (resultMap.get("sysconfDnsIp") != null) {
				sysAllConfigModel.setSysconf_dns_ip((String) resultMap
						.get("sysconfDnsIp"));
			}

			sysAllConfigModelList.add(sysAllConfigModel);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return SUCCESS;
	}

	/**
	 * 跳转到修改系统全局配置页面
	 * 
	 * @return
	 */
	public String toModifyWholeConfig() {
		logger.debug("toModifyWholeConfig -start");
		sysAllConfigModel = new SysAllConfigModel();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			Map<String, Object> resultMap = ossAdapter.invoke(0xa9, 0x04, map);
			if (resultMap.get("sdcId") != null) {
				sysAllConfigModel.setSdcId(((String) resultMap.get("sdcId"))
						.trim());
			}
			if (resultMap.get("sysconfSipIPID") != null) {
				sysAllConfigModel.setSysconf_sipip_id(((String) resultMap
						.get("sysconfSipIPID")).trim());
			}
			if (resultMap.get("sysconfSipPort") != null) {
				sysAllConfigModel.setSysconf_sipport(((String) resultMap
						.get("sysconfSipPort")).trim());
			}
			// if(resultMap.get("sysconfPLMN") != null){
			// sysAllConfigModel.setSysconf_plmn(((String)resultMap.get("sysconfPLMN")).trim());
			// }
			EnbGlobalConfigFacade facade = Util
					.getFacadeInstance(EnbGlobalConfigFacade.class);
			EnbGlobalConfig config = facade.queryEnbGlobalConfig();
			if (config != null) {
				String plmn = config.getMcc() + config.getMnc();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < plmn.length(); i++) {
					if (i % 2 != 0) {
						sb.append(plmn.charAt(i));
					}
				}
				if (sb.charAt(sb.length() - 1) == 'f'
						|| sb.charAt(sb.length() - 1) == 'F') {
					sb.deleteCharAt(sb.length() - 1);
				}
				sysAllConfigModel.setSysconf_plmn(sb.toString());
			}
			if (resultMap.get("sysconfMMECODE") != null) {
				sysAllConfigModel.setSysconf_mmecode(((String) resultMap
						.get("sysconfMMECODE")).trim());
			}
			if (resultMap.get("sysconfGROUPID") != null) {
				sysAllConfigModel.setSysconf_groupid(((String) resultMap
						.get("sysconfGROUPID")).trim());
			}
			if (resultMap.get("sysconfMMEIPID") != null) {
				sysAllConfigModel.setSysconf_mmeip_id(((String) resultMap
						.get("sysconfMMEIPID")).trim());
			}
			if (resultMap.get("sysconfRANIPID") != null) {
				sysAllConfigModel.setSysconf_ranip_id(((String) resultMap
						.get("sysconfRANIPID")).trim());
			}
			if (resultMap.get("sysconfPDNID") != null) {
				sysAllConfigModel.setSysconf_pdnip_id(((String) resultMap
						.get("sysconfPDNID")).trim());
			}
			if (resultMap.get("sysconfAPN") != null) {
				sysAllConfigModel.setSysconf_apn(((String) resultMap
						.get("sysconfAPN")).trim());
			}
			if (resultMap.get("sysconfMMES1PORT") != null) {
				sysAllConfigModel.setSysconf_mmes1_port(((String) resultMap
						.get("sysconfMMES1PORT")).trim());
			}
			if (resultMap.get("sysconfMasterKey") != null) {
				sysAllConfigModel.setSysconf_master_key(((String) resultMap
						.get("sysconfMasterKey")).trim());
			}
			if (resultMap.get("sysconfMaxDuration") != null) {
				sysAllConfigModel.setSysconf_maxduration(((String) resultMap
						.get("sysconfMaxDuration")).trim());
			}
			if (resultMap.get("sysconfRecordAudio") != null) {
				sysAllConfigModel
						.setSysconf_decordaudio(Integer
								.parseInt(((String) resultMap
										.get("sysconfRecordAudio")).trim()));
			}
			if (resultMap.get("sysconfRecordVedio") != null) {
				sysAllConfigModel
						.setSysconf_decordvedio(Integer
								.parseInt(((String) resultMap
										.get("sysconfRecordVedio")).trim()));
			}
			if (resultMap.get("sysconfRecordIM") != null) {
				sysAllConfigModel.setSysconf_decordim(Integer
						.parseInt(((String) resultMap.get("sysconfRecordIM"))
								.trim()));
			}
			if (resultMap.get("sysconfDnsIp") != null) {
				sysAllConfigModel.setSysconf_dns_ip((String) resultMap
						.get("sysconfDnsIp"));
			}if (resultMap.get("pdt_sip_port") != null) {
				sysAllConfigModel
				.setPdt_sip_port(Integer
						.parseInt(((String) resultMap
								.get("pdt_sip_port")).trim()));
	}

			Map<String, Object> addrMap = new HashMap<String, Object>();
			addrMap.put("ltePageSize", "50");
			addrMap.put("ltePageIndex", "1");
			Map<String, Object> addrResultMap = ossAdapter.invoke(0xa6, 0x05,
					addrMap);
			String flag = (String) resultMap.get("lteFlag");
			if ("0".equals(flag)) {
				systemAddressModelList = new ArrayList<SystemAddressModel>();
				List<Map> resultAddrList = (List<Map>) addrResultMap
						.get("ipInfo");
				SystemAddressModel sysAddressModel;
				if (null != resultAddrList) {
					for (Map rMap : resultAddrList) {
						sysAddressModel = new SystemAddressModel();
						if (rMap.get("ipID") != null) {
							sysAddressModel
									.setIp_id(((String) rMap.get("ipID"))
											.trim());
						}
						if (rMap.get("ipAddr") != null) {
							sysAddressModel.setIp_addr(((String) rMap
									.get("ipAddr")).trim());
						}
						systemAddressModelList.add(sysAddressModel);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("toModifyWholeConfig error:" + e.toString());

		}
		logger.debug("toModifyWholeConfig -end");
		return SUCCESS;
	}

	/**
	 * 修改系统全局配置
	 * 
	 * @return
	 */
	public String modifyWholeConfig() {

		logger.debug("modifyWholeConfig -start");
		JSONObject json = new JSONObject();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sdcId", sysAllConfigModel.getSdcId());
			map.put("sysconfSipIPID", sysAllConfigModel.getSysconf_sipip_id());
			
			// map.put("sysconfSipPort",
			// sysAllConfigModel.getSysconf_sipport());
			map.put("sysconfPLMN", sysAllConfigModel.getSysconf_plmn());
			map.put("sysconfMMECODE", sysAllConfigModel.getSysconf_mmecode());
			map.put("sysconfGROUPID", sysAllConfigModel.getSysconf_groupid());
			// map.put("sysconfMMEIPID",
			// sysAllConfigModel.getSysconf_mmeip_id());
			map.put("sysconfRANIPID", sysAllConfigModel.getSysconf_ranip_id());
			map.put("sysconfPDNID", sysAllConfigModel.getSysconf_pdnip_id());
			map.put("sysconfAPN", sysAllConfigModel.getSysconf_apn());
			/*
			 * if(!sysAllConfigModel.getSysconf_mmes1_port().equals("")){
			 * map.put("sysconfMMES1PORT",
			 * sysAllConfigModel.getSysconf_mmes1_port()); }
			 */
			map.put("sysconfMasterKey",
					sysAllConfigModel.getSysconf_master_key());
			map.put("sysconfMaxDuration",
					sysAllConfigModel.getSysconf_maxduration());
			map.put("sysconfRecordAudio",
					sysAllConfigModel.getSysconf_decordaudio());
			map.put("sysconfRecordVedio",
					sysAllConfigModel.getSysconf_decordvedio());
			
			map.put("pdt_sip_port",
					sysAllConfigModel.getPdt_sip_port());
			
			map.put("sysconfRecordIM", sysAllConfigModel.getSysconf_decordim());
			if (sysAllConfigModel.getSysconf_dns_ip().split("\\.").length == 4) {
				map.put("sysconfDnsIp", sysAllConfigModel.getSysconf_dns_ip());
			}

			Map<String, Object> resultMap = ossAdapter.invoke(0xa9, 0x03, map);
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
			logger.error("modifyWholeConfig error:" + e.toString());
		}
		logger.debug("modifyWholeConfig -end");
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

	public OnlinePage getOnlinePage() {
		return onlinePage;
	}

	public void setOnlinePage(OnlinePage onlinePage) {
		this.onlinePage = onlinePage;
	}

	public SysAllConfigModel getSysAllConfigModel() {
		return sysAllConfigModel;
	}

	public void setSysAllConfigModel(SysAllConfigModel sysAllConfigModel) {
		this.sysAllConfigModel = sysAllConfigModel;
	}

	public List<SysAllConfigModel> getSysAllConfigModelList() {
		return sysAllConfigModelList;
	}

	public void setSysAllConfigModelList(
			List<SysAllConfigModel> sysAllConfigModelList) {
		this.sysAllConfigModelList = sysAllConfigModelList;
	}

	public List<SystemAddressModel> getSystemAddressModelList() {
		return systemAddressModelList;
	}

	public void setSystemAddressModelList(
			List<SystemAddressModel> systemAddressModelList) {
		this.systemAddressModelList = systemAddressModelList;
	}

}
