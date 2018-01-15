/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.facade;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.facade.EnbTypeDDFacade;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB类型数据字段FacadeImpl
 * 
 * @author chenjunhua
 * 
 */

public class EnbTypeDDFacadeImpl implements EnbTypeDDFacade {

	@Override
	public List<EnbTypeDD> queryAllTypeDDs() throws Exception {
		List<EnbTypeDD> typeDDs = new LinkedList();
		Map<Integer, String> typeMap = EnbTypeDD.getSupportedTypeMap();
		Iterator<Integer> itr = typeMap.keySet().iterator();
		while (itr.hasNext()) {
			try {
				Integer enbTypeId = itr.next();
				String enbTypeName = OmpAppContext.getMessage("eNB.type."
						+ enbTypeId);
				EnbTypeDD typeDD = new EnbTypeDD(enbTypeId, enbTypeName);
				typeDDs.add(typeDD);
			} catch (Exception e) {
			}
		}
		return typeDDs;
	}

	@Override
	public EnbTypeDD queryTypeDDById(int enbTypeId) throws Exception {
		try {
			String enbTypeName = OmpAppContext.getMessage("eNB.type."
					+ enbTypeId);
			EnbTypeDD typeDD = new EnbTypeDD(enbTypeId, enbTypeName);
			return typeDD;
		} catch (Exception e) {
		}
		return null;
	}

}
