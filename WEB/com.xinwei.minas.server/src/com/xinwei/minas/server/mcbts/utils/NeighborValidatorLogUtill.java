package com.xinwei.minas.server.mcbts.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.xinwei.nms.common.util.RollingFileLogger;

/**
 * ��¼�ڽӱ������֤�������־������
 * @author zhuxiaozhan
 *
 */
public class NeighborValidatorLogUtill {
	private static final NeighborValidatorLogUtill instance = new NeighborValidatorLogUtill();
	
	private String logPath = "log/validator" ;
	
	// �Ƿ����м�¼��־
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
	 * ��¼��־����
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
		String head = "��վ[btsid]���ڽӱ���֤���.\n" + "�澯�����ɸ澯��Ϣ!";
		getInstance().writeLog(head);
	}
}
