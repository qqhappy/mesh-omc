/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.net;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.stat.OriginalDataManager;
import com.xinwei.minas.stat.core.StatUtil;
import com.xinwei.minas.stat.core.model.PerfDownTS;
import com.xinwei.minas.stat.core.model.PerfUpTS;
import com.xinwei.minas.stat.core.model.RealTimePerfData;
import com.xinwei.minas.stat.core.model.RealTimePerfResponse;
import com.xinwei.minas.stat.core.model.StatData;

/**
 * 
 * ʵʱ������Ϣ������
 * 
 * @author fanhaoyu
 * 
 */

public class RealTimeMsgHandler {

	private Log log = LogFactory.getLog(RealTimeMsgHandler.class);

	private static final RealTimeMsgHandler instance = new RealTimeMsgHandler();

	public static RealTimeMsgHandler getInstance() {
		return instance;
	}

	public void handle(McBtsMessage message) {
		// ������Ϣ
		StatData statData;
		try {
			statData = parse(message);
			// ����ͳ�ƶ��󵽻���
			log.debug("Received RealTimePerfResponse :" + statData.toString());
			OriginalDataManager.getInstance().add(statData);
		} catch (Exception e) {
			log.error("parse RealTimePerfResponse error.", e);
		}

	}

	private StatData parse(McBtsMessage message) throws Exception {
		int timeType = StatUtil.COLLECT_TYPE_REALTIME;
		StatData statData = new StatData();
		RealTimePerfResponse rsp = new RealTimePerfResponse();
		rsp.decode(message.getContent(), 0);
		// BTS ID
		long btsId = message.getBtsId();
		// ��Ϣ�ϴ�����(��)
		long interval = rsp.getIfPeriod();
		// ʱ��ȡ������ʱ��
		long collectTime = System.currentTimeMillis();
		statData.setBtsId(btsId);
		statData.setCollectTime(collectTime);
		statData.setInterval(interval);
		statData.setTimeType(timeType);

		statData.setItemMap(createItemDataMap(rsp));

		return statData;
	}

	private Map<Integer, Double> createItemDataMap(RealTimePerfResponse rsp) {

		Map<Integer, Double> itemMap = new HashMap<Integer, Double>();

		RealTimePerfData pf = rsp.getTPfBSRealTime();

		// �����ܴ���
		long upTSBW = pf.getTotalUpTSBW();
		// �����ܴ���
		long downTSBW = pf.getTotalDownTSBW();
		// ���п����ŵ���
		long upScgmaskAvailableTotal = pf.getUpScgmaskAvailableTotal();
		// ���п����ŵ���
		long downScgmaskAvailableTotal = pf.getScgmaskAvailableTotal();
		// ���в������ŵ���
		long upScgmaskOccupiedTotal = pf.getUpScgmaskOccupiedTotal();
		// ���в������ŵ���
		long downScgmaskOccupiedTotal = pf.getScgmaskOccupiedTotal();
		// Э���е�TotalTakenPwrƽ��, EMS�г�Ϊ: �����ܹ���(ƽ��)
		// double totalDownPwrAvg = pf.getTotalDownPwrAvg();
		// Э���е�TotalDownPwrƽ��, EMS�г�Ϊ: �������Ĺ���(ƽ��)
		double totalTakenPwrAvg = pf.getTotalTakenPwrAvg();
		// C/I��ƽ��ֵ
		double avgCI = pf.getAvgCI();
		// �����ն���
		long actMacSess = pf.getActMacSess();
		// ��վע���ն�����
		// long totalUT = pf.getTotalUT();
		// ��վ����ʱ϶��������
		PerfDownTS[] bsPfDownTsArray = rsp.getDnts();
		int downTsNum = bsPfDownTsArray.length;
		// ��վ����ʱ϶��������
		PerfUpTS[] bsPfUpTsArray = rsp.getUpts();

		// ���д���
		itemMap.put(StatUtil.COLLECT_ITEM_BWUP, getFormatValue(new Double(
				upTSBW)));
		// ���д���
		itemMap.put(StatUtil.COLLECT_ITEM_BWDOWN, getFormatValue(new Double(
				downTSBW)));
		// ʹ�ù���
		itemMap.put(StatUtil.COLLECT_ITEM_POWER_USED,
				getFormatValue(totalTakenPwrAvg));
		// �ܵĲ������ŵ�
		itemMap.put(StatUtil.COLLECT_ITEM_CHANNEL_USED,
				getFormatValue(new Double(upScgmaskOccupiedTotal
						+ downScgmaskOccupiedTotal)));
		// ��û���
		itemMap.put(StatUtil.COLLECT_ITEM_ACTIVE_USER,
				getFormatValue(new Double(actMacSess)));
		// �����
		itemMap.put(StatUtil.COLLECT_ITEM_AVERAGE_CI, getFormatValue(avgCI));
		// ���в������ŵ�
		itemMap.put(StatUtil.COLLECT_ITEM_UNAVAILABLE_UP_CHANNEL,
				getFormatValue(new Double(upScgmaskOccupiedTotal)));
		// ���в������ŵ�
		itemMap.put(StatUtil.COLLECT_ITEM_UNAVAILABLE_DOWN_CHANNEL,
				getFormatValue(new Double(downScgmaskOccupiedTotal)));
		// ���п����ŵ�
		itemMap.put(StatUtil.COLLECT_ITEM_IDLE_UP_CHANNEL,
				getFormatValue(new Double(upScgmaskAvailableTotal)));
		// ���п����ŵ�
		itemMap.put(StatUtil.COLLECT_ITEM_IDLE_DOWN_CHANNEL,
				getFormatValue(new Double(downScgmaskAvailableTotal)));
		// �ܿ����ŵ�
		itemMap.put(StatUtil.COLLECT_ITEM_IDLE_CHANNEL,
				getFormatValue(new Double(upScgmaskAvailableTotal
						+ downScgmaskAvailableTotal)));
		// ����ʱ϶ÿ��SCG��ƽ��CI
		for (int i = 0; i < 8 - downTsNum; i++) {
			PerfUpTS bsPfUpTs = bsPfUpTsArray[i];
			for (int j = 0; j < 5; j++) {
				int item = (downTsNum + i + 1) * 10 + (j + 1);
				Long _avgCI = bsPfUpTs.getCIAvg(j);
				itemMap.put(item, getFormatValue(new Double(_avgCI)));
			}
		}
		return itemMap;

	}

	public double getFormatValue(double value) {
		return Double.valueOf(String.format("%.2f", value));
	}

}
