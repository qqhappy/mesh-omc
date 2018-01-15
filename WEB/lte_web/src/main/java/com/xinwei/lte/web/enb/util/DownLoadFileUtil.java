package com.xinwei.lte.web.enb.util;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

/**
 * 下载工具类
 * 
 * @author sunzhangbin
 * 
 */
public class DownLoadFileUtil {

	/**
	 * 下载文件的方法
	 * 
	 * @param fileName
	 *            文件名字
	 * @param bytes
	 *            文件流的byte数组
	 */

	public void downLoadFile(String fileName, byte[] bytes) {
		ServletOutputStream outputstream = null;
		try {
			byte[] nameByte = null;
			HttpServletRequest httpRequest = ServletActionContext.getRequest();
			if (StringUtils.isEmpty(httpRequest.getHeader("User-Agent"))
					|| httpRequest.getHeader("User-Agent").contains("MSIE")) {
				nameByte = fileName.trim().getBytes();
			} else if (httpRequest.getHeader("User-Agent").contains("Firefox")
					|| httpRequest.getHeader("User-Agent").contains("Chrome")
					|| httpRequest.getHeader("User-Agent").contains("Opera")
					|| httpRequest.getHeader("User-Agent").contains("Safari")) {
				nameByte = fileName.trim().getBytes("UTF-8");
			} else {
				nameByte = fileName.trim().getBytes();
			}

			fileName = new String(nameByte, "ISO8859-1");
			HttpServletResponse response = ServletActionContext.getResponse();

			outputstream = response.getOutputStream();
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-disposition", "attachment;filename=\""
					+ fileName + "\"");
			outputstream.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (outputstream != null) {
					outputstream.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
