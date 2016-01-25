package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;

import log.MyLogger;
import dbmsPachage.DBMS;
import dbmsPachage.Parser;
import error.Error;

public class MyStatement implements Statement {

	Parser p;
	private MyConnection m;
	MyResultSet R;
	public ArrayList<String> SQL;
	private int[] values;
	public String DBname;
	private Boolean isClosed = true, done = false;
	private Error myError;
	private DBMS db;
	private int timeOut = 1000000000;
	public String Batch_faild = "";
	public String sa3eedMessege; // malaksh da3wa bel satr dah mte3mlsh feh 7aga :P

	public MyStatement(String s1, DBMS d) {
		DBname = s1;
		isClosed = false;
		myError = new Error();
		SQL = new ArrayList<String>();
		this.db = d;
		values = new int[10000];
		p = new Parser(db);
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
	public void addBatch(String arg0) throws SQLException {
		if (isClosed) {
			MyLogger.Log().error(Error.STATEMENT_IS_CLOSED);
			throw new SQLException(Error.STATEMENT_IS_CLOSED);
		}
		if (p.InsertIntoTableWith(arg0) || p.InsertIntoTableWithout(arg0)
				|| p.Update(arg0)) {
			SQL.add(arg0);
		} else {
			MyLogger.Log().error(Error.ADD_BATCH_ERROR);
			throw new SQLException(Error.ADD_BATCH_ERROR);
		}
	}

	@Override
	public void cancel() throws SQLException {
		// TODO Auto-generated method stub
	}

	@Override
	public void clearBatch() throws SQLException {
		// TODO Auto-generated method stub
		if (isClosed) {
			MyLogger.Log().error(Error.STATEMENT_IS_CLOSED);
			throw new SQLException(Error.STATEMENT_IS_CLOSED);
		}
		SQL.clear();
	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		if (isClosed())
			throw new SQLException(Error.STATEMENT_IS_CLOSED);
		isClosed = true;
		DBname = null;
		m = null;
		db = null;
		timeOut = 1000000000;
		R = null;
		Batch_faild = "";
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub

	}

	public boolean execute1(String arg0) {
		String s = db.execute(arg0);
		sa3eedMessege = s;
		if (myError.ISVALID(s) && !db.is_selected())
			return false;
		return true;
	}

	@Override
	public boolean execute(String arg0) throws SQLException {
		Boolean not = true;
		if (isClosed()) {
			not = false;
			MyLogger.Log().error(Error.STATEMENT_IS_CLOSED);
			throw new SQLException(Error.STATEMENT_IS_CLOSED);
		}
		done = false;
		if (not)
			return (boolean) Timer(arg0, "execute");
		else
			return false;
	}

	@Override
	public boolean execute(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String arg0, int[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String arg0, String[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public int[] executeBatch1() throws SQLException {
		for (int i = 0; i < SQL.size(); i++) {
			String s = db.execute(SQL.get(i));
			Batch_faild = "";
			if (myError.ISVALID(s)) {
				MyLogger.Log().error(Error.EXECUTE_BATCH_ERROR);
				throw new SQLException(Error.EXECUTE_BATCH_ERROR);
			} else {
				int x = db.getCount();
				if (x == 0)
					values[i] = Statement.SUCCESS_NO_INFO;
				else
					values[i] = x;
			}
		}
		int temp[] = new int[SQL.size()];
		for (int i = 0; i < SQL.size(); i++)
			temp[i] = values[i];
		return temp;
	}

	@Override
	public int[] executeBatch() throws SQLException {
		Boolean not = true;
		if (isClosed()) {
			not = false;
			MyLogger.Log().error(Error.STATEMENT_IS_CLOSED);
			throw new SQLException(Error.STATEMENT_IS_CLOSED);
		}
		done = false;
		if (not)
			return (int[]) Timer(null, "batch");
		else
			return null;
	}

	public ResultSet executeQuery1(String arg0) {
		db.execute(arg0);
		DBname = db.getDBName();
		R = new MyResultSet(DBname, db.getTableName(), db);
		try {
			R.absolute(db.getLastRow());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return R;
	}

	@Override
	public ResultSet executeQuery(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		Boolean not = true;
		if (isClosed()) {
			not = false;
			MyLogger.Log().error(Error.STATEMENT_IS_CLOSED);
			throw new SQLException(Error.STATEMENT_IS_CLOSED);
		}
		if (!p.SelectColumnFrom(arg0) && !p.SelectColumnFromWhere(arg0)
				&& !p.SelectFrom(arg0) && !p.SelectFromWhere(arg0)) {
			not = false;
			MyLogger.Log().error(Error.STATEMENT_NOT_SELECT);
			throw new SQLException(Error.STATEMENT_NOT_SELECT);
		}
		done = false;
		if (not)
			return (ResultSet) Timer(arg0, "query");
		else
			return null;
	}

	public int executeUpdate1(String arg0) {
		db.execute(arg0);
		int x = db.getCount();
		if (x == 0)
			return Statement.SUCCESS_NO_INFO;
		else
			return x;
	}

	@Override
	public int executeUpdate(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		Boolean not = true;
		if (isClosed()) {
			not = false;
			MyLogger.Log().error(Error.STATEMENT_IS_CLOSED);
			throw new SQLException(Error.STATEMENT_IS_CLOSED);
		}
		if (!p.check(arg0)) {
			not = false;
			MyLogger.Log().error(Error.EXECUTE_UPDATE_ERROR);
			throw new SQLException(Error.EXECUTE_UPDATE_ERROR);
		}
		done = false;
		if (not)
			return (int) Timer(arg0, "update");
		else
			return 0;

	}

	@Override
	public int executeUpdate(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String arg0, int[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String arg0, String[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	public String getMessage(){
		return sa3eedMessege;
	}
	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		Boolean not = true;
		if (isClosed()) {
			not = false;
			MyLogger.Log().error(Error.STATEMENT_IS_CLOSED);
			throw new SQLException(Error.STATEMENT_IS_CLOSED);
		}
		if (not) {
			m = new MyConnection(DBname, db);
			return m;
		} else
			return null;
	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getMoreResults(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		// TODO Auto-generated method stub
		if (isClosed()) {
			MyLogger.Log().error(Error.STATEMENT_IS_CLOSED);
			throw new SQLException(Error.STATEMENT_IS_CLOSED);
		}
		return timeOut;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		return R;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return isClosed;
	}

	@Override
	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCursorName(String arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEscapeProcessing(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub

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
	public void setMaxFieldSize(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxRows(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPoolable(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setQueryTimeout(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		if (isClosed()) {
			MyLogger.Log().error(Error.STATEMENT_IS_CLOSED);
			throw new SQLException(Error.STATEMENT_IS_CLOSED);
		}
		timeOut = arg0;

	}

	private class timed implements Runnable {
		String arg, temp;
		public Object returnO;

		public timed(String x, String type) {
			arg = x;
			temp = type;
		}

		@Override
		public void run() {
			if (done) {
				return;
			}
			switch (temp) {
			case "execute": {
				returnO = execute1(arg);
				break;
			}
			case "batch": {
				try {
					returnO = executeBatch1();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					Batch_faild = Error.EXECUTE_BATCH_ERROR;
				}

				break;
			}
			case "query": {
				returnO = executeQuery1(arg);
				break;
			}
			case "update": {
				returnO = executeUpdate1(arg);
				break;
			}
			}
			done = true;
		}
	}

	private Object Timer(String arg0, String type) throws SQLException {
		timed tt = new timed(arg0, type);
		Thread task = new Thread(tt);
		long maxTime = timeOut;
		task.start();
		try {
			task.join(maxTime);
		} catch (InterruptedException e) {
			/* if somebody interrupts us he knows what he is doing */
		}
		if (task.isAlive()) {
			task.interrupt();
			throw new SQLTimeoutException(Error.TIMEOUT);
		}
		return tt.returnO;
	}
}
