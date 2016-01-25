package dbmsPachage;

import junit.framework.TestCase;

public class MyTest extends TestCase {

	DBMS d = new MyDBMS(); // need to be initialized by the implemented class

	public void test_1() {
		assertEquals(
				DBMS.PARSING_ERROR,
				d.execute("CREATE TABLE Persons PersonID int,LastName varchar(255),FirstName varchar(255),Address varchar(255),City varchar(255);"));
		assertEquals(DBMS.PARSING_ERROR, d.execute("CRETE DATABASE LAB5;"));

		assertEquals(
				DBMS.PARSING_ERROR,
				d.execute("INSERT Persons (PersonID,LastName,FirstName,MiddleName)VALUES (1,'Mohamed','Tamer','Ali');"));

		assertEquals(DBMS.DB_NOT_FOUND, d.execute("DELETE FROM Persons;"));

		assertEquals(DBMS.PARSING_ERROR, d.execute("SELECT *  Customers;"));

		assertEquals(
				DBMS.PARSING_ERROR,
				d.execute("UPDATE Customers  ContactName='Alfred Schmidt', City='Hamburg' WHERE CustomerName='Alfreds Futterkiste'; "));
	}

	public void test_2() {
		assertEquals(
				DBMS.DB_NOT_FOUND,
				d.execute("CREATE TABLE Persons(PersonID int,LastName varchar(255),FirstName varchar(255),Address varchar(255),City varchar(255));"));
		assertEquals(DBMS.Con_DB, d.execute("CREATE DATABASE LAB5;"));
		assertEquals(
				DBMS.TABLE_NOT_FOUND,
				d.execute("INSERT INTO O (PersonID,LastName,FirstName)VALUES (1, 23,'Tamer');"));

		assertEquals(
				DBMS.Con_Table,
				d.execute("CREATE TABLE Persons(PersonID int,LastName varchar(255),FirstName varchar(255),Address varchar(255),City varchar(255));"));
		assertEquals(
				DBMS.TABLE_ALREADY_EXISTS,
				d.execute("CREATE TABLE Persons(PersonID int,LastName varchar(255),FirstName varchar(255),Address varchar(255),City varchar(255));"));
		assertEquals(
				DBMS.COLUMN_NOT_FOUND,
				d.execute("INSERT INTO Persons (PersonID,LastName,FirstName,MiddleName)VALUES (1,'Mohamed','Tamer','Ali');"));

		assertEquals(
				DBMS.COLUMN_TYPE_MISMATCH,
				d.execute("INSERT INTO Persons(PersonID,LastName,FirstName)VALUES (1, 23,'Tamer');"));

		assertEquals(
				DBMS.TABLE_NOT_FOUND,
				d.execute("INSERT INTO O(PersonID,LastName,FirstName)VALUES (1, 23,'Tamer');"));

	}

	public void test_3() {
		assertEquals(DBMS.Con_Delete, d.execute("Delete * from Persons;"));
		assertEquals(
				DBMS.Con_insert,
				d.execute("INSERT INTO Persons(PersonID,LastName,FirstName,Address,City) VALUES (1,'Mohamed','Ali','11 street','Tanta');"));
		assertEquals(
				DBMS.Con_insert,
				d.execute("INSERT INTO Persons(PersonID,LastName,FirstName,Address,city) VALUES (2,'Mohamed','Tamer','11 street','Alexandria');"));
		assertEquals(
				DBMS.Con_insert,
				d.execute("INSERT INTO Persons VALUES (3,'Ahmad','Mohsen','12 street','Cairo');"));
		assertEquals(
				DBMS.Con_insert,
				d.execute("INSERT INTO Persons VALUES (4,'Bassem','Yasser','43 street','Banha');"));

		assertEquals(
				"1  Mohamed  Ali  11 street  Tanta\n2  Mohamed  Tamer  11 street  Alexandria\n3  Ahmad  Mohsen  12 street  Cairo\n4  Bassem  Yasser  43 street  Banha",
				d.execute("select * from Persons"));

		assertEquals(
				"2  Mohamed  Tamer  11 street  Alexandria\n3  Ahmad  Mohsen  12 street  Cairo\n4  Bassem  Yasser  43 street  Banha",
				d.execute("select * from Persons where PersonID > 1"));

		assertEquals(
				"1  Mohamed  Ali  11 street  Tanta\n2  Mohamed  Tamer  11 street  Alexandria",
				d.execute("select * from Persons where LastName='Mohamed'"));

		assertEquals(
				"Mohsen",
				d.execute("select FirstName from Persons where address='12 street'"));

		assertEquals(
				DBMS.Con_Update,
				d.execute("UPDATE Persons SET Lastname='Salem', City='Hamburg' WHERE personid= 1;"));

		assertEquals("Ali",
				d.execute("select FirstName from Persons where City='Hamburg'"));

		assertEquals("1  Salem  Ali  11 street  Hamburg",
				d.execute("select * from Persons where City='Hamburg'"));

		assertEquals(DBMS.NOT_MATCH_CRITERIA,
				d.execute("DELETE FROM Persons WHERE lastname='salem';"));

		assertEquals(DBMS.Con_Delete,
				d.execute("DELETE FROM Persons WHERE lastname='Salem';"));

		assertEquals(DBMS.NOT_MATCH_CRITERIA,
				d.execute("select * from Persons where City='Hamburg'"));
	}

	public void test_4() {
		assertEquals(DBMS.Con_DB, d.execute("Create DataBase Match"));
		assertEquals(DBMS.TABLE_NOT_FOUND, d.execute("DELETE FROM Persons"));

	}
}
