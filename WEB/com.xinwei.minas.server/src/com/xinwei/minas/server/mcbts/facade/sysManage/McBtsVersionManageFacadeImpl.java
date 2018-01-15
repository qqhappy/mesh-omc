/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-19	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.McBtsVersionManageFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsCodeDownloadTask;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersionHistory;
import com.xinwei.minas.server.mcbts.service.sysManage.McBtsVersionManageService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 
 * @author chenshaohua
 * 
 */

@SuppressWarnings("serial")
public class McBtsVersionManageFacadeImpl extends UnicastRemoteObject implements
		McBtsVersionManageFacade {

	private Log log = LogFactory.getLog(McBtsVersionManageFacadeImpl.class);

	private McBtsVersionManageService service;

	private SequenceService sequenceService;

	protected McBtsVersionManageFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(McBtsVersionManageService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public List<McBtsVersion> queryAll() throws RemoteException, Exception {
		return service.queryAll();
	}

	@Override
	public void delete(OperObject operObject, McBtsVersion mcBtsVersion)
			throws RemoteException, Exception {
		// ��û��ʹ����ɾ�����ݿ�
		service.delete(mcBtsVersion);
	}

	@Override
	public void saveOrUpdate(OperObject operObject, McBtsVersion mcbtsVersion)
			throws RemoteException, Exception {
		if (mcbtsVersion.getIdx() == null) {
			mcbtsVersion.setIdx(sequenceService.getNext());
		}
		try {
			service.saveOrUpdate(mcbtsVersion);
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * ���ػ�վ���
	 * 
	 * @param mo
	 * @param btsId
	 * @param mcBtsVersion
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public int download(OperObject operObject, Mo mo, Long btsId,
			McBtsVersion mcBtsVersion) throws RemoteException, Exception {
		return service.download(mo, btsId, mcBtsVersion);
	}

	/**
	 * ���ڻ�վ�����Ͳ�ѯ��վ�汾
	 * 
	 * @param btsType
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public List<McBtsVersion> queryByBtsType(Integer btsType)
			throws RemoteException, Exception {
		return service.queryByBtsType(btsType);
	}

	/**
	 * ���ڻ�վID��ѯ���������վ�İ汾���ؼ�¼
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public List<McBtsVersionHistory> queryDownloadHistory(Long btsId)
			throws RemoteException, Exception {
		return service.queryDownloadHistory(btsId);
	}

	/**
	 * ��ȡ���һ�������״̬(ʱ��,״̬)
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public McBtsCodeDownloadTask getLatestStatus(Long btsId)
			throws RemoteException, Exception {
		return service.getLatestStatus(btsId);
	}

	@Override
	public Map<Long, String> queryCurrentDownloadTasks()
			throws RemoteException, Exception {
		return service.queryCurrentDownloadTasks();
	}

	/**
	 * ɾ��ĳ����վ�µ��������ؼ�¼
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public int deleteAllHistory(Long btsId) throws RemoteException, Exception {
		return service.deleteAllHistory(btsId);
	}

	/**
	 * ɾ��ĳ����ʷ��¼
	 * 
	 * @param history
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public int deleteHistory(McBtsVersionHistory history)
			throws RemoteException, Exception {
		return service.deleteHistory(history);
	}

	/**
	 * ���վ������������
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public void upgrade(OperObject operObject, Long moId, Integer ho_type)
			throws RemoteException, Exception {
		service.upgrade(moId, ho_type);
	}

	@Override
	public void add(String fileName, byte[] fileContent)
			throws RemoteException, Exception {
		service.add(fileName, fileContent);
	}

	@Override
	public boolean deleteFile(String fileName) throws RemoteException,
			Exception {
		return service.deleteFile(fileName);
	}

	public void add(McBtsVersion mcBtsVersion) throws RemoteException,
			Exception {
		// ����idx
		if (mcBtsVersion.getIdx() == null) {
			mcBtsVersion.setIdx(sequenceService.getNext());
		}
		service.add(mcBtsVersion);
	}
}
