/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-16	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.digester.Digester;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.xinwei.hlr.HLRModule;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.OperObjectTypeDD;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.core.model.secu.OperSignature;
import com.xinwei.minas.core.model.secu.syslog.LogParam;
import com.xinwei.oss.adapter.model.OssAdapterInputMessage;
import com.xinwei.oss.adapter.model.OssAdapterOutputMessage;
import com.xinwei.oss.adapter.model.meta.OssAdapterRequest;
import com.xinwei.oss.adapter.model.meta.OssAdapterResponse;
import com.xinwei.oss.adapter.model.meta.OssBiz;
import com.xinwei.oss.adapter.model.meta.OssBizItem;
import com.xinwei.oss.adapter.model.meta.OssInner;
import com.xinwei.oss.adapter.model.meta.OssOperation;
import com.xinwei.oss.adapter.parser.IOSSParser;
import com.xinwei.shlr.acc.wrap.keyProcess.shareKeyProcess.compositeType.CommonEntity;
import com.xinwei.shlr.acc.wrap.keyProcess.shareKeyProcess.compositeType.PropertyConfig;
import com.xinwei.shlr.acc.wrap.msg.MsgProcess;
import com.xinwei.system.action.web.WebConstants;

/**
 * 
 * 
 * @author chenshaohua
 * 
 */
public class OssAdapter {

	// 加载oss等配�?
	private Properties config = new Properties();

	// 从配置文件中加载的业务配置Map
	private Map<String, OssBiz> bizMap;

	public OssAdapter() throws IOException {

	}
	
	/**
	 * 向底层发送消�?
	 * 
	 * @param req
	 *            请求消息
	 * @return 应答消息
	 */
	public OssAdapterOutputMessage invoke(OssAdapterInputMessage req)
			throws Exception {
		OssAdapterOutputMessage resp = new OssAdapterOutputMessage();
		try {
			Map<String, Object> data = this.invoke(req.getOperId(),
					req.getAction(), req.getData());
			resp.setData(data);

			String result = (String) data.get("lteFlag");
			if (StringUtils.isNotEmpty(result)) {
				// 设置结果和失败原�?
				resp.setResult(result);
				if (resp.isFailed()) {
					String reason = LteFlag.flagReturn(result);
					resp.setReason(reason);
				}
			}
			
		} catch (Exception e) {
			resp.setResult("1");
			resp.setReason(e.getLocalizedMessage());
		}
		return resp;
	}

	/**
	 * 向底层发送消�?
	 * 
	 * @param operId操作对象
	 * @param action操作类型
	 *            ，增删改查等
	 * @param data要发送的数据
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> invoke(int operId, int action,
			Map<String, Object> data) throws Exception {
		// 找到对应配置
		OssBiz biz = bizMap.get(operId + "#" + action);
		if (biz == null)
			return null;
		// 鉴权
		checkPrivilege(biz);

		int ossOperObject = biz.getIntOperObject();
		int ossOperType = biz.getIntOperType();
		// 构造commonEntity对象
		CommonEntity commonEntity = new CommonEntity();
		if (data != null) {
			Iterator<String> iterator = data.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				OssBizItem item = biz.getRequest().getItemByName(key);
				if (item.getParser() != null) {
					// 把前台对象转化为后台对象
					IOSSParser ossParser = (IOSSParser) Class.forName(
							item.getParser()).newInstance();
					Object obj = ossParser.parse(data.get(key), item);
					commonEntity.setValue(item.getTag(), obj);
				} else {
					commonEntity.setValue(item.getTag(), data.get(key));
				}	
			}
		}
		// 调用底层通信
		CommonEntity reply = MsgProcess.sendSyncCommand(
				MsgProcess.MESSAGE_TYPE_REQUEST_BUSSINESS, ossOperObject,
				ossOperType, commonEntity);
		// 解析回复的消�?
		Map<String, Object> retMap = new HashMap<String, Object>();
		// 获取对应的item
		List<OssBizItem> responseItemList = biz.getResponse().getItemList();
		for (OssBizItem item : responseItemList) {
			Object value = reply.getValue(item.getTag());
			if (value != null) {
				if (item.getParser() != null) {
					// 把后台对象转化为前台对象
					IOSSParser ossParser = (IOSSParser) Class.forName(
							item.getParser()).newInstance();
					Object obj = ossParser.unParse(value, item);
					retMap.put(item.getName(), obj);
				} else {
					retMap.put(item.getName(), value);
				}
			}
		}

		// 记录操作日志
		logOperation(biz, data);

		return retMap;
	}

	/**
	 * 鉴权
	 * 
	 * @param biz
	 * @throws Exception
	 */
	private void checkPrivilege(OssBiz biz) throws Exception {
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
		OperSignature signature = new OperSignature();
		signature.setFacade(biz.getOperId());
		signature.setMethod(biz.getAction());
		MinasSession.getInstance().checkPrivilege(sessionId, signature);
	}

	/**
	 * 记操作日�?
	 * 
	 * @param biz
	 * @param data
	 */
	private void logOperation(OssBiz biz, Map<String, Object> data) {
		if (!biz.shouldLog()) {
			// 排查不需要记录日志的操作
			return;
		}
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
		OperSignature signature = new OperSignature();
		signature.setFacade(biz.getOperId());
		signature.setMethod(biz.getAction());
		OperObject operObject = null;
		// 获取操作对象类型
		String operObjectType = getOperObjectType(biz);
		if (operObjectType.equals(OperObjectTypeDD.TCN1000)) {
			operObject = OperObject.createTcn1000OperObject();
		} else {
			operObject = OperObject.createTcn1000UtUserOperObject(data);
		}
		LogParam logParam = new LogParam(sessionId, signature, operObject,
				new Object[] { data });
		MinasSession.getInstance().addLog(logParam);

	}
	
	

	/**
	 * 通过业务配置来判断操作对象类�?/br>
	 * <p>
	 * 终端用户operId范围 a0-a5 ad-af b1-b5
	 * </p>
	 * <p>
	 * TCN1000 operId范围a6-ac b0 b3
	 * </p>
	 * 
	 * @param biz
	 * @return
	 */
	private String getOperObjectType(OssBiz biz) {
		int operId = biz.getIntOperId();
		if ((operId >= 160 && operId <= 165)
				|| (operId >= 173 && operId <= 175)
				|| (operId >= 177 && operId <= 181)) {
			return OperObjectTypeDD.UT_USER;
		} else {
			return OperObjectTypeDD.TCN1000;
		}
	}

	public void initialize() throws Exception {
		config.load(OssAdapter.class
				.getResourceAsStream("/conf/oss/oss-config.properties"));
		String ip = config.getProperty("ip");
		String port = config.getProperty("port");
		String language = config.getProperty("language");

		String tvconfigXmlPath = config.getProperty("TVconfigXmlPath");
		// 初始化oss
		PropertyConfig.setConfigFileName(OssAdapter.class.getResource(
				tvconfigXmlPath).getPath());
		HLRModule hlrModule = HLRModule.getInstance();
		try {
			hlrModule.initialize(ip, Integer.parseInt(port), language);
		} catch (Exception e) {
			// eat exception, otherwise system will not start
			e.printStackTrace();
		}

		// 初始化业务列�?
		String adapterXmlPath = config.getProperty("adapterXmlPath");
		List<OssBiz> bizList = new LinkedList<OssBiz>();
		List<File> files = getFiles(OssAdapter.class
				.getResource(adapterXmlPath).getPath());
		for (File file : files) {
			bizList.addAll(loadBiz(file));
		}
		bizMap = new HashMap<String, OssBiz>();
		for (OssBiz bizConfig : bizList) {
			int operId = bizConfig.getIntOperId();
			int actionId = bizConfig.getIntAction();
			bizMap.put(operId + "#" + actionId, bizConfig);
		}
	}



	// 获取目录下的所有xml文件
	private List<File> getFiles(String filesPath) {
		File directory = new File(filesPath);
		List<File> fileList = (List<File>) FileUtils.listFiles(directory,
				new String[] { "xml" }, true);
		return fileList;

	}

	// 加载所有业�?
	private List<OssBiz> loadBiz(File file) throws Exception {
		Digester digester = new Digester();
		List<OssBiz> bizList = new ArrayList<OssBiz>();
		digester.push(bizList);
		digester.setValidating(false);

		digester.addObjectCreate("mapper/biz", OssBiz.class);
		digester.addSetProperties("mapper/biz");

		digester.addObjectCreate("mapper/biz/oss", OssOperation.class);
		digester.addSetProperties("mapper/biz/oss");
		digester.addSetNext("mapper/biz/oss", "setOss");

		digester.addObjectCreate("mapper/biz/request", OssAdapterRequest.class);
		digester.addObjectCreate("mapper/biz/request/item", OssBizItem.class);
		digester.addSetProperties("mapper/biz/request/item");

		digester.addSetNext("mapper/biz/request/item", "addItem");
		digester.addSetNext("mapper/biz/request", "setRequest");
		digester.addObjectCreate("mapper/biz/request/item/inner",
				OssInner.class);
		digester.addSetProperties("mapper/biz/request/item/inner");
		digester.addSetNext("mapper/biz/request/item/inner", "addInner");

		digester.addObjectCreate("mapper/biz/response",
				OssAdapterResponse.class);
		digester.addObjectCreate("mapper/biz/response/item", OssBizItem.class);
		digester.addSetProperties("mapper/biz/response/item");
		digester.addObjectCreate("mapper/biz/response/item/inner",
				OssInner.class);
		digester.addSetProperties("mapper/biz/response/item/inner");
		digester.addSetNext("mapper/biz/response/item/inner", "addInner");
		digester.addSetNext("mapper/biz/response/item", "addItem");
		digester.addSetNext("mapper/biz/response", "setResponse");

		digester.addSetNext("mapper/biz", "add");

		@SuppressWarnings("unchecked")
		List<OssBiz> result = (List<OssBiz>) digester.parse(file);

		return result;

	}

	/**
	 * 获取业务配置的map，key为operId#action，value为对应配�?
	 * 
	 * @return
	 */
	public Map<String, OssBiz> getBizMap() {
		return bizMap;
	}

	/**
	 * 设置业务配置的map，key为operId#action，value为对应配�?
	 * 
	 * @param bizMap
	 */
	public void setBizMap(Map<String, OssBiz> bizMap) {
		this.bizMap = bizMap;
	}

	public static void main(String[] args) {
		try {
			OssAdapter ada = new OssAdapter();
			ada.initialize();
			ada.invoke(0x8f, 0x51, new HashMap<String, Object>());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*******************************************************************************************/

	/********************************************************************************************/

}
