/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.analyze;

import com.xinwei.minas.server.stat.dao.StatDataDAO;
import com.xinwei.minas.server.stat.dao.StatDetailDAO;

/**
 * 
 * ͳ�Ʒ�������
 * 
 * @author fanhaoyu
 * 
 */

public class StatTask implements Runnable {

	/**
	 * ͳ������
	 */
	private int type;

	/**
	 * ͳ�ƿ�ʼʱ��,Ҫ�������ֵ,��λ��
	 */
	private long startTime;

	/**
	 * ͳ��ʱ����,��λ��
	 */
	private long analyzeInterval;

	/**
	 * ͳ�����ݷ��ʽӿ�
	 */
	private StatDataDAO collectDataDAO;

	/**
	 * ͳ����ϸ���ݷ��ʽӿ�
	 */
	private StatDetailDAO statDetailDAO;

	/**
	 * ���ݷ�����
	 */
	private DataAnalyzer analyzer;

	/**
	 * Ĭ�ϵ�ͳ����
	 */
	private int[] items = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	/**
	 * ͳ�������캯��
	 * 
	 * @param type
	 *            --ͳ������
	 * @param items
	 *            --ͳ����
	 * @param analyzeInterval
	 *            --����ʱ����
	 * @param collectDataDAO
	 *            --ͳ�����ݷ��ʽӿ�
	 * @param statDetailDAO
	 *            --ͳ����ϸ���ݷ��ʽӿ�
	 * @param analyzer
	 *            --���ݷ�����
	 * @param startTime
	 *            --���ݷ�����ʼʱ��
	 */
	public StatTask(int type, int[] items, long analyzeInterval,
			StatDataDAO collectDataDAO, StatDetailDAO statDetailDAO,
			DataAnalyzer analyzer, long startTime) {
		this.type = type;
		this.analyzeInterval = analyzeInterval;
		this.collectDataDAO = collectDataDAO;
		this.statDetailDAO = statDetailDAO;
		this.items = items;
		this.analyzer = analyzer;
		this.setStartTime(startTime);
	}

	/**
	 * ʹ��Ĭ�ϵ�ͳ����
	 * 
	 * @param type
	 * @param analyzeInterval
	 * @param collectDataDAO
	 * @param statDetailDAO
	 */
	public StatTask(int type, long analyzeInterval, StatDataDAO collectDataDAO,
			StatDetailDAO statDetailDAO, DataAnalyzer analyzer) {
		this.type = type;
		this.analyzeInterval = analyzeInterval;
		this.collectDataDAO = collectDataDAO;
		this.statDetailDAO = statDetailDAO;
		this.analyzer = analyzer;
	}

	public void run() {

		StatThread thread = this.createStatThread(type, analyzeInterval,
				collectDataDAO, statDetailDAO, analyzer, this.startTime);
		new Thread(thread).start();
		startTime += this.getAnalyzeInterval();
	}

	/**
	 * ��������ͳ���߳�
	 * 
	 * @param item
	 *            --ͳ����
	 * @param type
	 *            --ͳ������
	 * @param analyzeInterval
	 *            --���ݷ���ʱ����
	 * @param collectDataDAO
	 *            --ͳ�����ݷ��ʽӿ�
	 * @param statDetailDAO
	 *            --ͳ����ϸ���ݷ��ʽӿ�
	 * @param dataAnalyzer
	 *            --ͳ�����ݷ�����
	 * @return
	 */
	public StatThread createStatThread(int type, long analyzeInterval,
			StatDataDAO collectDataDAO, StatDetailDAO statDetailDAO,
			DataAnalyzer dataAnalyzer, long startTime) {
		StatThread thread = new StatThread(type, analyzeInterval,
				collectDataDAO, statDetailDAO, dataAnalyzer, startTime);
		this.setStartTime(startTime);
		return thread;
	}

	/**
	 * ȡ��ͳ�����ݷ���ʱ����
	 * 
	 * @return
	 */
	public long getAnalyzeInterval() {
		return analyzeInterval;
	}

	/**
	 * ����ͳ�����ݷ���ʱ����
	 * 
	 * @param analyzeInterval
	 */
	public void setAnalyzeInterval(long analyzeInterval) {
		this.analyzeInterval = analyzeInterval;
	}

	/**
	 * ȡ��ͳ�����ݷ��ʽӿ�
	 * 
	 * @return
	 */
	public StatDataDAO getCollectDataDAO() {
		return collectDataDAO;
	}

	/**
	 * ����ͳ�����ݷ��ʽӿ�
	 * 
	 * @param collectDataDAO
	 */
	public void setCollectDataDAO(StatDataDAO collectDataDAO) {
		this.collectDataDAO = collectDataDAO;
	}

	/**
	 * ȡ��ͳ����ϸ���ݷ��ʽӿ�
	 * 
	 * @return
	 */
	public StatDetailDAO getStatDetailDAO() {
		return statDetailDAO;
	}

	/**
	 * ����ͳ����ϸ���ݷ��ʽӿ�
	 * 
	 * @param statDetailDAO
	 */
	public void setStatDetailDAO(StatDetailDAO statDetailDAO) {
		this.statDetailDAO = statDetailDAO;
	}

	/**
	 * ȡ��ͳ����
	 * 
	 * @return
	 */
	public int[] getItems() {
		return items;
	}

	/**
	 * ����ͳ����
	 * 
	 * @param items
	 */
	public void setItems(int[] items) {
		this.items = items;
	}

	/**
	 * ȡ��ͳ�����ݷ�����
	 * 
	 * @return
	 */
	public DataAnalyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * ����ͳ�����ݷ�����
	 * 
	 * @param analyzer
	 */
	public void setAnalyzer(DataAnalyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * ȡ��ͳ������
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * ����ͳ������
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * ȡ��ͳ�ƿ�ʼʱ��
	 * 
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * ����ͳ�ƿ�ʼʱ��
	 * 
	 * @param startTime
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

}
