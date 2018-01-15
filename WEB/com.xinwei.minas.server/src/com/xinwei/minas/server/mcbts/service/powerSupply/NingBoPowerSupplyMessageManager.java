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
 * ������Ϣ������
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class NingBoPowerSupplyMessageManager implements PowerSupplyMessageManager {

	//��Դ
	private PowerSupply power;
	
	//��Ӧ��Ϣ
	private NingBoPowerSupplyMessage responseMsg;
	
	/*��ȡVer��CID1����Ϊ40H��41H��42H*/
    public static final byte CID2_0x50 = 0x50;

    /*��ȡAdr��CID1����Ϊ40H��41H��42H*/
    public static final byte CID2_0x4f = 0x4F;

    /*��ȡ�������澯����ѹ����������Ϣ ��CID1Ϊ40h*/
    public static final byte CID2_0x01 = 0x01;
    
    /*��ȡ����ģ���������������ٶȡ��¶ȡ������ѹ���澯����Ϣ��CID1Ϊ41h*/
    public static final byte CID2_0x0E = 0x0E;
    
    /*��ȡֱ�����ģ�������ݣ�CID1Ϊ42h */
    public static final byte CID2_0x16 = 0x16;
    
    /*
     * ��ȡ���е���Ч��λ��ģ���ַ
     */
    public static final byte CID2_0x17 = 0x17;
    
    public static final  byte adr = 0x01;
    
    
	private static final byte IDLE = 0; // ���յ�״̬
	private static final byte RECEIVE_HIGH = 1; // ���ո��ֽڵ�״̬
	private static final byte RECEIVE_LOW = 2; // ���յ��ֽڵ�״̬
	
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
		//����
		case PowerSupply.ALTERNATING_CURRENT:
			message = createAlternatingCurrentMsg(power.getIpAddress(), power.getPort(), adr);
			break;
		//ֱ��
		case PowerSupply.DIRECT_CURRENT:
			message = createDirectCurrentWarnMsg(power.getIpAddress(), power.getPort(), adr);
			break;
		//����
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
		
		//��ʼ������
		state = IDLE;
		buf = new byte[4096];
		bufIsNull = new byte[4096];
		sourceBuf = new byte[4096];
		num = 0;
		sourceNum = 0;
		receiveByte = 0;
		responseMsg = null;
		
		//������Ϣ
		for (int i = 0; i < message.length; i++) {
			handleByte(message[i]);
		}
		
		//������Ϣ���ɸ澯����
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
	 * ������ȡ�豸��ַ��Ϣ��
	 * 
	 * @param ip
	 *            ��Դ��ip��ַ
	 * @param port
	 *            ��Դ�Ķ˿ڵ�ַ
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
	 * ��ȡ�������澯����ѹ����������Ϣ
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
	 * ��ȡ����ģ���������������ٶȡ��¶ȡ������ѹ���澯����Ϣ
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
	 * ��ȡֱ�����ģ��������
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
	 * ��ȡ���е���Ч��λ��ģ���ַ
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
	 * ����У���
	 * @return
	 */
	public boolean chksumbody() {
		boolean ret = false;
		// �����Ϣ��CheckSum��ֵ
		int checksum = buf[num - 2];

		int chksumResult = 0;
		for(int i=0; i<num-2; i++) {
			chksumResult = chksumResult + sourceBuf[i];
		}
		ret = checksum == ((chksumResult % 256) & 0xffff);
		return ret;
	}

	/**
	 * ������Ϣ��װ
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
