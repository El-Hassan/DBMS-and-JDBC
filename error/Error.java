package error;

import java.lang.reflect.Field;

public final class Error {
	public static final String TABLE_NOT_FOUND = "This Table doesn't exists in database";
	public static final String COLUMN_NOT_FOUND = "This column doesn't exists in this table";
	public static final String TABLE_ALREADY_EXISTS = "This Table already exists";
	public static final String PARSING_ERROR = "bad formated input";
	public static final String DB_NOT_FOUND = "No database exists";
	public static final String COLUMN_TYPE_MISMATCH = "Entered value doesn't match column type";
	public static final String NOT_MATCH_CRITERIA = "no row exists with this criteria";
	public static final String RESULT_SET_CLOSED = "Result Set is Closed!";
	public static final String TABLE_IS_EMPTY = "This Table Is Empty";
	public static final String UNKNOW_TYPE = "This Type Is Unknown";
	public static final String CONNECTION_IS_CLOSED = "connection is closed";
	public static final String STATEMENT_IS_CLOSED = "statement is closed";
	public static final String ADD_BATCH_ERROR = "statement not insert or update";
	public static final String STATEMENT_NOT_SELECT = "statement is't select";
	public static final String FATAL_ERROR = "fatal error";
	public static final String NO_DB_CHOSEN = "NO Current DB Chosen USE \"USE DatabaseName;\"";
	public static final String TIMEOUT = "Time Limit Exceeded";
	public static final String EXECUTE_BATCH_ERROR = "execute batch failed" ;
	public static final String EXECUTE_UPDATE_ERROR = "can not be select statement";

	public boolean ISVALID(String s) {
		Field f[] = getClass().getFields();
		try {
			for (int i = 0; i < f.length; i++) {
				if (s.equals((String) f[i].get(null))) {
					return true;
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}
}
