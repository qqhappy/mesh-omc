/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-7-16	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.enbapi;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.xinwei.minas.core.model.EnbProperty;
import com.xinwei.minas.core.model.EnbSceneDataShow;

/**
 * 
 * 
 * @author chenlong
 * 
 */

public class EnbSceneDataManager {
	
	private EnbSceneDataShow enbSceneDataShow = new EnbSceneDataShow();
	
	private static EnbSceneDataManager instance = new EnbSceneDataManager();
	
	private String tagConfigFileZh = "plugins/enb/provision/EnbToOamDispXmlCH.xml";
	
	private String tagConfigFileEn = "plugins/enb/provision/EnbToOamDispXmlEN.xml";
	
	public static EnbSceneDataManager getInstance() {
		return instance;
	}
	
	/**
	 * ��ʼ������ 0Ӣ�� 1����
	 * 
	 * @param language
	 */
	public void initData(int language) {
		try {
			SAXReader reader = new SAXReader();
			File file = null;
			if (1 == language) {
				file = new File(tagConfigFileZh);
			}
			else {
				file = new File(tagConfigFileEn);
			}
			Document document = reader.read(file);
			Element root = document.getRootElement();
			List<Element> firstElements = root.elements();
			for (Element firstElement : firstElements) {
				System.out.println(firstElement.attributeValue("name"));
				List<EnbProperty> properties = null;
				if ("Ӧ�ó���".equals(firstElement.attributeValue("name"))) {
					properties = enbSceneDataShow.getSceneShow();
				}
				else if ("Ƶ��".equals(firstElement.attributeValue("name"))) {
					properties = enbSceneDataShow.getFreqBandShow();
				}
				else if ("����".equals(firstElement.attributeValue("name"))) {
					properties = enbSceneDataShow.getBandwidthShow();
				}
				else if ("��֡���".equals(firstElement.attributeValue("name"))) {
					properties = enbSceneDataShow.getSfCfgShow();
				}
				else if ("RRU����".equals(firstElement.attributeValue("name"))) {
					properties = enbSceneDataShow.getRruTypeShow();
				}
				else if ("���������".equals(firstElement.attributeValue("name"))) {
					properties = enbSceneDataShow.getAnNumShow();
				}
				List<Element> secondElements = firstElement.elements();
				for (Element secondElement : secondElements) {
					System.out.println(secondElement.attributeValue("��ʾ��")
							+ "," + secondElement.attributeValue("��ѡ��ID��"));
					properties.add(new EnbProperty(secondElement
							.attributeValue("��ʾ��"), secondElement
							.attributeValue("��ѡ��ID��")));
				}
			}
		}
		catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
	
	public EnbSceneDataShow getEnbSceneDataShow() {
		return enbSceneDataShow;
	}
	
	public void setEnbSceneDataShow(EnbSceneDataShow enbSceneDataShow) {
		this.enbSceneDataShow = enbSceneDataShow;
	}
	
	public static void main(String[] args) {
		EnbSceneDataManager.getInstance().initData(1);
	}
}
