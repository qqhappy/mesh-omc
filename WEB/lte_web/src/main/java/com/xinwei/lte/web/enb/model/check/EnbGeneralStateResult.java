/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-10-31	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model.check;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.lte.web.enb.model.BoardStatusModel;
import com.xinwei.lte.web.enb.model.CellStatusModel;

/**
 * 
 * 基站总体状态检查结果
 * 
 * @author Administrator
 * 
 */

public class EnbGeneralStateResult extends DetailCheckResult {
	
	// 基站与网管连接状态
	private int connectionStatus;
	
	// 单板状态
	private List<BoardStatusModel> boradsStatus;
	
	// 小区状态
	private List<CellStatusModel> cellsStatus;
	
	// SCTP偶联状态
	private int sctpStatus;
	
	// 以太网口状态
	private int ethernetPortStatus;
	
	@Override
	public String getCheckName() throws Exception {
		return "基站总体状态检查结果";
	}
	
	@Override
	public int check() throws Exception {
		int checkResult = EnbCheckConstants.NORMAL;
		// 检查基站与网管连接状态
		if (EnbCheckConstants.NORMAL != connectionStatus) {
			return EnbCheckConstants.NOT_NORMAL;
		}
		
		// 检查单板状态
		if (null != boradsStatus) {
			for (BoardStatusModel boardStatusModel : boradsStatus) {
				if (EnbCheckConstants.NORMAL != boardStatusModel.getStatus()) {
					return EnbCheckConstants.NOT_NORMAL;
				}
			}
		}
		
		// 检查小区状态
		if (null != cellsStatus) {
			for (CellStatusModel cellStatusModel : cellsStatus) {
				if (EnbCheckConstants.NORMAL != cellStatusModel.getStatus()) {
					return EnbCheckConstants.NOT_NORMAL;
				}
			}
		}
		
		// 检查SCTP链路状态
		if (EnbCheckConstants.NORMAL != sctpStatus) {
			return EnbCheckConstants.NOT_NORMAL;
		}
		
		// 检查以太网口状态
		if (EnbCheckConstants.NORMAL != ethernetPortStatus) {
			return EnbCheckConstants.NOT_NORMAL;
		}
		return checkResult;
	}
	
	@Override
	public String checkDesc() throws Exception {
		StringBuilder sb = new StringBuilder();
		// 检查基站与网管连接状态
		if (EnbCheckConstants.NORMAL != connectionStatus) {
			sb.append("基站与网管连接状态故障.\n");
		}
		
		// 检查单板状态
		if (null != boradsStatus) {
			for (BoardStatusModel boardStatusModel : boradsStatus) {
				if (EnbCheckConstants.NORMAL != boardStatusModel.getStatus()) {
					sb.append("单板状态故障.\n");
					break;
				}
			}
		}
		
		// 检查小区状态
		if (null != cellsStatus) {
			for (CellStatusModel cellStatusModel : cellsStatus) {
				if (EnbCheckConstants.NORMAL != cellStatusModel.getStatus()) {
					sb.append("小区状态故障.\n");
					break;
				}
			}
		}
		
		// 检查SCTP链路状态
		if (EnbCheckConstants.NORMAL != sctpStatus) {
			sb.append("SCTP偶联状态故障.\n");
		}
		
		// 检查以太网口状态
		if (EnbCheckConstants.NORMAL != ethernetPortStatus) {
			sb.append("以太网口状态故障.\n");
		}
		return sb.toString();
	}
	
	@Override
	public List<EnbCheckForm> getCheckForm() throws Exception {
		List<EnbCheckForm> checkForms = new ArrayList<EnbCheckForm>();
		// 基站与网管连接状态
		if (EnbCheckConstants.NORMAL == connectionStatus) {
			addCheckForm(checkForms, "基站与网管连接状态", "正常", "正常",
					EnbCheckConstants.NORMAL);
		}
		else {
			addCheckForm(checkForms, "基站与网管连接状态", "不正常", "正常",
					EnbCheckConstants.NOT_NORMAL);
		}
		// 单板状态
		if (null != boradsStatus) {
			for (BoardStatusModel boardStatusModel : boradsStatus) {
				if (EnbCheckConstants.NORMAL == boardStatusModel.getStatus()) {
					addCheckForm(checkForms, boardStatusModel.getBoardName()
							+ "单板状态", "正常", "正常", EnbCheckConstants.NORMAL);
				}
				else {
					addCheckForm(checkForms, boardStatusModel.getBoardName()
							+ "单板状态", "不正常", "正常", EnbCheckConstants.NOT_NORMAL);
				}
			}
		}
		
		// 小区状态
		if (null != cellsStatus) {
			for (CellStatusModel cellStatusModel : cellsStatus) {
				if (EnbCheckConstants.NORMAL == cellStatusModel.getStatus()) {
					addCheckForm(checkForms, cellStatusModel.getCellId() + "("
							+ cellStatusModel.getCellName() + ")" + "小区状态",
							"正常", "正常", EnbCheckConstants.NORMAL);
				}
				else {
					addCheckForm(checkForms, cellStatusModel.getCellId() + "("
							+ cellStatusModel.getCellName() + ")" + "小区状态",
							"不正常", "正常", EnbCheckConstants.NOT_NORMAL);
				}
			}
		}
		
		// SCTP链路状态
		if (EnbCheckConstants.NORMAL == sctpStatus) {
			addCheckForm(checkForms, "S1链路状态", "正常", "正常",
					EnbCheckConstants.NORMAL);
		}
		else {
			addCheckForm(checkForms, "S1链路状态", "不正常", "正常",
					EnbCheckConstants.NOT_NORMAL);
		}
		
		// 以太网口状态
		if (EnbCheckConstants.NORMAL == ethernetPortStatus) {
			addCheckForm(checkForms, "以太网口状态", "正常", "正常",
					EnbCheckConstants.NORMAL);
		}
		else {
			addCheckForm(checkForms, "以太网口状态", "不正常", "正常",
					EnbCheckConstants.NOT_NORMAL);
		}
		return checkForms;
	}
	
	public int getConnectionStatus() {
		return connectionStatus;
	}
	
	public void setConnectionStatus(int connectionStatus) {
		this.connectionStatus = connectionStatus;
	}
	
	public List<BoardStatusModel> getBoradsStatus() {
		return boradsStatus;
	}
	
	public void setBoradsStatus(List<BoardStatusModel> boradsStatus) {
		this.boradsStatus = boradsStatus;
	}
	
	public List<CellStatusModel> getCellsStatus() {
		return cellsStatus;
	}
	
	public void setCellsStatus(List<CellStatusModel> cellsStatus) {
		this.cellsStatus = cellsStatus;
	}
	
	public int getSctpStatus() {
		return sctpStatus;
	}
	
	public void setSctpStatus(int sctpStatus) {
		this.sctpStatus = sctpStatus;
	}
	
	public int getEthernetPortStatus() {
		return ethernetPortStatus;
	}
	
	public void setEthernetPortStatus(int ethernetPortStatus) {
		this.ethernetPortStatus = ethernetPortStatus;
	}
}
