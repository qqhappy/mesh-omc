/**
 * 
 */
package com.xinwei.minas.server.mcbts.proxy.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlan;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * @author chenshaohua
 * 
 */
public interface VlanProxy {

	/**
	 * ≈‰÷√÷’∂À
	 * 
	 * @param moId
	 * @param data
	 */
	void config(Long moId, GenericBizData data) throws Exception;

	/**
	 * ≤È—Øvlan
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<McBtsVlan> query(Long moId, GenericBizData data)
			throws Exception;
}
