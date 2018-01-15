/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.vo;

import com.xinwei.minas.zk.core.xnode.common.ZkNodeVO;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * SAG状态内容
 * 
 * @author fanhaoyu
 * 
 */

public class ZkSagPayLoadVo extends ZkNodeVO {
	
	public static final int LEN = 16;
	// 最大BTS组容量
	private byte maxGroupNum;
	// 已存在BTS组容量
	private byte existGroupNum;
	// 最大BTS数量
	private short maxBtsNum;
	// 当前BTS数量
	private short existBtsNum;
	// 保留字段
	private short reserve; 
	
//	private int maxBtsGroupNum
	// < 60
	private int userNum;
	
	private int cpuUseRate;

	public byte getMaxGroupNum() {
		return maxGroupNum;
	}

	public void setMaxGroupNum(byte maxGroupNum) {
		this.maxGroupNum = maxGroupNum;
	}

	public byte getExistGroupNum() {
		return existGroupNum;
	}

	public void setExistGroupNum(byte existGroupNum) {
		this.existGroupNum = existGroupNum;
	}

	public short getMaxBtsNum() {
		return maxBtsNum;
	}

	public void setMaxBtsNum(short maxBtsNum) {
		this.maxBtsNum = maxBtsNum;
	}

	public short getExistBtsNum() {
		return existBtsNum;
	}

	public void setExistBtsNum(short existBtsNum) {
		this.existBtsNum = existBtsNum;
	}

	public short getReserve() {
		return reserve;
	}

	public void setReserve(short reserve) {
		this.reserve = reserve;
	}

	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public int getCpuUseRate() {
		return cpuUseRate;
	}

	public void setCpuUseRate(int cpuUseRate) {
		this.cpuUseRate = cpuUseRate;
	}

	@Override
	public void decode(byte[] buf, int offset) {
		this.maxGroupNum = Integer.valueOf(ByteUtils.toInt(buf, offset, 1)).byteValue();
		offset += 1;
		this.existGroupNum = Integer.valueOf(ByteUtils.toInt(buf, offset, 1)).byteValue();
		offset += 1;
		this.maxBtsNum = Integer.valueOf(ByteUtils.toInt(buf, offset, 2)).shortValue();
		offset += 2;
		this.existBtsNum = Integer.valueOf(ByteUtils.toInt(buf, offset, 2)).shortValue();
		offset += 2;
		this.reserve = Integer.valueOf(ByteUtils.toInt(buf, offset, 2)).shortValue();
		offset += 2;
		this.userNum = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.cpuUseRate = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[LEN];
		int offset = 0;
		ByteUtils.putNumber(buf, offset, this.maxGroupNum + "", 1);
		offset += 1;
		ByteUtils.putNumber(buf, offset, this.existGroupNum + "", 1);
		offset += 1;
		ByteUtils.putNumber(buf, offset, this.maxBtsNum + "", 2);
		offset += 2;
		ByteUtils.putNumber(buf, offset, this.existBtsNum + "", 2);
		offset += 2;
		ByteUtils.putNumber(buf, offset, this.reserve + "", 2);
		offset += 2;
		ByteUtils.putNumber(buf, offset, this.userNum + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.cpuUseRate + "", 4);
		offset += 4;
		// copy到实际字节长度的缓存
		byte[] result = new byte[offset];
		System.arraycopy(buf, 0, result, 0, result.length);
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + cpuUseRate;
		result = prime * result + existBtsNum;
		result = prime * result + existGroupNum;
		result = prime * result + maxBtsNum;
		result = prime * result + maxGroupNum;
		result = prime * result + reserve;
		result = prime * result + userNum;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkSagPayLoadVo))
			return false;
		ZkSagPayLoadVo other = (ZkSagPayLoadVo) obj;
		if (cpuUseRate != other.cpuUseRate)
			return false;
		if (existBtsNum != other.existBtsNum)
			return false;
		if (existGroupNum != other.existGroupNum)
			return false;
		if (maxBtsNum != other.maxBtsNum)
			return false;
		if (maxGroupNum != other.maxGroupNum)
			return false;
		if (reserve != other.reserve)
			return false;
		if (userNum != other.userNum)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkSagPayLoadVo [maxGroupNum=" + maxGroupNum
				+ ", existGroupNum=" + existGroupNum + ", maxBtsNum="
				+ maxBtsNum + ", existBtsNum=" + existBtsNum + ", reserve="
				+ reserve + ", userNum=" + userNum + ", cpuUseRate="
				+ cpuUseRate + "]";
	}

	
	
}
