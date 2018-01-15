/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-17	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.Simulcast;

/**
 * 
 * ͬ����Դ�������ӿ�
 * 
 * @author tiance
 * 
 */

public interface SimulcastManageService {
	/**
	 * ��ȡͬ����Դ�б�
	 * 
	 * @return
	 */
	public List<Simulcast> queryAll();

	/**
	 * ���ݲ�ѯ����ID�µ�����ͬ����Դ
	 * 
	 * @param districtId
	 * @return
	 */
	public List<Simulcast> queryByDistrictId(long districtId);

	/**
	 * �������޸�ͬ����Դ
	 * 
	 * @param simulcast
	 */
	public void saveOrUpdate(Simulcast simulcast);

	/**
	 * ����ͬ����Դ�Ļ�վ��·��ϢΪδͬ��
	 */
	public void setSimulMcBtsLinkUnSync();

	/**
	 * ɾ��ͬ����Դ
	 * 
	 * @param simulcast
	 */
	public void delete(Simulcast simulcast);

	/**
	 * ��ѯͬ����״̬
	 * 
	 * @return
	 */
	public boolean[] querySyncStatus();

	/**
	 * ��ͬ����Դ����ͬ��
	 * <p>
	 * toSync[0]Ϊ��վ��·��Ϣ,toSync[1]Ϊͬ����Դ��Ϣ
	 * </p>
	 * <p>
	 * Ŀǰֻ֧��ͬ����Դ��Ϣ,֮����ʵ�ֻ�վ��·��Ϣ��ͬ��
	 * </p>
	 * 
	 * 
	 * @param toSync
	 *            : Ϊtrueʱͬ��,false��ͬ��
	 * @throws Exception
	 *             : ����δ�ɹ�ͬ����SAG ID�б�
	 */
	public void sync(boolean[] toSync) throws Exception;
}
