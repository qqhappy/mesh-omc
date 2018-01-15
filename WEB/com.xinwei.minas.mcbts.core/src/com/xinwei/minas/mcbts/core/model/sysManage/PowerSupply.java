/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-18	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * 
 * ��Դ������
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class PowerSupply implements Serializable{

	//��������
	//ֱ��
	public final static int DIRECT_CURRENT = 0x4216;
	
	//����
	public final static int ALTERNATING_CURRENT = 0x4001;
	
	//����
	public final static int RECTIFICATION = 0x410e;
	
	//����
	//����������
	public final static int FACTORY_NINGBO = 0;
	
	
	//����
	private Long idx;
	
	//IP��ַ
	private String ipAddress;
	
	//�˿�
	private int port;
	
	//��������
	private int currentType;
	
	//��������
	private int factoryType;
	
	//������moId
	private HashSet<Long> moIdSet = new HashSet<Long>();
	
	
	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Long getIdx() {
		return idx;
	}	

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getCurrentType() {
		return currentType;
	}

	public void setCurrentType(int currentType) {
		this.currentType = currentType;
	}

	public int getFactoryType() {
		return factoryType;
	}

	public void setFactoryType(int factoryType) {
		this.factoryType = factoryType;
	}
	
	
	public HashSet<Long> getMoIdSet() {
		return moIdSet;
	}

	public void setMoIdSet(HashSet<Long> moIdSet) {
		this.moIdSet = moIdSet;
	}
	
	public void addMoId(long moId) {
		this.moIdSet.add(moId);
	}
	
	public void addMoIdCollections(Collection<Long> moIds) {
		if (moIds != null && moIds.size() > 0) {
			moIdSet.addAll(moIds);
		}
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currentType;
		result = prime * result + factoryType;
		result = prime * result
				+ ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PowerSupply other = (PowerSupply) obj;
		if (currentType != other.currentType)
			return false;
		if (factoryType != other.factoryType)
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	

}
