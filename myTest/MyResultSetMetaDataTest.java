package myTest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import jdbc.MyResultSetMetaData;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;

import dbmsPachage.MyDBMS;

public class MyResultSetMetaDataTest {
	MyDBMS m = new MyDBMS();
	MyResultSetMetaData y = new MyResultSetMetaData("LAB5", "persons", false, m);

	@Test
	public void testResultSet() throws ParserConfigurationException,
			TransformerException, SAXException, IOException, SQLException {
		assertEquals(y.getColumnCount(), 5);
		assertEquals(y.getColumnLabel(1), "personid");
		assertEquals(y.getColumnLabel(6),
				"This column doesn't exists in this table");
		assertEquals(y.getColumnName(2), "lastname");
		assertEquals(y.getColumnType(1), 4);
		assertEquals(y.getTableName(1), "persons");
		assertEquals(y.getTableName(2), "This Table doesn't exists in database");
	}

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void exceptionTest() throws Exception {

	}
}
