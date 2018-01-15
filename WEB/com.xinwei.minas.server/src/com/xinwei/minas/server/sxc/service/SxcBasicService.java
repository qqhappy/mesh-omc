/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-18	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.sxc.service;

import java.util.List;

import com.xinwei.minas.sxc.core.model.SxcBasic;

/**
 * 
 * SXC������Ϣ����
 * 
 * @author chenjunhua
 * 
 */

public interface SxcBasicService {

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
	public Long addSxc(SxcBasic sxcBasic) throws Exception;

	/**
	 * �޸�SXC
	 * 
	 * @param oldSxc
	 * @param newSxc
	 * @throws Exception
	 */
	public void modifySxc(SxcBasic oldSxc, SxcBasic newSxc) throws Exception;

	/**
	 * ɾ��SXC
	 * 
	 * @param sxcBasic
	 * @throws Exception
	 */
	public void deleteSxc(SxcBasic sxcBasic) throws Exception;

}
