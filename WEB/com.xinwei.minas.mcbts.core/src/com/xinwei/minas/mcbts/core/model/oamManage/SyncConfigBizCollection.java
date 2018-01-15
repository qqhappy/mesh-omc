/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-10	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.oamManage;

import java.io.Serializable;
import java.util.List;

/**
 * ��վ֧��ҵ���Ԫ������
 * 
 * @author chenshaohua
 * 
 */

public class SyncConfigBizCollection implements Serializable {

	private BtsMeta[] btsMeta;

	public BtsMeta[] getBtsMeta() {
		return btsMeta;
	}

	public void setBtsMeta(BtsMeta[] btsMeta) {
		this.btsMeta = btsMeta;
	}

}
