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
	 * ������ֵת��Ϊ�ֽڣ���byte����ָ��λ�á� ����ֵ��ռ��һ���ֽڡ�
	 * 
	 * @param array
	 *            byte���顣
	 * @param offset
	 *            ƫ������
	 * @param val
	 *            ����������
	 */
	public static void putBoolean(byte[] array, int offset, boolean val) {
		array[offset] = (byte) (val ? 1 : 0);
	}

	/**
	 * ���ַ�ת��Ϊ�ֽڣ���byte����ָ��λ�á� һ���ַ�ռ�������ֽڣ����ʱԼ����λ��ǰ����λ�ں�
	 * 
	 * @param array
	 *            byte���顣
	 * @param offset
	 *            ƫ������
	 * @param val
	 *            �ַ�������
	 */
	public static void putChar(byte[] array, int offset, char val) {
		array[offset + 1] = (byte) (val >>> 0);
		array[offset + 0] = (byte) (val >>> 8);
	}

	/**
	 * ������������ת��Ϊ�ֽڣ���䵽byte����ָ��λ�á� һ������������ռ�������ֽڣ����ʱԼ����λ��ǰ����λ�ں�
	 * 
	 * @param array
	 *            byte���顣
	 * @param offset
	 *            ƫ������
	 * @param val
	 *            �����ͱ�����
	 */
	public static void putShort(byte[] array, int offset, short val) {
		array[offset + 1] = (byte) (val >>> 0);
		array[offset + 0] = (byte) (val >>> 8);
	}

	/**
	 * ����������ת��Ϊ�ֽڣ���䵽byte����ָ��λ�á� һ����������ռ��4���ֽڣ����ʱԼ����λ��ǰ����λ�ں�
	 * 
	 * @param array
	 *            byte���顣
	 * @param offset
	 *            ƫ������
	 * @param val
	 *            ���ͱ�����
	 */
	public static void putInt(byte[] array, int offset, int val) {
		array[offset + 3] = (byte) (val >>> 0);
		array[offset + 2] = (byte) (val >>> 8);
		array[offset + 1] = (byte) (val >>> 16);
		array[offset + 0] = (byte) (val >>> 24);
	}

	/**
	 * ��������ת��Ϊ�ֽڣ���䵽byte����ָ��λ�á� һ��������ռ��4���ֽڣ����ʱԼ����λ��ǰ����λ�ں�
	 * 
	 * @param b
	 *            byte���顣
	 * @param offset
	 *            ƫ������
	 * @param val
	 *            ��������
	 */
	public static void putFloat(byte[] array, int offset, float val) {
		int i = Float.floatToIntBits(val);
		array[offset + 0] = (byte) (i >>> 0);
		array[offset + 1] = (byte) (i >>> 8);
		array[offset + 2] = (byte) (i >>> 16);
		array[offset + 3] = (byte) (i >>> 24);
	}

	/**
	 * ��������ת��Ϊ�ֽڣ���䵽byte����ָ��λ�á� һ��������ռ��8���ֽڣ����ʱԼ����λ��ǰ����λ�ں�
	 * 
	 * @param b
	 *            byte���顣
	 * @param off
	 *            ƫ������
	 * @param val
	 *            �����ͱ�����
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
	 * ��Double��ת��Ϊ�ֽڣ����뵽byte�����У�Լ����λ��ǰ����λ�ں�
	 * 
	 * @param b
	 *            byte����
	 * @param off
	 *            ƫ����
	 * @param val
	 *            Double�ͱ���
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
	 * ��һ��ʮ�������ַ���ת���������ֽڣ�����䵽�ֽ������ָ��λ�á� ʮ�������ַ���ռ�õ��ֽ���Ϊ��val.length()/2��
	 * 
	 * @param array
	 *            �ֽ����顣
	 * @param offset
	 *            ƫ������
	 * @param val
	 *            ʮ�������ַ���
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
	 * �ֽڴ�ת������������Լ����λ��ǰ��λ�ں�
	 * 
	 * @param b
	 *            �ֽڴ�
	 * @return ������
	 */
	public static int toInt(byte[] b) {
		return toInt(b, 0, 4);
	}

	/**
	 * �ֽڴ�ת������������Լ����λ��ǰ��λ�ں�
	 * 
	 * @param b
	 *            �ֽڴ�
	 * @param off
	 *            ƫ����
	 * @return ������
	 */
	public static int toInt(byte[] b, int off) {
		return toInt(b, off, 4);
	}

	/**
	 * �ֽڴ�ת������������Լ����λ��ǰ��λ�ں�
	 * 
	 * @param b
	 *            �ֽڴ�
	 * @param off
	 *            ƫ����
	 * @param len
	 *            ���㳤��
	 * @return ������
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
	 * �ֽڴ�ת���ɸ�������Լ����λ��ǰ��λ�ں�
	 * 
	 * @param b
	 *            �ֽڴ�
	 * @return ������
	 */
	public static float toFloat(byte[] b) {
		return toFloat(b, 0, 4);
	}

	/**
	 * �ֽڴ�ת���ɸ�������Լ����λ��ǰ��λ�ں�
	 * 
	 * @param b
	 *            �ֽڴ�
	 * @param off
	 *            ƫ����
	 * @return ������
	 */
	public static float toFloat(byte[] array, int offset) {
		return toFloat(array, offset, 4);
	}

	/**
	 * �ֽڴ�ת���ɸ�������Լ����λ��ǰ��λ�ں�
	 * 
	 * @param b
	 *            �ֽڴ�
	 * @param off
	 *            ƫ����
	 * @param len
	 *            ���㳤��
	 * @return ������
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
	 * �ֽڴ�ת����double��Լ����λ��ǰ��λ�ں�
	 * 
	 * @param b
	 *            �ֽڴ�
	 * @param off
	 *            ƫ����
	 * @param len
	 *            ���㳤��
	 * @return ������
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
	 * BCD�ֽ���ת��Ϊ���� ���磺��ʾΪ0x24��BCD����Ҫת��Ϊ24������
	 * 
	 * @param b
	 *            �ֽ�����
	 * @param offset
	 *            ƫ����
	 * @return ����
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
	 * ���ֽ������һ����ת����16�����ַ�������ת����ÿ���ֽڶ��������޷�������ת����16���Ƶ� ASCII���֡�
	 * <p>
	 * ���磺byte[] array = new byte[]{0x00,0x12,0x41,(byte)0xff,(byte)0xAb,0x11};
	 * </p>
	 * <p>
	 * ת����16�����ַ���Ϊ��001241ffab11��
	 * </p>
	 * 
	 * @param array
	 *            �ֽ����顣
	 * @param offset
	 *            ƫ�������Ӹ�λ�ã�������ʼת����
	 * @param length
	 *            ���ȣ���ת��length�ֽڡ�
	 * @return ת���ɵ��ַ�����
	 */
	public static String toHexString(byte[] array, int offset, int length) {
		return toUnsignedString(array, offset, length, 4);
	}

	/**
	 * �������ֽ�����ת����16�����ַ�����
	 * 
	 * @param array
	 *            �ַ����顣
	 * @return ת���ɵ��ַ�����
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
	 * ��ָ�����ȵ���ֵת��Ϊ�ֽڣ���䵽byte����ָ��λ�á�
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
	 * ��ָ���ֽڳ��ȵ��ֽ�����ת��Ϊ�޷�����
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
	 * ��ָ���ֽڳ��ȵ��ֽ�����ת��Ϊ�з�����
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
	 * ����ip��Ϣ
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
	 * ����Version��Ϣ
	 * 
	 * @param buf
	 * @param offset
	 * @param ip
	 */
	public static void putVersion(byte[] buf, int offset, String version) {
		putIp(buf, offset, version);
	}

	/**
	 * �����ַ���
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
	 * ת��ΪIP
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
	 * ת��Ϊ�汾
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
	 * ���ֽ�����ת��Ϊ�ַ���
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
	 * ���ֽ�����ת��Ϊ�ַ���,Ȼ��ȥ������Ŀո�
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
