package com.xinwei.oss.adapter.parser.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.xinwei.oss.adapter.model.meta.OssBizItem;
import com.xinwei.oss.adapter.model.meta.OssInner;
import com.xinwei.oss.adapter.parser.IOSSParser;
import com.xinwei.shlr.acc.wrap.msgBody.TLVNestedRecord;

public class TLVParser implements IOSSParser {

	@SuppressWarnings("unchecked")
	@Override
	public Object parse(Object object, OssBizItem item) {
		// 构造后台对象
		TLVNestedRecord tlvNestedRecord = new TLVNestedRecord();
		// 把前台对象转化成后台对象
		Map<String, Object> valueMap = (Map<String, Object>) object;
		Iterator<String> iterator = valueMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			OssInner ossInner = item.getOssInnerByName(key);
			tlvNestedRecord.setValue(ossInner.getTvName(), valueMap.get(key));
		}

		return tlvNestedRecord;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object unParse(Object object, OssBizItem item) {
		if (object instanceof List) {
			// 多个tlv记录----- Map<itemName, List<Map<innerName, inner>>
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			List<TLVNestedRecord> tlvList = (List<TLVNestedRecord>) object;
			for (TLVNestedRecord tlv : tlvList) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 取出各个值
				List<OssInner> innerList = item.getInnerList();
				for (OssInner innerItem : innerList) {
					Object innerValue = tlv.getValue(innerItem.getTag());
					if (innerValue != null) {
						map.put(innerItem.getName(), innerValue);
					}
				}
				mapList.add(map);
			}
			return mapList;
		} else {
			// 单个tlv记录-----Map<itemName, List<Map<innerName, inner>>
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			TLVNestedRecord tlv = (TLVNestedRecord) object;
			Map<String, Object> map = new HashMap<String, Object>();
			// 取出各个值
			List<OssInner> innerList = item.getInnerList();
			for (OssInner innerItem : innerList) {
				Object innerValue = tlv.getValue(innerItem.getTag());
				if (innerValue != null) {
					map.put(innerItem.getName(), innerValue);
				}
			}
			mapList.add(map);
			return mapList;
		}
	}

}
