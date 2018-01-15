/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Ref Ԫ���ݶ��� ģ��
 * 
 * @author chenjunhua
 * 
 */

public class XMetaRef implements java.io.Serializable {

	// ���õı�
	private String refTable;

	// �����ֶ�
	private String keyColumn;

	// �����ֶ�
	private String descColumn;

	// ��������
	private String whereClause;

	public static List<XMetaRef> parseRef(List<String> refDefTextList) {
		List<XMetaRef> metaRefList = new LinkedList();
		for (String refDefText : refDefTextList) {
			XMetaRef metaRef = parseRef(refDefText);
			metaRefList.add(metaRef);
		}
		return metaRefList;
	}

	public static XMetaRef parseRef(String refDefText) {
		XMetaRef metaRef = new XMetaRef();
		String regex = "^ *table *= *(\\w+?) +key *= *(\\w+?) +desc *= *(\\w+?)(?: +where *= *(.+))? *$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(refDefText);
		if (m.find()) {
			int count = m.groupCount();
			for (int i = 1; i <= count; i++) {
				String s = m.group(i);
				switch (i) {
				case 1:
					metaRef.setRefTable(s);
					break;
				case 2:
					metaRef.setKeyColumn(s);
					break;
				case 3:
					metaRef.setDescColumn(s);
					break;
				case 4:
					metaRef.setWhereClause(s);
					break;
				}

			}
		}
		return metaRef;
	}

	public static void main(String[] args) {
		String refDefText = " table  = T_RACK  key=  u8RackNO  desc =  s8RackName where = a=1 and b='1'";
		XMetaRef.parseRef(refDefText);
	}

	public String getRefTable() {
		return refTable;
	}

	public void setRefTable(String refTable) {
		this.refTable = refTable;
	}

	public String getKeyColumn() {
		return keyColumn;
	}

	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}

	public String getDescColumn() {
		return descColumn;
	}

	public void setDescColumn(String descColumn) {
		this.descColumn = descColumn;
	}

	public String getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

}
