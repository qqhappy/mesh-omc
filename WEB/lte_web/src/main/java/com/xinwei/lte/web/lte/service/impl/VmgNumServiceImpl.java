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
import com.xinwei.lte.web.lte.model.VmgNumModel;
import com.xinwei.lte.web.lte.service.VmgNumService;

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
public class VmgNumServiceImpl implements VmgNumService
{
	private static Logger logger = LoggerFactory.getLogger(VmgNumServiceImpl.class);
	@Override
	public OperResult checkVmgNumModel(VmgNumModel vmgNumModel)
	{
		OperResult operResult = new OperResult();
		try
		{
			if (vmgNumModel == null)
			{
				throw new RuntimeException("传入对象为空");
			}
			
			if (vmgNumModel.getCall_number() == null
					|| "".equals(vmgNumModel.getCall_number()))
			{
				throw new RuntimeException("视频监控终端不可为空");
			}
			if (vmgNumModel.getAuth_password() == null
					|| "".equals(vmgNumModel.getAuth_password()))
			{
				throw new RuntimeException("密码不可为空");
			}

			if (vmgNumModel.getMonitor_name() == null
					|| "".equals(vmgNumModel.getMonitor_name()))
			{
				throw new RuntimeException("视频监控设备不可为空");
			}

			if (vmgNumModel.getChannel_id() == null
					|| "".equals(vmgNumModel.getChannel_id()))
			{
				throw new RuntimeException("监控通道号不可为空");
			}
			
			if (vmgNumModel.getComment() == null
					|| "".equals(vmgNumModel.getComment()))
			{
				throw new RuntimeException("描述信息不可为空");
			}
			operResult.setRetCode(OperResult.SUCCESS);
		}
		catch (Exception e)
		{
			operResult.setRetCode(1);
			operResult.setRetMsg(e.getLocalizedMessage());
			logger.error("checkvmgNumModel error, operResult = " + operResult);
		}
		return operResult;
	}
	
}
