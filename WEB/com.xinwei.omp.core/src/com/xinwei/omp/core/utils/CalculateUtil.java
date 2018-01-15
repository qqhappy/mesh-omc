/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-26	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.omp.core.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * 计算助手
 * 
 * @author fanhaoyu
 * 
 */

public class CalculateUtil {

	public static final String SIGN_PLUS = "+";

	public static final String SIGN_MINUS = "-";

	public static final String SIGN_MULTI = "*";

	public static final String SIGN_DIVIDE = "/";

	public static final String SIGN_POINT = ".";

	public static final String SIGN_LEFT_BRACKET = "(";

	public static final String SIGN_RIGHT_BRACKET = ")";

	public static final String SPLIT = "#";

	public static final String ZERO = "0";

	public static void main(String[] args) {
		String exp = "(1+2)*3-(4/4+9.5)-6*2+8";
		String result = calculate(exp);
		System.out.println(result);
	}

	/**
	 * 对一个表达式进行计算
	 * 
	 * @param exp
	 * @return
	 */
	public static String calculate(String exp) {
		if (isBlank(exp))
			return ZERO;
		// 如果有括号，入栈计算括号
		while (exp.contains(SIGN_LEFT_BRACKET)) {
			exp = calBracket(exp);
		}
		// 判断是否可以结束运算
		if (canFinish(exp)) {
			return exp;
		}
		// 如果表达式以负号开始，在前边补一个0
		if (exp.startsWith(SIGN_MINUS)) {
			exp = ZERO + exp;
		}
		return doCalc(exp);
	}

	/**
	 * 计算一个包含加减乘除的表达式
	 * 
	 * @param exp
	 * @return
	 */
	private static String doCalc(String exp) {
		List<String> numbers = getNumbers(exp);
		List<String> signs = getSigns(exp);
		String tempResult = null;
		int index = 0;
		while (index < signs.size()) {
			String sign = signs.get(index);
			// 如果是乘除运算，先进行计算
			if (sign.equals(SIGN_MULTI) || sign.equals(SIGN_DIVIDE)) {
				double num1 = Double.valueOf(numbers.get(index));
				double num2 = Double.valueOf(numbers.get(index + 1));
				tempResult = String.valueOf(calcDouble(num1, num2, sign));
				numbers.remove(index);
				numbers.remove(index);
				numbers.add(index, tempResult);
				signs.remove(index);
				continue;
			}
			index++;
		}
		while (numbers.size() != 1) {
			String sign = signs.get(0);
			// 如果两个数中有一个是double型，则计算结果必须是double型
			if (isDouble(numbers.get(0)) || isDouble(numbers.get(1))) {
				double num1 = Double.valueOf(numbers.get(0));
				double num2 = Double.valueOf(numbers.get(1));
				tempResult = String.valueOf(calcDouble(num1, num2, sign));
			} else {
				long num1 = Long.valueOf(numbers.get(0));
				long num2 = Long.valueOf(numbers.get(1));
				// 如果是乘除法，计算结果必须是double型
				if (sign.equals(SIGN_MULTI) || sign.equals(SIGN_DIVIDE)) {
					tempResult = String.valueOf(calcDouble(num1, num2, sign));
				} else {
					tempResult = String.valueOf(calcLong(num1, num2, sign));
				}
			}
			numbers.remove(0);
			numbers.remove(0);
			numbers.add(0, tempResult);
			signs.remove(0);
		}
		return numbers.get(0);
	}

	/**
	 * 计算一个带括号的表达式
	 * 
	 * @param exp
	 * @return
	 */
	private static String calBracket(String exp) {
		int leftCount = 0;
		// 左括号位置
		int leftIndex = exp.indexOf(SIGN_LEFT_BRACKET);
		int rightIndex = 0;
		for (int i = 0; i < exp.length(); i++) {
			char c = exp.charAt(i);
			if (c == '(') {
				leftCount++;
			} else if (c == ')') {
				leftCount--;
				if (leftCount == 0) {
					// 与第一个左括号对应的右括号的位置
					rightIndex = i;
					break;
				}
			}
		}
		// 获取以括号为分界的三个部分
		String leftPart = exp.substring(0, leftIndex);
		String midPart = exp.substring(leftIndex, rightIndex + 1);
		String rightPart = exp.substring(rightIndex + 1, exp.length());
		// 先去掉两边的括号，然后把中间的部分当做一个新的表达式来计算
		midPart = midPart.substring(1, midPart.length() - 1);
		midPart = calculate(midPart);
		// 将三个部分拼接
		return leftPart.concat(midPart).concat(rightPart);
	}

	/**
	 * 判断运算是否可以结束
	 * 
	 * @param exp
	 * @return
	 */
	private static boolean canFinish(String exp) {
		int signNum = getSigns(exp).size();
		// 如果以负号开头，且只有一个运算符，则可以结束
		if (exp.startsWith(SIGN_MINUS)) {
			if (signNum == 1) {
				return true;
			}
			return false;
		} else {
			if (signNum == 0)
				return true;
			return false;
		}
	}

	public static List<String> getNumbers(String exp) {
		// 将所有运算符替换为#
		exp = exp.replaceAll("\\" + SIGN_PLUS, SPLIT);
		exp = exp.replaceAll("\\" + SIGN_MINUS, SPLIT);
		exp = exp.replaceAll("\\" + SIGN_MULTI, SPLIT);
		exp = exp.replaceAll("\\" + SIGN_DIVIDE, SPLIT);
		return convertToList(exp.split("\\" + SPLIT));
	}

	public static List<String> getSigns(String exp) {
		List<String> signs = new LinkedList<String>();
		for (int i = 0; i < exp.length(); i++) {
			// 如果该字符是运算符号，则加入运算符列表中
			if (isCalcSign(exp.charAt(i))) {
				signs.add(new String(new char[] { exp.charAt(i) }));
			}
		}
		return signs;
	}

	public static double calcDouble(double num1, double num2, String sign) {
		if (sign.equals(SIGN_PLUS)) {
			return num1 + num2;
		} else if (sign.equals(SIGN_MINUS)) {
			return num1 - num2;
		} else if (sign.equals(SIGN_MULTI)) {
			return num1 * num2;
		} else {
			return num1 / num2;
		}
	}

	public static long calcLong(long num1, long num2, String sign) {
		if (sign.equals(SIGN_PLUS)) {
			return num1 + num2;
		} else if (sign.equals(SIGN_MINUS)) {
			return num1 - num2;
		} else if (sign.equals(SIGN_MULTI)) {
			return num1 * num2;
		} else {
			return num1 / num2;
		}
	}

	/**
	 * 判断一个字符是否是运算符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isCalcSign(char c) {
		String str = new String(new char[] { c });
		if (str.equals(SIGN_MULTI) || str.equals(SIGN_DIVIDE)
				|| str.equals(SIGN_PLUS) || str.equals(SIGN_MINUS))
			return true;
		return false;
	}

	public static boolean isDouble(String number) {
		if (number.contains(SIGN_POINT))
			return true;
		return false;
	}

	public static boolean isBlank(String exp) {
		if (exp == null || exp.length() == 0)
			return true;
		return false;
	}

	public static List<String> convertToList(String[] items) {
		List<String> list = new LinkedList<String>();
		for (String item : items) {
			list.add(item);
		}
		return list;

	}

}