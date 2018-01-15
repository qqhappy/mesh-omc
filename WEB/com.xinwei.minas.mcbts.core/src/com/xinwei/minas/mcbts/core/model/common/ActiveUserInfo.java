package com.xinwei.minas.mcbts.core.model.common;

import java.io.Serializable;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * �����û�ʵ��
 * 
 * @author fangping
 * 
 */
public class ActiveUserInfo implements Serializable {
	private long eid;
	private int press;

	public ActiveUserInfo(byte[] buf, int offset) {
		// ���ֽ�ת��Ϊģ��
		eid = ByteUtils.toLong(buf, offset, 4);
		offset += 4;
		// press�п����Ǹ���
		press = (int) ByteUtils.toSignedNumber(buf, offset, 2);
		offset += 2;

		offset += 26;
	}

	public long getEid() {
		return eid;
	}

	public int getPress() {
		return press;
	}

	public void setEid(long eid) {
		this.eid = eid;
	}

	public void setPress(int press) {
		this.press = press;
	}

}
