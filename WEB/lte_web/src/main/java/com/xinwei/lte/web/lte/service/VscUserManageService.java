package com.xinwei.lte.web.lte.service;

import java.util.Map;

/**
 * McLTE视频会议融合，用户身份管理
 * 
 * @author zhangqiang
 * 
 */
public interface VscUserManageService {
	
	/**
	 * 查询用户开户状态
	 * 
	 * @param vscip
	 *            vsc服务器地址
	 * @param port
	 *            vsc服务器端口
	 * @param phoneNumber
	 *            用户电话号码
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryUser(String vscip, int port,
			String phoneNumber) throws Exception;

	/**
	 * 增加用户
	 * 
	 * @param vscip
	 *            vsc服务器地址
	 * @param port
	 *            vsc服务器端口
	 * @param phoneNumber
	 *            用户电话号码
	 * @param survillance
	 *            监控权限 0-没有 1-有
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> addUser(String vscip, int port,
			String phoneNumber, int survillance) throws Exception;

	/**
	 * 删除用户
	 * 
	 * @param vscip
	 *            vsc服务器地址
	 * @param port
	 *            vsc服务器端口
	 * @param phoneNumber
	 *            用户电话号码
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> deleteUser(String vscip, int port,
			String phoneNumber) throws Exception;
}
