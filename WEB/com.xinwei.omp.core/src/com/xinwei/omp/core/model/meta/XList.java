/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * List模型
 * 
 * @author chenjunhua
 * 
 */

public class XList implements java.io.Serializable {

	public static final String TYPE_DISPLAY_STRING = "DisplayString";

	public static final String TYPE_HEX_ARRAY = "HexArray";

	public static final String TYPE_PASSWORD = "Password";

	public static final String TYPE_IPADDRESS = "IpAddress";

	public static final String TYPE_INTEGER = "INTEGER";

	public static final String TYPE_UNSIGNED32 = "Unsigned32";

	public static final String TYPE_SIGNED32 = "Signed32";

	public static final String TYPE_PLATFORM = "platform";

	public static final String TYPE_BUSINESS = "business";

	public static final String LANG_ZH = "zh";

	public static final String LANG_EN = "en";

	private String name;

	private XItems[] items = new XItems[0];

	private XList[] list = new XList[0];

	// 国际化语言选项
	private String language = "zh";

	// 属性名-值 映射表
	private Map<String, List<String>> propertyValueMap = new HashMap();

	/**
	 * 获取指定字段的元数据定义
	 * 
	 * @param fieldName
	 *            字段名称
	 * @return
	 */
	public XList getFieldMeta(String fieldName) {
		XList[] fieldMetas = this.getList();
		for (XList fieldMeta : fieldMetas) {
			if (fieldMeta.getName().equals(fieldName)) {
				return fieldMeta;
			}
		}
		return null;
	}

	public boolean isString() {
		return isDisplayString() || isPassword();
	}

	public boolean isUnsignedNum() {
		return isUnsigned32() || isEnum();
	}

	public boolean isSignedNum() {
		return isSigned32();
	}

	public boolean isDisplayString() {
		String type = getType();
		return TYPE_DISPLAY_STRING.equals(type);
	}

	public boolean isHexArray() {
		String type = getType();
		return TYPE_HEX_ARRAY.equals(type);
	}

	public boolean isPassword() {
		String type = getType();
		return TYPE_PASSWORD.equals(type);
	}

	public boolean isIpAddress() {
		String type = getType();
		return TYPE_IPADDRESS.equals(type);
	}

	public boolean isEnum() {
		String type = getType();
		return TYPE_INTEGER.equals(type);
	}

	public boolean isUnsigned32() {
		String type = getType();
		return TYPE_UNSIGNED32.equals(type);
	}

	public boolean isSigned32() {
		String type = getType();
		return TYPE_SIGNED32.equals(type);
	}

	/**
	 * 判断是否是引用字段
	 * 
	 * @return
	 */
	public boolean isRef() {
		return isUnsigned32() && containsRef();
	}

	/**
	 * 判断是否是input字段
	 * 
	 * @return
	 */
	public boolean isInput() {
		return isUnsigned32() && containsInput();
	}

	/**
	 * 判断是否是数值字段
	 * 
	 * @return
	 */
	public boolean isNumber() {
		return isUnsignedNum() || isSignedNum();
	}

	/**
	 * 判断是否是查询域
	 * 
	 * @return
	 */
	public boolean isQueryField() {
		String viewDef = getPropertyValue(P_VIEW);
		if (viewDef.indexOf("QUERY") >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 获取字段的引用元数据定义列表
	 * 
	 * @return
	 */
	public List<XMetaRef> getFieldRefs() {
		List<String> metaRefTextList = getPropertyValues(XList.P_REF);
		List<XMetaRef> metaRefList = XMetaRef.parseRef(metaRefTextList);
		return metaRefList;
	}

	/**
	 * 获取字段的引用表列表
	 * 
	 * @return
	 */
	public List<String> getFieldRefTables() {
		List<String> refTables = new LinkedList();
		List<XMetaRef> metaRefList = getFieldRefs();
		for (XMetaRef metaRef : metaRefList) {
			refTables.add(metaRef.getRefTable());
		}
		return refTables;
	}

	/**
	 * 获取描述信息
	 * 
	 * @return
	 */
	public String getDesc() {
		String descName = P_DESC + "_" + language;
		String desc = getPropertyValue(descName);
		if (desc == null) {
			return "";
		}
		return desc;
	}

	public int getFieldLength() {
		String sLen = getPropertyValue(P_LENGTH);
		if (sLen != null && !"".equals(sLen)) {
			return Integer.parseInt(sLen);
		}
		return 0;
	}

	/**
	 * 获取类型
	 * 
	 * @return
	 */
	public String getType() {
		String fullType = getPropertyValue(P_TYPE);
		String[] result = fullType.trim().split(" +");
		String type = result[0];
		return type;
	}
	
	/**
	 * 获取默认值
	 * @return
	 */
	public String getDefault() {
		String defaultValue = getPropertyValue(P_DEFAULT);
		return defaultValue;
	}

	/**
	 * 是否可写
	 * 
	 * @return
	 */
	public boolean isWritable() {
		return !isReadonly();
	}

	/**
	 * 是否只读属性
	 * 
	 * @return
	 */
	public boolean isReadonly() {
		String readonly = getPropertyValue(P_READONLY);
		if (readonly != null) {
			return readonly.equalsIgnoreCase("true");
		}
		return false;
	}

	/**
	 * 获取范围字符串 0..65535
	 * 
	 * @return
	 */
	public String getRangeText() {
		String rangeText = "";
		String fullType = getPropertyValue(P_TYPE);
		String regex = "\\d+\\.{2}\\d+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(fullType);
		if (m.find()) {
			rangeText = m.group();
		}
		return rangeText;
	}
	
	/**
	 * 将0..65535格式的范围解析成int类型数组
	 * @return
	 */
	public int[] getRangeByRangeText() {
		int[] rangeArray = new int[2];
		String rangeText = getRangeText();
		String[] split = rangeText.split("\\.");
		rangeArray[0] = Integer.valueOf(split[0]);
		rangeArray[1] = Integer.valueOf(split[2]);
		return rangeArray;
	}
	
	/**
	 * 将(0..1,2..3,5..6)格式的范围解析
	 * @return
	 */
	public int[] getRange() {
		String fullType = getPropertyValue(P_TYPE);
		char[] charArray = fullType.toCharArray();
		boolean start = false;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < charArray.length; i++) {
			if(')' == charArray[i]) {
				break;
			} else if(start) {
				sb.append(charArray[i]);
			} else if('(' == charArray[i]) {
				start = true;
				continue;
			}
		}
		String rangeText = sb.toString();
		String[] split = rangeText.split(",");
		int[][] rangeArray = new int[split.length][2];
		for (int i = 0; i < split.length; i++) {
			String[] split2 = split[i].split("\\.\\.");
			rangeArray[i][0] = Integer.valueOf(split2[0]);
			rangeArray[i][1] = Integer.valueOf(split2[1]);
		}
		int range[] = new int[2];
		range[0] = rangeArray[0][0];
		range[1] = rangeArray[rangeArray.length - 1][1];
		
		return range;
	}
	
	

	/**
	 * 设置语言 中文:zh, 英文:en
	 * 
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
		for (XList _list : list) {
			_list.setLanguage(language);
		}
	}

	/**
	 * 设置items，同时记录对应的属性
	 * 
	 * @param items
	 */
	public void setItems(XItems[] items) {
		this.items = items;
		XItem[] _items = items[0].getItem();
		for (XItem item : _items) {
			String name = item.getName();
			if (!propertyValueMap.containsKey(name)) {
				propertyValueMap.put(name, new LinkedList());
			}
			List valueList = propertyValueMap.get(name);
			valueList.add(item.getValue());
		}
	}

	/**
	 * 获取动作名称数组
	 * 
	 * @return
	 */
	public String[] getActionNames() {
		String operation = getPropertyValue(P_OPERATION);
		if (operation != null && !operation.isEmpty()) {
			String[] actions = operation.split("\\|");
			int i = 0;
			for (String action : actions) {
				actions[i++] = action.trim();
				return actions;
			}
		}
		return new String[0];
	}

	/**
	 * 判断是否包含指定的动作
	 * 
	 * @param actionName
	 * @return
	 */
	public boolean containsAction(String actionName) {
		String[] actionNames = getActionNames();
		for (String _actionName : actionNames) {
			if (_actionName.equals(actionName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取枚举文本 (0)长号|(1)GTN
	 * 
	 * @return
	 */
	public String getEnumText() {
		String enumText = getPropertyValue(P_ENUM + "_" + language);
		return enumText;
	}
	
	public int[] getEnumRange() {
		String enumText = getEnumText();
		String[] enumArray = enumText.split("\\|");
		int[] enumRange = new int[enumArray.length];
		for (int i = 0; i < enumRange.length; i++) {
			char[] charArray = enumArray[i].trim().toCharArray();
			StringBuilder sb = new StringBuilder();
			boolean start = false;
			for (int j = 0; j < charArray.length; j++) {
				if('(' == charArray[j]) {
					start = true;
					continue;
				}
				if(')' == charArray[j]) {
					break;
				}
				if(start) {
					sb.append(charArray[j]);
				}
			}
			if("".equals(sb.toString())) {
				enumRange[i] = Integer.valueOf(0);
			} else {
				enumRange[i] = Integer.valueOf(sb.toString());
			}
		}
		return enumRange;
	}

	/**
	 * 获取指定属性的第1个值
	 * 
	 * @param property
	 * @return
	 */
	public String getPropertyValue(String property) {
		List<String> values = getPropertyValues(property);
		if (!values.isEmpty()) {
			return values.get(0);
		}
		return null;
	}

	public String getLevel() {
		return getPropertyValue(P_LEVEL);
	}

	/**
	 * 获取指定属性的值列表
	 * 
	 * @param property
	 * @return
	 */
	public List<String> getPropertyValues(String property) {
		if (propertyValueMap.containsKey(property)) {
			return propertyValueMap.get(property);
		} else {
			return new LinkedList();
		}
	}

	/**
	 * 是否包含case
	 * 
	 * @return
	 */
	public boolean containsCase() {
		return !getPropertyValues(XList.P_CASE).isEmpty();
	}

	/**
	 * 是否包含input
	 * 
	 * @return
	 */
	public boolean containsInput() {
		return !getPropertyValues(XList.P_INPUT).isEmpty();
	}

	/**
	 * 是否包含ref
	 * 
	 * @return
	 */
	public boolean containsRef() {
		return !getPropertyValues(XList.P_REF).isEmpty();
	}

	/**
	 * 判断是否界面可见
	 * 
	 * @return
	 */
	public boolean isVisiable() {
		return !isEmpty();
	}

	/**
	 * 判断是否是主键字段
	 * 
	 * @param fieldName
	 * @return
	 */
	public boolean isIndex(String fieldName) {
		List<String> indexNames = getIndexList();
		return indexNames.contains(fieldName);
	}

	public List<String> getIndexList() {
		List<String> list = new LinkedList();
		String indexString = this.getPropertyValue(P_INDEX);
		StringTokenizer st = new StringTokenizer(indexString, ", ");
		while (st.hasMoreTokens()) {
			String indexName = st.nextToken();
			list.add(indexName);
		}
		return list;
	}

	/**
	 * 获取表容量
	 * 
	 * @return
	 */
	public int getTableSize() {
		int defaultSize = 65535;
		String sizeString = this.getPropertyValue(P_SIZE);
		if (sizeString == null || "".equals(sizeString)) {
			return defaultSize;
		}
		try {
			int size = Integer.parseInt(sizeString);
			return size;
		} catch (Exception e) {
			return defaultSize;
		}
	}

	/**
	 * 判断是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return name.equals("empty");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public XItems[] getItems() {
		return items;
	}

	/**
	 * 获取可视化的列表
	 * 
	 * @return
	 */
	public List<XList> getVisiableList() {
		List<XList> visiableList = new LinkedList();
		for (XList _list : list) {
			if (_list.isVisiable()) {
				visiableList.add(_list);
			}
		}
		return visiableList;
	}

	/**
	 * 获取只读的字段列表
	 * 
	 * @return
	 */
	public List<XList> getReadonlyFields() {
		List<XList> visiableList = new LinkedList();
		for (XList _list : list) {
			if (_list.isReadonly()) {
				visiableList.add(_list);
			}
		}
		return visiableList;
	}

	/**
	 * 获取只读的字段列表
	 * 
	 * @return
	 */
	public List<XList> getAllFields() {
		List<XList> visiableList = new LinkedList();
		for (XList _list : list) {
			visiableList.add(_list);
		}
		return visiableList;
	}

	public XList[] getFieldMetaList() {
		return getList();
	}

	public XList[] getList() {
		return list;
	}

	public void setList(XList[] list) {
		this.list = list;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XList other = (XList) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	// 以下是List的属性名常量
	public static final String P_INDEX = "index";

	public static final String P_SIZE = "size";

	public static final String P_OPERATION = "operation";

	public static final String P_TABITEMS = "tabItems";

	public static final String P_TYPE = "type";

	public static final String P_LENGTH = "length";

	public static final String P_READONLY = "readonly";

	public static final String P_DESC = "desc";

	public static final String P_REQUIRE = "require";

	public static final String P_ENABLE = "enable";

	public static final String P_VIEW = "view";

	public static final String P_TAB = "tab";

	public static final String P_ENUM = "enum";

	public static final String P_CASE = "case";

	public static final String P_INPUT = "input";

	public static final String P_REF = "ref";

	public static final String P_CODE_TYPE = "code_type";

	public static final String P_LIMITCHARS = "limitChars";

	public static final String P_DEFAULT = "default";

	public static final String P_DEFAULT_SEND_VALUE = "defaultSendValue";

	public static final String P_LEVEL = "level";

	public static void main(String[] args) {
		String testString = "DisplayString (SIZE(1..16))";
		String regex = "\\d+\\.{2}\\d+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(testString);
		if (m.find()) {
			System.out.println(m.group());
		}
	}
}
