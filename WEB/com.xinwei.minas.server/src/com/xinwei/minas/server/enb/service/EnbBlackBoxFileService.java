package com.xinwei.minas.server.enb.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.xinwei.minas.enb.core.model.EnbBlackBoxFileCondition;
import com.xinwei.minas.enb.core.model.EnbBlackBoxFileModel;
import com.xinwei.omp.core.model.biz.PagingData;

public interface EnbBlackBoxFileService {

	public Map<String, String> getAllResetReason();
	public PagingData<EnbBlackBoxFileModel> getBlackBoxFile(
			EnbBlackBoxFileCondition enbBlackBoxFileCondition);
	//�����ļ�����ȡbyte[]
	public byte[] getBytesByFileName(String name);
	//����name��ȡFile[]
	public List<File> getFileListByFileName(String name);
	
	public byte[] getZipFile(String name);

}
