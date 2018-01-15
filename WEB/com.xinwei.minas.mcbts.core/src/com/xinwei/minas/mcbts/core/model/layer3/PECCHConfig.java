/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-23	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

/**
 * PECCH_Config实体类
 * @author zhuxiaozhan
 * 
 */

public class PECCHConfig implements Serializable{
	
	//公共信道所在的子载波组序号
	private int mSCG_IDX; //2 bit;
	
	//PCH组数
	private int mN_PCH_SET; //3 bit;
	
	//当前小区在N_PCH_SET中的位置
	private int mPCH_ID; //3 bit;
	
	//RARCH组数
	private int mN_RARCH;//2 bit;
	
	//RRCH组数
	private int mN_RRCH; //1 bit;

	public int getmSCG_IDX() {
		return mSCG_IDX;
	}

	public void setmSCG_IDX(int mSCG_IDX) {
		this.mSCG_IDX = mSCG_IDX;
	}

	public int getmN_PCH_SET() {
		return mN_PCH_SET;
	}

	public void setmN_PCH_SET(int mN_PCH_SET) {
		this.mN_PCH_SET = mN_PCH_SET;
	}

	public int getmPCH_ID() {
		return mPCH_ID;
	}

	public void setmPCH_ID(int mPCH_ID) {
		this.mPCH_ID = mPCH_ID;
	}

	public int getmN_RARCH() {
		return mN_RARCH;
	}

	public void setmN_RARCH(int mN_RARCH) {
		this.mN_RARCH = mN_RARCH;
	}

	public int getmN_RRCH() {
		return mN_RRCH;
	}

	public void setmN_RRCH(int mN_RRCH) {
		this.mN_RRCH = mN_RRCH;
	}
	
	public int encode() {
		int code = 0;
		code = code |(mSCG_IDX << 14); // 16-15
		code = code |((mN_PCH_SET - 1) << 11); // 14-12
		code = code |(mPCH_ID - 1 << 8); //11-9
		code = code |((mN_RARCH - 1) << 6);//8-7
		code = code |((mN_RRCH - 1) << 5);//6
		return code;
	}
	
	public void decode(int code) {
		mSCG_IDX = code >> 14 & 3;
		mN_PCH_SET = (code >> 11 & 7) + 1;
		mPCH_ID = code >> 8 & 7 + 1;
		mN_RARCH = (code >> 6 & 3) + 1;
		mN_RRCH = (code >> 5 & 1) + 1;
	}
}
