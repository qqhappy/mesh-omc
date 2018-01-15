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
		// TODO:û����
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
		// TODO �Զ����ɷ������

	}

	/**
	 * ��ѯ���л�վƵ��
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
	 * ɾ������ϵͳ��ЧƵ��
	 */
	@Override
	public void deleteAll() throws Exception {
		List<TSysFreqModule> all = queryAllData();
		getHibernateTemplate().deleteAll(all);
	}
}
