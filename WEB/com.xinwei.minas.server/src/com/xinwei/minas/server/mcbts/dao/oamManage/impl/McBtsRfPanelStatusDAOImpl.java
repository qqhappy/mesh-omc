
package com.xinwei.minas.server.mcbts.dao.oamManage.impl;

import java.util.List;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.dao.DataAccessException;

import com.xinwei.minas.mcbts.core.model.common.McBtsRfPanelStatus;
import com.xinwei.minas.mcbts.core.model.common.McBtsSN;
import com.xinwei.minas.server.mcbts.dao.common.McBtsSNDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.impl.ACLDAOImpl;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;
import com.xinwei.minas.mcbts.core.model.oamManage.McBtsSateQuery;
import com.xinwei.minas.server.mcbts.dao.oamManage.McBtsRfPanelStatusDAO;
import com.xinwei.minas.server.mcbts.dao.oamManage.McBtsStateQueryDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;



/**
 * 
 * McBtsPfPanelStatusDAOImpl µœ÷
 * 
 * @author fangping
 * 
 */

public class McBtsRfPanelStatusDAOImpl extends GenericHibernateDAO<McBtsRfPanelStatus, Long>
		implements McBtsRfPanelStatusDAO {

	private static transient final Log log = LogFactory
			.getLog(McBtsRfPanelStatusDAOImpl.class);

	@Override
	public List<McBtsRfPanelStatus> queryStateQueryFromDB(long moId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveFixedCountRecord(McBtsRfPanelStatus mcbtsstatequery) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public McBtsRfPanelStatus queryNewestRecord(long moId) {
		// TODO Auto-generated method stub
		return null;
	}
}
