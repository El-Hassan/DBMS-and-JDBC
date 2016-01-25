package operations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import log.MyLogger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dbmsPachage.DBMS;

public class Create extends Operation {
	private int count = 0;

	public int getcount() {
		return count;
	}

	public String CreateDatabase(String DBname)
			throws ParserConfigurationException, TransformerException {
		if (checkDB(DBname)) {
			MyLogger.Log().error("Allready Have one With the name>" + DBname);
			return "allready have one";
		}
		File DBDirectory = new File(DBMS_Directory + "\\DB " + DBname);
		if (!DBDirectory.exists()) {
			try {
				DBDirectory.mkdir();
			} catch (SecurityException se) {
				MyLogger.Log().error(se.getMessage());
			}
		}
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(DBname);
		doc.appendChild(rootElement);
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(DBMS_Directory
				+ "\\DB " + DBname + "\\" + "schema" + ".xml"));
		transformer.transform(source, result);
		MyLogger.Log().info(DBMS.Con_DB);
		count = 0;
		return DBMS.Con_DB;
	}

	public void createRow(String DBname, String tableName,
			ArrayList<String> names, ArrayList<String> columsvalues)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerException {
		String dir = DBMS_Directory + "\\DB " + DBname + "\\" + tableName
				+ ".xml";
		Document doc = Load(dir);
		NodeList nl = doc.getElementsByTagName(tableName);
		Node node = nl.item(0);
		Element element = (Element) node;
		int rownum = maxRownumber(DBname, tableName) + 1;
		Node childnode = doc.createElement("ROW_NUM" + rownum);
		element.appendChild(childnode);
		Element tn = (Element) childnode;
		for (int i = 0; i < names.size(); i++) {
			Node temp = doc.createElement(names.get(i));
			temp.setTextContent(columsvalues.get(i));
			tn.appendChild(temp);
		}
		Save(doc, dir);
	}

	public void createTable(String DBname, String tableName)
			throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(tableName.toLowerCase());
		doc.appendChild(rootElement);
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult t = new StreamResult(new File(DBMS_Directory + "\\DB "
				+ DBname + "\\" + tableName.toLowerCase() + ".xml"));
		transformer.transform(source, t);
	}

	public String addTable(String DBname, String tableName,
			String[] ColumnNames, String[] datatype)
			throws ParserConfigurationException, TransformerException {
		if (!checkDB(DBname)) {
			MyLogger.Log().error(DBMS.DB_NOT_FOUND);
			return DBMS.DB_NOT_FOUND;
		}
		if (checkTable(DBname, tableName.toLowerCase())) {
			MyLogger.Log().error(DBMS.TABLE_ALREADY_EXISTS);
			return DBMS.TABLE_ALREADY_EXISTS;
		}
		try {
			File inputFile = new File(DBMS_Directory + "\\DB " + DBname + "\\"
					+ "schema" + ".xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// creating input stream
			Document doc = builder.parse(inputFile);
			NodeList nl = doc.getElementsByTagName(DBname);
			Node node = nl.item(0);
			Element element = (Element) node;
			Node childnode = doc.createElement(tableName);
			element.appendChild(childnode);
			Element tn = (Element) childnode;
			tn.setAttribute("ID", getMaxTableindex(DBname, tableName) + 1 + "");
			for (int i = 0; i < ColumnNames.length
					&& !ColumnNames[i].equals(""); i++) {
				Element xxxx = doc.createElement(ColumnNames[i]);
				xxxx.setAttribute("ID", i + 1 + "");
				Node temp = xxxx;
				temp.setTextContent(datatype[i]);
				tn.appendChild(temp);
			}
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			File momoen = new File(DBMS_Directory + "\\DB " + DBname + "\\"
					+ "schema" + ".xml");
			StreamResult result = new StreamResult(momoen);
			transformer.transform(source, result);
		} catch (Exception e) {
			MyLogger.Log().error(e.getMessage());
			e.printStackTrace();
			return "faild";
		}
		createTable(DBname, tableName);
		MyLogger.Log().info(DBMS.Con_Table);
		count = 0;
		return DBMS.Con_Table;
	}
}
