/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-15	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.vo;

import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.*;

import java.util.Arrays;

import com.xinwei.minas.zk.core.xnode.common.ZkNodeReserve;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeVO;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * RULES规则节点内容
 * 
 * @author chenjunhua
 * 
 */

public class ZkRulesVO extends ZkNodeVO {
	
	public static final int RULES_FIXEDPLAN = 0;
	public static final int RULES_PLANFIRST_RANDOM = 1;
	public static final int RULES_PLANFIRST_WEIGHT = 2;

	// 0- 固定规划; 1-规划优先-随机; 2-规划优先-加权
	private int accessType = RULES_PLANFIRST_RANDOM;
	
	// 切换规则
	private byte[] xmlData = new byte[NK_NODE_RULEXML_LEN];

	public int getAccessType() {
		return accessType;
	}

	public void setAccessType(int accessType) {
		this.accessType = accessType;
	}

	public byte[] getXmlData() {
		return xmlData;
	}

	public void setXmlData(byte[] xmlData) {
		this.xmlData = xmlData;
	}

	@Override
	public void decode(byte[] buf, int offset) {
		this.accessType = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.zkNodeReserve.decode(buf, offset);
		offset += ZkNodeReserve.LEN;
		System.arraycopy(buf, offset, this.xmlData, 0, NK_NODE_RULEXML_LEN);
		offset += NK_NODE_RULEXML_LEN;
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[4096];
		int offset = 0;
		ByteUtils.putNumber(buf, offset, accessType + "", 4);
		offset += 4;
		if(this.zkNodeReserve == null)
			this.zkNodeReserve = new ZkNodeReserve();
		System.arraycopy(this.zkNodeReserve.encode(), 0, buf, offset, ZkNodeReserve.LEN);
		offset += ZkNodeReserve.LEN;
		System.arraycopy(this.xmlData, 0, buf, offset, NK_NODE_RULEXML_LEN);
		offset += NK_NODE_RULEXML_LEN;
		// copy到实际字节长度的缓存
		byte[] result = new byte[offset];
		System.arraycopy(buf, 0, result, 0, result.length);
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + accessType;
		result = prime * result + Arrays.hashCode(xmlData);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkRulesVO))
			return false;
		ZkRulesVO other = (ZkRulesVO) obj;
		if (accessType != other.accessType)
			return false;
		if (!Arrays.equals(xmlData, other.xmlData))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkRulesVO [accessType=" + accessType + ", xmlData="
				+ Arrays.toString(xmlData) + "]";
	}

	
}
