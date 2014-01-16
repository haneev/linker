package org.utwente.bigdata;

import java.sql.*;

public class DBConnection {
	
	private String url = "jdbc:mysql://192.168.0.104:3306/bigdata";
	private String username = "bigdata";
	private String password = "rikhan";
	private Connection connection;
	
	/**
	 * Set-up the database connection
	 */
	public DBConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");	
		} catch (Exception e) {
			System.out.println("Can't load JDBC Driver.");
			return;
		}
		
		try {
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(url,username,password);
		} catch (SQLException e) {
			System.out.println("Connection failed: "+e.getMessage());
			return;
		}
	}
	
	/**
	 * Insert data into the database
	 * @param sql - An SQL-statement
	 */
	public void insertData(String sql) {
		try {
			Statement statement = connection.createStatement();		 
			statement.executeUpdate(sql);		 
			statement.close();
		} catch (SQLException e) {
				System.out.println("SQL Exception occurred: "+e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * Close the database connection
	 */
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception occurred: "+e.getMessage());
		}
	}
	
}
