/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-6	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsTemplate;

/**
 * 
 * ��վģ������Dao
 * 
 * 
 * @author tiance
 * 
 */

public interface McBtsTemplateManageDao {

	/**
	 * ��ȡ���л�վͬ����ģ��
	 * 
	 * @return ��վģ���б�
	 */
	public List<McBtsTemplate> queryAll();

	/**
	 * ����һ��ģ��ID,����һ�����õı���ģ��ID
	 * 
	 * @param referId
	 * @return
	 */
	public Long applyNewId(Long referId, McBtsTemplate temp);

	/**
	 * ��mcbts_template����һ��ģ��
	 * 
	 * @param temp
	 */
	public void insert(McBtsTemplate temp);

	/**
	 * �޸�mcbts_template�еļ�¼
	 * 
	 * @param temp
	 */
	public void update(McBtsTemplate temp);

	/**
	 * ��mcbts_templateɾ��һ��ģ��
	 * 
	 * @param moId
	 */
	public void delete(long moId);

	/**
	 * ͨ��moId��ѯһ��ģ��
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsTemplate queryByMoId(Long moId);

	/**
	 * ���ݱ�����moId(templateId),��ȡ�����
	 * 
	 * @param templateId
	 * @param table
	 * @return
	 */
	public List<Map<String, Object>> queryFromTable(long templateId,
			String table);

	/**
	 * ���ݱ���,moId����ָ�������ݼ�
	 * 
	 * @param moId
	 * @param table
	 * @param toInsert
	 */
	public void insertIntoTable(long moId, String table,
			List<Map<String, Object>> toInsert) throws Exception;

	/**
	 * ����ͬ��ģ�����ݸ���վ
	 * 
	 * @param mcbts
	 * @param table
	 * @param list
	 */
	public void batchInsertIntoTable(McBts[] mcbts,
			String table, List<Map<String, Object>> list) throws Exception;

	/**
	 * ���ݱ���,ɾ��ָ��moId������
	 * 
	 * @param moId
	 */
	public void deleteFromTable(long moId, String table) throws Exception;

}
