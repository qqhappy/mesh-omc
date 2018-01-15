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
 * json��������
 * @author wenhui
 *
 */
public class XJsonUtils {

	private static final String  DATE_FORMAT = "yyyy-MM-dd  ";

	/**
	 * ������ת��Ϊjson����
	 * ����Ĭ�ϵĸ�ʽ�������(dateformat:yyyy-MM-dd hh:mm:ss)
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
	 * ������ת��Ϊjson����
	 * @param obj ��ת������
	 * @param config ��ʽ�������
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
	 * ������ת��Ϊjson�ַ���
	 * ���ڸ�ʽ����Ĭ�ϵĸ�ʽ�������
	 * @param obj
	 * @return
	 */
	public static String object2JsonObjStr(Object obj) {
		if(obj == null) {
			return "";
		}
		//��JSONLib��� ��ΪfastJSON���
		//return object2JsonObject(obj).toString();
		return com.alibaba.fastjson.JSON.toJSONStringWithDateFormat(obj, DATE_FORMAT);
	}
	
	/**
	 * ������ת��Ϊjson�ַ����������Զ����ʽ�������
	 * @param obj ��ת���Ķ���
	 * @param config ��ʽ�������
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
	 * ��json����ת��Ϊʵ�ʶ�Ӧ�Ķ���
	 * @param json json����
	 * @param c  �����Ӧ����
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
	 * ��json��ʽ���ַ���ת��Ϊ����
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
	 * ͨ��jackson������ת��json��
	 * @param obj
	 * @return
	 */
	public static String obj2JSONStrByJackson(Object obj) {
		//��ȡjson��
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
	 * ��ȡ���ȫ·����
	 * @param c
	 * @return
	 */
	public static String getClassName(Class<?> c) {
		return c.getName();
	}
	
	/**
	 * ͨ������������json���ж�Ӧ��ֵ
	 * ��֧�ֶ�����򼯺ϵ�����ֵ���в���
	 * @param jsonStr json�ַ���
	 * @param propertyName ������
	 * @return
	 */
	public static Object getPropetyValueByName(String jsonStr, String propertyName) {
		com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(jsonStr);
		if(!jsonObject.containsKey(propertyName))return null; 
		return jsonObject.get(propertyName);
	}
	
	/**
	 * ������ת��ΪJSONArray
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
	 * ������ת��ΪJSONArray�ַ�����ʽ
	 * @param objs
	 * @return
	 */
	public static String array2JsonArrayStr(Object[] objs) {
		return array2JsonArray(objs).toString();
	}
	
	/**
	 * ��json��ת��Ϊ��������
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
	 * ��json��ת��Map��ʽ
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
	 * ����ת��Ϊjson��ʱ�����ڸ�ʽ���д���Ķ���
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
