/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-23	|  yinyuelin 	    |  create the file                       
 */

package com.xinwei.lte.web.domain;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class LteFlag{
	
	public static String NO_AUTHORITY = "当前用户无权进行此操作!";
	
	public static String flagReturn(String lteFlag){
		
		String result = "";
		
		int flag = Integer.parseInt(lteFlag);
		
		switch(flag){
			case 1 : 
				result = "协议解析错误";
				break;
			case 2 : 
				result = "网关未接入";
				break;
			case 3 : 
				result = "TAG值错误";
				break;
			case 16 : 
				result = "数据库执行错误";
				break;
			case 17 : 
				result = "MME未连接到数据库";
				break;
			case 18 : 
				result = "重复键值";
				break;
			case 19 : 
				result = "引用的记录不存在";
				break;
			case 20 : 
				result = "记录已被引用";
				break;
			case 21 : 
				result = "记录不存在";
				break;
			case 32 : 
				result = "GW处理错误";
				break;
			case 33 : 
				result = "消息格式错误";
				break;
			case 34 : 
				result = "消息内容非法";
				break;
			case 35 : 
				result = "IP地址总数益出";
				break;
			case 36 : 
				result = "IP地址池大小溢出";
				break;
			case 37 : 
				result = "IP地址池冲突";
				break;
			case 38 : 
				result = "用户正在线";
				break;
			case 39 : 
				result = "消息关键字存在";
				break;
			case 40 : 
				result = "关键字不存在";
				break;
			case 41 : 
				result = "存储数据出错";
				break;
			case 42 : 
				result = "处理内部错误";
				break;
			case 64 : 
				result = "SIP处理错误";
				break;
			case 80 : 
				result = "号码分析处理错误";
				break;
			case 96 : 
				result = "S1处理错误";
				break;
			case 97 : 
				result = "S1AP ID错误";
				break;
			case 112 : 
				result = "TA处理错误";
				break;
			case 113 : 
				result = "TA ID错误";
				break;
			case 128 : 
				result = "全局配置错误";
				break;
			case 129 : 
				result = "MME cfg.xml->OAM配置错误";
				break;
			case 130 : 
				result = "掩码错误";
				break;
			case 131 : 
				result = "RAN MME网络号不同";
				break;
			case 132 : 
				result = "地址已被使用";
				break;
			case 133 : 
				result = "ran地址错误";
				break;
			case 134 : 
				result = "一块板组网，MME和RAN地址ID不同";
				break;
			case 135 : 
				result = "两块板组网，MME和RAN地址相同";
				break;
			case 136 : 
				result = "PLMN输入错误，只能是数字";
				break;
			case 137 : 
				result = "RAN和PDN地址相同";
				break;
			case 144 : 
				result = "用户信息错误";
				break;
			case 145 : 
				result = "终端静态IP设置失败，内部错误";
				break;
			case 146 : 
				result = "SDC与GW安装在同一机器，不能直接修改SDC IP";
				break;
			case 147 : 
				result = "修改SDC IP 核心网将会重启";
				break;
			case 148 : 
				result = "修改RAN IP 核心网将会重启";
				break;
			case 149 : 
				result = "修改PDN IP 核心网将会重启";
				break;
			case 150 : 
				result = "修改系统配置表 核心网将会重启";
				break;
			case 151 : 
				result = "核心网系统错误";
				break;
			case 0x98 : 
				result = "用户IP资源中不存在当前配置的IP地址(段)";
				break;
			case 160 : 
				result = "用户业务错误";
				break;
			case 176 : 
				result = "参数模板错误";
				break;
			case 177 : 
				result = "APN带宽错误，须大于等于对应的UE带宽";
				break;
			case 178 : 
				result = "imsi 长度错误或imsi非纯数字";
				break;
			case 179 : 
				result = "imsi重复";
				break;
			case 180 : 
				result = "用户号码与组号码重复";
				break;
			case 181 : 
				result = "用户号码已被外网用户使用";
				break;
			case 182 : 
				result = "用户号码重复";
				break;
			case 183 : 
				result = "引用的imsi不存在";
				break;
			case 184 : 
				result = "imsi已被其他号码使用";
				break;
			case 185 : 
				result = "引用的temp_id不存在";
				break;
			case 186 : 
				result = "imsi已与用户号码绑定";
				break;
			case 187 : 
				result = "号码已被视频监控设备使用";
				break;
			case 188 : 
				result = "用户已被转为调度用户,不能删除";
				break;
			case 189 : 
				result = "模板id不能为0";
				break;
			case 190 : 
				result = "模板id已被使用";
				break;
			case 191 : 
				result = "视频监控设备id重复";
				break;
			case 192 : 
				result = "视频监控设备名称重复";
				break;
			case 193 : 
				result = "视频监控设备已与号码绑定";
				break;
			case 194 : 
				result = "号码不是视频监控号码或号码不存在";
				break;
			case 195 : 
				result = "号码已被其他视频监控设备使用";
				break;
			case 196 : 
				result = "监控号码索引重复";
				break;
			case 197 : 
				result = "通道号已与号码绑定";
				break;
			case 198 : 
				result = "视频监控设备不存在";
				break;
			case 0xC8 : 
				result = "批量开户请求参数错误";
				break;	
			case 0xC9 : 
				result = "批量开户部分失败";
				break;
			case 0xCA : 
				result = "批量开户全部失败";
				break;
			case 0xCB : 
				result = "当前正在执行批量开户操作, 请稍候重试!";
				break;
			case 0xCC : 
				result = "IP地址冲突";
				break;
			case 0xCD : 
				result = "TCN1000设备ID重复";
				break;
			case 0xCE : 
				result = "TCN1000设备ID不存在";
				break;
			case 0xCF : 
				result = "TCN1000仅支持连接单个录音服务器";
				break;
			case 0xD0:
				result = "紧急呼叫号码与内网号码重复";
				break;
			case 0xD1:
				result = "紧急呼叫号码与组号码重复";
				break;
			case 0xD2:
				result = "紧急呼叫号码与已添加的外网用户号码重复";
				break;
			case 0xD3:
				result = "紧急呼叫重定向号码所引用的内部号码不存在";
				break;
			case 0xD4:
				result = "紧急呼叫重定向号码为内部用户号码";
				break;
			case 0xD5:
				result = "紧急呼叫重定向号码为内部组号码";
				break;
			case 0xD6:
				result = "修改录音服务器信息,TCN1000将重启";
				break;
			case 0xD7 : 
				result = "呼叫类型为语音的呼叫QOS记录仅支持单条";
				break;
			case 0xD8 : 
				result = "呼叫类型为视频的呼叫QOS记录仅支持单条";
				break;
			case 0xD9 : 
				result = "未知呼叫类型";
				break;
			case 0xDA: 
				result = "子网ID重复";
				break;
			case 0xDB: 
				result = "用户IP资源段地址与ran同网段";
				break;
			case 0xDC: 
				result = "用户IP资源段地址与pdn同网段";
				break;
			case 0xDD: 
				result = "用户IP资源起始地址与结束地址相同";
				break;
			case 0xDE: 
				result = "号码已使用，不能修改号码类型";
				break;
			case 0xDF: 
				result = "PDT标识重复";
				break;
			case 0xE0: 
				result = "PDT ip、port重复";
				break;
			case 0xE1: 
				result = "恢复告警失败";
				break;
			case 0xE2: 
				result = "查询告警失败";
				break;
			case 0xE3: 
				result = "号码前缀已存在";
				break;
			case 0xE4: 
				result = "SIP通道不存在";
				break;
			case 0xE5: 
				result = "静态路由已存在";
				break;
			case 0xE6: 
				result = "sip通道id已被使用";
				break;
			case 0xE7: 
				result = "sip通道ip、port重复";
				break;
			case 0xE8: 
				result = "SDC系统地址与RAN地址必须引用同一系统地址";
				break;	
			case 0xE9: 
				result = "系统紧急呼叫记录已存在";
				break;
			case 0xEA:
				result = "不支持的设备类型";
				break;	
			case 0xFE : 
				result = "静态IP不能和RAN或PDN同网段 ";
				break;
			case 4096:
				result = "数据备份失败";
				break;
			case 4097:
				result = "数据恢复失败";
				break;
			default : 
				result = "操作失败(" + flag + ")";
				break;
		}
		
		return result;
	}
	
}
