/**
 * 
 */
package com.xinwei.minas.server.mcbts.service.layer3.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.common.util.ConvertUtil;
import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.ACLFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsACL;
import com.xinwei.minas.mcbts.core.model.layer3.WrappedACL;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.layer3.ACLDAO;
import com.xinwei.minas.server.mcbts.proxy.layer3.ACLProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.ACLService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * @author chenshaohua
 * 
 */
public class ACLServiceImpl implements ACLService {
	private Log log = LogFactory.getLog(ACLServiceImpl.class);

	private ACLDAO aCLDAO;

	private ACLProxy aCLProxy;

	public List<McBtsACL> queryByMoId(Long moId) throws Exception {
		return aCLDAO.queryByMoId(moId);
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {

		for (long moId : moIdList) {
			List<McBtsACL> aclList = queryByMoId(moId);
			if (aclList == null || aclList.isEmpty())
				continue;

			StringBuilder aclSb = new StringBuilder();
			for (McBtsACL acl : aclList) {
				aclSb.append(",{");
				aclSb.append(
						toJSON("protocol", String.valueOf(acl.getProtocol())))
						.append(",");
				aclSb.append(
						toJSON("sourceIP",
								ConvertUtil.longToIp(acl.getSourceIP())))
						.append(",");
				aclSb.append(
						toJSON("sourceIPMask",
								ConvertUtil.longToIp(acl.getSourceIPMask())))
						.append(",");
				aclSb.append(
						toJSON("sourcePort",
								String.valueOf(acl.getSourcePort()))).append(
						",");
				aclSb.append(
						toJSON("sourceOper",
								String.valueOf(acl.getSourceOper()))).append(
						",");
				aclSb.append(
						toJSON("desIp", ConvertUtil.longToIp(acl.getDesIp())))
						.append(",");
				aclSb.append(
						toJSON("desIPMask",
								ConvertUtil.longToIp(acl.getDesIPMask())))
						.append(",");
				aclSb.append(
						toJSON("desPort", String.valueOf(acl.getDesPort())))
						.append(",");
				aclSb.append(
						toJSON("desOper", String.valueOf(acl.getDesOper())))
						.append(",");
				aclSb.append(toJSON("permission",
						String.valueOf(acl.getPermission())));
				aclSb.append("}");
			}
			String content = "\"mcbts_acl\":[" + aclSb.substring(1) + "]";
			business.getCell("mcbts_acl").putContent(moId, content);
		}

	}

	private static String toJSON(String key, String value) {
		return "\"" + key + "\":\"" + value + "\"";
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// 形如[0,16843009,16843009,0,0,16843009,16843009,221,0,0];[0,33686018,33686018,0,0,33686037,1879179778,32,0,0]
		// 获得基站模型
		long btsId = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);

		// 创建对象
		List<McBtsACL> aclList = new ArrayList<McBtsACL>();

		// 填充对象
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value))
				continue;

			String[] aclStrs = value.replaceAll("(\\[|\\])", "").split(";");
			for (String aclStr : aclStrs) {
				String[] aclEles = aclStr.split(",");

				McBtsACL acl = new McBtsACL();
				acl.setMoId(mcBts.getMoId());
				acl.setProtocol(Integer.valueOf(aclEles[0]));
				acl.setSourceIP(ConvertUtil.ipToLong(aclEles[1]));
				acl.setSourceIPMask(ConvertUtil.ipToLong(aclEles[2]));
				acl.setSourcePort(Integer.valueOf(aclEles[3]));
				acl.setSourceOper(Integer.valueOf(aclEles[4]));
				acl.setDesIp(ConvertUtil.ipToLong(aclEles[5]));
				acl.setDesIPMask(ConvertUtil.ipToLong(aclEles[6]));
				acl.setDesPort(Integer.valueOf(aclEles[7]));
				acl.setDesOper(Integer.valueOf(aclEles[8]));
				acl.setPermission(Integer.valueOf(aclEles[9]));

				aclList.add(acl);
			}
		}

		// 保存对象
		ACLFacade facade = AppContext.getCtx().getBean(ACLFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, mcBts.getMoId(), aclList);

	}

	private List<McBtsACL> queryFromNE(long moId) throws Exception {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}

		WrappedACL wrappedAcl = null;
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			GenericBizData data = new GenericBizData("mcbts_acl");

			// 发送消息
			try {
				wrappedAcl = aCLProxy.query(moId, data);
			} catch (Exception e) {
				log.error(e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		if (wrappedAcl == null)
			return null;

		return wrappedAcl.getMcBtsACLList();
	}

	public void config(Long moId, List<McBtsACL> mcBtsACLList) throws Exception {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			WrappedACL entity = new WrappedACL();
			entity.setMcBtsACLList(mcBtsACLList);
			GenericBizData data = new GenericBizData("mcbts_acl");
			data.addEntity(entity);
			// 发送消息
			try {
				aCLProxy.config(moId, data);
			} catch (Exception e) {
				log.error(e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}

		// 保存数据库
		try {
			aCLDAO.saveOrUpdate(mcBtsACLList, moId);
		} catch (Exception e) {
			log.error(e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}

	}

	public void setACLDAO(ACLDAO acldao) {
		aCLDAO = acldao;
	}

	public void setACLProxy(ACLProxy proxy) {
		aCLProxy = proxy;
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		List<McBtsACL> mcBtsACLList = this.queryByMoId(moId);
		if (!mcBtsACLList.isEmpty() && mcBtsACLList != null) {
			this.config(moId, mcBtsACLList);
		}

	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		List<McBtsACL> aclList = queryFromNE(moId);

		if (aclList == null || aclList.isEmpty())
			return;

		SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);

		for (McBtsACL acl : aclList) {
			acl.setIdx(sequenceService.getNext());
			acl.setMoId(moId);
		}

		// 保存数据库
		try {
			aCLDAO.saveOrUpdate(aclList, moId);
		} catch (Exception e) {
			log.error(e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}
	}

	@Override
	public void delete(McBtsACL temp) throws Exception {
		aCLDAO.delete(temp);
	}

}
