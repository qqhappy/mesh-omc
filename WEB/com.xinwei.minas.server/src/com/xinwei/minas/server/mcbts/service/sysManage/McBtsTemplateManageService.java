/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-6	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsOperation;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsOperation.Operation;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsTemplate;

/**
 * 
 * ��վģ�����ķ���ӿ�
 * 
 * 
 * @author tiance
 * 
 */

public interface McBtsTemplateManageService {
	/**
	 * ��ȡ���л�վͬ����ģ��
	 * 
	 * @return ��վģ���б�
	 */
	public List<McBtsTemplate> queryAll();

	/**
	 * ����һ��ģ��ID,����һ���µĿ��õ�ģ��ID
	 * 
	 * @param referId
	 * @param temp
	 * @return
	 */
	public Long applyNewId(Long referId, McBtsTemplate temp);

	/**
	 * �����ݿ��ÿ����������ģ�屸������
	 * 
	 * @param templateId
	 */
	public void generateTemplateBackup(long templateId);

	/**
	 * �����ݿ��ÿ������ɾ��ģ�屸������
	 * 
	 * @param templateId
	 */
	public void deleteTemplateBackup(long templateId);

	/**
	 * ͨ��moId��ѯһ��ģ��
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsTemplate queryByMoId(Long moId);

	/**
	 * ��ʼ����վ����
	 * 
	 * @param mcbts
	 * @param templateId
	 */
	public void initMcBtsData(McBts mcbts);

	/**
	 * ͬ��һ����վ
	 * 
	 * @param templateId
	 * @param oprs
	 * @param mcbts
	 */
	public void syncAll(long templateId, Operation[] oprs, McBts[] mcbts)
			throws Exception;

	/**
	 * ɾ����վ�ĳ�ʼ������
	 * 
	 * @param mcbts
	 */
	public void rollBackMcBtsData(long moId);

	/**
	 * ��û�վҵ��ģ��
	 * 
	 * @return
	 */
	public List<McBtsOperation> getMcbtsOperation();

	/**
	 * ��MoCache�����Mo
	 * 
	 * @param mo
	 */
	public void addToMoCache(Long moId);

	/**
	 * ��MoCache��ɾ��Mo
	 * 
	 * @param moId
	 */
	public void removeFromMoCache(Long moId);

	/**
	 * ����ģ������
	 * 
	 * @param template
	 */
	public void updateTemplate(McBtsTemplate template);

	/**
	 * ��ȡ��ģ���������ɾ��ҵ���ʱ��ִ�лָ�����
	 * 
	 * @param moId
	 * @param operations
	 *            Ҫ�ظ����ݵ�ҵ��
	 * @param isDel
	 *            �Ƿ���ɾ��ҵ��
	 */
	public void recover(Long moId, List<String> operations, boolean isDel);
}
