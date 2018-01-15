/*      						
 * Copyright 3013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 3013-11-33	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer2;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.xinwei.minas.mcbts.core.model.common.MicroBtsSignalSendSetting;
import com.xinwei.omp.core.model.biz.FieldProperty;
import com.xinwei.omp.core.model.biz.Listable;

/**
 * 
 * 空中链路配置模型
 * 
 * @author jiayi
 * 
 */

public class AirlinkConfig implements Serializable, Listable {

	public static final int SEQ_COUNT = 16;

	public static final int SCG_COUNT = 5;

	public static final int W0_COUNT = 8;
	
	//微蜂窝基站的WO个数
	public static final int MICRO_W0_COUNT = 2;

	public static final int TS_COUNT = 8;

	public static final int BCH_COUNT = 10;

	public static final int RRCH_COUNT = 10;

	public static final int RARCH_COUNT = 20;

	public static final int FACH_COUNT = 10;

	public static final int RPCH_COUNT = 10;

	public final static int SCG_CHANNEL_COUNT = 16;

	public static final double DEFAULT_MAXSCALE = 0.25;

	private final String[] TIMESLOTVALUE = new String[] { "FORBIDDEN", "BCH",
			"BCHN1", "RRCH", "RRCHN1", "RARCH", "RARCHN1", "RACH", "RACHN1",
			"FACH", "FACHN1", "RPCH", "RPCHN1" };
	
	private final String[] PECCHTIMESLOTVALUE = new String[] {
		"EBCH", "EPCH", "ERARCH", "ERRCH", "ERACH" };

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 前导序列号
	private Integer sequenceId;

	// 前导系数
	private Integer preambleScale;

	// 最大系数
	private Integer maxScale;

	// 总时隙数
	private Integer totalTS;

	// 下行时隙
	private Integer downlinkTS;

	// 子载波组掩码
	private Integer scgMask;

	// TCH禁用
	private String tchForbidden;

	// 载波组通道信息配置
	// TODO
	private List<SCGChannelConfigItem> scgChannelConfigList = new ArrayList<SCGChannelConfigItem>();

	// 载波组scale配置
	private List<SCGScaleConfigItem> scgScaleConfigList = new ArrayList<SCGScaleConfigItem>();

	// W0配置
	private List<W0ConfigItem> w0ConfigList = new ArrayList<W0ConfigItem>();

	private MicroBtsSignalSendSetting sendSetting;

	// 强制终端重新注册
	private boolean forceUTRegist = false;

	// 判断是否支持小包
	private boolean smallPkgSupport = true;

	private PECCHSetting pecchSetting;

	public AirlinkConfig() {
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	/**
	 * @return the sequenceId
	 */
	public Integer getSequenceId() {
		return sequenceId;
	}

	/**
	 * @param sequenceId
	 *            the sequenceId to set
	 */
	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	/**
	 * @return the preambleScale
	 */
	public Integer getPreambleScale() {
		return preambleScale;
	}

	/**
	 * @param preambleScale
	 *            the preambleScale to set
	 */
	public void setPreambleScale(Integer preambleScale) {
		this.preambleScale = preambleScale;
	}

	/**
	 * @return the maxScale
	 */
	public Integer getMaxScale() {
		return maxScale;
	}

	/**
	 * @param maxScale
	 *            the maxScale to set
	 */
	public void setMaxScale(Integer maxScale) {
		this.maxScale = maxScale;
	}

	/**
	 * @return the totalTS
	 */
	public Integer getTotalTS() {
		return totalTS;
	}

	/**
	 * @param totalTS
	 *            the totalTS to set
	 */
	public void setTotalTS(Integer totalTS) {
		this.totalTS = totalTS;
	}

	/**
	 * @return the downlinkTS
	 */
	public Integer getDownlinkTS() {
		return downlinkTS;
	}

	/**
	 * @param downlinkTS
	 *            the downlinkTS to set
	 */
	public void setDownlinkTS(Integer downlinkTS) {
		this.downlinkTS = downlinkTS;
	}

	/**
	 * @return the scgMask
	 */
	public Integer getScgMask() {
		return scgMask;
	}

	/**
	 * @param scgMask
	 *            the scgMask to set
	 */
	public void setScgMask(Integer scgMask) {
		this.scgMask = scgMask;
	}

	/**
	 * @return the tchForbidden
	 */
	public String getTchForbidden() {
		return tchForbidden;
	}

	/**
	 * @param tchForbidden
	 *            the tchForbidden to set
	 */
	public void setTchForbidden(String tchForbidden) {
		this.tchForbidden = tchForbidden;
	}

	/**
	 * @return the w0ConfigList
	 */
	public List<W0ConfigItem> getW0ConfigList() {
		return w0ConfigList;
	}

	/**
	 * @param w0ConfigList
	 *            the w0ConfigList to set
	 */
	public void setW0ConfigList(List<W0ConfigItem> w0ConfigList) {
		this.w0ConfigList = w0ConfigList;
	}

	/**
	 * @return the scgChannelConfigList
	 */
	public List<SCGChannelConfigItem> getScgChannelConfigList() {
		return scgChannelConfigList;
	}

	/**
	 * @param scgChannelConfigList
	 *            the scgChannelConfigList to set
	 */
	public void setScgChannelConfigList(
			List<SCGChannelConfigItem> scgChannelConfigList) {
		this.scgChannelConfigList = scgChannelConfigList;
	}

	/**
	 * @return the scgScaleConfigList
	 */
	public List<SCGScaleConfigItem> getScgScaleConfigList() {
		return scgScaleConfigList;
	}

	/**
	 * @param scgScaleConfigList
	 *            the scgScaleConfigList to set
	 */
	public void setScgScaleConfigList(
			List<SCGScaleConfigItem> scgScaleConfigList) {
		this.scgScaleConfigList = scgScaleConfigList;
	}

	public void setSendSetting(MicroBtsSignalSendSetting sendSetting) {
		this.sendSetting = sendSetting;
	}

	public MicroBtsSignalSendSetting getSendSetting() {
		return sendSetting;
	}

	/**
	 * @return the forceUTRegist
	 */
	public boolean isForceUTRegist() {
		return forceUTRegist;
	}

	/**
	 * @param forceUTRegist
	 *            the forceUTRegist to set
	 */
	public void setForceUTRegist(boolean forceUTRegist) {
		this.forceUTRegist = forceUTRegist;
	}

	public boolean isSmallPkgSupport() {
		return smallPkgSupport;
	}

	public void setSmallPkgSupport(boolean smallPkgSupport) {
		this.smallPkgSupport = smallPkgSupport;
	}

	/**
	 * @return the pecchSetting
	 */
	public PECCHSetting getPecchSetting() {
		return pecchSetting;
	}

	/**
	 * @param pecchSetting the pecchSetting to set
	 */
	public void setPecchSetting(PECCHSetting pecchSetting) {
		this.pecchSetting = pecchSetting;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((downlinkTS == null) ? 0 : downlinkTS.hashCode());
		result = prime * result
				+ ((maxScale == null) ? 0 : maxScale.hashCode());
		result = prime * result
				+ ((preambleScale == null) ? 0 : preambleScale.hashCode());
		result = prime * result + ((scgMask == null) ? 0 : scgMask.hashCode());
		result = prime
				* result
				+ ((scgScaleConfigList == null) ? 0 : scgScaleConfigList
						.hashCode());
		result = prime * result
				+ ((sequenceId == null) ? 0 : sequenceId.hashCode());
		result = prime * result
				+ ((tchForbidden == null) ? 0 : tchForbidden.hashCode());
		result = prime * result + ((totalTS == null) ? 0 : totalTS.hashCode());
		result = prime * result
				+ ((w0ConfigList == null) ? 0 : w0ConfigList.hashCode());
		result = prime * result
				+ ((pecchSetting == null) ? 0 : pecchSetting.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AirlinkConfig other = (AirlinkConfig) obj;
		if (downlinkTS == null) {
			if (other.downlinkTS != null)
				return false;
		} else if (!downlinkTS.equals(other.downlinkTS))
			return false;
		if (maxScale == null) {
			if (other.maxScale != null)
				return false;
		} else if ((maxScale.intValue() - other.maxScale.intValue()) > 1)
			return false;
		if (preambleScale == null) {
			if (other.preambleScale != null)
				return false;
		} else if ((preambleScale.intValue() - other.preambleScale.intValue()) > 1)
			return false;
		if (scgChannelConfigList == null) {
			if (other.scgChannelConfigList != null)
				return false;
		} else if (!scgChannelConfigList.equals(other.scgChannelConfigList)) {
			
			List<SCGChannelConfigItem> tempList = new ArrayList<SCGChannelConfigItem>(
					scgChannelConfigList);
		
			for (Iterator<SCGChannelConfigItem> iter = scgChannelConfigList
					.iterator(); iter.hasNext();) {
				SCGChannelConfigItem item = iter.next();
				if (item.getChannelType() >= SCGChannelConfigItem.RACH) {
					tempList.remove(item);
				}
			}
			Collections.sort(tempList);
			Collections.sort(other.scgChannelConfigList);
			if (!tempList.equals(other.scgChannelConfigList)) {
				if (!other.smallPkgSupport) {
					for (Iterator<SCGChannelConfigItem> iter = scgChannelConfigList
							.iterator(); iter.hasNext();) {
						SCGChannelConfigItem item = iter.next();
						if (item.getChannelType() >= SCGChannelConfigItem.RACH
								|| item.getChannelType() % 2 == 0)
							iter.remove();
					}
					Collections.sort(scgChannelConfigList);
					Collections.sort(other.scgChannelConfigList);
					if (!scgChannelConfigList.equals(other.scgChannelConfigList))
						return false;
				} else {
					return false;
				}
			}
		}

		if (scgMask == null) {
			if (other.scgMask != null)
				return false;
		} else if (!scgMask.equals(other.scgMask))
			return false;
		if (scgScaleConfigList == null) {
			if (other.scgScaleConfigList != null)
				return false;
		} else if (!scgScaleConfigList.equals(other.scgScaleConfigList))
			return false;
		if (sequenceId == null) {
			if (other.sequenceId != null)
				return false;
		} else if (!sequenceId.equals(other.sequenceId))
			return false;
		if (tchForbidden == null) {
			if (other.tchForbidden != null)
				return false;
		} else if (!tchForbidden.equals(other.tchForbidden))
			return false;
		if (totalTS == null) {
			if (other.totalTS != null)
				return false;
		} else if (!totalTS.equals(other.totalTS))
			return false;
		if (w0ConfigList == null) {
			if (other.w0ConfigList != null)
				return false;
		} else if (!w0ConfigList.equals(other.w0ConfigList))
			return false;
		if (pecchSetting == null) {
			if (other.pecchSetting != null)
				return false;
		} else if (!pecchSetting.equals(other.pecchSetting))
			return false;
		return true;
	}

	@Override
	public List<FieldProperty> listAll() {
		return listAll(true);
	}

	public List<FieldProperty> listAll(boolean flag) {
		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		// 前导序列号
		allProperties
				.add(new FieldProperty(0, "listable.AirlinkConfig.sequenceId",
						String.valueOf(sequenceId)));
		// 前导系数
		allProperties.add(new FieldProperty(0,
				"listable.AirlinkConfig.preambleScale", String
						.valueOf(preambleScale)));
		// 最大系数
		allProperties.add(new FieldProperty(0,
				"listable.AirlinkConfig.maxScale", String.valueOf(maxScale)));
		// 总时隙数
		allProperties.add(new FieldProperty(0,
				"listable.AirlinkConfig.totalTS", String.valueOf(totalTS)));
		// 下行时隙
		allProperties
				.add(new FieldProperty(0, "listable.AirlinkConfig.downlinkTS",
						String.valueOf(downlinkTS)));
		// 子载波组掩码
		allProperties.add(new FieldProperty(0,
				"listable.AirlinkConfig.scgMask", String.valueOf(scgMask)));
		// TCH禁用
		allProperties.add(new FieldProperty(0,
				"listable.AirlinkConfig.tchForbidden", tchForbidden));

		Map<Integer, List<SCGChannelConfigItem>> carrierSlotMap = new HashMap<Integer, List<SCGChannelConfigItem>>();

		// 载波组只有5组
		for (int i = 0; i < SCG_COUNT; i++) {
			carrierSlotMap.put(i, new ArrayList<SCGChannelConfigItem>());
		}

		for (SCGChannelConfigItem scgChannelConfigItem : scgChannelConfigList) {
			if (scgChannelConfigItem.getChannelType() == SCGChannelConfigItem.RACH) {
				continue;
			}
			Integer scgIndex = scgChannelConfigItem.getScgIndex(); // 载波组索引
			List<SCGChannelConfigItem> scgItemList = carrierSlotMap
					.get(scgIndex);
			scgItemList.add(scgChannelConfigItem);
		}

		Map<Integer, SCGScaleConfigItem> timeSlotMap = new HashMap<Integer, SCGScaleConfigItem>();

		for (SCGScaleConfigItem scgScaleConfigItem : scgScaleConfigList) {
			Integer tsIndex = scgScaleConfigItem.getTsIndex();
			timeSlotMap.put(tsIndex, scgScaleConfigItem);
		}

		for (int i = 0; i < carrierSlotMap.size(); i++) {
			List<SCGChannelConfigItem> scgItemList = carrierSlotMap.get(i);
			allProperties.add(new FieldProperty(0,
					"listable.AirlinkConfig.carrier" + i, "")); // 载波组X
			
			for (int j = 0; j < TS_COUNT; j++) {
				allProperties.add(new FieldProperty(1,
						"listable.AirlinkConfig.timeSlot" + j, "")); //时隙X
				String carrier = "";
				List<Integer> indexList = new LinkedList<Integer>();;
				if (pecchSetting != null && i == pecchSetting.getScgIndex()) {
					indexList.addAll(getPECCHIndex(pecchSetting, j));
					Collections.sort(indexList);
					for (Integer index : indexList) {
						if("".equals(carrier)) {
							carrier = PECCHTIMESLOTVALUE[index];
						} else {
							carrier = carrier + "/" + PECCHTIMESLOTVALUE[index];
						}
					}
				} else {
					for (SCGChannelConfigItem scgChannelConfigItem : scgItemList) {
						if (scgChannelConfigItem.getTsIndex() == j) {
							indexList.add(scgChannelConfigItem.getChannelType());
						}
					}
					Collections.sort(indexList);
					for (Integer index : indexList) {
						if("".equals(carrier)) {
							carrier = TIMESLOTVALUE[index];
						} else {
							carrier = carrier + "/" + TIMESLOTVALUE[index];
						}
					}
				}
				
				allProperties.add(new FieldProperty(2,
						"listable.AirlinkConfig.carrier", carrier)); //通道类型
				SCGScaleConfigItem scgScale = timeSlotMap.get(j);
				double tchValue = 0;
				double bchValue = 0;
				if (scgScale != null) {
					tchValue = scgScale.getTchScale() / 10000.0;
					bchValue = scgScale.getBchScale() / 10000.0;
				}
				allProperties.add(new FieldProperty(2,
						"listable.AirlinkConfig.carrier5", String
								.valueOf(tchValue))); // TCH SCALE
				allProperties.add(new FieldProperty(2,
						"listable.AirlinkConfig.carrier6", String
								.valueOf(bchValue))); // BCH SCALE

			}

		}
		// w0信息设置
		allProperties.add(new FieldProperty(0,
				"listable.AirlinkConfig.w0InfoSettings", "w0_i/w0_q"));

		Collections.sort(w0ConfigList, new Comparator<W0ConfigItem>() {
			@Override
			public int compare(W0ConfigItem arg0, W0ConfigItem arg1) {
				return arg0.getAntennaIndex() - arg1.getAntennaIndex();
			}
		});

		for (W0ConfigItem item : w0ConfigList) {
			Integer antennaIndex = item.getAntennaIndex();
			Double w0I = item.getW0I();
			Double w0Q = item.getW0Q();

			allProperties.add(new FieldProperty(1,
					String.valueOf(antennaIndex), String.valueOf(w0I) + "/"
							+ String.valueOf(w0Q)));
		}

		return allProperties;
	}
	
	
	public static String w02Double(int value) {
		double ret = ((double) value) / 32767.0;
		DecimalFormat decimalFormat = new DecimalFormat("#.####");
		return decimalFormat.format(ret);
	}

	public String getBizName() {
		return "mcbts_airlink";
	}
	
	public List<Integer> getPECCHIndex(PECCHSetting pecchSetting, int ts_index) {
		List<Integer> indexList = new ArrayList<Integer>();
		int ebch_index = 0;
		Integer pchCount = pecchSetting.getPchCount();
		Integer rarchCount = pecchSetting.getRarchCount();
		Integer rrchCount = pecchSetting.getRrchCount();
		// 获得各时隙的PECCH公共信道并进行设置
		if (ts_index == 0) {
			// PECCH公共信道第一时隙被EBCH占据
			indexList.add(ebch_index);
		} 
		if (ts_index == 1) {
			// PECCH公共信道从第二时隙开始包含EPCH
			indexList.add(ebch_index + 1);
		}
		if ((ts_index >= (pchCount * PECCHSetting.PER_EPCH_OCCUPIED_CHANNELS)
				/ AirlinkConfig.SCG_CHANNEL_COUNT + 1)
				&& (ts_index <= floor(pchCount
						* PECCHSetting.PER_EPCH_OCCUPIED_CHANNELS
						+ rarchCount
						* PECCHSetting.PER_ERARCH_OCCUPIED_CHANNELS,
						AirlinkConfig.SCG_CHANNEL_COUNT))) {
			// PECCH公共信道是否包含ERARCH
			indexList.add(ebch_index + 2);
		}
		if ((ts_index >= (pchCount * PECCHSetting.PER_EPCH_OCCUPIED_CHANNELS + rarchCount
				* PECCHSetting.PER_ERARCH_OCCUPIED_CHANNELS)
				/ AirlinkConfig.SCG_CHANNEL_COUNT + 1)
				&& (ts_index <= floor(pchCount
						* PECCHSetting.PER_EPCH_OCCUPIED_CHANNELS
						+ rarchCount
						* PECCHSetting.PER_ERARCH_OCCUPIED_CHANNELS
						+ rrchCount
						* PECCHSetting.PER_ERRCH_OCCUPIED_CHANNELS,
						AirlinkConfig.SCG_CHANNEL_COUNT))) {
			// PECCH公共信道是否包含ERRCH
			indexList.add(ebch_index + 3);
		}
		if ((ts_index >= (pchCount * PECCHSetting.PER_EPCH_OCCUPIED_CHANNELS)
				/ AirlinkConfig.SCG_CHANNEL_COUNT + 1 + downlinkTS)
				&& (ts_index <= floor(pchCount
						* PECCHSetting.PER_EPCH_OCCUPIED_CHANNELS
						+ rarchCount
						* PECCHSetting.PER_ERARCH_OCCUPIED_CHANNELS,
						AirlinkConfig.SCG_CHANNEL_COUNT)
						+ downlinkTS)) {
			// PECCH公共信道是否包含ERACH
			indexList.add(ebch_index + 4);
		}
		
		return indexList;
	}
	
	private int floor(int value, int factor) {
		if (value % factor == 0) {
			return value / factor;
		}
		return value / factor + 1;
	}
}
