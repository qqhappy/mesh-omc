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
 * 类简要描述
 * 
 * @author tiance
 * 
 */

public class CalibrationResultGeneralConfigItem implements Serializable {
	public final static int LENGTH = 28;
	public final static int PRED_H_LENGTH = 23;

	private byte mPRED_H[];
	private int antennaCalibrationResult;
	private int mTX_GAIN;
	private int mRX_GAIN;

	public byte[] getmPRED_H() {
		return mPRED_H;
	}

	public void setmPRED_H(byte[] mPRED_H) {
		this.mPRED_H = mPRED_H;
	}

	public int getAntennaCalibrationResult() {
		return antennaCalibrationResult;
	}

	public void setAntennaCalibrationResult(int antennaCalibrationResult) {
		this.antennaCalibrationResult = antennaCalibrationResult;
	}

	public int getmTX_GAIN() {
		return mTX_GAIN;
	}

	public void setmTX_GAIN(int mTX_GAIN) {
		this.mTX_GAIN = mTX_GAIN;
	}

	public int getmRX_GAIN() {
		return mRX_GAIN;
	}

	public void setmRX_GAIN(int mRX_GAIN) {
		this.mRX_GAIN = mRX_GAIN;
	}

	public void decode(byte[] bt, int startindex) throws Exception {
		int currentindex = startindex;
		mPRED_H = new byte[PRED_H_LENGTH];
		// 把23位字节存入mPRED_H中,然后向后延23位
		for (int i = 0; i < PRED_H_LENGTH; i++) {
			mPRED_H[i] = bt[currentindex + i];
		}
		currentindex += PRED_H_LENGTH;
		// 解析天线校准结果
		antennaCalibrationResult = ByteUtils.toInt(bt, currentindex, 1);
		currentindex += 1;
		// 解析mTX_GAIN
		mTX_GAIN = ByteUtils.toInt(bt, currentindex, 2);
		currentindex += 2;
		// 解析mRX_GAIN
		mRX_GAIN = ByteUtils.toInt(bt, currentindex, 2);
		currentindex += 2;
	}
}
