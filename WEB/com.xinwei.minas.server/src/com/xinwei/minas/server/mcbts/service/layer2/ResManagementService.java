package com.xinwei.minas.server.mcbts.service.layer2;

import com.xinwei.minas.mcbts.core.model.layer2.TConfResmanagement;
import com.xinwei.minas.server.mcbts.service.ICustomService;


/**
 * ������Դ����ҵ���ӿ�
 * 
 * @author chenshaohua
 *
 */
public interface ResManagementService extends ICustomService {
	
	/**
	 * ��ѯ������Դ��Ϣ
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfResmanagement queryByMoId(Long moId) throws Exception;
	
	/**
	 * ����������Դ��Ϣ
	 * @param resManagement
	 * @throws Exception
	 */
	public void config(TConfResmanagement resManagement) throws Exception;
	
	/**
	 * ����Ԫ���������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public TConfResmanagement queryFromNE(Long moId) throws Exception;
}
