/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wutka.jox.JOXBeanInputStream;
import com.xinwei.omp.core.model.meta.XCollection;
/**
 * 
 * XCollectionManager
 * 
 * @author chenjunhua
 * 
 */

public class XCollectionManager {
	private Log log = LogFactory.getLog(XCollectionManager.class);

	private static final XCollectionManager instance = new XCollectionManager();

	private XCollectionManager() {

	}

	public static XCollectionManager getInstance() {
		return instance;
	}

	public XCollection initialize(URL url) {
		try {
			return this.initialize(url.openStream());
		} catch (IOException e) {
			return null;
		}
	}
	
	
	public XCollection initialize(InputStream inputStream) {
		JOXBeanInputStream joxIn = null;
		try {
			joxIn = new JOXBeanInputStream(inputStream);
			XCollection collection = (XCollection) joxIn
					.readObject(XCollection.class);
			return collection;
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (joxIn != null) {
				try {
					joxIn.close();
				} catch (IOException e) {

				}
			}
		}
		return null;
	}
}
