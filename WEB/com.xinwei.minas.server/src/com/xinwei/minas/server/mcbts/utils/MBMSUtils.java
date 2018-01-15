/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-21	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import com.arrowping.mcwill.ems.simplenms.common.SyncSagResult;
import com.arrowping.mcwill.ems.simplenms.util.SyncSagFailException;
import com.xinwei.dnss.common.util.DNSSMsgUtil;
import com.xinwei.dnss.corba.ClientMsg;
import com.xinwei.dnss.corba.CommClient;
import com.xinwei.dnss.corba.CommClientHelper;
import com.xinwei.dnss.corba.ReturnClientMsgHolder;
import com.xinwei.dnss.framework.biz.DNSSRequest;
import com.xinwei.dnss.framework.biz.DNSSResponse;
import com.xinwei.minas.mcbts.core.model.sysManage.ChannelComparableMode;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsLink;
import com.xinwei.minas.mcbts.core.model.sysManage.Simulcast;
import com.xinwei.minas.server.mcbts.service.sysManage.ChannelComparableModeService;
import com.xinwei.minas.server.mcbts.service.sysManage.impl.SimulcastManageServiceImpl;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * MBMS的工具集,用于连接SAG
 * 
 * @author tiance
 * 
 */

public class MBMSUtils {

	private static final String SESSION_ID = "jmx425178042";

	private static final String OPER_ID = "IM1001";

	private static final String QUERY_ACTION = "1";
	private static final String ADD_ACTION = "2";
	private static final String MODIFY_ACTION = "3";
	private static final String DEL_ACTION = "4";

	private static final String TABLE_NAME = "#TABLE_NAME";

	private static final String SAG_ID = "#SAG_ID";

	private static final String CONDITION = "*";

	private static final String SIMULCAST_TABLE = "xwSSSab1BtsRsvTable";
	private static final String BTS_LINK_TABLE = "xwSSSab1BtsLinkTable";

	// 同播资源信息的列名
	// 地域ID
	private static final String ZoneID_FIELD = "xwSSSab1BtsRsvZoneID";
	// 频率类型
	private static final String FreqType_FIELD = "xwSSSab1BtsRsvBSFreqType";
	// 频率偏移量
	private static final String Freq_FIELD = "xwSSSab1BtsRsvBSFreq";
	// 最小
	private static final String MinNofRsv_FIELD = "xwSSSab1BtsRsvMinNofRsv";
	// 最大
	private static final String MaxNofRsv_FIELD = "xwSSSab1BtsRsvMaxNofRsv";
	// 最大同播
	private static final String MaxGrpNum_FIELD = "xwSSSab1BtsRsvMaxGrpNum";

	// 基站链路的列名
	// 索引
	private static final String BTS_LINK_INDEX = "xwSSSab1BtsLinkIndex";
	// 基站ID
	private static final String BTS_ID = "xwSSSab1BtsLinkBtsID";
	// 基站信令点
	private static final String BTS_SIGNAL_POINT_ID = "xwSSSab1BtsLinkDpID";
	// 位置区ID
	private static final String AREA_ID = "xwSSSab1BtsLinkLai";
	// 基站类型
	private static final String BTS_TYPE = "xwSSSab1BtsType";
	// 安全参数索引
	private static final String SAFETY_PARAM_INDEX = "xwSSSab1BtsLinkSpi";
	// 基站语音直通指示
	private static final String BTS_DVOICE_FLAG = "xwSSSab1BtsLinkBtsDVoiceFlag";
	// 基站信令IP
	private static final String BTS_SIGNAL_IP = "xwSSSab1BtsLinkBsSignalIP";
	// 基站信令端口
	private static final String BTS_SIGNAL_PORT = "xwSSSab1BtsLinkBsSignalPort";
	// 基站媒体IP
	private static final String BTS_MEDIA_IP = "xwSSSab1BtsLinkBsMedIP";
	// 基站媒体端口
	private static final String BTS_MEDIA_PORT = "xwSSSab1BtsLinkBsMedPort";
	// SAG信令IP
	private static final String SAG_SIGNAL_IP = "xwSSSab1BtsLinkSagSignalIP";
	// SAG信令端口
	private static final String SAG_SIGNAL_PORT = "xwSSSab1BtsLinkSagSignalPort";
	// SAG媒体IP
	private static final String SAG_MEDIA_IP = "xwSSSab1BtsLinkSagMedIP";
	// SAG媒体端口
	private static final String SAG_MEDIA_PORT = "xwSSSab1BtsLinkSagMedPort";
	// NAT注册间隔(s)
	private static final String NAT_REGISTER_INTERVAL = "xwSSSab1BtsLinkT_EST";
	// 注册次数
	private static final String REGISTER_COUNT = "xwSSSab1BtsLinkN_EST";
	// 握手间隔(s)
	private static final String SHAKE_HANDS_INTERVAL = "xwSSSab1BtsLinkT_HS";
	// 等待握手响应时间(s)
	private static final String TIME_WAITING_SHAKE_HANDS = "xwSSSab1BtsLinkT_HS_ACK";
	// NAT链路握手失连次数
	private static final String NAT_SHAKE_HANDS_DISCONNECT_COUNT = "xwSSSab1BtsLinkN_HS";
	// 透传包认证指示
	private static final String OSS_CERTIFICATION = "xwSSSab1BtsLinkOPCODE";
	// 客户端初始加密认证序号
	private static final String CLIENT_INIT_ENCRYPT_CERT_INDEX = "xwSSSab1BtsLinkCSEQ_CLI";
	// 服务端初始加密认证序号
	private static final String SERVER_INIT_ENCRYPT_CERT_INDEX = "xwSSSab1BtsLinkCSEQ_SER";
	// 基站名称
	private static final String BTS_NAME = "xwSSSab1BtsLinkDesc";
	// 信令链路状态
	private static final String BTS_LINK_STATUS = "xwSSSab1BtsLinkSignalLinkState";
	// 媒体链路状态
	private static final String MEDIA_LINK_STATUS = "xwSSSab1BtsLinkMedLinkState";
	// 是否支持广播多播业务
	private static final String MBMS_FLAG = "xwSSSab1BtsLinkMBMSFlag";
	// 地域ID
	private static final String DISTRICT_ID = "xwSSSab1BtsLinkZoneIND";
	// 基站频点类型
	private static final String BTS_FREQ_TYPE = "xwSSSab1BtsLinkBSFreqType";
	// 基站频点偏移量
	private static final String BTS_FREQ_OFFSET = "xwSSSab1BtsLinkBSFreq";
	// 前导序列号
	private static final String SEQ_ID = "xwSSSab1BtsLinkSeqID";
	// 时隙个数
	private static final String TS_NUM = "xwSSSab1BtsLinkTsNum";
	// /下行时隙个数
	private static final String DL_TS_NUM = "xwSSSab1BtsLinkDownlinkTsNum";
	// BCH子载波组号和时隙号信息
	private static final String BCH_FIELD = "xwSSSab1BtsLinkBCH";
	// RRCH的子载波组号和时隙号信息
	private static final String RRCH_FIELD = "xwSSSab1BtsLinkRRCH";
	// RARCH的子载波组号和时隙号信息
	private static final String RARCH_FIELD = "xwSSSab1BtsLinkRARCH";
	// 子载波组掩码
	private static final String SCG_MASK = "xwSSSab1BtsLinkSCGMask";
	// 子载波组0时隙掩码
	private static final String SCG_MASK_0TS = "xwSSSab1BtsLinkSCG0TSMask";
	// 子载波组1时隙掩码
	private static final String SCG_MASK_1TS = "xwSSSab1BtsLinkSCG1TSMask";
	// 子载波组2时隙掩码
	private static final String SCG_MASK_2TS = "xwSSSab1BtsLinkSCG2TSMask";
	// 子载波组3时隙掩码
	private static final String SCG_MASK_3TS = "xwSSSab1BtsLinkSCG3TSMask";
	// 子载波组4时隙掩码
	private static final String SCG_MASK_4TS = "xwSSSab1BtsLinkSCG4TSMask";

	// 增强公共信道SCG_IDX
	private static final String PESCG_IDX = "xwSSSab1BtsLinkPESCGIdx";
	// 增强公共信道PCH个数
	private static final String PEPCH_NUM = "xwSSSab1BtsLinkPEPCHNum";
	// 增强公共信道PCHID
	private static final String PEPEPCHID = "xwSSSab1BtsLinkPEPEPCHID";
	// 增强公共信道RARCH个数
	private static final String PERARCH_NUM = "xwSSSab1BtsLinkPERARCHNum";
	// 增强公共信道RRCH个数
	private static final String PERRCH_NUM = "xwSSSab1BtsLinkPERRCHNum";

	private static final String serviceName = "account_agent";

	private static String iM3000IP;

	private static String iM3000PORT;

	private static CommClient accountAgent = null;

	private static org.omg.CORBA.ORB orb = null;

	public static void initIM3000Info(String serverIp, String serverPort) {
		iM3000IP = serverIp;
		iM3000PORT = serverPort;

		// 设置连接Corba时需要的系统变量
		System.setProperty("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
		System.setProperty("org.omg.CORBA.ORBSingletonClass",
				"org.jacorb.orb.ORBSingleton");
	}

	/**
	 * 获取SAG的同播信息列表
	 */
	public static List<Simulcast> querySimulcastList(long sagId)
			throws Exception {
		// 先从sag查询信息
		try {
			DNSSRequest dnssRequest = getDNSSSimuQueryRequest(sagId);
			SimulcastManageServiceImpl.getLogger().debug(
					"Query Simulcast List, SAG ID:" + sagId);

			DNSSResponse dnssResponse = getDNSSResponse(dnssRequest);

			List<Simulcast> simuList = convertFromDNSSResponseToSimuList(dnssResponse);

			return simuList;
		} catch (SyncSagFailException e) {
			throw new Exception("iM3000_CONFIG_ERROR");
		} catch (Exception e) {
			throw new NullPointerException();
		}
	}

	/**
	 * 创建DNSS的同播资源查询请求
	 * 
	 * @param sagId
	 * @return
	 */
	private static DNSSRequest getDNSSSimuQueryRequest(long sagId) {
		DNSSRequest dnssRequest = new DNSSRequest();

		dnssRequest.setHeader(SESSION_ID, OPER_ID, QUERY_ACTION);

		dnssRequest.setParamValue(TABLE_NAME, SIMULCAST_TABLE);
		dnssRequest.setParamValue(SAG_ID, String.valueOf(sagId));

		return dnssRequest;
	}

	/**
	 * 将DNSSResponse模型转换成同播资源模型列表
	 * 
	 * @return
	 */
	private static List<Simulcast> convertFromDNSSResponseToSimuList(
			DNSSResponse dnssResponse) {
		List<Simulcast> list = new ArrayList<Simulcast>();

		String[] districtId = dnssResponse.getParamValues(ZoneID_FIELD);
		String[] freqType = dnssResponse.getParamValues(FreqType_FIELD);
		String[] freqOffset = dnssResponse.getParamValues(Freq_FIELD);
		String[] minNum = dnssResponse.getParamValues(MinNofRsv_FIELD);
		String[] maxNum = dnssResponse.getParamValues(MaxNofRsv_FIELD);
		String[] maxMbmsNum = dnssResponse.getParamValues(MaxGrpNum_FIELD);

		for (int i = 0; i < districtId.length; i++) {
			Simulcast simulcast = new Simulcast();
			simulcast.setDistrictId(Long.valueOf(districtId[i]));
			simulcast.setFreqType(Integer.valueOf(freqType[i]));
			simulcast.setFreqOffset(Integer.valueOf(freqOffset[i]));
			simulcast.setMinNum(Integer.valueOf(minNum[i]));
			simulcast.setMaxNum(Integer.valueOf(maxNum[i]));
			simulcast.setMaxMbmsNum(Integer.valueOf(maxMbmsNum[i]));

			list.add(simulcast);
		}
		return list;
	}

	/**
	 * 查询基站链路的DNSSRequest对象
	 * 
	 * @param sagID
	 * @param sessionID
	 * @return
	 */
	public static List<McBtsLink> queryBtsLinkList(long sagId) throws Exception {
		try {
			DNSSRequest dnssRequest = getDNSSBtsLinkQueryRequest(sagId);

			SimulcastManageServiceImpl.getLogger().debug(
					"Query BtsLink List, SAG ID:" + sagId);

			DNSSResponse dnssResponse = getDNSSResponse(dnssRequest);

			List<McBtsLink> linkList = convertFromDNSSResponseToMcBtsLinkList(dnssResponse);

			return linkList;
		} catch (SyncSagFailException e) {
			SimulcastManageServiceImpl.getLogger().error(
					"Error querying bts link list.", e);
			throw new Exception("iM3000_CONFIG_ERROR");
		} catch (Exception e) {
			throw new Exception("iM3000_CONFIG_ERROR");
		}
	}

	/**
	 * 创建DNSS的基站链路查询请求
	 * 
	 * @param sagId
	 * @return
	 */
	private static DNSSRequest getDNSSBtsLinkQueryRequest(long sagId) {
		DNSSRequest dnssRequest = new DNSSRequest();

		dnssRequest.setHeader(SESSION_ID, OPER_ID, QUERY_ACTION);

		dnssRequest.setParamValue(TABLE_NAME, BTS_LINK_TABLE);
		dnssRequest.setParamValue(SAG_ID, String.valueOf(sagId));

		return dnssRequest;
	}

	private static List<McBtsLink> convertFromDNSSResponseToMcBtsLinkList(
			DNSSResponse dnssResponse) throws Exception {
		List<McBtsLink> list = new ArrayList<McBtsLink>();
		dnssResponse.getParamNames();
		// 索引
		String[] LinkIndexs = dnssResponse.getParamValues(BTS_LINK_INDEX);
		// 基站ID后三位的十进制
		String[] BtsIDs = dnssResponse.getParamValues(BTS_ID);
		// 位置区
		String[] BtsLinkLais = dnssResponse.getParamValues(AREA_ID);
		// 是否支持广播多播业务
		String[] MBMSFlags = dnssResponse.getParamValues(MBMS_FLAG);
		// 地域ID
		String[] dustrictIDs = dnssResponse.getParamValues(DISTRICT_ID);
		// 基站频点类型
		String[] freqTypes = dnssResponse.getParamValues(BTS_FREQ_TYPE);
		// 基站频率偏移量
		String[] freqOffsets = dnssResponse.getParamValues(BTS_FREQ_OFFSET);
		// 基站sequenceID
		String[] seqIDs = dnssResponse.getParamValues(SEQ_ID);
		// 时隙个数
		String[] TsNums = dnssResponse.getParamValues(TS_NUM);
		// 下行时隙个数
		String[] downlinkTsNums = dnssResponse.getParamValues(DL_TS_NUM);

		String[] BCHs = dnssResponse.getParamValues(BCH_FIELD);
		String[] RRCHs = dnssResponse.getParamValues(RRCH_FIELD);
		String[] RARCHs = dnssResponse.getParamValues(RARCH_FIELD);

		// 子载波组掩码
		String[] SCGMasks = dnssResponse.getParamValues(SCG_MASK);
		String[] SCG0TSMasks = dnssResponse.getParamValues(SCG_MASK_0TS);
		String[] SCG1TSMasks = dnssResponse.getParamValues(SCG_MASK_1TS);
		String[] SCG2TSMasks = dnssResponse.getParamValues(SCG_MASK_2TS);
		String[] SCG3TSMasks = dnssResponse.getParamValues(SCG_MASK_3TS);
		String[] SCG4TSMasks = dnssResponse.getParamValues(SCG_MASK_4TS);

		String[] pescgIdxs = dnssResponse.getParamValues(PESCG_IDX);
		String[] pepchNums = dnssResponse.getParamValues(PEPCH_NUM);
		String[] pepepchIds = dnssResponse.getParamValues(PEPEPCHID);
		String[] perarchNums = dnssResponse.getParamValues(PERARCH_NUM);
		String[] perrchNum = dnssResponse.getParamValues(PERRCH_NUM);

		// 基站信令点(编码)
		String[] btsSingalPointIDs = dnssResponse
				.getParamValues(BTS_SIGNAL_POINT_ID);
		// 位置区
		String[] areaIDs = dnssResponse.getParamValues(AREA_ID);
		// 基站信令IP
		String[] btsSignalIPs = dnssResponse.getParamValues(BTS_SIGNAL_IP);
		// 基站信令端口
		String[] btsSignalPorts = dnssResponse.getParamValues(BTS_SIGNAL_PORT);
		// 基站语音IP
		String[] btsMediaIPs = dnssResponse.getParamValues(BTS_MEDIA_IP);
		// 基站语音端口
		String[] btsMediaPorts = dnssResponse.getParamValues(BTS_MEDIA_PORT);
		// sag信令IP
		String[] sagSignalIPs = dnssResponse.getParamValues(SAG_SIGNAL_IP);
		// sag信令端口
		String[] sagSignalPorts = dnssResponse.getParamValues(SAG_SIGNAL_PORT);
		// sag媒体IP
		String[] sagMediaIPs = dnssResponse.getParamValues(SAG_MEDIA_IP);
		// sag语音端口
		String[] sagMediaPorts = dnssResponse.getParamValues(SAG_MEDIA_PORT);
		// 基站名称
		String[] btsNames = dnssResponse.getParamValues(BTS_NAME);
		// 安全参数索引
		String[] safeParamIndexes = dnssResponse
				.getParamValues(SAFETY_PARAM_INDEX);
		// 基站语音直通指示
		String[] voiceDConnFlags = dnssResponse.getParamValues(BTS_DVOICE_FLAG);

		for (int i = 0; i < LinkIndexs.length; i++) {
			McBtsLink btsLink = new McBtsLink();
			btsLink.setLinkIndex(LinkIndexs[i]);

			btsLink.setBtsId(generateBtsNeIdBy(BtsLinkLais[i], BtsIDs[i]));
			btsLink.setMBMSFlag(MBMSFlags[i]);
			btsLink.setDistrictId(dustrictIDs[i]);
			btsLink.setFreqType(freqTypes[i]);
			btsLink.setFreqOffset(freqOffsets[i]);
			btsLink.setSeqID(seqIDs[i]);
			btsLink.setTsNum(TsNums[i]);
			btsLink.setDownlinkTsNum(downlinkTsNums[i]);
			btsLink.setBCH(BCHs[i]);
			btsLink.setRRCH(RRCHs[i]);
			btsLink.setRARCH(RARCHs[i]);
			btsLink.setSCGMask(SCGMasks[i]);
			btsLink.setSCG0TSMask(SCG0TSMasks[i]);
			btsLink.setSCG1TSMask(SCG1TSMasks[i]);
			btsLink.setSCG2TSMask(SCG2TSMasks[i]);
			btsLink.setSCG3TSMask(SCG3TSMasks[i]);
			btsLink.setSCG4TSMask(SCG4TSMasks[i]);

			btsLink.setBtsSignalPointID(btsSingalPointIDs[i]);
			btsLink.setAreaID(areaIDs[i]);
			btsLink.setBtsSignalIP(btsSignalIPs[i]);
			btsLink.setBtsSignalPort(btsSignalPorts[i]);
			btsLink.setBtsMediaIP(btsMediaIPs[i]);
			btsLink.setBtsMediaPort(btsMediaPorts[i]);
			btsLink.setSagSignalIP(sagSignalIPs[i]);
			btsLink.setSagSignalPort(sagSignalPorts[i]);
			btsLink.setSagMediaIP(sagMediaIPs[i]);
			btsLink.setSagMediaPort(sagMediaPorts[i]);
			btsLink.setBtsName(btsNames[i]);
			btsLink.setSafeParamIndex(safeParamIndexes[i]);
			btsLink.setBtsDVoiceFlag(voiceDConnFlags[i]);

			if (needPECCH()) {
				btsLink.setPescgIdx(pescgIdxs[i]);
				btsLink.setPepchNum(pepchNums[i]);
				btsLink.setPepepchId(pepepchIds[i]);
				btsLink.setPerarchNum(perarchNums[i]);
				btsLink.setPerrchNum(perrchNum[i]);
			}

			list.add(btsLink);
		}

		return list;
	}

	/**
	 * 根据位置区ID和十进制的sxc存的btsId拼接为8位的btsId
	 * 
	 * @param lai
	 * @param btsId
	 * @return
	 */
	public static String generateBtsNeIdBy(String lai, String btsId) {
		String btsid = Long.toHexString(((Long.parseLong(lai, 16) >> 8) << 12)
				+ Integer.parseInt(btsId));
		return "00000000".substring(btsid.length(), 8) + btsid;
	}

	/**
	 * 从SAG删除一个同播信息
	 * 
	 * @param simulcast
	 */
	public static void deleteSimulcast(long sagId, Simulcast simu)
			throws Exception {
		try {
			DNSSRequest dnssRequest = getDNSSSimuDelRequest(sagId, simu);

			SimulcastManageServiceImpl.getLogger().debug(
					"Delete Simulcast:" + simu.getIdx() + ", SAG ID:" + sagId
							+ "");

			DNSSResponse dnssResponse = getDNSSResponse(dnssRequest);
		} catch (Exception e) {
			throw new Exception("CONFIG_ERROR:" + sagId);
		}
	}

	/**
	 * 获取删除地域资源的DNSSRequest对象
	 * 
	 * @param sagID
	 * @param simu
	 * @return
	 */
	private static DNSSRequest getDNSSSimuDelRequest(long sagId, Simulcast simu) {
		DNSSRequest dnssRequest = new DNSSRequest();
		dnssRequest.setHeader(SESSION_ID, OPER_ID, DEL_ACTION);

		dnssRequest.setParamValue(TABLE_NAME, SIMULCAST_TABLE);
		dnssRequest.setParamValue(SAG_ID, String.valueOf(sagId));

		dnssRequest.setParamValue(CONDITION + ZoneID_FIELD,
				String.valueOf(simu.getDistrictId()));
		dnssRequest.setParamValue(CONDITION + FreqType_FIELD,
				String.valueOf(simu.getFreqType()));
		dnssRequest.setParamValue(CONDITION + Freq_FIELD,
				String.valueOf(simu.getFreqOffset()));
		return dnssRequest;
	}

	/**
	 * 修改SAG中的一个同播信息
	 * 
	 * @param simulcast
	 */
	public static void modifySimulcast(long sagId, Simulcast sagSimu,
			Simulcast simu) throws Exception {
		try {
			DNSSRequest dnssRequest = getDNSSSimuModifyRequest(sagId, sagSimu,
					simu);

			SimulcastManageServiceImpl.getLogger().debug(
					"Modify Simulcast:" + simu.getIdx() + ", SAG ID:" + sagId);

			DNSSResponse dnssResponse = getDNSSResponse(dnssRequest);
		} catch (Exception e) {
			throw new Exception("CONFIG_ERROR:" + sagId);
		}
	}

	/**
	 * 获取修改地域资源的DNSSRequest对象
	 * 
	 * @param sagId
	 * @param sagSimu
	 * @param simu
	 * @return
	 */
	private static DNSSRequest getDNSSSimuModifyRequest(long sagId,
			Simulcast sagSimu, Simulcast simu) {
		DNSSRequest dnssRequest = new DNSSRequest();
		dnssRequest.setHeader(SESSION_ID, OPER_ID, MODIFY_ACTION);

		dnssRequest.setParamValue(TABLE_NAME, SIMULCAST_TABLE);
		dnssRequest.setParamValue(SAG_ID, String.valueOf(sagId));
		// 主键
		dnssRequest.setParamValue(ZoneID_FIELD,
				String.valueOf(simu.getDistrictId()));
		dnssRequest.setParamValue(FreqType_FIELD,
				String.valueOf(simu.getFreqType()));
		dnssRequest.setParamValue(Freq_FIELD,
				String.valueOf(simu.getFreqOffset()));
		// 条件
		dnssRequest.setParamValue(CONDITION + ZoneID_FIELD,
				String.valueOf(simu.getDistrictId()));
		dnssRequest.setParamValue(CONDITION + FreqType_FIELD,
				String.valueOf(simu.getFreqType()));
		dnssRequest.setParamValue(CONDITION + Freq_FIELD,
				String.valueOf(simu.getFreqOffset()));

		// 修改项
		dnssRequest.setParamValue(MinNofRsv_FIELD,
				String.valueOf(simu.getMinNum()));
		dnssRequest.setParamValue(MaxNofRsv_FIELD,
				String.valueOf(simu.getMaxNum()));
		dnssRequest.setParamValue(MaxGrpNum_FIELD,
				String.valueOf(simu.getMaxMbmsNum()));
		return dnssRequest;
	}

	/**
	 * 向SAG中增加同播信息
	 * 
	 * @param simu
	 */
	public static void addSimulcast(long sagId, Simulcast simu)
			throws Exception {
		try {
			DNSSRequest dnssRequest = getDNSSSimuAddRequest(sagId, simu);

			SimulcastManageServiceImpl.getLogger().debug(
					"Add Simulcast:" + simu.getIdx() + ", SAG ID:" + sagId);
			DNSSResponse dnssResponse = getDNSSResponse(dnssRequest);
		} catch (Exception e) {
			throw new Exception("CONFIG_ERROR:" + sagId);
		}
	}

	/**
	 * 获取增加同播资源的DNSSRequest对象
	 * 
	 * @param sagId
	 * @param simu
	 * @return
	 */
	private static DNSSRequest getDNSSSimuAddRequest(long sagId, Simulcast simu) {
		DNSSRequest dnssRequest = new DNSSRequest();

		dnssRequest.setHeader(SESSION_ID, OPER_ID, ADD_ACTION);

		dnssRequest.setParamValue(TABLE_NAME, SIMULCAST_TABLE);
		dnssRequest.setParamValue(SAG_ID, String.valueOf(sagId));

		dnssRequest.setParamValue(ZoneID_FIELD,
				String.valueOf(simu.getDistrictId()));
		dnssRequest.setParamValue(FreqType_FIELD,
				String.valueOf(simu.getFreqType()));
		dnssRequest.setParamValue(Freq_FIELD,
				String.valueOf(simu.getFreqOffset()));

		dnssRequest.setParamValue(MinNofRsv_FIELD,
				String.valueOf(simu.getMinNum()));
		dnssRequest.setParamValue(MaxNofRsv_FIELD,
				String.valueOf(simu.getMaxNum()));
		dnssRequest.setParamValue(MaxGrpNum_FIELD,
				String.valueOf(simu.getMaxMbmsNum()));

		return dnssRequest;
	}

	/**
	 * 向SAG中修改基站链路
	 * 
	 * @param sagId
	 * @param link
	 * @throws Exception
	 */
	public static void modifyMcBtsLink(long sagId, McBtsLink sagLink,
			McBtsLink link) throws Exception {
		try {
			DNSSRequest dnssRequest = getDNSSMcBtsLinkModifyRequest(sagId,
					sagLink, link);
			SimulcastManageServiceImpl.getLogger().debug(
					"Modify BtsLink, BTS ID:" + sagLink.getBtsId()
							+ ", SAG ID:" + sagId);
			DNSSResponse dnssResponse = getDNSSResponse(dnssRequest);
		} catch (Exception e) {
			throw new Exception("CONFIG_ERROR:" + sagId);
		}
	}

	/**
	 * 获取修改基站链路的DNSSRequest对象
	 * 
	 * @param sagId
	 * @param btsLink
	 * @return
	 * @throws Exception
	 */
	private static DNSSRequest getDNSSMcBtsLinkModifyRequest(long sagId,
			McBtsLink sagLink, McBtsLink btsLink) throws Exception {
		DNSSRequest dnssRequest = new DNSSRequest();

		dnssRequest.setHeader(SESSION_ID, OPER_ID, MODIFY_ACTION);

		dnssRequest.setParamValue(TABLE_NAME, BTS_LINK_TABLE);
		dnssRequest.setParamValue(SAG_ID, String.valueOf(sagId));

		dnssRequest.setParamValue(BTS_LINK_INDEX, sagLink.getLinkIndex()
				.toString());

		dnssRequest.setParamValue(CONDITION + BTS_LINK_INDEX, sagLink
				.getLinkIndex().toString());

		// btsId只取低12位的十进制数
		if (notEqual(btsLink.getBtsId(), sagLink.getBtsId())) {
			long btsId = Long.parseLong(btsLink.getBtsId(), 16);
			btsId = btsId & 0xfff;
			dnssRequest.setParamValue(BTS_ID, String.valueOf(btsId));
		}
		// 基站信令点
		if (notEqual(btsLink.getBtsSignalPointID(),
				sagLink.getBtsSignalPointID()))
			dnssRequest.setParamValue(BTS_SIGNAL_POINT_ID,
					btsLink.getBtsSignalPointID());
		// 位置区ID
		if (notEqual(btsLink.getAreaID(), sagLink.getAreaID()))
			dnssRequest.setParamValue(AREA_ID, btsLink.getAreaID());
		// 基站类型
		// if (notEqual(btsLink.getBtsType(), sagLink.getBtsType()))
		// dnssRequest.setParamValue(BTS_TYPE, btsLink.getBtsType());
		// 安全参数索引
		if (notEqual(btsLink.getSafeParamIndex(), sagLink.getSafeParamIndex()))
			dnssRequest.setParamValue(SAFETY_PARAM_INDEX,
					btsLink.getSafeParamIndex());
		// 基站语音直通指示
		if (notEqual(btsLink.getBtsDVoiceFlag(), sagLink.getBtsDVoiceFlag()))
			dnssRequest.setParamValue(BTS_DVOICE_FLAG,
					btsLink.getBtsDVoiceFlag());
		// 基站信令IP
		String btsSignalIp = btsLink.getBtsSignalIP();
		if (notEqual(btsSignalIp, sagLink.getBtsSignalIP())
				&& !btsSignalIp.equals("0.0.0.0"))
			dnssRequest.setParamValue(BTS_SIGNAL_IP, btsSignalIp);
		// 基站信令端口
		if (notEqual(btsLink.getBtsSignalPort(), sagLink.getBtsSignalPort()))
			dnssRequest.setParamValue(BTS_SIGNAL_PORT,
					btsLink.getBtsSignalPort());
		// 基站媒体IP
		String btsMediaIp = btsLink.getBtsMediaIP();
		if (notEqual(btsMediaIp, sagLink.getBtsMediaIP())
				&& !btsMediaIp.equals("0.0.0.0"))
			dnssRequest.setParamValue(BTS_MEDIA_IP, btsMediaIp);
		// 基站媒体端口
		if (notEqual(btsLink.getBtsMediaPort(), sagLink.getBtsMediaPort()))
			dnssRequest
					.setParamValue(BTS_MEDIA_PORT, btsLink.getBtsMediaPort());
		// SAG信令IP
		if (notEqual(btsLink.getSagSignalIP(), sagLink.getSagSignalIP()))
			dnssRequest.setParamValue(SAG_SIGNAL_IP, btsLink.getSagSignalIP());
		// SAG信令端口
		if (notEqual(btsLink.getSagSignalPort(), sagLink.getSagSignalPort()))
			dnssRequest.setParamValue(SAG_SIGNAL_PORT,
					btsLink.getSagSignalPort());
		// SAG媒体IP
		if (notEqual(btsLink.getSagMediaIP(), sagLink.getSagMediaIP()))
			dnssRequest.setParamValue(SAG_MEDIA_IP, btsLink.getSagMediaIP());
		// SAG媒体端口
		if (notEqual(btsLink.getSagMediaPort(), sagLink.getSagMediaPort()))
			dnssRequest
					.setParamValue(SAG_MEDIA_PORT, btsLink.getSagMediaPort());
		// NAT注册间隔(s)
		// if (notEqual(btsLink.getNatRegisterInterval(),
		// sagLink.getNatRegisterInterval()))
		// dnssRequest.setParamValue(NAT_REGISTER_INTERVAL,
		// btsLink.getNatRegisterInterval());
		// 注册次数
		// if (notEqual(btsLink.getRegisterCount(), sagLink.getRegisterCount()))
		// dnssRequest.setParamValue(REGISTER_COUNT,
		// btsLink.getRegisterCount());
		// 握手间隔(s)
		// if (notEqual(btsLink.getShakeHandsInteval(),
		// sagLink.getShakeHandsInteval()))
		// dnssRequest.setParamValue(SHAKE_HANDS_INTERVAL,
		// btsLink.getShakeHandsInteval());
		// 等待握手响应时间(s)
		// if (notEqual(btsLink.getTimeWaitingShakeHands(),
		// sagLink.getTimeWaitingShakeHands()))
		// dnssRequest.setParamValue(TIME_WAITING_SHAKE_HANDS,
		// btsLink.getTimeWaitingShakeHands());
		// NAT链路握手失连次数
		// if (notEqual(btsLink.getNatShakeHandsDisconnectCount(),
		// sagLink.getNatShakeHandsDisconnectCount()))
		// dnssRequest.setParamValue(NAT_SHAKE_HANDS_DISCONNECT_COUNT,
		// btsLink.getNatShakeHandsDisconnectCount());
		// 透传包认证指示
		// if (notEqual(btsLink.getOssCert(), sagLink.getOssCert()))
		// dnssRequest.setParamValue(OSS_CERTIFICATION, btsLink.getOssCert());
		// 客户端初始加密认证序号
		// if (notEqual(btsLink.getCliInitEncryptIndex(),
		// sagLink.getCliInitEncryptIndex()))
		// dnssRequest.setParamValue(CLIENT_INIT_ENCRYPT_CERT_INDEX,
		// btsLink.getCliInitEncryptIndex());
		// 服务端初始加密认证序号
		// if (notEqual(btsLink.getSerInitEncryptIndex(),
		// sagLink.getSerInitEncryptIndex()))
		// dnssRequest.setParamValue(SERVER_INIT_ENCRYPT_CERT_INDEX,
		// btsLink.getSerInitEncryptIndex());
		// 基站名称
		if (notEqual(btsLink.getBtsName(), sagLink.getBtsName()))
			dnssRequest.setParamValue(BTS_NAME, btsLink.getBtsName());
		// 信令链路状态
		// if (notEqual(btsLink.getBtsLinkStatus(), sagLink.getBtsLinkStatus()))
		// dnssRequest.setParamValue(BTS_LINK_STATUS,
		// btsLink.getBtsLinkStatus());
		// 媒体链路状态
		// if (notEqual(btsLink.getMediaLinkStatus(),
		// sagLink.getMediaLinkStatus()))
		// dnssRequest.setParamValue(MEDIA_LINK_STATUS,
		// btsLink.getMediaLinkStatus());
		if (notEqual(btsLink.getMBMSFlag(), sagLink.getMBMSFlag()))
			dnssRequest.setParamValue(MBMS_FLAG, btsLink.getMBMSFlag());
		if (notEqual(btsLink.getDistrictId(), sagLink.getDistrictId()))
			dnssRequest.setParamValue(DISTRICT_ID, btsLink.getDistrictId());
		if (notEqual(btsLink.getFreqType(), sagLink.getFreqType()))
			dnssRequest.setParamValue(BTS_FREQ_TYPE, btsLink.getFreqType());
		if (notEqual(btsLink.getFreqOffset(), sagLink.getFreqOffset()))
			dnssRequest.setParamValue(BTS_FREQ_OFFSET, btsLink.getFreqOffset());
		if (notEqual(btsLink.getSeqID(), sagLink.getSeqID()))
			dnssRequest.setParamValue(SEQ_ID, btsLink.getSeqID());
		if (notEqual(btsLink.getTsNum(), sagLink.getTsNum()))
			dnssRequest.setParamValue(TS_NUM, btsLink.getTsNum());
		if (notEqual(btsLink.getDownlinkTsNum(), sagLink.getDownlinkTsNum()))
			dnssRequest.setParamValue(DL_TS_NUM, btsLink.getDownlinkTsNum());
		if (notEqual(btsLink.getBCH(), sagLink.getBCH()))
			dnssRequest.setParamValue(BCH_FIELD, btsLink.getBCH());
		if (notEqual(btsLink.getRRCH(), sagLink.getRRCH()))
			dnssRequest.setParamValue(RRCH_FIELD, btsLink.getRRCH());
		if (notEqual(btsLink.getRARCH(), sagLink.getRARCH()))
			dnssRequest.setParamValue(RARCH_FIELD, btsLink.getRARCH());
		if (notEqual(btsLink.getSCGMask(), sagLink.getSCGMask()))
			dnssRequest.setParamValue(SCG_MASK, btsLink.getSCGMask());
		if (notEqual(btsLink.getSCG0TSMask(), sagLink.getSCG0TSMask()))
			dnssRequest.setParamValue(SCG_MASK_0TS, btsLink.getSCG0TSMask());
		if (notEqual(btsLink.getSCG1TSMask(), sagLink.getSCG1TSMask()))
			dnssRequest.setParamValue(SCG_MASK_1TS, btsLink.getSCG1TSMask());
		if (notEqual(btsLink.getSCG2TSMask(), btsLink.getSCG2TSMask()))
			dnssRequest.setParamValue(SCG_MASK_2TS, btsLink.getSCG2TSMask());
		if (notEqual(btsLink.getSCG3TSMask(), sagLink.getSCG3TSMask()))
			dnssRequest.setParamValue(SCG_MASK_3TS, btsLink.getSCG3TSMask());
		if (notEqual(btsLink.getSCG4TSMask(), sagLink.getSCG4TSMask()))
			dnssRequest.setParamValue(SCG_MASK_4TS, btsLink.getSCG4TSMask());

		if (needPECCH()) {
			if (notEqual(btsLink.getPescgIdx(), sagLink.getPescgIdx()))
				dnssRequest.setParamValue(PESCG_IDX, btsLink.getPescgIdx());
			if (notEqual(btsLink.getPepchNum(), sagLink.getPepchNum()))
				dnssRequest.setParamValue(PEPCH_NUM, btsLink.getPepchNum());
			if (notEqual(btsLink.getPepepchId(), sagLink.getPepepchId()))
				dnssRequest.setParamValue(PEPEPCHID, btsLink.getPepepchId());
			if (notEqual(btsLink.getPerarchNum(), sagLink.getPerarchNum()))
				dnssRequest.setParamValue(PERARCH_NUM, btsLink.getPerarchNum());
			if (notEqual(btsLink.getPerrchNum(), sagLink.getPerrchNum()))
				dnssRequest.setParamValue(PERRCH_NUM, btsLink.getPerrchNum());
		}

		return dnssRequest;
	}

	public static void addMcBtsLink(int index, long sagId, McBtsLink link)
			throws Exception {
		try {
			DNSSRequest dnssRequest = getDNSSMcBtsLinkAddRequest(index, sagId,
					link);

			SimulcastManageServiceImpl.getLogger().debug(
					"Add BtsLink,BTS ID:" + link.getBtsId() + ", SAG ID:"
							+ sagId);
			DNSSResponse dnssResponse = getDNSSResponse(dnssRequest);
		} catch (Exception e) {
			throw new Exception("CONFIG_ERROR:" + sagId);
		}
	}

	public static DNSSRequest getDNSSMcBtsLinkAddRequest(int index, long sagId,
			McBtsLink btsLink) throws Exception {
		DNSSRequest dnssRequest = new DNSSRequest();

		dnssRequest.setHeader(SESSION_ID, OPER_ID, ADD_ACTION);

		dnssRequest.setParamValue(TABLE_NAME, BTS_LINK_TABLE);
		dnssRequest.setParamValue(SAG_ID, String.valueOf(sagId));

		dnssRequest.setParamValue(BTS_LINK_INDEX, String.valueOf(index));

		dnssRequest.setParamValue(CONDITION + BTS_LINK_INDEX,
				String.valueOf(index));

		// btsId只取低12位的十进制数
		long btsId = Long.parseLong(btsLink.getBtsId(), 16);
		btsId = btsId & 0xfff;
		dnssRequest.setParamValue(BTS_ID, String.valueOf(btsId));
		// 基站信令点
		dnssRequest.setParamValue(BTS_SIGNAL_POINT_ID,
				btsLink.getBtsSignalPointID());
		// 位置区ID
		dnssRequest.setParamValue(AREA_ID, btsLink.getAreaID());
		// 基站类型
		dnssRequest.setParamValue(BTS_TYPE, btsLink.getBtsType());
		// 安全参数索引
		dnssRequest.setParamValue(SAFETY_PARAM_INDEX,
				btsLink.getSafeParamIndex());
		// 基站语音直通指示
		dnssRequest.setParamValue(BTS_DVOICE_FLAG, btsLink.getBtsDVoiceFlag());
		// 基站信令IP
		dnssRequest.setParamValue(BTS_SIGNAL_IP, btsLink.getBtsSignalIP());
		// 基站信令端口
		dnssRequest.setParamValue(BTS_SIGNAL_PORT, btsLink.getBtsSignalPort());
		// 基站媒体IP
		dnssRequest.setParamValue(BTS_MEDIA_IP, btsLink.getBtsMediaIP());
		// 基站媒体端口
		dnssRequest.setParamValue(BTS_MEDIA_PORT, btsLink.getBtsMediaPort());
		// SAG信令IP
		dnssRequest.setParamValue(SAG_SIGNAL_IP, btsLink.getSagSignalIP());
		// SAG信令端口
		dnssRequest.setParamValue(SAG_SIGNAL_PORT, btsLink.getSagSignalPort());
		// SAG媒体IP
		dnssRequest.setParamValue(SAG_MEDIA_IP, btsLink.getSagMediaIP());
		// SAG媒体端口
		// 修改媒体端口报错(修改记录失败！:DM EPI接口返回错误)
		dnssRequest.setParamValue(SAG_MEDIA_PORT, btsLink.getSagMediaPort());
		// NAT注册间隔(s)
		dnssRequest.setParamValue(NAT_REGISTER_INTERVAL,
				btsLink.getNatRegisterInterval());
		// 注册次数
		dnssRequest.setParamValue(REGISTER_COUNT, btsLink.getRegisterCount());
		// 握手间隔(s)
		dnssRequest.setParamValue(SHAKE_HANDS_INTERVAL,
				btsLink.getShakeHandsInteval());
		// 等待握手响应时间(s)
		dnssRequest.setParamValue(TIME_WAITING_SHAKE_HANDS,
				btsLink.getTimeWaitingShakeHands());
		// NAT链路握手失连次数
		dnssRequest.setParamValue(NAT_SHAKE_HANDS_DISCONNECT_COUNT,
				btsLink.getNatShakeHandsDisconnectCount());
		// 透传包认证指示
		dnssRequest.setParamValue(OSS_CERTIFICATION, btsLink.getOssCert());
		// 客户端初始加密认证序号
		dnssRequest.setParamValue(CLIENT_INIT_ENCRYPT_CERT_INDEX,
				btsLink.getCliInitEncryptIndex());
		// 服务端初始加密认证序号
		dnssRequest.setParamValue(SERVER_INIT_ENCRYPT_CERT_INDEX,
				btsLink.getSerInitEncryptIndex());
		// 基站名称
		dnssRequest.setParamValue(BTS_NAME, btsLink.getBtsName());
		// 信令链路状态
		// dnssRequest.setParamValue(BTS_LINK_STATUS,
		// btsLink.getBtsLinkStatus());
		// 媒体链路状态
		// dnssRequest.setParamValue(MEDIA_LINK_STATUS,
		// btsLink.getMediaLinkStatus());
		dnssRequest.setParamValue(MBMS_FLAG, btsLink.getMBMSFlag());
		dnssRequest.setParamValue(DISTRICT_ID, btsLink.getDistrictId());
		dnssRequest.setParamValue(BTS_FREQ_TYPE, btsLink.getFreqType());
		dnssRequest.setParamValue(BTS_FREQ_OFFSET, btsLink.getFreqOffset());
		dnssRequest.setParamValue(SEQ_ID, btsLink.getSeqID());
		dnssRequest.setParamValue(TS_NUM, btsLink.getTsNum());
		dnssRequest.setParamValue(DL_TS_NUM, btsLink.getDownlinkTsNum());
		dnssRequest.setParamValue(BCH_FIELD, btsLink.getBCH());
		dnssRequest.setParamValue(RRCH_FIELD, btsLink.getRRCH());
		dnssRequest.setParamValue(RARCH_FIELD, btsLink.getRARCH());
		dnssRequest.setParamValue(SCG_MASK, btsLink.getSCGMask());
		dnssRequest.setParamValue(SCG_MASK_0TS, btsLink.getSCG0TSMask());
		dnssRequest.setParamValue(SCG_MASK_1TS, btsLink.getSCG1TSMask());
		dnssRequest.setParamValue(SCG_MASK_2TS, btsLink.getSCG2TSMask());
		dnssRequest.setParamValue(SCG_MASK_3TS, btsLink.getSCG3TSMask());
		dnssRequest.setParamValue(SCG_MASK_4TS, btsLink.getSCG4TSMask());

		if (needPECCH()) {
			dnssRequest.setParamValue(PESCG_IDX, btsLink.getPescgIdx());
			dnssRequest.setParamValue(PEPCH_NUM, btsLink.getPepchNum());
			dnssRequest.setParamValue(PEPEPCHID, btsLink.getPepepchId());
			dnssRequest.setParamValue(PERARCH_NUM, btsLink.getPerarchNum());
			dnssRequest.setParamValue(PERRCH_NUM, btsLink.getPerrchNum());
		}

		return dnssRequest;
	}

	public static void deleteMcBtsLink(String index, long sagId)
			throws Exception {
		try {
			DNSSRequest dnssRequest = getDNSSMcBtsLinkDelRequest(index, sagId);

			SimulcastManageServiceImpl.getLogger().debug(
					"Delete BtsLink,index:" + index + " SAG ID:" + sagId);
			DNSSResponse dnssResponse = getDNSSResponse(dnssRequest);
		} catch (Exception e) {
			throw new Exception("CONFIG_ERROR:" + sagId);
		}
	}

	public static DNSSRequest getDNSSMcBtsLinkDelRequest(String index,
			long sagId) {
		DNSSRequest dnssRequest = new DNSSRequest();

		dnssRequest.setHeader(SESSION_ID, OPER_ID, DEL_ACTION);

		dnssRequest.setParamValue(TABLE_NAME, BTS_LINK_TABLE);
		dnssRequest.setParamValue(SAG_ID, String.valueOf(sagId));

		dnssRequest.setParamValue(BTS_LINK_INDEX, index);

		dnssRequest.setParamValue(CONDITION + BTS_LINK_INDEX, index);

		return dnssRequest;
	}

	/**
	 * 比较两个字符串是否相等
	 * 
	 * @param link
	 * @param sagLink
	 * @return boolean
	 */
	private static boolean notEqual(String link, String sagLink) {
		// 如果ems上的值是空的,那就不进行同步
		if (StringUtils.isBlank(link))
			return true;

		return !StringUtils.equals(link, sagLink);
	}

	/**
	 * 向iM3000发送请求,获得结果
	 * 
	 * @param dnssRequest
	 * @return
	 * @throws Exception
	 */
	private static DNSSResponse getDNSSResponse(DNSSRequest dnssRequest)
			throws Exception {
		DNSSResponse dnssResponse = new DNSSResponse();

		CommClient accountAgent = getAccountAgent();

		ClientMsg msg = DNSSMsgUtil.createClientMsg(dnssRequest);
		ReturnClientMsgHolder hoder = new ReturnClientMsgHolder();
		accountAgent.send_r_i(msg, hoder);

		// SimulcastManageServiceImpl.getLogger().debug(
		// "Sending DnssRequest to im3000, SAG ID:"
		// + dnssRequest.getParamValue(SAG_ID));
		dnssResponse = DNSSMsgUtil.createDNSSResponse(hoder.value);
		// 判断是否成功
		if (!dnssResponse.getResult().equals("0")) {
			SimulcastManageServiceImpl.getLogger().error(
					"DnssResponse Error: " + dnssResponse.getResult()
							+ ", Reason:" + dnssResponse.getReason());

			throw new Exception(dnssResponse.getReason());
		}
		return dnssResponse;
	}

	/**
	 * 获得CommClient对象
	 * 
	 * @return
	 */
	private static synchronized CommClient getAccountAgent()
			throws SyncSagFailException {

		if (accountAgent != null)
			return accountAgent;

		String[] args = new String[0];
		NamingContextExt nc = null;

		try {
			// 初始化Corba
			orb = org.omg.CORBA.ORB.init(args, getCorbaProperties());
			// 获取名字空间句柄
			nc = NamingContextExtHelper.narrow(orb
					.resolve_initial_references("NameService"));
		} catch (Exception ex) {
			throw new SyncSagFailException(
					SyncSagResult.RETURN_CONNNAMESERVER_FAIL);
		}
		// 获取cnms_agent 对象
		try {
			org.omg.CORBA.Object o = nc.resolve(nc.to_name(serviceName));
			accountAgent = CommClientHelper.narrow(o);
		} catch (Exception ex) {
			throw new SyncSagFailException(SyncSagResult.RETURN_GETSAGOBJ_FAIL);
		}

		return accountAgent;
	}

	/**
	 * 是否需要增强信道配置信息
	 * 
	 * @throws Exception
	 */
	private static boolean needPECCH() throws Exception {
		ChannelComparableModeService service = AppContext.getCtx().getBean(
				ChannelComparableModeService.class);
		ChannelComparableMode mode = service.queryFromEMS();
		if (mode.getChannelMode() == ChannelComparableMode.PCCH_ONLY)
			return false;
		return true;
	}

	/**
	 * 设置Corba连接的基本属性
	 * 
	 * @return
	 */
	private static Properties getCorbaProperties() {
		Properties proterties = new Properties();
		String corbaloc = "corbaloc:iiop:" + iM3000IP + ":" + iM3000PORT
				+ "/NameService";
		proterties.put("ORBInitRef.NameService", corbaloc);
		proterties.put("jacorb.retries", "0");
		proterties.put("jacorb.retry_interval", "500");
		proterties.put("jacorb.connection.client.connect_timeout", "5000");
		proterties.put("jacorb.connection.client.pending_reply_timeout",
				"15000");

		return proterties;
	}

	/**
	 * 获取ems的频率类型对应的频率值
	 * 
	 * @param freqType
	 * @return
	 */
	public static int getFreqNumber(int freqType) {
		switch (freqType) {
		case 0:
			return 1800;
		case 1:
			return 2400;
		case 2:
			return 400;
		case 3:
			return 3300;
		case 4:
			return 700;
		case 5:
			return 2100;
		case 6:
			return 340;
		case 7:
			// 340M(R20)
			return 340;
		case 8:
			return 1400;
		default:
			return 340;
		}
	}

	/**
	 * 断开Corba连接
	 */
	public static void disconnect() {
		if (accountAgent != null) {
			accountAgent = null;
		}

		if (orb != null) {
			orb.shutdown(true);
			orb.destroy();
			orb = null;
		}

	}
}
