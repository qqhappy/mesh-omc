/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-13	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsAttribute;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.server.mcbts.dao.common.GPSData2Dao;
import com.xinwei.minas.server.mcbts.service.McBtsBizService;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 类简要描述
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsQueryRRUVersionTaskManager {
	Log log = LogFactory.getLog(McBtsQueryRRUVersionTaskManager.class);

	private McBtsBizService service;

	private static McBtsQueryRRUVersionTaskManager instance = null;

	public McBtsQueryRRUVersionTaskManager() {
		super();

		doWork();
	}

	public static McBtsQueryRRUVersionTaskManager getInstance() {
		if (instance == null) {
			return new McBtsQueryRRUVersionTaskManager();
		}

		return instance;
	}

	private void doWork() {
		// 创建定时器
		ScheduledExecutorService scheduledExecutor = Executors
				.newScheduledThreadPool(1);

		// 执行任务
		scheduledExecutor.scheduleAtFixedRate(new VersionUpdate(), 20, 30,
				TimeUnit.SECONDS);
	}

	private class VersionUpdate implements Runnable {
		@Override
		public void run() {
			try {
				doWork();
			} catch (Exception e) {
				log.error("Error querying RRU version.", e);
			}
		}

		private void doWork() throws Exception {			
			if (!OmpAppContext.getCtx().containsBean("mcBtsBizService")) {
				log.warn("mcBtsBizService Bean not found.");
				return;
			}
			service = OmpAppContext.getCtx().getBean(McBtsBizService.class);

			List<McBts> mcbtsList = McBtsCache.getInstance().queryByBtsType(
					McBtsTypeDD.FDDI_MCBTS);

			GenericBizData genericBizData = new GenericBizData(
					"rru_software_version");

			for (McBts mcbts : mcbtsList) {
				if (!mcbts.isConfigurable()) {
					mcbts.addAttribute(McBtsAttribute.Key.MCU_VERSION, "");
					mcbts.addAttribute(McBtsAttribute.Key.FPGA_VERSION, "");
					continue;
				}

				// 向基站查询RRU软件版本
				try {
					GenericBizData result = service.queryFromNE(
							mcbts.getMoId(), genericBizData);

					// mcu版本
					String mcuVersion = String.valueOf(result.getProperty(
							"mcuSoftwareVersion").getValue());

					byte[] b = new byte[4];
					ByteUtils.putHexString(b, 0, mcuVersion);
					mcuVersion = ByteUtils.toVersion(b, 0, 4);

					mcbts.addAttribute(McBtsAttribute.Key.MCU_VERSION,
							mcuVersion);

					// fpga版本
					String fpgaVersion = String.valueOf(result.getProperty(
							"fpgaSoftwareVersion").getValue());

					ByteUtils.putHexString(b, 0, fpgaVersion);
					fpgaVersion = ByteUtils.toVersion(b, 0, 4);

					mcbts.addAttribute(McBtsAttribute.Key.FPGA_VERSION,
							fpgaVersion);
				} catch (Exception e) {
					log.error(
							"Error fetching rru version from ne, The mcbts might not initialize ready.BTS ID:"
									+ mcbts.getHexBtsId(), e);
					mcbts.addAttribute(McBtsAttribute.Key.MCU_VERSION, "");
					mcbts.addAttribute(McBtsAttribute.Key.FPGA_VERSION, "");
				}
			}
		}
	}
}
