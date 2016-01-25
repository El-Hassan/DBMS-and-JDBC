package jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import log.MyLogger;

import org.xml.sax.SAXException;

import dbmsPachage.DBMS;
import error.Error;

public class MyResultSet implements ResultSet {
	private int rowPointer = 0; // points to the current row
	private boolean isClosed = false; // checks if the ResultSet is closed
	private DBMS myDbms; // data base management
	private String DBName, tableName; // current table , current database
	private MyStatement myStatement;
	private MyResultSetMetaData meta = new MyResultSetMetaData(DBName,
			tableName, isClosed, myDbms);

	public MyResultSet(String DBName, String tableName, DBMS myDbms) {
		this.DBName = DBName;
		this.tableName = tableName;
		this.myDbms = myDbms;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	// points to a row "arg0", returns true if it exists false otherwise
	@Override
	public boolean absolute(int index) throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		try {
			int temp = myDbms.maxRownumber(DBName, tableName);
			if (index > 0 && index <= temp) {
				rowPointer = index;
				return true;
			} else {
				return false;
			}
		} catch (SAXException | IOException | ParserConfigurationException e) {
			MyLogger.Log().error(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	}

	// points to an object after the last one
	@Override
	public void afterLast() throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		} else {
			try {
				rowPointer = myDbms.maxRownumber(DBName, tableName) + 1;
			} catch (SAXException | IOException | ParserConfigurationException e) {
				MyLogger.Log().error(e.getMessage());
				throw new SQLException(e.getMessage());
			}
		}
	}

	// points to an object before the first one
	@Override
	public void beforeFirst() throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		} else {
			rowPointer = 0;
		}
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws SQLException {
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		isClosed = true;
		myDbms = null;
		DBName = "";
		tableName = "";
		rowPointer = -1;
		myStatement = null;
	}

	@Override
	public void deleteRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	// returns the index of the selected column "arg0"
	@Override
	public int findColumn(String columnLabel) throws SQLException {
		int tempPntr;
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		} else {
			try {
				tempPntr = myDbms
						.getColumnIndex(columnLabel, tableName, DBName);
				if (tempPntr == -1) {
					MyLogger.Log().error(Error.COLUMN_NOT_FOUND);
					throw new SQLException(DBMS.COLUMN_NOT_FOUND);
				} else {
					return tempPntr;
				}
			} catch (ParserConfigurationException | SAXException | IOException e) {
				MyLogger.Log().error(e.getMessage());
				throw new SQLException(e.getMessage());
			}
		}
	}

	// points to the first row
	@Override
	public boolean first() throws SQLException {
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		} else {
			try {
				int temp = myDbms.maxRownumber(DBName, tableName);
				if (temp > 0) {
					rowPointer = 1;
					return true;
				} else {
					return false;
				}
			} catch (SAXException | IOException | ParserConfigurationException e) {
				throw new SQLException(Error.FATAL_ERROR);
			}

		}
	}

	@Override
	public Array getArray(int colindex) throws SQLException {
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		} else {
			try {
				if (colindex > myDbms.getcolnumbers(DBName, tableName)) {
					MyLogger.Log().error(Error.COLUMN_NOT_FOUND);
					throw new SQLException(Error.COLUMN_NOT_FOUND);
				} else if (!myDbms.getDataType(colindex, tableName, DBName)
						.toLowerCase().contains("array")) {
					MyLogger.Log().error(Error.NOT_MATCH_CRITERIA);
					throw new SQLException(Error.NOT_MATCH_CRITERIA);
				} else {
					Object o = getObject(colindex);
					if (!o.getClass().toString().equals("class jdbc.MyArray")) {
						MyLogger.Log().error(Error.NOT_MATCH_CRITERIA);
						throw new SQLException(Error.NOT_MATCH_CRITERIA);
					} else {
						return (Array) o;
					}
				}
			} catch (SAXException | IOException | ParserConfigurationException e) {
				MyLogger.Log().error(e.getMessage());
				throw new SQLException(e.getMessage());
			}
		}
	}

	@Override
	public Array getArray(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getAsciiStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getAsciiStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	// returns the value in column "arg0" and the current row as boolean
	@Override
	public boolean getBoolean(int colIndex) throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		Object o = getObject(colIndex);
		if (o.getClass() != Boolean.class) {
			MyLogger.Log().error(Error.NOT_MATCH_CRITERIA);
			throw new SQLException(Error.NOT_MATCH_CRITERIA);
		} else {
			return (Boolean) o;
		}
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		try {
			int colNumber = myDbms.getColumnIndex(columnLabel, tableName,
					DBName);
			return getBoolean(colNumber);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			MyLogger.Log().error(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public byte getByte(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte getByte(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getBytes(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getBytes(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCursorName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(int colIndex) {
		return null;
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public Date getDate(int arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(String arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDouble(int colIndex) throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		Object o = getObject(colIndex);
		if (o.getClass() != Double.class) {
			MyLogger.Log().error(Error.NOT_MATCH_CRITERIA);
			throw new SQLException(Error.NOT_MATCH_CRITERIA);
		} else {
			return (Double) o;
		}
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		try {
			int colNumber = myDbms.getColumnIndex(columnLabel, tableName,
					DBName);
			return getDouble(colNumber);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			MyLogger.Log().error(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	// return fetch direction...
	public int getFetchDirection() throws SQLException {
		return ResultSet.FETCH_UNKNOWN;
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloat(int colIndex) throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		Object o = getObject(colIndex);
		if (o.getClass() != Float.class) {
			MyLogger.Log().error(Error.NOT_MATCH_CRITERIA);
			throw new SQLException(Error.NOT_MATCH_CRITERIA);
		} else {
			return (Float) o;
		}
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		try {
			int colNumber = myDbms.getColumnIndex(columnLabel, tableName,
					DBName);
			return getFloat(colNumber);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			MyLogger.Log().error(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(int colIndex) throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		Object o = getObject(colIndex);
		if (o.getClass() != Integer.class) {
			MyLogger.Log().error(Error.NOT_MATCH_CRITERIA);
			throw new SQLException(Error.NOT_MATCH_CRITERIA);
		} else {
			return (Integer) o;
		}
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		try {
			int colNumber = myDbms.getColumnIndex(columnLabel, tableName,
					DBName);
			return getInt(colNumber);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			MyLogger.Log().error(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public long getLong(int colIndex) throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		Object o = getObject(colIndex);
		if (o.getClass() != Long.class) {
			MyLogger.Log().error(Error.NOT_MATCH_CRITERIA);
			throw new SQLException(Error.NOT_MATCH_CRITERIA);
		} else {
			return (Long) o;
		}
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		try {
			int colNumber = myDbms.getColumnIndex(columnLabel, tableName,
					DBName);
			return getLong(colNumber);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			MyLogger.Log().error(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		return meta;
	}

	@Override
	public Reader getNCharacterStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getNCharacterStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	// returns the array
	private Array returnArray(String s, String d) {
		Array x = new MyArray(s, d);
		return x;
	}

	// returns object with SQL specified type
	private Object returnObjectOfSQL(String DataType, String o)
			throws SQLException, ParseException {
		DataType = DataType.toLowerCase();
		String ramdan[] = DataType.split("_");
		if (DataType.contains("array")) {
			return returnArray(o, ramdan[1]);
		} else {

			switch (DataType) {
			case "int":
				return Integer.parseInt(o);
			case "varchar":
				// o.replace("\"", "");
				return o;
			case "float":
				return new Float(o);
			case "bigint":
				return new Long(o);
			case "boolean":
				return new Boolean(o);
			case "double":
				return new Double(o);
			default:
				MyLogger.Log().error(Error.UNKNOW_TYPE);
				throw new SQLException(Error.UNKNOW_TYPE);
			}
		}
	}

	// byt3mal feeha el saleema
	// search in all data types

	@Override
	public Object getObject(int colNumber) throws SQLException {
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		} else {
			try {
				if (colNumber > myDbms.getcolnumbers(DBName, tableName)) {
					MyLogger.Log().error(Error.COLUMN_NOT_FOUND);
					throw new SQLException(Error.COLUMN_NOT_FOUND);
				} else {
					String dataType = myDbms.getDataType(colNumber, tableName,
							DBName);
					String objectString = myDbms.getcell(rowPointer, colNumber,
							tableName, DBName);
					Object o = returnObjectOfSQL(dataType, objectString);
					return o;
				}
			} catch (SAXException | IOException | ParserConfigurationException
					| ParseException e) {
				MyLogger.Log().error(e.getMessage());
				throw new SQLException(e.getMessage());
			}
		}
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		try {
			int colNumber = myDbms.getColumnIndex(columnLabel, tableName,
					DBName);
			return getObject(colNumber);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			MyLogger.Log().error(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public Object getObject(int arg0, Map<String, Class<?>> arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String arg0, Map<String, Class<?>> arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(int arg0, Class<T> arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(String arg0, Class<T> arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRow() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RowId getRowId(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowId getRowId(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getShort(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short getShort(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	// bteta5ed fe constructor
	@Override
	public Statement getStatement() throws SQLException {
		return myStatement;
	}

	@Override
	public String getString(int colIndex) throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		Object o = getObject(colIndex);
		if (o.getClass() != String.class) {
			MyLogger.Log().error(Error.COLUMN_TYPE_MISMATCH);
			throw new SQLException(Error.COLUMN_TYPE_MISMATCH);
		} else {
			return (String) o;
		}
	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		try {
			int colNumber = myDbms.getColumnIndex(columnLabel, tableName,
					DBName);
			return getString(colNumber);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			MyLogger.Log().error(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public Time getTime(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(int arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String arg0, Calendar arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public URL getURL(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getURL(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getUnicodeStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getUnicodeStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAfterLast() throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		try {
			int a = myDbms.maxRownumber(DBName, tableName);
			return rowPointer == a + 1;
		} catch (SAXException | IOException | ParserConfigurationException e) {
			MyLogger.Log().error(Error.FATAL_ERROR);
			throw new SQLException(Error.FATAL_ERROR);
		}
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		return rowPointer == 0;
	}

	@Override
	public boolean isClosed() throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		return isClosed;
	}

	@Override
	public boolean isFirst() throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		return rowPointer == 1;
	}

	@Override
	public boolean isLast() throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		} else {
			try {
				int temp = myDbms.maxRownumber(DBName, tableName);
				return rowPointer == temp;
			} catch (SAXException | IOException | ParserConfigurationException e) {
				MyLogger.Log().error(e.getMessage());
				throw new SQLException(e.getMessage());
			}
		}
	}

	// points at the last row
	@Override
	public boolean last() throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		} else {
			try {
				if (rowPointer > 0) {
					rowPointer = myDbms.maxRownumber(DBName, tableName);
					return true;
				} else {
					return false;
				}
			} catch (SAXException | IOException | ParserConfigurationException e) {
				MyLogger.Log().error(e.getMessage());
				throw new SQLException(e.getMessage());
			}
		}
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveToInsertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean next() throws SQLException {
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		if (isLast()) {
			return false;
		} else {
			rowPointer++;
			return true;
		}
	}

	@Override
	public boolean previous() throws SQLException {
		if (isClosed()) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		}
		if (isFirst()) {
			return false;
		} else {
			rowPointer--;
			return true;
		}
	}

	@Override
	public void refreshRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean relative(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowInserted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFetchDirection(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFetchSize(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateArray(int arg0, Array arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateArray(String arg0, Array arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBigDecimal(String arg0, BigDecimal arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int arg0, Blob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String arg0, Blob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBoolean(int arg0, boolean arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBoolean(String arg0, boolean arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateByte(int arg0, byte arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateByte(String arg0, byte arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBytes(int arg0, byte[] arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBytes(String arg0, byte[] arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int arg0, Reader arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String arg0, Reader arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int arg0, Clob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String arg0, Clob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDate(int arg0, Date arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDate(String arg0, Date arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDouble(int arg0, double arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDouble(String arg0, double arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFloat(int arg0, float arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFloat(String arg0, float arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInt(int arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInt(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLong(int arg0, long arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLong(String arg0, long arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(int arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(String arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNString(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNull(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNull(String arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(int arg0, Object arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(String arg0, Object arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(int arg0, Object arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(String arg0, Object arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRef(int arg0, Ref arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRef(String arg0, Ref arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRowId(int arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRowId(String arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateShort(int arg0, short arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateShort(String arg0, short arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateString(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTime(int arg0, Time arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTime(String arg0, Time arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTimestamp(int arg0, Timestamp arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTimestamp(String arg0, Timestamp arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean wasNull() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
