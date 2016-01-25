package jdbc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

import log.MyLogger;
import dbmsPachage.DBMS;
import dbmsPachage.MyDBMS;
import operations.Operation;

public class Mydriver implements Driver {
	private Connection con;
	private Operation op;
	private ArrayList<String> URL;
	private ArrayList<String> filelist;
	private ArrayList<String> user;
	private ArrayList<String> pass;
	public String dbnames[] ;
	DBMS myDbms;

	static {
		try {
			DriverManager.registerDriver(new Mydriver());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Mydriver() {
		myDbms = new MyDBMS();
		op = new Operation();
		URL = new ArrayList<String>();
		try {
			URL = op.getAllDB();	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dbnames= new String[URL.size()];
		for (int i = 0; i <URL.size(); i++) {
			dbnames[i]=URL.get(i).substring(11);
		}
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("prop.txt"));
			filelist = new ArrayList<String>();
			String str;
			while ((str = in.readLine()) != null) {
				filelist.add(str);
			}
			in.close();
		} catch (IOException ex) {
		}
		user = new ArrayList<String>();
		pass = new ArrayList<String>();
		for (int i = 0; i < filelist.size(); i++) {
			String[] splites = filelist.get(i).split(",");
			user.add(splites[0].substring(9));
			pass.add(splites[1].substring(9));
		}
	}

	@Override
	public boolean acceptsURL(String arg0) throws SQLException {
		
		// TODO Auto-generated method stub
		if (op.checkDB(cut(arg0))) {
			MyLogger.Log().info("Database name is valid");
			return true;
		} else {
			MyLogger.Log().error("Database name is not found");
			return false;
		}
	}

	// JDBC:CSED:DBName
	private String cut(String arg0) {
		int flag = 0;
		for (int i = 0; i < arg0.length(); i++) {
			if (arg0.charAt(i) == ':')
				flag++;
		}
		if (flag == 2) {
			String[] c = arg0.split(":");
			if (c[0].equals("JDBC") && c[1].equals("CSED")) {
				return c[2];
			} else
				return null;
		} else {

			return null;
		}
	}

	@Override
	public Connection connect(String arg0, Properties arg1) throws SQLException {
		for (int i = 0; i < user.size(); i++) {
			if (acceptsURL(arg0)
					&& (arg1.getProperty("username").equals(user.get(i)))
					&& (arg1.getProperty("password").equals(pass.get(i)))
					&& (cut(arg0) != null)) {
				myDbms.chooseDB(cut(arg0));
				con = new MyConnection(cut(arg0), myDbms);
				MyLogger.Log().info("connection was made");
				break;
			}
		}
		if (con == null) {
			MyLogger.Log().error("connection was not made");
		}
		return con;
	}

	@Override
	public int getMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String arg0, Properties arg1)
			throws SQLException {
		DriverPropertyInfo[] info = new DriverPropertyInfo[URL.size()];
		for (int i = 0; i < URL.size(); i++) {
			DriverPropertyInfo x = new DriverPropertyInfo("URL", dbnames[i]);
			info[i]=x;
		}
		
		return info;
	}


	@Override
	public boolean jdbcCompliant() {
		// TODO Auto-generated method stub
		return false;
	}

}
