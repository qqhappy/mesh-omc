package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.layer3.TConfFaultSwitch;
import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqModule;
import com.xinwei.minas.server.mcbts.dao.layer3.TSysFreqDao;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

public class TSysFreqDAOImpl extends GenericHibernateDAO<TSysFreqModule, Long>
		implements TSysFreqDao {
	private  Log log = LogFactory.getLog(TSysFreqDAOImpl.class);

	public void configBtsData(Long moId, List<TSysFreqModule> sysFreqList) {
		getHibernateTemplate().saveOrUpdate(sysFreqList);

	}

	public void configData(List<TSysFreqModule> sysFreqList, int FreqType) {
		// TODO:没做完
		getHibernateTemplate().saveOrUpdate(sysFreqList);
	}

	public List<TSysFreqModule> queryData(int freqType) {
		List<TSysFreqModule> tt = null;
		try {
			tt = (List<TSysFreqModule>) getHibernateTemplate().find(
					"from TSysFreqModule t where t.freqType = " + freqType);
		} catch (Exception e) {
			log.error(e);
		}
		return tt;
	}

	public void delete(TConfFaultSwitch entity) {
		// TODO 自动生成方法存根

	}

	/**
	 * 查询所有基站频点
	 * 
	 */
	public List<TSysFreqModule> queryAllData() throws Exception {
		List<TSysFreqModule> sysFreq = null;
		try {
			sysFreq = (List<TSysFreqModule>) getHibernateTemplate().find(
					"from TSysFreqModule");
		} catch (Exception e) {
			log.error(e);
		}
		return sysFreq;
	}

	public void saveOrUpdate(TSysFreqModule entity) {
		// TODO Auto-generated method stub

		getHibernateTemplate().saveOrUpdate(entity);
	}

	/**
	 * 删除所有系统有效频点
	 */
	@Override
	public void deleteAll() throws Exception {
		List<TSysFreqModule> all = queryAllData();
		getHibernateTemplate().deleteAll(all);
	}
}
