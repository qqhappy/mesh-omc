/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-20	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;
import EDU.oswego.cs.dl.util.concurrent.TimeoutException;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.proxy.EnbBizConfigProxy;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbFullTableReverseService;
import com.xinwei.minas.server.enb.task.EnbFullTableTaskManager;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class EnbFullTableReverseServiceImpl implements EnbFullTableReverseService {

	private Log log = LogFactory.getLog(EnbFullTableReverseServiceImpl.class);
	
	private EnbBizConfigProxy enbBizConfigProxy;
	
	public void setEnbBizConfigProxy(EnbBizConfigProxy enbBizConfigProxy) {
		this.enbBizConfigProxy = enbBizConfigProxy;
	}

	@Override
	public void config(Long moId) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		
		if (!enb.isConfigurable()) {
			throw new Exception(
					OmpAppContext.getMessage("enb_cannot_config"));
		}
		
		if (enb.setFullTableOperation(true)) {//�ж��Ƿ�����ã���������Ԫ��ס�����ڽ�����������
			enb.setBizName(EnbConstantUtils.FULL_TABLE_REVERSE);
			try {
				String fileName = EnbFullTableTaskManager.getInstance().createFileName(enb.getHexEnbId());
				
				GenericBizData gbData = new GenericBizData("");
				gbData.addProperty(new GenericBizProperty(EnbConstantUtils.FTP_IP,
						EnbFullTableTaskManager.getInstance().getFtpServerIp()));
				gbData.addProperty(new GenericBizProperty(EnbConstantUtils.FTP_PORT,
						EnbFullTableTaskManager.getInstance().getFtpPort()));
				gbData.addProperty(new GenericBizProperty(EnbConstantUtils.FTP_USER_NAME,
						EnbFullTableTaskManager.getInstance().getFtpUsername()));
				gbData.addProperty(new GenericBizProperty(EnbConstantUtils.FTP_PASSWORD,
						EnbFullTableTaskManager.getInstance().getFtpPassword()));
				gbData.addProperty(new GenericBizProperty(EnbConstantUtils.FILE_DIRECTORY,
						EnbFullTableTaskManager.getInstance()
								.getReverseFtpdDrectory()));
				gbData.addProperty(new GenericBizProperty(EnbConstantUtils.FILE_NAME, fileName));
				
				//����������Ϣ
				enbBizConfigProxy.fullTableConfig(enb.getEnbId(), gbData, 1);
				
				FutureResult result = new FutureResult();
				//���뵽���񻺴棬�ȴ����֪ͨ
				EnbFullTableTaskManager.getInstance().addFullTableReverseTask(enb.getEnbId(), result, fileName);
				//ͬ���ȴ����ý��
				String status = result.timedGet(EnbFullTableTaskManager.getInstance().getOverTime()).toString();
				//������ó������׳��쳣
				if (!EnbFullTableTaskManager.CONFIG_SUCCESS_FLAG.equals(status)) {
					throw new Exception(status);
				}
				
			} catch (TimeoutException e) {
				log.error(e.getMessage());
				throw new Exception(OmpAppContext.getMessage("enb_response_timeout")); 
			} catch (Exception e) {
				log.error(e);
				if (e instanceof java.util.concurrent.TimeoutException) {
					throw new Exception(OmpAppContext.getMessage("enb_response_timeout")); 
				}
				throw e;
			} finally {
				//����Ԫ����Ϊ������״̬
				enb.setBizName("");
				enb.setFullTableOperation(false);
				//�Ƴ�����
				EnbFullTableTaskManager.getInstance().removeFullTableReverseTask(enb.getEnbId());
			}
			
		} else {
			throw new Exception(
					OmpAppContext.getMessage("enb_unconfig_alert",
							new Object[] { OmpAppContext.getMessage(enb
									.getBizName()) }));
		}
	}

}
