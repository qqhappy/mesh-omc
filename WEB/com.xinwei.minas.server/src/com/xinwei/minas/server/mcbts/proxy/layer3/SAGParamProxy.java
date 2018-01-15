package com.xinwei.minas.server.mcbts.proxy.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfBackupSag;
import com.xinwei.omp.core.model.biz.GenericBizData;
/**
 * ����SAGҵ��Э��������
 * 
 * @author yinbinqiang
 *
 */
public interface SAGParamProxy {
	/**
	 * ��ѯ��Ԫҵ������
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfBackupSag query(Long moId, TConfBackupSag backupSag) throws Exception;
	
	/**
	 * ������Ԫҵ������
	 * 
	 * @param moId
	 * @param weakVoiceFault
	 * @throws Exception
	 */
	public void config(Long moId, GenericBizData bizData) throws Exception;
}
