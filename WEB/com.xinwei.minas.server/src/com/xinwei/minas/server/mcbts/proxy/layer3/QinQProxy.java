/**
 * 
 */
package com.xinwei.minas.server.mcbts.proxy.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlanAttach;
import com.xinwei.minas.mcbts.core.model.layer3.WrappedVlan;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * @author chenshaohua
 * 
 */
public interface QinQProxy {

	/**
	 * ≈‰÷√÷’∂À
	 * 
	 * @param moId
	 * @param data
	 */
	void config(Long moId, GenericBizData data, McBtsVlanAttach attach)
			throws Exception;

	/**
	 * ≤È—ØQinQ
	 * 
	 * @param moId
	 * @param wrappedVlan
	 * @param data
	 * @return
	 * @throws Exception
	 */
	WrappedVlan query(Long moId, WrappedVlan wrappedVlan, GenericBizData data)
			throws Exception;
}
