package com.xinwei.lte.web.lte.model;

import java.util.HashMap;
import java.util.Map;

public class WhiteAppName {
	
	private String whiteAppName;
	
	private String comment;
	public WhiteAppName(){
		
	}
	public WhiteAppName(Map data) {
		setWhiteAppName((String)data.get("whiteAppName"));
		setComment((String)data.get("comment"));
	}
	
	public Map<String, Object> toMapData() {
		Map<String, Object> data = new HashMap();
		data.put("whiteAppName", getWhiteAppName());
		data.put("comment", getComment());
		return data;
	}
	public String getWhiteAppName() {
		return whiteAppName;
	}

	public void setWhiteAppName(String whiteAppName) {
		this.whiteAppName = whiteAppName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	
	

}
