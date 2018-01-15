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
 * 基站模板管理的Dao
 * 
 * 
 * @author tiance
 * 
 */

public interface McBtsTemplateManageDao {

	/**
	 * 获取所有基站同步的模板
	 * 
	 * @return 基站模板列表
	 */
	public List<McBtsTemplate> queryAll();

	/**
	 * 基于一个模板ID,返回一个可用的备用模板ID
	 * 
	 * @param referId
	 * @return
	 */
	public Long applyNewId(Long referId, McBtsTemplate temp);

	/**
	 * 向mcbts_template插入一个模板
	 * 
	 * @param temp
	 */
	public void insert(McBtsTemplate temp);

	/**
	 * 修改mcbts_template中的记录
	 * 
	 * @param temp
	 */
	public void update(McBtsTemplate temp);

	/**
	 * 从mcbts_template删除一个模板
	 * 
	 * @param moId
	 */
	public void delete(long moId);

	/**
	 * 通过moId查询一个模板
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsTemplate queryByMoId(Long moId);

	/**
	 * 根据表名和moId(templateId),获取表对象
	 * 
	 * @param templateId
	 * @param table
	 * @return
	 */
	public List<Map<String, Object>> queryFromTable(long templateId,
			String table);

	/**
	 * 根据表名,moId插入指定的数据集
	 * 
	 * @param moId
	 * @param table
	 * @param toInsert
	 */
	public void insertIntoTable(long moId, String table,
			List<Map<String, Object>> toInsert) throws Exception;

	/**
	 * 批量同步模板数据给基站
	 * 
	 * @param mcbts
	 * @param table
	 * @param list
	 */
	public void batchInsertIntoTable(McBts[] mcbts,
			String table, List<Map<String, Object>> list) throws Exception;

	/**
	 * 根据表名,删除指定moId的数据
	 * 
	 * @param moId
	 */
	public void deleteFromTable(long moId, String table) throws Exception;

}
