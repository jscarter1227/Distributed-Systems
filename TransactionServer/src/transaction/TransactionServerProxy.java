package transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messages.Message;
import messages.msgTypes;

public class TransactionServerProxy implements msgTypes{
	
	String host = null;
	int port;
	
	Socket connection = null;
	ObjectOutputStream writeToNet = null;
	ObjectInputStream readFromNet = null;
	int transactionID = 0;
	int balance = 0;
	
	TransactionServerProxy(String incHost, int incPort) {
		host = incHost;
		port = incPort;

		// try to make a connection
		try{
			connection = new Socket(host,port);
			writeToNet = new ObjectOutputStream(connection.getOutputStream());
			readFromNet = new ObjectInputStream(connection.getInputStream());
		}
		catch(IOException e){
			e.printStackTrace();
		}

	}
	
	public int openTransaction() {
		 Message openTransaction = new Message(OPEN_TRANSACTION);

		//Implement Read/WriteFromNet here to create new object
		try{
			writeToNet.writeObject(openTransaction);
			transactionID = (int)readFromNet.readObject();
		}
		catch(IOException e){
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// For transaction
		return transactionID;
	}
	
	public void closeTransaction() {
		Message closeTransaction = new Message(CLOSE_TRANSACTION);

		// Close all streams at the end of transaction
		try {
			writeToNet.writeObject(closeTransaction);
			//writeToNet.close();
			//readFromNet.close();
			//connection.close();
		} catch (IOException e) {
			System.err.println("Error closing tranasaction");
		}

	}

	public int read(int accountNumber) {
		Message readMsg = new Message(READ_REQUEST, accountNumber);
		try{
			// Reads transaction
			writeToNet.writeObject(readMsg);

			// updates balance
			balance = (Integer) readFromNet.readObject();
		}
		catch(Exception e){
			System.out.println("Server proxy read function error");
			e.printStackTrace();
		}

		// Returns balance
		return balance;
	}
	
	public int write(int accountNumber, int amount) {
		Message writeMsg = new Message(WRITE_REQUEST, accountNumber, amount);

		try{
			// creates new transaction message
			writeToNet.writeObject(writeMsg);

			// Sends transaction to server??
			balance = (Integer) readFromNet.readObject();
		}
		catch(Exception e){
			System.out.println("Server proxy read function error");
			e.printStackTrace();
		}
		// Returns balance?
		return balance;
	}
}
