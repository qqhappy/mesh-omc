/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.powerSupply;

/**
 * 
 * 宁波消息结构
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class NingBoPowerSupplyMessage {
	
	public static final byte SOI = 0x7e;
    
	public static final byte EOI = 0x0d;
    
    
    /** 地址。*/
    protected byte adr;
    
    /** 设备类型。*/
    protected byte cid1;

    /** 命令信息。*/
    protected byte cid2;
    
    /** 电源信息长度*/
    protected byte length;
    
    /** 电源信息。*/
    protected byte[] info = new byte[0];
    
    /** 校验数。*/
    protected int chksum;
    
    /** IP */
	protected String powerIp;

	/** Port */
	protected int powerPort;
	
    /** 消息体校验结果。*/
    protected boolean bodyCheckResult = true;
    
    private byte infoIsNull[] = null;
    
	
    public NingBoPowerSupplyMessage() {
		
	}
	
	public NingBoPowerSupplyMessage(String ip, int port) {
		this.powerIp = ip;
		this.powerPort = port;
	}
	
	
    public int getMsgTag() {
		return  this.cid1;
	}
	
	public byte[] encode() {
		byte[] tempPowerBytes = new byte[4096];
		
		int infoIndex = 0;
		//info信息
		byte tempInfo[] = new byte[4096];
		for ( int j = 0; j < this.info.length; j++ )
	    {
			tempInfo[infoIndex++] = getHigh4Byte( this.info[j] );
	        tempInfo[infoIndex++] = getLow4Byte( this.info[j] );
	    }
		
		int index = 0;
		//起始位标示
//		tempPowerBytes[index++] = (byte) 0x8f;
		tempPowerBytes[index++] = (byte) SOI;
		
		//adr标示
		tempPowerBytes[index++] = getHigh4Byte(this.adr);
		tempPowerBytes[index++] = getLow4Byte(this.adr);
		
		//cid1标示
		tempPowerBytes[index++] = getHigh4Byte(this.cid1);
		tempPowerBytes[index++] = getLow4Byte(this.cid1);
		
		//cid2标示
		tempPowerBytes[index++] = getHigh4Byte(this.cid2);
		tempPowerBytes[index++] = getLow4Byte(this.cid2);
		
		//info的长度
		int infoLen = infoIndex/2;
		tempPowerBytes[index++] = getHigh4Byte((byte)infoLen);
		tempPowerBytes[index++] = getLow4Byte((byte)infoLen);
		
		//info信息
		System.arraycopy(tempInfo, 0, tempPowerBytes, index, infoIndex);
		
		index = index + infoIndex;
		
		//校验和
		int sumChecknum = getSumChecknum(tempPowerBytes, index );
		
		tempPowerBytes[index++] = getHigh4Byte((byte)sumChecknum);
		tempPowerBytes[index++] = getLow4Byte((byte)sumChecknum);
		
		//结束标志位
		tempPowerBytes[index++] = (byte)EOI;
		
		byte[] powerBytes = new byte[index];
		System.arraycopy(tempPowerBytes, 0, powerBytes, 0, index);
		
		return powerBytes;
	}
   
	private int getSumChecknum (byte[] tempPowerBytes, int len) {
		int checkNum = 0;
		for (int i=1; i<len; i++) {
			checkNum += (tempPowerBytes[i] & 0xff);
		}
		checkNum = checkNum % 256;
		this.setChksum(checkNum);
		return checkNum;
	}
	
	 /**
	 * @return the adr
	 */
	public byte getAdr() {
		return adr;
	}


	/**
	 * @param adr the adr to set
	 */
	public void setAdr(byte adr) {
		this.adr = adr;
	}


	/**
	 * @return the cid1
	 */
	public byte getCid1() {
		return cid1;
	}


	/**
	 * @param cid1 the cid1 to set
	 */
	public void setCid1(byte cid1) {
		this.cid1 = cid1;
	}


	/**
	 * @return the cid2
	 */
	public byte getCid2() {
		return cid2;
	}


	/**
	 * @param cid2 the cid2 to set
	 */
	public void setCid2(byte cid2) {
		this.cid2 = cid2;
	}


	/**
	 * @return the length
	 */
	public byte getLength() {
		return length;
	}


	/**
	 * @param length the length to set
	 */
	public void setLength(byte length) {
		this.length = length;
	}


	/**
	 * @return the info
	 */
	public byte[] getInfo() {
		return info;
	}


	/**
	 * @param info the info to set
	 */
	public void setInfo(byte[] info) {
		this.info = info;
	}
	
	public void setInfo( byte[] info , int offset , int length ) {
		 this.info = new byte[length];
         if ( info == null || length == 0 )
         {
             return;
         }
         System.arraycopy( info , offset , this.info , 0 , length );
	}


	/**
	 * @return the chksum
	 */
	public int getChksum() {
		return chksum;
	}


	/**
	 * @param chksum the chksum to set
	 */
	public void setChksum(int chksum) {
		this.chksum = chksum;
	}


	/**
	 * @param powerIp the powerIp to set
	 */
	public void setPowerIp(String powerIp) {
		this.powerIp = powerIp;
	}


	/**
	 * @param powerPort the powerPort to set
	 */
	public void setPowerPort(int powerPort) {
		this.powerPort = powerPort;
	}
	
	/**
	 * @return the powerIp
	 */
	public String getPowerIp() {
		return powerIp;
	}

	/**
	 * @return the powerPort
	 */
	public int getPowerPort() {
		return powerPort;
	}

	/**
	 * @return the bodyCheckResult
	 */
	public boolean isBodyCheckResult() {
		return bodyCheckResult;
	}

	/**
	 * @param bodyCheckResult the bodyCheckResult to set
	 */
	public void setBodyCheckResult(boolean bodyCheckResult) {
		this.bodyCheckResult = bodyCheckResult;
	}
	
	/**
	 * @return the infoIsNull
	 */
	public byte[] getInfoIsNull() {
		return infoIsNull;
	}

	 /**
     * 设置NullInfo信息。
     * @param info byte[]
     * @param offset int
     * @param len int
     */
    public void setIsNullInfo ( byte[] info , int offset , int len )
    {
        infoIsNull = null;
        infoIsNull = new byte[len];
        System.arraycopy( info , offset , infoIsNull , 0 , len );
    }

	/** 将一个byte字节的高4位加上0x30。*/
    private byte getHigh4Byte ( byte ll )
    {
        int intHighTemp = ( ( ll & 0xf0 ) >> 4 );
        return (byte)(intHighTemp + 48);
    }

    /** 将一个byte字节的低4位加上0x30。*/
    private byte getLow4Byte ( byte ll )
    {
        int intHighTemp = ( ( ll & 0x0f ) );
        return (byte)(intHighTemp + 48);
    }
}
