package com.shopfactory.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class JdbcUtil {
	
	private JdbcUtil(){};
	
	public static void createDB(String driver, String url, String user, String password) {
		String[] queries = readQueries();
		try(Connection connection = getConnection(driver, url, user, password);
			Statement statement	= connection.createStatement();) {
			for (String query : queries) {
				statement.executeUpdate(query);			
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static String[] readQueries() {
		try(BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(JdbcUtil.class.getResourceAsStream("/createdb.sql")));) {
			String script = bufferedReader.lines().collect(Collectors.joining("\n"));
			return script.substring(0, script.lastIndexOf("\n")).split(";");			
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return new String[0];
		
	}

	public static Connection getConnection(String driver, String url, String user, String password) {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			throw new IllegalStateException(e);
		}		
		return conn;
	}
}
