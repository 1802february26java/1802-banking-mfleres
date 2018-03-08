package com.revature.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
//import oracle.jdbc.driver.*;

/**
 * Utility class to obtain a connection object.
 *
 */
public class ConnectionUtil {
	
	private static Logger logger = Logger.getLogger(ConnectionUtil.class);
	static {
		logger.setLevel(Level.ALL);
	}

	public static Connection getConnection() throws SQLException {
		logger.info("Attempting to get connection");
		//String url = "jdbc:oracle:thin:@myrevaturerds.cwkouucgucbd.us-east-1.rds.amazonaws.com:1521:ORCL";
		//String username = "Bank_DB";
		//String password = "p4ssw0rd";
		
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String username = "CELEBRITY_DB";
		String password = "p4ssw0rd";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			logger.error("Could not find the oracle driver", e);
			
			e.printStackTrace();
		}
		return DriverManager.getConnection(url, username, password);
	}

	/** Test that connection is working **/
	public static void main(String[] args) {
		/*
		 * Try with resources will close resources automatically
		 * 
		 * Classes in this kind of try's need to implement AutoCloseable
		 */
		try(Connection connection = ConnectionUtil.getConnection()) {
			logger.info("Connection successful");
		} catch (SQLException e) {
			logger.error("Couldn't connect to the database", e);
		}
	}
}
