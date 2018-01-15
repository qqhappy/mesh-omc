/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-15	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.processor;

import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.task.EnbAssetTaskManager;
import com.xinwei.minas.server.enb.task.EnbFullTableTaskManager;

/**
 * 
 * enb������Ϣ��������
 * 
 * 
 * @author zhuxiaozhan
 * 
 */

public class EnbConfProcessor extends EnbMessageProcessor  {

	@Override
	public void doWork(EnbAppMessage message) {
		int moc = message.getMoc();
		switch(moc) {
		//�������ý��֪ͨ
		case EnbMessageConstants.MOC_FULLTABLECONFIG_NOTIFY:
			EnbFullTableTaskManager.getInstance()
					.handleFullTableConfigResultNotify(message);
			break;
		// ���������֪ͨ
		case EnbMessageConstants.MOC_FULLTABLEREVERSE_NOTIFY:
			EnbFullTableTaskManager.getInstance()
					.handleFullTableReverseResultNotify(message);
			break;
		// �ʲ���Ϣ�ϱ�
		case EnbMessageConstants.MOC_ASSET_INFO_NOTIFY:
			EnbAssetTaskManager.getInstance().handler(message);
		}
	}
	
	/**
	 * ��������
	 *
	 */
	@Override
	public void finishWork() {
		
	}

}
