/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-26	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.stat.core;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * 
 * 日期格式化助手
 * 
 * @author fanhaoyu
 * 
 */

public class DateFormater {
	public static final int YEAR = 1;

	public static final int MONTH = 2;

	public static final int WEEK = 3;

	public static final int DATE = 4;

	public static final int HOUR = 5;

	public static final int MINUTE = 6;

	public static final int SECOND = 7;

	public static final int MILLISECOND = 8;

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String TIME_FORMAT = "HH:mm:ss";

	public static final String ORACLE_DATE_TIME_FORMAT = "YYYY-MM-DD HH24:MI:SS";

	public static final SimpleDateFormat dfDateTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static final SimpleDateFormat dfDate = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static final SimpleDateFormat dfTime = new SimpleDateFormat(
			"HH:mm:ss");

	/**
	 * 按照指定时间格式，格式化指定时间
	 * 
	 * @param when
	 * @param format
	 * @return
	 */
	public static String format(Date when, String format) {
		try {
			SimpleDateFormat sdFormat = new SimpleDateFormat(format);
			return sdFormat.format(when);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 以"yyyy-MM-dd HH:mm:ss:SSSS"格式格式化日期对象
	 * 
	 * @param when
	 *            Date 要格式化的日期
	 * @return String 格式化后的结果
	 */
	public static String formatFull(Date when) {
		return format(when, "yyyy-MM-dd HH:mm:ss:SSSS");
	}

	/**
	 * 以"yyyy-MM-dd"格式格式化日期
	 * 
	 * @param when
	 *            Date 要格式化的日期
	 * @return String 格式化后的结果
	 */
	public static String formatDate(Date when) {
		return format(when, "yyyy-MM-dd");
	}

	/**
	 * 以"HH:mm:ss"格式格式化日期
	 * 
	 * @param when
	 *            Date 要格式化的日期
	 * @return String 格式化后的结果
	 */
	public static String formatTime(Date when) {
		return format(when, "HH:mm:ss");
	}

	/**
	 * 以"yyyy-MM-dd HH:mm:ss"格式格式化日期
	 * 
	 * @param when
	 *            Date 要格式化的日期
	 * @return String 格式化后的结果
	 */
	public static String formatDateTime(Date when) {
		return format(when, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 最小单位格式化的到分钟的字符串。
	 * 
	 * @param when
	 *            日期对象
	 * @return 格式化后的字符串
	 */
	public static String formatToMinute(Date when) {
		return format(when, "yyyy-MM-dd HH:mm");
	}

	/**
	 * 以指定的格式解析字符串为日期对象
	 * 
	 * @param string
	 *            String 要解析的字符串
	 * @param format
	 *            String 解析的格式字符串
	 * @return Date 解析出来的日期对象
	 */
	public static Date parse(String string, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(string);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 以"yyyy-MM-dd HH:mm:ss:SSSS"格式解析字符串为日期对象
	 * 
	 * @param string
	 *            String 要解析的字符串
	 * @return Date 解析出来的日期对象
	 */
	public static Date parseFull(String string) {
		return parse(string, "yyyy-MM-dd HH:mm:ss:SSSS");
	}

	/**
	 * 以"yyyy-MM-dd"格式解析字符串为日期对象
	 * 
	 * @param string
	 *            String 要解析的字符串
	 * @return Date 解析出来的日期对象
	 */
	public static Date parseDate(String string) {
		return parse(string, "yyyy-MM-dd");
	}

	/**
	 * 以"yyyy-MM-dd HH:mm:ss"格式解析字符串为日期对象
	 * 
	 * @param string
	 *            String 要解析的字符串
	 * @return Date 解析出来的日期对象
	 */
	public static Date parseDateTime(String string) {
		return parse(string, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 最小解析到分钟的。
	 * 
	 * @param string
	 *            要解析的字符串
	 * @return 解析后的Date对象，解析失败返回null
	 */
	public static Date parseToMinute(String string) {
		return parse(string, "yyyy-MM-dd HH:mm");
	}

	/**
	 * 比较两日期对象，返回Long对象，Long对象的值如下： < 0 -- before在after之前 ; = 0 --
	 * before和after相等； > 0 -- before在after之后。 返回值的绝对值是两日期相差的毫秒数
	 * 
	 * @param before
	 *            Date 在前的日期
	 * @param after
	 *            Date 在后的日期
	 * @return Long 返回结果的包装成Long，处理有异常时返回null
	 */
	public static Long compare(Date before, Date after) {
		try {
			long lTimeBetween = before.getTime() - after.getTime();
			return new Long(lTimeBetween);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将java.util.Date的日期对象转化为java.sql.Date的日期对象
	 * 
	 * @param when
	 *            Date java.util.Date的日期对象
	 * @return Date 转化后的java.sql.Date对象，有异常则返回null
	 */
	public static java.sql.Date changeToSqlDate(Date when) {
		try {
			long lMilliSecond = when.getTime();
			return new java.sql.Date(lMilliSecond);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将java.sql.Date的日期对象转化为java.util.Date的日期对象
	 * 
	 * @param when
	 *            Date java.sql.Date的日期对象
	 * @return Date 转化后的java.util.Date对象，有异常则返回null
	 */
	public static Date changeToUtilDate(java.sql.Date when) {
		try {
			long lMilliSecond = when.getTime();
			return new Date(lMilliSecond);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 取得系统当前时间,类型为Timestamp
	 * 
	 * @return 系统当前时间
	 */
	public static Timestamp getNowTimestamp() {
		java.util.Date d = new java.util.Date();
		Timestamp numTime = new Timestamp(d.getTime());
		return numTime;
	}

	/**
	 * 取得系统的当前时间,类型为java.sql.Date
	 * 
	 * @return java.sql.Date
	 */
	public static java.sql.Date getNowDate() {
		java.util.Date d = new java.util.Date();
		return new java.sql.Date(d.getTime());
	}

	/**
	 * 从Timestamp类型转化为yyyy-MM-dd类型的字符串
	 * 
	 * @param date
	 *            Timestamp类型数据
	 * @param strDefault
	 *            函数返回的缺省值（当date为空时）
	 * @return yyyy-MM-dd类型的字符串
	 */
	public static String TimestampToString(Timestamp date, String strDefault) {
		String strTemp = strDefault;
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			strTemp = formatter.format(date);
		}
		return strTemp;
	}

	/**
	 * 从Timestamp类型转化为yyyymmdd类型的字符串
	 * 
	 * @param date
	 *            Timestamp类型数据
	 * @param strDefault
	 *            函数返回的缺省值（当date为空时）
	 * @return yyyymmdd类型的字符串
	 */
	public static String TimestampToStringDate(Timestamp date, String strDefault) {
		String strTemp = strDefault;
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			strTemp = formatter.format(date);
		}
		return strTemp;
	}

	/**
	 * 从Timestamp类型转化为yyyy-MM-dd类型的字符串,如果为null,则返回null
	 * 
	 * @param date
	 *            Timestamp类型时间
	 * @return yyyy-MM-dd类型的字符串
	 */
	public static String TimestampToString(Timestamp date) {
		return TimestampToString(date, null);
	}

	/**
	 * 将date型转化为String（格式为yyyy-MM-dd型的字符串）
	 * 
	 * @param date
	 *            java.sql.Date类型数据
	 * @param strDefault
	 *            函数返回的缺省值（当date为null时）
	 * @return 格式为yyyy-MM-dd型的字符串
	 */
	public static String DateToString(java.sql.Date date, String strDefault) {
		String strTemp = strDefault;
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			strTemp = formatter.format(date);
		}
		return strTemp;
	}

	/**
	 * 将java.sql.Date型数据转化为String（格式为yyyy-MM-dd型的字符串） 当date为null时，返回值为null
	 * 
	 * @param date
	 *            java.sql.Date类型数据
	 * @return 格式为yyyy-MM-dd型的字符串
	 */
	public static String DateToString(java.sql.Date date) {
		return DateToString(date, null);
	}

	/**
	 * 以指定的格式返回日期的字符串，比如"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param date
	 *            日期
	 * @param formatter
	 *            格式字符串
	 * @return
	 */
	public static String DateTimeToString(java.sql.Date date, String formater) {
		try {
			SimpleDateFormat fm = new SimpleDateFormat(formater);
			return fm.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 以"yyyy-MM-dd HH:mm:ss"格式输出日期的字符串
	 * 
	 * @param date
	 *            要格式化的日期
	 * @return 格式化的字符串
	 */
	public static String DateTimeToString(java.sql.Date date) {
		return DateTimeToString(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 以指定的格式返回日期的字符串，比如"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param date
	 *            日期
	 * @param formatter
	 *            格式字符串
	 * @return
	 */
	public static String DateTimeToString(java.util.Date date, String formater) {
		try {
			SimpleDateFormat fm = new SimpleDateFormat(formater);
			return fm.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 以"yyyy-MM-dd HH:mm:ss"格式输出日期的字符串
	 * 
	 * @param date
	 *            要格式化的日期
	 * @return 格式化的字符串
	 */
	public static String DateTimeToString(java.util.Date date) {
		return DateTimeToString(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 比较时间大小
	 * 
	 * @param String
	 *            beforeDate,比较小的时间
	 * @param String
	 *            afterDate,比较大的时间
	 * @return boolean True if DateTime1<=DateTime2 ,else False
	 */
	public static boolean compareDateTimes(String DateTime1, String DateTime2) {
		boolean check = false;
		Date d1 = new Date();
		Date d2 = new Date();
		// DateFormat df = DateFormat.getDateTimeInstance(2,2,Locale.CHINESE);
		try {
			d1 = dfDateTime.parse(DateTime1);
			d2 = dfDateTime.parse(DateTime2);
		} catch (java.text.ParseException pe) {

		}
		// Add Time:2003-10-30
		if (d1.equals(d2))
			return true;
		check = d1.before(d2);
		return check;
	}

	/**
	 * 比较时间大小
	 * 
	 * @param String
	 *            Time1,比较小的时间
	 * @param String
	 *            Time2,比较大的时间
	 * @return boolean True if Time1<=Time2 ,else False
	 */
	public static boolean compareTimes(String Time1, String Time2) {
		boolean check = false;
		Date d1 = new Date();
		Date d2 = new Date();
		// DateFormat df = DateFormat.getTimeInstance(2,Locale.CHINESE);
		try {
			d1 = dfTime.parse(Time1);
			d2 = dfTime.parse(Time2);
		} catch (java.text.ParseException pe) {

		}
		// Add Time:2003-10-30
		if (d1.equals(d2))
			return true;
		check = d1.before(d2);
		return check;
	}

	/**
	 * 比较时间大小
	 * 
	 * @param String
	 *            beforeDate,比较小的时间
	 * @param String
	 *            afterDate,比较大的时间
	 * @return boolean True if Date1<=Date2 ,else False
	 */
	public static boolean compareDates(String Date1, String Date2) {
		boolean check = false;
		Date d1 = new Date();
		Date d2 = new Date();

		// DateFormat df = DateFormat.getDateInstance(2,Locale.getDefault());
		try {
			// d1=df.parse(Date1);
			// d2=df.parse(Date2);
			d1 = dfDate.parse(Date1);
			d2 = dfDate.parse(Date2);
		} catch (java.text.ParseException pe) {

		}
		// Add Time:2003-10-30
		if (d1.equals(d2))
			return true;
		check = d1.before(d2);
		return check;
	}

	/**
	 * 将String转成Date
	 */
	public static Date getDate(String strDate) {
		Date d = new Date();
		// DateFormat df =
		// DateFormat.getDateTimeInstance(2,2,Locale.getDefault());
		// DateFormat.getDateTimeInstanc1e(
		try {
			d = dfDateTime.parse(strDate);

		} catch (java.text.ParseException pe) {

			// check=false;
			// return check;
		}
		return d;
	}

	/**
	 * 将String转成Date
	 * 
	 * @param strDate
	 * @return
	 */
	/*
	 * private static Date parseDate(String strDate){ try{ return
	 * sdf.parse(strDate) ; } catch(Exception e) { return null ; } }
	 */

	/**
	 * 得到时间的间隔（分钟）
	 * 
	 * @param String
	 *            beforeDate,比较小的时间
	 * @param String
	 *            afterDate,比较大的时间
	 * @return int
	 */
	public static long getMinutesBetweenDates(String beforeDate,
			String afterDate) {
		long lbe = 0;
		Date d1 = new Date();
		Date d2 = new Date();
		// DateFormat df =
		// DateFormat.getDateTimeInstance(2,2,Locale.getDefault());
		try {
			d1 = dfDateTime.parse(beforeDate);
			d2 = dfDateTime.parse(afterDate);
		} catch (java.text.ParseException pe) {

			// check=false;
			// return check;
		}
		lbe = d2.getTime() - d1.getTime();
		lbe = lbe / 60000;

		return lbe;
	}

	/**
	 * 得到当前日期时间datetime
	 * 
	 * @param
	 * @return String
	 */
	public static String getCurrentDateTime() {
		String sdate = "";
		Date date = new Date();
		// DateFormat df =
		// DateFormat.getDateTimeInstance(2,2,Locale.getDefault());
		sdate = dfDateTime.format(date);
		return sdate;
	}

	/**
	 * 得到当前日期时间date
	 * 
	 * @param
	 * @return String
	 */
	public static String getCurrentDate() {
		Date curDate = new Date();
		return DateTimeToString(curDate, "yyyy-MM-dd");
	}

	/**
	 * 返回yyyyMM格式的数据
	 * 
	 * @param datetime
	 *            yyyy-MM-dd HH:mm:ss
	 * @return yyyyMM
	 */
	public static String getYearMonthFromDatetime(String datetime) {
		String ret = "";
		String year = "";
		String month = "";
		StringTokenizer st = new StringTokenizer(datetime, "-");
		if (st.hasMoreTokens()) {
			year = st.nextToken();
		}
		if (st.hasMoreTokens()) {
			month = st.nextToken();
		}
		if (month.length() == 1 && Integer.parseInt(month) < 10) {
			month = "0" + month;
		}

		ret = year + month;
		return ret;
	}

	/**
	 * 得到某年某月的天数
	 * 
	 * @param int year 年； int month 月
	 * @return int
	 */
	public static int getMonthDays(int year, int month) {
		int dayNum = 0;
		int DaysInMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		dayNum = DaysInMonth[month - 1];
		if (month == 2 && isRunNian(year)) {
			dayNum = 29;
		}
		return dayNum;
	}

	/**
	 * 检测是否为闰年
	 * 
	 * @param int year 要检测的年
	 * @return boolean: true表示是闰年； false表示不是闰年
	 */
	public static boolean isRunNian(int year) {
		boolean check = false;
		if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0))
			check = true;
		return check;
	}

	/**
	 * 得到当前时间的年
	 * 
	 * @param
	 * @return int
	 */
	public static int getCurrentYear() {
		int year = 1;
		Calendar cal = Calendar.getInstance();
		Date d = new Date();
		cal.setTime(d);
		year = cal.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 得到当前时间的月
	 * 
	 * @param
	 * @return int
	 */
	public static int getCurrentMonth() {
		int Month = 1;
		Calendar cal = Calendar.getInstance();
		Date d = new Date();
		cal.setTime(d);
		Month = cal.get(Calendar.MONTH) + 1;
		return Month;
	}

	/**
	 * 得到当前时间的日
	 * 
	 * @param
	 * @return int
	 */
	public static int getCurrentDay() {
		int Day = 1;
		Calendar cal = Calendar.getInstance();
		Date d = new Date();
		cal.setTime(d);
		Day = cal.get(Calendar.DATE);
		return Day;
	}

	/**
	 * 得到当前时间的时
	 * 
	 * @param
	 * @return int
	 */
	@SuppressWarnings("static-access")
	public static int getCurrentHour() {
		int Hour = 1;
		Calendar cal = Calendar.getInstance(Locale.CHINESE);
		Date d = new Date();
		cal.setTime(d);
		Hour = cal.get(Calendar.HOUR);
		if (cal.PM == 1)
			Hour += 12;
		return Hour;
	}

	/**
	 * 得到当前时间的分
	 * 
	 * @param
	 * @return int
	 */
	public static int getCurrentMinute() {
		int Minute = 1;
		Calendar cal = Calendar.getInstance();
		Date d = new Date();
		cal.setTime(d);
		Minute = cal.get(Calendar.MINUTE);
		return Minute;
	}

	/**
	 * 得到当前时间的秒
	 * 
	 * @param
	 * @return int
	 */
	public static int getCurrentSecond() {
		int Second = 1;
		Calendar cal = Calendar.getInstance();
		Date d = new Date();
		cal.setTime(d);
		Second = cal.get(Calendar.SECOND);
		return Second;
	}

	/**
	 * 得到当前日期的星期数
	 * 
	 * @param
	 * @return int
	 */
	public static int getCurrentWeekNum() {
		int week = 1;
		Calendar cal = Calendar.getInstance();
		Date d = new Date();
		cal.setTime(d);
		week = cal.get(Calendar.DAY_OF_WEEK);
		week = week - 1;
		if (week == 0)
			week = 7;
		return week;
	}

	/**
	 * 得到给定时间的年
	 * 
	 * @param String
	 * @return int
	 */
	public static int getYear(String d) {
		int year = 1;
		Calendar cal = Calendar.getInstance();
		Date d1 = new Date();
		// DateFormat df =
		// DateFormat.getDateTimeInstance(2,2,Locale.getDefault());
		try {
			d1 = dfDateTime.parse(d);
		} catch (java.text.ParseException pe) {
		}
		cal.setTime(d1);
		year = cal.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 得到给定时间的月
	 * 
	 * @param String
	 * @return int
	 */
	public static int getMonth(String d) {
		int Month = 1;
		Calendar cal = Calendar.getInstance();
		Date d1 = new Date();
		// DateFormat df =
		// DateFormat.getDateTimeInstance(2,2,Locale.getDefault());
		try {
			d1 = dfDateTime.parse(d);
		} catch (java.text.ParseException pe) {
		}
		cal.setTime(d1);
		Month = cal.get(Calendar.MONTH) + 1;
		return Month;
	}

	/**
	 * 得到给定时间的日
	 * 
	 * @param String
	 * @return int
	 */
	public static int getDay(String d) {
		int Day = 1;
		Calendar cal = Calendar.getInstance();
		Date d1 = new Date();
		// DateFormat df =
		// DateFormat.getDateTimeInstance(2,2,Locale.getDefault());
		try {
			d1 = dfDateTime.parse(d);

		} catch (java.text.ParseException pe) {

		}
		cal.setTime(d1);
		Day = cal.get(Calendar.DATE);
		return Day;
	}

	/**
	 * 得到给定时间的时
	 * 
	 * @param String
	 * @return int
	 */
	public static int getHour(String d) {
		int Hour = 0;
		String tmp = "";
		StringTokenizer st = new StringTokenizer(d, " ");
		while (st.hasMoreTokens()) {
			tmp = st.nextToken();
		}
		try {
			Hour = Integer.parseInt(tmp.substring(0, tmp.indexOf(":")));
		} catch (NumberFormatException ex) {
		}
		/*
		 * Calendar cal=Calendar.getInstance(); Date d1=new Date(); DateFormat
		 * df = DateFormat.getDateTimeInstance(2,2,Locale.getDefault()); try{
		 * d1=df.parse(d);
		 * 
		 * }catch(java.text.ParseException pe){ } cal.setTime(d1);
		 * Hour=cal.get(cal.HOUR);
		 * 
		 * if(cal.PM==1) Hour+=12;
		 */
		return Hour;
	}

	/**
	 * 得到给定时间的分
	 * 
	 * @param String
	 * @return int
	 */
	public static int getMinute(String d) {
		int Minute = 1;
		Calendar cal = Calendar.getInstance();
		Date d1 = new Date();
		// DateFormat df =
		// DateFormat.getDateTimeInstance(2,2,Locale.getDefault());
		try {
			d1 = dfDateTime.parse(d);

		} catch (java.text.ParseException pe) {

		}
		cal.setTime(d1);
		Minute = cal.get(Calendar.MINUTE);
		return Minute;
	}

	/**
	 * 得到给定时间的秒
	 * 
	 * @param String
	 * @return int
	 */
	public static int getSecond(String d) {
		int Second = 1;
		Calendar cal = Calendar.getInstance();
		Date d1 = new Date();
		// DateFormat df =
		// DateFormat.getDateTimeInstance(2,2,Locale.getDefault());
		try {
			d1 = dfDateTime.parse(d);

		} catch (java.text.ParseException pe) {

		}
		cal.setTime(d1);
		Second = cal.get(Calendar.SECOND);
		return Second;
	}

	/**
	 * 得到给定日期的星期数
	 * 
	 * @param String
	 * @return int
	 */
	public static int getWeekNum(String d) {
		int week = 1;
		Calendar cal = Calendar.getInstance();
		Date d1 = new Date();
		// DateFormat df =
		// DateFormat.getDateTimeInstance(2,2,Locale.getDefault());
		try {
			d1 = dfDateTime.parse(d);

		} catch (java.text.ParseException pe) {

		}
		cal.setTime(d1);
		week = cal.get(Calendar.DAY_OF_WEEK);
		week = week - 1;
		if (week == 0)
			week = 7;
		return week;
	}

	/**
	 * 字符串时间转换为long型时间，单位：milliseconds
	 * 
	 * @param strDatetime
	 * @return 至1970-1-1以来的long型时间，单位：milliseconds
	 */
	public static long StringDatetimeToLong(String strDatetime) {
		long time = -1;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		if (strDatetime != null) {
			try {
				time = sf.parse(strDatetime).getTime();
			} catch (Exception e) {
			}
		}
		return time;
	}

	/**
	 * 计算时间间隔
	 * 
	 * @param begDatetime
	 *            起始时间 yyyy-MM-dd HH:mm:ss
	 * @param endDatetime
	 *            结束时间 yyyy-MM-dd HH:mm:ss
	 * @param timeUnit
	 *            时间单位（s－秒、m－分钟、h－小时、d－天）
	 * @return 时间间隔
	 */
	public static String DateDiff(String begDatetime, String endDatetime,
			String timeUnit) {
		long startTime = StringDatetimeToLong(begDatetime);
		long endTime = StringDatetimeToLong(endDatetime);
		if (startTime == -1 || endTime == -1)
			return "0";
		long timeInterval = endTime - startTime;
		long timeSpace = 0;
		if (timeUnit.equals("s"))
			timeSpace = timeInterval / 1000;
		else if (timeUnit.equals("m"))
			timeSpace = timeInterval / 1000 / 60;
		else if (timeUnit.equals("h"))
			timeSpace = timeInterval / 1000 / 60 / 60;
		else if (timeUnit.equals("d"))
			timeSpace = timeInterval / 1000 / 60 / 60 / 24;
		return String.valueOf(timeSpace);
	}

	/**
	 * 按照时间间隔和时间单位计算一段时间范围内的时间数目
	 * 
	 * @param begDatetime
	 *            起始时间 yyyy-MM-dd HH:mm:ss
	 * @param endDatetime
	 *            结束时间 yyyy-MM-dd HH:mm:ss
	 * @param timeUnit
	 *            时间单位（s－秒、m－分钟、h－小时、d－天）
	 * @return 时间数目
	 */
	public static String DateDiff(String begDatetime, String endDatetime,
			String timeSpace, String timeUnit) {
		long startTime = StringDatetimeToLong(begDatetime);
		long endTime = StringDatetimeToLong(endDatetime);
		long longTimeSpace = Long.parseLong(timeSpace);
		if (startTime == -1 || endTime == -1)
			return "0";
		long timeInterval = endTime - startTime;

		long count = 0;
		if (timeUnit.equals("s"))
			count = timeInterval / 1000;
		else if (timeUnit.equals("m"))
			count = timeInterval / 1000 / 60;
		else if (timeUnit.equals("h"))
			count = timeInterval / 1000 / 60 / 60;
		else if (timeUnit.equals("d"))
			count = timeInterval / 1000 / 60 / 60 / 24;
		count = count / longTimeSpace;
		return String.valueOf(count);
	}

	/**
	 * 增加时间
	 * 
	 * @param begDatetime
	 *            起始时间 yyyy-MM-dd HH:mm:ss
	 * @param timeSpace
	 *            时间间隔 yyyy-MM-dd HH:mm:ss
	 * @param timeUnit
	 *            时间单位（m－分钟、h－小时、d－天、M－月、y－年）
	 * @return 增加后的时间
	 */
	public static String AddDateTime(String begDatetime, String timeSpace,
			String timeUnit) {
		float timeInterval = 0;
		try {
			timeInterval = Float.parseFloat(timeSpace); // 转换为long型的时间间隔，单位：milliseconds
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (timeUnit.equals("m")) // 分钟
			timeInterval = timeInterval * 60 * 1000;
		if (timeUnit.equals("h")) // 小时
			timeInterval = timeInterval * 60 * 60 * 1000;
		if (timeUnit.equals("d")) // 天
			timeInterval = timeInterval * 24 * 60 * 60 * 1000;

		long startTime = StringDatetimeToLong(begDatetime);

		long endTime = startTime + (long) timeInterval;

		Date time = new Date();
		time.setTime(endTime);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(time);
	}

	/**
	 * 增加时刻
	 * 
	 * @param begDatetime
	 *            起始时间 格式为HH:mm:ss
	 * @param timeSpace
	 *            时间间隔
	 * @param timeUnit
	 *            时间单位（m－分钟、h－小时）
	 * @return 增加后的时刻
	 */
	public static String AddTime(String begTime, String timeSpace,
			String timeUnit) {
		long timeInterval = Long.parseLong(timeSpace); // 转换为long型的时间间隔，单位：milliseconds

		if (timeUnit.equals("m")) // 分钟
			timeInterval = timeInterval * 60 * 1000;
		if (timeUnit.equals("h")) // 小时
			timeInterval = timeInterval * 60 * 60 * 1000;

		String begDatetime = "1980-01-01 " + begTime;

		long startTime = StringDatetimeToLong(begDatetime);
		long endTime = startTime + timeInterval;

		Date time = new Date();
		time.setTime(endTime);

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		return formatter.format(time);
	}

	/**
	 * 以指定的格式将字符串格式化为Date对象，从字符串头开始，不一定使用整个字符串
	 * 
	 * @param src
	 *            原始字符串
	 * @param format
	 *            格式字符串
	 * @return 格式化后的Date对象，如果出现异常则返回null
	 */
	public static java.util.Date parseDate(String src, String format) {
		java.text.SimpleDateFormat sdFormat = new java.text.SimpleDateFormat(
				format);
		try {
			return sdFormat.parse(src);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取当前日期所在的week或者month的第一天
	 * 
	 * @param when
	 *            当前时间
	 * @param unit
	 *            单位，周或者month
	 * @return
	 */
	public static java.util.Date getFirstDay(java.util.Date when, int unit) {
		if (when == null)
			return null;

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(when);
		if (unit == WEEK) {
			while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
				calendar.add(Calendar.DATE, -1);
			}
		} else if (unit == MONTH) {
			while (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
				calendar.add(Calendar.DATE, -1);
			}
		} else {
			return null;
		}

		return calendar.getTime();
	}

	/**
	 * 获取当前日期所在的week或者month的最后一天
	 * 
	 * @param when
	 *            当前时间
	 * @param unit
	 *            单位，周或者month
	 * @return
	 */
	public static java.util.Date getLastDay(java.util.Date when, int unit) {
		if (when == null)
			return null;

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(when);
		if (unit == WEEK) {
			while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				calendar.add(Calendar.DATE, 1);
			}
		} else if (unit == MONTH) {
			int iPreMonth = calendar.get(Calendar.MONTH);
			calendar.add(Calendar.DATE, 1);
			int iNextMonth = calendar.get(Calendar.MONTH);
			while (iPreMonth == iNextMonth) {
				iPreMonth = iNextMonth;
				calendar.add(Calendar.DATE, 1);
				iNextMonth = calendar.get(Calendar.MONTH);
			}
			calendar.add(Calendar.DATE, -1);
		} else {
			return null;
		}

		return calendar.getTime();
	}

	/**
	 * 取得一周前的时间
	 * 
	 * @param current
	 * @return
	 */
	public static long getLastWeekDay(long current) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(current);
		calendar.add(Calendar.DAY_OF_WEEK, -7);
		return calendar.getTimeInMillis();
	}

	/**
	 * 取得一月前的时间
	 * 
	 * @param current
	 * @return
	 */
	public static long getLastMonthDay(long current) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(current);
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTimeInMillis();
	}

	/**
	 * 取得一年前的时间
	 * 
	 * @param current
	 * @return
	 */
	public static long getLastYear(long current) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(current);
		calendar.add(Calendar.YEAR, -1);
		return calendar.getTimeInMillis();
	}

}
