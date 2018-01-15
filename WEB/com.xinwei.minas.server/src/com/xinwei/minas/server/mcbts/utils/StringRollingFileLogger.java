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
 * 类简要描述
 * 
 * <p>
 * 类详细描述
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
	 * 构造函数。
	 * 
	 * @param filename
	 *            文件名。可以包含路径，如果路径不存在，则创建路径。
	 * @param append
	 *            是否采用追加的方式打开文件。true使用追加方式；false不是用追加方式。
	 * @param maxFileSize
	 *            文件大小上限，单位是byte。
	 * @param maxBackupIndex
	 *            备份文件数目上限。
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
	 * 设置记录开关。当开关打开后才记录消息，否则不记录。
	 * 
	 * @param b
	 *            true表示启用，false表示禁用。
	 */
	public void setEnabled(boolean b) {
		enabled = b;
	}

	/**
	 * 判断该记录器是否已经启用。
	 * 
	 * @return 若启用则返回true，否则返回false。
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * 设置文件大小上限。
	 * 
	 * @param maxFileSize
	 *            文件大小上限，单位是字节
	 */
	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	/**
	 * 获取文件大小上限。
	 * 
	 * @return 文件大小上限。
	 * 
	 */
	public long getMaxFileSize() {
		return maxFileSize;
	}

	/**
	 * 设置备份文件数目上限。当达到这个上限后，将循环覆盖文件。
	 * 
	 * @param maxBackupIndex
	 *            备份文件数目上限。
	 * 
	 */
	public void setMaxBackupIndex(int maxBackupIndex) {
		this.maxBackupIndex = maxBackupIndex;
	}

	/**
	 * 获取备份文件数目上限。
	 * 
	 * @return 备份文件数目上限。
	 * 
	 */
	public int getMaxBackupIndex() {
		return maxBackupIndex;
	}

	/**
	 * 关闭该RollingFileLogger。把该RollingFileLogger打开的文件关掉
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
	 * 向文件中输出信息。每一行的开头会附加时间信息，格式为yy-mm-dd hh:mm:ss。
	 * 
	 * @param header
	 *            头信息，是一些描述文字。
	 * @param msg
	 *            消息。
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
