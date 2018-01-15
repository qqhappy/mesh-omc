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

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.VlanFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlan;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlanAttach;
import com.xinwei.minas.mcbts.core.model.layer3.WrappedVlan;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.layer3.VlanDAO;
import com.xinwei.minas.server.mcbts.proxy.layer3.QinQProxy;
import com.xinwei.minas.server.mcbts.proxy.layer3.VlanProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.VlanService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * @author chenshaohua
 * 
 */
public class VlanServiceImpl implements VlanService {

	private Log log = LogFactory.getLog(VlanServiceImpl.class);

	private VlanDAO vlanDAO;

	private VlanProxy vlanProxy;

	private QinQProxy qinQProxy;

	public List<McBtsVlan> queryAllByMoId(Long moId) throws Exception {
		return vlanDAO.queryAllByMoId(moId);
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		for (long moId : moIdList) {
			List<McBtsVlan> vlanList = queryAllByMoId(moId);
			if (vlanList == null || vlanList.isEmpty())
				continue;

			StringBuilder vlanSb = new StringBuilder();
			for (McBtsVlan vlan : vlanList) {
				vlanSb.append(",{");
				vlanSb.append(
						toJSON("vlanGroup", String.valueOf(vlan.getVlanGroup())))
						.append(",");
				vlanSb.append(toJSON("vlanID", String.valueOf(vlan.getVlanID())));
				vlanSb.append("}");
			}
			String content = "\"mcbts_vlan\":[" + vlanSb.substring(1) + "]";
			business.getCell("mcbts_vlan").putContent(moId, content);
		}
	}

	private static String toJSON(String key, String value) {
		return "\"" + key + "\":\"" + value + "\"";
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// 形如[12,212];[32,123]
		// 获得基站模型
		long btsId = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);

		// 创建对象
		List<McBtsVlan> vlanList = new ArrayList<McBtsVlan>();

		// 填充对象
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value))
				continue;

			String[] vlanStrs = value.replaceAll("(\\[|\\])", "").split(";");
			for (String vlanStr : vlanStrs) {
				String[] vlanEles = vlanStr.split(",");

				McBtsVlan vlan = new McBtsVlan();
				vlan.setMoId(mcBts.getMoId());
				vlan.setVlanGroup(Integer.valueOf(vlanEles[0]));
				vlan.setVlanID(Integer.valueOf(vlanEles[0]));

				vlanList.add(vlan);
			}
		}

		// 保存对象
		VlanFacade facade = AppContext.getCtx().getBean(VlanFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, vlanList, mcBts.getMoId());
	}

	@Override
	public McBtsVlanAttach queryAttachByMoId(long moId) throws Exception {
		return vlanDAO.queryAttachByMoId(moId);
	}

	public void config(List<McBtsVlan> mcBtsVlanList, Long moId)
			throws Exception {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			WrappedVlan entity = new WrappedVlan();
			entity.setMcBtsVlanList(mcBtsVlanList);
			// 用来发送vlan消息
			GenericBizData data = new GenericBizData("mcbts_vlan");
			data.addEntity(entity);
			// 发送消息
			try {
				vlanProxy.config(moId, data);
			} catch (Exception e) {
				log.error("Error configuring vlan.", e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		// 保存数据库
		try {
			vlanDAO.persistent(moId, mcBtsVlanList);
		} catch (Exception e) {
			log.error("Error saving vlan data.", e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}
	}

	private List<McBtsVlan> queryFromNE(Long moId) throws Exception {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			GenericBizData data = new GenericBizData("mcbts_vlan");
			List<McBtsVlan> vlanList = vlanProxy.query(moId, data);

			return vlanList;
		}

		return null;
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		List<McBtsVlan> mcBtsVlans = this.queryAllByMoId(moId);
		if (!mcBtsVlans.isEmpty() && mcBtsVlans != null) {
			this.config(mcBtsVlans, moId);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		List<McBtsVlan> vlanList = queryFromNE(moId);

		SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);

		for (McBtsVlan vlan : vlanList) {
			vlan.setMoId(moId);
			vlan.setIdx(sequenceService.getNext());
		}

		// 保存数据库
		try {
			vlanDAO.persistent(moId, vlanList);
		} catch (Exception e) {
			log.error("Error saving vlan data.", e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}
	}

	public VlanDAO getVlanDAO() {
		return vlanDAO;
	}

	public void setVlanDAO(VlanDAO vlanDAO) {
		this.vlanDAO = vlanDAO;
	}

	public VlanProxy getVlanProxy() {
		return vlanProxy;
	}

	public void setVlanProxy(VlanProxy vlanProxy) {
		this.vlanProxy = vlanProxy;
	}

	public QinQProxy getQinQProxy() {
		return qinQProxy;
	}

	public void setQinQProxy(QinQProxy qinQProxy) {
		this.qinQProxy = qinQProxy;
	}

}
