package com.xinwei.lte.web.enb.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcess  implements JsonValueProcessor{
	
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Override
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		if (value != null) {
			DateFormat format = new SimpleDateFormat();
			Date[] dates = (Date[]) value;
			String[] obj = new String[dates.length];
			for (int i = 0; i < obj.length; i++) {
				obj[i] = format.format(dates[i]);
			}
			return obj;
		}
		return value;
	}

	@Override
	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		if (value != null) {
			DateFormat format = new SimpleDateFormat(DATE_FORMAT);
			return format.format(value);
		}
		
		return value;
	}
	
	


}
