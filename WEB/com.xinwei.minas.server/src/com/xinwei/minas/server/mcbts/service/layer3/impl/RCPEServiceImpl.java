package com.xinwei.minas.server.mcbts.service.layer3.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.RCPEFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPE;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItem;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItemPK;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfRCPEDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfRCPEItemDAO;
import com.xinwei.minas.server.mcbts.proxy.layer3.RCPEProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.RCPEService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.server.OmpAppContext;

/**
 * rcpe配置基本业务层接口实现
 * 
 * @author yinbinqiang
 */
public class RCPEServiceImpl implements RCPEService {

	private TConfRCPEDAO rcpedao;
	private TConfRCPEItemDAO rcpeItemDAO;
	private RCPEProxy rcpeProxy;

	public void setRcpedao(TConfRCPEDAO rcpedao) {
		this.rcpedao = rcpedao;
	}

	public void setRcpeItemDAO(TConfRCPEItemDAO rcpeItemDAO) {
		this.rcpeItemDAO = rcpeItemDAO;
	}

	public void setRcpeProxy(RCPEProxy rcpeProxy) {
		this.rcpeProxy = rcpeProxy;
	}

	@Override
	public TConfRCPE queryByMoId(Long moId) throws Exception {
		TConfRCPE rcpe = rcpedao.queryByMoId(moId);
		List<TConfRCPEItem> rcpeItems = rcpeItemDAO.queryByMoId(moId);
		if (rcpeItems != null && rcpeItems.size() != 0) {
			rcpe.setItems(rcpeItems);
		}
		return rcpe;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		for (long moId : moIdList) {
			TConfRCPE rcpe = rcpedao.queryByMoId(moId);
			business.getCell("workMode").putContent(moId,
					toJSON("workMode", String.valueOf(rcpe.getWorkMode())));

			List<TConfRCPEItem> rcpeItems = rcpeItemDAO.queryByMoId(moId);

			StringBuilder rcpeSb = new StringBuilder();
			StringBuilder rcpePlusSb = new StringBuilder();

			for (TConfRCPEItem item : rcpeItems) {
				if (item.getUIDType() == TConfRCPE.CFG_FLAG_RCPE) {
					rcpeSb.append(",{\"eid\":\"").append(item.getrCPEUid())
							.append("\"}");
				} else if (item.getUIDType() == TConfRCPE.CFG_FLAG_RCPEEX) {
					rcpePlusSb.append(",{\"eid\":\"").append(item.getrCPEUid())
							.append("\"}");
				}
			}
			if (rcpeSb.length() > 0) {
				String content = "\"rcpe\":[" + rcpeSb.substring(1) + "]";
				business.getCell("rcpe").putContent(moId, content);
			}
			if (rcpePlusSb.length() > 0) {
				String content = "\"rcpe+\":[" + rcpePlusSb.substring(1) + "]";
				business.getCell("rcpe+").putContent(moId, content);
			}
		}
	}

	private static String toJSON(String key, String value) {
		return "\"" + key + "\":\"" + value + "\"";
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// 获得基站模型
		long btsId = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);

		// 创建RCPE对象
		TConfRCPE rcpe = queryByMoId(mcBts.getMoId());
		if (rcpe == null) {
			rcpe = new TConfRCPE();
			rcpe.setMoId(mcBts.getMoId());
		} else {
			rcpe.getItems().clear();
		}

		// 填充RCPE对象
		Map<String, Cell> cellMap = business.getCellMap();
		int rcpe_count = 0;
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getContentByBID(btsId);

			if (key.equals("workMode")) {
				if (StringUtils.isBlank(value)) {
					return;
				}
				rcpe.setWorkMode(Integer.parseInt(value));
			} else if (key.equals("rcpe")) {
				if (StringUtils.isBlank(value)) {
					continue;
				}

				String[] rs = value.split(";");
				for (rcpe_count = 0; rcpe_count < rs.length; rcpe_count++) {
					String r = rs[rcpe_count];
					TConfRCPEItem item = new TConfRCPEItem();

					item.setId(new TConfRCPEItemPK(mcBts.getMoId(), rcpe_count));
					item.setrCPEUid(Long.parseLong(r.replaceAll("[\\[\\]]", "")));
					item.setUIDType(TConfRCPE.CFG_FLAG_RCPE);

					rcpe.getItems().add(item);
				}
			} else if (key.equals("rcpe+")) {
				if (StringUtils.isBlank(value)) {
					continue;
				}

				String[] rs = value.split(";");
				for (int i = 0; i < rs.length; i++) {
					String r = rs[i];
					TConfRCPEItem item = new TConfRCPEItem();

					item.setId(new TConfRCPEItemPK(mcBts.getMoId(), i
							+ rcpe_count));
					item.setrCPEUid(Long.parseLong(r.replaceAll("[\\[\\]]", "")));
					item.setUIDType(TConfRCPE.CFG_FLAG_RCPEEX);

					rcpe.getItems().add(item);
				}
			}
		}

		// 保存对象
		RCPEFacade facade = AppContext.getCtx().getBean(RCPEFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, rcpe);
	}

	private TConfRCPE queryFromNE(Long moId) throws Exception {
		return rcpeProxy.query(moId);
	}

	@Override
	public void config(TConfRCPE rcpe) throws Exception {
		// 验证RCPE中的终端是否已成为其他基站的RCPE
		checkOccupied(rcpe);

		McBts bts = McBtsCache.getInstance().queryByMoId(rcpe.getMoId());
		if (bts == null && rcpe.getMoId() >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			try {
				rcpeProxy.config(rcpe.getMoId(), rcpe);
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}

		rcpedao.saveOrUpdate(rcpe);
		rcpeItemDAO.removeAll(rcpe.getMoId());
		if (rcpe.getItems() != null && rcpe.getItems().size() != 0) {
			rcpeItemDAO.saveRCPEItems(rcpe.getItems());
		}
	}

	/**
	 * 验证RCPE配置是否合法<br>
	 * 
	 * 一个终端被配置RCPE后就不能被任何基站配置为RCPE或RCPE+ <br>
	 * 一个终端被某基站配置为RCPE+后，不能被任何其他基站配置为RCPE，但可以被配置为RCPE+
	 * 
	 * @throws Exception
	 */
	private void checkOccupied(TConfRCPE rcpe) throws Exception {
		List<TConfRCPEItem> dbItems = rcpeItemDAO.queryAll();
		// 无已存在的配置项，则验证通过
		if (dbItems.size() == 0)
			return;
		// 重复的RCPE项
		List<TConfRCPEItem> dupRcpeItems = new ArrayList<TConfRCPEItem>();
		// 重复的RCPE+项
		List<TConfRCPEItem> dupRcpeExItems = new ArrayList<TConfRCPEItem>();
		// 获取新增的RCPE项
		List<TConfRCPEItem> newRcpeItems = rcpe.getRcpeItems();
		// 验证新增的RCPE项是否已被其他基站加为RCPE或RCPE+
		for (TConfRCPEItem newItem : newRcpeItems) {
			for (TConfRCPEItem dbItem : dbItems) {
				// 过滤基站本身
				if (dbItem.getId().getParentId().equals(rcpe.getMoId()))
					continue;
				// 如果不是模板
				if (dbItem.getId().getParentId() > 0) {
					if (newItem.getrCPEUid().equals(dbItem.getrCPEUid())) {
						dupRcpeItems.add(newItem);
						break;
					}
				}
				// 以下RCPE配置项与其他基站的RCPE配置冲突
			}
		}
		// 获取库里的所有RCPE项
		List<TConfRCPEItem> dbRcpeItems = new ArrayList<TConfRCPEItem>();
		for (TConfRCPEItem dbItem : dbItems) {
			if (dbItem.getUIDType().equals(TConfRCPE.CFG_FLAG_RCPE))
				dbRcpeItems.add(dbItem);
		}
		// 获取新增的RCPE+项
		List<TConfRCPEItem> newRcpeExItems = rcpe.getRcpeExItems();
		// 验证新增的RCPE+项是否已被其他基站配置为RCPE
		for (TConfRCPEItem newItem : newRcpeExItems) {
			for (TConfRCPEItem dbItem : dbRcpeItems) {
				// 过滤基站本身
				if (dbItem.getId().getParentId().equals(rcpe.getMoId()))
					continue;
				// 如果不是模板
				if (dbItem.getId().getParentId() > 0) {
					if (newItem.getrCPEUid().equals(dbItem.getrCPEUid())) {
						dupRcpeExItems.add(newItem);
						break;
					}
				}
			}
		}
		// 如果存在冲突的RCPE项
		StringBuilder msgBuilder = new StringBuilder();
		if (dupRcpeItems.size() > 0) {
			msgBuilder.append(OmpAppContext.getMessage("RCPE_item_occupied"));
			msgBuilder.append(to8HexString(dupRcpeItems.get(0).getrCPEUid()));
			for (int i = 1; i < dupRcpeItems.size(); i++) {
				msgBuilder.append(",").append(
						to8HexString(dupRcpeItems.get(i).getrCPEUid()));
			}
		}
		// 如果存在冲突的RCPE+项
		if (dupRcpeExItems.size() > 0) {
			msgBuilder.append("\r\n").append(
					OmpAppContext.getMessage("RCPEEx_item_occupied"));
			msgBuilder.append(to8HexString(dupRcpeExItems.get(0).getrCPEUid()));
			for (int i = 1; i < dupRcpeExItems.size(); i++) {
				msgBuilder.append(",").append(
						to8HexString(dupRcpeExItems.get(i).getrCPEUid()));
			}
		}
		String msg = msgBuilder.toString();
		if (!StringUtils.isBlank(msg)) {
			throw new Exception(msg);
		}
	}

	public String to8HexString(long id) {
		try {
			String strId = Long.toHexString(id);
			int fillLen = 8 - strId.length();
			for (int i = 0; i < fillLen; i++) {
				strId = "0" + strId;
			}
			return strId;
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		TConfRCPE rCPE = this.queryByMoId(moId);
		if (rCPE != null) {
			this.config(rCPE);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		TConfRCPE result = queryFromNE(moId);
		TConfRCPE dataFromDB = queryByMoId(moId);

		if (dataFromDB == null) {
			SequenceService sequenceService = AppContext.getCtx().getBean(
					SequenceService.class);
			result.setIdx(sequenceService.getNext());
		} else {
			result.setIdx(dataFromDB.getIdx());
		}
		result.setMoId(moId);
		rcpedao.saveOrUpdate(result);

		List<TConfRCPEItem> itemList = result.getItems();
		rcpeItemDAO.removeAll(result.getMoId());

		if (itemList == null || itemList.isEmpty())
			return;

		rcpeItemDAO.saveRCPEItems(itemList);
	}

	/**
	 * 删除指定RCPE信息
	 * 
	 * @param rcpeItem
	 * @throws Exception
	 */
	@Override
	public void deleteRcpe(TConfRCPEItem rcpeItem) throws Exception {
		rcpeItemDAO.delete(rcpeItem);
	}
}
