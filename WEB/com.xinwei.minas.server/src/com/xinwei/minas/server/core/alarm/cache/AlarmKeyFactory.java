/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.alarm.cache;


import com.xinwei.common.utils.HashCodeGenerator;
import com.xinwei.minas.core.model.alarm.Alarm;
/**
 * 
 * ¸æ¾¯Key¹¤³§
 * 
 */
public class AlarmKeyFactory {
	public static AlarmKey create(Alarm alarm) {
		return create(alarm.getEntityType(), alarm.getEntityOid(), alarm
				.getAlarmDefId());
	}

	public static AlarmKey create(String entityType, String entityOid,
			long alarmId) {
		return new AlarmKeyImpl1(entityType, entityOid, alarmId);
	}

	private static class AlarmKeyImpl1 implements AlarmKey {
		
		private long alarmId;

		private String entityType;

		private String entityOid;

		public AlarmKeyImpl1(String entityType, String entityOid, long alarmId) {
			this.entityType = entityType;
			this.entityOid = entityOid;
			this.alarmId = alarmId;
		}

		public int hashCode() {
			int hashCode = 0;
			hashCode ^= HashCodeGenerator.generate(entityType);
			hashCode ^= HashCodeGenerator.generate(entityOid);
			hashCode ^= HashCodeGenerator.generate(alarmId);
			return hashCode;
		}

		public boolean equals(Object obj) {
			if (obj instanceof AlarmKeyImpl1) {
				AlarmKeyImpl1 temp = (AlarmKeyImpl1) obj;
				return ((alarmId == temp.alarmId)
						&& (entityType.equals(temp.entityType)) && (entityOid
						.equals(temp.entityOid)));
			}
			return false;
		}

		public String toString() {
			return entityType + "." + entityOid + "." + alarmId;
		}
	}
}
