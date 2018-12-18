package com.topvision.ems.cmc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;

public class MyBatisUtils {
	public static void main(String[] args) throws FileNotFoundException {
		File mysqlFile = new File(
				"D:/EMS/mapper/mysql/Cmc.xml");
		File oracleFile = new File(
				"D:/EMS/mapper/oracle/Cmc.xml");
		XPathParser parser = new XPathParser(new FileInputStream(mysqlFile));
		System.out.println("test1");
		XNode node = parser.evalNode("sqlMap");
		List<XNode> sList = node.evalNodes("insert|select|delete|update");
		XPathParser oracleParser = new XPathParser(new FileInputStream(
				oracleFile));
		XNode oracleNode = oracleParser.evalNode("sqlMap");
		List<XNode> oList = oracleNode.evalNodes("insert|select|delete|update");
		System.out.println("MySql:" + sList.size());
		System.out.println("Oracle:" + oList.size());
        int sameSum = 0;
        int noSum = 0;
		for (int i = 0; i < sList.size(); i++) {
			XNode mNode = sList.get(i);
			for (int j = 0; j < oList.size(); j++) {
				XNode oNode = oList.get(j);
				if(mNode.getStringAttribute("id").equals(oNode.getStringAttribute("id"))){
					String m_parameterClass = mNode.getStringAttribute("parameterClass") == null ? "null" : mNode.getStringAttribute("parameterClass");
					String o_parameterClass = oNode.getStringAttribute("parameterClass") == null ? "null" : oNode.getStringAttribute("parameterClass");
					String m_resultClass = mNode.getStringAttribute("resultClass") == null ? "null" : mNode.getStringAttribute("resultClass");
					String o_resultClass = oNode.getStringAttribute("resultClass") == null ? "null" : oNode.getStringAttribute("resultClass");
					String mText = mNode.getStringBody().trim().replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", "");
					String oText = oNode.getStringBody().trim().replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", "");
					if(m_parameterClass.equals(o_parameterClass)&&m_resultClass.equals(o_resultClass)&&mText.equals(oText)){
						sameSum++;
						System.out.println("Same:"+oNode.getStringAttribute("id"));
					}else{
						noSum++;
						System.out.println("No:"+oNode.getStringAttribute("id"));
					}
				}
			}

		}
		System.out.println("Same:" + sameSum);
		System.out.println("No:" + noSum);
	}
}
