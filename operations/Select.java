package operations;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import log.MyLogger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dbmsPachage.DBMS;

public class Select extends Operation {
	private int count = 0;
	private int LastRow = 0;

	public int getcount() {
		return count;
	}

	public int getLastRow() {
		return LastRow;
	}

	public String selectAllTable(String DBname, String tableName)
			throws ParserConfigurationException, SAXException, IOException {
		if (!checkDB(DBname)) {
			MyLogger.Log().error(DBMS.DB_NOT_FOUND);
			return DBMS.DB_NOT_FOUND;
		}
		if (!checkTable(DBname, tableName)) {
			MyLogger.Log().error(DBMS.TABLE_NOT_FOUND);
			return DBMS.TABLE_NOT_FOUND;
		}
		String dir = DBMS_Directory + "\\DB " + DBname + "\\" + tableName
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		String columns = "";
		int numOfColumns = 0;
		for (int i = 1; i < nodeList.getLength(); i++) {
			Element fileElement = (Element) nodeList.item(i);
			if (!fileElement.getNodeName().toString().contains("ROW_NUM")
					&& !columns.contains(fileElement.getNodeName().toString())) {
				columns = columns + fileElement.getNodeName() + "  ";
				numOfColumns++;
			}
		}
		String values = "";
		int countValues = 0;
		NodeList nl = doc.getElementsByTagName(tableName);
		NodeList n = nl.item(0).getChildNodes();
		for (int i = 0; i < n.getLength(); i++) {
			NodeList rows = n.item(i).getChildNodes();
			for (int j = 0; j < rows.getLength(); j++) {
				countValues++;
				if (numOfColumns == countValues) {
					values = values
							+ rows.item(j).getChildNodes().item(0)
									.getNodeValue();
				} else {
					values = values
							+ rows.item(j).getChildNodes().item(0)
									.getNodeValue() + "  ";
				}
			}
			if (numOfColumns == countValues) {
				values += "\n";
				countValues = 0;
			}
		}
		if (values == "") {
			MyLogger.Log().error(DBMS.NOT_MATCH_CRITERIA);
			return DBMS.NOT_MATCH_CRITERIA;
		}
		values = values.substring(0, values.length() - 1);
		LastRow = maxRownumber(DBname, tableName);
		MyLogger.Log().info(DBMS.Con_Select);
		return values;
	}

	public String selectColumnfromTable(String DBname, String tableName,
			String[] columnName) throws ParserConfigurationException,
			SAXException, IOException {
		count = 0;
		if (!checkDB(DBname)) {
			MyLogger.Log().error(DBMS.DB_NOT_FOUND);
			return DBMS.DB_NOT_FOUND;
		}

		if (!checkTable(DBname, tableName)) {
			MyLogger.Log().error(DBMS.TABLE_NOT_FOUND);
			return DBMS.TABLE_NOT_FOUND;
		}
		if (!justcheckcolumn(DBname, tableName, columnName)) {
			MyLogger.Log().error(DBMS.COLUMN_NOT_FOUND);
			return DBMS.COLUMN_NOT_FOUND;
		}

		String dir = DBMS_Directory + "\\DB " + DBname + "\\" + tableName
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		int numOfColumns = columnName.length;
		ArrayList<String> cn = new ArrayList<String>();
		for (int i = 0; i < columnName.length; i++) {
			cn.add(columnName[i]);
		}
		String values = "";
		int countValues = 0;
		NodeList nl = doc.getElementsByTagName(tableName);
		NodeList n = nl.item(0).getChildNodes();
		for (int i = 0; i < n.getLength(); i++) {
			NodeList rows = n.item(i).getChildNodes();
			for (int j = 0; j < rows.getLength(); j++) {
				if (cn.contains(rows.item(j).getNodeName())) {
					countValues++;
					if (numOfColumns == countValues) {
						values = values
								+ rows.item(j).getChildNodes().item(0)
										.getNodeValue();
					} else {
						values = values
								+ rows.item(j).getChildNodes().item(0)
										.getNodeValue() + "  ";
					}
				}
			}
			if (numOfColumns == countValues) {
				values += "\n";
				count++;
				countValues = 0;
			}
		}
		if (values == "") {
			MyLogger.Log().error(DBMS.NOT_MATCH_CRITERIA);
			return DBMS.NOT_MATCH_CRITERIA;
		}
		values = values.substring(0, values.length() - 1);
		LastRow = maxRownumber(DBname, tableName);
		return values;
	}

	public String selectrow(String DBname, String tableName,
			String columnNames, String condition, String valuetype, int comp)
			throws ParserConfigurationException, SAXException, IOException {
		count = 0;
		if (!checkDB(DBname)) {
			MyLogger.Log().equals(DBMS.DB_NOT_FOUND);
			return DBMS.DB_NOT_FOUND;
		}
		if (!checkTable(DBname, tableName)) {
			MyLogger.Log().error(DBMS.TABLE_NOT_FOUND);
			return DBMS.TABLE_NOT_FOUND;
		}
		String[] r = new String[1];
		r[0] = columnNames;
		String[] rr = new String[1];
		rr[0] = valuetype;
		if (!checkcolumn(DBname, tableName, r, rr).equals("good")) {
			return checkcolumn(DBname, tableName, r, rr);
		}
		String dir = DBMS_Directory + "\\DB " + DBname + "\\" + tableName
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		String columns = "";
		int numOfColumns = 0;
		for (int i = 1; i < nodeList.getLength(); i++) {
			Element fileElement = (Element) nodeList.item(i);
			if (!fileElement.getNodeName().toString().contains("ROW_NUM")
					&& !columns.contains(fileElement.getNodeName().toString())) {
				columns = columns + fileElement.getNodeName() + "\t";
				numOfColumns++;
			}
		}
		String values = "";
		String rowValue = "";
		int countValues = 0;
		NodeList nl = doc.getElementsByTagName(tableName);
		NodeList n = nl.item(0).getChildNodes();
		boolean print = false;
		for (int i = 0; i < n.getLength(); i++) {
			NodeList rows = n.item(i).getChildNodes();
			for (int j = 0; j < rows.getLength(); j++) {
				String x = rows.item(j).getChildNodes().item(0).getNodeValue();
				switch (comp) {
				case 0:
					if (rows.item(j).getNodeName().equals(columnNames)
							&& x.equals(condition)) {
						print = true;
						count++;
						if (rows.item(j).getParentNode().getNodeName()
								.contains("ROW_NUM")) {
							String s = rows.item(j).getParentNode()
									.getNodeName().toString();
							s = s.replaceAll("\\D+", "");
							LastRow = Integer.parseInt(s);
						}
					}
					break;
				case 1:
					if (rows.item(j).getNodeName().equals(columnNames)) {
						double a = Double.parseDouble(rows.item(j)
								.getChildNodes().item(0).getNodeValue());
						double b = Double.parseDouble(condition);
						if (a > b) {
							count++;
							print = true;
							if (rows.item(j).getParentNode().getNodeName()
									.contains("ROW_NUM")) {
								String s = rows.item(j).getParentNode()
										.getNodeName().toString();
								s = s.replaceAll("\\D+", "");
								LastRow = Integer.parseInt(s);
							}
						}
					}
					break;
				case -1:
					if (rows.item(j).getNodeName() == columnNames) {
						double a = Double.parseDouble(rows.item(j)
								.getChildNodes().item(0).getNodeValue());
						double b = Double.parseDouble(condition);
						if (a < b) {
							count++;
							print = true;
							if (rows.item(j).getParentNode().getNodeName()
									.contains("ROW_NUM")) {
								String s = rows.item(j).getParentNode()
										.getNodeName().toString();
								s = s.replaceAll("\\D+", "");
								LastRow = Integer.parseInt(s);
							}
						}
					}
					break;
				default:
					break;
				}
				countValues++;
				if (numOfColumns == countValues) {
					rowValue = rowValue
							+ rows.item(j).getChildNodes().item(0)
									.getNodeValue();
				} else {
					rowValue = rowValue
							+ rows.item(j).getChildNodes().item(0)
									.getNodeValue() + "  ";
				}
			}
			if (print)
				values += rowValue;
			rowValue = "";
			if (numOfColumns == countValues && print) {
				values += "\n";
			}
			print = false;
			countValues = 0;
		}
		if (values == "") {
			MyLogger.Log().error(DBMS.NOT_MATCH_CRITERIA);
			return DBMS.NOT_MATCH_CRITERIA;
		}
		values = values.substring(0, values.length() - 1);
		MyLogger.Log().info(DBMS.Con_Select);
		return values;
	}

	public String selectRowFromColumn(String DBname, String tableName,
			String[] columnNames, String condition, String conditionValue,
			String valuetype, int comp) throws ParserConfigurationException,
			SAXException, IOException {
		count = 0;
		if (!checkDB(DBname)) {
			MyLogger.Log().error(DBMS.DB_NOT_FOUND);
			return DBMS.DB_NOT_FOUND;
		}
		if (!checkTable(DBname, tableName)) {
			MyLogger.Log().error(DBMS.TABLE_NOT_FOUND);
			return DBMS.TABLE_NOT_FOUND;
		}
		if (!justcheckcolumn(DBname, tableName, columnNames)) {
			MyLogger.Log().error(DBMS.COLUMN_NOT_FOUND);
			return DBMS.COLUMN_NOT_FOUND;
		}
		String[] rr = new String[1];
		rr[0] = valuetype.toLowerCase();
		String[] r = new String[1];
		r[0] = condition.toLowerCase();
		String temp = checkcolumn(DBname, tableName, r, rr);
		if (!temp.equals("good")) {
			MyLogger.Log().error(temp);
			return temp;
		}
		String dir = DBMS_Directory + "\\DB " + DBname + "\\" + tableName
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		ArrayList<String> cn = new ArrayList<String>();
		for (int i = 0; i < columnNames.length && !columnNames[i].equals(""); i++) {
			cn.add(columnNames[i].toLowerCase());
		}
		String values = "";
		NodeList nl = doc.getElementsByTagName(tableName);
		NodeList n = nl.item(0).getChildNodes();
		boolean print = false;
		for (int i = 0; i < n.getLength(); i++) {
			NodeList rows = n.item(i).getChildNodes();
			for (int j = 0; j < rows.getLength(); j++) {
				String x = rows.item(j).getChildNodes().item(0).getNodeValue();
				switch (comp) {
				case 0:
					if (rows.item(j).getNodeName()
							.equals(condition.toLowerCase())
							&& x.equals(conditionValue)) {
						for (int k = 0; k < rows.getLength(); k++) {
							if (cn.contains(rows.item(k).getNodeName())) {
								values += rows.item(k).getChildNodes().item(0)
										.getNodeValue() + '\n';
								count++;
								if (rows.item(j).getParentNode().getNodeName()
										.contains("ROW_NUM")) {
									String s = rows.item(j).getParentNode()
											.getNodeName().toString();
									s = s.replaceAll("\\D+", "");
									LastRow = Integer.parseInt(s);
								}
							}
						}
					}
					break;
				case 1:

					if (rows.item(j).getNodeName()
							.equals(condition.toLowerCase())
							&& cn.contains(condition.toLowerCase())) {
						double a = Double.parseDouble(rows.item(j)
								.getChildNodes().item(0).getNodeValue());
						double b = Double.parseDouble(condition);
						if (a > b) {
							print = true;
							count++;
							if (rows.item(j).getParentNode().getNodeName()
									.contains("ROW_NUM")) {
								String s = rows.item(j).getParentNode()
										.getNodeName().toString();
								s = s.replaceAll("\\D+", "");
								LastRow = Integer.parseInt(s);
							}
						}
					}
					break;
				case -1:
					if (cn.contains(condition.toLowerCase())
							&& rows.item(j).getNodeName()
									.equals(condition.toLowerCase())) {
						double a = Double.parseDouble(rows.item(j)
								.getChildNodes().item(0).getNodeValue());
						double b = Double.parseDouble(condition);
						if (a < b) {
							count++;
							print = true;
							if (rows.item(j).getParentNode().getNodeName()
									.contains("ROW_NUM")) {
								String s = rows.item(j).getParentNode()
										.getNodeName().toString();
								s = s.replaceAll("\\D+", "");
								LastRow = Integer.parseInt(s);
							}
						}
					}
					break;
				default:
					break;
				}
				if (print == true) {
					for (int k = 0; k < rows.getLength(); k++) {
						if (cn.contains(rows.item(k).getNodeName())) {
							values += rows.item(k).getChildNodes().item(0)
									.getNodeValue() + '\n';
						}
					}
				}
				print = false;
			}
		}
		if (values.length() == 0) {
			MyLogger.Log().error(DBMS.NOT_MATCH_CRITERIA);
			return DBMS.NOT_MATCH_CRITERIA;
		}
		values = values.substring(0, values.length() - 1);
		MyLogger.Log().info(DBMS.Con_Select);
		return values;
	}
}
