package com.xinwei.omp.core.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ByteUtils {
	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z' };

	// --------------fill byte array----------------------------

	/**
	 * 将布尔值转换为字节，添到byte数组指定位置。 布尔值仅占用一个字节。
	 * 
	 * @param array
	 *            byte数组。
	 * @param offset
	 *            偏移量。
	 * @param val
	 *            布尔变量。
	 */
	public static void putBoolean(byte[] array, int offset, boolean val) {
		array[offset] = (byte) (val ? 1 : 0);
	}

	/**
	 * 将字符转换为字节，添到byte数组指定位置。 一个字符占用两个字节，填充时约定高位在前，低位在后
	 * 
	 * @param array
	 *            byte数组。
	 * @param offset
	 *            偏移量。
	 * @param val
	 *            字符变量。
	 */
	public static void putChar(byte[] array, int offset, char val) {
		array[offset + 1] = (byte) (val >>> 0);
		array[offset + 0] = (byte) (val >>> 8);
	}

	/**
	 * 将短整型数字转换为字节，填充到byte数组指定位置。 一个短整型数字占用两个字节，填充时约定高位在前，低位在后。
	 * 
	 * @param array
	 *            byte数组。
	 * @param offset
	 *            偏移量。
	 * @param val
	 *            短整型变量。
	 */
	public static void putShort(byte[] array, int offset, short val) {
		array[offset + 1] = (byte) (val >>> 0);
		array[offset + 0] = (byte) (val >>> 8);
	}

	/**
	 * 将整型数字转换为字节，填充到byte数组指定位置。 一个整型数字占用4个字节，填充时约定高位在前，低位在后。
	 * 
	 * @param array
	 *            byte数组。
	 * @param offset
	 *            偏移量。
	 * @param val
	 *            整型变量。
	 */
	public static void putInt(byte[] array, int offset, int val) {
		array[offset + 3] = (byte) (val >>> 0);
		array[offset + 2] = (byte) (val >>> 8);
		array[offset + 1] = (byte) (val >>> 16);
		array[offset + 0] = (byte) (val >>> 24);
	}

	/**
	 * 将浮点数转换为字节，填充到byte数组指定位置。 一个浮点数占用4个字节，填充时约定高位在前，低位在后。
	 * 
	 * @param b
	 *            byte数组。
	 * @param offset
	 *            偏移量。
	 * @param val
	 *            浮点数。
	 */
	public static void putFloat(byte[] array, int offset, float val) {
		int i = Float.floatToIntBits(val);
		array[offset + 0] = (byte) (i >>> 0);
		array[offset + 1] = (byte) (i >>> 8);
		array[offset + 2] = (byte) (i >>> 16);
		array[offset + 3] = (byte) (i >>> 24);
	}

	/**
	 * 将长整型转换为字节，填充到byte数组指定位置。 一个长整型占用8个字节，填充时约定高位在前，低位在后。
	 * 
	 * @param b
	 *            byte数组。
	 * @param off
	 *            偏移量。
	 * @param val
	 *            长整型变量。
	 */
	public static void putLong(byte[] array, int offset, long val) {
		int index = offset + 7;
		array[index--] = (byte) (val >>> 0);
		array[index--] = (byte) (val >>> 8);
		array[index--] = (byte) (val >>> 16);
		array[index--] = (byte) (val >>> 24);
		array[index--] = (byte) (val >>> 32);
		array[index--] = (byte) (val >>> 40);
		array[index--] = (byte) (val >>> 48);
		array[index--] = (byte) (val >>> 56);
	}

	/**
	 * 将Double型转换为字节，加入到byte数组中（约定高位在前，低位在后）
	 * 
	 * @param b
	 *            byte数组
	 * @param off
	 *            偏移量
	 * @param val
	 *            Double型变量
	 */
	public static void putDouble(byte[] b, int offset, double val) {
		long j = Double.doubleToLongBits(val);
		b[offset + 0] = (byte) (j >>> 0);
		b[offset + 1] = (byte) (j >>> 8);
		b[offset + 2] = (byte) (j >>> 16);
		b[offset + 3] = (byte) (j >>> 24);
		b[offset + 4] = (byte) (j >>> 32);
		b[offset + 5] = (byte) (j >>> 40);
		b[offset + 6] = (byte) (j >>> 48);
		b[offset + 7] = (byte) (j >>> 56);
	}

	/**
	 * 把一个十六进制字符串转换成若干字节，并填充到字节数组的指定位置。 十六进制字符串占用的字节数为：val.length()/2。
	 * 
	 * @param array
	 *            字节数组。
	 * @param offset
	 *            偏移量。
	 * @param val
	 *            十六进制字符串
	 */
	public static void putHexString(byte[] array, int offset, String val) {
		int size = val.length() / 2;
		int max = offset + size;
		for (int i = offset, j = 0; i < max; i++, j++) {
			array[i] = (byte) (Integer.parseInt(
					val.substring(2 * j, 2 * j + 2), 16));
		}
	}

	// -----------converts bytes to primitive----------

	/**
	 * 字节串转换成整型数（约定高位在前低位在后）
	 * 
	 * @param b
	 *            字节串
	 * @return 整型数
	 */
	public static int toInt(byte[] b) {
		return toInt(b, 0, 4);
	}

	/**
	 * 字节串转换成整型数（约定高位在前低位在后）
	 * 
	 * @param b
	 *            字节串
	 * @param off
	 *            偏移量
	 * @return 整型数
	 */
	public static int toInt(byte[] b, int off) {
		return toInt(b, off, 4);
	}

	/**
	 * 字节串转换成整型数（约定高位在前低位在后）
	 * 
	 * @param b
	 *            字节串
	 * @param off
	 *            偏移量
	 * @param len
	 *            计算长度
	 * @return 整型数
	 */
	public static int toInt(byte[] b, int off, int len) {
		int st = 0;
		if (off < 0)
			off = 0;
		if (len > 4)
			len = 4;
		for (int i = 0; i < len && (i + off) < b.length; i++) {
			st <<= 8;
			st += (int) b[i + off] & 0xff;
		}
		return st;
	}

	public static long toLong(byte[] b, int off, int len) {
		long result = 0;
		if (off < 0)
			off = 0;
		if (len > 8)
			len = 8;
		for (int i = 0; i < len && (i + off) < b.length; i++) {
			result <<= 8;
			result += (int) b[i + off] & 0xff;
		}
		return result;
	}

	/**
	 * 字节串转换成浮点数（约定低位在前高位在后）
	 * 
	 * @param b
	 *            字节串
	 * @return 整型数
	 */
	public static float toFloat(byte[] b) {
		return toFloat(b, 0, 4);
	}

	/**
	 * 字节串转换成浮点数（约定低位在前高位在后）
	 * 
	 * @param b
	 *            字节串
	 * @param off
	 *            偏移量
	 * @return 整型数
	 */
	public static float toFloat(byte[] array, int offset) {
		return toFloat(array, offset, 4);
	}

	/**
	 * 字节串转换成浮点数（约定低位在前高位在后）
	 * 
	 * @param b
	 *            字节串
	 * @param off
	 *            偏移量
	 * @param len
	 *            计算长度
	 * @return 整型数
	 */
	public static float toFloat(byte[] array, int offset, int len) {
		int st = 0;
		if (offset < 0)
			offset = 0;
		if (len > 4)
			len = 4;
		for (int i = 0; i < len && (i + offset) < array.length; i++) {
			st += ((int) array[i + offset] & 0xff) << (8 * i);
		}
		return Float.intBitsToFloat(st);
	}

	/**
	 * 字节串转换成double（约定低位在前高位在后）
	 * 
	 * @param b
	 *            字节串
	 * @param off
	 *            偏移量
	 * @param len
	 *            计算长度
	 * @return 整型数
	 */
	public static double toDouble(byte[] array, int offset, int len) {
		long st = 0;
		if (offset < 0)
			offset = 0;
		if (len > 8)
			len = 8;
		for (int i = 0; i < len && (i + offset) < array.length; i++) {
			st += ((long) array[i + offset] & 0xff) << (8 * i);
		}
		return Double.longBitsToDouble(st);
	}

	/**
	 * BCD字节码转换为整数 例如：显示为0x24的BCD编码要转换为24的整数
	 * 
	 * @param b
	 *            字节数组
	 * @param offset
	 *            偏移量
	 * @return 整数
	 */
	public static int bcdByteToInt(byte[] array, int offset) {
		if (offset > array.length)
			return 0;
		return ((int) ((array[offset] & 0xff) / 16) * 10 + (int) (array[offset] & 0x0f));
	}

	public static char toChar(byte[] b) {
		char ch = 0;
		if (b[0] > 0)
			ch += b[0];
		else
			ch += 256 + b[0];
		ch *= 256;

		if (b[1] > 0)
			ch += b[1];
		else
			ch += 256 + b[1];
		return ch;
	}

	// ---------------convert bytes to string------------

	public static String toBinaryString(byte[] array, int offset, int length) {
		return toUnsignedString(array, offset, length, 1);
	}

	public static String toOctalString(byte[] array, int offset, int length) {
		return toUnsignedString(array, offset, length, 3);
	}

	/**
	 * 把字节数组的一部分转化成16进制字符串。被转换的每个字节都被当作无符号整数转换成16进制的 ASCII数字。
	 * <p>
	 * 例如：byte[] array = new byte[]{0x00,0x12,0x41,(byte)0xff,(byte)0xAb,0x11};
	 * </p>
	 * <p>
	 * 转换成16进制字符串为“001241ffab11”
	 * </p>
	 * 
	 * @param array
	 *            字节数组。
	 * @param offset
	 *            偏移量，从该位置（含）开始转换。
	 * @param length
	 *            长度，共转换length字节。
	 * @return 转换成的字符串。
	 */
	public static String toHexString(byte[] array, int offset, int length) {
		return toUnsignedString(array, offset, length, 4);
	}

	/**
	 * 把整个字节数组转化成16进制字符串。
	 * 
	 * @param array
	 *            字符数组。
	 * @return 转换成的字符串。
	 */
	public static String toHexString(byte[] array) {
		int len = (array == null) ? 0 : array.length;
		return toUnsignedString(array, 0, len, 4);
	}

	private static String toUnsignedString(byte[] bytes, int offset,
			int length, int shift) {
		int bytesNum = length;
		int charsNum = (shift == 3) ? 3 : 8 / shift;
		int bufSize = charsNum * bytesNum;

		char[] buf = new char[bufSize];
		int charPos = bufSize;
		int radix = 1 << shift;
		int mask = radix - 1;

		int startPos = 0;
		int b = 0;
		int min = offset - 1;
		for (int i = bytesNum - 1 + offset; i > min; i--) {
			b = bytes[i] & 0xff;
			startPos = charPos - charsNum;
			do {
				buf[--charPos] = digits[b & mask];
				b >>>= shift;
			} while (b != 0);

			for (int j = startPos; j < charPos; j++) {
				buf[j] = digits[0];
			}
			charPos = startPos;
		}
		return new String(buf);
	}

	/**
	 * 将指定长度的数值转换为字节，填充到byte数组指定位置。
	 * 
	 * @param buf
	 * @param offset
	 * @param value
	 * @param length
	 */
	public static int putNumber(byte[] buf, int offset, String value, int length) {
		long lValue = Long.parseLong(value);
		switch (length) {
		case 1:
			buf[offset] = (byte) lValue;
			break;
		case 2:
			short sValue = (short) lValue;
			ByteUtils.putShort(buf, offset, sValue);
			break;
		case 4:
			int iValue = (int) lValue;
			ByteUtils.putInt(buf, offset, iValue);
			break;
		case 8:
			ByteUtils.putLong(buf, offset, lValue);
			break;
		default:
			break;
		}
		offset += length;
		return offset;
	}

	/**
	 * 将指定字节长度的字节数组转换为无符号数
	 * 
	 * @param buf
	 * @param offset
	 * @param length
	 * @return
	 */
	public static long toUnsignedNumber(byte[] buf, int offset, int length) {
		return ByteUtils.toLong(buf, offset, length);
	}

	/**
	 * 将指定字节长度的字节数组转换为有符号数
	 * 
	 * @param buf
	 * @param offset
	 * @param length
	 * @return
	 */
	public static long toSignedNumber(byte[] buf, int offset, int length) {
		long value = ByteUtils.toLong(buf, offset, length);
		switch (length) {
		case 1:
			if (value > 0x7f) {
				value = value - (0xff + 1);
			}
			break;
		case 2:
			if (value > 0x7fff) {
				value = value - (0xffff + 1);
			}
			break;
		case 4:
			if (value > 0x7fffffff) {
				value = value - (0xffffffffL + 1);
			}
			break;
		default:
			break;
		}
		return value;
	}

	/**
	 * 放置ip信息
	 * 
	 * @param buf
	 * @param offset
	 * @param ip
	 */
	public static void putIp(byte[] buf, int offset, String ip) {
		String[] ipNums = ip.split("\\.");
		for (String ipNum : ipNums) {
			putNumber(buf, offset, ipNum, 1);
			offset++;
		}
	}

	/**
	 * 放置Version信息
	 * 
	 * @param buf
	 * @param offset
	 * @param ip
	 */
	public static void putVersion(byte[] buf, int offset, String version) {
		putIp(buf, offset, version);
	}

	/**
	 * 放置字符串
	 * 
	 * @param buf
	 * @param offset
	 * @param str
	 * @param length
	 * @param charset
	 */
	public static void putString(byte[] buf, int offset, String str,
			int length, char fillChar, String charsetName) {
		byte[] value = new byte[0];
		if (charsetName == null || charsetName.equals("")) {
			charsetName = System.getProperty("file.encoding");
		}
		try {
			value = str.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
			value = str.getBytes();
		}
		if (value.length > length) {
			System.arraycopy(value, 0, buf, offset, length);
		} else {
			System.arraycopy(value, 0, buf, offset, value.length);
			int fillCharLen = length - value.length;
			offset += value.length;
			for (int i = 0; i < fillCharLen; i++) {
				buf[offset++] = (byte) fillChar;
			}
		}
	}

	/**
	 * 转换为IP
	 * 
	 * @param buf
	 * @param offset
	 * @return
	 */
	public static String toIp(byte[] buf, int offset, int length) {
		StringBuilder ip = new StringBuilder();
		;
		for (int i = 0; i < length; i++) {
			int value = buf[offset++] & 0xff;
			ip.append(value).append(".");
		}
		ip.deleteCharAt(ip.length() - 1);
		return ip.toString();
	}

	/**
	 * 转换为版本
	 * 
	 * @param buf
	 * @param offset
	 * @param length
	 * @return
	 */
	public static String toVersion(byte[] buf, int offset, int length) {
		return toIp(buf, offset, length);
	}

	/**
	 * 将字节内容转换为字符串
	 * 
	 * @param buf
	 * @param offset
	 * @param length
	 * @param charsetName
	 * @return
	 */
	public static String toString(byte[] buf, int offset, int length,
			String charsetName) {
		String str = null;
		if (charsetName == null || charsetName.equals("")) {
			charsetName = System.getProperty("file.encoding");
		}
		try {
			str = new String(buf, offset, length, charsetName);
		} catch (UnsupportedEncodingException e) {
			str = new String(buf, offset, length);
		}
		return str;
	}

	/**
	 * 将字节内容转换为字符串,然后去掉两遍的空格
	 * 
	 * @param buf
	 * @param offset
	 * @param length
	 * @param charsetName
	 * @return
	 */
	public static String toTrimmedString(byte[] buf, int offset, int length,
			String charsetName) {
		return toString(buf, offset, length, charsetName).trim();
	}

	public static void main(String[] agrs) {
		byte[] buf = new byte[]{(byte) 0, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255};

		long a = ByteUtils.toUnsignedNumber(buf, 0, 8);
		long b = ByteUtils.toSignedNumber(buf, 0, 8);
		
		System.out.println("a=" + a);
		System.out.println("b=" + b);
		

	}

}
