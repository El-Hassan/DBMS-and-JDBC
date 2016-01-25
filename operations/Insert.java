package operations;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import log.MyLogger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dbmsPachage.DBMS;

public class Insert extends Create {
	public String insertRow(String DBname, String tableName,
			String[] ColumnNames, String[] values, String[] types,
			boolean freetogo) throws ParserConfigurationException,
			SAXException, IOException, TransformerException {
		if (ColumnNames[0].equals("sa3ed")) {
			freetogo = true;
		}
		if (!freetogo) {
			if (!checkDB(DBname)) {
				MyLogger.Log().error(DBMS.DB_NOT_FOUND);
				return DBMS.DB_NOT_FOUND;
			}
			if (!checkTable(DBname, tableName.toLowerCase())) {
				MyLogger.Log().error(DBMS.TABLE_NOT_FOUND);
				return DBMS.TABLE_NOT_FOUND;
			}
			String temp = checkcolumn(DBname, tableName, ColumnNames, types);
			if (!temp.equals("good")) {
				MyLogger.Log().error(temp);
				return temp;
			}

		}
		// search schema for columns
		ArrayList<String> names = new ArrayList<>();
		String dir = DBMS_Directory + "\\DB " + DBname + "\\" + "schema"
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element fileElement = (Element) nodeList.item(i);
			if (fileElement.getParentNode().getNodeName().equals(tableName)) {
				names.add(fileElement.getNodeName());
			}
		}
		ArrayList<String> columsvalues = new ArrayList<>();
		// search for the same name in columns names array if found add its
		// value else add null
		if (ColumnNames[0].equals("sa3ed")) {
			if (!checkDB(DBname)) {
				MyLogger.Log().error(DBMS.DB_NOT_FOUND);
				return DBMS.DB_NOT_FOUND;
			}
			if (!checkTable(DBname, tableName.toLowerCase())) {
				MyLogger.Log().error(DBMS.TABLE_NOT_FOUND);
				return DBMS.TABLE_NOT_FOUND;
			}
			String[] sa3ed = new String[names.size()];
			for (int i = 0; i < names.size(); i++) {
				sa3ed[i] = names.get(i);
			}
			String tt = "";
			tt = checkcolumn(DBname, tableName, sa3ed, types);
			if (!tt.equals("good")) {
				return tt;
			}
			for (int i = 0; i < values.length; i++) {
				columsvalues.add(values[i]);
			}
			createRow(DBname, tableName, names, columsvalues);
		} else {
			boolean found;
			for (int i = 0; i < names.size(); i++) {
				found = false;
				for (int j = 0; j < ColumnNames.length
						&& !ColumnNames[i].equals(""); j++) {
					if (ColumnNames[j].equals(names.get(i))) {
						columsvalues.add(values[j]);
						found = true;
						break;
					}
				}
				if (!found) {
					columsvalues.add("null");
				}
			}
			createRow(DBname, tableName, names, columsvalues);
		}
		MyLogger.Log().error(DBMS.Con_insert);
		return DBMS.Con_insert;
	}
}
