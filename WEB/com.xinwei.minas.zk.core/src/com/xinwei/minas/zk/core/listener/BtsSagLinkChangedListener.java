/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.listener;

import com.xinwei.minas.zk.core.xnode.vo.ZkBtsSagLinkVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsVO;

/**
 * 
 * ��վ��SAG����·�仯������
 * 
 * @author chenjunhua
 * 
 */

public interface BtsSagLinkChangedListener {

	/**
	 * ��վ��SAG����·�仯
	 * 
	 * @param zkBts
	 *            ��վģ��
	 * @param newBtsSagLink
	 *            ��վ��SAG���µ���·
	 */
	public void linkChanged(ZkBtsVO zkBts, ZkBtsSagLinkVO newBtsSagLink);
	
	
	/**
	 * ��վ��SAG����·ɾ��
	 * 
	 * @param zkBts
	 *            ��վģ��
	 * @param oldBtsSagLink
	 *            ��վ��SAG��ɵ���·
	 */
	public void linkDeleted(ZkBtsVO zkBts, ZkBtsSagLinkVO oldBtsSagLink);
}
