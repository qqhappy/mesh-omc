/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts;

import java.io.FileInputStream;
import java.util.List;

import com.wutka.jox.JOXBeanInputStream;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsAttribute;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.mcbts.core.model.layer1.RFConfig;
import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.mcbts.core.model.oamManage.McbtsSupportedBiz;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsTemplate;
import com.xinwei.minas.server.mcbts.dao.McBtsBasicDAO;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfRfConfigDAO;
import com.xinwei.minas.server.mcbts.dao.layer2.TConfAirlinkConfigDAO;
import com.xinwei.minas.server.mcbts.dao.oamManage.SupportedBizDAO;
import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsTemplateDataManageDao;
import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsTemplateManageDao;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMetaCollection;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.oamManage.UnsupportedMocCache;

/**
 * 
 * McBts模块
 * 
 * @author chenjunhua
 * 
 */

public class McBtsModule {

	private static final McBtsModule instance = new McBtsModule();

	// 协议元数据集合
	private McBtsProtocolMetaCollection protocolMetas;

	private McBtsBasicDAO mcBtsBasicDAO;

	private SupportedBizDAO supportedBizDAO;

	private McBtsTemplateManageDao mcBtsTemplateManageDao;

	private McBtsTemplateDataManageDao mcBtsTemplateDataManageDao;

	private TConfRfConfigDAO rfConfigDAO;

	private TConfAirlinkConfigDAO airlinkConfigDAO;

	private McBtsModule() {
	}

	public static McBtsModule getInstance() {
		return instance;
	}

	/**
	 * 模块初始化
	 */
	public void initialize(McBtsBasicDAO mcBtsBasicDAO,
			SupportedBizDAO supportedBizDAO,
			McBtsTemplateManageDao mcBtsTemplateManageDao,
			McBtsTemplateDataManageDao mcBtsTemplateDataManageDao,
			TConfRfConfigDAO rfConfigDAO, TConfAirlinkConfigDAO airlinkConfigDAO)
			throws Exception {
		this.mcBtsBasicDAO = mcBtsBasicDAO;
		this.supportedBizDAO = supportedBizDAO;
		this.mcBtsTemplateManageDao = mcBtsTemplateManageDao;
		this.mcBtsTemplateDataManageDao = mcBtsTemplateDataManageDao;
		this.rfConfigDAO = rfConfigDAO;
		this.airlinkConfigDAO = airlinkConfigDAO;
		this.initializeProtocolMeta();
		this.initializeMcBtsCache();
		this.initializeSupportedBizCache();
		this.initializeTemplate();
	}

	/**
	 * 获取协议元数据
	 * 
	 * @return
	 */
	public McBtsProtocolMetaCollection getProtocolMetas() {
		return protocolMetas;
	}

	/**
	 * 初始化协议元数据
	 */
	private void initializeProtocolMeta() throws Exception {
		String fileName = "./plugins/mcbts/net/mcbts-protocol.xml";
		FileInputStream inputStream = null;
		JOXBeanInputStream joxIn = null;
		try {
			inputStream = new FileInputStream(fileName);
			joxIn = new JOXBeanInputStream(inputStream);
			protocolMetas = (McBtsProtocolMetaCollection) joxIn
					.readObject(McBtsProtocolMetaCollection.class);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (joxIn != null) {
				joxIn.close();
			}
		}
	}

	/**
	 * 初始化McBTS缓存
	 * 
	 */
	private void initializeMcBtsCache() {
		if (mcBtsBasicDAO != null) {
			List<McBts> allBts = mcBtsBasicDAO.queryAll();
			List<RFConfig> allRfConfig = null;
			List<AirlinkConfig> allAirlinkConfig = null;
			if (rfConfigDAO != null) {
				allRfConfig = rfConfigDAO.queryAll();
			}
			if (airlinkConfigDAO != null) {
				allAirlinkConfig = airlinkConfigDAO.queryAll();
			}

			for (McBts mcBts : allBts) {
				// 加载基站中心频点信息
				if (allRfConfig != null) {
					for (RFConfig rfConfig : allRfConfig) {
						if (rfConfig.getMoId() == mcBts.getMoId()) {
							mcBts.setBtsFreq(rfConfig.getFreqOffset());
							break;
						}
					}
				}

				// 加载基站的签到序列号
				if (allAirlinkConfig != null) {
					for (AirlinkConfig airlink : allAirlinkConfig) {
						if (airlink.getMoId() == mcBts.getMoId())
							mcBts.addAttribute(McBtsAttribute.Key.SEQ_ID,
									airlink.getSequenceId());
					}
				}

				// 在未查询RRU版本之前(服务器刚刚启动),为FDDI插入空版本
				if (mcBts.getBtsType() == McBtsTypeDD.FDDI_MCBTS) {
					mcBts.addAttribute(McBtsAttribute.Key.MCU_VERSION, "");
					mcBts.addAttribute(McBtsAttribute.Key.FPGA_VERSION, "");
				}

				McBtsCache.getInstance().addOrUpdate(mcBts);
			}
		}
	}

	/**
	 * 初始化基站支持的moc缓存
	 * 
	 */
	private void initializeSupportedBizCache() {
		if (supportedBizDAO != null) {
			List<McbtsSupportedBiz> allBiz = supportedBizDAO.queryAll();
			for (McbtsSupportedBiz obj : allBiz) {
				UnsupportedMocCache.getInstance().addOneRecord(
						obj.getBtsType(), obj.getSoftwareVersion(),
						obj.getMoc(), obj.getSupport());
			}
		}
	}

	private void initializeTemplate() {
		if (mcBtsTemplateManageDao != null) {
			List<McBtsTemplate> allTemplates = mcBtsTemplateManageDao
					.queryAll();
			if (allTemplates == null || allTemplates.size() == 0)
				return;
			for (McBtsTemplate mt : allTemplates) {
				McBtsCache.getInstance().addTemplateToMoCache(mt);
			}
		}

		if (mcBtsTemplateDataManageDao != null) {
			// 对模板数据的初始修改
			mcBtsTemplateDataManageDao.updateFtpIp();
		}
	}
}
