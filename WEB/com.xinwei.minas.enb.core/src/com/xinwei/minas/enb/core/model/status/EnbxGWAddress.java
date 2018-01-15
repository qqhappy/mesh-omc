package com.xinwei.minas.enb.core.model.status;

import java.io.Serializable;

/**
 * 
 * xGW地址对信息查询请求
 * 
 * @author sunzhangbin
 * 
 */
@SuppressWarnings("serial")
public class EnbxGWAddress implements Serializable {
	//地址对个数
	private int  xgwAddress;

	//本端IP
	private String  localIp;
	
	//对端IP
	private String desIp;
	
	//Vlan索引
	private int vlanIndex;

	public int getXgwAddress() {
		return xgwAddress;
	}

	public void setXgwAddress(int xgwAddress) {
		this.xgwAddress = xgwAddress;
	}

	
	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public String getDesIp() {
		return desIp;
	}

	public void setDesIp(String desIp) {
		this.desIp = desIp;
	}

	public int getVlanIndex() {
		return vlanIndex;
	}

	public void setVlanIndex(int vlanIndex) {
		this.vlanIndex = vlanIndex;
	}

	
	
	
}
