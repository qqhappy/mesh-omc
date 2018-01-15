/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-09-02	| fanhaoyu 	| 	create the file                    
 */

package com.xinwei.minas.server.mem;

/**
 * 
 * MEMҵ����س���
 * 
 * @author fanhaoyu
 * 
 */

public class MemBizConstants {

	public final static int MA_MEM = 10;
	// MEM��Ϣ�ϱ�����
	public final static int MOC_MEMINFO_REQ = 0x0A01;
	// MEM��Ϣ�ϱ�Ӧ��
	public final static int MOC_MEMINFO_RSP = 0x0A02;

	// ����MEM�ۺϹ���
	public final static int MOC_MEM_MANAGE_REQ = 0x0A03;
	// ����MEM�ۺϹ���
	public final static int MOC_MEM_MANAGE_RSP = 0x0A04;
	// ����MEM�ۺϹ���1
	public final static int MOC_MEM_MANAGE1_REQ = 0x0A05;
	// ����MEM�ۺϹ���1
	public final static int MOC_MEM_MANAGE1_RSP = 0x0A06;
	// ���һ������־
	public final static int LAST_PACKAGE = 1;
	// ҵ��������Ϣ
	public final static int SERVICE_REQUEST = 0x00;
	// ҵ��Ӧ����Ϣ
	public final static int SERVICE_RESP = 0x01;
	// ����������MEM
	public final static int OPER_OBJ_MEM = 0x02;
	// operType
	public final static int OPERTYPE_MEMINFO = 0x30;

	public final static int OPERTYPE_MEM_MANAGE = 0x31;

	public final static int OPERTYPE_MEM_MANAGE1 = 0x32;

}