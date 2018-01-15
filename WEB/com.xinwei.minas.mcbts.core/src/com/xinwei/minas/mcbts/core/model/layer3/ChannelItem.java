/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

/**
 * 
 * Àà¼òÒªÃèÊö
 * 
 * <p>
 * ÀàÏêÏ¸ÃèÊö
 * </p>
 * 
 * @author chenshaohua
 * 
 */

public class ChannelItem implements Serializable {

	private int SCGindex;

	private int TSindex;

	public int getSCGindex() {
		return SCGindex;
	}

	public void setSCGindex(int sCGindex) {
		SCGindex = sCGindex;
	}

	public int getTSindex() {
		return TSindex;
	}

	public void setTSindex(int tSindex) {
		TSindex = tSindex;
	}

}
