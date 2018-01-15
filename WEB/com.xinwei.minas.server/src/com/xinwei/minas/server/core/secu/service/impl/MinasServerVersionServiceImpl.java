/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-27	| fangping 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.service.impl;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.secu.MinasServerVersion;
import com.xinwei.minas.server.core.secu.service.MinasServerVersionService;
import com.xinwei.omp.core.utils.DateUtils;

/**
 * 
 * 类简要描述
 * 
 * @author fangping
 * 
 */

public class MinasServerVersionServiceImpl implements MinasServerVersionService {
	Log log = LogFactory.getLog(MinasServerVersionServiceImpl.class);

	public MinasServerVersion getMinasServerVersion() throws RemoteException,
			Exception {
		// FileUtil读文件version
		MinasServerVersion entry = new MinasServerVersion();
		File file = new File("etc/version");
		if (!file.exists())
			return null;

		String minasVersion = "";
		String minasReleaseDate = "";
		try {
			List<String> linesList = FileUtils.readLines(file);
			for (String string : linesList) {
				if (string.contains("=")) {
					String[] splitLineList = string.split("=");
					if (splitLineList[0].equals("minas.releaseDate")) {
						minasReleaseDate = splitLineList[1] == null ? ""
								: splitLineList[1];
					}
					if (splitLineList[0].equals("minas.version")) {
						minasVersion = splitLineList[1] == null ? ""
								: splitLineList[1];
					}
				}
			}

			entry.setVersion(minasVersion);
			SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			if (StringUtils.isNotBlank(minasReleaseDate)) {
				Date date = s.parse(minasReleaseDate);
				entry.setBuildTime(date);
			}
		} catch (IOException e) {
			log.error(e);
		}
		return entry;
	}
}
