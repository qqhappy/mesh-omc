/**
 * 
 */
package com.xinwei.minas.server.mcbts.proxy.layer3;

import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * @author chenshaohua
 * 
 */
public interface TosConfProxy {

	/**
	 * ≈‰÷√tos
	 * 
	 * @param moId
	 * @param bizData
	 * @throws Exception
	 */
	public void config(Long moId, GenericBizData bizData) throws Exception;
}
