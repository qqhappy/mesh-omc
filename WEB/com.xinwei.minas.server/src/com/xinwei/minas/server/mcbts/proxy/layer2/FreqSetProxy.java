/**
 * 
 */
package com.xinwei.minas.server.mcbts.proxy.layer2;

import com.xinwei.minas.mcbts.core.model.layer2.TConfFreqSet;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * @author chenshaohua
 *
 */
public interface FreqSetProxy {

	/**
	 * 查询频点集
	 * @param moId
	 * @param freqSetEntity
	 * @return
	 * @throws Exception
	 */
	public TConfFreqSet query(Long moId, TConfFreqSet freqSetEntity) throws Exception;
	
	/**
	 * 配置频点集
	 * @param moId
	 * @param bizData
	 * @throws Exception
	 */
	public void config(Long moId, GenericBizData bizData) throws Exception;
	
}
