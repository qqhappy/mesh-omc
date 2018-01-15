package com.xinwei.minas.server.mcbts.service.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfLoadBalance;
import com.xinwei.minas.server.mcbts.service.ICustomService;
/**
 * ���ؾ�������ҵ���ӿ�
 * 
 * @author yinbinqiang
 *
 */
public interface LoadBalanceService extends ICustomService{
	
	/**
	 * ��ѯ���ؾ��������Ϣ
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfLoadBalance queryByMoId(Long moId) throws Exception;
	
	/**
	 * ���ø��ؾ��������Ϣ
	 * @param loadBalance
	 * @throws Exception
	 */
	public void config(TConfLoadBalance loadBalance) throws Exception;

}
