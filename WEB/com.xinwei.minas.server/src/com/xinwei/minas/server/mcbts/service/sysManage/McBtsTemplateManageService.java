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
 * 基站模板管理的服务接口
 * 
 * 
 * @author tiance
 * 
 */

public interface McBtsTemplateManageService {
	/**
	 * 获取所有基站同步的模板
	 * 
	 * @return 基站模板列表
	 */
	public List<McBtsTemplate> queryAll();

	/**
	 * 基于一个模板ID,返回一个新的可用的模板ID
	 * 
	 * @param referId
	 * @param temp
	 * @return
	 */
	public Long applyNewId(Long referId, McBtsTemplate temp);

	/**
	 * 在数据库的每个表中生成模板备份数据
	 * 
	 * @param templateId
	 */
	public void generateTemplateBackup(long templateId);

	/**
	 * 在数据库的每个表中删除模板备份数据
	 * 
	 * @param templateId
	 */
	public void deleteTemplateBackup(long templateId);

	/**
	 * 通过moId查询一个模板
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsTemplate queryByMoId(Long moId);

	/**
	 * 初始化基站数据
	 * 
	 * @param mcbts
	 * @param templateId
	 */
	public void initMcBtsData(McBts mcbts);

	/**
	 * 同步一批基站
	 * 
	 * @param templateId
	 * @param oprs
	 * @param mcbts
	 */
	public void syncAll(long templateId, Operation[] oprs, McBts[] mcbts)
			throws Exception;

	/**
	 * 删除基站的初始化数据
	 * 
	 * @param mcbts
	 */
	public void rollBackMcBtsData(long moId);

	/**
	 * 获得基站业务模型
	 * 
	 * @return
	 */
	public List<McBtsOperation> getMcbtsOperation();

	/**
	 * 向MoCache中添加Mo
	 * 
	 * @param mo
	 */
	public void addToMoCache(Long moId);

	/**
	 * 从MoCache中删除Mo
	 * 
	 * @param moId
	 */
	public void removeFromMoCache(Long moId);

	/**
	 * 更新模板数据
	 * 
	 * @param template
	 */
	public void updateTemplate(McBtsTemplate template);

	/**
	 * 在取消模板操作或者删除业务的时候执行恢复操作
	 * 
	 * @param moId
	 * @param operations
	 *            要回复数据的业务
	 * @param isDel
	 *            是否是删除业务
	 */
	public void recover(Long moId, List<String> operations, boolean isDel);
}
