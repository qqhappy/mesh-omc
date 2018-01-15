/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-15	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ZooKeeper��Ⱥģ��
 * 
 * @author chenjunhua
 * 
 */

public class ZkCluster implements java.io.Serializable {	

	// ��ȺID
	private Long id;

	// ��Ⱥ����
	private String name;
	
	// ����״̬
	private int connectState = ZkClusterConstant.STATE_DISCONNECTED;

	// ��Ⱥ�������б�
	private List<ZkHost> zkHosts = new ArrayList();

	/**
	 * ���������ַ��� e.g. "127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002"
	 * 
	 * @return
	 */
	public String getZkServersString() {
		StringBuilder buf = new StringBuilder();
		for (ZkHost zkHost : zkHosts) {
			buf.append(zkHost.getZkServerString()).append(",");
		}
		buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ZkHost> getZkHosts() {
		return zkHosts;
	}

	public void setZkHosts(List<ZkHost> zkHosts) {
		this.zkHosts = zkHosts;
	}

	public int getConnectState() {
		return connectState;
	}

	public void setConnectState(int connectState) {
		this.connectState = connectState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((zkHosts == null) ? 0 : zkHosts.hashCode());
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
		ZkCluster other = (ZkCluster) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (zkHosts == null) {
			if (other.zkHosts != null)
				return false;
		} else if (!zkHosts.equals(other.zkHosts))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkCluster [id=" + id + ", name=" + name + ", connectState="
				+ connectState + ", zkHosts=" + zkHosts + "]";
	}
	
	

}
