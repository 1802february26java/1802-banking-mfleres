package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.revature.exception.InvalidLoginException;
import com.revature.exception.NoUserException;
import com.revature.exception.UserAlreadyExistsException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.Banking;
import com.revature.model.Transaction;

public class BankJdbc implements Banking{
	
	private static Logger logger = Logger.getLogger(BankJdbc.class);
	/*static {
		logger.setLevel(Level.ALL);
	}*/
	private int currentUser = 0;
	
	/**
	 * Make a singleton
	 */
	private static BankJdbc database = new BankJdbc();
	
	private BankJdbc() {}
	
	public static BankJdbc getInstance() {
		return database;
	}
	
	@Override
	public double getBalance() {
		if(currentUser == 0) {
			throw new NoUserException();
		}
		logger.trace("Getting balance for user ID " + currentUser);
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT U_BALANCE FROM BANK_USER WHERE U_ID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, Integer.toString(currentUser));
			
			ResultSet result = statement.executeQuery();
			
			if(result.next()) {
				return result.getDouble("U_BALANCE");
			}else {
				throw new UserNotFoundException();
			}
		} catch (SQLException e) {
			logger.error("Error while getting balance.", e);
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void login(int userID, String password) {
		//logger.info("Getting balance for user ID " + currentUser);
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT U_HASH_PASSWORD FROM BANK_USER WHERE U_ID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, Integer.toString(userID));
			
			ResultSet result = statement.executeQuery();
			
			if(result.next()) {
				int dbPassword = result.getInt("U_HASH_PASSWORD");
				if(dbPassword == password.hashCode()) {
					//Good Login info
					currentUser = userID;
				} else {
					throw new InvalidLoginException();
				}
			}else {
				throw new InvalidLoginException();
			}
		} catch (SQLException e) {
			logger.error("Error while logging in.", e);
			e.printStackTrace();
		}		
	}

	@Override
	public void logout() {
		currentUser = 0;		
	}

	@Override
	public double withdraw(double amount) {
		//Lazy implementation...
		return deposit(-amount);
	}

	@Override
	public double deposit(double amount) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			double currentBalance = getBalance();
			double newBalance = currentBalance + amount;
			String sql = "UPDATE BANK_USER SET U_BALANCE=? WHERE U_ID=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setDouble(1, newBalance);
			statement.setInt(2, currentUser);
			if(statement.executeUpdate() > 0) {
				updateTransactionTable(amount);
				return newBalance;
			} else {
				return currentBalance;
			}
			
		} catch (SQLException|NoUserException e) {
			logger.error("Error in deposit(double).", e);
			throw new NoUserException();
		}	
	}
	
	private void updateTransactionTable(double change) throws SQLException {
			Connection connection = ConnectionUtil.getConnection();
			String sql = "INSERT INTO BANK_TRANSACTION VALUES(?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			Timestamp time = Timestamp.from(Instant.now());
			statement.setTimestamp(1, time);
			statement.setInt(2, currentUser);
			statement.setDouble(3, change);
			statement.setDouble(4, getBalance());
			statement.executeUpdate();
	}

	@Override
	public int register(String password) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			int id = getNextAvailableId();
			String sql = "INSERT INTO BANK_USER VALUES(?,?,0.0)";
			PreparedStatement statement = connection.prepareStatement(sql);
			logger.trace("Setting id to " + id);
			statement.setInt(1, id);
			statement.setInt(2, password.hashCode());
			logger.trace(password.hashCode());
			if(statement.executeUpdate() > 0) {
				currentUser = id;
				logger.trace("Registered user id "+id);
				return id;
			}
			else {
				throw new NoUserException();
			}
		} catch (SQLException|NoUserException e) {
			logger.error("Error in register(String).", e);
			throw new NoUserException();
		}
	}
	
	public int getNextAvailableId() {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT COUNT(U_ID) FROM BANK_USER WHERE U_ID = ?";
			for(int i = 1;;i++) {
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, i);
				ResultSet result = statement.executeQuery();
				if(result.next() && result.getInt(1) == 0) {
					logger.trace("Next available ID = " + i);
					return i;
				}
			}
		} catch (SQLException e) {
			logger.error("Error in getNextAvailableId().", e);
			throw new NoUserException();
		}
	}

	@Override
	public int getCurrentUser() {
		if(currentUser >= 0) {
			return currentUser;
		} else {
			throw new NoUserException(); 
		}
	}
	
	public void viewBank() {
		logger.trace("Printing out bank data...");
		//System.out.println("Printing bank data (sysout)");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT * FROM BANK_USER";
			Statement statement = connection.createStatement();	
			logger.trace("Executing query");
			ResultSet result = statement.executeQuery(sql);
			logger.trace("Begin iteration");
			while(result.next()) {
				//logger.trace("Displaying element");
				System.out.println(result.getLong("U_ID") + ", " + result.getInt("U_HASH_PASSWORD") + ", " + result.getDouble("U_BALANCE"));
			}
		} catch (SQLException e) {
			logger.error("Error in viewBank().", e);
			//e.printStackTrace();
		}		
	}

	@Override
	public void register(int id, String password) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO BANK_USER VALUES(?,?,0.0)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.setInt(2, password.hashCode());
			try {
				if(statement.executeUpdate() > 0) {
					currentUser = id;
				}
			} catch (SQLException e) {
				throw new UserAlreadyExistsException();
			}
		} catch (SQLException e) {
			logger.error("Error in register(int,String).", e);
		} 
	}

	@Override
	public void deregister(String password) {
		if(currentUser == 0) {
			throw new NoUserException();
		} else {
			try (Connection connection = ConnectionUtil.getConnection()) {
				int parameterIndex = 0;
				String sql = "DELETE FROM BANK_USER WHERE U_ID = ? AND U_HASH_PASSWORD = ?";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(++parameterIndex, currentUser);
				statement.setInt(++parameterIndex, password.hashCode());
				logger.trace("sql = \"DELETE FROM BANK_USER WHERE U_ID = "+currentUser+" AND U_HASH_PASSWORD = "+password.hashCode()+"\"");
				clearTransactionHistory();
				if(statement.executeUpdate() > 0) {
					logger.trace("DEregistered user id "+currentUser);
					currentUser = 0;
				}
				else {
					throw new NoUserException();
				}
			} catch (SQLException e) {
				logger.error("Error in deregister().", e);
				//e.printStackTrace();
			}
		}
	}
	
	private void clearTransactionHistory() {
		if(currentUser != 0) {
			try (Connection connection = ConnectionUtil.getConnection()) {
				String sql = "DELETE FROM BANK_TRANSACTION WHERE U_ID = ?";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, currentUser);
				statement.executeUpdate();
			} catch (SQLException e) {
				logger.error("Error in deregister().", e);
				//e.printStackTrace();
			}
		}
	}

	@Override
	public List<Transaction> getTransactionHistory() {
		if(currentUser == 0) {
			throw new NoUserException();
		} else {
			try (Connection connection = ConnectionUtil.getConnection()) {
				List<Transaction> transactionList = new ArrayList<Transaction>();
				int parameterIndex = 0;
				String sql = "SELECT * FROM BANK_TRANSACTION WHERE U_ID = ? ORDER BY T_TIME";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(++parameterIndex, currentUser);
				ResultSet results = statement.executeQuery();
				while(results.next()) {
					transactionList.add(new Transaction(results.getInt("U_ID"),results.getTimestamp("T_TIME"),results.getInt("T_CHANGE"),results.getInt("T_BALANCE")));
				}
				return transactionList;
				
			} catch (SQLException e) {
				logger.error("Error in getTransactionHistory().", e);
				//e.printStackTrace();
			}
		}
		return null;
	}
}
