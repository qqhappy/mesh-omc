/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-3	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.stat.core.model;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 基站上行时隙数据
 * 
 * @author fanhaoyu
 * 
 */

public class PerfUpTS {

	public final static int LENGTH = 2 * 5 + 5 * 16;
	RealTimePerfResponse rsp;

	public PerfUpTS(RealTimePerfResponse rsp) {
		this.rsp = rsp;
	}

	public int scgMask[] = new int[5]; // 2byte*5
	public int ci[][] = new int[5][16]; // 5 * 16 * 1byte
	private int ciAvg[] = new int[5];

	public Long getCIAvg(int scg) {
		return (long) ciAvg[scg];
	}

	// public Long getOccupied(){
	// char c;
	// long scgmaskOccupied=0;
	// for(int i=0;i<scgMask.length;i++){
	// String s=Integer.toBinaryString(scgMask[i]);
	//
	// int sln=s.length();
	// for(int k=sln;k<16;k++){
	// s="0"+s;
	// }
	//
	// for(int k=0;k<s.length();k++){
	// c=s.charAt(k);
	// if(c=='0'){
	// scgmaskOccupied++;
	// }
	// }
	// }
	// return scgmaskOccupied;
	// }
	// public Long getAvailable(){
	// return 76-getOccupied().longValue();
	// }

	// 上报的是可用信道Mask，标记为0是信道被占用，标记为1是信道可用
	public Long getAvailable() {
		int scgmaskAvailable = 0;
		for (int i = 0; i < scgMask.length; i++) {
			String temp = Integer.toBinaryString(scgMask[i]);
			for (int j = 0; j < temp.length(); j++) {
				if (temp.charAt(j) == '1') {
					scgmaskAvailable++;
				}
			}
		}
		return new Long(scgmaskAvailable);
	}

	public Long getOccupied() {
		return 76 - getAvailable().longValue();
	}

	public void decode(byte[] bt, int startindex) {
		int currentindex = startindex;
		for (int i = 0; i < scgMask.length; i++) {
			scgMask[i] = (int) ByteUtils.toUnsignedNumber(bt, currentindex, 2);
			currentindex += 2;
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 16; j++) {
				ci[i][j] = (int) ByteUtils.toSignedNumber(bt, currentindex, 1);
				currentindex += 1;
			}
			double[] calc = getBTSGlobalCILevelDispVal(ci[i],
					rsp.getRxSensitivity(), i);
			for (int j = 0; j < 16; j++) {
				ci[i][j] = (int) Math.round(calc[j]);
			}
			ciAvg[i] = (int) Math.round(calc[16]);
		}
	}

	public static double[] getBTSGlobalCILevelDispVal(int[] cls,
			int rxSensitivity, int scg) {
		double[] results = new double[cls.length + 1];
		double temp = 0.0;
		int startIndex = 0;
		int endIndex = cls.length;
		if (scg == 0 || scg == 4) {
			endIndex -= 2;
		}
		for (int i = startIndex; i < endIndex; i++) {
			results[i] = rxSensitivity - (-107) + cls[i];
			temp += Math.pow(10.0, results[i] / 10.0);
		}
		for (int i = endIndex; i < cls.length; i++) {
			results[i] = 0;
		}
		results[cls.length] = 10.0 * log(temp / (endIndex - startIndex), 10);
		return results;
	}

	public static double log(double value, double base) {
		return Math.log(value) / Math.log(base);
	}
}
