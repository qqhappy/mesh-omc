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
 * eNB ����ģ��
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
	
	// ϵͳ֧�ֵİ汾�б� : ��վ����-->[֧�ֵĻ�վ�汾���б�]
	private Map<String, List<String>> supportVersionMap = new HashMap();
	
	// �̳߳�
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
	 * ��ʼ��GUIӳ��
	 * 
	 * @throws Exception
	 */
	private void initGuiMapping() throws Exception {
		initGuiMappingByType(uiDefPathEnb);
		
		initGuiMappingByType(uiDefPathMicro);
		// ���û����ʼ���ɹ�
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
		// TODO:Ŀ¼��Ϊ��վ�ͺţ���Ҫ��ӳ��
		if (enbTypeDirs == null || enbTypeDirs.length == 0) {
			log.warn("no enb config file.");
			return;
		}
		// ������վ�ͺ�
		for (File enbTypeDir : enbTypeDirs) {
			String enbTypeName = enbTypeDir.getName();
			int enbTypeId = EnbTypeDD.getTypeIdByName(enbTypeName);
			if (enbTypeId < 0) {
				continue;
			}
			// ���ݻ�վ��������ϵͳ֧�ֵĻ�վ�汾���б�
			List<String> supportVersions = this
					.generateSupportVersions(enbTypeDir);
			log.debug("load enb config files start. dirName=" + enbTypeName);
			File[] versionDirs = enbTypeDir.listFiles();
			// ����ÿ���汾����Ŀ¼
			for (File versionDir : versionDirs) {
				
				String version = versionDir.getName();
				if (supportVersions.contains(version)) {
					File[] configFiles = versionDir.listFiles();
					// �����汾����Ŀ¼�е������ļ�
					for (File configFile : configFiles) {
						// ���ֲ�ͬ�����ļ�
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
		// ���û����ʼ���ɹ�
		XUIMetaCache.getInstance().setInitialized();
	}
	
	/**
	 * ���ݻ�վ��������ϵͳ֧�ֵĻ�վ�汾���б�
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
	 * ��ʼ��Tag����
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
	 * ���ؿ�վ������������
	 */
	private void initSceneCache() {
		EnbSceneDataManager.getInstance().initData(1);
		MicroEnbSceneDataManager.getInstance().initData(1);
	}
	
	/**
	 * ��ʼ��eNB����
	 */
	private void initEnbCache() {
		// �����ʲ���Ϣ��������
		initAsset();
		List<Enb> enbList = enbBasicDAO.queryAll();
		for (Enb enb : enbList) {
			EnbCache.getInstance().addOrUpdate(enb);
			// ���ÿ�վ״̬
			boolean isActive = validateHelper.checkEnbActive(enb);
			enb.setActive(isActive);
			// ����С��Id������С��ID��Ӧ��ϵ
			/*initCellDataByEnb(enb, EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
					EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);*/
			// ����С��Id��С�����������߼������ж�Ӧ��ϵ
			/*initCellDataByEnb(enb, EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
					EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX);*/
		}
		
	}
	
	/**
	 * �����ʲ���Ϣ������
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
	 * ����enb������С��ID������С��ID�Ķ�Ӧ����,����enb������С��ID��С���������ñ��߼������ж�Ӧ����
	 * 
	 * @param enb
	 */
	public void initCellDataByEnb(Enb enb, String tableName, String tableParam) {
		try {
			// ��ѯ��ǰenb������С��������
			XBizTable bizTablData = enbBizConfigDAO.query(enb.getMoId(),
					tableName, null);
			if (null == bizTablData) {
				return;
			}
			// ��ȡ�õ���enb������С��������
			List<XBizRecord> records = bizTablData.getRecords();
			if (null == records) {
				return;
			}
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			
			// ����С������
			for (XBizRecord xBizRecord : records) {
				// ��ȡС��ID
				int cellId = xBizRecord
						.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID);
				// ��ȡ��Ӧ����
				int value = xBizRecord.getIntValue(tableParam);
				// �����Ӧ��ϵ
				map.put(cellId, value);
			}
			
			// ����enb����
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
