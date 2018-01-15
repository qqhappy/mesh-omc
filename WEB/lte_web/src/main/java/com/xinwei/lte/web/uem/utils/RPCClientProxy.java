package com.xinwei.lte.web.uem.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RPCClientProxy {
	
	private static RPCClientProxy instant = new RPCClientProxy();
	//private final String[] ClassNameArr = {"DataServer","FtpServer","GisCommonManage","GisDevManage","GisEnbManage","GisUemManage","Im6000sdServer","NtpServer","QueryServer","UeManage","UeManageDebug"};
	//private Map<String,RPCClient> RPCClientMap;
	private RPCClient Client;
	private Log log = LogFactory.getLog(RPCClientProxy.class);
	
	public static RPCClientProxy getInstance(){
		return instant;
	}	

	public void setClient(RPCClient client) {
		Client = client;
	}

	public RPCClient getClient() {
		return Client;
	}
	
	public String SendMes(String Model,String MesType,String Mesbody) throws Exception {
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		AbstractInnerMessage ResMes = null;
		
		Mes.setTarget(Model);
		Mes.setMessageId(MesType);
		if(Mesbody !=null){
			Mes.setBody(Mesbody);
		}
		log.info("[SendMes] MesType="+MesType+" Mesbody="+Mesbody);
		try {
			ResMes = Client.InvokeMethod(Mes);
			if(ResMes.getMessageId().equals("ERROR")){
				throw new Exception(ResMes.getBody());
			}
			log.info("[SendMes] ResMes="+ResMes.getBody());
			return ResMes.getBody();
		} catch (Throwable e) {			
			e.printStackTrace();
			throw new Exception("Config响应错误，原因："+e.getLocalizedMessage());
		}
	}
}
