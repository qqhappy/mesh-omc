package com.xinwei.lte.web.enb.model;

import java.util.ArrayList;
import java.util.List;
/**
 * 操作员日志模型，用于action向页面传递查询出的数据
 * 
 * @author zhangqiang
 *
 */
public class LogListModel {
	/**
	 * 当前页号
	 */
	private int currentPage;
	
	/**
	 * 总页数
	 */
	private int totalPage;
	
	/**
	 * 查询出的日志列表
	 */
	private List<SystemLogModel> logModelList = new ArrayList<SystemLogModel>();

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<SystemLogModel> getLogModelList() {
		return logModelList;
	}

	public void setLogModelList(List<SystemLogModel> logModelList) {
		this.logModelList = logModelList;
	}



	
}
