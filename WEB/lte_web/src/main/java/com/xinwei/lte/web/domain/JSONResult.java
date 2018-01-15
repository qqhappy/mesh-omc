package com.xinwei.lte.web.domain;

import java.net.URL;

/**
 * 异步调用返回执行信息
 */
public class JSONResult {
	
	private boolean flag = true;
	// 详细信息
	private String detailInfo;
	// 执行失败的错误码
	private String errorCode="0";
	// 执行失败的错误信息
	private String errorMsg = "暂无相关数据";
	
	//操作时间
	private String operateDate;
	
	//操作员
	private String operator;
	
	//文件路径
	private String filePath;
	
	public String getFilePath()
	{
		return filePath;
	}
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	private int uploadStatus = 0;
		
	public int getUploadStatus()
	{
		return uploadStatus;
	}
	public void setUploadStatus(int uploadStatus)
	{
		this.uploadStatus = uploadStatus;
	}
	public String getOperator()
	{
		return operator;
	}
	public void setOperator(String operator)
	{
		this.operator = operator;
	}
	public String getOperateDate()
	{
		return operateDate;
	}
	public void setOperateDate(String operateDate)
	{
		this.operateDate = operateDate;
	}
	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	/**
	 * @return the detailInfo
	 */
	public String getDetailInfo() {
		return detailInfo;
	}
	/**
	 * @param detailInfo the detailInfo to set
	 */
	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}
	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}
	/**
	 * @param errorMsg the errorMsg to set
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public static void main(String[] args)
	{
		URL url = JSONResult.class.getResource("");
		URL obj = JSONResult.class.getClassLoader().getResource("");
		
		String tempDir = "/target/classes/com/xinwei/feeAndProduct/domain/";
		String dir = System.getProperty("user.dir");
		
		String path = dir + tempDir;
		System.out.println(path);
		System.out.println(System.getProperty("file.separator"));
		
		
		System.out.println(url);
		System.out.println(obj);
	}
}
