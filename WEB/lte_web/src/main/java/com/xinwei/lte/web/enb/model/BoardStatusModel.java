/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-30	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model;

/**
 * 
 * 单板状态模型
 * 
 * @author fanhaoyu
 * 
 */

public class BoardStatusModel {

	private String boardName;

	private int status;

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
