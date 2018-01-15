/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-8-6	|  yinyuelin 	    |  create the file                       
 */

package com.xinwei.lte.web.lte.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xinwei.lte.web.lte.model.OperResult;
import com.xinwei.lte.web.lte.model.VmgCfgModel;
import com.xinwei.lte.web.lte.service.VmgCfgService;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author yinyuelin
 * 
 */
@Service
public class VmgCfgServiceImpl implements VmgCfgService
{
	private static Logger logger = LoggerFactory.getLogger(VmgCfgServiceImpl.class);
	@Override
	public OperResult checkVmgCfgModel(VmgCfgModel vmgCfgModel)
	{
		OperResult operResult = new OperResult();
		try
		{
			if (vmgCfgModel == null)
			{
				throw new RuntimeException("传入对象为空");
			}
			
			if (vmgCfgModel.getMonitor_name() == null
					|| "".equals(vmgCfgModel.getMonitor_name()))
			{
				throw new RuntimeException("设备名称不可为空");
			}
			if (vmgCfgModel.getMonitor_type() == null
					|| "".equals(vmgCfgModel.getMonitor_type()))
			{
				throw new RuntimeException("设备类型不可为空");
			}
			if (Integer.parseInt(vmgCfgModel.getMonitor_type()) != 0
					&& Integer.parseInt(vmgCfgModel.getMonitor_type()) != 1)
			{
				throw new RuntimeException("设备类型取值错误");
			}
			if (vmgCfgModel.getMonitor_ip() == null
					|| "".equals(vmgCfgModel.getMonitor_ip()))
			{
				throw new RuntimeException("远端IP不可为空");
			}

			if (vmgCfgModel.getMonitor_ip().split("\\.").length != 4)
			{
				throw new RuntimeException("远端IP格式错误");
			}
			if (vmgCfgModel.getMonitor_port() == null
					|| "".equals(vmgCfgModel.getMonitor_port()))
			{
				throw new RuntimeException("远端端口不可为空");
			}
			if (Integer.parseInt(vmgCfgModel.getMonitor_port()) < 0
					|| Integer.parseInt(vmgCfgModel.getMonitor_port()) > 65535)
			{
				throw new RuntimeException("远端端口取值错误");
			}
			if (vmgCfgModel.getUser_name() == null
					|| "".equals(vmgCfgModel.getUser_name()))
			{
				throw new RuntimeException("用户名不可为空");
			}
			if (vmgCfgModel.getUser_password() == null
					|| "".equals(vmgCfgModel.getUser_password()))
			{
				throw new RuntimeException("密码不可为空");
			}
			if (vmgCfgModel.getComment() == null
					|| "".equals(vmgCfgModel.getComment()))
			{
				throw new RuntimeException("设备信息不可为空");
			}
			operResult.setRetCode(OperResult.SUCCESS);
		}
		catch (Exception e)
		{
			operResult.setRetCode(1);
			operResult.setRetMsg(e.getLocalizedMessage());
			logger.error("checkVmgCfgModel error, operResult = " + operResult);
		}
		return operResult;
	}
	
}
