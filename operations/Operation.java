package operations;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dbmsPachage.DBMS;

public class Operation {
	protected final String DBMS_Directory = "MY DBMS";
	final static Pattern lastIntPattern = Pattern.compile("[^0-9]+([0-9]+)$");

	protected String checkcolumn(String DBname, String tableName,
			String[] ColumnNames, String[] datatype)
			throws ParserConfigurationException, SAXException, IOException {
		if (!justcheckcolumn(DBname, tableName, ColumnNames)) {
			MyLogger.Log().error(DBMS.COLUMN_NOT_FOUND);
			return DBMS.COLUMN_NOT_FOUND;
		}
		ArrayList<String> names = new ArrayList<>();
		ArrayList<String> values = new ArrayList<>();
		String dir = DBMS_Directory + "\\DB " + DBname + "\\" + "schema"
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element fileElement = (Element) nodeList.item(i);
			if (fileElement.getParentNode().getNodeName().equals(tableName)) {
				names.add(fileElement.getNodeName());
				values.add(fileElement.getTextContent());
			}
		}

		int counter = 0;
		int texting = 0;
		for (int i = 0; i < ColumnNames.length && !ColumnNames[i].equals(""); i++) {
			texting++;
			for (int j = 0; j < names.size(); j++) {
				if ((names.get(j).equals(ColumnNames[i]))) {
					if (values.get(j).toLowerCase().equals(datatype[i])) {
						counter++;
						break;
					}
				}
			}
		}

		if (texting > counter) {
			MyLogger.Log().error(DBMS.COLUMN_TYPE_MISMATCH);
			return DBMS.COLUMN_TYPE_MISMATCH;
		}
		return "good";
	}

	public boolean checkDB(final String DBname) {
		File f = new File(DBMS_Directory);
		try {
			File[] matchingFiles = f.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.equals("DB " + DBname);
				}
			});
			return matchingFiles.length > 0;
		} catch (Exception e) {
			return false;
		}
	}

	protected boolean checkTable(String DBname, final String tableName) {
		File f = new File(DBMS_Directory + "\\DB " + DBname);

		File[] matchingFiles = f.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.equals(tableName.toLowerCase() + ".xml");
			}
		});
		return matchingFiles.length > 0;
	}

	protected boolean justcheckcolumn(String DBname, String tableName,
			String[] ColumnNames) throws ParserConfigurationException,
			SAXException, IOException {
		ArrayList<String> names = new ArrayList<>();
		ArrayList<String> values = new ArrayList<>();
		String dir = DBMS_Directory + "\\DB " + DBname + "\\" + "schema"
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element fileElement = (Element) nodeList.item(i);
			if (fileElement.getParentNode().getNodeName().toLowerCase()
					.equals(tableName.toLowerCase())) {
				names.add(fileElement.getNodeName());
				values.add(fileElement.getTextContent());
			}
		}
		int counter = 0;
		int texting = 0;
		for (int i = 0; i < ColumnNames.length && !ColumnNames[i].equals(""); i++) {
			texting++;
			for (int j = 0; j < names.size(); j++) {
				if (ColumnNames[i].toLowerCase().equals(
						names.get(j).toLowerCase())) {
					counter++;
					break;
				}
			}
		}
		if (texting > counter) {
			return false;
		}
		return true;
	}

	public Document Load(String dir) throws ParserConfigurationException,
			SAXException, IOException {
		File file = new File(dir);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		return doc;
	}

	public void Save(Document doc, String dir) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		File momoen = new File(dir);
		StreamResult result = new StreamResult(momoen);
		transformer.transform(source, result);
	}

	public int maxRownumber(String DBname, String tableName)
			throws SAXException, IOException, ParserConfigurationException {
		ArrayList<String> names = new ArrayList<>();
		String dir = DBMS_Directory + "\\DB " + DBname + "\\" + tableName
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
		int a = 0;
		for (int i = 0; i < names.size(); i++) {
			String input = names.get(i);
			Matcher matcher = lastIntPattern.matcher(input);
			if (matcher.find()) {
				String someNumberStr = matcher.group(1);
				a = Math.max(a, Integer.parseInt(someNumberStr));
			}
		}
		return a;
	}

	public int getMaxTableindex(String DbName, String tableName)
			throws ParserConfigurationException, SAXException, IOException {
		String dir = DBMS_Directory + "\\DB " + DbName + "\\" + "schema"
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		int counter = 0;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element fileElement = (Element) nodeList.item(i);
			if (fileElement.getParentNode().getNodeName().equals(DbName)) {
				double a = Double.parseDouble(fileElement.getAttribute("ID"));
				counter = (int) Math.max(counter, (int) a);
			}
		}
		return counter;
	}

	protected void correctindex(String DBname, String tablename)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerException {
		String dir = DBMS_Directory + "\\DB " + DBname + "\\" + tablename
				+ ".xml";
		Document doc = Load(dir);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		int counter = 1;
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeName().contains("ROW_NUM")) {
				doc.renameNode(nodeList.item(i), null, "ROW_NUM" + counter);
				counter++;
			}
		}
		Save(doc, dir);
	}

	public boolean checkRowbyIndex(String DBname, String tableName, int index)
			throws SAXException, IOException, ParserConfigurationException {
		int x = maxRownumber(DBname, tableName);
		return index <= x;
	}

	public ArrayList<String> getAllDB() throws Exception {
		ArrayList<String> s = new ArrayList<>();
		File f = new File(DBMS_Directory);
		try {
			File[] matchingFiles = f.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.contains("DB");
				}
			});
			for (int i = 0; i < matchingFiles.length; i++) {
				s.add(matchingFiles[i].toString());
			}
			return s;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}
