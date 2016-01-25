package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import jdbc.MyStatement;
import jdbc.Mydriver;

public class CMD {
	//private Console cs = System.console();
	private BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
	private String user, password, url;
	private Connection con;
	private Statement st;
	private Properties info;
	private Mydriver dr = new Mydriver();
	private DriverPropertyInfo[] t;
	private boolean acc;

	public static void main(String[] args) {
		CMD Instance = new CMD();
		Instance.askForUser();
		Instance.execute();
		Instance.close();
	}

	public void askForUser() {
		boolean flag = false;
		while (true) {
			try {
				System.out.print("enter username :");
				user = bf.readLine();
				System.out.print("enter Password :");
				password = bf.readLine();
				info = new Properties();
				info.put("username", user);
				info.put("password", password);
				if (!acc&&flag){
					System.out.println("available DataBases :");					
					t = new DriverPropertyInfo[dr.dbnames.length] ;
					t = dr.getPropertyInfo(null, info);
					for (int i = 0; i < t.length; i++) {
						System.out.println("JDBC:CSED:"+t[i].value);
					}	
				}
				System.out.print("enter url :");
				url = bf.readLine();
				acc = dr.acceptsURL(url);
				
			} catch (SQLException | IOException e) {
				System.out.println(e.getMessage());
			}
			try {
				DriverManager.registerDriver(new Mydriver());
				try {
					Class.forName("jdbc.Mydriver");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				con = DriverManager.getConnection(url, info);
				if (con == null) {
					System.out.println("wrong user/pass/url !");
					flag = true;
					continue;
				} else {
					break;
				}
			} catch (SQLException e) {
				if (acc) {
					System.out.println("wrong user / password !");
				} else {
					System.out.println("wrong user / password / url !");
				}
				flag = true;
			}
		}

		try {
			st = con.createStatement();
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}
		System.out.println("Connected Succesfully !");
	}

	public void execute() {
		System.out.println("enter your orders , EXIT to exit");
		while (true) {
			try {
				String statement = bf.readLine();
				if (statement.toLowerCase().equals("exit")) {
					return;
				}
				if (st.execute(statement)) {
					System.out.println("done successfully !");
				} else {
					System.out.println("error occured");
				}
				System.out.println(((MyStatement) st).getMessage());
			} catch (SQLException | IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public void close() {
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("error happened while closing :"
					+ e.getMessage());
		}
	}
}
