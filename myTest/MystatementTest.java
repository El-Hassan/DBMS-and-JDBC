package myTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import jdbc.MyStatement;
import junit.framework.TestCase;
import dbmsPachage.DBMS;
import dbmsPachage.MyDBMS;
import error.Error;

public class MystatementTest extends TestCase {
	DBMS db = new MyDBMS();
	MyStatement st = new MyStatement("LAB3", db);

	public void test1() throws SQLException {
		st.execute("create dataBase LAB3");
		db.chooseDB("LAB3");
		st.execute("CREATE TABLE win(name varchar,id int,kind boolean,groups float[])");
		st.execute("insert into win values('abdo', 41 ,'true','{2.3,2.4}');");
		st.execute("insert into win (name , id , kind , groups) values('romod', 20 ,'false','{2.3983,2.9844}');");
		st.execute("insert into win (name , id , kind , groups) values('mostafa', 33 ,'false','{83784.3983,9344.9844}');");
		st.execute("UPDATE win SET name='sa3ed' WHERE id=41 ;");
		assertEquals("2.3,2.4",
				db.execute("select groups from win where kind = 'true' ;"));
		assertEquals("romod\nmostafa",
				db.execute("select name from win where kind = 'false' ;"));
		assertEquals(
				false,
				st.execute("insert into win values('abdo', 41 ,'true','{2.3,'a7med'}');"));
		assertEquals(
				Error.PARSING_ERROR,
				db.execute("insert into win values('abdo', 41 ,'true','{2.3,'a7med'}');"));

		st.execute("CREATE TABLE csed(name varchar,id int)");
		st.addBatch("insert into csed values('abdo',41)");
		st.addBatch("insert into csed values('sa3eed',60)");
		st.addBatch("insert into 5alefa values ('ana',393);");
		st.executeBatch();
		assertEquals(st.Batch_faild, Error.EXECUTE_BATCH_ERROR);
		st.clearBatch();
		assertEquals(0, st.SQL.size());

		st.execute("create database this");
		st.execute("create table sa3ed(name varchar,id int)");
		st.execute("INSERT INTO sa3ed (name,id) VALUES ('who', '23');");
		st.addBatch("INSERT INTO sa3ed(name,id)VALUES ('why', 24);");
		st.addBatch("insert into sa3ed values('abdo',41);");
		try {
			st.addBatch("delete * from sa3ed");
		} catch (SQLException e) {
			assertEquals(Error.ADD_BATCH_ERROR, e.getMessage());
		}
		st.addBatch("UPDATE sa3ed SET name='Salem' WHERE id=41;");
		st.addBatch("insert into sa3ed values('sa3eed',60)");
		int arr[] = st.executeBatch();
		assertEquals(Statement.SUCCESS_NO_INFO, arr[0]);
		assertEquals(Statement.SUCCESS_NO_INFO, arr[1]);
		assertEquals(1, arr[2]);
		assertEquals(Statement.SUCCESS_NO_INFO, arr[3]);

		assertEquals(1,
				st.executeUpdate("update sa3ed set id='23' where id=41"));
		assertEquals(
				2,
				st.executeUpdate("update sa3ed set name = 'islam' where id =23"));

		st.executeQuery("select * from sa3ed");

		Connection con = st.getConnection();
		assertNotNull(con);
		assertEquals(1000000000, st.getQueryTimeout());
		st.setQueryTimeout(5000);
		assertEquals(5000, st.getQueryTimeout());
		st.execute("dummy");
		st.executeQuery("select * from sa3ed");

		st.setQueryTimeout(1000);
		assertEquals(1000, st.getQueryTimeout());
		st.setQueryTimeout(0);
		con = st.getConnection();
		assertNotNull(con);
		assertEquals(0, st.getQueryTimeout());
		st.setQueryTimeout(5000);

		try {
			st.execute("select * from csed");
		} catch (SQLException e) {
			assertEquals(Error.TIMEOUT, e.getMessage());
		}

		try {
			st.addBatch("select * from csed");
		} catch (SQLException e) {
			assertEquals(Error.ADD_BATCH_ERROR, e.getMessage());
		}
		st.clearBatch();
		st.addBatch("insert into csed values('fddf' ,'rer','erer',1);");
		try {
			st.executeBatch();
		} catch (SQLException e) {
			assertEquals(Error.EXECUTE_BATCH_ERROR, e.getMessage());
		}

		try {
			st.executeQuery("create database db");
		} catch (SQLException e) {
			assertEquals(Error.STATEMENT_NOT_SELECT, e.getMessage());
		}
		try {
			st.executeUpdate("select * from csed where name = 'abdo' ;");
		} catch (SQLException e) {
			assertEquals(Error.EXECUTE_UPDATE_ERROR, e.getMessage());
		}

		st.close();
		try {
			st.execute("select * from csed");
		} catch (SQLException e) {
			assertEquals(Error.STATEMENT_IS_CLOSED, e.getMessage());
		}
		assertEquals(true, st.isClosed());
		try {
			st.execute("select * from ana");
		} catch (SQLException e) {
			assertEquals(Error.STATEMENT_IS_CLOSED, e.getMessage());
		}

	}
}