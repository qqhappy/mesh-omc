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
 * 操作对象
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class OperObject implements Serializable {

	// 操作类别（配置，故障，安全）
	private String operType;

	// 操作对象类型
	private String objectType;

	// 操作对象编号
	private String objectId;

	/**
	 * 创建系统配置类型的操作对象
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
	 * 创建基站配置类型的操作对象
	 * 
	 * @param hexBtsId
	 *            16进制基站Id
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
	 * 创建eNB基站配置类型的操作对象
	 * 
	 * @param hexEnbId
	 *            16进制EnbId
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
	 * 创建SAG配置类型的操作对象
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
	 * 创建UT配置类型的操作对象
	 * 
	 * @param eid
	 *            终端eid
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
	 * 创建终端用户类型的操作对象
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
	 * 创建TCN1000终端用户类型的操作对象
	 * @param data
	 * @return
	 */
	public static OperObject createTcn1000UtUserOperObject(Map<String, Object> data) {
		String USR_NUMBER = "usrNumber";
		OperObject operObject = new OperObject();
		operObject.setOperType(OperTypeDD.UT_USER);
		operObject.setObjectType(OperObjectTypeDD.UT_USER);
		// 包含用户号码的用户类操作需要记录用户号码
		String usrNumber = (String)data.get(USR_NUMBER);
		if (usrNumber != null) {
			usrNumber = usrNumber.trim();
			operObject.setObjectId(usrNumber);
		}
		return operObject;
	}

	/**
	 * 创建TCN1000类型的操作对象
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
	 * 创建NK配置类型的操作对象
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
	 * 创建用户安全管理类型的操作对象
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
	 * 创建配置类型的操作对象
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
	 * 创建故障类型的操作对象
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
	 * 创建安全类型的操作对象
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