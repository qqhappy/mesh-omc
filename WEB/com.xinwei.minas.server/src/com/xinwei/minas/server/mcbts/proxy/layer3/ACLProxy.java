/**
 * 
 */
package com.xinwei.minas.server.mcbts.proxy.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsACL;
import com.xinwei.minas.mcbts.core.model.layer3.WrappedACL;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * @author chenshaohua
 * 
 */
public interface ACLProxy {

	/**
	 * ≈‰÷√÷’∂À
	 * 
	 * @param bizData
	 * @return
	 * @throws Exception
	 */
	public void config(Long moId, GenericBizData bizData) throws Exception,
			UnsupportedOperationException;

	public WrappedACL query(Long moId, GenericBizData bizData) throws Exception;

}
