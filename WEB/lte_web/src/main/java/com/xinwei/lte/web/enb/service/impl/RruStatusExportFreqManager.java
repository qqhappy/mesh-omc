/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-12-31	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.xinwei.lte.web.MinasSession;
import com.xinwei.minas.core.facade.conf.XMoBizConfigFacade;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbStatusFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.status.EnbStatusConstants;
import com.xinwei.minas.enb.core.model.status.EnbStatusQueryCondition;
import com.xinwei.minas.enb.core.model.status.RruRfStatus;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;

/**
 * 
 * RRU动态信息导出管理类
 * 
 * 
 * @author chenlong
 * 
 */

public class RruStatusExportFreqManager {
	
	private static final RruStatusExportFreqManager instance = new RruStatusExportFreqManager();
	
	private String NO_SPLIT_STRING = "-";
	
	// 开关
	private String exportSwitch;
	
	// 查询频率 单位为min
	private String freq;
	
	// 重复查询次数
	private String repeatTimes;
	
	public static RruStatusExportFreqManager getInstance() {
		return instance;
	}
	
	public synchronized void initialize(String exportSwitch, String freq, String repeatTimes)
			throws Exception {
		System.out.println("exportSwitch=" + exportSwitch + ",freq = " + freq + ",repeatTimes=" + repeatTimes);
		this.repeatTimes = repeatTimes;
		RruStatusExportFreqTask task = new RruStatusExportFreqTask(
				Integer.valueOf(exportSwitch), Integer.valueOf(freq));
		Thread thread = new Thread(task);
		thread.start();
	}
	
	public void export() throws Exception {
		System.out.println("export start!");
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		EnbBasicFacade enbBaiscfacade = MinasSession.getInstance().getFacade(
				MinasSession.DEFAULT_SESSION_ID, EnbBasicFacade.class);
		EnbStatusFacade enbStatusFacade = MinasSession.getInstance().getFacade(
				MinasSession.DEFAULT_SESSION_ID, EnbStatusFacade.class);
		List<Enb> enbList = enbBaiscfacade.queryAllEnb();
		if (null != enbList && enbList.size() > 0) {
			StringBuilder stringBuilder = new StringBuilder();
			for (Enb enb : enbList) {
				if (enb.isConfigurable()) {
					// 查询数据并封装到stringBuilder中
					query(enb, stringBuilder, enbStatusFacade);
				}
			}
			// 导出到excel文件中
			exportExcel(stringBuilder,date);
		}
		System.out.println("export end!");
	}
	
	private void exportExcel(StringBuilder stringBuilder, String date) throws Exception {
		// String filePath = "d:"+File.separator+"csv"+File.separator+"rru_" + date + ".xls";
		String realPath = "/usr" + File.separator + "xinwei" + File.separator + "wireless";
		// 如果没有rruData目录 则创建一个
		String dataPath = realPath + File.separator + "rruData";
		File checkDir = new File(dataPath);
		if (!checkDir.exists() && !checkDir.isDirectory()) {
			checkDir.mkdir();
		}
		String filePath = dataPath + File.separator + "rru_" + date + ".xls";
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet indexSheet = wb.createSheet();
		HSSFRow titleRow = indexSheet.createRow(0);
		String[] titles = {"基站标识","RRU","通道号","查询次数","接收功率(dbm)","通道驻波比","查询时间"};
		for (int i = 0; i < titles.length; i++) {
			HSSFCell titleCell = titleRow.createCell(i);
			titleCell.setCellValue(titles[i]);
		}
		
		String[] datas = stringBuilder.toString().split(";");
		for (int i = 0; i < datas.length; i++) {
			HSSFRow row = indexSheet.createRow((i+1));
			String[] dataArray = datas[i].split(",");
			for (int j = 0; j < dataArray.length; j++) {
				HSSFCell cell = row.createCell(j);
				cell.setCellValue(dataArray[j]);
			}
		}
		FileOutputStream fos = new FileOutputStream(new File(filePath));		
		wb.write(fos);
	}

	private void query(Enb enb, StringBuilder stringBuilder, EnbStatusFacade enbStatusFacade) {

		List<String> boardList = queryBoardList(enb);
		for (int i = 0; i < Integer.valueOf(repeatTimes.trim()); i++) {
			if (null != boardList) {
				for (String boardFlag : boardList) {
					String[] noArray = boardFlag
							.split(NO_SPLIT_STRING);
					// RRU射频状态列表
					List<RruRfStatus> rruRfStatusList = new ArrayList<RruRfStatus>();
					EnbStatusQueryCondition rruRfStatusCondition = generateQueryBoardStatusCondition(
							EnbStatusConstants.RRU_RF_STATUS,
							enb.getEnbId(),
							Integer.valueOf(noArray[0]),
							Integer.valueOf(noArray[1]),
							Integer.valueOf(noArray[2]));
					
					List rruRfList = null;
					try {
						rruRfList = enbStatusFacade
								.queryStatus(enb.getMoId(),
										rruRfStatusCondition);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					if (null != rruRfList && rruRfList.size() > 0) {
						for (Object status : rruRfList) {
							RruRfStatus rruRfStatus = (RruRfStatus) status;
							stringBuilder.append(switchEnbId(enb.getEnbId()));
							stringBuilder.append("," + (getRru(Integer.valueOf(noArray[0]))));
							stringBuilder.append("," + (rruRfStatus.getChannelNo() + 1));
							stringBuilder.append("," + (i+1));
							stringBuilder.append("," + parseDecimalPoint(rruRfStatus.getReceivePower() * 0.1));
							stringBuilder.append("," + parseDecimalPoint(rruRfStatus.getVswr() * 0.1));
							stringBuilder.append("," + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
							stringBuilder.append(";");
							rruRfStatusList.add(rruRfStatus);
						}
					}
				}
			}
		}
		
	}

	/**
	 * 查询单板列表
	 * 
	 * @param onlyRRU
	 *            只查询RRU板
	 * @return
	 * @throws Exception
	 */
	private List<String> queryBoardList(Enb enb) {
		
		XMoBizConfigFacade xMoBizConfigFacade = null;
		try {
			xMoBizConfigFacade = MinasSession.getInstance().getFacade(
					MinasSession.DEFAULT_SESSION_ID, XMoBizConfigFacade.class);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		XBizRecord condition = null;
		// 如果只查询RRU单板，则添加查询条件
		condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_BDTYPE,
				String.valueOf(EnbConstantUtils.BOARD_TYPE_RRU)));
		
		XBizTable bizTable = null;
		try {
			bizTable = xMoBizConfigFacade.queryFromEms(enb.getMoId(),
					EnbConstantUtils.TABLE_NAME_T_BOARD, condition, false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		List<XBizRecord> recordList = bizTable.getRecords();
		if (recordList == null || recordList.isEmpty()) {
			return null;
		}
		else {
			List<String> boardList = new ArrayList<String>();
			for (XBizRecord bizRecord : recordList) {
				int[] array = getRackShelfSlotNo(bizRecord);
				boardList.add(array[0] + NO_SPLIT_STRING + array[1]
						+ NO_SPLIT_STRING + array[2]);
			}
			return boardList;
		}
	}
	
	/**
	 * 从单板表记录中获取架、框、槽号
	 * 
	 * @param bizRecord
	 * @return
	 */
	private int[] getRackShelfSlotNo(XBizRecord bizRecord) {
		XBizField rackNoField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_RACKNO);
		XBizField shelfNoField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SHELFNO);
		XBizField slotNoField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SLOTNO);
		int rackNo = Integer.valueOf(rackNoField.getValue());
		int shelfNo = Integer.valueOf(shelfNoField.getValue());
		int slotNo = Integer.valueOf(slotNoField.getValue());
		return new int[] { rackNo, shelfNo, slotNo };
	}
	
	private EnbStatusQueryCondition generateQueryBoardStatusCondition(
			String statusFlag, Long enbId, int rackNo, int shelfNo, int slotNo) {
		EnbStatusQueryCondition condition = new EnbStatusQueryCondition();
		condition.setFlag(statusFlag);
		condition.setEnbId(enbId);
		condition.setRackNo(rackNo);
		condition.setShelfNo(shelfNo);
		condition.setSlotNo(slotNo);
		if (EnbStatusConstants.RRU_OPTICAL_STATUS.equals(statusFlag)) {
			condition
					.setModuleNo(EnbStatusConstants.FIBER_OPTICAL_MODULE_COUNT);
		}
		else if (EnbStatusConstants.RRU_OPTICAL_STATUS.equals(statusFlag)) {
			condition.setChannelNo(4);
		}
		
		return condition;
	}
	
	public String switchEnbId(long enbId) {
		String enbIdHex = Long.toHexString(enbId);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 8 - enbIdHex.length(); i++) {
			sb.append(0);
		}
		sb.append(enbIdHex);
		return sb.toString();
	}
	
	public String getRru(int rackNo) {
		if(2 == rackNo) {
			return "RRU1";
		} else if(3 == rackNo) {
			return "RRU2";
		} else if(4 == rackNo) {
			return "RRU3"; 
		} else {
			return "RRU";
		}
	}
	
	/**
	 * 保留小数点后一位
	 * @param d
	 * @return
	 */
	public String parseDecimalPoint(double d) {
		DecimalFormat df = new DecimalFormat("#.0");
		String result = df.format(d);
		return result;
	}
	
	public String getExportSwitch() {
		return exportSwitch;
	}
	
	public void setExportSwitch(String exportSwitch) {
		this.exportSwitch = exportSwitch;
	}
	
	public String getFreq() {
		return freq;
	}
	
	public void setFreq(String freq) {
		this.freq = freq;
	}
}
