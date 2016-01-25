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

public class Delete extends Create {
	private int count = 0;

	public int getcount() {
		return count;
	}

	public String delete(String DBname, String tableName)
			throws ParserConfigurationException, TransformerException {
		if (!checkDB(DBname)) {
			MyLogger.Log().error(DBMS.DB_NOT_FOUND);
			return DBMS.DB_NOT_FOUND;
		}
		if (!checkTable(DBname, tableName.toLowerCase())) {
			MyLogger.Log().error(DBMS.TABLE_NOT_FOUND);
			return DBMS.TABLE_NOT_FOUND;
		}
		createTable(DBname, tableName.toLowerCase());
		MyLogger.Log().info(DBMS.Con_Delete);
		return DBMS.Con_Delete;
	}

	public String delete(String DBname, String tableName, String ColumnName,
			String condition, String type, int comp, boolean freetogo)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerException {
		count = 0;
		if (!freetogo) {
			if (!checkDB(DBname)) {
				MyLogger.Log().error(DBMS.DB_NOT_FOUND);
				return DBMS.DB_NOT_FOUND;
			}
			if (!checkTable(DBname, tableName.toLowerCase())) {
				MyLogger.Log().error(DBMS.TABLE_NOT_FOUND);
				return DBMS.TABLE_NOT_FOUND;
			}
			String[] r = new String[1];
			r[0] = ColumnName;
			String[] rr = new String[1];
			rr[0] = type;
			String temp = checkcolumn(DBname, tableName, r, rr);
			if (!temp.equals("good")) {
				MyLogger.Log().error(temp);
				return temp;
			}
		}
		ArrayList<String> names = new ArrayList<>();
		String dir = DBMS_Directory + "\\DB " + DBname + "\\" + tableName
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		boolean deleted = false;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element fileElement = (Element) nodeList.item(i);
			if (fileElement.getParentNode().getNodeName().contains("ROW_NUM")) {
				if (fileElement.getNodeName().equals(ColumnName)) {
					switch (comp) {
					case 0:
						if (fileElement.getTextContent().equals(condition)) {
							names.add(fileElement.getParentNode().getNodeName());
							deleted = true;
							count++;
						}
						break;
					case 1:
						double a = Double.parseDouble(fileElement
								.getTextContent());
						double b = Double.parseDouble(condition);
						if (a > b) {
							names.add(fileElement.getParentNode().getNodeName());
							deleted = true;
							count++;
						}
						break;
					case -1:
						double aa = Double.parseDouble(fileElement
								.getTextContent());
						double bb = Double.parseDouble(condition);
						if (aa < bb) {
							names.add(fileElement.getParentNode().getNodeName());
							deleted = true;
							count++;
						}
						break;
					default:
						break;
					}
				}
			}
		}
		if (!deleted) {
			MyLogger.Log().error(DBMS.NOT_MATCH_CRITERIA);
			return DBMS.NOT_MATCH_CRITERIA;
		}
		for (int j = 0; j < names.size(); j++) {
			for (int i = nodeList.getLength() - 1; i >= 0; i--) {
				Element e = (Element) nodeList.item(i);
				if (e.getNodeName().equals(names.get(j))) {
					e.getParentNode().removeChild(e);
				}
			}
		}
		Save(doc, dir);
		correctindex(DBname, tableName);
		MyLogger.Log().info(DBMS.Con_Delete);
		return DBMS.Con_Delete;
	}
}
