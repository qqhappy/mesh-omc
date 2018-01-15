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
 * rcpe���û���ҵ���ӿ�ʵ��
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
		// ��û�վģ��
		long btsId = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);

		// ����RCPE����
		TConfRCPE rcpe = queryByMoId(mcBts.getMoId());
		if (rcpe == null) {
			rcpe = new TConfRCPE();
			rcpe.setMoId(mcBts.getMoId());
		} else {
			rcpe.getItems().clear();
		}

		// ���RCPE����
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

		// �������
		RCPEFacade facade = AppContext.getCtx().getBean(RCPEFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, rcpe);
	}

	private TConfRCPE queryFromNE(Long moId) throws Exception {
		return rcpeProxy.query(moId);
	}

	@Override
	public void config(TConfRCPE rcpe) throws Exception {
		// ��֤RCPE�е��ն��Ƿ��ѳ�Ϊ������վ��RCPE
		checkOccupied(rcpe);

		McBts bts = McBtsCache.getInstance().queryByMoId(rcpe.getMoId());
		if (bts == null && rcpe.getMoId() >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// ������״̬�£���Ҫͨ��Proxy��MO����������Ϣ
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
	 * ��֤RCPE�����Ƿ�Ϸ�<br>
	 * 
	 * һ���ն˱�����RCPE��Ͳ��ܱ��κλ�վ����ΪRCPE��RCPE+ <br>
	 * һ���ն˱�ĳ��վ����ΪRCPE+�󣬲��ܱ��κ�������վ����ΪRCPE�������Ա�����ΪRCPE+
	 * 
	 * @throws Exception
	 */
	private void checkOccupied(TConfRCPE rcpe) throws Exception {
		List<TConfRCPEItem> dbItems = rcpeItemDAO.queryAll();
		// ���Ѵ��ڵ����������֤ͨ��
		if (dbItems.size() == 0)
			return;
		// �ظ���RCPE��
		List<TConfRCPEItem> dupRcpeItems = new ArrayList<TConfRCPEItem>();
		// �ظ���RCPE+��
		List<TConfRCPEItem> dupRcpeExItems = new ArrayList<TConfRCPEItem>();
		// ��ȡ������RCPE��
		List<TConfRCPEItem> newRcpeItems = rcpe.getRcpeItems();
		// ��֤������RCPE���Ƿ��ѱ�������վ��ΪRCPE��RCPE+
		for (TConfRCPEItem newItem : newRcpeItems) {
			for (TConfRCPEItem dbItem : dbItems) {
				// ���˻�վ����
				if (dbItem.getId().getParentId().equals(rcpe.getMoId()))
					continue;
				// �������ģ��
				if (dbItem.getId().getParentId() > 0) {
					if (newItem.getrCPEUid().equals(dbItem.getrCPEUid())) {
						dupRcpeItems.add(newItem);
						break;
					}
				}
				// ����RCPE��������������վ��RCPE���ó�ͻ
			}
		}
		// ��ȡ���������RCPE��
		List<TConfRCPEItem> dbRcpeItems = new ArrayList<TConfRCPEItem>();
		for (TConfRCPEItem dbItem : dbItems) {
			if (dbItem.getUIDType().equals(TConfRCPE.CFG_FLAG_RCPE))
				dbRcpeItems.add(dbItem);
		}
		// ��ȡ������RCPE+��
		List<TConfRCPEItem> newRcpeExItems = rcpe.getRcpeExItems();
		// ��֤������RCPE+���Ƿ��ѱ�������վ����ΪRCPE
		for (TConfRCPEItem newItem : newRcpeExItems) {
			for (TConfRCPEItem dbItem : dbRcpeItems) {
				// ���˻�վ����
				if (dbItem.getId().getParentId().equals(rcpe.getMoId()))
					continue;
				// �������ģ��
				if (dbItem.getId().getParentId() > 0) {
					if (newItem.getrCPEUid().equals(dbItem.getrCPEUid())) {
						dupRcpeExItems.add(newItem);
						break;
					}
				}
			}
		}
		// ������ڳ�ͻ��RCPE��
		StringBuilder msgBuilder = new StringBuilder();
		if (dupRcpeItems.size() > 0) {
			msgBuilder.append(OmpAppContext.getMessage("RCPE_item_occupied"));
			msgBuilder.append(to8HexString(dupRcpeItems.get(0).getrCPEUid()));
			for (int i = 1; i < dupRcpeItems.size(); i++) {
				msgBuilder.append(",").append(
						to8HexString(dupRcpeItems.get(i).getrCPEUid()));
			}
		}
		// ������ڳ�ͻ��RCPE+��
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
	 * ɾ��ָ��RCPE��Ϣ
	 * 
	 * @param rcpeItem
	 * @throws Exception
	 */
	@Override
	public void deleteRcpe(TConfRCPEItem rcpeItem) throws Exception {
		rcpeItemDAO.delete(rcpeItem);
	}
}
