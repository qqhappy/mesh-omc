/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-13	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.utils;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * 反射助手
 * 
 * @author chenjunhua
 * 
 */

public class ReflectUtils {

	/**
	 * 查找类中指定方法名的所有方法
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	public static Method findMethod(Class clazz, String methodName) {
		List<Method> methods = findMethods(clazz, methodName);
		if (methods != null && !methods.isEmpty()) {
			return methods.get(0);
		}
		return null;
	}
	
	
	/**
	 * 查找类中指定方法名的所有方法
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	public static List<Method> findMethods(Class clazz, String methodName) {
		List<Method> methods = new LinkedList();
		Method[] _methods = clazz.getDeclaredMethods();
		for (Method method : _methods) {
			if (method.getName().equals(methodName)) {
				methods.add(method);
			}
		}
		return methods;
	}
	
	
	/**
	 * 查找实体内指定开始方法名的所有方法
	 * 
	 * @param entity
	 * @param startChar
	 * @return
	 */
	public static List<Method> findMethodsWithStartName(Class entityClazz, String startChar) {
		List<Method> methods = new LinkedList();
		findMethods(entityClazz, startChar, methods);
		return methods;
	}
	
	/**
	 * 查找实体内指定开始方法名的所有方法
	 * 
	 * @param entity
	 * @param startChar
	 * @return
	 */
	public static List<Method> findMethodsWithStartName(Object entity, String startChar) {
		List<Method> methods = new LinkedList();
		findMethods(entity.getClass(), startChar, methods);
		return methods;
	}
	
	
	/**
	 * 通过GET方法获取属性名
	 * @param methodName
	 * @return
	 */
	public static String getFiledByGetMethod(String methodName) {
		int from = "get".length();
		String fieldName = methodName.substring(from, from + 1)
				.toLowerCase()
				+ methodName.substring(from + 1);
		return fieldName;
	}
	
	
	/**
	 * 通过SET方法获取属性名
	 * @param methodName
	 * @return
	 */
	public static String getFiledBySetMethod(String methodName) {
		int from = "set".length();
		String fieldName = methodName.substring(from, from + 1)
				.toLowerCase()
				+ methodName.substring(from + 1);
		return fieldName;
	}

	
	
	/**
	 * 递归查找实体内指定开始方法名的所有方法
	 * @param entity
	 * @param startChar
	 * @param methods
	 */
	private static  void findMethods(Class clazz, String startChar, List<Method> methods) {
		Method[] _methods = clazz.getDeclaredMethods();
		for (Method method : _methods) {
			String methodName = method.getName();
			if (method.getName().startsWith(startChar)) {
				methods.add(method);
			}
		}	
		Class superClass = clazz.getSuperclass();
		if (superClass != null && !superClass.getName().equals("java.lang.Object")) {
			findMethods(superClass, startChar, methods);
		}
		
	}

	/**
	 * 获取getter方法
	 * 
	 * @param entity
	 * @return
	 */
	public static List<Method> findGetMethods(Class entityClazz) {
		return findMethodsWithStartName(entityClazz, "get");
	}
	
	
	/**
	 * 获取setter方法
	 * 
	 * @param entity
	 * @return
	 */
	public static List<Method> findSetMethods(Class entityClazz) {
		return findMethodsWithStartName(entityClazz, "set");
	}
	
	/**
	 * 获取getter方法
	 * 
	 * @param entity
	 * @return
	 */
	public static List<Method> findGetMethods(Object entity) {
		return findMethodsWithStartName(entity, "get");
	}

	/**
	 * 获取setter方法
	 * 
	 * @param entity
	 * @return
	 */
	public static List<Method> findSetMethods(Object entity) {
		return findMethodsWithStartName(entity, "set");
	}
	

}
