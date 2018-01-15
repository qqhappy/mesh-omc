/**
 * 
 */
package com.xinwei.minas.server.mcbts.proxy.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsRepeater;
import com.xinwei.minas.mcbts.core.model.layer3.WrappedRepeater;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * @author chenshaohua
 * 
 */
public interface RepeaterProxy {

	/**
	 * ≈‰÷√÷’∂À
	 * 
	 * @param moId
	 * @param data
	 * @throws Exception
	 */
	void config(Long moId, GenericBizData data) throws Exception;

	public WrappedRepeater query(Long moId, GenericBizData bizData)
			throws Exception;
}
