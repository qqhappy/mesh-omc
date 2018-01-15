package com.xinwei.omp.core.utils;

import java.math.BigInteger;

public class PhoneNumberUtils {
	public static byte[] telNo2Bytes(String telno) {
		byte ret[] = new byte[16];
		for (int i = 0; i < ret.length; i++) {
			encodeUnsignedInt2Bytes(0xff, ret, i, 1);
		}
		int telnolen = telno.length();
		int btind = 0;
		for (int i = 0; i < telnolen; i += 2) {
			int t;
			if (i < telnolen - 1)
				t = Integer.parseInt(
						telno.charAt(i) + "" + telno.charAt(i + 1), 16);
			else
				t = Integer.parseInt(telno.charAt(i) + "F", 16);
			encodeUnsignedInt2Bytes(t, ret, btind, 1);
			btind++;
		}
		return ret;
	}

	private static void encodeUnsignedInt2Bytes(int value, byte[] bt,
			int startIndex, int length) {
		if (value < 0) {
			return;
		}
		BigInteger bgInt;
		bgInt = new BigInteger(String.valueOf(value));
		byte[] by = bgInt.toByteArray();

		// 对于最高位为1的正数,java会在前面再补一个byte =0作为符号标志位.
		// 解码时,直接去掉即可.
		if (by.length > length + 1) {
			return;
		} else if (by.length == length + 1) {
			// 去符号位
			if (by[0] == 0) {
				for (int i = 0; i < length; i++) {
					bt[startIndex + i] = by[i + 1];
				}
			} else {
				return;
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
}
