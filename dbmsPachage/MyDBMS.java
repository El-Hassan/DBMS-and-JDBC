package dbmsPachage;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import operations.Create;
import operations.Delete;
import operations.Insert;
import operations.Select;
import operations.Update;
import operations.Utility;

import org.xml.sax.SAXException;

public class MyDBMS implements DBMS {
	public static String currentDB;
	Parser s;
	Create myCreate;
	Delete myDelete;
	Update myUpdate;
	Insert myInsert;
	Select mySelect;
	Utility myUtility;
	int myCounter;
	int LastRow;

	public MyDBMS() {
		currentDB = "";
		myCreate = new Create();
		myDelete = new Delete();
		myUpdate = new Update();
		myInsert = new Insert();
		mySelect = new Select();
		myUtility = new Utility();
		s = new Parser(this);
		myCounter = 0;
		LastRow = 0;
	}

	@Override
	public String CreateDatabase(String DBname) throws IOException,
			ParserConfigurationException, SAXException, TransformerException {
		String temp = new String();
		temp = myCreate.CreateDatabase(DBname);
		if (temp.equals(Con_DB)) {
			currentDB = DBname;
			myCounter = 0;
		}
		return temp;
	}

	@Override
	public String CreateTable(String tableName, String[] ColumnNames,
			String[] datatype) throws ParserConfigurationException,
			TransformerException {
		String temp = myCreate.addTable(currentDB, tableName.toLowerCase(),
				ColumnNames, datatype);
		if (temp.equals(Con_Table)) {
			myCounter = 0;
		}
		return temp;
	}

	@Override
	public String insertRow(String tableName, String[] ColumnNames,
			String[] values, String[] types, boolean freetogo)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerException {
		myCounter = 0;
		return myInsert.insertRow(currentDB, tableName.toLowerCase(),
				ColumnNames, values, types, freetogo);
	}

	@Override
	public String update(String tableName, String[] Column_to_be_updated,
			String[] values_to_be_updated, String[] types,
			String Column_condition, String values_condition, String valuetype)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerException {
		String temp = myUpdate.update(currentDB, tableName.toLowerCase(),
				Column_to_be_updated, values_to_be_updated, types,
				Column_condition, values_condition, valuetype);
		myCounter = myUpdate.getcount();
		return temp;
	}

	@Override
	public String delete(String tableName, String ColumnName, String values,
			String type, int comp, boolean freetogo)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerException {
		String temp = myDelete.delete(currentDB, tableName, ColumnName, values,
				type, comp, freetogo);
		myCounter = myDelete.getcount();
		return temp;
	}

	@Override
	public String selectAllTable(String tableName)
			throws ParserConfigurationException, SAXException, IOException {
		String temp = mySelect.selectAllTable(currentDB, tableName);
		LastRow = mySelect.getLastRow();
		return temp;
	}

	@Override
	public String selectColumnfromTable(String tableName, String[] columnName)
			throws ParserConfigurationException, SAXException, IOException {
		String temp = mySelect.selectColumnfromTable(currentDB, tableName,
				columnName);
		myCounter = mySelect.getcount();
		LastRow = mySelect.getLastRow();
		return temp;
	}

	@Override
	public String selectrow(String tableName, String columnNames,
			String condition, String valuetype, int comp)
			throws ParserConfigurationException, SAXException, IOException {
		String temp = mySelect.selectrow(currentDB, tableName, columnNames,
				condition, valuetype, comp);
		myCounter = mySelect.getcount();
		LastRow = mySelect.getLastRow();
		return temp;
	}

	@Override
	public String delete(String tableName) throws ParserConfigurationException,
			TransformerException {
		myCounter = 0;
		return myDelete.delete(currentDB, tableName);
	}

	@Override
	public void chooseDB(String dp) {
		currentDB = dp;
	}

	@Override
	public String selectRowFromColumn(String tableName, String[] columnNames,
			String condition, String conditionValue, String valuetype, int comp)
			throws ParserConfigurationException, SAXException, IOException {
		String x = mySelect.selectRowFromColumn(currentDB, tableName,
				columnNames, condition, conditionValue, valuetype, comp);
		myCounter = mySelect.getcount();
		LastRow = mySelect.getLastRow();
		return x;
	}

	@Override
	public int getColumnIndex(String columnLabel, String tableName,
			String DbName) throws ParserConfigurationException, SAXException,
			IOException {
		myCounter = 0;
		return myUtility.getColumnIndex(columnLabel, tableName, DbName);
	}

	public String getColumnLabel(int index, String tableName, String DbName)
			throws ParserConfigurationException, SAXException, IOException {
		return myUtility.getColumnLabel(index, tableName, DbName);
	}

	public String getDataType(int index, String tableName, String DbName)
			throws SAXException, IOException, ParserConfigurationException {
		return myUtility.getDataType(index, tableName, DbName);
	}

	public boolean checkRowbyIndex(String DBname, String tableName, int index)
			throws SAXException, IOException, ParserConfigurationException {
		return myUtility.checkRowbyIndex(DBname, tableName, index);
	}

	@Override
	public String execute(String sql) {
		return s.parser(sql);
	}

	@Override
	public int maxRownumber(String DBname, String tableName)
			throws SAXException, IOException, ParserConfigurationException {
		return myUtility.maxRownumber(DBname, tableName);
	}

	@Override
	public int getcolnumbers(String DBname, String tableName)
			throws SAXException, IOException, ParserConfigurationException {
		return myUtility.getcolnumbers(DBname, tableName);
	}

	public String getcell(int rowindex, int colindex, String tableName,
			String DbName) throws ParserConfigurationException, SAXException,
			IOException {
		return myUtility.getcell(rowindex, colindex, tableName, DbName);
	}

	@Override
	public int getMaxTableindex(String DbName, String tableName)
			throws ParserConfigurationException, SAXException, IOException {
		return myUtility.getMaxTableindex(DbName, tableName);
	}

	@Override
	public String getTableName(int index, String DbName)
			throws ParserConfigurationException, SAXException, IOException {
		return myUtility.getTableName(index, DbName);
	}

	@Override
	public int getCount() {
		return myCounter;
	}

	public int getLastRow() {
		return LastRow;
	}

	public Boolean is_selected() {
		return s.isSelected;
	}

	public String getTableName() {
		return s.getTableName();
	}

	public String getDBName() {
		return currentDB;
	}
}
