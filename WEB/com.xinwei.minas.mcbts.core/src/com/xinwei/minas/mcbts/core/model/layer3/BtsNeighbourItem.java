/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-25	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

/**
 * 
 * ¿‡ºÚ“™√Ë ˆ
 * 
 * <p>
 * ¿‡œÍœ∏√Ë ˆ
 * </p>
 * 
 * @author chenshaohua
 * 
 */

public class BtsNeighbourItem implements Serializable {

	// public final static int BCH_LEN = 10;
	//
	// public final static int RRCH_LEN = 10;
	//
	// public final static int RARCH_LEN = 20;

	private String btsIp; // 4

	private long mBTSID; // 4

	private int mFrequencyindex; // 2

	private int mSequenceID;// 1

	private int mSubcarriergroupMask; // 1

	private Airlink_BCH mAirlink_BCH[];

	private Airlink_RRCH mAirlink_RRCH[];

	private Airlink_RARCH mAirlink_RARCH[];

	private int mN_ANT;// 1

	private int mTRANSMIT_PWR; // 1

	private int mN_TS; // 1

	private int mN_DN_TS; // 1

	private int mRECEIVE_SENSITIVITY; // 1

	private int mMAX_SCALE; // 1

	private int mPREAMBLE_SCALE; // 1

	private int mTCH_SCALE0; // 1

	private int mTCH_SCALE1; // 1

	private int mTCH_SCALE2; // 1

	private int mTCH_SCALE3; // 1

	private int mTCH_SCALE4; // 1

	private int mTCH_SCALE5; // 1

	private int mTCH_SCALE6; // 1

	private int mRepeaternumber; // 2

	private RepeaterItem mRepeatItem[];

	public int getmFrequencyindex() {
		return mFrequencyindex;
	}

	public void setmFrequencyindex(int mFrequencyindex) {
		this.mFrequencyindex = mFrequencyindex;
	}

	public int getmSequenceID() {
		return mSequenceID;
	}

	public void setmSequenceID(int mSequenceID) {
		this.mSequenceID = mSequenceID;
	}

	public int getmSubcarriergroupMask() {
		return mSubcarriergroupMask;
	}

	public void setmSubcarriergroupMask(int mSubcarriergroupMask) {
		this.mSubcarriergroupMask = mSubcarriergroupMask;
	}

	public Airlink_BCH[] getmAirlink_BCH() {
		return mAirlink_BCH;
	}

	public void setmAirlink_BCH(Airlink_BCH[] mAirlink_BCH) {
		this.mAirlink_BCH = mAirlink_BCH;
	}

	public Airlink_RRCH[] getmAirlink_RRCH() {
		return mAirlink_RRCH;
	}

	public void setmAirlink_RRCH(Airlink_RRCH[] mAirlink_RRCH) {
		this.mAirlink_RRCH = mAirlink_RRCH;
	}

	public Airlink_RARCH[] getmAirlink_RARCH() {
		return mAirlink_RARCH;
	}

	public void setmAirlink_RARCH(Airlink_RARCH[] mAirlink_RARCH) {
		this.mAirlink_RARCH = mAirlink_RARCH;
	}

	public int getmN_ANT() {
		return mN_ANT;
	}

	public void setmN_ANT(int mN_ANT) {
		this.mN_ANT = mN_ANT;
	}

	public int getmTRANSMIT_PWR() {
		return mTRANSMIT_PWR;
	}

	public void setmTRANSMIT_PWR(int mTRANSMIT_PWR) {
		this.mTRANSMIT_PWR = mTRANSMIT_PWR;
	}

	public int getmN_TS() {
		return mN_TS;
	}

	public void setmN_TS(int mN_TS) {
		this.mN_TS = mN_TS;
	}

	public int getmN_DN_TS() {
		return mN_DN_TS;
	}

	public void setmN_DN_TS(int mN_DN_TS) {
		this.mN_DN_TS = mN_DN_TS;
	}

	public int getmRECEIVE_SENSITIVITY() {
		return mRECEIVE_SENSITIVITY;
	}

	public void setmRECEIVE_SENSITIVITY(int mRECEIVE_SENSITIVITY) {
		this.mRECEIVE_SENSITIVITY = mRECEIVE_SENSITIVITY;
	}

	public int getmMAX_SCALE() {
		return mMAX_SCALE;
	}

	public void setmMAX_SCALE(int mMAX_SCALE) {
		this.mMAX_SCALE = mMAX_SCALE;
	}

	public int getmPREAMBLE_SCALE() {
		return mPREAMBLE_SCALE;
	}

	public void setmPREAMBLE_SCALE(int mPREAMBLE_SCALE) {
		this.mPREAMBLE_SCALE = mPREAMBLE_SCALE;
	}

	public int getmTCH_SCALE0() {
		return mTCH_SCALE0;
	}

	public void setmTCH_SCALE0(int mTCH_SCALE0) {
		this.mTCH_SCALE0 = mTCH_SCALE0;
	}

	public int getmTCH_SCALE1() {
		return mTCH_SCALE1;
	}

	public void setmTCH_SCALE1(int mTCH_SCALE1) {
		this.mTCH_SCALE1 = mTCH_SCALE1;
	}

	public int getmTCH_SCALE2() {
		return mTCH_SCALE2;
	}

	public void setmTCH_SCALE2(int mTCH_SCALE2) {
		this.mTCH_SCALE2 = mTCH_SCALE2;
	}

	public int getmTCH_SCALE3() {
		return mTCH_SCALE3;
	}

	public void setmTCH_SCALE3(int mTCH_SCALE3) {
		this.mTCH_SCALE3 = mTCH_SCALE3;
	}

	public int getmTCH_SCALE4() {
		return mTCH_SCALE4;
	}

	public void setmTCH_SCALE4(int mTCH_SCALE4) {
		this.mTCH_SCALE4 = mTCH_SCALE4;
	}

	public int getmTCH_SCALE5() {
		return mTCH_SCALE5;
	}

	public void setmTCH_SCALE5(int mTCH_SCALE5) {
		this.mTCH_SCALE5 = mTCH_SCALE5;
	}

	public int getmTCH_SCALE6() {
		return mTCH_SCALE6;
	}

	public void setmTCH_SCALE6(int mTCH_SCALE6) {
		this.mTCH_SCALE6 = mTCH_SCALE6;
	}

	public int getmRepeaternumber() {
		return mRepeaternumber;
	}

	public void setmRepeaternumber(int mRepeaternumber) {
		this.mRepeaternumber = mRepeaternumber;
	}

	public RepeaterItem[] getmRepeatItem() {
		return mRepeatItem;
	}

	public void setmRepeatItem(RepeaterItem[] mRepeatItem) {
		this.mRepeatItem = mRepeatItem;
	}

	public String getBtsIp() {
		if (btsIp == null) {
			return "0.0.0.0";
		}
		return btsIp;
	}

	public void setBtsIp(String btsIp) {
		this.btsIp = btsIp;
	}

	public long getmBTSID() {
		return mBTSID;
	}

	public void setmBTSID(long mBTSID) {
		this.mBTSID = mBTSID;
	}

}
