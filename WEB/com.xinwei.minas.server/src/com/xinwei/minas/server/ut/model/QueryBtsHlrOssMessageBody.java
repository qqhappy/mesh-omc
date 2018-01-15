/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-1	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.hlr.net.oss.model.HlrOssMessageBody;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * ��ѯ��վ��oss��Ϣ��
 * 
 * 
 * @author tiance
 * 
 */

public class QueryBtsHlrOssMessageBody extends HlrOssMessageBody {
	// ��վ������
	private int btsNum;
	// ��ǰ�İ�������Ҫ���Ļ�վ��Ϣ�е�����
	private int pageIndex;
	// ��վ���б�
	private List<McBts> btsList;

	private static final int TAG_FLAG = 0x040b;
	private static final int TAG_BTS_NUM = 0x0520;
	private static final int TAG_PAGE_INDEX = 0x0521;
	private static final int TAG_PAGE_SIZE = 0x0522;
	private static final int TAG_TAG_BTS = 0x051f;
	private static final int TAG_LAID = 0x043b;
	private static final int TAG_BTSID4 = 0x051c;
	private static final int TAG_RAID = 0x0523;
	private static final int TAG_BTS_NAME = 0x0524;
	private static final int TAG_SAG_ID = 0x043c;

	// tag��Ӧ��value�ĳ���
	private static Map<Integer, Integer> valueLengthMap = new HashMap<Integer, Integer>(
			10);

	public QueryBtsHlrOssMessageBody(byte[] buf, int offset) {
		super(buf, offset);
	}

	public QueryBtsHlrOssMessageBody() {
		super();
		if (valueLengthMap.size() == 0) {
			valueLengthMap.put(TAG_FLAG, 1);
			valueLengthMap.put(TAG_BTS_NUM, 2);
			valueLengthMap.put(TAG_PAGE_INDEX, 2);
			valueLengthMap.put(TAG_PAGE_SIZE, 2);
			valueLengthMap.put(TAG_TAG_BTS, 2);
			valueLengthMap.put(TAG_LAID, 5);
			valueLengthMap.put(TAG_BTSID4, 4);
			valueLengthMap.put(TAG_RAID, 4);
			valueLengthMap.put(TAG_BTS_NAME, 21);
			valueLengthMap.put(TAG_SAG_ID, 4);
		}
	}

	public QueryBtsHlrOssMessageBody(int btsNum, int pageIndex,
			List<McBts> btsList) {
		this();
		this.btsNum = btsNum;
		this.pageIndex = pageIndex;
		this.btsList = btsList;

		body = toBytes();
	}

	/**
	 * ���һ��tv��buf��,Ȼ�󷵻���Ӻ��offset
	 * 
	 * @param buf
	 * @param tag
	 * @param value
	 * @param offset
	 * @return
	 */
	private static int addTV(byte[] buf, int tag, Object value, int offset) {
		// tag
		ByteUtils.putNumber(buf, offset, String.valueOf(tag), 2);

		offset += 2;

		// value
		if (tag == TAG_BTS_NAME) {
			ByteUtils.putString(buf, offset, String.valueOf(value),
					valueLengthMap.get(tag), '\0', "GBK");
		} else {
			ByteUtils.putNumber(buf, offset, String.valueOf(value),
					valueLengthMap.get(tag));
		}

		return offset + valueLengthMap.get(tag);
	}

	@Override
	public byte[] toBytes() {
		byte[] buf = new byte[2028];
		int offset = 0;

		// offset = addTV(buf, TAG_FLAG, 0, offset);

		offset = addTV(buf, TAG_BTS_NUM, btsNum, offset);

		offset = addTV(buf, TAG_PAGE_INDEX, pageIndex, offset);

		offset = addTV(buf, TAG_PAGE_SIZE, btsList.size(), offset);

		for (McBts mcbts : btsList) {
			offset = addTV(buf, TAG_TAG_BTS, 48, offset);

			offset = addTV(buf, TAG_LAID, 0, offset);
			offset = addTV(buf, TAG_BTSID4, mcbts.getBtsId(), offset);
			offset = addTV(buf, TAG_RAID, mcbts.getLocationAreaId(), offset);
			offset = addTV(buf, TAG_BTS_NAME, mcbts.getName(), offset);
			offset = addTV(buf, TAG_SAG_ID, mcbts.getSagDeviceId(), offset);
		}

		byte[] content = new byte[offset];
		System.arraycopy(buf, 0, content, 0, offset);

		return content;
	}
}
