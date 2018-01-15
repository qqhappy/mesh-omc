package com.xinwei.lte.web.lte.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.lte.model.OaSmsModel;
import com.xinwei.lte.web.lte.model.SysSyncModel;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.oss.adapter.model.OssAdapterInputMessage;
import com.xinwei.oss.adapter.model.OssAdapterOutputMessage;

public class SysSyncAction extends ActionSupport {

	private Log log = LogFactory.getLog(getClass());

	@Resource
	private OssAdapter ossAdapter;

	private SysSyncModel sysSyncModel;

	private String failedReason;

	public String querySync() {

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			log.debug("SysSyncAction.querySync() -- start ");
			sysSyncModel = new SysSyncModel();
			Map<String, Object> resultMap = ossAdapter.invoke(0xc6, 0x04, map);

			if (resultMap.get("syncSeq") != null) {
				sysSyncModel.setSyncSeq(resultMap.get("syncSeq") + "");
			}
			if (resultMap.get("syncPeriod") != null) {
				sysSyncModel.setSyncPeriod(resultMap.get("syncPeriod") + "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("SysSyncAction.querySync() -- failure ", e);
			failedReason = e.getLocalizedMessage();
		}

		return SUCCESS;
	}

	public String configSync() {
		log.debug("SysSyncAction.configSync() -- start ");
		try {
			// 构造请求消息
			Map<String, Object> data = new HashMap();
			data.put("syncSeq", sysSyncModel.getSyncSeq());
			data.put("syncPeriod", sysSyncModel.getSyncPeriod());

			OssAdapterInputMessage req = new OssAdapterInputMessage(0xc6, 0x03,
					data);
			// 调用适配层
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);
			if (resp.isFailed()) {
				// 应答失败
				failedReason = resp.getReason();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("SysSyncAction.configSync() -- failure ", e);
			failedReason = e.getLocalizedMessage();
		}
		log.debug("SysSyncAction.configSync() -- end ");
		return SUCCESS;
	}

	public SysSyncModel getSysSyncModel() {
		return sysSyncModel;
	}

	public void setSysSyncModel(SysSyncModel sysSyncModel) {
		this.sysSyncModel = sysSyncModel;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}

}
