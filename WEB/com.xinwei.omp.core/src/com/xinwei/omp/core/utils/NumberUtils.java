/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-14	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.utils;

/**
 * 
 * ��ֵ������
 * 
 * @author chenjunhua
 * 
 */

public class NumberUtils {

	/**
	 * �޷���Int����������չΪLong��
	 * 
	 * @param i
	 * @return
	 */
	public static long unsignedInt2Long(int i) {
		byte[] b = new byte[8];
		ByteUtils.putInt(b, 4, i);
		long l = ByteUtils.toLong(b, 0, 8);
		return l;
	} 
	
	/**
	 * Long����ֵת��Ϊ�޷���Int
	 * @param l
	 * @return
	 */
	public static int long2UnsignedInt(long l) {
		return (int) l;
	}
	
	/**
	 * int��ת��Ϊ�޷���short
	 * @param i
	 * @return
	 */
	public static short int2UnsignedShort(int i){
		return (short)i;
	}
	
	/**
	 * �޷���shortת��Ϊint��
	 * @param i
	 * @return
	 */
	public static int unsignedShort2Int(short s){
		byte[] b = new byte[8];
		ByteUtils.putShort(b, 2, s);
		int i = ByteUtils.toInt(b, 0, 4);
		return i;
	}
	
	/**
	 * �޷���byteת��Ϊint
	 * @param bt
	 * @return
	 */
	public static int unsignedbyte2Int(byte bt){
		return bt & 0xff;
	}
	
	/**
	 * int��ת��Ϊ�޷���byte
	 * @param i
	 * @return
	 */
	public static byte int2Unsignedbyte(int i){
		return (byte)i;
	}
	
	public static void main(String[] args) {
//		long a = 4294967294l;
//		System.out.println(a);
//		int b = long2UnsignedInt(a);
//		System.out.println(b);
//		long c = unsignedInt2Long(b);
//		System.out.println(c);
		
//		int d = 130;
//		System.out.println(d);
//		byte e = int2Unsignedbyte(d);
//		System.out.println(e);
//		int f = unsignedbyte2Int(e);
//		System.out.println(f);
		
		int g = 32770;
		System.out.println(g);
		short h = int2UnsignedShort(g);
		System.out.println(h);
		int i = unsignedShort2Int(h);
		System.out.println(i);
		
	}
}
