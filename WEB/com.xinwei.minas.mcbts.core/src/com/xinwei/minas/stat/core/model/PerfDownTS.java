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
 * 基站下行时隙数据
 * 
 * @author fanhaoyu
 * 
 */

public class PerfDownTS {

	public final static int LENGTH = 8 + 2 * 5;

	private long totalDownPwr; // 4 byte
	private long totalTakenPwr; // 4 byte
	private int scgMask[] = new int[5]; // 2*5 byte

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

	public void decode(byte[] buf, int offset) {
		totalDownPwr = ByteUtils.toUnsignedNumber(buf, offset, 4);
		offset += 4;
		totalTakenPwr = ByteUtils.toUnsignedNumber(buf, offset, 4);
		offset += 4;
		for (int i = 0; i < scgMask.length; i++) {
			scgMask[i] = (int) ByteUtils.toUnsignedNumber(buf, offset, 2);
			offset += 2;
		}
	}

	public void encode(byte[] buf, int offset) {
		ByteUtils.putNumber(buf, offset, String.valueOf(totalDownPwr), 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, String.valueOf(totalTakenPwr), 4);
		offset += 4;
		for (int i = 0; i < scgMask.length; i++) {
			ByteUtils.putNumber(buf, offset, String.valueOf(scgMask[i]), 2);
			offset += 2;
		}
	}

	public int[] getScgMask() {
		return scgMask;
	}

	public void setScgMask(int[] scgMask) {
		this.scgMask = scgMask;
	}

	public long getTotalDownPwr() {
		return totalDownPwr;
	}

	public void setTotalDownPwr(long totalDownPwr) {
		this.totalDownPwr = totalDownPwr;
	}

	public long getTotalTakenPwr() {
		return totalTakenPwr;
	}

	public void setTotalTakenPwr(long totalTakenPwr) {
		this.totalTakenPwr = totalTakenPwr;
	}
}
