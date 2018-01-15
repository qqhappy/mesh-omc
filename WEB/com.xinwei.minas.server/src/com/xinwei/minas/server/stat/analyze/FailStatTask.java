/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.analyze;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.stat.SystemContext;
import com.xinwei.minas.stat.core.StatUtil;
import com.xinwei.minas.stat.core.model.StatDetail;

/**
 * 
 * 失败的统计过程处理任务
 * 
 * @author fanhaoyu
 * 
 */

public class FailStatTask implements Runnable {

	private Log log = LogFactory.getLog(FailStatTask.class);

	private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 4, 3,
			TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20000));

	@Override
	public void run() {
		log.debug("Start failed stat task...... ");
		statFailStat();
		log.debug("Complete failed stat task....");
	}

	/**
	 * 重新统计失败任务
	 */
	private void statFailStat() {
		DataAnalyzer analyzer = new DataAnalyzer();
		SystemContext systemContext = SystemContext.getInstance();
		long index = 0;
		for (int i = 0; i < StatUtil.COLLECT_TYPES.length; i++) {
			try {
				long lasttime = systemContext.getStatDetailDAO()
						.getLatestStatTime(StatUtil.COLLECT_TYPES[i]);// 每个类型的最大统计结束时间

				List<StatDetail> list = systemContext.getStatDetailDAO()
						.getFailedStatDetailBeforeSpecTime(
								StatUtil.COLLECT_TYPES[i], lasttime);

				if (list != null) {
					for (StatDetail statDetail : list) {
						long interval = statDetail.getInterval();
						long statTime = statDetail.getTime();
						StatThread thread = new StatThread(
								StatUtil.COLLECT_TYPES[i], interval,
								systemContext.getStatDataDAO(),
								systemContext.getStatDetailDAO(), analyzer,
								(long) (statTime / 1000) - interval);
						index++;
						threadPool.execute(thread);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Excecute stat fail task error!" + index);
			}
		}
	}

}
