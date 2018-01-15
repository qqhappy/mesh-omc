package com.xinwei.minas.server.mcbts.model.message;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.ActiveUserInfo;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * BTS�����ն��б�ʵ����
 * 
 * @author fangping
 * 
 */

public class McBtsOnlineTerminalList implements Serializable {

	private Long btsId;
	private Long idx;
	private Long moId;
	private int transID;

	// �ֽڳ���
	private int totalCount;
	private int pageSize;
	private int fragment;
	private ActiveUserInfo activeInfoUser;

	private List<ActiveUserInfo> activeUsers = new LinkedList();

	public McBtsOnlineTerminalList() {

	}

	public McBtsOnlineTerminalList(Long eid, byte[] buf) {
		this(eid, buf, 0);
	}

	public McBtsOnlineTerminalList(Long btsId, byte[] buf, int offset) {
		this.setBtsId(btsId);
		// totalCount
		totalCount = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
		// pageSize
		pageSize = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
		// fragment
		fragment = ByteUtils.toInt(buf, offset, 2);
		offset += 2;

		for (int i = 0; i < pageSize; i++) {
			ActiveUserInfo activeUserInfo = new ActiveUserInfo(buf, offset);
			activeUsers.add(activeUserInfo);
			// ���ְ�����
			offset += 32;
		}
	}

	public int getTransID() {
		return transID;
	}

	public void setTransID(int transID) {
		this.transID = transID;
	}

	public Long getBtsId() {
		return btsId;
	}

	public void setBtsId(Long btsId) {
		this.btsId = btsId;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Long getMoId() {
		return moId;
	}

	public void setMoId(Long moId) {
		this.moId = moId;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getFragment() {
		return fragment;
	}

	public void setFragment(int fragment) {
		this.fragment = fragment;
	}

	public ActiveUserInfo getActiveInfoUser() {
		return activeInfoUser;
	}

	public void setActiveInfoUser(ActiveUserInfo activeInfoUser) {
		this.activeInfoUser = activeInfoUser;
	}

	public List<ActiveUserInfo> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<ActiveUserInfo> activeUsers) {
		this.activeUsers = activeUsers;
	}

}
