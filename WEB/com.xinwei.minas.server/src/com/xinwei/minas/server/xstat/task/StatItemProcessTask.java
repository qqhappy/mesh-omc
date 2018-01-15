/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-23	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.task;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.xstat.dao.OriginalStatDataDAO;
import com.xinwei.minas.xstat.core.model.StatItem;

/**
 * 
 * ͳ����ͨ��ʵ�崦��������
 * 
 * @author fanhaoyu
 * 
 */

public class StatItemProcessTask implements Runnable {

	private Log log = LogFactory.getLog(StatItemProcessTask.class);

	public static final String PARAM_REPLACER = "p";

	private List<StatItem> itemList;

	public StatItemProcessTask(List<StatItem> itemList) {
		this.itemList = itemList;
	}

	@Override
	public void run() {

		// ���
		try {
			OriginalStatDataDAO statEntityDAO = AppContext.getCtx().getBean(
					OriginalStatDataDAO.class);
			statEntityDAO.save(itemList);
		} catch (Exception e) {
			log.error("save StatItemList failed.", e);
		}
	}

}
