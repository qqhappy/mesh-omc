/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.model.alarm;

/**
 * 
 * �澯����ģ��
 * 
 */
public class AlarmDef implements java.io.Serializable{

	// �澯����Id
	private long alarmDefId;

	// �澯����
	private int alarmLevel;

	// ��Ԫ����
	private int moType;

	// �澯����
	private String alarmName;

	/**
	 * ��ȡ�澯ID��
	 * 
	 * @return �澯ID������û�������򷵻�-1��
	 */
	public long getAlarmDefId() {
		return alarmDefId;
	}

	/**
	 * ��ȡ�澯����
	 * 
	 * @return �澯��������û�������򷵻�-1��
	 * @see AlarmLevel��
	 */
	public int getAlarmLevel() {
		return alarmLevel;
	}

	/**
	 * ���ø澯������Ϣ��
	 * 
	 * @param alarmDesc
	 *            �澯������Ϣ��
	 */
	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	/**
	 * ���ø澯ID��
	 * 
	 * @param alarmID
	 *            �澯ID��
	 */
	public void setAlarmDefId(long alarmDefId) {
		this.alarmDefId = alarmDefId;
	}

	/**
	 * ���ø澯����
	 * 
	 * @param alarmLevel
	 *            �澯����
	 */
	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	/**
	 * ��ȡ�澯������
	 * 
	 * @return �澯��������û�������򷵻�null��
	 */
	public String getAlarmName() {
		return alarmName;
	}

	public int getMoType() {
		return moType;
	}

	public void setMoType(int moType) {
		this.moType = moType;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("id=");
		buf.append(alarmDefId);
		buf.append(";name=");
		buf.append(alarmName);
		buf.append(";level=");
		buf.append(alarmLevel);
		buf.append(";moType=");
		buf.append(moType);

		return buf.toString();
	}

}
