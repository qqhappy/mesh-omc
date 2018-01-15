/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.common;

import java.io.Serializable;

/**
 * 
 * ZK�ڵ����ݳ����� 
 * 
 * @author chenjunhua
 * 
 */

public abstract class ZkNodeVO implements Serializable, Cloneable{

	protected ZkNodeReserve zkNodeReserve = new ZkNodeReserve();
	
	public ZkNodeReserve getZkNodeReserve() {
		return zkNodeReserve;
	}

	public void setZkNodeReserve(ZkNodeReserve zkNodeReserve) {
		this.zkNodeReserve = zkNodeReserve;
	}

	/**
	 * ���ֽ�ת��Ϊģ��
	 * @param buf
	 * @param offset
	 */
	public abstract void decode(byte[] buf, int offset);
	
	/**
	 * ��ģ��ת��Ϊ�ֽ�
	 * @return
	 */
	public abstract byte[] encode();

//	/**
//	 * �������ڵ�ģ�ͱȽ�
//	 */
//	public abstract int compareTo(ZkNodeVO other);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((zkNodeReserve == null) ? 0 : zkNodeReserve.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ZkNodeVO))
			return false;
		ZkNodeVO other = (ZkNodeVO) obj;
		if (zkNodeReserve == null) {
			if (other.zkNodeReserve != null)
				return false;
		} else if (!zkNodeReserve.equals(other.zkNodeReserve))
			return false;
		return true;
	}
	
	@Override
	public ZkNodeVO clone() {
		try {
			return (ZkNodeVO)super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
