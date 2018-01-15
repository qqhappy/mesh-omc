package com.xinwei.minas.server.mcbts.service.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPE;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItem;
import com.xinwei.minas.server.mcbts.service.ICustomService;
/**
 * rcpe���û���ҵ���ӿ�
 * @author yinbinqiang
 *
 */
public interface RCPEService extends ICustomService{
	/**
	 * RCPE���������Ϣ
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfRCPE queryByMoId(Long moId) throws Exception;
	
	/**
	 * ����RCPE������Ϣ
	 * @param loadBalance
	 * @throws Exception
	 */
	public void config(TConfRCPE rcpe) throws Exception;
	
	/**
	 * ɾ��ָ��RCPE��Ϣ
	 * @param rcpeItem
	 * @throws Exception
	 */
	public void deleteRcpe(TConfRCPEItem rcpeItem) throws Exception;
}
