package com.revature.model;

import java.sql.Timestamp;

public class Transaction {
	private int id;
	private Timestamp time;
	private double change;
	private double balanceAfterChange;
	
	public Transaction(int id, Timestamp time, double change, double balanceAfterChange) {
		this.id = id;
		this.time = time;
		this.change = change;
		this.balanceAfterChange = balanceAfterChange;
	}
	
	@Override
	public String toString() {
		StringBuilder idString = new StringBuilder(new Integer(id).toString());
		
		return "|" + bufferLength(new Integer(id).toString(),6) + "|" + bufferLength(time.toString(),25) + "|" + bufferLength("$"+new Double(change).toString(),12) + "|" + bufferLength("$"+new Double(balanceAfterChange).toString(),15) + "|";
	}
	
	public static String bufferLength(String original, int desiredWidth) {
		if (desiredWidth <= original.length()) {
			return original;
		}
		StringBuilder builtString = new StringBuilder(original);
		if ((desiredWidth-original.length())%2 != 0) {
			//Odd number of chars
			builtString.insert(0, " ");
		}
		while(builtString.length() < desiredWidth) {
			builtString.insert(0, " ");
			builtString.append(" ");
		}
		return builtString.toString();
	}
}
