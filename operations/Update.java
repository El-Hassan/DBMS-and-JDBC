package operations;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import log.MyLogger;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dbmsPachage.DBMS;

public class Update extends Operation {
	private int count = 0;

	public int getcount() {
		return count;
	}

	public String update(String DBname, String tableName,
			String[] Column_to_be_updated, String[] values_to_be_updated,
			String[] types, String Column_condition, String values_condition,
			String valuetype) throws ParserConfigurationException,
			SAXException, IOException, TransformerException {
		count = 0;
		if (!checkDB(DBname)) {
			MyLogger.Log().error(DBMS.DB_NOT_FOUND);
			return DBMS.DB_NOT_FOUND;
		}
		if (!checkTable(DBname, tableName)) {
			MyLogger.Log().error(DBMS.TABLE_NOT_FOUND);
			return DBMS.TABLE_NOT_FOUND;
		}
		String temp = checkcolumn(DBname, tableName, Column_to_be_updated,
				types);
		if (!temp.equals("good")) {
			MyLogger.Log().error(temp);
			return temp;
		}
		String[] r = new String[1];
		r[0] = Column_condition;
		String[] rr = new String[1];
		rr[0] = valuetype;
		temp = checkcolumn(DBname, tableName, r, rr);
		if (!temp.equals("good")) {
			MyLogger.Log().error(temp);
			return temp;
		}
		// end check
		String dir = DBMS_Directory + "\\DB " + DBname + "\\" + tableName
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeName().contains("ROW_NUM")) {
				for (int p = i + 1; p <= i
						+ nodeList.item(i).getChildNodes().getLength(); p++) {
					if (nodeList.item(p).getNodeName().equals(Column_condition)) {
						if (nodeList.item(p).getTextContent()
								.equals(values_condition)) {
							count++;
							for (int j = 0; j < Column_to_be_updated.length; j++) {
								for (int k = i + 1; k <= i
										+ nodeList.item(i).getChildNodes()
												.getLength(); k++) {
									if (Column_to_be_updated[j].equals(nodeList
											.item(k).getNodeName())) {
										nodeList.item(k).setTextContent(
												values_to_be_updated[j]);
									}
								}
							}
						}
					}
				}
			}
		}
		Save(doc, dir);
		if (count == 0) {
			MyLogger.Log().error(DBMS.NOT_MATCH_CRITERIA);
			return DBMS.NOT_MATCH_CRITERIA;
		}
		MyLogger.Log().info(DBMS.Con_Update);
		return DBMS.Con_Update;
	}
}