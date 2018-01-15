/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-10	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.net;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.model.oamManage.McbtsSupportedBiz;
import com.xinwei.minas.server.mcbts.dao.oamManage.SupportedBizDAO;
import com.xinwei.minas.server.mcbts.service.oamManage.UnsupportedMocCache;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * McBTS自学习服务实现类
 * 
 * @author chenshaohua
 * 
 */

public class McBtsStudyServiceImpl implements McBtsStudyService {

	private SupportedBizDAO supportedBizDAO;

	private SequenceService sequenceService;

	public McBtsStudyServiceImpl() {
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public boolean isSupportedOperation(int btsType, String version, int moc) {
		// 向缓存查询
		return UnsupportedMocCache.getInstance().queryIsSupported(btsType,
				version, moc);
	}

	@Override
	public void addUnsupportedResult(int btsType, String version, int moc,
			Integer value) {
		UnsupportedMocCache.getInstance().addOneRecord(btsType, version, moc, value);
		McbtsSupportedBiz mcbtsSupportedBiz = new McbtsSupportedBiz();
		Long idx = sequenceService.getNext();
		mcbtsSupportedBiz.setIdx(idx);
		mcbtsSupportedBiz.setBtsType(btsType);
		mcbtsSupportedBiz.setSoftwareVersion(version);
		mcbtsSupportedBiz.setMoc(moc);
		mcbtsSupportedBiz.setSupport(value);
		// 更新数据库
		supportedBizDAO.saveOrUpdate(mcbtsSupportedBiz);
	}

	@Override
	public void clearSupportedOperation(int btsType, String version) {
		// 根据btsType和version删除数据库中对应记录
		supportedBizDAO.clearByBtsTypeAndVersion(btsType, version);
		// 清空缓存
		UnsupportedMocCache.getInstance().clearCache(btsType, version);
	}

	public SupportedBizDAO getSupportedBizDAO() {
		return supportedBizDAO;
	}

	public void setSupportedBizDAO(SupportedBizDAO supportedBizDAO) {
		this.supportedBizDAO = supportedBizDAO;
	}

}
