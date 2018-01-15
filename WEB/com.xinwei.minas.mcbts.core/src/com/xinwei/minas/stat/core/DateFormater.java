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
 * ���ڸ�ʽ������
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
	 * ����ָ��ʱ���ʽ����ʽ��ָ��ʱ��
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
	 * ��"yyyy-MM-dd HH:mm:ss:SSSS"��ʽ��ʽ�����ڶ���
	 * 
	 * @param when
	 *            Date Ҫ��ʽ��������
	 * @return String ��ʽ����Ľ��
	 */
	public static String formatFull(Date when) {
		return format(when, "yyyy-MM-dd HH:mm:ss:SSSS");
	}

	/**
	 * ��"yyyy-MM-dd"��ʽ��ʽ������
	 * 
	 * @param when
	 *            Date Ҫ��ʽ��������
	 * @return String ��ʽ����Ľ��
	 */
	public static String formatDate(Date when) {
		return format(when, "yyyy-MM-dd");
	}

	/**
	 * ��"HH:mm:ss"��ʽ��ʽ������
	 * 
	 * @param when
	 *            Date Ҫ��ʽ��������
	 * @return String ��ʽ����Ľ��
	 */
	public static String formatTime(Date when) {
		return format(when, "HH:mm:ss");
	}

	/**
	 * ��"yyyy-MM-dd HH:mm:ss"��ʽ��ʽ������
	 * 
	 * @param when
	 *            Date Ҫ��ʽ��������
	 * @return String ��ʽ����Ľ��
	 */
	public static String formatDateTime(Date when) {
		return format(when, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * ��С��λ��ʽ���ĵ����ӵ��ַ�����
	 * 
	 * @param when
	 *            ���ڶ���
	 * @return ��ʽ������ַ���
	 */
	public static String formatToMinute(Date when) {
		return format(when, "yyyy-MM-dd HH:mm");
	}

	/**
	 * ��ָ���ĸ�ʽ�����ַ���Ϊ���ڶ���
	 * 
	 * @param string
	 *            String Ҫ�������ַ���
	 * @param format
	 *            String �����ĸ�ʽ�ַ���
	 * @return Date �������������ڶ���
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
	 * ��"yyyy-MM-dd HH:mm:ss:SSSS"��ʽ�����ַ���Ϊ���ڶ���
	 * 
	 * @param string
	 *            String Ҫ�������ַ���
	 * @return Date �������������ڶ���
	 */
	public static Date parseFull(String string) {
		return parse(string, "yyyy-MM-dd HH:mm:ss:SSSS");
	}

	/**
	 * ��"yyyy-MM-dd"��ʽ�����ַ���Ϊ���ڶ���
	 * 
	 * @param string
	 *            String Ҫ�������ַ���
	 * @return Date �������������ڶ���
	 */
	public static Date parseDate(String string) {
		return parse(string, "yyyy-MM-dd");
	}

	/**
	 * ��"yyyy-MM-dd HH:mm:ss"��ʽ�����ַ���Ϊ���ڶ���
	 * 
	 * @param string
	 *            String Ҫ�������ַ���
	 * @return Date �������������ڶ���
	 */
	public static Date parseDateTime(String string) {
		return parse(string, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * ��С���������ӵġ�
	 * 
	 * @param string
	 *            Ҫ�������ַ���
	 * @return �������Date���󣬽���ʧ�ܷ���null
	 */
	public static Date parseToMinute(String string) {
		return parse(string, "yyyy-MM-dd HH:mm");
	}

	/**
	 * �Ƚ������ڶ��󣬷���Long����Long�����ֵ���£� < 0 -- before��after֮ǰ ; = 0 --
	 * before��after��ȣ� > 0 -- before��after֮�� ����ֵ�ľ���ֵ�����������ĺ�����
	 * 
	 * @param before
	 *            Date ��ǰ������
	 * @param after
	 *            Date �ں������
	 * @return Long ���ؽ���İ�װ��Long���������쳣ʱ����null
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
	 * ��java.util.Date�����ڶ���ת��Ϊjava.sql.Date�����ڶ���
	 * 
	 * @param when
	 *            Date java.util.Date�����ڶ���
	 * @return Date ת�����java.sql.Date�������쳣�򷵻�null
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
	 * ��java.sql.Date�����ڶ���ת��Ϊjava.util.Date�����ڶ���
	 * 
	 * @param when
	 *            Date java.sql.Date�����ڶ���
	 * @return Date ת�����java.util.Date�������쳣�򷵻�null
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
	 * ȡ��ϵͳ��ǰʱ��,����ΪTimestamp
	 * 
	 * @return ϵͳ��ǰʱ��
	 */
	public static Timestamp getNowTimestamp() {
		java.util.Date d = new java.util.Date();
		Timestamp numTime = new Timestamp(d.getTime());
		return numTime;
	}

	/**
	 * ȡ��ϵͳ�ĵ�ǰʱ��,����Ϊjava.sql.Date
	 * 
	 * @return java.sql.Date
	 */
	public static java.sql.Date getNowDate() {
		java.util.Date d = new java.util.Date();
		return new java.sql.Date(d.getTime());
	}

	/**
	 * ��Timestamp����ת��Ϊyyyy-MM-dd���͵��ַ���
	 * 
	 * @param date
	 *            Timestamp��������
	 * @param strDefault
	 *            �������ص�ȱʡֵ����dateΪ��ʱ��
	 * @return yyyy-MM-dd���͵��ַ���
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
	 * ��Timestamp����ת��Ϊyyyymmdd���͵��ַ���
	 * 
	 * @param date
	 *            Timestamp��������
	 * @param strDefault
	 *            �������ص�ȱʡֵ����dateΪ��ʱ��
	 * @return yyyymmdd���͵��ַ���
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
	 * ��Timestamp����ת��Ϊyyyy-MM-dd���͵��ַ���,���Ϊnull,�򷵻�null
	 * 
	 * @param date
	 *            Timestamp����ʱ��
	 * @return yyyy-MM-dd���͵��ַ���
	 */
	public static String TimestampToString(Timestamp date) {
		return TimestampToString(date, null);
	}

	/**
	 * ��date��ת��ΪString����ʽΪyyyy-MM-dd�͵��ַ�����
	 * 
	 * @param date
	 *            java.sql.Date��������
	 * @param strDefault
	 *            �������ص�ȱʡֵ����dateΪnullʱ��
	 * @return ��ʽΪyyyy-MM-dd�͵��ַ���
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
	 * ��java.sql.Date������ת��ΪString����ʽΪyyyy-MM-dd�͵��ַ����� ��dateΪnullʱ������ֵΪnull
	 * 
	 * @param date
	 *            java.sql.Date��������
	 * @return ��ʽΪyyyy-MM-dd�͵��ַ���
	 */
	public static String DateToString(java.sql.Date date) {
		return DateToString(date, null);
	}

	/**
	 * ��ָ���ĸ�ʽ�������ڵ��ַ���������"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param date
	 *            ����
	 * @param formatter
	 *            ��ʽ�ַ���
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
	 * ��"yyyy-MM-dd HH:mm:ss"��ʽ������ڵ��ַ���
	 * 
	 * @param date
	 *            Ҫ��ʽ��������
	 * @return ��ʽ�����ַ���
	 */
	public static String DateTimeToString(java.sql.Date date) {
		return DateTimeToString(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * ��ָ���ĸ�ʽ�������ڵ��ַ���������"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param date
	 *            ����
	 * @param formatter
	 *            ��ʽ�ַ���
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
	 * ��"yyyy-MM-dd HH:mm:ss"��ʽ������ڵ��ַ���
	 * 
	 * @param date
	 *            Ҫ��ʽ��������
	 * @return ��ʽ�����ַ���
	 */
	public static String DateTimeToString(java.util.Date date) {
		return DateTimeToString(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * �Ƚ�ʱ���С
	 * 
	 * @param String
	 *            beforeDate,�Ƚ�С��ʱ��
	 * @param String
	 *            afterDate,�Ƚϴ��ʱ��
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
	 * �Ƚ�ʱ���С
	 * 
	 * @param String
	 *            Time1,�Ƚ�С��ʱ��
	 * @param String
	 *            Time2,�Ƚϴ��ʱ��
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
	 * �Ƚ�ʱ���С
	 * 
	 * @param String
	 *            beforeDate,�Ƚ�С��ʱ��
	 * @param String
	 *            afterDate,�Ƚϴ��ʱ��
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
	 * ��Stringת��Date
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
	 * ��Stringת��Date
	 * 
	 * @param strDate
	 * @return
	 */
	/*
	 * private static Date parseDate(String strDate){ try{ return
	 * sdf.parse(strDate) ; } catch(Exception e) { return null ; } }
	 */

	/**
	 * �õ�ʱ��ļ�������ӣ�
	 * 
	 * @param String
	 *            beforeDate,�Ƚ�С��ʱ��
	 * @param String
	 *            afterDate,�Ƚϴ��ʱ��
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
	 * �õ���ǰ����ʱ��datetime
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
	 * �õ���ǰ����ʱ��date
	 * 
	 * @param
	 * @return String
	 */
	public static String getCurrentDate() {
		Date curDate = new Date();
		return DateTimeToString(curDate, "yyyy-MM-dd");
	}

	/**
	 * ����yyyyMM��ʽ������
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
	 * �õ�ĳ��ĳ�µ�����
	 * 
	 * @param int year �ꣻ int month ��
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
	 * ����Ƿ�Ϊ����
	 * 
	 * @param int year Ҫ������
	 * @return boolean: true��ʾ�����ꣻ false��ʾ��������
	 */
	public static boolean isRunNian(int year) {
		boolean check = false;
		if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0))
			check = true;
		return check;
	}

	/**
	 * �õ���ǰʱ�����
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
	 * �õ���ǰʱ�����
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
	 * �õ���ǰʱ�����
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
	 * �õ���ǰʱ���ʱ
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
	 * �õ���ǰʱ��ķ�
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
	 * �õ���ǰʱ�����
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
	 * �õ���ǰ���ڵ�������
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
	 * �õ�����ʱ�����
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
	 * �õ�����ʱ�����
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
	 * �õ�����ʱ�����
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
	 * �õ�����ʱ���ʱ
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
	 * �õ�����ʱ��ķ�
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
	 * �õ�����ʱ�����
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
	 * �õ��������ڵ�������
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
	 * �ַ���ʱ��ת��Ϊlong��ʱ�䣬��λ��milliseconds
	 * 
	 * @param strDatetime
	 * @return ��1970-1-1������long��ʱ�䣬��λ��milliseconds
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
	 * ����ʱ����
	 * 
	 * @param begDatetime
	 *            ��ʼʱ�� yyyy-MM-dd HH:mm:ss
	 * @param endDatetime
	 *            ����ʱ�� yyyy-MM-dd HH:mm:ss
	 * @param timeUnit
	 *            ʱ�䵥λ��s���롢m�����ӡ�h��Сʱ��d���죩
	 * @return ʱ����
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
	 * ����ʱ������ʱ�䵥λ����һ��ʱ�䷶Χ�ڵ�ʱ����Ŀ
	 * 
	 * @param begDatetime
	 *            ��ʼʱ�� yyyy-MM-dd HH:mm:ss
	 * @param endDatetime
	 *            ����ʱ�� yyyy-MM-dd HH:mm:ss
	 * @param timeUnit
	 *            ʱ�䵥λ��s���롢m�����ӡ�h��Сʱ��d���죩
	 * @return ʱ����Ŀ
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
	 * ����ʱ��
	 * 
	 * @param begDatetime
	 *            ��ʼʱ�� yyyy-MM-dd HH:mm:ss
	 * @param timeSpace
	 *            ʱ���� yyyy-MM-dd HH:mm:ss
	 * @param timeUnit
	 *            ʱ�䵥λ��m�����ӡ�h��Сʱ��d���졢M���¡�y���꣩
	 * @return ���Ӻ��ʱ��
	 */
	public static String AddDateTime(String begDatetime, String timeSpace,
			String timeUnit) {
		float timeInterval = 0;
		try {
			timeInterval = Float.parseFloat(timeSpace); // ת��Ϊlong�͵�ʱ��������λ��milliseconds
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (timeUnit.equals("m")) // ����
			timeInterval = timeInterval * 60 * 1000;
		if (timeUnit.equals("h")) // Сʱ
			timeInterval = timeInterval * 60 * 60 * 1000;
		if (timeUnit.equals("d")) // ��
			timeInterval = timeInterval * 24 * 60 * 60 * 1000;

		long startTime = StringDatetimeToLong(begDatetime);

		long endTime = startTime + (long) timeInterval;

		Date time = new Date();
		time.setTime(endTime);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(time);
	}

	/**
	 * ����ʱ��
	 * 
	 * @param begDatetime
	 *            ��ʼʱ�� ��ʽΪHH:mm:ss
	 * @param timeSpace
	 *            ʱ����
	 * @param timeUnit
	 *            ʱ�䵥λ��m�����ӡ�h��Сʱ��
	 * @return ���Ӻ��ʱ��
	 */
	public static String AddTime(String begTime, String timeSpace,
			String timeUnit) {
		long timeInterval = Long.parseLong(timeSpace); // ת��Ϊlong�͵�ʱ��������λ��milliseconds

		if (timeUnit.equals("m")) // ����
			timeInterval = timeInterval * 60 * 1000;
		if (timeUnit.equals("h")) // Сʱ
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
	 * ��ָ���ĸ�ʽ���ַ�����ʽ��ΪDate���󣬴��ַ���ͷ��ʼ����һ��ʹ�������ַ���
	 * 
	 * @param src
	 *            ԭʼ�ַ���
	 * @param format
	 *            ��ʽ�ַ���
	 * @return ��ʽ�����Date������������쳣�򷵻�null
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
	 * ��ȡ��ǰ�������ڵ�week����month�ĵ�һ��
	 * 
	 * @param when
	 *            ��ǰʱ��
	 * @param unit
	 *            ��λ���ܻ���month
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
	 * ��ȡ��ǰ�������ڵ�week����month�����һ��
	 * 
	 * @param when
	 *            ��ǰʱ��
	 * @param unit
	 *            ��λ���ܻ���month
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
	 * ȡ��һ��ǰ��ʱ��
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
	 * ȡ��һ��ǰ��ʱ��
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
	 * ȡ��һ��ǰ��ʱ��
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
