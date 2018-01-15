/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer2.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.mcbts.core.facade.common.MicroBtsSignalSendSettingFacade;
import com.xinwei.minas.mcbts.core.facade.McBtsBasicFacade;
import com.xinwei.minas.mcbts.core.facade.layer2.AirlinkFacade;
import com.xinwei.minas.mcbts.core.facade.layer3.NeighbourFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsAttribute;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.mcbts.core.model.common.MicroBtsSignalSendSetting;
import com.xinwei.minas.mcbts.core.model.layer1.RFConfig;
import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.mcbts.core.model.layer2.PECCHSetting;
import com.xinwei.minas.mcbts.core.model.layer2.SCGChannelConfigItem;
import com.xinwei.minas.mcbts.core.model.layer2.SCGScaleConfigItem;
import com.xinwei.minas.mcbts.core.model.layer2.W0ConfigItem;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsNeighbour;
import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.core.conf.service.MoCache;
import com.xinwei.minas.mcbts.core.model.sysManage.ChannelComparableMode;
import com.xinwei.minas.server.mcbts.dao.McBtsBizDAO;
import com.xinwei.minas.server.mcbts.dao.layer2.TConfAirlinkConfigDAO;
import com.xinwei.minas.server.mcbts.dao.layer2.TConfSubChannelConfigDAO;
import com.xinwei.minas.server.mcbts.dao.layer2.TConfSubScaleConfigDAO;
import com.xinwei.minas.server.mcbts.dao.layer2.TConfSubW0ConfigDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.NeighbourDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfMBMSClusterDAO;
import com.xinwei.minas.server.mcbts.facade.common.MicroBtsSignalSendSettingFacadeImpl;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.proxy.layer2.AirlinkProxy;
import com.xinwei.minas.server.mcbts.service.McBtsBizService;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer2.AirlinkService;
import com.xinwei.minas.server.mcbts.service.layer3.NeighborService;
import com.xinwei.minas.server.mcbts.service.sysManage.ChannelComparableModeService;
import com.xinwei.minas.server.mcbts.service.sysManage.SimulcastManageService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.GenericBizRecord;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 校准类型配置业务服务接口实现
 * 
 * @author chenjunhua
 * 
 */

public class AirlinkServiceImpl implements AirlinkService {
	private Log log = LogFactory.getLog(AirlinkServiceImpl.class);

	public static final String PECCH_BIZ = "mcbts_airlink_pecch";

	private AirlinkProxy airlinkProxy;

	private McBtsBizProxy mcBtsBizProxy;

	private TConfAirlinkConfigDAO airlinkConfigDAO;

	private TConfSubChannelConfigDAO subChannelConfigDAO;

	private TConfSubScaleConfigDAO subScaleConfigDAO;

	private TConfSubW0ConfigDAO subW0ConfigDAO;

	private NeighbourDAO neighbourDAO;

	private McBtsBizDAO mcBtsBizDAO;


	private TConfMBMSClusterDAO mBMSClusterDAO;
	/**
	 * 查询设备校准数据配置信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public AirlinkConfig queryFromNE(Long moId) throws Exception {

		McBtsBizService service = AppContext.getCtx().getBean(
				McBtsBizService.class);

		ChannelComparableModeService comparableModeService = AppContext
				.getCtx().getBean(ChannelComparableModeService.class);
		
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		
		// 获取基站兼容模式
		ChannelComparableMode channelComparableMode = comparableModeService
				.queryFromEMS();

		AirlinkConfig result = new AirlinkConfig();
		// 在线管理状态下，需要通过Proxy向MO发送查询消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			try {
				// 按基站兼容模式发送查询消息
				result = airlinkProxy.query(moId, channelComparableMode);

				// 微蜂窝基站天线发送方式
				if (bts.getBtsType() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
					
					//微蜂窝基站只取前面两个
					ArrayList<W0ConfigItem> W0List = new ArrayList<W0ConfigItem>(
							result.getW0ConfigList().subList(0,
									AirlinkConfig.MICRO_W0_COUNT));
					result.setW0ConfigList(W0List);
					
					GenericBizData data = service.queryFromNE(moId,
							new GenericBizData(
									MicroBtsSignalSendSettingFacade.MICRO_SIGNAL_BIZ));
					MicroBtsSignalSendSetting setting = null;
					if (!data.getRecords().isEmpty()) {
						GenericBizRecord record = data.getRecords().get(0);
						setting = MicroBtsSignalSendSettingFacadeImpl
								.convertGenericBizRecordToSetting(record);
						setting.setMoId(moId);
						result.setSendSetting(setting);
					}
				}
			} catch (Exception e) {
				log.error(e);
				throw e;
			}
		} else {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_is_offline_manage"));
		}

		return result;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {

		for (Long moId : moIdList) {
			AirlinkConfig airlink = this.queryByMoId(moId);

			business.getCell("sequenceId").putContent(
					moId,
					toJSON("sequenceId",
							String.valueOf(airlink.getSequenceId())));

			business.getCell("preambleScale").putContent(
					moId,
					toJSON("preambleScale",
							String.valueOf(airlink.getPreambleScale())));

			business.getCell("maxScale").putContent(moId,
					toJSON("maxScale", String.valueOf(airlink.getMaxScale())));

			business.getCell("totalTS").putContent(moId,
					toJSON("totalTS", String.valueOf(airlink.getTotalTS())));

			business.getCell("downlinkTS").putContent(
					moId,
					toJSON("downlinkTS",
							String.valueOf(airlink.getDownlinkTS())));

			business.getCell("scgMask").putContent(moId,
					toJSON("scgMask", String.valueOf(airlink.getScgMask())));

			// SCG channel
			fillSCGChannel(moId, business, airlink);

			// TCH BCH scale
			StringBuilder scaleBuilder = new StringBuilder();
			scaleBuilder.append("\"tch_bch\":[");
			for (SCGScaleConfigItem item : airlink.getScgScaleConfigList()) {
				if (scaleBuilder.indexOf("}") > 0) {
					scaleBuilder.append(",");
				}
				scaleBuilder.append("{\"tch\":\"").append(item.getTchScale())
						.append("\",\"bch\":\"").append(item.getBchScale())
						.append("\"}");
			}
			scaleBuilder.append("]");
			business.getCell("tch_bch").putContent(moId,
					scaleBuilder.toString());

			// w0
			StringBuilder w0Builder = new StringBuilder();
			w0Builder.append("\"w0\":[");
			for (W0ConfigItem item : airlink.getW0ConfigList()) {
				if (w0Builder.indexOf("}") > 0) {
					w0Builder.append(",");
				}
				w0Builder.append("{\"w0i\":\"").append(item.getW0I())
						.append("\",\"w0q\":\"").append(item.getW0Q())
						.append("\"}");
				;
			}
			w0Builder.append("]");
			business.getCell("w0").putContent(moId, w0Builder.toString());

			// 添加天线发送方式
			MicroBtsSignalSendSetting setting = airlink.getSendSetting();
			if (setting != null) {
				business.getCell("sendMode").putContent(
						moId,
						toJSON("sendMode",
								String.valueOf(setting.getSendMode())));
				business.getCell("antIndex").putContent(
						moId,
						toJSON("antIndex",
								String.valueOf(setting.getAntIndex())));
			}

		}
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// 获得基站模型
		long btsId = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		// 创建对象
		AirlinkConfig airlink = this.queryByMoId(mcBts.getMoId());
		if (airlink == null) {
			airlink = new AirlinkConfig();
			airlink.setMoId(mcBts.getMoId());
		}

		List<SCGChannelConfigItem> scgChannelConfigItemList = new ArrayList<SCGChannelConfigItem>();
		List<SCGScaleConfigItem> scgScaleConfigItemList = new ArrayList<SCGScaleConfigItem>();
		List<W0ConfigItem> w0ConfigItemList = new ArrayList<W0ConfigItem>();
		MicroBtsSignalSendSetting setting = null;
		// 如果是微蜂窝基站，则需要创建天线发送方式对象
		if (mcBts.getBtsType() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
			setting = new MicroBtsSignalSendSetting();
			setting.setMoId(mcBts.getMoId());
			airlink.setSendSetting(setting);
		}
		// 填充对象
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value)) {
				continue;
			}

			if (key.equals("sequenceId")) {
				airlink.setSequenceId(Integer.valueOf(value));
			} else if (key.equals("preambleScale")) {
				airlink.setPreambleScale(Integer.valueOf(value));
			} else if (key.equals("maxScale")) {
				airlink.setMaxScale(Integer.valueOf(value));
			} else if (key.equals("totalTS")) {
				airlink.setTotalTS(Integer.valueOf(value));
			} else if (key.equals("downlinkTS")) {
				airlink.setDownlinkTS(Integer.valueOf(value));
			} else if (key.equals("scgMask")) {
				airlink.setScgMask(Integer.valueOf(value));
			} else if (key.equals("bch")) {
				List<SCGChannelConfigItem> items = parseSCGChannelConfigItem(
						mcBts.getMoId(), SCGChannelConfigItem.BCH, value);
				if (items != null) {
					scgChannelConfigItemList.addAll(items);
				}
			} else if (key.equals("bchn1")) {
				List<SCGChannelConfigItem> items = parseSCGChannelConfigItem(
						mcBts.getMoId(), SCGChannelConfigItem.BCHN1, value);
				if (items != null) {
					scgChannelConfigItemList.addAll(items);
				}
			} else if (key.equals("rrch")) {
				List<SCGChannelConfigItem> items = parseSCGChannelConfigItem(
						mcBts.getMoId(), SCGChannelConfigItem.RRCH, value);
				if (items != null) {
					scgChannelConfigItemList.addAll(items);
				}
			} else if (key.equals("rrchn1")) {
				List<SCGChannelConfigItem> items = parseSCGChannelConfigItem(
						mcBts.getMoId(), SCGChannelConfigItem.RRCHN1, value);
				if (items != null) {
					scgChannelConfigItemList.addAll(items);
				}
			} else if (key.equals("rarch")) {
				List<SCGChannelConfigItem> items = parseSCGChannelConfigItem(
						mcBts.getMoId(), SCGChannelConfigItem.RARCH, value);
				if (items != null) {
					scgChannelConfigItemList.addAll(items);
				}
			} else if (key.equals("rarchn1")) {
				List<SCGChannelConfigItem> items = parseSCGChannelConfigItem(
						mcBts.getMoId(), SCGChannelConfigItem.RARCHN1, value);
				if (items != null) {
					scgChannelConfigItemList.addAll(items);
				}
			} else if (key.equals("rach")) {
				List<SCGChannelConfigItem> items = parseSCGChannelConfigItem(
						mcBts.getMoId(), SCGChannelConfigItem.RACH, value);
				if (items != null) {
					scgChannelConfigItemList.addAll(items);
				}
			} else if (key.equals("rachn1")) {
				List<SCGChannelConfigItem> items = parseSCGChannelConfigItem(
						mcBts.getMoId(), SCGChannelConfigItem.RACHN1, value);
				if (items != null) {
					scgChannelConfigItemList.addAll(items);
				}
			} else if (key.equals("fach")) {
				List<SCGChannelConfigItem> items = parseSCGChannelConfigItem(
						mcBts.getMoId(), SCGChannelConfigItem.FACH, value);
				if (items != null) {
					scgChannelConfigItemList.addAll(items);
				}
			} else if (key.equals("fachn1")) {
				List<SCGChannelConfigItem> items = parseSCGChannelConfigItem(
						mcBts.getMoId(), SCGChannelConfigItem.RACHN1, value);
				if (items != null) {
					scgChannelConfigItemList.addAll(items);
				}
			} else if (key.equals("rpch")) {
				List<SCGChannelConfigItem> items = parseSCGChannelConfigItem(
						mcBts.getMoId(), SCGChannelConfigItem.RPCH, value);
				if (items != null) {
					scgChannelConfigItemList.addAll(items);
				}
			} else if (key.equals("rpchn1")) {
				List<SCGChannelConfigItem> items = parseSCGChannelConfigItem(
						mcBts.getMoId(), SCGChannelConfigItem.RPCHN1, value);
				if (items != null) {
					scgChannelConfigItemList.addAll(items);
				}
			} else if (key.equals("forbidden")) {
				List<SCGChannelConfigItem> items = parseSCGChannelConfigItem(
						mcBts.getMoId(), SCGChannelConfigItem.FORBIDDEN, value);
				if (items != null) {
					scgChannelConfigItemList.addAll(items);
				}
			} else if (key.equals("tch_bch")) {
				String[] itemStrs = value.replaceAll("(\\[|\\])", "")
						.split(";");

				for (int i = 0; i < itemStrs.length; i++) {
					String itemStr = itemStrs[i];
					String[] chValue = itemStr.split(",");

					SCGScaleConfigItem item = new SCGScaleConfigItem();
					item.setMoId(mcBts.getMoId());
					item.setTsIndex(i);
					item.setTchScale(Integer.valueOf(chValue[0]));
					item.setBchScale(Integer.valueOf(chValue[1]));

					scgScaleConfigItemList.add(item);
				}

			} else if (key.equals("w0")) {
				String[] itemStrs = value.replaceAll("(\\[|\\])", "")
						.split(";");

				for (int i = 0; i < itemStrs.length; i++) {
					String itemStr = itemStrs[i];
					String[] w0Value = itemStr.split(",");

					W0ConfigItem item = new W0ConfigItem();
					item.setMoId(mcBts.getMoId());
					item.setAntennaIndex(i);
					item.setW0I(Double.valueOf(w0Value[0]));
					item.setW0Q(Double.valueOf(w0Value[1]));

					w0ConfigItemList.add(item);
				}
			} else if (key.equals("sendMode")) {
				if (setting != null) {
					setting.setSendMode(Integer.valueOf(value));
				}
			} else if (key.equals("antIndex")) {
				if (setting != null) {
					setting.setAntIndex(Integer.valueOf(value));
				}
			}
		}

		airlink.setScgChannelConfigList(scgChannelConfigItemList);
		airlink.setScgScaleConfigList(scgScaleConfigItemList);

	}

	private List<SCGChannelConfigItem> parseSCGChannelConfigItem(long moId,
			int type, String businessContent) {
		if (StringUtils.isBlank(businessContent))
			return null;

		List<SCGChannelConfigItem> itemList = new ArrayList<SCGChannelConfigItem>();

		String[] itemStrs = businessContent.replaceAll("(\\[|\\])", "").split(
				";");
		for (String itemStr : itemStrs) {
			String[] scgValue = itemStr.split(",");

			SCGChannelConfigItem item = new SCGChannelConfigItem();
			item.setMoId(moId);
			item.setChannelType(type);
			item.setScgIndex(Integer.valueOf(scgValue[0].substring(1)));
			item.setTsIndex(Integer.valueOf(scgValue[1].substring(1)));

			itemList.add(item);

		}
		return itemList;
	}

	private void fillSCGChannel(long moId, Business business,
			AirlinkConfig airlink) {
		// 初始化
		business.getCell("bch").putContent(moId, "\"bch\":\"\"");
		business.getCell("bchn1").putContent(moId, "\"bchn1\":\"\"");
		business.getCell("rrch").putContent(moId, "\"rrch\":\"\"");
		business.getCell("rrchn1").putContent(moId, "\"rrchn1\":\"\"");
		business.getCell("rarch").putContent(moId, "\"rarch\":\"\"");
		business.getCell("rarchn1").putContent(moId, "\"rarchn1\":\"\"");
		business.getCell("rach").putContent(moId, "\"rach\":\"\"");
		business.getCell("rachn1").putContent(moId, "\"rachn1\":\"\"");
		business.getCell("fach").putContent(moId, "\"fach\":\"\"");
		business.getCell("fachn1").putContent(moId, "\"fachn1\":\"\"");
		business.getCell("rpch").putContent(moId, "\"rpch\":\"\"");
		business.getCell("rpchn1").putContent(moId, "\"rpchn1\":\"\"");
		business.getCell("forbidden").putContent(moId, "\"forbidden\":\"\"");

		// 设置值
		for (SCGChannelConfigItem item : airlink.getScgChannelConfigList()) {
			Cell cell = null;
			switch (item.getChannelType()) {
			case SCGChannelConfigItem.BCH:
				cell = business.getCell("bch");
				break;
			case SCGChannelConfigItem.BCHN1:
				cell = business.getCell("bchn1");
				break;
			case SCGChannelConfigItem.RRCH:
				cell = business.getCell("rrch");
				break;
			case SCGChannelConfigItem.RRCHN1:
				cell = business.getCell("rrchn1");
				break;
			case SCGChannelConfigItem.RARCH:
				cell = business.getCell("rarch");
				break;
			case SCGChannelConfigItem.RARCHN1:
				cell = business.getCell("rarchn1");
				break;
			case SCGChannelConfigItem.RACH:
				cell = business.getCell("rach");
				break;
			case SCGChannelConfigItem.RACHN1:
				cell = business.getCell("rachn1");
				break;
			case SCGChannelConfigItem.FACH:
				cell = business.getCell("fach");
				break;
			case SCGChannelConfigItem.FACHN1:
				cell = business.getCell("fachn1");
				break;
			case SCGChannelConfigItem.RPCH:
				cell = business.getCell("rpch");
				break;
			case SCGChannelConfigItem.RPCHN1:
				cell = business.getCell("rpchn1");
				break;
			case SCGChannelConfigItem.FORBIDDEN:
				cell = business.getCell("forbidden");
				break;
			}

			String content = cell.getContentByMoId(moId);

			// 转换JSON模型
			content = content.substring(0, content.length() - 1);
			if (content.contains(",")) {
				content = content + ";[S" + item.getScgIndex() + ",C"
						+ item.getTsIndex() + "]\"";
			} else {
				content = content + "[S" + item.getScgIndex() + ",C"
						+ item.getTsIndex() + "]\"";
			}

			cell.putContent(moId, content);
		}
	}

	private static String toJSON(String key, String value) {
		return "\"" + key + "\":\"" + value + "\"";
	}

	/**
	 * 查询网管校准数据配置信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public AirlinkConfig queryByMoId(Long moId) throws Exception {

		ChannelComparableModeService comparableModeService = AppContext
				.getCtx().getBean(ChannelComparableModeService.class);
		ChannelComparableMode mode = comparableModeService.queryFromEMS();

		McBtsBizService service = AppContext.getCtx().getBean(
				McBtsBizService.class);

		// 查询增强公共信道参数
		GenericBizData pecchData = service.queryAllBy(moId, PECCH_BIZ);
		PECCHSetting pecchSetting = null;
		if (!pecchData.getRecords().isEmpty()) {
			GenericBizRecord record = pecchData.getRecords().get(0);
			pecchSetting = convertGenericBizRecordToPECCHSetting(record);
			pecchSetting.setMoId(moId);
		}

		// 查询通道选择信息
		List<SCGChannelConfigItem> scgChannelList = subChannelConfigDAO
				.queryByMoId(moId);

		if ((pecchSetting == null && mode.getChannelMode() != ChannelComparableMode.PCCH_ONLY)
				|| (pecchSetting != null && mode.getChannelMode() == ChannelComparableMode.PCCH_ONLY)
				|| (!scgChannelList.isEmpty() && mode.getChannelMode() == ChannelComparableMode.PECCH_ONLY)
				|| (scgChannelList.isEmpty() && mode.getChannelMode() != ChannelComparableMode.PECCH_ONLY)) {
			// 基站兼容模式包括PECCH配置，但无PECCH配置数据
			// 基站兼容模式不包括PECCH配置，但有PECCH配置数据
			// 基站兼容模式包括PCCH配置，但无PCCH配置数据
			// 基站兼容模式不包括PCCH配置，但有PCCH配置数据
			McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
			return getDefaultConfigByComparableMode(mcBts, mode);
		}
		
		return queryFromDB(moId);
	}
	
	
	@Override
	public AirlinkConfig queryFromEMS(Long moId) throws Exception {
		return queryFromDB(moId);
	}
	
	private AirlinkConfig queryFromDB(Long moId) throws Exception {

		// 空中链路信息
		AirlinkConfig result = airlinkConfigDAO.queryByMoId(moId);

		if (result != null) {
			// 通道选择信息
			List<SCGChannelConfigItem> scgChannelList = subChannelConfigDAO
					.queryByMoId(moId);
			// 按照时隙号和载波组号进行排序
			Collections.sort(scgChannelList,
					new Comparator<SCGChannelConfigItem>() {
						public int compare(SCGChannelConfigItem item1,
								SCGChannelConfigItem item2) {
							if (item1.getTsIndex() - item2.getTsIndex() == 0) {
								return item1.getScgIndex()
										- item2.getScgIndex();
							}
							return item1.getTsIndex() - item2.getTsIndex();
						}
					});
			result.setScgChannelConfigList(scgChannelList);

			// Scale信息
			List<SCGScaleConfigItem> scgScaleList = subScaleConfigDAO
					.queryByMoId(moId);
			// 按照时隙号进行排序
			Collections.sort(scgScaleList,
					new Comparator<SCGScaleConfigItem>() {
						public int compare(SCGScaleConfigItem item1,
								SCGScaleConfigItem item2) {
							return item1.getTsIndex() - item2.getTsIndex();
						}
					});
			result.setScgScaleConfigList(scgScaleList);

			// W0参数
			List<W0ConfigItem> w0ConfigList = subW0ConfigDAO.queryByMoId(moId);
			// 按照天线号进行排序
			Collections.sort(w0ConfigList, new Comparator<W0ConfigItem>() {
				public int compare(W0ConfigItem config1, W0ConfigItem config2) {
					return config1.getAntennaIndex()
							- config2.getAntennaIndex();
				}
			});
			result.setW0ConfigList(w0ConfigList);

			McBtsBizService service = AppContext.getCtx().getBean(
					McBtsBizService.class);

			// 查询增强公共信道参数
			GenericBizData pecchData = service.queryAllBy(moId, PECCH_BIZ);
			PECCHSetting pecchSetting = null;
			if (!pecchData.getRecords().isEmpty()) {
				GenericBizRecord record = pecchData.getRecords().get(0);
				pecchSetting = convertGenericBizRecordToPECCHSetting(record);
				pecchSetting.setMoId(moId);
			}
			result.setPecchSetting(pecchSetting);

			// 微蜂窝基站天线发送方式
			GenericBizData data = service.queryAllBy(moId,
					MicroBtsSignalSendSettingFacade.MICRO_SIGNAL_BIZ);
			MicroBtsSignalSendSetting sendSetting = null;
			if (!data.getRecords().isEmpty()) {
				GenericBizRecord record = data.getRecords().get(0);
				sendSetting = MicroBtsSignalSendSettingFacadeImpl
						.convertGenericBizRecordToSetting(record);
				sendSetting.setMoId(moId);
			}
			result.setSendSetting(sendSetting);
		}

		return result;
	}

	/**
	 * 配置校准类型配置信息
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	public void config(Long moId, AirlinkConfig airlinkConfig,
			boolean isSynConfig) throws Exception {

		// 设置IDX
		setIdx(airlinkConfig);

		McBtsBizService service = AppContext.getCtx().getBean(
				McBtsBizService.class);

		ChannelComparableModeService comparableModeService = AppContext
				.getCtx().getBean(ChannelComparableModeService.class);

		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		if (mcBts == null) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		
		// 获取基站兼容模式
		ChannelComparableMode channelComparableMode = comparableModeService
				.queryFromEMS();

		GenericBizData data = MicroBtsSignalSendSettingFacadeImpl
				.convertSettingToGenericBizRecord(airlinkConfig
						.getSendSetting());
		// 在线管理状态下，需要通过Proxy向MO发送配置消息
		if (mcBts != null && mcBts.isConfigurable()) {
			// 特殊配置
			try {
				airlinkProxy.config(moId, airlinkConfig, channelComparableMode);

				// 如果是小基站，则需要发送信号发送方式配置消息
				if (mcBts.getBtsType() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
					service.config(moId, data);
				}
			} catch (Exception e) {
				log.error(e);
				// 不支持的业务不往上抛
				if (!(e instanceof UnsupportedOperationException)) {
					throw new Exception(e.getLocalizedMessage());
				}
			}
		}

		// 将sequenceId加入到基站的缓存属性中
		if (mcBts != null) {
			mcBts.addAttribute(McBtsAttribute.Key.SEQ_ID,
					airlinkConfig.getSequenceId());
		}
		// 保存空中链路基本信息
		airlinkConfigDAO.saveOrUpdate(airlinkConfig);

		// 保存通道选择信息
		List<SCGChannelConfigItem> channelConfigs = airlinkConfig
				.getScgChannelConfigList();
		if (channelConfigs != null) {
			subChannelConfigDAO.saveOrUpdate(moId, channelConfigs);
		}
		// 保存Scale信息
		List<SCGScaleConfigItem> scaleConfigs = airlinkConfig
				.getScgScaleConfigList();
		if (scaleConfigs != null) {
			subScaleConfigDAO.saveOrUpdate(scaleConfigs);
		}
		// 保存W0信息
		List<W0ConfigItem> w0Configs = airlinkConfig.getW0ConfigList();
		if (w0Configs != null) {
			subW0ConfigDAO.saveOrUpdate(w0Configs);
		}

		// 保存增强公共信道参数
		GenericBizData pecchData = convertPECCHSettingToGenericBizRecord(airlinkConfig
				.getPecchSetting());
		if (pecchData != null) {
			mcBtsBizDAO.addOrUpdate(moId, pecchData);
		} else {
			if (moId > 0) {
				mcBtsBizDAO.delete(PECCH_BIZ, moId);
			}
		}

		// 强制终端注册处理
		if (airlinkConfig.isForceUTRegist()) {
			// 构造强制终端注册信息
			GenericBizData genericBizData = new GenericBizData(
					"mcbts_forceUTRegister");
			// 查询邻接表，下发邻接基站信息
			List<McBtsNeighbour> neighborList = neighbourDAO
					.queryNeighbour(moId);
			for (McBtsNeighbour neighbor : neighborList) {
				McBts neighborMo = McBtsCache.getInstance().queryByMoId(
						neighbor.getNeighbourMoId());
				// 对在线的邻接表发送强制终端注册命令
				if (neighborMo != null && neighborMo.isConfigurable()) {
					// 在线管理状态下需要向网元发送消息
					try {
						mcBtsBizProxy
								.config(neighbor.getMoId(), genericBizData);
					} catch (Exception e) {
						log.error(e);
						throw new Exception(e.getLocalizedMessage());
					}
				}
			}
		}

	
		if (mcBts != null && mcBts.isConfigurable()) {
			NeighborService neighborService = AppContext.getCtx().getBean(
					NeighborService.class);
			// 发送邻接信息
			neighborService.sendNeighborConfig(moId, isSynConfig);
		}

		// 设置同播资源中的基站链路信息为未同步
		SimulcastManageService simulcastManageService = AppContext.getCtx()
				.getBean(SimulcastManageService.class);
		simulcastManageService.setSimulMcBtsLinkUnSync();
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		AirlinkConfig airlinkConfig = this.queryByMoId(moId);
		if (airlinkConfig != null) {
			this.config(moId, airlinkConfig, false);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {

		SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);

		AirlinkConfig result = queryFromNE(moId);
		AirlinkConfig dataFromDB = queryByMoId(moId);

		// 保存空中链路基本信息
		if (dataFromDB == null) {
			result.setIdx(sequenceService.getNext());
		} else {
			result.setIdx(dataFromDB.getIdx());
		}
		result.setMoId(moId);
		airlinkConfigDAO.saveOrUpdate(result);

		// 保存通道选择信息
		List<SCGChannelConfigItem> channelList = result
				.getScgChannelConfigList();
		for (SCGChannelConfigItem channel : channelList) {
			channel.setIdx(sequenceService.getNext());
			channel.setMoId(moId);
		}
		subChannelConfigDAO.saveOrUpdate(moId, channelList);

		// 保存Scale信息
		List<SCGScaleConfigItem> scaleList = result.getScgScaleConfigList();
		for (SCGScaleConfigItem scale : scaleList) {
			scale.setIdx(sequenceService.getNext());
			scale.setMoId(moId);
		}
		subScaleConfigDAO.saveOrUpdate(scaleList);

		// 保存W0信息
		List<W0ConfigItem> woList = result.getW0ConfigList();
		for (W0ConfigItem wo : woList) {
			wo.setIdx(sequenceService.getNext());
			wo.setMoId(moId);
		}
		subW0ConfigDAO.saveOrUpdate(woList);

		// 保存增强公共信道参数
		PECCHSetting pecchSetting = result.getPecchSetting();
		PECCHSetting pecchSettingFromDB = dataFromDB.getPecchSetting();
		if (pecchSetting != null) {
			if (pecchSettingFromDB == null) {
				pecchSetting.setIdx(sequenceService.getNext());
			} else {
				pecchSetting.setIdx(pecchSettingFromDB.getIdx());
			}
			GenericBizData pecchData = convertPECCHSettingToGenericBizRecord(pecchSetting);
			mcBtsBizDAO.addOrUpdate(moId, pecchData);
		}

		// 保存信号发送方式配置
		MicroBtsSignalSendSetting sendSetting = result.getSendSetting();
		MicroBtsSignalSendSetting sendSettingFromDB = dataFromDB.getSendSetting();
		if (sendSetting != null) {
			if (sendSettingFromDB == null) {
				sendSetting.setIdx(sequenceService.getNext());
			} else {
				sendSetting.setIdx(sendSettingFromDB.getIdx());
			}
			GenericBizData sendData = MicroBtsSignalSendSettingFacadeImpl
					.convertSettingToGenericBizRecord(sendSetting);
			mcBtsBizDAO.addOrUpdate(moId, sendData);
		}
	}

	/**
	 * 获得基站默认W0配置值
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public List<W0ConfigItem> getDefaultW0(Long moId) throws Exception {

		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		int antennaType = bts.getAntennaType();
		int btsType = bts.getBtsType();

		List<W0ConfigItem> ret = new ArrayList<W0ConfigItem>();
		if (btsType == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
			// 微蜂窝基站
			for (int i = 0; i < 2; i++) {
				W0ConfigItem item = new W0ConfigItem();
				item.setMoId(moId);
				item.setAntennaIndex(i);
				item.setW0I(1.0);
				item.setW0Q(0.0);
				ret.add(item);
			}
		} else {
			ret = getDefaultW0ByAntennaType(antennaType);
			for (W0ConfigItem item : ret) {
				item.setMoId(moId);
			}
		}

		return ret;
	}

	/**
	 * 根据天线类型获得默认W0配置值
	 * 
	 * @param antennaType
	 * @throws Exception
	 */
	public List<W0ConfigItem> getDefaultW0ByAntennaType(int antennaType)
			throws Exception {

		List<W0ConfigItem> ret = new ArrayList<W0ConfigItem>();
		for (int i = 0; i < 8; i++) {
			W0ConfigItem item = new W0ConfigItem();
			item.setAntennaIndex(i);
			if (antennaType == McBts.ANTENNA_TYPE_PANEL) {
				// 定向天线
				if (i == 0 || 7 - i == 0) {
					item.setW0I(7753.0 / 32767.0);
					item.setW0Q(-8608.0 / 32767.0);
				} else if (i == 1 || 7 - i == 1) {
					item.setW0I(-10230.0 / 32767.0);
					item.setW0Q(-5439.0 / 32767.0);
				} else if (i == 2 || 7 - i == 2) {
					item.setW0I(7122.0 / 32767.0);
					item.setW0Q(9129.0 / 32767.0);
				} else if (i == 3 || 7 - i == 3) {
					item.setW0I(11585.0 / 32767.0);
					item.setW0Q(0.0 / 32767.0);
				}
			} else {
				// 全向天线
				item.setW0I(1.0);
				item.setW0Q(0.0);
			}
			ret.add(item);
		}

		return ret;
	}

	public static PECCHSetting convertGenericBizRecordToPECCHSetting(
			GenericBizRecord record) {
		PECCHSetting setting = new PECCHSetting();
		GenericBizProperty property = record.getPropertyValue("idx");
		setting.setIdx(Long.parseLong(property.getValue().toString()));
		property = record.getPropertyValue("scgIndex");
		setting.setScgIndex(Integer.parseInt(property.getValue().toString()));
		property = record.getPropertyValue("pchCount");
		setting.setPchCount(Integer.parseInt(property.getValue().toString()));
		property = record.getPropertyValue("pchIndex");
		setting.setPchIndex(Integer.parseInt(property.getValue().toString()));
		property = record.getPropertyValue("rarchCount");
		setting.setRarchCount(Integer.parseInt(property.getValue().toString()));
		property = record.getPropertyValue("rrchCount");
		setting.setRrchCount(Integer.parseInt(property.getValue().toString()));
		return setting;
	}

	public static GenericBizData convertPECCHSettingToGenericBizRecord(
			PECCHSetting setting) {

		if (setting == null) {
			return null;
		}

		GenericBizData data = new GenericBizData(PECCH_BIZ);
		if (setting.getIdx() != null) {
			data.addProperty(new GenericBizProperty("idx", setting.getIdx()));
		}
		data.addProperty(new GenericBizProperty("scgIndex", setting
				.getScgIndex()));
		data.addProperty(new GenericBizProperty("pchCount", setting
				.getPchCount()));
		data.addProperty(new GenericBizProperty("pchIndex", setting
				.getPchIndex()));
		data.addProperty(new GenericBizProperty("rarchCount", setting
				.getRarchCount()));
		data.addProperty(new GenericBizProperty("rrchCount", setting
				.getRrchCount()));
		Set<String> primaryKeys = new HashSet<String>();
		primaryKeys.add("idx");
		data.setPrimaryKeys(primaryKeys);
		return data;
	}

	private long getTemplateMoIdByBtsType(int btsType) {
		if (btsType == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
			return -300;
		} else if (btsType == McBtsTypeDD.FDDI_MCBTS) {
			return -400;
		}
		return -200;
	}

	/**
	 * 根据基站兼容模式获得默认空中链路配置值
	 * 
	 * @param mcBts
	 * @param mode
	 * @throws Exception
	 */
	public AirlinkConfig getDefaultConfigByComparableMode(McBts mcBts,
			ChannelComparableMode mode) throws Exception {

		SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);

		long tempMoId = getTemplateMoIdByBtsType(mcBts.getBtsType());

		// 加载基站类型对应的默认空中链路配置
		AirlinkConfig tempConfig = queryFromDB(tempMoId);
		AirlinkConfig config = queryFromDB(mcBts.getMoId());
		
		config.setScgMask(tempConfig.getScgMask());
		config.setTotalTS(tempConfig.getTotalTS());
		config.setDownlinkTS(tempConfig.getDownlinkTS());
		config.setTchForbidden(tempConfig.getTchForbidden());

		if (mode.getChannelMode() == ChannelComparableMode.PECCH_ONLY) {
			// 仅支持增强公共信道配置时，清空公共信道配置
			config.getScgChannelConfigList().clear();
		} else {
			// 公共信道配置设置为默认值
			config.setScgChannelConfigList(tempConfig.getScgChannelConfigList());
			List<SCGChannelConfigItem> channelList = config
					.getScgChannelConfigList();
			for (SCGChannelConfigItem channel : channelList) {
				channel.setIdx(sequenceService.getNext());
				channel.setMoId(mcBts.getMoId());
			}
			config.setScgScaleConfigList(tempConfig.getScgScaleConfigList());
			List<SCGScaleConfigItem> scaleList = config
					.getScgScaleConfigList();
			for (SCGScaleConfigItem scale : scaleList) {
				scale.setIdx(sequenceService.getNext());
				scale.setMoId(mcBts.getMoId());
			}
		}

		if (mode.getChannelMode() == ChannelComparableMode.PCCH_ONLY) {
			// 仅支持公共信道配置时，清空增强公共信道配置
			config.setPecchSetting(null);
		} else {
			PECCHSetting pecchSetting = config.getPecchSetting();
			PECCHSetting tempPecchSetting = tempConfig.getPecchSetting();
			if (tempPecchSetting == null) {
				tempPecchSetting = new PECCHSetting();
				tempPecchSetting.setScgIndex(1);
				tempPecchSetting.setPchCount(4);
				tempPecchSetting.setRarchCount(2);
				tempPecchSetting.setRrchCount(2);
			}
			if (pecchSetting != null) {
				tempPecchSetting.setIdx(pecchSetting.getIdx());
			} else {
				tempPecchSetting.setIdx(sequenceService.getNext());
			}
			tempPecchSetting.setMoId(mcBts.getMoId());
			// 增强公共信道配置的PCH组序号需要进行调整
			tempPecchSetting
					.setPchIndex(((int) (mcBts.getBtsId() % tempPecchSetting
							.getPchCount())) + 1);
			// 增强公共信道配置设置为默认值
			config.setPecchSetting(tempPecchSetting);
		}

		return config;
	}

	private void setIdx(AirlinkConfig airlinkConfig) {

		SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);

		if (airlinkConfig.getIdx() == null) {
			airlinkConfig.setIdx(sequenceService.getNext());
		}
		List<SCGChannelConfigItem> scgChannelConfigList = airlinkConfig
				.getScgChannelConfigList();
		for (SCGChannelConfigItem channelItem : scgChannelConfigList) {
			if (channelItem != null && channelItem.getIdx() == null) {
				channelItem.setIdx(sequenceService.getNext());
			}
		}
		List<SCGScaleConfigItem> scgScaleConfigList = airlinkConfig
				.getScgScaleConfigList();
		for (SCGScaleConfigItem scaleItem : scgScaleConfigList) {
			if (scaleItem != null && scaleItem.getIdx() == null) {
				scaleItem.setIdx(sequenceService.getNext());
			}
		}
		List<W0ConfigItem> w0ConfigList = airlinkConfig.getW0ConfigList();
		for (W0ConfigItem w0Item : w0ConfigList) {
			if (w0Item != null && w0Item.getIdx() == null) {
				w0Item.setIdx(sequenceService.getNext());
			}
		}
	}
	
	/*
	 * 下边为spring bean的set方法
	 */
	public void setAirlinkProxy(AirlinkProxy airlinkProxy) {
		this.airlinkProxy = airlinkProxy;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	public void setAirlinkConfigDAO(TConfAirlinkConfigDAO airlinkConfigDAO) {
		this.airlinkConfigDAO = airlinkConfigDAO;
	}

	public void setSubScaleConfigDAO(TConfSubScaleConfigDAO subScaleConfigDAO) {
		this.subScaleConfigDAO = subScaleConfigDAO;
	}

	public void setSubW0ConfigDAO(TConfSubW0ConfigDAO subW0ConfigDAO) {
		this.subW0ConfigDAO = subW0ConfigDAO;
	}

	public void setSubChannelConfigDAO(
			TConfSubChannelConfigDAO subChannelConfigDAO) {
		this.subChannelConfigDAO = subChannelConfigDAO;
	}

	public void setNeighbourDAO(NeighbourDAO neighbourDAO) {
		this.neighbourDAO = neighbourDAO;
	}

	public void setMcBtsBizDAO(McBtsBizDAO mcBtsBizDAO) {
		this.mcBtsBizDAO = mcBtsBizDAO;
	}

	public void setmBMSClusterDAO(TConfMBMSClusterDAO mBMSClusterDAO) {
		this.mBMSClusterDAO = mBMSClusterDAO;
	}

	@Override
	public String validateAirlink(AirlinkConfig airlinkConfig,
			RFConfig rfConfig, GenericBizData clusterConf,
			TConfRemoteBts remoteBts, TConfMBMSBts mbmsBts, McBts bts)
			throws Exception {

		String retstr = "";
		
		//在兼容模式2下，由于只有增强信道，不需要进行以下校验规则
		if (getChannelComparableMode() != ChannelComparableMode.PECCH_ONLY) {
			
			// 校验RARCH信道和RRCH信道的分配
			retstr += validateTimeslotOfRarchAndRrch(airlinkConfig, remoteBts);
			if (!retstr.equals("")) {
				return retstr;
			}

			// 集群基站,应至少有一组控制信道的BCH和RARCH在同一个SCG
			retstr += validateClusterBchAndRarch(airlinkConfig, clusterConf);
			if (!retstr.equals("")) {
				return retstr;
			}
		}
		
		// 同播集群基站需校验seqID、超时隙
		retstr += validateMBMSCluster(airlinkConfig, clusterConf, mbmsBts);
		if (!retstr.equals("")) {
			return retstr;
		}

		// 非模板配置时才需要进行如下校验
		if (!isTemplate(airlinkConfig.getMoId())) {
			retstr += validateSameFreqBts(airlinkConfig, rfConfig, clusterConf,
					mbmsBts, bts);
			if (!retstr.equals("")) {
				return retstr;
			}
		}

		return "";
	}

	/**
	 * 获取系统的当前兼容模式
	 * @return
	 * @throws Exception
	 */
	private int getChannelComparableMode() throws Exception {
		try {
			ChannelComparableModeService comparableModeService = AppContext
			.getCtx().getBean(ChannelComparableModeService.class);
			
			 ChannelComparableMode mode = comparableModeService.queryFromEMS();
			 return mode.getChannelMode();
		} catch (Exception e) {
			throw new Exception(OmpAppContext.getMessage("validata_error.channel_compare_mode"));
		}
	}
	
	private static boolean isTemplate(long moId) {
		return moId < 0;
	}

	/**
	 * 校验RARCH信道和RRCH信道的分配
	 * 
	 * @param conf
	 * @param remoteBts
	 * @return
	 */
	private static String validateTimeslotOfRarchAndRrch(AirlinkConfig conf,
			TConfRemoteBts remoteBts) {
		int totalTS = conf.getTotalTS();
		int downlinkTS = conf.getDownlinkTS();
		int uplinkTS = totalTS - downlinkTS;
		List<SCGChannelConfigItem> scgChannelItems = conf
				.getScgChannelConfigList();

		// 遍历信道
		for (SCGChannelConfigItem scgChannelItem : scgChannelItems) {
			// 上行时隙数=4：4时，RARCH不能占用下行最后一个时隙
			if ((totalTS == 8)
					&& (downlinkTS == 4)
					&& scgChannelItem.getChannelType() == SCGChannelConfigItem.RARCH) {
				if (scgChannelItem.getTsIndex() == downlinkTS - 1) {
					return OmpAppContext
							.getMessage("validata_error.bts_44mode_rarch");
				}
			}
			// 超远距离基站，RARCH和RRCH均不能占用下行最后一个时隙
			if (remoteBts.getFlag() == TConfRemoteBts.FLAG_SUPPORT
					&& (scgChannelItem.getChannelType() == SCGChannelConfigItem.RARCH || scgChannelItem
							.getChannelType() == SCGChannelConfigItem.RRCH)) {
				if (scgChannelItem.getTsIndex() == downlinkTS - 1) {
					return OmpAppContext
							.getMessage("validata_error.remotebts_rarch_rrch");
				}
			}

		}

		// 当系统分配的上行时隙小于下行时隙数，RARCH按照上行的最大时隙数分配时隙
		if (downlinkTS >= uplinkTS) {
			for (SCGChannelConfigItem scgChannelItem : scgChannelItems) {
				if (scgChannelItem.getChannelType() != SCGChannelConfigItem.RARCH) {
					continue;
				}
				// 下行时隙数大于上行时隙数时，多出的下行时隙上不可定义RARCH
				if (scgChannelItem.getTsIndex() >= uplinkTS) {
					return OmpAppContext
							.getMessage("validata_error.bts_downlink_rarch");
				}
			}
		}

		return "";
	}

	/**
	 * 判断是否是集群基站
	 * 
	 * @param clusterConf
	 * @return
	 */
	private static boolean isClusterBts(GenericBizData clusterConf) {
		int clusterFlag = 0;
		GenericBizProperty flagProperty = clusterConf.getProperty("Flag");
		if (flagProperty != null) {
			Object flagValue = flagProperty.getValue();
			if (flagValue != null) {
				clusterFlag = Integer.parseInt(flagValue.toString());
			}
		}
		return clusterFlag == 1;
	}

	/**
	 * 支持普通集群的基站，应至少有一组控制信道的BCH和RARCH在同一个SCG
	 * 
	 * @param conf
	 * @param clusterConf
	 * @return
	 */
	private static String validateClusterBchAndRarch(AirlinkConfig conf,
			GenericBizData clusterConf) {

		// 非集群基站不校验
		if (!isClusterBts(clusterConf)) {
			return "";
		}

		List<SCGChannelConfigItem> scgChannelItems = conf
				.getScgChannelConfigList();
		Set<Integer> bchScgIndexs = new HashSet();
		Set<Integer> rarchScgIndexs = new HashSet();
		for (SCGChannelConfigItem scgChannelItem : scgChannelItems) {
			int channelType = scgChannelItem.getChannelType();
			int scgIndex = scgChannelItem.getScgIndex();
			if (channelType == SCGChannelConfigItem.BCH) {
				// 记录BCH所在的SCG
				bchScgIndexs.add(scgIndex);

			} else if (channelType == SCGChannelConfigItem.RARCH) {
				// 记录RARCH所在的SCG和时隙
				rarchScgIndexs.add(scgIndex);
			}
		}
		for (Integer bchScgIndex : bchScgIndexs) {
			if (rarchScgIndexs.contains(bchScgIndex)) {
				return "";
			}
		}

		return OmpAppContext
				.getMessage("validata_error.cluster_bch_rarch_validate");
	}

	// 对于同播集群环境，seqID=15不可用
	// 对于同播集群环境，任一基站的下行最后一个时隙不能被任何公共信道占用
	private static String validateMBMSCluster(AirlinkConfig conf,
			GenericBizData clusterConf, TConfMBMSBts mbmsBts) {

		int downlinkTS = conf.getDownlinkTS();

		// 同播集群基站判断
		if (mbmsBts.isMBMSBTS() && isClusterBts(clusterConf)) {

			// seqid不能为15
			if (conf.getSequenceId() == AirlinkConfig.SEQ_COUNT - 1) {
				return OmpAppContext.getMessage(
						"validata_error.mbms_cluster_seqid",
						new Object[] { (AirlinkConfig.SEQ_COUNT - 1) });
			}

			// 超时隙不能被禁止
			String tsForbiddenStr = conf.getTchForbidden();
			char[] tsForbiddenChars = tsForbiddenStr.toCharArray();
			if (tsForbiddenChars.length >= 20) {
				for (int i = 0; i < AirlinkConfig.SCG_COUNT; i++) {
					String str = new String(tsForbiddenChars, i * 4, 4);
					int tsMask = Integer.parseInt(str, 16);
					if (((tsMask >> (downlinkTS - 1)) & 1) == 1) {
						return OmpAppContext
								.getMessage("validata_error.mbms_cluster_forbid_ts");
					}
				}
			}

			// 超时隙不能配置公共信道
			List<SCGChannelConfigItem> scgChannelItems = conf
					.getScgChannelConfigList();
			for (SCGChannelConfigItem scgChannelItem : scgChannelItems) {
				if (scgChannelItem.getTsIndex().intValue() == downlinkTS - 1) {
					return OmpAppContext
							.getMessage("validata_error.mbms_cluster_channel_ts");
				}
			}
		}

		return "";
	}

	/**
	 * 校验同频基站相关属性
	 * 
	 * @param conf
	 * @param rfConfig
	 * @param clusterConf
	 * @param mbmsBts
	 * @param bts
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	private String validateSameFreqBts(AirlinkConfig conf, RFConfig rfConfig,
			GenericBizData clusterConf, TConfMBMSBts mbmsBts, McBts bts)
			throws Exception {
		String retstr = "";
		String otherTsBts = "";
		String otherScgBts = "";
		int rowTsCount = 0;
		int colTsCount = 0;
		int rowScgCount = 0;
		int colScgCount = 0;
		boolean tsCountFull = false;
		boolean scgCountFull = false;

		List<McBts> allBts = McBtsCache.getInstance().queryAll();
		// 查找频点相同的基站
		List<Long> sameSeqMoIdList = new LinkedList();
		for (McBts mcBts : allBts) {
			if (mcBts.getBtsFreqType() == bts.getBtsFreqType()
					&& mcBts.getBtsFreq() == rfConfig.getFreqOffset()) {

				sameSeqMoIdList.add(mcBts.getMoId());
			}
		}
		// 查找所有同频基站的空中链路配置
		List<AirlinkConfig> airlinkConfigs = airlinkConfigDAO
				.queryByMoIdList(sameSeqMoIdList);

		// 查找所有同播集群下基站
		List<Long> mbmsClusterBts = mBMSClusterDAO.queryAllMBMSClusterBtsMoId();

		// 遍历同频基站的空中链路配置
		for (AirlinkConfig airlink : airlinkConfigs) {
			if (airlink.getMoId() == bts.getMoId()) {
				continue;
			}
			// 校验时隙配比
			if (!tsCountFull) {
				if (conf.getTotalTS().intValue() != airlink.getTotalTS()
						.intValue()
						|| conf.getDownlinkTS().intValue() != airlink
								.getDownlinkTS().intValue()) {
					if (!"".equals(otherTsBts)) {
						otherTsBts += ", ";
						colTsCount++;
						if (colTsCount % 3 == 0) {
							otherTsBts += "\r\n";
							rowTsCount++;
						}
					}
					McBts sameFreqBts = McBtsCache.getInstance().queryByMoId(
							airlink.getMoId());
					if (sameFreqBts != null) {
						otherTsBts += "0x" + sameFreqBts.getHexBtsId();
					}
					// 最多显示10个同频同播集群基站
					if (rowTsCount == 3) {
						otherTsBts += ", ...";
						tsCountFull = true;
					}
				}
			}

			// 同播集群基站判断
			if (mbmsBts.isMBMSBTS() && isClusterBts(clusterConf)) {

				// 如果不是同播集群下的基站，则不需要与其校验
				if (!mbmsClusterBts.contains(airlink.getMoId())) {
					continue;
				}

				// 子载波组掩码不同
				if (!scgCountFull) {
					if (conf.getScgMask().intValue() != airlink.getScgMask()
							.intValue()) {
						if (!"".equals(otherScgBts)) {
							otherScgBts += ", ";
							colScgCount++;
							if (colScgCount % 3 == 0) {
								otherScgBts += "\r\n";
								rowScgCount++;
							}
						}
						McBts sameFreqBts = McBtsCache.getInstance()
								.queryByMoId(airlink.getMoId());
						if (sameFreqBts != null) {
							otherScgBts += "0x" + sameFreqBts.getHexBtsId();
						}
						// 最多显示10个同频同播集群基站
						if (rowScgCount == 3) {
							otherScgBts += ", ...";
							scgCountFull = true;
						}
					}
				}
			}

			if (tsCountFull && scgCountFull) {
				break;
			}
		}

		if (!"".equals(otherTsBts)) {
			retstr += OmpAppContext.getMessage(
					"validata_error.mbms_cluster_freq_ts",
					new Object[] { bts.getHexBtsId(), "\r\n" + otherTsBts });
		}

		if (!"".equals(otherScgBts)) {
			if (!"".equals(retstr)) {
				retstr += "\r\n";
			}
			retstr += OmpAppContext.getMessage(
					"validata_error.mbms_cluster_freq_scg",
					new Object[] { bts.getHexBtsId(), "\r\n" + otherScgBts });
		}

		if (!"".equals(retstr)) {
			retstr += "\r\n";
		}

		return retstr;
	}

	/**
	 * 
	 * @deprecated
	 */
	private String validateSameFreqBts2(AirlinkConfig conf, RFConfig rfConfig,
			GenericBizData clusterConf, TConfMBMSBts mbmsBts, McBts bts)
			throws Exception {
		String retstr = "";

		// 外层主键为频率，内层主键为SEQ ID
		Map<String, Map<Integer, String>> btsFreqSeqInfo = null;
		try {
			// 获取所有基站频点及前导序列号信息
			NeighbourFacade neighbourFacade = OmpAppContext.getCtx().getBean(
					NeighbourFacade.class);
			btsFreqSeqInfo = neighbourFacade.queryBtsFreqSeqInfo();
		} catch (Exception e) {
			btsFreqSeqInfo = new LinkedHashMap<String, Map<Integer, String>>();
		}

		String[] sameFreqMoIds = new String[0];

		// 查询获取的同频同SEQID为数据库中的值
		// 比较时需要使用用户配置的空中链路值进行比较
		String freqKey = bts.getBtsFreqType() + "#" + rfConfig.getFreqOffset();
		Map<Integer, String> btsSeqInfo = btsFreqSeqInfo.get(freqKey);
		if (btsSeqInfo != null) {
			String sameFreqMoIdStr = "";
			for (String btsMoIds : btsSeqInfo.values()) {
				sameFreqMoIdStr = sameFreqMoIdStr + btsMoIds;
				sameFreqMoIds = new String[] { sameFreqMoIdStr };
				if (sameFreqMoIdStr.indexOf(")(") >= 0) {
					sameFreqMoIds = sameFreqMoIdStr.split("\\)\\(");
				}
			}
		}

		String otherTsBts = "";
		String otherScgBts = "";
		int rowTsCount = 0;
		int colTsCount = 0;
		int rowScgCount = 0;
		int colScgCount = 0;
		boolean tsCountFull = false;
		boolean scgCountFull = false;
		for (String sameFreqMoId : sameFreqMoIds) {
			sameFreqMoId = sameFreqMoId.replaceAll("\\(", "").replaceAll("\\)",
					"");
			Long btsMoId = Long.parseLong(sameFreqMoId.trim());

			// 跳过当前基站自己
			if (btsMoId.equals(bts.getMoId())) {
				continue;
			}

			AirlinkConfig airlink = null;
			try {
				// 查询空中链路数据
				AirlinkFacade facade = OmpAppContext.getCtx().getBean(
						AirlinkFacade.class);
				airlink = facade.queryConfigByMoId(btsMoId);
			} catch (Exception e) {
			}
			if (airlink == null) {
				continue;
			}

			McBts sameFreqBts = null;
			try {
				// 查询基站信息
				McBtsBasicFacade basicFacade = OmpAppContext.getCtx().getBean(
						McBtsBasicFacade.class);
				sameFreqBts = basicFacade.queryByMoId(btsMoId);
			} catch (Exception e) {
			}
			if (sameFreqBts == null) {
				continue;
			}

			// 上下行时隙比不同
			if (!tsCountFull) {
				if (conf.getTotalTS().intValue() != airlink.getTotalTS()
						.intValue()
						|| conf.getDownlinkTS().intValue() != airlink
								.getDownlinkTS().intValue()) {
					if (!"".equals(otherTsBts)) {
						otherTsBts += ", ";
						colTsCount++;
						if (colTsCount % 3 == 0) {
							otherTsBts += "\r\n";
							rowTsCount++;
						}
					}
					otherTsBts += "0x" + sameFreqBts.getHexBtsId();
					// 最多显示10个同频同播集群基站
					if (rowTsCount == 3) {
						otherTsBts += ", ...";
						tsCountFull = true;
					}
				}
			}

			// 同播集群基站判断
			if (mbmsBts.isMBMSBTS() && isClusterBts(clusterConf)) {
				// 子载波组掩码不同
				if (!scgCountFull) {
					if (conf.getScgMask().intValue() != airlink.getScgMask()
							.intValue()) {
						if (!"".equals(otherScgBts)) {
							otherScgBts += ", ";
							colScgCount++;
							if (colScgCount % 3 == 0) {
								otherScgBts += "\r\n";
								rowScgCount++;
							}
						}
						otherScgBts += "0x" + sameFreqBts.getHexBtsId();
						// 最多显示10个同频同播集群基站
						if (rowScgCount == 3) {
							otherScgBts += ", ...";
							scgCountFull = true;
						}
					}
				}
			}

			if (tsCountFull && scgCountFull) {
				break;
			}
		}

		if (!"".equals(otherTsBts)) {
			retstr += OmpAppContext.getMessage(
					"validata_error.mbms_cluster_freq_ts",
					new Object[] { bts.getHexBtsId(), "\r\n" + otherTsBts });
		}

		if (!"".equals(otherScgBts)) {
			if (!"".equals(retstr)) {
				retstr += "\r\n";
			}
			retstr += OmpAppContext.getMessage(
					"validata_error.mbms_cluster_freq_scg",
					new Object[] { bts.getHexBtsId(), "\r\n" + otherScgBts });
		}

		if (!"".equals(retstr)) {
			retstr += "\r\n";
		}

		return retstr;
	}

}
