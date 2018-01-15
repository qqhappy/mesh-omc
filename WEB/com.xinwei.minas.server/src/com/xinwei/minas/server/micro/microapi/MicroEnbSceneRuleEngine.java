/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-30	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.micro.microapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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



/**
 * 
 * eNodeB������������
 * 
 * @author zhoujie
 * 
 */
// public interface EnbSceneRuleEngine {
public class MicroEnbSceneRuleEngine {
	public static void main(String[] args) throws Exception {

		List<MicroEnbToOamDispItem> mm = new ArrayList<MicroEnbToOamDispItem>();
		MicroConfigIntf aa = new MicroConfigIntf();
		MicroDataInputInfo dataInfo = new MicroDataInputInfo();
		List<MicroEnbIputItem> inputList = new ArrayList<MicroEnbIputItem>();
		MicroDataInputInfo bb = new MicroDataInputInfo();
		int languageInd = 1;// ����ѡ�1��ʾ���ģ�2��ʾӢ��
		int retValue = 0;

		// ��������ѡ���ȡ������ʾ��
		mm = aa.GetDispConfig(languageInd);

		System.out.println("===========���ܽ�����������ʾ.begin===========");
		for (int i = 0; i < mm.size(); i++) {
			MicroEnbToOamDispItem map = mm.get(i);
			int Id = map.getId();
			String Name = map.getName();
			System.out.println("-Id:" + Id + "\t\t -" + "Name:" + Name);
			map.getOptionDataInfo();
		}
		System.out.println("===========���ܽ�����������ʾ.end===========");

		int[] au8mcc = { 4, 6, 0 };
		int[] au8mnc = { 0, 0, 0xff };
		int u8FreqBandInd = 63;
		int u8RruTypeId = 19;
		int u32CenterFreq = 59160;
		
		dataInfo.setRruType(u8RruTypeId);
		dataInfo.setMCC(au8mcc);
		dataInfo.setMNC(au8mnc);
		dataInfo.seteNBName("L3Enb");
		dataInfo.setCellLable("Cell1");
		dataInfo.setFreqBandInd(u8FreqBandInd);
		dataInfo.setCenterFreq(u32CenterFreq);
		dataInfo.setSysBandWidth(1);
		dataInfo.setPhyCellId(160);
		dataInfo.setTopoNO(0);
		// dataInfo.setStatus(1);
		dataInfo.setManualOP(0);
		dataInfo.setRootSeqIndex(0);

		MicroEnbSceneOutput cc = new MicroEnbSceneOutput();

		MicroEnbIputItem enbDisp = new MicroEnbIputItem(1, 2);
		inputList.add(enbDisp);
		MicroEnbIputItem enbDisp1 = new MicroEnbIputItem(2, u8FreqBandInd);
		inputList.add(enbDisp1);
		MicroEnbIputItem enbDisp2 = new MicroEnbIputItem(3, 2);
		inputList.add(enbDisp2);
		MicroEnbIputItem enbDisp3 = new MicroEnbIputItem(4, 2);
		inputList.add(enbDisp3);
		MicroEnbIputItem enbDisp4 = new MicroEnbIputItem(5, u8RruTypeId);
		inputList.add(enbDisp4);
		MicroEnbIputItem enbDisp5 = new MicroEnbIputItem(6, 0);
		inputList.add(enbDisp5);

		System.out.println("===========����������.begin===========");
		try {
			checkEnbPara(languageInd, dataInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("===========����������.end===========");

		cc = generate(languageInd,inputList);
	}

	// private static void SearhData(EnbSceneInput input) {
	public static MicroEnbSceneOutput generate(int languageInd,List<MicroEnbIputItem> inputList) throws Exception {
		int DataNum = 0;
		MicroOutDataInfo ff[] = new MicroOutDataInfo[50];
		MicroEnbSceneOutput cc = new MicroEnbSceneOutput();
		int ERSnorValue = 0;
        int CellSpeRefSigPwr = 17;
		int CRS_EPRE_max = 0;
		int Cfi = 3;
		
		for (int n = 0; n < ff.length; n++)
			ff[n] = new MicroOutDataInfo();

		MicroOamInputDate input = getConfigDate(inputList);

		String Scene = getSceneString(input.Scene);// ��ȡ����
		String FreqBand = getFreqBandString(input.FreqBand);// ��ȡƵ��
		String SysBandWidth = getBandWidthString(input.SysBandWidth);// ��ȡ����
		String SfCfg = getSfCfgString(input.SfCfg);// ��ȡ��֡���
		String RruType = getRruTypeString(input.RruType, input.AnNum);// ��ȡRRU����

		if ((null == Scene) || (null == FreqBand) || (null == Scene)
				|| (null == SfCfg) || (null == RruType)) {
			if (1 == languageInd)
				throw new Exception("��������");
			else
				throw new Exception("Input Info Error");			
		}
		
		if ("RRU2J30_CC&an4" == RruType)
		{	
			if (1 == languageInd)
				throw new Exception("RRU2J30_CCֻ֧��2��2��");
			else
				throw new Exception("RRU2J30_CC Only support 2R2T");
		}
		
		//���Ƴ�Զ���ǳ�����ѡ�����2������Զ���Ǻ����2����
		if (("��Զ����" == Scene) && ("���2" == SfCfg))
		{
			if (1 == languageInd)
				throw new Exception("��Զ���ǳ����²���ѡ�����2");
			else
				throw new Exception("The Scene Not Support SfCfg 2");
		}
		
		if ("���2" == SfCfg)
		{
			Cfi = 2;
		}
		//�ϡ�������������������ѡ5M
		if (("��������" == Scene) || ("��������" == Scene))
		{
			Cfi = 1;
		}
		
		// ����һ��DocumentBuilderFactory�Ķ���
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		Map<String, Map<String, String>> tableMap = cc.getTableMap();
		
		try {
			// ����DocumentBuilder����
			DocumentBuilder db = dbf.newDocumentBuilder();
			// ͨ��DocumentBuilder�����parser��������DataConfigXml.xml�ļ�����ǰ��Ŀ��
			Document document = db
					.parse("plugins/enb/provision/DataConfigXml.xml");
			// ��ȡ���б�ڵ�ļ���
			NodeList tableList = document.getElementsByTagName("table");
			// ͨ��nodelist��getLength()�������Ի�ȡtableList�ĳ���
			System.out.println("һ����" + tableList.getLength() + "�ű�");

			if (0 == input.AnNum)// �������������Ϊ1
			{
				// ������T_CEL_PARA���ֶ�����u8UlAntNum
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

				// ������T_CEL_PARA���ֶ�����u8UlAntUsdNum
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

				// ������T_CEL_PARA���ֶ�����u8UeTransMode
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

				// ������T_CEL_PARA���ֶ�����u8DlAntPortNum
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
				
				// ������T_CEL_PARA���ֶ�����u8DlAntNum
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

				// ������T_CEL_PARA���ֶ�����u8DlAntUsdNum
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
					    ("RRU4L30_CF&an1" == RruType))
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
					
					// ����ʹ����������
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
					
					// ����ʹ����������
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
			else if (1 == input.AnNum)// //2����
			{
				if (("RRU4B30_CF&an2" == RruType) ||
				    ("RRU4D30_CF&an2" == RruType) ||
				    ("RRU4E30_CF&an2" == RruType) ||
				    ("RRU4E30_CF&an2" == RruType) ||
				    ("RRU2J30_CC&an2" == RruType) ||
				    ("RRU4K30_CF&an2" == RruType) ||
				    ("RRU4J30_CF&an2" == RruType) ||
				    ("RRU4L30_CF&an2" == RruType))
				{
					// ����ʹ����������
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
					
					// ����ʹ����������
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
			else if(2 == input.AnNum)//4����
			{
				// ������T_CEL_PARA���ֶ�����u8UlAntNum
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

				// ������T_CEL_PARA���ֶ�����u8UlAntUsdNum
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

				// ����ʹ����������
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
					    ("RRU4L30_CF&an4" == RruType))
				{
					// ����ʹ����������
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
			
			ERSnorValue = getERSnorValue(input.SysBandWidth);//����ȡERS_nor
			
			// ����ÿһ��table�ڵ�
			for (int i = 0; i < tableList.getLength(); i++) {
				System.out.println("=================���濪ʼ������" + (i + 1)
						+ "�ű������=================");

				// ͨ�� item(i)���� ��ȡһ��table�ڵ㣬nodelist������ֵ��0��ʼ
				Node table = tableList.item(i);

				// ��ȡtable�ڵ���������Լ���
				NamedNodeMap attrs = table.getAttributes();
				System.out.println("�� " + (i + 1) + "�ű���" + attrs.getLength()
						+ "������");

				// ÿ����ֻ��һ������
				Node attr = attrs.item(0);
				// ��ȡ������
				System.out.print("��������" + attr.getNodeName());

				// ��ȡ����ֵ
				System.out.println("--����ֵ:" + attr.getNodeValue());

				// ����table�ڵ���ӽڵ�
				NodeList childNodes = table.getChildNodes();

				// ����childNodes��ȡÿ���ڵ�Ľڵ����ͽڵ�ֵ
				System.out.println("��" + (i + 1) + "�ű���"
						+ childNodes.getLength() + "��");

				for (int k = 0; k < childNodes.getLength(); k++) {
					// ���ֳ�text���͵�node�Լ�element���͵�node
					if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
						if (attr.getNodeValue().equals("RRU�ͺ�")) {
							if (RruType.equals(childNodes.item(k)
									.getAttributes().item(0).getNodeValue())) {
								// ������T_CEL_PARA���ֶ�����u16CellTransPwr
								ff[DataNum].tableName = "T_CEL_DLPC";
								ff[DataNum].fieldName = "u16CellTransPwr";
								ff[DataNum].fieldValue = childNodes.item(k)
										.getAttributes()
										.getNamedItem("ʵ������书��")
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
								System.out.println("--u16CellTransPwr��"
										+ childNodes.item(k).getAttributes()
												.getNamedItem("ʵ������书��")
												.getNodeValue());
								
								//����T_CEL_DLPC���u16CellSpeRefSigPwr�����õ����ֵ
								//�������߶˿���Ϊ1����2 ,u16CellTransPwr
					            if (0 == input.AnNum)
					            {
					            	//CRS_EPRE_max = T_CEL_DLPC.u16CellTransPwr - 10*log10(u8DlAntPortNum) + ERS_nor 
					            	CRS_EPRE_max = Integer.valueOf(childNodes.item(k).getAttributes().getNamedItem("ʵ������书��").getNodeValue()) + (ERSnorValue*10);
					            }
					            else
					            {
					               	CRS_EPRE_max = Integer.valueOf(childNodes.item(k).getAttributes().getNamedItem("ʵ������书��").getNodeValue()) - 30 + (ERSnorValue*10);
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
								
								System.out.println("--CRS_EPRE_max��"+ CRS_EPRE_max);
								System.out.println("--CellSpeRefSigPwr��"+ CellSpeRefSigPwr);
								break;
							}
						}

						if (attr.getNodeValue().equals("FreqBand")) {
							if (childNodes.item(k).getAttributes()
									.getNamedItem(FreqBand).getNodeValue() != "") {
								ff[DataNum].tableName = childNodes.item(k)
										.getAttributes().getNamedItem("����")
										.getNodeValue();
								ff[DataNum].fieldName = childNodes.item(k)
										.getAttributes().getNamedItem("�ֶ���")
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

								System.out.println("--������"
										+ childNodes.item(k).getAttributes()
												.getNamedItem("����")
												.getNodeValue());
								System.out.println("--�ֶ�����"
										+ childNodes.item(k).getAttributes()
												.getNamedItem("�ֶ���")
												.getNodeValue());
								System.out.println("--value��"
										+ childNodes.item(k).getAttributes()
												.getNamedItem(FreqBand)
												.getNodeValue());
							}
						}

						if (attr.getNodeValue().equals("SFCfg")) {
							if (childNodes.item(k).getAttributes()
									.getNamedItem(SfCfg).getNodeValue() != "") {
								ff[DataNum].tableName = childNodes.item(k)
										.getAttributes().getNamedItem("����")
										.getNodeValue();
								ff[DataNum].fieldName = childNodes.item(k)
										.getAttributes().getNamedItem("�ֶ���")
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

								System.out.println("--������"
										+ childNodes.item(k).getAttributes()
												.getNamedItem("����")
												.getNodeValue());
								System.out.println("--�ֶ�����"
										+ childNodes.item(k).getAttributes()
												.getNamedItem("�ֶ���")
												.getNodeValue());
								System.out.println("--value��"
										+ childNodes.item(k).getAttributes()
												.getNamedItem(SfCfg)
												.getNodeValue());
							}
						}

						if (attr.getNodeValue().equals("BandWidth")) {
							if (childNodes.item(k).getAttributes()
									.getNamedItem(SysBandWidth).getNodeValue() != "") {
								ff[DataNum].tableName = childNodes.item(k)
										.getAttributes().getNamedItem("����")
										.getNodeValue();
								ff[DataNum].fieldName = childNodes.item(k)
										.getAttributes().getNamedItem("�ֶ���")
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

								System.out.println("--������"
										+ childNodes.item(k).getAttributes()
												.getNamedItem("����")
												.getNodeValue());
								System.out.println("--�ֶ�����"
										+ childNodes.item(k).getAttributes()
												.getNamedItem("�ֶ���")
												.getNodeValue());
								System.out.println("--value��"
										+ childNodes.item(k).getAttributes()
												.getNamedItem(SysBandWidth)
												.getNodeValue());
							}
						}

						if ((attr.getNodeValue().equals("Ӧ�ó���"))
								& (Scene != "Ĭ������")) {
							if (childNodes.item(k).getAttributes()
									.getNamedItem(Scene).getNodeValue() != "") {
								ff[DataNum].tableName = childNodes.item(k)
										.getAttributes().getNamedItem("����")
										.getNodeValue();
								ff[DataNum].fieldName = childNodes.item(k)
										.getAttributes().getNamedItem("�ֶ���")
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

								System.out.println("--������"
										+ childNodes.item(k).getAttributes()
												.getNamedItem("����")
												.getNodeValue());
								System.out.println("--�ֶ�����"
										+ childNodes.item(k).getAttributes()
												.getNamedItem("�ֶ���")
												.getNodeValue());
								System.out.println("--value��"
										+ childNodes.item(k).getAttributes()
												.getNamedItem(Scene)
												.getNodeValue());
							}
						}
					}
				}
				System.out.println("======================����������" + (i + 1)
						+ "���������=================");
			}
			
			// ������T_CEL_PARA���ֶ�����u8UlAntNum
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

			System.out.println("===================���������Ϣ===================");
			System.out
					.println("--����                                   --�ֶ���                                                            --value");
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

	// ���������ȡ��������
	public static MicroOamInputDate getConfigDate(List<MicroEnbIputItem> inputList) {
		MicroOamInputDate input = new MicroOamInputDate();

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

	// ��ȡ������Ϣ
	private static String getSceneString(int SceneId) {
		switch (SceneId) {
		case 0:
			return "Ĭ������";
		case 1:
			return "��������";
		case 2:
			return "��������";
		case 3:
			return "��Զ����";
		case 4:
			return "��������";
		case 5:
			return "��������";
		default:
			return "null";
		}
	}

	// ��ȡƵ����Ϣ
	private static String getFreqBandString(int FreqBandId) {
		switch (FreqBandId) {
		case 38:
			return "Ƶ��38";
		case 39:
			return "Ƶ��39";
		case 40:
			return "Ƶ��40";
		case 53:
			return "Ƶ��53";
		case 58:
			return "Ƶ��58";
		case 61:
			return "Ƶ��61";
		case 62:
			return "Ƶ��62";
		case 63:
			return "Ƶ��63";
		default:
			return "null";
		}
	}

	// ��ȡ������Ϣ
	private static String getBandWidthString(int BandWidthId) {
		switch (BandWidthId) {
		case 2:
			return "����5M";
		case 3:
			return "����10M";
		case 4:
			return "����15M";
		case 5:
			return "����20M";
		default:
			return "null";
		}
	}

	
	// ��ȡ������Ϣ��ȡERS_nor,��ʱC�����PA��PB�ֱ�ȡĬ��ֵ2��1
	private static int getERSnorValue(int BandWidthId) {
		switch (BandWidthId) {
		case 2: //����5M
			return -22;
		case 3: //����10M
			return -25;
		case 4: //����15M
			return -27;
		case 5: //����20M
			return -28;
		default:
			return 0;
		}
	}
	
	// ��ȡ��֡�����Ϣ
	private static String getSfCfgString(int SfCfgId) {
		switch (SfCfgId) {
		case 0:
			return "���0";
		case 1:
			return "���1";
		case 2:
			return "���2";
		default:
			return "null";
		}
	}

	// ��ȡRRU������Ϣ
	private static String getRruTypeString(int RruTypeId, int AnNum) {
		switch (RruTypeId) {
		case 0:// RRU8A10
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

		case 1:// RRU8B10
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

		case 2:// RRU8C10
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

		case 3:// RRU8D10
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

		case 4:// RRU8E10a
			switch (AnNum) {
			case 0:
				return "RRU8E10a&an1";
			case 1:
				return "RRU8E10a&an2";
			case 2:
				return "RRU8E10a&an4";
			case 3:
				return "RRU8E10a&an8";
			default:
				return "null";
			}

		case 5:// RRU8F10
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

		case 6:// RRU8G10
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

		case 7:// RRU8H10
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

		case 8:// RRU8I10
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

		case 9:// RRU8J10
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

		case 10:// RRU8K10
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

		case 11:// RRU4B10_FF
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

		case 12:// RRU4B30_CF
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

		case 13:// RRU4D30_CF
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

		case 14:// RRU4E10_FF
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

		case 15:// RRU4E30_CF
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

		case 16:// RRU2J30_CC
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

		case 17:// RRU4K30_CF
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
			}
			
		case 18:// RRU4J30_CF
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
			
		case 19:// RRU4L30_CF
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

		case 20:// RRU8E10
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
			
		case 21:// RRU8E10b
			switch (AnNum) {
			case 0:
				return "RRU8E10b&an1";
			case 1:
				return "RRU8E10b&an2";
			case 2:
				return "RRU8E10b&an4";
			case 3:
				return "RRU8E10b&an8";
			default:
				return "null";				
			}	
			
		default:
			return "null";
		}	
	}

	// ������飬��0ΪOk������������ʧ��
	public static void checkEnbPara(int languageInd, MicroDataInputInfo dataInfo)
			throws Exception {
		int Ret1 = checkTAC(dataInfo.getTAC());
		if (Ret1 == 0) {
			if (1 == languageInd)
				throw new Exception("TAC��Ϣ����");
			else
				throw new Exception("TAC Info Error");
		}

		int Ret2 = CheckMCC(dataInfo.getMCC());
		if (Ret2 == 0) {
			if (1 == languageInd)
				throw new Exception("MCC��Ϣ����");
			else
				throw new Exception("MCC Info Error");
		}

		int Ret3 = CheckAu8Mnc(dataInfo.getMNC());
		if (Ret3 == 0) {
			if (1 == languageInd)
				throw new Exception("MNC��Ϣ����");
			else
				throw new Exception("MNC Info Error");
		}

		int Ret4 = checku8FreqBandInd(dataInfo.getFreqBandInd());
		if (Ret4 == 0) {
			if (1 == languageInd)
				throw new Exception("Ƶ����Ϣ����");
			else
				throw new Exception("Freq Band Info Error");
		}

		int Ret5 = checku32CenterFreq(dataInfo.getSysBandWidth(),
				dataInfo.getFreqBandInd(), dataInfo.getCenterFreq());
		if (Ret5 == 0) {
			if (1 == languageInd)
				throw new Exception("����Ƶ����Ϣ����");
			else
				throw new Exception("Center Freq Info Error");
		}
	
		int Ret6 = checkSysBandWidth(dataInfo.getSysBandWidth());
		if (Ret6 == 0) {
			if (1 == languageInd)
				throw new Exception("ϵͳ������Ϣ����");
			else
				throw new Exception("System Bandwidth Info Error");
		}

		int Ret7 = checkPhyCellId(dataInfo.getPhyCellId());
		if (Ret7 == 0) {
			if (1 == languageInd)
				throw new Exception("����С����Ϣ����");
			else
				throw new Exception("physic CellId Info Error");
		}

		int Ret8 = checkTopoNO(dataInfo.getTopoNO());
		if (Ret8 == 0) {
			if (1 == languageInd)
				throw new Exception("С�����˺���Ϣ����");
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
				throw new Exception("С�����˺���Ϣ����");
		}

		int Ret11 = checkRootSeqIndex(dataInfo.getRootSeqIndex());
		if (Ret11 == 0) {
			if (1 == languageInd)
				throw new Exception("Root SeqIndex Info Error");
			else
				throw new Exception("�߼���������Ϣ����");
		}

		/*int Ret12 = checkRruTypeCenterFreq(dataInfo.geteRruType(), dataInfo.getCenterFreq());
		if (Ret12 == 0) {
			if (1 == languageInd)
				throw new Exception("RRU��Ӧ������Ƶ����Ϣ����");
			else
				throw new Exception("RruType And Center Freq Info Error");
		}*/
		// return 0;
	}

	// ��������,0-65535
	public static int checkTAC(int u16TAC) {
		if ((u16TAC < 0) || (u16TAC > 65535))
			return 0;
		else
			return 1;
	}

	// ���MCC
	public static int CheckMCC(int[] au8MCC) {

		for (int i = 0; i < 3; i++) {
			if (((au8MCC[i] >= 0) & (au8MCC[i] <= 9)) || (au8MCC[i] == 0xff))
				continue;
			else
				return 0;
		}
		return 1;
	}

	// ���MNC
	public static int CheckAu8Mnc(int[] au8MNC) {

		for (int i = 0; i < 3; i++) {
			if (((au8MNC[i] >= 0) & (au8MNC[i] <= 9)) || (au8MNC[i] == 0xff))
				continue;
			else
				return 0;
		}
		return 1;
	}

	// Ƶ��ָʾ,33-40,61-63,53,58
	public static int checku8FreqBandInd(int u8FreqBandInd) {
		if (((u8FreqBandInd >= 33) & (u8FreqBandInd <= 40))
				|| ((u8FreqBandInd >= 61) & (u8FreqBandInd <= 63))
				|| (u8FreqBandInd == 53) || (u8FreqBandInd == 58))
			return 1;
		else
			return 0;
	}
		
	// ����Ƶ��,(33:1900-1920, 34:2010-2025, 35:1850-1910, 36:1930-1990,
	// 37:1910-1930, 38:2570-2620, 39:1880-1920,
	// 40:2300-2400MHZ��61:1447-1467��62:1785-1805��63:606-678,53:778-798,58:380-430
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
			if ((u32LowCenterFreq >= 36950) & (u32HighCenterFreq <= 37550))
				return 1;
			else
				return 0;
		case 37:
			if ((u32LowCenterFreq >= 37550) & (u32HighCenterFreq <= 37750))
				return 1;
			else
				return 0;
		case 38:
			if ((u32LowCenterFreq >= 37750) & (u32HighCenterFreq <= 38250))
				return 1;
			else
				return 0;
		case 39:
			if ((u32LowCenterFreq >= 38250) & (u32HighCenterFreq <= 38650))
				return 1;
			else
				return 0;
		case 40:
			if ((u32LowCenterFreq >= 38650) & (u32HighCenterFreq <= 39650))
				return 1;
			else
				return 0;
		case 61:
			if ((u32LowCenterFreq >= 65000) & (u32HighCenterFreq <= 65200))
				return 1;
			else
				return 0;
		case 62:
			if ((u32LowCenterFreq >= 65200) & (u32HighCenterFreq <= 65400))
				return 1;
			else
				return 0;
		case 63:
			if ((u32LowCenterFreq >= 59060) & (u32HighCenterFreq <= 59780))
				return 1;
			else
				return 0;
		case 53:
			if ((u32LowCenterFreq >= 64000) & (u32HighCenterFreq <= 64200))
				return 1;
			else
				return 0;
		case 58:
			if ((u32LowCenterFreq >= 58200) & (u32HighCenterFreq <= 58700))
				return 1;
			else
				return 0;
		default:
			return 0;
		}
	}

	//���RRU���Ͷ�Ӧ������Ƶ���Ƿ�Ϸ�
	private static int checkRruTypeCenterFreq(int RruTypeId, int u32CenterFreq) {
		switch (RruTypeId) {
		case 0:// RRU8A10
				return 0;//��֧��

		case 1:// RRU8B10
		case 11:// RRU4B10_FF
		case 12:// RRU4B30_CF
			if ((u32CenterFreq >= 58400) & (u32CenterFreq <= 58700))
				return 1;
			else
				return 0;			

		case 2:// RRU8C10
			return 0;//��֧��
			
		case 3:// RRU8D10
		case 13:// RRU4D30_CF
			if ((u32CenterFreq >= 65000) & (u32CenterFreq <= 65200))
				return 1;
			else
				return 0;
			
		case 4:// RRU8E10a
			if ((u32CenterFreq >= 65230) & (u32CenterFreq <= 65380))
				return 1;
			else
				return 0;			

		case 5:// RRU8F10
		case 6:// RRU8G10
		case 7:// RRU8H10
		case 9:// RRU8J10
				return 0;//��֧��
				
		case 14:// RRU4E10_FF
		case 21:// RRU8E10b
			if ((u32CenterFreq >= 65200) & (u32CenterFreq <= 65400))
				return 1;
			else
				return 0;				
			
		case 8:// RRU8I10
		case 16:// RRU2J30_CC
		case 18:// RRU4J30_CF				
			if ((u32CenterFreq >= 59060) & (u32CenterFreq <= 59780))
				return 1;
			else
				return 0;		
			
		case 10:// RRU8K10
		case 17:// RRU4K30_CF
			if ((u32CenterFreq >= 64000) & (u32CenterFreq <= 64200))
				return 1;
			else
				return 0;
	
		case 15:// RRU4E30_CF
			if ((u32CenterFreq >= 65234.5) & (u32CenterFreq <= 65369.5))
				return 1;
			else
				return 0;			
			
		case 19:// RRU4L30_CF
			if ((u32CenterFreq >= 59060) & (u32CenterFreq <= 59260))
				return 1;
			else
				return 0;	
			
		case 20:// RRU8E10
			if ((u32CenterFreq >= 65200) & (u32CenterFreq <= 65375))
				return 1;
			else
				return 0;
			
		default:
			return 0;
		}
	}
	
	public static int checkSysBandWidth(int u8SysBandWidth) {
		// ��ǰ��֧��1.4M
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
