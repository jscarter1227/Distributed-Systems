package transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TransactionServerProxy {
	
	String host = null;
	int port;
	
	Socket connection = null;
	ObjectOutputStream writeToNet = null;
	ObjectInputStream readFromNet = null;
	int transactionID = 0;
	
	TransactionServerProxy(String incHost, int incPort) {
		host = incHost;
		port = incPort;
	}
	
	public int openTransaction() {
		// TODO: Implement Read/WriteFromNet here to create new object
		// For transaction
		return transactionID;
	}
	
	public void closeTransaction() {
		
		// Close all streams at the end of transaction
		try {
			writeToNet.close();
			readFromNet.close();
			connection.close();
		} catch (IOException e) {
			System.err.println("Error closing tranasaction");
		}

	}

	public int read() {
		// Reads transaction
		// Updates balance
		// Returns balance
		return 0;
	}
	
	public int write() {
		// Creates new transaction message
		// Sends transaction to server
		// Returns balance?
		return 0;
	}
}
