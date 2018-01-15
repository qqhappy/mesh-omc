package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cell implements Serializable {
	// 单元名
	private String name;
	// 单元的描述(翻译)
	private String desc_zh;
	// 单元的描述(翻译)
	private String desc_en;
	// 判断是否是无线侧数据
	private boolean isWirelessData;

	// 显示的内容,导出时:key为moId,导入时:key为btsId
	private Map<Long, String> content = new HashMap<Long, String>();

	public Cell(String name, String desc_zh, String desc_en, String wireless) {
		this.name = name;
		this.desc_zh = desc_zh;
		this.desc_en = desc_en;
		this.isWirelessData = wireless.equalsIgnoreCase("true");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc_zh() {
		return desc_zh;
	}

	public void setDesc_zh(String desc_zh) {
		this.desc_zh = desc_zh;
	}

	public String getDesc_en() {
		return desc_en;
	}

	public void setDesc_en(String desc_en) {
		this.desc_en = desc_en;
	}

	public boolean isWirelessData() {
		return isWirelessData;
	}

	public void setWirelessData(boolean isWirelessData) {
		this.isWirelessData = isWirelessData;
	}

	public Map<Long, String> getContent() {
		return content;
	}

	public String getContentByMoId(long moId) {
		return content.get(moId);
	}

	public String getContentByBID(long bid) {
		return content.get(bid);
	}

	public void putContent(long id, String value) {
		content.put(id, value);
	}

}
