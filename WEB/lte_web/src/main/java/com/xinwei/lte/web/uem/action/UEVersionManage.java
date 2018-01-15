package com.xinwei.lte.web.uem.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xinwei.lte.web.uem.model.ParaConverter;
import com.xinwei.lte.web.uem.model.UEConfigModel;
import com.xinwei.lte.web.uem.utils.AbstractInnerMessage;
import com.xinwei.lte.web.uem.utils.AbstractMessage;
import com.xinwei.lte.web.uem.utils.RPCClient;
import com.xinwei.lte.web.uem.utils.UemConstantUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.mcbts.core.facade.sysManage.McBtsVersionManageFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersion;
import com.xinwei.system.action.web.WebConstants;

public class UEVersionManage extends ActionSupport {	
	private Integer UEID;
	private Integer type;
	private File file;
	private String fileFileName;
	private String Version;
	private LinkedList<Map<String, String>> UEVersionList = new LinkedList<Map<String,String>>(){};
	private String UEIDList;
	private String VerFileList;
	private String IMSI;
	
	private Log log = LogFactory.getLog(UEManage.class);

	private String ueVersionFolder = "/usr/xinwei/wireless/im5000/plugins/enb/ftp/softwareversion/ueload";
	/**
	 * 是否已上传,1：成功 2:失败
	 */
	private int isUploaded;
	private String error = "";
	private RPCClient Client;
	
	public String TurnToUEUpgradeLeft(){	
		return SUCCESS;
	}
	
	
	public String TurnToAddUEVersionPage(){
		return SUCCESS;
	}
	
	public String TurnToQueryUEVersionPage()  throws Exception{
		int index = 0;		
		try{
			AbstractInnerMessage Mes = new AbstractInnerMessage();
			AbstractInnerMessage ResMes = null;
			JSONObject QueryParas = new JSONObject();
			
			Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
			Mes.setMessageId(UemConstantUtils.ACTION_UEVERSION_QUERY);
			
			QueryParas.put("type", type);
			
			Mes.setBody(QueryParas.toString());
			ResMes = SendMes(Mes);
			
			JSONObject ResObj = JSONObject.fromObject(ResMes.getBody());
			if(!ResObj.get("result").equals("success")){
				log.warn("ue version Query result is fail!");
				throw new Exception("UE版本查询失败");			
			}
			
			JSONArray UEVersionJsonArr = (JSONArray)ResObj.get("DataBody");
			Map<String,String> UEVersion = null;		
			
			if(UEVersionJsonArr.isEmpty()){
				log.warn("UE Version Query Result is empty");	
				UEVersionList = null;
			}
			else{
				for(index=0;index<UEVersionJsonArr.size();index++){
					JSONObject UEVersionJson = (JSONObject)(UEVersionJsonArr.get(index));
					UEVersion = new HashMap<String, String>(){};
					InsertKVToMap(UEVersionJson,UEVersion);
					UEVersionList.push(UEVersion);
				}
			}	
			
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
			error = "UE版本查询失败";
			return ERROR;
		} 

		
		return SUCCESS;
	}
	
	/**
	 * 
	 * 上传UE版本接口
	 * @author yangyunyun
	 * 
	 */
	public String uploadUEVersion(){
		
		FileInputStream fin = null;
		boolean res = false;
		try {
			// 将文件转为byte[]
			fin = new FileInputStream(file);
			byte[] bt = new byte[fin.available()];
			fin.read(bt);
			
			res = UploadFile(fileFileName, bt);
			if (!res){
				log.warn("write ue version fail!");
				isUploaded = 2;
				return ERROR;
			}
			log.info("upload ue version file  " + fileFileName);
			//存数据库
			AbstractInnerMessage Mes = new AbstractInnerMessage();
			AbstractInnerMessage ResMes = null;
			JSONObject QueryParas = new JSONObject();
			
			Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
			Mes.setMessageId(UemConstantUtils.ACTION_UEVERSION_UPLOAD);
			
			QueryParas.put("bin_file", fileFileName);
			
			Mes.setBody(QueryParas.toString());
			ResMes = SendMes(Mes);
			
			JSONObject ResObj = JSONObject.fromObject(ResMes.getBody());
			if(!ResObj.get("result").equals("success")){
				log.warn("upload ue save database failed!");
				isUploaded = 2;			
				return ERROR;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = e.getLocalizedMessage();
			isUploaded = 2;
			log.warn(error);
			return ERROR;
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		isUploaded = 1;
		
		return SUCCESS;
	}
	
	
	
	/**
	 * 
	 * 升级UE接口 
	 * @author yangyunyun 
	 * 
	 */
	
	public String UpgradeUE(){
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		AbstractInnerMessage ResMes = null;
		JSONObject QueryParas = new JSONObject();
		
		Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
		Mes.setMessageId(UemConstantUtils.ACTION_UEVERSION_UPGRADE);
		
		
		if(UEIDList !=null && UEIDList.length() !=0 && Version != "undefined"){
			QueryParas.put("Version", Version);
			QueryParas.put("SoftwareType", type);
			QueryParas.put("IsUpgradeAll", false);
			QueryParas.put("UeIdArry", JSONArray.fromObject(UEIDList.split(",")));
			String[] arr = UEIDList.split(",");
			UEID  = Integer.valueOf(arr[0]);
			
		}

		Mes.setBody(QueryParas.toString());
		try {
			ResMes = SendMes(Mes);
			Util.ajaxSimpleUtil(ResMes.getBody());
			JSONObject ResObj = JSONObject.fromObject(ResMes.getBody());
			if(!ResObj.get("result").equals("success")){
				log.warn("upgrade ue version fail!");	
				error = "升级UE版本失败!";
				return ERROR;
			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = e.getLocalizedMessage();
			log.warn(error);
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * 删除UE版本接口 
	 * @author yangyunyun 
	 * 
	 */
	public void deleteUEVersion(){
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		AbstractInnerMessage ResMes = null;
		JSONObject QueryParas = new JSONObject();
		
		Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
		Mes.setMessageId(UemConstantUtils.ACTION_CONFIGUE_DELTET);
		
		if(VerFileList !=null && VerFileList.length() !=0){
			QueryParas.put("VerList", JSONArray.fromObject(VerFileList.split(",")));		
		}
		
		Mes.setBody(QueryParas.toString());
		try {
			ResMes = SendMes(Mes);
			JSONObject ResObj = JSONObject.fromObject(ResMes.getBody());
			if(ResObj.get("result").equals("success")){
				//删除ftp目录
				String[] fileArr= VerFileList.split(",");
				for(int index = 0; index < fileArr.length; index++){
					deleteFile(fileArr[index]);
				}
			}
			Util.ajaxSimpleUtil(ResMes.getBody());	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = e.getLocalizedMessage();
			log.warn(error);
			return;
		}
			
		return;
	}
	
	private boolean UploadFile(String fileName, byte[] fileContent){
		
		// 创建路径
		File content = new File(ueVersionFolder);
		if (!content.exists()) {
			content.mkdirs();
		}
		// 要上传的文件路径
		String filePath = ueVersionFolder + File.separator + fileName;
		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
			}
			fos = new FileOutputStream(file);
			fos.write(fileContent);
			log.debug("上传文件到服务器");
		} catch (Exception e) {
			log.error("上传文件到服务器失败", e);
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					log.error("关闭文件输出流失败", e);
				}

			}
		}		
		return true;
	}
	
	
	private boolean deleteFile(String fileName){
		String filePath = ueVersionFolder + File.separator + fileName;
		File file = new File(filePath);
		if (file.exists()) {
			log.info("delete ue version file  " + fileName);
			file.delete();
		} 
		return true;
	}
	
	private AbstractInnerMessage SendMes(AbstractInnerMessage mes) throws Exception{
		try {
			log.info("[SendMes] MesType="+mes.getMessageId()+" Mesbody="+mes.getBody());
			AbstractInnerMessage Res = Client.InvokeMethod(mes);
			log.info("[SendMes] ResMesBody= "+Res.getBody());
			if(Res.getMessageId().equals("ERROR")){
				throw new Exception("错误："+Res.getBody());
			}
			return Res;
			
		} catch (Throwable e) {			
			throw new Exception("Config响应错误:"+e.getLocalizedMessage());
		}
		
	}
	
	
	
	private void InsertKVToMap(JSONObject Json, Map<String, String> Map) {	
		Iterator it = null;
		it = Json.keys();
		while(it.hasNext()){
			String key = String.valueOf(it.next());
			String val = Json.get(key)+"";
			Map.put(key, val);
		}
	}
	
	public Integer getUEID() {
		return UEID;
	}
	public void setUEID(Integer uEID) {
		UEID = uEID;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getVersion() {
		return Version;
	}


	public void setVersion(String version) {
		Version = version;
	}


	public LinkedList<Map<String, String>> getUEVersionList() {
		return UEVersionList;
	}
	public void setUEVersionList(LinkedList<Map<String, String>> uEVersionList) {
		UEVersionList = uEVersionList;
	}
	public int getIsUploaded() {
		return isUploaded;
	}
	public void setIsUploaded(int isUploaded) {
		this.isUploaded = isUploaded;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public RPCClient getClient() {
		return Client;
	}
	public void setClient(RPCClient client) {
		Client = client;
	}


	public String getUEIDList() {
		return UEIDList;
	}


	public void setUEIDList(String uEIDList) {
		UEIDList = uEIDList;
	}


	public String getVerFileList() {
		return VerFileList;
	}


	public void setVerFileList(String verFileList) {
		VerFileList = verFileList;
	}


	public String getIMSI() {
		return IMSI;
	}


	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}
	
	
	
}
