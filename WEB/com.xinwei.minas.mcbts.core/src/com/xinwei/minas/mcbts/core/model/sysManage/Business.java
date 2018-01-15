package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Business implements Serializable {
	// 业务名称
	private String name;
	// 中文名
	private String desc_zh;
	// 英文名
	private String desc_en;
	// 是否通用业务
	private boolean isGeneric;
	// 业务是否可以通过基站配置导入导出功能配置
	private boolean configurable;
	// 业务字段数
	private volatile int cellNumber = 0;
	// 业务需要访问的dao实现;如果是通用业务,则存储的是GenericBizData的bizName
	private List<String> sources = new ArrayList<String>();

	// 业务中的内容信息,相当于列,String:cellName,
	private List<Map<String, Cell>> cellList = new ArrayList<Map<String, Cell>>();

	private Map<String, Cell> temp = null;

	/**
	 * 初始化一个Business
	 * 
	 * @param name
	 * @param desc_zh
	 * @param desc_en
	 * @param generic
	 */
	public void initBusiness(String name, String desc_zh, String desc_en,
			String generic, String configurable) {
		this.name = name;
		this.desc_zh = desc_zh;
		this.desc_en = desc_en;
		this.isGeneric = generic.equalsIgnoreCase("TRUE");
		this.configurable = configurable.equalsIgnoreCase("TRUE");
	}

	/**
	 * 新增一个数据源(即一个dao或者一个bizName信息)
	 * 
	 * @param value
	 */
	public void addSource(String value) {
		this.sources.add(value);
		cellList.add(temp);

		temp = null;
	}

	/**
	 * 增加一个单元信息
	 * 
	 * @param name
	 * @param desc_zh
	 * @param desc_en
	 */
	public synchronized void addCell(String name, String desc_zh,
			String desc_en, String wireless) {
		Cell cell = new Cell(name.trim(), desc_zh.trim(), desc_en.trim(),
				wireless.trim());

		if (temp == null) {
			temp = new LinkedHashMap<String, Cell>();
		}
		temp.put(cell.getName(), cell);

		cellNumber++;
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

	public boolean isGeneric() {
		return isGeneric;
	}

	public void setGeneric(boolean isGeneric) {
		this.isGeneric = isGeneric;
	}

	public boolean isConfigurable() {
		return configurable;
	}

	@Override
	public String toString() {
		return "name:" + this.name + ", desc_zh:" + this.desc_zh + ", desc_en"
				+ this.desc_en;

	}

	public int getCellNumber() {
		return cellNumber;
	}

	public List<String> getSources() {
		return sources;
	}

	public Set<String> getCellNames() {
		Set<String> keySets = new LinkedHashSet<String>();
		for (Map<String, Cell> cellMap : cellList) {
			Set<String> keys = cellMap.keySet();
			keySets.addAll(keys);
		}
		return keySets;
	}

	public List<Cell> getCellList() {
		List<Cell> list = new LinkedList<Cell>();
		for (Map<String, Cell> cellMap : cellList) {
			list.addAll(cellMap.values());
		}
		return list;
	}

	public Cell getCell(String cellName) {
		for (Map<String, Cell> cellMap : cellList) {
			Cell cell = cellMap.get(cellName);
			if (cell != null)
				return cell;
		}
		return null;
	}

	public Map<String, Cell> getCellMap() {
		Map<String, Cell> map = new LinkedHashMap<String, Cell>();

		for (Map<String, Cell> cellMap : cellList) {
			map.putAll(cellMap);
		}
		return map;
	}

	public List<Cell> getCellListByOperName(String operName) {
		if (operName == null || operName.isEmpty()) {
			return null;
		}

		for (int i = 0; i < sources.size(); i++) {
			if (sources.get(i).equals(operName)) {
				return new LinkedList<Cell>(this.cellList.get(i).values());
			}
		}
		return null;
	}

}
