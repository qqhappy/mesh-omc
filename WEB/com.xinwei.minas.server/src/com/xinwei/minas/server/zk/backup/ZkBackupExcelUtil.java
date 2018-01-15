/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-14	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import com.xinwei.minas.zk.core.xnode.common.ZkNode;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeHeader;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeReserve;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsGroupVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsInfoVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsSagLinkVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsServiceSagVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkGroupedBtsVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkRulesVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagDataVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagGroupVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagInfoVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagVO;
import com.xinwei.omp.core.utils.ReflectUtils;
import com.xinwei.omp.core.utils.StringUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 备份excel文件助手
 * 
 * @author fanhaoyu
 * 
 */

public class ZkBackupExcelUtil {
	private Log log = LogFactory.getLog(ZkBackupExcelUtil.class);
	public final static String EXCEL_POSTFIX = ".xls";

	private final static String[] SHEETNAMES = new String[] { "SAG_GROUP",
			"SAG_LIST", "BTS_GROUP_LIST", "BTS_LIST" };
	private final static String[] SAG_GROUP_PROPERTY_NAMES = new String[] {
			"path", "sagGroupId", "maxSubNode" };
	private final static String[] SAG_INFO_PROPERTY_NAMES = new String[] { "maxSubNode" };
	private final static String[] RULES_PROPERTY_NAMES = new String[] { "accessType" };// "xmlData",
	private final static String[] BTS_INFO_PROPERTY_NAMES = new String[] { "maxSubNode" };
	private final static String[] SAG_PROPERTY_NAMES = new String[] { "sagId",
			"maxBtsGroupNum", "planBtsGroupIds" };
	private final static String[] SAG_DATA_PROPERTY_NAMES = new String[] {
			"sagSignalIp", "sagSignalPort", "sagMediaIP", "sagMediaPort",
			"btsDVoiceFlag", "T_EST", "N_EST", "T_HS", "T_HS_ACK", "N_HS",
			"OPCODE", "CSEQ_CLI", "CSEQ_SER", "btsLinkMBMSFlag", "sagDPID" };
	private final static String[] BTS_GROUP_PROPERTY_NAMES = new String[] {
			"btsGroupId", "maxSubNode", "homeSagId", "rulesFlag" };
	private final static String[] BTS_PROPERTY_NAMES = new String[] { "btsId",
			"homeBtsGroupId", "btsSignalIP", "btsSignalPort", "btsMediaIP",
			"btsMediaPort", "btsDVoiceFlag", "T_EST", "N_EST", "T_HS",
			"T_HS_ACK", "N_HS", "OPCODE", "CSEQ_CLI", "CSEQ_SER",
			"btsLinkMBMSFlag", "btsDpId", "LAI", "SPI" };

	private boolean hasHeaderName = false;

	private String[] SAG_GROUP_HEADER_NAMES;
	private String[] SAG_INFO_HEADER_NAMES;
	private String[] RULES_HEADER_NAMES;
	private String[] BTS_INFO_HEADER_NAMES;
	private String[] SAG_HEADER_NAMES;
	private String[] SAG_DATA_HEADER_NAMES;
	private String[] BTS_GROUP_HEADER_NAMES;
	private String[] BTS_HEADER_NAMES;

	private HSSFWorkbook workBook;
	private ZkNode sagGroupNode;
	private String excelFilePath;

	public ZkBackupExcelUtil(String importExcelFilePath) throws IOException {

		FileInputStream excelInStream = null;
		POIFSFileSystem fs = null;
		try {
			excelInStream = new FileInputStream(importExcelFilePath);
			fs = new POIFSFileSystem(excelInStream);
			workBook = new HSSFWorkbook(fs);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (excelInStream != null) {
				excelInStream.close();
			}
		}
	}

	public ZkBackupExcelUtil(ZkNode sagGroupNode, String exportExcelFilePath)
			throws IOException {
		this.sagGroupNode = sagGroupNode;
		this.excelFilePath = exportExcelFilePath;

		FileInputStream excelInStream = null;
		POIFSFileSystem fs = null;
		try {
			excelInStream = new FileInputStream(exportExcelFilePath);
			fs = new POIFSFileSystem(excelInStream);
			workBook = new HSSFWorkbook(fs);
		} catch (IOException e) {
			throw e;
		} finally {
			if (excelInStream != null) {
				excelInStream.close();
			}
		}
	}

	public void setHeaderNames(String[] SAG_GROUP_HEADER_NAMES,
			String[] SAG_INFO_HEADER_NAMES, String[] RULES_HEADER_NAMES,
			String[] BTS_INFO_HEADER_NAMES, String[] SAG_HEADER_NAMES,
			String[] SAG_DATA_HEADER_NAMES, String[] BTS_GROUP_HEADER_NAMES,
			String[] BTS_HEADER_NAMES) {
		this.SAG_GROUP_HEADER_NAMES = SAG_GROUP_HEADER_NAMES;
		this.SAG_INFO_HEADER_NAMES = SAG_INFO_HEADER_NAMES;
		this.RULES_HEADER_NAMES = RULES_HEADER_NAMES;
		this.BTS_INFO_HEADER_NAMES = BTS_INFO_HEADER_NAMES;
		this.SAG_HEADER_NAMES = SAG_HEADER_NAMES;
		this.SAG_DATA_HEADER_NAMES = SAG_DATA_HEADER_NAMES;
		this.BTS_GROUP_HEADER_NAMES = BTS_GROUP_HEADER_NAMES;
		this.BTS_HEADER_NAMES = BTS_HEADER_NAMES;
		this.hasHeaderName = true;
	}

	public void exportSagGroup() throws IOException {
		// 首先清除多余的sheet
		clearSheetofWorkBook();
		if (sagGroupNode == null)
			return;
		// 设置标题名称
		if (!hasHeaderName) {
			this.SAG_GROUP_HEADER_NAMES = SAG_GROUP_PROPERTY_NAMES;
			this.SAG_INFO_HEADER_NAMES = SAG_INFO_PROPERTY_NAMES;
			this.RULES_HEADER_NAMES = RULES_PROPERTY_NAMES;
			this.BTS_INFO_HEADER_NAMES = BTS_INFO_PROPERTY_NAMES;
			this.SAG_HEADER_NAMES = SAG_PROPERTY_NAMES;
			this.SAG_DATA_HEADER_NAMES = SAG_DATA_PROPERTY_NAMES;
			this.BTS_GROUP_HEADER_NAMES = BTS_GROUP_PROPERTY_NAMES;
			this.BTS_HEADER_NAMES = BTS_PROPERTY_NAMES;
		}
		// 创建sheets
		for (int i = 0; i < SHEETNAMES.length; i++)
			createSheet(SHEETNAMES[i]);
		// 写入Excel文件
		FileOutputStream excelOutStream = null;
		try {
			excelOutStream = new FileOutputStream(excelFilePath);
			workBook.write(excelOutStream);
		} catch (IOException e) {
			throw new IOException(
					OmpAppContext.getMessage("ExcelUtil.export_failed"));
		} finally {
			if (excelOutStream != null) {
				excelOutStream.close();
			}
		}
	}

	public ZkNode importSagGroup() throws Exception {
		// 解析Excel文件
		List<ZkNode> nodeList = parseExcelFile();
		if (nodeList == null || nodeList.isEmpty())
			return null;
		// 找到SagGroup节点
		ZkNode sagGroupNode = findSagGroupNode(nodeList);
		String name = sagGroupNode.getPath();
		sagGroupNode.setPath(ZkNodeConstant.SAG_ROOT_PATH + "/" + name);
		// 找到SagGroup节点的孩子节点
		sagGroupNode.setChildren(findSagGroupChildrenNodes(nodeList,
				sagGroupNode));
		return sagGroupNode;
	}

	/**
	 * 判断一个文件是否为有效的.xls文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isXLSFile(String filePath) {
		int index = filePath.lastIndexOf(".");
		if (index < 0)
			return false;
		String postfix = filePath.substring(filePath.length() - 4,
				filePath.length());
		if (!postfix.toLowerCase().equals(EXCEL_POSTFIX))
			return false;
		return true;
	}

	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		if (file.exists())
			return true;
		return false;
	}

	public static void createNewXLSFile(String filePath) throws IOException {
		FileOutputStream fout = null;
		try {
			File file = new File(filePath);
			HSSFWorkbook workbook = new HSSFWorkbook();
			fout = new FileOutputStream(file);
			workbook.createSheet("sheet1");
			workbook.write(fout);
			fout.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			if (fout != null) {
				fout.close();
			}
		}
	}

	// 解析Excel文件
	private List<ZkNode> parseExcelFile() throws Exception {
		List<ZkNode> nodeList = new ArrayList<ZkNode>();
		int sheetCount = workBook.getNumberOfSheets();
		for (int i = 0; i < sheetCount; i++) {
			HSSFSheet sheet = workBook.getSheetAt(i);
			if (sheet == null)
				break;
			List<ZkNode> nodes = getZkNodes(sheet);
			if (nodes == null || nodes.isEmpty())
				continue;
			nodeList.addAll(nodes);
		}
		return nodeList;
	}

	// 从sheet中获取所有ZkNode数据
	private List<ZkNode> getZkNodes(HSSFSheet sheet) throws Exception {

		// 第一行为path
		String sheetName = sheet.getSheetName();
		if (sheetName.equals("SAG_GROUP")) {
			return importSagGroupSheet(sheet);
		} else if (sheetName.equals("SAG_LIST")) {
			return importSagListSheet(sheet);
		} else if (sheetName.equals("BTS_GROUP_LIST")) {
			return importBtsGroupListSheet(sheet);
		} else if (sheetName.equals("BTS_LIST")) {
			return importBtsListSheet(sheet);
		}
		return null;
	}

	private List<ZkNode> importSagGroupSheet(HSSFSheet sheet) throws Exception {

		List<ZkNode> nodeList = new ArrayList<ZkNode>();
		ZkNode node = null;
		ZkNodeVO currentVo = null;
		ZkNodeHeader currentHeader = null;
		int rowCount = sheet.getLastRowNum();
		int rowIndex = 0;
		String title = null;
		String[] names = null;
		while (true) {
			HSSFRow row = sheet.getRow(rowIndex);
			if (rowIndex >= rowCount)
				break;
			if (row == null) {
				rowIndex++;
				continue;
			}
			int columnIndex = 0;
			HSSFCell tempCell = row.getCell(columnIndex);
			if (tempCell != null) {
				if (tempCell.getCellType() == Cell.CELL_TYPE_BLANK) {
					rowIndex++;
					continue;
				}
			}
			while (true) {
				HSSFCell cell = row.getCell(columnIndex);
				if (cell == null) {
					break;
				}
				title = getCellValue(cell).toString();
				if (title.equals("SAG_GROUP")) {
					names = SAG_GROUP_PROPERTY_NAMES;
					currentHeader = new ZkNodeHeader(
							ZkNodeConstant.NODE_TYPE_SAG_GROUP);
					currentVo = new ZkSagGroupVO();
				} else if (title.equals("SAG_INFO")) {
					names = SAG_INFO_PROPERTY_NAMES;
					currentHeader = new ZkNodeHeader(
							ZkNodeConstant.NODE_TYPE_SAG_INFO);
					currentVo = new ZkSagInfoVO();
					node = new ZkNode(ZkNodeConstant.SAG_INFO_NAME);
					node.setHeader(currentHeader);
					node.setZkNodeVO(currentVo);
					nodeList.add(node);
				} else if (title.equals("RULES")) {
					names = RULES_PROPERTY_NAMES;
					currentHeader = new ZkNodeHeader(
							ZkNodeConstant.NODE_TYPE_RULES);
					currentVo = new ZkRulesVO();
					node = new ZkNode(ZkNodeConstant.RULES_NAME);
					node.setHeader(currentHeader);
					node.setZkNodeVO(currentVo);
					nodeList.add(node);
				} else if (title.equals("BTS_INFO")) {
					names = BTS_INFO_PROPERTY_NAMES;
					currentHeader = new ZkNodeHeader(
							ZkNodeConstant.NODE_TYPE_BTS_INFO);
					currentVo = new ZkBtsInfoVO();
					node = new ZkNode(ZkNodeConstant.BTS_INFO_NAME);
					node.setHeader(currentHeader);
					node.setZkNodeVO(currentVo);
					nodeList.add(node);
				} else {
					HSSFCell nameCell = row.getCell(columnIndex);
					String propertyName = getCellValue(nameCell).toString();
					if (isExist(propertyName, names)) {
						rowIndex = row.getRowNum() + 2;
						HSSFRow valueRow = sheet.getRow(rowIndex);
						HSSFCell valueCell = valueRow.getCell(columnIndex);

						Object obj = getCellValue(valueCell);
						if (obj == null) {
							throw new Exception(
									MessageFormat.format(
											OmpAppContext
													.getMessage("ZkBackupExcelUtil.property_can_not_be_null"),
											propertyName));
						}
						String propertyValue = String.valueOf(obj);
						setProperty(currentVo, propertyName, propertyValue);

						if (propertyName.equals("path")) {
							if (valueCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
								propertyValue = propertyValue.substring(0,
										propertyValue.indexOf("."));
							}
							node = new ZkNode(propertyValue);
							node.setHeader(currentHeader);
							node.setZkNodeVO(currentVo);
							nodeList.add(node);
						}
					}
				}

				columnIndex++;
			}
			rowIndex++;
		}
		return nodeList;
	}

	private List<ZkNode> importSagListSheet(HSSFSheet sheet) throws Exception {

		List<ZkNode> nodeList = new ArrayList<ZkNode>();
		HSSFRow propertyNameRow = sheet.getRow(0);
		// 第一行是属性名
		int columnCount = propertyNameRow.getLastCellNum();
		int rowCount = sheet.getLastRowNum();

		String[] propertyNames = new String[columnCount];
		@SuppressWarnings("rawtypes")
		Iterator cellIterator = propertyNameRow.cellIterator();

		for (int i = 0; cellIterator.hasNext(); i++) {
			Object obj = getCellValue((HSSFCell) cellIterator.next());
			if (obj == null)
				propertyNames[i] = "";
			else
				propertyNames[i] = (String) obj;
		}
		ZkNode sagNode = null;
		ZkNode sagDataNode = null;
		ZkNodeVO currentVo = null;

		for (int rowIndex = 2; rowIndex <= rowCount; rowIndex++) {
			// 从第三行开始是值
			HSSFRow row = sheet.getRow(rowIndex);
			if (row == null)
				continue;
			ZkSagVO sagVO = new ZkSagVO();
			ZkSagDataVO sagDataVO = new ZkSagDataVO();
			ZkNodeHeader sagHeader = new ZkNodeHeader(
					ZkNodeConstant.NODE_TYPE_SAG);
			ZkNodeHeader sagDataHeader = new ZkNodeHeader(
					ZkNodeConstant.NODE_TYPE_SAGDATA);
			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
				String propertyName = propertyNames[columnIndex];
				if (propertyName.equals(""))
					continue;
				HSSFCell valueCell = row.getCell(columnIndex);
				if (valueCell == null) {
					throw new Exception(
							MessageFormat.format(
									OmpAppContext
											.getMessage("ZkBackupExcelUtil.property_can_not_be_null"),
									propertyName));
				}

				Object obj = getCellValue(valueCell);
				if (obj == null) {
					throw new Exception(
							MessageFormat.format(
									OmpAppContext
											.getMessage("ZkBackupExcelUtil.property_can_not_be_null"),
									propertyName));
				}
				String propertyValue = String.valueOf(obj);

				if (propertyName.equals("sagId")) {
					// propertyValue = this.format2IntString(propertyValue);
					Double value = Double.parseDouble(propertyValue);
					long sagId = value.longValue();
					sagNode = new ZkNode("SAG_" + sagId);
					sagDataNode = new ZkNode(ZkNodeConstant.SAGDATA_NODE_NAME);
					sagVO.setSagId(sagId);
					sagDataVO.setSagId(sagId);
					continue;
				}
				if (isExist(propertyName, SAG_PROPERTY_NAMES)) {
					currentVo = sagVO;
				} else if (isExist(propertyName, SAG_DATA_PROPERTY_NAMES)) {
					currentVo = sagDataVO;
				}

				setProperty(currentVo, propertyName, propertyValue);
			}
			sagNode.setHeader(sagHeader);
			sagNode.setZkNodeVO(sagVO);

			sagDataNode.setHeader(sagDataHeader);
			sagDataNode.setZkNodeVO(sagDataVO);

			nodeList.add(sagNode);
			nodeList.add(sagDataNode);
		}
		return nodeList;
	}

	private List<ZkNode> importBtsGroupListSheet(HSSFSheet sheet)
			throws Exception {

		List<ZkNode> nodeList = new ArrayList<ZkNode>();
		HSSFRow propertyNameRow = sheet.getRow(0);
		// 第一行是属性名
		int columnCount = propertyNameRow.getLastCellNum();
		int rowCount = sheet.getLastRowNum();

		String[] propertyNames = new String[columnCount];
		@SuppressWarnings("rawtypes")
		Iterator cellIterator = propertyNameRow.cellIterator();
		for (int i = 0; cellIterator.hasNext(); i++) {
			Object obj = getCellValue((HSSFCell) cellIterator.next());
			if (obj == null)
				propertyNames[i] = "";
			else
				propertyNames[i] = (String) obj;
		}
		for (int rowIndex = 2; rowIndex <= rowCount; rowIndex++) {
			// 从第三行开始是值
			HSSFRow row = sheet.getRow(rowIndex);
			if (row == null)
				continue;
			ZkNode node = null;
			ZkNodeHeader header = new ZkNodeHeader(
					ZkNodeConstant.NODE_TYPE_BTS_GROUP);
			ZkNodeVO vo = new ZkBtsGroupVO();
			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
				String propertyName = propertyNames[columnIndex];
				if (propertyName.equals(""))
					continue;
				HSSFCell valueCell = row.getCell(columnIndex);
				if (valueCell == null) {
					throw new Exception(
							MessageFormat.format(
									OmpAppContext
											.getMessage("ZkBackupExcelUtil.property_can_not_be_null"),
									propertyName));
				}

				Object obj = getCellValue(valueCell);
				if (obj == null) {
					throw new Exception(
							MessageFormat.format(
									OmpAppContext
											.getMessage("ZkBackupExcelUtil.property_can_not_be_null"),
									propertyName));
				}
				String propertyValue = String.valueOf(obj);

				if (propertyName.equals("btsGroupId")) {
					Double value = Double.parseDouble(propertyValue);
					long btsGroupId = value.longValue();
					node = new ZkNode(ZkNodeConstant.BTS_GROUP_NODE_PREFIX
							+ btsGroupId);
				}
				setProperty(vo, propertyName, propertyValue);
			}
			node.setHeader(header);
			node.setZkNodeVO(vo);
			nodeList.add(node);
		}
		return nodeList;
	}

	private List<ZkNode> importBtsListSheet(HSSFSheet sheet) throws Exception {

		List<ZkNode> nodeList = new ArrayList<ZkNode>();
		HSSFRow propertyNameRow = sheet.getRow(0);
		// 第一行是属性名
		int columnCount = propertyNameRow.getLastCellNum();
		int rowCount = sheet.getLastRowNum();

		String[] propertyNames = new String[columnCount];
		@SuppressWarnings("rawtypes")
		Iterator cellIterator = propertyNameRow.cellIterator();
		for (int i = 0; cellIterator.hasNext(); i++) {
			Object obj = getCellValue((HSSFCell) cellIterator.next());
			if (obj == null)
				propertyNames[i] = "";
			else
				propertyNames[i] = (String) obj;
		}
		for (int rowIndex = 2; rowIndex <= rowCount; rowIndex++) {
			// 从第三行开始是值
			HSSFRow row = sheet.getRow(rowIndex);
			if (row == null)
				continue;
			ZkNode node = null;
			ZkNodeHeader header = new ZkNodeHeader(ZkNodeConstant.NODE_TYPE_BTS);
			ZkNodeVO vo = new ZkBtsVO();
			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
				String propertyName = propertyNames[columnIndex];
				if (propertyName.equals(""))
					continue;

				HSSFCell valueCell = row.getCell(columnIndex);
				if (valueCell == null) {
					throw new Exception(
							MessageFormat.format(
									OmpAppContext
											.getMessage("ZkBackupExcelUtil.property_can_not_be_null"),
									propertyName));
				}

				Object obj = getCellValue(valueCell);
				if (obj == null) {
					throw new Exception(
							MessageFormat.format(
									OmpAppContext
											.getMessage("ZkBackupExcelUtil.property_can_not_be_null"),
									propertyName));
				}
				String propertyValue = String.valueOf(obj);

				if (propertyName.equals("btsId")) {
					if (valueCell.getCellType() == Cell.CELL_TYPE_STRING) {
						if (!isValidHex(propertyValue)) {
							throw new Exception(
									OmpAppContext
											.getMessage("ZkBackupExcelUtil.invalid_sagId"));
						}
					} else {
						if (!isValidInteger(propertyValue)) {
							throw new Exception(
									OmpAppContext
											.getMessage("ZkBackupExcelUtil.invalid_sagId"));
						}
						propertyValue = this.format2IntString(propertyValue);
					}
					// 8位16进制字符串
					String hexBtsId = StringUtils.appendPrefix(propertyValue,
							"0", 8).toUpperCase();
					// 10进制字符串
					String decBtsId = String.valueOf(Long.parseLong(hexBtsId,
							16));
					node = new ZkNode("BTS_" + hexBtsId);
					setProperty(vo, propertyName, decBtsId);
				} else {
					setProperty(vo, propertyName, propertyValue);
				}
			}
			node.setHeader(header);
			node.setZkNodeVO(vo);
			nodeList.add(node);
		}
		return nodeList;
	}

	private boolean isExist(String propertyName, String[] propertyNames) {
		for (String property : propertyNames) {
			if (property.equals(propertyName))
				return true;
		}
		return false;
	}

	// 根据Cell类型获取相应的值
	private Object getCellValue(HSSFCell cell) throws Exception {
		int cellType = cell.getCellType();
		switch (cellType) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		default:
			return null;
		}
	}

	// 根据Cell类型获取相应的值
	private void setCellValue(HSSFCell cell, Object value) {
		int cellType = cell.getCellType();
		switch (cellType) {
		case Cell.CELL_TYPE_STRING:
			cell.setCellValue(value.toString());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			cell.setCellValue(Double.parseDouble(value.toString()));
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cell.setCellValue(Boolean.parseBoolean(value.toString()));
			break;
		default:
			break;
		}
	}

	// 设置node的属性值
	@SuppressWarnings("rawtypes")
	private void setProperty(Object object, String propertyName,
			String propertyValue) throws Exception {
		// set方法属性名称首字母大写
		propertyName = propertyName.substring(0, 1).toUpperCase()
				+ propertyName.substring(1);
		Method method = ReflectUtils.findMethod(object.getClass(), "set"
				+ propertyName);
		if (method != null) {
			Class[] paramTypes = method.getParameterTypes();
			try {
				String typeName = paramTypes[0].getSimpleName();
				if (typeName.equals(Integer.class.getSimpleName())
						|| typeName.equals("int")) {
					Double value = new Double(0);
					if (!propertyValue.equals(""))
						value = Double.parseDouble(propertyValue);
					method.invoke(object, value.intValue());
				} else if (typeName.equals(Long.class.getSimpleName())
						|| typeName.equals("long")) {
					Double value = new Double(0);
					if (!propertyValue.equals(""))
						value = Double.parseDouble(propertyValue);
					method.invoke(object, value.longValue());
				} else if (typeName.equals(Double.class.getSimpleName())
						|| typeName.equals("double")) {
					method.invoke(object, Double.parseDouble(propertyValue));
				} else if (typeName.equals(Float.class.getSimpleName())
						|| typeName.equals("float")) {
					Float value = new Float(0);
					if (!propertyValue.equals(""))
						value = Float.parseFloat(propertyValue);
					method.invoke(object, value);
				} else if (typeName.equals(String.class.getSimpleName())) {
					method.invoke(object, propertyValue);
				} else if (typeName.equals(byte[].class.getSimpleName())) {
					byte[] buf = null;
					if (propertyName.equals("LAI")) {
						buf = new byte[16];
						try {
							byte[] valueBuf = propertyValue
									.getBytes(ZkNodeConstant.CHARSET_US_ASCII);
							if (valueBuf.length > 16)
								throw new Exception(
										OmpAppContext
												.getMessage("ZkBackupExcelUtil.length_of_LAI_should_be_16_or_smaller"));
							System.arraycopy(valueBuf, 0, buf, 0,
									valueBuf.length);
							for (int i = valueBuf.length; i < buf.length; i++)
								buf[i] = '\0';
						} catch (UnsupportedEncodingException e) {
							log.error(e);
						}
					}
					// else if (propertyName.equals("XmlData")) {
					// buf = new byte[ZkNodeConstant.NK_NODE_RULEXML_LEN];
					// ByteUtils.putString(buf, 0, propertyValue,
					// ZkNodeConstant.NK_NODE_RULEXML_LEN, '\0',
					// ZkNodeConstant.CHARSET_US_ASCII);
					// }
					method.invoke(object, buf);
				} else if (typeName.equals(Set.class.getSimpleName())) {
					@SuppressWarnings("unchecked")
					Set<Long> planBtsGroupIds = new HashSet();
					if (!propertyValue.equals("")) {
						String[] ids = propertyValue.split(",");
						if (ids != null && ids.length != 0) {
							for (int i = 0; i < ids.length; i++) {
								Double id = Double.parseDouble(ids[i]);
								planBtsGroupIds.add(id.longValue());
							}
						}
					}
					method.invoke(object, planBtsGroupIds);
				}
			} catch (Exception e) {
				throw new Exception(propertyName + " "
						+ e.getLocalizedMessage(), e);
			}
		}
	}

	// 找到SagGroup节点
	private ZkNode findSagGroupNode(List<ZkNode> nodeList) {
		for (ZkNode node : nodeList) {
			int nodeType = node.getNodeType();
			if (nodeType == ZkNodeConstant.NODE_TYPE_SAG_GROUP) {
				return node;
			}
		}
		return null;
	}

	// 找到SagGroup节点的孩子节点
	private Set<ZkNode> findSagGroupChildrenNodes(List<ZkNode> nodeList,
			ZkNode sagGroup) throws Exception {
		Set<ZkNode> childreNodes = new HashSet<ZkNode>();
		for (ZkNode node : nodeList) {
			int nodeType = node.getNodeType();
			if (nodeType == ZkNodeConstant.NODE_TYPE_SAG_INFO) {
				node.setPath(sagGroup.getPath() + "/" + node.getPath());
				Set<ZkNode> sagNodes = findSagInfoChildrenNodes(nodeList, node);
				ZkSagInfoVO sagInfoVO = (ZkSagInfoVO) node.getZkNodeVO();
				// 判断sagInfo子节点个数是否超过设定的值
				if (sagNodes.size() > sagInfoVO.getMaxSubNode())
					throw new Exception(
							MessageFormat.format(
									OmpAppContext
											.getMessage("ZkBackupExcelUtil.subnodenum_over_setting_value"),
									node.getPath()));
				// 添加sagGroup子节点
				sagGroup.addChild(node);
				childreNodes.add(node);
			} else if (nodeType == ZkNodeConstant.NODE_TYPE_RULES) {
				node.setPath(sagGroup.getPath() + "/" + node.getPath());
				sagGroup.addChild(node);
				childreNodes.add(node);
			} else if (nodeType == ZkNodeConstant.NODE_TYPE_BTS_INFO) {
				node.setPath(sagGroup.getPath() + "/" + node.getPath());
				Set<ZkNode> btsGroupNodes = findBtsInfoChildrenNodes(nodeList,
						node);
				ZkBtsInfoVO btsInfoVO = (ZkBtsInfoVO) node.getZkNodeVO();
				// 判断btsInfo子节点个数是否超过设定的值
				if (btsGroupNodes.size() > btsInfoVO.getMaxSubNode())
					throw new Exception(
							MessageFormat.format(
									OmpAppContext
											.getMessage("ZkBackupExcelUtil.subnodenum_over_setting_value"),
									node.getPath()));
				sagGroup.addChild(node);
				childreNodes.add(node);
			}
		}
		return childreNodes;
	}

	// 找到BtsInfo节点的孩子节点(BtsGroup)
	private Set<ZkNode> findBtsInfoChildrenNodes(List<ZkNode> nodeList,
			ZkNode btsInfo) throws Exception {
		Set<ZkNode> btsGroupNodes = new HashSet<ZkNode>();
		for (ZkNode btsGroup : nodeList) {
			int nodeType = btsGroup.getNodeType();
			if (nodeType == ZkNodeConstant.NODE_TYPE_BTS_GROUP) {
				btsGroup.setPath(btsInfo.getPath() + "/" + btsGroup.getPath());
				btsInfo.addChild(btsGroup);
				btsGroupNodes.add(btsGroup);

				Set<ZkNode> nodes = findBtsGroupChildrenNodes(nodeList,
						btsGroup);
				ZkBtsGroupVO btsGroupVO = (ZkBtsGroupVO) btsGroup.getZkNodeVO();
				if (nodes.size() > btsGroupVO.getMaxSubNode())
					throw new Exception(
							MessageFormat.format(
									OmpAppContext
											.getMessage("ZkBackupExcelUtil.subnodenum_over_setting_value"),
									btsGroup.getPath()));
				ZkNode serviceSag = new ZkNode(btsGroup.getPath() + "/"
						+ ZkNodeConstant.SERVICESAG_NAME);
				serviceSag.setHeader(new ZkNodeHeader(
						ZkNodeConstant.NODE_TYPE_SERVSAG));
				ZkBtsServiceSagVO serviceSagVO = new ZkBtsServiceSagVO();
				serviceSagVO.setLastServiceSagId(0);
				serviceSagVO.setZkNodeReserve(new ZkNodeReserve());
				serviceSag.setZkNodeVO(serviceSagVO);
				btsGroup.addChild(serviceSag);
			}
		}
		return btsGroupNodes;
	}

	// 找到BtsGroup节点的孩子节点(Bts)
	private Set<ZkNode> findBtsGroupChildrenNodes(List<ZkNode> nodeList,
			ZkNode btsGroup) {
		Set<ZkNode> childrenNodes = new HashSet<ZkNode>();
		for (ZkNode bts : nodeList) {
			int nodeType = bts.getNodeType();
			if (nodeType == ZkNodeConstant.NODE_TYPE_BTS) {
				ZkBtsGroupVO parentVo = (ZkBtsGroupVO) btsGroup.getZkNodeVO();
				ZkBtsVO childVo = (ZkBtsVO) bts.getZkNodeVO();
				if (parentVo.getBtsGroupId()
						.equals(childVo.getHomeBtsGroupId())) {
					bts.setPath(btsGroup.getPath() + "/" + bts.getPath());
					btsGroup.addChild(bts);
					childrenNodes.add(bts);

					ZkNode sagLink = new ZkNode(bts.getPath() + "/" + "SAGLink");
					sagLink.setHeader(new ZkNodeHeader(
							ZkNodeConstant.NODE_TYPE_SAGLINK));
					ZkBtsSagLinkVO sagLinkVO = new ZkBtsSagLinkVO();
					sagLinkVO.setSagId(0);
					sagLinkVO.setSagSignalIp("0.0.0.0");
					sagLinkVO.setSagSignalPort(0);
					sagLinkVO.setSagMediaIP("0.0.0.0");
					sagLinkVO.setSagMediaPort(0);
					sagLinkVO.setSagDPID(0);
					sagLinkVO.setZkNodeReserve(new ZkNodeReserve());
					sagLink.setZkNodeVO(sagLinkVO);
					bts.addChild(sagLink);
				}
			}
		}
		return childrenNodes;
	}

	// 找到SagInfo节点的孩子节点(Sag)
	private Set<ZkNode> findSagInfoChildrenNodes(List<ZkNode> nodeList,
			ZkNode sagInfo) {
		Set<ZkNode> childrenNodes = new HashSet<ZkNode>();
		for (ZkNode node : nodeList) {
			int nodeType = node.getNodeType();
			if (nodeType == ZkNodeConstant.NODE_TYPE_SAG) {
				node.setPath(sagInfo.getPath() + "/" + node.getPath());
				node.setChildren(findSagChildrenNodes(nodeList, node));
				sagInfo.addChild(node);
				childrenNodes.add(node);
			}
		}
		return childrenNodes;
	}

	// 找到Sag节点的孩子节点(SagData)
	private Set<ZkNode> findSagChildrenNodes(List<ZkNode> nodeList,
			ZkNode sagNode) {
		Set<ZkNode> childrenNodes = new HashSet<ZkNode>();
		for (ZkNode node : nodeList) {
			int nodeType = node.getNodeType();
			if (nodeType == ZkNodeConstant.NODE_TYPE_SAGDATA) {
				ZkSagVO parentVo = (ZkSagVO) sagNode.getZkNodeVO();
				ZkSagDataVO childVo = (ZkSagDataVO) node.getZkNodeVO();
				if (parentVo.getSagId().equals(childVo.getSagId())) {
					node.setPath(sagNode.getPath() + "/" + node.getPath());
					sagNode.addChild(node);

					ZkNode groupedBts = new ZkNode(node.getPath() + "/"
							+ "GROUPEDBTS");
					groupedBts.setHeader(new ZkNodeHeader(
							ZkNodeConstant.NODE_TYPE_GROUPEDBTS));
					ZkGroupedBtsVO groupedBtsVO = new ZkGroupedBtsVO();
					groupedBtsVO.setReserve(0);
					groupedBtsVO.setZkNodeReserve(new ZkNodeReserve());
					groupedBts.setZkNodeVO(groupedBtsVO);
					node.addChild(groupedBts);
					childrenNodes.add(node);
				}
			}
		}
		return childrenNodes;
	}

	private void createSheet(String sheetName) {
		HSSFSheet sheet = null;
		if (sheetName.equals("SAG_GROUP")) {
			// 更改第一个sheet的名称
			workBook.setSheetName(0, "SAG_GROUP");
			sheet = workBook.getSheetAt(0);
			// 清空sheet中所有行
			clearRowsofSheet(sheet);

			HSSFCellStyle titleStyle = workBook.createCellStyle();
			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			titleStyle.setFillForegroundColor(HSSFColor.GREEN.index);
			titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

			// 创建标题行
			if (isSheetEmpty(sheet)) {

				HSSFRow titleRow = sheet.createRow(sheet.getLastRowNum());
				HSSFCell titleCell = titleRow.createCell(0);
				titleCell.setCellValue("SAG_GROUP");
				titleCell.setCellStyle(titleStyle);
				createHeaderRow(sheet, ZkNodeConstant.NODE_TYPE_SAG_GROUP);
				createHeaderNameRow(sheet, ZkNodeConstant.NODE_TYPE_SAG_GROUP);
			}
			// 写入SagGroupNode
			writeZkNodeData(sheet, sagGroupNode);
			// 写入SagInfo、BtsInfo、Rules
			for (ZkNode node : sagGroupNode.getChildren()) {
				// 空白行
				sheet.createRow(sheet.getLastRowNum() + 1);
				HSSFRow titleRow = sheet.createRow(sheet.getLastRowNum() + 1);
				HSSFCell titleCell = titleRow.createCell(0);
				if (node.getNodeType() == ZkNodeConstant.NODE_TYPE_SAG_INFO)
					titleCell.setCellValue("SAG_INFO");
				else if (node.getNodeType() == ZkNodeConstant.NODE_TYPE_BTS_INFO)
					titleCell.setCellValue("BTS_INFO");
				else if (node.getNodeType() == ZkNodeConstant.NODE_TYPE_RULES)
					titleCell.setCellValue("RULES");
				titleCell.setCellStyle(titleStyle);
				createHeaderRow(sheet, node.getNodeType());
				createHeaderNameRow(sheet, node.getNodeType());
				writeZkNodeData(sheet, node);
			}
		} else if (sheetName.equals("SAG_LIST")) {
			sheet = workBook.createSheet("SAG_LIST");
			// 创建标题行
			if (isSheetEmpty(sheet)) {
				createHeaderRow(sheet, ZkNodeConstant.NODE_TYPE_SAG);
				createHeaderNameRow(sheet, ZkNodeConstant.NODE_TYPE_SAG);
			}
			List<ZkNode> sagNodeList = new ArrayList<ZkNode>();
			for (ZkNode node : sagGroupNode.getChildren()) {
				if (node.getNodeType() == ZkNodeConstant.NODE_TYPE_SAG_INFO) {
					sagNodeList.addAll(node.getChildren());
				}
			}
			Collections.sort(sagNodeList, new Comparator<ZkNode>() {

				@Override
				public int compare(ZkNode node1, ZkNode node2) {
					ZkSagVO vo1 = (ZkSagVO) node1.getZkNodeVO();
					ZkSagVO vo2 = (ZkSagVO) node2.getZkNodeVO();
					return (int) (vo1.getSagId() - vo2.getSagId());
				}
			});
			for (ZkNode sagNode : sagNodeList) {
				writeZkNodeData(sheet, sagNode);
			}
		} else if (sheetName.equals("BTS_GROUP_LIST")) {
			sheet = workBook.createSheet("BTS_GROUP_LIST");
			// 创建标题行
			if (isSheetEmpty(sheet)) {
				createHeaderRow(sheet, ZkNodeConstant.NODE_TYPE_BTS_GROUP);
				createHeaderNameRow(sheet, ZkNodeConstant.NODE_TYPE_BTS_GROUP);
			}
			List<ZkNode> btsGroupNodeList = new ArrayList<ZkNode>();
			for (ZkNode node : sagGroupNode.getChildren()) {
				if (node.getNodeType() == ZkNodeConstant.NODE_TYPE_BTS_INFO) {
					btsGroupNodeList.addAll(node.getChildren());
				}
			}
			Collections.sort(btsGroupNodeList, new Comparator<ZkNode>() {

				@Override
				public int compare(ZkNode node1, ZkNode node2) {
					ZkBtsGroupVO vo1 = (ZkBtsGroupVO) node1.getZkNodeVO();
					ZkBtsGroupVO vo2 = (ZkBtsGroupVO) node2.getZkNodeVO();
					return (int) (vo1.getBtsGroupId() - vo2.getBtsGroupId());
				}
			});
			for (ZkNode btsGroupNode : btsGroupNodeList) {
				writeZkNodeData(sheet, btsGroupNode);
			}
		} else if (sheetName.equals("BTS_LIST")) {
			sheet = workBook.createSheet("BTS_LIST");
			// 创建标题行
			if (isSheetEmpty(sheet)) {
				createHeaderRow(sheet, ZkNodeConstant.NODE_TYPE_BTS);
				createHeaderNameRow(sheet, ZkNodeConstant.NODE_TYPE_BTS);
			}
			List<ZkNode> btsGroupNodeList = new ArrayList<ZkNode>();
			List<ZkNode> btsNodeList = new ArrayList<ZkNode>();
			for (ZkNode node : sagGroupNode.getChildren()) {
				if (node.getNodeType() == ZkNodeConstant.NODE_TYPE_BTS_INFO) {
					btsGroupNodeList.addAll(node.getChildren());
				}
			}
			for (ZkNode btsGroupNode : btsGroupNodeList) {
				for (ZkNode btsNode : btsGroupNode.getChildren()) {
					if (btsNode.getNodeType() == ZkNodeConstant.NODE_TYPE_BTS) {
						btsNodeList.add(btsNode);
					}
				}
			}
			Collections.sort(btsNodeList, new Comparator<ZkNode>() {

				@Override
				public int compare(ZkNode node1, ZkNode node2) {
					ZkBtsVO vo1 = (ZkBtsVO) node1.getZkNodeVO();
					ZkBtsVO vo2 = (ZkBtsVO) node2.getZkNodeVO();
					return (int) (vo1.getBtsId() - vo2.getBtsId());
				}
			});
			for (ZkNode btsNode : btsNodeList) {
				writeZkNodeData(sheet, btsNode);
			}
		}
		setColumnAutoSize(sheet);
	}

	private void writeZkNodeData(HSSFSheet sheet, ZkNode node) {
		if (node == null)
			return;
		List<Object> objectList = new ArrayList<Object>();
		int nodeType = node.getNodeType();

		switch (nodeType) {
		case ZkNodeConstant.NODE_TYPE_SAG_GROUP:

			ZkSagGroupVO sagGroupVo = (ZkSagGroupVO) node.getZkNodeVO();
			objectList.add(node.getName());
			objectList.add(sagGroupVo.getSagGroupId());
			objectList.add(sagGroupVo.getMaxSubNode());
			// 创建行
			HSSFRow sagGroupRow = sheet.createRow(sheet.getLastRowNum() + 1);
			// 填充sheet
			fillSheet(objectList, sagGroupRow);
			break;
		case ZkNodeConstant.NODE_TYPE_SAG_INFO:

			ZkSagInfoVO sagInfoVo = (ZkSagInfoVO) node.getZkNodeVO();
			objectList.add(sagInfoVo.getMaxSubNode());
			// 创建行
			HSSFRow sagInfoRow = sheet.createRow(sheet.getLastRowNum() + 1);
			// 填充sheet
			fillSheet(objectList, sagInfoRow);
			break;
		case ZkNodeConstant.NODE_TYPE_BTS_INFO:

			ZkBtsInfoVO btsInfoVO = (ZkBtsInfoVO) node.getZkNodeVO();
			objectList.add(btsInfoVO.getMaxSubNode());
			// 创建行
			HSSFRow btsInfoRow = sheet.createRow(sheet.getLastRowNum() + 1);
			// 填充sheet
			fillSheet(objectList, btsInfoRow);
			break;
		case ZkNodeConstant.NODE_TYPE_RULES:

			ZkRulesVO rulesVO = (ZkRulesVO) node.getZkNodeVO();
			objectList.add(rulesVO.getAccessType());
			// objectList.add(rulesVO.getXmlData());
			// 创建行
			HSSFRow rulesRow = sheet.createRow(sheet.getLastRowNum() + 1);
			// 填充sheet
			fillSheet(objectList, rulesRow);
			break;
		case ZkNodeConstant.NODE_TYPE_SAG:

			ZkSagVO sagVo = (ZkSagVO) node.getZkNodeVO();
			for (int i = 0; i < SAG_PROPERTY_NAMES.length; i++) {
				if (SAG_PROPERTY_NAMES[i].equals("planBtsGroupIds")) {
					objectList.add(sagVo.getPlanBtsGroupIdsString());
					continue;
				}
				objectList.add(getProperty(sagVo, SAG_PROPERTY_NAMES[i]));
			}
			// 创建行
			HSSFRow sagRow = sheet.createRow(sheet.getLastRowNum() + 1);
			// 填充sheet
			fillSheet(objectList, sagRow);
			objectList.clear();
			// 填充SagDataNode
			{
				ZkSagDataVO sagDataVo = null;
				if (!node.getChildren().isEmpty()) {
					// Sag节点只有一个孩子节点
					sagDataVo = (ZkSagDataVO) ((ZkNode) node.getChildren()
							.toArray()[0]).getZkNodeVO();
					for (int i = 0; i < SAG_DATA_PROPERTY_NAMES.length; i++) {
						objectList.add(getProperty(sagDataVo,
								SAG_DATA_PROPERTY_NAMES[i]));
					}
				}
				// 填充sheet
				fillSheet(objectList, sagRow);
			}
			break;
		case ZkNodeConstant.NODE_TYPE_BTS_GROUP:

			ZkBtsGroupVO btsGroupVo = (ZkBtsGroupVO) node.getZkNodeVO();
			for (int i = 0; i < BTS_GROUP_PROPERTY_NAMES.length; i++) {
				objectList.add(getProperty(btsGroupVo,
						BTS_GROUP_PROPERTY_NAMES[i]));
			}
			// 创建行
			HSSFRow btsGroupRow = sheet.createRow(sheet.getLastRowNum() + 1);
			// 填充sheet
			fillSheet(objectList, btsGroupRow);
			break;
		case ZkNodeConstant.NODE_TYPE_BTS:
			ZkBtsVO btsVo = (ZkBtsVO) node.getZkNodeVO();
			ZkBtsGroupVO groupVO = (ZkBtsGroupVO) node.getParent()
					.getZkNodeVO();
			btsVo.setHomeBtsGroupId(groupVO.getBtsGroupId());
			for (int i = 0; i < BTS_PROPERTY_NAMES.length; i++) {
				objectList.add(getProperty(btsVo, BTS_PROPERTY_NAMES[i]));
			}
			// 创建行
			HSSFRow btsRow = sheet.createRow(sheet.getLastRowNum() + 1);
			// 填充sheet
			fillSheet(objectList, btsRow);
			break;
		default:
			break;
		}
	}

	// 获取node的属性值
	private Object getProperty(Object object, String propertyName) {
		// set方法属性名称首字母大写
		propertyName = propertyName.substring(0, 1).toUpperCase()
				+ propertyName.substring(1);
		Method method = ReflectUtils.findMethod(object.getClass(), "get"
				+ propertyName);
		if (method != null) {
			try {
				Object obj = method.invoke(object);
				if (propertyName.equalsIgnoreCase("btsId")) {
					return Long.toHexString((Long) obj);
				}
				return obj;
			} catch (Exception e) {
				log.error(e);
			}
		}
		return "";
	}

	// 设置header行
	private void createHeaderRow(HSSFSheet sheet, int nodeType) {
		HSSFCellStyle style = workBook.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);

		HSSFRow headerRow = null;
		HSSFCell cell = null;
		int cellIndex = 0;
		if (nodeType == ZkNodeConstant.NODE_TYPE_SAG_GROUP) {
			headerRow = sheet.createRow(sheet.getLastRowNum() + 1);
			for (int i = 0; i < SAG_GROUP_PROPERTY_NAMES.length; i++) {
				cell = headerRow.createCell(cellIndex++, Cell.CELL_TYPE_STRING);
				cell.setCellValue(SAG_GROUP_PROPERTY_NAMES[i]);
				cell.setCellStyle(style);
			}
		} else if (nodeType == ZkNodeConstant.NODE_TYPE_SAG_INFO) {
			headerRow = sheet.createRow(sheet.getLastRowNum() + 1);
			for (int i = 0; i < SAG_INFO_PROPERTY_NAMES.length; i++) {
				cell = headerRow.createCell(cellIndex++, Cell.CELL_TYPE_STRING);
				cell.setCellValue(SAG_INFO_PROPERTY_NAMES[i]);
				cell.setCellStyle(style);
			}
		} else if (nodeType == ZkNodeConstant.NODE_TYPE_BTS_INFO) {
			headerRow = sheet.createRow(sheet.getLastRowNum() + 1);
			for (int i = 0; i < BTS_INFO_PROPERTY_NAMES.length; i++) {
				cell = headerRow.createCell(cellIndex++, Cell.CELL_TYPE_STRING);
				cell.setCellValue(BTS_INFO_PROPERTY_NAMES[i]);
				cell.setCellStyle(style);
			}
		} else if (nodeType == ZkNodeConstant.NODE_TYPE_RULES) {
			headerRow = sheet.createRow(sheet.getLastRowNum() + 1);
			for (int i = 0; i < RULES_PROPERTY_NAMES.length; i++) {
				cell = headerRow.createCell(cellIndex++, Cell.CELL_TYPE_STRING);
				cell.setCellValue(RULES_PROPERTY_NAMES[i]);
				cell.setCellStyle(style);
			}
		} else if (nodeType == ZkNodeConstant.NODE_TYPE_SAG) {
			headerRow = sheet.createRow(0);
			for (int i = 0; i < SAG_PROPERTY_NAMES.length; i++) {
				cell = headerRow.createCell(cellIndex++, Cell.CELL_TYPE_STRING);
				cell.setCellValue(SAG_PROPERTY_NAMES[i]);
				cell.setCellStyle(style);
			}
			for (int i = 0; i < SAG_DATA_PROPERTY_NAMES.length; i++) {
				cell = headerRow.createCell(cellIndex++, Cell.CELL_TYPE_STRING);
				cell.setCellValue(SAG_DATA_PROPERTY_NAMES[i]);
				cell.setCellStyle(style);
			}
		} else if (nodeType == ZkNodeConstant.NODE_TYPE_BTS_GROUP) {
			headerRow = sheet.createRow(0);
			for (int i = 0; i < BTS_GROUP_PROPERTY_NAMES.length; i++) {
				cell = headerRow.createCell(cellIndex++, Cell.CELL_TYPE_STRING);
				cell.setCellValue(BTS_GROUP_PROPERTY_NAMES[i]);
				cell.setCellStyle(style);
			}
		} else if (nodeType == ZkNodeConstant.NODE_TYPE_BTS) {
			headerRow = sheet.createRow(0);
			for (int i = 0; i < BTS_PROPERTY_NAMES.length; i++) {
				cell = headerRow.createCell(cellIndex++, Cell.CELL_TYPE_STRING);
				cell.setCellValue(BTS_PROPERTY_NAMES[i]);
				cell.setCellStyle(style);
			}
		}
		headerRow.setZeroHeight(true);
	}

	// 设置header名称行
	private void createHeaderNameRow(HSSFSheet sheet, int nodeType) {
		HSSFCellStyle style = workBook.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);

		HSSFRow headerNameRow = null;
		HSSFCell cell = null;
		int cellIndex = 0;
		if (nodeType == ZkNodeConstant.NODE_TYPE_SAG_GROUP) {
			headerNameRow = sheet.createRow(sheet.getLastRowNum() + 1);
			for (int i = 0; i < SAG_GROUP_HEADER_NAMES.length; i++) {
				cell = headerNameRow.createCell(cellIndex++,
						Cell.CELL_TYPE_STRING);
				cell.setCellValue(SAG_GROUP_HEADER_NAMES[i]);
				cell.setCellStyle(style);
			}
		} else if (nodeType == ZkNodeConstant.NODE_TYPE_SAG_INFO) {
			headerNameRow = sheet.createRow(sheet.getLastRowNum() + 1);
			for (int i = 0; i < SAG_INFO_HEADER_NAMES.length; i++) {
				cell = headerNameRow.createCell(cellIndex++,
						Cell.CELL_TYPE_STRING);
				cell.setCellValue(SAG_INFO_HEADER_NAMES[i]);
				cell.setCellStyle(style);
			}
		} else if (nodeType == ZkNodeConstant.NODE_TYPE_BTS_INFO) {
			headerNameRow = sheet.createRow(sheet.getLastRowNum() + 1);
			for (int i = 0; i < BTS_INFO_HEADER_NAMES.length; i++) {
				cell = headerNameRow.createCell(cellIndex++,
						Cell.CELL_TYPE_STRING);
				cell.setCellValue(BTS_INFO_HEADER_NAMES[i]);
				cell.setCellStyle(style);
			}
		} else if (nodeType == ZkNodeConstant.NODE_TYPE_RULES) {
			headerNameRow = sheet.createRow(sheet.getLastRowNum() + 1);
			for (int i = 0; i < RULES_HEADER_NAMES.length; i++) {
				cell = headerNameRow.createCell(cellIndex++,
						Cell.CELL_TYPE_STRING);
				cell.setCellValue(RULES_HEADER_NAMES[i]);
				cell.setCellStyle(style);
			}
		} else if (nodeType == ZkNodeConstant.NODE_TYPE_SAG) {
			headerNameRow = sheet.createRow(1);
			for (int i = 0; i < SAG_HEADER_NAMES.length; i++) {
				cell = headerNameRow.createCell(cellIndex++,
						Cell.CELL_TYPE_STRING);
				cell.setCellValue(SAG_HEADER_NAMES[i]);
				cell.setCellStyle(style);
			}
			for (int i = 0; i < SAG_DATA_HEADER_NAMES.length; i++) {
				cell = headerNameRow.createCell(cellIndex++,
						Cell.CELL_TYPE_STRING);
				cell.setCellValue(SAG_DATA_HEADER_NAMES[i]);
				cell.setCellStyle(style);
			}
		} else if (nodeType == ZkNodeConstant.NODE_TYPE_BTS_GROUP) {
			headerNameRow = sheet.createRow(1);
			for (int i = 0; i < BTS_GROUP_HEADER_NAMES.length; i++) {
				cell = headerNameRow.createCell(cellIndex++,
						Cell.CELL_TYPE_STRING);
				cell.setCellValue(BTS_GROUP_HEADER_NAMES[i]);
				cell.setCellStyle(style);
			}
		} else if (nodeType == ZkNodeConstant.NODE_TYPE_BTS) {
			headerNameRow = sheet.createRow(1);
			for (int i = 0; i < BTS_HEADER_NAMES.length; i++) {
				cell = headerNameRow.createCell(cellIndex++,
						Cell.CELL_TYPE_STRING);
				cell.setCellValue(BTS_HEADER_NAMES[i]);
				cell.setCellStyle(style);
			}
		}
	}

	// 设置自动列宽
	private void setColumnAutoSize(HSSFSheet sheet) {
		int maxColumnIndex = 0;
		if (!isSheetEmpty(sheet)) {
			int columnIndex = 0;
			@SuppressWarnings("rawtypes")
			Iterator rowIterator = sheet.rowIterator();
			HSSFRow row = null;
			while (rowIterator.hasNext()) {
				row = (HSSFRow) rowIterator.next();
				columnIndex = row.getLastCellNum();
				if (columnIndex > maxColumnIndex)
					maxColumnIndex = columnIndex;
			}
		}
		for (int i = 0; i < maxColumnIndex; i++)
			sheet.autoSizeColumn(i);
	}

	private boolean isSheetEmpty(HSSFSheet sheet) {
		if (sheet.rowIterator().hasNext())
			return false;
		return true;
	}

	private void fillSheet(List<Object> propertyList, HSSFRow row) {
		int cellIndex = 0;
		if (row.getLastCellNum() > 0)
			cellIndex = row.getLastCellNum();
		CellStyle style = workBook.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);

		for (Object property : propertyList) {
			// 创建值单元格
			HSSFCell valueCell = row.createCell(cellIndex++);
			valueCell.setCellType(getCellType(property.getClass()));
			if (property.getClass().equals(byte[].class)) {
				byte[] valueBytes = (byte[]) property;
				char[] valueChars = new char[valueBytes.length];
				for (int i = 0; i < valueBytes.length; i++) {
					valueChars[i] = (char) valueBytes[i];
				}
				String valueString = new String(valueChars);
				property = valueString;
			}
			setCellValue(valueCell, property);
			valueCell.setCellStyle(style);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private int getCellType(Class type) {
		if (type == null)
			return Cell.CELL_TYPE_BLANK;
		else if (type.equals(Integer.class))
			return Cell.CELL_TYPE_NUMERIC;
		else if (type.equals(Long.class))
			return Cell.CELL_TYPE_NUMERIC;
		else if (type.equals(Double.class))
			return Cell.CELL_TYPE_NUMERIC;
		else if (type.equals(Float.class))
			return Cell.CELL_TYPE_NUMERIC;
		else if (type.equals(String.class))
			return Cell.CELL_TYPE_STRING;
		else
			return Cell.CELL_TYPE_STRING;
	}

	// 清除多余的sheet
	private void clearSheetofWorkBook() {
		while (workBook.getNumberOfSheets() > 1) {
			// 第一个sheet不可删除,所以从第二个开始删除
			HSSFSheet sheet = workBook.getSheetAt(1);
			if (sheet == null)
				break;
			workBook.removeSheetAt(1);
		}
	}

	// 清除sheet中的原有的Row
	private void clearRowsofSheet(HSSFSheet sheet) {
		if (isSheetEmpty(sheet))
			return;
		while (sheet.rowIterator().hasNext()) {
			sheet.removeRow(sheet.rowIterator().next());
		}
	}

	private boolean isValidHex(String number) {
		if (number.length() > 8)
			return false;
		for (char item : number.toLowerCase().toCharArray()) {
			if (item < 48 || (item > 57 && item < 97) || item > 102)
				return false;
		}
		return true;
	}

	// 判断是否是有效的整数字符串
	private boolean isValidInteger(String number) {
		// 判断是否存在小数点
		int index = number.indexOf(".");
		if (index >= 0) {
			for (int i = index + 1; i < number.length(); i++) {
				if (number.charAt(i) != '0') {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 格式化为整数字符串
	 * 
	 * @param str
	 * @return
	 */
	private String format2IntString(String str) {
		String propertyValue = str;
		if (propertyValue.indexOf(".") >= 0) {
			propertyValue = propertyValue.substring(0,
					propertyValue.indexOf("."));
		}
		return propertyValue;
	}
}