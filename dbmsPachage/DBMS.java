package dbmsPachage;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public interface DBMS {
	public static final String TABLE_NOT_FOUND = "This Table doesn't exists in database";
	public static final String COLUMN_NOT_FOUND = "This column doesn't exists in this table";
	public static final String TABLE_ALREADY_EXISTS = "This Table already exists";
	public static final String PARSING_ERROR = "bad formated input";
	public static final String DB_NOT_FOUND = "No database exists";
	public static final String COLUMN_TYPE_MISMATCH = "Entered value doesn't match column type";
	public static final String Con_DB = "DB created";
	public static final String Con_Table = "Table created";
	public static final String Con_insert = "insertion Complete";
	public static final String Con_Delete = "Row/s deleted";
	public static final String Con_Update = "Row/s Updated";
	public static final String NOT_MATCH_CRITERIA = "no row exists with this criteria";
	public static final String Con_Select = "Select Operation was Successful";
	public static final String DB_Found = "DB chosen was Successful";

	// CREATE DATABASE database name;
	public String CreateDatabase(String dataBaseName) throws IOException,
			ParserConfigurationException, SAXException, TransformerException;

	/*
	 * CREATE TABLE table_name ( column_name1 data_type(size), column_name2
	 * data_type(size), column_name3 data_type(size), .... );
	 */
	public String CreateTable(String tableName, String[] ColumnNames,
			String[] datatype) throws ParserConfigurationException,
			TransformerException;

	// INSERT INTO table_name (column1,column2,column3,...VALUES
	// (value1,value2,value3,...);
	public String insertRow(String tableName, String[] ColumnNames,
			String[] values, String[] types, boolean freetogo)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerException;

	// UPDATE table_name SET column1=value1,column2=value2,...WHERE
	// some_column=some_value;
	// UPDATE Customers SET ContactName='Alfred Schmidt', City='Hamburg' WHERE
	// CustomerName='Alfreds Futterkiste';
	public String update(String tableName, String[] Column_to_be_updated,
			String[] values_to_be_updated, String[] types,
			String Column_condition, String values_condition, String valuetype)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerException;

	// DELETE FROM table_name
	// WHERE some_column=some_value;
	// DELETE FROM Customers
	// WHERE CustomerName='Alfreds Futterkiste' AND ContactName='Maria Anders';

	public String delete(String tableName, String ColumnNames, String values,
			String type, int comp, boolean freetogo)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerException;

	public String delete(String tableName) throws ParserConfigurationException,
			TransformerException;

	// SELECT * FROM table_name;

	public String selectAllTable(String tableName)
			throws ParserConfigurationException, SAXException, IOException;

	// SELECT column_name,column_name
	// FROM table_name;
	public String selectColumnfromTable(String tableName, String[] columnName)
			throws ParserConfigurationException, SAXException, IOException;

	public String selectrow(String tableName, String columnNames,
			String condition, String valuetype, int comp)
			throws ParserConfigurationException, SAXException, IOException;

	public void chooseDB(String dp);

	public String selectRowFromColumn(String tableName, String[] columnNames,
			String condition, String conditionValue, String valuetype, int comp)
			throws ParserConfigurationException, SAXException, IOException;

	public int getColumnIndex(String columnLabel, String tableName,
			String DbName) throws ParserConfigurationException, SAXException,
			IOException;

	public String getColumnLabel(int index, String tableName, String DbName)
			throws ParserConfigurationException, SAXException, IOException;

	public String getDataType(int index, String tableName, String DbName)
			throws SAXException, IOException, ParserConfigurationException;

	public boolean checkRowbyIndex(String DBname, String tableName, int index)
			throws SAXException, IOException, ParserConfigurationException;

	public String execute(String sql);

	public int maxRownumber(String DBname, String tableName)
			throws SAXException, IOException, ParserConfigurationException;

	public int getcolnumbers(String DBname, String tableName)
			throws SAXException, IOException, ParserConfigurationException;

	public String getcell(int rowindex, int colindex, String tableName,
			String DbName) throws ParserConfigurationException, SAXException,
			IOException;

	public int getMaxTableindex(String DbName, String tableName)
			throws ParserConfigurationException, SAXException, IOException;

	public String getTableName(int index, String DbName)
			throws ParserConfigurationException, SAXException, IOException;

	public int getCount();

	public int getLastRow();

	public Boolean is_selected();

	public String getTableName();

	public String getDBName();

}
