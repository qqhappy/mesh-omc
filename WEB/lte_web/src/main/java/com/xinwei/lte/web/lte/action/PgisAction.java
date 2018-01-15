package com.xinwei.lte.web.lte.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.lte.model.OaSmsModel;
import com.xinwei.lte.web.lte.model.PgisModel;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.oss.adapter.model.OssAdapterInputMessage;
import com.xinwei.oss.adapter.model.OssAdapterOutputMessage;

public class PgisAction extends ActionSupport {

	private Log log = LogFactory.getLog(getClass());

	@Resource
	private OssAdapter ossAdapter;

	private PgisModel pgisModel;

	private String failedReason;

	public String queryPgis() {

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			log.debug("PgisAction.queryPgis() -- start ");
			pgisModel = new PgisModel();
			Map<String, Object> resultMap = ossAdapter.invoke(0xc5, 0x04, map);

			if (resultMap.get("pgisAuth") != null) {
				pgisModel.setPgisAuth(resultMap.get("pgisAuth") + "");
			}
			if (resultMap.get("pgisIp") != null) {
				pgisModel.setPgisIp(resultMap.get("pgisIp") + "");
			}
			if (resultMap.get("pgisPort") != null) {
				pgisModel.setPgisPort(resultMap.get("pgisPort") + "");
			}
			if (resultMap.get("pgisStatus") != null) {
				pgisModel.setPgisStatus(resultMap.get("pgisStatus") + "");
			}
			if (resultMap.get("comment") != null) {
				pgisModel.setComment(resultMap.get("comment") + "");
			}
		} catch (Exception e) {
				e.printStackTrace();
				log.error("PgisAction.queryPgis() -- failure ", e);
				failedReason = e.getLocalizedMessage();
		}

		return SUCCESS;
	}

	public String configPgis() {
		log.debug("PgisAction.configPgis() -- start ");
		try {
			// 构造请求消息
			Map<String, Object> data = new HashMap();
			data.put("pgisAuth", pgisModel.getPgisAuth());
			data.put("pgisIp", pgisModel.getPgisIp());
			data.put("pgisPort", pgisModel.getPgisPort());
			data.put("comment", pgisModel.getComment());

			OssAdapterInputMessage req = new OssAdapterInputMessage(0xc5, 0x03,
					data);
			// 调用适配层
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);
			if (resp.isFailed()) {
				// 应答失败
				failedReason = resp.getReason();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PgisAction.configPgis() -- failure ", e);
			failedReason = e.getLocalizedMessage();
		}
		log.debug("PgisAction.configPgis() -- end ");
		return SUCCESS;
	}



	public PgisModel getPgisModel() {
		return pgisModel;
	}

	public void setPgisModel(PgisModel pgisModel) {
		this.pgisModel = pgisModel;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}

}
