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

public class Airlink_RRCH implements Serializable {

	private int mRRCHSCGindex;
	
    private int mRRCHTSindex;

	public int getmRRCHSCGindex() {
		return mRRCHSCGindex;
	}

	public void setmRRCHSCGindex(int mRRCHSCGindex) {
		this.mRRCHSCGindex = mRRCHSCGindex;
	}

	public int getmRRCHTSindex() {
		return mRRCHTSindex;
	}

	public void setmRRCHTSindex(int mRRCHTSindex) {
		this.mRRCHTSindex = mRRCHTSindex;
	}
    
    
}
