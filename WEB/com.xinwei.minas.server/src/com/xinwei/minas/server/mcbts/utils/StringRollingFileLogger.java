/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-13	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.xinwei.nms.common.util.ByteArrayUtil;
import com.xinwei.nms.common.util.CountingWriter;
import com.xinwei.nms.common.util.RollingFileLogger;

/**
 * 
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����
 * </p>
 * 
 * @author chenjunhua
 * 
 */

public class StringRollingFileLogger {

	private final Logger log = Logger.getLogger(RollingFileLogger.class);

	/**
	 * The default maximum file size is 1MB.
	 */
	protected long maxFileSize = 1 * 1024 * 1024;

	/**
	 * There is one backup file by default.
	 */
	protected int maxBackupIndex = 1;

	protected CountingWriter out;

	private boolean enabled = false;

	private String filename;

	private boolean append = true;

	private DateFormat dataFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss SSS");

	/**
	 * ���캯����
	 * 
	 * @param filename
	 *            �ļ��������԰���·�������·�������ڣ��򴴽�·����
	 * @param append
	 *            �Ƿ����׷�ӵķ�ʽ���ļ���trueʹ��׷�ӷ�ʽ��false������׷�ӷ�ʽ��
	 * @param maxFileSize
	 *            �ļ���С���ޣ���λ��byte��
	 * @param maxBackupIndex
	 *            �����ļ���Ŀ���ޡ�
	 */
	public StringRollingFileLogger(String filename, boolean append,
			long maxFileSize, int maxBackupIndex) {
		this.filename = filename;
		this.append = append;
		this.maxFileSize = maxFileSize;
		this.maxBackupIndex = maxBackupIndex;
		recreateWriter();
	}

	/**
	 * ���ü�¼���ء������ش򿪺�ż�¼��Ϣ�����򲻼�¼��
	 * 
	 * @param b
	 *            true��ʾ���ã�false��ʾ���á�
	 */
	public void setEnabled(boolean b) {
		enabled = b;
	}

	/**
	 * �жϸü�¼���Ƿ��Ѿ����á�
	 * 
	 * @return �������򷵻�true�����򷵻�false��
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * �����ļ���С���ޡ�
	 * 
	 * @param maxFileSize
	 *            �ļ���С���ޣ���λ���ֽ�
	 */
	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	/**
	 * ��ȡ�ļ���С���ޡ�
	 * 
	 * @return �ļ���С���ޡ�
	 * 
	 */
	public long getMaxFileSize() {
		return maxFileSize;
	}

	/**
	 * ���ñ����ļ���Ŀ���ޡ����ﵽ������޺󣬽�ѭ�������ļ���
	 * 
	 * @param maxBackupIndex
	 *            �����ļ���Ŀ���ޡ�
	 * 
	 */
	public void setMaxBackupIndex(int maxBackupIndex) {
		this.maxBackupIndex = maxBackupIndex;
	}

	/**
	 * ��ȡ�����ļ���Ŀ���ޡ�
	 * 
	 * @return �����ļ���Ŀ���ޡ�
	 * 
	 */
	public int getMaxBackupIndex() {
		return maxBackupIndex;
	}

	/**
	 * �رո�RollingFileLogger���Ѹ�RollingFileLogger�򿪵��ļ��ص�
	 */
	public synchronized void close() {
		try {
			if (out != null) {
				out.close();
				out = null;
			}
		} catch (IOException ex) {
			log.error("failed to close file,filename=" + filename, ex);
		}
	}

	private void recreateWriter() {
		try {
			if (out != null) {
				out.close();
				out = null;
			}
			File file = new File(filename);
			File parent = file.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}
			this.out = new CountingWriter(new FileWriter(file, append));
			if (append) {
				out.setCount(file.length());
			}
		} catch (IOException ex) {
			log.error("failed to create writer,filename=" + filename, ex);
		}

	}

	/**
	 * ���ļ��������Ϣ��ÿһ�еĿ�ͷ�ḽ��ʱ����Ϣ����ʽΪyy-mm-dd hh:mm:ss��
	 * 
	 * @param header
	 *            ͷ��Ϣ����һЩ�������֡�
	 * @param msg
	 *            ��Ϣ��
	 */
	public synchronized void log(String header, String message) {
		if (!enabled) {
			return;
		}
		try {
			/*
			 * StringBuffer buf=new StringBuffer();
			 * buf.append(getCurrentDateTime()); buf.append(' ');
			 * buf.append(header); buf.append(' ');
			 * buf.append(ByteArrayUtil.toSeparatedHexString(msg, offset,
			 * length)); out.write(buf.toString());
			 */
			out.write(getCurrentDateTime());
			out.write(' ');
			out.write(header);
			out.write(' ');
			out.write(message);
			out.newLine();
			out.flush();

			if ((filename != null) && (out.getCount() >= maxFileSize)) {
				this.rollOver();
				recreateWriter();
			}
		} catch (IOException ex) {
			log.error("failed to write string", ex);
		}
	}

	private String getCurrentDateTime() {
		return dataFormat.format(new Date(System.currentTimeMillis()));
	}

	private void rollOver() {
		File target;
		File file;

		// If maxBackups <= 0, then there is no file renaming to be done.
		if (maxBackupIndex > 0) {
			// Delete the oldest file, to keep Windows happy.
			file = new File(filename + '.' + maxBackupIndex);
			if (file.exists())
				file.delete();

			// Map {(maxBackupIndex - 1), ..., 2, 1} to {maxBackupIndex, ..., 3,
			// 2}
			for (int i = maxBackupIndex - 1; i >= 1; i--) {
				file = new File(filename + "." + i);
				if (file.exists()) {
					target = new File(filename + '.' + (i + 1));
					file.renameTo(target);
				}
			}

			// Rename fileName to fileName.1
			target = new File(filename + "." + 1);

			try {
				out.close(); // keep windows happy.
			} catch (IOException ex) {
				log.error("fail to close file " + filename, ex);
			}

			file = new File(filename);
			file.renameTo(target);
		}
	}
}
