/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-1-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.generic;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.xinwei.omp.core.utils.ReflectUtils;

/**
 * 
 * 通用模型
 * 
 * @author chenjunhua
 * 
 */

public abstract class GenericModel implements java.io.Serializable {

	private static final String CLAZZ = "clazz";

	/**
	 * 将输入字符串转换为模型
	 * 
	 * @param input
	 */
	public static GenericModel toModel(String input) {
		// 
		Map<String, String> map = new HashMap();
		StringTokenizer st = new StringTokenizer(input, ", ");
		while (st.hasMoreTokens()) {
			String nameAndValue = st.nextToken();
			StringTokenizer st2 = new StringTokenizer(nameAndValue, "= ");
			String name = st2.nextToken();
			String value = st2.nextToken();
			map.put(name, value);
		}

		String className = map.get(CLAZZ);
		try {
			Class clazz = Class.forName(className);
			GenericModel model = (GenericModel) clazz.newInstance();
			List<Method> methods = ReflectUtils.findSetMethods(clazz);
			for (Method method : methods) {
				// 参数类型
				Class fieldType = method.getParameterTypes()[0];
				String fieldName = ReflectUtils.getFiledBySetMethod(method
						.getName());
				String fieldValue = map.get(fieldName);
				Object value = null;
				String fieldTypeName = fieldType.getName();
				if (fieldTypeName.equalsIgnoreCase("java.lang.Long")
						|| fieldTypeName.equalsIgnoreCase("long")) {
					value = Long.parseLong(fieldValue);
				} else if (fieldTypeName.equalsIgnoreCase("java.lang.Integer")
						|| fieldTypeName.equalsIgnoreCase("int")) {
					value = Integer.parseInt(fieldValue);
				} else if (fieldTypeName.equalsIgnoreCase("java.lang.Short")
						|| fieldTypeName.equalsIgnoreCase("short")) {
					value = Short.parseShort(fieldValue);
				} else if (fieldTypeName.equalsIgnoreCase("byte")) {
					value = Byte.parseByte(fieldValue);
				} else if (fieldTypeName.equalsIgnoreCase("java.lang.String")) {
					value = fieldValue;
				}
				method.invoke(model, new Object[] { value });
			}
			return model;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将模型转换为字符串
	 * 
	 * @return
	 */
	public String toModelString() {
		StringBuilder buf = new StringBuilder();
		Class clazz = this.getClass();
		String className = clazz.getCanonicalName();
		buf.append(CLAZZ).append("=").append(className);
		List<Method> methods = ReflectUtils.findGetMethods(clazz);
		for (Method method : methods) {
			String fieldName = ReflectUtils.getFiledByGetMethod(method
					.getName());
			if (fieldName.equals("class")) {
				continue;
			}
			try {
				Object fieldValue = method.invoke(this);
				buf.append(", ").append(fieldName).append("=").append(
						fieldValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		String input = "clazz=com.xinwei.minas.core.model.test.GenericTestModel, btsId=1, btsName=abcde, i1=-102, i2=101, l1=92, s1=2, s2=3, b1=85";
		GenericModel model = GenericModel.toModel(input);
		String modelString = model.toModelString();
		System.out.println(modelString);
	}

}
