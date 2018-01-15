/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-16	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 日期助手
 * 
 * @author chenjunhua
 * 
 */

public class DateUtils {

	/**
	 * 从毫秒格式转换到14位的yyyyMMddHHmmss数字格式
	 * 
	 * @param millisecondTime
	 * @return
	 */
	public static long getBriefTimeFromMillisecondTime(long millisecondTime) {
		try {
			Date date = new Date(millisecondTime);
			String briefTime = getStringByYYYYMMDDHHMMSS(date);
			return Long.parseLong(briefTime);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 从14位yyyyMMddHHmmss数字格式时间格式转成毫秒
	 * 
	 * @param briefTime
	 * @return
	 */
	public static long getMillisecondTimeFromBriefTime(long briefTime) {
		Date date = getDateByYYYYMMDDHHMMSS(String.valueOf(briefTime));
		return date.getTime();
	}
	
	/**
	 * 将"2012-04-02 21:15:46"格式的字符串转换为"20120402211546"格式 Author : chenlong
	 * 
	 * @param standardTime "2012-04-02 21:15:46"格式的时间字符串
	 * @return "20120402211546"时间格式字符串
	 */
	public static long getBriefTimeFromStandardTime(String standardTime) {
		if (standardTime == null || standardTime.length() == 0)
			return 0;
		StringBuilder sb = new StringBuilder();
		char[] cs = standardTime.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] >= '0' && cs[i] <= '9') {
				sb.append(cs[i]);
			}
		}
		return Long.parseLong(sb.toString());
	}

	public static long getStandardTimeFromDate(Date date){
	 String brieTime = 	getStringByYYYYMMDDHHMMSS(date);
	 long standardTime= getBriefTimeFromStandardTime(brieTime);
	 	return standardTime;
	}
	
	/**
	 * 将"20120402211546"格式时间转换为标准时间2012-04-02 21:15:46
	 * @param briefTime
	 * @return
	 */
	public static String getStandardTimeFromBriefTime(long briefTime) {
		StringBuffer strBuffer = new StringBuffer();
		String str = Long.toString(briefTime);
		strBuffer.append(str.substring(0, 4));
		strBuffer.append("-");
		strBuffer.append(str.substring(4, 6));
		strBuffer.append("-");
		strBuffer.append(str.substring(6, 8));
		strBuffer.append(" ");
		strBuffer.append(str.substring(8, 10));
		strBuffer.append(":");
		strBuffer.append(str.substring(10, 12));
		strBuffer.append(":");
		strBuffer.append(str.substring(12, str.length()));
		return strBuffer.toString();
	}
	/**
	 * 按照YYYYMMDDHHMMSS格式解析字符串获取时间 Author : yanghongliang Create Date : 2012-7-5
	 * 
	 * @param str
	 * @return
	 */
	public static Date getDateByYYYYMMDDHHMMSS(String str) {
		if (str == null || str.length() == 0)
			return null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		try {
			return simpleDateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 按照YYYYMMDDHHMMSS格式解析时间获取字符串 Author : yanghongliang Create Date : 2012-7-5
	 * 
	 * @param str
	 * @return
	 */
	public static String getStringByYYYYMMDDHHMMSS(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		return simpleDateFormat.format(date);
	}
}
