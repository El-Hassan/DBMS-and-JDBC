package jdbc;

import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import log.MyLogger;

import org.xml.sax.SAXException;

import dbmsPachage.DBMS;
import error.Error;

public class MyResultSetMetaData implements ResultSetMetaData {
	private boolean isClosed; // checks if the ResultSet is closed
	private DBMS myDbms; // data base management
	private String DBName, tableName; // current table , current database

	public MyResultSetMetaData(String DBName, String TableName,
			boolean isClosed, DBMS myDbms) {
		this.DBName = DBName;
		this.tableName = TableName;
		this.myDbms = myDbms;
		this.isClosed = isClosed;
	}

	public void setclosed(boolean isClosed) {
		this.isClosed = isClosed;
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

	@Override
	public String getCatalogName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnClassName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	//
	public int getColumnCount() throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		} else {
			try {
				return myDbms.getcolnumbers(DBName, tableName);
			} catch (SAXException | IOException | ParserConfigurationException e) {
				MyLogger.Log().error(e.getMessage());
				throw new SQLException(e.getMessage());
			}
		}
	}

	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	// this
	public String getColumnLabel(int column) throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		} else {
			try {
				return myDbms.getColumnLabel(column, tableName, DBName);
			} catch (SAXException | IOException | ParserConfigurationException e) {
				MyLogger.Log().error(e.getMessage());
				throw new SQLException(e.getMessage());
			}
		}
	}

	@Override
	// this
	public String getColumnName(int index) throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		} else {
			try {
				return myDbms.getColumnLabel(index, tableName, DBName);
			} catch (SAXException | IOException | ParserConfigurationException e) {
				MyLogger.Log().error(e.getMessage());
				throw new SQLException(e.getMessage());
			}
		}
	}

	// this
	private int getType(String DataType) throws SQLException, ParseException {
		DataType = DataType.toLowerCase();
		switch (DataType) {
		case "int":
			return Types.INTEGER;
		case "varchar":
			// o.replace("\"", "");
			return Types.VARCHAR;
		case "float":
			return Types.FLOAT;
		case "long":
			return Types.BIGINT;
		case "boolean":
			return Types.BOOLEAN;
		case "array":
			return Types.ARRAY;
		case "double":
			return Types.DOUBLE;
		case "date":
			return Types.DATE;
		default:
			MyLogger.Log().error(Error.UNKNOW_TYPE);
			throw new SQLException(Error.UNKNOW_TYPE);
		}
	}

	@Override
	public int getColumnType(int index) throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		} else {
			try {
				return getType(myDbms.getDataType(index, tableName, DBName));
			} catch (SAXException | IOException | ParserConfigurationException e) {
				MyLogger.Log().error(e.getMessage());
				throw new SQLException(e.getMessage());
			} catch (ParseException e) {
				MyLogger.Log().error(e.getMessage());
				throw new SQLException(e.getMessage());
			}
		}
	}

	@Override
	public String getColumnTypeName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPrecision(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScale(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSchemaName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	// this
	public String getTableName(int index) throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.RESULT_SET_CLOSED);
			throw new SQLException(Error.RESULT_SET_CLOSED);
		} else {
			try {
				return myDbms.getTableName(index, DBName);
			} catch (SAXException | IOException | ParserConfigurationException e) {
				MyLogger.Log().error(e.getMessage());
				throw new SQLException(e.getMessage());
			}
		}
	}

	@Override
	// this
	public boolean isAutoIncrement(int column) throws SQLException {
		return false;
	}

	@Override
	public boolean isCaseSensitive(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCurrency(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDefinitelyWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	// this
	public int isNullable(int column) throws SQLException {
		// columnNoNulls
		return 1;
	}

	@Override
	// this
	public boolean isReadOnly(int column) throws SQLException {
		return false;
	}

	@Override
	// this
	public boolean isSearchable(int column) throws SQLException {
		return true;
	}

	@Override
	public boolean isSigned(int column) throws SQLException {
		return true;
	}

	@Override
	// this
	public boolean isWritable(int column) throws SQLException {
		return true;
	}

}
