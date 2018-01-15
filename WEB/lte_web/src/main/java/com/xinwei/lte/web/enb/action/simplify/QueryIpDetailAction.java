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
 * 根据ipId从IPV4表中查出具体的ip地址
 * 
 * @author zhangqiang
 * 
 */
public class QueryIpDetailAction extends ActionSupport {

	private long moId;

	/**
	 * IP标识
	 */
	private String ipId;

	/**
	 * 根据IP标识查询实际IP
	 * 
	 * @return
	 */
	public String queryIpDetail() { 
		String decimalIpString = null;
		try {
			XEnbBizConfigFacade facade = Util
					.getFacadeInstance(XEnbBizConfigFacade.class);
			XBizTable xBizTable = facade.queryFromEms(moId, "T_IPV4", null);
			List<XBizRecord> records = xBizTable.getRecords();
			for (XBizRecord record : records) {
				Map<String, XBizField> map = record.getFieldMap();
				if (map != null) {
					XBizField field = map.get("u8IPID");
					if (field != null) {
						String value = field.getValue();
						if (ipId.equals(value)) {
							decimalIpString = map.get("au8IPAddr").getValue();
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Util.ajaxSimpleUtil(decimalIpString);
		return NONE;
	}

	public String getIpId() {
		return ipId;
	}

	public void setIpId(String ipId) {
		this.ipId = ipId;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}
}
