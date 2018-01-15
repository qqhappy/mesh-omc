/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-11-6	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model.check;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.model.status.RruOpticalStatus;
import com.xinwei.minas.enb.core.model.status.RruRfStatus;
import com.xinwei.minas.enb.core.model.status.RruRunningStatus;

/**
 * 
 * RRU检查结果
 * 
 * @author Administrator
 * 
 */

public class RruStatusResult extends DetailCheckResult {
	
	private int rackNo;
	
	// RRU上行射频开关查询(主键是通道号)
	private Map<Integer, Integer> ulRfSwitchMap = new HashMap<Integer, Integer>();
	
	// RRU下行射频开关查询(主键是通道号)
	private Map<Integer, Integer> dlRfSwitchMap = new HashMap<Integer, Integer>();
	
	// RRU射频状态列表
	private List<RruRfStatus> rruRfStatusList = new ArrayList<RruRfStatus>();
	
	// RRU运行状态列表
	private List<RruRunningStatus> rruRunningStatusList = new ArrayList<RruRunningStatus>();
	
	// RRU光模块状态列表
	private List<RruOpticalStatus> rruOpticalStatusList = new ArrayList<RruOpticalStatus>();
	
	@Override
	public int check() throws Exception {
		int checkResult = EnbCheckConstants.NORMAL;
		// 检查RRU射频上下行状态和RRU上下行射频开关
		for (RruRfStatus rruRfStatus : rruRfStatusList) {
			int ulAntStatus = rruRfStatus.getUlAntStatus();
			Integer ulRfSwitch = ulRfSwitchMap.get(rruRfStatus.getChannelNo());
			// 如果当天线开关打开时，检测天线状态为关闭，则检查不通过
			if(null != ulRfSwitch) {
				if(1 == ulRfSwitch && 0 == ulAntStatus) {
					return EnbCheckConstants.NOT_NORMAL;
				}
			}
			int dlAntStatus = rruRfStatus.getDlAntStatus();
			Integer dlRfSwitch = dlRfSwitchMap.get(rruRfStatus.getChannelNo());
			if(null != dlRfSwitch) {
				if(1 == dlRfSwitch && 0 == dlAntStatus) {
					return EnbCheckConstants.NOT_NORMAL;
				}
			}
		}
		return checkResult;
	}
	
	@Override
	public String checkDesc() throws Exception {
		StringBuilder sb = new StringBuilder();
		// 检查RRU射频上下行状态和RRU上下行射频开关
		for (RruRfStatus rruRfStatus : rruRfStatusList) {
			int ulAntStatus = rruRfStatus.getUlAntStatus();
			Integer ulRfSwitch = ulRfSwitchMap.get(rruRfStatus.getChannelNo());
			// 如果当天线开关打开时，检测天线状态为关闭，则检查不通过
			if(null != ulRfSwitch) {
				if(1 == ulRfSwitch && 0 == ulAntStatus) {
					sb.append("射频上行天线状态故障.\n");
					break;
				}
			}
		}
		for (RruRfStatus rruRfStatus : rruRfStatusList) {
			int dlAntStatus = rruRfStatus.getDlAntStatus();
			Integer dlRfSwitch = dlRfSwitchMap.get(rruRfStatus.getChannelNo());
			if(null != dlRfSwitch) {
				if(1 == dlRfSwitch && 0 == dlAntStatus) {
					sb.append("射频下行天线状态故障.\n");
					break;
				}
			}
		}
		return sb.toString();
	}
	
	@Override
	public String getCheckName() throws Exception {
		return getRru(rackNo) + "检查结果";
	}
	
	@Override
	public List<EnbCheckForm> getCheckForm() throws Exception {
		List<EnbCheckForm> checkForms = new ArrayList<EnbCheckForm>();
		// RRU运行状态
		getRruRunningStatus(checkForms);
		// RRU射频状态
		getRruRfStatus(checkForms);
		// RRU光模块
		getRruOpticalStatus(checkForms);
		return checkForms;
	}
	
	/**
	 * 获取RRU射频状态结果表格
	 * 
	 * @param checkForms
	 */
	private void getRruRfStatus(List<EnbCheckForm> checkForms) {
		if (null != rruRfStatusList && rruRfStatusList.size() > 0) {
			for (RruRfStatus rfStatus : rruRfStatusList) {
				// 选择通道 : 通道1 通道2 通道3 通道4 全部
				int channelNo = rfStatus.getChannelNo() + 1;
				// 下行天线状态
				if (rfStatus.getDlAntStatus() == 0) {
					addCheckForm(checkForms, "通道" + channelNo + ":下行天线状态",
							"关闭", "", EnbCheckConstants.NOT_CHECK);
				}
				else if (rfStatus.getDlAntStatus() == 1) {
					addCheckForm(checkForms, "通道" + channelNo + ":下行天线状态",
							"开启", "", EnbCheckConstants.NOT_CHECK);
				}
				else {
					addCheckForm(checkForms, "通道" + channelNo + ":下行天线状态",
							"未知", "", EnbCheckConstants.NOT_CHECK);
				}
				
				// 上行天线状态
				if (rfStatus.getUlAntStatus() == 0) {
					addCheckForm(checkForms, "通道" + channelNo + ":上行天线状态",
							"关闭", "", EnbCheckConstants.NOT_CHECK);
				}
				else if (rfStatus.getUlAntStatus() == 1) {
					addCheckForm(checkForms, "通道" + channelNo + ":上行天线状态",
							"开启", "", EnbCheckConstants.NOT_CHECK);
				}
				else {
					addCheckForm(checkForms, "通道" + channelNo + ":上行天线状态",
							"未知", "", EnbCheckConstants.NOT_CHECK);
				}
				
				// 通道温度 (℃)
				addCheckForm(checkForms, "通道" + channelNo + ":温度(℃)",
						String.valueOf(rfStatus.getChannelTemperature()), "",
						EnbCheckConstants.NOT_CHECK);
				// 发射功率(0.1dBm)
				addCheckForm(checkForms, "通道" + channelNo + ":发射功率(dBm)",
						parseDecimalPoint(rfStatus.getSendPower() * 0.1), "",
						EnbCheckConstants.NOT_CHECK);
				// 发射增益(0.5dB)
				addCheckForm(checkForms, "通道" + channelNo + ":发射增益(dB)",
						parseDecimalPoint(rfStatus.getSendGain() * 0.5), "",
						EnbCheckConstants.NOT_CHECK);
				// 接收功率(0.1dBm)
				addCheckForm(checkForms, "通道" + channelNo + ":接收功率(dBm)",
						parseDecimalPoint(rfStatus.getReceivePower() * 0.1), "",
						EnbCheckConstants.NOT_CHECK);
				
				// 接收增益(0.5dB)
				addCheckForm(checkForms, "通道" + channelNo + ":接收增益(dB)",
						parseDecimalPoint(rfStatus.getReceiveGain() * 0.5), "",
						EnbCheckConstants.NOT_CHECK);
				// 通道驻波比(0.1)
				addCheckForm(checkForms, "通道" + channelNo + ":通道驻波比",
						parseDecimalPoint(rfStatus.getVswr() * 0.1), "",
						EnbCheckConstants.NOT_CHECK);
				// 下行功率读取结果
				if (rfStatus.getDlPowerReadResult() == 0) {
					addCheckForm(checkForms, "通道" + channelNo + ":下行功率读取结果", "成功",
							"", EnbCheckConstants.NOT_CHECK);
				}
				else if (rfStatus.getDlPowerReadResult() == 1) {
					addCheckForm(checkForms, "通道" + channelNo + ":下行功率读取结果", "失败",
							"", EnbCheckConstants.NOT_CHECK);
				}
				else {
					addCheckForm(checkForms, "通道" + channelNo + ":下行功率读取结果", "未知",
							"", EnbCheckConstants.NOT_CHECK);
				}
				
				// 上行功率读取结果
				if (rfStatus.getUlPowerReadResult() == 0) {
					addCheckForm(checkForms, "通道" + channelNo + ":上行功率读取结果", "成功",
							"", EnbCheckConstants.NOT_CHECK);
				}
				else if (rfStatus.getUlPowerReadResult() == 1) {
					addCheckForm(checkForms, "通道" + channelNo + ":上行功率读取结果", "失败",
							"", EnbCheckConstants.NOT_CHECK);
				}
				else {
					addCheckForm(checkForms, "通道" + channelNo + ":上行功率读取结果", "未知",
							"", EnbCheckConstants.NOT_CHECK);
				}
				
				// 驻波比计算结果
				if (rfStatus.getVswrCalResult() == 0) {
					addCheckForm(checkForms, "通道" + channelNo + ":驻波比计算结果",
							"成功", "", EnbCheckConstants.NOT_CHECK);
				}
				else if (rfStatus.getVswrCalResult() == 1) {
					addCheckForm(checkForms, "通道" + channelNo + ":驻波比计算结果",
							"失败", "", EnbCheckConstants.NOT_CHECK);
				}
				else {
					addCheckForm(checkForms, "通道" + channelNo + ":驻波比计算结果",
							"未知", "", EnbCheckConstants.NOT_CHECK);
				}
				
			}
		}
	}
	
	/**
	 * 获取RRU运行状态结果表格
	 * 
	 * @param checkForms
	 * @throws Exception 
	 */
	private void getRruRunningStatus(List<EnbCheckForm> checkForms) throws Exception {
		if (null != rruRunningStatusList && rruRunningStatusList.size() > 0) {
			for (RruRunningStatus rruRunningStatus : rruRunningStatusList) {
				// 射频本振频率(单位:10KHz)
				addCheckForm(checkForms, "射频本振频率(10KHz)",
						String.valueOf(rruRunningStatus.getRfLocalFreq()), "",
						EnbCheckConstants.NOT_CHECK);
				// 射频本振状态
				if (rruRunningStatus.getRfLocalStatus() == 0) {
					addCheckForm(checkForms, "射频本振状态", "锁定", "",
							EnbCheckConstants.NOT_CHECK);
				}
				else if (rruRunningStatus.getRfLocalStatus() == 1) {
					addCheckForm(checkForms, "射频本振状态", "失锁", "",
							EnbCheckConstants.NOT_CHECK);
				}
				else {
					addCheckForm(checkForms, "射频本振状态", "未知", "",
							EnbCheckConstants.NOT_CHECK);
				}
				// 时钟状态
				if (rruRunningStatus.getClockStatus() == 0) {
					addCheckForm(checkForms, "时钟状态", "同步", "",
							EnbCheckConstants.NOT_CHECK);
				}
				else if (rruRunningStatus.getClockStatus() == 1) {
					addCheckForm(checkForms, "时钟状态", "失步", "",
							EnbCheckConstants.NOT_CHECK);
				}
				else {
					addCheckForm(checkForms, "时钟状态", "未知", "",
							EnbCheckConstants.NOT_CHECK);
				}
				// Ir接口工作模式
				if (rruRunningStatus.getIrInfWorkMode() == 1) {
					addCheckForm(checkForms, "Ir接口工作模式", "普通模式", "",
							EnbCheckConstants.NOT_CHECK);
				}
				else if (rruRunningStatus.getIrInfWorkMode() == 2) {
					addCheckForm(checkForms, "Ir接口工作模式", "级联模式", "",
							EnbCheckConstants.NOT_CHECK);
				}
				else {
					addCheckForm(checkForms, "Ir接口工作模式", "未知", "",
							EnbCheckConstants.NOT_CHECK);
				}
				// RRU运行状态
				if (rruRunningStatus.getRunningStatus() == 0) {
					addCheckForm(checkForms, "RRU运行状态", "未运营", "",
							EnbCheckConstants.NOT_CHECK);
				}
				else if (rruRunningStatus.getRunningStatus() == 1) {
					addCheckForm(checkForms, "RRU运行状态", "测试中", "",
							EnbCheckConstants.NOT_CHECK);
				}
				else if (rruRunningStatus.getRunningStatus() == 2) {
					addCheckForm(checkForms, "RRU运行状态", "运营中", "",
							EnbCheckConstants.NOT_CHECK);
				}
				else if (rruRunningStatus.getRunningStatus() == 3) {
					addCheckForm(checkForms, "RRU运行状态", "版本升级中", "",
							EnbCheckConstants.NOT_CHECK);
				}
				else if (rruRunningStatus.getRunningStatus() == 4) {
					addCheckForm(checkForms, "RRU运行状态", "故障", "",
							EnbCheckConstants.NOT_CHECK);
				}
				else {
					addCheckForm(checkForms, "RRU运行状态", "未知", "",
							EnbCheckConstants.NOT_CHECK);
				}
				// 主板温度(℃)
				addCheckForm(checkForms, "主板温度(℃) ",
						String.valueOf(rruRunningStatus.getMainBoardTemp()),
						"", EnbCheckConstants.NOT_CHECK);
				// 从板温度(℃)
				addCheckForm(checkForms, "从板温度(℃) ",
						String.valueOf(rruRunningStatus.getSlaveBoardTemp()),
						"", EnbCheckConstants.NOT_CHECK);
				// DPD训练结果
				try {
					String[] dpdResult = getDpdResult(rruRunningStatus.getChannelNum(), rruRunningStatus.getDpdTrainResult());
					for (int i = 0; i < dpdResult.length; i++) {
						addCheckForm(checkForms, "DPD训练结果,通道"+(i+1),
								dpdResult[i],
								"", EnbCheckConstants.NOT_CHECK);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 获取dpd训练结果数组
	 * @param channelNum
	 * @param dpdTrainResult
	 * @return
	 */
	public String[] getDpdResult(int channelNum,int dpdTrainResult) throws Exception {
		String[] dpds = new String[channelNum];
		String binaryArray = "";
		if(2 == channelNum) {
			binaryArray = "11";
		}
		else if(4 == channelNum) {
			binaryArray = getBinaryArray(dpdTrainResult);
		} else if(8 == channelNum) {
			int temp = dpdTrainResult >>> 4;
			binaryArray = getBinaryArray(temp);
			temp = dpdTrainResult << 28;
			temp = temp >>> 28;
			binaryArray = binaryArray + getBinaryArray(temp);
		}
		char[] charArray = binaryArray.toCharArray();
		for (int i = 0; i < dpds.length; i++) {
			if(charArray[channelNum - i - 1] == '0') {
				dpds[i] = "失败";
			} else {
				dpds[i] = "成功";
			} 
		}
		return dpds;
	}
	
	
	public String getBinaryArray(int number) {
		if(number == 0) {
			return "0000";
		} else if(number == 1) {
			return "0001";
		} else if(number == 2) {
			return "0010";
		} else if(number == 3) {
			return "0011";
		} else if(number == 4) {
			return "0100";
		} else if(number == 5) {
			return "0101";
		} else if(number == 6) {
			return "0110";
		} else if(number == 7) {
			return "0111";
		} else if(number == 8) {
			return "1000";
		} else if(number == 9) {
			return "1001";
		} else if(number == 10) {
			return "1010";
		} else if(number == 11) {
			return "1011";
		} else if(number == 12) {
			return "1100";
		} else if(number == 13) {
			return "1101";
		} else if(number == 14) {
			return "1110";
		} else if(number == 15) {
			return "1111";
		} else {
			return "";
		} 
	}
	
	
	/**
	 * 获取光模块状态结果表格
	 * 
	 * @param checkForms
	 */
	private void getRruOpticalStatus(List<EnbCheckForm> checkForms) {
		if (null != rruOpticalStatusList && rruOpticalStatusList.size() > 0) {
			for (RruOpticalStatus rruOpticalStatus : rruOpticalStatusList) {
				int moduleNo = rruOpticalStatus.getModuleNo() + 1;
				// 收功率0.1uW
				addCheckForm(
						checkForms,
						"光口" + moduleNo + ":收功率(uW)",
						parseDecimalPoint(rruOpticalStatus.getReceivePower() * 0.1),
						"", EnbCheckConstants.NOT_CHECK);
				// 发功率0.1uW
				addCheckForm(checkForms, "光口" + moduleNo + ":发功率(uW)",
						parseDecimalPoint(rruOpticalStatus.getSendPower() * 0.1),
						"", EnbCheckConstants.NOT_CHECK);
			
				// 电压
				addCheckForm(checkForms, "光口" + moduleNo + ":电压(mV)",
						String.valueOf(rruOpticalStatus.getVoltage()), "",
						EnbCheckConstants.NOT_CHECK);
				// 电流
				addCheckForm(checkForms, "光口" + moduleNo + ":电流(mA)",
						String.valueOf(rruOpticalStatus.getCurrent()), "",
						EnbCheckConstants.NOT_CHECK);
				// 温度
				addCheckForm(checkForms, "光口" + moduleNo + ":温度(℃)",
						String.valueOf(rruOpticalStatus.getTemperature()), "",
						EnbCheckConstants.NOT_CHECK);
				// 光模块传输bit速率
				addCheckForm(checkForms, "光口" + moduleNo + ":传输bit速率(Mbit/s)",
						String.valueOf(rruOpticalStatus.getTransBitRate()), "",
						EnbCheckConstants.NOT_CHECK);
			}
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
	
	public List<RruRfStatus> getRruRfStatusList() {
		return rruRfStatusList;
	}
	
	public void setRruRfStatusList(List<RruRfStatus> rruRfStatusList) {
		this.rruRfStatusList = rruRfStatusList;
	}
	
	public List<RruRunningStatus> getRruRunningStatusList() {
		return rruRunningStatusList;
	}
	
	public void setRruRunningStatusList(
			List<RruRunningStatus> rruRunningStatusList) {
		this.rruRunningStatusList = rruRunningStatusList;
	}
	
	public List<RruOpticalStatus> getRruOpticalStatusList() {
		return rruOpticalStatusList;
	}
	
	public void setRruOpticalStatusList(
			List<RruOpticalStatus> rruOpticalStatusList) {
		this.rruOpticalStatusList = rruOpticalStatusList;
	}
	
	public int getRackNo() {
		return rackNo;
	}
	
	public void setRackNo(int rackNo) {
		this.rackNo = rackNo;
	}
	
	public Map<Integer, Integer> getUlRfSwitchMap() {
		return ulRfSwitchMap;
	}
	
	public void setUlRfSwitchMap(Map<Integer, Integer> ulRfSwitchMap) {
		this.ulRfSwitchMap = ulRfSwitchMap;
	}
	
	public Map<Integer, Integer> getDlRfSwitchMap() {
		return dlRfSwitchMap;
	}
	
	public void setDlRfSwitchMap(Map<Integer, Integer> dlRfSwitchMap) {
		this.dlRfSwitchMap = dlRfSwitchMap;
	}
	
	public static void main(String[] args) {
		int i = 100;//1010
		int j = 100;
		i  = i >>> 20;
		j = j >> 20;
		System.out.println(i);
		System.out.println(j);
		
	}
}
