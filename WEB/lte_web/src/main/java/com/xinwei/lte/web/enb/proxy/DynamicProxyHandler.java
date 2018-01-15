package com.xinwei.lte.web.enb.proxy;

/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-4	| chenjunhua 	| 	create the file                       
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.lte.web.MinasSession;
import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.facade.MinasServerFacade;
import com.xinwei.minas.core.model.OperNameConstant;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.OperObjectTypeDD;
import com.xinwei.minas.core.model.secu.OperSignature;
import com.xinwei.minas.core.model.secu.syslog.LogParam;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * 客户端门面动态代理
 * 
 * @author chenjunhua
 * 
 */

public class DynamicProxyHandler<T> implements InvocationHandler {

	private Log log = LogFactory.getLog(DynamicProxyHandler.class);

	// 客户端Session ID
	private String sessionId;

	// 被代理对象
	private T target;

	// 被代理对象的Class
	private Class<T> targetInterface;

	private MinasServerFacade minasServerFacade;

	public DynamicProxyHandler(String sessionId, T target,
			Class<T> targetInterface, MinasServerFacade minasServerFacade) {
		this.sessionId = sessionId;
		this.target = target;
		this.targetInterface = targetInterface;
		this.minasServerFacade = minasServerFacade;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		String facadeName = targetInterface.getCanonicalName();
		String methodName = method.getName();

		Object result = null;
		try {

			// 根据方法注解判断是否需要进行鉴权，要记录日志的操作也需要鉴权
			Loggable loggable = method.getAnnotation(Loggable.class);
			if (loggable != null) {
				// 鉴权
				OperSignature signature = getOperSignature(proxy, method, args);
				MinasSession.getInstance().checkPrivilege(sessionId, signature);
			}

			long time1 = System.currentTimeMillis();
			// 进行真正的调用
			result = method.invoke(target, args);

			long time2 = System.currentTimeMillis();
			// 计算耗时
			long usedTime = time2 - time1;

			log.debug("facade name[" + facadeName + "],method name["
					+ methodName + "] use " + usedTime + " ms");

			// 调用成功后，根据方法注解判断是否需要进行日志记录
			if (loggable != null) {
				try {
					logOperation(proxy, method, args);
				} catch (Throwable e) {
					log.error("failed to log operation. facadeName="
							+ facadeName + ", methodName=" + methodName, e);
				}
			}
		} catch (InvocationTargetException e) {
			Throwable ex = e.getTargetException();
			log.error("failed to invoke. facadeName=" + facadeName
					+ ", methodName=" + methodName, ex);
			throw ex;
		}
		return result;
	}

	/**
	 * 记录操作日志
	 * 
	 * @param proxy
	 * @param method
	 * @param args
	 * @throws Throwable
	 */
	private void logOperation(Object proxy, Method method, Object[] args)
			throws Throwable {
		String facadeName = targetInterface.getCanonicalName();
		String methodName = method.getName();

		// 第一个参数为OperObject, 需要把OperObject移除
		OperObject operObject = null;
		Object[] params = args;
		if (args != null && args[0] != null && args[0] instanceof OperObject) {
			operObject = (OperObject) args[0];
			int length = args.length - 1;
			// 复制参数列表
			params = new Object[length];
			if (length > 0) {
				System.arraycopy(args, 1, params, 0, args.length - 1);
			}
		}
		// 验证权限需要知道facade和method对应的operAction
		OperSignature signature = new OperSignature();
		signature.setFacade(facadeName);
		signature.setMethod(methodName);
		// 如果是特殊业务，进行处理
		handleSpecialBiz(operObject, signature);
		if (facadeName
				.equals("com.xinwei.minas.core.facade.conf.XMoBizConfigFacade")) {
			// TODO:数据同步特殊处理
			if (methodName.equals("compareAndSyncEmsDataToNe")
					|| methodName.equals("compareAndSyncNeDataToEms")) {
				long moId = Long.valueOf(args[0].toString());
				operObject = OperObject.createEnbOperObject(minasServerFacade
						.getFacade(sessionId, EnbBasicFacade.class)
						.queryByMoId(moId).getHexEnbId());
			} else {
				// 如果是通用业务数据的操作，需要获取bizName
				signature.setGenericFlag(true);
				signature.setBizName(getBizName(args));
				long moId = Long.valueOf(args[0].toString());
				operObject = OperObject.createEnbOperObject(minasServerFacade
						.getFacade(sessionId, EnbBasicFacade.class)
						.queryByMoId(moId).getHexEnbId());
			}
		} else if (facadeName
				.equals("com.xinwei.minas.mcbts.core.facade.McBtsBizFacade")) {

			// 如果是通用业务数据的操作，需要获取bizName
			signature.setGenericFlag(true);
			signature.setBizName(getBizName(args));

		} else if (facadeName
				.equals("com.xinwei.minas.sxc.core.facade.SxcBasicFacade")) {
			// SAG(SAG ID在后台特殊处理)
			operObject = OperObject.createSagOperObject(0L);
		} else if (facadeName.startsWith("com.xinwei.minas.zk")) {
			// NK集群
			operObject = OperObject.createNkOperObject();
			return;
		} else if (facadeName
				.equals("com.xinwei.minas.core.facade.alarm.AlarmFacade")) {
			// 告警
			operObject = OperObject.createAlarmOperObject();
		} else if (facadeName
				.equals("com.xinwei.minas.core.facade.secu.UserSecuFacade")) {
			// 安全
			operObject = OperObject.createUserOperObject();
		}
		if (operObject == null) {
			log.error("operObject is null. " + signature.toString());
			return;
		}
		// 记录日志
		MinasSession.getInstance().addLog(
				new LogParam(sessionId, signature, operObject, params));
	}

	/**
	 * 获取操作签名
	 * 
	 * @param proxy
	 * @param method
	 * @param args
	 * @return
	 */
	private OperSignature getOperSignature(Object proxy, Method method,
			Object[] args) {

		String facadeName = targetInterface.getCanonicalName();
		String methodName = method.getName();
		// 验证权限需要知道facade和method对应的operAction
		OperSignature signature = new OperSignature();
		signature.setFacade(facadeName);
		signature.setMethod(methodName);

		OperObject operObject = null;
		if (args != null && args[0] != null && args[0] instanceof OperObject) {
			operObject = (OperObject) args[0];
		}

		// 如果是特殊业务，进行处理
		handleSpecialBiz(operObject, signature);
		if (facadeName
				.equals("com.xinwei.minas.core.facade.conf.XMoBizConfigFacade")) {
			// 如果是通用业务数据的操作，需要获取bizName
			signature.setGenericFlag(true);
			signature.setBizName(getBizName(args));
		} else if (facadeName
				.equals("com.xinwei.minas.mcbts.core.facade.McBtsBizFacade")) {

			// 如果是通用业务数据的操作，需要获取bizName
			signature.setGenericFlag(true);
			signature.setBizName(getBizName(args));
		}
		return signature;
	}

	/**
	 * 处理特殊的业务 <br>
	 * 系统有效频点、系统有效频点下发、数据包过滤准则配置、数据包过滤准则下发
	 * 
	 * @param operObject
	 * @param signature
	 * @return
	 */
	private void handleSpecialBiz(OperObject operObject, OperSignature signature) {
		// 系统有效频点
		if (signature.getFacade().equals(
				"com.xinwei.minas.mcbts.core.facade.layer3.SysFreqFacade")) {
			// 如果操作对象是系统，说明是系统管理
			if (operObject.getObjectType().equals(OperObjectTypeDD.SYSTEM)) {
				signature.setBizName(OperNameConstant.SYS_FREQ_CONFIG);
			} else if (operObject.getObjectType()
					.equals(OperObjectTypeDD.MCBTS)) {
				signature.setBizName(OperNameConstant.SYS_FREQ_PUSHDOWN);
			}
			signature.setSpecialFlag(true);
		} else if (signature
				.getFacade()
				.equals("com.xinwei.minas.mcbts.core.facade.layer3.DataPackageFilterFacade")) {
			if (operObject.getObjectType().equals(OperObjectTypeDD.SYSTEM)) {
				signature
						.setBizName(OperNameConstant.DATA_PACKAGE_FILTER_CONFIG);
			} else if (operObject.getObjectType()
					.equals(OperObjectTypeDD.MCBTS)) {
				signature
						.setBizName(OperNameConstant.DATA_PACKAGE_FILTER_PUSHDOWN);
			}
			signature.setSpecialFlag(true);
		}
	}

	/**
	 * 获取调用参数中的业务名称
	 * 
	 * @param args
	 * @return
	 */
	private String getBizName(Object[] args) {
		// 对于McBtsBizFacade
		for (Object obj : args) {
			if (obj instanceof GenericBizData) {
				GenericBizData data = (GenericBizData) obj;
				return data.getBizName();
			}
		}
		// 对于XMoBizConfigFacade
		for (Object obj : args) {
			if (obj instanceof String) {
				return obj.toString();
			}
		}
		return "";
	}

	/**
	 * 获取指定对象的动态代理
	 * 
	 * @param sessionId
	 * @param target
	 * @param targetInterface
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T getProxy(String sessionId, T target,
			Class<T> targetInterface, MinasServerFacade minasServerFacade) {
		DynamicProxyHandler handler = new DynamicProxyHandler<T>(sessionId,
				target, targetInterface, minasServerFacade);
		return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
				target.getClass().getInterfaces(), handler);
	}

	// /**
	// * 获取指定对象数组的打印字符串
	// *
	// * @param args
	// * @return
	// */
	// private String getPrintString(Object[] args) {
	// StringBuilder buf = new StringBuilder();
	// if (args != null && args.length > 0) {
	// for (Object obj : args) {
	// buf.append("[" + obj + "], ");
	// }
	// }
	// return buf.toString();
	// }

}
