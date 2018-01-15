package com.xinwei.minas.server.mcbts.service.layer2;

import com.xinwei.minas.mcbts.core.model.layer2.SDMAConfig;
import com.xinwei.minas.server.mcbts.service.ICustomService;
/**
 * SDMA����ҵ���ӿ�
 * 
 * @author fangping
 *
 */
public interface SDMAConfigService extends ICustomService{
	
	/**
	 * ��ѯSDMA������Ϣ
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public SDMAConfig queryByMoId(Long moId) throws Exception;
	
	/**
	 * ����SDMA������Ϣ
	 * @param loadBalance
	 * @throws Exception
	 */
	public void config(SDMAConfig SDMAConfig) throws Exception;

}
