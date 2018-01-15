/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-25	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

/**
 * 
 * ¿‡ºÚ“™√Ë ˆ
 * 
 * <p>
 * ¿‡œÍœ∏√Ë ˆ
 * </p> 
 * 
 * @author chenshaohua
 * 
 */

public class Airlink_BCH implements Serializable {

	private int mBCHSCGindex;
	
    private int mBCHTSindex;

	public int getmBCHSCGindex() {
		return mBCHSCGindex;
	}

	public void setmBCHSCGindex(int mBCHSCGindex) {
		this.mBCHSCGindex = mBCHSCGindex;
	}

	public int getmBCHTSindex() {
		return mBCHTSindex;
	}

	public void setmBCHTSindex(int mBCHTSindex) {
		this.mBCHTSindex = mBCHTSindex;
	}
    
    
}
