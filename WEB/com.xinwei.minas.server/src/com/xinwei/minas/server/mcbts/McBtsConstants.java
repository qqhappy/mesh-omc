/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-27	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts;

/**
 * 
 * McBts业务操作常量
 * 
 * @author chenjunhua
 * 
 */

public class McBtsConstants {

	public static final String DOT = ".";

	// McBts实体类型
	public static final String ENTITY_TYPE_MCBTS = "McBts";
	public static final String ENTITY_TYPE_MCBTS_L3 = "McBts.L3";
	public static final String ENTITY_TYPE_MCBTS_L2 = "McBts.L2";
	public static final String ENTITY_TYPE_MCBTS_L1 = "McBts.L1";
	public static final String ENTITY_TYPE_MCBTS_MCP = "McBts.MCP";
	public static final String ENTITY_TYPE_MCBTS_AUX = "McBts.AUX";
	public static final String ENTITY_TYPE_MCBTS_ENV = "McBts.ENV";
	public static final String ENTITY_TYPE_MCBTS_PLL = "McBts.PLL";
	public static final String ENTITY_TYPE_MCBTS_RF = "McBts.RF";
	public static final String ENTITY_TYPE_MCBTS_GPS = "McBts.GPS";
	public static final String ENTITY_TYPE_MCBTS_FPGA = "McBts.FPGA";
	public static final String ENTITY_TYPE_MCBTS_FEP = "McBts.FEP";
	public static final String ENTITY_TYPE_MCBTS_CORE9 = "McBts.CORE9";
	public static final String ENTITY_TYPE_MCBTS_RRU = "McBts.RRU";
	public static final String ENTITY_TYPE_MCBTS_AIF = "McBts.AIF";
	public static final String ENTITY_TYPE_MCBTS_BATTERY = "McBts.BATTERY";

	public static final String OPERATION_QUERY = "query";

	public static final String OPERATION_CONFIG = "config";

	public static final String PROTOCOL_MSG_AREA = "MsgArea";

	public static final String PROTOCOL_MA = "MA";

	public static final String PROTOCOL_MOC = "MOC";

	public static final String PROTOCOL_ACTION_TYPE = "ActionType";

	public static final String CHARSET_US_ASCII = "US-ASCII";

	public static final String TYPE_UNSIGNED_NUMBER = "UnsignedNumber";

	public static final String TYPE_SIGNED_NUMBER = "SignedNumber";

	public static final String TYPE_IPv4 = "IPv4";

	// 定长String
	public static final String TYPE_STRING = "String";

	public static final String TYPE_TRIMMED_STRING = "TrimString";

	// 变长String
	public static final String TYPE_VAR_STRING = "VarString";

	// 十六进制定长String
	public static final String TYPE_HEX_STRING = "HexString";

	//保留字段
	public static final String TYPE_RESERVE = "Reserve";

	public static final String TYPE_LIST = "List";

}
