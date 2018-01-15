/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-22	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.common;

import java.io.Serializable;

/**
 * 
 * ApacheµÄStat·â×°
 * 
 * @author fanhaoyu
 * 
 */

public class ZkNodeStat implements Serializable, Cloneable {

	private long czxid;
	private long mzxid;
	private long ctime;
	private long mtime;
	private int version;
	private int cversion;
	private int aversion;
	private long ephemeralOwner;
	private int dataLength;
	private int numChildren;
	private long pzxid;

	public ZkNodeStat() {
	}

	public ZkNodeStat(long czxid, long mzxid, long ctime, long mtime,
			int version, int cversion, int aversion, long ephemeralOwner,
			int dataLength, int numChildren, long pzxid) {
		this.czxid = czxid;
		this.mzxid = mzxid;
		this.ctime = ctime;
		this.mtime = mtime;
		this.version = version;
		this.cversion = cversion;
		this.aversion = aversion;
		this.ephemeralOwner = ephemeralOwner;
		this.dataLength = dataLength;
		this.numChildren = numChildren;
		this.pzxid = pzxid;
	}

	public long getCzxid() {
		return czxid;
	}

	public void setCzxid(long czxid) {
		this.czxid = czxid;
	}

	public long getMzxid() {
		return mzxid;
	}

	public void setMzxid(long mzxid) {
		this.mzxid = mzxid;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public long getMtime() {
		return mtime;
	}

	public void setMtime(long mtime) {
		this.mtime = mtime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getCversion() {
		return cversion;
	}

	public void setCversion(int cversion) {
		this.cversion = cversion;
	}

	public int getAversion() {
		return aversion;
	}

	public void setAversion(int aversion) {
		this.aversion = aversion;
	}

	public long getEphemeralOwner() {
		return ephemeralOwner;
	}

	public void setEphemeralOwner(long ephemeralOwner) {
		this.ephemeralOwner = ephemeralOwner;
	}

	public int getDataLength() {
		return dataLength;
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	public int getNumChildren() {
		return numChildren;
	}

	public void setNumChildren(int numChildren) {
		this.numChildren = numChildren;
	}

	public long getPzxid() {
		return pzxid;
	}

	public void setPzxid(long pzxid) {
		this.pzxid = pzxid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + aversion;
		result = prime * result + (int) (ctime ^ (ctime >>> 32));
		result = prime * result + cversion;
		result = prime * result + (int) (czxid ^ (czxid >>> 32));
		result = prime * result + dataLength;
		result = prime * result
				+ (int) (ephemeralOwner ^ (ephemeralOwner >>> 32));
		result = prime * result + (int) (mtime ^ (mtime >>> 32));
		result = prime * result + (int) (mzxid ^ (mzxid >>> 32));
		result = prime * result + numChildren;
		result = prime * result + (int) (pzxid ^ (pzxid >>> 32));
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ZkNodeStat))
			return false;
		ZkNodeStat other = (ZkNodeStat) obj;
		if (aversion != other.aversion)
			return false;
		if (ctime != other.ctime)
			return false;
		if (cversion != other.cversion)
			return false;
		if (czxid != other.czxid)
			return false;
		if (dataLength != other.dataLength)
			return false;
		if (ephemeralOwner != other.ephemeralOwner)
			return false;
		if (mtime != other.mtime)
			return false;
		if (mzxid != other.mzxid)
			return false;
		if (numChildren != other.numChildren)
			return false;
		if (pzxid != other.pzxid)
			return false;
		if (version != other.version)
			return false;
		return true;
	}
	
	@Override
	public ZkNodeStat clone() {
		try {
			return (ZkNodeStat)super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
