/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common.impl;

import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.model.common.CommonChannelSynInfo;
import com.xinwei.minas.server.mcbts.dao.common.CommonChannelSynDAO;
import com.xinwei.minas.server.mcbts.service.common.CommonChannelService;

/**
 * 
 * ¿‡ºÚ“™√Ë ˆ
 * 
 * <p>
 * ¿‡œÍœ∏√Ë ˆ
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class CommonChannelServiceImpl implements CommonChannelService {
	
	private CommonChannelSynDAO commonChannelSynDAO;
	private SequenceService sequenceService;


	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	public void setCommonChannelSynDAO(CommonChannelSynDAO commonChannelSynDAO) {
		this.commonChannelSynDAO = commonChannelSynDAO;
	}

	@Override
	public void saveCommonChannelSynInfos(List<CommonChannelSynInfo> synInfos) {
		if (synInfos != null) {
			for (CommonChannelSynInfo synInfo : synInfos) {
				CommonChannelSynInfo tempSyn = commonChannelSynDAO.queryByMoId(synInfo.getMoId());
				if (tempSyn != null) {
					synInfo.setIdx(tempSyn.getIdx());
				} else {
					synInfo.setIdx(sequenceService.getNext());
				}
				commonChannelSynDAO.saveOrUpdate(synInfo);
			}
		}
	}

}
