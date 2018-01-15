/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-13	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * �ַ�������
 * 
 * @author chenjunhua
 * 
 */

public class StringUtils {

	/**
	 * ��ʮ������ֵת��Ϊ8λ���ȵ��ַ���������λ��ǰ�油0
	 * 
	 * @param number
	 * @param len
	 * @return
	 */
	public static String to8HexString(Long number) {
		return toHexString(number, 8);
	}

	/**
	 * ��ʮ������ֵת��Ϊָ�����ȵ��ַ���������λ��ǰ�油0
	 * 
	 * @param number
	 * @param len
	 * @return
	 */
	public static String toHexString(Long number, int len) {
		StringBuilder buf = new StringBuilder();
		String hex = Long.toHexString(number);
		int hexLen = hex.length();
		int delta = len - hexLen;
		if (delta > 0) {
			for (int i = 0; i < delta; i++) {
				buf.append("0");
			}
		}
		buf.append(hex);
		return buf.toString();
	}

	/**
	 * ���ַ�������ǰ׺
	 * 
	 * @param str
	 * @param prefix
	 * @param totalLen
	 * @return
	 */
	public static String appendPrefix(String str, String prefix, int totalLen) {
		StringBuilder buf = new StringBuilder();
		int len = str.length();
		int delta = totalLen - len;
		for (int i = 0; i < delta; i++) {
			buf.append(prefix);
		}
		buf.append(str);
		return buf.toString();
	}

	/**
	 * ���ַ����͵�Ipת��Ϊlong����
	 * 
	 * @param ipString
	 * @return
	 */
	public static long toHexIp(String ipString) {
		if ("".equals(ipString)) {
			throw new RuntimeException("ipString is empty!");
		}
		String[] ipArr = ipString.split("\\.");
		if (ipArr.length != 4) {
			throw new RuntimeException("ipString is illegal!");
		} else {
			long hexIp = (Integer.parseInt(ipArr[0]) << 24)
					+ (Integer.parseInt(ipArr[1]) << 16)
					+ (Integer.parseInt(ipArr[2]) << 8)
					+ Integer.parseInt(ipArr[3]);
			return hexIp;
		}
	}

	public static boolean isValidIPv4Address(String source) {
		String reg = "^(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)"
				+ "\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)"
				+ "\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)"
				+ "\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)$";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(source);
		return m.find();
	}

	public static void main(String[] args) {
		Long a = 4294967295l;
		String b = toHexString(a, 8);
		System.out.println(b);
	}

}
