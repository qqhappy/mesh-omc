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
 * ��������
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
	 * ��һ�����ʽ���м���
	 * 
	 * @param exp
	 * @return
	 */
	public static String calculate(String exp) {
		if (isBlank(exp))
			return ZERO;
		// ��������ţ���ջ��������
		while (exp.contains(SIGN_LEFT_BRACKET)) {
			exp = calBracket(exp);
		}
		// �ж��Ƿ���Խ�������
		if (canFinish(exp)) {
			return exp;
		}
		// ������ʽ�Ը��ſ�ʼ����ǰ�߲�һ��0
		if (exp.startsWith(SIGN_MINUS)) {
			exp = ZERO + exp;
		}
		return doCalc(exp);
	}

	/**
	 * ����һ�������Ӽ��˳��ı��ʽ
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
			// ����ǳ˳����㣬�Ƚ��м���
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
			// �������������һ����double�ͣ��������������double��
			if (isDouble(numbers.get(0)) || isDouble(numbers.get(1))) {
				double num1 = Double.valueOf(numbers.get(0));
				double num2 = Double.valueOf(numbers.get(1));
				tempResult = String.valueOf(calcDouble(num1, num2, sign));
			} else {
				long num1 = Long.valueOf(numbers.get(0));
				long num2 = Long.valueOf(numbers.get(1));
				// ����ǳ˳�����������������double��
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
	 * ����һ�������ŵı��ʽ
	 * 
	 * @param exp
	 * @return
	 */
	private static String calBracket(String exp) {
		int leftCount = 0;
		// ������λ��
		int leftIndex = exp.indexOf(SIGN_LEFT_BRACKET);
		int rightIndex = 0;
		for (int i = 0; i < exp.length(); i++) {
			char c = exp.charAt(i);
			if (c == '(') {
				leftCount++;
			} else if (c == ')') {
				leftCount--;
				if (leftCount == 0) {
					// ���һ�������Ŷ�Ӧ�������ŵ�λ��
					rightIndex = i;
					break;
				}
			}
		}
		// ��ȡ������Ϊ�ֽ����������
		String leftPart = exp.substring(0, leftIndex);
		String midPart = exp.substring(leftIndex, rightIndex + 1);
		String rightPart = exp.substring(rightIndex + 1, exp.length());
		// ��ȥ�����ߵ����ţ�Ȼ����м�Ĳ��ֵ���һ���µı��ʽ������
		midPart = midPart.substring(1, midPart.length() - 1);
		midPart = calculate(midPart);
		// ����������ƴ��
		return leftPart.concat(midPart).concat(rightPart);
	}

	/**
	 * �ж������Ƿ���Խ���
	 * 
	 * @param exp
	 * @return
	 */
	private static boolean canFinish(String exp) {
		int signNum = getSigns(exp).size();
		// ����Ը��ſ�ͷ����ֻ��һ�������������Խ���
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
		// ������������滻Ϊ#
		exp = exp.replaceAll("\\" + SIGN_PLUS, SPLIT);
		exp = exp.replaceAll("\\" + SIGN_MINUS, SPLIT);
		exp = exp.replaceAll("\\" + SIGN_MULTI, SPLIT);
		exp = exp.replaceAll("\\" + SIGN_DIVIDE, SPLIT);
		return convertToList(exp.split("\\" + SPLIT));
	}

	public static List<String> getSigns(String exp) {
		List<String> signs = new LinkedList<String>();
		for (int i = 0; i < exp.length(); i++) {
			// ������ַ���������ţ������������б���
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
	 * �ж�һ���ַ��Ƿ��������
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