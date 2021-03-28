package transaction;

import java.io.*;
import java.util.*;


public class TransactionClient extends Thread{
	int numTransactions;
	int numAccounts;
	int initialBalance;
	String host;
	int port;
	StringBuffer log = new StringBuffer("");

    // constructor
	public TransactionClient(String properties) throws FileNotFoundException{
		
		// open the properties file
		try(InputStream propsFile = new FileInputStream(properties)){
			
			// create a properties object then load the file
			Properties props = new Properties();
			props.load(propsFile);

            // get the properties for each of the varables declared above
			numTransactions = Integer.parseInt(props.getProperty("NUMBER_TRANSACTIONS"));
			numAccounts = Integer.parseInt(props.getProperty("NUMBER_ACCOUNTS"));
			initialBalance = Integer.parseInt(props.getProperty("INITIAL_BALANCE"));
			host = props.getProperty("HOST");
			port = Integer.parseInt(props.getProperty("PORT"));
		}

		catch(IOException e){
			e.printStackTrace();
		}
	}

	public void run() {
		for(int i = 0; i < numTransactions; i++) {
			new Thread() {
				public void run() {
					TransactionServerProxy trans = new TransactionServerProxy(host, port);
					int transID = trans.openTransaction();
					System.out.println("Transaction #: " + transID + " started");
	
					// account info
					int accountFrom = (int) Math.floor(Math.random() * numAccounts);
					int accountTo = (int) Math.floor(Math.random() * numAccounts);
					int amount = (int) Math.ceil(Math.random() * initialBalance);
					int balance;
					System.out.println("\tTransaction #: " + transID + ", $" + amount + " " + accountFrom + "->" + accountTo);
	                
					// withdraw action
					balance = trans.read(accountFrom);
					trans.write(accountFrom, balance - amount);
	
					// deposit action
					balance = trans.read(accountTo);
					trans.write(accountTo, balance + amount);
	
					trans.closeTransaction();
	
					System.out.println("Transaction #: " + transID + " finished");
				}
			}.start();
		}
	}
	// main method to run the properties config file
	public static void main(String[] args) throws FileNotFoundException{
		(new TransactionClient("C:\\Users\\joebr\\eclipse-workspace\\TransactionServer\\src\\props\\TransactionServer.properties")).start();
	}
}


