/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-20	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.alarm;

import java.util.List;

import com.xinwei.minas.core.model.alarm.AlarmDef;
import com.xinwei.minas.server.core.alarm.cache.AlarmDefCache;
import com.xinwei.minas.server.core.alarm.dao.AlarmDAO;

/**
 * 
 * Server Alarm Module 
 * 
 * @author chenjunhua
 * 
 */

public class AlarmModule {
	
	private static final AlarmModule instance = new AlarmModule();
	
	private AlarmModule() {		
	}
	
	public static AlarmModule getInstance() {
		return instance;
	}
	
	public void initialize(AlarmDAO alarmDAO) {
		// ³õÊ¼»¯¸æ¾¯»º´æ
		List<AlarmDef> alarmDefs = alarmDAO.queryAllAlarmDef();
		for (AlarmDef alarmDef : alarmDefs) {
			AlarmDefCache.getInstance().add(alarmDef);
		}
	}
}
