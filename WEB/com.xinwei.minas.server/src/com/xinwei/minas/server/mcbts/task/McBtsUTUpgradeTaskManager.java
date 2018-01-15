/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-7	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * �ն��������Ⱥͽ����¼�����������
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsUTUpgradeTaskManager {

	// Map<btsId,Map<�ն�pid,����>>
	private Map<Long, Map<String, String>> map = new ConcurrentHashMap<Long, Map<String, String>>();

	private McBtsUTUpgradeTaskManager() {
	}

	private static McBtsUTUpgradeTaskManager instance = new McBtsUTUpgradeTaskManager();

	public static McBtsUTUpgradeTaskManager getInstance() {
		return instance;
	}

	/**
	 * �����ն���������֪ͨ
	 * 
	 * @param message
	 */
	public void handleUTUpgradeProgressNotify(McBtsMessage message) {
		Long btsId = message.getBtsId();

		byte[] content = message.getContent();
		int offset = 0;

		String pid = ByteUtils.toHexString(content, offset, 4);
		offset += 4;

		int value = ByteUtils.toInt(content, offset, 1);
		offset += 1;

		setUTProgress(btsId, pid, String.valueOf(value));
	}

	/**
	 * �����ն��������֪ͨ
	 * 
	 * @param message
	 */
	public void handleUTUpgradeResultNotify(McBtsMessage message) {
		Long btsId = message.getBtsId();

		byte[] content = message.getContent();
		int offset = 0;

		String pid = ByteUtils.toHexString(content, offset, 4);
		offset += 4;

		String value = ByteUtils.toHexString(content, offset, 2);
		offset += 2;

		setUTProgress(btsId, pid, value.equals("0000") ? "100" : value);
	}

	/**
	 * ��ȡһЩ�ն˵Ľ���
	 * 
	 * @param utList
	 *            Ҫ��ѯ���ȵ��ն�
	 * @return
	 */
	public synchronized Map<String, String> getUTProgress(
			List<UserTerminal> utList) {

		if (utList == null || utList.isEmpty())
			return null;

		Map<String, String> result = new HashMap<String, String>();

		// ����Ҫ��ѯ���ȵ��ն˵��б�
		for (UserTerminal ut : utList) {
			Long btsId = ut.getBtsId();

			Map<String, String> utMap = map.get(btsId);

			// ���������վ��map���ǿյ�,�Ͳ���null
			if (utMap == null || utMap.isEmpty()) {
				// TODO fake
//				utMap = new HashMap<String, String>();
//				map.put(btsId, utMap);
//				utMap.put(ut.getPid(), "100");
				// TODO fake end

				result.put(ut.getPid(), null);
				continue;
			}

			String progress = utMap.get(ut.getPid());
			
			if (progress == null) {
				result.put(ut.getPid(), null);
				continue;
			}

			// δ���ʱ��ʾ��35֮���2λ����,��ɺ���ʾ100��2934�ȴ�����.��˳���>2ʱɾ����ɵĽ��
			if (progress.length() > 2)
				utMap.remove(ut.getPid());

			result.put(ut.getPid(), progress);
		}
		return result;
	}

	public synchronized void setUTProgress(Long btsId, String pid, String value) {
		Map<String, String> utMap = map.get(btsId);

		if (utMap == null) {
			utMap = new HashMap<String, String>();
			map.put(btsId, utMap);
		}

		utMap.put(pid, value);
	}
}
