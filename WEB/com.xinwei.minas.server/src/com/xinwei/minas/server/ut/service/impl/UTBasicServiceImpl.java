/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-30	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.server.ut.dao.UTBasicDao;
import com.xinwei.minas.server.ut.proxy.UTBasicProxy;
import com.xinwei.minas.server.ut.service.UTBasicService;
import com.xinwei.minas.ut.core.model.UTCondition;
import com.xinwei.minas.ut.core.model.UTQueryResult;
import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * McBts基本业务服务
 * 
 * 
 * @author tiance
 * 
 */

public class UTBasicServiceImpl implements UTBasicService {

	private UTBasicProxy utBasicProxy;

	private UTBasicDao utBasicDao;

	public UTBasicServiceImpl() {
		utBasicProxy = OmpAppContext.getCtx().getBean(UTBasicProxy.class);
		utBasicDao = OmpAppContext.getCtx().getBean(UTBasicDao.class);
	}

	/**
	 * 按条件向HLR查询终端
	 * 
	 * @param utc
	 * @return
	 * @throws Exception
	 */
	@Override
	public UTQueryResult queryUTByCondition(UTCondition utc) throws Exception {
		UTQueryResult result = utBasicProxy.queryUTByCondition(utc);

		if (result == null)
			return null;

		// 为获得的结果重新排序
//		Collections.sort(result.getUtList(), new UTComparator(utc.getSortBy(),
//				utc.getSortType() == null ? 1 : utc.getSortType()));

		return result;
	}

	/**
	 * 获得某个终端的最新状态
	 * 
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@Override
	public UserTerminal queryUTByPid(String pid) throws Exception {
		UTCondition utc = new UTCondition();
		utc.setPid(pid);
		UTQueryResult result = queryUTByCondition(utc);
		if (result == null)
			return null;

		return result.getUtList().get(0);
	}

	/**
	 * 向数据库查询所有终端类型
	 * 
	 * @return
	 */
	@Override
	public List<TerminalVersion> queryUTTypes() {
		return utBasicDao.queryUTTypes();
	}

	/**
	 * 终端排序算法
	 * 
	 * @author tiance
	 * 
	 */
	private class UTComparator implements Comparator<UserTerminal> {
		private int sortBy;
		private int sortType;

		public UTComparator(int sortBy, int sortType) {
			this.sortBy = sortBy;
			this.sortType = sortType; // 1 or -1
		}

		@Override
		public int compare(UserTerminal ut1, UserTerminal ut2) {
			switch (sortBy) {
			case UTCondition.SORT_BY_NONE:
				return 0;
			case UTCondition.SORT_BY_UID:
				return ut1.getUid().compareTo(ut2.getUid()) * sortType;
			case UTCondition.SORT_BY_TEL_NO:
				// 由于返回结果无电话号码,所以无法排序
				return 0;
			case UTCondition.SORT_BY_PID:
				return ut1.getPid().compareTo(ut2.getPid()) * sortType;
			case UTCondition.SORT_BY_ALIAS:
				return ut1.getAlias().compareTo(ut2.getAlias()) * sortType;
			case UTCondition.SORT_BY_UT_TYPE:
				return ut1.getHwType().compareTo(ut2.getHwType()) * sortType;
			case UTCondition.SORT_BY_UT_STATUS:
//				if (ut1.getStatus() < ut2.getStatus())
//					return -1 * sortType;
//				else if (ut1.getStatus() > ut2.getStatus())
//					return 1 * sortType;
//				return 0;
				return (ut1.getStatus() - ut2.getStatus()) * sortType;
			case UTCondition.SORT_BY_GROUP:
				return ut1.getGroup().compareTo(ut2.getGroup()) * sortType;
			}
			return 0;
		}
	}
}
