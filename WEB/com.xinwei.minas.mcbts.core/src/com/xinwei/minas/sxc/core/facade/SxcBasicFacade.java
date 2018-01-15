/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-16	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.sxc.core.facade;

import java.rmi.Remote;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.sxc.core.model.SxcBasic;

/**
 * 
 * SXC����ҵ������
 * 
 * @author chenjunhua
 * 
 */

public interface SxcBasicFacade extends Remote {

	/**
	 * ��ѯ����SXC
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SxcBasic> queryAll() throws Exception;

	/**
	 * ����SXC
	 * 
	 * @param sxcBasic
	 * @throws Exception
	 */
	@Loggable
	public Long addSxc(SxcBasic sxcBasic) throws Exception;

	/**
	 * �޸�SXC
	 * 
	 * @param oldSxc
	 * @param newSxc
	 * @throws Exception
	 */
	@Loggable
	public void modifySxc(SxcBasic oldSxc, SxcBasic newSxc) throws Exception;

	/**
	 * ɾ��SXC
	 * 
	 * @param sxcBasic
	 * @throws Exception
	 */
	@Loggable
	public void deleteSxc(SxcBasic sxcBasic) throws Exception;

}
