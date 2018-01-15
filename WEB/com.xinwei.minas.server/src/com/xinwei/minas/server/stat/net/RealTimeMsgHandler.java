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
 * 实时性能消息处理器
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
		// 解析消息
		StatData statData;
		try {
			statData = parse(message);
			// 增加统计对象到缓存
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
		// 消息上传周期(秒)
		long interval = rsp.getIfPeriod();
		// 时间取服务器时间
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

		// 上行总带宽
		long upTSBW = pf.getTotalUpTSBW();
		// 下行总带宽
		long downTSBW = pf.getTotalDownTSBW();
		// 上行空闲信道数
		long upScgmaskAvailableTotal = pf.getUpScgmaskAvailableTotal();
		// 下行空闲信道数
		long downScgmaskAvailableTotal = pf.getScgmaskAvailableTotal();
		// 上行不可用信道数
		long upScgmaskOccupiedTotal = pf.getUpScgmaskOccupiedTotal();
		// 下行不可用信道数
		long downScgmaskOccupiedTotal = pf.getScgmaskOccupiedTotal();
		// 协议中的TotalTakenPwr平均, EMS中称为: 下行总功率(平均)
		// double totalDownPwrAvg = pf.getTotalDownPwrAvg();
		// 协议中的TotalDownPwr平均, EMS中称为: 下行消耗功率(平均)
		double totalTakenPwrAvg = pf.getTotalTakenPwrAvg();
		// C/I的平均值
		double avgCI = pf.getAvgCI();
		// 并发终端数
		long actMacSess = pf.getActMacSess();
		// 基站注册终端数量
		// long totalUT = pf.getTotalUT();
		// 基站下行时隙数据数组
		PerfDownTS[] bsPfDownTsArray = rsp.getDnts();
		int downTsNum = bsPfDownTsArray.length;
		// 基站上行时隙数据数组
		PerfUpTS[] bsPfUpTsArray = rsp.getUpts();

		// 上行带宽
		itemMap.put(StatUtil.COLLECT_ITEM_BWUP, getFormatValue(new Double(
				upTSBW)));
		// 下行带宽
		itemMap.put(StatUtil.COLLECT_ITEM_BWDOWN, getFormatValue(new Double(
				downTSBW)));
		// 使用功率
		itemMap.put(StatUtil.COLLECT_ITEM_POWER_USED,
				getFormatValue(totalTakenPwrAvg));
		// 总的不可用信道
		itemMap.put(StatUtil.COLLECT_ITEM_CHANNEL_USED,
				getFormatValue(new Double(upScgmaskOccupiedTotal
						+ downScgmaskOccupiedTotal)));
		// 活动用户数
		itemMap.put(StatUtil.COLLECT_ITEM_ACTIVE_USER,
				getFormatValue(new Double(actMacSess)));
		// 信噪比
		itemMap.put(StatUtil.COLLECT_ITEM_AVERAGE_CI, getFormatValue(avgCI));
		// 上行不可用信道
		itemMap.put(StatUtil.COLLECT_ITEM_UNAVAILABLE_UP_CHANNEL,
				getFormatValue(new Double(upScgmaskOccupiedTotal)));
		// 下行不可用信道
		itemMap.put(StatUtil.COLLECT_ITEM_UNAVAILABLE_DOWN_CHANNEL,
				getFormatValue(new Double(downScgmaskOccupiedTotal)));
		// 上行空闲信道
		itemMap.put(StatUtil.COLLECT_ITEM_IDLE_UP_CHANNEL,
				getFormatValue(new Double(upScgmaskAvailableTotal)));
		// 下行空闲信道
		itemMap.put(StatUtil.COLLECT_ITEM_IDLE_DOWN_CHANNEL,
				getFormatValue(new Double(downScgmaskAvailableTotal)));
		// 总空闲信道
		itemMap.put(StatUtil.COLLECT_ITEM_IDLE_CHANNEL,
				getFormatValue(new Double(upScgmaskAvailableTotal
						+ downScgmaskAvailableTotal)));
		// 上行时隙每个SCG的平均CI
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
