/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-24	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer1;

import java.io.Serializable;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * ¿‡ºÚ“™√Ë ˆ
 * 
 * 
 * @author tiance
 * 
 */

public class CalibrationResultDataInfo implements Serializable {
	public final static int HEAD_LENGTH = 14;
	public final static int CAL_DATA_SIZE = 640;
	public final static int LEGNTH = HEAD_LENGTH + 8 + CAL_DATA_SIZE * 2;

	public final static int CALIBRATION_TYPE_TXCAL_I = 1;
	public final static int CALIBRATION_TYPE_TXCAL_Q = 2;
	public final static int CALIBRATION_TYPE_RXCAL_I = 3;
	public final static int CALIBRATION_TYPE_RXCAL_Q = 4;

	private int mType;
	private int mAntennalIndex;
	private int mSCstartingIndex;
	private int mSC = CAL_DATA_SIZE;
	private int mCALdataforSubcarrieri[] = new int[CAL_DATA_SIZE];

	public void decode(byte[] bt) throws Exception {
		if (bt.length < HEAD_LENGTH + 8) {
			throw new Exception();
		}
		// this.decodeHead(bt);
		int currentindex = HEAD_LENGTH;
		mType = ByteUtils.toInt(bt, currentindex, 2);
		currentindex += 2;
		mAntennalIndex = ByteUtils.toInt(bt, currentindex, 2);
		currentindex += 2;
		mSCstartingIndex = ByteUtils.toInt(bt, currentindex, 2);
		currentindex += 2;
		currentindex += 2;// skip sc
		mSC = CAL_DATA_SIZE;
		mCALdataforSubcarrieri = new int[mSC];
		for (int i = 0; i < mSC; i++) {
			mCALdataforSubcarrieri[i] = (int) ByteUtils.toSignedNumber(bt,
					currentindex, 2);
			currentindex += 2;
		}
	}

	public int getType() {
		return mType;
	}

	public void setType(int mType) {
		this.mType = mType;
	}

	public int getAntennalIndex() {
		return mAntennalIndex;
	}

	public void setAntennalIndex(int mAntennalIndex) {
		this.mAntennalIndex = mAntennalIndex;
	}

	public int getSCstartingIndex() {
		return mSCstartingIndex;
	}

	public void setSCstartingIndex(int mSCstartingIndex) {
		this.mSCstartingIndex = mSCstartingIndex;
	}

	public int getSC() {
		return mSC;
	}

	public void setSC(int mSC) {
		this.mSC = mSC;
	}

	public int[] getCALdataforSubcarrieri() {
		return mCALdataforSubcarrieri;
	}

	public void setCALdataforSubcarrieri(int[] mCALdataforSubcarrieri) {
		this.mCALdataforSubcarrieri = mCALdataforSubcarrieri;
	}

}
