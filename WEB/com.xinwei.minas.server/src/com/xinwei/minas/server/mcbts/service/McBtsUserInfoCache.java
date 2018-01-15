/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-15	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserAllInfo;
import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserGroup;

/**
 * 
 * ��վ�û����ݻ���
 * 
 * @author fanhaoyu
 * 
 */

public class McBtsUserInfoCache {

	private static final McBtsUserInfoCache instance = new McBtsUserInfoCache();

	private Map<Long, List<McBtsUserAllInfo>> mapByPid = new ConcurrentHashMap<Long, List<McBtsUserAllInfo>>();

	private McBtsUserInfoCache() {
	}

	public static McBtsUserInfoCache getInstance() {
		return instance;
	}

	public List<McBtsUserAllInfo> getInfoList(long btsId) {

		if (mapByPid.get(btsId) == null) {
			return Collections.emptyList();
		}

		Collections.sort(mapByPid.get(btsId),
				new Comparator<McBtsUserAllInfo>() {

					@Override
					public int compare(McBtsUserAllInfo info1,
							McBtsUserAllInfo info2) {
						return (int) (info1.getPid() - info2.getPid());
					}
				});
		return mapByPid.get(btsId);
	}

	public List<McBtsUserGroup> getGroupList(long btsId) {
		List<McBtsUserAllInfo> infoList = getInfoList(btsId);
		if (infoList == null || infoList.isEmpty()) {
			return Collections.emptyList();
		}
		List<McBtsUserGroup> groupList = new LinkedList<McBtsUserGroup>();
		boolean repeat = false;
		for (McBtsUserAllInfo info : infoList) {
			repeat = false;
			for (McBtsUserGroup group : groupList) {
				if (info.getGroupId().equals(group.getGroupId())) {
					repeat = true;
					break;
				}
			}
			if (!repeat) {
				McBtsUserGroup group = new McBtsUserGroup();
				group.setGroupId(info.getGroupId());
				group.setGroupName(info.getGroupName());
				group.setGroupPrior(info.getGroupPrior());
				groupList.add(group);
			}
		}
		return groupList;
	}

	/**
	 * �򻺴���������ݣ������Ϣ�Ѵ��ڣ������ʧ��
	 * 
	 * @param btsId
	 * @param infoList
	 * @return
	 */
	public boolean addInfo(Long btsId, McBtsUserAllInfo info) {
		List<McBtsUserAllInfo> infoList = mapByPid.get(btsId);

		if (infoList == null) {
			infoList = new LinkedList<McBtsUserAllInfo>();
			mapByPid.put(btsId, infoList);
		}
		for (McBtsUserAllInfo info2 : infoList) {
			if (info2.getPid().equals(info.getPid())) {
				return false;
			}
		}
		return infoList.add(info);
	}

	public boolean addInfoList(Long btsId, List<McBtsUserAllInfo> infoList) {
		List<McBtsUserAllInfo> infoList1 = mapByPid.get(btsId);
		if (infoList1 == null) {
			mapByPid.put(btsId, infoList);
			return true;
		}
		return infoList1.addAll(infoList);
	}

	/**
	 * �޸���Ϣ�������Ϣ�����ڣ����޸�ʧ��
	 * 
	 * @param info
	 * @return
	 */
	public boolean modifyInfo(Long btsId, McBtsUserAllInfo info) {
		List<McBtsUserAllInfo> infoList = mapByPid.get(btsId);
		McBtsUserAllInfo temp = null;
		for (McBtsUserAllInfo info2 : infoList) {
			if (info2.getPid().equals(info.getPid())) {
				temp = info2;
				break;
			}
		}
		if (temp == null)
			return false;
		infoList.remove(temp);
		infoList.add(info);
		return true;
	}

	/**
	 * ɾ����Ϣ�������Ϣ�����ڣ���ɾ��ʧ��
	 * 
	 * @param pid
	 * @return
	 */
	public boolean deleteInfo(Long btsId, Long pid) {
		List<McBtsUserAllInfo> infoList = mapByPid.get(btsId);

		if (infoList == null) {
			return false;
		}
		McBtsUserAllInfo temp = null;
		for (McBtsUserAllInfo info2 : infoList) {
			if (info2.getPid().equals(pid)) {
				temp = info2;
				break;
			}
		}
		if (temp == null)
			return false;
		return infoList.remove(temp);
	}

	public void clearInfo(Long btsId) {
		if (mapByPid.get(btsId) == null)
			return;
		mapByPid.get(btsId).clear();
	}
}