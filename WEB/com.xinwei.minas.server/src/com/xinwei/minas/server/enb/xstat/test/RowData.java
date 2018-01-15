/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-3	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.test;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 行数�?
 * 
 * @author fanhaoyu
 * 
 */

public class RowData {

	private int rowIndex;

	private Map<String, String> cellMap;

	public RowData(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public synchronized void addCell(String columnName, String cellValue) {
		if (cellMap == null) {
			cellMap = new HashMap<String, String>();
		}
		cellMap.put(columnName, cellValue);
	}

	public String getColumnValue(String columnName) {
		if (cellMap == null)
			return null;
		return cellMap.get(columnName);
	}

	public int getRowIndex() {
		return rowIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cellMap == null) ? 0 : cellMap.hashCode());
		result = prime * result + rowIndex;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RowData other = (RowData) obj;
		if (cellMap == null) {
			if (other.cellMap != null)
				return false;
		} else if (!cellMap.equals(other.cellMap))
			return false;
		if (rowIndex != other.rowIndex)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RowData [rowIndex=" + rowIndex + ", cellMap=" + cellMap + "]";
	}
}
