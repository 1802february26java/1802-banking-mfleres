package com.revature.model;

import java.util.HashMap;
import java.util.List;

import com.revature.exception.*;
import java.io.*;

public class Bank implements Banking, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String VAULT_LOCATION = "src/main/resources/vault.bnk";
	private HashMap<Integer,User> vault = new HashMap<>();
	private int currentUser = 0;
	String vaultLocation = VAULT_LOCATION;
	
	public Bank() {
		this(VAULT_LOCATION);
	}
	
	public Bank(String vaultFile) {
		ObjectInputStream inputStream = null;
		try {
			System.out.println("Bank Constructor");
			vaultLocation = vaultFile;
			inputStream = new ObjectInputStream(new FileInputStream(vaultFile));
			
			Bank bankFromFile = (Bank)inputStream.readObject();
			this.currentUser = bankFromFile.currentUser;
			this.vault = bankFromFile.vault;
			this.vaultLocation = bankFromFile.vaultLocation;
			/*
			HashMap<Integer, Double> inputVault = (HashMap<Integer,Double>)inputStream.readObject();
			vault = inputVault;
			try {
				int inputUser = inputStream.readInt();
				currentUser = inputUser;
				System.out.println(currentUser);
			} catch (EOFException e) {
				System.out.println("EOF");
				currentUser = 0;
			}*/
			
		} catch (FileNotFoundException e) {
			// Initialize Vault
			System.out.println("Initializing the vault.");
			initVault(vaultFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void initVault(String fileLocation) {
		ObjectOutputStream output = null;
		try {
			output = new ObjectOutputStream(new FileOutputStream(fileLocation));
			System.out.println("Init Bank File");
			output.writeObject(this);
			//System.out.println("init currUser");
			//output.writeInt(currentUser);
			//System.out.println("Done init");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(output != null) {
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public double getBalance(){
		if(currentUser != 0) {
			System.out.println(vault.get(currentUser));
			return vault.get(currentUser).getBalance();
		} else {
			System.out.println("Please login before checking balance.");
			return 0.0;
		}
	}

	@Override
	public void login(int userID, String password){
		if(vault.containsKey(userID) && vault.get(userID).checkPassword(password)) {
			//System.out.println("Logged in as user ID " + userID);
			currentUser = userID;
			updateVault(vaultLocation);
		} else {
			throw new InvalidLoginException();
		}
	}

	@Override
	public void logout() {
		if(currentUser != 0 ) {
			//System.out.println("Logged out");
			currentUser = 0;
			updateVault(vaultLocation);
		} else {
			throw new NoUserException();
			//System.out.println("No user is currently logged in");
		}
	}

	@Override
	public double withdraw(double amount){
		if(currentUser != 0) {
			User userObject = vault.get(currentUser);
			userObject.setBalance(userObject.getBalance()-amount);
			updateVault(vaultLocation);
		}
		else
		{
			throw new NoUserException();
		}
		return 0;
	}

	@Override
	public double deposit(double amount) {
		if(currentUser != 0) {
			User userObject = vault.get(currentUser);
			userObject.setBalance(userObject.getBalance()+amount);
			updateVault(vaultLocation);
		}
		else
		{
			throw new NoUserException();
			//System.out.println("Please log in before making a deposit.");
		}
		return 0;
	}

	@Override
	public String toString() {
		return "Bank [vault=" + vault + ", currentUser=" + currentUser + "]";
	}

	@Override
	public int register(String password) {
		for(int i = 1;;i++) {
			if(!vault.containsKey(i)) {
				vault.put(i, new User(i,password));
				//System.out.println("Registered user ID "+i);
				updateVault(vaultLocation);
				return i;
			}
		}
	}
	
	private void updateVault(String fileLocation) {
		//System.out.println("Updating vault file...");
		ObjectOutputStream output = null;
		try {
			output = new ObjectOutputStream(new FileOutputStream(fileLocation));
			output.writeObject(this);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(output != null) {
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//System.out.println("Done updating vault file");
		}
		
	}

	@Override
	public int getCurrentUser() {
		return currentUser;
	}

	@Override
	public void register(int id, String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deregister(String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Transaction> getTransactionHistory() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
