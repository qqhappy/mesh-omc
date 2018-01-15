/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-19	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage;

import java.util.HashSet;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.PowerSupply;

/**
 * 
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����:������վ�͵�Դ�Ĺ�ϵ
 * </p>
 * 
 * @author qiwei
 * 
 */

public interface McBtsPowerConfigDAO {
	/**
	 * ���ݻ�վmoid��ѯ����Ӧ��Դ
	 * 
	 */
	public PowerSupply queryByMoId(long moid);

	/**
	 * �洢��Դ�ͻ�վ��Ӧ��ϵ��
	 */
	public void saveByMoIdAndPowerId(long idx, PowerSupply powerSupply);

	/**
	 * ���ݵ�Դ��Ϣ��ѯ��Ӧ��վ��moId����
	 */
	public HashSet<Long> queryMcbtsByPowerSupply(PowerSupply powerSupply);

	/**
	 * ɾ����վʱɾ����Ӧ��ϵ
	 */
	public void deleteRelationByMoId(long moid);
}
