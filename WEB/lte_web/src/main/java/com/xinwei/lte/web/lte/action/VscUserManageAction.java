package com.xinwei.lte.web.lte.action;

import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.lte.action.Utils.Util;
import com.xinwei.lte.web.lte.service.VscUserManageService;
import com.xinwei.lte.web.lte.service.impl.VscUserManageServiceImpl;
import com.xinwei.minas.enb.core.facade.EnbGlobalConfigFacade;
import com.xinwei.minas.enb.core.model.corenet.EnbGlobalConfig;

public class VscUserManageAction extends ActionSupport {

	private String phoneNumber;

	// 监控权限
	private static final int survillance = 1;

	private static final int query_message_id = 309;

	private static final int add_message_id = 300;

	private static final int delete_message_id = 301;

	private VscUserManageService vscUserManageService;

	public String queryVscUser() {
		JSONObject json = new JSONObject();
		json.put("status", 1);
		// TODO:改为注入
		vscUserManageService = new VscUserManageServiceImpl();
		try {

			EnbGlobalConfigFacade facade = Util
					.getFacadeInstance(EnbGlobalConfigFacade.class);
			EnbGlobalConfig config = facade.queryEnbGlobalConfig();
			String vscip = config.getVideoIp();
			int vscport = Integer.valueOf(config.getVideoPort());

			Map<String, Object> map = vscUserManageService.queryUser(vscip,
					vscport, phoneNumber);
			if (map != null) {
				int message_id = Integer.valueOf(String.valueOf(map
						.get("message_id")));
				String uid = String.valueOf(map.get("uid"));
				int result = Integer.valueOf(String.valueOf(map.get("result")));
				if (message_id == query_message_id && phoneNumber != null
						&& phoneNumber.equals(uid)) {
					json.put("status", 0);
					json.put("message", result);
				}
			}

			System.out.println("######query########");
			System.out.println(map);
			System.out.println("#######query#######");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	public String addVscUser() {
		JSONObject json = new JSONObject();
		json.put("status", 1);
		// TODO:改为注入
		vscUserManageService = new VscUserManageServiceImpl();
		try {

			EnbGlobalConfigFacade facade = Util
					.getFacadeInstance(EnbGlobalConfigFacade.class);
			EnbGlobalConfig config = facade.queryEnbGlobalConfig();
			String vscip = config.getVideoIp();
			int vscport = Integer.valueOf(config.getVideoPort());

			Map<String, Object> map = vscUserManageService.addUser(vscip,
					vscport, phoneNumber, survillance);
			if (map != null) {
				int message_id = Integer.valueOf(String.valueOf(map
						.get("message_id")));
				String uid = String.valueOf(map.get("uid"));
				int result = Integer.valueOf(String.valueOf(map.get("result")));
				if (message_id == add_message_id && phoneNumber != null
						&& phoneNumber.equals(uid) && result == 0) {
					json.put("status", 0);
				}
			}

			System.out.println("$$$$$$add$$$$$$");
			System.out.println(map);
			System.out.println("$$$$$$$add$$$$$$$$");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());

		return NONE;
	}

	public String deleteVscUser() {
		JSONObject json = new JSONObject();
		json.put("status", 1);
		// TODO:改为注入
		vscUserManageService = new VscUserManageServiceImpl();
		try {

			EnbGlobalConfigFacade facade = Util
					.getFacadeInstance(EnbGlobalConfigFacade.class);
			EnbGlobalConfig config = facade.queryEnbGlobalConfig();
			String vscip = config.getVideoIp();
			int vscport = Integer.valueOf(config.getVideoPort());

			String[] str = phoneNumber.split(",");
			for (int i = 0; i < str.length; i++) {
				Map<String, Object> map = vscUserManageService.deleteUser(
						vscip, vscport, str[i]);
				if (map != null) {
					int message_id = Integer.valueOf(String.valueOf(map
							.get("message_id")));
					String uid = String.valueOf(map.get("uid"));
					int result = Integer.valueOf(String.valueOf(map
							.get("result")));
					if (message_id == delete_message_id && str[i] != null
							&& str[i].equals(uid) && result == 0) {
						json.put("status", 0);
					} else {
						json.put("status", 1);
						break;
					}
				}

				System.out.println("%%%%%%delete%%%%%%%%%");
				System.out.println(map);
				System.out.println("%%%%%%delete%%%%%%%%%");

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
