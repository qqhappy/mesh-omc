/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wutka.jox.JOXBeanInputStream;
import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbAsset;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.dao.EnbAssetDAO;
import com.xinwei.minas.server.enb.dao.EnbBasicDAO;
import com.xinwei.minas.server.enb.dao.EnbBizConfigDAO;
import com.xinwei.minas.server.enb.enbapi.EnbSceneDataManager;
import com.xinwei.minas.server.enb.helper.EnbBizUniqueIdHelper;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.micro.microapi.MicroEnbSceneDataManager;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XCollection;
import com.xinwei.omp.core.model.meta.XMetaTagCollection;
import com.xinwei.omp.server.cache.XUIMetaCache;
import com.xinwei.omp.server.utils.XCollectionManager;

/**
 * 
 * eNB 主控模块
 * 
 * @author chenjunhua
 * 
 */

public class EnbModule {
	
	private Log log = LogFactory.getLog(EnbModule.class);
	
	private static final EnbModule instance = new EnbModule();
	
	private String uiDefPathEnb = "plugins/enb/biz";
	
	private String uiDefPathMicro = "plugins/micro/biz";
	
	private String tagConfigFile = "plugins/enb/net/tag-config.xml";
	
	private EnbBasicDAO enbBasicDAO;
	
	private EnbBizConfigDAO enbBizConfigDAO;
	
	private EnbBizDataValidateHelper validateHelper;
	
	private XMetaTagCollection tagCollection;
	
	private EnbAssetDAO enbAssetDAO;
	
	// 系统支持的版本列表 : 基站类型-->[支持的基站版本号列表]
	private Map<String, List<String>> supportVersionMap = new HashMap();
	
	// 线程池
	private ExecutorService threadPool = Executors.newFixedThreadPool(1);
	
	private EnbModule() {
	}
	
	public static EnbModule getInstance() {
		return instance;
	}
	
	public void initialize(EnbBasicDAO enbBasicDAO,
			EnbBizDataValidateHelper validateHelper,
			EnbBizConfigDAO enbBizConfigDAO, EnbAssetDAO enbAssetDAO)
			throws Exception {
		this.enbBasicDAO = enbBasicDAO;
		this.validateHelper = validateHelper;
		this.enbBizConfigDAO = enbBizConfigDAO;
		this.enbAssetDAO = enbAssetDAO;
		threadPool.submit(new Runnable() {
			
			public void run() {
				try {
					initGuiMapping();
					initTagConfig();
					initEnbCache();
					initSceneCache();
				}
				catch (Exception e) {
					log.error(
							"EnbModule initialize GuiMapping TagConfig EnbCache failed.",
							e);
				}
				
			}
		});
	}
	
	/**
	 * 初始化GUI映射
	 * 
	 * @throws Exception
	 */
	private void initGuiMapping() throws Exception {
		initGuiMappingByType(uiDefPathEnb);
		
		initGuiMappingByType(uiDefPathMicro);
		// 设置缓存初始化成功
		XUIMetaCache.getInstance().setInitialized();
	}
	
	private void initGuiMappingByType(String filePath) throws Exception {
		File bizConfigDir = new File(filePath);
		File[] enbTypeDirs = bizConfigDir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				name = name.toLowerCase();
				return EnbTypeDD.isSupported(name);
			}
		});
		// TODO:目录名为基站型号，需要做映射
		if (enbTypeDirs == null || enbTypeDirs.length == 0) {
			log.warn("no enb config file.");
			return;
		}
		// 遍历基站型号
		for (File enbTypeDir : enbTypeDirs) {
			String enbTypeName = enbTypeDir.getName();
			int enbTypeId = EnbTypeDD.getTypeIdByName(enbTypeName);
			if (enbTypeId < 0) {
				continue;
			}
			// 根据基站类型生成系统支持的基站版本号列表
			List<String> supportVersions = this
					.generateSupportVersions(enbTypeDir);
			log.debug("load enb config files start. dirName=" + enbTypeName);
			File[] versionDirs = enbTypeDir.listFiles();
			// 遍历每个版本配置目录
			for (File versionDir : versionDirs) {
				
				String version = versionDir.getName();
				if (supportVersions.contains(version)) {
					File[] configFiles = versionDir.listFiles();
					// 遍历版本配置目录中的配置文件
					for (File configFile : configFiles) {
						// 区分不同配置文件
						String fileName = configFile.getName();
						if (fileName.contains("enb-biz")
								&& fileName.endsWith("xml")) {
							loadBizConfig(enbTypeId, version, configFile);
						}
					}
				}
				// finally {
			}
		}
		// 设置缓存初始化成功
		XUIMetaCache.getInstance().setInitialized();
	}
	
	/**
	 * 根据基站类型生成系统支持的基站版本号列表
	 * 
	 * @param enbTypeDir
	 * @return
	 */
	private List<String> generateSupportVersions(File enbTypeDir) {
		List versionList = new LinkedList();
		try {
			List<String> lines = FileUtils.readLines(new File(enbTypeDir,
					"biz-conf.properties"));
			for (String line : lines) {
				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}
				String[] array = line.split(" *= *");
				String key = array[0];
				String value = array[1];
				if (key.equals("support.versions")) {
					String[] versions = value.split(" *, *");
					for (String version : versions) {
						versionList.add(version);
					}
					return versionList;
				}
			}
		}
		catch (Exception e) {
		}
		return new LinkedList();
	}
	
	private void loadBizConfig(int enbTypeId, String version, File configFile)
			throws IOException {
		log.debug("load enb biz file start. fileName=" + configFile.getName()
				+ ", typeId=" + enbTypeId + ", version=" + version);
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(configFile);
			XCollectionManager metaDataManager = XCollectionManager
					.getInstance();
			XCollection moUI = metaDataManager.initialize(inputStream);
			XUIMetaCache.getInstance().addMoBizMetas(MoTypeDD.ENODEB,
					enbTypeId, version, moUI);
		}
		catch (Exception e) {
			log.error("load enb biz file failed. fileName="
					+ configFile.getName() + ", typeId=" + enbTypeId
					+ ", version=" + version);
		}
		finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		log.debug("load enb biz file finish. fileName=" + configFile.getName()
				+ ", typeId=" + enbTypeId + ", version=" + version);
	}
	
	/**
	 * 初始化Tag配置
	 * 
	 * @throws Exception
	 */
	private void initTagConfig() throws Exception {
		InputStream inputStream = null;
		JOXBeanInputStream joxIn = null;
		try {
			inputStream = new FileInputStream(tagConfigFile);
			joxIn = new JOXBeanInputStream(inputStream);
			setTagCollection((XMetaTagCollection) joxIn
					.readObject(XMetaTagCollection.class));
		}
		finally {
			if (joxIn != null) {
				joxIn.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}
	
	/**
	 * 加载开站场景缓存数据
	 */
	private void initSceneCache() {
		EnbSceneDataManager.getInstance().initData(1);
		MicroEnbSceneDataManager.getInstance().initData(1);
	}
	
	/**
	 * 初始化eNB缓存
	 */
	private void initEnbCache() {
		// 加载资产信息到缓存中
		initAsset();
		List<Enb> enbList = enbBasicDAO.queryAll();
		for (Enb enb : enbList) {
			EnbCache.getInstance().addOrUpdate(enb);
			// 设置开站状态
			boolean isActive = validateHelper.checkEnbActive(enb);
			enb.setActive(isActive);
			// 加载小区Id与物理小区ID对应关系
			/*initCellDataByEnb(enb, EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
					EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);*/
			// 加载小区Id与小区参数配置逻辑根序列对应关系
			/*initCellDataByEnb(enb, EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
					EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX);*/
		}
		
	}
	
	/**
	 * 加载资产信息到缓存
	 */
	public void initAsset() {
		try {
			List<EnbAsset> assets = enbAssetDAO.queryAll();
			for (EnbAsset asset : assets) {
				EnbCache.getInstance()
						.addOrUpdateAsset(asset.getEnbId(), asset);
			}
		}
		catch (Exception e) {
			log.error("Init asset error", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 加载enb下所有小区ID与物理小区ID的对应数据,加载enb下所有小区ID与小区参数配置表逻辑根序列对应数据
	 * 
	 * @param enb
	 */
	public void initCellDataByEnb(Enb enb, String tableName, String tableParam) {
		try {
			// 查询当前enb下所有小区表数据
			XBizTable bizTablData = enbBizConfigDAO.query(enb.getMoId(),
					tableName, null);
			if (null == bizTablData) {
				return;
			}
			// 获取拿到该enb下所有小区的数据
			List<XBizRecord> records = bizTablData.getRecords();
			if (null == records) {
				return;
			}
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			
			// 遍历小区数据
			for (XBizRecord xBizRecord : records) {
				// 获取小区ID
				int cellId = xBizRecord
						.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID);
				// 获取对应数据
				int value = xBizRecord.getIntValue(tableParam);
				// 加入对应关系
				map.put(cellId, value);
			}
			
			// 加入enb缓存
			EnbBizUniqueIdHelper.addOrUpdate(tableName, tableParam,
					enb.getEnbId(), map);
		}
		catch (Exception e) {
			log.error("Init cellDataIdByEnb error,enbId=" + enb.getEnbId()
					+ ",table=" + tableName, e);
			e.printStackTrace();
		}
	}
	
	public void setTagCollection(XMetaTagCollection tagCollection) {
		this.tagCollection = tagCollection;
	}
	
	public XMetaTagCollection getTagCollection() {
		return tagCollection;
	}
	
}
