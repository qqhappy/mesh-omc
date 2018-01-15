/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-30	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.enbapi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * eNodeB场景规则引擎
 * 
 * @author zhoujie
 * 
 */
// public interface EnbSceneRuleEngine {
public class EnbSceneRuleEngine {
	public static void main(String[] args) throws Exception {

		List<EnbToOamDispItem> mm = new ArrayList<EnbToOamDispItem>();
		ConfigIntf aa = new ConfigIntf();
		DataInputInfo dataInfo = new DataInputInfo();
		List<EnbIputItem> inputList = new ArrayList<EnbIputItem>();
		DataInputInfo bb = new DataInputInfo();
		int languageInd = 1;// 语言选项，1表示中文，2表示英文
		int retValue = 0;

		// 根据语言选项获取场景显示项
		mm = aa.GetDispConfig(languageInd);

		System.out.println("===========网管界面配置向导显示.begin===========");
		for (int i = 0; i < mm.size(); i++) {
			EnbToOamDispItem map = mm.get(i);
			int Id = map.getId();
			String Name = map.getName();
			System.out.println("-Id:" + Id + "\t\t -" + "Name:" + Name);
			map.getOptionDataInfo();
		}
		System.out.println("===========网管界面配置向导显示.end===========");

		int[] au8mcc = { 4, 6, 0 };
		int[] au8mnc = { 0, 0, 0xff };

		dataInfo.setMCC(au8mcc);
		dataInfo.setMNC(au8mnc);
		dataInfo.seteNBName("L3Enb");
		dataInfo.setCellLable("Cell1");
		dataInfo.setFreqBandInd(38);
		dataInfo.setCenterFreq(38050);
		dataInfo.setSysBandWidth(1);
		dataInfo.setPhyCellId(160);
		dataInfo.setTopoNO(0);
		// dataInfo.setStatus(1);
		dataInfo.setManualOP(0);
		dataInfo.setRootSeqIndex(0);

		EnbSceneOutput cc = new EnbSceneOutput();

		EnbIputItem enbDisp = new EnbIputItem(1, 2);
		inputList.add(enbDisp);
		EnbIputItem enbDisp1 = new EnbIputItem(2, 39);
		inputList.add(enbDisp1);
		EnbIputItem enbDisp2 = new EnbIputItem(3, 2);
		inputList.add(enbDisp2);
		EnbIputItem enbDisp3 = new EnbIputItem(4, 2);
		inputList.add(enbDisp3);
		EnbIputItem enbDisp4 = new EnbIputItem(5, 17);
		inputList.add(enbDisp4);
		EnbIputItem enbDisp5 = new EnbIputItem(6, 1);
		inputList.add(enbDisp5);

		System.out.println("===========输入参数检查.begin===========");
		try {
			checkEnbPara(languageInd, dataInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("===========输入参数检查.end===========");

		cc = generate(languageInd,inputList);
	}

	// private static void SearhData(EnbSceneInput input) {
	public static EnbSceneOutput generate(int languageInd,List<EnbIputItem> inputList) throws Exception {
		int DataNum = 0;
		OutDataInfo ff[] = new OutDataInfo[50];
		EnbSceneOutput cc = new EnbSceneOutput();
		int ERSnorValue = 0;
        int CellSpeRefSigPwr = 13;
		int CRS_EPRE_max = 0;
		int Cfi = 3;
		
		for (int n = 0; n < ff.length; n++)
			ff[n] = new OutDataInfo();

		OamInputDate input = getConfigDate(inputList);

		String Scene = getSceneString(input.Scene);// 获取场景
		String FreqBand = getFreqBandString(input.FreqBand);// 获取频段
		String SysBandWidth = getBandWidthString(input.SysBandWidth);// 获取带宽
		String SfCfg = getSfCfgString(input.SfCfg);// 获取子帧配比
		String RruType = getRruTypeString(input.RruType, input.AnNum);// 获取RRU类型

		if ((null == Scene) || (null == FreqBand) || (null == Scene)
				|| (null == SfCfg) || (null == RruType)) {
			if (1 == languageInd)
				throw new Exception("输入有误");
			else
				throw new Exception("Input Info Error");			
		}
		
		if ("RRU2J30_CC&an4" == RruType)
		{	
			if (1 == languageInd)
				throw new Exception("RRU2J30_CC只支持2发2收");
			else
				throw new Exception("RRU2J30_CC Only support 2R2T");
		}
		
		//限制超远覆盖场景下选择配比2，即超远覆盖和配比2互斥
		if (("超远覆盖" == Scene) && ("配比2" == SfCfg))
		{
			if (1 == languageInd)
				throw new Exception("超远覆盖场景下不能选择配比2");
			else
				throw new Exception("The Scene Not Support SfCfg 2");
		}
		
		if ("配比2" == SfCfg)
		{
			Cfi = 2;
		}
		//上、下行流量场景带宽不能选5M
		if (("上行流量" == Scene) || ("下行流量" == Scene))
		{
			Cfi = 1;
		}
		
		// 创建一个DocumentBuilderFactory的对象
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		Map<String, Map<String, String>> tableMap = cc.getTableMap();
		
		try {
			// 创建DocumentBuilder对象
			DocumentBuilder db = dbf.newDocumentBuilder();
			// 通过DocumentBuilder对象的parser方法加载DataConfigXml.xml文件到当前项目下
			Document document = db
					.parse("plugins/enb/provision/DataConfigXml.xml");
			// 获取所有表节点的集合
			NodeList tableList = document.getElementsByTagName("table");
			// 通过nodelist的getLength()方法可以获取tableList的长度
			System.out.println("一共有" + tableList.getLength() + "张表");

			if (0 == input.AnNum)// 输入最大天线数为1
			{
				// 表名：T_CEL_PARA、字段名：u8UlAntNum
				ff[DataNum].tableName = "T_CEL_PARA";
				ff[DataNum].fieldName = "u8UlAntNum";
				ff[DataNum].fieldValue = "0";

				Map<String, String> fieldMap = tableMap
						.get(ff[DataNum].tableName);
				if (null == fieldMap) {
					fieldMap = new HashMap();
					tableMap.put("T_CEL_PARA", fieldMap);
				}

				fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
				DataNum++;

				// 表名：T_CEL_PARA、字段名：u8UlAntUsdNum
				ff[DataNum].tableName = "T_CEL_PARA";
				ff[DataNum].fieldName = "u8UlAntUsdNum";
				ff[DataNum].fieldValue = "0";

				Map<String, String> fieldMap1 = tableMap
						.get(ff[DataNum].tableName);
				if (null == fieldMap1) {
					fieldMap1 = new HashMap();
					tableMap.put("T_CEL_PARA", fieldMap1);
				}
				fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
				DataNum++;

				// 表名：T_CEL_PARA、字段名：u8UeTransMode
				ff[DataNum].tableName = "T_CEL_PARA";
				ff[DataNum].fieldName = "u8UeTransMode";
				ff[DataNum].fieldValue = "0";

				Map<String, String> fieldMap2 = tableMap
						.get(ff[DataNum].tableName);
				if (null == fieldMap2) {
					fieldMap2 = new HashMap();
					tableMap.put("T_CEL_PARA", fieldMap2);
				}

				fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
				DataNum++;			

				// 表名：T_CEL_PARA、字段名：u8DlAntPortNum
				ff[DataNum].tableName = "T_CEL_PARA";
				ff[DataNum].fieldName = "u8DlAntPortNum";
				ff[DataNum].fieldValue = "0";

				Map<String, String> fieldMap3 = tableMap
						.get(ff[DataNum].tableName);
				if (null == fieldMap3) {
					fieldMap3 = new HashMap();
					tableMap.put("T_CEL_PARA", fieldMap3);
				}
				fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
				DataNum++;
				
				// 表名：T_CEL_PARA、字段名：u8DlAntNum
				ff[DataNum].tableName = "T_CEL_PARA";
				ff[DataNum].fieldName = "u8DlAntNum";
				ff[DataNum].fieldValue = "0";

				Map<String, String> fieldMap4 = tableMap
						.get(ff[DataNum].tableName);
				if (null == fieldMap4) {
					fieldMap4 = new HashMap();
					tableMap.put("T_CEL_PARA", fieldMap4);
				}

				fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
				DataNum++;

				// 表名：T_CEL_PARA、字段名：u8DlAntUsdNum
				ff[DataNum].tableName = "T_CEL_PARA";
				ff[DataNum].fieldName = "u8DlAntUsdNum";
				ff[DataNum].fieldValue = "0";

				Map<String, String> fieldMap5 = tableMap
						.get(ff[DataNum].tableName);
				if (null == fieldMap5) {
					fieldMap5 = new HashMap();
					tableMap.put("T_CEL_PARA", fieldMap5);
				}

				fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
				DataNum++;

				if (("RRU4B30_CF&an1" == RruType) ||
					    ("RRU4D30_CF&an1" == RruType) ||
					    ("RRU4E30_CF&an1" == RruType) ||
					    ("RRU4E30_CF&an1" == RruType) ||
					    ("RRU2J30_CC&an1" == RruType) ||
					    ("RRU4K30_CF&an1" == RruType) ||
					    ("RRU4J30_CF&an1" == RruType) ||
					    ("RRU4L30_CF&an1" == RruType) ||
					    ("RRU4E30a_CF&an1" == RruType))
				{
					ff[DataNum].tableName = "T_CEL_PARA";
					ff[DataNum].fieldName = "au8DlAntUsdIdx";
					ff[DataNum].fieldValue = "0000030000000000";
	
					Map<String, String> fieldMap6 = tableMap
							.get(ff[DataNum].tableName);
					if (null == fieldMap6) {
						fieldMap6 = new HashMap();
						tableMap.put("T_CEL_PARA", fieldMap6);
					}
					fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
					DataNum++;
	
					ff[DataNum].tableName = "T_CEL_PARA";
					ff[DataNum].fieldName = "au8DlAntPortMap";
					ff[DataNum].fieldValue = "04000000";
	
					Map<String, String> fieldMap7 = tableMap
							.get(ff[DataNum].tableName);
					if (null == fieldMap7) {
						fieldMap7 = new HashMap();
						tableMap.put("T_CEL_PARA", fieldMap7);
					}
					fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
					DataNum++;
					
					// 上行使能天线索引
					ff[DataNum].tableName = "T_CEL_PARA";
					ff[DataNum].fieldName = "au8UlAntUsdIdx";
					ff[DataNum].fieldValue = "0000030000000000";
	
					Map<String, String> fieldMap8 = tableMap
							.get(ff[DataNum].tableName);
					if (null == fieldMap8) {
						fieldMap8 = new HashMap();
						tableMap.put("T_CEL_PARA", fieldMap8);
					}
					fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
					DataNum++;						
				}
				else
				{
					ff[DataNum].tableName = "T_CEL_PARA";
					ff[DataNum].fieldName = "au8DlAntUsdIdx";
					ff[DataNum].fieldValue = "0100000000000000";
	
					Map<String, String> fieldMap6 = tableMap
							.get(ff[DataNum].tableName);
					if (null == fieldMap6) {
						fieldMap6 = new HashMap();
						tableMap.put("T_CEL_PARA", fieldMap6);
					}
					fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
					DataNum++;
	
					ff[DataNum].tableName = "T_CEL_PARA";
					ff[DataNum].fieldName = "au8DlAntPortMap";
					ff[DataNum].fieldValue = "01000000";
	
					Map<String, String> fieldMap7 = tableMap
							.get(ff[DataNum].tableName);
					if (null == fieldMap7) {
						fieldMap7 = new HashMap();
						tableMap.put("T_CEL_PARA", fieldMap7);
					}
					fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
					DataNum++;
					
					// 上行使能天线索引
					ff[DataNum].tableName = "T_CEL_PARA";
					ff[DataNum].fieldName = "au8UlAntUsdIdx";
					ff[DataNum].fieldValue = "0100000000000000";
	
					Map<String, String> fieldMap8 = tableMap
							.get(ff[DataNum].tableName);
					if (null == fieldMap8) {
						fieldMap8 = new HashMap();
						tableMap.put("T_CEL_PARA", fieldMap8);
					}
					fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
					DataNum++;		
				}
			}
			else if (1 == input.AnNum)// //2天线
			{
				if (("RRU4B30_CF&an2" == RruType) ||
				    ("RRU4D30_CF&an2" == RruType) ||
				    ("RRU4E30_CF&an2" == RruType) ||
				    ("RRU4E30_CF&an2" == RruType) ||
				    ("RRU2J30_CC&an2" == RruType) ||
				    ("RRU4K30_CF&an2" == RruType) ||
				    ("RRU4J30_CF&an2" == RruType) ||
				    ("RRU4L30_CF&an2" == RruType) ||
				    ("RRU4E30a_CF&an2" == RruType))
				{
					// 上行使能天线索引
					ff[DataNum].tableName = "T_CEL_PARA";
					ff[DataNum].fieldName = "au8UlAntUsdIdx";
					ff[DataNum].fieldValue = "0000030400000000";
	
					Map<String, String> fieldMap = tableMap
							.get(ff[DataNum].tableName);
					if (null == fieldMap) {
						fieldMap = new HashMap();
						tableMap.put("T_CEL_PARA", fieldMap);
					}
					fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
					DataNum++;	
					
					// 下行使能天线索引
					ff[DataNum].tableName = "T_CEL_PARA";
					ff[DataNum].fieldName = "au8DlAntUsdIdx";
					ff[DataNum].fieldValue = "0000030400000000";
	
					Map<String, String> fieldMap1 = tableMap
							.get(ff[DataNum].tableName);
					if (null == fieldMap1) {
						fieldMap1 = new HashMap();
						tableMap.put("T_CEL_PARA", fieldMap1);
					}
					fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
					DataNum++;		
					
					ff[DataNum].tableName = "T_CEL_PARA";
					ff[DataNum].fieldName = "au8DlAntPortMap";
					ff[DataNum].fieldValue = "04080000";
	
					Map<String, String> fieldMap2 = tableMap
							.get(ff[DataNum].tableName);
					if (null == fieldMap2) {
						fieldMap2 = new HashMap();
						tableMap.put("T_CEL_PARA", fieldMap2);
					}
					fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
					DataNum++;
				}
			}
			else if(2 == input.AnNum)//4天线
			{
				// 表名：T_CEL_PARA、字段名：u8UlAntNum
				ff[DataNum].tableName = "T_CEL_PARA";
				ff[DataNum].fieldName = "u8UlAntNum";
				ff[DataNum].fieldValue = "2";

				Map<String, String> fieldMap = tableMap
						.get(ff[DataNum].tableName);
				if (null == fieldMap) {
					fieldMap = new HashMap();
					tableMap.put("T_CEL_PARA", fieldMap);
				}

				fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
				DataNum++;

				// 表名：T_CEL_PARA、字段名：u8UlAntUsdNum
				ff[DataNum].tableName = "T_CEL_PARA";
				ff[DataNum].fieldName = "u8UlAntUsdNum";
				ff[DataNum].fieldValue = "2";

				Map<String, String> fieldMap1 = tableMap
						.get(ff[DataNum].tableName);
				if (null == fieldMap1) {
					fieldMap1 = new HashMap();
					tableMap.put("T_CEL_PARA", fieldMap1);
				}
				fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
				DataNum++;

				// 上行使能天线索引
				ff[DataNum].tableName = "T_CEL_PARA";
				ff[DataNum].fieldName = "au8UlAntUsdIdx";
				ff[DataNum].fieldValue = "0102030400000000";

				Map<String, String> fieldMap2 = tableMap
						.get(ff[DataNum].tableName);
				if (null == fieldMap2) {
					fieldMap2 = new HashMap();
					tableMap.put("T_CEL_PARA", fieldMap2);
				}
				fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
				DataNum++;	
				
				if (("RRU4B30_CF&an4" == RruType) ||
					    ("RRU4D30_CF&an4" == RruType) ||
					    ("RRU4E30_CF&an4" == RruType) ||
					    ("RRU4E30_CF&an4" == RruType) ||
					    ("RRU4K30_CF&an4" == RruType) ||
					    ("RRU4J30_CF&an4" == RruType) ||
					    ("RRU4L30_CF&an4" == RruType) ||
					    ("RRU4E30a_CF&an4" == RruType))
				{
					// 下行使能天线索引
					ff[DataNum].tableName = "T_CEL_PARA";
					ff[DataNum].fieldName = "au8DlAntUsdIdx";
					ff[DataNum].fieldValue = "0000030400000000";
	
					Map<String, String> fieldMap3 = tableMap
							.get(ff[DataNum].tableName);
					if (null == fieldMap3) {
						fieldMap3 = new HashMap();
						tableMap.put("T_CEL_PARA", fieldMap3);
					}
					fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
					DataNum++;		
					
					ff[DataNum].tableName = "T_CEL_PARA";
					ff[DataNum].fieldName = "au8DlAntPortMap";
					ff[DataNum].fieldValue = "04080000";
	
					Map<String, String> fieldMap4 = tableMap
							.get(ff[DataNum].tableName);
					if (null == fieldMap4) {
						fieldMap4 = new HashMap();
						tableMap.put("T_CEL_PARA", fieldMap4);
					}
					fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
					DataNum++;	
				}
			}		
			
			ERSnorValue = getERSnorValue(input.SysBandWidth);//查表获取ERS_nor
			
			// 遍历每一个table节点
			for (int i = 0; i < tableList.getLength(); i++) {
				System.out.println("=================下面开始遍历第" + (i + 1)
						+ "张表的内容=================");

				// 通过 item(i)方法 获取一个table节点，nodelist的索引值从0开始
				Node table = tableList.item(i);

				// 获取table节点的所有属性集合
				NamedNodeMap attrs = table.getAttributes();
				System.out.println("第 " + (i + 1) + "张表共有" + attrs.getLength()
						+ "个属性");

				// 每个表只有一个属性
				Node attr = attrs.item(0);
				// 获取属性名
				System.out.print("属性名：" + attr.getNodeName());

				// 获取属性值
				System.out.println("--属性值:" + attr.getNodeValue());

				// 解析table节点的子节点
				NodeList childNodes = table.getChildNodes();

				// 遍历childNodes获取每个节点的节点名和节点值
				System.out.println("第" + (i + 1) + "张表共有"
						+ childNodes.getLength() + "行");

				for (int k = 0; k < childNodes.getLength(); k++) {
					// 区分出text类型的node以及element类型的node
					if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
						if (attr.getNodeValue().equals("RRU型号")) {
							if (RruType.equals(childNodes.item(k)
									.getAttributes().item(0).getNodeValue())) {
								// 表名：T_CEL_PARA、字段名：u16CellTransPwr
								ff[DataNum].tableName = "T_CEL_DLPC";
								ff[DataNum].fieldName = "u16CellTransPwr";
								ff[DataNum].fieldValue = childNodes.item(k)
										.getAttributes()
										.getNamedItem("实际最大发射功率")
										.getNodeValue();

								Map<String, String> fieldMap = tableMap
										.get(ff[DataNum].tableName);
								if (null == fieldMap) {
									fieldMap = new HashMap();
									tableMap.put("T_CEL_DLPC", fieldMap);
								}

								fieldMap.put(ff[DataNum].fieldName,
										ff[DataNum].fieldValue);
								DataNum++;
								System.out.println("--u16CellTransPwr："
										+ childNodes.item(k).getAttributes()
												.getNamedItem("实际最大发射功率")
												.getNodeValue());
								
								//计算T_CEL_DLPC表的u16CellSpeRefSigPwr可配置的最大值
								//下行天线端口数为1或者2 ,u16CellTransPwr
					            if (0 == input.AnNum)
					            {
					            	//CRS_EPRE_max = T_CEL_DLPC.u16CellTransPwr - 10*log10(u8DlAntPortNum) + ERS_nor 
					            	CRS_EPRE_max = Integer.valueOf(childNodes.item(k).getAttributes().getNamedItem("实际最大发射功率").getNodeValue()) + (ERSnorValue*10);
					            }
					            else
					            {
					               	CRS_EPRE_max = Integer.valueOf(childNodes.item(k).getAttributes().getNamedItem("实际最大发射功率").getNodeValue()) - 30 + (ERSnorValue*10);
					            }		
					            
					            if (CellSpeRefSigPwr*10 > CRS_EPRE_max)
					            {
					            	CellSpeRefSigPwr = CRS_EPRE_max/10;
					            }
					            CellSpeRefSigPwr += 60;
								ff[DataNum].tableName = "T_CEL_DLPC";
								ff[DataNum].fieldName = "u16CellSpeRefSigPwr";
								ff[DataNum].fieldValue = String.valueOf(CellSpeRefSigPwr);
								
								fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
								DataNum++;
								
								System.out.println("--CRS_EPRE_max："+ CRS_EPRE_max);
								System.out.println("--CellSpeRefSigPwr："+ CellSpeRefSigPwr);
								break;
							}
						}

						if (attr.getNodeValue().equals("FreqBand")) {
							if (childNodes.item(k).getAttributes()
									.getNamedItem(FreqBand).getNodeValue() != "") {
								ff[DataNum].tableName = childNodes.item(k)
										.getAttributes().getNamedItem("表名")
										.getNodeValue();
								ff[DataNum].fieldName = childNodes.item(k)
										.getAttributes().getNamedItem("字段名")
										.getNodeValue();
								ff[DataNum].fieldValue = childNodes.item(k)
										.getAttributes().getNamedItem(FreqBand)
										.getNodeValue();

								Map<String, String> fieldMap = tableMap
										.get(ff[DataNum].tableName);
								if (null == fieldMap) {
									fieldMap = new HashMap();
									tableMap.put(ff[DataNum].tableName,
											fieldMap);
								}

								fieldMap.put(ff[DataNum].fieldName,
										ff[DataNum].fieldValue);
								DataNum++;

								System.out.println("--表名："
										+ childNodes.item(k).getAttributes()
												.getNamedItem("表名")
												.getNodeValue());
								System.out.println("--字段名："
										+ childNodes.item(k).getAttributes()
												.getNamedItem("字段名")
												.getNodeValue());
								System.out.println("--value："
										+ childNodes.item(k).getAttributes()
												.getNamedItem(FreqBand)
												.getNodeValue());
							}
						}

						if (attr.getNodeValue().equals("SFCfg")) {
							if (childNodes.item(k).getAttributes()
									.getNamedItem(SfCfg).getNodeValue() != "") {
								ff[DataNum].tableName = childNodes.item(k)
										.getAttributes().getNamedItem("表名")
										.getNodeValue();
								ff[DataNum].fieldName = childNodes.item(k)
										.getAttributes().getNamedItem("字段名")
										.getNodeValue();
								ff[DataNum].fieldValue = childNodes.item(k)
										.getAttributes().getNamedItem(SfCfg)
										.getNodeValue();

								Map<String, String> fieldMap = tableMap
										.get(ff[DataNum].tableName);
								if (null == fieldMap) {
									fieldMap = new HashMap();
									tableMap.put(ff[DataNum].tableName,
											fieldMap);
								}

								fieldMap.put(ff[DataNum].fieldName,
										ff[DataNum].fieldValue);
								DataNum++;

								System.out.println("--表名："
										+ childNodes.item(k).getAttributes()
												.getNamedItem("表名")
												.getNodeValue());
								System.out.println("--字段名："
										+ childNodes.item(k).getAttributes()
												.getNamedItem("字段名")
												.getNodeValue());
								System.out.println("--value："
										+ childNodes.item(k).getAttributes()
												.getNamedItem(SfCfg)
												.getNodeValue());
							}
						}

						if (attr.getNodeValue().equals("BandWidth")) {
							if (childNodes.item(k).getAttributes()
									.getNamedItem(SysBandWidth).getNodeValue() != "") {
								ff[DataNum].tableName = childNodes.item(k)
										.getAttributes().getNamedItem("表名")
										.getNodeValue();
								ff[DataNum].fieldName = childNodes.item(k)
										.getAttributes().getNamedItem("字段名")
										.getNodeValue();
								ff[DataNum].fieldValue = childNodes.item(k)
										.getAttributes()
										.getNamedItem(SysBandWidth)
										.getNodeValue();

								Map<String, String> fieldMap = tableMap
										.get(ff[DataNum].tableName);
								if (null == fieldMap) {
									fieldMap = new HashMap();
									tableMap.put(ff[DataNum].tableName,
											fieldMap);
								}

								fieldMap.put(ff[DataNum].fieldName,
										ff[DataNum].fieldValue);
								DataNum++;

								System.out.println("--表名："
										+ childNodes.item(k).getAttributes()
												.getNamedItem("表名")
												.getNodeValue());
								System.out.println("--字段名："
										+ childNodes.item(k).getAttributes()
												.getNamedItem("字段名")
												.getNodeValue());
								System.out.println("--value："
										+ childNodes.item(k).getAttributes()
												.getNamedItem(SysBandWidth)
												.getNodeValue());
							}
						}

						if ((attr.getNodeValue().equals("应用场景"))
								& (Scene != "默认配置")) {
							if (childNodes.item(k).getAttributes()
									.getNamedItem(Scene).getNodeValue() != "") {
								ff[DataNum].tableName = childNodes.item(k)
										.getAttributes().getNamedItem("表名")
										.getNodeValue();
								ff[DataNum].fieldName = childNodes.item(k)
										.getAttributes().getNamedItem("字段名")
										.getNodeValue();
								ff[DataNum].fieldValue = childNodes.item(k)
										.getAttributes().getNamedItem(Scene)
										.getNodeValue();
								Map<String, String> fieldMap = tableMap
										.get(ff[DataNum].tableName);
								if (null == fieldMap) {
									fieldMap = new HashMap();
									tableMap.put(ff[DataNum].tableName,
											fieldMap);
								}

								fieldMap.put(ff[DataNum].fieldName,
										ff[DataNum].fieldValue);
								DataNum++;

								System.out.println("--表名："
										+ childNodes.item(k).getAttributes()
												.getNamedItem("表名")
												.getNodeValue());
								System.out.println("--字段名："
										+ childNodes.item(k).getAttributes()
												.getNamedItem("字段名")
												.getNodeValue());
								System.out.println("--value："
										+ childNodes.item(k).getAttributes()
												.getNamedItem(Scene)
												.getNodeValue());
							}
						}
					}
				}
				System.out.println("======================结束遍历第" + (i + 1)
						+ "个表的内容=================");
			}
			
			// 表名：T_CEL_PARA、字段名：u8UlAntNum
			ff[DataNum].tableName = "T_CEL_ALG";
			ff[DataNum].fieldName = "u8Cfi";
			ff[DataNum].fieldValue = Cfi+"";

			Map<String, String> fieldMap = tableMap
					.get(ff[DataNum].tableName);
			if (null == fieldMap) {
				fieldMap = new HashMap();
				tableMap.put("T_CEL_ALG", fieldMap);
			}

			fieldMap.put(ff[DataNum].fieldName, ff[DataNum].fieldValue);
			DataNum++;

			System.out.println("===================配置输出信息===================");
			System.out
					.println("--表名                                   --字段名                                                            --value");
			for (int n = 0; n < ff.length; n++) {
				if (ff[n].tableName == null) {
					break;
				}
				System.out.println("--" + ff[n].tableName + "\t   --"
						+ ff[n].fieldName + "\t\t --" + ff[n].fieldValue);
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Map<String, String>> printTableMap = cc.getTableMap();
		Set<String> printTableNames = printTableMap.keySet();
		for (String printTableName : printTableNames) {
			Map<String, String> printFieldMap = printTableMap
					.get(printTableName);
			Set<String> printFieldNames = printFieldMap.keySet();
			for (String printFieldName : printFieldNames) {
				System.out.println("tableName=" + printTableName
						+ ",fieldName=" + printFieldName + ",value="
						+ printFieldMap.get(printFieldName));
			}
		}

		return cc;
	}

	// 根据输入获取场景参数
	public static OamInputDate getConfigDate(List<EnbIputItem> inputList) {
		OamInputDate input = new OamInputDate();

		for (int i = 0; i < inputList.size(); i++) {
			switch (inputList.get(i).id) {
			case 1:
				input.Scene = inputList.get(i).value;
				continue;
			case 2:
				input.FreqBand = inputList.get(i).value;
				continue;
			case 3:
				input.SysBandWidth = inputList.get(i).value;
				continue;
			case 4:
				input.SfCfg = inputList.get(i).value;
				continue;
			case 5:
				input.RruType = inputList.get(i).value;
				continue;
			case 6:
				input.AnNum = inputList.get(i).value;
				continue;
			default:
				return null;
			}
		}
		return input;
	}

	// 获取场景信息
	private static String getSceneString(int SceneId) {
		switch (SceneId) {
		case 0:
			return "默认配置";
		case 1:
			return "市区覆盖";
		case 2:
			return "郊区覆盖";
		case 3:
			return "超远覆盖";
		case 4:
			return "上行流量";
		case 5:
			return "下行流量";
		default:
			return "null";
		}
	}

	// 获取频段信息
	private static String getFreqBandString(int FreqBandId) {
		switch (FreqBandId) {
		case 38:
			return "频段38";
		case 39:
			return "频段39";
		case 40:
			return "频段40";
		case 45:
			return "频段45";			
		case 53:
			return "频段53";
		case 58:
			return "频段58";
		case 59:
			return "频段59";			
		case 61:
			return "频段61";
		case 62:
			return "频段62";
		case 63:
			return "频段63";
		default:
			return "null";
		}
	}

	// 获取带宽信息
	private static String getBandWidthString(int BandWidthId) {
		switch (BandWidthId) {
		case 2:
			return "带宽5M";
		case 3:
			return "带宽10M";
		case 4:
			return "带宽15M";
		case 5:
			return "带宽20M";
		default:
			return "null";
		}
	}

	
	// 获取带宽信息获取ERS_nor,此时C类参数PA、PB分别取默认值2、1
	private static int getERSnorValue(int BandWidthId) {
		switch (BandWidthId) {
		case 2: //带宽5M
			return -22;
		case 3: //带宽10M
			return -25;
		case 4: //带宽15M
			return -27;
		case 5: //带宽20M
			return -28;
		default:
			return 0;
		}
	}
	
	// 获取子帧配比信息
	private static String getSfCfgString(int SfCfgId) {
		switch (SfCfgId) {
		case 0:
			return "配比0";
		case 1:
			return "配比1";
		case 2:
			return "配比2";
		default:
			return "null";
		}
	}

	// 获取RRU类型信息
	private static String getRruTypeString(int RruTypeId, int AnNum) {
		switch (RruTypeId) {
		case 1:// RRU8A10
			switch (AnNum) {
			case 0:
				return "RRU8A10&an1";
			case 1:
				return "RRU8A10&an2";
			case 2:
				return "RRU8A10&an4";
			case 3:
				return "RRU8A10&an8";
			default:
				return "null";
			}

		case 2:// RRU8B10
			switch (AnNum) {
			case 0:
				return "RRU8B10&an1";
			case 1:
				return "RRU8B10&an2";
			case 2:
				return "RRU8B10&an4";
			case 3:
				return "RRU8B10&an8";
			default:
				return "null";
			}

		case 3:// RRU8C10
			switch (AnNum) {
			case 0:
				return "RRU8C10&an1";
			case 1:
				return "RRU8C10&an2";
			case 2:
				return "RRU8C10&an4";
			case 3:
				return "RRU8C10&an8";
			default:
				return "null";
			}

		case 4:// RRU8D10
			switch (AnNum) {
			case 0:
				return "RRU8D10&an1";
			case 1:
				return "RRU8D10&an2";
			case 2:
				return "RRU8D10&an4";
			case 3:
				return "RRU8D10&an8";
			default:
				return "null";
			}

		case 5:// RRU8E10
			switch (AnNum) {
			case 0:
				return "RRU8E10&an1";
			case 1:
				return "RRU8E10&an2";
			case 2:
				return "RRU8E10&an4";
			case 3:
				return "RRU8E10&an8";
			default:
				return "null";
			}

		case 6:// RRU8F10
			switch (AnNum) {
			case 0:
				return "RRU8F10&an1";
			case 1:
				return "RRU8F10&an2";
			case 2:
				return "RRU8F10&an4";
			case 3:
				return "RRU8F10&an8";
			default:
				return "null";
			}

		case 7:// RRU8G10
			switch (AnNum) {
			case 0:
				return "RRU8G10&an1";
			case 1:
				return "RRU8G10&an2";
			case 2:
				return "RRU8G10&an4";
			case 3:
				return "RRU8G10&an8";
			default:
				return "null";
			}

		case 8:// RRU8H10
			switch (AnNum) {
			case 0:
				return "RRU8H10&an1";
			case 1:
				return "RRU8H10&an2";
			case 2:
				return "RRU8H10&an4";
			case 3:
				return "RRU8H10&an8";
			default:
				return "null";
			}

		case 9:// RRU8I10
			switch (AnNum) {
			case 0:
				return "RRU8I10&an1";
			case 1:
				return "RRU8I10&an2";
			case 2:
				return "RRU8I10&an4";
			case 3:
				return "RRU8I10&an8";
			default:
				return "null";
			}

		case 10:// RRU8J10
			switch (AnNum) {
			case 0:
				return "RRU8J10&an1";
			case 1:
				return "RRU8J10&an2";
			case 2:
				return "RRU8J10&an4";
			case 3:
				return "RRU8J10&an8";
			default:
				return "null";
			}

		case 11:// RRU8K10
			switch (AnNum) {
			case 0:
				return "RRU8K10&an1";
			case 1:
				return "RRU8K10&an2";
			case 2:
				return "RRU8K10&an4";
			case 3:
				return "RRU8K10&an8";
			default:
				return "null";
			}

		case 12:// RRU4B10_FF
			switch (AnNum) {
			case 0:
				return "RRU4B10_FF&an1";
			case 1:
				return "RRU4B10_FF&an2";
			case 2:
				return "RRU4B10_FF&an4";
			case 3:
				return "RRU4B10_FF&an8";
			default:
				return "null";
			}

		case 13:// RRU4B30_CF
			switch (AnNum) {
			case 0:
				return "RRU4B30_CF&an1";
			case 1:
				return "RRU4B30_CF&an2";
			case 2:
				return "RRU4B30_CF&an4";
			case 3:
				return "RRU4B30_CF&an8";
			default:
				return "null";
			}

		case 14:// RRU4D30_CF
			switch (AnNum) {
			case 0:
				return "RRU4D30_CF&an1";
			case 1:
				return "RRU4D30_CF&an2";
			case 2:
				return "RRU4D30_CF&an4";
			case 3:
				return "RRU4D30_CF&an8";
			default:
				return "null";
			}

		case 15:// RRU4E10_FF
			switch (AnNum) {
			case 0:
				return "RRU4E10_FF&an1";
			case 1:
				return "RRU4E10_FF&an2";
			case 2:
				return "RRU4E10_FF&an4";
			case 3:
				return "RRU4E10_FF&an8";
			default:
				return "null";
			}

		case 16:// RRU4E30_CF
			switch (AnNum) {
			case 0:
				return "RRU4E30_CF&an1";
			case 1:
				return "RRU4E30_CF&an2";
			case 2:
				return "RRU4E30_CF&an4";
			case 3:
				return "RRU4E30_CF&an8";
			default:
				return "null";
			}

		case 17:// RRU2J30_CC
			switch (AnNum) {
			case 0:
				return "RRU2J30_CC&an1";
			case 1:
				return "RRU2J30_CC&an2";
			case 2:
				return "RRU2J30_CC&an4";
			case 3:
				return "RRU2J30_CC&an8";
			default:
				return "null";
			}

		case 18:// RRU4K30_CF
			switch (AnNum) {
			case 0:
				return "RRU4K30_CF&an1";
			case 1:
				return "RRU4K30_CF&an2";
			case 2:
				return "RRU4K30_CF&an4";
			case 3:
				return "RRU4K30_CF&an8";
			default:
				return "null";
				
		case 19:// RRU4J30_CF
			switch (AnNum) {
			case 0:
				return "RRU4J30_CF&an1";
			case 1:
				return "RRU4J30_CF&an2";
			case 2:
				return "RRU4J30_CF&an4";
			case 3:
				return "RRU4J30_CF&an8";
			default:
				return "null";				
			}
		case 23:// RRU4E30a_CF
			switch (AnNum) {
			case 0:
				return "RRU4E30a_CF&an1";
			case 1:
				return "RRU4E30a_CF&an2";
			case 2:
				return "RRU4E30a_CF&an4";
			case 3:
				return "RRU4E30a_CF&an8";
			default:
				return "null";	
			}
		case 24:// RRU4L30_CF
			switch (AnNum) {
			case 0:
				return "RRU4L30_CF&an1";
			case 1:
				return "RRU4L30_CF&an2";
			case 2:
				return "RRU4L30_CF&an4";
			case 3:
				return "RRU4L30_CF&an8";
			default:
				return "null";	
			}
		}
		default:
			return "null";
		}
	}

	// 参数检查，返0为Ok，否则参数检查失败
	public static void checkEnbPara(int languageInd, DataInputInfo dataInfo)
			throws Exception {
		int Ret1 = checkTAC(dataInfo.getTAC());
		if (Ret1 == 0) {
			if (1 == languageInd)
				throw new Exception("TAC信息错误");
			else
				throw new Exception("TAC Info Error");
		}

		int Ret2 = CheckMCC(dataInfo.getMCC());
		if (Ret2 == 0) {
			if (1 == languageInd)
				throw new Exception("MCC信息错误");
			else
				throw new Exception("MCC Info Error");
		}

		int Ret3 = CheckAu8Mnc(dataInfo.getMNC());
		if (Ret3 == 0) {
			if (1 == languageInd)
				throw new Exception("MNC信息错误");
			else
				throw new Exception("MNC Info Error");
		}

		int Ret4 = checku8FreqBandInd(dataInfo.getFreqBandInd());
		if (Ret4 == 0) {
			if (1 == languageInd)
				throw new Exception("频段信息错误");
			else
				throw new Exception("Freq Band Info Error");
		}

		int Ret5 = checku32CenterFreq(dataInfo.getSysBandWidth(),
				dataInfo.getFreqBandInd(), dataInfo.getCenterFreq());
		if (Ret5 == 0) {
			if (1 == languageInd)
				throw new Exception("中心频点信息错误");
			else
				throw new Exception("Center Freq Info Error");
		}

		int Ret6 = checkSysBandWidth(dataInfo.getSysBandWidth());
		if (Ret6 == 0) {
			if (1 == languageInd)
				throw new Exception("系统带宽信息错误");
			else
				throw new Exception("System Bandwidth Info Error");
		}

		int Ret7 = checkPhyCellId(dataInfo.getPhyCellId());
		if (Ret7 == 0) {
			if (1 == languageInd)
				throw new Exception("物理小区信息错误");
			else
				throw new Exception("physic CellId Info Error");
		}

		int Ret8 = checkTopoNO(dataInfo.getTopoNO());
		if (Ret8 == 0) {
			if (1 == languageInd)
				throw new Exception("小区拓扑号信息错误");
			else
				throw new Exception("Cell TopoNO Info Error");
		}

		// int Ret9 = checkStatus(dataInfo.getStatus());
		// if (Ret9 == 0)
		// throw new Exception("999");

		int Ret10 = checkManualOP(dataInfo.getManualOP());
		if (Ret10 == 0) {
			if (1 == languageInd)
				throw new Exception("Cell TopoNO Info Error");
			else
				throw new Exception("小区拓扑号信息错误");
		}

		int Ret11 = checkRootSeqIndex(dataInfo.getRootSeqIndex());
		if (Ret11 == 0) {
			if (1 == languageInd)
				throw new Exception("Root SeqIndex Info Error");
			else
				throw new Exception("逻辑根序列信息错误");
		}

		// return 0;
	}

	// 跟踪区码,0-65535
	public static int checkTAC(int u16TAC) {
		if ((u16TAC < 0) || (u16TAC > 65535))
			return 0;
		else
			return 1;
	}

	// 检查MCC
	public static int CheckMCC(int[] au8MCC) {

		for (int i = 0; i < 3; i++) {
			if (((au8MCC[i] >= 0) & (au8MCC[i] <= 9)) || (au8MCC[i] == 0xff))
				continue;
			else
				return 0;
		}
		return 1;
	}

	// 检查MNC
	public static int CheckAu8Mnc(int[] au8MNC) {

		for (int i = 0; i < 3; i++) {
			if (((au8MNC[i] >= 0) & (au8MNC[i] <= 9)) || (au8MNC[i] == 0xff))
				continue;
			else
				return 0;
		}
		return 1;
	}

	// 频段指示,33-40,61-63,53,58,45,59
	public static int checku8FreqBandInd(int u8FreqBandInd) {
		if (((u8FreqBandInd >= 33) & (u8FreqBandInd <= 40))
				|| ((u8FreqBandInd >= 61) & (u8FreqBandInd <= 63))
				|| (u8FreqBandInd == 53) || (u8FreqBandInd == 58) || 
				(u8FreqBandInd == 45) || (u8FreqBandInd == 59))
			return 1;
		else
			return 0;
	}
		
	// 中心频点,(33:1900-1920, 34:2010-2025, 35:1850-1910, 36:1930-1990,
	// 37:1910-1930, 38:2570-2620, 39:1880-1920,
	// 40:2300-2400MHZ，61:1447-1467，62:1785-1805，63:606-678,53:778-798,58:380-430
	public static int checku32CenterFreq(int u8SysBandWidth, int u8FreqBandInd,
			int u32CenterFreq) {
		int u32LowCenterFreq = 0;
		int u32HighCenterFreq = 0;
		u32LowCenterFreq = u32CenterFreq - ((u8SysBandWidth * 10) / 2);
		u32HighCenterFreq = u32CenterFreq + ((u8SysBandWidth * 10) / 2);

		switch (u8FreqBandInd) {
		case 33:
			if ((u32LowCenterFreq >= 36000) & (u32HighCenterFreq <= 36200))
				return 1;
			else
				return 0;
		case 34:
			if ((u32LowCenterFreq >= 36200) & (u32HighCenterFreq <= 36350))
				return 1;
			else
				return 0;
		case 35:
			if ((u32LowCenterFreq >= 36350) & (u32HighCenterFreq <= 36950))
				return 1;
			else
				return 0;
		case 36:
			if ((u32LowCenterFreq >= 36950) & (u32HighCenterFreq < 37550))
				return 1;
			else
				return 0;
		case 37:
			if ((u32LowCenterFreq > 37550) & (u32HighCenterFreq < 37750))
				return 1;
			else
				return 0;
		case 38:
			if ((u32LowCenterFreq > 37750) & (u32HighCenterFreq < 38250))
				return 1;
			else
				return 0;
		case 39:
			if ((u32LowCenterFreq >= 38250) & (u32HighCenterFreq <= 38650))
				return 1;
			else
				return 0;
		case 40:
			if ((u32LowCenterFreq > 38650) & (u32HighCenterFreq <= 39650))
				return 1;
			else
				return 0;
		case 45:
			if ((u32LowCenterFreq > 46590) & (u32HighCenterFreq <= 46789))
				return 1;
			else
				return 0;
		case 59:
			if ((u32LowCenterFreq > 54200) & (u32HighCenterFreq <= 54399))
				return 1;
			else
				return 0;			
		case 61:
			if ((u32LowCenterFreq > 65000) & (u32HighCenterFreq < 65200))
				return 1;
			else
				return 0;
		case 62:
			if ((u32LowCenterFreq > 65200) & (u32HighCenterFreq < 65400))
				return 1;
			else
				return 0;
		case 63:
			if ((u32LowCenterFreq > 58260) & (u32HighCenterFreq < 59780))
				return 1;
			else
				return 0;
		case 53:
			if ((u32LowCenterFreq >= 64000) & (u32HighCenterFreq <= 64200))
				return 1;
			else
				return 0;
		case 58:
			if ((u32LowCenterFreq > 58200) & (u32HighCenterFreq < 58700))
				return 1;
			else
				return 0;
		default:
			return 0;
		}
	}

	public static int checkSysBandWidth(int u8SysBandWidth) {
		// 当前不支持1.4M
		if ((u8SysBandWidth >= 1) || (u8SysBandWidth <= 5))
			return 1;
		else
			return 0;
	}

	public static int checkPhyCellId(int u16PhyCellId) {
		if ((u16PhyCellId >= 0) || (u16PhyCellId <= 503))
			return 1;
		else
			return 0;
	}

	public static int checkTopoNO(int u8TopoNO) {
		if ((u8TopoNO == 0) || (u8TopoNO == 1) || (u8TopoNO == 2))
			return 1;
		else
			return 0;
	}

	public static int checkStatus(int u32Status) {
		if ((u32Status == 0) || (u32Status == 1))
			return 1;
		else
			return 0;
	}

	public static int checkManualOP(int u8ManualOP) {
		if ((u8ManualOP == 0) || (u8ManualOP == 1))
			return 1;
		else
			return 0;
	}

	public static int checkRootSeqIndex(int u16RootSeqIndex) {
		if ((u16RootSeqIndex >= 0) || (u16RootSeqIndex <= 837))
			return 1;
		else
			return 0;
	}
}
