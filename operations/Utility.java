package operations;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import log.MyLogger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dbmsPachage.DBMS;
import error.Error;

public class Utility extends Operation {
	public String getcell(int rowindex, int colindex, String tableName,
			String DbName) throws ParserConfigurationException, SAXException,
			IOException {
		String dir = DBMS_Directory + "\\DB " + DbName + "\\" + tableName
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element fileElement = (Element) nodeList.item(i);
			if (fileElement.getParentNode().getNodeName()
					.equals("ROW_NUM" + rowindex)) {
				if (getColumnIndex(fileElement.getNodeName(), tableName, DbName) == colindex) {
					MyLogger.Log().info(DBMS.Con_Select);
					return fileElement.getTextContent();
				}
			}
		}
		MyLogger.Log().error(Error.FATAL_ERROR);
		return Error.FATAL_ERROR;
	}

	public int getcolnumbers(String DBname, String tableName)
			throws SAXException, IOException, ParserConfigurationException {
		String dir = DBMS_Directory + "\\DB " + DBname + "\\" + "schema"
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		int counter = 0;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element fileElement = (Element) nodeList.item(i);
			if (fileElement.getParentNode().getNodeName().equals(tableName)) {
				counter++;
			}
		}
		return counter;
	}

	public int getColumnIndex(String columnLabel, String tableName,
			String DbName) throws ParserConfigurationException, SAXException,
			IOException {
		String dir = DBMS_Directory + "\\DB " + DbName + "\\" + "schema"
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element fileElement = (Element) nodeList.item(i);
			if (fileElement.getParentNode().getNodeName().equals(tableName)
					&& fileElement.getNodeName().equals(columnLabel)) {
				MyLogger.Log().error(DBMS.Con_Select);
				return Integer.parseInt(fileElement.getAttribute("ID"));
			}
		}
		return -1;
	}

	public String getColumnLabel(int index, String tableName, String DbName)
			throws ParserConfigurationException, SAXException, IOException {
		String dir = DBMS_Directory + "\\DB " + DbName + "\\" + "schema"
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element fileElement = (Element) nodeList.item(i);
			if (fileElement.getParentNode().getNodeName().equals(tableName)
					&& fileElement.getAttribute("ID").equals(index + "")) {
				return fileElement.getNodeName();
			}
		}
		MyLogger.Log().error(DBMS.COLUMN_NOT_FOUND);
		return DBMS.COLUMN_NOT_FOUND;
	}

	public String getDataType(int index, String tableName, String DbName)
			throws SAXException, IOException, ParserConfigurationException {
		String dir = DBMS_Directory + "\\DB " + DbName + "\\" + "schema"
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element fileElement = (Element) nodeList.item(i);
			if (fileElement.getParentNode().getNodeName().equals(tableName)
					&& fileElement.getAttribute("ID").equals(index + "")) {
				return fileElement.getTextContent();
			}
		}
		MyLogger.Log().error(DBMS.COLUMN_NOT_FOUND);
		return DBMS.COLUMN_NOT_FOUND;
	}

	public String getTableName(int index, String DbName)
			throws ParserConfigurationException, SAXException, IOException {
		String dir = DBMS_Directory + "\\DB " + DbName + "\\" + "schema"
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element fileElement = (Element) nodeList.item(i);
			if (fileElement.getParentNode().getNodeName().equals(DbName)
					&& fileElement.getAttribute("ID").equals(index + "")) {
				MyLogger.Log().error(DBMS.Con_Select);
				return fileElement.getNodeName();
			}
		}
		MyLogger.Log().error(DBMS.TABLE_NOT_FOUND);
		return DBMS.TABLE_NOT_FOUND;
	}

	public boolean checkRowbyIndex(String DBname, String tableName, int index)
			throws SAXException, IOException, ParserConfigurationException {
		int x = maxRownumber(DBname, tableName);
		return index <= x && index >= 1;
	}
}
