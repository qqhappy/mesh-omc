package com.xinwei.minas.server.enb.facade;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.xinwei.minas.enb.core.facade.EnbBlackBoxFileFacade;
import com.xinwei.minas.enb.core.model.EnbBlackBoxFileCondition;
import com.xinwei.minas.enb.core.model.EnbBlackBoxFileModel;
import com.xinwei.minas.server.enb.service.EnbAlarmService;
import com.xinwei.minas.server.enb.service.EnbBlackBoxFileService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.PagingData;
@SuppressWarnings("serial")
public class EnbBlackBoxFileFacadeImpl extends UnicastRemoteObject implements
		EnbBlackBoxFileFacade {


	protected EnbBlackBoxFileFacadeImpl() throws RemoteException {
		super();
	}

    public EnbBlackBoxFileService getEnbBlackBoxFileService(){
    	return AppContext.getCtx().getBean(EnbBlackBoxFileService.class);
    }

	@Override
	public PagingData<EnbBlackBoxFileModel> getBlackBoxFile(
			EnbBlackBoxFileCondition enbBlackBoxFileCondition) throws Exception {
		return getEnbBlackBoxFileService().getBlackBoxFile(enbBlackBoxFileCondition);
	}

	@Override
	//获取复位原因
	public Map<String, String> getAllResetReason() throws Exception {
		return getEnbBlackBoxFileService().getAllResetReason();
	}

	@Override
	public byte[] getBytesByFileName(String name) {
		return getEnbBlackBoxFileService().getBytesByFileName(name);
	}

	@Override
	public byte[] getZipFile(String name) {
		return getEnbBlackBoxFileService().getZipFile(name);
	}
 
	

}
