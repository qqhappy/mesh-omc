package com.xinwei.lte.web.enb.action.simplify;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 页面跳转action，用于简单的页面跳转
 * @author zhangqiang
 *
 */
public class HtmlDispatcherAction extends ActionSupport {
	
	/**
	 * 页面文件名
	 */
	private String htmlFileName;
	
	/**
	 * 跟踪区码列表中的当前页号
	 */
	private int currentPage;
	
	/**
	 * 页面跳转
	 * @return
	 */
	public String htmlDispactcher(){
		return htmlFileName;
	}

	public String getHtmlFileName() {
		return htmlFileName;
	}

	public void setHtmlFileName(String htmlFileName) {
		this.htmlFileName = htmlFileName;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}		
}
