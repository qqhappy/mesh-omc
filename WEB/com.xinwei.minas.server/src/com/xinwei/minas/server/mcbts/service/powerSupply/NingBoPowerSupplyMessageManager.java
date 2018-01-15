/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.powerSupply;

import com.xinwei.minas.mcbts.core.model.sysManage.PowerSupply;

/**
 * 
 * 宁波消息管理类
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class NingBoPowerSupplyMessageManager implements PowerSupplyMessageManager {

	//电源
	private PowerSupply power;
	
	//响应消息
	private NingBoPowerSupplyMessage responseMsg;
	
	/*获取Ver，CID1可以为40H、41H或42H*/
    public static final byte CID2_0x50 = 0x50;

    /*获取Adr，CID1可以为40H、41H或42H*/
    public static final byte CID2_0x4f = 0x4F;

    /*获取交流配电告警、电压、电流等信息 ，CID1为40h*/
    public static final byte CID2_0x01 = 0x01;
    
    /*获取整流模块输出电流、风机速度、温度、输出电压、告警等信息，CID1为41h*/
    public static final byte CID2_0x0E = 0x0E;
    
    /*获取直流配电模拟量数据，CID1为42h */
    public static final byte CID2_0x16 = 0x16;
    
    /*
     * 获取所有的有效在位的模块地址
     */
    public static final byte CID2_0x17 = 0x17;
    
    public static final  byte adr = 0x01;
    
    
	private static final byte IDLE = 0; // 接收的状态
	private static final byte RECEIVE_HIGH = 1; // 接收高字节的状态
	private static final byte RECEIVE_LOW = 2; // 接收低字节的状态
	
	public byte state = IDLE;
	public byte[] buf;
	public byte[] bufIsNull;
	public byte[] sourceBuf;
	public int num;
	public int sourceNum;
	private byte receiveByte;
	
	public NingBoPowerSupplyMessageManager(PowerSupply power) {
		this.power = power;
	}
    
    
	@Override
	public byte[] createPollCmdMessage() {
		NingBoPowerSupplyMessage message = null;
		
		switch(power.getCurrentType()) {
		//交流
		case PowerSupply.ALTERNATING_CURRENT:
			message = createAlternatingCurrentMsg(power.getIpAddress(), power.getPort(), adr);
			break;
		//直流
		case PowerSupply.DIRECT_CURRENT:
			message = createDirectCurrentWarnMsg(power.getIpAddress(), power.getPort(), adr);
			break;
		//整流
		case PowerSupply.RECTIFICATION:
			message = createRectificationWarnMsg(power.getIpAddress(), power.getPort(), adr);
			break;
		}
		
		if (message != null) {
			return message.encode();
		}
		
		return null;
	}

	@Override
	public String parseMessage(byte[] message) throws Exception{
		
		//初始化数据
		state = IDLE;
		buf = new byte[4096];
		bufIsNull = new byte[4096];
		sourceBuf = new byte[4096];
		num = 0;
		sourceNum = 0;
		receiveByte = 0;
		responseMsg = null;
		
		//重组消息
		for (int i = 0; i < message.length; i++) {
			handleByte(message[i]);
		}
		
		//解析消息生成告警内容
		if (responseMsg != null) {
			StringBuffer parseResult = new StringBuffer();
			boolean flag = NingBoPowerSupplyMessagePaserHelpler.getInstance().parse(responseMsg, parseResult);
			if (flag == false) {
				return parseResult.toString();
			}
		}
		
		return null;
	}
    

	/**
	 * 创建获取设备地址消息。
	 * 
	 * @param ip
	 *            电源的ip地址
	 * @param port
	 *            电源的端口地址
	 * @param cid1
	 * @return
	 */
	public static NingBoPowerSupplyMessage createGetAddrMsg(String ip,
			int port, byte cid1) {
		NingBoPowerSupplyMessage msg = new NingBoPowerSupplyMessage(ip, port);

		msg.setAdr((byte) 0x01);
		msg.setCid1((byte) cid1);
		msg.setCid2((byte) CID2_0x01);
		msg.setLength((byte) 0);
		byte[] info = {};
		msg.setInfo(info, 0, msg.getLength());
		msg.setChksum((byte) 0);
		return msg;
	}

	/**
	 * 获取交流配电告警、电压、电流等信息
	 * 
	 * @param ip
	 * @param port
	 * @param adr
	 * @return
	 */
	public static NingBoPowerSupplyMessage createAlternatingCurrentMsg(
			String ip, int port, byte adr) {
		NingBoPowerSupplyMessage msg = new NingBoPowerSupplyMessage(ip, port);
		msg.setAdr((byte) adr);
		msg.setCid1((byte) 0x40);
		msg.setCid2((byte) CID2_0x01);
		msg.setInfo(null, 0, 0);
		return msg;
	}

	/**
	 * 获取整流模块输出电流、风机速度、温度、输出电压、告警等信息
	 * 
	 * @param ip
	 * @param port
	 * @param adr
	 * @return
	 */
	public static NingBoPowerSupplyMessage createRectificationWarnMsg(
			String ip, int port, byte adr) {
		NingBoPowerSupplyMessage temp = new NingBoPowerSupplyMessage(ip, port);
		temp.setAdr((byte) adr);
		temp.setCid1((byte) 0x41);
		temp.setCid2((byte) CID2_0x0E);
		// byte[] info = new byte[10];
		byte[] info = { 0x01 };
		int index = info.length;
		temp.setInfo(info, 0, index);
		return temp;
	}

	/**
	 * 获取直流配电模拟量数据
	 * 
	 * @param ip
	 * @param port
	 * @param adr
	 * @return
	 */
	public static NingBoPowerSupplyMessage createDirectCurrentWarnMsg(
			String ip, int port, byte adr) {
		NingBoPowerSupplyMessage temp = new NingBoPowerSupplyMessage(ip, port);
		temp.setAdr((byte) adr);
		temp.setCid1((byte) 0x42);
		temp.setCid2((byte) CID2_0x16);
		byte[] info = new byte[10];
		int index = 0;
		temp.setInfo(info, 0, index);
		return temp;
	}

	/**
	 * 获取所有的有效在位的模块地址
	 * 
	 * @param ip
	 * @param port
	 * @param adr
	 * @return
	 */
	public static NingBoPowerSupplyMessage createGetAllModuleAddr(String ip,
			int port, byte adr) {
		NingBoPowerSupplyMessage temp = new NingBoPowerSupplyMessage();
		temp.setAdr((byte) adr);
		temp.setCid1((byte) 0x42);
		temp.setCid2((byte) CID2_0x17);
		byte[] info = new byte[10];
		int index = 0;
		temp.setInfo(info, 0, index);
		return temp;
	}
	
	
	/**
	 * 进行校验和
	 * @return
	 */
	public boolean chksumbody() {
		boolean ret = false;
		// 获得消息中CheckSum的值
		int checksum = buf[num - 2];

		int chksumResult = 0;
		for(int i=0; i<num-2; i++) {
			chksumResult = chksumResult + sourceBuf[i];
		}
		ret = checksum == ((chksumResult % 256) & 0xffff);
		return ret;
	}

	/**
	 * 进行消息组装
	 * 
	 * @param value
	 */
	private void handleByte(int value) {
		switch (this.state) {
		case IDLE:
			if (value == 0x7E) {
				handleSoi(value);
			}
			break;
		case RECEIVE_HIGH:
			this.receiveByte = 0x00;
			if (value == 0x0d) {
				this.buf[this.num] = (byte) value;
				this.bufIsNull[this.num] = 1;
				this.num++;
				this.sourceBuf[this.sourceNum++] = (byte) value;

				this.state = IDLE;
				createResponseMsg();
			} else if (value == 0x7E) {
				handleSoi(value);
			} else if (value == 0x20) { // space
				this.bufIsNull[this.num] = 0;
				this.sourceBuf[this.sourceNum++] = (byte) value;

				this.receiveByte = 0;
				this.state = RECEIVE_LOW;
			} else {
				this.bufIsNull[this.num] = 1;
				this.sourceBuf[this.sourceNum++] = (byte) value;
				this.receiveByte = (byte) ((value - 0x30) * 16);
				this.state = RECEIVE_LOW;
			}
			break;
		case RECEIVE_LOW:
			if (value == 0x0d) {
				this.state = IDLE;
			} else if (value == 0x7e) {
				handleSoi(value);
			} else if (value == 0x20) { // space
				this.sourceBuf[this.sourceNum] = (byte) value;
				this.sourceNum++;
				this.bufIsNull[this.num] = 0;
				this.buf[this.num] = (byte) 0;
				this.num++;
				this.receiveByte = 0;
				this.state = RECEIVE_HIGH;
			} else {
				this.sourceBuf[this.sourceNum] = (byte) value;
				this.sourceNum++;

				this.receiveByte = (byte) ((this.receiveByte & 0xf0) + (byte) (value - 0x30));
				this.state = RECEIVE_HIGH;
				this.buf[this.num] = (byte) this.receiveByte;
				if (this.bufIsNull[this.num] != 0)
					this.bufIsNull[this.num] = 1;
				this.num++;
			}
			break;
		}
	}

	private void handleSoi(int soi) {
		this.num = 0;
		this.sourceNum = 0;
		this.buf[this.num] = (byte) soi;
		this.bufIsNull[this.num] = 1;
		this.num++;
		this.sourceBuf[this.sourceNum++] = (byte) soi;
		this.state = RECEIVE_HIGH;
	}
	
	private void createResponseMsg() {
		responseMsg = new NingBoPowerSupplyMessage();
		int index = 1;

		responseMsg.setPowerIp(power.getIpAddress());
		responseMsg.setPowerPort(power.getPort());

		responseMsg.setAdr(buf[index++]);
		responseMsg.setCid1(buf[index++]);
		responseMsg.setCid2(buf[index++]);

		int len = buf[index++];
		responseMsg.setLength((byte) len);
		responseMsg.setInfo(buf, index, len);
		
		responseMsg.setBodyCheckResult(chksumbody());
		
		responseMsg.setIsNullInfo(this.bufIsNull, index, len);
	}
	
	

}
