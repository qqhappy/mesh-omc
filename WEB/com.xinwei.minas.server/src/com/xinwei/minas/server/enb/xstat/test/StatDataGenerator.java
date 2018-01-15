/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-8-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.EnbStatEntity;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.enb.core.utils.EnbStatConstants;
import com.xinwei.minas.server.enb.service.EnbBizConfigService;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.xstat.EnbStatModule;
import com.xinwei.minas.server.enb.xstat.analyze.EnbStatAnalyzer;
import com.xinwei.minas.server.enb.xstat.dao.EnbStatItemConfigDAO;
import com.xinwei.minas.server.enb.xstat.util.EnbStatUtil;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.xstat.dao.OriginalStatDataDAO;
import com.xinwei.minas.xstat.core.model.StatItem;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.utils.DateUtils;

/**
 * 
 * ͳ������������-ģ��
 * 
 * @author fanhaoyu
 * 
 */

public class StatDataGenerator {

	private Log log = LogFactory.getLog(StatDataGenerator.class);

	private static StatDataGenerator instance = new StatDataGenerator();

	private Timer timer;

	private Thread thread;

	private List<CounterItemConfig> configList;

	private long startTime = 0;

	private long endTime = 0;

	public static StatDataGenerator getInstance() {
		return instance;
	}

	/**
	 * ���oneByoneΪtrue����������ʱ����ÿ15�����������ݣ����Ϊfalse������startTime��endTimeһ����������������
	 * 
	 * @param oneByOne
	 * @throws Exception
	 */
	public void start(boolean oneByOne) throws Exception {
		if (oneByOne) {
			generateOneByOne();
		} else {
			generateAtOneTime();
		}
	}

	/**
	 * ��ʽΪyyyymmddHHMMSS
	 * 
	 * @param startTime
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * ��ʽΪyyyymmddHHMMSS
	 * 
	 * @param startTime
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * �����������
	 */
	private void generateOneByOne() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		timer = new Timer();
		long currentTime = System.currentTimeMillis();
		long fifteenMin = 15 * 60 * 1000;
		long count = currentTime / fifteenMin;
		long delay = (count + 1) * fifteenMin - currentTime;
		timer.scheduleAtFixedRate(new Generator(), delay, 15 * 60 * 1000);
	}

	/**
	 * һ����������������
	 * 
	 * @throws Exception
	 */
	private void generateAtOneTime() throws Exception {
		if (startTime == 0 || endTime == 0)
			throw new Exception("startTime or endTime must be set.");

		EnbStatItemConfigDAO dao = AppContext.getCtx().getBean(
				EnbStatItemConfigDAO.class);
		try {
			configList = dao.queryCounterConfigs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (thread != null) {
			if (thread.isAlive()) {
				System.out.println("generate at one time is processing.");
				return;
			}
		}
		thread = new Thread(new GeneratorAtOneTime());
		thread.start();
	}

	class GeneratorAtOneTime implements Runnable {
		@Override
		public void run() {
			long startTime = StatDataGenerator.this.startTime;
			long endTime = StatDataGenerator.this.endTime;
			log.info("start generate data at one time. startTime=" + startTime
					+ ", endTime=" + endTime);

			long start = System.currentTimeMillis();

			long fifteenMin = 15 * 60 * 1000;
			startTime = DateUtils.getMillisecondTimeFromBriefTime(startTime);
			endTime = DateUtils.getMillisecondTimeFromBriefTime(endTime);
			while (startTime < endTime) {
				try {
					doWork(startTime);
				} catch (Exception e) {
					log.error(
							"StatDataGenerator generate data failed. startTime="
									+ startTime, e);
				}
				startTime += fifteenMin;
			}

			long end = System.currentTimeMillis();
			long cost = end - start;
			log.info("generate data at one time cost " + cost + " ms");
		}

	}

	class Generator extends TimerTask {

		@Override
		public void run() {
			try {
				log.debug("start generate stat data.");
				doWork(System.currentTimeMillis());
			} catch (Exception e) {
				log.error("Generator do work with error.", e);
			}
		}

	}

	private void doWork(long startTime) throws Exception {

		long endTime = startTime + 60 * 15 * 1000;
		Random random = new Random(startTime);
		List<Enb> enbList = EnbCache.getInstance().queryAll();
		for (Enb enb : enbList) {
			EnbStatEntity entity = new EnbStatEntity();
			entity.setEnbId(enb.getEnbId());
			entity.setStartStatTime(DateUtils
					.getBriefTimeFromMillisecondTime(startTime));
			entity.setEndStatTime(DateUtils
					.getBriefTimeFromMillisecondTime(endTime));
			entity.setVersion(111);
			try {
				List<Integer> cellIdList = queryCellByMoId(enb.getMoId());
				for (CounterItemConfig config : configList) {
					int counterId = config.getCounterId();
					if (config.getStatObject().equals(
							EnbStatConstants.STAT_OBJECT_CELL)) {
						for (Integer cellId : cellIdList) {
							entity.addItem(cellId, counterId,
									Math.abs(random.nextInt() % 100));
						}
					} else {
						entity.addItem(counterId,
								Math.abs(random.nextInt() % 100));
					}
				}
			} catch (Exception e) {
				log.error(
						"generate stat data failed. enbId=" + enb.getHexEnbId(),
						e);
			}
			List<EnbStatEntity> entityList = new ArrayList<EnbStatEntity>();
			entityList.add(entity);
			try {
				process(entityList);
			} catch (Exception e) {
				log.error(
						"process entityList with error. enbId="
								+ enb.getHexEnbId(), e);
			}
		}
	}

	// key=moId, value=cellIdList
	private Map<Long, List<Integer>> cellMap = new HashMap<Long, List<Integer>>();

	/**
	 * ��ѯĳ����վ��С��ID�б�
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	private List<Integer> queryCellByMoId(long moId) throws Exception {
		// ����У���ֱ����cellMap��ȡ������ӵ�cellMap��
		List<Integer> cellIdList = cellMap.get(moId);
		if (cellIdList == null) {
			cellIdList = new ArrayList<Integer>();
			cellMap.put(moId, cellIdList);

			EnbBizConfigService service = AppContext.getCtx().getBean(
					EnbBizConfigService.class);
			XBizTable xBizTable = service.queryFromEms(moId,
					EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null, false);
			List<XBizRecord> recordList = xBizTable.getRecords();
			if (recordList != null && !recordList.isEmpty()) {
				for (XBizRecord xBizRecord : recordList) {
					XBizField field = xBizRecord
							.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
					cellIdList
							.add(Integer.valueOf(field.getValue().toString()));
				}
			}
		}
		return cellIdList;
	}

	public void process(List<EnbStatEntity> entities) throws Exception {
		for (EnbStatEntity entity : entities) {
			// ת��Ϊͨ�õ�ͳ�����б�
			List<StatItem> itemList = new ArrayList<StatItem>();
			Map<Integer, StatItem> btsStatItemMap = new HashMap<Integer, StatItem>();
			Map<Integer, Map<Integer, StatItem>> cellMap = new HashMap<Integer, Map<Integer, StatItem>>();
			Enb enb = EnbCache.getInstance().queryByEnbId(entity.getEnbId());
			Map<String, Double> itemMap = entity.getItemMap();
			for (String key : itemMap.keySet()) {
				StatItem item = null;
				if (key.contains(".")) {
					String[] temp = key.split("\\.");
					int cellId = Integer.valueOf(temp[0]);
					int itemId = Integer.valueOf(temp[1]);

					item = EnbStatUtil.createAreaCounterItem(enb.getMoId(),
							cellId, itemId, itemMap.get(key), entity);
					itemList.add(item);

					Map<Integer, StatItem> counterMap = cellMap.get(cellId);
					if (counterMap == null) {
						counterMap = new HashMap<Integer, StatItem>();
						cellMap.put(cellId, counterMap);
					}
					counterMap.put(itemId, item);
				} else {
					int itemId = Integer.valueOf(key);
					item = EnbStatUtil.createBtsCounterItem(enb.getMoId(),
							itemId, itemMap.get(key), entity);
					itemList.add(item);
					btsStatItemMap.put(itemId, item);
				}
			}
			// ���������counter
			Map<Integer, CounterItemConfig> counterConfigMap = EnbStatModule
					.getInstance().getCounterConfigMap();
			for (StatItem statItem : itemList) {
				CounterItemConfig config = counterConfigMap.get(statItem
						.getItemId());
				String exp = config.getExp();
				if (exp == null || exp.equals(""))
					continue;
				if (config.getStatObject().equals(
						EnbStatConstants.STAT_OBJECT_CELL)) {
					int cellId = Integer.valueOf(statItem.getEntityOid().split(
							"\\.")[1]);
					Map<String, Double> paramMap = EnbStatAnalyzer.getParamMap(
							exp, cellMap.get(cellId));
					double value = EnbStatAnalyzer.calExpressionValue(exp,
							paramMap);
					statItem.setStatValue(value);
				} else {
					Map<String, Double> paramMap = EnbStatAnalyzer.getParamMap(
							exp, btsStatItemMap);
					double value = EnbStatAnalyzer.calExpressionValue(exp,
							paramMap);
					statItem.setStatValue(value);
				}
			}
			if (itemList.isEmpty())
				continue;
			OriginalStatDataDAO statEntityDAO = AppContext.getCtx().getBean(
					OriginalStatDataDAO.class);
			statEntityDAO.save(itemList);
		}
	}

}
