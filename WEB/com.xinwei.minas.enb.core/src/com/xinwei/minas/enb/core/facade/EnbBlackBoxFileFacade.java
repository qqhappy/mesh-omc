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
	
	
	//��ȡ��λԭ��
	public Map<String, String> getAllResetReason() throws Exception;
    //��ȡ��ҳ��Ϣ
	public PagingData<EnbBlackBoxFileModel> getBlackBoxFile(EnbBlackBoxFileCondition enbBlackBoxFileCondition) throws Exception;
	//�����ļ�����ȡbyte[]
	public byte[] getBytesByFileName(String name)throws Exception;
	
	public byte[] getZipFile(String name)throws Exception;
}
