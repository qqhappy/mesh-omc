package com.xinwei.minas.server.mcbts.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.facade.McBtsBasicFacade;
import com.xinwei.minas.mcbts.core.facade.layer1.CalibrationDataFacade;
import com.xinwei.minas.mcbts.core.facade.layer2.AirlinkFacade;
import com.xinwei.minas.mcbts.core.facade.layer3.NeighbourFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.FreqRelatedConfigure;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationDataInfo;
import com.xinwei.minas.mcbts.core.model.layer1.RFConfig;
import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.mcbts.core.model.layer2.SCGChannelConfigItem;
import com.xinwei.minas.mcbts.core.utils.FreqConvertUtil;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.server.OmpAppContext;

public class FreqConfigurationValidator {

	
	// 同频组网配置验证相关的参数
	public static String validateFreqConfiguration(FreqRelatedConfigure freqconf)
			throws Exception {

		if (isTemplate(freqconf.getMoId()))
			return "";

		try {
			// 设置要验证的数据对象
			initData(freqconf);
		} catch (Exception e) {
			throw new Exception(
					OmpAppContext.getMessage("validata_error.bts_init_data")
							+ e.getMessage());
		}

		//判断是否允许强制配置
		hasAddNewNeighbor(freqconf);
		
		// //频点类型+频点偏移量+前导序列号作为key
		Map<String, ArrayList<Long>> btsFreqSeqInfo = null;
		try {
			// 获取所有基站频点及前导序列号信息
			NeighbourFacade neighbourFacade = AppContext.getCtx().getBean(
					NeighbourFacade.class);
			btsFreqSeqInfo = neighbourFacade.querySameFreqSeqInfo();
		} catch (Exception e) {
			btsFreqSeqInfo = new HashMap<String, ArrayList<Long>>();
		}

		// 获取所有要设置为邻接表的基站
		List<McBts> neighborsToSet = getNeighborsToSet(freqconf);
		
		McBts curBts = freqconf.getBts();
		String freqType = curBts.getBtsFreqType() + "#";
		String retstr = "";
		
		String ffKey = (freqconf.getBts().getBtsFreqType()) + "#"
		+ freqconf.getRfconf().getFreqOffset() + "#";
		
		List<Long> btsFreqList = new ArrayList<Long>();
		
		List<ArrayList<String>> validateFreqSeqBts = new ArrayList<ArrayList<String>>();
		
		// 同频同前导序列号校验
		for (String key : btsFreqSeqInfo.keySet()) {
			
			//跳过不是该频点类型的信息
			if (!key.startsWith(freqType)) {
				continue;
			}
			ArrayList<Long> btsList = btsFreqSeqInfo.get(key);
			if (btsList == null || btsList.size() == 0) {
				continue;
			}
			if (key.startsWith(ffKey)) {
				btsFreqList.addAll(btsList);
			}
			for (McBts neighbor : neighborsToSet) {
				List<String> sameFreqSeqBts = new ArrayList<String>();
				List<McBts> curNodeNeighbors = null;
				if (neighbor.getMoId() == curBts.getMoId()) {
					curNodeNeighbors = neighborsToSet;
				} else {
					curNodeNeighbors = getAllNeighbor(neighbor.getMoId());
					curNodeNeighbors.add(neighbor);
				}
				if (!curNodeNeighbors.contains(curBts)) {
					curNodeNeighbors.add(curBts);
				}
				for (McBts curNode : curNodeNeighbors) {
					if (btsList.contains(curNode.getMoId())) {
						sameFreqSeqBts.add(curNode.getHexBtsId());
					}
				}
				if (sameFreqSeqBts.size() > 1) {
					processResult(sameFreqSeqBts, validateFreqSeqBts);
				}
			}
		}
		
		retstr = getTheResult(validateFreqSeqBts);
		
		if (!retstr.equals("")) {
			retstr += OmpAppContext.getMessage("validata_error.bts_cannot_be_neighbor")
					+ "\r\n";
			if (freqconf.isPermitForceConfig()) {
				return retstr;
			} else {
				throw new Exception(retstr);
			}
			
		}

		// 获取同频、异频邻接表基站
		List<McBts> neighbors = freqconf.getNeighborList();
		List<McBts> sameFreqNeighbors = new LinkedList<McBts>();
		List<McBts> diffFreqNeighbors = new LinkedList<McBts>();
		for (McBts neighbor : neighbors) {
			boolean isSameFreq = false;
			if (btsFreqList.contains(neighbor.getMoId())) {
				isSameFreq = true;
			}
			if (isSameFreq) {
				if (!sameFreqNeighbors.contains(neighbor)) {
					sameFreqNeighbors.add(neighbor);
				}
			} else if (!diffFreqNeighbors.contains(neighbor)) {
				diffFreqNeighbors.add(neighbor);
			}
		}

		//获取附加邻接表中的异频邻接站,以及邻接站的邻接表中的异频基站
		List<McBts> appendNeighbors = freqconf.getAppendNeighborList();
		List<McBts> tempBtsList = new ArrayList<McBts>();
		tempBtsList.addAll(freqconf.getNeighborList());
		tempBtsList.addAll(freqconf.getAppendNeighborList());
		List<McBts> allNeighborList = new ArrayList<McBts>();
		allNeighborList.addAll(appendNeighbors);
		for (McBts bts : tempBtsList) {
			List<McBts> nbList = getAllNeighbor(bts.getMoId());
			for (McBts tempBts : nbList) {
				if (!allNeighborList.contains(tempBts)
						&& !tempBtsList.contains(tempBts)
						&& tempBts.getMoId() != curBts.getMoId()) {
					allNeighborList.add(tempBts);
				}
			}
		}
		
		for (McBts neighbor : allNeighborList) {
			boolean isSameFreq = false;
			if (btsFreqList.contains(neighbor.getMoId())) {
				isSameFreq = true;
			}
			if (!isSameFreq) {
				if (!diffFreqNeighbors.contains(neighbor)) {
					diffFreqNeighbors.add(neighbor);
				}
			}
		}
		
		// 校验当前基站与其邻接同频基站之间控制信道冲突
		if (!sameFreqNeighbors.isEmpty()) {
			for (McBts sameFreqNeighbor : sameFreqNeighbors) {
				AirlinkConfig airNeighbor = null;
				try {
					// 获取同频邻接基站的空中链路配置信息
					AirlinkFacade facade = AppContext.getCtx().getBean(
							AirlinkFacade.class);
					airNeighbor = facade.queryConfigByMoId(sameFreqNeighbor
							.getMoId());
				} catch (Exception e) {
				}
				String tmp = validateChannelConflict(freqconf.getAirlink(),
						airNeighbor, freqconf.getBts(), sameFreqNeighbor);
				if (!tmp.equals("")) {
					tmp += "\r\n";
					retstr += tmp;
				}
			}
		}
		if (!retstr.equals("")) {
			if (freqconf.isPermitForceConfig()) {
				return retstr;
			} else {
				throw new Exception(retstr);
			}
		}

		// 校验当前基站与其邻接非同频基站之间频点冲突
		if (!diffFreqNeighbors.isEmpty()) {
			for (McBts diffFreqNeighbor : diffFreqNeighbors) {
				RFConfig rfNeighbor = null;
				try {
					// 获取非同频邻接基站的校准数据配置信息
					CalibrationDataFacade calibFacade = AppContext.getCtx().getBean(CalibrationDataFacade.class);
					CalibrationDataInfo calibData = calibFacade
							.queryByMoId(diffFreqNeighbor.getMoId());
					rfNeighbor = calibData.getRfConfig();
				} catch (Exception e) {
				}
				String tmp = validateFreqDiff(freqconf.getRfconf(), rfNeighbor,
						freqconf.getBts(), diffFreqNeighbor);
				if (!tmp.equals("")) {
					tmp += "\r\n";
					retstr += tmp;
				}
			}
		}
		
		return retstr;
	}
	
	private static List<McBts> getNeighborsToSet(FreqRelatedConfigure freqconf) throws Exception{
		List<McBts> neighbors = new LinkedList<McBts>();

		neighbors.add(freqconf.getBts());

		// 邻接表
		List<McBts> neighborList = freqconf.getNeighborList();
		for (McBts bts : neighborList) {
			if (!neighbors.contains(bts)) {
				neighbors.add(bts);
			}
		}

		//附加邻接表
		List<McBts> appendNeighborList = freqconf.getAppendNeighborList();
		for (McBts mcBts : appendNeighborList) {
			if (!neighbors.contains(mcBts)) {
				neighbors.add(mcBts);
			}
		}
		
		return neighbors;
	}

	/**
	 * 获取所有的邻接站
	 * @param moId
	 * @return
	 */
	public static List<McBts> getAllNeighbor(Long moId) {
		List<McBts> nbList = new ArrayList<McBts>();
		try {
			NeighbourFacade neighbourFacade = AppContext.getCtx().getBean(
					NeighbourFacade.class);
			List<McBts> tempNb = neighbourFacade.queryNeighbour(moId);
			List<McBts> tempAppendNb = neighbourFacade.queryAppendNeighbour(moId);
			for (McBts bts : tempNb) {
				if (!nbList.contains(bts)) {
					nbList.add(bts);
				}
			}
			
			for (McBts bts : tempAppendNb) {
				if (!nbList.contains(bts)) {
					nbList.add(bts);
				}
			}
		} catch (Exception e) {
			
		}
		
		return nbList;
	}
	
	private static String validateFreqDiff(RFConfig rfconf1, RFConfig rfconf2,
			McBts bts, McBts neighbor) throws Exception {

		String retstr = "";

		FreqConvertUtil freqUtil = new FreqConvertUtil();
		freqUtil.setFreqType(bts.getBtsFreqType());

		// 频率不同
		if (rfconf1.getFreqOffset().intValue() != rfconf2.getFreqOffset()
				.intValue()) {
			double freqDiff = freqUtil.getMidFreqValue(rfconf1.getFreqOffset())
					- freqUtil.getMidFreqValue(rfconf2.getFreqOffset());
			int coverage = (int) Math.abs(rfconf1.getFreqOffset()
					- rfconf2.getFreqOffset());
			int diff = (int) (5.00 / freqUtil.getStep());
			if (coverage % diff != 0) {
				// 差值不是5的倍数
				retstr += OmpAppContext.getMessage(
						"validata_error.bts_freq_overlap", new Object[]{ bts.getHexBtsId(),
								neighbor.getHexBtsId(), "" + freqDiff});
			}
		}
		return retstr;
	}

	private static String validateChannelConflict(AirlinkConfig air1,
			AirlinkConfig air2, McBts bts, McBts neighbor) {

		String retstr = "";

		if (air1 == null || air2 == null) {
			return retstr;
		}

		// 校验BCH/BCHN1通道定义是否有冲突
		Map<String, Object> mp = new HashMap<String, Object>();
		retstr += validateConflictOfBCH(air1, air2, mp, bts, neighbor);
		retstr += validateConflictOfBCH(air2, air1, mp, bts, neighbor);

		return retstr;
	}

	// 检查BCH/BCHN1 List是否有冲突
	private static String validateConflictOfBCH(AirlinkConfig air1,
			AirlinkConfig air2, Map<String, Object> conflictmp, McBts bts,
			McBts neighbor) {

		String tmpstr = "";

		int sid = air1.getSequenceId().intValue();
		int sid2 = air2.getSequenceId().intValue();

		int bch1_val1 = (2 * sid) % 14;
		int bch1_val2 = (2 * sid + 1) % 14;
		int bch2_val1 = (2 * sid2) % 14;
		int bch2_val2 = (2 * sid2 + 1) % 14;
		int rrch2_val = (2 * sid2 + 2) % 14;
		int rarch2_val1 = (2 * sid2 + 4) % 14;
		int rarch2_val2 = (2 * sid2 + 5) % 14;

		List<SCGChannelConfigItem> scgChannelItems = air1
				.getScgChannelConfigList();
		List<SCGChannelConfigItem> scgChannelItems2 = air2
				.getScgChannelConfigList();
		for (SCGChannelConfigItem scgChannelItem : scgChannelItems) {
			if (scgChannelItem.getChannelType() != SCGChannelConfigItem.BCH
					&& scgChannelItem.getChannelType() != SCGChannelConfigItem.BCHN1) {
				continue;
			}
			for (SCGChannelConfigItem scgChannelItem2 : scgChannelItems2) {
				boolean sameScgIndex = (scgChannelItem.getTsIndex().equals(scgChannelItem2
						.getTsIndex()) && scgChannelItem.getScgIndex().equals(scgChannelItem2
						.getScgIndex()));
				if (!sameScgIndex) {
					continue;
				}
				boolean isConflict = false;
				String conflictType = "";
				if (scgChannelItem.getChannelType() == SCGChannelConfigItem.BCH) {
					if (scgChannelItem2.getChannelType() == SCGChannelConfigItem.BCH) {
						isConflict = (bch1_val1 == bch2_val1
								|| bch1_val1 == bch2_val2
								|| bch1_val2 == bch2_val1 || bch1_val2 == bch2_val2);
						conflictType = "BCH-BCH";
					}
					if (scgChannelItem2.getChannelType() == SCGChannelConfigItem.RRCH) {
						isConflict = (bch1_val1 == rrch2_val || bch1_val2 == rrch2_val);
						conflictType = "BCH-RRCH";
					}
					if (scgChannelItem2.getChannelType() == SCGChannelConfigItem.RARCH) {
						isConflict = (bch1_val1 == rarch2_val1
								|| bch1_val1 == rarch2_val2
								|| bch1_val2 == rarch2_val1 || bch1_val2 == rarch2_val2);
						conflictType = "BCH-RARCH";
					}
				} else if (scgChannelItem.getChannelType() == SCGChannelConfigItem.BCHN1) {
					if (scgChannelItem2.getChannelType() == SCGChannelConfigItem.BCHN1) {
						isConflict = (bch1_val1 == bch2_val1
								|| bch1_val1 == bch2_val2
								|| bch1_val2 == bch2_val1 || bch1_val2 == bch2_val2);
						conflictType = "BCH*-BCH*";
					}
					if (scgChannelItem2.getChannelType() == SCGChannelConfigItem.RRCHN1) {
						isConflict = (bch1_val1 == rrch2_val || bch1_val2 == rrch2_val);
						conflictType = "BCH*-RRCH*";
					}
					if (scgChannelItem2.getChannelType() == SCGChannelConfigItem.RARCHN1) {
						isConflict = (bch1_val1 == rarch2_val1
								|| bch1_val1 == rarch2_val2
								|| bch1_val2 == rarch2_val1 || bch1_val2 == rarch2_val2);
						conflictType = "BCH*-RARCH*";
					}
				}
				if (isConflict) {
					String key = scgChannelItem.getScgIndex() + ":"
							+ scgChannelItem.getTsIndex() + ":"
							+ scgChannelItem2.getChannelType();
					if (!conflictmp.containsKey(key)) {
						tmpstr = OmpAppContext.getMessage(
								"validata_error.bts_carriar_ts_conflict",
								new Object[]{bts.getHexBtsId(), neighbor.getHexBtsId(),
										conflictType, "" + scgChannelItem.getScgIndex(),
										"" + scgChannelItem.getTsIndex()});
						conflictmp.put(key, null);
					}
				}
			}
		}

		return tmpstr;
	}

	private static boolean isTemplate(long moId) {
		return moId < 0;
	}
	
	/**
	 * 初始化同频组网配置验证相关的参数
	 * 
	 * @param freqconf
	 * @throws Exception
	 */
	public static void initData(FreqRelatedConfigure freqconf) throws Exception{
		Long moId = freqconf.getMoId();
		McBts bts = freqconf.getBts();
		if (bts == null) {
			// 查询基站信息
			McBtsBasicFacade basicFacade = AppContext.getCtx().getBean(
					McBtsBasicFacade.class);
			bts = basicFacade.queryByMoId(moId);
			if (bts == null) {
				throw new Exception("BTS is null");
			}
			freqconf.setBts(bts);
		}
		
		List<McBts> neighborList = freqconf.getNeighborList();
		if (neighborList == null) {
			// 查询基站的邻接基站
			NeighbourFacade neighbourFacade = AppContext.getCtx().getBean(
					NeighbourFacade.class);
			neighborList = neighbourFacade.queryNeighbour(moId);
			if (neighborList == null) {
				throw new Exception("NeighborList is null");
			}
			freqconf.setNeighborList(neighborList);
		}
		
		List<McBts> appendNeighborList = freqconf.getAppendNeighborList();
		if (appendNeighborList == null) {
			//查询基站的附加邻接表
			NeighbourFacade neighbourFacade = AppContext.getCtx().getBean(
					NeighbourFacade.class);
			appendNeighborList = neighbourFacade.queryAppendNeighbour(moId);
			if (appendNeighborList == null) {
				throw new Exception("appendNeighborList is null");
			}
			freqconf.setAppendNeighborList(appendNeighborList);
		}

		AirlinkConfig airlink = freqconf.getAirlink();
		if (airlink == null) {
			// 查询空中链路数据
			AirlinkFacade facade = AppContext.getCtx().getBean(
					AirlinkFacade.class);
			airlink = facade.queryConfigByMoId(moId);
			if (airlink == null) {
				throw new Exception("Airlink is null");
			}
			freqconf.setAirlink(airlink);
		}

		RFConfig rfconf = freqconf.getRfconf();
		if (rfconf == null) {
			// 查询基站频点
			CalibrationDataFacade calibFacade = AppContext.getCtx().getBean(CalibrationDataFacade.class);
			CalibrationDataInfo calibData = calibFacade.queryByMoId(moId);
			rfconf = calibData.getRfConfig();
			if (rfconf == null) {
				throw new Exception("RfConfig is null");
			}
			freqconf.setRfconf(rfconf);
		}
	}
	
	/**
	 * 判断是否有新的邻接站加入
	 * @param freqconf
	 */
	public static void hasAddNewNeighbor(FreqRelatedConfigure freqconf) {
		
		//不允许强制配置，则直接返回
		if (!freqconf.isPermitForceConfig()) {
			return;
		}
		
		try {
			NeighbourFacade neighbourFacade = AppContext.getCtx().getBean(
					NeighbourFacade.class);
			List<McBts> tempNb = neighbourFacade.queryNeighbour(freqconf.getMoId());
		
			for (McBts bts : freqconf.getNeighborList()) {
				if (!tempNb.contains(bts)) {
					freqconf.setPermitForceConfig(false);
					return;
				}
			}
			List<McBts> tempAppendNb = neighbourFacade.queryAppendNeighbour(freqconf.getMoId());
			for (McBts bts : freqconf.getAppendNeighborList()) {
				if (!tempAppendNb.contains(bts)) {
					freqconf.setPermitForceConfig(false);
				}
			}
		} catch (Exception e) {
			
		}
	}
	
	public static void processResult(List<String> sameFreqSeq, List<ArrayList<String>> validateFreqSeqBts) {
		List<Integer> needRemove = new ArrayList<Integer>();
		int index = 0;
		for (List<String> sfq : validateFreqSeqBts) {
			if (judgeMentContains(sameFreqSeq, sfq)) {
				return;
			} else if (judgeMentContains(sfq, sameFreqSeq)) {
				needRemove.add(index);
			}
			index ++;
		}
		if (needRemove.size() > 0) {
			for (Integer curIndex : needRemove) {
				validateFreqSeqBts.remove(curIndex.intValue());
			}
		}
		validateFreqSeqBts.add((ArrayList<String>) sameFreqSeq);
		
	}
	
	public static String getTheResult(List<ArrayList<String>> validateFreqSeqBts) {
		String result = "";
		for (List<String> sfq : validateFreqSeqBts) {
			String subRetstr = "";
			for (String btsId : sfq) {
				if (subRetstr.equals("")) {
					subRetstr = "0x" + btsId;
				} else {
					subRetstr += "," + "0x" + btsId;
				}
			}
//			subRetstr += OmpAppContext.getMessage("validata_error.neighbors_same_freqseq")
//				+ "\r\n";
			subRetstr = OmpAppContext.getMessage("validata_error.neighbors_same_freqseq", new Object[]{subRetstr})
			+ "\r\n";
			result += subRetstr;
		}
		return result;
	}
	
	private static boolean judgeMentContains(List<String> sList, List<String> dList) {
		for (String ss : sList) {
			if (!dList.contains(ss)) {
				return false;
			}
		}
		return true;
	}
}
