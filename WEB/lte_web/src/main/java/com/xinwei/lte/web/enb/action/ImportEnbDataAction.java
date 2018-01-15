package com.xinwei.lte.web.enb.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.util.EnbBizDataExcelImporter;
import com.xinwei.lte.web.enb.util.EnbModelExcelExporter;
import com.xinwei.lte.web.enb.util.EnbModelExcelImporter;
import com.xinwei.lte.web.enb.util.poi.PoiUtil;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbDataImportExportFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.system.action.web.WebConstants;

public class ImportEnbDataAction extends ActionSupport {

	/**
	 * 导入的文件
	 */
	private File dataFile;

	private String dataFileFileName;

	private String dataFileContentType;

	private String error;

	/**
	 * 导入状态，0等待导入、1 正在导入、2导入失败
	 */
	private int importState = 0;

	public String importEnbData() {
		try {
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();

			FileInputStream inputStream = new FileInputStream(dataFile);
			HSSFWorkbook workbook = null;
			try {
				workbook = PoiUtil.parseWorkbook(inputStream);
			} catch (Exception e) {
				throw new Exception("目标文件内部格式非法!");
			}
			// 读取文件中的eNB列表
			List<Enb> enbListToImport = importEnbList(workbook);
			if (enbListToImport.isEmpty()) {
				throw new Exception("目标文件中无eNB!");
			}

			// 判断文件中是否包含所有eNB的数据
			List<String> sheetNames = PoiUtil.getSheetNames(workbook);
			 // 验证数据完整性
			 //checkAllEnbDataExist(enbListToImport, sheetNames);
			// 依次导入每个eNB的数据
			Map<Enb, List<XBizTable>> dataMap = importEnbBizData(sheetNames,
					enbListToImport, workbook);

			// 获取当前系统中已存在的eNB ID
			List<Enb> enbListExist = getExistEnbList(sessionId, enbListToImport);
			if (enbListExist != null) {
				// TODO 给页面提示已存在的eNB，询问是否覆盖
			}

			// 获取facade，传给后台
			EnbDataImportExportFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, EnbDataImportExportFacade.class);
			facade.importEnbData(OperObject.createSystemOperObject(), dataMap);
			importState = 1;
		} catch (Exception e) {
			error = e.getLocalizedMessage();
			importState = 2;
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 导入文件中的eNB列表
	 * 
	 * @param workbook
	 * @return
	 * @throws Exception
	 */
	private List<Enb> importEnbList(HSSFWorkbook workbook) throws Exception {
		EnbModelExcelImporter enbImporter = new EnbModelExcelImporter(workbook,
				EnbModelExcelImporter.SHEET_NAME_ENB);
		List<String> enbColumnList = new ArrayList<String>();
		Collections.addAll(enbColumnList, EnbModelExcelImporter.COLUMN_NAMES);
		enbImporter.setColumnList(enbColumnList);
		return enbImporter.importModelList();
	}

	/**
	 * 根据导入文件中的eNB列表导入eNB数据
	 * 
	 * @param enbListToImport
	 * @param workbook
	 * @return
	 * @throws Exception
	 */

	private Map<Enb, List<XBizTable>> importEnbBizData(List<String> sheetNames,
			List<Enb> enbListToImport, HSSFWorkbook workbook) throws Exception {
		StringBuilder msgBuilder = null;
		Map<Enb, List<XBizTable>> dataMap = new LinkedHashMap<Enb, List<XBizTable>>();
		for(Enb enb:enbListToImport){
			List<XBizTable> tableList=new LinkedList<XBizTable>();
			dataMap.put(enb, tableList);
		}
		// 依次导入每个eNB的数据
		for (String sheetName : sheetNames) {
			try {
				if (!sheetName.equals(EnbModelExcelExporter.SHEET_NAME_ENB)) {
					EnbBizDataExcelImporter bizDataImporte = new EnbBizDataExcelImporter(
							workbook, sheetName);
					// 获取列明
					HSSFSheet sheet = workbook.getSheet(sheetName);
					HSSFRow row = sheet.getRow(sheet.getFirstRowNum());
					List<String> enbColumnList = PoiUtil.getRowData(row);
					bizDataImporte.setColumnList(enbColumnList);
					List<Map<String, XBizTable>> dataList = bizDataImporte.importModelList();
					Map<String, XBizTable> xBizTableMap=dataList.get(0);
					Iterator<Entry<String, XBizTable>> iterator=xBizTableMap.entrySet().iterator();
					while(iterator.hasNext()){
						Entry<String, XBizTable> entry=iterator.next();
						String enbId=entry.getKey();
						XBizTable xbizTable=entry.getValue();
						List l=xbizTable.getRecords();
						Iterator<Entry<Enb, List<XBizTable>>> dataIterator=dataMap.entrySet().iterator();
						while(dataIterator.hasNext()){
							Entry<Enb, List<XBizTable>> dataEntry=dataIterator.next();
							Enb enb=dataEntry.getKey();
							List<XBizTable> datalist=dataEntry.getValue();
							if(enb.getHexEnbId().equals(enbId)){
								datalist.add(xbizTable);
							}
						}
						
					}
				}
			} catch (Exception e) {
				// 记录错误消息
				if (msgBuilder == null) {
					msgBuilder = new StringBuilder();
				}
				msgBuilder.append("</br>").append("eNB ID:").append(sheetName)
						.append("  错误:").append(e.getLocalizedMessage());
			}
		}
		if (msgBuilder != null) {
			throw new Exception(msgBuilder.toString());
		}
		return dataMap;
	}

	/*
	 * private Map<Enb, List<XBizTable>> importEnbBizData( List<Enb>
	 * enbListToImport, HSSFWorkbook workbook) throws Exception { Map<Enb,
	 * List<XBizTable>> dataMap = new HashMap<Enb, List<XBizTable>>();
	 * StringBuilder msgBuilder = null; // 依次导入每个eNB的数据 for (Enb enb :
	 * enbListToImport) { try { EnbBizDataExcelImporter bizDataImporter = new
	 * EnbBizDataExcelImporter( workbook, enb.getHexEnbId()); List<String>
	 * enbColumnList = new ArrayList<String>();
	 * Collections.addAll(enbColumnList, EnbBizDataExcelImporter.COLUMN_NAMES);
	 * bizDataImporter.setColumnList(enbColumnList); List<XBizTable> tables =
	 * bizDataImporter.importModelList();
	 * 
	 * dataMap.put(enb, tables); } catch (Exception e) { // 记录错误消息 if
	 * (msgBuilder == null) { msgBuilder = new StringBuilder(); }
	 * msgBuilder.append("</br>").append("eNB ID:")
	 * .append(enb.getHexEnbId()).append("  错误:")
	 * .append(e.getLocalizedMessage()); } } if (msgBuilder != null) { throw new
	 * Exception(msgBuilder.toString()); } return dataMap; }
	 */

	/**
	 * 获取当前系统中已存在的eNB ID
	 * 
	 * @param sessionId
	 * @param enbListToImport
	 * @return
	 * @throws Exception
	 */
	private List<Enb> getExistEnbList(String sessionId,
			List<Enb> enbListToImport) throws Exception {
		List<Enb> enbListExist = null;
		List<Long> enbIdsInDb = queryEnbIdListInEms(sessionId);
		for (Enb enb : enbListToImport) {
			if (enbIdsInDb.contains(enb.getEnbId())) {
				if (enbListExist == null) {
					enbListExist = new LinkedList<Enb>();
				}
				enbListExist.add(enb);
			}
		}
		return enbListExist;
	}

	/**
	 * 验证根据文件中的eNB列表，是否所有eNB的数据配置都存在
	 * 
	 * @param enbList
	 * @param sheetNames
	 * @throws Exception
	 */
	private void checkAllEnbDataExist(List<Enb> enbList, List<String> sheetNames)
			throws Exception {
		List<String> missgingEnb = null;
		for (Enb enb : enbList) {
			String hexEnbId = enb.getHexEnbId();
			if (!sheetNames.contains(hexEnbId)) {
				if (missgingEnb == null) {
					missgingEnb = new LinkedList<String>();
				}
				missgingEnb.add(hexEnbId);
			}
		}
		if (missgingEnb != null) {
			StringBuilder msgBuilder = new StringBuilder();
			msgBuilder.append("根据文件中的eNB列表,以下eNB的数据配置在文件中未找到:").append(
					missgingEnb.get(0));
			for (int i = 1; i < missgingEnb.size(); i++) {
				msgBuilder.append(", ").append(missgingEnb.get(i));
			}
			throw new Exception(msgBuilder.toString());
		}
	}

	/**
	 * 获取当前系统中的eNB列表
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	private List<Long> queryEnbIdListInEms(String sessionId) throws Exception {
		EnbBasicFacade enbBasicFacade = MinasSession.getInstance().getFacade(
				sessionId, EnbBasicFacade.class);
		List<Enb> enbList = enbBasicFacade.queryAllEnb();
		if (enbList == null || enbList.isEmpty())
			return Collections.emptyList();
		List<Long> idList = new LinkedList<Long>();
		for (Enb enb : enbList) {
			idList.add(enb.getEnbId());
		}
		return idList;
	}

	class EnbCompartor implements Comparator<Enb> {
		@Override
		public int compare(Enb enb1, Enb enb2) {
			long result = enb1.getEnbId() - enb2.getEnbId();
			if (result > 0)
				return 1;
			else if (result < 0)
				return -1;
			return 0;
		}

	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	public File getDataFile() {
		return dataFile;
	}

	/**
	 * @param importState
	 *            the importState to set
	 */
	public void setImportState(int importState) {
		this.importState = importState;
	}

	/**
	 * @return the importState
	 */
	public int getImportState() {
		return importState;
	}

	/**
	 * @param dataFileFileName
	 *            the dataFileFileName to set
	 */
	public void setDataFileFileName(String dataFileFileName) {
		this.dataFileFileName = dataFileFileName;
	}

	/**
	 * @return the dataFileFileName
	 */
	public String getDataFileFileName() {
		return dataFileFileName;
	}

	/**
	 * @param dataFileContentType
	 *            the dataFileContentType to set
	 */
	public void setDataFileContentType(String dataFileContentType) {
		this.dataFileContentType = dataFileContentType;
	}

	/**
	 * @return the dataFileContentType
	 */
	public String getDataFileContentType() {
		return dataFileContentType;
	}
	
	

}
