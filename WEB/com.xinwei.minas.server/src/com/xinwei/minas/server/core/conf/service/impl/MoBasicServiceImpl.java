/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-28	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.service.impl;

import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.server.core.conf.dao.MoBasicDAO;
import com.xinwei.minas.server.core.conf.service.MoBasicService;
import com.xinwei.minas.server.core.conf.service.MoCache;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * ���ܶ����������ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class MoBasicServiceImpl implements MoBasicService {
	
	private MoBasicDAO moBasicDAO;
	
	
	public void setMoBasicDAO(MoBasicDAO moBasicDAO) {
		this.moBasicDAO = moBasicDAO;
	}
	
	
	/**
	 * ��������MO
	 * 
	 * @return
	 */
	public List<Mo> queryAll() {
		return MoCache.getInstance().queryAll();
	}

	/**
	 * ��ȡ�ƶ����͵�MO
	 * 
	 * @param moTypeId
	 *            ����
	 * @return
	 */
	public List<Mo> queryByType(int moTypeId) {
		return MoCache.getInstance().queryByType(moTypeId);
	}

	/**
	 * ���ӱ��ܶ���
	 * 
	 * @param mo
	 *            ���ܶ���
	 * @param regionId
	 *            �����������
	 */
	public void add(Mo mo, long regionId) {
		SequenceService sequenceService = AppContext.getCtx().getBean(SequenceService.class);
		long moId = sequenceService.getNext();
		mo.setMoId(moId);
		// �������ݿ�
		moBasicDAO.add(mo, regionId);
		// ���»���
		MoCache.getInstance().add(mo);
	}
	

	/**
	 * �޸�MO
	 * 
	 * @param mo
	 */
	public void modify(Mo mo) {
		// �������ݿ�
		moBasicDAO.saveOrUpdate(mo);
		// ���»���
		MoCache.getInstance().update(mo);
	}

	/**
	 * ɾ��ָ��ID��MO
	 * 
	 * @param moId
	 */
	public void delete(Long moId) {
		// �������ݿ�
		moBasicDAO.delete(moId);
		// ���»���
		MoCache.getInstance().delete(moId);
	}
	
	
	/**
	 * �޸�MO����״̬
	 * 
	 * @param mo
	 */
	public void changeManageState(Mo mo) {		
		// �������ݿ�
		moBasicDAO.saveOrUpdate(mo);
		// ���»���
		MoCache.getInstance().changeManageState(mo);
	}


	/**
	 * ��ȡָ��moId�� MO
	 * 
	 * @param moId
	 * @return
	 */
	public Mo queryByMoId(Long moId) {
		// TODO Auto-generated method stub
		return MoCache.getInstance().queryByMoId(moId);
	}
}
