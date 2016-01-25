package myTest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import jdbc.MyResultSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;

import dbmsPachage.MyDBMS;

public class MyResultSetTest {
	MyDBMS m = new MyDBMS();
	MyResultSet x = new MyResultSet("LAB5", "persons", m);
	MyResultSet y = new MyResultSet("LAB4", "win", m);

	@Test
	public void testResultSet() throws ParserConfigurationException,
			TransformerException, SAXException, IOException, SQLException {
		m.chooseDB("LAB5");
		assertEquals(x.absolute(1), true);
		assertEquals(x.absolute(-1), false);
		assertEquals(x.absolute(-10), false);

		assertEquals(x.previous(), false);
		assertEquals(x.next(), true);
		x.afterLast();
		assertEquals(x.isAfterLast(), true);
		x.beforeFirst();
		assertEquals(x.isBeforeFirst(), true);

		assertEquals(x.findColumn("personid"), 1);
		x.absolute(1);
		assertEquals(x.isFirst(), true);
		assertEquals(x.first(), true);
		assertEquals(x.isFirst(), true);
		assertEquals(x.last(), true);
		assertEquals(x.isFirst(), false);
		assertEquals(x.isLast(), true);
		assertEquals(x.previous(), true);
		assertEquals(x.next(), x.isLast());
	}

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void exceptionTest() throws Exception {
		expectedEx.expect(SQLException.class);
		expectedEx.expectMessage("This column doesn't exists in this table");
		assertEquals(x.findColumn("asda"), -1);

		x.close();
		expectedEx.expect(SQLException.class);
		expectedEx.expectMessage("Result Set is Closed!");
		x.absolute(1);
	}

	@Test
	public void test4() throws SQLException {
		m.chooseDB("LAB4");
		y.absolute(1);
		assertEquals(true, y.getBoolean(3));
		assertEquals("abdo", y.getString(1));
		assertEquals(41, y.getInt(2));
		y.absolute(3);
		assertEquals(false, y.getBoolean(3));
		assertEquals("mostafa", y.getString(1));
		assertEquals(33, y.getInt(2));
		y.previous();
		assertEquals(false, y.getBoolean(3));
		assertEquals("romod", y.getString(1));
		assertEquals(20, y.getInt(2));
		assertEquals(ResultSet.FETCH_UNKNOWN, y.getFetchDirection());
		float t[] = new float[2];
		t[0] = (float) 2.3983;
		t[1] = (float) 2.9844;
		Array x = y.getArray(4);
		float[] f = (float[]) x.getArray();
		boolean a = (t[0] == f[0]) && (t[1] == f[1]);
		assertEquals(a, true);
	}
}
