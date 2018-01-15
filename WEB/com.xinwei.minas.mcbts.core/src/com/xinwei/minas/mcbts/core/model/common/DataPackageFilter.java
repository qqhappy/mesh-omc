/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-10	| fangping 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.common;

import java.io.Serializable;

/**
 * 
 * 类简要描述 数据包过滤准则配置实体类
 * 
 * @author fangping
 * 
 */

public class DataPackageFilter implements Serializable {

	public static final int TYPE_NO_FILTER = 0;
	public static final int TYPE_PORT_FILTER = 1;
	public static final int TYPE_IP_FILTER = 2;
	public static final int TYPE_BOTH_FILTER = 3;

	// 记录索引
	private long idx;
	// IP
	private String ip;
	// 端口号
	private int port;
	// 过滤类型
	private int type;

	public DataPackageFilter() {
	}

	public DataPackageFilter(String ip, int port, int type) {
		this.ip = ip;
		this.port = port;
		this.type = type;
	}

	public long getIdx() {
		return idx;
	}

	public void setIdx(long idx) {
		this.idx = idx;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
