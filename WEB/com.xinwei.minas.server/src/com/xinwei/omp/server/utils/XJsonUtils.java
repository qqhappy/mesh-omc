/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.server.utils;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.JSONUtils;

import org.apache.commons.beanutils.PropertyUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;




/**
 * json处理工具类
 * @author wenhui
 *
 */
public class XJsonUtils {

	private static final String  DATE_FORMAT = "yyyy-MM-dd  ";

	/**
	 * 将对象转换为json对象
	 * 采用默认的格式处理对象(dateformat:yyyy-MM-dd hh:mm:ss)
	 * @param obj
	 * @return
	 */
	@Deprecated
	public static JSONObject object2JsonObject(Object obj) {
		if(obj == null) {
			return null;
		}
		return JSONObject.fromObject(obj, getDateJsonConfig());
	}
	
	
	public static Object  object2JsonObjectByFast(Object obj) {
		if(obj == null) {
			return null;
		}
		return com.alibaba.fastjson.JSON.toJSON(obj);
	}
	
	/**
	 * 将对象转换为json对象
	 * @param obj 被转换对象
	 * @param config 格式处理对象
	 * @return
	 */
	@Deprecated
	public static JSONObject object2JsonObject(Object obj, JsonConfig config) {
		if(obj == null) {
			return null;
		}
		return JSONObject.fromObject(obj, config);
	}
	
	/**
	 * 将对象转换为json字符串
	 * 日期格式采用默认的格式处理对象
	 * @param obj
	 * @return
	 */
	public static String object2JsonObjStr(Object obj) {
		if(obj == null) {
			return "";
		}
		//将JSONLib框架 改为fastJSON框架
		//return object2JsonObject(obj).toString();
		return com.alibaba.fastjson.JSON.toJSONStringWithDateFormat(obj, DATE_FORMAT);
	}
	
	/**
	 * 将对象转换为json字符串，传入自定义格式处理对象
	 * @param obj 被转换的对象
	 * @param config 格式处理对象
	 * @return
	 */
	@Deprecated
	public static String object2JsonObjStr(Object obj, JsonConfig config) {
		if(obj == null) {
			return "";
		}
		return JSONObject.fromObject(obj, config).toString();
	}
	
	/**
	 * 将json对象转换为实际对应的对象
	 * @param json json对象
	 * @param c  对象对应的类
	 * @return
	 */
	@Deprecated
	public static <T> T json2Object(JSONObject json, Class<T> c) {
		if(json == null) {
			return null;
		}
		return c.cast(JSONObject.toBean(json, c));
	}
	
	/**
	 * 将json格式的字符串转换为对象
	 * @param JsonStr
	 * @return
	 */
	public static<T> T jsonObjStr2Object(String jsonStr, Class<T> c) {
		if(jsonStr == null) {
			return null;
		}
		//JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		//JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[]{DATE_FORMAT}));
		//return c.cast(JSONObject.toBean(jsonObject, c));
		return com.alibaba.fastjson.JSON.parseObject(jsonStr, c);
	}
	
	/**
	 * 通过jackson将对象转成json串
	 * @param obj
	 * @return
	 */
	public static String obj2JSONStrByJackson(Object obj) {
		//获取json串
		ObjectMapper mapper = new ObjectMapper();
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		mapper.setDateFormat(dateFormat);
		String jsonStr = null;
		try {
			jsonStr = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}
	/**
	 * 获取类的全路径名
	 * @param c
	 * @return
	 */
	public static String getClassName(Class<?> c) {
		return c.getName();
	}
	
	/**
	 * 通过属性名查找json串中对应的值
	 * 不支持对数组或集合的属性值进行查找
	 * @param jsonStr json字符串
	 * @param propertyName 属性名
	 * @return
	 */
	public static Object getPropetyValueByName(String jsonStr, String propertyName) {
		com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(jsonStr);
		if(!jsonObject.containsKey(propertyName))return null; 
		return jsonObject.get(propertyName);
	}
	
	/**
	 * 将数组转换为JSONArray
	 * @param objs
	 * @return
	 */
	public static JSONArray array2JsonArray(Object[] objs) {
		if(objs == null) {
			return null;
		}
		return JSONArray.fromObject(objs);
	}
	
	/**
	 * 将数组转换为JSONArray字符串形式
	 * @param objs
	 * @return
	 */
	public static String array2JsonArrayStr(Object[] objs) {
		return array2JsonArray(objs).toString();
	}
	
	/**
	 * 将json串转换为对象数组
	 * @param jsonArrayStr
	 * @param c
	 * @return
	 */
	public static<T> T[] jsonArrayStr2Array(String jsonArrayStr, Class<T> c) {
		JSONArray jsonArray = JSONArray.fromObject(jsonArrayStr);
		return (T[])JSONArray.toArray(jsonArray, c);
	}
	
	
	public static<T> String list2JsonStr(List<T> list) {
		return JSONArray.fromObject(list).toString();
	}
	
	public static<T> List<T> jsonStr2List(String str, Class<T> c) {
		T[] t = jsonArrayStr2Array(str, c);
		return Arrays.asList(t);
	}

	
	/**
	 * 将json串转成Map形式
	 * @param jsonStr
	 * @param clazzMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, ?> jsonstr2Map(String jsonStr, Map<String, Class<?>> clazzMap) {
		JSONObject jsonObj = JSONObject.fromObject(jsonStr);
		return (Map<String, ?>) JSONObject.toBean(jsonObj, Map.class, clazzMap);
	}
	
	
	/**
	 * 对象转换为json串时对日期格式进行处理的对象
	 * @return
	 */
	private static JsonConfig getDateJsonConfig() {
		JsonConfig jsonConfig = new JsonConfig();       
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {        
			public Object processArrayValue(Object value, JsonConfig jsonConfig) {            
				if (value != null) {
					DateFormat format = new SimpleDateFormat(DATE_FORMAT);
					Date[] dates = (Date[])value;
					String[] obj = new String[dates.length];
					for(int i=0; i<obj.length; i++) {
						obj[i] = format.format(dates[i]);
					}
					return obj;
				}            
				return value;        
			}         
			public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {            
			    if(value != null) {
			    	DateFormat format = new SimpleDateFormat(DATE_FORMAT);
			    	return format.format(value);
				}
			    return value;
			}
		});
		return jsonConfig;
	}
	

}
