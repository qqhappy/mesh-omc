package com.xinwei.minas.enb.core.model.status;

import java.io.Serializable;

/**
 * 
 * xGW��ַ����Ϣ��ѯ����
 * 
 * @author sunzhangbin
 * 
 */
@SuppressWarnings("serial")
public class EnbxGWAddress implements Serializable {
	//��ַ�Ը���
	private int  xgwAddress;

	//����IP
	private String  localIp;
	
	//�Զ�IP
	private String desIp;
	
	//Vlan����
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
