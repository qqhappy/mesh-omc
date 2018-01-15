/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-3	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.stat.core.model;

import java.math.BigInteger;
import java.util.Calendar;

/**
 * 
 * 基站实时性能回复数据实体
 * 
 * @author fanhaoyu
 * 
 */

public class RealTimePerfResponse {

	private int rxSensitivity;
	private int totalUt;
	private int activeMacSession;
	private PerfDownTS dnts[];
	private PerfUpTS upts[];
	private long TotalBW[] = new long[8]; // 8 * 4byte
	private long TimeStamp; // 8 byte
	// 消息上传周期
	private int ifPeriod = 0;

	public void decode(byte[] buf, int offset) {
		int _deplexMode = decodeUnsignedInt(buf, offset, 2);
		offset += 2;
		rxSensitivity = decodeSignedInt(buf, offset);
		offset += 2;
		totalUt = decodeUnsignedInt(buf, offset, 2);
		offset += 2;
		activeMacSession = decodeUnsignedInt(buf, offset, 2);
		offset += 2;

		dnts = new PerfDownTS[_deplexMode];
		for (int i = 0; i < _deplexMode; i++) {
			dnts[i] = new PerfDownTS();
			dnts[i].decode(buf, offset);
			offset += PerfDownTS.LENGTH;
		}
		upts = new PerfUpTS[8 - _deplexMode];
		for (int i = 0; i < 8 - _deplexMode; i++) {
			upts[i] = new PerfUpTS(this);
			upts[i].decode(buf, offset);
			offset += PerfUpTS.LENGTH;
		}
		for (int i = 0; i < TotalBW.length; i++) {
			TotalBW[i] = decodeUnsignedLong(buf, offset, 4);
			offset += 4;
		}
		// 兼容没有TimeStamp和ifPeriod的情况
		if (offset == buf.length)
			return;

		Calendar c = Calendar.getInstance();
		int year = decodeUnsignedInt(buf, offset, 2);
		offset += 2;
		int month = decodeUnsignedInt(buf, offset, 1) - 1;
		offset += 1;
		int date = decodeUnsignedInt(buf, offset, 1);
		offset += 1;
		int hourOfDay = decodeUnsignedInt(buf, offset, 1);
		offset += 1;
		int minute = decodeUnsignedInt(buf, offset, 1);
		offset += 1;
		int second = decodeUnsignedInt(buf, offset, 1);
		offset += 1;
		c.set(year, month, date, hourOfDay, minute, second);
		c.set(Calendar.MILLISECOND, 0);
		TimeStamp = c.getTimeInMillis() / 1000;
		offset += 1;

		ifPeriod = decodeUnsignedInt(buf, offset, 2);
		offset += 2;
	}

	public RealTimePerfData getTPfBSRealTime() {
		RealTimePerfData p = new RealTimePerfData();
		p.setActMacSess(new Long(this.getActiveMacSession()));
		p.setTotalUT(new Long(this.getTotalUt()));

		int uptsindex = 0;
		if (this.dnts.length >= 1) {
			p.setTotalTakenPwr_1(this.dnts[0].getTotalTakenPwr());
			p.setTotalDownPwr_1(this.dnts[0].getTotalDownPwr());
			p.setScgmaskOccupied_1(this.dnts[0].getOccupied());
			p.setScgmaskAvailable_1(this.dnts[0].getAvailable());

			p.setUpScgmaskAvailable_1(null);
			p.setUpScgmaskOccupied_1(null);
			p.setUpCI_1_1(null);
			p.setUpCI_1_2(null);
			p.setUpCI_1_3(null);
			p.setUpCI_1_4(null);
			p.setUpCI_1_5(null);
		} else {
			p.setTotalTakenPwr_1(null);
			p.setTotalDownPwr_1(null);
			p.setScgmaskOccupied_1(null);
			p.setScgmaskAvailable_1(null);

			p.setUpScgmaskAvailable_1(upts[uptsindex].getAvailable());
			p.setUpScgmaskOccupied_1(upts[uptsindex].getOccupied());
			p.setUpCI_1_1(upts[uptsindex].getCIAvg(0));
			p.setUpCI_1_2(upts[uptsindex].getCIAvg(1));
			p.setUpCI_1_3(upts[uptsindex].getCIAvg(2));
			p.setUpCI_1_4(upts[uptsindex].getCIAvg(3));
			p.setUpCI_1_5(upts[uptsindex].getCIAvg(4));
			uptsindex++;
		}

		if (this.dnts.length >= 2) {
			p.setTotalTakenPwr_2(this.dnts[1].getTotalTakenPwr());
			p.setTotalDownPwr_2(this.dnts[1].getTotalDownPwr());
			p.setScgmaskOccupied_2(this.dnts[1].getOccupied());
			p.setScgmaskAvailable_2(this.dnts[1].getAvailable());

			p.setUpScgmaskAvailable_2(null);
			p.setUpScgmaskOccupied_2(null);
			p.setUpCI_2_1(null);
			p.setUpCI_2_2(null);
			p.setUpCI_2_3(null);
			p.setUpCI_2_4(null);
			p.setUpCI_2_5(null);
		} else {
			p.setTotalTakenPwr_2(null);
			p.setTotalDownPwr_2(null);
			p.setScgmaskOccupied_2(null);
			p.setScgmaskAvailable_2(null);

			p.setUpScgmaskAvailable_2(upts[uptsindex].getAvailable());
			p.setUpScgmaskOccupied_2(upts[uptsindex].getOccupied());
			p.setUpCI_2_1(upts[uptsindex].getCIAvg(0));
			p.setUpCI_2_2(upts[uptsindex].getCIAvg(1));
			p.setUpCI_2_3(upts[uptsindex].getCIAvg(2));
			p.setUpCI_2_4(upts[uptsindex].getCIAvg(3));
			p.setUpCI_2_5(upts[uptsindex].getCIAvg(4));
			uptsindex++;
		}

		if (this.dnts.length >= 3) {
			p.setTotalTakenPwr_3(this.dnts[2].getTotalTakenPwr());
			p.setTotalDownPwr_3(this.dnts[2].getTotalDownPwr());
			p.setScgmaskOccupied_3(this.dnts[2].getOccupied());
			p.setScgmaskAvailable_3(this.dnts[2].getAvailable());

			p.setUpScgmaskAvailable_3(null);
			p.setUpScgmaskOccupied_3(null);
			p.setUpCI_3_1(null);
			p.setUpCI_3_2(null);
			p.setUpCI_3_3(null);
			p.setUpCI_3_4(null);
			p.setUpCI_3_5(null);
		} else {
			p.setTotalTakenPwr_3(null);
			p.setTotalDownPwr_3(null);
			p.setScgmaskOccupied_3(null);
			p.setScgmaskAvailable_3(null);

			p.setUpScgmaskAvailable_3(upts[uptsindex].getAvailable());
			p.setUpScgmaskOccupied_3(upts[uptsindex].getOccupied());
			p.setUpCI_3_1(upts[uptsindex].getCIAvg(0));
			p.setUpCI_3_2(upts[uptsindex].getCIAvg(1));
			p.setUpCI_3_3(upts[uptsindex].getCIAvg(2));
			p.setUpCI_3_4(upts[uptsindex].getCIAvg(3));
			p.setUpCI_3_5(upts[uptsindex].getCIAvg(4));
			uptsindex++;
		}

		if (this.dnts.length >= 4) {
			p.setTotalTakenPwr_4(this.dnts[3].getTotalTakenPwr());
			p.setTotalDownPwr_4(this.dnts[3].getTotalDownPwr());
			p.setScgmaskOccupied_4(this.dnts[3].getOccupied());
			p.setScgmaskAvailable_4(this.dnts[3].getAvailable());

			p.setUpScgmaskAvailable_4(null);
			p.setUpScgmaskOccupied_4(null);
			p.setUpCI_4_1(null);
			p.setUpCI_4_2(null);
			p.setUpCI_4_3(null);
			p.setUpCI_4_4(null);
			p.setUpCI_4_5(null);
		} else {
			p.setTotalTakenPwr_4(null);
			p.setTotalDownPwr_4(null);
			p.setScgmaskOccupied_4(null);
			p.setScgmaskAvailable_4(null);

			p.setUpScgmaskAvailable_4(upts[uptsindex].getAvailable());
			p.setUpScgmaskOccupied_4(upts[uptsindex].getOccupied());
			p.setUpCI_4_1(upts[uptsindex].getCIAvg(0));
			p.setUpCI_4_2(upts[uptsindex].getCIAvg(1));
			p.setUpCI_4_3(upts[uptsindex].getCIAvg(2));
			p.setUpCI_4_4(upts[uptsindex].getCIAvg(3));
			p.setUpCI_4_5(upts[uptsindex].getCIAvg(4));
			uptsindex++;
		}

		if (this.dnts.length >= 5) {
			p.setTotalTakenPwr_5(this.dnts[4].getTotalTakenPwr());
			p.setTotalDownPwr_5(this.dnts[4].getTotalDownPwr());
			p.setScgmaskOccupied_5(this.dnts[4].getOccupied());
			p.setScgmaskAvailable_5(this.dnts[4].getAvailable());

			p.setUpScgmaskAvailable_5(null);
			p.setUpScgmaskOccupied_5(null);
			p.setUpCI_5_1(null);
			p.setUpCI_5_2(null);
			p.setUpCI_5_3(null);
			p.setUpCI_5_4(null);
			p.setUpCI_5_5(null);
		} else {
			p.setTotalTakenPwr_5(null);
			p.setTotalDownPwr_5(null);
			p.setScgmaskOccupied_5(null);
			p.setScgmaskAvailable_5(null);

			p.setUpScgmaskAvailable_5(upts[uptsindex].getAvailable());
			p.setUpScgmaskOccupied_5(upts[uptsindex].getOccupied());
			p.setUpCI_5_1(upts[uptsindex].getCIAvg(0));
			p.setUpCI_5_2(upts[uptsindex].getCIAvg(1));
			p.setUpCI_5_3(upts[uptsindex].getCIAvg(2));
			p.setUpCI_5_4(upts[uptsindex].getCIAvg(3));
			p.setUpCI_5_5(upts[uptsindex].getCIAvg(4));
			uptsindex++;
		}

		if (this.dnts.length >= 6) {
			p.setTotalTakenPwr_6(this.dnts[5].getTotalTakenPwr());
			p.setTotalDownPwr_6(this.dnts[5].getTotalDownPwr());
			p.setScgmaskOccupied_6(this.dnts[5].getOccupied());
			p.setScgmaskAvailable_6(this.dnts[5].getAvailable());

			p.setUpScgmaskAvailable_6(null);
			p.setUpScgmaskOccupied_6(null);
			p.setUpCI_6_1(null);
			p.setUpCI_6_2(null);
			p.setUpCI_6_3(null);
			p.setUpCI_6_4(null);
			p.setUpCI_6_5(null);
		} else {
			p.setTotalTakenPwr_6(null);
			p.setTotalDownPwr_6(null);
			p.setScgmaskOccupied_6(null);
			p.setScgmaskAvailable_6(null);

			p.setUpScgmaskAvailable_6(upts[uptsindex].getAvailable());
			p.setUpScgmaskOccupied_6(upts[uptsindex].getOccupied());
			p.setUpCI_6_1(upts[uptsindex].getCIAvg(0));
			p.setUpCI_6_2(upts[uptsindex].getCIAvg(1));
			p.setUpCI_6_3(upts[uptsindex].getCIAvg(2));
			p.setUpCI_6_4(upts[uptsindex].getCIAvg(3));
			p.setUpCI_6_5(upts[uptsindex].getCIAvg(4));
			uptsindex++;
		}

		if (this.dnts.length >= 7) {
			p.setTotalTakenPwr_7(this.dnts[6].getTotalTakenPwr());
			p.setTotalDownPwr_7(this.dnts[6].getTotalDownPwr());
			p.setScgmaskOccupied_7(this.dnts[6].getOccupied());
			p.setScgmaskAvailable_7(this.dnts[6].getAvailable());

			p.setUpScgmaskAvailable_7(null);
			p.setUpScgmaskOccupied_7(null);
			p.setUpCI_7_1(null);
			p.setUpCI_7_2(null);
			p.setUpCI_7_3(null);
			p.setUpCI_7_4(null);
			p.setUpCI_7_5(null);
		} else {
			p.setTotalTakenPwr_7(null);
			p.setTotalDownPwr_7(null);
			p.setScgmaskOccupied_7(null);
			p.setScgmaskAvailable_7(null);

			p.setUpScgmaskAvailable_7(upts[uptsindex].getAvailable());
			p.setUpScgmaskOccupied_7(upts[uptsindex].getOccupied());
			p.setUpCI_7_1(upts[uptsindex].getCIAvg(0));
			p.setUpCI_7_2(upts[uptsindex].getCIAvg(1));
			p.setUpCI_7_3(upts[uptsindex].getCIAvg(2));
			p.setUpCI_7_4(upts[uptsindex].getCIAvg(3));
			p.setUpCI_7_5(upts[uptsindex].getCIAvg(4));
			uptsindex++;
		}

		if (this.dnts.length >= 8) {
			p.setTotalTakenPwr_8(this.dnts[7].getTotalTakenPwr());
			p.setTotalDownPwr_8(this.dnts[7].getTotalDownPwr());
			p.setScgmaskOccupied_8(this.dnts[7].getOccupied());
			p.setScgmaskAvailable_8(this.dnts[7].getAvailable());

			p.setUpScgmaskAvailable_8(null);
			p.setUpScgmaskOccupied_8(null);
			p.setUpCI_8_1(null);
			p.setUpCI_8_2(null);
			p.setUpCI_8_3(null);
			p.setUpCI_8_4(null);
			p.setUpCI_8_5(null);
		} else {
			p.setTotalTakenPwr_8(null);
			p.setTotalDownPwr_8(null);
			p.setScgmaskOccupied_8(null);
			p.setScgmaskAvailable_8(null);

			p.setUpScgmaskAvailable_8(upts[uptsindex].getAvailable());
			p.setUpScgmaskOccupied_8(upts[uptsindex].getOccupied());
			p.setUpCI_8_1(upts[uptsindex].getCIAvg(0));
			p.setUpCI_8_2(upts[uptsindex].getCIAvg(1));
			p.setUpCI_8_3(upts[uptsindex].getCIAvg(2));
			p.setUpCI_8_4(upts[uptsindex].getCIAvg(3));
			p.setUpCI_8_5(upts[uptsindex].getCIAvg(4));
			uptsindex++;
		}

		p.setTotalBW_1(this.TotalBW[0]);
		p.setTotalBW_2(this.TotalBW[1]);
		p.setTotalBW_3(this.TotalBW[2]);
		p.setTotalBW_4(this.TotalBW[3]);
		p.setTotalBW_5(this.TotalBW[4]);
		p.setTotalBW_6(this.TotalBW[5]);
		p.setTotalBW_7(this.TotalBW[6]);
		p.setTotalBW_8(this.TotalBW[7]);
		return p;

	}

	public int getActiveMacSession() {
		return activeMacSession;
	}

	public void setActiveMacSession(int activeMacSession) {
		this.activeMacSession = activeMacSession;
	}

	public PerfDownTS[] getDnts() {
		return dnts;
	}

	public void setDnts(PerfDownTS[] dnts) {
		this.dnts = dnts;
	}

	public long[] getTotalBW() {
		return TotalBW;
	}

	public void setTotalBW(long[] totalBW) {
		TotalBW = totalBW;
	}

	public int getTotalUt() {
		return totalUt;
	}

	public void setTotalUt(int totalUt) {
		this.totalUt = totalUt;
	}

	public PerfUpTS[] getUpts() {
		return upts;
	}

	public void setUpts(PerfUpTS[] upts) {
		this.upts = upts;
	}

	public long getTimeStamp() {
		return TimeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		TimeStamp = timeStamp;
	}

	public int getIfPeriod() {
		return ifPeriod;
	}

	public void setIfPeriod(int ifPeriod) {
		this.ifPeriod = ifPeriod;
	}

	public int getRxSensitivity() {
		return rxSensitivity;
	}

	public void setRxSensitivity(int rxSensitivity) {
		this.rxSensitivity = rxSensitivity;
	}

	/**
	 * 解析多个字节的无符号整型数
	 * 
	 * @param bts
	 *            ，消息包
	 * @param startindex
	 *            ，起始byte地址
	 * @param byte的个数
	 * @return
	 */
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

	/**
	 * 解析两个字节的带符号整型数
	 * 
	 * @param bts
	 *            ，消息包
	 * @param startindex
	 *            ，起始byte地址
	 * @param byte的个数
	 * @return
	 */
	public static int decodeSignedInt(byte[] bts, int startindex) {
		byte btResult[] = new byte[2];
		for (int i = 0; i < 2; i++) {
			btResult[i] = bts[startindex + i];
		}
		BigInteger bgInt = new BigInteger(btResult);
		return bgInt.intValue();
	}

	/**
	 * 解析多个字节的无符号整型数
	 * 
	 * @param buf
	 *            ，消息包
	 * @param offset
	 *            ，起始byte地址
	 * @param byte的个数
	 * @return
	 */
	public static long decodeUnsignedLong(byte[] buf, int offset, int length) {
		long lret = 0;
		for (int i = 0; i < length; i++) {
			lret += unsignedbyte2int(buf[i + offset])
					* Math.pow(256, length - i - 1);
		}
		return lret;
	}

	private static int unsignedbyte2int(byte bt) {
		int val;
		if (bt <= 127 && bt >= 0) {
			val = bt;
		} else {
			// 128~255
			// -128~-1
			val = 256 + (int) bt;
		}
		return val;
	}

}
