package com.xinwei.omp.core.utils;

import java.math.BigInteger;

public class ReversalCodeUtils {
	/**
	 * 编码
	 * 
	 * @return
	 */
	public static byte[] encode(String val, int len) {
		byte[] bt = new byte[len];
		if (val == null)
			val = "";
		int currentindex = 0;
		for (int i = 0; i < len; i++) {
			char ch1 = 'F';
			char ch2 = 'F';
			if (i * 2 < val.length()) {
				ch2 = char2BCD(val.charAt(i * 2));
				if ((i * 2 + 1) < val.length()) {
					ch1 = char2BCD(val.charAt(i * 2 + 1));
				}
			}
			int v = Integer.parseInt(ch1 + "" + ch2, 16);
			try {
				encodeUnsignedInt2Bytes(v, bt, currentindex, 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			currentindex += 1;
		}
		currentindex += 1;

		return bt;
	}

	/**
	 * 解码
	 * 
	 * @return
	 */
	public static String decode(byte bt[], int len) {

		int currentindex = 0;
		int prefix_val[] = new int[len];
		for (int i = 0; i < len; i++) {
			prefix_val[i] = decodeUnsignedInt(bt, currentindex, 1);
			currentindex += 1;
		}
		String prefix = "";
		for (int i = 0; i < prefix_val.length; i++) {
			String hex = Integer.toHexString(prefix_val[i]).toLowerCase();
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			char c = BCD2char(hex.charAt(1));
			if (c == 'f')
				break;
			else
				prefix += c;

			c = BCD2char(hex.charAt(0));
			if (c == 'f')
				break;
			else
				prefix += c;
		}

		return prefix;
	}

	private static char BCD2char(char c) {
		if (c == 'a')
			c = '0';
		else if (c == 'b')
			c = '*';
		else if (c == 'c')
			c = '#';
		return c;
	}

	private static char char2BCD(char c) {
		if (c == '0')
			c = 'a';
		else if (c == '*')
			c = 'b';
		else if (c == '#')
			c = 'c';
		return c;
	}

	private static void encodeUnsignedInt2Bytes(int value, byte[] bt,
			int startIndex, int length) throws Exception {
		if (value < 0) {
			throw new Exception();
		}
		BigInteger bgInt;
		bgInt = new BigInteger(String.valueOf(value));
		byte[] by = bgInt.toByteArray();

		// 对于最高位为1的正数,java会在前面再补一个byte =0作为符号标志位.
		// 解码时,直接去掉即可.
		if (by.length > length + 1) {
			throw new Exception();
		} else if (by.length == length + 1) {
			// 去符号位
			if (by[0] == 0) {
				for (int i = 0; i < length; i++) {
					bt[startIndex + i] = by[i + 1];
				}
			} else {
				throw new Exception();
			}
		} else {
			// 清零
			for (int i = startIndex; i < startIndex + length; i++) {
				bt[i] = 0;
			}

			// 赋值
			for (int i = by.length; i > 0; i--) {
				bt[startIndex + length - (by.length - i + 1)] = by[i - 1];
			}
		}
	}

	public static int decodeUnsignedInt(byte[] bts, int startindex, int length) {
		byte btResult[] = new byte[length + 1];
		BigInteger bgInt;
		btResult[0] = 0;

		for (int i = 0; i < length; i++) {
			btResult[i + 1] = bts[startindex + i];
		}
		bgInt = new BigInteger(btResult);
		return bgInt.intValue();
	}

	public static void main(String[] args) {
		String a = "";
		byte[] bt = encode(a, 5);
		for (byte b : bt) {
			System.out.println(b);
		}
		System.out.println(decode(bt, 5));
	}
}
