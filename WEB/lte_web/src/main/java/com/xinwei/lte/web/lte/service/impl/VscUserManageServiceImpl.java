package com.xinwei.lte.web.lte.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.xinwei.lte.web.lte.service.VscUserManageService;
import com.xinwei.omp.core.utils.ByteUtils;

public class VscUserManageServiceImpl implements VscUserManageService {

	private static final int app_data = 1;

	private static final int source_ip = 99999;

	private static final int service_type = 81;
	
	private static final int query_message_id = 309;

	private static final int add_message_id = 300;

	private static final int delete_message_id = 301;
	
	
	public Map<String, Object> queryUser(String vscip, int port,
			String phoneNumber) throws Exception {
		Socket socket = null;
		InputStream is = null;
		OutputStream os = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			socket = new Socket(vscip, port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			String message_info = phoneNumber;
			byte[] buf = getMessageByte(app_data, source_ip, service_type,
					query_message_id, message_info);
			os.write(buf);
			os.flush();
			byte[] b = new byte[128];
			if ((is.read(b)) != -1) {
				map.put("message_id", ByteUtils.toInt(b, 0, 4));
				map.put("uid", ByteUtils.toString(b, 4, phoneNumber.length(),
						"UTF-8"));
				map.put("result",
						ByteUtils.toInt(b, 4 + phoneNumber.length(), 4));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public Map<String, Object> addUser(String vscip, int port,
			String phoneNumber, int survillance) throws Exception {
		Socket socket = null;
		InputStream is = null;
		OutputStream os = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			socket = new Socket(vscip, port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			String message_info = phoneNumber + "|" + phoneNumber + "|"
					+ survillance;
			byte[] buf = getMessageByte(app_data, source_ip, service_type,
					add_message_id, message_info);
			os.write(buf);
			os.flush();
			byte[] b = new byte[128];
			if ((is.read(b)) != -1) {
				map.put("message_id", ByteUtils.toInt(b, 0, 4));
				map.put("uid", ByteUtils.toString(b, 4, phoneNumber.length(),
						"UTF-8"));
				map.put("result",
						ByteUtils.toInt(b, 4 + phoneNumber.length(), 4));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public Map<String, Object> deleteUser(String vscip, int port,
			String phoneNumber) throws Exception {
		Socket socket = null;
		InputStream is = null;
		OutputStream os = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			socket = new Socket(vscip, port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			String message_info = phoneNumber;
			byte[] buf = getMessageByte(app_data, source_ip, service_type,
					delete_message_id, message_info);
			os.write(buf);
			os.flush();
			byte[] b = new byte[128];
			if ((is.read(b)) != -1) {
				map.put("message_id", ByteUtils.toInt(b, 0, 4));
				map.put("uid", ByteUtils.toString(b, 4, phoneNumber.length(),
						"UTF-8"));
				map.put("result",
						ByteUtils.toInt(b, 4 + phoneNumber.length(), 4));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	private byte[] getMessageByte(int app_data, int source_ip,
			int service_type, int message_id, String message_info) {
		byte[] buf = new byte[128];
		ByteUtils.putInt(buf, 0, app_data);
		ByteUtils.putInt(buf, 4, message_info.length() + 12);
		ByteUtils.putInt(buf, 8, source_ip);
		ByteUtils.putInt(buf, 12, service_type);
		ByteUtils.putInt(buf, 16, message_id);
		ByteUtils.putString(buf, 20, message_info, message_info.length(), '\0',
				"UTF-8");
		return buf;
	}
}
