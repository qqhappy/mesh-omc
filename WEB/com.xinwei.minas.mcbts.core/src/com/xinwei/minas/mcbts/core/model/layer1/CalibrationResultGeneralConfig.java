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
 * 校准结果综合配置模型
 * 
 * @author tiance
 * 
 */

public class CalibrationResultGeneralConfig implements Serializable {

	// private int msgArea = 1;
	// private long mBtsID = 0;
	// private int mTransID = 0;
	// private int mMoc = 0;
	// private int mMA = 0;
	// private int mActionType = 0;

	public final static int HEAD_LENGTH = 14;
	public final static int ANTENNAERROR_LENGTH = 8;
	public final static int LEGNTH = HEAD_LENGTH + 6 + ANTENNAERROR_LENGTH
			* CalibGenConfigItem.LENGTH + ANTENNAERROR_LENGTH
			* AntennaError.LENGTH;

	private CalibrationResultGeneralConfigItem mCalibrationGeneralItem[];
	private int mSYN_TXGain;
	private int mSYN_RXGain;
	private AntennaError mAntennaError[];
	private int mErrorInfo;

	public CalibrationResultGeneralConfigItem[] getCalibrationGeneralItem() {
		return mCalibrationGeneralItem;
	}

	public void setCalibrationGeneralItem(
			CalibrationResultGeneralConfigItem[] mCalibrationGeneralItem) {
		this.mCalibrationGeneralItem = mCalibrationGeneralItem;
	}

	public int getSYN_TXGain() {
		return mSYN_TXGain;
	}

	public void setSYN_TXGain(int mSYN_TXGain) {
		this.mSYN_TXGain = mSYN_TXGain;
	}

	public int getSYN_RXGain() {
		return mSYN_RXGain;
	}

	public void setSYN_RXGain(int mSYN_RXGain) {
		this.mSYN_RXGain = mSYN_RXGain;
	}

	public AntennaError[] getAntennaError() {
		return mAntennaError;
	}

	public void setAntennaError(AntennaError[] mAntennaError) {
		this.mAntennaError = mAntennaError;
	}

	public int getErrorInfo() {
		return mErrorInfo;
	}

	public void setErrorInfo(int mErrorInfo) {
		this.mErrorInfo = mErrorInfo;
	}

	public void parse(byte[] bt) throws Exception {
		if (bt.length < HEAD_LENGTH + 6) {
			throw new Exception("长度不对");
		}
		// 解析文件头,14个字节
		// decodeHead(bt);

		int currentindex = HEAD_LENGTH;
		// 创建校准通用模型,8根
		mCalibrationGeneralItem = new CalibrationResultGeneralConfigItem[ANTENNAERROR_LENGTH];
		for (int i = 0; i < ANTENNAERROR_LENGTH; i++) {
			mCalibrationGeneralItem[i] = new CalibrationResultGeneralConfigItem();
			// 解析模型
			mCalibrationGeneralItem[i].decode(bt, currentindex + i
					* CalibrationResultGeneralConfigItem.LENGTH);
		}
		currentindex += ANTENNAERROR_LENGTH
				* CalibrationResultGeneralConfigItem.LENGTH;
		// 解析mSYN_TXGain
		mSYN_TXGain = ByteUtils.toInt(bt, currentindex, 2);
		currentindex += 2;
		// 解析mSYN_RXGain
		mSYN_RXGain = ByteUtils.toInt(bt, currentindex, 2);
		currentindex += 2;
		// 创建天线错误模型,8根
		mAntennaError = new AntennaError[ANTENNAERROR_LENGTH];
		for (int i = 0; i < ANTENNAERROR_LENGTH; i++) {
			mAntennaError[i] = new AntennaError();
			// 解析天线错误模型
			mAntennaError[i].decode(bt, currentindex + i * AntennaError.LENGTH);
		}
		currentindex += ANTENNAERROR_LENGTH * AntennaError.LENGTH;
		// 解析mErrorInfo
		mErrorInfo = ByteUtils.toInt(bt, currentindex, 2);
		currentindex += 2;
	}

//	/**
//	 * 从传入的byte数组中解析消息头的内容到对象的属性
//	 * 
//	 * @param bt
//	 * @throws MsgDecodeException
//	 */
//	public void decodeHead(byte[] bt) throws Exception {
//		if (bt.length < 14) {
//			throw new Exception();
//		}
//
//		// 解码消息头
//		int currindex = 0;
//		// msg Area
//		msgArea = ByteUtils.toInt(bt, currindex, 2);
//		currindex += 2;
//		// BST ID
//		mBtsID = ByteUtils.toLong(bt, currindex, 4);
//		currindex += 4;
//		// MA
//		mMA = ByteUtils.toInt(bt, currindex, 2);
//		currindex += 2;
//		// Moc
//		mMoc = ByteUtils.toInt(bt, currindex, 2);
//		currindex += 2;
//		// ActionType
//		mActionType = ByteUtils.toInt(bt, currindex, 2);
//		currindex += 2;
//		// Trans ID
//		mTransID = ByteUtils.toInt(bt, currindex, 2);
//		currindex += 2;
//	}
}
