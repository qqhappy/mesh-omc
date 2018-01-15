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
 * 给网管呈现的场景显示项
 * 
 * @author zhoujie
 * 
 */
public class ConfigIntf {
	public List<EnbToOamDispItem> Dispconfig = new ArrayList<EnbToOamDispItem>();
	
	public List<EnbToOamDispItem> GetDispConfig(int languageInd)
	{
		try {			
			//创建一个DocumentBuilderFactory的对象
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	
			//创建DocumentBuilder对象
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = null;

			//通过DocumentBuilder对象的parser方法加载中英文xml文件到当前项目下
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
			
			//获取所有表节点的集合
			NodeList tableList = document.getElementsByTagName("table");
			
			//通过nodelist的getLength()方法可以获取tableList的长度
			System.out.println("一共有" + tableList.getLength() + "张表");
			
			//遍历每一个table节点
			for (int i = 0; i < tableList.getLength(); i++) {
				System.out.println("=================下面开始遍历第" + (i + 1) + "张表的内容=================");
	
				//通过 item(i)方法 获取一个table节点，nodelist的索引值从0开始
				Node table = tableList.item(i);
                
				//获取table节点的所有属性集合
				NamedNodeMap attrs = table.getAttributes();
				System.out.println("第 " + (i + 1) + "张表共有" + attrs.getLength() + "个属性");
				
				//解析table节点的子节点
				NodeList childNodes = table.getChildNodes();
				
				//遍历childNodes获取每个节点的节点名和节点值
				System.out.println("第" + (i+1) + "张表共有" + 
				childNodes.getLength() + "行");	
				
				//每个表只有一个属性
				Node attr = attrs.item(0);
				//获取属性名
				System.out.print("属性名：" + attr.getNodeName());
				
				//获取属性值
				System.out.println("--属性值:" + attr.getNodeValue());					

				EnbToOamDispItem enbDisp = new EnbToOamDispItem(i+1, attr.getNodeValue());
				Dispconfig.add(enbDisp);
				
				for (int k = 0; k < childNodes.getLength(); k++) {			
					String Name = childNodes.item(k).getAttributes().getNamedItem("显示项").getNodeValue();
					int value = Integer.parseInt(childNodes.item(k).getAttributes().getNamedItem("子选项ID号").getNodeValue());
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
