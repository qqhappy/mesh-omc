package com.xinwei.minas.enb.core.facade;

import java.io.File;
import java.rmi.Remote;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.xinwei.minas.enb.core.model.EnbBlackBoxFileCondition;
import com.xinwei.minas.enb.core.model.EnbBlackBoxFileModel;
import com.xinwei.omp.core.model.biz.PagingData;

public interface EnbBlackBoxFileFacade extends Remote {
	
	
	//获取复位原因
	public Map<String, String> getAllResetReason() throws Exception;
    //获取分页信息
	public PagingData<EnbBlackBoxFileModel> getBlackBoxFile(EnbBlackBoxFileCondition enbBlackBoxFileCondition) throws Exception;
	//根据文件名获取byte[]
	public byte[] getBytesByFileName(String name)throws Exception;
	
	public byte[] getZipFile(String name)throws Exception;
}
