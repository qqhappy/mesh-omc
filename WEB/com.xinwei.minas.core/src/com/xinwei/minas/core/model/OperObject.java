/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.core.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * ��������
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class OperObject implements Serializable {

	// ����������ã����ϣ���ȫ��
	private String operType;

	// ������������
	private String objectType;

	// ����������
	private String objectId;

	/**
	 * ����ϵͳ�������͵Ĳ�������
	 * 
	 * @return
	 */
	public static OperObject createSystemOperObject() {
		OperObject operObject = new OperObject();
		operObject.setOperType(OperTypeDD.CONFIG);
		operObject.setObjectType(OperObjectTypeDD.SYSTEM);
		return operObject;
	}

	/**
	 * ������վ�������͵Ĳ�������
	 * 
	 * @param hexBtsId
	 *            16���ƻ�վId
	 * @return
	 */
	public static OperObject createBtsOperObject(String hexBtsId) {
		OperObject operObject = new OperObject();
		operObject.setOperType(OperTypeDD.CONFIG);
		operObject.setObjectType(OperObjectTypeDD.MCBTS);
		operObject.setObjectId(hexBtsId);
		return operObject;
	}

	/**
	 * ����eNB��վ�������͵Ĳ�������
	 * 
	 * @param hexEnbId
	 *            16����EnbId
	 * @return
	 */
	public static OperObject createEnbOperObject(String hexEnbId) {
		OperObject operObject = new OperObject();
		operObject.setOperType(OperTypeDD.CONFIG);
		operObject.setObjectType(OperObjectTypeDD.ENB);
		operObject.setObjectId(hexEnbId);
		return operObject;
	}

	/**
	 * ����SAG�������͵Ĳ�������
	 * 
	 * @param sagId
	 *            SAG ID
	 * @return
	 */
	public static OperObject createSagOperObject(Long sagId) {
		OperObject operObject = new OperObject();
		operObject.setOperType(OperTypeDD.CONFIG);
		operObject.setObjectType(OperObjectTypeDD.SAG);
		operObject.setObjectId(String.valueOf(sagId));
		return operObject;
	}

	/**
	 * ����UT�������͵Ĳ�������
	 * 
	 * @param eid
	 *            �ն�eid
	 * @return
	 */
	public static OperObject createUtOperObject(String eid) {
		OperObject operObject = new OperObject();
		operObject.setOperType(OperTypeDD.CONFIG);
		operObject.setObjectType(OperObjectTypeDD.UT);
		operObject.setObjectId(eid);
		return operObject;
	}

	
	/**
	 * �����ն��û����͵Ĳ�������
	 * 
	 * @return
	 */
	public static OperObject createUtUserOperObject() {
		OperObject operObject = new OperObject();
		operObject.setOperType(OperTypeDD.UT_USER);
		operObject.setObjectType(OperObjectTypeDD.UT_USER);
		return operObject;

	}
	
	/**
	 * ����TCN1000�ն��û����͵Ĳ�������
	 * @param data
	 * @return
	 */
	public static OperObject createTcn1000UtUserOperObject(Map<String, Object> data) {
		String USR_NUMBER = "usrNumber";
		OperObject operObject = new OperObject();
		operObject.setOperType(OperTypeDD.UT_USER);
		operObject.setObjectType(OperObjectTypeDD.UT_USER);
		// �����û�������û��������Ҫ��¼�û�����
		String usrNumber = (String)data.get(USR_NUMBER);
		if (usrNumber != null) {
			usrNumber = usrNumber.trim();
			operObject.setObjectId(usrNumber);
		}
		return operObject;
	}

	/**
	 * ����TCN1000���͵Ĳ�������
	 * 
	 * @return
	 */
	public static OperObject createTcn1000OperObject() {
		OperObject operObject = new OperObject();
		operObject.setOperType(OperTypeDD.CONFIG);
		operObject.setObjectType(OperObjectTypeDD.TCN1000);
		return operObject;
	}

	/**
	 * ����NK�������͵Ĳ�������
	 * 
	 * @return
	 */
	public static OperObject createNkOperObject() {
		OperObject operObject = new OperObject();
		operObject.setOperType(OperTypeDD.CONFIG);
		operObject.setObjectType(OperObjectTypeDD.NK);
		return operObject;
	}

	/**
	 * �����û���ȫ�������͵Ĳ�������
	 * 
	 * @return
	 */
	public static OperObject createUserOperObject() {
		OperObject operObject = new OperObject();
		operObject.setOperType(OperTypeDD.SECURITY);
		operObject.setObjectType(OperObjectTypeDD.USER);
		return operObject;
	}

	/**
	 * �����������͵Ĳ�������
	 * 
	 * @param objectType
	 * @param objectId
	 * @return
	 */
	public static OperObject createConfigOperObject(String objectType,
			String objectId) {
		OperObject operObject = new OperObject();
		operObject.setOperType(OperTypeDD.CONFIG);
		return operObject;
	}

	/**
	 * �����������͵Ĳ�������
	 * 
	 * @return
	 */
	public static OperObject createAlarmOperObject() {
		OperObject operObject = new OperObject();
		operObject.setOperType(OperTypeDD.ALARM);
		operObject.setObjectType(OperObjectTypeDD.ALARM);
		return operObject;
	}

	/**
	 * ������ȫ���͵Ĳ�������
	 * 
	 * @return
	 */
	public static OperObject createSecurityOperObject(String objectType) {
		OperObject operObject = new OperObject();
		operObject.setOperType(OperTypeDD.SECURITY);
		operObject.setObjectType(objectType);
		return operObject;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	@Override
	public String toString() {
		return "OperObject [operType=" + operType + ", objectType="
				+ objectType + ", objectId=" + objectId + "]";
	}

}