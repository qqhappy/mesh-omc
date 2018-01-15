package com.xinwei.lte.web.enb.action.simplify;

import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;

/**
 * 用于在单板表中显示光口号
 * @author zhangqiang
 *
 */
public class QueryU8FiberPortForBoardAction extends ActionSupport {
	
	private long moId;
	
	/**
	 * 从机架号
	 */
	private String u8SRackNO;
	
	/**
	 * 根据从机架号查询topo表中对应记录的光口号 
	 * @return
	 */
	public String queryU8FiberPort(){
		String u8FiberPort = null;
		try {
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			XBizTable xBizTable = facade.queryFromEms(moId, "T_TOPO", null);
			List<XBizRecord> records = xBizTable.getRecords();
			for (XBizRecord record : records) {
				Map<String, XBizField> map = record.getFieldMap();
				if (map != null) {
					XBizField field = map.get("u8SRackNO");
					if (field != null) {
						String value = field.getValue();
						if (u8SRackNO.equals(value)) {
							u8FiberPort = map.get("u8FiberPort").getValue();
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Util.ajaxSimpleUtil(u8FiberPort);
		return NONE;
	}
	
	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public String getU8SRackNO() {
		return u8SRackNO;
	}

	public void setU8SRackNO(String u8sRackNO) {
		u8SRackNO = u8sRackNO;
	}
	
	
	
	
}
