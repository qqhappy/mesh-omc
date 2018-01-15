package com.xinwei.minas.server.mcbts.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.xinwei.nms.common.util.RollingFileLogger;

/**
 * 记录邻接表规则验证结果的日志工具类
 * @author zhuxiaozhan
 *
 */
public class NeighborValidatorLogUtill {
	private static final NeighborValidatorLogUtill instance = new NeighborValidatorLogUtill();
	
	private String logPath = "log/validator" ;
	
	// 是否运行记录日志
	private boolean enabled = true;
	
	private NeighborValidatorLogUtill() {
		
	}
	
	public static NeighborValidatorLogUtill getInstance() {
		return instance;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * 记录日志内容
	 * @param logContent
	 */
	public void writeLog(String logContent) {
		getLogger().log(logContent, null, 0, 0);
	}
	
	public RollingFileLogger getLogger() {
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy_MM_dd'T'HH_mm_ss");
		String dateTime = sf.format(date);
		RollingFileLogger logger = new RollingFileLogger(logPath + File.separator + "neighborValidatorResult(" + dateTime + ").log", true,
				5 * 1024 * 1024, 30);
		logger.setEnabled(enabled);
		return logger;
	}
	
	public static void main(String args[]) {
		String head = "基站[btsid]的邻接表验证结果.\n" + "告警：生成告警信息!";
		getInstance().writeLog(head);
	}
}
