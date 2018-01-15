package com.xinwei.minas.server.enb.enbapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * �����ܳ��ֵĳ�����ʾ��
 * 
 * @author zhoujie
 * 
 */
public class ConfigIntf {
	public List<EnbToOamDispItem> Dispconfig = new ArrayList<EnbToOamDispItem>();
	
	public List<EnbToOamDispItem> GetDispConfig(int languageInd)
	{
		try {			
			//����һ��DocumentBuilderFactory�Ķ���
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	
			//����DocumentBuilder����
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = null;

			//ͨ��DocumentBuilder�����parser����������Ӣ��xml�ļ�����ǰ��Ŀ��
			if (languageInd == 1)
			{
				document = db.parse("plugins/enb/provision/EnbToOamDispXmlCH.xml");
			}
			else if(languageInd == 2)
			{
				document = db.parse("plugins/enb/provision/EnbToOamDispXmlEN.xml");
			}
			else
			{
			    return null;	
			}
			
			//��ȡ���б�ڵ�ļ���
			NodeList tableList = document.getElementsByTagName("table");
			
			//ͨ��nodelist��getLength()�������Ի�ȡtableList�ĳ���
			System.out.println("һ����" + tableList.getLength() + "�ű�");
			
			//����ÿһ��table�ڵ�
			for (int i = 0; i < tableList.getLength(); i++) {
				System.out.println("=================���濪ʼ������" + (i + 1) + "�ű������=================");
	
				//ͨ�� item(i)���� ��ȡһ��table�ڵ㣬nodelist������ֵ��0��ʼ
				Node table = tableList.item(i);
                
				//��ȡtable�ڵ���������Լ���
				NamedNodeMap attrs = table.getAttributes();
				System.out.println("�� " + (i + 1) + "�ű���" + attrs.getLength() + "������");
				
				//����table�ڵ���ӽڵ�
				NodeList childNodes = table.getChildNodes();
				
				//����childNodes��ȡÿ���ڵ�Ľڵ����ͽڵ�ֵ
				System.out.println("��" + (i+1) + "�ű���" + 
				childNodes.getLength() + "��");	
				
				//ÿ����ֻ��һ������
				Node attr = attrs.item(0);
				//��ȡ������
				System.out.print("��������" + attr.getNodeName());
				
				//��ȡ����ֵ
				System.out.println("--����ֵ:" + attr.getNodeValue());					

				EnbToOamDispItem enbDisp = new EnbToOamDispItem(i+1, attr.getNodeValue());
				Dispconfig.add(enbDisp);
				
				for (int k = 0; k < childNodes.getLength(); k++) {			
					String Name = childNodes.item(k).getAttributes().getNamedItem("��ʾ��").getNodeValue();
					int value = Integer.parseInt(childNodes.item(k).getAttributes().getNamedItem("��ѡ��ID��").getNodeValue());
					OptionDataInfo opdi = new OptionDataInfo(value, Name);
					enbDisp.AddOptData(opdi);
				}	
		    }			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}  catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Dispconfig;
	}

}
